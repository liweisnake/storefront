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
package com.hp.sdf.ngp.api.system;

import com.hp.sdf.ngp.api.exception.AssetCatalogServiceException;
import com.hp.sdf.ngp.api.exception.StoreClientServiceException;

/**
 * 
 * JNDI binding location: /com/hp/sdf/ngp/api/system/SystemConfigService
 *
 */
public interface SystemConfigService {

	/**
	 * Gets system config value.
	 * 
	 * @param key the system config key
	 * @return system config value.
	 * @throws StoreClientServiceException when internal exception occurs.
	 */
	public String getConfigValue(String key) throws StoreClientServiceException;
	
	/**
	 * Sets system config value.
	 * If the config key-value exists, the new value will replace the old one.
	 * 
	 * @param key the system config key
	 * @param value system config value
	 * @throws AssetCatalogServiceException when internal exception occurs.
	 */
	public void setConfigValue(String key, String value) throws AssetCatalogServiceException;
}

// $Id$