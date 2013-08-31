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
package com.hp.sdf.ngp.manager;

import java.math.BigDecimal;

public class DownloadDescriptor {
	
	private String type;
	
	private BigDecimal size;
	
	private String objectURI;
	
	private String installNotifyURI;
	
	private String nextURL;
	
	private String DDVersion;
	
	private String name;
	
	private String description;
	
	private String vendor;
	
	private String infoURL;
	
	private String iconURI;
	
	private String installParam;
	
	public DownloadDescriptor(String type, BigDecimal size, String objectURI){
		this.type = type;
		this.size = size;
		this.objectURI = objectURI;
	}
	
	public String toXML(){
		StringBuffer dd = new StringBuffer();
		dd.append("<media xmlns=\"http://www.openmobilealliance.org/xmlns/dd\">");
		dd.append("<type>");
		dd.append(this.getType());
		dd.append("</type>");
		dd.append("<size>");
		dd.append(this.getSize());
		dd.append("</size>");
		dd.append("<objectURI>");
		dd.append(this.getObjectURI());
		dd.append("</objectURI>");
		if(this.getInstallNotifyURI() != null){
			dd.append("<installNotifyURI>");
			dd.append(this.getInstallNotifyURI());
			dd.append("</installNotifyURI>");
		}
		if(this.getNextURL() != null){
			dd.append("<nextURL>");
			dd.append(this.getNextURL());
			dd.append("</nextURL>");
		}
		if(this.getDDVersion() != null){
			dd.append("<DDVersion>");
			dd.append(this.getDDVersion());
			dd.append("</DDVersion>");
		}
		if(this.getName() != null){
			dd.append("<name>");
			dd.append(this.getName());
			dd.append("</name>");
		}
		if(this.getDescription() != null){
			dd.append("<description>");
			dd.append(this.getDescription());
			dd.append("</description>");
		}
		if(this.getVendor() != null){
			dd.append("<vendor>");
			dd.append(this.getVendor());
			dd.append("</vendor>");
		}
		if(this.getInfoURL() != null){
			dd.append("<infoURL>");
			dd.append(this.getInfoURL());
			dd.append("</infoURL>");
		}
		if(this.getIconURI() != null){
			dd.append("<iconURI>");
			dd.append(this.getIconURI());
			dd.append("</iconURI>");
		}
		if(this.getInstallParam() != null){
			dd.append("<installParam>");
			dd.append(this.getInstallParam());
			dd.append("</installParam>");
		}
		dd.append("</media>");
		return dd.toString();
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public BigDecimal getSize() {
		return size;
	}

	public void setSize(BigDecimal size) {
		this.size = size;
	}

	public String getObjectURI() {
		return objectURI;
	}

	public void setObjectURI(String objectURI) {
		this.objectURI = objectURI;
	}

	public String getInstallNotifyURI() {
		return installNotifyURI;
	}

	public void setInstallNotifyURI(String installNotifyURI) {
		this.installNotifyURI = installNotifyURI;
	}

	public String getNextURL() {
		return nextURL;
	}

	public void setNextURL(String nextURL) {
		this.nextURL = nextURL;
	}

	public String getDDVersion() {
		return DDVersion;
	}

	public void setDDVersion(String version) {
		DDVersion = version;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getVendor() {
		return vendor;
	}

	public void setVendor(String vendor) {
		this.vendor = vendor;
	}

	public String getInfoURL() {
		return infoURL;
	}

	public void setInfoURL(String infoURL) {
		this.infoURL = infoURL;
	}

	public String getIconURI() {
		return iconURI;
	}

	public void setIconURI(String iconURI) {
		this.iconURI = iconURI;
	}

	public String getInstallParam() {
		return installParam;
	}

	public void setInstallParam(String installParam) {
		this.installParam = installParam;
	}

}

// $Id$