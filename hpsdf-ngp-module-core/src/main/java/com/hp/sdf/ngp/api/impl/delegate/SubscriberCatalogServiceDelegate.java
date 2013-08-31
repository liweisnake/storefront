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

import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;

import com.hp.sdf.ngp.api.comon.util.JndiUtil;
import com.hp.sdf.ngp.api.exception.SubscriberCatalogServiceException;
import com.hp.sdf.ngp.api.impl.subscribercatalog.SubscriberCatalogServiceImpl;
import com.hp.sdf.ngp.api.model.Subscriber;
import com.hp.sdf.ngp.api.search.SearchExpression;
import com.hp.sdf.ngp.api.subscribercatalog.SubscriberCatalogService;
import com.hp.sdf.ngp.common.delegate.ComponentDelegate;

@Component(value = "subscriberCatalogService")
public class SubscriberCatalogServiceDelegate extends ComponentDelegate<SubscriberCatalogService> implements SubscriberCatalogService {

	@SuppressWarnings("unchecked")
	@Override
	protected Class<SubscriberCatalogService> getDefaultComponent() {
		return (Class) SubscriberCatalogServiceImpl.class;

	}

	@PostConstruct
	protected void init() {
		JndiUtil.bind(SubscriberCatalogService.class.getCanonicalName(), this);
	}

	public void addAttribute(String userId, String attributeName, Date value) throws SubscriberCatalogServiceException {
		component.addAttribute(userId, attributeName, value);
	}

	public void addAttribute(String userId, String attributeName, Float value) throws SubscriberCatalogServiceException {
		component.addAttribute(userId, attributeName, value);
	}

	public void addAttribute(String userId, String attributeName, String value) throws SubscriberCatalogServiceException {
		component.addAttribute(userId, attributeName, value);
	}

	public void addSubscriber(Subscriber subscriber) throws SubscriberCatalogServiceException {
		component.addSubscriber(subscriber);
	}

	public Subscriber constructSubscriberObject() throws SubscriberCatalogServiceException {
		return component.constructSubscriberObject();
	}

	public void deleteSubscriber(String userId) throws SubscriberCatalogServiceException {
		component.deleteSubscriber(userId);
	}

	public Subscriber getSubscriber(String userId) throws SubscriberCatalogServiceException {
		return component.getSubscriber(userId);
	}

	public void removeAttributes(String userId, String attributeName) throws SubscriberCatalogServiceException {
		component.removeAttributes(userId, attributeName);
	}

	public List<Subscriber> searchSubscriber(SearchExpression searchExpression) throws SubscriberCatalogServiceException {
		return component.searchSubscriber(searchExpression);
	}

	public void updateDisplayName(String userId, String displayName) throws SubscriberCatalogServiceException {
		component.updateDisplayName(userId, displayName);
	}

	public void updateSubscriber(Subscriber subscriber) throws SubscriberCatalogServiceException {
		component.updateSubscriber(subscriber);
	}
	
	
}

// $Id$