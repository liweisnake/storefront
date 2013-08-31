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
package com.hp.sdf.ngp.sdp.mock;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

import com.hp.sdf.ngp.common.exception.SGFCallingFailureException;
import com.hp.sdf.ngp.sdp.SGFWebService;
import com.hp.sdf.ngp.sdp.model.SGFAccount;
import com.hp.sdf.ngp.sdp.model.SGFOperation;
import com.hp.sdf.ngp.sdp.model.SGFPartner;
import com.hp.sdf.ngp.sdp.model.SGFRestServiceMetadata;
import com.hp.sdf.ngp.sdp.model.SGFSG;
import com.hp.sdf.ngp.sdp.model.SGFService;

@Component
public class SGFWebServiceMock implements SGFWebService{
	
	public final static Log log = LogFactory.getLog(SGFWebServiceMock.class);
	
	/**
	 * Create Partner in SGF
	 * @param partner
	 * @return
	 * @throws SGFCallingFailureException
	 */
	public boolean createPartner(SGFPartner partner) throws SGFCallingFailureException{
		log.debug("Start SGF Mock service, create Partner. Partner name: " + partner.getLoginInfo().getUsername());
		return true;
	}

	/**
	 * List all Service in SGF
	 * @return
	 * @throws SGFCallingFailureException
	 */
	public List<SGFService> getAllServices() throws SGFCallingFailureException{
		log.debug("Start SGF Mock service, Get all Service in SGF");
		List<SGFService> result = new ArrayList<SGFService>();
		result.add(this.getMockService());
		return result;
	}

	/**
	 * Get Service by Service Discovery Group in SGF
	 * @param sdg
	 * @return
	 * @throws SGFCallingFailureException
	 */
	public List<SGFService> getServicesBySdg(String sdg) throws SGFCallingFailureException{
		log.debug("Start SGF Mock service, Get Service of Service Discovery Group in SGF");
		List<SGFService> result = new ArrayList<SGFService>();
		result.add(this.getMockService());
		return result;
	}

	/**
	 * Get Service by Service ID in SGF
	 * @param serviceId
	 * @return
	 * @throws SGFCallingFailureException
	 */
	public SGFService getService(String serviceId) throws SGFCallingFailureException{
		log.debug("Start SGF Mock service, Get Service in SGF by Service ID");
		return this.getMockService();
	}

	/**
	 * Create Service Group in SGF
	 * @param sg
	 * @return
	 * @throws SGFCallingFailureException
	 */
	public boolean createServiceGroup(SGFSG sg) throws SGFCallingFailureException{
		log.debug("Start SGF Mock service, create Service Group. Service Group's name: " + sg.getName());
		return true;
	}
	
	/**
	 * List Operations of Service by Service ID
	 * @param serviceID
	 * @return
	 * @throws SGFCallingFailureException
	 */
	public List<SGFOperation> getOperationByService(String serviceID) throws SGFCallingFailureException{
		log.debug("Start SGF Mock service, List Service Operations in SGF by Service ID");
		List<SGFOperation> result = new ArrayList<SGFOperation>();
		SGFOperation op = new SGFOperation();
		op.setName("SGF Mock Serivce Operation");
		result.add(op);
		return result;
	}
	
	/**
	 * List all Operations with Rate information of Services which have Rate information
	 * @return
	 * @throws SGFCallingFailureException
	 */
	public List<SGFOperation> listServiceOperationRate() throws SGFCallingFailureException{
		log.debug("Start SGF Mock service, List all Service Operations in SGF with Rate information which have Rate information");
		List<SGFOperation> result = new ArrayList<SGFOperation>();
		SGFOperation op = new SGFOperation();
		op.setName("SGF Mock Serivce Operation");
		op.setRate("5");
		result.add(op);
		return result;
	}
	
	/**
	 * Create Account in SGF
	 * @param account
	 * @throws SGFCallingFailureException
	 */
	public void createAccount(SGFAccount account) throws SGFCallingFailureException{
		log.debug("Start SGF Mock service, create Account in SGF. Partner name: " + account.getPartnerName());
	}

	/**
	 * To Get Account details in SGF
	 * @param partner
	 * @throws SGFCallingFailureException
	 */
	public SGFAccount getAccount(String partner) throws SGFCallingFailureException{
		log.debug("Start SGF Mock service, Get Account details in SGF by Partner name");
		return this.getMockAccount();
	}
	
	/**
	 * Get Metadata of REST Service from SGF
	 * @param id
	 * @return
	 * @throws SGFCallingFailureException
	 */
	public SGFRestServiceMetadata getMetadataForRESTService(String id) throws SGFCallingFailureException{
		log.debug("Start SGF Mock service, Get Metadata of REST Service from SGF by Service ID");
		return null;
	}
	
	private SGFService getMockService(){
		SGFService service = new SGFService();
		service.setName("SGF_Mock_Service");
		service.setId("1bde219-122c45ee325-1615713278");
		service.setVersion("1.0");
		service.setDescription("SGF_Mock_Service Description...");
		service.setServiceProvider("SGF_Mock_Partner");
		service.setTrafficType("NT");
		service.setServiceIndication("Generic Application end-point");
		service.setAccessInterface("webservice");
		SGFService.OutboundSecurity sos = service.new OutboundSecurity();
		sos.setAuthentication("None");
		sos.setTransport("HTTP");
		service.setOutboundSecurity(sos);
		service.setServiceCategory("Web Services");
		service.setKeywords("Mock Service");
		service.setEnablerName("default");
		service.setStatus("approved");
		SGFService.BrokerService sbs = service.new BrokerService();
		sbs.setAuthType("BASIC");
		sbs.setBrokeredServiceName("Brocked_SGF_Mock_Service");
		sbs.setUrl("http://sgf/service/mock/binding");
		List<SGFService.BrokerService> sbsList = new ArrayList<SGFService.BrokerService>();
		sbsList.add(sbs);
		service.setBrokerServices(sbsList);
		return service;
	}
	
	private SGFAccount getMockAccount(){
		SGFAccount account = new SGFAccount();
		account.setPartnerID("SGF_Mock_Partner");
		account.setPartnerName("SGF_Mock_Partner");
		Calendar begin = new GregorianCalendar();
		Calendar end = new GregorianCalendar();
		try {
			Date bd = new SimpleDateFormat("yyyy-MM-dd").parse("1900-01-01");
			Date ed = new SimpleDateFormat("yyyy-MM-dd").parse("2200-01-01");
			begin.setTime(bd);
			end.setTime(ed);
		} catch (ParseException e1) {
			e1.printStackTrace();
		}
		account.setVaildFrom(begin);
		account.setExpireDate(end);
		account.setBalanceType("Web2.0");
		Map<Calendar, String> map = new HashMap<Calendar, String>();
		map.put(begin, "Account Creation Date");
		account.setDatedTransaction(map);
		account.setBalanceAmount(new BigDecimal("100"));
		account.setLastUpdated(begin);
		return account;
	}

}

// $Id$