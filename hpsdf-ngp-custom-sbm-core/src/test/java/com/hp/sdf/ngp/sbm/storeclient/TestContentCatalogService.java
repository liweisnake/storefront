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
package com.hp.sdf.ngp.sbm.storeclient;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.annotation.Resource;

import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.ITable;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.hp.sdf.ngp.DBEnablerTestBase;
import com.hp.sdf.ngp.api.exception.AssetCatalogServiceException;
import com.hp.sdf.ngp.api.model.AssetProvider;
import com.hp.sdf.ngp.api.model.User;
import com.hp.sdf.ngp.api.search.SearchExpression;
import com.hp.sdf.ngp.api.search.Condition.DateComparer;
import com.hp.sdf.ngp.api.search.Condition.NumberComparer;
import com.hp.sdf.ngp.api.search.Condition.StringComparer;
import com.hp.sdf.ngp.custom.sbm.api.impl.model.ContentItemImpl;
import com.hp.sdf.ngp.custom.sbm.api.impl.model.PurchaseHistoryExtendImpl;
import com.hp.sdf.ngp.custom.sbm.api.storeclient.model.ContentItem;
import com.hp.sdf.ngp.custom.sbm.api.storeclient.model.PurchaseHistoryExtend;
import com.hp.sdf.ngp.custom.sbm.api.storeclient.service.ClientContentService;
import com.hp.sdf.ngp.custom.sbm.api.storeclient.service.ContentCatalogService;
import com.hp.sdf.ngp.search.condition.contentitem.ContentItemAssetIdCondition;
import com.hp.sdf.ngp.search.condition.contentitem.ContentItemItemIdCondition;
import com.hp.sdf.ngp.search.condition.purchasehistoryextend.PurchaseHistoryExtendAssetIdCondition;
import com.hp.sdf.ngp.search.condition.purchasehistoryextend.PurchaseHistoryExtendCompleteDateCondition;
import com.hp.sdf.ngp.search.condition.purchasehistoryextend.PurchaseHistoryExtendItemIdCondition;
import com.hp.sdf.ngp.search.condition.securitytoken.SecurityTokenExpireDateCondtion;
import com.hp.sdf.ngp.search.engine.SearchExpressionImpl;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:META-INF/applicationContext-test.xml" })
public class TestContentCatalogService extends DBEnablerTestBase {

	@Resource
	private ContentCatalogService contentCatalogService;
	
	@Resource
	private ClientContentService clientContentService;

	// @Resource
	// private ApplicationService applicationService;
	//	
	// @Resource
	// private ContentItemDao contentItemDao;

	@Override
	public String dataSetFileName() {
		return "/data_init3.xml";
	}

	@Test
	public void testAddContentItem() throws DataSetException, SQLException, Exception {
		// // asset
		// com.hp.sdf.ngp.model.Asset asset = new com.hp.sdf.ngp.model.Asset();
		//
		// Status status = new Status("test_status");
		// applicationService.saveStatus(status);
		//
		// asset.setName("shaoping");
		// asset.setStatus(status);
		//
		// applicationService.saveAsset(asset);

		ContentItem contentItem = new ContentItemImpl();
		contentItem.setItemName("test_itemName");
		contentItem.setAssetId(1L);

		contentCatalogService.addContentItem(contentItem);

		ITable tableValue = databaseTester.getConnection().createQueryTable("result",
				"select a.id from contentItem a where a.itemName = 'test_itemName' and a.assetId = 1 and a.id = " + contentItem.getId());
		assertTrue(tableValue.getRowCount() == 1);
	}

	@Test
	public void testConstructContentItem() throws AssetCatalogServiceException {
		assertTrue(contentCatalogService.constructContentItem() instanceof ContentItem);

		ContentItem contentItem = contentCatalogService.constructContentItem();
		contentItem.setAssetId(1L);
		contentItem.setDisplayText("test_displayText");

		com.hp.sdf.ngp.custom.sbm.storeclient.model.ContentItem content_item = ((ContentItemImpl) contentItem).getContentItem();
		assertTrue(content_item.getAssetId() == 1L);
		assertEquals(content_item.getDisplayText(), "test_displayText");
	}

