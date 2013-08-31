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

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.ITable;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.hp.sdf.ngp.DBEnablerTestBase;
import com.hp.sdf.ngp.api.assetcatalog.AssetCatalogService;
import com.hp.sdf.ngp.api.comon.EntityRelationShipType;
import com.hp.sdf.ngp.api.comon.EntityType;
import com.hp.sdf.ngp.api.exception.AssetCatalogServiceException;
import com.hp.sdf.ngp.api.exception.StoreClientServiceException;
import com.hp.sdf.ngp.api.exception.UserCommentsCensorFailException;
import com.hp.sdf.ngp.api.impl.search.SearchEngineImpl;
import com.hp.sdf.ngp.api.model.AssetCategory;
import com.hp.sdf.ngp.api.model.AssetComment;
import com.hp.sdf.ngp.api.model.BinaryVersion;
import com.hp.sdf.ngp.api.search.Condition;
import com.hp.sdf.ngp.api.search.ConditionDescriptor;
import com.hp.sdf.ngp.api.search.OrderBy;
import com.hp.sdf.ngp.api.search.OrderEnum;
import com.hp.sdf.ngp.api.search.SearchEngine;
import com.hp.sdf.ngp.api.search.SearchExpression;
import com.hp.sdf.ngp.api.search.Condition.DateComparer;
import com.hp.sdf.ngp.api.search.Condition.NumberComparer;
import com.hp.sdf.ngp.api.search.Condition.StringComparer;
import com.hp.sdf.ngp.api.search.condition.asset.AssetAssetCategoryIdConditionDescriptor;
import com.hp.sdf.ngp.api.search.condition.assetbinaryversion.AssetBinaryVersionAssetProviderIdConditionDescriptor;
import com.hp.sdf.ngp.api.search.condition.assetbinaryversion.AssetBinaryVersionExternalIdConditionDescriptor;
import com.hp.sdf.ngp.api.search.condition.assetbinaryversion.AssetBinaryVersionOwnerAssetParentIdConditionDescriptor;
import com.hp.sdf.ngp.api.search.condition.assetbinaryversion.AssetBinaryVersionStatusNameConditionDescriptor;
import com.hp.sdf.ngp.api.search.condition.eav.EavNumberConditionDescriptor;
import com.hp.sdf.ngp.api.search.condition.eav.EavStringConditionDescriptor;
import com.hp.sdf.ngp.api.search.orderby.BinaryVersionOrderBy;
import com.hp.sdf.ngp.custom.sbm.api.impl.model.PurchaseHistoryExtendImpl;
import com.hp.sdf.ngp.custom.sbm.api.impl.model.SecurityTokenImpl;
import com.hp.sdf.ngp.custom.sbm.api.search.condition.parentassetversionsummary.ParentAssetVersionSummaryAssetCategoryIdConditionDescriptor;
import com.hp.sdf.ngp.custom.sbm.api.search.condition.parentassetversionsummary.ParentAssetVersionSummaryBinaryVersionStatusNameConditionDescriptor;
import com.hp.sdf.ngp.custom.sbm.api.search.condition.parentassetversionsummary.ParentAssetVersionSummaryLowestPriceConditionDescriptor;
import com.hp.sdf.ngp.custom.sbm.api.search.condition.parentassetversionsummary.ParentAssetVersionSummaryNewArrivalFlagConditionDescriptor;
import com.hp.sdf.ngp.custom.sbm.api.search.condition.parentassetversionsummary.ParentAssetVersionSummaryPublishFlagConditionDescriptor;
import com.hp.sdf.ngp.custom.sbm.api.search.orderby.BinaryVersionOrderByExt;
import com.hp.sdf.ngp.custom.sbm.api.storeclient.model.ContentItem;
import com.hp.sdf.ngp.custom.sbm.api.storeclient.model.HandSetDevice;
import com.hp.sdf.ngp.custom.sbm.api.storeclient.model.ParentAssetVersionSummary;
import com.hp.sdf.ngp.custom.sbm.api.storeclient.model.PurchaseHistoryExtend;
import com.hp.sdf.ngp.custom.sbm.api.storeclient.model.SecurityToken;
import com.hp.sdf.ngp.custom.sbm.api.storeclient.service.ClientContentService;
import com.hp.sdf.ngp.model.Asset;
import com.hp.sdf.ngp.model.Tag;
import com.hp.sdf.ngp.search.condition.contentitem.ContentItemAssetExternalIdCondition;
import com.hp.sdf.ngp.search.condition.contentitem.ContentItemAssetIdCondition;
import com.hp.sdf.ngp.search.condition.handsetdevice.HandSetDeviceDeviceNameCondition;
import com.hp.sdf.ngp.search.condition.handsetdevice.HandSetDeviceFunctionFilterCondition;
import com.hp.sdf.ngp.search.condition.parentassetversionsummary.ParentAssetVersionSummaryAssetBinaryVersionNameEqualCondition;
import com.hp.sdf.ngp.search.condition.parentassetversionsummary.ParentAssetVersionSummaryAssetCategoryIdCondition;
import com.hp.sdf.ngp.search.condition.parentassetversionsummary.ParentAssetVersionSummaryNewArrivalFlagCondition;
import com.hp.sdf.ngp.search.condition.parentassetversionsummary.ParentAssetVersionSummaryPublishFlagCondition;
import com.hp.sdf.ngp.search.condition.parentassetversionsummary.ParentAssetVersionSummaryTagNameCondition;
import com.hp.sdf.ngp.search.condition.purchasehistoryextend.PurchaseHistoryExtendAssetIdCondition;
import com.hp.sdf.ngp.search.condition.purchasehistoryextend.PurchaseHistoryExtendCompleteDateCondition;
import com.hp.sdf.ngp.search.condition.purchasehistoryextend.PurchaseHistoryExtendEventIdCondition;
import com.hp.sdf.ngp.search.condition.purchasehistoryextend.PurchaseHistoryExtendStatusCondition;
import com.hp.sdf.ngp.search.condition.purchasehistoryextend.PurchaseHistoryExtendUserIdCondition;
import com.hp.sdf.ngp.search.condition.securitytoken.SecurityTokenExpireDateCondtion;
import com.hp.sdf.ngp.search.condition.securitytoken.SecurityTokenImsiCondition;
import com.hp.sdf.ngp.search.engine.SearchExpressionImpl;
import com.hp.sdf.ngp.search.order.eav.EavStringOrderExecutor;
import com.hp.sdf.ngp.service.ApplicationService;

@Component
class TempEavStringOrderExecutor extends EavStringOrderExecutor {

	@Override
	public String getEavAttributeName() {

		return "TempEav";
	}

	@Override
	protected OrderBy getOrderBy() {
		// TODO Auto-generated method stub
		return null;
	}

}

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:META-INF/applicationContext-test.xml" })
public class TestClientContentService extends DBEnablerTestBase {

	@Resource
	private TempEavStringOrderExecutor tempEavStringOrderExecutor;

	@Resource
	private ClientContentService clientContentService;

	@Resource
	private ApplicationService applicationService;

	@Resource
	private AssetCatalogService assetCatalogService;

	@Resource
	private SearchEngine searchEngine;

	@Override
	public String dataSetFileName() {
		return "/data_init3.xml";
	}

	@Resource
	private MockClass mockClass;

	@Test
	public void testCommentsWithCensorWords() {

		Asset asset = new Asset();
		asset.setName("sdf");
		asset.setBrief("sdf");
		asset.setStatus(this.applicationService.getStatusById(1L));
		this.applicationService.saveAsset(asset);
		AssetComment assetComment = null;
		try {
			assetComment = this.clientContentService.constructAssetComment();
		} catch (Throwable e) {
			Assert.assertTrue(false);
		}
		assetComment.setAssetId(1L);
		assetComment.setAssetVersion("1");
		assetComment.setComment("fuck");
		assetComment.setTitle("");

		try {
			this.clientContentService.saveOrUpdateAssetComment(assetComment);
			Assert.assertTrue(false);
		} catch (UserCommentsCensorFailException e) {

		} catch (Throwable e) {
			Throwable able = e;
			Assert.assertTrue(false);
		}
		assetComment.setComment("sdfsfd");
		try {
			this.clientContentService.saveOrUpdateAssetComment(assetComment);
			Assert.assertTrue(true);
		} catch (UserCommentsCensorFailException e) {
			Assert.assertTrue(false);
		} catch (Throwable e) {
			Throwable able = e;
			Assert.assertTrue(false);
		}
		assetComment.setComment("sdffucksfd");
		try {
			this.clientContentService.saveOrUpdateAssetComment(assetComment);
			Assert.assertTrue(false);
		} catch (UserCommentsCensorFailException e) {
			Assert.assertTrue(true);
		} catch (Throwable e) {
			Throwable able = e;
			Assert.assertTrue(false);
		}
		assetComment.setComment("sfdsf");
		assetComment.setTitle("fuck");
		try {
			this.clientContentService.saveOrUpdateAssetComment(assetComment);
			Assert.assertTrue(false);
		} catch (UserCommentsCensorFailException e) {
			Assert.assertTrue(true);
		} catch (Throwable e) {
			Throwable able = e;
			Assert.assertTrue(false);
		}

	}

