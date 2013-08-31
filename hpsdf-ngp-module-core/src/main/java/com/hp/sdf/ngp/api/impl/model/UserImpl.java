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

import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.hp.sdf.ngp.api.model.User;
import com.hp.sdf.ngp.model.Language;
import com.hp.sdf.ngp.model.UserProfile;

public class UserImpl implements User {
	
	private Map<String, List<Object>> objects;

	public Map<String, List<Object>> getObjects() {
		return objects;
	}

	public void setObjects(Map<String, List<Object>> objects) {
		this.objects = objects;
	}
	public List<Object> getAttributeValue(String attributeName) {
		if(null != objects){
			return objects.get(attributeName);
		}
		return null;
	}

	public Map<String, List<Object>> getAttributes() {
		return objects;
	}
	public UserProfile getUserProfile() {
		if(null == userProfile){
			return new UserProfile();
		}
		return userProfile;
	}

	private UserProfile userProfile;
	
	public UserImpl(){
		userProfile = new UserProfile();
	}
	
	public UserImpl(UserProfile userProfile){
		this.userProfile = userProfile;
		if (null != this.userProfile) {
			this.userProfile.getAddress();//load information to avoid the lazy load
		}
	}

	public String getAddress() {
		return userProfile.getAddress();
	}

	public Date getBirthday() {
		return userProfile.getBirthday();
	}

	public String getCellphone() {
		return userProfile.getCellphone();
	}

	public String getCompany() {
		return userProfile.getCompany();
	}

	public Date getCreateDate() {
		return userProfile.getCreateDate();
	}

	public String getEmail() {
		return userProfile.getEmail();
	}

	public String getFirstname() {
		return userProfile.getFirstname();
	}

	public String getGender() {
		return userProfile.getGender();
	}

	public String getIdcard() {
		return userProfile.getIdcard();
	}

	public String getLastname() {
		return userProfile.getLastname();
	}

	public Locale getLocale() {
		Language language = userProfile.getLanguage();
		if(null != language){
			return new Locale(language.getLocale());
		}
		return null;
	}

	public String getPassword() {
		return userProfile.getPassword();
	}

	public String getUserid() {
		return userProfile.getUserid();
	}

	public String getVerificationCode() {
		return userProfile.getVerificationCode();
	}

	public String getZip() {
		return userProfile.getZip();
	}

	public void setAddress(String address) {
//		log.debug("address:"+address);
		userProfile.setAddress(address);
	}

	public void setBirthday(Date birthday) {
//		log.debug("birthday:"+birthday);
		userProfile.setBirthday(birthday);
	}

	public void setCellphone(String cellphone) {
//		log.debug("cellphone:"+cellphone);
		userProfile.setCellphone(cellphone);
	}

	public void setCompany(String company) {
//		log.debug("company:"+company);
		userProfile.setCompany(company);
	}

	public void setEmail(String email) {
//		log.debug("email:"+email);
		userProfile.setEmail(email);
	}

	public void setFirstname(String firstname) {
//		log.debug("firstname:"+firstname);
		userProfile.setFirstname(firstname);
	}

	public void setGender(String gender) {
//		log.debug("gender:"+gender);
		userProfile.setGender(gender);
	}

	public void setIdNumber(String idcard) {
//		log.debug("idcard:"+idcard);
		userProfile.setIdcard(idcard);
	}

	public void setLastname(String lastname) {
//		log.debug("lastname:"+lastname);
		userProfile.setLastname(lastname);
	}

	public void setLocale(Locale locale) {
//		log.debug("locale:"+locale);
		Language language = new Language();
		language.setLocale(locale.getLanguage());
		userProfile.setLanguage(language);
	}

	public void setPassword(String password) {
//		log.debug("password:"+password);
		userProfile.setPassword(password);
	}

	public void setUserid(String userid) {
//		log.debug("userid:"+userid);
		userProfile.setUserid(userid);
	}

	public void setVerificationCode(String verificationCode) {
//		log.debug("verificationCode:"+verificationCode);
		userProfile.setVerificationCode(verificationCode);
	}

	public void setZip(String zip) {
//		log.debug("zip:"+zip);
		userProfile.setZip(zip);
	}
	
	@Override
	public String toString(){
		Locale locale = getLocale();
		String displayName = null;
		if(null != locale){
			displayName = locale.getDisplayName();
		}
		return "User[birthday="+getBirthday()+",cellphone="+getCellphone()+",company="+getCompany()+",email="+getEmail()+",firstname="+getFirstname()
		+",gender="+getGender()+",idcard="+getIdcard()+",address="+getAddress()+",lastName="+getLastname()+",locale="+displayName
		+",password="+getPassword()+",userid="+getUserid()+",verificationCode="+getVerificationCode()+",zip="+getZip()+"]";
	}
}

// $Id$