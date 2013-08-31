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
package com.hp.sdf.ngp.search;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.hp.sdf.ngp.DBEnablerTestBase;
import com.hp.sdf.ngp.api.assetcatalog.AssetCatalogService;
import com.hp.sdf.ngp.api.comon.EntityRelationShipType;
import com.hp.sdf.ngp.api.comon.EntityType;
import com.hp.sdf.ngp.api.model.BinaryVersion;
import com.hp.sdf.ngp.api.search.OrderEnum;
import com.hp.sdf.ngp.api.search.SearchEngine;
import com.hp.sdf.ngp.api.search.SearchExpression;
import com.hp.sdf.ngp.api.search.Condition.DateComparer;
import com.hp.sdf.ngp.api.search.Condition.NumberComparer;
import com.hp.sdf.ngp.api.search.Condition.StringComparer;
import com.hp.sdf.ngp.api.search.condition.eav.EavDateConditionDescriptor;
import com.hp.sdf.ngp.api.search.condition.eav.EavNumberConditionDescriptor;
import com.hp.sdf.ngp.api.search.condition.eav.EavStringConditionDescriptor;
import com.hp.sdf.ngp.custom.sbm.api.search.orderby.BinaryVersionOrderByExt;
import com.hp.sdf.ngp.search.condition.assetbinaryversion.AssetBinaryVersionCategoryIdCondition;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:META-INF/applicationContext-test.xml" })
public class TestEavSearch extends DBEnablerTestBase {

	@Resource
	private AssetCatalogService assetCatalogService;

	@Resource
	private SearchEngine searchEngine;

	@Override
	public String dataSetFileName() {
		return "/data_init.xml";
	}