	@Test
	public void testConstructPurchaseHistoryExtend() throws StoreClientServiceException {
		assertTrue(clientContentService.constructPurchaseHistoryExtend() instanceof PurchaseHistoryExtend);

		Date reqConfirmDate = Calendar.getInstance().getTime();
		PurchaseHistoryExtend purchaseHistoryExtend = clientContentService.constructPurchaseHistoryExtend();
		purchaseHistoryExtend.setAssetId(1);
		purchaseHistoryExtend.setReqConfirmDate(reqConfirmDate);

		com.hp.sdf.ngp.custom.sbm.storeclient.model.PurchaseHistoryExtend purchaseHistory = ((PurchaseHistoryExtendImpl) purchaseHistoryExtend)
				.getPurchaseHistoryExtend();
		assertEquals(purchaseHistory.getReqconfirmDate(), reqConfirmDate);
		assertTrue(purchaseHistory.getAssetId() == 1);
	}

	@Test
	public void testAddPurchaseHistory() throws DataSetException, SQLException, Exception {
		PurchaseHistoryExtend purchaseHistoryExtend = new PurchaseHistoryExtendImpl();
		purchaseHistoryExtend.setEventId("101");
		purchaseHistoryExtend.setOrderNo("5");
		purchaseHistoryExtend.setAssetId(3L);

		clientContentService.addPurchaseHistory(purchaseHistoryExtend);

		ITable tableValue = databaseTester.getConnection().createQueryTable("result",
				"select a.* from PurchaseHistoryExtend a where a.assetId = 3 and a.orderNo = 5 ");
		assertTrue(tableValue.getRowCount() == 1);
	}

	@Test
	public void testUpdatePurchaseHistory() throws DataSetException, SQLException, Exception {
		
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
		List<PurchaseHistoryExtend> purchaseHistoryExtends = clientContentService.searchPurchaseHistory(searchExpression);
		 purchaseHistoryExtend = purchaseHistoryExtends.get(0);
		purchaseHistoryExtend.setMsisdn("test_msisdn");
		purchaseHistoryExtend.setItemId("33");
		// purchaseHistoryExtend.setReqConfirmDetailCode("eeeweewwe32");

		clientContentService.updatePurchaseHistory(purchaseHistoryExtend);
		ITable tableValue = databaseTester.getConnection().createQueryTable(
				"result",
				"select a.* from PurchaseHistoryExtend a where a.msisdn = 'test_msisdn' and a.itemId = 33 and a.eventId = "
						+ purchaseHistoryExtend.getEventId());
		assertTrue(tableValue.getRowCount() == 1);
	}

	@Test
	public void testSearchPurchaseHistory() throws DataSetException, SQLException, Exception {
		
		PurchaseHistoryExtend purchaseHistoryExtend = new PurchaseHistoryExtendImpl();
		purchaseHistoryExtend.setEventId("101");
		purchaseHistoryExtend.setItemId("1");
		purchaseHistoryExtend.setOrderNo("5");
		purchaseHistoryExtend.setAssetId(1L);
		purchaseHistoryExtend.setStatus("provided");
		clientContentService.addPurchaseHistory(purchaseHistoryExtend);
		
		purchaseHistoryExtend = new PurchaseHistoryExtendImpl();
		purchaseHistoryExtend.setEventId("102");
		purchaseHistoryExtend.setOrderNo("5");
		purchaseHistoryExtend.setItemId("1");
		purchaseHistoryExtend.setAssetId(1L);
		purchaseHistoryExtend.setStatus("testing");
		clientContentService.addPurchaseHistory(purchaseHistoryExtend);
		
		purchaseHistoryExtend = new PurchaseHistoryExtendImpl();
		purchaseHistoryExtend.setEventId("103");
		purchaseHistoryExtend.setItemId("1");
		purchaseHistoryExtend.setOrderNo("5");
		purchaseHistoryExtend.setAssetId(2L);
		purchaseHistoryExtend.setStatus("provided");
		clientContentService.addPurchaseHistory(purchaseHistoryExtend);
		
		SearchExpression searchExpression = new SearchExpressionImpl();
		searchExpression.addCondition(new PurchaseHistoryExtendAssetIdCondition(1L, NumberComparer.EQUAL));

		// searchExpression.addCondition(new
		// PurchaseHistoryExtendEventIdCondition("1",StringComparer.NOT_EQUAL,false,false));
		searchExpression.addCondition(new PurchaseHistoryExtendStatusCondition("provided", StringComparer.NOT_EQUAL, false, false));
		searchExpression.setFirst(0);
		searchExpression.setMax(1);

		List<PurchaseHistoryExtend> purchaseHistoryExtends = clientContentService.searchPurchaseHistory(searchExpression);
		assertTrue(purchaseHistoryExtends.size() == 1);
	}

	@Test
	public void testGetContentItem() throws StoreClientServiceException {
		ContentItem contentItem = clientContentService.getContentItem("2", "1");
		assertTrue(contentItem.getItemName().equals("shaoping"));

	}

	@Test
	public void testSearchContentItem() throws StoreClientServiceException {
		SearchExpression searchExpression = new SearchExpressionImpl();
		searchExpression.addCondition(new ContentItemAssetIdCondition(1L, NumberComparer.EQUAL));
		searchExpression.addCondition(new ContentItemAssetExternalIdCondition("1", StringComparer.EQUAL, false, false));

		List<ContentItem> contentItems = clientContentService.searchContentItem(searchExpression);
		assertTrue(contentItems.size() == 1);
	}

	@Test
	public void testIsClientTester() throws StoreClientServiceException {
		boolean flag = clientContentService.isClientTester("test_msisdn");
		assertTrue(flag);
	}

	@Test
	public void testAddSecurityToken() throws DataSetException, SQLException, Exception {
		SecurityToken securityToken = new SecurityTokenImpl();
		securityToken.setToken("test_token1");

		securityToken.setMsisdn("test_msisdn1");
		securityToken.setExpireTime(Calendar.getInstance().getTime());
		securityToken.setLockFlag(1);
		clientContentService.addSecurityToken(securityToken);

		ITable tableValue = databaseTester.getConnection().createQueryTable("result",
				"select a.* from securityToken a where a.token = 'test_token1' and a.msisdn = 'test_msisdn1' ");
		assertTrue(tableValue.getRowCount() == 1);

	}

	@Test
	public void testUpdateSecurityToken() throws DataSetException, SQLException, Exception {
		SecurityToken securityToken = new SecurityTokenImpl();
		securityToken.setMsisdn("test_msisdn2");
		securityToken.setExpireTime(Calendar.getInstance().getTime());
		securityToken.setLockFlag(1);

		securityToken.setToken("token1");
		clientContentService.updateSecurityToken(securityToken);
		ITable tableValue = databaseTester.getConnection().createQueryTable("result",
				"select a.* from securityToken a where a.lockFlag = 1 and a.msisdn = 'test_msisdn2' ");
		assertTrue(tableValue.getRowCount() == 1);
	}

	@Test
	public void testDeleteSecurityToken() throws DataSetException, SQLException, Exception {
		clientContentService.deleteSecurityToken("token1");

		ITable tableValue = databaseTester.getConnection().createQueryTable("result", "select a.* from securityToken a where a.msisdn = 'shaoping' ");
		assertTrue(tableValue.getRowCount() == 0);
	}

	@Test
	public void testGetSecurityToken() throws StoreClientServiceException {
		SecurityToken token = clientContentService.getSecurityToken("token2");
		assertEquals(token.getMsisdn(), "shaoping2");
	}

	@Test
	public void constructSecurityToken() throws StoreClientServiceException {
		assertTrue(clientContentService.constructSecurityToken() instanceof SecurityToken);

		SecurityToken securityToken = clientContentService.constructSecurityToken();
		securityToken.setLockFlag(1);
		securityToken.setMsisdn("test_msisdn");

		com.hp.sdf.ngp.custom.sbm.storeclient.model.SecurityToken security_token = ((SecurityTokenImpl) securityToken).getSecurityToken();
		assertTrue(security_token.getLockFlag() == 1);
		assertEquals(security_token.getMsisdn(), "test_msisdn");
	}

