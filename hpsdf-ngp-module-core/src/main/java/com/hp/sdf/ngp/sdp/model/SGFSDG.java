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

import java.util.List;

public class SGFSDG extends SGFBase {
	
	private String code;
	
	private SGFPartner partner;
	
	private List<SGFService> services;

	public List<SGFService> getServices() {
		return services;
	}

	public SGFPartner getPartner() {
		return partner;
	}

	public void setPartner(SGFPartner partner) {
		this.partner = partner;
	}

	public void setServices(List<SGFService> services) {
		this.services = services;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

}
