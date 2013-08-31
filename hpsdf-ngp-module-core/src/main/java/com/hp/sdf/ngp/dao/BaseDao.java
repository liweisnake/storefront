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
package com.hp.sdf.ngp.dao;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.ObjectNotFoundException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Projection;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.impl.CriteriaImpl;
import org.hibernate.impl.SessionImpl;
import org.hibernate.transform.ResultTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import com.hp.sdf.ngp.api.search.OrderEnum;
import com.hp.sdf.ngp.common.exception.ConditionNoMatchedException;
import com.hp.sdf.ngp.common.exception.EntityNotFoundException;
import com.hp.sdf.ngp.common.util.Utils;
import com.hp.sdf.ngp.eav.EavRepository;
import com.hp.sdf.ngp.search.engine.SearchExpressionImpl;
import com.hp.sdf.ngp.search.engine.model.JoinMetaData;
import com.hp.sdf.ngp.search.engine.model.Order;
import com.hp.sdf.ngp.search.engine.model.JoinMetaData.JoinPurpose;
import com.hp.sdf.ngp.search.engine.model.JoinMetaData.JoinType;

import edu.emory.mathcs.backport.java.util.Arrays;

@SuppressWarnings("unchecked")
public abstract class BaseDao<PK extends Serializable, T> {

	private final static Log log = LogFactory.getLog(BaseDao.class);

	@Resource
	private EavRepository eavRepository;

	protected SessionFactory localSessionFactory;

	protected Class<T> entityClass;

	public enum DatabaseType {
		ORACLE,

		MYSQL,

		HSQLDB;
	}

	public EavRepository getEavRepository() {
		return eavRepository;
	}

	public DatabaseType getCurrentDatabaseType() {

		try {
			String dialect = ((SessionImpl) this.getSession()).getFactory().getDialect().getClass().getCanonicalName().toLowerCase();

			log.debug("the current used dialect is [" + dialect + "]");
			if (dialect.contains("oracle")) {
				return DatabaseType.ORACLE;
			}
			if (dialect.contains("mysql")) {
				return DatabaseType.MYSQL;
			}
			if (dialect.contains("hsql")) {
				return DatabaseType.HSQLDB;
			}

			return DatabaseType.ORACLE;
		} catch (Throwable e) {
			log.warn("Error when check the Dialect, return the default value[oracle]", e);
			return DatabaseType.ORACLE;
		}

	}

	public BaseDao() {
		this.entityClass = Utils.getSuperClassGenricType(getClass(), 1);
	}

	@Autowired
	public void setLocalSessionFactory(SessionFactory localSessionFactory) {
		this.localSessionFactory = localSessionFactory;
	}

	public Session getSession() {
		return localSessionFactory.getCurrentSession();
	}

	/**
	 * The legend method. persist, remove, merge, findById
	 */

	/**
	 * insert
	 */
	public void persist(T entity) {
		Assert.notNull(entity, "entity can not be null.");
		log.debug("Save entity " + entityClass);
		getSession().save(entity);
		getSession().flush();
	}

	/**
	 * delete
	 * 
	 * @param entity
	 */
	public void remove(T entity) {
		Assert.notNull(entity, "entity can not be null.");
		log.debug("delete entity:" + entityClass);

		getSession().delete(entity);
		getSession().flush();

	}

	/**
	 * update
	 * 
	 * @param entity
	 */
	public void merge(T entity) {
		Assert.notNull(entity, "entity can not be null.");
		log.debug("Update entity " + entityClass);
		try {
			getSession().clear();
			getSession().update(entity);
			getSession().flush();

		} catch (ObjectNotFoundException e) {
			throw new EntityNotFoundException("Entity not found: " + entity);
		}
	}

	/**
	 * find entity by id
	 * 
	 * @param id
	 * @return
	 */
	public T findById(PK id) {
		Assert.notNull(id, "id can not be null.");
		T t = (T) getSession().get(entityClass, id);
		if (t != null) {
			getSession().refresh(t);
		}
		return t;
	}

	/**
	 * delete entity by id
	 * 
	 * @param id
	 */
	public void remove(final PK id) {
		Assert.notNull(id, "id can not be null.");

		remove(findById(id));
		if (log.isDebugEnabled())
			log.debug("delete entity " + entityClass.getSimpleName() + "id is " + id);
	}