	@Test
	public void searchSecurityToken() throws Exception {
		SearchExpression searchExpression = new SearchExpressionImpl();
		searchExpression.addCondition(new SecurityTokenExpireDateCondtion(Calendar.getInstance().getTime(), DateComparer.LESS_THAN));
		searchExpression.addCondition(new SecurityTokenImsiCondition("hi", StringComparer.EQUAL, false, false));
		List<SecurityToken> securityTokens = clientContentService.searchSecurityToken(searchExpression);

		assertTrue(securityTokens.size() == 1);
		assertEquals(securityTokens.get(0).getUserId(), "2");
	}

	@Test
	public void testSearchHandSetDevice() throws StoreClientServiceException {
		SearchExpression searchExpression = new SearchExpressionImpl();
		searchExpression.addCondition(new HandSetDeviceDeviceNameCondition("app", StringComparer.LIKE, false, false));
		searchExpression.addCondition(new HandSetDeviceFunctionFilterCondition("filter", StringComparer.EQUAL, false, false));

		List<HandSetDevice> handSetDevices = clientContentService.searchHandSetDevice(searchExpression);
		assertTrue(handSetDevices.size() == 2);
		assertEquals(handSetDevices.get(0).getFunctionFilter(), "filter");
	}

	// ----

	@Test
	public void testAddPurchaseHistoryWithNonexistentAsset() throws Exception {
		PurchaseHistoryExtend purchaseHistoryExtend = new PurchaseHistoryExtendImpl();
		purchaseHistoryExtend.setEventId("102");
		purchaseHistoryExtend.setOrderNo("5");

		// no exist
		purchaseHistoryExtend.setAssetId(13L);

		try {
			clientContentService.addPurchaseHistory(purchaseHistoryExtend);
			Assert.assertTrue(false);
		} catch (Throwable e) {
			Assert.assertTrue(true);
		}

	}

	@Test
	public void testGetNonexistentContentItem() throws Exception {
		ContentItem contentItem = clientContentService.getContentItem("3", "1");
		assertTrue(contentItem == null);
	}

	@Test
	public void testSearchContentItemWithMoreConditions() throws Exception {
		SearchExpression searchExpression = new SearchExpressionImpl();
		searchExpression.addCondition(new ContentItemAssetIdCondition(1L, NumberComparer.EQUAL));
		searchExpression.addCondition(new ContentItemAssetExternalIdCondition("1", StringComparer.EQUAL, false, false));
		List<ContentItem> contentItems = clientContentService.searchContentItem(searchExpression);
		assertTrue(contentItems.size() == 1);
	}

	@Test
	public void testSearchPurchaseHistoryWithMoreConditions() throws Exception {
		
		PurchaseHistoryExtend purchaseHistoryExtend = new PurchaseHistoryExtendImpl();
		purchaseHistoryExtend.setEventId("101");
		purchaseHistoryExtend.setItemId("1");
		purchaseHistoryExtend.setOrderNo("5");
		purchaseHistoryExtend.setAssetId(1L);
		purchaseHistoryExtend.setUserId("1");
		clientContentService.addPurchaseHistory(purchaseHistoryExtend);
		
		purchaseHistoryExtend = new PurchaseHistoryExtendImpl();
		purchaseHistoryExtend.setEventId("102");
		purchaseHistoryExtend.setOrderNo("5");
		purchaseHistoryExtend.setItemId("1");
		purchaseHistoryExtend.setAssetId(1L);
		purchaseHistoryExtend.setUserId("1");
		clientContentService.addPurchaseHistory(purchaseHistoryExtend);
		
		purchaseHistoryExtend = new PurchaseHistoryExtendImpl();
		purchaseHistoryExtend.setEventId("103");
		purchaseHistoryExtend.setItemId("1");
		purchaseHistoryExtend.setOrderNo("5");
		purchaseHistoryExtend.setAssetId(2L);
		purchaseHistoryExtend.setUserId("1");
		clientContentService.addPurchaseHistory(purchaseHistoryExtend);
		
		SearchExpression searchExpression = new SearchExpressionImpl();
		searchExpression.addCondition(new PurchaseHistoryExtendAssetIdCondition(1L, NumberComparer.EQUAL));
		searchExpression.addCondition(new PurchaseHistoryExtendUserIdCondition("1", StringComparer.EQUAL, false, false));
		List<PurchaseHistoryExtend> purchaseHistoryExtends = clientContentService.searchPurchaseHistory(searchExpression);

		assertTrue(purchaseHistoryExtends.size() == 2);
	}

	@Test
	public void testSearchParentAssetVersionSummary() throws StoreClientServiceException {
		SearchExpression searchExpression = new SearchExpressionImpl();
		searchExpression.addCondition(new ParentAssetVersionSummaryPublishFlagCondition("flag", StringComparer.LIKE, false, false));
		searchExpression.addCondition(new ParentAssetVersionSummaryNewArrivalFlagCondition(1L, NumberComparer.EQUAL));

		List<ParentAssetVersionSummary> parentAssetVersionSummarys = clientContentService.searchParentAssetVersionSummary(searchExpression);

		assertTrue(parentAssetVersionSummarys.size() == 1);
		assertTrue(parentAssetVersionSummarys.get(0).getSampleFlag() == 0);
	}

	@Test
	public void testSearchBinaryVersionsV2() throws Exception {
		// with these condition ,null

		SearchExpression searchExpression = new SearchExpressionImpl();
		SearchEngine eg = new SearchEngineImpl();

		searchExpression.addCondition(eg.createCondition(new AssetBinaryVersionOwnerAssetParentIdConditionDescriptor(46001L, NumberComparer.EQUAL)));

		EavStringConditionDescriptor eavFunctionFilter = new EavStringConditionDescriptor(EntityType.BINARYVERSION, "PUBLISHFLG", "02",
				StringComparer.EQUAL, false, false);
		searchExpression.addCondition(eg.createCondition(eavFunctionFilter));

		AssetBinaryVersionStatusNameConditionDescriptor statusNamedesc1 = new AssetBinaryVersionStatusNameConditionDescriptor("SBM_WAIT",
				StringComparer.EQUAL, false, false);
		Condition statusName = eg.createCondition(statusNamedesc1);

		AssetBinaryVersionStatusNameConditionDescriptor statusNamedesc2 = new AssetBinaryVersionStatusNameConditionDescriptor("TEST_WAIT",
				StringComparer.EQUAL, false, false);
		statusName.or(eg.createCondition(statusNamedesc2));

		AssetBinaryVersionStatusNameConditionDescriptor statusNamedesc3 = new AssetBinaryVersionStatusNameConditionDescriptor("SUBMIT_WAIT",
				StringComparer.EQUAL, false, false);
		statusName.or(eg.createCondition(statusNamedesc3));

		AssetBinaryVersionStatusNameConditionDescriptor statusNamedesc4 = new AssetBinaryVersionStatusNameConditionDescriptor("PrePublic",
				StringComparer.EQUAL, false, false);
		statusName.or(eg.createCondition(statusNamedesc4));

		// searchExpression.addCondition(statusName);

		searchExpression.addCondition(eg.createCondition(new AssetBinaryVersionAssetProviderIdConditionDescriptor(5L, NumberComparer.EQUAL)));

		searchExpression.addOrder(BinaryVersionOrderBy.PROVIDERPRIORITY, OrderEnum.ASC);
		searchExpression.addOrder(BinaryVersionOrderByExt.SERIAL, OrderEnum.ASC);

		List<BinaryVersion> bvs = clientContentService.searchBinaryVersions(searchExpression);
		bvs.size();

		// BG75

	}

	@Test
	public void testSearchParentAssetVersionSummaryByAssetCatId() throws StoreClientServiceException {

		SearchExpression searchExpression = new SearchExpressionImpl();

		searchExpression.addCondition(new ParentAssetVersionSummaryAssetCategoryIdCondition(1L, NumberComparer.EQUAL));

		searchExpression.addCondition(new ParentAssetVersionSummaryPublishFlagCondition("flag", StringComparer.LIKE, false, false));

		List<ParentAssetVersionSummary> parentAssetVersionSummarys = clientContentService.searchParentAssetVersionSummary(searchExpression);

		assertTrue(parentAssetVersionSummarys.size() == 1);
	}

