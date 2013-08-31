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

import java.io.Serializable;
import java.util.Date;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Cascade;

@Entity
@Table
public class HandSetDevice implements Serializable {

	private static final long serialVersionUID = 2144137709510542380L;

	private Long id;
	private String displayName;
	private String deviceName;
	private Date createTime=new Date();
	private String functionFilter;
	private Long resolutionFilter;

	private Set<MimeType> mimeTypes = new HashSet<MimeType>(0);

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column
	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	@Column
	public String getDeviceName() {
		return deviceName;
	}

	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}

	@Column
	public String getFunctionFilter() {
		return functionFilter;
	}

	public void setFunctionFilter(String functionFilter) {
		this.functionFilter = functionFilter;
	}

	@Column
	public Long getResolutionFilter() {
		return resolutionFilter;
	}

	public void setResolutionFilter(Long resolutionFilter) {
		this.resolutionFilter = resolutionFilter;
	}
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column
	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "handSetDevice")
	@Cascade(org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	public Set<MimeType> getMimeTypes() {
		return mimeTypes;
	}

	public void setMimeTypes(Set<MimeType> mimeTypes) {
		this.mimeTypes = mimeTypes;
	}

}

// $Id$