	/**
	 * get all entity in entity table
	 * 
	 * @param first
	 * @param max
	 * @return
	 */
	public List<T> getAll(int first, int max) {
		List<T> list = simpleFind(first, max);
		if (log.isDebugEnabled())
			log.debug("get all " + entityClass.getSimpleName() + ", the list count is " + list.size());
		return list;
	}

	/**
	 * get all entity count
	 * 
	 * @return
	 */
	public int getAllCount() {
		int count = findCriteriaCount(createCriteria());
		if (log.isDebugEnabled())
			log.debug("get all " + entityClass.getSimpleName() + " count" + count);
		return count;
	}

	/**
	 * get all entity in entity table and order the result
	 * 
	 * @param order
	 * @param start
	 * @param max
	 * @return
	 */
	public List<T> getAll(Order order, int start, int max) {
		Criteria c = createCriteria();
		if (order != null) {
			if (order.getOrder() == OrderEnum.ASC) {
				c.addOrder(org.hibernate.criterion.Order.asc(order.getOrderBy()));
			} else {
				c.addOrder(org.hibernate.criterion.Order.desc(order.getOrderBy()));
			}
		}
		List<T> list = c.list();
		if (log.isDebugEnabled())
			log.debug("get all " + entityClass.getSimpleName() + " by order"
					+ (order == null ? " null" : order.getOrderBy() + " " + order.getOrder()) + ", the list count is " + list.size());
		return list;
	}

	/**
	 * Search about method.
	 */
	/**
	 * support method
	 */

	protected Criteria createCriteria(final Criterion... criterions) {
		Criteria criteria = getSession().createCriteria(entityClass);
		StringBuilder sb = new StringBuilder();
		sb.append("All connected criteria: ");
		for (Criterion c : criterions) {
			sb.append(c + " ");
			criteria.add(c);
		}
		log.debug(sb);
		return criteria;
	}

	protected Criteria createCriteria(final List<org.hibernate.criterion.Order> orders, final List<Criterion> criterions) {
		return createCriteria(null, criterions, null, orders, 0, Integer.MAX_VALUE);
	}

	protected Criteria buildCriteria(final List<Criterion> criterions) {
		Criteria criteria = null;
		if (criterions == null)
			criteria = createCriteria();
		else
			criteria = createCriteria(criterions.toArray(new Criterion[criterions.size()]));
		return criteria;
	}

	protected Criteria buildProjection(final Criteria criteria, final ProjectionList projections) {// new
		// EntityProjection();
		Projection projection = Projections.alias(Projections.id(), "pid");
		if (projections == null) {
			projection = Projections.distinct(projection);
		} else {
			projections.add(projection);
			projection = Projections.distinct(projections);
		}
		criteria.setProjection(projection);
		return criteria;
	}

	protected Criteria buildEavCondition(final Criteria criteria, final com.hp.sdf.ngp.eav.Condition eavCondition) {
		// eavCondition
		if (eavCondition != null) {
			// String sql = null;
			// if ("mysql".equals(getDbType())) {
			// String eavConditionTableName = "eav_table_"
			// + eavCondition.hashCode();
			// String preSql = "(create temporary table " +
			// eavConditionTableName
			// + " (attributevalueid int(11) NOT NULL) "
			// + eavRepository.searchSqlNativeEntity(eavCondition) + ")";
			// // sqlExecute(preSql);
			// sql = " join " + eavConditionTableName + " on pid = entityid "
			// + preSql;
			// } else {
			// String eavSql =
			// eavRepository.searchSqlNativeEntity(eavCondition);
			// if (eavSql == null) {
			// throw new EavValueNotFoundException(
			// "Eav value not found for condition: " + eavCondition);
			// }
			// sql = " or " + criteria.getAlias() + "_.entityId in (" + eavSql +
			// ")";

			// }
		}
		return criteria;
	}

	protected Criteria buildJoinCondition(Criteria criteria, final Map<String, JoinMetaData> joinCriterions) {
		return buildJoinCondition(criteria, joinCriterions, JoinPurpose.ALL);
	}

