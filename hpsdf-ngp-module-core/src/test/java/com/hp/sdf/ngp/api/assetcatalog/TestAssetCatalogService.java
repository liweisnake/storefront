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
package com.hp.sdf.ngp.api.assetcatalog;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Currency;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;

import javax.annotation.Resource;

import junit.framework.Assert;

import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.ITable;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.hp.sdf.ngp.DBEnablerTestBase;
import com.hp.sdf.ngp.api.comon.EntityType;
import com.hp.sdf.ngp.api.exception.AssetCatalogServiceException;
import com.hp.sdf.ngp.api.impl.model.AssetCategoryImpl;
import com.hp.sdf.ngp.api.impl.model.AssetLifecycleActionHistoryImpl;
import com.hp.sdf.ngp.api.impl.model.AssetProviderImpl;
import com.hp.sdf.ngp.api.impl.model.BinaryVersionImpl;
import com.hp.sdf.ngp.api.impl.model.CatalogAssetImpl;
import com.hp.sdf.ngp.api.impl.model.RestrictionTypeImpl;
import com.hp.sdf.ngp.api.impl.model.ScreenshotImpl;
import com.hp.sdf.ngp.api.impl.model.UserImpl;
import com.hp.sdf.ngp.api.impl.search.SearchEngineImpl;
import com.hp.sdf.ngp.api.model.Asset;
import com.hp.sdf.ngp.api.model.AssetCategory;
import com.hp.sdf.ngp.api.model.AssetComment;
import com.hp.sdf.ngp.api.model.AssetLifecycleActionHistory;
import com.hp.sdf.ngp.api.model.AssetProvider;
import com.hp.sdf.ngp.api.model.AttributeList;
import com.hp.sdf.ngp.api.model.BinaryVersion;
import com.hp.sdf.ngp.api.model.CatalogAsset;
import com.hp.sdf.ngp.api.model.RestrictionType;
import com.hp.sdf.ngp.api.model.Screenshot;
import com.hp.sdf.ngp.api.model.StoreClientAsset;
import com.hp.sdf.ngp.api.model.User;
import com.hp.sdf.ngp.api.search.Condition;
import com.hp.sdf.ngp.api.search.OrderEnum;
import com.hp.sdf.ngp.api.search.SearchEngine;
import com.hp.sdf.ngp.api.search.SearchExpression;
import com.hp.sdf.ngp.api.search.Condition.DateComparer;
import com.hp.sdf.ngp.api.search.Condition.NumberComparer;
import com.hp.sdf.ngp.api.search.Condition.StringComparer;
import com.hp.sdf.ngp.api.search.condition.asset.AssetExternalIdConditionDescriptor;
import com.hp.sdf.ngp.api.search.condition.asset.AssetSourceConditionDescriptor;
import com.hp.sdf.ngp.api.search.condition.assetbinaryversion.AssetBinaryVersionExternalIdConditionDescriptor;
import com.hp.sdf.ngp.api.search.condition.assetbinaryversion.AssetBinaryVersionSourceConditionDescriptor;
import com.hp.sdf.ngp.api.search.condition.assetbinaryversion.AssetBinaryVersionStatusNameConditionDescriptor;
import com.hp.sdf.ngp.api.search.condition.eav.EavStringConditionDescriptor;
import com.hp.sdf.ngp.api.search.orderby.AssetOrderBy;
import com.hp.sdf.ngp.api.search.orderby.BinaryVersionOrderBy;
import com.hp.sdf.ngp.api.search.orderby.CategoryOrderBy;
import com.hp.sdf.ngp.api.search.orderby.CommentsOrderBy;
import com.hp.sdf.ngp.eav.model.AttributeValue;
import com.hp.sdf.ngp.model.AssetBinaryVersion;
import com.hp.sdf.ngp.model.Category;
import com.hp.sdf.ngp.model.Provider;
import com.hp.sdf.ngp.model.RestrictedType;
import com.hp.sdf.ngp.model.ScreenShots;
import com.hp.sdf.ngp.model.Status;
import com.hp.sdf.ngp.model.UserProfile;
import com.hp.sdf.ngp.search.condition.asset.AssetExternalIdCondition;
import com.hp.sdf.ngp.search.condition.asset.AssetNameCondition;
import com.hp.sdf.ngp.search.condition.asset.AssetParentIdCondition;
import com.hp.sdf.ngp.search.condition.asset.AssetProviderIdCondition;
import com.hp.sdf.ngp.search.condition.asset.AssetStatusIdCondition;
import com.hp.sdf.ngp.search.condition.assetbinaryversion.AssetBinaryVersionAssetProviderIdCondition;
import com.hp.sdf.ngp.search.condition.assetbinaryversion.AssetBinaryVersionCategoryIdCondition;
import com.hp.sdf.ngp.search.condition.assetbinaryversion.AssetBinaryVersionExternalIdCondition;
import com.hp.sdf.ngp.search.condition.assetbinaryversion.AssetBinaryVersionFileNameCondition;
import com.hp.sdf.ngp.search.condition.assetbinaryversion.AssetBinaryVersionIdCondition;
import com.hp.sdf.ngp.search.condition.assetbinaryversion.AssetBinaryVersionNewArrivalDueDateCondition;
import com.hp.sdf.ngp.search.condition.assetbinaryversion.AssetBinaryVersionSourceCondition;
import com.hp.sdf.ngp.search.condition.assetbinaryversion.AssetBinaryVersionStatusIdCondition;
import com.hp.sdf.ngp.search.condition.assetbinaryversion.AssetBinaryVersionStatusNameCondition;
import com.hp.sdf.ngp.search.condition.assetlifecycleactionhistory.AssetLifecycleActionHistoryPostStatusCondition;
import com.hp.sdf.ngp.search.condition.assetlifecycleactionhistory.AssetLifecycleActionHistoryPreStatusCondition;
import com.hp.sdf.ngp.search.condition.assetlifecycleactionhistory.AssetLifecycleActionHistorySourceCondition;
import com.hp.sdf.ngp.search.condition.assetprovider.AssetProviderExternalIdCondition;
import com.hp.sdf.ngp.search.condition.assetprovider.AssetProviderSourceCondition;
import com.hp.sdf.ngp.search.condition.category.CategoryDescriptionCondition;
import com.hp.sdf.ngp.search.condition.category.CategoryIdCondition;
import com.hp.sdf.ngp.search.condition.category.CategoryNameCondition;
import com.hp.sdf.ngp.search.condition.comments.CommentsContentCondition;
import com.hp.sdf.ngp.search.condition.comments.CommentsUpdateDateCondition;
import com.hp.sdf.ngp.search.condition.eav.EavDateCondition;
import com.hp.sdf.ngp.search.condition.eav.EavStringCondition;
import com.hp.sdf.ngp.search.engine.SearchExpressionImpl;
import com.hp.sdf.ngp.service.ApplicationService;
import com.hp.sdf.ngp.service.UserService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:META-INF/applicationContext-test.xml" })
public class TestAssetCatalogService extends DBEnablerTestBase {

	@Resource
	private AssetCatalogService assetCatalogService;

	@Resource
	private ApplicationService applicationService;

	@Resource
	private SearchEngine searchEngine;

	@Resource
	private UserService userService;

	@Override
	public String dataSetFileName() {
		return "/data_init.xml";
	}

	// -----start

	@Test
	@Transactional
	public void testSearchBinaryVersionsBySource() throws Exception {
		SearchExpression searchExpression = new SearchExpressionImpl();
		Condition c1 = new AssetBinaryVersionSourceCondition("shaoping", StringComparer.EQUAL, false, false);

		searchExpression.addCondition(c1);
		searchExpression.addOrder(BinaryVersionOrderBy.ID, OrderEnum.ASC);

		List<BinaryVersion> binaryVersions = assetCatalogService.searchBinaryVersions(searchExpression);

		assertTrue(binaryVersions.size() == 2);
		assertTrue(binaryVersions.get(0).getId() == 1);
		assertTrue(binaryVersions.get(1).getId() == 2);

		StoreClientAsset storeClientAsset = binaryVersions.get(0).getStoreClientAsset();
		assertEquals(storeClientAsset.getStatus(), "uploaded");
		assertEquals(storeClientAsset.getProviderName(), "liuchao");
	}

	@Test
	public void testSearchBinaryVersionsByDate() throws Exception {
		BinaryVersion binaryVersion = assetCatalogService.constructBinaryVersion();
		binaryVersion.setAssetId(1L);
		binaryVersion.setStatus("uploaded");

		assetCatalogService.addBinaryVersion(binaryVersion);

		BinaryVersion binaryVersion1 = assetCatalogService.constructBinaryVersion();
		binaryVersion1.setAssetId(1L);
		binaryVersion1.setStatus("uploaded");
		assetCatalogService.addBinaryVersion(binaryVersion1);

		assetCatalogService.addAttribute(EntityType.BINARYVERSION, binaryVersion.getId(), "Category", "mytest");
		assetCatalogService.addAttribute(EntityType.BINARYVERSION, binaryVersion1.getId(), "Category", "mytest");

		SearchExpression searchExpression = new SearchExpressionImpl();
		Condition c2 = new EavStringCondition(EntityType.BINARYVERSION, "Category", "mytest", StringComparer.EQUAL, false, false);
		;

		searchExpression.addCondition(c2);
		searchExpression.addOrder(BinaryVersionOrderBy.ID, OrderEnum.ASC);

		List<BinaryVersion> binaryVersions = assetCatalogService.searchBinaryVersions(searchExpression);

		assertTrue(binaryVersions.size() == 2);
		assertTrue(binaryVersions.get(0).getId().longValue() == binaryVersion.getId().longValue());
		assertTrue(binaryVersions.get(1).getId().longValue() == binaryVersion1.getId().longValue());
	}

