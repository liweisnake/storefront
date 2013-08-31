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
package com.hp.sdf.ngp.custom.sbm.api.impl.storeclient;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;

import com.hp.sdf.ngp.api.comon.util.JndiUtil;
import com.hp.sdf.ngp.api.exception.StoreClientServiceException;
import com.hp.sdf.ngp.common.delegate.ComponentDelegate;
import com.hp.sdf.ngp.custom.sbm.api.storeclient.model.ClientAppSetting;
import com.hp.sdf.ngp.custom.sbm.api.storeclient.model.ClientApplication;
import com.hp.sdf.ngp.custom.sbm.api.storeclient.model.StoreClientSoftware;
import com.hp.sdf.ngp.custom.sbm.api.storeclient.service.ClientSoftwareService;
import com.hp.sdf.ngp.custom.sbm.storeclient.service.ClientSoftwareServiceImpl;

@Component(value = "clientSoftwareService")
public class ClientSoftwareServiceDelegate extends ComponentDelegate<ClientSoftwareService> implements ClientSoftwareService {

	@SuppressWarnings("unchecked")
	@Override
	protected Class<ClientSoftwareService> getDefaultComponent() {
		return (Class) ClientSoftwareServiceImpl.class;

	}
	
	@PostConstruct
	protected void init() {
		JndiUtil.bind(ClientSoftwareService.class.getCanonicalName(), this);
	}

	public ClientAppSetting constructClientAppSetting() {
		return this.component.constructClientAppSetting();
	}

	public ClientApplication constructClientApplication() {

		return this.component.constructClientApplication();
	}

	public void deleteClientAppSetting(Long clientAppSettingId) throws StoreClientServiceException {
		this.component.deleteClientAppSetting(clientAppSettingId);

	}

	public void deleteClientApplication(Long clientApplicationId) throws StoreClientServiceException {
		this.component.deleteClientApplication(clientApplicationId);

	}

	public ClientAppSetting getClientAppSetting(Long clientAppSettingId) throws StoreClientServiceException {

		return this.component.getClientAppSetting(clientAppSettingId);
	}

	public ClientApplication getClientApplication(Long clientApplicationId) throws StoreClientServiceException {

		return this.component.getClientApplication(clientApplicationId);
	}

	public StoreClientSoftware getLatestStoreClientSoftware(String clientName) throws StoreClientServiceException {

		return this.component.getLatestStoreClientSoftware(clientName);
	}

	public void saveClientAppSetting(ClientAppSetting clientAppSetting) throws StoreClientServiceException {
		this.component.saveClientAppSetting(clientAppSetting);

	}

	public void saveClientApplication(ClientApplication clientApplication) throws StoreClientServiceException {
		this.component.saveClientApplication(clientApplication);

	}

	public String getStoreClientSoftwareConfigFileLatestVersion(String clientName) throws StoreClientServiceException {
		return this.component.getStoreClientSoftwareConfigFileLatestVersion(clientName);
	}

	public String getStoreClientSoftwareLatestVersion(String clientName) throws StoreClientServiceException {
		return this.component.getStoreClientSoftwareLatestVersion(clientName);
	}

}

// $Id$