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
package com.hp.sdf.ngp.eav.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import org.springframework.stereotype.Component;
import com.hp.sdf.ngp.dao.JpaDao;
import com.hp.sdf.ngp.eav.AttributeType;
import com.hp.sdf.ngp.eav.Condition;
import com.hp.sdf.ngp.eav.EavRepository;
import com.hp.sdf.ngp.eav.dao.AttributeValueCharDAO;
import com.hp.sdf.ngp.eav.dao.AttributeValueDateDAO;
import com.hp.sdf.ngp.eav.dao.AttributeValueNumberDAO;
import com.hp.sdf.ngp.eav.model.Attribute;
import com.hp.sdf.ngp.eav.model.AttributeValue;
import com.hp.sdf.ngp.eav.model.AttributeValueChar;
import com.hp.sdf.ngp.eav.model.AttributeValueDate;
import com.hp.sdf.ngp.eav.model.AttributeValueNumber;
import com.hp.sdf.ngp.eav.model.EntityAttribute;

@Component
public class EavUtil {
	
	@Resource
	private AttributeValueCharDAO attributeValueCharDao;
	
	@Resource
	private AttributeValueNumberDAO attributeValueNumberDao;
	
	@Resource
	private AttributeValueDateDAO attributeValueDateDao;
	
	@Resource
	private EavRepository eavRepository;

	@SuppressWarnings("unchecked")
	public JpaDao getAttributeValueDao(AttributeType type){
		JpaDao dao = null;
		switch(type){
		case number:
			dao = this.attributeValueNumberDao;
			break;
		case varchar:
			dao = this.attributeValueCharDao;
			break;
		case date:
			dao = this.attributeValueDateDao;
			break;
		}
		return dao;
	}
	
	@SuppressWarnings("unchecked")
	public AttributeValue getAttributeValueModel(AttributeType type){
		AttributeValue model = null;
		switch(type){
		case number:
			model = new AttributeValueNumber();
			break;
		case varchar:
			model = new AttributeValueChar();
			break;
		case date:
			model = new AttributeValueDate();
			break;
		}
		return model;
	}
	
