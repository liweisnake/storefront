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
package com.hp.sdf.ngp.service.impl;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.hp.sdf.ngp.api.search.SearchExpression;
import com.hp.sdf.ngp.api.search.Condition.DateComparer;
import com.hp.sdf.ngp.api.search.Condition.NumberComparer;
import com.hp.sdf.ngp.api.search.Condition.StringComparer;
import com.hp.sdf.ngp.common.constant.AssetSubscriptionStatus;
import com.hp.sdf.ngp.common.exception.NgpException;
import com.hp.sdf.ngp.common.exception.NgpRuntimeException;
import com.hp.sdf.ngp.dao.PurchaseHistoryDAO;
import com.hp.sdf.ngp.dao.ShoppingCartDAO;
import com.hp.sdf.ngp.model.Asset;
import com.hp.sdf.ngp.model.PurchaseHistory;
import com.hp.sdf.ngp.model.ShoppingCart;
import com.hp.sdf.ngp.search.condition.purchasehistory.PurchaseHistoryAssetIdCondition;
import com.hp.sdf.ngp.search.condition.purchasehistory.PurchaseHistoryPurchaseDateCondition;
import com.hp.sdf.ngp.search.condition.purchasehistory.PurchaseHistoryStatusCondition;
import com.hp.sdf.ngp.search.condition.purchasehistory.PurchaseHistoryUseridCondition;
import com.hp.sdf.ngp.search.condition.shoppingcart.ShoppingCartStatusCondition;
import com.hp.sdf.ngp.search.condition.shoppingcart.ShoppingCartUseridCondition;
import com.hp.sdf.ngp.search.engine.SearchExpressionImpl;
import com.hp.sdf.ngp.service.PurchaseService;

@Component
@Transactional
public class PurchaseServiceImpl implements PurchaseService {

	Log log = LogFactory.getLog(PurchaseServiceImpl.class);

	@Resource
	private PurchaseHistoryDAO purchaseHistoryDao;

	@Resource
	private ShoppingCartDAO shoppingCartDao;

	public ShoppingCart addToShoppingCart(String userId, long assetId, Currency currency, BigDecimal amount) {

		String errorMsg = null;

		if (userId == null) {
			errorMsg = "invalid userId";
			log.warn(errorMsg);
			throw new NgpRuntimeException(errorMsg);
		}

		ShoppingCart shoppingCart = new ShoppingCart();

		Asset asset = new Asset();
		asset.setId(assetId);
		shoppingCart.setAsset(asset);
		shoppingCart.setItemPrice(amount);
		shoppingCart.setCurrency(currency.getCurrencyCode());
		shoppingCart.setStatus(AssetSubscriptionStatus.INCART.toString());
		shoppingCart.setUserid(userId);
		shoppingCartDao.persist(shoppingCart);

		return shoppingCart;
	}

	public void removeFromShoppingCart(List<ShoppingCart> subscriptionList) {
		String errorMsg = null;
		if (subscriptionList == null) {
			errorMsg = "invalid subscription list";
			log.error(errorMsg);
			throw new NgpRuntimeException(errorMsg);
		}

		for (ShoppingCart subscription : subscriptionList) {
			subscription = shoppingCartDao.findById(subscription.getId());
			shoppingCartDao.remove(subscription);
		}

	}

	public double calculateBillAmount(List<ShoppingCart> subscriptionList) throws NgpException {
		String msg = null;
		if (subscriptionList == null) {
			msg = "invalid subscription list";
			log.error(msg);
			throw new NgpException(msg);
		}

		double amount = 0.0d;
		for (ShoppingCart subscription : subscriptionList) {
			amount += subscription.getItemPrice().doubleValue();
		}

		return amount;
	}

	public List<PurchaseHistory> getPurchaseHistory(String userId, int start, int count) throws NgpException {
		if (userId == null) {
			throw new NgpException("invalid user ID.");
		}
		SearchExpression expression = new SearchExpressionImpl();
		expression.setFirst(start);
		expression.setMax(count);
		expression.addCondition(new PurchaseHistoryUseridCondition(userId, StringComparer.EQUAL, false, false));
		return purchaseHistoryDao.search(expression);
	}

	public ShoppingCart getShoppingCart(Long subscriptionId) {
		if (subscriptionId == null) {
			throw new NgpRuntimeException("invalid Asset subscription ID.");
		}
		ShoppingCart subscription = shoppingCartDao.findById(subscriptionId);
		return subscription;
	}

	public PurchaseHistory getPurchaseHistory(Long id) throws NgpException {
		if (id == null) {
			throw new NgpException("invalid Asset subscription ID.");
		}

		return purchaseHistoryDao.findById(id);
	}

