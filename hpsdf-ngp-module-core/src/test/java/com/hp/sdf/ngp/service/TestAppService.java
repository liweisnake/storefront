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
package com.hp.sdf.ngp.service;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import junit.framework.Assert;

import org.apache.commons.io.FileUtils;
import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.ITable;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.hp.sdf.ngp.DBEnablerTestBase;
import com.hp.sdf.ngp.api.assetcatalog.AssetCatalogService;
import com.hp.sdf.ngp.api.comon.EntityType;
import com.hp.sdf.ngp.api.model.BinaryVersion;
import com.hp.sdf.ngp.api.search.Condition;
import com.hp.sdf.ngp.api.search.OrderEnum;
import com.hp.sdf.ngp.api.search.SearchExpression;
import com.hp.sdf.ngp.api.search.Condition.DateComparer;
import com.hp.sdf.ngp.api.search.Condition.NumberComparer;
import com.hp.sdf.ngp.api.search.Condition.StringComparer;
import com.hp.sdf.ngp.api.search.orderby.AssetOrderBy;
import com.hp.sdf.ngp.common.exception.CensoredWordFoundException;
import com.hp.sdf.ngp.common.exception.NgpRuntimeException;
import com.hp.sdf.ngp.dao.AssetDAO;
import com.hp.sdf.ngp.eav.model.AttributeValue;
import com.hp.sdf.ngp.model.ApiKey;
import com.hp.sdf.ngp.model.Asset;
import com.hp.sdf.ngp.model.AssetBinaryVersion;
import com.hp.sdf.ngp.model.AssetLifecycleAction;
import com.hp.sdf.ngp.model.AssetLifecycleActionHistory;
import com.hp.sdf.ngp.model.AssetPrice;
import com.hp.sdf.ngp.model.AssetRating;
import com.hp.sdf.ngp.model.AssetRestrictionRelation;
import com.hp.sdf.ngp.model.Category;
import com.hp.sdf.ngp.model.Comments;
import com.hp.sdf.ngp.model.CommentsSensorWord;
import com.hp.sdf.ngp.model.ParentAssetVersionSummary;
import com.hp.sdf.ngp.model.Platform;
import com.hp.sdf.ngp.model.Provider;
import com.hp.sdf.ngp.model.RestrictedType;
import com.hp.sdf.ngp.model.ScreenShots;
import com.hp.sdf.ngp.model.Status;
import com.hp.sdf.ngp.model.SystemConfig;
import com.hp.sdf.ngp.model.Tag;
import com.hp.sdf.ngp.model.UserDownloadHistory;
import com.hp.sdf.ngp.search.condition.asset.AssetAssetCategoryDescriptionCondition;
import com.hp.sdf.ngp.search.condition.asset.AssetAssetCategoryIdCondition;
import com.hp.sdf.ngp.search.condition.asset.AssetAssetCategoryNameCondition;
import com.hp.sdf.ngp.search.condition.asset.AssetAssetPriceAmountCondition;
import com.hp.sdf.ngp.search.condition.asset.AssetAuthoridCondition;
import com.hp.sdf.ngp.search.condition.asset.AssetAverageUserRatingCondition;
import com.hp.sdf.ngp.search.condition.asset.AssetBriefCondition;
import com.hp.sdf.ngp.search.condition.asset.AssetCreateDateCondition;
import com.hp.sdf.ngp.search.condition.asset.AssetDescriptionCondition;
import com.hp.sdf.ngp.search.condition.asset.AssetDownloadCountCondition;
import com.hp.sdf.ngp.search.condition.asset.AssetIdCondition;
import com.hp.sdf.ngp.search.condition.asset.AssetLatestVersionCondition;
import com.hp.sdf.ngp.search.condition.asset.AssetNameCondition;
import com.hp.sdf.ngp.search.condition.asset.AssetPlatformDescriptionCondition;
import com.hp.sdf.ngp.search.condition.asset.AssetPlatformIdCondition;
import com.hp.sdf.ngp.search.condition.asset.AssetPlatformNameCondition;
import com.hp.sdf.ngp.search.condition.asset.AssetProviderIdCondition;
import com.hp.sdf.ngp.search.condition.asset.AssetPurchaseUserCondition;
import com.hp.sdf.ngp.search.condition.asset.AssetSourceCondition;
import com.hp.sdf.ngp.search.condition.asset.AssetStatusIdCondition;
import com.hp.sdf.ngp.search.condition.asset.AssetStatusNameCondition;
import com.hp.sdf.ngp.search.condition.asset.AssetTagDescriptionCondition;
import com.hp.sdf.ngp.search.condition.asset.AssetTagIdCondition;
import com.hp.sdf.ngp.search.condition.asset.AssetTagNameCondition;
import com.hp.sdf.ngp.search.condition.asset.AssetUpdateDateCondition;
import com.hp.sdf.ngp.search.condition.assetbinaryversion.AssetBinaryVersionCategoryNameCondition;
import com.hp.sdf.ngp.search.condition.assetbinaryversion.AssetBinaryVersionIdCondition;
import com.hp.sdf.ngp.search.condition.assetlifecycleaction.AssetLifecycleActionAssetIdCondition;
import com.hp.sdf.ngp.search.condition.assetlifecycleaction.AssetLifecycleActionOwneridCondition;
import com.hp.sdf.ngp.search.condition.assetlifecycleactionhistory.AssetLifecycleActionHistoryCompleteDateCondition;
import com.hp.sdf.ngp.search.condition.assetlifecycleactionhistory.AssetLifecycleActionHistorySourceCondition;
import com.hp.sdf.ngp.search.condition.category.CategoryAssetCategoryRelationAssetIdCondition;
import com.hp.sdf.ngp.search.condition.category.CategoryNameCondition;
import com.hp.sdf.ngp.search.condition.comments.CommentsAssetIdCondition;
import com.hp.sdf.ngp.search.condition.comments.CommentsAssetRatingCondition;
import com.hp.sdf.ngp.search.condition.comments.CommentsContentCondition;
import com.hp.sdf.ngp.search.condition.comments.CommentsUseridCondition;
import com.hp.sdf.ngp.search.condition.eav.EavNumberCondition;
import com.hp.sdf.ngp.search.condition.eav.EavStringCondition;
import com.hp.sdf.ngp.search.condition.provider.ProviderNameCondition;
import com.hp.sdf.ngp.search.condition.screenshots.ScreenShotsAssetIdCondition;
import com.hp.sdf.ngp.search.condition.systemconfig.SystemConfigConfigKeyCondition;
import com.hp.sdf.ngp.search.condition.userdownloadhistory.UserDownloadHistoryAssetIdCondition;
import com.hp.sdf.ngp.search.condition.userdownloadhistory.UserDownloadHistoryStatusCondition;
import com.hp.sdf.ngp.search.condition.userdownloadhistory.UserDownloadHistoryUseridCondition;
import com.hp.sdf.ngp.search.engine.SearchExpressionImpl;

import edu.emory.mathcs.backport.java.util.Arrays;

