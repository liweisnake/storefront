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

import com.hp.sdf.ngp.api.comon.EntityRelationShipType;

public class EavOrder {

	private  String eavAttributeName;
	
	private  String eavJoinName;

	private EntityRelationShipType entityRelationShipType;
	
	public EavOrder(String eavAttributeName,String eavJoinName,EntityRelationShipType entityRelationShipType)
	{
		this.eavAttributeName=eavAttributeName;
		this.eavJoinName=eavJoinName;
		this.entityRelationShipType=entityRelationShipType;
	}

	public String getEavAttributeName() {
		return eavAttributeName;
	}

	public void setEavAttributeName(String eavAttributeName) {
		this.eavAttributeName = eavAttributeName;
	}

	public String getEavJoinName() {
		return eavJoinName;
	}

	public void setEavJoinName(String eavJoinName) {
		this.eavJoinName = eavJoinName;
	}

	public EntityRelationShipType getEntityRelationShipType() {
		return entityRelationShipType;
	}

	public void setEntityRelationShipType(EntityRelationShipType entityRelationShipType) {
		this.entityRelationShipType = entityRelationShipType;
	}


}

// $Id$