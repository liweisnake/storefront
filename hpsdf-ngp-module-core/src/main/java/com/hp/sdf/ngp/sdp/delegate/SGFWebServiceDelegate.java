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
package com.hp.sdf.ngp.sdp.delegate;

import java.util.List;

import org.springframework.stereotype.Component;

import com.hp.sdf.ngp.common.annotation.Value;
import com.hp.sdf.ngp.common.delegate.ComponentDelegate;
import com.hp.sdf.ngp.common.exception.SGFCallingFailureException;
import com.hp.sdf.ngp.sdp.SGFWebService;
import com.hp.sdf.ngp.sdp.impl.SGFWebServiceImpl;
import com.hp.sdf.ngp.sdp.model.SGFAccount;
import com.hp.sdf.ngp.sdp.model.SGFOperation;
import com.hp.sdf.ngp.sdp.model.SGFPartner;
import com.hp.sdf.ngp.sdp.model.SGFRestServiceMetadata;
import com.hp.sdf.ngp.sdp.model.SGFSG;
import com.hp.sdf.ngp.sdp.model.SGFService;

@Component(value = "sGFWebService")
public class SGFWebServiceDelegate extends ComponentDelegate<SGFWebService> implements SGFWebService{
	
	private boolean sgfEnable = true;

	public boolean isSgfEnable() {
		return sgfEnable;
	}

	@Value("SGF.Enabled")
	public void setSgfEnable(boolean sgfEnable) {
		this.sgfEnable = sgfEnable;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected Class<SGFWebService> getDefaultComponent() {
		return (Class)SGFWebServiceImpl.class;
	}

	public void createAccount(SGFAccount account)
			throws SGFCallingFailureException {
		if(!this.sgfEnable)
			return;
		this.component.createAccount(account);
	}

	public boolean createPartner(SGFPartner partner)
			throws SGFCallingFailureException {
		if(!this.sgfEnable)
			return true;
		this.component.createPartner(partner);
		return true;
	}

	public boolean createServiceGroup(SGFSG sg)
			throws SGFCallingFailureException {
		if(!this.sgfEnable)
			return true;
		this.component.createServiceGroup(sg);
		return true;
	}

	public SGFAccount getAccount(String partner) throws SGFCallingFailureException {
		if(!this.sgfEnable)
			return null;
		return this.component.getAccount(partner);
	}

	public List<SGFService> getAllServices() throws SGFCallingFailureException {
		if(!this.sgfEnable)
			return null;
		return this.component.getAllServices();
	}

	public List<SGFOperation> getOperationByService(String serviceID)
			throws SGFCallingFailureException {
		if(!this.sgfEnable)
			return null;
		return this.component.getOperationByService(serviceID);
	}

	public SGFService getService(String serviceId)
			throws SGFCallingFailureException {
		if(!this.sgfEnable)
			return null;
		return this.component.getService(serviceId);
	}

	public List<SGFService> getServicesBySdg(String sdg)
			throws SGFCallingFailureException {
		if(!this.sgfEnable)
			return null;
		return this.component.getServicesBySdg(sdg);
	}

	public List<SGFOperation> listServiceOperationRate()
			throws SGFCallingFailureException {
		if(!this.sgfEnable)
			return null;
		return this.component.listServiceOperationRate();
	}
	
	public SGFRestServiceMetadata getMetadataForRESTService(String id) throws SGFCallingFailureException{
		if(!this.sgfEnable)
			return null;
		return this.component.getMetadataForRESTService(id);
	}

}

// $Id$