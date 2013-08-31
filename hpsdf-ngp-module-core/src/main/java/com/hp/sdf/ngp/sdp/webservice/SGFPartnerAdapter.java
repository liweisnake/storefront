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
package com.hp.sdf.ngp.sdp.webservice;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.axis2.AxisFault;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

import com.hp.sdf.ngp.common.annotation.Value;
import com.hp.sdf.ngp.common.exception.SGFCallingFailureException;
import com.hp.sdf.ngp.sdp.model.SGFPartner;

@Component
public class SGFPartnerAdapter {
	
	public final static Log log = LogFactory.getLog(SGFPartnerAdapter.class);

	private String urlPartner;
	private String sgfDomain;
	private String partnerType;
	private String partnerStatus;
	private String partnerCategory;
	
	public String getPartnerCategory() {
		return partnerCategory;
	}

	@Value("SGF.Partner.Category")
	public void setPartnerCategory(String partnerCategory) {
		this.partnerCategory = partnerCategory;
	}

	public String getPartnerType() {
		return partnerType;
	}

	@Value("SGF.Partner.Type")
	public void setPartnerType(String partnerType) {
		this.partnerType = partnerType;
	}

	public String getPartnerStatus() {
		return partnerStatus;
	}

	@Value("SGF.Partner.Status")
	public void setPartnerStatus(String partnerStatus) {
		this.partnerStatus = partnerStatus;
	}

	public String getUrlPartner() {
		return urlPartner;
	}

	@Value("SGF.WS.url.Partner")
	public void setUrlPartner(String urlPartner) {
		this.urlPartner = urlPartner;
	}
	
	public String getSgfDomain() {
		return sgfDomain;
	}

	@Value("SGF.Partner.Domain")
	public void setSgfDomain(String sgfDomain) {
		this.sgfDomain = sgfDomain;
	}

	/**
	 * Register SGF Partner
	 * @param partner
	 */
	public void registerSGFPartner(SGFPartner partner) throws SGFCallingFailureException{
		log.debug("Register Partner to SGF. Endpoint: " + urlPartner + ". Partner name: " + partner.getLoginInfo().getUsername());
		PartnerServiceStub stub;
		try {
			stub = new PartnerServiceStub(urlPartner);
			PartnerServiceStub.CreatePartner request = new PartnerServiceStub.CreatePartner();
			request.setPartner(this.mappingPartner(partner));
			PartnerServiceStub.CreatePartnerE requestE = new PartnerServiceStub.CreatePartnerE();
			requestE.setCreatePartner(request);
			stub.createPartner(requestE);
			log.debug("Register Partner to SGF. Endpoint: " + urlPartner + " successfully. Partner name: " + partner.getLoginInfo().getUsername());
		} catch (AxisFault e) {
			if(e.getDetail() != null)
				log.error(e.getDetail().toString());
			throw SGFErrorHandler.handleError(e);
		} catch (RemoteException e) {
			throw new SGFCallingFailureException(e.getMessage(),e);
		} catch (SGFException e) {
			throw new SGFCallingFailureException(e.getMessage(),e);
		}
	}
	
	/**
	 * Modify SGF Partner
	 * @param partner
	 */
	public void modifySGFPartner(SGFPartner partner) throws SGFCallingFailureException{
		log.debug("Modify Partner to SGF. Endpoint: " + urlPartner + ". Partner name: " + partner.getLoginInfo().getUsername());
		PartnerServiceStub stub;
		try {
			stub = new PartnerServiceStub(urlPartner);
			PartnerServiceStub.ModifyPartner request = new PartnerServiceStub.ModifyPartner();
			request.setPartner(this.mappingPartner(partner));
			PartnerServiceStub.ModifyPartnerE requestE = new PartnerServiceStub.ModifyPartnerE();
			requestE.setModifyPartner(request);
			stub.modifyPartner(requestE);
			log.debug("Modify Partner to SGF. Endpoint: " + urlPartner + " successfully. Partner name: " + partner.getLoginInfo().getUsername());
		} catch (AxisFault e) {
			if(e.getDetail() != null)
				log.error(e.getDetail().toString());
			throw new SGFCallingFailureException(e.getMessage(),e);
		} catch (RemoteException e) {
			throw new SGFCallingFailureException(e.getMessage(),e);
		} catch (SGFException e) {
			throw new SGFCallingFailureException(e.getMessage(),e);
		}
	}
	
