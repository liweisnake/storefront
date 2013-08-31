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
package com.hp.sdf.ngp.api.impl.system;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.hp.sdf.ngp.api.comon.util.JndiUtil;
import com.hp.sdf.ngp.api.exception.AssetCatalogServiceException;
import com.hp.sdf.ngp.api.exception.StoreClientEntityNotFoundException;
import com.hp.sdf.ngp.api.exception.StoreClientServiceException;
import com.hp.sdf.ngp.api.system.SystemConfigService;
import com.hp.sdf.ngp.service.SystemConfigurationService;

@Component
@Transactional
public class SystemConfigServiceImpl implements SystemConfigService {
	
	@Resource
	private SystemConfigurationService systemConfigurationService;
	
	private final static Log log = LogFactory
	.getLog(SystemConfigServiceImpl.class);
	
	public String getConfigValue(String key) throws StoreClientServiceException {
		log.debug("enter getConfigValue:key="+key);
		try {
			String str = systemConfigurationService.getConfigValue(key);
			if(str==null)
			{
				throw new StoreClientEntityNotFoundException("CatalogAsset returns null.");
			}
			log.debug("getConfigValue returns:"+str);
			return str;
		} catch (com.hp.sdf.ngp.common.exception.EntityNotFoundException enfe) {
			log.error("EntityNotFoundException exception: " + enfe);
			throw new StoreClientEntityNotFoundException(enfe);
		} catch (Exception e) {
			if(e instanceof StoreClientEntityNotFoundException)
			{
				throw (StoreClientEntityNotFoundException)e;
			}
			throw new StoreClientServiceException(e);
		}
	}

	public void setConfigValue(String key, String value)
			throws AssetCatalogServiceException {
		log.debug("enter setConfigValue:key='"+key+",value='"+value+"'");
		try {
			systemConfigurationService.setConfigValue(key, value);
		} catch (Exception e) {
			throw new AssetCatalogServiceException(e);
		}
	}

}

// $Id$