/**
 * 
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:META-INF/applicationContext-test.xml" })
public class TestAppService extends DBEnablerTestBase {

	@Resource
	private ApplicationService applicationService;

	@Resource
	private AssetCatalogService assetCatalogService;

	@Resource
	private ApiService apiService;

	@Resource
	private AssetDAO baseDao;

	private String filePath = "c://tmp/liuchao/";

	private String testFileLocation = "src/test/resources/file.zip";

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	@Override
	public String dataSetFileName() {
		return "/data_init.xml";
	}

	@Test
	public void testFindAssetById() throws Exception {
		Asset asset = applicationService.getAsset(1L);
		Assert.assertTrue(asset.getName().equals("APP1"));
	}

	@Test
	public void testDeleteCommentsByAssetId() throws Exception {

		ITable tableValue1 = databaseTester.getConnection().createQueryTable("result", "select * from comments where assetid = 3");
		Assert.assertTrue(tableValue1.getRowCount() == 2);

		applicationService.deleteCommentsByAssetId(3L);
		ITable tableValue2 = databaseTester.getConnection().createQueryTable("result", "select * from comments where assetid = 3");
		Assert.assertTrue(tableValue2.getRowCount() == 0);
	}

	@Test
	public void testDeleteCommentsByUserId() throws Exception {

		ITable tableValue1 = databaseTester.getConnection().createQueryTable("result", "select * from comments where userid = 'yulin'");
		Assert.assertTrue(tableValue1.getRowCount() == 2);

		applicationService.deleteCommentsByUserId("yulin");
		ITable tableValue2 = databaseTester.getConnection().createQueryTable("result", "select * from comments where userid = 'yulin'");
		Assert.assertTrue(tableValue2.getRowCount() == 0);
	}

	@Test
	public void testGetAllCommentsByAssetId() throws Exception {
		List<Comments> comments = applicationService.getAllCommentsByAssetId(1L, 0, Integer.MAX_VALUE);
		Assert.assertTrue(comments.size() == 1);
	}

	@Test
	public void testGetAllCommentsCountByAssetId() throws Exception {
		long count = applicationService.getAllCommentsCountByAssetId(3L);
		Assert.assertTrue(count == 2);
	}

	@Test
	public void testSearchAssetOrderBy() throws Exception {
		ITable tableValue = databaseTester.getConnection().createQueryTable("result",
				"select id,AVERAGEUSERRATING from asset order by asset.AVERAGEUSERRATING desc");
		long one = (Long) tableValue.getValue(0, "id");
		SearchExpression searchExpression = new SearchExpressionImpl();
		searchExpression.addOrder(AssetOrderBy.RATING, OrderEnum.DESC);
		searchExpression.addOrder(AssetOrderBy.NAME, OrderEnum.DESC);
		searchExpression.addOrder(AssetOrderBy.PROVIDERPRIORITY, OrderEnum.DESC);
		searchExpression.setFirst(0);
		searchExpression.setMax(1);
		List<Asset> assets = applicationService.searchAsset(searchExpression);
		Assert.assertTrue(assets.get(0).getId() == one);

	}

	@Test
	public void testSaveComments() throws Exception {
		ITable tableValue1 = databaseTester.getConnection().createQueryTable("result", "select * from comments where userid = 'liuchao'");
		Assert.assertTrue(tableValue1.getRowCount() == 0);

		Comments comment = new Comments();
		comment.setUserid("liuchao");
		comment.setCreateDate(new Date());
		comment.setAsset(new Asset(1L));
		comment.setContent("value");
		applicationService.saveComments(comment);

		ITable tableValue2 = databaseTester.getConnection().createQueryTable("result", "select * from comments where userid = 'liuchao'");
		Assert.assertTrue(tableValue2.getRowCount() == 1);
		CommentsSensorWord commentsSensorWord = new CommentsSensorWord();
		commentsSensorWord.setSensorWord("value");
		commentsSensorWord.setUpdateDate(new Date());
		applicationService.saveOrUpdateCommentsSensorWord(commentsSensorWord);
		commentsSensorWord = new CommentsSensorWord();
		commentsSensorWord.setSensorWord("test");
		commentsSensorWord.setUpdateDate(new Date());
		applicationService.saveOrUpdateCommentsSensorWord(commentsSensorWord);

		comment = new Comments();
		comment.setUserid("liuchao");
		comment.setCreateDate(new Date());
		comment.setAsset(new Asset(1L));
		comment.setContent("my value name");
		try {
			applicationService.saveComments(comment);
			Assert.assertTrue(false);
		} catch (CensoredWordFoundException e) {

		}
		comment = new Comments();
		comment.setUserid("liuchao");
		comment.setCreateDate(new Date());
		comment.setAsset(new Asset(1L));
		comment.setContent("my name");
		applicationService.saveComments(comment);
		tableValue2 = databaseTester.getConnection().createQueryTable("result", "select * from comments where userid = 'liuchao'");
		Assert.assertTrue(tableValue2.getRowCount() == 2);
	}

	@Test
	public void testGetAllPlatform() throws Exception {
		List<Platform> platform = applicationService.getAllPlatform(0, Integer.MAX_VALUE);
		Assert.assertTrue(platform.size() == 4);
	}

	@Test
	public void testDeletePlatformById() throws Exception {
		ITable tableValue1 = databaseTester.getConnection().createQueryTable("result", "select * from platform where name = 'j2ee'");
		Assert.assertTrue(tableValue1.getRowCount() == 1);

		applicationService.deletePlatformById(4L);
		ITable tableValue2 = databaseTester.getConnection().createQueryTable("result", "select * from platform where name = 'j2ee'");
		Assert.assertTrue(tableValue2.getRowCount() == 0);
	}

	@Test
	public void testSavePlatform() throws Exception {
		ITable tableValue1 = databaseTester.getConnection().createQueryTable("result", "select * from platform where name = 'liuchao'");
		Assert.assertTrue(tableValue1.getRowCount() == 0);

		Platform platform = new Platform();
		platform.setName("liuchao");
		applicationService.savePlatform(platform);

		ITable tableValue2 = databaseTester.getConnection().createQueryTable("result", "select * from platform where name = 'liuchao'");
		Assert.assertTrue(tableValue2.getRowCount() == 1);
	}

	@Test
	public void testUpdatePlatform() throws Exception {
		Platform platform = new Platform(1L);
		platform.setName("liuchao");
		applicationService.updatePlatform(platform);

		ITable tableValue1 = databaseTester.getConnection().createQueryTable("result", "select * from platform where name = 'liuchao'");
		Assert.assertTrue(tableValue1.getRowCount() == 1);
		ITable tableValue2 = databaseTester.getConnection().createQueryTable("result", "select * from platform where name = 'linux'");
		Assert.assertTrue(tableValue2.getRowCount() == 0);
	}

	@Test
	public void testDeleteCategoryById() throws Exception {

		applicationService.deleteCategoryById(1L);

		ITable tableValue2 = databaseTester.getConnection().createQueryTable("result", "select * from category where id = 1");
		Assert.assertTrue(tableValue2.getRowCount() == 0);

		tableValue2 = databaseTester.getConnection().createQueryTable("result", "select * from category where id = 2");
		Assert.assertTrue(tableValue2.getRowCount() == 1);
	}

	@Test
	public void testGetAllCategory() throws Exception {
		List<Category> categorys = applicationService.getAllCategory(0, Integer.MAX_VALUE);
		Assert.assertTrue(categorys.size() == 4);
	}

	@Test
	public void testGetAllCategoryByAssetId() throws Exception {
		List<Category> categorys = applicationService.getAllCategoryByAssetId(2L, 0, Integer.MAX_VALUE);
		Assert.assertTrue(categorys.size() == 1);
	}

	@Test
	public void testSaveCategory() throws Exception {
		Category category = new Category();
		category.setName("liuchao");
		applicationService.saveCategory(category);

		ITable tableValue2 = databaseTester.getConnection().createQueryTable("result", "select * from category where name = 'liuchao'");
		Assert.assertTrue(tableValue2.getRowCount() == 1);
	}

	@Test
	public void testGetCategoryByName() throws Exception {
		Category category = applicationService.getCategoryByName("business");

		Assert.assertTrue(category.getId() == 1);
	}

	@Test
	public void testUpdateCategory() throws Exception {
		Category category = new Category(2L, "liuchao");
		applicationService.updateCategory(category);

		ITable tableValue2 = databaseTester.getConnection().createQueryTable("result", "select * from category where name = 'liuchao'");
		Assert.assertTrue(tableValue2.getRowCount() == 1);
	}

	@Test
	public void testAssociateCategoryByAssetidCategoryids() throws Exception {
		Set<Long> categoryIds = new HashSet<Long>();
		categoryIds.add(3L);
		categoryIds.add(2L);
		applicationService.associateCategory(1L, 1L, categoryIds);

		ITable tableValue1 = databaseTester.getConnection().createQueryTable("result",
				"select * from assetcategoryrelation where assetid=1 and (ctgid=2 or ctgid=3)");

		Assert.assertTrue(tableValue1.getRowCount() == 2);

		ITable tableValue2 = databaseTester.getConnection().createQueryTable("result", "select * from assetcategoryrelation");

		Assert.assertTrue(tableValue2.getRowCount() == 5);

	}

	@Test
	public void testAssociateCategory() throws Exception {
		applicationService.associateCategory(3L, 1L, 2L);
		ITable tableValue1 = databaseTester.getConnection().createQueryTable("result",
				"select * from assetcategoryrelation where assetid = 3 and ctgid=2");

		Assert.assertTrue(tableValue1.getRowCount() == 1);

		ITable tableValue2 = databaseTester.getConnection().createQueryTable("result", "select * from assetcategoryrelation");

		Assert.assertTrue(tableValue2.getRowCount() == 4);

	}

	@Test
	public void testDisassociateCategory() throws Exception {
		/**
		 * Disassociate category by Asset id and AssetBinaryVersion id
		 */
		applicationService.disassociateCategory(2L, 2L);
		ITable tableValue1 = databaseTester.getConnection().createQueryTable("result",
				"select * from assetcategoryrelation where assetid = 2 and versionid=2");
		Assert.assertTrue(tableValue1.getRowCount() == 0);

		/**
		 * Disassociate category by Asset id
		 */
		applicationService.disassociateCategory(3L);
		ITable tableValue2 = databaseTester.getConnection().createQueryTable("result", "select * from assetcategoryrelation where assetid = 3");
		Assert.assertTrue(tableValue2.getRowCount() == 0);

		/**
		 * Disassociate category by Asset id and AssetBinaryVersion id and
		 * category id
		 */
		applicationService.disassociateCategory(1L, 1L, 1L);
		ITable tableValue3 = databaseTester.getConnection().createQueryTable("result",
				"select * from assetcategoryrelation where assetid = 1 and ctgid=1 and versionid=1");
		Assert.assertTrue(tableValue3.getRowCount() == 0);

		/**
		 * All the data in table assetcategoryrelation should be deleted after
		 * abrove 3 steps
		 */
		ITable tableValue4 = databaseTester.getConnection().createQueryTable("result", "select * from assetcategoryrelation");

		Assert.assertEquals(0, tableValue4.getRowCount());

	}

	@Test
	public void testUpdateAssetVersion() throws Exception {
		applicationService.updateAssetVersion(1L, 2L);
		ITable tableValue = databaseTester.getConnection().createQueryTable("result", "select * from asset where id=1");

		Assert.assertEquals("2.0", tableValue.getValue(0, "currentversion"));
		Assert.assertEquals(2L, tableValue.getValue(0, "currentversionid"));
	}

	@Test
	public void testDeleteTag() throws Exception {
		applicationService.deleteTag(2L);
		ITable tableValue1 = databaseTester.getConnection().createQueryTable("result", "select * from tag where id = 2");
		Assert.assertTrue(tableValue1.getRowCount() == 0);
		ITable tableValue2 = databaseTester.getConnection().createQueryTable("result", "select * from assettagrelation where tagid = 2");
		Assert.assertTrue(tableValue2.getRowCount() == 0);
	}

	@Test
	public void testDeleteTagByAssetIdTagidVersionId() throws Exception {
		applicationService.deleteTag(1L, 1L, 1L);

		ITable tableValue = databaseTester.getConnection().createQueryTable("result",
				"select * from assettagrelation where tagid = 1 and assetid=1 and versionid=1");
		Assert.assertTrue(tableValue.getRowCount() == 0);

		applicationService.deleteTag(3L, 1L, 3L);
		ITable tableValue2 = databaseTester.getConnection().createQueryTable("result",
				"select * from assettagrelation where tagid = 1 and assetid=3 and versionid=3");
		Assert.assertTrue(tableValue2.getRowCount() == 0);

		ITable tableValue3 = databaseTester.getConnection().createQueryTable("result", "select * from tag where id = 1 ");
		Assert.assertTrue(tableValue3.getRowCount() == 0);
	}

	@Test
	public void testgetAllTags() throws Exception {
		List<Tag> tags = applicationService.getAllTags(0, Integer.MAX_VALUE);
		Assert.assertTrue(tags.size() == 4);
	}

	@Test
	public void testGetAllTagsByAssetId() throws Exception {
		List<Tag> tags = applicationService.getAllTagsByAsset(3L,null, 0, Integer.MAX_VALUE);
		Assert.assertTrue(tags.size() == 4);

	}

	@Test
	public void testGetAllTagsCount() throws Exception {
		long result = applicationService.getAllTagsCount();
		Assert.assertTrue(result == 4);
	}

	@Test
	public void testGetTagById() throws Exception {
		Tag tag = applicationService.getTagById(2L);
		Assert.assertTrue(tag != null);
	}

	@Test
	public void testGetTagByName() throws Exception {
		Tag tag = applicationService.getTagByName("java");
		Assert.assertTrue(tag != null);
	}

	@Test
	public void testSaveTagByAssetidTag() throws Exception {
		applicationService.saveTag(3L, new Tag("liuchao"));
		ITable tableValue1 = databaseTester.getConnection().createQueryTable("result", "select * from tag where id = 4");
		Assert.assertTrue(tableValue1.getRowCount() == 1);
		ITable tableValue2 = databaseTester.getConnection().createQueryTable("result", "select * from assettagrelation where tagid = 4");
		Assert.assertTrue(tableValue2.getRowCount() == 1);
	}

	@Test
	public void testSaveTag() throws Exception {
		applicationService.saveTag(new Tag("liuchao"));
		ITable tableValue1 = databaseTester.getConnection().createQueryTable("result", "select * from tag where name = 'liuchao'");
		Assert.assertTrue(tableValue1.getRowCount() == 1);
	}

	@Test
	public void testUpdateTag() throws Exception {
		applicationService.updateTag(new Tag(2L, "liuchao"));
		ITable tableValue1 = databaseTester.getConnection().createQueryTable("result", "select * from tag where name = 'liuchao'");
		Assert.assertTrue(tableValue1.getRowCount() == 1);
	}

	@Test
	public void testAssociateTagRelation() throws Exception {
		applicationService.associateTagRelation(4L, 1L, 1L);

		ITable tableValue = databaseTester.getConnection().createQueryTable("result",
				"select * from assettagrelation where assetid=1 and tagid=4 and versionid=1");

		Assert.assertTrue(tableValue.getRowCount() == 1);

		ITable tableValue1 = databaseTester.getConnection().createQueryTable("result", "select * from assettagrelation");

		Assert.assertTrue(tableValue1.getRowCount() == 7);

	}

	@Test
	public void testAssociateTagRelationByTagIds() throws Exception {
		Set<Long> tagIds = new HashSet<Long>();
		tagIds.add(2L);
		tagIds.add(3L);
		applicationService.associateTagRelation(1L, 2L, tagIds);

		ITable tableValue = databaseTester.getConnection().createQueryTable("result",
				"select * from assettagrelation where assetid=1 and (tagid=3 or tagid=2) and versionid=2");

		Assert.assertTrue(tableValue.getRowCount() == 2);

		ITable tableValue1 = databaseTester.getConnection().createQueryTable("result", "select * from assettagrelation");

		Assert.assertTrue(tableValue1.getRowCount() == 8);

	}

	@Test
	public void testDisassociateTagRelation() throws Exception {
		/**
		 * Disassociate tag relation base on asset id and version id and tag id
		 */
		applicationService.disassociateTagRelation(1L, 1L, 1L);
		ITable tableValue = databaseTester.getConnection().createQueryTable("result",
				"select * from assettagrelation where assetid=1 and versionid=1 and tagid=1");
		Assert.assertTrue(tableValue.getRowCount() == 0);
		/**
		 * Disassociate tag relation base on asset id and version id
		 */
		applicationService.disassociateTagRelation(1L, 1L);
		ITable tableValue1 = databaseTester.getConnection().createQueryTable("result",
				"select * from assettagrelation where assetid=1 and versionid=1 ");

		Assert.assertTrue(tableValue1.getRowCount() == 0);
		ITable tableValue2 = databaseTester.getConnection().createQueryTable("result", "select * from assettagrelation");

		Assert.assertTrue(tableValue2.getRowCount() == 4);

		/**
		 * Disassociate tag relation base on asset id
		 */
		applicationService.disassociateTagRelation(2L);
		ITable tableValue3 = databaseTester.getConnection().createQueryTable("result", "select * from assettagrelation where assetid=2");
		Assert.assertTrue(tableValue3.getRowCount() == 0);

	}

	@Test
	public void testGetAllStatus() throws Exception {
		List<Status> status = applicationService.getAllStatus();
		Assert.assertTrue(status.size() == 9);
	}

	@Test
	public void testGetStatusByName() throws Exception {
		Status status = applicationService.getStatusByName("testing");
		Assert.assertTrue(status != null);
	}

	@Test
	public void testSaveStatus() throws Exception {
		applicationService.saveStatus(new Status("liuchao"));
		ITable tableValue1 = databaseTester.getConnection().createQueryTable("result", "select * from status where status = 'liuchao'");
		Assert.assertTrue(tableValue1.getRowCount() == 1);
	}

	@Test
	public void testSaveApiKey() throws Exception {
		ApiKey assetKey = new ApiKey();
		assetKey.setDescription("liuchao");
		assetKey.setSgName("liuchao");
		assetKey.setSgPassword("liuchao");

		List l = new ArrayList();
		l.add(apiService.findServiceById(1L, 0, Integer.MAX_VALUE));

		applicationService.saveApiKey(assetKey, l, "userid");
		ITable tableValue = databaseTester.getConnection().createQueryTable("result", "select * from apikey where sgname = 'liuchao'");
		Assert.assertTrue(tableValue.getRowCount() == 1);
	}

	@Test
	public void testDeleteAssetRating() throws Exception {
		applicationService.deleteAssetRating(1L);

		ITable tableValue = databaseTester.getConnection().createQueryTable("result", "select * from assetrating where id = 1");
		Assert.assertTrue(tableValue.getRowCount() == 0);
	}

	@Test
	public void testSaveOrUpdateAssetRating() throws Exception {
		applicationService.saveOrUpdateAssetRating(2L, "liuchao", 3.5);
		ITable tableValue = databaseTester.getConnection().createQueryTable("result", "select * from assetrating where userid = 'liuchao'");
		Assert.assertTrue(tableValue.getRowCount() == 1);

		applicationService.saveOrUpdateAssetRating(1L, "3", 1.0);
		ITable tableValue1 = databaseTester.getConnection().createQueryTable("result", "select * from assetrating where rating = 1");
		Assert.assertTrue(tableValue1.getRowCount() == 1);
	}

	@Test
	public void testGetAssetRating() throws Exception {
		AssetRating assetRating = applicationService.getAssetRating("3", 1L);
		Assert.assertTrue(assetRating != null);
	}

	@Test
	public void testGetAllAsset() throws Exception {
		List<Asset> asset = applicationService.getAllAsset(0, Integer.MAX_VALUE);
		Assert.assertTrue(asset.size() == 3);
	}

	@Test
	public void testGetAsset() throws Exception {
		Asset asset = applicationService.getAsset(1L);
		Assert.assertTrue(asset != null);
	}

	@Test
	public void testGetAssetByStatus() throws Exception {
		List<Asset> asset = applicationService.getAssetByStatus(1L);
		Assert.assertTrue(asset.size() == 2);
	}

	@Test
	public void testGetMyAsset() throws Exception {
		List<Asset> asset = applicationService.getMyAsset("levi");
		Assert.assertTrue(asset.size() == 3);
	}

	@Test
	public void testGetMyAssetPageCount() throws Exception {
		long result = applicationService.getMyAssetPageCount("levi");
		Assert.assertTrue(result == 3);
	}

	@Test
	public void testDeleteAsset2() throws Exception {
		applicationService.associateAssetGroup(1L, Arrays.asList(new Long[] { 2L, 3L }));
		applicationService.deleteAsset(1L);

		ITable tableValue4 = databaseTester.getConnection().createQueryTable("result", "select * from asset where id = 2 and PARENTID is null");
		Assert.assertEquals(1, tableValue4.getRowCount());
		applicationService.deleteAsset(2L);
		tableValue4 = databaseTester.getConnection().createQueryTable("result", "select * from asset where id = 2 and PARENTID is null");
		Assert.assertEquals(0, tableValue4.getRowCount());
	}

	@Test
	public void testDeleteAsset() throws Exception {
		ITable tableValue3 = databaseTester.getConnection().createQueryTable("result", "select * from assetbinaryversion where assetid = 1");
		Assert.assertEquals(2, tableValue3.getRowCount());

		System.out.println("filePath=" + filePath);
		List<AssetBinaryVersion> binaryVersions = applicationService.getAssetBinaryByAssetId(1L);
		System.out.println("binary version size=" + binaryVersions.size());

		Asset asset = applicationService.getAsset(1L);

		for (int i = 0; i < binaryVersions.size(); i++) {
			this.createFile(binaryVersions.get(i));
		}
		Date date = asset.getCreateDate();
		String location = filePath + "assetcatalog/" + new SimpleDateFormat("yyyyMMdd").format((date)) + "/"
				+ new SimpleDateFormat("HHmm").format((date)) + "/1";
		System.out.println("location=" + location);
		File file = new File(location);
		Assert.assertTrue(file.exists());
		applicationService.deleteAsset(1L);
		Assert.assertTrue(!file.exists());

		ITable tableValue2 = databaseTester.getConnection().createQueryTable("result", "select * from asset where id = 1");
		Assert.assertEquals(0, tableValue2.getRowCount());

		ITable tableValue1 = databaseTester.getConnection().createQueryTable("result", "select * from assetbinaryversion where assetid = 1");
		Assert.assertEquals(0, tableValue1.getRowCount());

		ITable tableValue4 = databaseTester.getConnection().createQueryTable("result", "select * from screenshots where assetid = 1");
		Assert.assertEquals(0, tableValue4.getRowCount());
	}

	@Test
	public void testSaveAsset() throws Exception {
		Asset asset = new Asset();
		asset.setStatus(new Status(1L));
		asset.setAuthorid("liuchao");
		asset.setAssetProvider(new Provider(1L));
		applicationService.saveAsset(asset);

		ITable tableValue1 = databaseTester.getConnection().createQueryTable("result", "select * from asset where id=4 and authorid = 'liuchao'");
		Assert.assertTrue(tableValue1.getRowCount() == 1);
		Assert.assertTrue(asset.getEntityId() != 0);
	}

	@Test
	public void testSaveAssetByAssetIdCategoryId() throws Exception {
		Asset asset = new Asset();
		asset.setStatus(new Status(1L));
		asset.setAuthorid("liuchao");
		asset.setAssetProvider(new Provider(1L));
		List<Long> categoryIds = new ArrayList<Long>();
		categoryIds.add(1L);
		categoryIds.add(2L);
		applicationService.saveAsset(asset, categoryIds);

		ITable tableValue1 = databaseTester.getConnection().createQueryTable("result", "select * from asset where authorid = 'liuchao'");
		Assert.assertTrue(tableValue1.getRowCount() == 1);

		ITable tableValue2 = databaseTester.getConnection().createQueryTable("result", "select * from assetcategoryrelation");
		Assert.assertTrue(tableValue2.getRowCount() == 5);
	}

	@Test
	public void testGetAssetByProperties() throws Exception {
		SearchExpression searchExpression = new SearchExpressionImpl();
		searchExpression.addCondition(new AssetNameCondition("APP1", StringComparer.EQUAL, false, false));
		searchExpression.addCondition(new AssetAuthoridCondition("levi", StringComparer.EQUAL, false, false));
		searchExpression.addCondition(new AssetLatestVersionCondition("3.5", StringComparer.EQUAL, false, false));
		List<Asset> asset = applicationService.searchAsset(searchExpression);

		Assert.assertTrue(asset.size() == 1);
	}

	@Test
	public void testSearchAssetPageCount() throws Exception {

		SearchExpression searchExpression = new SearchExpressionImpl();
		searchExpression.addCondition(new AssetNameCondition("APP1", StringComparer.LIKE, true, false));
		searchExpression.addCondition(new AssetAuthoridCondition("levi", StringComparer.EQUAL, false, false));
		searchExpression.addCondition(new AssetLatestVersionCondition("3.5", StringComparer.EQUAL, false, false));
		searchExpression.addCondition(new AssetStatusIdCondition(1L, NumberComparer.EQUAL));

		long result = applicationService.searchAssetPageCount(searchExpression);

		System.err.println("result=" + result);
		Assert.assertTrue(result == 1);
	}

	@Test
	public void testUpdateAsset() throws Exception {
		Asset asset = new Asset();
		asset.setId(1L);
		asset.setStatus(new Status(1L));
		asset.setAuthorid("liuchao");
		asset.setAssetProvider(new Provider(1L));
		asset.setDownloadCount(1L);
		asset.setEntityId(1L);
		applicationService.updateAsset(asset);

		ITable tableValue1 = databaseTester.getConnection().createQueryTable("result", "select * from asset where authorid = 'liuchao' and id=1");
		Assert.assertTrue(tableValue1.getRowCount() == 1);
	}

	@Test
	public void testSaveAssetPicture() throws Exception {
		byte[] content = this.getBytesFromFile(new File(testFileLocation));
		applicationService.saveAssetPicture(1L, content, AssetPictureType.THUMBNAILIMAGE, "a.jpg");
		Asset asset = applicationService.getAsset(1L);
		Date date = asset.getCreateDate();
		String location = "assetcatalog/" + new SimpleDateFormat("yyyyMMdd").format((date)) + "/" + new SimpleDateFormat("HHmm").format((date)) + "/"
				+ asset.getId() + "/picture/" + AssetPictureType.THUMBNAILIMAGE.getAttributeName() + "/a.jpg";
		System.err.println(location);
		File file = new File(filePath + location);

		Assert.assertTrue(file.exists());

		ITable tableValue1 = databaseTester.getConnection().createQueryTable("result",
				"select * from asset where id=1 and thumbnaillocation = '" + location + "'");
		Assert.assertTrue(tableValue1.getRowCount() == 1);

		Assert.assertTrue(tableValue1.getRowCount() == 1);
		file.delete();
	}
	
	@Test
	public void testUpdateBinaryVersionPics() throws DataSetException, SQLException, Exception {

		BinaryVersion binaryVersion = assetCatalogService.getBinaryVersion(2L);
		
		binaryVersion.setThumbnail("jpg", new ByteArrayInputStream("testContents".getBytes()));
		binaryVersion.setMedThumbnail("jpg", new ByteArrayInputStream("testContentsMiddle".getBytes()));
		binaryVersion.setBigThumbnail("jpg", new ByteArrayInputStream("testContentsBig".getBytes()));
		
		assetCatalogService.updateBinaryVersion(binaryVersion);
		assertNotNull(binaryVersion.getThumbnailLocation());
		assertNotNull(binaryVersion.getThumbnailMedLocation());
		assertNotNull(binaryVersion.getThumbnailBigLocation());
		
	}
	
	// @Test
	// public void testAddAssetAttributeAsFile() throws Exception{
	// byte[] content=this.getBytesFromFile(new File(testFileLocation));
	// applicationService.addAssetAttributeAsFile(1L, "test", "test.zip",
	// content);
	// Asset asset=assetDao.findById(1L);
	// Date date=asset.getCreateDate();
	// String location=filePath+"assetcatalog/"+new
	// SimpleDateFormat("yyyyMMdd").format((date))+"/"+new
	// SimpleDateFormat("HHMM").format((date))+"/"+asset.getId()+"/eav/"+"test.zip";
	// File file=new File(location);
	// Assert.assertTrue(file.exists());
	// }

	@Test
	public void testCountAssetLifecycleActionResult() throws Exception {
		long result = applicationService.countAssetLifecycleActionResult(2L, true);
		Assert.assertTrue(result == 1);
	}

	@Test
	public void testDeleteAssetLifecycleActionById() throws Exception {
		applicationService.deleteAssetLifecycleActionById(1L);
		ITable tableValue1 = databaseTester.getConnection().createQueryTable("result", "select * from assetlifecycleaction where id = 1");
		Assert.assertEquals(0, tableValue1.getRowCount());
	}

	@Test
	public void testGetAssetLifecycleAction() throws Exception {
		SearchExpression searchExpression = new SearchExpressionImpl();
		searchExpression.addCondition(new AssetLifecycleActionOwneridCondition("levi", StringComparer.EQUAL, false, false));
		searchExpression.addCondition(new AssetLifecycleActionAssetIdCondition(1L, NumberComparer.EQUAL));
		List<AssetLifecycleAction> assetLifecycleActions = applicationService.getAssetLifecycleAction(searchExpression);
		Assert.assertTrue(assetLifecycleActions.size() == 2);
	}

	@Test
	public void testGetAssetLifecycleActionById() throws Exception {
		AssetLifecycleAction assetLifecycleAction = applicationService.getAssetLifecycleActionById(1L);
		Assert.assertTrue(assetLifecycleAction != null);
	}

	@Test
	public void testSaveOrUpdateAssetLifecycleAction() throws Exception {
		AssetLifecycleAction assetLifecycleAction = new AssetLifecycleAction();
		assetLifecycleAction.setOwnerid("liuchao");
		assetLifecycleAction.setSubmitterid("liuchao");
		assetLifecycleAction.setAsset(new Asset(1L));
		assetLifecycleAction.setEvent("approve");
		applicationService.saveOrUpdateAssetLifecycleAction(assetLifecycleAction);

		ITable tableValue1 = databaseTester.getConnection()
				.createQueryTable("result", "select * from assetlifecycleaction where ownerid = 'liuchao'");
		Assert.assertTrue(tableValue1.getRowCount() == 1);

		assetLifecycleAction.setId(2L);

		applicationService.saveOrUpdateAssetLifecycleAction(assetLifecycleAction);
		ITable tableValue2 = databaseTester.getConnection().createQueryTable("result",
				"select * from assetlifecycleaction where id = 2 and event='approve'");
		Assert.assertTrue(tableValue2.getRowCount() == 1);
	}

	@Test
	public void testCountAssetBinaryByAssetId() throws Exception {
		long result = applicationService.countAssetBinaryByAssetId(1L);
		Assert.assertTrue(result == 2);
	}

	@Test
	public void testDeleteAssetBinary() throws Exception {

		AssetBinaryVersion binaryVersion = applicationService.getAssetBinaryById(2L);
		this.createFile(binaryVersion);
		String tmp = filePath + binaryVersion.getLocation();
		applicationService.deleteAssetBinary(2L);

		ITable tableValue2 = databaseTester.getConnection().createQueryTable("result", "select * from assetbinaryversion where id = 2");
		System.out.println("size=" + tableValue2.getRowCount());
		Assert.assertTrue(tableValue2.getRowCount() == 0);
		File file = new File(tmp.substring(0, tmp.indexOf(binaryVersion.getFileName())));
		Assert.assertTrue(!file.exists());
	}
	
	@Test
	public void testDeleteAssetBinaryWithScreenShots() throws Exception{
		AssetBinaryVersion binaryVersion = applicationService.getAssetBinaryById(1L);
		this.createFile(binaryVersion);
		
		String tmp = filePath + binaryVersion.getLocation();
		applicationService.deleteAssetBinary(1L);

		ITable tableValue2 = databaseTester.getConnection().createQueryTable("result", "select * from assetbinaryversion where id = 1");
		Assert.assertTrue(tableValue2.getRowCount() == 0);
		File file = new File(tmp.substring(0, tmp.indexOf(binaryVersion.getFileName())));
		Assert.assertTrue(!file.exists());
		
		
		byte[] content = this.getBytesFromFile(new File(testFileLocation));
		List<ScreenShots> screenShots=applicationService.getScreenShotsByAssetBinaryVersionId(binaryVersion.getId());
	
		for (ScreenShots screenShot : screenShots){
			String location = filePath + screenShot.getStoreLocation();
			String directory = location.substring(0, location.indexOf("test.zip"));
			File ssd = new File(directory);

			if (!ssd.exists()) {
				ssd.mkdirs();
			}
			this.saveFile(new File(location), content);
		}

		
		
		tableValue2 = databaseTester.getConnection().createQueryTable("result", "select * from screenshots where versionid = 1");
		assertEquals(0,tableValue2.getRowCount());
		File ssd=new File(filePath+"assetcatalog/20090711/1200/1/screenshots/");
		Assert.assertTrue(!ssd.exists());
	}

	@Test
	public void testGetAssetBinaryById() throws Exception {
		AssetBinaryVersion binaryVersion = applicationService.getAssetBinaryById(1L);
		Assert.assertTrue(binaryVersion != null);
	}

	@Test
	public void testGetAssetBinaryByAssetId() throws Exception {
		List<AssetBinaryVersion> binaryVersions = applicationService.getAssetBinaryByAssetId(1L);
		Assert.assertTrue(binaryVersions.size() == 2);
	}

	@Test
	public void testSaveAssetBinaryPicture() throws Exception {
		byte[] content = this.getBytesFromFile(new File(testFileLocation));

		applicationService.saveAssetBinaryPicture(2L, content, AssetPictureType.THUMBNAILBIGIMAGE, "THUMBNAILBIGIMAGE.jpg");

		AssetBinaryVersion binary = applicationService.getAssetBinaryById(2L);
		Asset asset = applicationService.getAsset(binary.getAsset().getId());

		Date date = asset.getCreateDate();
		String location = filePath + "assetcatalog/" + new SimpleDateFormat("yyyyMMdd").format((date)) + "/"
				+ new SimpleDateFormat("HHmm").format((date)) + "/" + asset.getId() + "/picture/binary/"
				+ AssetPictureType.THUMBNAILBIGIMAGE.getAttributeName() + "/" + AssetPictureType.THUMBNAILBIGIMAGE.getAttributeName() + ".jpg";

		File file = new File(location);
		Assert.assertTrue(file.exists());

		ITable tableValue1 = databaseTester.getConnection().createQueryTable("result", "select * from assetbinaryversion where id=2");
		Assert.assertEquals("assetcatalog/20090711/1200/1/picture/binary/thumbnailbigimage/THUMBNAILBIGIMAGE.jpg", tableValue1.getValue(0,
				"THUMBNAILBIGLOCATION"));
	}

	@Test
	public void testGetLatestVersion() throws Exception {
		AssetBinaryVersion binaryVersions = applicationService.getLatestVersion(1L);
		Assert.assertTrue(binaryVersions.getVersion().equalsIgnoreCase("2.0"));
	}

	@Test
	public void testGetLatestVersionByStatus() throws Exception {
		Status status = applicationService.getStatusById(1L);
		AssetBinaryVersion binaryVersions = applicationService.getLatestVersion(1L, status);
		Assert.assertTrue(binaryVersions.getVersion().equalsIgnoreCase("2.0"));
	}

	@Test
	public void testUpdateAssetBinary() throws Exception {
		AssetBinaryVersion binaryVersion = applicationService.getAssetBinaryById(1L);
		this.createFile(binaryVersion);
		File file = new File(testFileLocation);

		applicationService.updateAssetBinary(getBytesFromFile(file), 1L);
		File f = new File(filePath + binaryVersion.getLocation());
		Assert.assertTrue(f.exists());
	}

	@Test
	public void testUpdateBinaryVersion() throws Exception {
		AssetBinaryVersion binaryVersion = applicationService.getAssetBinaryById(1L);
		binaryVersion.setFileName("liuchao");
		applicationService.updateBinaryVersion(binaryVersion);

		ITable tableValue2 = databaseTester.getConnection().createQueryTable("result", "select * from assetbinaryversion where filename='liuchao'");
		Assert.assertTrue(tableValue2.getRowCount() == 1);
	}

	@Test
	public void testSaveAssetBinary() throws Exception {
		AssetBinaryVersion binaryVersion = new AssetBinaryVersion();
		Asset asset = applicationService.getAsset(1L);
		binaryVersion.setFileName("file.zip");
		binaryVersion.setVersion("5.0");
		binaryVersion.setStatus(new Status(1L));
		binaryVersion.setAsset(new Asset(1L));
		Date date = asset.getCreateDate();
		applicationService.saveAssetBinary(getBytesFromFile(new File(testFileLocation)), binaryVersion);
		String location = "assetcatalog/" + new SimpleDateFormat("yyyyMMdd").format((date)) + "/" + new SimpleDateFormat("HHmm").format((date)) + "/"
				+ binaryVersion.getAsset().getId() + "/binary/" + binaryVersion.getId() + "/" + binaryVersion.getFileName();
		System.out.println("save location=" + location);

		ITable tableValue2 = databaseTester.getConnection().createQueryTable("result", "select * from assetbinaryversion where version='5.0'");
		Assert.assertTrue(tableValue2.getRowCount() == 1);

		File file = new File(filePath + location);
		Assert.assertTrue(file.exists());

		FileUtils.forceDelete(file);
	}

	@Test
	public void testBatchAddTags() throws Exception {

		List<Long> assetIds = new ArrayList<Long>();
		for (int i = 1; i < 4; i++) {
			long index = i;
			assetIds.add(index);
		}
		String tagName = "batchadd";
		applicationService.batchAddTags(assetIds, tagName);
		ITable tableValue = databaseTester.getConnection().createQueryTable("result", "select * from tag where name = 'batchadd'");
		System.out.println("TAGID=" + tableValue.getValue(0, "ID"));
		Assert.assertTrue(tableValue.getRowCount() == 1);

		ITable tableValue2 = databaseTester.getConnection().createQueryTable("result",
				"select * from assettagrelation where tagid =" + tableValue.getValue(0, "ID"));
		Assert.assertTrue(tableValue2.getRowCount() == 3);
	}

	@Test
	public void testBatchDeleteComments() throws Exception {
		List<Long> commentId = new ArrayList<Long>();
		for (int i = 1; i < 4; i++) {
			long index = i;
			commentId.add(index);
		}

		ITable tableValue = databaseTester.getConnection().createQueryTable("result", "select * from comments where id=1 or id=2 or id=3");

		Assert.assertTrue(tableValue.getRowCount() == 3);
		applicationService.batchDeleteComments(commentId);

		ITable tableValue2 = databaseTester.getConnection().createQueryTable("result", "select * from comments where id=1 or id=2 or id=3");

		Assert.assertTrue(tableValue2.getRowCount() == 0);

	}

	@Test
	public void testBatchDeleteTagRelations() throws Exception {

		String tagName = "java";
		List<Long> assetIds = new ArrayList<Long>();
		assetIds.add(1L);
		assetIds.add(3L);

		ITable tableValue = databaseTester.getConnection().createQueryTable("result",
				"select * from assettagrelation where tagid=1 and assetid=1 or tagid=1 and assetid=3");

		Assert.assertTrue(tableValue.getRowCount() == 2);

		applicationService.batchDeleteTagRelations(assetIds, tagName, 1L);
		ITable tableValue2 = databaseTester.getConnection().createQueryTable("result",
				"select * from assettagrelation where tagid=1 and assetid=1 and versionid=1");

		Assert.assertTrue(tableValue2.getRowCount() == 0);
	}

	@Test
	public void testBatchUpdateAppStatus() throws Exception {
		List<Long> assetIds = new ArrayList<Long>();
		for (int i = 1; i < 4; i++) {
			long index = i;
			assetIds.add(index);
		}

		ITable tableValue = databaseTester.getConnection().createQueryTable("result", "select * from asset where statusid=8");

		Assert.assertTrue(tableValue.getRowCount() == 0);
		applicationService.batchUpdateAssetStatus(assetIds, 8L);

		ITable tableValue2 = databaseTester.getConnection().createQueryTable("result", "select * from asset where statusid=8");

		Assert.assertTrue(tableValue2.getRowCount() == 3);
	}

	@Test
	public void testBatchUpdateAssetBinaryStatus() throws Exception {
		List<Long> ids = new ArrayList<Long>();
		ids.add(1L);
		ids.add(2L);

		applicationService.batchUpdateAssetBinaryStatus(ids, 3L);

		ITable tableValue = databaseTester.getConnection().createQueryTable("result", "select * from assetbinaryversion where id=1");

		ITable tableValue2 = databaseTester.getConnection().createQueryTable("result", "select * from assetbinaryversion where id=2");

		Assert.assertEquals(3L, tableValue.getValue(0, "statusid"));
		Assert.assertEquals(3L, tableValue2.getValue(0, "statusid"));
	}

	@Test
	public void testBatchUpdateAppCategory() throws Exception {
		List<Long> assetIds = new ArrayList<Long>();
		for (int i = 1; i < 4; i++) {
			long index = i;
			assetIds.add(index);
		}

		ITable tableValue = databaseTester.getConnection().createQueryTable("result", "select * from assetcategoryrelation where ctgid=4");

		Assert.assertTrue(tableValue.getRowCount() == 0);
		applicationService.batchUpdateCategory(assetIds, 4L);

		ITable tableValue2 = databaseTester.getConnection().createQueryTable("result", "select * from assetcategoryrelation where ctgid=4");

		Assert.assertTrue(tableValue2.getRowCount() == 3);

	}

	@Test
	public void testDeleteUserDownloadHistoryById() throws Exception {
		applicationService.deleteUserDownloadHistoryById(1L);
		ITable tableValue2 = databaseTester.getConnection().createQueryTable("result", "select * from userdownloadhistory where id=1");

		Assert.assertTrue(tableValue2.getRowCount() == 0);
	}

	@Test
	public void testGetUserDownloadHistory() throws Exception {
		SearchExpression searchExpression = new SearchExpressionImpl();
		searchExpression.addCondition(new UserDownloadHistoryStatusCondition("finish", StringComparer.EQUAL, false, false));
		searchExpression.addCondition(new UserDownloadHistoryUseridCondition("levi", StringComparer.EQUAL, false, false));
		searchExpression.addCondition(new UserDownloadHistoryAssetIdCondition(1L, NumberComparer.EQUAL));
		List<UserDownloadHistory> userDownloadHistorys = applicationService.getUserDownloadHistory(searchExpression);
		Assert.assertTrue(userDownloadHistorys.size() == 2);
	}

	@Test
	public void testGetUserDownloadHistoryById() throws Exception {
		UserDownloadHistory userDownloadHistory = applicationService.getUserDownloadHistoryById(1L);
		Assert.assertTrue(userDownloadHistory != null);
	}

	@Test
	public void testGetUserDownloadHistoryByUserid() throws Exception {
		List<UserDownloadHistory> userDownloadHistorys = applicationService.getUserDownloadHistoryByUserid("levi");
		Assert.assertTrue(userDownloadHistorys.size() == 2);
	}

	@Test
	public void testGetUserDownloadHistoryByUseridAssetid() throws Exception {
		List<UserDownloadHistory> userDownloadHistorys = applicationService.getUserDownloadHistoryByUseridAssetid("levi", 1L);
		Assert.assertTrue(userDownloadHistorys.size() == 2);
	}

	@Test
	public void testSaveOrUpdateUserDownloadHistory() throws Exception {
		UserDownloadHistory userDownloadHistory = new UserDownloadHistory();
		userDownloadHistory.setAsset(new Asset(2L));
		userDownloadHistory.setVersion("3.0");
		userDownloadHistory.setUserid("liuchao");
		userDownloadHistory.setStatus("test");
		applicationService.saveOrUpdateUserDownloadHistory(userDownloadHistory);
		ITable tableValue2 = databaseTester.getConnection().createQueryTable("result", "select * from userdownloadhistory where userid='liuchao'");

		Assert.assertTrue(tableValue2.getRowCount() == 1);

		userDownloadHistory.setId(2L);
		applicationService.saveOrUpdateUserDownloadHistory(userDownloadHistory);

		ITable tableValue1 = databaseTester.getConnection().createQueryTable("result",
				"select * from userdownloadhistory where id=2 and userid='liuchao'");

		Assert.assertTrue(tableValue1.getRowCount() == 1);
	}

	@Test
	public void testSearchAsseetByPrice() throws Exception {
		SearchExpression searchExpression1 = new SearchExpressionImpl();
		searchExpression1.addCondition(new AssetAssetPriceAmountCondition(new BigDecimal(10.0), NumberComparer.EQUAL));
		List<Asset> assets1 = applicationService.searchAsset(searchExpression1);
		Assert.assertEquals(1, assets1.size());
	}

	@Test
	public void testSearchAsseetByAuthorId() throws Exception {
		SearchExpression searchExpression2 = new SearchExpressionImpl();
		searchExpression2.addCondition(new AssetAuthoridCondition("levi", StringComparer.EQUAL, false, false));
		List<Asset> assets2 = applicationService.searchAsset(searchExpression2);
		Assert.assertEquals(3, assets2.size());
	}

	@Test
	public void testSearchAsseetByAverageUserRating() throws Exception {
		SearchExpression searchExpression3 = new SearchExpressionImpl();
		searchExpression3.addCondition(new AssetAverageUserRatingCondition(3d, NumberComparer.EQUAL));
		List<Asset> assets3 = applicationService.searchAsset(searchExpression3);
		Assert.assertEquals(1, assets3.size());
	}

	@Test
	public void testSearchAsseetByBrief() throws Exception {
		SearchExpression searchExpression4 = new SearchExpressionImpl();
		searchExpression4.addCondition(new AssetBriefCondition("null", StringComparer.EQUAL, false, false));
		List<Asset> assets4 = applicationService.searchAsset(searchExpression4);
		Assert.assertEquals(3, assets4.size());
	}

	@Test
	public void testSearchAsseetByCategoryDescription() throws Exception {
		SearchExpression searchExpression5 = new SearchExpressionImpl();
		searchExpression5.addCondition(new AssetAssetCategoryDescriptionCondition("business", StringComparer.EQUAL, false, false));
		List<Asset> assets5 = applicationService.searchAsset(searchExpression5);
		Assert.assertEquals(1, assets5.size());
	}

	@Test
	public void testSearchAsseetByCategoryId() throws Exception {
		SearchExpression searchExpression6 = new SearchExpressionImpl();
		searchExpression6.addCondition(new AssetAssetCategoryIdCondition(1L, NumberComparer.EQUAL));
		List<Asset> assets6 = applicationService.searchAsset(searchExpression6);
		Assert.assertEquals(1, assets6.size());
	}

	@Test
	public void testSearchAsseetByCategoryName() throws Exception {
		SearchExpression searchExpression7 = new SearchExpressionImpl();
		searchExpression7.addCondition(new AssetAssetCategoryNameCondition("business", StringComparer.EQUAL, false, false));
		List<Asset> assets7 = applicationService.searchAsset(searchExpression7);
		Assert.assertEquals(1, assets7.size());
	}

	@Test
	public void testSearchAsseetByAssetCreateDate() throws Exception {
		SearchExpression searchExpression8 = new SearchExpressionImpl();
		searchExpression8.addCondition(new AssetCreateDateCondition(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse("2009-07-11 11:00:00"),
				DateComparer.GREAT_THAN));
		List<Asset> assets8 = applicationService.searchAsset(searchExpression8);
		Assert.assertEquals(3, assets8.size());
	}

	@Test
	public void testSearchAsseetByAssetDescription() throws Exception {
		SearchExpression searchExpression9 = new SearchExpressionImpl();
		searchExpression9.addCondition(new AssetDescriptionCondition("null", StringComparer.EQUAL, false, false));
		List<Asset> assets9 = applicationService.searchAsset(searchExpression9);
		Assert.assertEquals(3, assets9.size());
	}

	@Test
	public void testSearchAsseetByDownloadTime() throws Exception {
		SearchExpression searchExpression10 = new SearchExpressionImpl();
		searchExpression10.addCondition(new AssetDownloadCountCondition(1L, NumberComparer.EQUAL));
		List<Asset> assets10 = applicationService.searchAsset(searchExpression10);
		Assert.assertEquals(2, assets10.size());
	}

	@Test
	public void testSearchAsseetByAssetId() throws Exception {
		SearchExpression searchExpression11 = new SearchExpressionImpl();
		searchExpression11.addCondition(new AssetIdCondition(1L, NumberComparer.GREAT_EQUAL));
		searchExpression11.addCondition(new AssetIdCondition(3L, NumberComparer.LESS_EQUAL));
		List<Asset> assets11 = applicationService.searchAsset(searchExpression11);
		Assert.assertEquals(3, assets11.size());
	}

	@Test
	public void testSearchAsseetByLatestVersion() throws Exception {
		SearchExpression searchExpression12 = new SearchExpressionImpl();
		searchExpression12.addCondition(new AssetLatestVersionCondition("3.5", StringComparer.EQUAL, false, false));
		List<Asset> assets12 = applicationService.searchAsset(searchExpression12);
		Assert.assertEquals(1, assets12.size());
	}

	@Test
	public void testSearchAsseetByAssetName() throws Exception {
		SearchExpression searchExpression13 = new SearchExpressionImpl();
		searchExpression13.addCondition(new AssetNameCondition("APP1", StringComparer.EQUAL, false, false));
		List<Asset> assets13 = applicationService.searchAsset(searchExpression13);
		Assert.assertEquals(1, assets13.size());
	}

	@Test
	public void testSearchAsseetByPlatformDescription() throws Exception {
		SearchExpression searchExpression14 = new SearchExpressionImpl();
		searchExpression14.addCondition(new AssetPlatformDescriptionCondition("This is for Linux", StringComparer.EQUAL, false, false));
		List<Asset> assets14 = applicationService.searchAsset(searchExpression14);
		Assert.assertEquals(1, assets14.size());
	}

	@Test
	public void testSearchAsseetByPlatformId() throws Exception {
		SearchExpression searchExpression15 = new SearchExpressionImpl();
		searchExpression15.addCondition(new AssetPlatformIdCondition(1L, NumberComparer.EQUAL));
		List<Asset> assets15 = applicationService.searchAsset(searchExpression15);
		Assert.assertEquals(1, assets15.size());
	}

	@Test
	public void testSearchAsseetByPlatformName() throws Exception {
		SearchExpression searchExpression16 = new SearchExpressionImpl();
		searchExpression16.addCondition(new AssetPlatformNameCondition("linux", StringComparer.EQUAL, false, false));
		List<Asset> assets16 = applicationService.searchAsset(searchExpression16);
		Assert.assertEquals(1, assets16.size());
	}

	@Test
	public void testSearchAsseetBySource() throws Exception {
		SearchExpression searchExpression17 = new SearchExpressionImpl();
		searchExpression17.addCondition(new AssetSourceCondition("null", StringComparer.EQUAL, false, false));
		List<Asset> assets17 = applicationService.searchAsset(searchExpression17);
		Assert.assertEquals(1, assets17.size());
	}

	@Test
	public void testSearchAsseetByStatusId() throws Exception {
		SearchExpression searchExpression18 = new SearchExpressionImpl();
		searchExpression18.addCondition(new AssetStatusIdCondition(3L, NumberComparer.EQUAL));
		List<Asset> assets18 = applicationService.searchAsset(searchExpression18);
		Assert.assertEquals(1, assets18.size());
	}

	@Test
	public void testSearchAsseetByStatusName() throws Exception {
		SearchExpression searchExpression19 = new SearchExpressionImpl();
		searchExpression19.addCondition(new AssetStatusNameCondition("uploaded", StringComparer.EQUAL, false, false));
		List<Asset> assets19 = applicationService.searchAsset(searchExpression19);
		Assert.assertEquals(2, assets19.size());
	}

	@Test
	public void testSearchAsseetByTagDescription() throws Exception {
		SearchExpression searchExpression20 = new SearchExpressionImpl();
		searchExpression20.addCondition(new AssetTagDescriptionCondition("this is for java", StringComparer.EQUAL, false, false));
		List<Asset> assets20 = applicationService.searchAsset(searchExpression20);
		Assert.assertEquals(2, assets20.size());
	}

	@Test
	public void testSearchAsseetByTagId() throws Exception {
		SearchExpression searchExpression21 = new SearchExpressionImpl();
		searchExpression21.addCondition(new AssetTagIdCondition(1L, NumberComparer.EQUAL));
		List<Asset> assets21 = applicationService.searchAsset(searchExpression21);
		Assert.assertEquals(2, assets21.size());
	}

	@Test
	public void testSearchAsseetByTagName() throws Exception {
		SearchExpression searchExpression22 = new SearchExpressionImpl();
		searchExpression22.addCondition(new AssetTagNameCondition("java", StringComparer.EQUAL, false, false));
		List<Asset> assets22 = applicationService.searchAsset(searchExpression22);
		Assert.assertEquals(2, assets22.size());
	}

	@Test
	public void testSearchAsseetByAssetUpdateDate() throws Exception {
		SearchExpression searchExpression23 = new SearchExpressionImpl();

		searchExpression23.addCondition(new AssetUpdateDateCondition(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse("2009-07-10 11:00:00"),
				DateComparer.GREAT_THAN));
		List<Asset> assets23 = applicationService.searchAsset(searchExpression23);
		Assert.assertEquals(3, assets23.size());
	}

	@Test
	public void testSearchAsseetByPurchaseUser() throws Exception {
		SearchExpression searchExpression24 = new SearchExpressionImpl();
		searchExpression24.addCondition(new AssetPurchaseUserCondition("levi", StringComparer.EQUAL, false, false));
		List<Asset> assets24 = applicationService.searchAsset(searchExpression24);
		Assert.assertEquals(2, assets24.size());
	}

	@Test
	public void testSearchAsseetByComplicatedConditions() throws Exception {
		SearchExpression searchExpression = new SearchExpressionImpl();
		searchExpression.addCondition(new AssetNameCondition("APP1", StringComparer.EQUAL, false, false));
		searchExpression.addCondition(new AssetAuthoridCondition("levi", StringComparer.EQUAL, false, false));
		searchExpression.addCondition(new AssetLatestVersionCondition("3.5", StringComparer.EQUAL, false, false));
		List<Asset> assets = applicationService.searchAsset(searchExpression);
		Assert.assertEquals(1, assets.size());
	}

	@Test
	public void testSearchAsseetByComplicatedConditions1() throws Exception {
		SearchExpression searchExpression = new SearchExpressionImpl();
		searchExpression.addCondition(new AssetNameCondition("APP1", StringComparer.EQUAL, false, false));
		searchExpression.addCondition(new AssetPlatformNameCondition("linux", StringComparer.EQUAL, false, false));
		searchExpression.addCondition(new AssetStatusNameCondition("uploaded", StringComparer.EQUAL, false, false));

		List<Asset> assets = applicationService.searchAsset(searchExpression);
		Assert.assertEquals(1, assets.size());
	}

	@Test
	public void testAssociateOrDisAssociatePlatform() throws Exception {
		applicationService.associatePlatform(2L, 2L);

		ITable tableValue1 = databaseTester.getConnection().createQueryTable("result",
				"select * from assetplatformrelation where assetid=2 and platformid=2");

		Assert.assertEquals(1, tableValue1.getRowCount());

		applicationService.disassociatePlatform(2L, 2L);

		ITable tableValue = databaseTester.getConnection().createQueryTable("result",
				"select * from assetplatformrelation where assetid=2 and platformid=2");

		Assert.assertEquals(0, tableValue.getRowCount());
	}

	@Test
	public void testAssociatePlatformOrDisAssociateByListPlatformid() throws Exception {
		List<Long> platformIds = new ArrayList<Long>();
		platformIds.add(2L);
		platformIds.add(3L);
		applicationService.associatePlatform(1L, platformIds);

		ITable tableValue1 = databaseTester.getConnection().createQueryTable("result",
				"select * from assetplatformrelation where assetid=1 and platformid=2 or assetid=1 and platformid=3");

		Assert.assertTrue(tableValue1.getRowCount() == 2);

		applicationService.disassociatePlatform(1L);

		ITable tableValue = databaseTester.getConnection().createQueryTable("result", "select * from assetplatformrelation where assetid=1");

		Assert.assertTrue(tableValue.getRowCount() == 0);
	}

	@Test
	public void testGetPlatformByAssetId() throws Exception {
		List<Platform> platforms = applicationService.getPlatformByAssetId(1L);

		Assert.assertTrue(platforms.size() == 1);
	}

	@Test
	public void testGetRestrictedType() throws Exception {
		RestrictedType restrictedType = applicationService.getRestrictedTypeById(1L);

		Assert.assertTrue(restrictedType != null);

		restrictedType = applicationService.getRestrictedTypeByType("test");
		Assert.assertTrue(restrictedType != null);
	}

	@Test
	public void testSaveOrUpdateOrDeleteRestrictedType() throws Exception {
		RestrictedType restrictedType = new RestrictedType();

		restrictedType.setType("black");
		applicationService.saveRestrictedType(restrictedType);

		ITable tableValue = databaseTester.getConnection().createQueryTable("result", "select * from restrictedtype where id=4 and type='black'");

		Assert.assertTrue(tableValue.getRowCount() == 1);

		restrictedType.setType("white");
		applicationService.updateRestrictedType(restrictedType);

		ITable tableValue1 = databaseTester.getConnection().createQueryTable("result", "select * from restrictedtype where id=4 and type='white'");

		Assert.assertTrue(tableValue1.getRowCount() == 1);

		applicationService.deleteRestrictedTypeById(2L);
		ITable tableValue2 = databaseTester.getConnection().createQueryTable("result", "select * from restrictedtype where id=2");

		Assert.assertEquals(0, tableValue2.getRowCount());
	}

	@Test
	public void testUpdateAssetRestrictionRelation() throws Exception {
		AssetRestrictionRelation arr = new AssetRestrictionRelation();
		arr.setId(1L);
		arr.setAsset(applicationService.getAsset(2L));
		arr.setRestrictedType(applicationService.getRestrictedTypeById(2L));

		applicationService.updateAssetRestrictionRelation(arr);
		ITable tableValue = databaseTester.getConnection().createQueryTable("result",
				"select * from assetrestrictionrelation where id=1 and restrictionid=2 and assetid=2");

		Assert.assertEquals(1, tableValue.getRowCount());
	}

	@Test
	public void testGetAllRestrictedType() {
		List<RestrictedType> restrictedTypes = applicationService.getAllRestrictedType(0, 2);
		Assert.assertEquals(2, restrictedTypes.size());
	}

	@Test
	public void testGetAllRestrictedTypesByAssetId() {
		List<RestrictedType> restrictedTypes = applicationService.getAllRestrictedTypesByAssetId(1L, 0, 2);
		Assert.assertEquals(2, restrictedTypes.size());
	}

	@Test
	public void testGetScreenShots() throws Exception {
		List<ScreenShots> screenShots = applicationService.getScreenShotsByAssetId(1L);

		Assert.assertTrue(screenShots.size() == 1);
	}

	@Test
	public void testSaveAndDeleteScreenShotsById() throws Exception {
		byte[] data = getBytesFromFile(new File(testFileLocation));
		ScreenShots screenShots = new ScreenShots();
		Asset asset = applicationService.getAsset(1L);
		screenShots.setAsset(new Asset(1L));
		applicationService.saveScreenShots(screenShots, data, "file.zip");

		Date date = asset.getCreateDate();
		String location = filePath + "assetcatalog/" + new SimpleDateFormat("yyyyMMdd").format((date)) + "/"
				+ new SimpleDateFormat("HHmm").format((date)) + "/1/screenshots/file.zip";
		File file = new File(location);
		Assert.assertTrue(file.exists());

		ITable tableValue1 = databaseTester.getConnection().createQueryTable("result", "select * from screenshots where id=2");

		Assert.assertEquals(1, tableValue1.getRowCount());

		applicationService.deleteScreenShotsById(2L);
		ITable tableValue = databaseTester.getConnection().createQueryTable("result", "select * from screenshots where id=2");

		Assert.assertEquals(0, tableValue.getRowCount());
		Assert.assertTrue(!file.exists());

	}

	@Test
	public void testSaveScreenShots() throws Exception {
		byte[] data = getBytesFromFile(new File(testFileLocation));

		/**
		 * Create a ScreenShots object which has AssetBinaryVersion and save it
		 */
		ScreenShots screenShots = new ScreenShots();
		Asset asset = applicationService.getAsset(2L);
		AssetBinaryVersion abv = applicationService.getAssetBinaryById(2L);
		screenShots.setAsset(asset);
		screenShots.setBinaryVersion(abv);

		applicationService.saveScreenShots(screenShots, data, "file.zip");

		Date date = asset.getCreateDate();
		String location = filePath + "assetcatalog/" + new SimpleDateFormat("yyyyMMdd").format((date)) + "/"
				+ new SimpleDateFormat("HHmm").format((date)) + "/2" + "/binary/" + screenShots.getBinaryVersion().getId() + "/screenshots/file.zip";
		File file = new File(location);
		Assert.assertTrue(file.exists());

		ITable tableValue1 = databaseTester.getConnection().createQueryTable("result", "select * from screenshots");

		Assert.assertEquals(2, tableValue1.getRowCount());
	}

	@Test
	public void testSaveExistScreenShots() throws Exception {
		ScreenShots screenShots = new ScreenShots();
		screenShots.setAsset(new Asset(1L));
		byte[] data = getBytesFromFile(new File(testFileLocation));
		try {
			applicationService.saveScreenShots(screenShots, data, "test.zip");
		} catch (NgpRuntimeException e) {
			Assert.assertEquals("the file is exist", e.getMessage());
		}

	}

	@Test
	public void testUpdateScreenShots() throws Exception {
		byte[] data = getBytesFromFile(new File(testFileLocation));
		ScreenShots screenShots = applicationService.getScreenShotsById(1L);
		applicationService.updateScreenShots(screenShots, data, "file.zip");
		String location = screenShots.getStoreLocation();

		ITable tableValue = databaseTester.getConnection().createQueryTable("result",
				"select * from screenshots where id=1 and storelocation='" + location + "'");

		Assert.assertEquals(1, tableValue.getRowCount());
		File file = new File(filePath + location);
		Assert.assertTrue(file.exists());

		applicationService.deleteScreenShotsByAssetId(1L);
		ITable tableValue2 = databaseTester.getConnection().createQueryTable("result", "select * from screenshots where assetid=1");

		Assert.assertEquals(0, tableValue2.getRowCount());
		Assert.assertTrue(!file.exists());

	}

	@Test
	public void testSearchScreenShots() {
		SearchExpressionImpl searchExpression = new SearchExpressionImpl();
		searchExpression.addCondition(new ScreenShotsAssetIdCondition(1L, NumberComparer.EQUAL));

		List<ScreenShots> screenShots = applicationService.searchScreenShots(searchExpression);

		Assert.assertTrue(screenShots.size() == 1);
	}

	@Test
	public void testSearchScreenShotsCount() {
		SearchExpressionImpl searchExpression = new SearchExpressionImpl();
		searchExpression.addCondition(new ScreenShotsAssetIdCondition(1L, NumberComparer.EQUAL));

		int count = applicationService.searchScreenShotsCount(searchExpression);

		Assert.assertTrue(count == 1);
	}

	@Test
	public void testGetAssetPriceByAssetId() throws Exception {
		List<AssetPrice> assetPrices = applicationService.getAssetPriceByAssetId(1L);

		Assert.assertEquals(1, assetPrices.size());
	}

	@Test
	public void testSaveOrUpdateAssetPrice() throws Exception {
		AssetPrice assetPrice = new AssetPrice();

		assetPrice.setCurrency("abc");
		assetPrice.setAsset(new Asset(1L));
		applicationService.saveAssetPrice(assetPrice);
		ITable tableValue2 = databaseTester.getConnection().createQueryTable("result", "select * from assetprice where currency='abc' and id=4");

		Assert.assertTrue(tableValue2.getRowCount() == 1);

		assetPrice.setCurrency("def");
		applicationService.updateAssetPrice(assetPrice);
		ITable tableValue1 = databaseTester.getConnection().createQueryTable("result", "select * from assetprice where currency='def' and id=4");

		Assert.assertTrue(tableValue1.getRowCount() == 1);
	}

	@Test
	public void testDeleteAssetPriceByAssetId() throws Exception {
		applicationService.deleteAssetPriceByAssetId(1L);
		ITable tableValue1 = databaseTester.getConnection().createQueryTable("result", "select * from assetprice where assetid=1");

		Assert.assertTrue(tableValue1.getRowCount() == 0);
	}

	@Test
	public void testSaveOrUpdateAssetProvider() throws Exception {

		Provider provider = new Provider();
		provider.setCountry("test");
		provider.setName("lc");

		applicationService.saveAssetProvider(provider);
		ITable tableValue2 = databaseTester.getConnection().createQueryTable("result", "select * from assetprovider where country='test' and id=4");

		Assert.assertTrue(tableValue2.getRowCount() == 1);

		provider.setCountry("def");
		applicationService.updateAssetProvider(provider);
		ITable tableValue1 = databaseTester.getConnection().createQueryTable("result", "select * from assetprovider where country='def' and id=4");

		Assert.assertTrue(tableValue1.getRowCount() == 1);
	}

	@Test
	public void testGetAssetProviderByName() throws Exception {
		Provider provider = applicationService.getAssetProviderByName("liuchao");

		Assert.assertTrue(provider != null);
	}

	@Test
	public void testSearchAssetProviderCount() {
		SearchExpressionImpl searchExpression = new SearchExpressionImpl();
		searchExpression.addCondition(new ProviderNameCondition("liuchao", StringComparer.EQUAL, true, true));

		int count = applicationService.searchAssetProviderCount(searchExpression);

		Assert.assertTrue(count == 1);
	}

	@Test
	public void testAssetGroup() throws Exception {
		List<Long> ids = new ArrayList<Long>();
		ids.add(2L);
		ids.add(3L);
		applicationService.associateAssetGroup(1L, ids);

		ITable tableValue1 = databaseTester.getConnection().createQueryTable("result",
				"select * from asset where parentid=1 and id=2 or parentid=1 and id=3");

		Assert.assertEquals(2, tableValue1.getRowCount());

		applicationService.disassociateAssetGroup(ids);
		ITable tableValue = databaseTester.getConnection().createQueryTable("result",
				"select * from asset where parentid=1 and id=2 or parentid=1 and id=3");

		Assert.assertEquals(0, tableValue.getRowCount());
	}

	@Test
	public void testCategoryRelationship() throws Exception {
		List<Long> ids = new ArrayList<Long>();
		ids.add(2L);
		ids.add(3L);
		applicationService.createCategoryRelationShip(1L, ids);

		ITable tableValue1 = databaseTester.getConnection().createQueryTable("result",
				"select * from category where parentid=1 and id=2 or parentid=1 and id=3");

		Assert.assertEquals(2, tableValue1.getRowCount());

		applicationService.dropCategoryRelationShip(ids);
		ITable tableValue = databaseTester.getConnection().createQueryTable("result",
				"select * from category where parentid=1 and id=2 or parentid=1 and id=3");

		Assert.assertEquals(0, tableValue.getRowCount());
	}

	@Test
	public void testAddVarcharAtrribute() throws Exception {
		applicationService.addAttribute(1L, EntityType.ASSET, "Category", "music");
		ITable tableValue1 = databaseTester.getConnection().createQueryTable("result", "select * from attributevaluechar where attributevalueid=8");

		Assert.assertEquals("music", tableValue1.getValue(0, "value"));

	}

	@Test
	public void testAddNumberAtrribute() throws Exception {
		applicationService.addAttribute(1L, EntityType.ASSET, "sampleAttributeNumber", 1.0f);
		ITable tableValue2 = databaseTester.getConnection().createQueryTable("result", "select * from attributevaluenumber where attributevalueid=3");
		Object value = tableValue2.getValue(0, "value");
		Float floatValue = Float.valueOf(value.toString());
		Assert.assertTrue(floatValue == 1L);

		applicationService.addAttribute(1L, EntityType.ASSET, "sampleAttributeNumber", 10f);
		tableValue2 = databaseTester.getConnection().createQueryTable("result", "select * from attributevaluenumber where attributevalueid=4");
		floatValue = Float.valueOf(tableValue2.getValue(0, "value").toString());
		Assert.assertTrue(floatValue == 10L);

	}

	@Test
	public void testAddDateAtrribute() throws Exception {
		applicationService.addAttribute(1L, EntityType.ASSET, "sampleAttributeDate", new SimpleDateFormat("yyyy-MM-dd hh:mm:ss")
				.parse("2009-07-11 11:00:00 PM"));
		ITable tableValue3 = databaseTester.getConnection().createQueryTable("result", "select * from attributevaluedate where attributevalueid=2");

		Assert.assertEquals(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse("2009-07-11 11:00:00 PM"), tableValue3.getValue(0, "value"));
	}

	@Test
	public void testAddNewAtrribute() throws Exception {
		applicationService.addAttribute(1L, EntityType.ASSET, "Status", "downloaded");
		ITable tableValue = databaseTester.getConnection().createQueryTable("result", "select * from attribute where ATTRIBUTENAME='Status'");
		Assert.assertEquals(1, tableValue.getRowCount());

		ITable tableValue2 = databaseTester.getConnection().createQueryTable("result", "select * from attributevaluechar");

		Assert.assertEquals("downloaded", tableValue2.getValue(7, "value"));

		ITable tableValue3 = databaseTester.getConnection().createQueryTable("result", "select * from entityattribute");

		Assert.assertEquals(7, tableValue3.getRowCount());

	}

	@Test
	public void testGetAttributeValue() throws Exception {
		Asset asset = new Asset();
		asset.setStatus(new Status(1L));
		this.applicationService.saveAsset(asset);
		applicationService.addAttribute(asset.getId(), EntityType.ASSET, "MyNumber", 1L);
		List<AttributeValue> attributeValues = applicationService.getAttributeValue(asset.getId(), EntityType.ASSET, "MyNumber");
		Assert.assertEquals(1, attributeValues.size());
	}

	@Test
	public void testGetAttributes() throws Exception {
		Asset asset = new Asset();
		asset.setStatus(new Status(1L));
		this.applicationService.saveAsset(asset);
		applicationService.addAttribute(asset.getId(), EntityType.ASSET, "newValue1", 1L);
		applicationService.addAttribute(asset.getId(), EntityType.ASSET, "newValue2", 1L);
		
		Map<String, List<AttributeValue>> attributes = applicationService.getAttributes(asset.getId(), EntityType.ASSET);
		Assert.assertEquals(1, attributes.get("newValue1").size());
	}

	@Test
	public void testRemoveAttributes() throws Exception {
		
		applicationService.addAttribute(1L, EntityType.ASSET, "Category","sport");
		applicationService.removeAttributes(1L, EntityType.ASSET, "Category");
		ITable tableValue = databaseTester.getConnection().createQueryTable("result", "select * from attributevaluechar where attributeid=1 and entityid=1");

		Assert.assertEquals(0, tableValue.getRowCount());
	}

	protected byte[] getBytesFromFile(File file) {
		FileInputStream is;
		ByteArrayOutputStream out = new ByteArrayOutputStream(1000);
		try {
			is = new FileInputStream(file);
			byte[] b = new byte[1000];
			int n;
			while ((n = is.read(b)) != -1) {
				out.write(b, 0, n);
			}
			is.close();
			out.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return out.toByteArray();

	}

	protected static void saveFile(File file, byte[] content) {
		try {
			FileUtils.writeByteArrayToFile(file, content);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected void createFile(AssetBinaryVersion binaryVersion) {
		byte[] content = this.getBytesFromFile(new File(testFileLocation));

		String location = filePath + binaryVersion.getLocation();
		System.out.println("location=" + location);
		String directory = location.substring(0, location.indexOf(binaryVersion.getFileName()));
		System.out.println("directory=" + directory);
		File file = new File(directory);

		if (!file.exists()) {
			file.mkdirs();
		}
		this.saveFile(new File(location), content);
	}

	@Test
	public void testSearch() throws Exception {
		SearchExpression searchExpression = null;
		searchExpression = new SearchExpressionImpl();
		searchExpression.addCondition(new AssetNameCondition("APP", StringComparer.LIKE, true, false));
		// searchExpression.addCondition(new AssetDescriptionCondition(keyword,
		// StringComparer.LIKE, true, false));
		searchExpression.addOrder(AssetOrderBy.PROVIDERPRIORITY, OrderEnum.ASC);
		searchExpression.setFirst(0);
		searchExpression.setMax(Integer.MAX_VALUE);

		// searchExpression.addCondition(new AssetCategoryIdCondition(
		// 2L,
		// NumberComparer.EQUAL));
		// searchExpression.addCondition(new AssetTagIdCondition(1L,
		// NumberComparer.EQUAL));
		// searchExpression.addCondition(new AssetPlatformIdCondition(3L,
		// NumberComparer.EQUAL));
		// searchExpression.addCondition(new AssetStatusIdCondition(1L,
		// NumberComparer.EQUAL));
		List<Asset> list = applicationService.searchAsset(searchExpression);
		System.out.println("asset list" + list);

		long count = applicationService.searchAssetPageCount(searchExpression);
		System.out.println(count);
	}

	@Test
	public void testSearchCategoryId() {
		SearchExpression searchExpression1 = new SearchExpressionImpl();
		searchExpression1.addCondition(new AssetAssetCategoryIdCondition(1L, NumberComparer.EQUAL));
		List<Asset> assets1 = applicationService.searchAsset(searchExpression1);
		Assert.assertEquals(1, assets1.size());
	}

	// @Test
	// public void testSearchCategoryDescription() {
	// SearchExpression searchExpression1 = new SearchExpressionImpl();
	// searchExpression1.addCondition(new
	// AssetAssetCategoryDescriptionCondition(
	// "sports", StringComparer.EQUAL, true, false));
	// List<Asset> assets1 = applicationService.searchAsset(searchExpression1);
	// Assert.assertEquals(1, assets1.size());
	// }

	// @Test
	// public void testSearchCategoryName() {
	// SearchExpression searchExpression1 = new SearchExpressionImpl();
	// searchExpression1.addCondition(new
	// AssetAssetCategoryNameCondition("sports",
	// StringComparer.EQUAL, true, false));
	// List<Asset> assets1 = applicationService.searchAsset(searchExpression1);
	// Assert.assertEquals(1, assets1.size());
	// }

	// @Test
	// public void testSearchCategoryJoin() {
	// SearchExpression searchExpression1 = new SearchExpressionImpl();
	// searchExpression1.addCondition(new AssetAssetCategoryIdCondition(3L,
	// NumberComparer.EQUAL)
	// .and(new AssetAssetCategoryDescriptionCondition("sports",
	// StringComparer.EQUAL, true, false)
	// .and(new AssetAssetCategoryNameCondition("sports",
	// StringComparer.EQUAL, true, false))));
	// List<Asset> assets1 = applicationService.searchAsset(searchExpression1);
	// Assert.assertEquals(1, assets1.size());
	// }

	@Test
	public void testSearchCategoryAssetId() {
		// SearchExpression searchExpression1 = new SearchExpressionImpl();
		// searchExpression1.addCondition(new
		// CategoryAssetCategoryRelationAssetIdCondition(1L,
		// NumberComparer.EQUAL));
		applicationService.getAllCategoryByAssetId(1L, 1, 10);
	}

	@Test
	public void testSearchCommentsByAssetIdAndUserId() throws Exception {
		SearchExpression searchExpression = new SearchExpressionImpl();
		searchExpression.addCondition(new CommentsAssetIdCondition(1L, NumberComparer.EQUAL));
		searchExpression.addCondition(new CommentsUseridCondition("levi", StringComparer.EQUAL, false, false));

		List<Comments> comments = applicationService.searchComments(searchExpression);
		Assert.assertEquals(1, comments.size());

	}

	@Test
	public void testSearchCommentsByAssetRating() throws Exception {
		SearchExpression searchExpression = new SearchExpressionImpl();
		searchExpression.addCondition(new CommentsAssetRatingCondition(0d, NumberComparer.EQUAL));

		List<Comments> comments = applicationService.searchComments(searchExpression);
		Assert.assertEquals(1, comments.size());

	}

	@Test
	public void testSearchCategoryByAssetId() throws Exception {
		SearchExpression searchExpression = new SearchExpressionImpl();
		searchExpression.addCondition(new CategoryAssetCategoryRelationAssetIdCondition(1L, NumberComparer.EQUAL));

		List<Category> categories = applicationService.searchCategory(searchExpression);
		Assert.assertEquals(1, categories.size());
	}

	@Test
	public void testCountComment() {
		SearchExpression searchExpression = new SearchExpressionImpl();
		searchExpression.addCondition(new CommentsContentCondition("s", StringComparer.LIKE, true, false));

		searchExpression.setFirst(0);
		searchExpression.setMax(Integer.MAX_VALUE);

		long l = applicationService.countComments(searchExpression);
	}

	@Test
	public void testSearchComments() {
		SearchExpression searchExpression = new SearchExpressionImpl();
		searchExpression.addCondition(new CommentsContentCondition("s", StringComparer.LIKE, true, false));

		searchExpression.setFirst(0);
		searchExpression.setMax(5);

		// searchExpression.addOrder(CommentsOrderBy.PROVIDERID, OrderEnum.ASC);
		List<Comments> list = applicationService.searchComments(searchExpression);
	}

	@Test
	public void testSearchAppEav() {
		SearchExpression searchExpression = new SearchExpressionImpl();
		Asset asset = new Asset();
		asset.setName("liang");
		asset.setStatus(applicationService.getStatusByName("provided"));
		applicationService.saveAsset(asset);
		applicationService.addAttribute(asset.getId(), EntityType.ASSET, "test1", 1F);
		applicationService.addAttribute(asset.getId(), EntityType.ASSET, "test2", new Date());
		applicationService.addAttribute(asset.getId(), EntityType.ASSET, "test3", "value");

		Condition c = new EavStringCondition(EntityType.ASSET, "test3", "va", StringComparer.LIKE, true, false);

		searchExpression.addCondition(c);

		List<Asset> list1 = applicationService.searchAsset(searchExpression);

		System.out.println(list1.size());

		Assert.assertTrue(list1.size() == 1);

		searchExpression = new SearchExpressionImpl();
		c = new EavStringCondition(EntityType.ASSET, "test3", "va", StringComparer.LIKE, true, false);
		c = c.and(new AssetNameCondition("liang", StringComparer.EQUAL, true, false));
		searchExpression.addCondition(c);
		list1 = applicationService.searchAsset(searchExpression);
		Assert.assertTrue(list1.size() == 1);

		searchExpression = new SearchExpressionImpl();
		c = new EavStringCondition(EntityType.ASSET, "test3", "va", StringComparer.LIKE, true, false);
		c = c.and(new AssetNameCondition("or", StringComparer.EQUAL, true, false));
		searchExpression.addCondition(c);
		list1 = applicationService.searchAsset(searchExpression);
		Assert.assertTrue(list1.size() == 0);

		searchExpression = new SearchExpressionImpl();
		c = new EavStringCondition(EntityType.ASSET, "test3", "va", StringComparer.LIKE, true, false);
		c = c.or(new AssetNameCondition("or", StringComparer.EQUAL, true, false));
		searchExpression.addCondition(c);
		list1 = applicationService.searchAsset(searchExpression);
		Assert.assertTrue(list1.size() == 1);

		searchExpression = new SearchExpressionImpl();
		c = new EavStringCondition(EntityType.ASSET, "test3", "va", StringComparer.LIKE, true, false);
		c = c.and(new AssetNameCondition("liang", StringComparer.EQUAL, true, false));
		searchExpression.addCondition(c);
		searchExpression.addCondition(new EavNumberCondition(EntityType.ASSET, "test1", 1F, NumberComparer.EQUAL));
		list1 = applicationService.searchAsset(searchExpression);
		Assert.assertTrue(list1.size() == 1);

	}
	
	@Test
	public void testSearchAppEav2() {
		String value="va%";
		
		
		SearchExpression searchExpression = new SearchExpressionImpl();
		Asset asset = new Asset();
		asset.setName("liang");
		asset.setStatus(applicationService.getStatusByName("provided"));
		applicationService.saveAsset(asset);
		applicationService.addAttribute(asset.getId(), EntityType.ASSET, "test1", 1F);
		applicationService.addAttribute(asset.getId(), EntityType.ASSET, "test2", new Date());
		applicationService.addAttribute(asset.getId(), EntityType.ASSET, "test3", "value");

		Condition c = new EavStringCondition(EntityType.ASSET, "test3", "va%", StringComparer.LIKE, true, true);

		searchExpression.addCondition(c);

		List<Asset> list1 = applicationService.searchAsset(searchExpression);

		System.out.println(list1.size());

		Assert.assertTrue(list1.size() == 0);

		searchExpression = new SearchExpressionImpl();
		c = new EavStringCondition(EntityType.ASSET, "test3", "%alue", StringComparer.LIKE, true, true);
		
		searchExpression.addCondition(c);
		list1 = applicationService.searchAsset(searchExpression);
		Assert.assertTrue(list1.size() == 0);

		searchExpression = new SearchExpressionImpl();
		c = new EavStringCondition(EntityType.ASSET, "test3", "va", StringComparer.LIKE, true, true);
		searchExpression.addCondition(c);
		list1 = applicationService.searchAsset(searchExpression);
		Assert.assertTrue(list1.size() == 0);
		
		searchExpression = new SearchExpressionImpl();
		c = new EavStringCondition(EntityType.ASSET, "test3", "%alue", StringComparer.LIKE, true, true);
		
		searchExpression.addCondition(c);
		list1 = applicationService.searchAsset(searchExpression);
		Assert.assertTrue(list1.size() == 0);

		searchExpression = new SearchExpressionImpl();
		c = new EavStringCondition(EntityType.ASSET, "test3", "%va", StringComparer.LIKE, true, true);
		searchExpression.addCondition(c);
		list1 = applicationService.searchAsset(searchExpression);
		Assert.assertTrue(list1.size() == 0);
		
		searchExpression = new SearchExpressionImpl();
		c = new EavStringCondition(EntityType.ASSET, "test3", "a%", StringComparer.LIKE, true, true);
		searchExpression.addCondition(c);
		list1 = applicationService.searchAsset(searchExpression);
		Assert.assertTrue(list1.size() == 0);

		

	}
	
	@Test
	public void testSearchAppEav3() {
		SearchExpression searchExpression = new SearchExpressionImpl();
		Asset asset = new Asset();
		asset.setName("liang");
		asset.setStatus(applicationService.getStatusByName("provided"));
		applicationService.saveAsset(asset);
		applicationService.addAttribute(asset.getId(), EntityType.ASSET, "test1", 1F);
		applicationService.addAttribute(asset.getId(), EntityType.ASSET, "test2", new Date());
		applicationService.addAttribute(asset.getId(), EntityType.ASSET, "test3", "value");
		
		searchExpression = new SearchExpressionImpl();
		Condition c = new EavStringCondition(EntityType.ASSET, "test3", "al", StringComparer.LIKE, true, false);
		searchExpression.addCondition(c);
		List<Asset> list1 = applicationService.searchAsset(searchExpression);
		Assert.assertTrue(list1.size() == 1);

		searchExpression = new SearchExpressionImpl();
		 c = new EavStringCondition(EntityType.ASSET, "test3", "va*", StringComparer.LIKE, true, true);

		searchExpression.addCondition(c);

		 list1 = applicationService.searchAsset(searchExpression);

		System.out.println(list1.size());

		Assert.assertTrue(list1.size() == 1);

		searchExpression = new SearchExpressionImpl();
		c = new EavStringCondition(EntityType.ASSET, "test3", "*alue", StringComparer.LIKE, true, true);
		
		searchExpression.addCondition(c);
		list1 = applicationService.searchAsset(searchExpression);
		Assert.assertTrue(list1.size() == 1);

		searchExpression = new SearchExpressionImpl();
		c = new EavStringCondition(EntityType.ASSET, "test3", "va", StringComparer.LIKE, true, true);
		searchExpression.addCondition(c);
		list1 = applicationService.searchAsset(searchExpression);
		Assert.assertTrue(list1.size() == 0);
		
		searchExpression = new SearchExpressionImpl();
		c = new EavStringCondition(EntityType.ASSET, "test3", "*alue", StringComparer.LIKE, true, true);
		
		searchExpression.addCondition(c);
		list1 = applicationService.searchAsset(searchExpression);
		Assert.assertTrue(list1.size() == 1);

		searchExpression = new SearchExpressionImpl();
		c = new EavStringCondition(EntityType.ASSET, "test3", "*va", StringComparer.LIKE, true, true);
		searchExpression.addCondition(c);
		list1 = applicationService.searchAsset(searchExpression);
		Assert.assertTrue(list1.size() == 0);
		
		searchExpression = new SearchExpressionImpl();
		c = new EavStringCondition(EntityType.ASSET, "test3", "a*", StringComparer.LIKE, true, true);
		searchExpression.addCondition(c);
		list1 = applicationService.searchAsset(searchExpression);
		Assert.assertTrue(list1.size() == 0);
		
	


		

	}

	@Test
	public void testSearchApp() {
		SearchExpression searchExpression = new SearchExpressionImpl();

		List<Asset> list1 = applicationService.searchAsset(searchExpression);
		Assert.assertTrue(list1 != null);
		Assert.assertTrue(list1.size() > 0);

		int i = list1.size();

		searchExpression = new SearchExpressionImpl();
		Condition c = new AssetProviderIdCondition(1L, NumberComparer.EQUAL);
		searchExpression.addCondition(c);
		list1 = applicationService.searchAsset(searchExpression);
		Assert.assertTrue(list1 != null);
		Assert.assertTrue(list1.size() > 0);

		Assert.assertTrue(list1.size() < i);

		searchExpression = new SearchExpressionImpl();
		c = new AssetAssetCategoryIdCondition(1L, NumberComparer.EQUAL);
		searchExpression.addCondition(c);
		list1 = applicationService.searchAsset(searchExpression);
		Assert.assertTrue(list1 != null);
		Assert.assertTrue(list1.size() > 0);

		Assert.assertTrue(list1.size() < i);

	}

	@Test
	public void testGetAllCommentsSensorWords() throws Exception {
		List<CommentsSensorWord> commentsSensorWords = applicationService.getAllCommentsSensorWord(0, Integer.MAX_VALUE);
		Assert.assertTrue(commentsSensorWords.size() == 4);
	}

	@Test
	public void testGetAllCommentsSensorWordCount() throws Exception {
		int count = applicationService.getAllCommentsSensorWordCount();
		Assert.assertTrue(count == 4);
	}

	@Test
	public void testSaveOrUpdateCommentsSensorWord() throws Exception {
		CommentsSensorWord csw = new CommentsSensorWord();
		csw.setSensorWord("suck");
		applicationService.saveOrUpdateCommentsSensorWord(csw);
		int count = applicationService.getAllCommentsSensorWordCount();
		Assert.assertTrue(count == 5);

	}

	@Test
	public void testBatchDeleteCommentsSensorWord() throws Exception {
		List<Long> commentsSensorWordId = new ArrayList<Long>();
		commentsSensorWordId.add(1L);
		commentsSensorWordId.add(2L);
		commentsSensorWordId.add(3L);

		applicationService.batchDeleteCommentsSensorWord(commentsSensorWordId);
		int count = applicationService.getAllCommentsSensorWordCount();
		Assert.assertTrue(count == 1);

		ITable tableValue = databaseTester.getConnection().createQueryTable("result", "select * from commentssensorword");

		Assert.assertTrue("mouse".equals(tableValue.getValue(0, "SENSORWORD")));
	}

	@Test
	public void testCheckCommentsSensorWordByName() throws Exception {
		boolean flag = applicationService.checkCommentsSensorWordByName("mouse");
		Assert.assertTrue(flag);
	}

	@Test
	public void testSearchCategory() {
		SearchExpressionImpl searchExpression = new SearchExpressionImpl();
		searchExpression.addCondition(new CategoryNameCondition("test", StringComparer.LIKE, true, false));
		searchExpression.setFirst(0);
		searchExpression.setMax(Integer.MAX_VALUE);
		List<Category> list = applicationService.searchCategory(searchExpression);
	}

	@Test
	public void testSearchSimpleOrder() throws Exception {
		SearchExpression searchExpression = null;
		searchExpression = new SearchExpressionImpl();
		searchExpression.addOrder(AssetOrderBy.NAME, OrderEnum.DESC);
		searchExpression.setFirst(0);
		searchExpression.setMax(Integer.MAX_VALUE);
		List<Asset> list = applicationService.searchAsset(searchExpression);
		System.out.println("asset list" + list);
		long count = applicationService.searchAssetPageCount(searchExpression);
		for (Asset asset : list) {
			System.out.println(asset.getId());
		}
		System.out.println(count);
		if (list.get(0).getId() == 3 && list.get(1).getId() == 2 && list.get(2).getId() == 1)
			Assert.assertTrue(true);
		else
			Assert.assertTrue(false);
	}

	@Test
	public void testSearchComplexOrder() throws Exception {
		SearchExpression searchExpression = null;
		searchExpression = new SearchExpressionImpl();
		searchExpression.addCondition(new AssetNameCondition("APP", StringComparer.LIKE, true, false));
		searchExpression.addOrder(AssetOrderBy.PROVIDERPRIORITY, OrderEnum.DESC);
		searchExpression.setFirst(0);
		searchExpression.setMax(Integer.MAX_VALUE);
		List<Asset> list = applicationService.searchAsset(searchExpression);
		System.out.println("asset list" + list);
		long count = applicationService.searchAssetPageCount(searchExpression);
		for (Asset asset : list) {
			System.out.println(asset.getId());
		}
		System.out.println(count);
		if (list.get(0).getId() == 2 && list.get(1).getId() == 3 && list.get(2).getId() == 1)
			Assert.assertTrue(true);
		else
			Assert.assertTrue(false);
	}

	@Test
	public void testBatchUpdateAssetBinaryRecommend() throws Exception {
		List<Long> binaryVersionIds = new ArrayList<Long>();
		binaryVersionIds.add(1L);
		binaryVersionIds.add(2L);
		Date date = new Date();

		applicationService.batchUpdateAssetBinaryRecommend(binaryVersionIds, date, date, 1L);

		ITable tableValue = databaseTester.getConnection().createQueryTable("result", "select * from assetbinaryversion");

		Assert.assertTrue((Long) tableValue.getValue(1, "recommendOrder") == 1L);

		Date recommendStartDate = (Date) tableValue.getValue(1, "RECOMMENDSTARTDATE");
		Date recommendDueDate = (Date) tableValue.getValue(1, "RECOMMENDDUEDATE");
		Date recommendUpdateDate = (Date) tableValue.getValue(1, "RECOMMENDUPDATEDATE");

		Assert.assertTrue(new SimpleDateFormat("yyyyMMdd").format(recommendStartDate).equals(new SimpleDateFormat("yyyyMMdd").format(date)));
		Assert.assertTrue(new SimpleDateFormat("yyyyMMdd").format(recommendDueDate).equals(new SimpleDateFormat("yyyyMMdd").format(date)));
		Assert.assertTrue(new SimpleDateFormat("yyyyMMdd").format(recommendUpdateDate).equals(new SimpleDateFormat("yyyyMMdd").format(date)));

	}

	@Test
	public void testBatchUpdateAssetBinaryNewArrivalDueDate() throws Exception {
		List<Long> binaryVersionIds = new ArrayList<Long>();
		binaryVersionIds.add(1L);
		binaryVersionIds.add(2L);
		Date date = new Date();

		applicationService.batchUpdateAssetBinaryNewArrivalDueDate(binaryVersionIds, date);

		ITable tableValue = databaseTester.getConnection().createQueryTable("result", "select * from assetbinaryversion");

		Date newArrivalDueDate = (Date) tableValue.getValue(1, "NEWARRIVALDUEDATE");

		Assert.assertTrue(new SimpleDateFormat("yyyyMMdd").format(newArrivalDueDate).equals(new SimpleDateFormat("yyyyMMdd").format(date)));
	}

	@Test
	public void testBatchUpdateAssetBinaryProviderCommissionRate() throws Exception {
		List<Long> binaryVersionIds = new ArrayList<Long>();
		binaryVersionIds.add(2L);
		binaryVersionIds.add(3L);

		applicationService.batchUpdateAssetBinaryProviderCommissionRate(binaryVersionIds, 0.1);

		ITable tableValue = databaseTester.getConnection().createQueryTable("result", "select * from assetprovider");

		Assert.assertTrue((Double) tableValue.getValue(0, "COMMISSIONRATE") == 0.1);
		Assert.assertTrue((Double) tableValue.getValue(1, "COMMISSIONRATE") == 0.1);
	}

	@Test
	public void testIgnoreCase() throws Exception {
		SearchExpressionImpl searchExpression = new SearchExpressionImpl();
		searchExpression.addCondition(new AssetNameCondition("app2", StringComparer.EQUAL, true, false));
		searchExpression.setFirst(0);
		searchExpression.setMax(Integer.MAX_VALUE);
		List<Asset> list = applicationService.searchAsset(searchExpression);
		Assert.assertTrue(list.size() == 1);
	}

	@Test
	public void testWildCard() throws Exception {
		SearchExpressionImpl searchExpression = new SearchExpressionImpl();
		searchExpression.addCondition(new AssetNameCondition("app*", StringComparer.LIKE, true, true));
		searchExpression.setFirst(0);
		searchExpression.setMax(Integer.MAX_VALUE);
		List<Asset> list = applicationService.searchAsset(searchExpression);
		Assert.assertTrue(list.size() == 3);
	}

	@Test
	public void testSearchAssetByCategoryName() throws Exception {

		databaseTester.getConnection().getConnection().createStatement().execute("delete from assetcategoryrelation");

		this.applicationService.associateCategory(1L, null, 1L);

		this.applicationService.associateCategory(1L, 2L, 2L);

		this.applicationService.associateCategory(1L, 1L, 3L);

		SearchExpressionImpl searchExpression = new SearchExpressionImpl();
		searchExpression.addCondition(new AssetAssetCategoryNameCondition("business", StringComparer.EQUAL, true, false));
		searchExpression.addCondition(new AssetAssetPriceAmountCondition(new BigDecimal(10.0), NumberComparer.EQUAL));
		searchExpression.setFirst(0);
		searchExpression.setMax(Integer.MAX_VALUE);
		List<Asset> list = applicationService.searchAsset(searchExpression);

		Assert.assertTrue(list.size() == 1);

		Asset asset = list.get(0);
		Assert.assertTrue(asset.getId() == 1L);
		searchExpression = new SearchExpressionImpl();
		searchExpression.addCondition(new AssetAssetCategoryNameCondition("game", StringComparer.EQUAL, true, false));
		searchExpression.addCondition(new AssetAssetPriceAmountCondition(new BigDecimal(9.9), NumberComparer.EQUAL));
		searchExpression.setFirst(0);
		searchExpression.setMax(Integer.MAX_VALUE);
		list = applicationService.searchAsset(searchExpression);

		// ITable tableValue = databaseTester
		// .getConnection()
		// .createQueryTable(
		// "result",
		// "select distinct this_.id as y0_ from Asset this_ inner join AssetCategoryRelation assetcateg1_ on this_.id=assetcateg1_.ASSETID inner join Category category2_  on assetcateg1_.CTGID=category2_.id inner join  AssetBinaryVersion binaryvers3_ on this_.id=binaryvers3_.ASSETID where( (( lower(category2_.name)='game' and assetcateg1_.VERSIONID is not null) and this_.currentVersionId=binaryvers3_.id))");

		Assert.assertTrue(list.size() == 0);

		searchExpression = new SearchExpressionImpl();
		searchExpression.addCondition(new AssetAssetCategoryNameCondition("sports", StringComparer.EQUAL, true, false));
		searchExpression.addCondition(new AssetAssetPriceAmountCondition(new BigDecimal(10.0), NumberComparer.EQUAL));
		searchExpression.setFirst(0);
		searchExpression.setMax(Integer.MAX_VALUE);
		list = applicationService.searchAsset(searchExpression);
		Assert.assertTrue(list.size() == 1);
		asset = list.get(0);
		Assert.assertTrue(asset.getId() == 1L);

		this.applicationService.associateCategory(1L, 1L, 2L);

		searchExpression = new SearchExpressionImpl();
		searchExpression.addCondition(new AssetAssetCategoryNameCondition("game", StringComparer.EQUAL, true, false));
		searchExpression.addCondition(new AssetAssetPriceAmountCondition(new BigDecimal(10.0), NumberComparer.EQUAL));
		searchExpression.setFirst(0);
		searchExpression.setMax(Integer.MAX_VALUE);
		list = applicationService.searchAsset(searchExpression);
		Assert.assertTrue(list.size() == 1);
		asset = list.get(0);
		Assert.assertTrue(asset.getId() == 1L);

	}

	@Test
	public void testSearchAssetByCategoryId() throws Exception {

		databaseTester.getConnection().getConnection().createStatement().execute("delete from assetcategoryrelation");

		this.applicationService.associateCategory(1L, null, 1L);

		this.applicationService.associateCategory(1L, 2L, 2L);

		this.applicationService.associateCategory(1L, 1L, 3L);

		SearchExpressionImpl searchExpression = new SearchExpressionImpl();
		searchExpression.addCondition(new AssetAssetCategoryIdCondition(1L, NumberComparer.EQUAL));
		searchExpression.addCondition(new AssetAssetPriceAmountCondition(new BigDecimal(10.0), NumberComparer.EQUAL));
		searchExpression.setFirst(0);
		searchExpression.setMax(Integer.MAX_VALUE);
		List<Asset> list = applicationService.searchAsset(searchExpression);

		Assert.assertTrue(list.size() == 1);

		Asset asset = list.get(0);
		Assert.assertTrue(asset.getId() == 1L);
		searchExpression = new SearchExpressionImpl();
		searchExpression.addCondition(new AssetAssetCategoryIdCondition(2L, NumberComparer.EQUAL));
		searchExpression.addCondition(new AssetAssetPriceAmountCondition(new BigDecimal(9.9), NumberComparer.EQUAL));
		searchExpression.setFirst(0);
		searchExpression.setMax(Integer.MAX_VALUE);
		list = applicationService.searchAsset(searchExpression);

		// ITable tableValue = databaseTester
		// .getConnection()
		// .createQueryTable(
		// "result",
		// "select distinct this_.id as y0_ from Asset this_ inner join AssetCategoryRelation assetcateg1_ on this_.id=assetcateg1_.ASSETID inner join Category category2_  on assetcateg1_.CTGID=category2_.id inner join  AssetBinaryVersion binaryvers3_ on this_.id=binaryvers3_.ASSETID where( (( lower(category2_.name)='game' and assetcateg1_.VERSIONID is not null) and this_.currentVersionId=binaryvers3_.id))");

		Assert.assertTrue(list.size() == 0);

		searchExpression = new SearchExpressionImpl();
		searchExpression.addCondition(new AssetAssetCategoryIdCondition(3L, NumberComparer.EQUAL));
		searchExpression.addCondition(new AssetAssetPriceAmountCondition(new BigDecimal(10.0), NumberComparer.EQUAL));
		searchExpression.setFirst(0);
		searchExpression.setMax(Integer.MAX_VALUE);
		list = applicationService.searchAsset(searchExpression);
		Assert.assertTrue(list.size() == 1);
		asset = list.get(0);
		Assert.assertTrue(asset.getId() == 1L);

		this.applicationService.associateCategory(3L, 3L, 3L);

		searchExpression = new SearchExpressionImpl();
		searchExpression.addCondition(new AssetAssetCategoryIdCondition(3L, NumberComparer.EQUAL));
		searchExpression.addCondition(new AssetAssetPriceAmountCondition(new BigDecimal(0.0), NumberComparer.EQUAL));
		searchExpression.setFirst(0);
		searchExpression.setMax(Integer.MAX_VALUE);
		list = applicationService.searchAsset(searchExpression);
		Assert.assertTrue(list.size() == 1);
		asset = list.get(0);
		Assert.assertTrue(asset.getId() == 3L);

	}

	@Test
	public void testSearchAssetByCategoryDescription() throws Exception {

		databaseTester.getConnection().getConnection().createStatement().execute("delete from assetcategoryrelation");

		this.applicationService.associateCategory(1L, null, 1L);

		this.applicationService.associateCategory(1L, 2L, 2L);

		this.applicationService.associateCategory(1L, 1L, 3L);

		SearchExpressionImpl searchExpression = new SearchExpressionImpl();
		searchExpression.addCondition(new AssetAssetCategoryDescriptionCondition("business", StringComparer.EQUAL, true, false));
		searchExpression.addCondition(new AssetAssetPriceAmountCondition(new BigDecimal(10.0), NumberComparer.EQUAL));
		searchExpression.setFirst(0);
		searchExpression.setMax(Integer.MAX_VALUE);
		List<Asset> list = applicationService.searchAsset(searchExpression);

		Assert.assertTrue(list.size() == 1);

		Asset asset = list.get(0);
		Assert.assertTrue(asset.getId() == 1L);
		searchExpression = new SearchExpressionImpl();
		searchExpression.addCondition(new AssetAssetCategoryDescriptionCondition("game", StringComparer.EQUAL, true, false));
		searchExpression.addCondition(new AssetAssetPriceAmountCondition(new BigDecimal(9.9), NumberComparer.EQUAL));
		searchExpression.setFirst(0);
		searchExpression.setMax(Integer.MAX_VALUE);
		list = applicationService.searchAsset(searchExpression);

		// ITable tableValue = databaseTester
		// .getConnection()
		// .createQueryTable(
		// "result",
		// "select distinct this_.id as y0_ from Asset this_ inner join AssetCategoryRelation assetcateg1_ on this_.id=assetcateg1_.ASSETID inner join Category category2_  on assetcateg1_.CTGID=category2_.id inner join  AssetBinaryVersion binaryvers3_ on this_.id=binaryvers3_.ASSETID where( (( lower(category2_.name)='game' and assetcateg1_.VERSIONID is not null) and this_.currentVersionId=binaryvers3_.id))");

		Assert.assertTrue(list.size() == 0);

		searchExpression = new SearchExpressionImpl();
		searchExpression.addCondition(new AssetAssetCategoryDescriptionCondition("sports", StringComparer.EQUAL, true, false));
		searchExpression.addCondition(new AssetAssetPriceAmountCondition(new BigDecimal(10.0), NumberComparer.EQUAL));
		searchExpression.setFirst(0);
		searchExpression.setMax(Integer.MAX_VALUE);
		list = applicationService.searchAsset(searchExpression);
		Assert.assertTrue(list.size() == 1);
		asset = list.get(0);
		Assert.assertTrue(asset.getId() == 1L);

		this.applicationService.associateCategory(1L, 2L, 1L);

		searchExpression = new SearchExpressionImpl();
		searchExpression.addCondition(new AssetAssetCategoryDescriptionCondition("business", StringComparer.EQUAL, true, false));
		searchExpression.addCondition(new AssetAssetPriceAmountCondition(new BigDecimal(10.0), NumberComparer.EQUAL));
		searchExpression.setFirst(0);
		searchExpression.setMax(Integer.MAX_VALUE);
		list = applicationService.searchAsset(searchExpression);
		Assert.assertTrue(list.size() == 1);
		asset = list.get(0);
		Assert.assertTrue(asset.getId() == 1L);
	}

	@Test
	public void testAssetBinaryVersionCategoryNameCondition() throws Exception {
		SearchExpression searchExpression = new SearchExpressionImpl();
		searchExpression.addCondition(new AssetBinaryVersionCategoryNameCondition("business", StringComparer.EQUAL, true, false));
		List<AssetBinaryVersion> list = applicationService.searchAssetBinary(searchExpression);
	}

	@Test
	public void testSaveOrUpdateAssetLifecycleActionHistory() throws Exception {
		Date date = Calendar.getInstance().getTime();
		String str = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
		AssetLifecycleActionHistory assetLifecycleActionHistory = new AssetLifecycleActionHistory();
		assetLifecycleActionHistory.setEvent("test_event");
		assetLifecycleActionHistory.setAssetId(1L);
		assetLifecycleActionHistory.setCreateDate(date);

		applicationService.saveOrUpdateAssetLifecycleActionHistory(assetLifecycleActionHistory);

		ITable tableValue1 = databaseTester.getConnection().createQueryTable("result",
				"select * from assetlifecycleactionhistory where event = 'test_event' and assetId = 1 ");

		Assert.assertTrue(tableValue1.getRowCount() == 1);

		assetLifecycleActionHistory.setEvent("test_event2");
		assetLifecycleActionHistory.setAssetId(2L);

		applicationService.saveOrUpdateAssetLifecycleActionHistory(assetLifecycleActionHistory);

		tableValue1 = databaseTester.getConnection().createQueryTable("result",
				"select * from assetlifecycleactionhistory where event = 'test_event2' and assetId = 2 ");
		Assert.assertTrue(tableValue1.getRowCount() == 1);

	}

	@Test
	public void testSearchAssetLifecycleActionHistory() {
		SearchExpression searchExpression = new SearchExpressionImpl();
		Date date = Calendar.getInstance().getTime();
		searchExpression.addCondition(new AssetLifecycleActionHistoryCompleteDateCondition(date, DateComparer.LESS_THAN));
		searchExpression.addCondition(new AssetLifecycleActionHistorySourceCondition("shaoping1", StringComparer.EQUAL, false, false));

		List<AssetLifecycleActionHistory> assetLifecycleActionHistorys = applicationService.searchAssetLifecycleActionHistory(searchExpression);
		Assert.assertTrue(assetLifecycleActionHistorys.size() == 1);
		Assert.assertEquals(assetLifecycleActionHistorys.get(0).getPrestatus(), "pre_status1");
	}

	@Test
	public void testSearchAssetLifecycleActionHistoryCount() {
		SearchExpression searchExpression = new SearchExpressionImpl();
		Date date = Calendar.getInstance().getTime();
		searchExpression.addCondition(new AssetLifecycleActionHistoryCompleteDateCondition(date, DateComparer.LESS_THAN));
		int count = applicationService.searchAssetLifecycleActionHistoryCount(searchExpression);

		Assert.assertTrue(count == 2);
	}

	@Test
	public void testSearchSystemConfig() {
		SearchExpression searchExpression = new SearchExpressionImpl();
		searchExpression.addCondition(new SystemConfigConfigKeyCondition("default.new.arrival.due.days", StringComparer.EQUAL, false, false));

		List<SystemConfig> systemConfigs = applicationService.searchSystemConfig(searchExpression);

		Assert.assertTrue(systemConfigs.size() == 1);
	}

	@Test
	public void testSearchSystemConfigCount() {
		SearchExpression searchExpression = new SearchExpressionImpl();
		searchExpression.addCondition(new SystemConfigConfigKeyCondition("default.new.arrival.due.days", StringComparer.EQUAL, false, false));

		int count = applicationService.searchSystemConfigCount(searchExpression);

		Assert.assertTrue(count == 1);
	}

	@Test
	public void testGetSystemConfigByKey() {
		SystemConfig systemConfig = applicationService.getSystemConfigByKey("default.new.arrival.due.days");
		Assert.assertEquals("0", systemConfig.getValue());
	}

	@Test
	public void testSaveOrUpdateSystemConfig() throws DataSetException, SQLException, Exception {
		SystemConfig systemConfig = new SystemConfig();
		systemConfig.setConfigKey("key3");
		systemConfig.setValue("value3");

		applicationService.saveOrUpdateSystemConfig(systemConfig);
		ITable tableValue = databaseTester.getConnection().createQueryTable("result", "select * from SystemConfig where id=4");

		Assert.assertTrue(tableValue.getRowCount() == 1);
		Assert.assertEquals("value3", tableValue.getValue(0, "VALUE"));

		systemConfig.setValue("value4");
		applicationService.saveOrUpdateSystemConfig(systemConfig);

		applicationService.saveOrUpdateSystemConfig(systemConfig);
		ITable tableValue1 = databaseTester.getConnection().createQueryTable("result", "select * from SystemConfig where id=4");

		Assert.assertTrue(tableValue1.getRowCount() == 1);
		Assert.assertEquals("value4", tableValue1.getValue(0, "VALUE"));
	}

	@Test
	public void testGetSystemConfigById() {
		SystemConfig systemConfig = applicationService.getSystemConfigById(1L);
		Assert.assertEquals("0", systemConfig.getValue());
	}

	@Test
	public void testNotWildCardLike() {
		SearchExpression searchExpression = new SearchExpressionImpl();
		searchExpression.addCondition(new AssetNameCondition("APP", StringComparer.LIKE, true, false));
		long result = this.applicationService.searchAssetPageCount(searchExpression);
		Assert.assertEquals(result, 3);
	}

	@Test
	public void testGetCategoryByParentCategoryId() throws Exception {
		applicationService.createCategoryRelationShip(1L, Arrays.asList(new Long[] { 2L }));
		List<Category> categorys = applicationService.getCategoryByParentCategoryId(1L);
		Assert.assertEquals(1, categorys.size());
		applicationService.dropCategoryRelationShip(Arrays.asList(new Long[] { 2L }));
	}

	@Test
	public void testSearchBinaryVersion() throws Exception{
		
		String pubFlg = "publishflg";
		Long binaryVersionId = 4L;
		
		databaseTester.getConnection().getConnection().createStatement().execute("delete from entityattribute");

		databaseTester.getConnection().getConnection().createStatement().execute("delete from attributevaluechar");

		databaseTester.getConnection().getConnection().createStatement().execute("delete from attributevaluenumber");

		databaseTester.getConnection().getConnection().createStatement().execute("delete from attributevaluedate");
		
		this.applicationService.addAttribute(4L, EntityType.BINARYVERSION, pubFlg, "published");

		
		
		SearchExpression searchExpression = new SearchExpressionImpl();
		searchExpression.addCondition(new AssetBinaryVersionIdCondition(binaryVersionId, NumberComparer.EQUAL));
		searchExpression.addCondition(new EavStringCondition(EntityType.BINARYVERSION, pubFlg, "published", StringComparer.EQUAL, true, false));
		List<AssetBinaryVersion> o = applicationService.searchAssetBinary(searchExpression);
		Assert.assertEquals(1, o.size());
		Assert.assertTrue(4L==o.get(0).getId());
	}

	@Test
	public void testAddAttribute() {
		Date dateValue = new Date();
		this.applicationService.addAttribute(1L, EntityType.ASSET, "mytest", 1L);
		this.applicationService.addAttribute(1L, EntityType.ASSET, "mytest1", 1);
		this.applicationService.addAttribute(1L, EntityType.ASSET, "mytest2", new BigDecimal(1));
		this.applicationService.addAttribute(1L, EntityType.ASSET, "mytest3", new Double(1));
		this.applicationService.addAttribute(1L, EntityType.ASSET, "mytest4", "value");
		this.applicationService.addAttribute(1L, EntityType.ASSET, "mytest5", dateValue);

		List<AttributeValue> attributeValues = this.applicationService.getAttributeValue(1L, EntityType.ASSET, "mytest");
		Assert.assertTrue(attributeValues != null);
		Assert.assertTrue(attributeValues.size() == 1);
		Assert.assertTrue(((Float) attributeValues.get(0).getValue()).longValue() == 1);
		attributeValues = this.applicationService.getAttributeValue(1L, EntityType.ASSET, "mytest1");
		Assert.assertTrue(attributeValues != null);
		Assert.assertTrue(attributeValues.size() == 1);
		Assert.assertTrue(((Float) attributeValues.get(0).getValue()).intValue() == 1);
		attributeValues = this.applicationService.getAttributeValue(1L, EntityType.ASSET, "mytest3");
		Assert.assertTrue(attributeValues != null);
		Assert.assertTrue(attributeValues.size() == 1);
		Assert.assertTrue(((Float) attributeValues.get(0).getValue()).doubleValue() == 1);
		attributeValues = this.applicationService.getAttributeValue(1L, EntityType.ASSET, "mytest4");
		Assert.assertTrue(attributeValues != null);
		Assert.assertTrue(attributeValues.size() == 1);
		Assert.assertTrue(((String) attributeValues.get(0).getValue()).equals("value"));
		attributeValues = this.applicationService.getAttributeValue(1L, EntityType.ASSET, "mytest5");
		Assert.assertTrue(attributeValues != null);
		Assert.assertTrue(attributeValues.size() == 1);

	}

	@Test
	public void testRemoveAttribute() {
		this.applicationService.addAttribute(1L, EntityType.ASSET, "mytest", 1L);
		this.applicationService.removeAttributes(1L, EntityType.ASSET, "mytest");
		this.applicationService.removeAttributes(1L, EntityType.ASSET, "mytest");
		this.applicationService.removeAttributes(1L, EntityType.ASSET, "mytest");
	}
	
	@Test
	public void testSaveComments2() throws Exception{
		databaseTester.getConnection().getConnection().createStatement().execute("delete from commentssensorword");
		Comments comment=new Comments();
		comment.setAsset(new Asset(1L));
		comment.setContent("fuck");
		this.applicationService.saveComments(comment);
		ITable tableValue = databaseTester.getConnection().createQueryTable("result", "select * from comments where ASSETID=1 and CONTENT='fuck'");

		Assert.assertEquals(1, tableValue.getRowCount());
	}
	
	@Test
	@Transactional
	public void testTransactional() throws Exception{
		Asset asset=this.applicationService.getAsset(1L);
		Assert.assertTrue(asset.getBinaryVersions().size()>0);
	}
	
	@Test
	public void testModelInitialization1() throws Exception{
		ParentAssetVersionSummary parentAssetVersionSummary=new ParentAssetVersionSummary();
		
		Assert.assertTrue(parentAssetVersionSummary.getNewArrivalFlag()==0);
		Assert.assertTrue(parentAssetVersionSummary.getRecommendFlag()==0);
		Assert.assertTrue(parentAssetVersionSummary.getSaleFlag()==0);
		Assert.assertTrue(parentAssetVersionSummary.getDownloadTime()==0);
		Assert.assertTrue(parentAssetVersionSummary.getPriceEqualFlg()==1);
		Assert.assertTrue(parentAssetVersionSummary.getSampleFlag()==0);
		Assert.assertTrue(parentAssetVersionSummary.getLowestPrice()==Float.MAX_VALUE);
		Assert.assertTrue(parentAssetVersionSummary.getRecommendOrder()==Integer.MAX_VALUE);
		Date startDate=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse("1000-01-01 00:00:00");
		Date endDate=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse("9999-12-31 23:59:59");
		
		Assert.assertEquals(startDate, parentAssetVersionSummary.getPublishDate());
		Assert.assertEquals(startDate, parentAssetVersionSummary.getSaleStart());
		Assert.assertEquals(endDate, parentAssetVersionSummary.getSaleEnd());
		
	}
	
	@Test
	public void testModelInitialization2() throws Exception{
		Asset asset=new Asset();
		
		Date startDate=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse("1000-01-01 00:00:00");
		Date endDate=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse("9999-12-31 23:59:59");
		
		Assert.assertEquals(endDate, asset.getNewArrivalDueDate());
		Assert.assertEquals(startDate, asset.getRecommendStartDate());
		Assert.assertEquals(endDate, asset.getRecommendDueDate());
		
	}
	
	@Test
	public void testModelInitialization3() throws Exception{
		AssetBinaryVersion binary=new AssetBinaryVersion();
		
		Date startDate=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse("1000-01-01 00:00:00");
		Date endDate=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse("9999-12-31 23:59:59");
		
		Assert.assertEquals(endDate, binary.getNewArrivalDueDate());
		Assert.assertEquals(startDate, binary.getRecommendStartDate());
		Assert.assertEquals(endDate, binary.getRecommendDueDate());
		Assert.assertTrue(binary.getRecommendOrder()==Integer.MAX_VALUE);
		
	}
	
	

}

// $Id$