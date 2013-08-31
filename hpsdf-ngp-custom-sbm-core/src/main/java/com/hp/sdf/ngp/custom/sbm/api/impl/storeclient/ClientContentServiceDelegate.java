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
package com.hp.sdf.ngp.custom.sbm.api.impl.storeclient;

import java.math.BigDecimal;
import java.net.URL;
import java.util.Currency;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;

import com.hp.sdf.ngp.api.comon.util.JndiUtil;
import com.hp.sdf.ngp.api.exception.StoreClientServiceException;
import com.hp.sdf.ngp.api.model.Asset;
import com.hp.sdf.ngp.api.model.AssetCategory;
import com.hp.sdf.ngp.api.model.AssetComment;
import com.hp.sdf.ngp.api.model.AssetPurchaseHistory;
import com.hp.sdf.ngp.api.model.AssetRatingComments;
import com.hp.sdf.ngp.api.model.BinaryVersion;
import com.hp.sdf.ngp.api.model.StoreClientAsset;
import com.hp.sdf.ngp.api.search.SearchExpression;
import com.hp.sdf.ngp.common.delegate.ComponentDelegate;
import com.hp.sdf.ngp.custom.sbm.api.storeclient.model.ContentItem;
import com.hp.sdf.ngp.custom.sbm.api.storeclient.model.HandSetDevice;
import com.hp.sdf.ngp.custom.sbm.api.storeclient.model.ParentAssetVersionSummary;
import com.hp.sdf.ngp.custom.sbm.api.storeclient.model.PurchaseHistoryExtend;
import com.hp.sdf.ngp.custom.sbm.api.storeclient.model.SecurityToken;
import com.hp.sdf.ngp.custom.sbm.api.storeclient.service.ClientContentService;
import com.hp.sdf.ngp.custom.sbm.storeclient.service.ClientContentServiceImpl;

@Component(value = "clientContentService")
public class ClientContentServiceDelegate extends ComponentDelegate<ClientContentService> implements ClientContentService {

	@SuppressWarnings("unchecked")
	@Override
	protected Class<ClientContentService> getDefaultComponent() {
		return (Class) ClientContentServiceImpl.class;

	}

	@PostConstruct
	protected void init() {
		JndiUtil.bind(ClientContentService.class.getCanonicalName(), this);
	}

	public void addPurchaseHistory(PurchaseHistoryExtend purchaseHistory) throws StoreClientServiceException {

		this.component.addPurchaseHistory(purchaseHistory);

	}

	public void addSecurityToken(SecurityToken token) throws StoreClientServiceException {
		this.component.addSecurityToken(token);
	}

	public AssetComment constructAssetComment() throws StoreClientServiceException {
		return this.component.constructAssetComment();
	}

	public PurchaseHistoryExtend constructPurchaseHistoryExtend() throws StoreClientServiceException {
		return this.component.constructPurchaseHistoryExtend();
	}

	public SecurityToken constructSecurityToken() throws StoreClientServiceException {
		return this.component.constructSecurityToken();
	}

	public void deleteSecurityToken(String msisdn) throws StoreClientServiceException {
		this.component.deleteSecurityToken(msisdn);
	}

	public StoreClientAsset getAsset(Long assetId) throws StoreClientServiceException {
		return this.component.getAsset(assetId);
	}

	public List<AssetCategory> getAssetCategories() throws StoreClientServiceException {
		return this.component.getAssetCategories();
	}

	public AssetCategory getAssetCategoryById(Long id) throws StoreClientServiceException {
		return this.component.getAssetCategoryById(id);
	}

	public List<AssetComment> getAssetComment(Long assetId, int start, int count) throws StoreClientServiceException {
		return this.component.getAssetComment(assetId, start, count);
	}

	public List<AssetComment> getAssetCommentByUserId(Long assetId, String userId) throws StoreClientServiceException {
		return this.component.getAssetCommentByUserId(assetId, userId);
	}

	public Long getAssetCommentCount(Long assetId) throws StoreClientServiceException {
		return this.component.getAssetCommentCount(assetId);
	}

	public Long getAssetCommentsSearchResultCount(SearchExpression searchExpression) throws StoreClientServiceException {
		return this.component.getAssetCommentsSearchResultCount(searchExpression);
	}

	public Long getAssetSearchResultCount(SearchExpression searchExpression) throws StoreClientServiceException {
		return this.component.getAssetSearchResultCount(searchExpression);
	}

	public BinaryVersion getBinaryVersion(Long versionId) throws StoreClientServiceException {
		return this.component.getBinaryVersion(versionId);
	}

	public Long getBinaryVersionSearchResultCount(SearchExpression searchExpression) throws StoreClientServiceException {
		return this.component.getBinaryVersionSearchResultCount(searchExpression);
	}

