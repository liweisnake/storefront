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

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.ITable;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.hp.sdf.ngp.DBEnablerTestBase;
import com.hp.sdf.ngp.api.exception.SubscriberCatalogEntityNotFoundException;
import com.hp.sdf.ngp.api.exception.SubscriberCatalogServiceException;
import com.hp.sdf.ngp.api.impl.model.SubscriberImpl;
import com.hp.sdf.ngp.api.model.Subscriber;
import com.hp.sdf.ngp.api.search.SearchExpression;
import com.hp.sdf.ngp.api.search.Condition.StringComparer;
import com.hp.sdf.ngp.model.SubscriberProfile;
import com.hp.sdf.ngp.search.condition.subscriberprofile.SubscriberProfileDisplayNameCondition;
import com.hp.sdf.ngp.search.condition.subscriberprofile.SubscriberProfileMsisdnCondition;
import com.hp.sdf.ngp.search.engine.SearchExpressionImpl;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:META-INF/applicationContext-test.xml" })
public class TestSubscriberCatalogService extends DBEnablerTestBase {

	@Resource
	private SubscriberCatalogService subscriberCatalogService;

	// @Resource
	// private SubscriptionServiceImpl subscriptionServiceImpl;

	// @Resource
	// private EavRepository eavRepository;

	@Override
	public String dataSetFileName() {
		return "/data_init.xml";
	}

	@Test
	public void testAddAttributeString() throws DataSetException, SQLException, Exception {
		// SubscriberImpl subscriberImpl = new SubscriberImpl();
		// subscriberImpl.setUserId("test_userId");
		// subscriberImpl.setDisplayName("display_name");
		//
		// SubscriberProfile subscriberProfile =
		// subscriberImpl.getSubscriberProfile();
		// Entity entity = new Entity();
		// entity.setEntityType(EntityType.SUBSCRIBER.ordinal());
		// eavRepository.addEntity(entity);
		// subscriberProfile.setEntityId(entity.getEntityID());
		//
		// subscriptionServiceImpl.saveSubscriber(subscriberProfile);

		subscriberCatalogService.addAttribute("tester", "name_varchar", "value_varchar");

		ITable tableValue = databaseTester.getConnection().createQueryTable(
				"result",
				"select a.AttributeID from Attribute a, AttributeValueChar b where "
						+ "a.attributeId = b.attributeId and a.attributeName = 'name_varchar' and b.value = 'value_varchar' ");
		assertTrue(tableValue.getRowCount() == 1);
	}

	@Test
	public void testAddAttributeFloat() throws DataSetException, SQLException, Exception {
		// SubscriberImpl subscriberImpl = new SubscriberImpl();
		// subscriberImpl.setUserId("test_userId");
		// subscriberImpl.setDisplayName("display_name");
		//
		// SubscriberProfile subscriberProfile =
		// subscriberImpl.getSubscriberProfile();
		// Entity entity = new Entity();
		// entity.setEntityType(EntityType.SUBSCRIBER.ordinal());
		// eavRepository.addEntity(entity);
		// subscriberProfile.setEntityId(entity.getEntityID());
		//
		// subscriptionServiceImpl.saveSubscriber(subscriberProfile);

		subscriberCatalogService.addAttribute("username", "name_float", 3.0f);

		ITable tableValue = databaseTester.getConnection().createQueryTable(
				"result",
				"select a.AttributeID from Attribute a, AttributeValueNumber b where "
						+ "a.attributeId = b.attributeId and a.attributeName = 'name_float' and b.value = 3.0 ");
		assertTrue(tableValue.getRowCount() == 1);
	}

