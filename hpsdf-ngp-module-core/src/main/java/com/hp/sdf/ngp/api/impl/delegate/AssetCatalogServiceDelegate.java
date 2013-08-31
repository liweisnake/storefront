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
import java.util.Currency;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;

import com.hp.sdf.ngp.api.assetcatalog.AssetCatalogService;
import com.hp.sdf.ngp.api.comon.EntityType;
import com.hp.sdf.ngp.api.comon.util.JndiUtil;
import com.hp.sdf.ngp.api.exception.AssetCatalogServiceException;
import com.hp.sdf.ngp.api.impl.assetcatalog.AssetCatalogServiceImpl;
import com.hp.sdf.ngp.api.model.AssetCategory;
import com.hp.sdf.ngp.api.model.AssetComment;
import com.hp.sdf.ngp.api.model.AssetLifecycleActionHistory;
import com.hp.sdf.ngp.api.model.AssetProvider;
import com.hp.sdf.ngp.api.model.AttributeList;
import com.hp.sdf.ngp.api.model.BinaryVersion;
import com.hp.sdf.ngp.api.model.CatalogAsset;
import com.hp.sdf.ngp.api.model.RestrictionType;
import com.hp.sdf.ngp.api.model.Screenshot;
import com.hp.sdf.ngp.api.model.User;
import com.hp.sdf.ngp.api.search.SearchExpression;
import com.hp.sdf.ngp.common.delegate.ComponentDelegate;

@Component(value = "assetCatalogService")
public class AssetCatalogServiceDelegate extends ComponentDelegate<AssetCatalogService> implements AssetCatalogService {

	@SuppressWarnings("unchecked")
	@Override
	protected Class<AssetCatalogService> getDefaultComponent() {
		return (Class) AssetCatalogServiceImpl.class;

	}

	@PostConstruct
	protected void init() {
		JndiUtil.bind(AssetCatalogService.class.getCanonicalName(), this);
	}

	public Long addAsset(CatalogAsset asset) throws AssetCatalogServiceException {
		return this.component.addAsset(asset);
	}

	public void addAssetIntoCategories(Long assetId, Long versionId, Set<Long> categoryIds) throws AssetCatalogServiceException {
		this.component.addAssetIntoCategories(assetId, versionId, categoryIds);
	}

	public void addAssetIntoCategory(Long assetId, Long versionId, Long categoryId) throws AssetCatalogServiceException {
		this.component.addAssetIntoCategory(assetId, versionId, categoryId);
	}

	public Long addAssetLifecycleActionHistory(AssetLifecycleActionHistory assetLifecycleActionHistory) throws AssetCatalogServiceException {
		return this.component.addAssetLifecycleActionHistory(assetLifecycleActionHistory);
	}

	public void addAssetProvider(AssetProvider provider) throws AssetCatalogServiceException {
		this.component.addAssetProvider(provider);
	}

	public void addAttribute(EntityType entityType, Long objectId, String attributeName, Date value) throws AssetCatalogServiceException {
		this.component.addAttribute(entityType, objectId, attributeName, value);
	}

	public void addAttribute(EntityType entityType, Long objectId, String attributeName, Float value) throws AssetCatalogServiceException {
		this.component.addAttribute(entityType, objectId, attributeName, value);
	}

	public void addAttribute(EntityType entityType, Long objectId, String attributeName, String value) throws AssetCatalogServiceException {
		this.component.addAttribute(entityType, objectId, attributeName, value);
	}

	public void addAttributes(EntityType entityType, Long objectId, AttributeList attributes) throws AssetCatalogServiceException {
		this.component.addAttributes(entityType, objectId, attributes);
	}

	public Long addBinaryVersion(BinaryVersion binaryVersion) throws AssetCatalogServiceException {
		return this.component.addBinaryVersion(binaryVersion);
	}

	public AssetCategory addCategory(AssetCategory category) throws AssetCatalogServiceException {
		return this.component.addCategory(category);
	}

	public void addPlatform(Long assetId, String platform) throws AssetCatalogServiceException {
		this.component.addPlatform(assetId, platform);
	}

	public void addPrice(Long assetId, Long versionId, Currency currency, BigDecimal amount) throws AssetCatalogServiceException {
		this.component.addPrice(assetId, versionId, currency, amount);
	}

	public void addRestrictionType(RestrictionType restrictionType) throws AssetCatalogServiceException {
		this.component.addRestrictionType(restrictionType);
	}

	public void addScreenshot(Screenshot screenshot) throws AssetCatalogServiceException {
		this.component.addScreenshot(screenshot);
	}