	@Test
	public void testGetStoreClientAssetOfBinaryVersion() throws Exception {
		SearchExpression searchExpression = new SearchExpressionImpl();
		Condition c1 = new AssetBinaryVersionIdCondition(1L, NumberComparer.EQUAL);

		searchExpression.addCondition(c1);
		searchExpression.addOrder(BinaryVersionOrderBy.ID, OrderEnum.ASC);

		List<BinaryVersion> binaryVersions = assetCatalogService.searchBinaryVersions(searchExpression);

		BinaryVersion binaryVersion = binaryVersions.get(0);
		assertNotNull(binaryVersion.getStoreClientAsset());

		AssetBinaryVersion assetBinaryVersion = ((BinaryVersionImpl) binaryVersions.get(0)).getAssetBinaryVersion();
		assertNotNull(assetBinaryVersion.getAsset().getId());
	}

	@Test
	public void testSearchBinaryVersionsByAnd() throws Exception {
		this.clearEAVAttribute();
		this.applicationService.addAttribute(2L, EntityType.BINARYVERSION, "Category", "Business");

		SearchExpression searchExpression = new SearchExpressionImpl();
		Condition c1 = new AssetBinaryVersionSourceCondition("shaoping", StringComparer.EQUAL, false, false);

		Condition c2 = new EavStringCondition(EntityType.BINARYVERSION, "Category", "Business", StringComparer.EQUAL, false, false);
		;

		searchExpression.addCondition(c1.and(c2));
		searchExpression.addOrder(BinaryVersionOrderBy.ID, OrderEnum.ASC);

		List<BinaryVersion> binaryVersions = assetCatalogService.searchBinaryVersions(searchExpression);

		assertTrue(binaryVersions.size() == 1);
		assertTrue(binaryVersions.get(0).getId() == 2);
	}

	@Test
	public void testSearchBinaryVersionsOrderBy() throws Exception {
		SearchExpression searchExpression = new SearchExpressionImpl();
		Condition c1 = new AssetBinaryVersionSourceCondition("shaoping", StringComparer.EQUAL, false, false);

		searchExpression.addCondition(c1);
		searchExpression.addOrder(BinaryVersionOrderBy.ID, OrderEnum.ASC);
		searchExpression.addOrder(BinaryVersionOrderBy.PROVIDERID, OrderEnum.ASC);
		searchExpression.addOrder(BinaryVersionOrderBy.ASSETID, OrderEnum.ASC);
		searchExpression.addOrder(BinaryVersionOrderBy.RECOMMENDORDER, OrderEnum.ASC);

		assetCatalogService.searchBinaryVersions(searchExpression);

	}

	@Test
	public void testSearchBinaryVersionsByOr() throws Exception {

		BinaryVersion binaryVersion = assetCatalogService.constructBinaryVersion();
		binaryVersion.setAssetId(1L);
		binaryVersion.setStatus("uploaded");

		assetCatalogService.addBinaryVersion(binaryVersion);

		BinaryVersion binaryVersion1 = assetCatalogService.constructBinaryVersion();
		binaryVersion1.setAssetId(1L);
		binaryVersion1.setStatus("uploaded");
		assetCatalogService.addBinaryVersion(binaryVersion1);

		assetCatalogService.addAttribute(EntityType.BINARYVERSION, binaryVersion.getId(), "Category", "mytest");
		assetCatalogService.addAttribute(EntityType.BINARYVERSION, binaryVersion1.getId(), "Category", "mytest");

		com.hp.sdf.ngp.model.Asset asset = new com.hp.sdf.ngp.model.Asset();
		asset.setSource("mysource");
		asset.setStatus(new Status(1L));
		this.applicationService.saveAsset(asset);

		BinaryVersion binaryVersion2 = assetCatalogService.constructBinaryVersion();
		binaryVersion2.setAssetId(asset.getId());
		binaryVersion2.setStatus("uploaded");

		assetCatalogService.addBinaryVersion(binaryVersion2);

		SearchExpression searchExpression = new SearchExpressionImpl();
		Condition c1 = new AssetBinaryVersionSourceCondition("mysource", StringComparer.EQUAL, false, false);
		Condition c2 = new EavStringCondition(EntityType.BINARYVERSION, "Category", "mytest", StringComparer.EQUAL, false, false);
		;

		searchExpression.addCondition(c1.or(c2));
		searchExpression.addOrder(BinaryVersionOrderBy.ID, OrderEnum.DESC);

		List<BinaryVersion> binaryVersions = assetCatalogService.searchBinaryVersions(searchExpression);

		assertTrue(binaryVersions.size() == 3);
		assertTrue(binaryVersions.get(2).getId().longValue() == binaryVersion.getId().longValue());
		assertTrue(binaryVersions.get(1).getId().longValue() == binaryVersion1.getId().longValue());
		assertTrue(binaryVersions.get(0).getId().longValue() == binaryVersion2.getId().longValue());
	}

	// ----end

	@Test
	public void testAddAsset() throws DataSetException, SQLException, Exception {
		// CatalogAsset catalogAsset = new CatalogAssetImpl();
		//
		// catalogAsset.setName("APP100");
		// catalogAsset.setStatus("uploaded");
		//
		// assetCatalogService.addAsset(catalogAsset);
		//
		// Long id = ((CatalogAssetImpl) catalogAsset).getAsset().getId();
		//
		// ITable tableValue =
		// databaseTester.getConnection().createQueryTable("result",
		// "select a.id from Asset a, Status b where a.statusid = b.id and a.name = 'APP100' and b.status = 'uploaded' and a.id = "
		// + id);
		// assertTrue(tableValue.getRowCount() == 1);

		CatalogAsset asset = assetCatalogService.constructAssetObject();

		asset.setSource("test_source");
		asset.setName("");
		asset.setStatus("uploaded");

		long assetId = assetCatalogService.addAsset(asset);
		// assertTrue(asset.getCurrentVersionId() == 0);

		BinaryVersion binaryVersion = assetCatalogService.constructBinaryVersion();
		binaryVersion.setAssetId(assetId);
		binaryVersion.setAssetParentId(assetId);
		binaryVersion.setName("Test Data");
		binaryVersion.setVersion("1.0");

		long versionId = assetCatalogService.addBinaryVersion(binaryVersion);
		System.out.println(versionId);
	}

	@Test
	public void testGetAsset() throws DataSetException, SQLException, Exception {

		CatalogAsset catalogAsset = assetCatalogService.getAsset(1L);

		assertEquals(catalogAsset.getName(), "APP1");
		assertEquals(catalogAsset.getProvider().getName(), "liuchao");
	}

	@Test
	public void testUpdateAsset() throws DataSetException, SQLException, Exception {
		CatalogAsset catalogAsset = assetCatalogService.getAsset(1L);
		InputStream buf = new ByteArrayInputStream("jiangshaoping".getBytes());

		catalogAsset.setName("APP101");
		catalogAsset.setStatus("testing");
		catalogAsset.setBigThumbnail("pdf", buf);

		assetCatalogService.updateAsset(catalogAsset);

		Long id = ((CatalogAssetImpl) catalogAsset).getAsset().getId();

		ITable tableValue = databaseTester.getConnection().createQueryTable("result",
				"select a.* from Asset a, Status b where a.statusid = b.id and a.name = 'APP101' and b.status = 'testing' and a.id = " + id);
		assertTrue(tableValue.getRowCount() == 1);
		assertNotNull(catalogAsset.getThumbnailBigUrl());
	}

	@Test
	public void testGetAssetSearchResultCount() throws DataSetException, SQLException, Exception {
		SearchExpression searchExpression = new SearchExpressionImpl();
		searchExpression.addCondition(new AssetNameCondition("APP", StringComparer.LIKE, true, false));
		searchExpression.addCondition(new AssetStatusIdCondition(2L, NumberComparer.LESS_THAN));
		searchExpression.addOrder(AssetOrderBy.CREATEDATE, OrderEnum.ASC);

		Long count = assetCatalogService.getAssetSearchResultCount(searchExpression);

		assertTrue(2 == count);
	}

	@Test
	public void testSearchAsset() throws DataSetException, SQLException, Exception {
		SearchExpression searchExpression = new SearchExpressionImpl();
		searchExpression.addCondition(new AssetParentIdCondition(null, NumberComparer.NULL));
		searchExpression.addCondition(new AssetProviderIdCondition(1L, NumberComparer.GREAT_EQUAL));
		searchExpression.addCondition(new AssetNameCondition("APP3", StringComparer.NOT_EQUAL, false, false));
		searchExpression.addOrder(AssetOrderBy.PROVIDERPRIORITY, OrderEnum.ASC);

		List<CatalogAsset> catalogAssets = assetCatalogService.searchAsset(searchExpression);

		assertTrue(2 == catalogAssets.size());
		assertEquals(catalogAssets.get(0).getProvider().getName(), "liuchao");
	}

	@Test
	public void testAddAssetIntoCategories() throws DataSetException, SQLException, Exception {
		Set<Long> categoryIds = new HashSet<Long>();
		categoryIds.add(2L);
		categoryIds.add(3L);

		assetCatalogService.addAssetIntoCategories(1L, 1L, categoryIds);

		ITable tableValue = databaseTester.getConnection().createQueryTable("result",
				"select * from AssetCategoryRelation where assetId = 1 and versionId = 1 and (ctgId = 2 or ctgId = 3) ");
		assertTrue(tableValue.getRowCount() == 2);
	}

	@Test
	public void testaddAssetIntoCategory() throws DataSetException, SQLException, Exception {

		assetCatalogService.addAssetIntoCategory(1L, 1L, 2L);

		ITable tableValue = databaseTester.getConnection().createQueryTable("result",
				"select * from AssetCategoryRelation where assetId = 1 and versionId = 1 and ctgId = 2 ");
		assertTrue(tableValue.getRowCount() == 1);
	}

	@Test
	public void testRemoveAssetFromCategory() throws DataSetException, SQLException, Exception {
		assetCatalogService.removeAssetFromCategory(2L, 2L, 2L);

		ITable tableValue = databaseTester.getConnection().createQueryTable("result",
				"select * from AssetCategoryRelation where assetId = 2 and versionId = 2 and ctgId = 2 ");
		assertTrue(tableValue.getRowCount() == 0);
	}

