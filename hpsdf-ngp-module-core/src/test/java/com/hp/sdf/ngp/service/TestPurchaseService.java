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

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Currency;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import junit.framework.Assert;

import org.dbunit.dataset.ITable;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.hp.sdf.ngp.DBEnablerTestBase;
import com.hp.sdf.ngp.api.search.SearchExpression;
import com.hp.sdf.ngp.api.search.Condition.StringComparer;
import com.hp.sdf.ngp.common.constant.AssetSubscriptionStatus;
import com.hp.sdf.ngp.model.Asset;
import com.hp.sdf.ngp.model.AssetPrice;
import com.hp.sdf.ngp.model.PurchaseHistory;
import com.hp.sdf.ngp.model.ShoppingCart;
import com.hp.sdf.ngp.search.condition.purchasehistory.PurchaseHistoryAssetNameCondition;
import com.hp.sdf.ngp.search.condition.purchasehistory.PurchaseHistoryStatusCondition;
import com.hp.sdf.ngp.search.condition.purchasehistory.PurchaseHistoryUseridCondition;
import com.hp.sdf.ngp.search.engine.SearchExpressionImpl;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:META-INF/applicationContext-test.xml" })
public class TestPurchaseService extends DBEnablerTestBase {

	@Resource
	PurchaseService purchaseService;

	@Resource
	ApplicationService applicationService;

	@Override
	public String dataSetFileName() {
		return "/data_init.xml";
	}

	@Test
	public void testGetSubscription() throws Exception {
		ShoppingCart subscription = purchaseService.getShoppingCart(1l);
		Assert.assertEquals("incart", subscription.getStatus());
	}

	@Test
	public void testAddAppToShoppingCart() throws Exception {
		ShoppingCart subscription, newsub;
		Asset app = applicationService.getAsset(2l);
		AssetPrice assetPrice = applicationService.getAssetPriceById(2l);

		subscription = purchaseService.addToShoppingCart("you", app.getId(),
				Currency.getInstance(assetPrice.getCurrency()), assetPrice
						.getAmount());
		newsub = purchaseService.getShoppingCart(subscription.getId());

		Assert.assertEquals(9.9d, newsub.getItemPrice().doubleValue());
		Assert.assertEquals("incart", newsub.getStatus());
		// Assert.assertNotNull(newsub.getCreateDate());
	}

	@Test
	public void testRemoveFromShoppingCart() throws Exception {
		List<ShoppingCart> list = purchaseService
				.getShoppingCartSubscriptions(
						"levi",
						new AssetSubscriptionStatus[] { AssetSubscriptionStatus.INCART });
		Assert.assertEquals(1, list.size());

		purchaseService.removeFromShoppingCart(list);

		list = purchaseService
				.getShoppingCartSubscriptions(
						"levi",
						new AssetSubscriptionStatus[] { AssetSubscriptionStatus.INCART });
		Assert.assertEquals(0, list.size());
	}

	@Test
	public void testCalculateBillAmount() throws Exception {
		List<ShoppingCart> list = purchaseService.getShoppingCartSubscriptions(
				"levi", null);
		Assert.assertEquals(2, list.size());

		double amount = purchaseService.calculateBillAmount(list);
		Assert.assertEquals(28.0, amount);
	}

	@Test
	public void testGetPurchaseHistory() throws Exception {
		List<PurchaseHistory> list = purchaseService
				.getPurchaseHistorySubscriptions("levi", null);
		Assert.assertEquals(2, list.size());
	}

	@Test
	public void testListSubscriptions() throws Exception {
		List<ShoppingCart> list = purchaseService
				.getShoppingCartSubscriptions(
						"levi",
						new AssetSubscriptionStatus[] { AssetSubscriptionStatus.INCART });
		Assert.assertEquals(1, list.size());
	}

	@Test
	public void testCompletePurchase() throws Exception {
		ShoppingCart subscription = purchaseService.getShoppingCart(1l);
		List<ShoppingCart> list = new ArrayList<ShoppingCart>();
		list.add(subscription);
		purchaseService.completePurchase(list);

		PurchaseHistory purchaseHistory = purchaseService
				.getPurchaseHistory(4l);
		Assert.assertEquals(AssetSubscriptionStatus.PURCHASED.toString(),
				purchaseHistory.getStatus());
		Assert.assertNotNull(purchaseHistory.getPurchaseDate());

		ITable tableValue = databaseTester.getConnection().createQueryTable(
				"result", "select * from shoppingcart where id=1");

		Assert.assertEquals(0, tableValue.getRowCount());

	}

	@Test
	public void testListAllSubscriptions() throws Exception {

		SearchExpression searchExpression = new SearchExpressionImpl();
		searchExpression.addCondition(new PurchaseHistoryUseridCondition(
				"levi", StringComparer.EQUAL, false, false));
		searchExpression.addCondition(new PurchaseHistoryStatusCondition(
				"purchased", StringComparer.EQUAL, false, false));
		searchExpression.addCondition(new PurchaseHistoryAssetNameCondition(
				"APP1", StringComparer.EQUAL, false, false));

		List<PurchaseHistory> list = purchaseService
				.searchPurchaseHistory(searchExpression);
		Assert.assertEquals(1, list.size());
	}
	
	@Test
	public void testCalculateAssetPrice() throws Exception{
		Date startDate=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse("2009-07-15 11:00:00");
		Date endDate=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse("2009-07-15 13:00:00");
		BigDecimal result=purchaseService.calculateAssetPrice(2L, startDate, endDate);
		Assert.assertEquals(19D, result.doubleValue());
		
		result=purchaseService.calculateAssetPrice(2L, startDate, null);
		Assert.assertEquals(37D, result.doubleValue());
		
		result=purchaseService.calculateAssetPrice(2L, null, endDate);
		Assert.assertEquals(19D, result.doubleValue());
	}
}

// $Id$