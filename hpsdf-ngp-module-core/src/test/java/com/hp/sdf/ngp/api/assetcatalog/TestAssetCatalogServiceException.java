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

import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Currency;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import javax.annotation.Resource;

import org.dbunit.dataset.ITable;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.hp.sdf.ngp.DBEnablerTestBase;
import com.hp.sdf.ngp.api.comon.EntityType;
import com.hp.sdf.ngp.api.exception.AssetCatalogEntityNotFoundException;
import com.hp.sdf.ngp.api.exception.AssetCatalogServiceException;
import com.hp.sdf.ngp.api.impl.model.AssetCategoryImpl;
import com.hp.sdf.ngp.api.impl.model.AssetProviderImpl;
import com.hp.sdf.ngp.api.impl.model.BinaryVersionImpl;
import com.hp.sdf.ngp.api.impl.model.CatalogAssetImpl;
import com.hp.sdf.ngp.api.impl.model.ScreenshotImpl;
import com.hp.sdf.ngp.api.model.AssetCategory;
import com.hp.sdf.ngp.api.model.AssetProvider;
import com.hp.sdf.ngp.api.model.BinaryVersion;
import com.hp.sdf.ngp.api.model.CatalogAsset;
import com.hp.sdf.ngp.api.model.Screenshot;
import com.hp.sdf.ngp.api.search.OrderEnum;
import com.hp.sdf.ngp.api.search.SearchExpression;
import com.hp.sdf.ngp.api.search.Condition.StringComparer;
import com.hp.sdf.ngp.api.search.orderby.BinaryVersionOrderBy;
import com.hp.sdf.ngp.search.condition.assetbinaryversion.AssetBinaryVersionFileNameCondition;
import com.hp.sdf.ngp.search.condition.assetprovider.AssetProviderExternalIdCondition;
import com.hp.sdf.ngp.search.condition.assetprovider.AssetProviderSourceCondition;
import com.hp.sdf.ngp.search.engine.SearchExpressionImpl;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:META-INF/applicationContext-test.xml" })
public class TestAssetCatalogServiceException extends DBEnablerTestBase {

	@Resource
	private AssetCatalogService assetCatalogService;

	@Override
	public String dataSetFileName() {
		return "/data_init.xml";
	}

	@Test(expected = AssetCatalogServiceException.class)
	public void testAddAsset() throws Exception {
		CatalogAsset catalogAsset = new CatalogAssetImpl();

		catalogAsset.setName("APP100");
		catalogAsset.setDescription("shaoping_description");
//		catalogAsset.setStatus("uploaded");
		catalogAsset.setProvider(101L);

		assetCatalogService.addAsset(catalogAsset);

		Long id = ((CatalogAssetImpl) catalogAsset).getAsset().getId();

		ITable tableValue = databaseTester.getConnection().createQueryTable("result",
				"select a.id from Asset a, Status b where a.statusid = b.id and a.name = 'APP100' and a.id = " + id);
		assertTrue(tableValue.getRowCount() == 1);
	}

	@Test(expected = Exception.class)
	public void testAddAssetIntoCategories() throws Exception {
		Set<Long> categoryIds = new HashSet<Long>();
		// cause an error
		categoryIds.add(10L);
		categoryIds.add(3L);

		assetCatalogService.addAssetIntoCategories(1L, 1L, categoryIds);

		ITable tableValue = databaseTester.getConnection().createQueryTable("result",
				"select * from AssetCategoryRelation where assetId = 1 and versionId = 1 and (ctgId = 2 or ctgId = 3) ");
		assertTrue(tableValue.getRowCount() == 2);
	}

	@Test(expected = Exception.class)
	public void testAddAssetIntoCategory() throws Exception {
		// cause an error
		assetCatalogService.addAssetIntoCategory(1L, 101L, 2L);

		ITable tableValue = databaseTester.getConnection().createQueryTable("result",
				"select * from AssetCategoryRelation where assetId = 1 and versionId = 1 and ctgId = 2 ");
		assertTrue(tableValue.getRowCount() == 1);
	}

