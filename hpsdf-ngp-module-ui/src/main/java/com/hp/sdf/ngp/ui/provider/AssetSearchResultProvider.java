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
import com.hp.sdf.ngp.api.search.Condition.StringComparer;
import com.hp.sdf.ngp.api.search.orderby.AssetOrderBy;
import com.hp.sdf.ngp.model.Asset;
import com.hp.sdf.ngp.search.condition.asset.AssetAssetCategoryIdCondition;
import com.hp.sdf.ngp.search.condition.asset.AssetNameCondition;
import com.hp.sdf.ngp.search.condition.asset.AssetPlatformIdCondition;
import com.hp.sdf.ngp.search.condition.asset.AssetStatusIdCondition;
import com.hp.sdf.ngp.search.engine.SearchExpressionImpl;
import com.hp.sdf.ngp.service.ApplicationService;
import com.hp.sdf.ngp.ui.common.SearchCondition;

public class AssetSearchResultProvider implements IDataProvider<Asset> {

	private static final long serialVersionUID = -7901372154709286262L;

	private ApplicationService applicationService;

	private SearchCondition searchCondition;

	public AssetSearchResultProvider(ApplicationService applicationService, SearchCondition searchCondition) {
		this.applicationService = applicationService;
		this.searchCondition = searchCondition;
	}

	public SearchExpression getCondition() {
		SearchExpression searchExpression = new SearchExpressionImpl();
		if (searchCondition != null) {
			if (searchCondition.getKeyword() != null) {
				searchExpression.addCondition(new AssetNameCondition(searchCondition.getKeyword(), StringComparer.LIKE, true, false));
			}
			if (searchCondition.getPlatformId() != null) {
				searchExpression.addCondition(new AssetPlatformIdCondition(searchCondition.getPlatformId(), NumberComparer.EQUAL));
			}
			if (searchCondition.getCategoryId() != null) {
				searchExpression.addCondition(new AssetAssetCategoryIdCondition(searchCondition.getCategoryId(), NumberComparer.EQUAL));
			}
			if (searchCondition.getStatusId() != null) {
				searchExpression.addCondition(new AssetStatusIdCondition(searchCondition.getStatusId(), NumberComparer.EQUAL));
			}
		}
		return searchExpression;
	}

	public Iterator<? extends Asset> iterator(int first, int count) {
		SearchExpression searchExpression = getCondition();
		searchExpression.setFirst(first);
		searchExpression.setMax(count);
		searchExpression.addOrder(AssetOrderBy.ID, OrderEnum.ASC);
		List<Asset> list = applicationService.searchAsset(searchExpression);

		if (list != null) {
			return list.iterator();
		}

		return null;
	}

	public IModel<Asset> model(Asset object) {
		return new Model<Asset>(object);
	}

	public int size() {
		SearchExpression searchExpression = getCondition();
		searchExpression.setFirst(0);
		searchExpression.setMax(Integer.MAX_VALUE);
		return (int) applicationService.searchAssetPageCount(searchExpression);
	}

	public void detach() {
	}
}
