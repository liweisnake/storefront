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

import com.hp.sdf.ngp.api.model.AssetProvider;
import com.hp.sdf.ngp.model.Provider;

public class AssetProviderImpl implements AssetProvider {

	public void setObjects(Map<String, List<Object>> objects) {
		this.objects = objects;
	}

	public Provider getAssetProvider() {
		if(null == assetProvider){
			assetProvider = new Provider();
		}
		return assetProvider;
	}

	private Provider assetProvider;
	
	private Map<String, List<Object>> objects;
	
	public AssetProviderImpl() {
		assetProvider = new Provider();
	}

	public AssetProviderImpl(Provider assetProvider) {
		this.assetProvider = assetProvider;
		
		if (null != this.assetProvider) {
			this.assetProvider.getEmail();//load information to avoid the lazy load
		}
	}
	
	public String getCity() {
		return assetProvider.getCity();
	}

	public Double getCommissionRate() {
		return assetProvider.getCommissionRate();
	}

	public Date getContractExpireDate() {
		return assetProvider.getContractExpireDate();
	}

	public String getCountry() {
		return assetProvider.getCountry();
	}

	public String getEmail() {
		return assetProvider.getEmail();
	}

	public String getExternalId() {
		return assetProvider.getExternalId();
	}

	public String getHomePage() {
		return assetProvider.getHomePage();
	}

	public Long getId() {
			return assetProvider.getId();

	}

	public Locale getLocale() {
		String locale = assetProvider.getLocale();
		if(null != locale){
			return new Locale(locale);
		}
		return null;
	}

	public String getName() {
		return assetProvider.getName();
	}

	public String getOrganization() {
		return assetProvider.getOrganization();
	}

	public String getPhone() {
		return assetProvider.getPhone();
	}

	public String getSource() {
		return assetProvider.getSource();
	}

	public String getStreetAddress() {
		return assetProvider.getStreetAddress();
	}

	public void setCity(String city) {
//		log.debug("city:"+city);
		assetProvider.setCity(city);
	}

	public void setCommissionRate(Double rate) {
//		log.debug("comissionRate:"+rate);
		assetProvider.setCommissionRate(rate);
	}

	public void setContractExpireDate(Date contractExpireDate) {
//		log.debug("contractExpireDate:"+contractExpireDate);
		assetProvider.setContractExpireDate(contractExpireDate);
	}

	public void setCountry(String country) {
//		log.debug("country:"+country);
		assetProvider.setCountry(country);
	}

	public void setEmail(String email) {
//		log.debug("email:"+email);
		assetProvider.setEmail(email);
	}

	public void setExternalId(String externalId) {
//		log.debug("externalId:"+externalId);
		assetProvider.setExternalId(externalId);
	}

	public void setHomePage(String homePage) {
//		log.debug("homePage:"+homePage);
		assetProvider.setHomePage(homePage);
	}

	public void setLocale(Locale l) {
//		log.debug("locale:" + l.getDisplayName());
		assetProvider.setLocale(l.toString());
	}

	public void setName(String name) {
//		log.debug("name:" + name);
		assetProvider.setName(name);
	}

	public void setOrganization(String name) {
//		log.debug("organization:" + name);
		assetProvider.setOrganization(name);
	}

	public void setPhone(String phone) {
//		log.debug("phone:"+phone);
		assetProvider.setPhone(phone);
	}

	public void setSource(String source) {
//		log.debug("source:"+source);
		assetProvider.setSource(source);
	}

	public void setStreetAddress(String address) {
//		log.debug("streetAddress:"+address);
		assetProvider.setStreetAddress(address);
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

	public Date getContractStartDate() {
		return assetProvider.getContractStartDate();
	}

	public void setContractStartDate(Date contractExpireDate) {
//		log.debug("setContractStartDate:"+contractExpireDate);
		assetProvider.setContractStartDate(contractExpireDate);
	}
	
	@Override
	public String toString(){						
		Locale locale = getLocale();
		String displayName = null;
		if(null != locale){
			displayName = locale.getDisplayName();
		}
		
		return "AssetProvider[city="+getCity()+",comissionRate="+getCommissionRate()+",contractExpireDate="+getContractExpireDate()+",country="+getCountry()
		+",email="+getEmail()+",externalId="+getExternalId()+",homePage="+getHomePage()+",locale="+displayName+",name="+getName()
		+",organization="+getOrganization()+",phone="+getPhone()+",source="+getSource()+",streetAddress="+getStreetAddress()+",contractExpireDate="+getContractStartDate()+"]";
	}

}

// $Id$