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
package com.hp.sdf.ngp.manager.impl;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.hp.sdf.ngp.manager.ApiManager;

@Component
public class ApiSynchronizer {
	
	private final static Log log = LogFactory.getLog(ApiSynchronizer.class);

	@Resource
	private ApiManager apiManager;
	
	@Scheduled(cron = "0 0 * * * ?")
	public void synchronize() {
		try {
			log.info("Start API synchronized");
			apiManager.synchronizeSGFAPI();
		} catch (Throwable e) {
			log.warn("API synchronized fails",e);
		}
	}

}

// $Id$