	@Test
	public void testComplexSearch() throws Exception {
		clearEAVAttribute();

		Calendar calendar = Calendar.getInstance();

		this.assetCatalogService.addAttribute(EntityType.BINARYVERSION, 1L, "MIMETYPEFILTER", "1");
		this.assetCatalogService.addAttribute(EntityType.BINARYVERSION, 1L, "FUNCTIONFILTER", "2");
		this.assetCatalogService.addAttribute(EntityType.BINARYVERSION, 1L, "RESOLUTIONFILTER", 3F);
		this.assetCatalogService.addAttribute(EntityType.BINARYVERSION, 1L, "CPPRIORITY", 7F);

		calendar.add(Calendar.HOUR, -1);
		this.assetCatalogService.addAttribute(EntityType.BINARYVERSION, 2L, "MIMETYPEFILTER", "1");
		this.assetCatalogService.addAttribute(EntityType.BINARYVERSION, 2L, "FUNCTIONFILTER", "2");
		this.assetCatalogService.addAttribute(EntityType.BINARYVERSION, 2L, "RESOLUTIONFILTER", 2F);
		this.assetCatalogService.addAttribute(EntityType.BINARYVERSION, 2L, "GETIMAGEDATE", calendar.getTime());
		this.assetCatalogService.addAttribute(EntityType.BINARYVERSION, 2L, "CPPRIORITY", 2F);

		this.assetCatalogService.addAttribute(EntityType.BINARYVERSION, 3L, "MIMETYPEFILTER", "1");
		this.assetCatalogService.addAttribute(EntityType.BINARYVERSION, 3L, "FUNCTIONFILTER", "2");
		this.assetCatalogService.addAttribute(EntityType.BINARYVERSION, 3L, "RESOLUTIONFILTER", 4F);
		this.assetCatalogService.addAttribute(EntityType.BINARYVERSION, 3L, "CPPRIORITY", 3F);

		calendar.add(Calendar.HOUR, -1);
		this.assetCatalogService.addAttribute(EntityType.BINARYVERSION, 4L, "MIMETYPEFILTER", "1");
		this.assetCatalogService.addAttribute(EntityType.BINARYVERSION, 4L, "FUNCTIONFILTER", "2");
		this.assetCatalogService.addAttribute(EntityType.BINARYVERSION, 4L, "RESOLUTIONFILTER", 1F);
		this.assetCatalogService.addAttribute(EntityType.BINARYVERSION, 4L, "GETIMAGEDATE", calendar.getTime());
		this.assetCatalogService.addAttribute(EntityType.BINARYVERSION, 4L, "CPPRIORITY", 4F);

		EavStringConditionDescriptor eavMimeTypeFilter = new EavStringConditionDescriptor(EntityType.BINARYVERSION, "MIMETYPEFILTER", "1",
				StringComparer.EQUAL, false, false);
		EavStringConditionDescriptor eavFunctionFilter = new EavStringConditionDescriptor(EntityType.BINARYVERSION, "FUNCTIONFILTER", "2",
				StringComparer.EQUAL, false, false);
		EavNumberConditionDescriptor<Float> eavNumberDesc = new EavNumberConditionDescriptor<Float>(EntityType.BINARYVERSION, "RESOLUTIONFILTER", 3F,
				NumberComparer.LESS_EQUAL);

		EavDateConditionDescriptor eavDateDesc = new EavDateConditionDescriptor(EntityType.BINARYVERSION, "GETIMAGEDATE", new Date(),
				DateComparer.LESS_THAN);

		SearchExpression searchExpression = searchEngine.createSearchExpression();
		searchExpression.addCondition(searchEngine.createCondition(eavMimeTypeFilter));
		searchExpression.addCondition(searchEngine.createCondition(eavFunctionFilter));
		searchExpression.addCondition(searchEngine.createCondition(eavNumberDesc));
		searchExpression.addOrder(BinaryVersionOrderByExt.ASSETPROVIDERPRIORITY, OrderEnum.DESC);
		List<BinaryVersion> binaryVersions = this.assetCatalogService.searchBinaryVersions(searchExpression);

		Assert.assertTrue(binaryVersions.size() == 3);
		Assert.assertTrue(binaryVersions.get(0).getId() == 1L);
		Assert.assertTrue(binaryVersions.get(1).getId() == 4L);
		Assert.assertTrue(binaryVersions.get(2).getId() == 2L);

		searchExpression = searchEngine.createSearchExpression();
		searchExpression.addCondition(searchEngine.createCondition(eavMimeTypeFilter));
		searchExpression.addCondition(searchEngine.createCondition(eavFunctionFilter));
		searchExpression.addCondition(searchEngine.createCondition(eavNumberDesc));
		searchExpression.addCondition(searchEngine.createCondition(eavDateDesc));
		searchExpression.addOrder(BinaryVersionOrderByExt.GETIMAGEDATE, OrderEnum.ASC);
		binaryVersions = this.assetCatalogService.searchBinaryVersions(searchExpression);
		Assert.assertTrue(binaryVersions.size() == 2);
		Assert.assertTrue(binaryVersions.get(0).getId() == 4L);

		assetCatalogService.addAssetIntoCategory(binaryVersions.get(0).getAssetId(), binaryVersions.get(0).getId(), 1L);
		assetCatalogService.addAssetIntoCategory(binaryVersions.get(1).getAssetId(), binaryVersions.get(1).getId(), 2L);

		AssetBinaryVersionCategoryIdCondition categoryIdCondition = new AssetBinaryVersionCategoryIdCondition(2L, NumberComparer.EQUAL);

		searchExpression = searchEngine.createSearchExpression();
		searchExpression.addCondition(searchEngine.createCondition(eavMimeTypeFilter));
		searchExpression.addCondition(searchEngine.createCondition(eavFunctionFilter));
		searchExpression.addCondition(searchEngine.createCondition(eavNumberDesc));
		searchExpression.addCondition(searchEngine.createCondition(eavDateDesc));
		searchExpression.addOrder(BinaryVersionOrderByExt.GETIMAGEDATE, OrderEnum.ASC);
		searchExpression.addCondition(categoryIdCondition);

		binaryVersions = this.assetCatalogService.searchBinaryVersions(searchExpression);

		Assert.assertTrue(binaryVersions.size() == 1);
		Assert.assertTrue(binaryVersions.get(0).getId() == 2L);

	}

