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

import com.hp.sdf.ngp.api.comon.EntityRelationShipType;
import com.hp.sdf.ngp.api.comon.EntityType;
import com.hp.sdf.ngp.api.search.Condition.NumberComparer;
import com.hp.sdf.ngp.api.search.condition.basecondition.EavConditionDescriptor;

public class EavNumberConditionDescriptor<T> extends
		EavConditionDescriptor {

	private T value;

	private NumberComparer matchType;

	public T getValue() {
		return value;
	}

	public NumberComparer getMatchType() {
		return matchType;
	}

	public EavNumberConditionDescriptor(EntityType entityType,
			String attributeName, T value, NumberComparer matchType) {
		this(null,entityType, attributeName,value,matchType);
		

	}
	public EavNumberConditionDescriptor(EntityRelationShipType entityRelationShipType,EntityType entityType,
			String attributeName, T value, NumberComparer matchType) {
		super(entityRelationShipType,entityType, attributeName);
		this.value = value;
		this.matchType = matchType;

	}

}

// $Id$