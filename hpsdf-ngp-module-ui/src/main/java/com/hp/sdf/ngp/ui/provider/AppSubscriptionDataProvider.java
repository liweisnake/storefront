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
package com.hp.sdf.ngp.ui.provider;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import com.hp.sdf.ngp.api.search.SearchExpression;
import com.hp.sdf.ngp.api.search.Condition.DateComparer;
import com.hp.sdf.ngp.api.search.Condition.StringComparer;
import com.hp.sdf.ngp.model.PurchaseHistory;
import com.hp.sdf.ngp.search.condition.purchasehistory.PurchaseHistoryAssetNameCondition;
import com.hp.sdf.ngp.search.condition.purchasehistory.PurchaseHistoryPurchaseDateCondition;
import com.hp.sdf.ngp.search.condition.purchasehistory.PurchaseHistoryStatusCondition;
import com.hp.sdf.ngp.search.condition.purchasehistory.PurchaseHistoryUseridCondition;
import com.hp.sdf.ngp.search.engine.SearchExpressionImpl;
import com.hp.sdf.ngp.service.PurchaseService;
import com.hp.sdf.ngp.ui.page.purchase.SearchCondition;

public class AppSubscriptionDataProvider implements IDataProvider<PurchaseHistory> {

	private final static Log log = LogFactory.getLog(AppSubscriptionDataProvider.class);

	private static final long serialVersionUID = 1L;

	private PurchaseService purchaseService;

	private SearchCondition searchCondition = null;

	public SearchCondition getSearchCondition() {
		return searchCondition;
	}

	public void setSearchCondition(SearchCondition searchCondition) {
		this.searchCondition = searchCondition;
	}

	public PurchaseService getPurchaseService() {
		return purchaseService;
	}

	public void setPurchaseService(PurchaseService purchaseService) {
		this.purchaseService = purchaseService;
	}

	public AppSubscriptionDataProvider(PurchaseService purchaseService, SearchCondition searchCondition) {
		this.purchaseService = purchaseService;
		this.searchCondition = searchCondition;
	}

	public Iterator<? extends PurchaseHistory> iterator(int first, int count) {
		if (this.searchCondition == null)
			return new ArrayList<PurchaseHistory>().iterator();

		SearchExpression searchExpression = listSubscriptions(searchCondition);
		searchExpression.setFirst(first);
		searchExpression.setMax(count);

		List<PurchaseHistory> list = purchaseService.searchPurchaseHistory(searchExpression);

		if (list != null)
			return list.iterator();
		return null;
	}

	public IModel<PurchaseHistory> model(PurchaseHistory object) {
		return new Model<PurchaseHistory>(object);
	}

	public int size() {
		SearchExpression searchExpression = listSubscriptions(searchCondition);
		searchExpression.setFirst(0);
		searchExpression.setMax(Integer.MAX_VALUE);

		return purchaseService.searchPurchaseHistoryCount(searchExpression);
	}

	public void detach() {

	}

	private SearchExpression listSubscriptions(SearchCondition condition) {

		SearchExpression searchExpression = new SearchExpressionImpl();

		if (condition != null) {
			if (condition.getKeyword() != null) {
				searchExpression.addCondition(new PurchaseHistoryAssetNameCondition(condition.getKeyword(), StringComparer.LIKE, true, false));
			}
			if (condition.getUserId() != null) {
				searchExpression.addCondition(new PurchaseHistoryUseridCondition(condition.getUserId(), StringComparer.EQUAL, false, false));
			}
			if (condition.getStatus() != null) {
				searchExpression.addCondition(new PurchaseHistoryStatusCondition(condition.getStatus(), StringComparer.EQUAL, false, false));
			}
			if (condition.getStartDate() != null) {
				searchExpression.addCondition(new PurchaseHistoryPurchaseDateCondition(condition.getStartDate(), DateComparer.GREAT_THAN));
			}
			if (condition.getEndDate() != null) {
				searchExpression.addCondition(new PurchaseHistoryPurchaseDateCondition(condition.getEndDate(), DateComparer.LESS_THAN));
			}
		}
		return searchExpression;
	}

}