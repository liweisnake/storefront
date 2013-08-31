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
package com.hp.sdf.ngp.api.model;

import java.util.Date;


/**
 * Subscriber information
 *
 */
public interface Subscriber {
	
	public String getUserId();
	public void setUserId(String userId);
	
	public String getMsisdn();
	public void setMsisdn(String msisdn);
	
	public String getDisplayName();
	public void setDisplayName(String displayName);
	
	public boolean isClientTester();
	public void setClientTester(boolean testerFlag);
	
	public Long getClientOwnerProviderId();
	public void setClientOwnerProviderId(Long providerId);
	
	/**
	 * Gets the user ID of the tester which this subcriber client belongs to.
	 * 
	 * @return user ID of the tester, null if the client is not a tester client.
	 */
	public String getOwnerTesterUserId();
	public void setOwnerTesterUserId(String ownerTesterUserId); 
}