	@Test
	public void testSearchParentAssetVersionSummaryWithJoin() throws StoreClientServiceException {

		SearchExpression searchExpression = new SearchExpressionImpl();

		SearchEngine searchEngineJndiObject = new SearchEngineImpl();

		EavStringConditionDescriptor eavMimeTypeFilter = new EavStringConditionDescriptor(
				EntityRelationShipType.PARENTASSETVERSIONSUMMARY_BINARYVERSION, EntityType.BINARYVERSION, "MIMETYPEFILTER", "a",
				StringComparer.EQUAL, false, false);
		searchExpression.addCondition(searchEngineJndiObject.createCondition(eavMimeTypeFilter));

		EavStringConditionDescriptor eavFunctionFilter = new EavStringConditionDescriptor(
				EntityRelationShipType.PARENTASSETVERSIONSUMMARY_BINARYVERSION, EntityType.BINARYVERSION, "FUNCTIONFILTER", "f",
				StringComparer.EQUAL, false, false);
		searchExpression.addCondition(searchEngineJndiObject.createCondition(eavFunctionFilter));

		EavNumberConditionDescriptor<Long> eavNumberDesc = new EavNumberConditionDescriptor<Long>(
				EntityRelationShipType.PARENTASSETVERSIONSUMMARY_BINARYVERSION, EntityType.BINARYVERSION, "RESOLUTIONFILTER", 1L,
				NumberComparer.LESS_THAN);// NumberComparer.LESS_EQUAL

		searchExpression.addCondition(searchEngineJndiObject.createCondition(eavNumberDesc));

		ParentAssetVersionSummaryPublishFlagConditionDescriptor publishFlagDesc = new ParentAssetVersionSummaryPublishFlagConditionDescriptor("02",
				StringComparer.EQUAL, false, false);
		searchExpression.addCondition(searchEngineJndiObject.createCondition(publishFlagDesc));

		AssetBinaryVersionStatusNameConditionDescriptor statusNamedesc1 = new AssetBinaryVersionStatusNameConditionDescriptor("SBM_WAIT",
				StringComparer.EQUAL, false, false);
		Condition statusName = searchEngineJndiObject.createCondition(statusNamedesc1);

		AssetBinaryVersionStatusNameConditionDescriptor statusNamedesc2 = new AssetBinaryVersionStatusNameConditionDescriptor("TEST_WAIT",
				StringComparer.EQUAL, false, false);
		statusName.or(searchEngineJndiObject.createCondition(statusNamedesc2));

		AssetBinaryVersionStatusNameConditionDescriptor statusNamedesc3 = new AssetBinaryVersionStatusNameConditionDescriptor("SUBMIT_WAIT",
				StringComparer.EQUAL, false, false);
		statusName.or(searchEngineJndiObject.createCondition(statusNamedesc3));

		AssetBinaryVersionStatusNameConditionDescriptor statusNamedesc4 = new AssetBinaryVersionStatusNameConditionDescriptor("PrePublic",
				StringComparer.EQUAL, false, false);
		statusName.or(searchEngineJndiObject.createCondition(statusNamedesc4));

		// searchExpression.addCondition(statusName);

		ParentAssetVersionSummaryAssetCategoryIdConditionDescriptor categoryDesc = new ParentAssetVersionSummaryAssetCategoryIdConditionDescriptor(
				1L, NumberComparer.EQUAL);
		searchExpression.addCondition(searchEngineJndiObject.createCondition(categoryDesc));

		clientContentService.searchParentAssetVersionSummary(searchExpression);

	}

	@Test
	public void testSearchBinaryVersionsWithUnSupportCompare() throws DataSetException, SQLException, Exception {
		SearchExpression searchExpression = new SearchExpressionImpl();

		SearchEngine searchEngineJndiObject = new SearchEngineImpl();

		EavStringConditionDescriptor eavMimeTypeFilter = new EavStringConditionDescriptor(
				EntityRelationShipType.PARENTASSETVERSIONSUMMARY_BINARYVERSION, EntityType.BINARYVERSION, "MIMETYPEFILTER", "a",
				StringComparer.EQUAL, false, false);
		searchExpression.addCondition(searchEngineJndiObject.createCondition(eavMimeTypeFilter));

		EavStringConditionDescriptor eavFunctionFilter = new EavStringConditionDescriptor(
				EntityRelationShipType.PARENTASSETVERSIONSUMMARY_BINARYVERSION, EntityType.BINARYVERSION, "FUNCTIONFILTER", "f",
				StringComparer.EQUAL, false, false);
		searchExpression.addCondition(searchEngineJndiObject.createCondition(eavFunctionFilter));

		EavNumberConditionDescriptor<Long> eavNumberDesc = new EavNumberConditionDescriptor<Long>(
				EntityRelationShipType.PARENTASSETVERSIONSUMMARY_BINARYVERSION, EntityType.BINARYVERSION, "RESOLUTIONFILTER", 1L,
				NumberComparer.LESS_THAN);// LESS_THAN[OK]
		// NumberComparer.LESS_EQUAL[error]

		searchExpression.addCondition(searchEngineJndiObject.createCondition(eavNumberDesc));

		clientContentService.searchParentAssetVersionSummary(searchExpression);

	}

	@Test
	public void testSearchBinaryVersionsByEavAndOrder() throws DataSetException, SQLException, Exception {

		// TODO BUG 21; search and order by EAV
		SearchExpression searchExpression = new SearchExpressionImpl();
		SearchEngine searchEngine = new SearchEngineImpl();

		// add eav
		assetCatalogService.addAttribute(EntityType.BINARYVERSION, 1L, "testEavAndOrder", "testEavAndOrderValue");

		// test search
		ConditionDescriptor conditionDescriptor = new EavStringConditionDescriptor(EntityType.BINARYVERSION, "testEavAndOrder",
				"testEavAndOrderValue", Condition.StringComparer.EQUAL, false, false);
		searchExpression.addCondition(searchEngine.createCondition(conditionDescriptor));

		/**
		 * Calendar c = Calendar.getInstance(); Date getImageDate = c.getTime();
		 * 
		 * ConditionDescriptor getImageDateConditionDescriptor = new
		 * EavDateConditionDescriptor(EntityType.BINARYVERSION, "GETIMAGEDATE",
		 * getImageDate, Condition.DateComparer.LESS_THAN);
		 * 
		 * Condition getImageDateCondition =
		 * searchEngine.createCondition(getImageDateConditionDescriptor);
		 * searchExpression.addCondition(getImageDateCondition);
		 * 
		 * searchExpression.addOrder(BinaryVersionOrderByExt.GETIMAGEDATE,
		 * OrderEnum.ASC);
		 ***/

		// List<BinaryVersion> contentsList =
		assetCatalogService.searchBinaryVersions(searchExpression);

		// assertTrue(contentsList.size() == 1);
	}

	@Test
	public void testSearchParentAssetVersionSummaryEAV() throws Exception {
		this.assetCatalogService.addAttribute(EntityType.BINARYVERSION, 1L, "MIMETYPEFILTER", "1");
		this.assetCatalogService.addAttribute(EntityType.BINARYVERSION, 1L, "FUNCTIONFILTER", "2");
		this.assetCatalogService.addAttribute(EntityType.BINARYVERSION, 1L, "RESOLUTIONFILTER", 3F);
		this.assetCatalogService.addAttribute(EntityType.BINARYVERSION, 1L, "RESOLUTIONFILTER", 3F);

		EavStringConditionDescriptor eavMimeTypeFilter = new EavStringConditionDescriptor(
				EntityRelationShipType.PARENTASSETVERSIONSUMMARY_BINARYVERSION, EntityType.BINARYVERSION, "MIMETYPEFILTER", "1",
				StringComparer.EQUAL, false, false);
		EavStringConditionDescriptor eavFunctionFilter = new EavStringConditionDescriptor(
				EntityRelationShipType.PARENTASSETVERSIONSUMMARY_BINARYVERSION, EntityType.BINARYVERSION, "FUNCTIONFILTER", "2",
				StringComparer.EQUAL, false, false);
		EavNumberConditionDescriptor<Long> eavNumberDesc = new EavNumberConditionDescriptor<Long>(
				EntityRelationShipType.PARENTASSETVERSIONSUMMARY_BINARYVERSION, EntityType.BINARYVERSION, "RESOLUTIONFILTER", 3L,
				NumberComparer.LESS_EQUAL);
		ParentAssetVersionSummaryPublishFlagConditionDescriptor publishFlagDesc = new ParentAssetVersionSummaryPublishFlagConditionDescriptor(
				"flag1", StringComparer.EQUAL, false, false);
		ParentAssetVersionSummaryBinaryVersionStatusNameConditionDescriptor statusNamedesc1 = new ParentAssetVersionSummaryBinaryVersionStatusNameConditionDescriptor(
				"testing", StringComparer.EQUAL, false, false);
		SearchExpression searchExpression = searchEngine.createSearchExpression();
		searchExpression.addCondition(searchEngine.createCondition(eavMimeTypeFilter));
		searchExpression.addCondition(searchEngine.createCondition(eavFunctionFilter));
		searchExpression.addCondition(searchEngine.createCondition(eavNumberDesc));
		searchExpression.addCondition(searchEngine.createCondition(publishFlagDesc));
		searchExpression.addCondition(searchEngine.createCondition(statusNamedesc1));
		List<ParentAssetVersionSummary> parentAssetVersionSummaries = this.clientContentService.searchParentAssetVersionSummary(searchExpression);
		Assert.assertTrue(parentAssetVersionSummaries.size() == 1);

	}

