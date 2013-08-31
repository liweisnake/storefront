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
package com.hp.sdf.ngp.sdp;

import java.util.List;

import com.hp.sdf.ngp.common.exception.SGFCallingFailureException;
import com.hp.sdf.ngp.sdp.model.SGFAccount;
import com.hp.sdf.ngp.sdp.model.SGFOperation;
import com.hp.sdf.ngp.sdp.model.SGFPartner;
import com.hp.sdf.ngp.sdp.model.SGFRestServiceMetadata;
import com.hp.sdf.ngp.sdp.model.SGFSG;
import com.hp.sdf.ngp.sdp.model.SGFService;

/**
 * This interface is used to call the SGF Web Service API
 * 
 *
 */
public interface SGFWebService {
	
	/**
	 * To create a partner in SGF
	 * @param partner
	 * @return
	 */
	public boolean createPartner(SGFPartner partner) throws SGFCallingFailureException;
	
	/**
	 * To get SGF Service details by Service ID
	 * @param serviceId
	 * @return
	 */
	public SGFService getService(String serviceId) throws SGFCallingFailureException;
	
	/**
	 * To get all SGF Services
	 * @return
	 */
	public List<SGFService> getAllServices() throws SGFCallingFailureException;
	
	/**
	 * To create Service Group in SGF
	 * @param sg
	 * @return
	 */
	public boolean createServiceGroup(SGFSG sg) throws SGFCallingFailureException;

	/**
	 * To get SGF Services by Service Discovery Group name
	 * @param sdg
	 * @return
	 */
	public List<SGFService> getServicesBySdg(String sdg) throws SGFCallingFailureException;
	
	/**
	 * To list SGF Service Operation details by Service ID
	 * @param serviceID
	 * @return
	 */
	public List<SGFOperation> getOperationByService(String serviceID) throws SGFCallingFailureException;
	
	/**
	 * To list all SGF Service Operation details including Rate information
	 * @return
	 */
	public List<SGFOperation> listServiceOperationRate() throws SGFCallingFailureException;
	
	/**
	 * To Create Account in SGF
	 * @param account
	 */
	public void createAccount(SGFAccount account) throws SGFCallingFailureException;
	
	/**
	 * To Get Account details in SGF
	 * @param partner
	 * @throws SGFCallingFailureException
	 */
	public SGFAccount getAccount(String partner) throws SGFCallingFailureException;
	
	/**
	 * To Get Metadata of REST Service from SGF
	 * @param id
	 * @return
	 * @throws SGFCallingFailureException
	 */
	public SGFRestServiceMetadata getMetadataForRESTService(String id) throws SGFCallingFailureException;

}
