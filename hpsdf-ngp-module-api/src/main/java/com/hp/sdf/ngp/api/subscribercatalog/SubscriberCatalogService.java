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
package com.hp.sdf.ngp.api.subscribercatalog;

import java.util.Date;
import java.util.List;

import com.hp.sdf.ngp.api.exception.SubscriberCatalogServiceException;
import com.hp.sdf.ngp.api.model.Subscriber;
import com.hp.sdf.ngp.api.search.SearchExpression;

/**
 * Interface to operate subscriber profile.
 * JNDI binding location: /com/hp/sdf/ngp/api/subscribercatalog/SubscriberCatalogService
 * 
 */
public interface SubscriberCatalogService {

	/**
	 * Creates an empty subscriber object.
	 * 
	 * @return an empty subscriber object.
	 * @throws SubscriberCatalogServiceException
	 *             when internal exception occurs.
	 */
	public Subscriber constructSubscriberObject()
			throws SubscriberCatalogServiceException;

	/**
	 * Adds a subscriber to the system. It is possible that one user's display
	 * name is same with anothers. Invoker can avoid this by searching existing
	 * display name before update the display name.
	 * 
	 * @param subscriber
	 *            basic information of a subscriber
	 * @throws SubscriberCatalogServiceException
	 *             when internal exception occurs.
	 */
	public void addSubscriber(Subscriber subscriber)
			throws SubscriberCatalogServiceException;

	/**
	 * Deletes a subscriber from the system.
	 * 
	 * @param userId
	 *            subscriber's user ID
	 * @throws SubscriberCatalogServiceException
	 *             when internal exception occurs.
	 */
	public void deleteSubscriber(String userId)
			throws SubscriberCatalogServiceException;

	/**
	 * Updates a subscriber in the system. It is possible that one user's
	 * display name is same with anothers. Invoker can avoid this by searching
	 * existing display name before update the display name.
	 * 
	 * @param subscriber
	 *            basic information of a subscriber
	 * @throws SubscriberCatalogServiceException
	 *             when internal exception occurs.
	 */
	public void updateSubscriber(Subscriber subscriber)
			throws SubscriberCatalogServiceException;

	/**
	 * Finds a subscriber from the system by his/her user ID.
	 * 
	 * @param userId
	 *            subscriber user ID
	 * @return a subscriber from the system with the given user ID.
	 * @throws SubscriberCatalogServiceException
	 *             when internal exception occurs.
	 */
	public Subscriber getSubscriber(String userId)
			throws SubscriberCatalogServiceException;

	/**
	 * Updates a subscriber's displayName. It is possible that one user's
	 * display name is same with anothers. Invoker can avoid this by searching
	 * existing display name before update the display name.
	 * 
	 * @param userId
	 * @param displayName
	 * @throws SubscriberCatalogServiceException
	 *             when internal exception occurs.
	 */
	public void updateDisplayName(String userId, String displayName)
			throws SubscriberCatalogServiceException;

	/**
	 * Finds subscribers by given conditions.
	 * 
	 * @param searchExpression
	 *            search conditions
	 * @return subscribers match the given conditions.
	 * @throws SubscriberCatalogServiceException
	 *             when internal exception occurs.
	 */
	public List<Subscriber> searchSubscriber(SearchExpression searchExpression)
			throws SubscriberCatalogServiceException;

	/**
	 * Adds string type EAV attributes to a subscriber.
	 * 
	 * @param userId
	 *            subscriber user ID.
	 * @param attributeName
	 * @param value
	 * @throws SubscriberCatalogServiceException
	 *             when internal exception occurs.
	 */
	public void addAttribute(String userId, String attributeName, String value)
			throws SubscriberCatalogServiceException;

	/**
	 * Adds number type EAV attributes to a subscriber.
	 * 
	 * @param userId
	 *            subscriber user ID.
	 * @param attributeName
	 * @param value
	 * @throws SubscriberCatalogServiceException
	 *             when internal exception occurs.
	 */
	public void addAttribute(String userId, String attributeName, Float value)
			throws SubscriberCatalogServiceException;

	/**
	 * Adds date type EAV attributes to a subscriber.
	 * 
	 * @param userId
	 *            subscriber user ID.
	 * @param attributeName
	 * @param value
	 * @throws SubscriberCatalogServiceException
	 *             when internal exception occurs.
	 */
	public void addAttribute(String userId, String attributeName, Date value)
			throws SubscriberCatalogServiceException;

	/**
	 * Removes EAV attributes from a subscriber. Some EAV attribute of a
	 * subscriber may have the same attribute name. They will all be removed.
	 * 
	 * @param userId
	 * @param attributeName
	 * @throws SubscriberCatalogServiceException
	 *             when internal exception occurs.
	 */
	public void removeAttributes(String userId, String attributeName)
			throws SubscriberCatalogServiceException;
}

// $Id$