	public List<ShoppingCart> getShoppingCartSubscriptions(String userId, AssetSubscriptionStatus[] statuses) {
		String msg;
		if (userId == null) {
			msg = "invalid user ID.";
			log.error(msg);
			throw new NgpRuntimeException(msg);
		}

		List<ShoppingCart> subscriptionList = null;

		if (statuses != null) {
			for (AssetSubscriptionStatus status : statuses) {
				SearchExpression expression = new SearchExpressionImpl();
				expression.addCondition(new ShoppingCartUseridCondition(userId, StringComparer.EQUAL, false, false));
				expression.addCondition(new ShoppingCartStatusCondition(status.toString(), StringComparer.EQUAL, false, false));
				List<ShoppingCart> tempList = shoppingCartDao.search(expression);
				if (tempList != null) {
					if (subscriptionList == null) {
						subscriptionList = tempList;
					} else {
						subscriptionList.addAll(tempList);
					}
				}
			}
		} else {
			SearchExpression expression = new SearchExpressionImpl();
			expression.addCondition(new ShoppingCartUseridCondition(userId, StringComparer.EQUAL, false, false));
			subscriptionList = shoppingCartDao.search(expression);
		}

		return subscriptionList;
	}

	public void completePurchase(List<ShoppingCart> subscriptionList) {
		String errorMsg = null;
		if (subscriptionList == null) {
			errorMsg = "invalid subscriptions inputs.";
			log.error(errorMsg);
			throw new NgpRuntimeException(errorMsg);
		}

		for (ShoppingCart subscription : subscriptionList) {
			PurchaseHistory purchaseHistory = new PurchaseHistory();
			purchaseHistory.setStatus(AssetSubscriptionStatus.PURCHASED.toString());
			purchaseHistory.setCurrency(subscription.getCurrency());
			purchaseHistory.setPurchaseDate(new Date());
			purchaseHistory.setAsset(subscription.getAsset());
			purchaseHistory.setUserid(subscription.getUserid());
			purchaseHistory.setPaidPrice(subscription.getItemPrice());
			purchaseHistoryDao.persist(purchaseHistory);
			shoppingCartDao.remove(subscription);
		}

	}

	@Transactional(readOnly = true)
	public List<PurchaseHistory> searchPurchaseHistory(SearchExpression searchExpression) {
		return purchaseHistoryDao.search(searchExpression);
	}

	public long getMyPurchasedAssetCount(String userId) {
		return purchaseHistoryDao.findByCount("userid", userId);
	}

	public List<ShoppingCart> getShoppingCartHistory(String userId, int start, int count) {
		SearchExpression expression = new SearchExpressionImpl();
		expression.setFirst(start);
		expression.setMax(count);
		expression.addCondition(new ShoppingCartUseridCondition(userId, StringComparer.EQUAL, false, false));
		return shoppingCartDao.search(expression);
	}

	public List<PurchaseHistory> getPurchaseHistorySubscriptions(String userId, AssetSubscriptionStatus[] statuses) {

		String msg;
		if (userId == null) {
			msg = "invalid user ID.";
			log.error(msg);
			throw new NgpRuntimeException(msg);
		}

		List<PurchaseHistory> subscriptionList = null;

		if (statuses != null) {
			for (AssetSubscriptionStatus status : statuses) {
				SearchExpression expression = new SearchExpressionImpl();
				expression.addCondition(new PurchaseHistoryUseridCondition(userId, StringComparer.EQUAL, false, false));
				expression.addCondition(new PurchaseHistoryStatusCondition(status.toString(), StringComparer.EQUAL, false, false));
				List<PurchaseHistory> tempList = purchaseHistoryDao.search(expression);
				if (tempList != null) {
					if (subscriptionList == null) {
						subscriptionList = tempList;
					} else {
						subscriptionList.addAll(tempList);
					}
				}
			}
		} else {
			SearchExpression expression = new SearchExpressionImpl();
			expression.addCondition(new PurchaseHistoryUseridCondition(userId, StringComparer.EQUAL, false, false));
			subscriptionList = purchaseHistoryDao.search(expression);
		}

		return subscriptionList;
	}

	@Transactional(readOnly = true)
	public int searchPurchaseHistoryCount(SearchExpression searchExpression) {
		return purchaseHistoryDao.searchCount(searchExpression);
	}

	public BigDecimal calculateAssetPrice(Long assetId, Date startDate, Date endDate) {

		BigDecimal result = new BigDecimal(0);
		SearchExpression expression = new SearchExpressionImpl();
		if (startDate != null) {
			expression.addCondition(new PurchaseHistoryPurchaseDateCondition(startDate, DateComparer.GREAT_THAN));
		}
		if (endDate != null) {
			expression.addCondition(new PurchaseHistoryPurchaseDateCondition(endDate, DateComparer.LESS_THAN));
		}
		expression.addCondition(new PurchaseHistoryAssetIdCondition(assetId, NumberComparer.EQUAL));
		List<PurchaseHistory> purachaseHistorys = purchaseHistoryDao.search(expression);
		if (purachaseHistorys == null || purachaseHistorys.size() == 0) {
			return null;
		}

		for (PurchaseHistory purachaseHistory : purachaseHistorys) {
			if (purachaseHistory.getPaidPrice() != null) {
				result = result.add(purachaseHistory.getPaidPrice());
			}
		}

		return result;

	}

}

// $Id$
