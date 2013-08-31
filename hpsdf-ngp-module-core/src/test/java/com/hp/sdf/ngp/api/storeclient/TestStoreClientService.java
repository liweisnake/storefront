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
package com.hp.sdf.ngp.api.storeclient;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

import java.net.URL;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.List;

import javax.annotation.Resource;

import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.ITable;
import org.dom4j.DocumentException;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.hp.sdf.ngp.DBEnablerTestBase;
import com.hp.sdf.ngp.api.comon.EntityType;
import com.hp.sdf.ngp.api.exception.StoreClientEntityNotFoundException;
import com.hp.sdf.ngp.api.exception.StoreClientServiceException;
import com.hp.sdf.ngp.api.impl.model.AssetCommentImpl;
import com.hp.sdf.ngp.api.impl.model.BinaryVersionImpl;
import com.hp.sdf.ngp.api.model.Asset;
import com.hp.sdf.ngp.api.model.AssetCategory;
import com.hp.sdf.ngp.api.model.AssetComment;
import com.hp.sdf.ngp.api.model.AssetPurchaseHistory;
import com.hp.sdf.ngp.api.model.AssetRatingComments;
import com.hp.sdf.ngp.api.model.BinaryVersion;
import com.hp.sdf.ngp.api.model.StoreClientAsset;
import com.hp.sdf.ngp.api.search.OrderEnum;
import com.hp.sdf.ngp.api.search.SearchExpression;
import com.hp.sdf.ngp.api.search.Condition.DateComparer;
import com.hp.sdf.ngp.api.search.Condition.NumberComparer;
import com.hp.sdf.ngp.api.search.Condition.StringComparer;
import com.hp.sdf.ngp.api.search.orderby.AssetOrderBy;
import com.hp.sdf.ngp.api.search.orderby.BinaryVersionOrderBy;
import com.hp.sdf.ngp.api.search.orderby.CommentsOrderBy;
import com.hp.sdf.ngp.model.AssetBinaryVersion;
import com.hp.sdf.ngp.model.Comments;
import com.hp.sdf.ngp.model.Status;
import com.hp.sdf.ngp.search.condition.asset.AssetNameCondition;
import com.hp.sdf.ngp.search.condition.asset.AssetProviderIdCondition;
import com.hp.sdf.ngp.search.condition.asset.AssetProviderNameCondition;
import com.hp.sdf.ngp.search.condition.asset.AssetStatusIdCondition;
import com.hp.sdf.ngp.search.condition.assetbinaryversion.AssetBinaryVersionFileNameCondition;
import com.hp.sdf.ngp.search.condition.assetbinaryversion.AssetBinaryVersionOwnerAssetParentIdCondition;
import com.hp.sdf.ngp.search.condition.assetbinaryversion.AssetBinaryVersionStatusIdCondition;
import com.hp.sdf.ngp.search.condition.comments.CommentsAssetVersionCondition;
import com.hp.sdf.ngp.search.condition.comments.CommentsContentCondition;
import com.hp.sdf.ngp.search.condition.comments.CommentsTitleCondition;
import com.hp.sdf.ngp.search.condition.comments.CommentsUpdateDateCondition;
import com.hp.sdf.ngp.search.condition.eav.EavStringCondition;
import com.hp.sdf.ngp.search.engine.SearchExpressionImpl;
import com.hp.sdf.ngp.service.ApplicationService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:META-INF/applicationContext-test.xml" })
public class TestStoreClientService extends DBEnablerTestBase {

	 @Resource
	 private ApplicationService applicationService;

	@Resource
	private StoreClientService storeClientService;

	// @Resource
	// private AssetCatalogService assetCatalogService;

	// @Resource
	// private PurchaseHistoryDAO purchaseHistoryDAO;

	@Override
	public String dataSetFileName() {
		return "/data_init.xml";
	}

	@Test
	@Transactional
	public void testGetAsset() throws StoreClientServiceException {
		StoreClientAsset storeClientAsset = storeClientService.getAsset(1L);

		assertEquals(storeClientAsset.getName(), "APP1");
		assertEquals(storeClientAsset.getProviderName(), "liuchao");
	}