	public void addTags(Set<String> tags) throws AssetCatalogServiceException {
		this.component.addTags(tags);
	}

	public void addUser(User user) throws AssetCatalogServiceException {
		this.component.addUser(user);
	}

	public void addUserToProvider(String userId, Long providerId) throws AssetCatalogServiceException {
		this.component.addUserToProvider(userId, providerId);
	}

	public void assignRole(String userId, String roleName) throws AssetCatalogServiceException {
		this.component.assignRole(userId, roleName);
	}

	public AssetCategory constructAssetCategory() throws AssetCatalogServiceException {
		return this.component.constructAssetCategory();
	}

	public AssetLifecycleActionHistory constructAssetLifecycleActionHistory() throws AssetCatalogServiceException {
		return this.component.constructAssetLifecycleActionHistory();
	}

	public CatalogAsset constructAssetObject() throws AssetCatalogServiceException {
		return this.component.constructAssetObject();
	}

	public AssetProvider constructAssetProvider() throws AssetCatalogServiceException {
		return this.component.constructAssetProvider();
	}

	public AttributeList constructAttributeList() throws AssetCatalogServiceException {
		return this.component.constructAttributeList();
	}

	public BinaryVersion constructBinaryVersion() throws AssetCatalogServiceException {
		return this.component.constructBinaryVersion();
	}

	public RestrictionType constructRestrictionType() {
		return this.component.constructRestrictionType();
	}

	public User constructUser() throws AssetCatalogServiceException {
		return this.component.constructUser();
	}

	public Screenshot contructScreenshot() throws AssetCatalogServiceException {
		return this.component.contructScreenshot();
	}

	public void createAssetGroup(Long parentAssetId, List<Long> childAssetIds) throws AssetCatalogServiceException {
		this.component.createAssetGroup(parentAssetId, childAssetIds);
	}

	public void createCategoryRelationShip(Long parentCategoryId, List<Long> childCategoryIds) throws AssetCatalogServiceException {
		this.component.createCategoryRelationShip(parentCategoryId, childCategoryIds);
	}

	public void deleteAssetProvider(Long providerId) throws AssetCatalogServiceException {
		this.component.deleteAssetProvider(providerId);
	}

	public void deleteBinaryVersion(Long versionId) throws AssetCatalogServiceException {
		this.component.deleteBinaryVersion(versionId);
	}

	public AssetCategory deleteCategory(Long categoryId) throws AssetCatalogServiceException {
		return this.component.deleteCategory(categoryId);
	}

	public void deletePlatfrom(Long assetId, String platform) throws AssetCatalogServiceException {
		this.component.deletePlatfrom(assetId, platform);
	}

	public void deletePrice(Long assetId, Long versionId, Currency currency) throws AssetCatalogServiceException {
		this.component.deletePrice(assetId, versionId, currency);
	}

	public void deleteRestrictionType(Long restrictionTypeId) throws AssetCatalogServiceException {
		this.component.deleteRestrictionType(restrictionTypeId);
	}

	public void deleteScreenshot(Long screenshotId) throws AssetCatalogServiceException {
		this.component.deleteScreenshot(screenshotId);
	}

	public void deleteUser(String userId) throws AssetCatalogServiceException {
		this.component.deleteUser(userId);
	}

	public List<RestrictionType> getAllRestrictionTypes() throws AssetCatalogServiceException {
		return this.component.getAllRestrictionTypes();
	}

	public CatalogAsset getAsset(Long assetId) throws AssetCatalogServiceException {
		return this.component.getAsset(assetId);
	}

	public List<AssetComment> getAssetComment(Long assetId, int start, int count) throws AssetCatalogServiceException {
		return this.component.getAssetComment(assetId, start, count);
	}

	public Long getAssetCommentCount(Long assetId) throws AssetCatalogServiceException {
		return this.component.getAssetCommentCount(assetId);
	}

	public AssetProvider getAssetProvider(Long providerId) throws AssetCatalogServiceException {
		return this.component.getAssetProvider(providerId);
	}

	public Long getAssetSearchResultCount(SearchExpression searchExpression) throws AssetCatalogServiceException {
		return this.component.getAssetSearchResultCount(searchExpression);
	}

	public BinaryVersion getBinaryVersion(Long versionId) throws AssetCatalogServiceException {
		return this.component.getBinaryVersion(versionId);
	}

	public Long getBinaryVersionSearchResultCount(SearchExpression searchExpression) throws AssetCatalogServiceException {
		return this.component.getBinaryVersionSearchResultCount(searchExpression);
	}

	public AssetCategory getCategory(Long categoryId) throws AssetCatalogServiceException {
		return this.component.getCategory(categoryId);
	}

