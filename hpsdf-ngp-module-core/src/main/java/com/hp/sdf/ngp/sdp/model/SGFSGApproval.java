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
package com.hp.sdf.ngp.sdp.model;

import java.io.Serializable;

public class SGFSGApproval implements Serializable {
	
	private String cosCategory;
	
	private String wlngAppType;
	
	private String wlngShortCode;
	
	private String wlngUsername;
	
	private String wlngPassword;
	
	private String wlngPrefix;

	public String getWlngAppType() {
		return wlngAppType;
	}

	public void setWlngAppType(String wlngAppType) {
		this.wlngAppType = wlngAppType;
	}

	public String getWlngShortCode() {
		return wlngShortCode;
	}

	public void setWlngShortCode(String wlngShortCode) {
		this.wlngShortCode = wlngShortCode;
	}

	public String getWlngUsername() {
		return wlngUsername;
	}

	public void setWlngUsername(String wlngUsername) {
		this.wlngUsername = wlngUsername;
	}

	public String getWlngPassword() {
		return wlngPassword;
	}

	public void setWlngPassword(String wlngPassword) {
		this.wlngPassword = wlngPassword;
	}

	public String getWlngPrefix() {
		return wlngPrefix;
	}

	public void setWlngPrefix(String wlngPrefix) {
		this.wlngPrefix = wlngPrefix;
	}

	public String getCosCategory() {
		return cosCategory;
	}

	public void setCosCategory(String cosCategory) {
		this.cosCategory = cosCategory;
	}

}