	@Test
	public void testGetAssetCategoryById() throws DataSetException, SQLException, Exception {
		AssetCategory assetCategory = storeClientService.getAssetCategoryById(3L);
		// assertEquals(assetCategory.getParent().getName(), "business");
		assertEquals(assetCategory.getName(), "sports");
	}

	@Test
	public void testGetAssetComment() throws StoreClientServiceException {
		List<AssetComment> assetComments = storeClientService.getAssetComment(3L, 0, Integer.MAX_VALUE);
		assertTrue(assetComments.size() == 2);
		assertTrue(assetComments.get(0).getTitle().equals("stronger") && assetComments.get(1).getTitle().equals("stronger"));
	}

	@Test
	public void testGetAssetCommentByUserId() throws StoreClientServiceException {

		List<AssetComment> assetComments = storeClientService.getAssetCommentByUserId(3L, "yulin");
		assertTrue(assetComments.size() == 2);
		assertTrue(assetComments.get(0).getComment().equals("strong man") && assetComments.get(1).getComment().equals("strong man"));
	}

	@Test
	public void testGetAssetCommentCount() throws StoreClientServiceException {

		Long count = storeClientService.getAssetCommentCount(3L);
		assertTrue(count == 2);
	}

	@Test
	public void testGetAssetSearchResultCount() throws StoreClientServiceException {

		SearchExpression searchExpression = new SearchExpressionImpl();
		searchExpression.addCondition(new AssetStatusIdCondition(1L, NumberComparer.EQUAL));
		searchExpression.addCondition(new AssetNameCondition("APP", StringComparer.LIKE, true, false));
		searchExpression.addCondition(new AssetStatusIdCondition(2L, NumberComparer.LESS_THAN));
		searchExpression.addOrder(AssetOrderBy.CREATEDATE, OrderEnum.ASC);

		Long count = storeClientService.getAssetSearchResultCount(searchExpression);
		assertTrue(count == 2);
	}

	@Test

	public void testGetBinaryVersion() throws DataSetException, SQLException, Exception {
		BinaryVersion binaryVersion =  storeClientService.getBinaryVersion(2L);

		AssetBinaryVersion assetBinaryVersion = ((BinaryVersionImpl) binaryVersion).getAssetBinaryVersion();

		assertEquals(binaryVersion.getAssetId().longValue(), 1L);

		StoreClientAsset asset = binaryVersion.getStoreClientAsset();
		assertEquals(asset.getName(), "APP1");

		assertEquals(assetBinaryVersion.getStatus().getStatus(), "uploaded");
	}

	@Test
	public void testGetPurchasedAsset() throws StoreClientServiceException {
		List<Asset> assets = storeClientService.getPurchasedAsset("levi", 0, Integer.MAX_VALUE);

		assertTrue(assets.size() == 2);
	}

	@Test
	public void testGetPurchasedAssetCount() throws StoreClientServiceException {
		Long count = storeClientService.getPurchasedAssetCount("levi");

		assertTrue(count == 2);
	}

	@Test
	public void testGetStorePurchaseHistory() throws StoreClientServiceException {
		List<AssetPurchaseHistory> assetPurchaseHistorys = storeClientService.getStorePurchaseHistory(2L, "levi");

		assertTrue(assetPurchaseHistorys.size() == 1);
	}

	@Test
	public void testGetStorePurchaseHistoryByUser() throws StoreClientServiceException {
		List<AssetPurchaseHistory> assetPurchaseHistorys = storeClientService.getStorePurchaseHistoryByUser("levi");

		assertTrue(assetPurchaseHistorys.size() == 2);
	}

	@Test
	public void testGetUserCommentsWithRating() throws StoreClientServiceException {

		List<AssetRatingComments> assetRatingComments_list = storeClientService.getUserCommentsWithRating(3L, 0, Integer.MAX_VALUE);
		assertTrue(assetRatingComments_list.size() == 2);
		assertEquals(assetRatingComments_list.get(0).getAssetComment().getComment(), "strong man");
	}

	@Test
	public void testGetUserRating() throws StoreClientServiceException {
		float rating = storeClientService.getUserRating(1L, "3");

		assertTrue(4.2f == rating);
	}

	// @Test
	// public void testPurchase() {
	// fail("Not yet implemented");
	// }