	protected Criteria buildJoinCondition(Criteria criteria, final Map<String, JoinMetaData> joinCriterions, JoinPurpose purpose) {
		if (joinCriterions != null && joinCriterions.size() > 0) {
			Map<String, String> map = new HashMap<String, String>();
			for (Entry<String, JoinMetaData> joinCriterion : joinCriterions.entrySet()) {
				if (purpose == JoinPurpose.ALL || joinCriterion.getValue().getPurpose() == purpose) {
					String alias = joinCriterion.getKey();
					String newAlias = StringUtils.substringAfter(alias, ".");
					int joinType = -1;
					if (joinCriterion.getValue().getType() == JoinType.INNER) {
						joinType = CriteriaSpecification.INNER_JOIN;
					} else if (joinCriterion.getValue().getType() == JoinType.LEFTOUT) {
						joinType = CriteriaSpecification.LEFT_JOIN;
					} else if (joinCriterion.getValue().getType() == JoinType.FULL) {
						joinType = CriteriaSpecification.FULL_JOIN;
					}
					String aliaShortName = "".equals(newAlias) ? alias : newAlias;
					String tmpAliaShortName = aliaShortName;
					for (int i = 0; map.get(tmpAliaShortName) != null; i++) {
						tmpAliaShortName = aliaShortName + i;
					}
					map.put(tmpAliaShortName, tmpAliaShortName);
					criteria = criteria.createAlias(alias, tmpAliaShortName, joinType);
				}
				// if (!joinCriterion.getValue().empty()) {
				// for (Criterion c : joinCriterion.getValue().getList()) {
				// criteria.add(c);
				// }
				// }
			}
		}
		return criteria;
	}

	protected Criteria buildOrder(Criteria criteria, final List<org.hibernate.criterion.Order> orders) {
		if (orders != null) {
			for (org.hibernate.criterion.Order order : orders) {
				criteria.addOrder(order);
			}
		}
		return criteria;
	}

	protected Criteria createCriteria(final ProjectionList projection, final List<Criterion> criterions,
			final Map<String, JoinMetaData> joinCriterions, final List<org.hibernate.criterion.Order> orders, final int first, final int max) {

		Criteria criteria = buildCriteria(criterions);

		buildProjection(criteria, projection);

		// buildEavCondition(criteria, eavCondition);

		buildJoinCondition(criteria, joinCriterions);

		buildOrder(criteria, orders);

		criteria.setFirstResult(first);

		criteria.setMaxResults(max);

		return criteria;
	}

	protected Query createQueryByNameParam(final String queryString, final Map<String, Object> values) {
		Assert.hasText(queryString, "queryString can not be null.");
		Query query = getSession().createQuery(queryString);
		if (values != null) {
			query.setProperties(values);
		}
		log.debug("Query string is:" + query.getQueryString());
		return query;
	}

	protected Query createQueryBySequenceParam(final String queryString, final Object... values) {
		Assert.hasText(queryString, "queryString can not be null.");
		Query query = getSession().createQuery(queryString);
		if (values != null) {
			for (int i = 0; i < values.length; i++) {
				query.setParameter(i, values[i]);
			}
		}
		log.debug("Query string is:" + query.getQueryString());
		return query;
	}

	/**
	 * <pre>
	 * Hql batch operation. Binding by value
	 * 
	 * example:
	 * 
	 * String description = &quot;abc&quot;;
	 * String name = &quot;ded&quot;;
	 * String hql = &quot;select from asset where description = ? and name = ?&quot;;
	 * batchExecute(hql, description, name);
	 * 
	 * </pre>
	 * 
	 * @param hql
	 *            hql string.
	 * @param values
	 *            Object parameter
	 * @return affected rows
	 */
	public int executeHQLBySequenceParam(final String hql, final Object... values) {
		return createQueryBySequenceParam(hql, values).executeUpdate();
	}

	/**
	 * <pre>
	 * Hql batch operation. Binding by name parameter
	 * 
	 * example:
	 * 
	 * String description = &quot;abc&quot;;
	 * String name = &quot;ded&quot;;
	 * String hql = &quot;select from asset where description = :description and name = :name&quot;;
	 * Map&lt;String, Object&gt; map = new HashMap&lt;String, Object&gt;();
	 * map.add(&quot;description&quot;, description);
	 * map.add(&quot;name&quot;, name);
	 * batchExecute(hql, map);
	 * 
	 * </pre>
	 * 
	 * @param hql
	 *            hql string.
	 * @param values
	 *            named parameter
	 * @return affected rows
	 */
	public int executeHQL(final String hql, final Map<String, Object> values) {
		return createQueryByNameParam(hql, values).executeUpdate();
	}

