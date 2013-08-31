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
package com.hp.sdf.ngp.service;

import javax.annotation.Resource;

import junit.framework.Assert;

import org.dbunit.dataset.ITable;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.hp.sdf.ngp.DBEnablerTestBase;
import com.hp.sdf.ngp.model.SubscriberProfile;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:META-INF/applicationContext-test.xml" })
public class TestSubscriptionService extends DBEnablerTestBase {
	
	@Resource
	private SubscriptionService subscriptionService;
	
	@Override
	public String dataSetFileName() {
		return "/data_init.xml";
	}
	
	@Test
	public void testSubscriberService() throws Exception{
		SubscriberProfile sp=new SubscriberProfile();
		sp.setUserId("liuchao");
		sp.setDisplayName("liuchao");
		sp.setMsisdn("12");
		subscriptionService .saveSubscriber(sp);
		ITable tableValue = databaseTester.getConnection().createQueryTable(
				"result", "select * from subscriberprofile where userid='liuchao' and displayname='liuchao'");
		
		Assert.assertTrue(tableValue.getRowCount() == 1);
		
		sp.setDisplayName("chaoliu");
		subscriptionService.updateSubscriber(sp);
		ITable tableValue1 = databaseTester.getConnection().createQueryTable(
				"result", "select * from subscriberprofile where userid='liuchao' and displayname='chaoliu'");
		
		Assert.assertTrue(tableValue1.getRowCount() == 1);
	}
	
	@Test
	public void testRetrieveAndDeleteSubscriber() throws Exception{
		SubscriberProfile sp=subscriptionService.retrieveSubscriber("tester");
		Assert.assertTrue(sp!=null);
		
		subscriptionService.deleteSubscriber("tester");
		
		ITable tableValue1 = databaseTester.getConnection().createQueryTable(
				"result", "select * from subscriberprofile where userid='tester'");
		
		Assert.assertTrue(tableValue1.getRowCount() == 0);
	}

}

// $Id$