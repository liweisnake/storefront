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
package com.hp.sdf.ngp.custom.sbm.storeclient.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table
public class DeviceFunction  implements java.io.Serializable {
	
	private static final long serialVersionUID = -3162222241954746700L;
	
	private Long id;
	private String function;
	
	private Set<HandSetFunctionRelation> handSetFunctionRelations=new HashSet<HandSetFunctionRelation>(0);
	
	// Constructors

	/** default constructor */
	public DeviceFunction() {
	}
	
	public DeviceFunction(Long id,String function) {
		this.id=id;
		this.function=function;
	}
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	@Column
	public String getFunction() {
		return function;
	}
	
	public void setFunction(String function) {
		this.function = function;
	}
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "deviceFunction")
	public Set<HandSetFunctionRelation> getHandSetFunctionRelations() {
		return handSetFunctionRelations;
	}

	public void setHandSetFunctionRelations(
			Set<HandSetFunctionRelation> handSetFunctionRelations) {
		this.handSetFunctionRelations = handSetFunctionRelations;
	}
}

// $Id$