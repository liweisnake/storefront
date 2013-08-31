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

import java.util.Date;

import com.hp.sdf.ngp.api.comon.EntityRelationShipType;
import com.hp.sdf.ngp.api.comon.EntityType;
import com.hp.sdf.ngp.search.engine.model.BaseCondition;
import com.hp.sdf.ngp.search.engine.model.DatePropertyCondition;
import com.hp.sdf.ngp.search.engine.model.EavCondition;

public class EavDateCondition extends EavCondition {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6690643709394339642L;

	
	private Date value;

	private DateComparer matchType;

	public String getEavJoinName() {
		return "attributeValueDates";
	}
	
	public BaseCondition createEavPropertyCondition(String eavPropertyName)
	{
		return new DatePropertyCondition(eavPropertyName, value, matchType);
	}

	public EavDateCondition(EntityType entityType, String attributeName, Date value, DateComparer matchType) {
		this(null, entityType, attributeName, value, matchType);
	}

	public EavDateCondition(EntityRelationShipType entityRelationShipType, EntityType entityType, String attributeName, Date value,
			DateComparer matchType) {
		super(entityRelationShipType, entityType, attributeName);
		this.value = value;
		this.matchType = matchType;
		
		
	}

	public Date getValue() {
		return value;
	}

	public void setValue(Date value) {
		this.value = value;
	}

	public DateComparer getMatchType() {
		return matchType;
	}

	public void setMatchType(DateComparer matchType) {
		this.matchType = matchType;
	}

}

// $Id$