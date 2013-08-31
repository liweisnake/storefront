/*
 * Copyright (c) 2009 Hewlett-Packard Company, All Rights Reserved.
 *
 * RESTRICTED RIGHTS LEGEND Use, duplication, or disclosure by the U.S.
 * Government is subject to restrictions as set forth in sub-paragraph
 * (c)(1)(ii) of the Rights in Technical Data and Computer Software
 * clause in DFARS 252.227-7013.
 *
 * Hewlett-Packard Company
 * 3000 Hanover Street
 * Palo Alto, CA 94304 U.S.A.
 * Rights for non-DOD U.S. Government Departments and Agencies are as
 * set forth in FAR 52.227-19(c)(1,2).
 */
package com.hp.sdf.ngp.search.engine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.util.Assert;

import com.hp.sdf.ngp.api.comon.EntityRelationShipType;
import com.hp.sdf.ngp.api.search.Condition;
import com.hp.sdf.ngp.api.search.OrderBy;
import com.hp.sdf.ngp.api.search.OrderEnum;
import com.hp.sdf.ngp.api.search.SearchExpression;
import com.hp.sdf.ngp.api.search.Condition.StringComparer;
import com.hp.sdf.ngp.common.exception.ConditionNoMatchedException;
import com.hp.sdf.ngp.common.exception.NgpRuntimeException;
import com.hp.sdf.ngp.common.util.Utils;
import com.hp.sdf.ngp.dao.BaseDao;
import com.hp.sdf.ngp.search.engine.model.BaseCondition;
import com.hp.sdf.ngp.search.engine.model.EavCondition;
import com.hp.sdf.ngp.search.engine.model.EavOrder;
import com.hp.sdf.ngp.search.engine.model.Function;
import com.hp.sdf.ngp.search.engine.model.JoinMetaData;
import com.hp.sdf.ngp.search.engine.model.JoinPropertyCondition;
import com.hp.sdf.ngp.search.engine.model.LogicCondition;
import com.hp.sdf.ngp.search.engine.model.ObjectPropertyCondition;
import com.hp.sdf.ngp.search.engine.model.Order;
import com.hp.sdf.ngp.search.engine.model.OrderExecutor;
import com.hp.sdf.ngp.search.engine.model.OrderSql;
import com.hp.sdf.ngp.search.engine.model.PropertyCondition;
import com.hp.sdf.ngp.search.engine.model.PropertyEqualCondition;
import com.hp.sdf.ngp.search.engine.model.SimpleFunction;
import com.hp.sdf.ngp.search.engine.model.SqlOrder;
import com.hp.sdf.ngp.search.engine.model.StringPropertyCondition;
import com.hp.sdf.ngp.search.engine.model.Function.FunctionCollection;
import com.hp.sdf.ngp.search.engine.model.JoinMetaData.JoinPurpose;
import com.hp.sdf.ngp.search.engine.model.LogicCondition.ConnectionLogic;

public class SearchExpressionImpl implements SearchExpression {

	private final static Log log = LogFactory.getLog(SearchExpressionImpl.class);

	private BaseDao dao;

	public BaseDao getDao() {
		return dao;
	}

	public void setDao(BaseDao dao) {
		this.dao = dao;
	}

	private static final long serialVersionUID = 1L;

	private static Map<OrderBy, OrderExecutor> orderExecutors = new HashMap<OrderBy, OrderExecutor>();

	public static void registerOrderExecutor(OrderBy orderBy, OrderExecutor orderExecutor)

	{
		orderExecutors.put(orderBy, orderExecutor);
	}

	protected List<Function> functions = new ArrayList<Function>();

	protected List<Condition> conditions = new ArrayList<Condition>();

	protected List<Condition> eavCondition = new ArrayList<Condition>();

	protected List<Condition> joinCondition = new ArrayList<Condition>();

	

	protected List<Order> orders = new ArrayList<Order>();

	protected Map<String, JoinMetaData> map = new LinkedHashMap<String, JoinMetaData>();

	protected Map<String, String> eavJoinNameMap = new LinkedHashMap<String, String>();
	protected Map<String, String> eavJoinAttributeNameMap = new LinkedHashMap<String, String>();
	
	protected List<Condition> eavOrderAttributeNameCondtions=new ArrayList<Condition>();

	protected int first = 0;

	protected int max = Integer.MAX_VALUE;

	// -------------------interface method-------------------
	public void addCondition(Condition condition) {
		Assert.notNull(condition, "condition can not be null.");
		// if (condition.getConditionType() == ConditionType.join) {
		// joinCondition.add(condition);
		// return;
		// }
		// if (condition.getConditionType() == ConditionType.eav) {
		// eavCondition.add(condition);
		// return;
		// }
		conditions.add(condition);
	}

