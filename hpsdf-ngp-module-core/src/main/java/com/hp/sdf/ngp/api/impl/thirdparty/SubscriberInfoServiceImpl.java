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
package com.hp.sdf.ngp.api.impl.thirdparty;

import javax.annotation.PostConstruct;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

import com.hp.sdf.ngp.api.comon.util.JndiUtil;
import com.hp.sdf.ngp.api.exception.StoreClientServiceException;
import com.hp.sdf.ngp.api.thirdparty.SubscriberInfoService;

@Component
public class SubscriberInfoServiceImpl implements SubscriberInfoService {

	private SubscriberInfoService subscriberInfoService;

	private final static Log log = LogFactory.getLog(SubscriberInfoServiceImpl.class);

	@PostConstruct
	protected void init() {
		subscriberInfoService = (SubscriberInfoService) JndiUtil.lookupObject(SubscriberInfoService.class.getCanonicalName());
		log.info("Look up JNDI field[" + SubscriberInfoService.class.getCanonicalName() + "] to get the SubscriberInfoService["
				+ subscriberInfoService + "]");

	}

	public String getSubscriberId(String msisdn) throws StoreClientServiceException{
		if (subscriberInfoService != null) {
			return subscriberInfoService.getSubscriberId(msisdn);
		} else {
			this.init();

			if (subscriberInfoService != null) {
				return subscriberInfoService.getSubscriberId(msisdn);
			} else {
				log.warn("Can't get the object via look up JNDI field[" + SubscriberInfoService.class.getCanonicalName() + "]");
			}
		}
		return msisdn;
	}

}

// $Id$