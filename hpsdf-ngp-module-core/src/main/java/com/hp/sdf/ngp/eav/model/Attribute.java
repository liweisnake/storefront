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
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.hp.sdf.ngp.eav.AttributeType;

@Entity
@Table
public class Attribute implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
//	public enum AttributeType{
//		number,
//		varchar,
//		date;
//	}

	private Long attributeID;
	private String attributeName;
	private String attributeDesc;
	private AttributeType attributeType;
	private Set<AttributeValueDomain> attributeValueDomain;
	
	public Attribute(){
	}
	
	public Attribute(Long attributeID){
		this.attributeID = attributeID;
	}
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Long getAttributeID() {
		return attributeID;
	}

	public void setAttributeID(Long attributeID) {
		this.attributeID = attributeID;
	}

	@Column(nullable = false,unique = true, length = 100)
	public String getAttributeName() {
		return attributeName;
	}

	public void setAttributeName(String attributeName) {
		this.attributeName = attributeName;
	}

	@Column(length = 225)
	public String getAttributeDesc() {
		return attributeDesc;
	}

	public void setAttributeDesc(String attributeDesc) {
		this.attributeDesc = attributeDesc;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "attribute")
	public Set<AttributeValueDomain> getAttributeValueDomain() {
		return attributeValueDomain;
	}

	public void setAttributeValueDomain(Set<AttributeValueDomain> attributeValueDomain) {
		this.attributeValueDomain = attributeValueDomain;
	}

	@Column(nullable = false, length = 100)
	public AttributeType getAttributeType() {
		return attributeType;
	}

	public void setAttributeType(AttributeType attributeType) {
		this.attributeType = attributeType;
	}
}

// $Id$