	@Test
	public void testRemoveAssetFromAllCategories() throws DataSetException, SQLException, Exception {
		assetCatalogService.removeAssetFromAllCategories(2L, 2L);

		ITable tableValue = databaseTester.getConnection().createQueryTable("result",
				"select * from AssetCategoryRelation where assetId = 2 and versionId = 2 ");
		assertTrue(tableValue.getRowCount() == 0);
	}

	@Test
	public void testAddAssetProvider() throws DataSetException, SQLException, Exception {
		AssetProvider provider = new AssetProviderImpl();
		provider.setCity("shanghai");
		provider.setName("levi");
		provider.setCommissionRate(1.5);
		provider.setContractExpireDate(new Date());
		provider.setCountry("china");
		provider.setEmail("wei.li20@hp.com");
		provider.setOrganization("HP");
		assetCatalogService.addAssetProvider(provider);

		assertTrue(provider.getId() > 0);

		ITable tableValue = databaseTester.getConnection().createQueryTable("result", "select * from ASSETPROVIDER where email = 'wei.li20@hp.com'");
		assertTrue(tableValue.getRowCount() == 1);
	}

	@Test
	public void testSearchAssetProvider() throws DataSetException, SQLException, Exception {
		SearchExpression searchExpression = new SearchExpressionImpl();
		searchExpression.addCondition(new AssetProviderSourceCondition("source", StringComparer.LIKE, false, false));
		searchExpression.addCondition(new AssetProviderExternalIdCondition("33", StringComparer.NOT_EQUAL, false, false));

		List<AssetProvider> assetProviders = assetCatalogService.searchAssetProvider(searchExpression);

		assertTrue(assetProviders.size() == 2);
	}

	@Test
	public void testUpdateAssetProvider() throws DataSetException, SQLException, Exception {

		AssetProvider assetProvider = assetCatalogService.getAssetProvider(1L);
		assetProvider.setName("shaoping");

		assetCatalogService.updateAssetProvider(assetProvider);

		ITable tableValue = databaseTester.getConnection().createQueryTable("result",
				"select * from ASSETPROVIDER where name = 'shaoping' and id = 1");
		assertTrue(tableValue.getRowCount() == 1);
	}

	@Test
	public void testAddAttributeChar() throws DataSetException, SQLException, Exception {
		assetCatalogService.addAttribute(EntityType.ASSET, 1L, "char_name", "char_value");

		ITable tableValue = databaseTester.getConnection().createQueryTable(
				"result",
				"select a.AttributeID from Attribute a, AttributeValueChar b where "
						+ "a.attributeId = b.attributeId and a.attributeName = 'char_name' and b.value = 'char_value' ");
		assertTrue(tableValue.getRowCount() == 1);
	}

	@Test
	public void testAddAttributeNumber() throws DataSetException, SQLException, Exception {
		assetCatalogService.addAttribute(EntityType.ASSET, 1L, "number_name", 22.0f);

		ITable tableValue = databaseTester.getConnection().createQueryTable(
				"result",
				"select a.AttributeID from Attribute a, AttributeValueNumber b where "
						+ "a.attributeId = b.attributeId and a.attributeName = 'number_name' and b.value = 22.0 ");
		assertTrue(tableValue.getRowCount() == 1);
	}

