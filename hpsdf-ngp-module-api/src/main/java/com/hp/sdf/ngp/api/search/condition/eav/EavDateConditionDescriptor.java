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
package com.hp.sdf.ngp.api.search.condition.eav;

import java.util.Date;

import com.hp.sdf.ngp.api.comon.EntityRelationShipType;
import com.hp.sdf.ngp.api.comon.EntityType;
import com.hp.sdf.ngp.api.search.Condition.DateComparer;
import com.hp.sdf.ngp.api.search.condition.basecondition.EavConditionDescriptor;

public class EavDateConditionDescriptor extends EavConditionDescriptor {

	private Date value;

	private DateComparer matchType;

	public Date getValue() {
		return value;
	}

	public DateComparer getMatchType() {
		return matchType;
	}

	public EavDateConditionDescriptor(EntityType entityType,
			String attributeName, Date value, DateComparer matchType) {
		this(null,entityType, attributeName,value,matchType);

	}
	public EavDateConditionDescriptor(EntityRelationShipType entityRelationShipType,EntityType entityType,
			String attributeName, Date value, DateComparer matchType) {
		super(entityRelationShipType,entityType, attributeName);
		this.value = value;
		this.matchType = matchType;
	}

}

// $Id$