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
package com.hp.sdf.ngp.api.impl.subscribercatalog;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.hp.sdf.ngp.api.comon.EntityType;
import com.hp.sdf.ngp.api.exception.SubscriberCatalogEntityNotFoundException;
import com.hp.sdf.ngp.api.exception.SubscriberCatalogServiceException;
import com.hp.sdf.ngp.api.impl.model.SubscriberImpl;
import com.hp.sdf.ngp.api.model.Subscriber;
import com.hp.sdf.ngp.api.search.SearchExpression;
import com.hp.sdf.ngp.api.subscribercatalog.SubscriberCatalogService;
import com.hp.sdf.ngp.common.exception.NgpRuntimeException;
import com.hp.sdf.ngp.model.SubscriberProfile;
import com.hp.sdf.ngp.service.ApplicationService;
import com.hp.sdf.ngp.service.SubscriptionService;

@Component
@Transactional
public class SubscriberCatalogServiceImpl implements SubscriberCatalogService {

	@Resource
	private ApplicationService applicationService;

	@Resource
	private SubscriptionService subscriptionService;

	private final static Log log = LogFactory.getLog(SubscriberCatalogServiceImpl.class);

	public void addAttribute(String userId, String attributeName, String value) throws SubscriberCatalogServiceException {
		log.debug("enter String addAttribute:userId='" + userId + "',attributeName='" + attributeName + "',value='" + value + "'");
		try {
			applicationService.addAttribute(userId, EntityType.SUBSCRIBER, attributeName, value);

		} catch (NgpRuntimeException e) {
			e.printStackTrace();
			log.error("Comment exception: " + e);
			throw new SubscriberCatalogServiceException(e);
		}
	}

	public void addAttribute(String userId, String attributeName, Float value) throws SubscriberCatalogServiceException {
		log.debug("enter Number addAttribute:userId='" + userId + "',attributeName='" + attributeName + "',value='" + value + "'");
		try {
			applicationService.addAttribute(userId, EntityType.SUBSCRIBER, attributeName, value);

		} catch (NgpRuntimeException e) {
			e.printStackTrace();
			log.error("Comment exception: " + e);
			throw new SubscriberCatalogServiceException(e);
		}
	}

	public void addAttribute(String userId, String attributeName, Date value) throws SubscriberCatalogServiceException {
		log.debug("enter Date addAttribute:userId='" + userId + "',attributeName='" + attributeName + "',value='" + value + "'");
		try {
			applicationService.addAttribute(userId, EntityType.SUBSCRIBER, attributeName, value);
		} catch (NgpRuntimeException e) {
			e.printStackTrace();
			log.error("Comment exception: " + e);
			throw new SubscriberCatalogServiceException(e);
		}
	}

	/*
	 * @SuppressWarnings("unchecked")
	 * 
	 * @Transactional private void fillSubscriber(SubscriberImpl subscriberImpl)
	 * { SubscriberProfile subscriberProfile =
	 * subscriberImpl.getSubscriberProfile(); if (null != subscriberProfile) {
	 * // objects Map<String, List<AttributeValue>> values =
	 * applicationService.getAttributes(subscriberProfile.getUserId(),
	 * EntityType.SUBSCRIBER); Map<String, List<Object>> objects = new
	 * HashMap<String, List<Object>>(); if (null != values) { for
	 * (Map.Entry<String, List<AttributeValue>> map : values.entrySet()) {
	 * List<Object> object_list = new ArrayList<Object>(map.getValue());
	 * objects.put(map.getKey(), object_list); } }
	 * subscriberImpl.setObjects(objects); }
	 * 
	 * }
	 */

	public void addSubscriber(Subscriber subscriber) throws SubscriberCatalogServiceException {
		log.debug("enter addSubscriber:" + subscriber.toString());
		try {
			if (!(subscriber instanceof SubscriberImpl)) {
				throw new SubscriberCatalogServiceException("The method parameter subscriber is not a instance of SubscriberImpl");
			}

			SubscriberImpl subscriberImpl = (SubscriberImpl) subscriber;
			SubscriberProfile subscriberProfile = subscriberImpl.getSubscriberProfile();
			subscriptionService.saveSubscriber(subscriberProfile);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Comment exception: " + e);
			throw new SubscriberCatalogServiceException(e);
		}
	}