	@Test
	public void testAddAttributeDate() throws DataSetException, SQLException, Exception {
		// SubscriberImpl subscriberImpl = new SubscriberImpl();
		// subscriberImpl.setUserId("test_userId");
		// subscriberImpl.setDisplayName("display_name");
		//
		// SubscriberProfile subscriberProfile =
		// subscriberImpl.getSubscriberProfile();
		// Entity entity = new Entity();
		// entity.setEntityType(EntityType.ASSETPROVIDER.ordinal());
		// eavRepository.addEntity(entity);
		// subscriberProfile.setEntityId(entity.getEntityID());
		//
		// subscriptionServiceImpl.saveSubscriber(subscriberProfile);

		Date date = Calendar.getInstance().getTime();
		subscriberCatalogService.addAttribute("tester", "name_date", date);

		ITable tableValue = databaseTester.getConnection().createQueryTable(
				"result",
				"select a.AttributeID,b.value from Attribute a, AttributeValueDate b where "
						+ "a.attributeId = b.attributeId and a.attributeName = 'name_date'");
		assertTrue(tableValue.getRowCount() == 1);
		assertTrue(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(tableValue.getValue(0, "value")).equals(
				new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date)));
	}

	@Test
	public void testAddSubscriber() throws DataSetException, SQLException, Exception {
		Subscriber subscriber = new SubscriberImpl();
		subscriber.setUserId("test_userId");
		subscriber.setDisplayName("display_name");
		subscriber.setMsisdn("test_msidn");

		subscriberCatalogService.addSubscriber(subscriber);

		ITable tableValue = databaseTester.getConnection().createQueryTable("result",
				"select a.userId from SubscriberProfile a where a.userId = 'test_userId' and a.displayName='display_name' ");
		assertTrue(tableValue.getRowCount() == 1);
	}
	
	@Test
	public void testGetSubscriber2() throws DataSetException, SQLException, Exception {
		Subscriber subscriber = new SubscriberImpl();
		subscriber.setUserId("test_userId");
		subscriber.setDisplayName("display_name");
		subscriber.setMsisdn("test_msidn");

		subscriberCatalogService.addSubscriber(subscriber);

		ITable tableValue = databaseTester.getConnection().createQueryTable("result",
				"select a.userId from SubscriberProfile a where a.userId = 'test_userId' and a.displayName='display_name' ");
		assertTrue(tableValue.getRowCount() == 1);
		subscriberCatalogService.addAttribute("test_userId", "test1", new Date());
		subscriberCatalogService.addAttribute("test_userId", "test2", "DSF");
		subscriberCatalogService.addAttribute("test_userId", "test3", 1F);
		Subscriber  subscriber1 =subscriberCatalogService.getSubscriber("test_userId");
		Assert.assertTrue(subscriber1.getMsisdn().equals("test_msidn"));
		
		
		
	}

	@Test
	public void testConstructSubscriberObject() throws SubscriberCatalogServiceException {
		assertTrue(subscriberCatalogService.constructSubscriberObject() instanceof Subscriber);
		Subscriber subscriber = subscriberCatalogService.constructSubscriberObject();
		
		subscriber.setClientOwnerProviderId(2L);
		subscriber.setDisplayName("test_displayName");
		
		SubscriberProfile subscriberProfile = ((SubscriberImpl)subscriber).getSubscriberProfile();
		assertEquals(subscriberProfile.getDisplayName(),"test_displayName");
		assertTrue(subscriberProfile.getClientOwnerProviderId() == 2L);
	}

	@Test
	public void testDeleteSubscriber() throws DataSetException, SQLException, Exception {
		// SubscriberImpl subscriberImpl = new SubscriberImpl();
		// subscriberImpl.setUserId("test_userId");
		// subscriberImpl.setDisplayName("display_name");
		//
		// subscriberCatalogService.addSubscriber(subscriberImpl);
		//
		// ITable tableValue =
		// databaseTester.getConnection().createQueryTable("result",
		// "select a.userId from SubscriberProfile a where a.userId = 'test_userId' and a.displayName='display_name' ");
		// assertTrue(tableValue.getRowCount() == 1);

		subscriberCatalogService.deleteSubscriber("username");

		ITable tableValue = databaseTester.getConnection().createQueryTable("result",
				"select a.userId from SubscriberProfile a where a.userId = 'username' ");
		assertTrue(tableValue.getRowCount() == 0);
	}

	@Test
	public void testGetSubscriber() throws SubscriberCatalogServiceException {
//		 SubscriberImpl subscriberImpl = new SubscriberImpl();
//		 subscriberImpl.setUserId("test_userId");
//		 subscriberImpl.setDisplayName("display_name");
//		
//		 subscriberCatalogService.addSubscriber(subscriberImpl);

		Subscriber subscriber = subscriberCatalogService.getSubscriber("username");

		assertEquals(subscriber.getDisplayName(), "jsp");
//		Assert.assertNotNull(subscriber);
	}

	

	@Test
	public void testSearchSubscriber() throws SubscriberCatalogServiceException {
		// SubscriberImpl subscriberImpl = new SubscriberImpl();
		// subscriberImpl.setUserId("test_userId");
		// subscriberImpl.setDisplayName("display_name");
		//
		// subscriberCatalogService.addSubscriber(subscriberImpl);

		SearchExpression searchExpression = new SearchExpressionImpl();
		searchExpression.addCondition(new SubscriberProfileDisplayNameCondition("jsp", StringComparer.EQUAL, false, false));
		searchExpression.addCondition(new SubscriberProfileMsisdnCondition("msisdn", StringComparer.LIKE, false, false));
		List<Subscriber> subscribers = subscriberCatalogService.searchSubscriber(searchExpression);

		assertTrue(subscribers.size() == 1);
		assertTrue(subscribers.get(0).isClientTester());
	}

	@Test
	public void testUpdateDisplayName() throws DataSetException, SQLException, Exception {
		// SubscriberImpl subscriberImpl = new SubscriberImpl();
		// subscriberImpl.setUserId("test_userId");
		// subscriberImpl.setDisplayName("display_name");
		//
		// subscriberCatalogService.addSubscriber(subscriberImpl);

		Subscriber subscriber = subscriberCatalogService.getSubscriber("username");

		subscriberCatalogService.updateDisplayName(subscriber.getUserId(), "new_name");

		ITable tableValue = databaseTester.getConnection().createQueryTable("result",
				"select a.userId from SubscriberProfile a where a.displayName = 'new_name' and a.userid = 'username'");
		assertTrue(tableValue.getRowCount() == 1);
	}

	@Test
	public void testUpdateSubscriber() throws DataSetException, SQLException, Exception {
		// SubscriberImpl subscriberImpl = new SubscriberImpl();
		// subscriberImpl.setUserId("test_userId");
		// subscriberImpl.setDisplayName("display_name");
		//
		// subscriberCatalogService.addSubscriber(subscriberImpl);
		//
		// subscriberImpl.setDisplayName("new_name");

		Subscriber subscriber = subscriberCatalogService.getSubscriber("username");

		subscriber.setDisplayName("new_name");

		subscriberCatalogService.updateSubscriber(subscriber);

		ITable tableValue = databaseTester.getConnection().createQueryTable("result",
				"select a.userId from SubscriberProfile a where a.displayName = 'new_name' and a.userid = 'username'");
		assertTrue(tableValue.getRowCount() == 1);
	}
	
	
	@Test(expected = SubscriberCatalogEntityNotFoundException.class )
	public void testGetSubscriberByWrongName() throws Exception{
		Subscriber subscriber = subscriberCatalogService.getSubscriber("username2");

		assertTrue(subscriber == null);
	}

	@Test
	public void testSearchSubscriberWithMoreConditions() throws Exception{
		SearchExpression searchExpression = new SearchExpressionImpl();
		searchExpression.addCondition(new SubscriberProfileDisplayNameCondition("jsp", StringComparer.EQUAL, false, false));
		searchExpression.addCondition(new SubscriberProfileMsisdnCondition("test", StringComparer.LIKE, false, false));
		List<Subscriber> subscribers = subscriberCatalogService.searchSubscriber(searchExpression);

		assertTrue(subscribers.size() == 1);
	}
}

// $Id$