	/**
	 * Get SGF Partner Detail Information
	 * @param name
	 * @return
	 */
	public SGFPartner getPartnerDetails(String name) throws SGFCallingFailureException{
		log.debug("Get Partner details from SGF. Endpoint: " + urlPartner + ". Partner name: " + name);
		SGFPartner result = null;
		PartnerServiceStub stub;
		try {
			stub = new PartnerServiceStub(urlPartner);
			PartnerServiceStub.GetPartnerDetails request = new PartnerServiceStub.GetPartnerDetails();
			request.setUsername(name);
			PartnerServiceStub.GetPartnerDetailsE requestE = new PartnerServiceStub.GetPartnerDetailsE();
			requestE.setGetPartnerDetails(request);
			PartnerServiceStub.GetPartnerDetailsResponseE responseE = stub.getPartnerDetails(requestE);
			PartnerServiceStub.Partner partner = responseE.getGetPartnerDetailsResponse().getPartner();
			result = this.mappingPartner(partner);
			log.debug("Get Partner details from SGF successfully. Endpoint: " + urlPartner + ". Partner name: " + name);
		} catch (AxisFault e) {
			if(e.getDetail() != null)
				log.error(e.getDetail().toString());
			throw new SGFCallingFailureException(e.getMessage(),e);
		} catch (RemoteException e) {
			throw new SGFCallingFailureException(e.getMessage(),e);
		} catch (SGFException e) {
			throw new SGFCallingFailureException(e.getMessage(),e);
		}
		return result;
	}
	
