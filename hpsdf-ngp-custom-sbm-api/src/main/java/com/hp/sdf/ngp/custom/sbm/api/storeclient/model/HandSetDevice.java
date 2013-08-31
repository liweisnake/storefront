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

import java.io.Serializable;
import java.util.List;

public interface HandSetDevice extends Serializable{

	public String getDisplayName();
	public void setDisplayName(String displayName);
	
	public String getDeviceName();
	public void setDeviceName(String deviceName);
	
	public String getFunctionFilter();
	public void setFunctionFilter(String functionFilter);
	
	public Long getResolutionFilter();
	public void setResolutionFilter(Long resolutionFilter);
	
	public List<String> getMimeType();
	public void addMimeType(String mimeType);
	
}