	public void setFirst(int first) {
		this.first = first;
	}

	public void setMax(int max) {
		this.max = max;
	}

	public void addOrder(OrderBy orderBy, OrderEnum orderEnum) {
		OrderExecutor orderExecutor = orderExecutors.get(orderBy);
		if (orderExecutor == null) {
			log.warn("Can't find the actual orderby executor[" + orderBy + "]");
		}
		orderExecutor.execute(orderBy, orderEnum, this);
	}

	public ProjectionList getProjection() {
		ProjectionList list = Projections.projectionList();
		for (Function f : functions) {
			list.add(f.getProjection());
		}
		return list;
	}

	// -------------------interface support method-------------------
	public void addFunction(Function function) {
		functions.add(function);
	}
	private String retrieveEavJoinName(String eavJoinName)
	{
		if (eavJoinNameMap.containsKey(eavJoinName)) {
			String tempEavJoinName = eavJoinName;
			for (int eavJoinNameIndex = 0; eavJoinNameMap.containsKey(eavJoinName); eavJoinNameIndex++) {
				eavJoinName = tempEavJoinName + eavJoinNameIndex;
			}
		}
		eavJoinNameMap.put(eavJoinName, eavJoinName);
		return eavJoinName;
	}
	
	private String retrieveEavJoinAttributeName()
	{
		String eavJoinAttributeName = "attribute";
		if (eavJoinAttributeNameMap.containsKey(eavJoinAttributeName)) {
			String tempEavJoinAttributeName = eavJoinAttributeName;
			for (int eavJoinAttributeNameIndex = 0; eavJoinAttributeNameMap.containsKey(eavJoinAttributeName); eavJoinAttributeNameIndex++) {
				eavJoinAttributeName = tempEavJoinAttributeName + eavJoinAttributeNameIndex;
			}
		}
		eavJoinAttributeNameMap.put(eavJoinAttributeName,eavJoinAttributeName);
		
		return eavJoinAttributeName;
	}
	public void addOrder(OrderEnum orderEnum, EavOrder eavOrder) {
		
		String eavJoinName =retrieveEavJoinName(eavOrder.getEavJoinName());
		
		String eavJoinAttributeName=retrieveEavJoinAttributeName();
		
		BaseCondition eavJoinAttributeCondition=this.createEavAttributeJoinCriterion(eavOrder.getEntityRelationShipType(), eavOrder.getEavAttributeName(), eavJoinName, eavJoinAttributeName);

		eavJoinAttributeCondition.setJoinPurpose(JoinPurpose.EAV_ORDER);
		
		eavOrderAttributeNameCondtions.add(eavJoinAttributeCondition);
		
		this.addCondition(eavJoinAttributeCondition);
		this.addOrder(orderEnum,eavJoinName+".value",true);
		
		
	}
	
	public void addOrder(OrderEnum orderEnum, String orderAliaPath, boolean isEAVOrder) {
		Assert.hasLength(orderAliaPath, "order alia path can not be null.");
		String aliaPath = orderAliaPath;
		if (aliaPath.contains(".")) {
			aliaPath = StringUtils.substringBeforeLast(orderAliaPath, ".");
			if (isEAVOrder) {
				addJoinCriterion(aliaPath, JoinPurpose.EAV_ORDER);
			} else {
				addJoinCriterion(aliaPath, JoinPurpose.ORDER);
			}
		}
		addFunction(new SimpleFunction(FunctionCollection.PROPERTY, Utils.substringAfterLast2(orderAliaPath, ".")));
		orders.add(new Order(orderAliaPath, orderEnum));
	}

	public void addOrder(OrderEnum orderEnum, String orderAliaPath) {
		addOrder(orderEnum, orderAliaPath, false);
	}

	public void addSimpleOrder(OrderEnum orderEnum, String orderAliaPath) {
		Assert.hasLength(orderAliaPath, "order alia path can not be null.");
		orders.add(new Order(orderAliaPath, orderEnum));
	}

	public void addSqlOrder(String sqlOrder) {
		Assert.hasLength(sqlOrder, "sql order can not be null.");
		orders.add(new OrderSql(sqlOrder));
	}

	// -------------------search interface method-------------------
	public List<Criterion> getCriterions() throws ConditionNoMatchedException {
		List<Criterion> criterions = new ArrayList<Criterion>();
		for (Condition c : conditions) {
			criterions.add(getCriterionByCondition(c));
		}
		return criterions;
	}
	