	@Test
	public void testSearchParentAssetVersionSummaryEAV2() throws Exception {
		this.assetCatalogService.addAttribute(EntityType.BINARYVERSION, 1L, "MIMETYPEFILTER", "1");
		this.assetCatalogService.addAttribute(EntityType.BINARYVERSION, 1L, "FUNCTIONFILTER", "2");
		this.assetCatalogService.addAttribute(EntityType.BINARYVERSION, 1L, "RESOLUTIONFILTER", 4F);
		this.assetCatalogService.addAttribute(EntityType.BINARYVERSION, 1L, "RESOLUTIONFILTER", 4F);

		EavStringConditionDescriptor eavMimeTypeFilter = new EavStringConditionDescriptor(
				EntityRelationShipType.PARENTASSETVERSIONSUMMARY_BINARYVERSION, EntityType.BINARYVERSION, "MIMETYPEFILTER", "1",
				StringComparer.EQUAL, false, false);
		EavStringConditionDescriptor eavFunctionFilter = new EavStringConditionDescriptor(
				EntityRelationShipType.PARENTASSETVERSIONSUMMARY_BINARYVERSION, EntityType.BINARYVERSION, "FUNCTIONFILTER", "2",
				StringComparer.EQUAL, false, false);
		EavNumberConditionDescriptor<Long> eavNumberDesc = new EavNumberConditionDescriptor<Long>(
				EntityRelationShipType.PARENTASSETVERSIONSUMMARY_BINARYVERSION, EntityType.BINARYVERSION, "RESOLUTIONFILTER", 3L,
				NumberComparer.LESS_EQUAL);
		ParentAssetVersionSummaryPublishFlagConditionDescriptor publishFlagDesc = new ParentAssetVersionSummaryPublishFlagConditionDescriptor(
				"flag1", StringComparer.EQUAL, false, false);
		ParentAssetVersionSummaryBinaryVersionStatusNameConditionDescriptor statusNamedesc1 = new ParentAssetVersionSummaryBinaryVersionStatusNameConditionDescriptor(
				"testing", StringComparer.EQUAL, false, false);
		SearchExpression searchExpression = searchEngine.createSearchExpression();
		searchExpression.addCondition(searchEngine.createCondition(eavMimeTypeFilter));
		searchExpression.addCondition(searchEngine.createCondition(eavFunctionFilter));
		searchExpression.addCondition(searchEngine.createCondition(eavNumberDesc));
		searchExpression.addCondition(searchEngine.createCondition(publishFlagDesc));
		searchExpression.addCondition(searchEngine.createCondition(statusNamedesc1));
		List<ParentAssetVersionSummary> parentAssetVersionSummaries = this.clientContentService.searchParentAssetVersionSummary(searchExpression);
		Assert.assertTrue(parentAssetVersionSummaries.size() == 0);

	}

	@Test
	public void testSearchParentAssetVersionSummaryEAV3() throws Exception {
		this.assetCatalogService.addAttribute(EntityType.BINARYVERSION, 1L, "DISPLAYORDER", 1f);
		this.assetCatalogService.addAttribute(EntityType.BINARYVERSION, 2L, "DISPLAYORDER", 2f);
		this.assetCatalogService.addAttribute(EntityType.BINARYVERSION, 3L, "DISPLAYORDER", 3f);
		SearchExpression searchExpression = searchEngine.createSearchExpression();
		searchExpression.addOrder(BinaryVersionOrderByExt.SERIAL, OrderEnum.DESC);
		List<BinaryVersion> list = this.assetCatalogService.searchBinaryVersions(searchExpression);
		Assert.assertTrue(list.size() == 3);
		Assert.assertTrue(list.get(0).getId() == 3);
		Assert.assertTrue(list.get(1).getId() == 2);
		Assert.assertTrue(list.get(2).getId() == 1);

		this.assetCatalogService.addAttribute(EntityType.BINARYVERSION, 1L, "CPPRIORITY", 1f);
		this.assetCatalogService.addAttribute(EntityType.BINARYVERSION, 2L, "CPPRIORITY", 2f);
		this.assetCatalogService.addAttribute(EntityType.BINARYVERSION, 3L, "CPPRIORITY", 3f);
		searchExpression = searchEngine.createSearchExpression();
		searchExpression.addOrder(BinaryVersionOrderByExt.ASSETPROVIDERPRIORITY, OrderEnum.DESC);
		list = this.assetCatalogService.searchBinaryVersions(searchExpression);
		Assert.assertTrue(list.size() == 3);
		Assert.assertTrue(list.get(0).getId() == 3);
		Assert.assertTrue(list.get(1).getId() == 2);
		Assert.assertTrue(list.get(2).getId() == 1);

		Calendar calendar1 = Calendar.getInstance();
		calendar1.add(Calendar.MINUTE, -3);
		Calendar calendar2 = Calendar.getInstance();
		calendar2.add(Calendar.MINUTE, -2);

		Calendar calendar3 = Calendar.getInstance();
		calendar3.add(Calendar.MINUTE, -1);

		this.assetCatalogService.addAttribute(EntityType.BINARYVERSION, 1L, "GETIMAGEDATE", calendar1.getTime());
		this.assetCatalogService.addAttribute(EntityType.BINARYVERSION, 2L, "GETIMAGEDATE", calendar2.getTime());
		this.assetCatalogService.addAttribute(EntityType.BINARYVERSION, 3L, "GETIMAGEDATE", calendar3.getTime());
		searchExpression = searchEngine.createSearchExpression();
		searchExpression.addOrder(BinaryVersionOrderByExt.GETIMAGEDATE, OrderEnum.DESC);
		list = this.assetCatalogService.searchBinaryVersions(searchExpression);
		Assert.assertTrue(list.size() == 3);
		Assert.assertTrue(list.get(0).getId() == 3);
		Assert.assertTrue(list.get(1).getId() == 2);
		Assert.assertTrue(list.get(2).getId() == 1);

		this.assetCatalogService.addAttribute(EntityType.BINARYVERSION, 1L, this.tempEavStringOrderExecutor.getEavAttributeName(), "1");
		this.assetCatalogService.addAttribute(EntityType.BINARYVERSION, 2L, this.tempEavStringOrderExecutor.getEavAttributeName(), "2");
		this.assetCatalogService.addAttribute(EntityType.BINARYVERSION, 3L, this.tempEavStringOrderExecutor.getEavAttributeName(), "3");
		searchExpression = searchEngine.createSearchExpression();
		tempEavStringOrderExecutor.execute(null, OrderEnum.DESC, (SearchExpressionImpl) searchExpression);
		// searchExpression.addOrder(BinaryVersionOrderByExt.GETIMAGEDATE,
		// OrderEnum.DESC);
		list = this.assetCatalogService.searchBinaryVersions(searchExpression);
		Assert.assertTrue(list.size() == 3);
		Assert.assertTrue(list.get(0).getId() == 3);
		Assert.assertTrue(list.get(1).getId() == 2);
		Assert.assertTrue(list.get(2).getId() == 1);

	}

