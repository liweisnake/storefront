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

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import com.hp.sdf.ngp.api.search.OrderEnum;
import com.hp.sdf.ngp.api.search.SearchExpression;
import com.hp.sdf.ngp.api.search.Condition.DateComparer;
import com.hp.sdf.ngp.api.search.Condition.StringComparer;
import com.hp.sdf.ngp.api.search.orderby.AssetOrderBy;
import com.hp.sdf.ngp.model.Asset;
import com.hp.sdf.ngp.search.condition.asset.AssetNameCondition;
import com.hp.sdf.ngp.search.condition.asset.AssetPurchaseHistoryPurchaseDateCondition;
import com.hp.sdf.ngp.search.engine.SearchExpressionImpl;
import com.hp.sdf.ngp.service.ApplicationService;
import com.hp.sdf.ngp.ui.common.TopSalesCondition;

public class TopSalesDataProvider implements IDataProvider<Asset> {

	private static final long serialVersionUID = -6829478296346665192L;

	private static Log log = LogFactory.getLog(TopSalesDataProvider.class);

	private ApplicationService applicationService;

	private TopSalesCondition condition;

	public TopSalesDataProvider(ApplicationService applicationService, TopSalesCondition condition) {
		this(applicationService);
		this.condition = condition;
	}

	public TopSalesDataProvider(ApplicationService applicationService) {
		this.applicationService = applicationService;
	}

	public Iterator<Asset> iterator(int first, int count) {
		SearchExpression searchExpression = genCondition();
		searchExpression.setFirst(first);
		searchExpression.setMax(count);

		List<Asset> list = applicationService.searchAsset(searchExpression);

		if (list != null)
			return list.iterator();

		return null;
	}

	public IModel<Asset> model(Asset object) {
		return new Model<Asset>(object);
	}

	public int size() {
		SearchExpression searchExpression = genCondition();
		searchExpression.setFirst(0);
		searchExpression.setMax(Integer.MAX_VALUE);
		System.out.println(applicationService.searchAssetPageCount(searchExpression));
		return (int) applicationService.searchAssetPageCount(searchExpression);
	}

	public void detach() {
	}

	private SearchExpression genCondition() {
		SearchExpression searchExpression = new SearchExpressionImpl();

		if (null == this.condition)
			this.condition = new TopSalesCondition();

		Date beginDate = condition.getBeginDate();
		if (beginDate != null) {
			searchExpression.addCondition(new AssetPurchaseHistoryPurchaseDateCondition(beginDate, DateComparer.GREAT_THAN));
		}
		Date endDate = condition.getEndDate();
		if (endDate != null) {
			searchExpression.addCondition(new AssetPurchaseHistoryPurchaseDateCondition(endDate, DateComparer.LESS_THAN));
		}
		String name = condition.getAssetName();
		if (name != null) {
			searchExpression.addCondition(new AssetNameCondition(name, StringComparer.LIKE, true, true));
		}

		// Order By
		if (condition.getOrderBy() != null) {
			switch (condition.getOrderBy()) {
			case 2:
				searchExpression.addOrder(AssetOrderBy.CREATEDATE, OrderEnum.ASC);
				break;
			case 3:
				searchExpression.addOrder(AssetOrderBy.RATING, OrderEnum.ASC);
				break;
			case 4:
				searchExpression.addOrder(AssetOrderBy.DOWNLOADCOUNT, OrderEnum.ASC);
				break;
			default:
				searchExpression.addOrder(AssetOrderBy.NAME, OrderEnum.ASC);
				break;
			}
		}

		return searchExpression;
	}

	public TopSalesCondition getCondition() {
		return condition;
	}

	public void setCondition(TopSalesCondition condition) {
		this.condition = condition;
	}
}