	@SuppressWarnings("unchecked")
	public String parseConditionNativeOracle(Condition condition){
		String sql = null;
		String sqlExt = null;
		if(condition.isLeaf()){
			Attribute attribute = null;
			if(condition.getAttributeID() != null)
				attribute = this.eavRepository.getAttributeDefineByID(condition.getAttributeID(), false);
			else if(condition.getAttributeName() != null)
				attribute = this.eavRepository.getAttributeDefineByName(condition.getAttributeName(), false);
			if(attribute == null)
				return null;
			AttributeValue model = this.getAttributeValueModel(attribute.getAttributeType());
			sql = "select distinct ea.entityID from EntityAttribute ea where ea.attributeValueID "
				+ "in (select model.attributeValueID from " 
				+ model.getClass().getSimpleName() 
				+ " model where model.attributeID = " 
				+ attribute.getAttributeID();
			if(condition.getValue() != null){
				switch(attribute.getAttributeType()){
				case number:
					switch(condition.getComparer()){
					case equals:
						sqlExt = " and model.value = " + condition.getValue() + "";
						break;
					case lessthan:
						sqlExt = " and model.value < " + condition.getValue() + "";
						break;
					case lessequal:
						sqlExt = " and model.value <= " + condition.getValue() + "";
						break;
					case like:
						break;
					case morethan:
						sqlExt = " and model.value > " + condition.getValue() + "";
						break;
					case moreequal:
						sqlExt = " and model.value >= " + condition.getValue() + "";
						break;
					case notEquals:
						sqlExt = " and model.value <> " + condition.getValue() + "";
						break;
					}
					sql += sqlExt;
					sql += ")";
					sql += " and ea.attributeType = " + AttributeType.number.ordinal();
					break;
				case varchar:
					switch(condition.getComparer()){
					case equals:
						sqlExt = " and model.value = '" + condition.getValue().toString() + "'";
						break;
					case lessthan:
						break;
					case lessequal:						
						break;
					case like:
						sqlExt = " and model.value like '%" + condition.getValue().toString() + "%'";
						break;
						
					case likewildcard:
						sqlExt = " and model.value like '" + condition.getValue().toString() + "'";
						break;
					case morethan:
						break;
					case moreequal:
						break;
					case notEquals:
						sqlExt = " and model.value <> '" + condition.getValue().toString() + "'";
						break;
					}
					sql += sqlExt;
					sql += ")";
					sql += " and ea.attributeType = " + AttributeType.varchar.ordinal();
					break;
				case date:
					switch(condition.getComparer()){
					case equals:
						sqlExt = " and model.value = '" + new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format((Date)condition.getValue()) + "'";
						break;
					case lessthan:
						sqlExt = " and model.value < '" + new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format((Date)condition.getValue()) + "'";
						break;
					case lessequal:
						sqlExt = " and model.value <= '" + new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format((Date)condition.getValue()) + "'";
						break;
					case like:
						break;
					case morethan:
						sqlExt = " and model.value > '" + new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format((Date)condition.getValue()) + "'";
						break;
					case moreequal:
						sqlExt = " and model.value >= '" + new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format((Date)condition.getValue()) + "'";
						break;
					case notEquals:
						sqlExt = " and model.value <> '" + new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format((Date)condition.getValue()) + "'";
						break;
					}
					sql += sqlExt;
					sql += ")";
					sql += " and ea.attributeType = " + AttributeType.date.ordinal();
					break;
				}
			}
		}else{
			String leftCondition = this.parseConditionNativeOracle(condition.getLeftCondition());
			String rightCondition = this.parseConditionNativeOracle(condition.getRightCondition());
			if(leftCondition == null && rightCondition == null)
				sql = null;
			else if(leftCondition != null && rightCondition == null)
				sql = leftCondition;
			else if(leftCondition == null && rightCondition != null)
				sql  = rightCondition;
			else{
				sql = "((" 
					+ leftCondition 
					+  ")";
				switch(condition.getConnector()){
				case and:
					sql += " intersect ";
					break;
				case or:
					sql += " union ";
					break;
				}
				sql += "(" + rightCondition + "))";
			}
		}
		
		return sql;
	}
	
