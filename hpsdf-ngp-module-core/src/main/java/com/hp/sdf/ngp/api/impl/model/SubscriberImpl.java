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
package com.hp.sdf.ngp.api.impl.model;

import com.hp.sdf.ngp.api.model.Subscriber;
import com.hp.sdf.ngp.model.SubscriberProfile;

public class SubscriberImpl implements Subscriber {
	
//	private final static Log log = LogFactory.getLog(SubscriberImpl.class);
	
	public SubscriberProfile getSubscriberProfile() {
		if(null == subscriberProfile){
			subscriberProfile = new SubscriberProfile();
		}
		return subscriberProfile;
	}

	

	private SubscriberProfile subscriberProfile;
	

	
	
	public SubscriberImpl(){
		subscriberProfile = new SubscriberProfile();
	}
	
	public SubscriberImpl(SubscriberProfile subscriberProfile){
		this.subscriberProfile = subscriberProfile;
		if (null != this.subscriberProfile) {
			this.subscriberProfile.getDisplayName();//load information to avoid the lazy load
		}
	}

	public String getDisplayName() {
		return subscriberProfile.getDisplayName();
	}

	public String getMsisdn() {
		return subscriberProfile.getMsisdn();
	}

//	public String getSecurityToken() {
//		return subscriberProfile.getSecurityToken();
//	}

//	public String getSecurityTokenCreateDate() {
//		Date date = subscriberProfile.getTokenCreateDate();
//		if(null != date){
//			new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
//		}
//		return null;
//	}

	public String getUserId() {
		return subscriberProfile.getUserId();
	}

	public void setDisplayName(String displayName) {
//		log.debug("displayName:"+displayName);
		subscriberProfile.setDisplayName(displayName);
	}

	public void setMsisdn(String msisdn) {
//		log.debug("msisdn:"+msisdn);
		subscriberProfile.setMsisdn(msisdn);
	}

//	public void setSecurityToken(String securityToken) {
//		subscriberProfile.setSecurityToken(securityToken);
//	}

//	public void setSecurityTokenCreateDate(Date securityTokenCreateDate) {
//		subscriberProfile.setTokenCreateDate(securityTokenCreateDate);
//	}

	public void setUserId(String userId) {
//		log.debug("userId:"+userId);
		subscriberProfile.setUserId(userId);
	}

	

//	public boolean isTesterClient() {
//		Long flag = subscriberProfile.getTestClientFlag();
//		if(null != flag && flag == 1){
//			return true;
//		}
//		return false;
//	}

//	public void setTesterClient() {
//		// TODO Auto-generated method stub
//	}

	public Long getClientOwnerProviderId() {
		Long id = subscriberProfile.getClientOwnerProviderId();
		return id;
		
	}

//	public long getId() {
//		// TODO Auto-generated method stub
//		throw new RuntimeException();
//	}

	public boolean isClientTester() {
		Long flag = subscriberProfile.getClientTesterFlag();
		if(null != flag && flag == 1){
			return true;
		}
		return false;
	}

	public void setClientOwnerProviderId(Long providerId) {
//		log.debug("clientOwnerProviderId:"+providerId);
		subscriberProfile.setClientOwnerProviderId(providerId);
	}

	public void setClientTester() {
		subscriberProfile.setClientTesterFlag(1L);
	}

	public String getOwnerTesterUserId() {
		return subscriberProfile.getOwnerTesterUserId();
	}

	public void setOwnerTesterUserId(String ownerTesterUserId) {
//		log.debug("ownerTesterUserId:"+ownerTesterUserId);
		subscriberProfile.setOwnerTesterUserId(ownerTesterUserId);
	}

	public void setClientTester(boolean testerFlag) {
		if(testerFlag)
			subscriberProfile.setClientTesterFlag(1L);
		else{
			subscriberProfile.setClientTesterFlag(0L);
		}
	}

	@Override
	public String toString(){
		return "SubscriberImpl[displayName="+getDisplayName()+",msisdn="+getMsisdn()+",userId="+getUserId()+",providerId="
		+getClientOwnerProviderId()	+",ownerTesterUserId="+getOwnerTesterUserId()+"]";
	}
}

// $Id$