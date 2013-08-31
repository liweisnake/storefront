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
package com.hp.sdf.ngp.api.impl.delegate;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;

import com.hp.sdf.ngp.api.comon.util.JndiUtil;
import com.hp.sdf.ngp.api.exception.AssetCatalogServiceException;
import com.hp.sdf.ngp.api.exception.StoreClientServiceException;
import com.hp.sdf.ngp.api.impl.system.SystemConfigServiceImpl;
import com.hp.sdf.ngp.api.system.SystemConfigService;
import com.hp.sdf.ngp.common.delegate.ComponentDelegate;

@Component(value = "systemConfigService")
public class SystemConfigServiceDelegate extends ComponentDelegate<SystemConfigService> implements SystemConfigService {

	@SuppressWarnings("unchecked")
	@Override
	protected Class<SystemConfigService> getDefaultComponent() {
		return (Class) SystemConfigServiceImpl.class;

	}

	@PostConstruct
	protected void init() {
		JndiUtil.bind(SystemConfigService.class.getCanonicalName(), this);
	}

	public String getConfigValue(String key) throws StoreClientServiceException {
		return component.getConfigValue(key);
	}

	public void setConfigValue(String key, String value) throws AssetCatalogServiceException {
		component.setConfigValue(key, value);
	}
	
	
}

// $Id$