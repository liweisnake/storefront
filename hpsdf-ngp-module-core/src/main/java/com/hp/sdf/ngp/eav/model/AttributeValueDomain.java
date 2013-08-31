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
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table
public class AttributeValueDomain implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Long id;
	private Attribute attribute;
	private Long attributeValueID;
	private Object attributeValueObject;
	private boolean isDefault;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(nullable = false)
	public Long getAttributeValueID() {
		return attributeValueID;
	}

	public void setAttributeValueID(Long attributeValueID) {
		this.attributeValueID = attributeValueID;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ATTRIBUTEID")
	public Attribute getAttribute() {
		return attribute;
	}

	public void setAttribute(Attribute attribute) {
		this.attribute = attribute;
	}

	@Column(nullable = false)
	public boolean getIsDefault() {
		return isDefault;
	}

	public void setIsDefault(boolean isDefault) {
		this.isDefault = isDefault;
	}
	
	@Transient
	public Object getAttributeValueObject() {
		return attributeValueObject;
	}

	public void setAttributeValueObject(Object attributeValueObject) {
		this.attributeValueObject = attributeValueObject;
	}

}

// $Id$