		@SuppressWarnings("unchecked")
	public String parseConditionNativeMySql(Condition condition, List<String> sqls){
		String result = null;
		if(condition.isLeaf()){
			Attribute attribute = null;
			if(condition.getAttributeID() != null)
				attribute = this.eavRepository.getAttributeDefineByID(condition.getAttributeID(), false);
			else if(condition.getAttributeName() != null)
				attribute = this.eavRepository.getAttributeDefineByName(condition.getAttributeName(), false);
			if(attribute == null)
				return null;
			AttributeValue model = this.getAttributeValueModel(attribute.getAttributeType());
			String sql = null;
			sql = "create temporary table temp_value_" + condition.hashCode() + " (attributevalueid int(11) NOT NULL) " + 
						"select model.attributeValueID from " + model.getClass().getSimpleName() + " model " +
						"where model.attributeID = " + attribute.getAttributeID();
			String sqlExt = null;
			if(condition.getValue() != null){
				switch(attribute.getAttributeType()){
				case number:
					switch(condition.getComparer()){
					case equals:
						sqlExt = " and model.value = " + condition.getValue() + "";
						break;
					case lessthan:
						sqlExt = " and model.value < " + condition.getValue() + "";
						break;
					case lessequal:
						sqlExt = " and model.value <= " + condition.getValue() + "";
						break;
					case like:
						break;
					case morethan:
						sqlExt = " and model.value > " + condition.getValue() + "";
						break;
					case moreequal:
						sqlExt = " and model.value >=" + condition.getValue() + "";
						break;
					case notEquals:
						sqlExt = " and model.value <> " + condition.getValue() + "";
						break;
					}
					break;
				case varchar:
					switch(condition.getComparer()){
					case equals:
						sqlExt = " and model.value = '" + condition.getValue().toString() + "'";
						break;
					case lessthan:
						break;
					case lessequal:
						break;
					case like:
						sqlExt = " and model.value like '%" + condition.getValue().toString() + "%'";
						break;
					case likewildcard:
						sqlExt = " and model.value like '" + condition.getValue().toString() + "'";
						break;
					case morethan:
						break;
					case moreequal:
						break;
					case notEquals:
						sqlExt = " and model.value <> '" + condition.getValue().toString() + "'";
						break;
					}
					break;
				case date:
					switch(condition.getComparer()){
					case equals:
						sqlExt = " and model.value = '" + new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format((Date)condition.getValue()) + "'";
						break;
					case lessthan:
						sqlExt = " and model.value < '" + new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format((Date)condition.getValue()) + "'";
						break;
					case lessequal:
						sqlExt = " and model.value <= '" + new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format((Date)condition.getValue()) + "'";
						break;
					case like:
						break;
					case morethan:
						sqlExt = " and model.value > '" + new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format((Date)condition.getValue()) + "'";
						break;
					case moreequal:
						sqlExt = " and model.value >= '" + new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format((Date)condition.getValue()) + "'";
						break;
					case notEquals:
						sqlExt = " and model.value <> '" + new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format((Date)condition.getValue()) + "'";
						break;
					}
					break;
				}
			}
			sql += sqlExt;
			sqls.add(sql);
			String sql2 = "create temporary table temp_entity_" + condition.hashCode() + " (entityid int(11) NOT NULL) " + 
						"select ea.entityid from " + EntityAttribute.class.getSimpleName() + " ea, temp_value_" + condition.hashCode() + " v " +
						"where ea.attributevalueid = v.attributevalueid";
			if(condition.getValue() != null){
				switch(attribute.getAttributeType()){
				case number:
					sql2 += " and ea.attributeType = " + AttributeType.number.ordinal();
					break;
				case varchar:
					sql2 += " and ea.attributeType = " + AttributeType.varchar.ordinal();
					break;
				case date:
					sql2 += " and ea.attributeType = " + AttributeType.date.ordinal();
					break;
				}
			}
			sqls.add(sql2);
			sqls.add("drop temporary table temp_value_" + condition.hashCode());
			result = "select entityid from temp_entity_" + condition.hashCode();
		}else{
				switch(condition.getConnector()){
				case and:
					this.parseConditionNativeMySql(condition.getLeftCondition(),sqls);
					this.parseConditionNativeMySql(condition.getRightCondition(),sqls);
					String sql = "create temporary table temp_entity_" + condition.hashCode() + " (entityid int(11) NOT NULL) " + 
								"select ea1.entityid from temp_entity_" + condition.getLeftCondition().hashCode() + " ea1, " +
									"temp_entity_" + condition.getRightCondition().hashCode() + " ea2 " +
								"where ea1.entityid = ea2.entityid";
					sqls.add(sql);
					sqls.add("drop temporary table temp_entity_" + condition.getLeftCondition().hashCode());
					sqls.add("drop temporary table temp_entity_" + condition.getRightCondition().hashCode());
					result = "select entityid from temp_entity_" + condition.hashCode();
					break;
				case or:
					this.parseConditionNativeMySql(condition.getLeftCondition(),sqls);
					this.parseConditionNativeMySql(condition.getRightCondition(),sqls);
					String sql2 = "create temporary table temp_entity_" + condition.hashCode() + " (entityid int(11) NOT NULL) " + 
								"select ea1.entityid from temp_entity_" + condition.getLeftCondition().hashCode() + " ea1 union " +
								"select ea2.entityid from temp_entity_" + condition.getRightCondition().hashCode() + " ea2";
					sqls.add(sql2);
					sqls.add("drop temporary table temp_entity_" + condition.getLeftCondition().hashCode());
					sqls.add("drop temporary table temp_entity_" + condition.getRightCondition().hashCode());
					result = "select entityid from temp_entity_" + condition.hashCode();
					break;
				}
		}
		
		return result;
	}

}

// $Id$