	@Test
	public void testRetrieveDownloadDescriptor() throws StoreClientServiceException, DocumentException {
		String xml_str = storeClientService.retrieveDownloadDescriptor(1L, null, "test_userid", "http://www.baidu.com");

		String url_str = xml_str.substring(xml_str.indexOf("<installNotifyURI>"), xml_str.indexOf("</installNotifyURI>")) + "</installNotifyURI>";
		assertEquals(url_str, "<installNotifyURI>http://www.baidu.com?assetId=1&versionId=1&userId=test_userid</installNotifyURI>");

	}
	
	@Test
	public void testSetAssetVersionWhenSaveAssetComment() throws Exception{
		AssetComment assetComment =new AssetCommentImpl();
		assetComment.setAssetId(1L);
		assetComment.setAssetVersion("1.0");
		ITable tableValue = databaseTester.getConnection().createQueryTable("result",
				"select * from comments");
		long id=tableValue.getRowCount()+1;
		storeClientService.saveOrUpdateAssetComment(assetComment);
		tableValue = databaseTester.getConnection().createQueryTable("result",
		"select * from comments where assetId="+1+" and assetversion='1.0'");
		
		assertEquals(1,tableValue.getRowCount());
	}
	
	@Test
	public void testSetAssetVersionWhenUpdateAssetComment() throws Exception{
		Comments comment=applicationService.getComments(1L);
		AssetComment assetComment=new AssetCommentImpl(comment);
		assetComment.setAssetId(comment.getAsset().getId());
		assetComment.setAssetVersion("99.0");
		ITable tableValue = databaseTester.getConnection().createQueryTable("result",
		"select * from comments where id=1 and assetversion='99.0'");
		assertEquals(0,tableValue.getRowCount());
		storeClientService.saveOrUpdateAssetComment(assetComment);
		
		tableValue = databaseTester.getConnection().createQueryTable("result",
				"select * from comments where id=1 and assetversion='99.0'");
		
		assertEquals(1,tableValue.getRowCount());
	}
	
	
	@Test
	public void testSaveAssetCommentWithExist() throws Exception{
		AssetComment assetComment =new AssetCommentImpl();
		assetComment.setAssetId(3L);
		assetComment.setUserId("yulin");
		ITable tableValue = databaseTester.getConnection().createQueryTable("result",
		"select * from comments where assetid=3 and userid='yulin'");
		assertEquals(2, tableValue.getRowCount());
		 tableValue = databaseTester.getConnection().createQueryTable("result",
			"select * from comments");
		 AssetCommentImpl assetCommentImpl=(AssetCommentImpl)assetComment;
		storeClientService.saveOrUpdateAssetComment(assetComment);
		tableValue = databaseTester.getConnection().createQueryTable("result",
		"select * from comments where assetid=3 and userid='yulin'");
		assertEquals(1, tableValue.getRowCount());
		assertEquals(assetCommentImpl.getComments().getId(), tableValue.getValue(0, "id"));
		
	}

	@Test
	public void testSaveOrUpdateAssetComment() throws DataSetException, SQLException, Exception {
		List<AssetComment> assetComments = storeClientService.getAssetCommentByUserId(1L, "levi");
		AssetComment assetComment = assetComments.get(0);
		assertTrue(assetComment.getAssetId() != null);
		System.out.println(assetComment.getAssetId());
		assetComment.setComment("test_content");
		assetComment.setAssetId(1L);
		assetComment.setAssetVersion("3.0");
		storeClientService.saveOrUpdateAssetComment(assetComment);
		Long id = ((AssetCommentImpl) assetComment).getComments().getId();

		ITable tableValue = databaseTester.getConnection().createQueryTable("result",
				"select a.userid from comments a,asset b where a.assetid = b.id and a.content = 'test_content' and assetversion='3.0'and a.id = " + id);
		assertTrue(tableValue.getRowCount() == 1);

		assetComment = new AssetCommentImpl();
		assetComment.setAssetId(1L);
		assetComment.setUserId("test_userid");
		assetComment.setTitle("test_title");
		assetComment.setComment("test_comment");
		assetComment.setAssetVersion("4.0");
		storeClientService.saveOrUpdateAssetComment(assetComment);

		tableValue = databaseTester
				.getConnection()
				.createQueryTable(
						"result",
						"select a.userid from comments a,asset b where a.assetid = b.id and a.title = 'test_title' and b.name = 'APP1' and a.assetVersion='4.0'");
		assertTrue(tableValue.getRowCount() == 1);
	}

