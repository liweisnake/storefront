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
package com.hp.sdf.ngp.eav.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.hp.sdf.ngp.eav.AttributeType;

@javax.persistence.Entity
@Table
public class EntityAttribute implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Long id;
	private Entity entity;
	private Attribute attribute;
	private Long attributeValueID;
	private Object attributeValueObject;
	private AttributeType attributeType;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ENTITYID")
	public Entity getEntity() {
		return entity;
	}

	public void setEntity(Entity entity) {
		this.entity = entity;
	}

	@Column(nullable = false)
	public Long getAttributeValueID() {
		return attributeValueID;
	}

	public void setAttributeValueID(Long attributeValueID) {
		this.attributeValueID = attributeValueID;
	}

	@Column(nullable = false,length = 100)
	public AttributeType getAttributeType() {
		return attributeType;
	}

	public void setAttributeType(AttributeType attributeType) {
		this.attributeType = attributeType;
	}

	@Transient
	public Object getAttributeValueObject() {
		return attributeValueObject;
	}

	public void setAttributeValueObject(Object attributeValueObject) {
		this.attributeValueObject = attributeValueObject;
	}

	@Transient
	public Attribute getAttribute() {
		return attribute;
	}

	public void setAttribute(Attribute attribute) {
		this.attribute = attribute;
	}

}

// $Id$