	@Test
	public void testDeleteBatchContentItem() throws DataSetException, SQLException, Exception {
		SearchExpression searchExpression = new SearchExpressionImpl();
		searchExpression.addCondition(new ContentItemAssetIdCondition(1L, NumberComparer.EQUAL));
		contentCatalogService.deleteBatchContentItem(searchExpression);

		ITable tableValue = databaseTester.getConnection().createQueryTable("result", "select a.id from contentItem a where a.assetId = 1 ");
		assertTrue(tableValue.getRowCount() == 0);
	}

	@Test
	public void testDeleteBatchPurchaseHistoryExtend() throws DataSetException, SQLException, Exception {
		SearchExpression searchExpression = new SearchExpressionImpl();
		searchExpression.addCondition(new PurchaseHistoryExtendAssetIdCondition(1L, NumberComparer.EQUAL));
		contentCatalogService.deleteBatchPurchaseHistoryExtend(searchExpression);

		ITable tableValue = databaseTester.getConnection().createQueryTable("result",
				"select a.eventId from PurchaseHistoryExtend a where a.assetId = 1 ");
		assertTrue(tableValue.getRowCount() == 0);
	}

	@Test
	public void testDeleteBatchSecurityToken() throws DataSetException, SQLException, Exception {
		SearchExpression searchExpression = new SearchExpressionImpl();
		searchExpression.addCondition(new SecurityTokenExpireDateCondtion(Calendar.getInstance().getTime(), DateComparer.LESS_THAN));
		contentCatalogService.deleteBatchSecurityToken(searchExpression);

		ITable tableValue = databaseTester.getConnection().createQueryTable("result",
				"select a.msisdn from SecurityToken a where a.expireTime < Now() ");
		assertTrue(tableValue.getRowCount() == 0);
	}

	@Test
	public void testDeleteContentItem() throws DataSetException, SQLException, Exception {
		contentCatalogService.deleteContentItem(1L);

		ITable tableValue = databaseTester.getConnection().createQueryTable("result", "select a.id from contentItem a where a.itemId = 1 ");
		assertTrue(tableValue.getRowCount() == 0);
	}

	@Test
	public void testSearchPurchaseHistory() throws Exception {
		PurchaseHistoryExtend purchaseHistoryExtend = new PurchaseHistoryExtendImpl();
		purchaseHistoryExtend.setEventId("101");
		purchaseHistoryExtend.setItemId("1");
		purchaseHistoryExtend.setOrderNo("5");
		purchaseHistoryExtend.setAssetId(1L);
		clientContentService.addPurchaseHistory(purchaseHistoryExtend);
		
		purchaseHistoryExtend = new PurchaseHistoryExtendImpl();
		purchaseHistoryExtend.setEventId("102");
		purchaseHistoryExtend.setOrderNo("5");
		purchaseHistoryExtend.setItemId("1");
		purchaseHistoryExtend.setAssetId(1L);
		clientContentService.addPurchaseHistory(purchaseHistoryExtend);
		
		purchaseHistoryExtend = new PurchaseHistoryExtendImpl();
		purchaseHistoryExtend.setEventId("103");
		purchaseHistoryExtend.setItemId("1");
		purchaseHistoryExtend.setOrderNo("5");
		purchaseHistoryExtend.setAssetId(2L);
		clientContentService.addPurchaseHistory(purchaseHistoryExtend);

		
		SearchExpression searchExpression = new SearchExpressionImpl();
		searchExpression.addCondition(new PurchaseHistoryExtendAssetIdCondition(1L, NumberComparer.EQUAL));
		searchExpression.addCondition(new PurchaseHistoryExtendItemIdCondition(1L, NumberComparer.EQUAL));

		List<PurchaseHistoryExtend> purchaseHistoryExtends = contentCatalogService.searchPurchaseHistory(searchExpression);
		assertTrue(purchaseHistoryExtends.size() == 2);
	}

	@Test
	public void testUpdateContentItem() throws DataSetException, SQLException, Exception {
		ContentItem contentItem = new ContentItemImpl();
		contentItem.setItemName("test_itemName");
		contentItem.setAssetId(1L);

		contentCatalogService.addContentItem(contentItem);
		ITable tableValue = databaseTester.getConnection().createQueryTable("result",
				"select a.id from contentItem a where a.itemName = 'test_itemName' and a.assetId = 1 and a.id = " + contentItem.getId());
		assertTrue(tableValue.getRowCount() == 1);

		contentItem.setItemName("new_itemName");
		contentCatalogService.updateContentItem(contentItem);

		tableValue = databaseTester.getConnection().createQueryTable("result",
				"select a.id from contentItem a where a.itemName = 'new_itemName' and a.assetId = 1 and a.id = " + contentItem.getId());
		assertTrue(tableValue.getRowCount() == 1);
	}

