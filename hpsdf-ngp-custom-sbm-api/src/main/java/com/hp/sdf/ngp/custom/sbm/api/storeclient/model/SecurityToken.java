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
package com.hp.sdf.ngp.custom.sbm.api.storeclient.model;

import java.util.Date;

public interface SecurityToken {

	public String getUserId();
	public void setUserId(String userId);
	
	public String getMsisdn();
	public void setMsisdn(String Msisdn);
	
	public String getToken();
	public void setToken(String token);
	
	public Date getExpireTime();
	public void setExpireTime(Date expireTime);
	
	public int getLockFlag();
	public void setLockFlag(int lockFlag);
	
	public int getTestClientFlag();
	public void setTestClientFlag(int testClientFlag);
	
	public String getProvideId();
	public void setProvideId(String provideId);
	
	public String getImsi();
	public void setImsi(String imsi);
	
}

// $Id$