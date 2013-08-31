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

import com.hp.sdf.ngp.api.comon.EntityType;
import com.hp.sdf.ngp.api.search.SearchExpression;
import com.hp.sdf.ngp.dao.SubscriberProfileDAO;
import com.hp.sdf.ngp.eav.EavRepository;
import com.hp.sdf.ngp.eav.model.Entity;
import com.hp.sdf.ngp.model.SubscriberProfile;
import com.hp.sdf.ngp.service.SubscriptionService;

@Component
@Transactional
public class SubscriptionServiceImpl implements SubscriptionService {

	Log log = LogFactory.getLog(SubscriptionServiceImpl.class);

	@Resource
	private SubscriberProfileDAO subscriberProfileDao;

	@Resource
	private EavRepository eavRepository;

	public void deleteSubscriber(String userid) {
		subscriberProfileDao.remove(subscriberProfileDao.findById(userid));
	}

	public SubscriberProfile retrieveSubscriber(String userid) {
		return subscriberProfileDao.findById(userid);
	}

	public void saveSubscriber(SubscriberProfile sp) {
		Entity entity = new Entity();
		entity.setEntityType(EntityType.SUBSCRIBER.ordinal());
		eavRepository.addEntity(entity);
		sp.setEntityId(entity.getEntityID());
		subscriberProfileDao.persist(sp);
	}

	public void updateSubscriber(SubscriberProfile sp) {
		subscriberProfileDao.merge(sp);
	}

	@Transactional(readOnly = true)
	public List<SubscriberProfile> searchSubscriber(SearchExpression searchExpression) {
		return subscriberProfileDao.search(searchExpression);
	}

	@Transactional(readOnly = true)
	public long searchSubscriberProfileCount(SearchExpression searchExpression) {
		return subscriberProfileDao.searchCount(searchExpression);
	}

}