	/**
	 * Mapping Partner data model from NGP to SGF
	 * @param partner
	 * @return
	 */
	private PartnerServiceStub.Partner mappingPartner(SGFPartner partner){
		PartnerServiceStub.Partner result = null;
		if(partner != null)
			result = new PartnerServiceStub.Partner();
		
		partner.setDomainName(this.sgfDomain);
		partner.setPartnerType(this.partnerType);
		partner.setStatus(this.partnerStatus);
		if(this.partnerCategory != null){
			StringTokenizer st = new StringTokenizer(this.partnerCategory);
			List<String> categories = new ArrayList<String>();
			while(st.hasMoreTokens())
				categories.add(st.nextToken());
			partner.setCategories(categories);
		}
		
		PartnerServiceStub.CompanyInfo comanyInfo = new PartnerServiceStub.CompanyInfo();
		PartnerServiceStub.Address address = new PartnerServiceStub.Address();
		address.setCity(partner.getCompanyInfo().getAddress().getCity());
		address.setCountry(new PartnerServiceStub.Country(partner.getCompanyInfo().getAddress().getCountry(),true));
		address.setState(partner.getCompanyInfo().getAddress().getState());
		address.setStreet(partner.getCompanyInfo().getAddress().getStreet());
		address.setZip(partner.getCompanyInfo().getAddress().getZip());
		comanyInfo.setAddress(address);
		comanyInfo.setName(partner.getCompanyInfo().getName());
		comanyInfo.setDescription(partner.getCompanyInfo().getDescription());
		comanyInfo.setUrl(partner.getCompanyInfo().getUrl());
		
		PartnerServiceStub.ContactDetails contactDetails = new PartnerServiceStub.ContactDetails();
		PartnerServiceStub.Address address2 = new PartnerServiceStub.Address();
		address2.setCity(partner.getContactDetails().getAddress().getCity());
		address2.setCountry(new PartnerServiceStub.Country(partner.getContactDetails().getAddress().getCountry(),true));
		address2.setState(partner.getContactDetails().getAddress().getState());
		address2.setStreet(partner.getContactDetails().getAddress().getStreet());
		address2.setZip(partner.getContactDetails().getAddress().getZip());
		contactDetails.setAddress(address2);
		contactDetails.setTitle(new PartnerServiceStub.Title(partner.getContactDetails().getTitle(),true));
		contactDetails.setFirstname(partner.getContactDetails().getFirstname());
		contactDetails.setLastname(partner.getContactDetails().getLastname());
		contactDetails.setEmail(partner.getContactDetails().getEmail());
		contactDetails.setResponsibility(partner.getContactDetails().getResponsibility());
		contactDetails.setFax(partner.getContactDetails().getFax());
		contactDetails.setPhone(partner.getContactDetails().getPhone());
		contactDetails.setMobile(partner.getContactDetails().getMobile());
		contactDetails.setLanguage(new PartnerServiceStub.Language(partner.getContactDetails().getLanguage(),true));
		
		PartnerServiceStub.LoginInfo loginInfo = new PartnerServiceStub.LoginInfo();
		PartnerServiceStub.LoginCredentials login = new PartnerServiceStub.LoginCredentials();
		login.setPassword(partner.getLoginInfo().getPassword());
		login.setUsername(partner.getLoginInfo().getUsername());
		loginInfo.setLogin(login);
		loginInfo.setPasswordAnswer(partner.getLoginInfo().getPasswordAnswer());
		loginInfo.setPasswordQuestion(new PartnerServiceStub.PasswordQuestion(partner.getLoginInfo().getPasswordQuestion(),true));
		
		PartnerServiceStub.PartnerCategory categories = new PartnerServiceStub.PartnerCategory();
		List<String> list = partner.getCategories();
		if(list != null && list.size()>0){
			categories.setValue(new PartnerServiceStub.CategoryValue[list.size()]);
			for(int i = 0; i< list.size(); i++){
				PartnerServiceStub.CategoryValue category = new PartnerServiceStub.CategoryValue(list.get(i),true);
				categories.getValue()[i] = category;
			}
		}
				
		result.setCompanyInfo(comanyInfo);
		result.setContactDetails(contactDetails);
		result.setLoginInfo(loginInfo);
		result.setCategory(categories);
		result.setServiceDiscoveryGroup(partner.getServiceDiscoveryGroup());
		result.setDomainName(partner.getDomainName());
		result.setPartnerType(new PartnerServiceStub.PartnerType(partner.getPartnerType(),true));
		result.setStatus(partner.getStatus());
		result.setModificationDate(partner.getModificationDate());
		result.setComment(partner.getComment());		
		
		return result;
	}
	