	public Query distinct(Query query) {
		query.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
		return query;
	}

	public Criteria distinct(Criteria criteria) {
		criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
		return criteria;
	}

	public List<T> distinct(List list) {
		Set<T> set = new LinkedHashSet<T>(list);
		return new ArrayList<T>(set);
	}

	/**
	 * find method
	 */
	/**
	 * Use criteria
	 */
	protected List<T> simpleFind(int first, int max, final Criterion... criterions) {
		if (first < 0)
			first = 0;
		return find(null, Arrays.asList(criterions), null, null, null, first, max, null);
	}

	protected int simpleFindCount(final Criterion... criterions) {
		// Criteria criteria = createCriteria(criterions);
		// int count = findCriteriaCount(distinct(criteria));
		// if (log.isDebugEnabled())
		// log.debug("simpleFindCount" + entityClass.getSimpleName()
		// + " count" + count);
		return findCount(null, Arrays.asList(criterions), null, null, 0, Integer.MAX_VALUE);
	}

	protected List<T> find(SearchExpressionImpl impl) throws ConditionNoMatchedException {
		return find(impl.getProjection(), impl.getCriterions(), impl.getEavAttributeNameCriterions(), impl.getJoinedCriterions(), impl.getOrder(),
				impl.getFirst(), impl.getMax(), impl);
	}

	/**
	 * find by criteria all set.Set includes projection, criterions and joined
	 * criterions, orders, first result and max result
	 * 
	 * @param projection
	 * @param criterions
	 * @param joinCriterions
	 * @param orders
	 * @param first
	 * @param max
	 * @return
	 */
	protected List<T> find(final ProjectionList projection, final List<Criterion> criterions, final List<Criterion> eavAttributeNameCriterions,
			final Map<String, JoinMetaData> joinCriterions, final List<org.hibernate.criterion.Order> orders, final int first, final int max,
			final SearchExpressionImpl impl) {
		List list = new ArrayList<T>();
		list = createCriteria(projection, criterions, joinCriterions, orders, first, max).list();

		if (list.size() > 0) {
			List idList = new ArrayList(list.size());
			for (Object obj : list) {
				if (obj instanceof Object[]) {
					Object[] array = (Object[]) obj;
					idList.add(array[array.length - 1]);
				} else {
					idList.add(obj);
				}
			}
			String key = Utils.getPKName(entityClass);
			Criteria entityCriteria = buildCriteria(eavAttributeNameCriterions);
			// buildProjection(entityCriteria, projection);
			buildJoinCondition(entityCriteria, joinCriterions, JoinPurpose.ORDER);
			buildJoinCondition(entityCriteria, joinCriterions, JoinPurpose.EAV_ORDER);
			buildOrder(entityCriteria, orders);
			Criteria resultCriteria = distinct(entityCriteria.add(getLimitedIdList(key, idList)));
			resultCriteria.setCacheable(true);
			// if (impl != null)
			// clean(impl.getTempTableName(), impl.getTempTableNum());
			return resultCriteria.list();
		} else {
			// if (impl != null)
			// clean(impl.getTempTableName(), impl.getTempTableNum());
			return new ArrayList();
		}

	}

	/*
	 * protected void clean(String tableName, int maxTableNum) { String sql =
	 * "drop temporary table " + tableName; for (int i = 0; i < maxTableNum;
	 * i++) { this.sqlExecute(sql + i); } }
	 */

	private Criterion getLimitedIdList(String key, List idList) {
		int size = idList.size();
		int loopCount = size / 1000;
		int rest = size % 1000;
		Criterion criterion = Restrictions.in(key, idList.subList(0, loopCount == 0 ? rest : 999));
		for (int i = 1; i <= loopCount; i++) {
			Restrictions.or(criterion, Restrictions.in(key, idList.subList(i * 1000, i == loopCount ? rest + i * 1000 : (i + 1) * 1000)));
		}
		return criterion;
	}

