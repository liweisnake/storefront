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
package com.hp.sdf.ngp.eav;

import java.util.List;

import com.hp.sdf.ngp.eav.model.Attribute;
import com.hp.sdf.ngp.eav.model.AttributeValue;
import com.hp.sdf.ngp.eav.model.Entity;
import com.hp.sdf.ngp.eav.model.EntityAttribute;

public interface EavRepository extends java.io.Serializable{
	
	public void addEntity(Entity entity);
	
	public void deleteEntity(Long entityID);
	
	public Entity getEntityByID(Long entityID);
	
	public void addEntityAttribute(List<EntityAttribute> attributes);
	
	public Long addEntityAttribute(Long entityID, Long attributeID, AttributeType attributeType, Object value);
	
	public Long addEntityAttribute(Long entityID, String attributeName, AttributeType attributeType, Object value);
	
	public void updateEntityAttribute(List<EntityAttribute> attributes);
	
	public void updateEntityAttribute(Long entityAttributeID, Object value);
	
	public void updateEntityAttribute(Long attributeValueID, AttributeType attributeType, Object value);
	
	public void deleteEntityAttribute(Long entityAttributeID);
	
	public void deleteEntityAttribute(Long entityID, String attributeName);
	
	@SuppressWarnings("unchecked")
	public List<AttributeValue> getEntityAttributes(Long entityID, Long attributeID);
	
	@SuppressWarnings("unchecked")
	public List<AttributeValue> getEntityAttributes(Long entityID, String attributeName);
	
	@SuppressWarnings("unchecked")
	public List<AttributeValue> getEntityAttributes(Long entityID);
	
	public void addAttributeDefine(Attribute attribute);
	
	public void updateAttributeDefine(Attribute attribute);
	
	public void deleteAttributeDefine(Long attributeID);
	
	public Long addAttributeValueDomain(Long attributeID, Object value, boolean isDefault);
	
	public void deleteAttributeValueDomain(Long attributeValueDomainID);
	
	public Attribute getAttributeDefineByID(Long attributeID, boolean loadDomain);
	
	public Attribute getAttributeDefineByName(String attributeName, boolean loadDomain);
	
	public boolean existsAttributeDefine(String name);
	
	

}

// $Id$