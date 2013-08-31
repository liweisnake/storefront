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
package com.hp.sdf.ngp.api.search.condition.basecondition;

import com.hp.sdf.ngp.api.comon.EntityRelationShipType;
import com.hp.sdf.ngp.api.comon.EntityType;
import com.hp.sdf.ngp.api.search.ConditionDescriptor;

public abstract class EavConditionDescriptor implements ConditionDescriptor {

	private EntityRelationShipType entityRelationShipType;
	
	private EntityType entityType;
	
	private String attributeName;
	
	public EntityType getEntityType() {
		return entityType;
	}

	public void setEntityType(EntityType entityType) {
		this.entityType = entityType;
	}

	public String getAttributeName() {
		return attributeName;
	}

	public void setAttributeName(String attributeName) {
		this.attributeName = attributeName;
	}

	public EavConditionDescriptor(EntityRelationShipType entityRelationShipType,EntityType entityType, String attributeName) {
		this.entityRelationShipType=entityRelationShipType;
		this.attributeName = attributeName;
		this.entityType = entityType;
	}
	public EntityRelationShipType getEntityRelationShipType() {
		return entityRelationShipType;
	}

	public void setEntityRelationShipType(
			EntityRelationShipType entityRelationShipType) {
		this.entityRelationShipType = entityRelationShipType;
	}


}

// $Id$