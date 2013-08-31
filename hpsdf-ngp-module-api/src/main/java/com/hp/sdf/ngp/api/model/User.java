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

import javax.security.auth.callback.LanguageCallback;

public interface User extends AttributeContainer {

	public String getUserid();
	
	public void setUserid(String userid);
	
	/**
	 * Returns the country and language information of a user.
	 * @return
	 */
	public Locale getLocale();
	
	public void setLocale(Locale locale);
	
	public String getPassword();
	
	public void setPassword(String password);
	
	public String getEmail();
	
	public void setEmail(String email);
	
	public String getIdcard();
	
	/**
	 * Sets the user's identifier number, or security number in some country.
	 * @param idcard
	 */
	public void setIdNumber(String idcard);
	
	public String getGender();
	
	public void setGender(String gender);
	
	public String getCellphone();
	
	public void setCellphone(String cellphone);
	
	public String getCompany();
	
	public void setCompany(String company);
	
	public String getAddress();
	
	public void setAddress(String address);
	
	public String getZip();
	
	public void setZip(String zip);
	
	public Date getBirthday();
	
	public void setBirthday(Date birthday);
	
	public String getFirstname();
	
	public void setFirstname(String firstname);
	
	public String getLastname();
	
	public void setLastname(String lastname);
	
	public String getVerificationCode();
	
	public void setVerificationCode(String verificationCode);
	
	public Date getCreateDate();

}

// $Id$