	@Test
	public void testSearchBug101() throws Exception {
		SearchExpression searchExpression = searchEngine.createSearchExpression();

		// 専嶕忦審丗丂(慡偰偺忦審偼AND偱楢寢偝傟傞)
		// 夝憸搙偺僼傿儖僞
		EavNumberConditionDescriptor eavResolutionFilter = new EavNumberConditionDescriptor(
				EntityRelationShipType.PARENTASSETVERSIONSUMMARY_BINARYVERSION, EntityType.BINARYVERSION, "RESOLUTIONFILTER", "3",
				NumberComparer.LESS_THAN);
		Condition eavResolutionCondition = createCondition(searchEngine, eavResolutionFilter, "resolutionFilter=".concat(""));

		EavNumberConditionDescriptor eavResolutionFilterEq = new EavNumberConditionDescriptor(
				EntityRelationShipType.PARENTASSETVERSIONSUMMARY_BINARYVERSION, EntityType.BINARYVERSION, "RESOLUTIONFILTER", "3",
				NumberComparer.EQUAL);
		eavResolutionCondition.or(createCondition(searchEngine, eavResolutionFilterEq, "resolutionFilter=".concat("")));

		// 偦偺懠僼傿儖僞
		EavStringConditionDescriptor eavFunctionFilter = new EavStringConditionDescriptor(
				EntityRelationShipType.PARENTASSETVERSIONSUMMARY_BINARYVERSION, EntityType.BINARYVERSION, "FUNCTIONFILTER", "4",
				StringComparer.EQUAL, false, false);
		Condition condition = createCondition(searchEngine, eavFunctionFilter, "functionFilter=".concat(""));
		condition.and(eavResolutionCondition);

		// MIMETYPE
		Condition eavMimeTypeCond = null;
		List<String> mimeTypes = new ArrayList<String>();
		mimeTypes.add("json");
		mimeTypes.add("xml");
		if (mimeTypes != null) {
			for (String mimeType : mimeTypes) {
				EavStringConditionDescriptor eavMimeTypeFilter = new EavStringConditionDescriptor(
						EntityRelationShipType.PARENTASSETVERSIONSUMMARY_BINARYVERSION, EntityType.BINARYVERSION, "MIMETYPEFILTER", mimeType,
						StringComparer.EQUAL, false, false);
				Condition eavMimeTypeCondition = createCondition(searchEngine, eavMimeTypeFilter, "mimeType=".concat(mimeType));
				if (eavMimeTypeCond == null) {
					eavMimeTypeCond = eavMimeTypeCondition;
				} else {
					eavMimeTypeCond.or(eavMimeTypeCondition);
				}
			}
		}
		if (eavMimeTypeCond != null) {
			condition.and(eavMimeTypeCond);
		}
		boolean isCache = true;
		// 僼傿儖僞儕儞僌僼儔僌
		if (isCache) {
			EavStringConditionDescriptor eavFilterFlagDesc = new EavStringConditionDescriptor(
					EntityRelationShipType.PARENTASSETVERSIONSUMMARY_BINARYVERSION, EntityType.BINARYVERSION, "FILTERFLAG", "0",
					StringComparer.EQUAL, false, false);
			Condition eavFilterFlagCond = createCondition(searchEngine, eavFilterFlagDesc, "filterFlag=0");
			condition.and(eavFilterFlagCond);
		}
		List<Long> generIds = new ArrayList<Long>();
		generIds.add(new Long(101));
		// 巜掕偝傟偨僕儍儞儖偲丄偦偺壓埵奒憌偺僕儍儞儖偺専嶕忦審偑丄僆傾忦審偲偡傞
		if (generIds != null) {
			Condition generIdCondition = null;
			for (Long id : generIds) {
				AssetAssetCategoryIdConditionDescriptor categoryDesc = new AssetAssetCategoryIdConditionDescriptor(id, NumberComparer.EQUAL);
				Condition anotherCondition = createCondition(searchEngine, categoryDesc, "assetCategoryId=".concat(id.toString()));
				if (generIdCondition != null) {
					generIdCondition.or(anotherCondition);
				} else {
					generIdCondition = anotherCondition;
				}
			}
			condition.and(generIdCondition);
		}

		String pid = "5";
		boolean isClientTester = true;

		// 岞奐敾抐僼儔僌
		EavStringConditionDescriptor publishFlag = new EavStringConditionDescriptor(EntityRelationShipType.PARENTASSETVERSIONSUMMARY_BINARYVERSION,
				EntityType.BINARYVERSION, "", (isClientTester ? "02" : "01"), StringComparer.EQUAL, false, false);
		Condition publishFlagCond = createCondition(searchEngine, publishFlag, "publishFlag=" + (isClientTester ? "02" : "01"));
		condition.and(publishFlagCond);

		// 僐儞僥儞僣僗僥乕僞僗
		if (isClientTester) {
			// "SBM_WAIT" OR "TEST_WAIT" OR "SUBMIT_WAIT" OR
			// "PrePublic"乮専徹抂枛偺応崌乯
			AssetBinaryVersionStatusNameConditionDescriptor statusNamedesc1 = new AssetBinaryVersionStatusNameConditionDescriptor("",
					StringComparer.EQUAL, false, false);
			Condition statusNameCond = createCondition(searchEngine, statusNamedesc1, "statusName=SBM_WAIT");

			AssetBinaryVersionStatusNameConditionDescriptor statusNamedesc2 = new AssetBinaryVersionStatusNameConditionDescriptor("",
					StringComparer.EQUAL, false, false);
			statusNameCond.or(createCondition(searchEngine, statusNamedesc2, "statusName=TEST_WAIT"));

			AssetBinaryVersionStatusNameConditionDescriptor statusNamedesc3 = new AssetBinaryVersionStatusNameConditionDescriptor("",
					StringComparer.EQUAL, false, false);
			statusNameCond.or(createCondition(searchEngine, statusNamedesc3, "statusName=SUBMIT_WAIT"));

			AssetBinaryVersionStatusNameConditionDescriptor statusNamedesc4 = new AssetBinaryVersionStatusNameConditionDescriptor("",
					StringComparer.EQUAL, false, false);
			statusNameCond.or(createCondition(searchEngine, statusNamedesc4, "statusName=PrePublic"));

			condition.and(statusNameCond);
		} else {
			// "PUBLISHED" 乮旕専徹抂枛偺応崌乯
			AssetBinaryVersionStatusNameConditionDescriptor statusNamedesc1 = new AssetBinaryVersionStatusNameConditionDescriptor("",
					StringComparer.EQUAL, false, false);
			Condition statusNameCond = createCondition(searchEngine, statusNamedesc1, "statusName=PUBLISHED");
			condition.and(statusNameCond);
		}
		boolean isCPClientTester = true;
		// CP専徹抂枛偺惂栺
		if (isCPClientTester) {
			AssetBinaryVersionAssetProviderIdConditionDescriptor providerIdDesc = new AssetBinaryVersionAssetProviderIdConditionDescriptor(Long
					.valueOf(pid), NumberComparer.EQUAL);
			Condition providerIdCond = this.createCondition(searchEngine, providerIdDesc, "providerId=".concat(pid));
			condition.and(providerIdCond);
		}

		searchExpression.addCondition(condition);

		// 僜乕僩忦審
		searchExpression.addOrder(BinaryVersionOrderByExt.VERSIONSUMMARY_DOWNLOADTIME, OrderEnum.DESC);

		clientContentService.searchParentAssetVersionSummary(searchExpression);
	}

	@Test
	public void testBug106() throws StoreClientServiceException {
		SearchExpression searchExpression = searchEngine.createSearchExpression();
		ParentAssetVersionSummaryNewArrivalFlagConditionDescriptor flag = new ParentAssetVersionSummaryNewArrivalFlagConditionDescriptor(Long
				.valueOf("1"), NumberComparer.EQUAL);
		searchExpression.addCondition(searchEngine.createCondition(flag));
		List<ParentAssetVersionSummary> list = clientContentService.searchParentAssetVersionSummary(searchExpression);
		Assert.assertTrue(list.size() == 1);
	}

	private Condition createCondition(SearchEngine searchEngine, ConditionDescriptor desc, String logParameter) throws Exception {
		Condition condition = searchEngine.createCondition(desc);
		return condition;
	}

	@Test
	public void testParentAsset() throws Exception {
		SearchExpression searchExpression = searchEngine.createSearchExpression();
		Condition c = new ParentAssetVersionSummaryAssetBinaryVersionNameEqualCondition();
		searchExpression.addCondition(c);
		List<ParentAssetVersionSummary> list = clientContentService.searchParentAssetVersionSummary(searchExpression);
		Assert.assertTrue(list.size() == 1);
	}

	@Test
	public void testParentAssetVersionSummaryLowestPriceConditionDescriptor() throws StoreClientServiceException {
		SearchExpression searchExpression = new SearchExpressionImpl();
		ParentAssetVersionSummaryLowestPriceConditionDescriptor flag = new ParentAssetVersionSummaryLowestPriceConditionDescriptor(Double
				.valueOf("1.0"), NumberComparer.EQUAL);
		searchExpression.addCondition(searchEngine.createCondition(flag));

		List<ParentAssetVersionSummary> parentAssetVersionSummarys = clientContentService.searchParentAssetVersionSummary(searchExpression);

		assertTrue(parentAssetVersionSummarys.size() == 1);
		// assertEquals(parentAssetVersionSummarys.get(0).getId(), 11L);
	}

