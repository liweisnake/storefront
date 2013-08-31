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
package com.hp.sdf.ngp.search.condition.eav;

import java.math.BigDecimal;

import com.hp.sdf.ngp.api.comon.EntityRelationShipType;
import com.hp.sdf.ngp.api.comon.EntityType;
import com.hp.sdf.ngp.search.engine.model.BaseCondition;
import com.hp.sdf.ngp.search.engine.model.EavCondition;
import com.hp.sdf.ngp.search.engine.model.NumberPropertyCondition;

public class EavNumberCondition extends EavCondition {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5342604905072573538L;

	private Float floatValue;

	private NumberComparer matchType;

	public String getEavJoinName() {
		return "attributeValueNumbers";
	}
	public BaseCondition createEavPropertyCondition(String eavPropertyName)
	{
		return new NumberPropertyCondition(eavPropertyName, this.floatValue, matchType);
	}
	public EavNumberCondition(EntityType entityType, String attributeName, Object value, NumberComparer matchType) {
		this(null, entityType, attributeName, value, matchType);
	}

	public EavNumberCondition(EntityRelationShipType entityRelationShipType, EntityType entityType, String attributeName, Object value,
			NumberComparer matchType) {
		super(entityRelationShipType, entityType, attributeName);

		this.floatValue = convertToFloat(value);;
		this.matchType = matchType;
	}

	public Float getValue() {
		return floatValue;
	}

	public void setValue(Object value) {
		this.floatValue = convertToFloat(value);
	}

	public NumberComparer getMatchType() {
		return matchType;
	}

	public void setMatchType(NumberComparer matchType) {
		this.matchType = matchType;
	}

	private Float convertToFloat(Object value) {
		Float attributeValue=null;
		
			if (value instanceof Integer) {
				attributeValue = ((Integer) value).floatValue();
			} else if (value instanceof Double) {
				attributeValue = ((Double) value).floatValue();
			} else if (value instanceof BigDecimal) {
				attributeValue = ((BigDecimal) value).floatValue();
			} else if (value instanceof Long) {
				attributeValue = ((Long) value).floatValue();
			} 
			else if (value instanceof Float) {
				attributeValue = (Float) value;
			} else if (value instanceof String) {
				attributeValue = Float.valueOf((String) value);
			} 
			else {
				attributeValue = (Float) value;
			}
		
		return attributeValue;
	}

}

// $Id$