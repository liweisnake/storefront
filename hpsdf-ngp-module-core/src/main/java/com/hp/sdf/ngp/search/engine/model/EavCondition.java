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
package com.hp.sdf.ngp.search.engine.model;

import java.util.HashMap;
import java.util.Map;

import com.hp.sdf.ngp.api.comon.EntityRelationShipType;
import com.hp.sdf.ngp.api.comon.EntityType;
import com.hp.sdf.ngp.search.engine.model.JoinMetaData.JoinPurpose;

public abstract class EavCondition extends BaseCondition {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3842353805355464383L;

	private static Map<EntityRelationShipType, String> entityRelationShipTypes = new HashMap<EntityRelationShipType, String>();

	static {
		entityRelationShipTypes.put(EntityRelationShipType.PARENTASSETVERSIONSUMMARY_BINARYVERSION, "binaryVersions");

	}

	private EntityType entityType;

	private String attributeName;

	private EntityRelationShipType entityRelationShipType;

	

	

	public EavCondition(EntityRelationShipType entityRelationShipType, EntityType entityType, String attributeName) {
		super();
		this.entityRelationShipType = entityRelationShipType;
		this.conditionType = ConditionType.eav;
		this.purpose = JoinPurpose.EAV;
		this.entityType = entityType;
		this.attributeName = attributeName;
	}

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

	public EntityRelationShipType getEntityRelationShipType() {
		return entityRelationShipType;
	}

	public void setEntityRelationShipType(EntityRelationShipType entityRelationShipType) {
		this.entityRelationShipType = entityRelationShipType;
	}

	public abstract Object getValue();

	public String getEntityRelationShipTypeName() {
		return entityRelationShipTypes.get(this.entityRelationShipType);
	}

	public static String getEntityRelationShipTypeName(EntityRelationShipType entityRelationShipType) {
		return entityRelationShipTypes.get(entityRelationShipType);
	}

	public abstract String getEavJoinName();

	

	
	
	public abstract BaseCondition createEavPropertyCondition(String eavPropertyName);
	
	
	
	

}

// $Id$