	@Test
	public void testBug120() throws AssetCatalogServiceException {
		BinaryVersion bv = assetCatalogService.constructBinaryVersion();

		bv.setAssetId(1);
		bv.setAssetParentId(2);
		bv.setExpireDate(new Date());
		bv.setBrief("brief");
		bv.setName("name");
		bv.setExternalId("3");
		bv.setFileSize(100);
		List<String> list = new ArrayList<String>();
		list.add("game");
		bv.setTags(list);
		bv.setStatus("provided");

		Long bvId = assetCatalogService.addBinaryVersion(bv);
		BinaryVersion binaryVersion = assetCatalogService.getBinaryVersion(bvId);
		Set<String> tagSet = binaryVersion.getTags();

		assertEquals("game", tagSet.iterator().next());
		assetCatalogService.deleteBinaryVersion(bv.getId());

	}

	@Test
	public void testBug121() throws DataSetException, SQLException, Exception {
		PurchaseHistoryExtend purchaseHistoryExtend = new PurchaseHistoryExtendImpl();

		purchaseHistoryExtend.setStatus("published");
		purchaseHistoryExtend.setEventId("202");
		purchaseHistoryExtend.setOrderNo("5");
		purchaseHistoryExtend.setTempPaidDate(new Date());
		purchaseHistoryExtend.setMsisdn("msisdn");
		purchaseHistoryExtend.setPaidPrice(new BigDecimal(0));
		purchaseHistoryExtend.setAssetId(3L);
		purchaseHistoryExtend.setAssetExternalId("1");
		purchaseHistoryExtend.setUserId("userid");
		purchaseHistoryExtend.setVersion("1.1");
		purchaseHistoryExtend.setTempPaidResult("tempPaidResult");
		purchaseHistoryExtend.setTempPaidDetailCode("tempPaidDetailCode");

		clientContentService.addPurchaseHistory(purchaseHistoryExtend);

		ITable tableValue = databaseTester.getConnection()
				.createQueryTable("result", "select a.* from PurchaseHistoryExtend a where a.eventid = 202");
		assertTrue(tableValue.getRowCount() == 1);
	}

	@Test
	public void testBug103() throws DataSetException, SQLException, Exception {
		PurchaseHistoryExtend purchaseHistoryExtend = new PurchaseHistoryExtendImpl();
		purchaseHistoryExtend.setEventId("201");
		purchaseHistoryExtend.setAssetId(3L);
		clientContentService.addPurchaseHistory(purchaseHistoryExtend);

		SearchExpression searchExpression = new SearchExpressionImpl();
		searchExpression.addCondition(new PurchaseHistoryExtendEventIdCondition("201", StringComparer.EQUAL, false, false));
		List<PurchaseHistoryExtend> list = clientContentService.searchPurchaseHistory(searchExpression);
		PurchaseHistoryExtend updateModel = list.get(0);
		updateModel.setMsisdn("msisdn");

		clientContentService.updatePurchaseHistory(updateModel);
	}

	@Test
	public void testBug127() throws Exception {
		ITable tableValue = databaseTester.getConnection().createQueryTable("result", " SELECT a.* FROM binaryfile a where a.filelocation = '/screenshot/image' ");
		assertTrue(tableValue.getRowCount() == 1);
		
		assetCatalogService.deleteBinaryVersion(4L);
		
		tableValue = databaseTester.getConnection().createQueryTable("result", " SELECT a.* FROM binaryfile a where a.filelocation = '/screenshot/image' ");
		assertTrue(tableValue.getRowCount() == 0);
	}

	@Test
	public void testBug97() throws Exception {

		// SearchExpression searchExpression = new SearchExpressionImpl();
		// searchExpression.addOrder(BinaryVersionOrderByExt.GETIMAGEDATE,
		// OrderEnum.ASC);
		//
		// List<BinaryVersion> bvs =
		// clientContentService.searchBinaryVersions(searchExpression);
		//
		// for

		Calendar calendar1 = Calendar.getInstance();
		calendar1.add(Calendar.MINUTE, -3);
		Calendar calendar2 = Calendar.getInstance();
		calendar2.add(Calendar.MINUTE, -2);
		Calendar calendar3 = Calendar.getInstance();
		calendar3.add(Calendar.MINUTE, -1);

		this.assetCatalogService.addAttribute(EntityType.BINARYVERSION, 1L, "GETIMAGEDATE", calendar1.getTime());
		this.assetCatalogService.addAttribute(EntityType.BINARYVERSION, 2L, "GETIMAGEDATE", calendar2.getTime());
		this.assetCatalogService.addAttribute(EntityType.BINARYVERSION, 3L, "GETIMAGEDATE", calendar3.getTime());
		SearchExpression searchExpression = searchEngine.createSearchExpression();
		searchExpression.addOrder(BinaryVersionOrderByExt.GETIMAGEDATE, OrderEnum.DESC);
		List<BinaryVersion> list = this.assetCatalogService.searchBinaryVersions(searchExpression);
		Assert.assertTrue(list.size() == 3);
		Assert.assertTrue(list.get(0).getId() == 3);
		Assert.assertTrue(list.get(1).getId() == 2);
		Assert.assertTrue(list.get(2).getId() == 1);

	}

	@Test
	public void testBug128() throws Exception {

		BinaryVersion bv = assetCatalogService.getBinaryVersion(1L);

		bv.getAttributeValue("");

		assetCatalogService.addAttribute(EntityType.BINARYVERSION, 1L, "hello", "zhuzhen");
		assetCatalogService.addAttribute(EntityType.BINARYVERSION, 1L, "china", "zhuzhen");
		assetCatalogService.addAttribute(EntityType.BINARYVERSION, 1L, "name", "zhuzhen");
		assetCatalogService.removeAttributes(EntityType.BINARYVERSION, 1L, "hello");
		// assetCatalogService.addAttribute(EntityType.BINARYVERSION, 1L,
		// "hello", "guolili");

	}

	@Test
	public void testBug128_1() throws Exception {
		testBug128();
	}

	@Test
	@Transactional
	public void tesBug129() throws Exception {
		Tag tag = new Tag("zhuzhen");
		applicationService.saveTag(tag);

	}

	@Test
	public void tesBug129_1() throws Exception {
		tesBug129();
		// assetCatalogService.addAttribute(EntityType.BINARYVERSION, 1L,
		// "hello", "guolili");
	}

	@Test
	public void testBug123_2() throws Exception {
		BinaryVersion bv = assetCatalogService.constructBinaryVersion();
		bv.setAssetId(1L);
		bv.setAssetParentId(2L);
		bv.setName("name");
		bv.setExternalId("CP01SID11000000002");
		bv.setStatus("published");
		Long bvId = assetCatalogService.addBinaryVersion(bv);

		assetCatalogService.addAttribute(EntityType.BINARYVERSION, bvId, "ARTISTNAME", "old");
		assetCatalogService.addAttribute(EntityType.BINARYVERSION, bvId, "PUBLISHFLG", "01");

		mockClass.processDiffChangeContent("CP01SID11000000002", "zhuzhen");

		BinaryVersion bvNew = assetCatalogService.getBinaryVersion(bvId + 1);

		assertEquals(1, bvNew.getAttributeValue("ARTISTNAME").size());

	}

	@Test
	@Transactional
	public void testBug130() throws Exception {
		bug130();
	}

	@Transactional
	private void bug130() throws Exception {
		SearchExpression searchExpression = searchEngine.createSearchExpression();
		List<BinaryVersion> contentsList = null;
		try {
			contentsList = assetCatalogService.searchBinaryVersions(searchExpression);
			BinaryVersion binaryVersion = contentsList.get(0);

			assetCatalogService.removeAttributes(EntityType.BINARYVERSION, binaryVersion.getId(), "METASYNCFLAG");
			assetCatalogService.addAttribute(EntityType.BINARYVERSION, binaryVersion.getId(), "HELLO", "zhuzhen");
			assetCatalogService.updateBinaryVersion(binaryVersion);
		} catch (AssetCatalogServiceException e) {
			throw new Exception();
		}
	}