	public List<Criterion> getEavAttributeNameCriterions() throws ConditionNoMatchedException {
		List<Criterion> attributeNameCriterions=new ArrayList<Criterion>();
		for (Condition c : this.eavOrderAttributeNameCondtions) {
			attributeNameCriterions.add(getCriterionByCondition(c));
		}
		return attributeNameCriterions;
	}
	
	// -------------------search interface method-------------------
	

	public Map<String, JoinMetaData> getJoinedCriterions() throws ConditionNoMatchedException {
		for (Condition c : joinCondition) {
			addJoinCondition(c);
		}
		return map;
	}

	// public void getEavCondition() throws ConditionNoMatchedException {
	// com.hp.sdf.ngp.eav.Condition con = null;
	// for (Condition c : eavCondition) {
	// if (con == null) {
	// if (c instanceof EavCondition) {
	// con = ((EavCondition) c).getActualEavCondition();
	// }
	// } else {
	// con = new com.hp.sdf.ngp.eav.Condition(con, SqlConnector.and,
	// getEavCondition(c));
	// }
	// }
	// if (con != null)
	// criterions.add(getEavCriterion(con));
	// }

	public List<org.hibernate.criterion.Order> getOrder() {
		List<org.hibernate.criterion.Order> newOrders = new ArrayList<org.hibernate.criterion.Order>();
		for (Order orginOrder : orders) {
			if (orginOrder instanceof OrderSql) {
				newOrders.add(SqlOrder.sqlOrder(((OrderSql) orginOrder).getSql()));
			} else {
				String orderByString = orginOrder.getOrderBy();
				orderByString = Utils.substringAfterLast2(orderByString, ".");
				if (orginOrder.getOrder() == OrderEnum.ASC) {
					newOrders.add(org.hibernate.criterion.Order.asc(orderByString));
				} else if (orginOrder.getOrder() == OrderEnum.DESC) {
					newOrders.add(org.hibernate.criterion.Order.desc(orderByString));
				}
			}
		}
		return newOrders;
	}

	public int getMax() {
		return max;
	}

	public int getFirst() {
		return first;
	}

	// -------------------search interface support method-------------------
	protected void addJoinCondition(Condition c) throws ConditionNoMatchedException {
		if (c instanceof LogicCondition) {
			addLogicCondition((LogicCondition) c);
		}
		if (c instanceof JoinPropertyCondition) {
			addJoinPropertyCondition((JoinPropertyCondition) c);
		}
		if (c instanceof ObjectPropertyCondition) {
			addCondition((ObjectPropertyCondition) c);
		}
	}

	protected void addLogicCondition(LogicCondition logicCondition) throws ConditionNoMatchedException {
		Condition left = logicCondition.getLeft();
		if (left instanceof LogicCondition) {
			addLogicCondition((LogicCondition) left);
		} else if (left instanceof JoinPropertyCondition) {
			addJoinPropertyCondition((JoinPropertyCondition) left);
		} else if (left instanceof ObjectPropertyCondition) {
			addCondition((ObjectPropertyCondition) left);
		}

		Condition right = logicCondition.getRight();
		if (right instanceof LogicCondition) {
			addLogicCondition((LogicCondition) right);
		} else if (right instanceof JoinPropertyCondition) {
			addJoinPropertyCondition((JoinPropertyCondition) right);
		} else if (right instanceof ObjectPropertyCondition) {
			addCondition((ObjectPropertyCondition) right);
		}
	}

	protected void addJoinPropertyCondition(JoinPropertyCondition joinCondition) throws ConditionNoMatchedException {
		String path = joinCondition.getPath();
		addJoinCriterion(path, joinCondition.getJoinPurpose());
	}

	protected void addJoinCriterion(String path, JoinPurpose purpose) {
		String[] entityNames = path.split("\\.");

		String lastEntityName = null;
		for (int i = 0; i < entityNames.length; i++) {

			String entityName = entityNames[i];

			String alias = lastEntityName == null ? entityName : lastEntityName + "." + entityName;

			if (map.containsKey(alias)) {
				JoinMetaData joinMetaData = map.get(alias);
				if (joinMetaData.getPurpose() == JoinPurpose.COMMON || joinMetaData.getPurpose() == JoinPurpose.ALL) {
					joinMetaData.setPurpose(purpose);
				}

			} else {
				JoinMetaData joinMetaData = new JoinMetaData(purpose);
				map.put(alias, joinMetaData);

			}
			lastEntityName = entityName;
		}
	}