	@Test(expected = AssetCatalogServiceException.class)
	public void testAddAssetProvider() throws Exception {
		AssetProvider provider = new AssetProviderImpl();
		// provider.setName("shaoping_name");
		provider.setEmail("jiangsp@tom.com");
		provider.setStreetAddress("shanghai");

		assetCatalogService.addAssetProvider(provider);

		ITable tableValue = databaseTester.getConnection().createQueryTable("result",
				"select * from ASSETPROVIDER where email = 'jiangsp@tom.com' and id = " + provider.getId());
		assertTrue(tableValue.getRowCount() == 1);
	}

	@Test
	public void testAddAttributeChar() {
		Exception exception = null;
		try {
			// cause an error
			assetCatalogService.addAttribute(EntityType.ASSET, 12L, "char_name", "char_value");
			ITable tableValue = databaseTester.getConnection().createQueryTable(
					"result",
					"select a.AttributeID from Attribute a, AttributeValueChar b where "
							+ "a.attributeId = b.attributeId and a.attributeName = 'char_name' and b.value = 'char_value' ");
			assertTrue(tableValue.getRowCount() == 1);
		} catch (Exception e) {
			exception = e;
		}

		assertNotNull(exception);
	}

	@Test
	public void testAddAttributeNumber() {
		Exception exception = null;
		try {
			// cause an error
			assetCatalogService.addAttribute(EntityType.ASSET, 12L, "number_name", 22.0f);

			ITable tableValue = databaseTester.getConnection().createQueryTable(
					"result",
					"select a.AttributeID from Attribute a, AttributeValueNumber b where "
							+ "a.attributeId = b.attributeId and a.attributeName = 'number_name' and b.value = 22.0 ");
			assertTrue(tableValue.getRowCount() == 1);
		} catch (Exception e) {
			exception = e;
		}

		assertNotNull(exception);
	}

	@Test
	public void testAddAttributeDate() {
		Exception exception = null;
		try {
			Date date = Calendar.getInstance().getTime();
			// cause an error
			assetCatalogService.addAttribute(EntityType.ASSET, 11L, "date_name", date);

			ITable tableValue = databaseTester.getConnection().createQueryTable(
					"result",
					"select a.AttributeID,b.value from Attribute a, AttributeValueDate b where "
							+ "a.attributeId = b.attributeId and a.attributeName = 'date_name'");
			assertTrue(tableValue.getRowCount() == 1);
			assertTrue(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(tableValue.getValue(0, "value")).equals(
					new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date)));
		} catch (Exception e) {
			exception = e;
		}