	@Test
	@Transactional
	public void testBug126() throws Exception {
		SearchExpression searchExpr = searchEngine.createSearchExpression();
		List<BinaryVersion> bvList = assetCatalogService.searchBinaryVersions(searchExpr);
		assertEquals(6, bvList.size());

		BinaryVersion bv = bvList.get(0);
		assetCatalogService.deleteBinaryVersion(bv.getId());

		SearchExpression searchExpr1 = searchEngine.createSearchExpression();
		List<BinaryVersion> bvList1 = assetCatalogService.searchBinaryVersions(searchExpr1);
		assertEquals(5, bvList1.size());
	}

	@Test
	public void testBug126_2() throws Exception {
		SearchExpression searchExpr = searchEngine.createSearchExpression();
		List<BinaryVersion> bvList = assetCatalogService.searchBinaryVersions(searchExpr);
		assertEquals(6, bvList.size());

		BinaryVersion bv = bvList.get(0);
		assetCatalogService.deleteBinaryVersion(bv.getId());

		SearchExpression searchExpr1 = searchEngine.createSearchExpression();
		List<BinaryVersion> bvList1 = assetCatalogService.searchBinaryVersions(searchExpr1);
		assertEquals(5, bvList1.size());
	}

	public List<BinaryVersion> searchBinaryVersions(String id, String openFlg) throws Exception {
		try {
			SearchExpression searchExpr = searchEngine.createSearchExpression();
			// AssetBinaryVersionSourceConditionDescriptor abvscDesc = new
			// AssetBinaryVersionSourceConditionDescriptor("02",
			// Condition.StringComparer.EQUAL, false, false);
			// searchExpr.addCondition(searchEngine.createCondition(abvscDesc));
			//
			// AssetBinaryVersionStatusNameConditionDescriptor abvsncDesc = new
			// AssetBinaryVersionStatusNameConditionDescriptor("REVOKED",
			// Condition.StringComparer.NOT_EQUAL, false, false);
			// searchExpr.addCondition(searchEngine.createCondition(abvsncDesc));

			if (id != null && id.length() > 0) {
				AssetBinaryVersionExternalIdConditionDescriptor abveicDesc = new AssetBinaryVersionExternalIdConditionDescriptor(id,
						Condition.StringComparer.EQUAL, false, false);
				searchExpr.addCondition(searchEngine.createCondition(abveicDesc));
			}
			if (openFlg != null && openFlg.length() > 0) {
				EavStringConditionDescriptor escDesc = new EavStringConditionDescriptor(EntityType.BINARYVERSION, "PUBLISHFLG", openFlg,
						Condition.StringComparer.EQUAL, false, false);
				searchExpr.addCondition(searchEngine.createCondition(escDesc));
			}

			List<BinaryVersion> bvList = assetCatalogService.searchBinaryVersions(searchExpr);

			return bvList;
		} catch (AssetCatalogServiceException e) {
			throw new Exception();
		}
	}

	@Test
	public void testBug126_1() throws Exception {
		testBug126_3();
	}

	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void testBug126_3() throws Exception {
		List<BinaryVersion> bvList = testBug126_4("1", "02");
		assertEquals(2, bvList.size());
		assetCatalogService.deleteBinaryVersion(bvList.get(0).getId());
		// List<AssetProvider> apList = searchAssetProviderByExternalId("CP01");
		// System.out.println("==================apList"+apList.size());
		//        
		// List<CatalogAsset> caList =
		// searchAssetByExternalId("CP01SID11000000002");
		// System.out.println("==================caList"+caList.size());
		// caList = searchAssetByExternalId("CP01SID11000000002");
		// System.out.println("==================caList"+caList.size());
		//        
		// List<AssetCategory> acList = searchCategoryByExternalId("002");
		// System.out.println("==================acList"+acList.size());

		bvList = testBug126_4("1", "02");
		assertEquals(1, bvList.size());
		// if(bvList.size()>0)
		// removeBinaryVersion(bvList.get(0).getId());
	}

	public List<BinaryVersion> testBug126_4(String id, String openFlg) throws Exception {
		try {
			SearchExpression searchExpr = searchEngine.createSearchExpression();
			// AssetBinaryVersionSourceConditionDescriptor abvscDesc = new
			// AssetBinaryVersionSourceConditionDescriptor("02",
			// Condition.StringComparer.EQUAL, false, false);
			// searchExpr.addCondition(searchEngine.createCondition(abvscDesc));

			// AssetBinaryVersionStatusNameConditionDescriptor abvsncDesc = new
			// AssetBinaryVersionStatusNameConditionDescriptor("REVOKED",
			// Condition.StringComparer.NOT_EQUAL, false, false);
			// searchExpr.addCondition(searchEngine.createCondition(abvsncDesc));

			if (id != null && id.length() > 0) {
				AssetBinaryVersionExternalIdConditionDescriptor abveicDesc = new AssetBinaryVersionExternalIdConditionDescriptor(id,
						Condition.StringComparer.EQUAL, false, false);
				searchExpr.addCondition(searchEngine.createCondition(abveicDesc));
			}
			if (openFlg != null && openFlg.length() > 0) {
				EavStringConditionDescriptor escDesc = new EavStringConditionDescriptor(EntityType.BINARYVERSION, "PUBLISHFLG", openFlg,
						Condition.StringComparer.EQUAL, false, false);
				searchExpr.addCondition(searchEngine.createCondition(escDesc));
			}

			List<BinaryVersion> bvList = assetCatalogService.searchBinaryVersions(searchExpr);

			return bvList;
		} catch (AssetCatalogServiceException e) {
			throw new Exception();
		}
	}

	@Test
	public void testBug113() throws StoreClientServiceException {
		SearchExpression searchExpression = new SearchExpressionImpl();
		searchExpression.addCondition(new ParentAssetVersionSummaryTagNameCondition("helloworld", StringComparer.LIKE, false, false));

		List<ParentAssetVersionSummary> parentAssetVersionSummarys = clientContentService.searchParentAssetVersionSummary(searchExpression);

		assertTrue(parentAssetVersionSummarys.size() == 0);
	}

	@Test
	public void testBug136() throws StoreClientServiceException, AssetCatalogServiceException {
		this.assetCatalogService.addAttribute(EntityType.BINARYVERSION, 11L, "CPPRIORITY", 2f);
		this.assetCatalogService.addAttribute(EntityType.BINARYVERSION, 12L, "CPPRIORITY", 1f);
		this.assetCatalogService.addAttribute(EntityType.BINARYVERSION, 11L, "DISPLAYORDER", 2f);
		this.assetCatalogService.addAttribute(EntityType.BINARYVERSION, 12L, "DISPLAYORDER", 1f);

		SearchExpression searchExpression = new SearchExpressionImpl();
		searchExpression.addOrder(BinaryVersionOrderByExt.ASSETPROVIDERPRIORITY, OrderEnum.DESC);
		searchExpression.addOrder(BinaryVersionOrderByExt.SERIAL, OrderEnum.ASC);
		List<BinaryVersion> binaryVersionList = clientContentService.searchBinaryVersions(searchExpression);

		Assert.assertTrue(binaryVersionList.get(0).getId() == 11);
		Assert.assertTrue(binaryVersionList.get(1).getId() == 12);
	}

	@Test
	public void testBug137() throws StoreClientServiceException {

		EavStringConditionDescriptor parentPublishFlagDesc = new EavStringConditionDescriptor(EntityType.BINARYVERSION, "PUBLISHFLG", "02",
				StringComparer.EQUAL, false, false);
		AssetBinaryVersionStatusNameConditionDescriptor statusNamedesc1 = new AssetBinaryVersionStatusNameConditionDescriptor("published",
				StringComparer.EQUAL, false, false);

		SearchExpression searchExpression = new SearchExpressionImpl();
		SearchEngine eg = new SearchEngineImpl();
		searchExpression.addCondition(eg.createCondition(parentPublishFlagDesc));
		searchExpression.addCondition(eg.createCondition(statusNamedesc1));

		List<BinaryVersion> binaryVersionList = clientContentService.searchBinaryVersions(searchExpression);
		for (BinaryVersion binaryVersion : binaryVersionList)
			System.out.println(binaryVersion.getId());
	}

	@Test
	public void testBug138() throws StoreClientServiceException {
		SearchExpression searchExpression = new SearchExpressionImpl();
		List<BinaryVersion> binaryVersionList = clientContentService.searchBinaryVersions(searchExpression);
		for (BinaryVersion binaryVersion : binaryVersionList) {
			System.out.println("*****" + binaryVersion.getId());
			Set<AssetCategory> categorySet = binaryVersion.getStoreClientAsset().getCategories();
			for (AssetCategory assetCategory : categorySet) {
				System.out.println(">>>>>>>" + assetCategory.getId());
			}
		}
	}
}

// $Id$