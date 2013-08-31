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
package com.hp.sdf.ngp.sdp.impl;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hp.sdf.ngp.common.exception.SGFCallingFailureException;
import com.hp.sdf.ngp.sdp.SGFWebService;
import com.hp.sdf.ngp.sdp.model.SGFAccount;
import com.hp.sdf.ngp.sdp.model.SGFOperation;
import com.hp.sdf.ngp.sdp.model.SGFPartner;
import com.hp.sdf.ngp.sdp.model.SGFRestServiceMetadata;
import com.hp.sdf.ngp.sdp.model.SGFSG;
import com.hp.sdf.ngp.sdp.model.SGFService;
import com.hp.sdf.ngp.sdp.webservice.SGFAccountManagementAdapter;
import com.hp.sdf.ngp.sdp.webservice.SGFFlexReportsAdapter;
import com.hp.sdf.ngp.sdp.webservice.SGFPartnerAdapter;
import com.hp.sdf.ngp.sdp.webservice.SGFRateManagementAdapter;
import com.hp.sdf.ngp.sdp.webservice.SGFServiceAdapter;
import com.hp.sdf.ngp.sdp.webservice.SGFServiceGroupExAdapter;

@Service(value = "sgfServiceImpl")
public class SGFWebServiceImpl implements SGFWebService{
	
	public final static Log log = LogFactory.getLog(SGFWebServiceImpl.class);
	
	@Autowired
	private SGFPartnerAdapter sgfPartnerAdapter;
	
	@Autowired
	private SGFServiceGroupExAdapter sgfServiceGroupExAdapter;
	
	@Autowired
	private SGFServiceAdapter sgfServiceAdapter;
	
	@Autowired
	private SGFFlexReportsAdapter sgfFlexReportsAdapter;
	
	@Autowired
	private SGFRateManagementAdapter sgfRateManagementAdapter;
	
	@Autowired
	private SGFAccountManagementAdapter sgfAccountManagementAdapter;
	
	/**
	 * Create Partner in SGF
	 * @param partner
	 * @return
	 * @throws SGFCallingFailureException
	 */
	public boolean createPartner(SGFPartner partner) throws SGFCallingFailureException{
		log.debug("Start SGF service, create partner. Partner name: " + partner.getLoginInfo().getUsername());
		this.sgfPartnerAdapter.registerSGFPartner(partner);
		return true;
	}

	/**
	 * List all Service in SGF
	 * @return
	 * @throws SGFCallingFailureException
	 */
	public List<SGFService> getAllServices() throws SGFCallingFailureException{
		return this.sgfServiceAdapter.getSGFAllServices();
	}

	/**
	 * Get Service by Service Discovery Group in SGF
	 * @param sdg
	 * @return
	 * @throws SGFCallingFailureException
	 */
	public List<SGFService> getServicesBySdg(String sdg) throws SGFCallingFailureException{
		return this.sgfServiceAdapter.getSGFServicesBySDG(sdg);
	}

	/**
	 * Get Service by Service ID in SGF
	 * @param serviceId
	 * @return
	 * @throws SGFCallingFailureException
	 */
	public SGFService getService(String serviceId) throws SGFCallingFailureException{
		return this.sgfServiceAdapter.getSGFServiceByID(serviceId);
	}

	/**
	 * Create Service Group in SGF
	 * @param sg
	 * @return
	 * @throws SGFCallingFailureException
	 */
	public boolean createServiceGroup(SGFSG sg) throws SGFCallingFailureException{
		//sg.setAccessMode("Commercial");// enum
		SGFSG.WlngDetails wlng = sg.new WlngDetails();
		//wlng.setApplicationType("apptype");
		wlng.setUsername(sg.getName());// unique
		wlng.setPassword(sg.getName());// unique

		sg.setWlngDetails(wlng);
		this.sgfServiceGroupExAdapter.registerSGFServiceGroup(sg);
		return true;
	}
	
	/**
	 * List Operations of Service by Service ID
	 * @param serviceID
	 * @return
	 * @throws SGFCallingFailureException
	 */
	public List<SGFOperation> getOperationByService(String serviceID) throws SGFCallingFailureException{
		return this.sgfFlexReportsAdapter.getOperationsPerService(serviceID);
	}
	
	/**
	 * List all Operations with Rate information of Services which have Rate information
	 * @return
	 * @throws SGFCallingFailureException
	 */
	public List<SGFOperation> listServiceOperationRate() throws SGFCallingFailureException{
		return this.sgfRateManagementAdapter.listOperationRate();
	}
	
	/**
	 * Create Account in SGF
	 * @param account
	 * @throws SGFCallingFailureException
	 */
	public void createAccount(SGFAccount account) throws SGFCallingFailureException{
		this.sgfAccountManagementAdapter.createAccount(account);
	}

	/**
	 * To Get Account details in SGF
	 * @param partner
	 * @throws SGFCallingFailureException
	 */
	public SGFAccount getAccount(String partner) throws SGFCallingFailureException{
		return this.sgfAccountManagementAdapter.getAccount(partner);
	}
	
	/**
	 * Get Metadata of REST Service from SGF
	 * @param id
	 * @return
	 * @throws SGFCallingFailureException
	 */
	public SGFRestServiceMetadata getMetadataForRESTService(String id) throws SGFCallingFailureException{
		return this.sgfServiceAdapter.getMetadataForRESTService(id);
	}

}

// $Id$