	@Test
	public void testSaveOrUpdateUserRating() throws DataSetException, SQLException, Exception {

		storeClientService.saveOrUpdateUserRating(1L, "tester", 3);

		ITable tableValue = databaseTester.getConnection().createQueryTable("result",
				"select a.userid from AssetRating a where a.userid = 'tester' and a.rating = 3 and a.assetId = 1 ");
		assertTrue(tableValue.getRowCount() == 1);
	}


	@Test
	public void testSearchAsset() throws StoreClientServiceException {

		SearchExpression searchExpression = new SearchExpressionImpl();
		searchExpression.addCondition(new AssetNameCondition("APP1", StringComparer.EQUAL, false, false));
		searchExpression.addOrder(AssetOrderBy.PROVIDERPRIORITY, OrderEnum.ASC);
		searchExpression.addCondition(new AssetProviderIdCondition(1L, NumberComparer.GREAT_EQUAL));

		List<StoreClientAsset> storeClientAssets = storeClientService.searchAsset(searchExpression);
		assertTrue(storeClientAssets.size() == 1);
		assertEquals(storeClientAssets.get(0).getProviderName(), "liuchao");
	}

	@Test
	public void testSearchBinaryVersions() throws Exception {
		AssetBinaryVersion binaryVersion=new AssetBinaryVersion();
		binaryVersion.setAsset(new com.hp.sdf.ngp.model.Asset(1L));
		binaryVersion.setOwnerAssetParentId(1L);
		binaryVersion.setStatus(new Status(1L));
		this.applicationService.saveAssetBinary(null, binaryVersion);
		this.applicationService.addAttribute(binaryVersion.getId(), EntityType.BINARYVERSION, "PUBLISHFLG", "published");
		
		SearchExpression searchExpression = new SearchExpressionImpl();
		searchExpression.addCondition(new AssetBinaryVersionOwnerAssetParentIdCondition(1L, NumberComparer.EQUAL));
		searchExpression
				.addCondition(new EavStringCondition(EntityType.BINARYVERSION, "PUBLISHFLG", "published", StringComparer.EQUAL, false, false));
		searchExpression.addOrder(BinaryVersionOrderBy.CREATEDATE, OrderEnum.ASC);
		List<BinaryVersion> binaryVersions = storeClientService.searchBinaryVersions(searchExpression);

		assertTrue(binaryVersions.size() == 1);
		assertEquals(binaryVersions.get(0).getId().longValue(), binaryVersion.getId().longValue());
	}

	@Test
	public void testSearchAssetComments() throws StoreClientServiceException {
		SearchExpression searchExpression = new SearchExpressionImpl();
		searchExpression.addCondition(new CommentsContentCondition("go", StringComparer.LIKE, false, false));
		searchExpression.addCondition(new CommentsUpdateDateCondition(Calendar.getInstance().getTime(), DateComparer.LESS_THAN));
		searchExpression.addOrder(CommentsOrderBy.ASSETID, OrderEnum.DESC);

		List<AssetComment> assetComments = storeClientService.searchAssetComments(searchExpression);
		assertTrue(assetComments.size() == 2);
		assertEquals(assetComments.get(0).getAssetVersion(), "1.0");
	}

	@Test
	public void testGetAssetCommentsSearchResultCount() throws StoreClientServiceException {
		SearchExpression searchExpression = new SearchExpressionImpl();
		searchExpression.addCondition(new CommentsTitleCondition("stronger", StringComparer.LIKE, false, false));
		searchExpression.addCondition(new CommentsAssetVersionCondition("1.0", StringComparer.EQUAL, false, false));

		Long count = storeClientService.getAssetCommentsSearchResultCount(searchExpression);
		assertTrue(count == 1);
	}

