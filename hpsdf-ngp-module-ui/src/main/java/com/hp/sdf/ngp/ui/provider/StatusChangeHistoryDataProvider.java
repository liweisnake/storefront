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

import java.util.Iterator;
import java.util.List;

import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import com.hp.sdf.ngp.api.search.OrderEnum;
import com.hp.sdf.ngp.api.search.SearchExpression;
import com.hp.sdf.ngp.api.search.Condition.NumberComparer;
import com.hp.sdf.ngp.api.search.orderby.LifecycleActionHistoryOrderBy;
import com.hp.sdf.ngp.model.AssetLifecycleActionHistory;
import com.hp.sdf.ngp.search.condition.assetlifecycleactionhistory.AssetLifecycleActionHistoryAssetIdCondition;
import com.hp.sdf.ngp.search.engine.SearchExpressionImpl;
import com.hp.sdf.ngp.service.ApplicationService;

public class StatusChangeHistoryDataProvider implements IDataProvider<AssetLifecycleActionHistory> {

	private static final long serialVersionUID = -6829478296346665192L;

	private ApplicationService applicationService;

	private Long assetId;

	public StatusChangeHistoryDataProvider(ApplicationService applicationService, Long assetId) {
		this(applicationService);
		this.assetId = assetId;
	}

	public StatusChangeHistoryDataProvider(ApplicationService applicationService) {
		this.applicationService = applicationService;
	}

	public Iterator<AssetLifecycleActionHistory> iterator(int first, int count) {
		SearchExpression searchExpression = new SearchExpressionImpl();
		if (assetId != null)
			searchExpression.addCondition(new AssetLifecycleActionHistoryAssetIdCondition(assetId, NumberComparer.EQUAL));
		searchExpression.setFirst(first);
		searchExpression.setMax(count);
		searchExpression.addOrder(LifecycleActionHistoryOrderBy.CREATEDATE, OrderEnum.ASC);
		List<AssetLifecycleActionHistory> list = applicationService.searchAssetLifecycleActionHistory(searchExpression);

		if (list != null)
			return list.iterator();

		return null;
	}

	public IModel<AssetLifecycleActionHistory> model(AssetLifecycleActionHistory object) {
		return new Model<AssetLifecycleActionHistory>(object);
	}

	public int size() {
		SearchExpression searchExpression = new SearchExpressionImpl();
		if (assetId != null)
			searchExpression.addCondition(new AssetLifecycleActionHistoryAssetIdCondition(assetId, NumberComparer.EQUAL));
		searchExpression.setFirst(0);
		searchExpression.setMax(Integer.MAX_VALUE);
		return applicationService.searchAssetLifecycleActionHistoryCount(searchExpression);
	}

	public void detach() {
	}

	public Long getAssetId() {
		return assetId;
	}

	public void setAssetId(Long assetId) {
		this.assetId = assetId;
	}
}