	protected Criterion getCriterionByCondition(Condition con) throws ConditionNoMatchedException {
		if (con == null)
			return null;
		if (con instanceof LogicCondition) {
			ConnectionLogic logic = ((LogicCondition) con).getConnectionLogic();
			Criterion left = getCriterionByCondition(((LogicCondition) con).getLeft());
			if (logic == ConnectionLogic.AND)
				return Restrictions.and(left, getCriterionByCondition(((LogicCondition) con).getRight()));
			else if (logic == ConnectionLogic.OR) {
				return Restrictions.or(left, getCriterionByCondition(((LogicCondition) con).getRight()));
			} else if (logic == ConnectionLogic.NOT) {
				return Restrictions.not(left);
			}
		}
		if (con instanceof EavCondition) {
			return getEavCriterion((EavCondition) con);
		}
		if (con instanceof JoinPropertyCondition) {
			addJoinPropertyCondition((JoinPropertyCondition) con);
		}
		if (con instanceof PropertyCondition) {
			if (con instanceof PropertyEqualCondition)
				addPropertyEqualJoinCriterion((PropertyEqualCondition) con);
			return ((PropertyCondition) con).getCriterion();
		}
		if (con instanceof ObjectPropertyCondition) {
			return getCriterionByCondition(((ObjectPropertyCondition) con).getCondition());
		}
		throw new ConditionNoMatchedException("Not support this kind of condition:" + con.getClass().getName());
	}

	public  BaseCondition createEavAttributeJoinCriterion(EntityRelationShipType entityRelationShipType, String attributeName, String eavJoinName,
			String eavAttributeJoinName) {

		BaseCondition attributeNameCondition = new StringPropertyCondition(eavAttributeJoinName + ".attributeName", attributeName,
				StringComparer.EQUAL, false, false);
		
		attributeNameCondition.setJoinPurpose(JoinPurpose.EAV_ORDER);

		BaseCondition joinCondition = null;

		if (entityRelationShipType != null) {

			String rsTableName = EavCondition.getEntityRelationShipTypeName(entityRelationShipType);
			if (rsTableName != null) {
				joinCondition = new JoinPropertyCondition(rsTableName + "." + eavJoinName + "." + eavAttributeJoinName, attributeNameCondition);
			} else {

				throw new NgpRuntimeException("No acutal implementation for this EntityRelationShipType[" + entityRelationShipType + "]");
			}
		} else {

			joinCondition = new JoinPropertyCondition(eavJoinName + "."+ eavAttributeJoinName, attributeNameCondition);

		}
		
		return joinCondition;

	}

	public Criterion getEavCriterion(EavCondition eavCon) throws ConditionNoMatchedException {

		String eavJoinName =retrieveEavJoinName(eavCon.getEavJoinName());
		
		String eavJoinAttributeName=retrieveEavJoinAttributeName();
		
		
		
		BaseCondition eavJoinCondition=eavCon.createEavPropertyCondition(eavJoinName+".value");
		BaseCondition eavJoinAttributeCondition=this.createEavAttributeJoinCriterion(eavCon.getEntityRelationShipType(), eavCon.getAttributeName(), eavJoinName, eavJoinAttributeName);
		
		
		Criterion c = getCriterionByCondition(eavJoinCondition.and(eavJoinAttributeCondition));

		if (eavCon.getEntityRelationShipType() != null) {

			String rsTableName = eavCon.getEntityRelationShipTypeName();
			if (rsTableName != null) {
				addJoinCriterion(rsTableName + "." + eavJoinName, JoinPurpose.EAV);

			} else {
				log.warn("No acutal implementation for this EntityRelationShipType[" + eavCon.getEntityRelationShipType() + "]");
				throw new ConditionNoMatchedException("No acutal implementation for this EntityRelationShipType["
						+ eavCon.getEntityRelationShipType() + "]");
			}
		} else {
			addJoinCriterion(eavJoinName, JoinPurpose.EAV);

		}

		return c;

	}

	protected void addPropertyEqualJoinCriterion(PropertyEqualCondition con) {
		if (con.isNeedJoin()) {
			String first = con.getFirstProperty();
			String second = con.getSecondProperty();

			if (first.contains(".")) {
				addJoinCriterion(StringUtils.substringBeforeLast(first, "."), JoinPurpose.COMMON);
			}
			if (second.contains(".")) {
				addJoinCriterion(StringUtils.substringBeforeLast(second, "."), JoinPurpose.COMMON);
			}
		}
	}

	public static void main(String[] args) {
		System.out.println(StringUtils.substringAfter("a", "."));
	}

}

// $Id$