	public Subscriber constructSubscriberObject() throws SubscriberCatalogServiceException {
		return new SubscriberImpl();
	}

	public void deleteSubscriber(String userId) throws SubscriberCatalogServiceException {
		log.debug("enter deleteSubscriber:userId=" + userId);
		try {
			subscriptionService.deleteSubscriber(userId);

		} catch (Exception e) {
			e.printStackTrace();
			log.error("Comment exception: " + e);
			throw new SubscriberCatalogServiceException(e);
		}
	}

	public Subscriber getSubscriber(String userId) throws SubscriberCatalogServiceException {
		log.debug("enter getSubscriber:userId=" + userId);

		SubscriberProfile subscriberProfile = subscriptionService.retrieveSubscriber(userId);
		if (null != subscriberProfile) {
			SubscriberImpl subscriberImpl = new SubscriberImpl(subscriberProfile);
			// fillSubscriber(subscriberImpl);

			log.debug("getSubscriber returns:" + subscriberImpl.toString());
			return subscriberImpl;
		} else {

			log.debug("getSubscriber returns null");
			throw new SubscriberCatalogEntityNotFoundException("getSubscriber returns null");
		}

	}

	public void removeAttributes(String userId, String attributeName) throws SubscriberCatalogServiceException {
		log.debug("enter removeAttributes:userId='" + userId + "',attributeName='" + attributeName + "'");
		try {
			applicationService.removeAttributes(userId, EntityType.SUBSCRIBER, attributeName);

		} catch (NgpRuntimeException e) {
			e.printStackTrace();
			log.error("Comment exception: " + e);
			throw new SubscriberCatalogServiceException(e);
		}
	}

	public List<Subscriber> searchSubscriber(SearchExpression searchExpression) throws SubscriberCatalogServiceException {
		log.debug("enter searchSubscriber:searchExpression=" + searchExpression.toString());
		try {
			List<Subscriber> subscribers = new ArrayList<Subscriber>();
			List<SubscriberProfile> subscriberProfiles = subscriptionService.searchSubscriber(searchExpression);
			if (null != subscriberProfiles) {
				for (SubscriberProfile subscriberProfile : subscriberProfiles) {
					SubscriberImpl subscriberImpl = new SubscriberImpl(subscriberProfile);
					// fillSubscriber(subscriberImpl);
					subscribers.add(subscriberImpl);
				}
			}

			log.debug("searchSubscriber returns size:" + subscribers.size());
			return subscribers;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Comment exception: " + e);
			throw new SubscriberCatalogServiceException(e);
		}
	}

	public void updateDisplayName(String userId, String displayName) throws SubscriberCatalogServiceException {
		log.debug("enter updateDisplayName:userId='" + userId + "',displayName='" + displayName + "'");
		try {
			SubscriberProfile subscriberProfile = subscriptionService.retrieveSubscriber(userId);
			if (null != subscriberProfile) {
				subscriberProfile.setDisplayName(displayName);
				subscriptionService.updateSubscriber(subscriberProfile);
			}

		} catch (Exception e) {
			e.printStackTrace();
			log.error("Comment exception: " + e);
			throw new SubscriberCatalogServiceException(e);
		}
	}

	public void updateSubscriber(Subscriber subscriber) throws SubscriberCatalogServiceException {
		log.debug("enter updateSubscriber:" + subscriber.toString());
		try {
			if (!(subscriber instanceof SubscriberImpl)) {
				throw new SubscriberCatalogServiceException("The method parameter subscriber is not a instance of SubscriberImpl");
			}

			SubscriberImpl subscriberImpl = (SubscriberImpl) subscriber;
			SubscriberProfile subscriberProfile = subscriberImpl.getSubscriberProfile();

			subscriptionService.updateSubscriber(subscriberProfile);
		} catch (com.hp.sdf.ngp.common.exception.EntityNotFoundException enfe) {
			log.error("EntityNotFoundException exception: " + enfe);
			throw new SubscriberCatalogEntityNotFoundException(enfe);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Comment exception: " + e);
			throw new SubscriberCatalogServiceException(e);
		}
	}

}

// $Id$