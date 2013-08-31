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
package com.hp.sdf.ngp.custom.sbm.api.storeclient.service;

import com.hp.sdf.ngp.api.exception.StoreClientServiceException;
import com.hp.sdf.ngp.custom.sbm.api.storeclient.model.ClientAppSetting;
import com.hp.sdf.ngp.custom.sbm.api.storeclient.model.ClientApplication;
import com.hp.sdf.ngp.custom.sbm.api.storeclient.model.StoreClientSoftware;

/**
 * JNDI binding location: /com/hp/sdf/ngp/custom/sbm/api/storeclient/service/ClientSoftwareService
 * 
 *
 */
public interface ClientSoftwareService {

	/**
	 * Retrieve the latest version of the supported store client application
	 * <p/>
	 * client software version is decided by create date.
	 * 
	 * @param clientName
	 *            The name of the client application. E.g. "Softbank Mobile"
	 * @return
	 */
	public StoreClientSoftware getLatestStoreClientSoftware(String clientName)
			throws StoreClientServiceException;

	public ClientApplication constructClientApplication();

	/**
 	 * Saves new version of client application file.
	 * The file binary should be saved in both file system and database.
	 * @param clientApplication
	 */
	public void saveClientApplication(ClientApplication clientApplication)throws StoreClientServiceException;
	public void deleteClientApplication(Long clientApplicationId)throws StoreClientServiceException;
	public ClientApplication getClientApplication(Long clientApplicationId)throws StoreClientServiceException;
	
	
	public ClientAppSetting constructClientAppSetting();
	/** 
	 * Saves new version of client application setting file.
	 * The file binary should be saved in both file system and database.
	 * @param clientAppSetting
	 */
	public void saveClientAppSetting(ClientAppSetting clientAppSetting)throws StoreClientServiceException;
	public void deleteClientAppSetting(Long clientAppSettingId) throws StoreClientServiceException;
	public ClientAppSetting getClientAppSetting(Long clientAppSettingId) throws StoreClientServiceException;
	
	/**
	 * Retrieve the version of the supported store client application
	 * <p/>
	 * client software version is decided by create date.
	 * 
	 * @param clientName
	 *            The name of the client application. E.g. "Softbank Mobile"
	 * @return
	 */
	public String getStoreClientSoftwareLatestVersion(String clientName)
			throws StoreClientServiceException;

	/**
	 * Retrieve latest version of the store client application config file.
	 * <p/>
	 * client software config version is decided by create date.
	 * 
	 * @param clientName
	 *            The name of the client application. E.g. "Softbank Mobile"
	 * @return
	 */
	public String getStoreClientSoftwareConfigFileLatestVersion(
			String clientName) throws StoreClientServiceException;
}

// $Id$