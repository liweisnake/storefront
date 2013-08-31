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
package com.hp.sdf.ngp.api.impl.delegate;

import java.math.BigDecimal;
import java.net.URL;
import java.util.Currency;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;

import com.hp.sdf.ngp.api.comon.util.JndiUtil;
import com.hp.sdf.ngp.api.exception.StoreClientServiceException;
import com.hp.sdf.ngp.api.impl.storeclient.StoreClientServiceImpl;
import com.hp.sdf.ngp.api.model.Asset;
import com.hp.sdf.ngp.api.model.AssetCategory;
import com.hp.sdf.ngp.api.model.AssetComment;
import com.hp.sdf.ngp.api.model.AssetPurchaseHistory;
import com.hp.sdf.ngp.api.model.AssetRatingComments;
import com.hp.sdf.ngp.api.model.BinaryVersion;
import com.hp.sdf.ngp.api.model.StoreClientAsset;
import com.hp.sdf.ngp.api.search.SearchExpression;
import com.hp.sdf.ngp.api.storeclient.StoreClientService;
import com.hp.sdf.ngp.common.delegate.ComponentDelegate;

@Component(value = "storeClientService")
public class StoreClientServiceDelegate extends ComponentDelegate<StoreClientService> implements StoreClientService {

	@SuppressWarnings("unchecked")
	@Override
	protected Class<StoreClientService> getDefaultComponent() {
		return (Class) StoreClientServiceImpl.class;

	}

	@PostConstruct
	protected void init() {
		JndiUtil.bind(StoreClientService.class.getCanonicalName(), this);
	}

	public AssetComment constructAssetComment() throws StoreClientServiceException {
		return component.constructAssetComment();
	}

	public StoreClientAsset getAsset(Long assetId) throws StoreClientServiceException {
		return component.getAsset(assetId);
	}

	public List<AssetCategory> getAssetCategories() throws StoreClientServiceException {
		return component.getAssetCategories();
	}

	public AssetCategory getAssetCategoryById(Long id) throws StoreClientServiceException {
		return component.getAssetCategoryById(id);
	}

	public List<AssetComment> getAssetComment(Long assetId, int start, int count) throws StoreClientServiceException {
		return component.getAssetComment(assetId, start, count);
	}

	public List<AssetComment> getAssetCommentByUserId(Long assetId, String userId) throws StoreClientServiceException {
		return component.getAssetCommentByUserId(assetId, userId);
	}

	public Long getAssetCommentCount(Long assetId) throws StoreClientServiceException {
		return component.getAssetCommentCount(assetId);
	}

	public Long getAssetCommentsSearchResultCount(SearchExpression searchExpression) throws StoreClientServiceException {
		return component.getAssetCommentsSearchResultCount(searchExpression);
	}

	public Long getAssetSearchResultCount(SearchExpression searchExpression) throws StoreClientServiceException {
		return component.getAssetSearchResultCount(searchExpression);
	}

	public BinaryVersion getBinaryVersion(Long versionId) throws StoreClientServiceException {
		return component.getBinaryVersion(versionId);
	}

	public Long getBinaryVersionSearchResultCount(SearchExpression searchExpression) throws StoreClientServiceException {
		return component.getBinaryVersionSearchResultCount(searchExpression);
	}

	public byte[] getFileFromDatabase(String fileLocation) throws StoreClientServiceException {
		return component.getFileFromDatabase(fileLocation);
	}

	public List<Asset> getPurchasedAsset(String userId, int start, int count) throws StoreClientServiceException {
		return component.getPurchasedAsset(userId, start, count);
	}

	public Long getPurchasedAssetCount(String userId) throws StoreClientServiceException {
		return component.getPurchasedAssetCount(userId);
	}

	public List<AssetPurchaseHistory> getStorePurchaseHistory(Long assetId, String userId) throws StoreClientServiceException {
		return component.getStorePurchaseHistory(assetId, userId);
	}

	public List<AssetPurchaseHistory> getStorePurchaseHistoryByUser(String userId) throws StoreClientServiceException {
		return component.getStorePurchaseHistoryByUser(userId);
	}

	public List<AssetRatingComments> getUserCommentsWithRating(Long assetId, int start, int count) throws StoreClientServiceException {
		return component.getUserCommentsWithRating(assetId, start, count);
	}

	public float getUserRating(Long assetId, String userId) throws StoreClientServiceException {
		return component.getUserRating(assetId, userId);
	}

	public void purchase(String userId, Long assetId, Currency currency, BigDecimal amount) throws StoreClientServiceException {
		component.purchase(userId, assetId, currency, amount);
	}

	public void recordAssetDownloadHistory(String userId, Long assetId, Long versionId) throws StoreClientServiceException {
		component.recordAssetDownloadHistory(userId, assetId, versionId);
	}

	public String retrieveDownloadDescriptor(Long assetId, Long versionId, String userId, String finishURL) throws StoreClientServiceException {
		return component.retrieveDownloadDescriptor(assetId, versionId, userId, finishURL);
	}

	public URL retriveBinaryVersionURL(Long assetId, Long versionId, String userId) throws StoreClientServiceException {
		return component.retriveBinaryVersionURL(assetId, versionId, userId);
	}

	public void saveOrUpdateAssetComment(AssetComment assetComment) throws StoreClientServiceException {
		component.saveOrUpdateAssetComment(assetComment);
	}

	public void saveOrUpdateUserRating(Long assetId, String userId, float rating) throws StoreClientServiceException {
		component.saveOrUpdateUserRating(assetId, userId, rating);
	}

	public List<StoreClientAsset> searchAsset(SearchExpression searchExpression) throws StoreClientServiceException {
		return component.searchAsset(searchExpression);
	}

	public List<AssetComment> searchAssetComments(SearchExpression searchExpression) throws StoreClientServiceException {
		return component.searchAssetComments(searchExpression);
	}

	public List<BinaryVersion> searchBinaryVersions(SearchExpression searchExpression) throws StoreClientServiceException {
		return component.searchBinaryVersions(searchExpression);
	}

	public String getStoreLocationPrefix() throws StoreClientServiceException {
		return component.getStoreLocationPrefix();
	}

}

// $Id$