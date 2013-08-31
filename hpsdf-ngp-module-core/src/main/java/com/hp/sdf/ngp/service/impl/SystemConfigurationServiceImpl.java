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
package com.hp.sdf.ngp.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.hp.sdf.ngp.api.search.SearchExpression;
import com.hp.sdf.ngp.api.search.Condition.StringComparer;
import com.hp.sdf.ngp.common.exception.NgpRuntimeException;
import com.hp.sdf.ngp.dao.SystemConfigDAO;
import com.hp.sdf.ngp.model.SystemConfig;
import com.hp.sdf.ngp.search.condition.systemconfig.SystemConfigConfigKeyCondition;
import com.hp.sdf.ngp.search.condition.systemconfig.SystemConfigValueCondition;
import com.hp.sdf.ngp.search.engine.SearchExpressionImpl;
import com.hp.sdf.ngp.service.SystemConfigurationService;

@Component
@Transactional
public class SystemConfigurationServiceImpl implements SystemConfigurationService {
	
	@Resource
	private SystemConfigDAO systemConfigDAO;
	
	private final static Log log = LogFactory
	.getLog(SystemConfigurationServiceImpl.class);
	
	public String getConfigValue(String key) {
		try {
			SystemConfig systemConfig = systemConfigDAO.findUniqueBy("configKey", key);
			if(null != systemConfig){
				return systemConfig.getValue();
			}
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("SystemConfiguration exception: " + e);
			throw new NgpRuntimeException(e);
		}
	}

	public void setConfigValue(String key, String value) {
		try {
			SearchExpression searchExpression = new SearchExpressionImpl();
			searchExpression.addCondition(new SystemConfigConfigKeyCondition(key,StringComparer.EQUAL, false, false));
			searchExpression.addCondition(new SystemConfigValueCondition(value,StringComparer.EQUAL, false, false));
			
			List<SystemConfig> systemConfigs = systemConfigDAO.search(searchExpression);
			if(null != systemConfigs && systemConfigs.size() > 0){
				systemConfigDAO.remove(systemConfigs.get(0).getId());
			}
			
			SystemConfig systemConfig = new SystemConfig();
			systemConfig.setConfigKey(key);
			systemConfig.setValue(value);
			systemConfigDAO.persist(systemConfig);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("SystemConfiguration exception: " + e);
			throw new NgpRuntimeException(e);
		}
	}

}

// $Id$