	public User getUser(String userId) throws AssetCatalogServiceException {
		return this.component.getUser(userId);
	}

	public List<User> getUsersByProvider(Long providerId) throws AssetCatalogServiceException {
		return this.component.getUsersByProvider(providerId);
	}

	public void removeAssetFromAllCategories(Long assetId, Long versionId) throws AssetCatalogServiceException {
		this.component.removeAssetFromAllCategories(assetId, versionId);
	}

	public void removeAssetFromCategory(Long assetId, Long versionId, Long categoryId) throws AssetCatalogServiceException {
		this.component.removeAssetFromCategory(assetId, versionId, categoryId);
	}

	public void removeAttributes(EntityType entityType, Long objectId, String attributeName) throws AssetCatalogServiceException {
		this.component.removeAttributes(entityType, objectId, attributeName);
	}

	public void removeTags(Long assetId, Set<String> tags) throws AssetCatalogServiceException {
		this.component.removeTags(assetId, tags);
	}

	public void removeTags(Set<String> tags) throws AssetCatalogServiceException {
		this.component.removeTags(tags);
	}

	public void removeUserFromProvider(String userId, Long providerId) throws AssetCatalogServiceException {
		this.component.removeUserFromProvider(userId, providerId);
	}

	public List<CatalogAsset> searchAsset(SearchExpression searchExpression) throws AssetCatalogServiceException {
		return this.component.searchAsset(searchExpression);
	}

	public List<AssetComment> searchAssetComments(SearchExpression searchExpression) throws AssetCatalogServiceException {
		return this.component.searchAssetComments(searchExpression);
	}

	public List<AssetLifecycleActionHistory> searchAssetLifecycleActionHistory(SearchExpression searchExpresstion) throws AssetCatalogServiceException {
		return this.component.searchAssetLifecycleActionHistory(searchExpresstion);
	}

	public List<AssetProvider> searchAssetProvider(SearchExpression searchExpression) throws AssetCatalogServiceException {
		return this.component.searchAssetProvider(searchExpression);
	}

	public List<BinaryVersion> searchBinaryVersions(SearchExpression searchExpression) throws AssetCatalogServiceException {
		return this.component.searchBinaryVersions(searchExpression);
	}

	public List<AssetCategory> searchCategory(SearchExpression searchExpression) throws AssetCatalogServiceException {
		return this.component.searchCategory(searchExpression);
	}

	public void setRestrictionTypes(Long assetId, Long versionId, Set<Long> restrictionTypeIds) throws AssetCatalogServiceException {
		this.component.setRestrictionTypes(assetId, versionId, restrictionTypeIds);
	}

	public void unassignRole(String userId, String roleName) throws AssetCatalogServiceException {
		this.component.unassignRole(userId, roleName);
	}

	public void updateAsset(CatalogAsset asset) throws AssetCatalogServiceException {
		this.component.updateAsset(asset);
	}

	public void updateAssetCurrentVersion(Long binaryVersionId) throws AssetCatalogServiceException {
		this.component.updateAssetCurrentVersion(binaryVersionId);
	}

	public void updateAssetLifecycleActionHistory(AssetLifecycleActionHistory assetLifecycleActionHistory) throws AssetCatalogServiceException {
		this.component.updateAssetLifecycleActionHistory(assetLifecycleActionHistory);
	}

	public void updateAssetProvider(AssetProvider provider) throws AssetCatalogServiceException {
		this.component.updateAssetProvider(provider);
	}

	public void updateBinaryVersion(BinaryVersion binaryVersion) throws AssetCatalogServiceException {
		this.component.updateBinaryVersion(binaryVersion);
	}

	public AssetCategory updateCategory(AssetCategory category) throws AssetCatalogServiceException {
		return this.component.updateCategory(category);
	}

	public void updateUser(User user) throws AssetCatalogServiceException {
		this.component.updateUser(user);
	}

	public void updateUserPassword(String userId, String password) throws AssetCatalogServiceException {
		this.component.updateUserPassword(userId, password);
	}

	public void upgradeAssetCurrentVersion(Long assetId, Long binaryVersionId) throws AssetCatalogServiceException {
		this.component.upgradeAssetCurrentVersion(assetId, binaryVersionId);
	}

	public String getStoreLocationPrefix() throws AssetCatalogServiceException {
		return this.component.getStoreLocationPrefix();
	}
	
	public void recordAssetDownloadHistory(String userId, Long assetId, String version)
	    throws AssetCatalogServiceException {
		this.component.recordAssetDownloadHistory(userId, assetId, version);
	}

}

// $Id$