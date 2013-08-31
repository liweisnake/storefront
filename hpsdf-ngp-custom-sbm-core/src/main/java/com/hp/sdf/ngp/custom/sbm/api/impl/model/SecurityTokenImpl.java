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
package com.hp.sdf.ngp.custom.sbm.api.impl.model;

import java.io.Serializable;
import java.util.Date;

import com.hp.sdf.ngp.custom.sbm.api.storeclient.model.SecurityToken;

public class SecurityTokenImpl implements SecurityToken, Serializable {

	private static final long serialVersionUID = -505352221910265348L;

	// private final static Log log =
	// LogFactory.getLog(SecurityTokenImpl.class);

	public com.hp.sdf.ngp.custom.sbm.storeclient.model.SecurityToken getSecurityToken() {
		if (null == securityToken) {
			securityToken = new com.hp.sdf.ngp.custom.sbm.storeclient.model.SecurityToken();
		}
		return securityToken;
	}

	private com.hp.sdf.ngp.custom.sbm.storeclient.model.SecurityToken securityToken;

	public SecurityTokenImpl() {
		securityToken = new com.hp.sdf.ngp.custom.sbm.storeclient.model.SecurityToken();
	}

	public SecurityTokenImpl(com.hp.sdf.ngp.custom.sbm.storeclient.model.SecurityToken securityToken) {
		this.securityToken = securityToken;

		if (null != this.securityToken) {
			this.securityToken.getExpireTime();// load information to avoid the
												// lazy load
		}
	}

	// public Date getExpireDate() {
	// return securityToken.getExpireTime();
	// }

	public String getMsisdn() {
		return securityToken.getMsisdn();
	}

	public String getToken() {
		return securityToken.getToken();
	}

	public void setExpireTime(Date expireTime) {
		// log.debug("expireDate:" + expireTime);
		securityToken.setExpireTime(expireTime);
	}

	public void setMsisdn(String Msisdn) {
		// log.debug("msisdn:" + Msisdn);
		securityToken.setMsisdn(Msisdn);
	}

	public void setToken(String token) {
		// log.debug("token:" + token);
		securityToken.setToken(token);
	}

	public int getLockFlag() {
		return securityToken.getLockFlag();
	}

	public void setLockFlag(int lockFlag) {
		// log.debug("lockFlag:" + lockFlag);
		securityToken.setLockFlag(lockFlag);

	}

	public Date getExpireTime() {
		return securityToken.getExpireTime();
	}

	public String getProvideId() {
		return securityToken.getProvideId();
	}

	public int getTestClientFlag() {
		Integer flag = securityToken.getTestClientFlag();
		if (null != flag) {
			return flag;
		}
		return 0;
		// throw new RuntimeException();
	}

	// public String getUid() {
	// return securityToken.getUserId();
	// }

	public void setProvideId(String provideId) {
		// log.debug("provideId:"+provideId);
		securityToken.setProvideId(provideId);
	}

	public void setTestClientFlag(int testClientFlag) {
		// log.debug("testClientFlag:"+testClientFlag);
		securityToken.setTestClientFlag(testClientFlag);
	}

	// public void setUid(String uid) {
	// securityToken.setUserId(uid);
	// }

	public String getUserId() {
		return securityToken.getUserId();
	}

	public void setUserId(String userId) {
		// log.debug("userId:"+userId);
		securityToken.setUserId(userId);
	}

	public String getImsi() {
		return securityToken.getImsi();
	}

	public void setImsi(String imsi) {
		// log.debug("imsi:"+imsi);
		securityToken.setImsi(imsi);
	}

	@Override
	public String toString() {
		return "SecurityToken[:expireTime=" + getExpireTime() + ",msisdn=" + getMsisdn() + ",token=" + getToken() + ",lockFlag=" + getLockFlag()
				+ ",provideId=" + getProvideId() + ",testClientFlag=" + getTestClientFlag() + ",userId=" + getUserId() + ",imsi+" + getImsi() + "]";
	}
}

// $Id$