	@Test
	public void testAddAttributeDate() throws DataSetException, SQLException, Exception {
		Date date = Calendar.getInstance().getTime();
		assetCatalogService.addAttribute(EntityType.ASSET, 1L, "date_name", date);

		ITable tableValue = databaseTester.getConnection().createQueryTable(
				"result",
				"select a.AttributeID,b.value from Attribute a, AttributeValueDate b where "
						+ "a.attributeId = b.attributeId and a.attributeName = 'date_name'");
		assertTrue(tableValue.getRowCount() == 1);
		assertTrue(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(tableValue.getValue(0, "value")).equals(
				new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date)));
	}

	@Test
	public void testAddAttributes() throws DataSetException, SQLException, Exception {

		AttributeList attributes = assetCatalogService.constructAttributeList();
		attributes.addAttribute("jiang", "shaoping");
		assetCatalogService.addAttributes(EntityType.ASSET, 1L, attributes);

		ITable tableValue = databaseTester.getConnection().createQueryTable(
				"result",
				"select a.AttributeID from Attribute a, AttributeValueChar b where "
						+ "a.attributeId = b.attributeId and a.attributeName = 'jiang' and b.value = 'shaoping' ");
		assertTrue(tableValue.getRowCount() == 1);

		CatalogAsset catalogAsset = assetCatalogService.getAsset(1L);
		Assert.assertTrue(((String) catalogAsset.getAttributes().get("jiang").get(0)).equals("shaoping"));
	}

	@Test
	public void testRemoveAttributes() throws DataSetException, SQLException, Exception {

		assetCatalogService.addAttribute(EntityType.ASSET, 1L, "newAttribute", "value");
		ITable tableValue = databaseTester.getConnection().createQueryTable(
				"result",
				"select b.entityId from Attribute a, AttributeValueChar b where "
						+ "a.attributeId = b.attributeId and a.attributeName = 'newAttribute' and b.value = 'value' ");
		assertTrue(tableValue.getRowCount() == 1);
		assertTrue((Long)tableValue.getValue(0, "entityId") == 1);
		
		assetCatalogService.removeAttributes(EntityType.ASSET, 1L, "newAttribute");
		 tableValue = databaseTester.getConnection().createQueryTable(
				"result",
				"select b.entityId from Attribute a, AttributeValueChar b where "
						+ "a.attributeId = b.attributeId and a.attributeName = 'newAttribute' and b.value = 'value' ");
		assertTrue(tableValue.getRowCount() == 0);
		
	}

	@Test
	public void testRemoveTheSameAttributeInForLoop() throws DataSetException, SQLException, Exception {

		assetCatalogService.addAttribute(EntityType.BINARYVERSION, 1L, "TheSameAttributeRecycle", "TheSameAttributeRecycleValue");

		ITable tableValue = databaseTester
				.getConnection()
				.createQueryTable(
						"result",
						"select a.AttributeID from Attribute a, AttributeValueChar b where "
								+ "a.attributeId = b.attributeId and a.attributeName = 'TheSameAttributeRecycle' and b.value = 'TheSameAttributeRecycleValue' ");
		assertTrue(tableValue.getRowCount() == 1);
		/* sbm bug 31 */
		for (int i = 0; i < 3; i++) {
			assetCatalogService.removeAttributes(EntityType.BINARYVERSION, 1L, "TheSameAttributeRecycle");
		}

		tableValue = databaseTester
				.getConnection()
				.createQueryTable(
						"result",
						"select a.AttributeID from Attribute a, AttributeValueChar b where "
								+ "a.attributeId = b.attributeId and a.attributeName = 'TheSameAttributeRecycle' and b.value = 'TheSameAttributeRecycleValue' ");
		assertTrue(tableValue.getRowCount() == 0);
	}

	@Test
	public void testAddBinaryVersion() throws DataSetException, SQLException, Exception {
		BinaryVersion binaryVersion = this.assetCatalogService.constructBinaryVersion();
		InputStream inputStream = new ByteArrayInputStream("test".getBytes());

		binaryVersion.setFileName("file.doc");
		binaryVersion.setAssetId(2);
		binaryVersion.setFile(inputStream);
		binaryVersion.setStatus("uploaded");
		assetCatalogService.addBinaryVersion(binaryVersion);

		Long id = ((BinaryVersionImpl) binaryVersion).getId();

		ITable tableValue = databaseTester.getConnection().createQueryTable("result",
				"select a.filename from AssetBinaryVersion a where a.statusId = 1 and a.assetId = 2 and a.id = " + id);
		assertTrue(tableValue.getRowCount() == 1);
	}

	@Test
	public void testGetThumbnailUrlBinaryVersion() throws DataSetException, SQLException, Exception {

		BinaryVersion binaryVersion = assetCatalogService.getBinaryVersion(2L);

		binaryVersion.getThumbnailUrl();
	}

	@Test
	public void testDeleteBinaryVersion() throws DataSetException, SQLException, Exception {
		assetCatalogService.deleteBinaryVersion(1L);

		ITable tableValue = databaseTester.getConnection().createQueryTable("result", "select a.filename from AssetBinaryVersion a where  a.id = 1 ");
		assertTrue(tableValue.getRowCount() == 0);

		tableValue = databaseTester.getConnection().createQueryTable("result", "select a.* from AssetCategoryRelation a where a.versionid = 1 ");
		assertTrue(tableValue.getRowCount() == 0);
	}

	@Test
	public void aa() throws DataSetException, SQLException, Exception {
		// CatalogAsset asset = assetCatalogService.constructAssetObject();
		//
		// asset.setSource("test_source");
		// asset.setName("");
		// asset.setStatus("uploaded");
		//
		// long assetId = assetCatalogService.addAsset(asset);
		// // assertTrue(asset.getCurrentVersionId() == 0);
		//
		// BinaryVersion binaryVersion =
		// assetCatalogService.constructBinaryVersion();
		// binaryVersion.setAssetId(assetId);
		// binaryVersion.setAssetParentId(assetId);
		// binaryVersion.setName("Test Data");
		// binaryVersion.setVersion("1.0");
		//
		// long versionId = assetCatalogService.addBinaryVersion(binaryVersion);
		//		
		// ITable tableValue =
		// databaseTester.getConnection().createQueryTable("result",
		// "select a.filename from AssetBinaryVersion a where a.id = "
		// +versionId);
		// assertTrue(tableValue.getRowCount() == 1);

		assetCatalogService.deleteBinaryVersion(2L);
		ITable tableValue = databaseTester.getConnection()
				.createQueryTable("result", "select a.filename from AssetBinaryVersion a where a.id = " + 2);
		assertTrue(tableValue.getRowCount() == 0);
	}

	@Test
	public void testDeleteBinaryVersion0() throws Exception {

		SearchExpression searchExpression = new SearchExpressionImpl();
		searchExpression.addCondition(new AssetExternalIdCondition("CP01SID11000000001", StringComparer.EQUAL, false, false));
		List<CatalogAsset> caList = assetCatalogService.searchAsset(searchExpression);

		searchExpression = new SearchExpressionImpl();
		searchExpression.addCondition(new AssetBinaryVersionExternalIdCondition("CP01SID11000000002", StringComparer.EQUAL, false, false));

		List<BinaryVersion> bvList = assetCatalogService.searchBinaryVersions(searchExpression);
		long id = bvList.get(0).getId();

		assetCatalogService.deleteBinaryVersion(id);

	}

	@Test
	public void testDeleteBinaryVersion2() throws Exception {
		SearchEngine searchEngine = new SearchEngineImpl();
		AssetSourceConditionDescriptor ascDesc = new AssetSourceConditionDescriptor("02", Condition.StringComparer.EQUAL, false, false);
		AssetExternalIdConditionDescriptor aeicDesc = new AssetExternalIdConditionDescriptor("CP01SID11000000002", Condition.StringComparer.EQUAL,
				false, false);
		SearchExpression searchExpr = searchEngine.createSearchExpression();
		searchExpr.addCondition(searchEngine.createCondition(ascDesc));
		searchExpr.addCondition(searchEngine.createCondition(aeicDesc));
		List<CatalogAsset> caList = assetCatalogService.searchAsset(searchExpr);

		SearchExpression searchExpression = new SearchExpressionImpl();
		searchExpression.addCondition(new AssetBinaryVersionExternalIdCondition("CP01SID11000000002", StringComparer.EQUAL, false, false));

		List<BinaryVersion> bvList = assetCatalogService.searchBinaryVersions(searchExpression);
		long id = bvList.get(0).getId();

		assetCatalogService.deleteBinaryVersion(id);
	}

	@Test
	public void testDeleteBinaryVersion1() throws DataSetException, SQLException, Exception {

		com.hp.sdf.ngp.model.Asset asset = new com.hp.sdf.ngp.model.Asset();

		asset.setName(UUID.randomUUID().toString());
		asset.setAuthorid("");

		asset.setStatus(this.applicationService.getStatusById(1L));

		asset.setExternalId("test");
		this.applicationService.saveAsset(asset);

		AssetBinaryVersion assetBinaryVersion = new AssetBinaryVersion();

		assetBinaryVersion.setAsset(asset);
		assetBinaryVersion.setExternalId("test");
		assetBinaryVersion.setName(asset.getName());
		assetBinaryVersion.setStatus(this.applicationService.getStatusById(1L));
		this.applicationService.saveAssetBinary(null, assetBinaryVersion);

		this.assetCatalogService.addAttribute(EntityType.BINARYVERSION, assetBinaryVersion.getId(), "test", 1F);

		this.assetCatalogService.addAttribute(EntityType.BINARYVERSION, assetBinaryVersion.getId(), "test1", new Date());

		this.assetCatalogService.addAttribute(EntityType.BINARYVERSION, assetBinaryVersion.getId(), "test2", "sdfsdf");

		this.assetCatalogService.addAssetIntoCategory(asset.getId(), assetBinaryVersion.getId(), 1L);
		this.assetCatalogService.addAssetIntoCategory(asset.getId(), null, 1L);

		// this.applicationService.associateTagRelation(1L, asset.getId());

		this.applicationService.associateTagRelation(1L, asset.getId(), assetBinaryVersion.getId());

		SearchExpression searchExpression = new SearchExpressionImpl();
		searchExpression.addCondition(new AssetExternalIdCondition("test", StringComparer.EQUAL, false, false));
		List<CatalogAsset> caList = assetCatalogService.searchAsset(searchExpression);

		searchExpression = new SearchExpressionImpl();
		searchExpression.addCondition(new AssetBinaryVersionExternalIdCondition("test", StringComparer.EQUAL, false, false));

		List<BinaryVersion> bvList = assetCatalogService.searchBinaryVersions(searchExpression);

		long id = bvList.get(0).getId();

		assetCatalogService.deleteBinaryVersion(id);

	}

	@Test
	public void testGetBinaryVersion() throws DataSetException, SQLException, Exception {
		BinaryVersion binaryVersion = assetCatalogService.getBinaryVersion(2L);

		assertEquals(binaryVersion.getFileLocation(), "assetcatalog/20090711/1200/1/binary/2.0/file.zip");
	}

	@SuppressWarnings("deprecation")
	@Test
	public void testUpdateBinaryVersion() throws DataSetException, SQLException, Exception {
		BinaryVersion binaryVersion = assetCatalogService.getBinaryVersion(2L);
		InputStream inputStream = new ByteArrayInputStream("shaoping".getBytes());
		binaryVersion.setFileName("jsp.doc");
		binaryVersion.setStatus("published");
		binaryVersion.setFile(inputStream);

		binaryVersion.setAssetId(2L);

		assetCatalogService.updateBinaryVersion(binaryVersion);

		ITable tableValue = databaseTester.getConnection().createQueryTable("result",
				"select a.* from AssetBinaryVersion a,status b where a.statusId = b.id and a.assetId = 2 and b.status = 'published' and a.id = 2 ");
		assertTrue(tableValue.getRowCount() == 1);
		// assertEquals(((BinaryVersionImpl)
		// binaryVersion).getAssetBinaryVersion().getAsset().getName(), "APP2");
		assertNotNull(tableValue.getValue(0, "fileSize"));
		assertTrue(String.valueOf(tableValue.getValue(0, "location")).endsWith("jsp.doc"));
		assertNotNull(binaryVersion.getFileUrl());
	}

	@Test
	public void testGetBinaryVersionSearchResultCount() throws DataSetException, SQLException, Exception {
		Date date = Calendar.getInstance().getTime();
		SearchExpression searchExpression = new SearchExpressionImpl();
		searchExpression.addCondition(new AssetBinaryVersionFileNameCondition("file", StringComparer.LIKE, false, false));
		searchExpression.addCondition(new AssetBinaryVersionStatusIdCondition(1L, NumberComparer.EQUAL));
		searchExpression.addCondition(new AssetBinaryVersionStatusNameCondition("uploaded", StringComparer.EQUAL, false, false));
		searchExpression.addCondition(new AssetBinaryVersionNewArrivalDueDateCondition(date, DateComparer.LESS_THAN));

		Long count = assetCatalogService.getBinaryVersionSearchResultCount(searchExpression);
		assertTrue(count == 4);
	}

	@Test
	public void testSearchBinaryVersions() throws DataSetException, SQLException, Exception {

		SearchExpression searchExpression = new SearchExpressionImpl();

		searchExpression.addCondition(new AssetBinaryVersionSourceCondition("test_source", StringComparer.EQUAL, false, false));

		searchExpression.addOrder(BinaryVersionOrderBy.ID, OrderEnum.ASC);

		List<BinaryVersion> binaryVersions = assetCatalogService.searchBinaryVersions(searchExpression);

		assertTrue(binaryVersions.size() > 0);
		assertEquals(binaryVersions.get(0).getFileName(), "file.zip");
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.HOUR, -1);
		this.applicationService.addAttribute(binaryVersions.get(0).getId(), EntityType.BINARYVERSION, "sampleAttributeDate", calendar.getTime());

		searchExpression = new SearchExpressionImpl();
		searchExpression.addCondition(new AssetBinaryVersionSourceCondition("test_source", StringComparer.EQUAL, false, false));
		searchExpression.addCondition(new EavDateCondition(EntityType.BINARYVERSION, "sampleAttributeDate", new Date(), DateComparer.LESS_THAN));
		searchExpression.addOrder(BinaryVersionOrderBy.ID, OrderEnum.ASC);

		binaryVersions = assetCatalogService.searchBinaryVersions(searchExpression);
		assertTrue(binaryVersions.size() == 1);
	}

	@Test
	public void testSearchBinaryVersions3() throws DataSetException, SQLException, Exception {
		this.clearEAVAttribute();

		SearchExpression searchExpression = new SearchExpressionImpl();

		searchExpression.addCondition(new AssetBinaryVersionStatusNameCondition("uploaded", StringComparer.EQUAL, false, false));

		List<BinaryVersion> contentsList = assetCatalogService.searchBinaryVersions(searchExpression);

		assertTrue(contentsList.size() > 0);

		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.HOUR, -1);

		searchExpression = new SearchExpressionImpl();
		searchExpression.addCondition(new AssetBinaryVersionStatusNameCondition("uploaded", StringComparer.EQUAL, false, false));

		this.assetCatalogService.addAttribute(EntityType.BINARYVERSION, contentsList.get(0).getId(), "publishflg", "published");
		this.assetCatalogService.addAttribute(EntityType.BINARYVERSION, contentsList.get(0).getId(), "sampleAttributeDate", calendar.getTime());

		searchExpression
				.addCondition(new EavStringCondition(EntityType.BINARYVERSION, "publishflg", "published", StringComparer.EQUAL, false, false));
		searchExpression.addCondition(new EavDateCondition(EntityType.BINARYVERSION, "sampleAttributeDate", new Date(), DateComparer.LESS_THAN));

		contentsList = assetCatalogService.searchBinaryVersions(searchExpression);

		assertTrue(contentsList.size() == 1);
	}

	@Test
	public void testDeleteCategory() throws DataSetException, SQLException, Exception {
		assetCatalogService.deleteCategory(2L);
		ITable tableValue = databaseTester.getConnection().createQueryTable("result",
				"select a.name from category a where a.name = 'game' and a.ID = 2");
		assertTrue(tableValue.getRowCount() == 0);
	}

	@Test
	public void testGetCategory() throws DataSetException, SQLException, Exception {

		AssetCategory assetCategory = assetCatalogService.getCategory(4L);
		// assertEquals(assetCategory.getParent().getName(), "game");
		assertEquals(assetCategory.getName(), "for test");
	}

	@Test
	public void testGetCategoryCatchException() {

		try {
			AssetCategory assetCategory = assetCatalogService.getCategory(4000L);
		} catch (AssetCatalogServiceException e) {
			int i = e.getMessage().indexOf("getCategory returns 0");
			assertTrue(i > 0);

		}

	}

	@Test
	public void testUpdateCategory() throws DataSetException, SQLException, Exception {
		AssetCategory assetCategory = assetCatalogService.getCategory(3L);
		assetCategory.setName("basketball");
		assetCatalogService.updateCategory(assetCategory);

		ITable tableValue = databaseTester.getConnection().createQueryTable("result",
				"select a.name from category a where a.name = 'basketball' and a.ID = 3");
		assertTrue(tableValue.getRowCount() == 1);
	}

	@Test
	public void testAddPlatform() throws DataSetException, SQLException, Exception {
		assetCatalogService.addPlatform(1L, "window");

		ITable tableValue = databaseTester.getConnection().createQueryTable("result",
				"select a.id from assetPlatformRelation a,Platform b where a.assetId = 1 and a.platformId = b.id and b.name = 'window' ");
		assertTrue(tableValue.getRowCount() == 1);
	}

	@Test
	public void testDeletePlatform() throws DataSetException, SQLException, Exception {
		assetCatalogService.deletePlatfrom(1L, "linux");

		ITable tableValue = databaseTester.getConnection().createQueryTable("result",
				"select a.id from assetPlatformRelation a, Platform b where a.assetId = 1 and a.platformId = b.id and b.name = 'linux' ");
		assertTrue(tableValue.getRowCount() == 0);
	}

	@Test
	public void testAddPrice() throws DataSetException, SQLException, Exception {
		Currency currency = Currency.getInstance(Locale.US);
		BigDecimal amount = new BigDecimal(5);

		assetCatalogService.addPrice(2L, 2L, currency, amount);

		ITable tableValue = databaseTester.getConnection().createQueryTable("result",
				"select a.id from assetPrice a where a.assetId = 2 and a.amount = 5 and a.versionId = 2 ");
		assertTrue(tableValue.getRowCount() == 1);
	}

	@Test
	public void testDeletePrice() throws DataSetException, SQLException, Exception {
		assetCatalogService.deletePrice(2L, 3L, Currency.getInstance(Locale.US));
		ITable tableValue = databaseTester.getConnection().createQueryTable("result",
				"select a.id from assetPrice a where a.assetId = 2 and a.versionId = 3 and a.currency = 'USD' ");
		assertTrue(tableValue.getRowCount() == 0);
	}

	@Test
	public void testAddScreenshot() throws DataSetException, SQLException, Exception {
		Screenshot screenshot = new ScreenshotImpl();
		InputStream inputStream = new ByteArrayInputStream("test".getBytes());

		screenshot.setAssetId(1L);
		screenshot.setDescription("test_desc");
		screenshot.setPicture("txt", inputStream);

		assetCatalogService.addScreenshot(screenshot);

		ITable tableValue = databaseTester.getConnection().createQueryTable("result",
				"select a.* from ScreenShots a where a.assetId = 1 and a.description = 'test_desc' and a.id = " + screenshot.getId());
		assertTrue(tableValue.getRowCount() == 1);
		// assertNotNull(tableValue.getValue(0, "pictureUrl"));
	}

	@Test
	public void testAddScreenShotWithCreateDate() throws Exception {
		Screenshot screenshot = new ScreenshotImpl();
		InputStream inputStream = new ByteArrayInputStream("test".getBytes());

		screenshot.setAssetId(1L);
		screenshot.setDescription("test_desc");
		screenshot.setPicture("txt", inputStream);

		assetCatalogService.addScreenshot(screenshot);
		ITable tableValue = databaseTester.getConnection().createQueryTable("result",
				"select a.* from ScreenShots a where a.assetId = 1 and a.description = 'test_desc' and a.id = " + screenshot.getId());
		assertEquals(1, tableValue.getRowCount());
		Date date = (Date) tableValue.getValue(0, "createDate");
		assertTrue(date != null);
	}

	@Test
	public void testDeleteScreenshot() throws DataSetException, SQLException, Exception {
		assetCatalogService.deleteScreenshot(1L);

		ITable tableValue = databaseTester.getConnection().createQueryTable("result",
				"select a.id from ScreenShots a where a.assetId = 1 and a.id = 1 ");
		assertTrue(tableValue.getRowCount() == 0);
	}

	@Test
	public void testAddTags() throws DataSetException, SQLException, Exception {
		Set<String> tags = new HashSet<String>();
		tags.add("java");
		tags.add("portlet");

		// assetCatalogService.addTags(1L, tags);

		// ITable tableValue =
		// databaseTester.getConnection().createQueryTable("result",
		// "select a.id from AssetTagRelation a, Tag b where a.assetId = 1 and a.tagId = b.id and (b.name = 'java' or b.name = 'portlet')");
		// assertTrue(tableValue.getRowCount() == 3);
	}

	@Test
	public void testRemoveTags() throws DataSetException, SQLException, Exception {
		Set<String> tags = new HashSet<String>();
		tags.add("java");
		tags.add("portlet");

		assetCatalogService.removeTags(1L, tags);

		ITable tableValue = databaseTester.getConnection().createQueryTable("result",
				"select a.id from AssetTagRelation a, Tag b where a.assetId = 1 and a.tagId = b.id and (b.name = 'java' or b.name = 'portlet')");
		assertTrue(tableValue.getRowCount() == 0);
	}

	@Test
	public void testConstructAssetObject() throws AssetCatalogServiceException {
		assertTrue(assetCatalogService.constructAssetObject() instanceof Asset);

		Date date = Calendar.getInstance().getTime();
		byte[] buf = "test".getBytes();
		InputStream in = new ByteArrayInputStream(buf);

		CatalogAsset catalogAsset = assetCatalogService.constructAssetObject();

		catalogAsset.setBrief("test_brief");
		catalogAsset.setCreationDate(date);
		catalogAsset.setStatus("test_status");
		catalogAsset.setProvider(1L);
		catalogAsset.setThumbnail("gif", in);

		CatalogAssetImpl catalogAssetImpl = (CatalogAssetImpl) catalogAsset;
		com.hp.sdf.ngp.model.Asset asset = catalogAssetImpl.getAsset();
		assertEquals(asset.getBrief(), "test_brief");
		assertEquals(asset.getCreateDate(), date);
		// assertEquals(asset.getStatus().getStatus(), "test_status");
		assertTrue(asset.getAssetProvider().getId() == 1L);

		byte[] b = catalogAssetImpl.getThumbnail();
		for (int i = 0; i < buf.length; i++) {
			assertTrue(buf[i] == b[i]);
		}
	}

	@Test
	public void testConstructAssetCategory() throws AssetCatalogServiceException {
		assertTrue(assetCatalogService.constructAssetCategory() instanceof AssetCategory);

		AssetCategory assetCategory = assetCatalogService.constructAssetCategory();
		assetCategory.setDescription("test_description");
		assetCategory.setDisplayName("test_displayName");
		assetCategory.setName("test_name");
		assetCategory.setParentId(2L);

		Category category = ((AssetCategoryImpl) assetCategory).getCategory();

		assertEquals(category.getDescription(), "test_description");
		assertEquals(category.getDisplayName(), "test_displayName");
		assertEquals(category.getName(), "test_name");
		assertTrue(category.getParentCategory().getId() == 2L);
	}

	@Test
	public void testConstructAssetProvider() throws AssetCatalogServiceException {
		assertTrue(assetCatalogService.constructAssetProvider() instanceof AssetProvider);

		AssetProvider assetProvider = assetCatalogService.constructAssetProvider();
		assetProvider.setCommissionRate(0.6);
		assetProvider.setLocale(Locale.US);

		Provider provider = ((AssetProviderImpl) assetProvider).getAssetProvider();
		assertTrue(provider.getCommissionRate() == 0.6);
		assertEquals(provider.getLocale(), Locale.US.toString());
	}

	@Test
	public void testConstructAttributeList() throws AssetCatalogServiceException {
		assertTrue(assetCatalogService.constructAssetCategory() instanceof AssetCategory);
	}

	@Test
	public void testConstructBinaryVersion() throws AssetCatalogServiceException {
		assertTrue(assetCatalogService.constructBinaryVersion() instanceof BinaryVersion);

		BinaryVersion binaryVersion = assetCatalogService.constructBinaryVersion();
		binaryVersion.setAssetId(1L);
		binaryVersion.setStatus("provided");
		binaryVersion.setFileSize(5);

		AssetBinaryVersion assetBinaryVersion = ((BinaryVersionImpl) binaryVersion).getAssetBinaryVersion();
		assertTrue(assetBinaryVersion.getAsset().getId() == 1L);
		// assertEquals(assetBinaryVersion.getStatus().getStatus(),"provided");
		assertEquals(assetBinaryVersion.getFileSize().intValue(), 5);
	}

	@Test
	public void testContructScreenshot() throws AssetCatalogServiceException {
		assertTrue(assetCatalogService.contructScreenshot() instanceof Screenshot);

		Screenshot screenshot = assetCatalogService.contructScreenshot();
		byte[] buf = "test".getBytes();
		InputStream in = new ByteArrayInputStream(buf);

		screenshot.setAssetId(1L);
		screenshot.setBinaryVersionId(1L);
		screenshot.setDescription("test_description");
		screenshot.setMimeType("test_mimeType");
		screenshot.setPicture("png", in);

		ScreenshotImpl screenshotImpl = (ScreenshotImpl) screenshot;
		ScreenShots screenShots = screenshotImpl.getScreenShot();
		assertTrue(screenShots.getAsset().getId() == 1L);
		assertTrue(screenShots.getBinaryVersion().getId() == 1L);
		assertEquals(screenShots.getDescription(), "test_description");
		assertEquals(screenShots.getMediaType(), "test_mimeType");

		byte[] b = screenshotImpl.getContent();
		for (int i = 0; i < buf.length; i++) {
			assertTrue(buf[i] == b[i]);
		}
	}

	@Test
	public void testGetAssetComment() throws AssetCatalogServiceException {
		List<AssetComment> assetComments = assetCatalogService.getAssetComment(1L, 0, Integer.MAX_VALUE);
		assertTrue(assetComments.size() == 1);
	}

	@Test
	public void testGetAssetCommentCount() throws AssetCatalogServiceException {
		Long count = assetCatalogService.getAssetCommentCount(1L);
		assertTrue(count == 1);
	}

	@Test
	public void testUpdateAssetCurrentVersion() throws AssetCatalogServiceException {
		assetCatalogService.updateAssetCurrentVersion(2L);

		CatalogAsset catalogAsset = assetCatalogService.getAsset(1L);
		assertTrue(catalogAsset.getCurrentVersion().equals("2.0"));
		assertTrue(catalogAsset.getCurrentVersionId() == 2);
	}

	@Test
	public void testDeleteAssetProviderId() throws DataSetException, SQLException, Exception {
		// Exception occured when remove Asset's providerId column
		CatalogAsset catalogAsset = assetCatalogService.getAsset(1L);
		assertNotNull(catalogAsset.getProvider());
		catalogAsset.setProvider(null);
		assetCatalogService.updateAsset(catalogAsset);

		catalogAsset = assetCatalogService.getAsset(1L);
		Assert.assertNull(catalogAsset.getProvider());
	}

	@Test
	public void testDeleteAssetProvider() throws DataSetException, SQLException, Exception {

		assetCatalogService.deleteAssetProvider(2L);

		ITable tableValue = databaseTester.getConnection().createQueryTable("result", "select * from ASSETPROVIDER where id = 2");
		assertTrue(tableValue.getRowCount() == 0);
	}

	@Test
	public void testGetAssetProvider() throws DataSetException, SQLException, Exception {

		AssetProvider assetProvider = assetCatalogService.getAssetProvider(2L);
		assertEquals(assetProvider.getName(), "liuchao2");
	}

	@Test
	public void testCreateAssetGroup() throws DataSetException, SQLException, Exception {

		List<Long> childAssetIds = new ArrayList<Long>();
		childAssetIds.add(1L);
		childAssetIds.add(2L);

		assetCatalogService.createAssetGroup(3L, childAssetIds);

		ITable tableValue = databaseTester.getConnection().createQueryTable("result",
				"select a.id from Asset a where a.PARENTID = 3 and (a.id = 1 or a.id = 2)");
		assertTrue(tableValue.getRowCount() == 2);

		// ----
		com.hp.sdf.ngp.model.Asset asset1 = applicationService.getAsset(1L);
		asset1.setAsset(null);
		applicationService.updateAsset(asset1);

		com.hp.sdf.ngp.model.Asset asset2 = applicationService.getAsset(2L);
		asset2.setAsset(null);
		applicationService.updateAsset(asset2);

	}

	@Test
	public void testSearchCategory() throws AssetCatalogServiceException {
		SearchExpression searchExpression = new SearchExpressionImpl();
		searchExpression.addCondition(new CategoryIdCondition(2L, NumberComparer.LESS_EQUAL));
		searchExpression.addCondition(new CategoryNameCondition("for test", StringComparer.NOT_EQUAL, false, false));
		searchExpression.addCondition(new CategoryDescriptionCondition("sports", StringComparer.NOT_EQUAL, false, false));
		searchExpression.addOrder(CategoryOrderBy.ID, OrderEnum.DESC);

		List<AssetCategory> assetCategorys = assetCatalogService.searchCategory(searchExpression);
		assertTrue(assetCategorys.size() == 2);
		assertEquals(assetCategorys.get(0).getDescription(), "game");
	}

	@Test
	public void testSearchCategoryParentAndSub() throws AssetCatalogServiceException {
		// BG95
		AssetCategory ct1 = new AssetCategoryImpl();
		ct1.setName("ct1_category");
		assetCatalogService.addCategory(ct1);

		AssetCategory ct2 = new AssetCategoryImpl();
		ct2.setName("ct2_category");
		ct2.setParentId(ct1.getId());
		assetCatalogService.addCategory(ct2);

		AssetCategory ct3 = new AssetCategoryImpl();
		ct3.setName("ct32_category");
		ct3.setParentId(ct2.getId());
		assetCatalogService.addCategory(ct3);

		SearchExpression searchExpression = new SearchExpressionImpl();
		searchExpression.addCondition(new CategoryIdCondition(ct3.getId(), NumberComparer.EQUAL));

		List<AssetCategory> assetCategorys = assetCatalogService.searchCategory(searchExpression);
		assertTrue(assetCategorys.size() == 1);
		assertNotNull(assetCategorys.get(0).getParent().getId());
		assertNotNull(assetCategorys.get(0).getParent().getSubCategories());
		assertNotNull(assetCategorys.get(0).getParent().getParent().getId());
		assertNotNull(assetCategorys.get(0).getParent().getParent().getSubCategories());
		this.applicationService.deleteCategoryById(ct1.getId());
		this.applicationService.deleteCategoryById(ct2.getId());
		this.applicationService.deleteCategoryById(ct3.getId());

	}

	@Test
	public void testAddAssetLifecycleActionHistory() throws Exception {
		AssetLifecycleActionHistory assetLifecycleActionHistory = new AssetLifecycleActionHistoryImpl();
		assetLifecycleActionHistory.setSource("test_source");
		assetLifecycleActionHistory.setAssetId(2L);

		Long id = assetCatalogService.addAssetLifecycleActionHistory(assetLifecycleActionHistory);

		ITable tableValue = databaseTester.getConnection().createQueryTable("result",
				"select a.id from AssetLifecycleActionHistory a where a.source = 'test_source' and a.assetId = 2 and a.id = " + id);
		assertTrue(tableValue.getRowCount() == 1);
	}

	@Test
	public void testConstructAssetLifecycleActionHistory() throws AssetCatalogServiceException {
		assertTrue(assetCatalogService.constructAssetLifecycleActionHistory() instanceof AssetLifecycleActionHistory);
		AssetLifecycleActionHistory assetLifecycleActionHistory = assetCatalogService.constructAssetLifecycleActionHistory();
		assetLifecycleActionHistory.setComments("test_comment");
		assetLifecycleActionHistory.setDescription("test_description");

		com.hp.sdf.ngp.model.AssetLifecycleActionHistory assetLifecycleAction = ((AssetLifecycleActionHistoryImpl) assetLifecycleActionHistory)
				.getAssetLifecycleActionHistory();
		assertEquals(assetLifecycleAction.getComments(), "test_comment");
		assertEquals(assetLifecycleAction.getDescription(), "test_description");
	}

	@Test
	public void testSearchAssetLifecycleActionHistory() throws AssetCatalogServiceException {
		SearchExpression searchExpression = new SearchExpressionImpl();
		searchExpression.addCondition(new AssetLifecycleActionHistorySourceCondition("shaoping", StringComparer.LIKE, false, false));
		searchExpression.addCondition(new AssetLifecycleActionHistoryPreStatusCondition("pre_status", StringComparer.LIKE, false, false));
		searchExpression.addCondition(new AssetLifecycleActionHistoryPostStatusCondition("post_status", StringComparer.LIKE, false, false));

		List<AssetLifecycleActionHistory> assetLifecycleActionHistorys = assetCatalogService.searchAssetLifecycleActionHistory(searchExpression);
		assertTrue(assetLifecycleActionHistorys.size() == 2);
		assertTrue(assetLifecycleActionHistorys.get(0).getAssetId() == 3L);
	}

	@Test
	public void testUpdateAssetLifecycleActionHistory() throws Exception {
		AssetLifecycleActionHistory assetLifecycleActionHistory = new AssetLifecycleActionHistoryImpl();
		assetLifecycleActionHistory.setId(2L);
		assetLifecycleActionHistory.setSource("steven");
		assetLifecycleActionHistory.setAssetId(2L);

		assetCatalogService.updateAssetLifecycleActionHistory(assetLifecycleActionHistory);

		ITable tableValue = databaseTester.getConnection().createQueryTable("result",
				"select a.id from AssetLifecycleActionHistory a where a.source = 'steven' and a.assetId = 2 and a.id = 2 ");
		assertTrue(tableValue.getRowCount() == 1);
	}

	@Test
	public void testSearchAssetComments() throws AssetCatalogServiceException {
		SearchExpression searchExpression = new SearchExpressionImpl();
		searchExpression.addCondition(new CommentsContentCondition("go", StringComparer.LIKE, false, false));
		searchExpression.addCondition(new CommentsUpdateDateCondition(Calendar.getInstance().getTime(), DateComparer.LESS_THAN));
		searchExpression.addOrder(CommentsOrderBy.ASSETID, OrderEnum.DESC);

		List<AssetComment> assetComments = assetCatalogService.searchAssetComments(searchExpression);
		assertTrue(assetComments.size() == 2);
		assertEquals(assetComments.get(0).getAssetVersion(), "1.0");
	}

	@Test
	public void testUpgradeAssetCurrentVersion() throws AssetCatalogServiceException {
		assetCatalogService.upgradeAssetCurrentVersion(1L, 1L);

		Asset asset = assetCatalogService.getAsset(1L);
		BinaryVersion binaryVersion = assetCatalogService.getBinaryVersion(1L);

		assertEquals(asset.getName(), binaryVersion.getName());
		assertEquals(asset.getRecommendStartDate(), binaryVersion.getRecommendStartDate());
	}

	@Test
	public void testCreateCategoryRelationShip() throws DataSetException, SQLException, Exception {
		List<Long> childCategoryIds = new ArrayList<Long>();
		childCategoryIds.add(1L);
		childCategoryIds.add(2L);

		assetCatalogService.createCategoryRelationShip(3L, childCategoryIds);

		ITable tableValue = databaseTester.getConnection().createQueryTable("result",
				"select a.id from Category a where a.PARENTID = 3 and (a.id = 1 or a.id = 2)");
		assertTrue(tableValue.getRowCount() == 2);

		// ----
		Category category1 = applicationService.getCategoryById(1L);
		category1.setParentCategory(null);
		applicationService.updateCategory(category1);

		Category category2 = applicationService.getCategoryById(2L);
		category2.setParentCategory(null);
		applicationService.updateCategory(category2);
	}

	@Test
	public void testAddCategory() throws DataSetException, SQLException, Exception {
		AssetCategory assetCategory = new AssetCategoryImpl();

		assetCategory.setName("child_category");
		assetCategory.setParentId(4L);
		assetCatalogService.addCategory(assetCategory);

		ITable tableValue = databaseTester.getConnection().createQueryTable("result",
				"select a.name from category a where a.name = 'child_category' and a.PARENTID = 4 and a.id = " + assetCategory.getId());
		assertTrue(tableValue.getRowCount() == 1);

		// ----
		Category category1 = applicationService.getCategoryById(assetCategory.getId());
		category1.setParentCategory(null);
		applicationService.updateCategory(category1);
	}

	@Test
	public void testSetRestrictionTypes() throws Exception {
		Set<Long> restrictionTypeIds = new HashSet<Long>();
		restrictionTypeIds.add(1L);
		restrictionTypeIds.add(2L);

		assetCatalogService.setRestrictionTypes(2L, 2L, restrictionTypeIds);

		ITable tableValue = databaseTester.getConnection().createQueryTable("result",
				"select a.* from AssetRestrictionRelation a where a.assetId = 2 and a.versionId = 2 and a.RESTRICTIONID in (1,2) ");
		assertTrue(tableValue.getRowCount() == 2);
	}

	@Test
	public void testAddRestrictionType() throws Exception {
		RestrictionType restrictionType = new RestrictionTypeImpl();
		restrictionType.setType("test_type");

		assetCatalogService.addRestrictionType(restrictionType);

		ITable tableValue = databaseTester.getConnection().createQueryTable("result",
				"select a.* from RestrictedType a where a.type = 'test_type' and a.id = " + restrictionType.getId());
		assertTrue(tableValue.getRowCount() == 1);
	}

	@Test
	public void testConstructRestrictionType() {
		assertTrue(assetCatalogService.constructRestrictionType() instanceof RestrictionType);

		RestrictionType restrictionType = assetCatalogService.constructRestrictionType();
		restrictionType.setType("jsp_type");

		RestrictedType restrictedType = ((RestrictionTypeImpl) restrictionType).getRestrictedType();

		assertEquals(restrictedType.getType(), "jsp_type");
	}

	@Test
	public void testDeleteRestrictionType() throws Exception {
		assetCatalogService.deleteRestrictionType(2L);

		ITable tableValue = databaseTester.getConnection().createQueryTable("result", "select a.* from RestrictedType a where a.id = 2 ");
		assertTrue(tableValue.getRowCount() == 0);
	}

	@Test
	public void testGetAllRestrictionTypes() throws AssetCatalogServiceException {
		List<RestrictionType> restrictionTypes = assetCatalogService.getAllRestrictionTypes();

		assertTrue(restrictionTypes.size() == 3);
		assertTrue(restrictionTypes.get(2).getType().contains("test"));
	}

	@Test
	public void testAddUser() throws Exception {
		User user = new UserImpl();
		user.setUserid("jsp");
		user.setPassword("123456");
		user.setAddress("zhangjiang district");
		// user.setEmail("jiangsp@tom.com");

		assetCatalogService.addUser(user);

		ITable tableValue = databaseTester.getConnection().createQueryTable("result",
				"select a.* from UserProfile a where a.userid = 'jsp' and a.address = 'zhangjiang district' ");
		assertTrue(tableValue.getRowCount() == 1);
	}

	@Test
	public void testAddUserToProvider() throws Exception {
		assetCatalogService.addUserToProvider("yulin", 1L);

		ITable tableValue = databaseTester.getConnection().createQueryTable("result",
				"select a.* from ContentProviderOperator a where a.userid = 'yulin' and a.assetProviderId = 1 ");
		assertTrue(tableValue.getRowCount() == 1);
	}

	@Test
	public void testAssignRole() throws Exception {
		assetCatalogService.assignRole("levi", "Tester");

		ITable tableValue = databaseTester.getConnection().createQueryTable("result",
				"select a.* from UserRoleCategory a,RoleCategory b where a.userid = 'levi' and a.roleId = b.id and b.roleName = 'Tester' ");
		assertTrue(tableValue.getRowCount() == 1);
	}

	@Test
	public void testConstructUser() throws Exception {
		User user = assetCatalogService.constructUser();

		user.setCompany("hp gdcc");
		user.setEmail("jiangsp@tom.com");
		user.setLocale(Locale.US);

		UserImpl userImpl = (UserImpl) user;
		UserProfile userProfile = userImpl.getUserProfile();

		assertEquals(userProfile.getCompany(), "hp gdcc");
		assertEquals(userProfile.getEmail(), "jiangsp@tom.com");
		assertEquals(userProfile.getLanguage().getLocale(), Locale.US.getLanguage());
	}

	@Test
	public void testDeleteUser() throws Exception {
		assetCatalogService.deleteUser("levi");

		ITable tableValue = databaseTester.getConnection().createQueryTable("result", "select a.* from UserProfile a where a.userid = 'levi' ");
		assertTrue(tableValue.getRowCount() == 0);
	}

	@Test
	public void testGetUser() throws Exception {
		User user = assetCatalogService.getUser("zhenyu");

		assertEquals(user.getEmail(), "wei.li20@hp.com");
		assertTrue(user.getCreateDate().compareTo(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2009-07-16 12:00:00")) == 0);
		assertEquals(user.getPassword(), "123456");
	}

	@Test
	public void testRemoveUserFromProvider() throws Exception {
		assetCatalogService.removeUserFromProvider("you", 1L);

		ITable tableValue = databaseTester.getConnection().createQueryTable("result",
				"select a.* from contentProviderOperator a where a.userid = 'you' and a.assetProviderId = 1 ");
		assertTrue(tableValue.getRowCount() == 0);
	}

	@Test
	public void testUnassignRole() throws Exception {
		assetCatalogService.unassignRole("you", "Subscriber");

		ITable tableValue = databaseTester.getConnection().createQueryTable("result",
				"select a.* from UserRoleCategory a,RoleCategory b where a.userid = 'you' and a.roleId = b.id and b.roleName = 'Subscriber' ");
		assertTrue(tableValue.getRowCount() == 0);
	}

	@Test
	public void testUpdateUser() throws Exception {

		User user = new UserImpl();
		user.setUserid("testnew");
		user.setEmail("jiangsp@tom.com");
		user.setPassword("654321");
		assetCatalogService.addUser(user);

		ITable tableValue = databaseTester.getConnection().createQueryTable("result",
				"select a.* from UserProfile a where a.userid = 'testnew' and a.email = 'jiangsp@tom.com' ");
		assertTrue(tableValue.getRowCount() == 1);
		Assert.assertTrue(userService.validatePassword("testnew", "654321"));

		user.setPassword("123456");
		user.setEmail("123456@tom.com");
		assetCatalogService.updateUser(user);
		tableValue = databaseTester.getConnection().createQueryTable("result",
				"select a.* from UserProfile a where a.userid = 'testnew' and a.email = 'jiangsp@tom.com' ");
		assertTrue(tableValue.getRowCount() == 0);
		tableValue = databaseTester.getConnection().createQueryTable("result",
				"select a.* from UserProfile a where a.userid = 'testnew' and a.email = '123456@tom.com' ");
		assertTrue(tableValue.getRowCount() == 1);
		Assert.assertFalse(userService.validatePassword("testnew", "123456"));

	}

	@Test
	public void testGetUsersByProvider() throws Exception {
		List<User> users = assetCatalogService.getUsersByProvider(1L);

		assertTrue(users.size() == 1);
		assertEquals(users.get(0).getUserid(), "you");
	}

	@Test
	public void testCategoryWithParent() throws Exception {
		Category category = this.applicationService.getCategoryById(1L);
		category.setDisplayName("Category1");
		this.applicationService.updateCategory(category);
		category = this.applicationService.getCategoryById(2L);
		category.setDisplayName("Category2");
		this.applicationService.updateCategory(category);
		category = this.applicationService.getCategoryById(3L);
		category.setDisplayName("Category3");
		this.applicationService.updateCategory(category);
		this.applicationService.createCategoryRelationShip(1L, Arrays.asList(new Long[] { 2L, 3L }));
		AssetCategory assetCategory = this.assetCatalogService.getCategory(1L);
		Assert.assertTrue(assetCategory.getSubCategories().get(0).getDisplayName() != null);

		assetCategory = this.assetCatalogService.getCategory(2L);
		Assert.assertTrue(assetCategory.getParent().getDisplayName() != null);

		this.applicationService.dropCategoryRelationShip(Arrays.asList(new Long[] { 2L, 3L }));

	}

	@Test
	public void testAddAfterRemoveAttributes() throws Exception {
		addAfterRemoveAttributes();
	}

	@Transactional
	private void addAfterRemoveAttributes() throws Exception {

		SearchExpression searchExpression = new SearchExpressionImpl();

		searchExpression.addCondition(new AssetBinaryVersionIdCondition(6L, NumberComparer.LESS_EQUAL));
		assetCatalogService.searchBinaryVersions(searchExpression);

		assetCatalogService.deleteBinaryVersion(1L);

		assetCatalogService.removeAttributes(EntityType.BINARYVERSION, 2L, "AddAfterRemoveAttribute");

		assetCatalogService.addAttribute(EntityType.BINARYVERSION, 2L, "AddAfterRemoveAttribute", "AttributeValue");

		assetCatalogService.deleteBinaryVersion(2L);

	}

	@Test
	public void testAddAfterRemoveAttributes_1() throws DataSetException, SQLException, Exception {

		testAddAfterRemoveAttributes();

	}

	@Test
	public void testAddAfterRemoveAttributes1() throws DataSetException, SQLException, Exception {

		SearchExpression searchExpression = new SearchExpressionImpl();

		searchExpression.addCondition(new AssetBinaryVersionIdCondition(6L, NumberComparer.LESS_EQUAL));
		assetCatalogService.searchBinaryVersions(searchExpression);

		assetCatalogService.deleteBinaryVersion(1L);

		assetCatalogService.removeAttributes(EntityType.BINARYVERSION, 2L, "AddAfterRemoveAttribute");

		assetCatalogService.addAttribute(EntityType.BINARYVERSION, 2L, "AddAfterRemoveAttribute", "AttributeValue");

		assetCatalogService.deleteBinaryVersion(2L);

	}

	@Test
	public void testAddAfterRemoveAttributes2() throws DataSetException, SQLException, Exception {

		SearchExpression searchExpression = new SearchExpressionImpl();

		searchExpression.addCondition(new AssetBinaryVersionIdCondition(6L, NumberComparer.LESS_EQUAL));
		applicationService.searchAssetBinary(searchExpression);

		applicationService.deleteAssetBinary(1L);

		applicationService.removeAttributes(2L, EntityType.BINARYVERSION, "AddAfterRemoveAttribute");

		applicationService.addAttribute(2L, EntityType.BINARYVERSION, "AddAfterRemoveAttribute", "AttributeValue");

		applicationService.deleteAssetBinary(2L);

	}

	@Test
	public void testAddAfterRemoveAttributes4() throws DataSetException, SQLException, Exception {
		addAfterRemoveAttributes4();
	}

	@Transactional
	private void addAfterRemoveAttributes4() throws DataSetException, SQLException, Exception {
		applicationService.removeAttributes(2L, EntityType.BINARYVERSION, "AddAfterRemoveAttribute");

		applicationService.addAttribute(2L, EntityType.BINARYVERSION, "AddAfterRemoveAttribute", "AttributeValue");

		List<AttributeValue> attributeValues = applicationService.getAttributeValue(2L, EntityType.BINARYVERSION, "AddAfterRemoveAttribute");
		assertTrue(attributeValues.size() > 0);
	}

	@Test
	public void testAddAfterRemoveAttributes3() throws DataSetException, SQLException, Exception {
		addAfterRemoveAttributes3();
	}

	@Transactional(propagation = Propagation.REQUIRED)
	private void addAfterRemoveAttributes3() throws DataSetException, SQLException, Exception {

		SearchExpression searchExpression = new SearchExpressionImpl();

		searchExpression.addCondition(new AssetBinaryVersionIdCondition(6L, NumberComparer.LESS_EQUAL));
		List<AssetBinaryVersion> versionObject = applicationService.searchAssetBinary(searchExpression);

		applicationService.deleteAssetBinary(1L);

		applicationService.removeAttributes(2L, EntityType.BINARYVERSION, "AddAfterRemoveAttribute");

		applicationService.addAttribute(2L, EntityType.BINARYVERSION, "AddAfterRemoveAttribute", "AttributeValue");

		List<AttributeValue> attributeValues = applicationService.getAttributeValue(2L, EntityType.BINARYVERSION, "AddAfterRemoveAttribute");
		assertTrue(attributeValues.size() > 0);

		applicationService.deleteAssetBinary(2L);

		applicationService.deleteAssetBinary(3L);

	}

	@Test
	public void testUpdateAfterSearchMix() throws DataSetException, SQLException, Exception {
		updateAfterSearchMix();
	}

	@Transactional
	private void updateAfterSearchMix() throws DataSetException, SQLException, Exception {

		SearchExpression searchExpression = new SearchExpressionImpl();

		searchExpression.addCondition(new AssetBinaryVersionIdCondition(6L, NumberComparer.LESS_EQUAL));
		assetCatalogService.searchBinaryVersions(searchExpression);

		assetCatalogService.deleteBinaryVersion(1L);

		BinaryVersion v = assetCatalogService.getBinaryVersion(2L);
		v.setBrief("bf");
		assetCatalogService.updateBinaryVersion(v);

		assetCatalogService.upgradeAssetCurrentVersion(v.getAssetId(), v.getId());
		Asset asset = assetCatalogService.getAsset(v.getAssetId());
		assertEquals(asset.getBrief(), "bf");
	}

	@Test
	public void TestSearchBinaryVersions() throws Exception {

		String id = "CP01SID11000000001";
		String openFlg = null;

		// create expression
		SearchExpression searchExpr = searchEngine.createSearchExpression();
		// descriptor
		AssetBinaryVersionSourceConditionDescriptor abvscDesc = new AssetBinaryVersionSourceConditionDescriptor("02", Condition.StringComparer.EQUAL,
				false, false);
		// add condition to expression
		searchExpr.addCondition(searchEngine.createCondition(abvscDesc));

		// status is not REVOKED
		AssetBinaryVersionStatusNameConditionDescriptor abvsncDesc = new AssetBinaryVersionStatusNameConditionDescriptor("REVOKED",
				Condition.StringComparer.NOT_EQUAL, false, false);
		// add condition to expression
		searchExpr.addCondition(searchEngine.createCondition(abvsncDesc));

		if (id != null && id.length() > 0) {// for id
			// descriptor
			AssetBinaryVersionExternalIdConditionDescriptor abveicDesc = new AssetBinaryVersionExternalIdConditionDescriptor(id,
					Condition.StringComparer.EQUAL, false, false);
			// add condition to expression
			searchExpr.addCondition(searchEngine.createCondition(abveicDesc));
		}
		if (openFlg != null && openFlg.length() > 0) {// for flg
			// descriptor
			EavStringConditionDescriptor escDesc = new EavStringConditionDescriptor(EntityType.BINARYVERSION, "LID", openFlg,
					Condition.StringComparer.EQUAL, false, false);
			// add condition to expression
			searchExpr.addCondition(searchEngine.createCondition(escDesc));
		}

		// do search
		List<BinaryVersion> bvList = assetCatalogService.searchBinaryVersions(searchExpr);

		// return

	}

	@Test
	public void TestSearchBinaryVersions1() throws Exception {

		this.applicationService.getAllCategoryByAssetId(1L, 0, Integer.MAX_VALUE);
		// return

	}

	@Test
	@Transactional
	public void TestSearchBinaryVersions2() throws Exception {

		List<BinaryVersion> bvList = this.assetCatalogService.searchBinaryVersions(new SearchExpressionImpl());

		System.out.println("------------------" + bvList.size());
		this.assetCatalogService.deleteBinaryVersion(bvList.get(0).getId());
		bvList = this.assetCatalogService.searchBinaryVersions(new SearchExpressionImpl());
		System.out.println("==================" + bvList.size());
		if (bvList.size() > 0)
			this.assetCatalogService.deleteBinaryVersion(bvList.get(0).getId());

	}

	@Test
	@Transactional
	public void TestSearchBinaryVersions3() throws Exception {
		List<BinaryVersion> bvList = this.assetCatalogService.searchBinaryVersions(new SearchExpressionImpl());
		int oldSize = bvList.size();
		System.out.println("------------------" + bvList.size());
		this.assetCatalogService.deleteBinaryVersion(bvList.get(0).getId());
		bvList = this.assetCatalogService.searchBinaryVersions(new SearchExpressionImpl());
		int newSize = bvList.size();
		System.out.println("==================" + bvList.size());
		Assert.assertTrue(oldSize == newSize + 1);

	}

	private void clearEAVAttribute() throws Exception {
		databaseTester.getConnection().getConnection().createStatement().execute("delete from entityattribute");

		databaseTester.getConnection().getConnection().createStatement().execute("delete from attributevaluechar");

		databaseTester.getConnection().getConnection().createStatement().execute("delete from attributevaluenumber");

		databaseTester.getConnection().getConnection().createStatement().execute("delete from attributevaluedate");

	}

	@Test
	public void testNormalSearch() throws Exception {
		databaseTester.getConnection().getConnection().createStatement().execute("delete from assetcategoryrelation");
		SearchExpression searchExpression = new SearchExpressionImpl();

		List<AssetBinaryVersion> versions = this.applicationService.searchAssetBinary(searchExpression);

		this.applicationService.associateCategory(versions.get(0).getAsset().getId(), versions.get(0).getId(), 1L);
		this.applicationService.associateCategory(versions.get(1).getAsset().getId(), versions.get(1).getId(), 1L);
		this.applicationService.associateCategory(versions.get(2).getAsset().getId(), versions.get(2).getId(), 3L);
		
		searchExpression = new SearchExpressionImpl();
		AssetBinaryVersionCategoryIdCondition asssetBinaryVersionCategoryIdCondition = new AssetBinaryVersionCategoryIdCondition(1L,
				NumberComparer.EQUAL);
		AssetBinaryVersionAssetProviderIdCondition assetBinaryVersionAssetProviderIdCondition = new AssetBinaryVersionAssetProviderIdCondition(0L,
				NumberComparer.GREAT_THAN);
		searchExpression.addCondition(asssetBinaryVersionCategoryIdCondition);
		searchExpression.addCondition(assetBinaryVersionAssetProviderIdCondition);
		searchExpression.addOrder(BinaryVersionOrderBy.PROVIDERID, OrderEnum.DESC);
		long count=this.applicationService.countAssetBinary(searchExpression);
		Assert.assertTrue(count==2);
		versions = this.applicationService.searchAssetBinary(searchExpression);
		Assert.assertTrue(versions.size()==2);
		
		

	}

	@Test
	public void TestRecordAssetDownloadHistory() throws Exception {
		this.assetCatalogService.recordAssetDownloadHistory("levi", 1L, "1.8");
		
		ITable tableValue = databaseTester
		.getConnection()
		.createQueryTable("result",
				"select a.userid from UserDownloadHistory a where a.userid = 'levi' and a.assetId = 1 and a.version = '1.8'");
        assertTrue(tableValue.getRowCount() == 1);
	}
}

// $Id$