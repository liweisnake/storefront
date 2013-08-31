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
@Entity
@Table
public class ClientApplication implements Serializable {
	private static final long serialVersionUID = 1358105129208652474L;
	
	private Long id;
	private String clientName;
	private String version;
	private String fileLocation;
	private Date updateDate;
	
	private Set<ClientSettingAppRelation> clientSettingAppRelations=new HashSet<ClientSettingAppRelation>(0);
	private Set<ClientAppHandSetRelation> clientAppHandSetRelationS=new HashSet<ClientAppHandSetRelation>(0);
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	@Column
	public String getClientName() {
		return clientName;
	}
	
	public void setClientName(String clientName) {
		this.clientName = clientName;
	}
	
	@Column
	public String getVersion() {
		return version;
	}
	
	public void setVersion(String version) {
		this.version = version;
	}
	
	@Column
	public String getFileLocation() {
		return fileLocation;
	}
	
	public void setFileLocation(String fileLocation) {
		this.fileLocation = fileLocation;
	}
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column
	public Date getUpdateDate() {
		return updateDate;
	}
	
	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "clientApplication")
	public Set<ClientSettingAppRelation> getClientSettingAppRelations() {
		return clientSettingAppRelations;
	}

	public void setClientSettingAppRelations(
			Set<ClientSettingAppRelation> clientSettingAppRelations) {
		this.clientSettingAppRelations = clientSettingAppRelations;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "clientApplication")
	public Set<ClientAppHandSetRelation> getClientAppHandSetRelationS() {
		return clientAppHandSetRelationS;
	}

	public void setClientAppHandSetRelationS(
			Set<ClientAppHandSetRelation> clientAppHandSetRelationS) {
		this.clientAppHandSetRelationS = clientAppHandSetRelationS;
	}
	
	
	
}

// $Id$