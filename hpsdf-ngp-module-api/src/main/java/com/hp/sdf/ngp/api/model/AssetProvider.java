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
import java.util.Locale;

/**
 * The content provider information
 *
 */
public interface AssetProvider extends AttributeContainer {
	
	public Long getId();
	
	public String getExternalId();
	public void setExternalId(String externalId);
	
	public String getSource();
	public void setSource(String source);
	
	public void setLocale(Locale l);
	public Locale getLocale();
	
	public void setName(String name);
	public String getName();
	
	public void setOrganization(String name);
	public String getOrganization();
	
	public void setStreetAddress(String address);
	public String getStreetAddress();
	
	public void setCity(String city);
	public String getCity();
	
	public void setCountry(String country);
	public String getCountry();
	
	public void setPhone(String phone);
	public String getPhone();
	
	public void setEmail(String email);
	public String getEmail();
	
	public void setCommissionRate(Double rate);
	public Double getCommissionRate();
	
	public void setHomePage(String homePage);
	public String getHomePage();
	
	public void setContractExpireDate(Date contractExpireDate);
	public Date getContractExpireDate();
	
	public void setContractStartDate(Date contractExpireDate);
	public Date getContractStartDate();
	
}
