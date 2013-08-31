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
import com.hp.sdf.ngp.search.condition.asset.AssetTagIdCondition;
import com.hp.sdf.ngp.search.engine.SearchExpressionImpl;
import com.hp.sdf.ngp.service.ApplicationService;
import com.hp.sdf.ngp.ui.common.SearchMetaInfo;

public class AppDataProvider implements IDataProvider<Asset> {

	private static final long serialVersionUID = 4609514328333011423L;

	private ApplicationService applicationService;

	private String keyword;
	private List<SearchMetaInfo> searchMetaInfoList;
	private AssetOrderBy assetOrderBy;
	private OrderEnum orderEnum;

	public AssetOrderBy getAssetOrderBy() {
		return assetOrderBy;
	}

	public void setAssetOrderBy(AssetOrderBy assetOrderBy) {
		this.assetOrderBy = assetOrderBy;
	}

	public OrderEnum getOrderEnum() {
		return orderEnum;
	}

	public void setOrderEnum(OrderEnum orderEnum) {
		this.orderEnum = orderEnum;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public List<SearchMetaInfo> getSearchMetaInfo() {
		return this.searchMetaInfoList;
	}

	public void setSearchMetaInfo(List<SearchMetaInfo> searchMetaInfo) {
		this.searchMetaInfoList = searchMetaInfo;
	}

	public AppDataProvider(ApplicationService applicationService) {
		this.applicationService = applicationService;
	}

	public Iterator<? extends Asset> iterator(int first, int count) {
		SearchExpression searchExpression = getSearchExpr();
		searchExpression.setFirst(first);
		searchExpression.setMax(count);
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
		SearchExpression searchExpression = getSearchExpr();
		searchExpression.setFirst(0);
		searchExpression.setMax(Integer.MAX_VALUE);
		return (int) applicationService.searchAssetPageCount(searchExpression);
	}

	private SearchExpression getSearchExpr() {
		SearchExpression searchExpression = new SearchExpressionImpl();

		if (null == assetOrderBy)
			assetOrderBy = AssetOrderBy.NAME;
		if (null == orderEnum)
			orderEnum = OrderEnum.ASC;
		searchExpression.addOrder(assetOrderBy, orderEnum);

		if (keyword != null) {
			searchExpression.addCondition(new AssetNameCondition(keyword, StringComparer.LIKE, true, false));
			// searchExpression.addCondition(new AssetDescriptionCondition(keyword, StringComparer.LIKE, true, false));
		}

		if (searchMetaInfoList != null) {
			for (SearchMetaInfo searchMetaInfo : searchMetaInfoList) {
				switch (searchMetaInfo.getSearchBy()) {
				case ASSETCATEGORYRELATION_CATEGORY_ID:
					searchExpression.addCondition(new AssetAssetCategoryIdCondition(Long.parseLong(searchMetaInfo.getId()), NumberComparer.EQUAL));
					break;
				case ASSETTAGRELATION_TAG_ID:
					searchExpression.addCondition(new AssetTagIdCondition(Long.parseLong(searchMetaInfo.getId()), NumberComparer.EQUAL));
					break;
				case PLATFORM_ID:
					searchExpression.addCondition(new AssetPlatformIdCondition(Long.parseLong(searchMetaInfo.getId()), NumberComparer.EQUAL));
					break;
				case STATUS_ID:
					searchExpression.addCondition(new AssetStatusIdCondition(Long.parseLong(searchMetaInfo.getId()), NumberComparer.EQUAL));
					break;
				}
			}
		}
		return searchExpression;
	}

	public void detach() {
	}

}