	@Test
	public void testDeleteRelationUserProvider() throws Exception {
		// bg 42
		AssetProvider provider = contentCatalogService.constructAssetProvider();
		provider.setName("Test");
		provider.setExternalId("CP00001");

		contentCatalogService.addAssetProvider(provider);

		User user = contentCatalogService.constructUser();
		user.setUserid("testUser");
		user.setPassword("AAAAAAAA");
		contentCatalogService.addUser(user);

		contentCatalogService.addUserToProvider("testUser", provider.getId());
		contentCatalogService.assignRole("testUser", "default");

		ITable tableValue = databaseTester.getConnection().createQueryTable("result",
				"select a.* from ContentProviderOperator a where a.userid = 'testUser' and a.assetProviderId = "+provider.getId());
		assertTrue(tableValue.getRowCount() == 1);

		//check round
	
		contentCatalogService.deleteUser("testUser");

		//round 3
		
		user = contentCatalogService.constructUser();
		user.setUserid("userTest");
		user.setPassword("BBBBBBBBBB");
		contentCatalogService.addUser(user);

		contentCatalogService.assignRole("userTest", "default");
		contentCatalogService.addUserToProvider("userTest", provider.getId());
		
		tableValue = databaseTester.getConnection().createQueryTable("result",
		"select a.* from ContentProviderOperator a where a.userid = 'userTest' and a.assetProviderId = "+provider.getId());
		assertTrue(tableValue.getRowCount() == 1);

	}

	// ----
	@Test
	public void testDeleteBatchContentItemWithMoreConditions() throws Exception {
		SearchExpression searchExpression = new SearchExpressionImpl();
		searchExpression.addCondition(new ContentItemAssetIdCondition(1L, NumberComparer.EQUAL));
		searchExpression.addCondition(new ContentItemItemIdCondition("1", StringComparer.EQUAL, false, false));
		contentCatalogService.deleteBatchContentItem(searchExpression);

		ITable tableValue = databaseTester.getConnection().createQueryTable("result",
				"select a.id from contentItem a where a.assetId = 1 and a.itemId = 1 ");
		assertTrue(tableValue.getRowCount() == 0);
	}

	@Test
	public void testDeleteBatchPurchaseHistoryExtendWithMoreConditions() throws Exception {
		SearchExpression searchExpression = new SearchExpressionImpl();
		searchExpression.addCondition(new PurchaseHistoryExtendAssetIdCondition(1L, NumberComparer.EQUAL));
		searchExpression.addCondition(new PurchaseHistoryExtendCompleteDateCondition(Calendar.getInstance().getTime(), DateComparer.LESS_THAN));
		contentCatalogService.deleteBatchPurchaseHistoryExtend(searchExpression);

		ITable tableValue = databaseTester.getConnection().createQueryTable("result",
				"select a.eventId from PurchaseHistoryExtend a where a.assetId = 1 ");
		assertTrue(tableValue.getRowCount() == 0);
	}

	@Test
	public void testDeleteNonexistentContentItem() throws Exception {
		contentCatalogService.deleteContentItem(11L);

		ITable tableValue = databaseTester.getConnection().createQueryTable("result", "select a.id from contentItem a where a.itemId = 11 ");
		assertTrue(tableValue.getRowCount() == 0);
	}

	// see also TestStoreProcedure.java
	@Ignore
	@Test
	public void testGenerateBinaryVersionIndex() throws Exception {
		// List<String> excludeStatus = new ArrayList<String>();
		// excludeStatus.add("provided");
		// excludeStatus.add("invalid");
		// Map<String, Date> maps =
		// contentCatalogService.generateBinaryVersionIndex(excludeStatus,
		// "test_source", "sampleAttributeDate");
		//		
		// assertTrue(maps.size() == 1);
	}
	
	@Test
	public void testCallProcedure() throws AssetCatalogServiceException{
		List<Object> list = new ArrayList<Object>();
		list.add(1001);
		list.add("01");
		contentCatalogService.callDatabaseProcedure("UpdateAssetVersionSummaryForInput", list);
	}
}

// $Id$