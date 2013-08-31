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

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
@Entity
@Table
public class ClientAppHandSetRelation implements Serializable {
	private static final long serialVersionUID = 7314753774811021673L;
	
	private Long id;
	private ClientApplication clientApplication;
	private HandSetDevice handSetDevice;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CLIENTAPPID")
	public ClientApplication getClientApplication() {
		return clientApplication;
	}
	
	public void setClientApplication(ClientApplication clientApplication) {
		this.clientApplication = clientApplication;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "HANDSETDEVICEID")
	public HandSetDevice getHandSetDevice() {
		return handSetDevice;
	}
	
	public void setHandSetDevice(HandSetDevice handSetDevice) {
		this.handSetDevice = handSetDevice;
	}
	
	
	
}

// $Id$