		assertNotNull(exception);
	}

	
	@Test(expected = Exception.class)
	public void testAddBinaryVersion() throws Exception {
		BinaryVersion binaryVersion = this.assetCatalogService.constructBinaryVersion();

		binaryVersion.setFileName("file.doc");

//		InputStream buf = this.getClass().getResourceAsStream("inherit.gif");
//		binaryVersion.setThumbnail("gif", buf);

		// cause an error
		binaryVersion.setAssetId(12L);
		assetCatalogService.addBinaryVersion(binaryVersion);

		Long id = ((BinaryVersionImpl) binaryVersion).getId();

		ITable tableValue = databaseTester.getConnection().createQueryTable("result",
				"select a.* from AssetBinaryVersion a where a.filename = 'file.doc' and a.assetId = 12 and a.id = " + id);
		assertTrue(tableValue.getRowCount() == 1);

//		String url = (String) tableValue.getValue(0, "thumbnailLocation");
//
//		InputStream in = new FileInputStream("C:/tmp/liuchao/" + url);
//		assertNotNull(in);
	}

	@Test(expected = AssetCatalogServiceException.class)
	public void testAddCategory() throws Exception {
		AssetCategory assetCategory = new AssetCategoryImpl();

//		assetCategory.setName("child_category");
		assetCategory.setParentId(4L);
		assetCatalogService.addCategory(assetCategory);

		ITable tableValue = databaseTester.getConnection().createQueryTable("result",
				"select a.name from category a where a.name = 'child_category' and a.PARENTID = 4 and a.id = " + assetCategory.getId());
		assertTrue(tableValue.getRowCount() == 1);
	}
	
	@Test(expected = Exception.class)
	public void testAddPlatform() throws Exception {
		// cause an error
		assetCatalogService.addPlatform(12L, "window");

		ITable tableValue = databaseTester.getConnection().createQueryTable("result",
				"select a.id from assetPlatformRelation a,Platform b where a.assetId = 12 and a.platformId = b.id and b.name = 'window' ");
		assertTrue(tableValue.getRowCount() == 1);
	}
	
	@Test(expected = AssetCatalogServiceException.class)
	public void testAddPrice() throws Exception {
		Currency currency = Currency.getInstance(Locale.US);
		BigDecimal amount = new BigDecimal(5);

		// cause an error
		assetCatalogService.addPrice(2L, 12L, currency, amount);

		ITable tableValue = databaseTester.getConnection().createQueryTable("result",
				"select a.id from assetPrice a where a.assetId = 2 and a.amount = 5 and a.versionId = 12 ");
		assertTrue(tableValue.getRowCount() == 1);
	}
	
	@Test(expected = AssetCatalogServiceException.class)
	public void testAddScreenshot() throws Exception{
		Screenshot screenshot = new ScreenshotImpl();

		// cause an error
		screenshot.setAssetId(12L);
		screenshot.setDescription("test_desc");

		assetCatalogService.addScreenshot(screenshot);

		ITable tableValue = databaseTester.getConnection().createQueryTable("result",
				"select a.id from ScreenShots a where a.assetId = 12 and a.description = 'test_desc' and a.id = " + screenshot.getId());
		assertTrue(tableValue.getRowCount() == 1);
	}
	
	@Ignore
	@Test(expected = AssetCatalogServiceException.class)
	public void testAddTags() throws Exception {
		Set<String> tags = new HashSet<String>();
		tags.add("java");
		//cause an error
		tags.add("portlet_test");

//		assetCatalogService.addTags(1L, tags);

//		ITable tableValue = databaseTester.getConnection().createQueryTable("result",
//				"select a.id from AssetTagRelation a, Tag b where a.assetId = 11 and a.tagId = b.id and (b.name = 'java' or b.name = 'portlet')");
//		assertTrue(tableValue.getRowCount() == 3);
	}
	
	@Test(expected = Exception.class)
	public void testCreateAssetGroup() throws Exception{
		List<Long> childAssetIds = new ArrayList<Long>();
		childAssetIds.add(1L);
		
		//cause an error
		childAssetIds.add(12L);

		assetCatalogService.createAssetGroup(3L, childAssetIds);

		ITable tableValue = databaseTester.getConnection().createQueryTable("result",
				"select a.id from Asset a where a.PARENTID = 3 and (a.id = 1 or a.id = 21)");
		assertTrue(tableValue.getRowCount() == 2);
	}
	
	@Test(expected = Exception.class)
	public void testCreateCategoryRelationShip() throws Exception {
		List<Long> childCategoryIds = new ArrayList<Long>();
		childCategoryIds.add(1L);
		childCategoryIds.add(2L);

		// cause an error
		assetCatalogService.createCategoryRelationShip(13L, childCategoryIds);

		ITable tableValue = databaseTester.getConnection().createQueryTable("result",
				"select a.id from Category a where a.PARENTID = 13 and (a.id = 1 or a.id = 2)");
		assertTrue(tableValue.getRowCount() == 2);
	}
	
	@Test(expected = Exception.class)
	public void testDeleteAssetProvider() throws Exception{
		// cause an error
		assetCatalogService.deleteAssetProvider(12L);

		ITable tableValue = databaseTester.getConnection().createQueryTable("result", "select * from ASSETPROVIDER where id = 12");
		assertTrue(tableValue.getRowCount() == 0);
	}
	
	@Test(expected = Exception.class)
	public void testDeleteBinaryVersion() throws Exception{
		// cause an error
		assetCatalogService.deleteBinaryVersion(13L);

		ITable tableValue = databaseTester.getConnection().createQueryTable("result",
				"select a.filename from AssetBinaryVersion a where a.filename = 'file.zip' and a.id = 13 ");
		assertTrue(tableValue.getRowCount() == 0);
	}
	
	@Test(expected = Exception.class)
	public void testDeleteCategory() throws Exception{
		// cause an error
		assetCatalogService.deleteCategory(12L);
		
		ITable tableValue = databaseTester.getConnection().createQueryTable("result",
				"select a.name from category a where a.name = 'game' and a.ID = 12");
		assertTrue(tableValue.getRowCount() == 0);
	}
	
	@Test
	public void testDeletePlatfrom() throws Exception {
		// cause an error
		assetCatalogService.deletePlatfrom(11L, "linux");

		ITable tableValue = databaseTester.getConnection().createQueryTable("result",
				"select a.id from assetPlatformRelation a, Platform b where a.assetId = 11 and a.platformId = b.id and b.name = 'linux' ");
		assertTrue(tableValue.getRowCount() == 0);
	}
	
	public void testDeleteScreenshot() throws Exception{
		// cause an error
		assetCatalogService.deleteScreenshot(11L);

		ITable tableValue = databaseTester.getConnection().createQueryTable("result",
				"select a.id from ScreenShots a where a.assetId = 1 and a.id = 11 ");
		assertTrue(tableValue.getRowCount() == 0);
	}
	
	@Test
	public void testRemoveAssetFromAllCategories() throws Exception{
		// cause an error
		assetCatalogService.removeAssetFromAllCategories(2L, 12L);

		ITable tableValue = databaseTester.getConnection().createQueryTable("result",
				"select * from AssetCategoryRelation where assetId = 2 and versionId = 12 ");
		assertTrue(tableValue.getRowCount() == 0);
	}
	
	@Test
	public void testRemoveAssetFromCategory() throws Exception{
		assetCatalogService.removeAssetFromCategory(2L, 12L, 2L);

		ITable tableValue = databaseTester.getConnection().createQueryTable("result",
				"select * from AssetCategoryRelation where assetId = 2 and versionId = 12 and ctgId = 2 ");
		assertTrue(tableValue.getRowCount() == 0);
	}
	
	@Test
	public void testAddNoParentCategory() throws Exception{
		AssetCategory assetCategory = new AssetCategoryImpl();

		assetCategory.setName("child_category");
		assetCatalogService.addCategory(assetCategory);

		ITable tableValue = databaseTester.getConnection().createQueryTable("result",
				"select a.name from category a where a.name = 'child_category' and a.id = " + assetCategory.getId());
		assertTrue(tableValue.getRowCount() == 1);
	}
	
	@Test(expected = Exception.class)
	public void testDeleteNonexistentCategory() throws Exception{
		// cause an error
		assetCatalogService.deleteCategory(12L);
		
		ITable tableValue = databaseTester.getConnection().createQueryTable("result",
				"select a.name from category a where a.name = 'game' and a.ID = 12");
		assertTrue(tableValue.getRowCount() == 0);
	}
	
	@Test(expected = AssetCatalogEntityNotFoundException.class )
	public void testGetNonexistentCategory() throws Exception{
		// not exist
		AssetCategory assetCategory = assetCatalogService.getCategory(14L);
		assertTrue(assetCategory == null);
	}
	
	@Test(expected = AssetCatalogServiceException.class)
	public void testDeleteNonexistentPlatform() throws Exception{
		// no exist platform
		assetCatalogService.deletePlatfrom(1L, "test_linux");

		ITable tableValue = databaseTester.getConnection().createQueryTable("result",
				"select a.id from assetPlatformRelation a, Platform b where a.assetId = 1 and a.platformId = b.id and b.name = 'linux' ");
		assertTrue(tableValue.getRowCount() == 0);
	}
	
	@Test(expected = AssetCatalogServiceException.class)
	public void testAddScreenshotForNonexistentAsset() throws Exception{
		Screenshot screenshot = new ScreenshotImpl();

		// cause an error
		screenshot.setAssetId(12L);
		screenshot.setDescription("test_desc");

		assetCatalogService.addScreenshot(screenshot);

		ITable tableValue = databaseTester.getConnection().createQueryTable("result",
				"select a.id from ScreenShots a where a.assetId = 1 and a.description = 'test_desc' and a.id = " + screenshot.getId());
		assertTrue(tableValue.getRowCount() == 1);
	}
	
	@Test(expected = Exception.class)
	public void testDeleteNonexistentBinaryVersion() throws Exception{
		// cause an error
		assetCatalogService.deleteBinaryVersion(13L);

		ITable tableValue = databaseTester.getConnection().createQueryTable("result",
				"select a.filename from AssetBinaryVersion a where a.filename = 'file.zip' and a.id = 13 ");
		assertTrue(tableValue.getRowCount() == 0);
	}
	
	@Test(expected = AssetCatalogEntityNotFoundException.class )
	public void testGetNonexistentBinaryVersion() throws Exception{
		BinaryVersion binaryVersion = assetCatalogService.getBinaryVersion(12L);

		assertTrue(binaryVersion == null);
	}
	
	@Test(expected = Exception.class)
	public void testDeleteNonexistentAssetProvider() throws Exception{
		// cause an error
		assetCatalogService.deleteAssetProvider(12L);

		ITable tableValue = databaseTester.getConnection().createQueryTable("result", "select * from ASSETPROVIDER where id = 12");
		assertTrue(tableValue.getRowCount() == 0);
	}
	
	@Test(expected = AssetCatalogEntityNotFoundException.class )
	public void testGetNonexistentAssetProvider() throws Exception{
		AssetProvider assetProvider = assetCatalogService.getAssetProvider(12L);
		assertTrue(assetProvider == null);
	}
	
	@Test(expected = Exception.class)
	public void testCreateAssetGroupWithErrorParent() throws Exception{
		List<Long> childAssetIds = new ArrayList<Long>();
		childAssetIds.add(1L);
		childAssetIds.add(2L);

		//cause an error
		assetCatalogService.createAssetGroup(13L, childAssetIds);

		ITable tableValue = databaseTester.getConnection().createQueryTable("result",
				"select a.id from Asset a where a.PARENTID = 13 and (a.id = 1 or a.id = 21)");
		assertTrue(tableValue.getRowCount() == 0);
	}
	
	@Test(expected = Exception.class)
	public void testCreateCategoryRelationShipWithErrorParent() throws Exception{
		List<Long> childCategoryIds = new ArrayList<Long>();
		childCategoryIds.add(1L);
		childCategoryIds.add(2L);

		// cause an error
		assetCatalogService.createCategoryRelationShip(13L, childCategoryIds);

		ITable tableValue = databaseTester.getConnection().createQueryTable("result",
				"select a.id from Category a where a.PARENTID = 13 and (a.id = 1 or a.id = 2)");
		assertTrue(tableValue.getRowCount() == 2);
	}
	
	@Test(expected = Exception.class)
	public void testAddAttribute() throws Exception{
		Object object = "char_value";
		assetCatalogService.addAttribute(EntityType.ASSET, 1L, "char_name", (Float)object);

		ITable tableValue = databaseTester.getConnection().createQueryTable(
				"result",
				"select a.AttributeID from Attribute a, AttributeValueChar b where "
						+ "a.attributeId = b.attributeId and a.attributeName = 'char_name' and b.value = 'char_value' ");
		assertTrue(tableValue.getRowCount() == 1);
	}
	
	@Test
	public void testSearchBinaryVersionsWithMoreConditions() throws Exception{
		SearchExpression searchExpression = new SearchExpressionImpl();
		searchExpression.addCondition(new AssetBinaryVersionFileNameCondition("file", StringComparer.LIKE, false, false));
		searchExpression.addOrder(BinaryVersionOrderBy.ID, OrderEnum.ASC);

		List<BinaryVersion> binaryVersions = assetCatalogService.searchBinaryVersions(searchExpression);
		assertTrue(binaryVersions.size() == 5);
//		assertEquals(binaryVersions.get(0).getVersion(),"1.0");
	}
	
	@Test
	public void testSearchAssetProviderWithMoreConditions() throws Exception{
		SearchExpression searchExpression = new SearchExpressionImpl();
		searchExpression.addCondition(new AssetProviderSourceCondition("source", StringComparer.LIKE, false, false));
		searchExpression.addCondition(new AssetProviderExternalIdCondition("22", StringComparer.EQUAL, false, false));

		List<AssetProvider> assetProviders = assetCatalogService.searchAssetProvider(searchExpression);

		assertTrue(assetProviders.size() == 1);
	}
}

// $Id$