	/**
	 * Mapping Partner data model from SGF to NGP
	 * @param partner
	 * @return
	 */
	private SGFPartner mappingPartner(PartnerServiceStub.Partner partner){
		SGFPartner result = null;
		if(partner != null)
			result = new SGFPartner();
		
		SGFPartner.CompanyInfo comanyInfo = result.new CompanyInfo();
		SGFPartner.Address address = result.new Address();
		address.setCity(partner.getCompanyInfo().getAddress().getCity());
		address.setCountry(partner.getCompanyInfo().getAddress().getCountry().getValue());
		address.setState(partner.getCompanyInfo().getAddress().getState());
		address.setStreet(partner.getCompanyInfo().getAddress().getStreet());
		address.setZip(partner.getCompanyInfo().getAddress().getZip());
		comanyInfo.setAddress(address);
		comanyInfo.setName(partner.getCompanyInfo().getName());
		comanyInfo.setDescription(partner.getCompanyInfo().getDescription());
		comanyInfo.setUrl(partner.getCompanyInfo().getUrl());
	
		SGFPartner.ContactDetails contactDetails = result.new ContactDetails();
		SGFPartner.Address address2 = result.new Address();
		address2.setCity(partner.getContactDetails().getAddress().getCity());
		address2.setCountry(partner.getContactDetails().getAddress().getCountry().getValue());
		address2.setState(partner.getContactDetails().getAddress().getState());
		address2.setStreet(partner.getContactDetails().getAddress().getStreet());
		address2.setZip(partner.getContactDetails().getAddress().getZip());
		contactDetails.setAddress(address2);
		contactDetails.setTitle(partner.getContactDetails().getTitle().getValue());
		contactDetails.setFirstname(partner.getContactDetails().getFirstname());
		contactDetails.setLastname(partner.getContactDetails().getLastname());
		contactDetails.setEmail(partner.getContactDetails().getEmail());
		contactDetails.setResponsibility(partner.getContactDetails().getResponsibility());
		contactDetails.setFax(partner.getContactDetails().getFax());
		contactDetails.setPhone(partner.getContactDetails().getPhone());
		contactDetails.setMobile(partner.getContactDetails().getMobile());
		contactDetails.setLanguage(partner.getContactDetails().getLanguage().getValue());
		
		SGFPartner.LoginInfo loginInfo = result.new LoginInfo();
		loginInfo.setUsername(partner.getLoginInfo().getLogin().getUsername());
		loginInfo.setPassword(partner.getLoginInfo().getLogin().getPassword());
		loginInfo.setPasswordAnswer(partner.getLoginInfo().getPasswordAnswer());
		loginInfo.setPasswordQuestion(partner.getLoginInfo().getPasswordQuestion().getValue());
		
		List<String> list = new ArrayList<String>();
		PartnerServiceStub.CategoryValue[] categories = partner.getCategory().getValue();
		if(categories != null && categories.length > 0){
			for(int i = 0; i< categories.length; i++){
				list.add(categories[i].getValue());
			}
		}
				
		result.setCompanyInfo(comanyInfo);
		result.setContactDetails(contactDetails);
		result.setLoginInfo(loginInfo);
		result.setCategories(list);
		result.setServiceDiscoveryGroup(partner.getServiceDiscoveryGroup());
		result.setDomainName(partner.getDomainName());
		result.setPartnerType(partner.getPartnerType().getValue());
		result.setStatus(partner.getStatus());
		result.setModificationDate(partner.getModificationDate());
		result.setComment(partner.getComment());		
		
		return result;
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		SGFPartnerAdapter adapter = new SGFPartnerAdapter();

		SGFPartner partner = new SGFPartner();
		SGFPartner.CompanyInfo companyInfo = partner.new CompanyInfo();
		SGFPartner.Address address = partner.new Address();
		address.setCity("Shanghai");
		address.setCountry("China");
		address.setState("Shanghai");
		address.setStreet("Jinke");
		address.setZip("201203");
		companyInfo.setAddress(address);
		companyInfo.setName("eds");
		companyInfo.setUrl("http://www.hp.com");
		
		SGFPartner.ContactDetails contact = partner.new ContactDetails();
		contact.setTitle("Mr.");
		contact.setFirstname("Bruce");
		contact.setLastname("Zhang");
		address = partner.new Address();
		address.setCountry("China");
		contact.setAddress(address);
		contact.setEmail("zhenyu.zhang@hp.com");
		contact.setLanguage("English");
		
		SGFPartner.LoginInfo login = partner.new LoginInfo();
		login.setUsername("testNGPPartner");//unique
		login.setPassword("password");
		login.setPasswordQuestion("Company URL");//enum
		login.setPasswordAnswer("http://www.hp.com");
		
		List<String> categories = new ArrayList<String>();
		categories.add("ASP");//enum
		categories.add("SP");//enum
		categories.add("CP");//enum
		
		partner.setCompanyInfo(companyInfo);
		partner.setContactDetails(contact);
		partner.setLoginInfo(login);
		partner.setCategories(categories);
		partner.setDomainName("DEFAULT");//Fixed
		partner.setPartnerType("External");//enum
		partner.setStatus("ENABLED");//Fixed
		
		
		try {
			adapter.registerSGFPartner(partner);
			System.out.println("ok");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
