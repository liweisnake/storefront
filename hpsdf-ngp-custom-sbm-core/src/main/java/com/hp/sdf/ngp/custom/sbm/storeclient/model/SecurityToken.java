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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table
public class SecurityToken implements Serializable{
	
	private static final long serialVersionUID = -721561077118665432L;
	
	private String userId;
	private String msisdn;
	private String token;
	
	private Date expireTime;
	private int lockFlag=0;
	private String imsi;
	private String provideId;
	private Integer testClientFlag;
	
	/** default constructor */
	public SecurityToken() {
	}
	
	
	@Id
	@Column
	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
	
	@Column
	public String getUserId() {
		return userId;
	}


	public void setUserId(String userId) {
		this.userId = userId;
	}

	@Column
	public String getImsi() {
		return imsi;
	}


	public void setImsi(String imsi) {
		this.imsi = imsi;
	}


	@Column
	public String getMsisdn() {
		return msisdn;
	}

	public void setMsisdn(String msisdn) {
		this.msisdn = msisdn;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column
	public Date getExpireTime() {
		return expireTime;
	}

	public void setExpireTime(Date expireTime) {
		this.expireTime = expireTime;
	}

	@Column
	public int getLockFlag() {
		return lockFlag;
	}

	public void setLockFlag(int lockFlag) {
		this.lockFlag = lockFlag;
	}


	@Column
	public String getProvideId() {
		return provideId;
	}


	public void setProvideId(String provideId) {
		this.provideId = provideId;
	}

	@Column
	public Integer getTestClientFlag() {
		return testClientFlag;
	}

	public void setTestClientFlag(Integer testClientFlag) {
		this.testClientFlag = testClientFlag;
	}
}

// $Id$