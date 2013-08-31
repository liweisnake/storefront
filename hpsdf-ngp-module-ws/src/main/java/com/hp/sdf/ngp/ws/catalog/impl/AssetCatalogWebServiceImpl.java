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
package com.hp.sdf.ngp.ws.catalog.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hp.sdf.ngp.api.search.SearchExpression;
import com.hp.sdf.ngp.api.search.Condition.DateComparer;
import com.hp.sdf.ngp.api.search.Condition.StringComparer;
import com.hp.sdf.ngp.model.AssetPrice;
import com.hp.sdf.ngp.model.Category;
import com.hp.sdf.ngp.model.Platform;
import com.hp.sdf.ngp.search.condition.asset.AssetAssetCategoryNameCondition;
import com.hp.sdf.ngp.search.condition.asset.AssetAuthoridCondition;
import com.hp.sdf.ngp.search.condition.asset.AssetUpdateDateCondition;
import com.hp.sdf.ngp.search.engine.SearchExpressionImpl;
import com.hp.sdf.ngp.service.ApplicationService;
import com.hp.sdf.ngp.ws.catalog.AssetCatalogWebService;
import com.hp.sdf.ngp.ws.common.GetPropValueFromList;
import com.hp.sdf.ngp.ws.model.Asset;

@Service
public class AssetCatalogWebServiceImpl implements AssetCatalogWebService {

	@Autowired
	private ApplicationService applicationService;

	public List<Asset> getAllAssets() {
		List<com.hp.sdf.ngp.model.Asset> assets = applicationService.getAllAsset(0, Integer.MAX_VALUE);
		return this.convertAsset(assets);
	}
	
	public List<Asset> getAssets(int start, int max) {
		List<com.hp.sdf.ngp.model.Asset> assets = applicationService.getAllAsset(start, max);
		return this.convertAsset(assets);
	}

	public List<String> getAllCategories() {
		List<String> result = null;
		List<Category> list = applicationService.getAllCategory(0, Integer.MAX_VALUE);
		if (list != null && list.size() > 0) {
			result = new ArrayList<String>();
			for (Category c : list) {
				result.add(c.getName());
			}
		}
		return result;
	}

	public List<Asset> getAssetsByCategory(String category) {

		
		SearchExpression searchExpression = new SearchExpressionImpl();
		searchExpression.addCondition(new AssetAssetCategoryNameCondition(category, StringComparer.EQUAL, false, false));
		searchExpression.setFirst(0);
		searchExpression.setMax(Integer.MAX_VALUE);
		List<com.hp.sdf.ngp.model.Asset> assets = applicationService.searchAsset(searchExpression);
		return this.convertAsset(assets);
	}

	public List<Asset> getAssetsByDate(Date date) {
		SearchExpression searchExpression = new SearchExpressionImpl();
		searchExpression.addCondition(new AssetUpdateDateCondition(date,DateComparer.GREAT_THAN));
		searchExpression.setFirst(0);
		searchExpression.setMax(Integer.MAX_VALUE);
		
		List<com.hp.sdf.ngp.model.Asset> assets = applicationService.searchAsset(searchExpression);
		
		return this.convertAsset(assets);
	}

	public List<Asset> getAssetsByDeveloper(String developer) {

		
		SearchExpression searchExpression = new SearchExpressionImpl();
		searchExpression.addCondition(new AssetAuthoridCondition(developer, StringComparer.EQUAL, false,false));
		searchExpression.setFirst(0);
		searchExpression.setMax(Integer.MAX_VALUE);
		
		List<com.hp.sdf.ngp.model.Asset> assets = applicationService.searchAsset(searchExpression);
		
		return this.convertAsset(assets);
	}

	

	private Asset convertAsset(com.hp.sdf.ngp.model.Asset asset) {
		Asset result = new Asset();
		List<AssetPrice> assetPriceList = applicationService.getAssetPriceByAssetId(asset.getId());
		result.setAssetPrice(GetPropValueFromList.getAssetPriceFromListDollars(assetPriceList));
		result.setBrief(asset.getBrief());
		result.setCreateDate(asset.getCreateDate());
		result.setDescription(asset.getDescription());
		result.setDoc(asset.getDocUrl());
		result.setDownloadCount(asset.getDownloadCount());
		result.setId(asset.getId());
		result.setName(asset.getName());
		List<Platform> platformList = applicationService.getPlatformByAssetId(asset.getId());
		result.setPlatform(GetPropValueFromList.getPlatfromNameFromList(platformList));
		result.setPreview(asset.getThumbnailBigLocation());
		result.setRating(asset.getAverageUserRating());
		result.setRecommendOrder(asset.getRecommendOrder());
		result.setSource(asset.getSource());
		result.setStatus(asset.getStatus().getStatus());
		result.setThumbnail(asset.getThumbnailLocation());
		result.setUpdateDate(asset.getUpdateDate());
		result.setUserId(asset.getAuthorid());
		result.setVersion(asset.getCurrentVersion());
		return result;
	}

	private List<Asset> convertAsset(List<com.hp.sdf.ngp.model.Asset> assets) {
		List<Asset> list = null;
		if (assets != null && assets.size() > 0) {
			list = new ArrayList<Asset>();
			for (com.hp.sdf.ngp.model.Asset asset : assets) {
				list.add(this.convertAsset(asset));
			}
		}
		return list;
	}

}

// $Id$