	/**
	 * get count
	 * 
	 * @param projection
	 * @param criterions
	 * @param joinCriterions
	 * @param orders
	 * @param first
	 * @param max
	 * @return
	 */
	protected int findCount(final ProjectionList projection, final List<Criterion> criterions, final Map<String, JoinMetaData> joinCriterions,
			final List<org.hibernate.criterion.Order> orders, final int first, final int max) {
		return findCriteriaCount(distinct(createCriteria(projection, criterions, joinCriterions, null, 0, Integer.MAX_VALUE)));
	}

	protected int findCount(SearchExpressionImpl impl) throws ConditionNoMatchedException {
		return findCount(impl.getProjection(), impl.getCriterions(), impl.getJoinedCriterions(), impl.getOrder(), impl.getFirst(), impl.getMax());
	}

	/**
	 * find by one property
	 * 
	 * @param propertyName
	 * @param value
	 * @return
	 */
	public List<T> findBy(final String propertyName, final Object value, int first, int max) {
		Assert.hasText(propertyName, "propertyName can not be null.");
		Criterion criterion = Restrictions.eq(propertyName, value);
		return simpleFind(first, max, criterion);
	}

	public List<T> findBy(final String propertyName, final Object value) {
		return findBy(propertyName, value, 0, Integer.MAX_VALUE);
	}

	/**
	 * count find by one property
	 * 
	 * @param propertyName
	 * @param value
	 * @return
	 */
	public int findByCount(final String propertyName, final Object value) {
		Assert.hasText(propertyName, "propertyName can not be null.");
		Criterion criterion = Restrictions.eq(propertyName, value);
		int count = findCriteriaCount(createCriteria(criterion));
		if (log.isDebugEnabled())
			log.debug("findByCount " + entityClass.getSimpleName() + " count " + count);
		return count;
	}

	/**
	 * find entity by property
	 * 
	 * @param propertyName
	 * @param value
	 * @return
	 */
	public T findUniqueBy(final String propertyName, final Object value) {
		Assert.hasText(propertyName, "propertyName can not be null.");
		Criterion criterion = Restrictions.eq(propertyName, value);
		return (T) createCriteria(criterion).uniqueResult();
	}

	/**
	 * find entity by a set of criterions
	 * 
	 * @param criterions
	 * @return
	 */
	protected T findUnique(final Criterion... criterions) {
		return (T) createCriteria(criterions).uniqueResult();
	}

	protected int findCriteriaCount(final Criteria c) {
		CriteriaImpl impl = (CriteriaImpl) c;

		Projection projection = impl.getProjection();
		ResultTransformer transformer = impl.getResultTransformer();

		List<CriteriaImpl.OrderEntry> orderEntries = null;
		try {
			Field f = impl.getClass().getDeclaredField("orderEntries");
			f.setAccessible(true);
			orderEntries = (List) f.get(impl);
			f.set(impl, new ArrayList());
		} catch (Exception e) {
			e.printStackTrace();
			log.debug("Unexceptable exception:" + e.getMessage());
		}

		int totalCount = (Integer) c.setProjection(Projections.rowCount()).uniqueResult();

		c.setProjection(projection);

		if (projection == null) {
			c.setResultTransformer(CriteriaSpecification.ROOT_ENTITY);
		}
		if (transformer != null) {
			c.setResultTransformer(transformer);
		}
		try {
			Field f = impl.getClass().getDeclaredField("orderEntries");
			f.setAccessible(true);
			f.set(impl, orderEntries);
		} catch (Exception e) {
			log.debug("Unexceptable exception:" + e.getMessage());
		}

		return totalCount;
	}

	/**
	 * Use hql
	 */
	/**
	 * find by hql. Binding by value
	 * 
	 * example:
	 * 
	 * String description = &quot;abc&quot;; String name = &quot;ded&quot;;
	 * String hql = &quot;select from asset where description = ? and name =
	 * ?&quot;; findHql(hql, description, name);
	 */
	public List<T> findHql(final String hql, final Object... values) {
		return createQueryBySequenceParam(hql, values).list();
	}

	public Object findHqlUnique(final String hql, final Object... values) {
		return createQueryBySequenceParam(hql, values).uniqueResult();
	}

