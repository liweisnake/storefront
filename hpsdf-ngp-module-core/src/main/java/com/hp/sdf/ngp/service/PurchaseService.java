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
import java.util.Currency;
import java.util.Date;
import java.util.List;

import com.hp.sdf.ngp.api.search.SearchExpression;
import com.hp.sdf.ngp.common.constant.AssetSubscriptionStatus;
import com.hp.sdf.ngp.common.exception.NgpException;
import com.hp.sdf.ngp.model.PurchaseHistory;
import com.hp.sdf.ngp.model.ShoppingCart;

public interface PurchaseService {

	/**
	 * Gets subscription by its ID
	 * 
	 * @param subscriptionId
	 * @return
	 */
	public ShoppingCart getShoppingCart(Long id) ;

	/**
	 * 
	 * @param userId
	 * @param app
	 * @return
	 * @throws NgpException
	 */
	public ShoppingCart addToShoppingCart(String userId, long assetId,
			Currency currency, BigDecimal amount) ;

	/**
	 * Removes subscriptions from the shopping cart.
	 * 
	 * @param subscriptionList
	 */
	public void removeFromShoppingCart(List<ShoppingCart> subscriptionList);

	public List<ShoppingCart> getShoppingCartSubscriptions(String userId,
			AssetSubscriptionStatus[] statuses);

	public List<PurchaseHistory> getPurchaseHistorySubscriptions(String userId,
			AssetSubscriptionStatus[] statuses);

	public List<PurchaseHistory> searchPurchaseHistory(
			SearchExpression searchExpression);
	
	public int searchPurchaseHistoryCount(
			SearchExpression searchExpression);

	/**
	 * Marks if an asset is successfully purchased.
	 * 
	 * @param appSubscription
	 */
	public void completePurchase(List<ShoppingCart> subscriptionList);

	/**
	 * Calculate bill amount
	 * 
	 * @param subsriptionList
	 * @return
	 */
	public double calculateBillAmount(List<ShoppingCart> subscriptionList)
			throws NgpException;

	/**
	 * Gets pruchase history
	 * 
	 * @param userId
	 * @return
	 */
	public List<PurchaseHistory> getPurchaseHistory(String userId, int start,
			int count) throws NgpException;

	public long getMyPurchasedAssetCount(String userId);

	public List<ShoppingCart> getShoppingCartHistory(String userId, int start,
			int count);

	public PurchaseHistory getPurchaseHistory(Long id) throws NgpException;
	
	public BigDecimal calculateAssetPrice(Long assetId, Date startDate, Date endDate);
}

// $Id$