	@Test
	public void testComplexSearch1() throws Exception {
		clearEAVAttribute();

		Calendar calendar = Calendar.getInstance();

		this.assetCatalogService.addAttribute(EntityType.BINARYVERSION, 1L, "MIMETYPEFILTER", "1");
		this.assetCatalogService.addAttribute(EntityType.BINARYVERSION, 1L, "FUNCTIONFILTER", "2");
		this.assetCatalogService.addAttribute(EntityType.BINARYVERSION, 1L, "RESOLUTIONFILTER", 3F);
		this.assetCatalogService.addAttribute(EntityType.BINARYVERSION, 1L, "CPPRIORITY", 7F);

		calendar.add(Calendar.HOUR, -1);
		this.assetCatalogService.addAttribute(EntityType.BINARYVERSION, 2L, "MIMETYPEFILTER", "1");
		this.assetCatalogService.addAttribute(EntityType.BINARYVERSION, 2L, "FUNCTIONFILTER", "2");
		this.assetCatalogService.addAttribute(EntityType.BINARYVERSION, 2L, "RESOLUTIONFILTER", 2F);
		this.assetCatalogService.addAttribute(EntityType.BINARYVERSION, 2L, "GETIMAGEDATE", calendar.getTime());
		this.assetCatalogService.addAttribute(EntityType.BINARYVERSION, 2L, "CPPRIORITY", 2F);

		this.assetCatalogService.addAttribute(EntityType.BINARYVERSION, 3L, "MIMETYPEFILTER", "3");
		this.assetCatalogService.addAttribute(EntityType.BINARYVERSION, 3L, "FUNCTIONFILTER", "4");
		this.assetCatalogService.addAttribute(EntityType.BINARYVERSION, 3L, "RESOLUTIONFILTER", 3F);
		this.assetCatalogService.addAttribute(EntityType.BINARYVERSION, 3L, "CPPRIORITY", 3F);

		calendar.add(Calendar.HOUR, -1);
		this.assetCatalogService.addAttribute(EntityType.BINARYVERSION, 4L, "MIMETYPEFILTER", "3");
		this.assetCatalogService.addAttribute(EntityType.BINARYVERSION, 4L, "FUNCTIONFILTER", "4");
		this.assetCatalogService.addAttribute(EntityType.BINARYVERSION, 4L, "RESOLUTIONFILTER", 5F);
		this.assetCatalogService.addAttribute(EntityType.BINARYVERSION, 4L, "GETIMAGEDATE", calendar.getTime());
		this.assetCatalogService.addAttribute(EntityType.BINARYVERSION, 4L, "CPPRIORITY", 4F);

		EavStringConditionDescriptor eavMimeTypeFilter = new EavStringConditionDescriptor(EntityType.BINARYVERSION, "MIMETYPEFILTER", "1",
				StringComparer.EQUAL, false, false);
		EavStringConditionDescriptor eavFunctionFilter = new EavStringConditionDescriptor(EntityType.BINARYVERSION, "FUNCTIONFILTER", "2",
				StringComparer.EQUAL, false, false);
		EavNumberConditionDescriptor<Float> eavNumberDesc = new EavNumberConditionDescriptor<Float>(EntityType.BINARYVERSION, "RESOLUTIONFILTER", 3F,
				NumberComparer.GREAT_THAN);

		EavNumberConditionDescriptor<Float> eavCPPRIORITYDesc = new EavNumberConditionDescriptor<Float>(EntityType.BINARYVERSION, "CPPRIORITY", 3F,
				NumberComparer.GREAT_THAN);

		SearchExpression searchExpression = searchEngine.createSearchExpression();
		searchExpression.addCondition(searchEngine.createCondition(eavMimeTypeFilter).and(searchEngine.createCondition(eavFunctionFilter)).or(
				searchEngine.createCondition(eavNumberDesc)));

		searchExpression.addOrder(BinaryVersionOrderByExt.ASSETPROVIDERPRIORITY, OrderEnum.DESC);
		List<BinaryVersion> binaryVersions = this.assetCatalogService.searchBinaryVersions(searchExpression);

		Assert.assertTrue(binaryVersions.size() == 3);
		Assert.assertTrue(binaryVersions.get(0).getId() == 1L);
		Assert.assertTrue(binaryVersions.get(1).getId() == 4L);
		Assert.assertTrue(binaryVersions.get(2).getId() == 2L);

		searchExpression = searchEngine.createSearchExpression();
		searchExpression.addCondition(searchEngine.createCondition(eavMimeTypeFilter).and(searchEngine.createCondition(eavFunctionFilter)).or(
				searchEngine.createCondition(eavNumberDesc)));
		searchExpression.addCondition(searchEngine.createCondition(eavCPPRIORITYDesc));
		searchExpression.addOrder(BinaryVersionOrderByExt.ASSETPROVIDERPRIORITY, OrderEnum.DESC);
		binaryVersions = this.assetCatalogService.searchBinaryVersions(searchExpression);

		Assert.assertTrue(binaryVersions.size() == 2);
		Assert.assertTrue(binaryVersions.get(0).getId() == 1L);
		Assert.assertTrue(binaryVersions.get(1).getId() == 4L);

		assetCatalogService.addAssetIntoCategory(binaryVersions.get(0).getAssetId(), binaryVersions.get(0).getId(), 1L);
		assetCatalogService.addAssetIntoCategory(binaryVersions.get(1).getAssetId(), binaryVersions.get(1).getId(), 2L);

		AssetBinaryVersionCategoryIdCondition categoryIdCondition = new AssetBinaryVersionCategoryIdCondition(2L, NumberComparer.EQUAL);

		searchExpression = searchEngine.createSearchExpression();
		searchExpression.addCondition(searchEngine.createCondition(eavMimeTypeFilter).and(searchEngine.createCondition(eavFunctionFilter)).or(
				searchEngine.createCondition(eavNumberDesc)));
		searchExpression.addCondition(categoryIdCondition);

		binaryVersions = this.assetCatalogService.searchBinaryVersions(searchExpression);

		Assert.assertTrue(binaryVersions.size() == 1);
		Assert.assertTrue(binaryVersions.get(0).getId() == 4L);

		searchExpression = searchEngine.createSearchExpression();
		searchExpression.addCondition(categoryIdCondition.or(searchEngine.createCondition(eavMimeTypeFilter)));
		binaryVersions = this.assetCatalogService.searchBinaryVersions(searchExpression);
		Assert.assertTrue(binaryVersions.size() == 3);

	}

