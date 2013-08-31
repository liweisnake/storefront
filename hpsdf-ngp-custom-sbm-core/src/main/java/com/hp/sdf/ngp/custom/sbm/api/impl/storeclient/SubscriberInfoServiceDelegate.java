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

import org.springframework.stereotype.Component;

import com.hp.sdf.ngp.api.exception.StoreClientServiceException;
import com.hp.sdf.ngp.api.thirdparty.SubscriberInfoService;
import com.hp.sdf.ngp.common.delegate.ComponentDelegate;
import com.hp.sdf.ngp.custom.sbm.storeclient.service.SbmSubscriberInfoServiceImpl;

@Component(value = "subscriberInfoService")
public class SubscriberInfoServiceDelegate extends ComponentDelegate<SubscriberInfoService> implements SubscriberInfoService {

	@SuppressWarnings("unchecked")
	@Override
	protected Class<SubscriberInfoService> getDefaultComponent() {
		return (Class) SbmSubscriberInfoServiceImpl.class;
	}
	public String getSubscriberId(String msisdn) throws StoreClientServiceException {
		return this.component.getSubscriberId(msisdn);
	}
}

// $Id$