	public ContentItem getContentItem(String itemId, String identifierId) throws StoreClientServiceException {
		return this.component.getContentItem(itemId, identifierId);
	}

	public byte[] getFileFromDatabase(String fileLocation) throws StoreClientServiceException {
		return this.component.getFileFromDatabase(fileLocation);
	}

	public List<Asset> getPurchasedAsset(String userId, int start, int count) throws StoreClientServiceException {
		return this.component.getPurchasedAsset(userId, start, count);
	}

	public Long getPurchasedAssetCount(String userId) throws StoreClientServiceException {
		return this.component.getPurchasedAssetCount(userId);
	}

	public SecurityToken getSecurityToken(String token) throws StoreClientServiceException {
		return this.component.getSecurityToken(token);
	}

	public List<AssetPurchaseHistory> getStorePurchaseHistory(Long assetId, String userId) throws StoreClientServiceException {
		return this.component.getStorePurchaseHistory(assetId, userId);
	}

	public List<AssetPurchaseHistory> getStorePurchaseHistoryByUser(String userId) throws StoreClientServiceException {
		return this.component.getStorePurchaseHistoryByUser(userId);
	}

	public List<AssetRatingComments> getUserCommentsWithRating(Long assetId, int start, int count) throws StoreClientServiceException {
		return this.component.getUserCommentsWithRating(assetId, start, count);
	}

	public float getUserRating(Long assetId, String userId) throws StoreClientServiceException {
		return this.component.getUserRating(assetId, userId);
	}

	public boolean isClientTester(String msisdn) throws StoreClientServiceException {
		return this.component.isClientTester(msisdn);
	}

	public void purchase(String userId, Long assetId, Currency currency, BigDecimal amount) throws StoreClientServiceException {
		this.component.purchase(userId, assetId, currency, amount);
	}

	public void recordAssetDownloadHistory(String userId, Long assetId, Long versionId) throws StoreClientServiceException {
		this.component.recordAssetDownloadHistory(userId, assetId, versionId);
	}

	public String retrieveDownloadDescriptor(Long assetId, Long versionId, String userId, String finishURL) throws StoreClientServiceException {
		return this.component.retrieveDownloadDescriptor(assetId, versionId, userId, finishURL);
	}

	public URL retriveBinaryVersionURL(Long assetId, Long versionId, String userId) throws StoreClientServiceException {
		return this.component.retriveBinaryVersionURL(assetId, versionId, userId);
	}

	public void saveOrUpdateAssetComment(AssetComment assetComment) throws StoreClientServiceException {
		this.component.saveOrUpdateAssetComment(assetComment);
	}

	public void saveOrUpdateUserRating(Long assetId, String userId, float rating) throws StoreClientServiceException {
		this.component.saveOrUpdateUserRating(assetId, userId, rating);
	}

	public List<StoreClientAsset> searchAsset(SearchExpression searchExpression) throws StoreClientServiceException {
		return this.component.searchAsset(searchExpression);
	}

	public List<AssetComment> searchAssetComments(SearchExpression searchExpression) throws StoreClientServiceException {
		return this.component.searchAssetComments(searchExpression);
	}

	public List<BinaryVersion> searchBinaryVersions(SearchExpression searchExpression) throws StoreClientServiceException {
		return this.component.searchBinaryVersions(searchExpression);
	}

	public List<ContentItem> searchContentItem(SearchExpression searchExpression) throws StoreClientServiceException {
		return this.component.searchContentItem(searchExpression);
	}

	public List<HandSetDevice> searchHandSetDevice(SearchExpression searchExpression) throws StoreClientServiceException {
		return this.component.searchHandSetDevice(searchExpression);
	}

	public List<ParentAssetVersionSummary> searchParentAssetVersionSummary(SearchExpression searchExpression) throws StoreClientServiceException {
		return this.component.searchParentAssetVersionSummary(searchExpression);
	}

	public List<PurchaseHistoryExtend> searchPurchaseHistory(SearchExpression searchExpression) throws StoreClientServiceException {
		return this.component.searchPurchaseHistory(searchExpression);
	}

	public List<SecurityToken> searchSecurityToken(SearchExpression searchExpression) throws StoreClientServiceException {
		return this.component.searchSecurityToken(searchExpression);
	}

	public void updatePurchaseHistory(PurchaseHistoryExtend purchaseHistory) throws StoreClientServiceException {

		this.component.updatePurchaseHistory(purchaseHistory);

	}

	public void updateSecurityToken(SecurityToken token) throws StoreClientServiceException {
		this.component.updateSecurityToken(token);
	}

	public String getStoreLocationPrefix() throws StoreClientServiceException {
		return this.component.getStoreLocationPrefix();
	}

}

// $Id$