	@Test
	public void testOrderBy() throws Exception {
		clearEAVAttribute();

		Calendar calendar = Calendar.getInstance();

		this.assetCatalogService.addAttribute(EntityType.BINARYVERSION, 1L, "MIMETYPEFILTER", "1");
		this.assetCatalogService.addAttribute(EntityType.BINARYVERSION, 1L, "FUNCTIONFILTER", "2");
		this.assetCatalogService.addAttribute(EntityType.BINARYVERSION, 1L, "RESOLUTIONFILTER", 3F);
		this.assetCatalogService.addAttribute(EntityType.BINARYVERSION, 1L, "CPPRIORITY", 7F);
		this.assetCatalogService.addAttribute(EntityType.BINARYVERSION, 1L, "DISPLAYORDER", 1F);

		calendar.add(Calendar.HOUR, -1);
		this.assetCatalogService.addAttribute(EntityType.BINARYVERSION, 2L, "MIMETYPEFILTER", "1");
		this.assetCatalogService.addAttribute(EntityType.BINARYVERSION, 2L, "FUNCTIONFILTER", "2");
		this.assetCatalogService.addAttribute(EntityType.BINARYVERSION, 2L, "RESOLUTIONFILTER", 2F);
		this.assetCatalogService.addAttribute(EntityType.BINARYVERSION, 2L, "GETIMAGEDATE", calendar.getTime());
		this.assetCatalogService.addAttribute(EntityType.BINARYVERSION, 2L, "CPPRIORITY", 2F);
		this.assetCatalogService.addAttribute(EntityType.BINARYVERSION, 2L, "DISPLAYORDER", 2F);

		this.assetCatalogService.addAttribute(EntityType.BINARYVERSION, 3L, "MIMETYPEFILTER", "1");
		this.assetCatalogService.addAttribute(EntityType.BINARYVERSION, 3L, "FUNCTIONFILTER", "2");
		this.assetCatalogService.addAttribute(EntityType.BINARYVERSION, 3L, "RESOLUTIONFILTER", 3F);
		this.assetCatalogService.addAttribute(EntityType.BINARYVERSION, 3L, "CPPRIORITY", 3F);
		this.assetCatalogService.addAttribute(EntityType.BINARYVERSION, 3L, "DISPLAYORDER", 1F);

		calendar.add(Calendar.HOUR, -1);
		this.assetCatalogService.addAttribute(EntityType.BINARYVERSION, 4L, "MIMETYPEFILTER", "3");
		this.assetCatalogService.addAttribute(EntityType.BINARYVERSION, 4L, "FUNCTIONFILTER", "4");
		this.assetCatalogService.addAttribute(EntityType.BINARYVERSION, 4L, "RESOLUTIONFILTER", 5F);
		this.assetCatalogService.addAttribute(EntityType.BINARYVERSION, 4L, "GETIMAGEDATE", calendar.getTime());
		this.assetCatalogService.addAttribute(EntityType.BINARYVERSION, 4L, "CPPRIORITY", 4F);
		this.assetCatalogService.addAttribute(EntityType.BINARYVERSION, 4L, "DISPLAYORDER", 2F);

		EavStringConditionDescriptor eavMimeTypeFilter = new EavStringConditionDescriptor(EntityType.BINARYVERSION, "MIMETYPEFILTER", "1",
				StringComparer.EQUAL, false, false);
		EavStringConditionDescriptor eavFunctionFilter = new EavStringConditionDescriptor(EntityType.BINARYVERSION, "FUNCTIONFILTER", "2",
				StringComparer.EQUAL, false, false);

		SearchExpression searchExpression = searchEngine.createSearchExpression();

		searchExpression.addOrder(BinaryVersionOrderByExt.ASSETPROVIDERPRIORITY, OrderEnum.DESC);
		
		long count=this.assetCatalogService.getBinaryVersionSearchResultCount(searchExpression);
		Assert.assertTrue(count == 4);
		List<BinaryVersion> binaryVersions = this.assetCatalogService.searchBinaryVersions(searchExpression);

		Assert.assertTrue(binaryVersions.size() == 4);
		Assert.assertTrue(binaryVersions.get(0).getId() == 1L );
		Assert.assertTrue(binaryVersions.get(1).getId() == 4L );
		Assert.assertTrue(binaryVersions.get(2).getId() == 3L );

		Assert.assertTrue(binaryVersions.get(3).getId() == 2L );


		searchExpression = searchEngine.createSearchExpression();

		searchExpression.addOrder(BinaryVersionOrderByExt.SERIAL, OrderEnum.DESC);
		count=this.assetCatalogService.getBinaryVersionSearchResultCount(searchExpression);
		Assert.assertTrue(count == 4);
		 binaryVersions = this.assetCatalogService.searchBinaryVersions(searchExpression);

		Assert.assertTrue(binaryVersions.size() == 4);

		Assert.assertTrue(binaryVersions.get(0).getId() == 2L || binaryVersions.get(0).getId() == 4L);
		Assert.assertTrue(binaryVersions.get(2).getId() == 1L || binaryVersions.get(2).getId() == 3L);


		searchExpression = searchEngine.createSearchExpression();
		searchExpression.addCondition(searchEngine.createCondition(eavMimeTypeFilter).and(searchEngine.createCondition(eavFunctionFilter)));

		searchExpression.addOrder(BinaryVersionOrderByExt.SERIAL, OrderEnum.DESC);
		searchExpression.addOrder(BinaryVersionOrderByExt.ASSETPROVIDERPRIORITY, OrderEnum.DESC);
		count=this.assetCatalogService.getBinaryVersionSearchResultCount(searchExpression);
		Assert.assertTrue(count == 3);
		 binaryVersions = this.assetCatalogService.searchBinaryVersions(searchExpression);

		Assert.assertTrue(binaryVersions.size() == 3);
		Assert.assertTrue(binaryVersions.get(0).getId() == 2L);
		Assert.assertTrue(binaryVersions.get(1).getId() == 1L);
		Assert.assertTrue(binaryVersions.get(2).getId() == 3L);

	}

	private void clearEAVAttribute() throws Exception {
		databaseTester.getConnection().getConnection().createStatement().execute("delete from entityattribute");

		databaseTester.getConnection().getConnection().createStatement().execute("delete from attributevaluechar");

		databaseTester.getConnection().getConnection().createStatement().execute("delete from attributevaluenumber");

		databaseTester.getConnection().getConnection().createStatement().execute("delete from attributevaluedate");

		databaseTester.getConnection().getConnection().createStatement().execute("delete from assetcategoryrelation");

	}

}

// $Id$