	/**
	 * For paging
	 * 
	 * @param hql
	 * @param first
	 * @param max
	 * @param values
	 * @return
	 */
	public List<T> findHql(final String hql, int first, int max, final Object... values) {
		Query query = createQueryBySequenceParam(hql, values);
		query.setFirstResult(first);
		query.setMaxResults(max);
		return query.list();
	}

	/**
	 * find by hql. Binding by value
	 * 
	 * example:
	 * 
	 * String description = &quot;abc&quot;; String name = &quot;ded&quot;;
	 * String hql = &quot;select from asset where description = :description and
	 * name = :name&quot;; Map&lt;String, Object&gt; map = new
	 * HashMap&lt;String, Object&gt;(); map.add(&quot;description&quot;,
	 * description); map.add(&quot;name&quot;, name); findHql(hql, map);
	 * 
	 * @param hql
	 * @param first
	 * @param max
	 * @param values
	 * @return
	 */
	public List<T> findHql(final String hql, final Map<String, Object> values) {
		return createQueryByNameParam(hql, values).list();
	}

	/**
	 * For paging.
	 * 
	 * @param hql
	 * @param first
	 * @param max
	 * @param values
	 * @return
	 */
	public List<T> findHql(final String hql, int first, int max, final Map<String, Object> values) {
		Query query = createQueryByNameParam(hql, values);
		query.setFirstResult(first);
		query.setMaxResults(max);
		return query.list();
	}

	public Object findHqlUnique(final String hql, final Map<String, Object> values) {
		return createQueryByNameParam(hql, values).uniqueResult();
	}

	/**
	 * Use sql, not recommented
	 */
	public List<Object[]> findSql(final String sql, int first, int max) {
		Assert.hasText(sql, "sql can not be null.");
		Query query = getSession().createSQLQuery(sql);
		query.setFirstResult(first);
		query.setMaxResults(max);
		return query.list();
	}

	/**
	 * Execute sql.
	 * 
	 * @param sql
	 *            sql string
	 * @return number of rows be effected
	 */
	public int sqlExecute(final String sql) {
		Assert.hasText(sql, "sql can not be null.");
		Query query = getSession().createSQLQuery(sql);
		return query.executeUpdate();
	}

	public Object sqlExecute(final String sql, List<Object> objs) {
		Assert.hasText(sql, "sql can not be null.");
		Query query = getSession().createSQLQuery(sql);
		for (int i = 0; i < objs.size(); i++) {
			if (objs.get(i) instanceof String) {
				query.setString(i, (String) objs.get(i));
			} else if (objs.get(i) instanceof Integer) {
				query.setInteger(i, (Integer) objs.get(i));
			} else if (objs.get(i) instanceof Long) {
				query.setLong(i, (Long) objs.get(i));
			} else if (objs.get(i) instanceof Double) {
				query.setDouble(i, (Double) objs.get(i));
			} else if (objs.get(i) instanceof Float) {
				query.setFloat(i, (Float) objs.get(i));
			} else if (objs.get(i) instanceof Date) {
				query.setDate(i, (Date) objs.get(i));
			}
		}
		return query.uniqueResult();
	}

	public Object findSqlUnique(final String sql) {
		Assert.hasText(sql, "sql can not be null.");
		Query query = getSession().createSQLQuery(sql);
		return query.uniqueResult();
	}

	public void clear() {
		getSession().clear();
	}

	public void evict(Object object) {
		getSession().evict(object);
	}

	public Long findEntityIdById(PK id) {
		String pkName = Utils.getPKName(entityClass);
		String hql = "select entityId from " + entityClass.getSimpleName() + " where " + pkName + "= ?";
		List<Long> entityIds = (List<Long>) this.findHql(hql, id);
		if (entityIds == null || entityIds.size() == 0) {
			return null;
		}
		return entityIds.get(0);
	}
	
	public List<T> findByHQL(final String propertyName, final Object value, int first, int max) {
		Assert.hasText(propertyName, "propertyName can not be null.");
		return this.findHql("from "+this.entityClass.getSimpleName() +" where "+propertyName+" = ? ", first, max,value );

	}

	public List<T> findByHQL(final String propertyName, final Object value) {
		return findByHQL(propertyName, value, 0, Integer.MAX_VALUE);
	}
}

// $Id$