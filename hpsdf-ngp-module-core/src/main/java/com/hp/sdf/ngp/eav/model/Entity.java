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
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

@javax.persistence.Entity
@Table
public class Entity implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long entityID;

	private String name;
	private String description;
	/**
	 * This entity attribute is only used for relationship definition, not used
	 * in entity object
	 */
	private List<EntityAttribute> attributes;
	private Integer entityType;

	public Entity() {
	}

	public Entity(Long entityID) {
		this.entityID = entityID;
	}

	public Entity(Object[] properties) {
		if (properties == null || properties.length != 2)
			return;
		this.entityID = Long.valueOf(properties[0].toString());
		this.entityType = new Integer(properties[1].toString());
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Long getEntityID() {
		return entityID;
	}

	public void setEntityID(Long entityID) {
		this.entityID = entityID;
	}

	@Transient
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Transient
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@OneToMany(cascade = CascadeType.REMOVE, fetch = FetchType.LAZY, mappedBy = "entity")
	protected List<EntityAttribute> getAttributes() {
		return attributes;
	}

	protected void setAttributes(List<EntityAttribute> attributes) {
		this.attributes = attributes;
	}

	@Column(nullable = false)
	public Integer getEntityType() {
		return entityType;
	}

	public void setEntityType(Integer entityType) {
		this.entityType = entityType;
	}

}

// $Id$