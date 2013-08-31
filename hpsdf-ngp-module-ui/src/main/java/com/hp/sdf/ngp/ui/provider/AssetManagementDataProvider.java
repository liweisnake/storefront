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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import com.hp.sdf.ngp.api.search.OrderEnum;
import com.hp.sdf.ngp.api.search.SearchExpression;
import com.hp.sdf.ngp.api.search.Condition.NumberComparer;
import com.hp.sdf.ngp.api.search.Condition.StringComparer;
import com.hp.sdf.ngp.api.search.orderby.AssetOrderBy;
import com.hp.sdf.ngp.model.Asset;
import com.hp.sdf.ngp.search.condition.asset.AssetIdCondition;
import com.hp.sdf.ngp.search.condition.asset.AssetNameCondition;
import com.hp.sdf.ngp.search.condition.asset.AssetParentIdCondition;
import com.hp.sdf.ngp.search.condition.asset.AssetProviderIdCondition;
import com.hp.sdf.ngp.search.condition.asset.AssetProviderNameCondition;
import com.hp.sdf.ngp.search.condition.asset.AssetStatusIdCondition;
import com.hp.sdf.ngp.search.engine.SearchExpressionImpl;
import com.hp.sdf.ngp.service.ApplicationService;
import com.hp.sdf.ngp.ui.common.AssetManagementCondition;

public class AssetManagementDataProvider implements IDataProvider<Asset> {

	private static final long serialVersionUID = -6829478296346665192L;

	private static Log log = LogFactory.getLog(AssetManagementDataProvider.class);

	private ApplicationService applicationService;

	private AssetManagementCondition condition;

	public AssetManagementDataProvider(ApplicationService applicationService, AssetManagementCondition condition) {
		this(applicationService);
		this.condition = condition;
	}

	public AssetManagementDataProvider(ApplicationService applicationService) {
		this.applicationService = applicationService;
	}

	public Iterator<Asset> iterator(int first, int count) {
		SearchExpression searchExpression = GenCondition();

		searchExpression.setFirst(first);
		searchExpression.setMax(count);

		searchExpression.addOrder(AssetOrderBy.ID, OrderEnum.ASC);

		List<Asset> list = applicationService.searchAsset(searchExpression);

		if (list != null)
			return list.iterator();

		return null;
	}

	public IModel<Asset> model(Asset object) {
		return new Model<Asset>(object);
	}

	public List<Asset> getAll() {

		SearchExpression searchExpression = GenCondition();

		searchExpression.setFirst(0);
		searchExpression.setMax(Integer.MAX_VALUE);

		searchExpression.addOrder(AssetOrderBy.ID, OrderEnum.ASC);

		return applicationService.searchAsset(searchExpression);
	}

	public int size() {

		SearchExpression searchExpression = GenCondition();

		searchExpression.setFirst(0);
		searchExpression.setMax(Integer.MAX_VALUE);

		int result = (int) applicationService.searchAssetPageCount(searchExpression);
		return result;
	}

	public void detach() {
	}

	private SearchExpression GenCondition() {
		SearchExpression searchExpression = new SearchExpressionImpl();

		if (null == this.condition)
			this.condition = new AssetManagementCondition();

		Long providerId = condition.getProviderId();
		if (providerId != null) {
			searchExpression.addCondition(new AssetProviderIdCondition(providerId, NumberComparer.EQUAL));
		}
		String providerName = condition.getProviderName();
		if (providerName != null) {
			searchExpression.addCondition(new AssetProviderNameCondition(providerName, StringComparer.LIKE, true, true));
		}
		Long parentAssetId = condition.getParentAssetId();
		if (parentAssetId != null) {
			searchExpression.addCondition(new AssetParentIdCondition(parentAssetId, NumberComparer.EQUAL));
		}
		Long assetId = condition.getAssetId();
		if (assetId != null) {
			searchExpression.addCondition(new AssetIdCondition(assetId, NumberComparer.EQUAL));
		}
		String assetName = condition.getAssetName();
		if (assetName != null) {
			searchExpression.addCondition(new AssetNameCondition(assetName, StringComparer.LIKE, true, true));
		}
		Long statusId = condition.getStatusId();
		if (statusId != null) {
			searchExpression.addCondition(new AssetStatusIdCondition(statusId, NumberComparer.EQUAL));
		}

		return searchExpression;
	}

	public AssetManagementCondition getCondition() {
		return condition;
	}

	public void setCondition(AssetManagementCondition condition) {
		this.condition = condition;
	}
}