	@Test
	public void testGetBinaryVersionSearchResultCount() throws StoreClientServiceException {
		SearchExpression searchExpression = new SearchExpressionImpl();
		searchExpression.addCondition(new AssetBinaryVersionFileNameCondition("file", StringComparer.LIKE, false, false));
		searchExpression.addCondition(new AssetBinaryVersionStatusIdCondition(1L, NumberComparer.EQUAL));

		Long count = storeClientService.getBinaryVersionSearchResultCount(searchExpression);
		assertTrue(count == 4);
	}

	// -----
	@Test(expected = StoreClientEntityNotFoundException.class)
	public void testGetNonexistentAsset() throws Exception {
		// not exist
		AssetCategory assetCategory = storeClientService.getAssetCategoryById(13L);
		// assertEquals(assetCategory.getParent().getName(), "business");
		assertTrue(assetCategory == null);
	}

	@Test
	public void testGetNonexistentAssetComment() throws Exception {
		// not exist
		List<AssetComment> assetComments = storeClientService.getAssetComment(13L, 0, Integer.MAX_VALUE);
		assertTrue(assetComments.size() == 0);
	}

	@Test
	public void testGetAssetCommentByNonexistentUserId() throws Exception {
		// not exist
		List<AssetComment> assetComments = storeClientService.getAssetCommentByUserId(13L, "yulin");
		assertTrue(assetComments.size() == 0);
	}

	@Test(expected = StoreClientEntityNotFoundException.class)
	public void testGetNonexistentBinaryVersion() throws Exception {
		BinaryVersion binaryVersion = storeClientService.getBinaryVersion(12L);

		assertTrue(binaryVersion == null);
	}

	@Test
	public void testGetUserRatingForNonexistentAsset() throws Exception {
		// not exist
		float rating = storeClientService.getUserRating(11L, "3");

		assertTrue(0 == rating);
	}

	@Test
	public void testRecordAssetDownloadHistory() throws Exception {
		storeClientService.recordAssetDownloadHistory("levi", 2L, 2L);

		ITable tableValue = databaseTester
				.getConnection()
				.createQueryTable("result",
						"select a.userid from UserDownloadHistory a where a.userid = 'levi' and a.assetId = 2 and a.version = '2.0' and a.status = 'uploaded' ");
		assertTrue(tableValue.getRowCount() == 1);
	}

	@Test
	public void testRetriveBinaryVersionURL() throws Exception {
		URL url = storeClientService.retriveBinaryVersionURL(null, 2L, null);
		assertEquals(url, new URL("file:///c:/tmp/liuchao/" + "assetcatalog/20090711/1200/1/binary/2.0/file.zip"));
	}

	@Test
	public void testGetFileFromDatabase() throws Exception {
		byte[] b = storeClientService.getFileFromDatabase("location1");
		Assert.assertNotNull(b);
	}

	@Test
	public void testTransactional() throws Exception {

		//StoreClientAsset storeClientAsset = this.storeClientService.getAsset(1L);
		//Assert.assertTrue(storeClientAsset.getCategories().size() > 0);
		TestObject testObject = new TestObject(storeClientService);
		StoreClientAsset storeClientAsset = testObject.getAsset(1L);
		Assert.assertTrue(storeClientAsset.getCategories().size() > 0);

	}

	class TestObject {
		private StoreClientService storeClientService;

		public TestObject(StoreClientService storeClientService) {
			this.storeClientService = storeClientService;
		}

		public StoreClientAsset getAsset(Long assetId) throws Exception {
			return storeClientService.getAsset(assetId);
		}
	}
	
	@Test
	public void testLazyLoadForStoreClientAsset() throws Exception {

		//StoreClientAsset storeClientAsset = this.storeClientService.getAsset(1L);
		//Assert.assertTrue(storeClientAsset.getCategories().size() > 0);
		TestObject testObject = new TestObject(storeClientService);
		StoreClientAsset storeClientAsset = testObject.getAsset(1L);
		Assert.assertTrue(storeClientAsset.getProviderName() !=null);
		SearchExpression searchExpression=new SearchExpressionImpl();
		searchExpression.addCondition(new AssetProviderNameCondition(storeClientAsset.getProviderName(),StringComparer.EQUAL,false,false));
		
		storeClientAsset=this.storeClientService.searchAsset(searchExpression).get(0);
		
		Assert.assertTrue(storeClientAsset.getProviderName() !=null);

	}

}

// $Id$