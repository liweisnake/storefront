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

package com.hp.sdf.ngp.api.assetcatalog;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Date;
import java.util.List;
import java.util.Set;

import com.hp.sdf.ngp.api.comon.EntityType;
import com.hp.sdf.ngp.api.exception.AssetCatalogServiceException;
import com.hp.sdf.ngp.api.exception.StoreClientServiceException;
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

/**
 * Asset catalog service to import, export assets. It operates asset and related
 * category, provider
 * <p/>
 * Also supports modifying asset attributes, asset upgrade, etc. JNDI binding
 * location: /com/hp/sdf/ngp/api/assetcatalog/AssetCatalogService
 * 
 */
public interface AssetCatalogService  {
	/**
	 * Creates a new asset object.
	 * <p/>
	 * The new Asset object is empty and has not asset ID. Until the
	 * <code>updateAsset(asset)</code> is invoked, it is not physically created
	 * in the database
	 */
	public CatalogAsset constructAssetObject()
			throws AssetCatalogServiceException;

	/**
	 * Finds an asset by its ID
	 * 
	 * @param assetId
	 * @return
	 */
	public CatalogAsset getAsset(Long assetId)
			throws AssetCatalogServiceException;

	/**
	 * Adds a new asset to the system.
	 * 
	 * @param asset
	 *            the new asset to be added.
	 * @return new asset ID after saved.
	 * @throws AssetCatalogServiceException
	 *             when internal exception occurs.
	 */
	public Long addAsset(CatalogAsset asset)
			throws AssetCatalogServiceException;

	/**
	 * Update an existing asset in the catalog.
	 * <p/>
	 * The asset can be an existing one in the catalog, where it will be
	 * udpated.
	 */
	public void updateAsset(CatalogAsset asset)
			throws AssetCatalogServiceException;

	/**
	 * Set asset current binary version to the new version. Should update both
	 * current versionId, and current version code of the asset entity.
	 */
	public void updateAssetCurrentVersion(Long binaryVersionId)
			throws AssetCatalogServiceException;

	/**
	 * create an asset group, the child asset will beLong to the parent asset
	 * 
	 * @param parentAssetId
	 * @param childAssetId
	 */
	public void createAssetGroup(Long parentAssetId, List<Long> childAssetIds)
			throws AssetCatalogServiceException;

	public List<CatalogAsset> searchAsset(SearchExpression searchExpression)
			throws AssetCatalogServiceException;

	public Long getAssetSearchResultCount(SearchExpression searchExpression)
			throws AssetCatalogServiceException;

	/**
	 * Returns a new empty asset category object.
	 * 
	 * @return a new empty asset category object.
	 */
	public AssetCategory constructAssetCategory()
			throws AssetCatalogServiceException;

	/**
	 * Get an existing category
	 */
	public AssetCategory getCategory(Long categoryId)
			throws AssetCatalogServiceException;

	/**
	 * Creates a new category without saving its child categories.
	 * 
	 */
	public AssetCategory addCategory(AssetCategory category)
			throws AssetCatalogServiceException;

	public AssetCategory updateCategory(AssetCategory category)
			throws AssetCatalogServiceException;

	/**
	 * Deletes a category when it has no children and no asset associated.
	 * 
	 * @param categoryId
	 * @return
	 */
	public AssetCategory deleteCategory(Long categoryId)
			throws AssetCatalogServiceException;

	public List<AssetCategory> searchCategory(SearchExpression searchExpression)
			throws AssetCatalogServiceException;

	/**
	 * Create the relation between multiple categories.
	 * 
	 * @param parentCategoryId
	 * @param childCategoryIds
	 */
	public void createCategoryRelationShip(Long parentCategoryId,
			List<Long> childCategoryIds) throws AssetCatalogServiceException;

	/**
	 * Creates a new Provider object.
	 * <p/>
	 * The new object is empty and has no ID. It is not physically created in
	 * the database until the <code>updateProvider()</code> is invoked.
	 */
	public AssetProvider constructAssetProvider()
			throws AssetCatalogServiceException;

	public AssetProvider getAssetProvider(Long providerId)
			throws AssetCatalogServiceException;

	public void addAssetProvider(AssetProvider provider)
			throws AssetCatalogServiceException;

	/** Retrieve an existing provider */
	public List<AssetProvider> searchAssetProvider(
			SearchExpression searchExpression)
			throws AssetCatalogServiceException;

	/**
	 * Updates an existing provider in the database
	 * 
	 * @param provider
	 * @throws AssetCatalogServiceException
	 */
	public void updateAssetProvider(AssetProvider provider)
			throws AssetCatalogServiceException;

	public void deleteAssetProvider(Long providerId)
			throws AssetCatalogServiceException;

	/* BinaryVersion operations */
	public BinaryVersion constructBinaryVersion()
			throws AssetCatalogServiceException;

	public BinaryVersion getBinaryVersion(Long versionId)
			throws AssetCatalogServiceException;

	/**
	 * 
	 * @param criteria
	 * @param orderBy
	 * @param order
	 * @param first
	 * @param count
	 * @return
	 * @throws AssetCatalogServiceException
	 */
	public List<BinaryVersion> searchBinaryVersions(
			SearchExpression searchExpression)
			throws AssetCatalogServiceException;

	public Long getBinaryVersionSearchResultCount(
			SearchExpression searchExpression)
			throws AssetCatalogServiceException;

	/**
	 * 
	 * @param assetId
	 * @param binaryVersion
	 * @return binary version ID.
	 */
	public Long addBinaryVersion(BinaryVersion binaryVersion)
			throws AssetCatalogServiceException;

	public void updateBinaryVersion(BinaryVersion binaryVersion)
			throws AssetCatalogServiceException;

	/**
	 * Deletes binaryversion Also need to delete version related price, files,
	 * screenshots, categoryrelation,
	 * 
	 * @param versionId
	 */
	public void deleteBinaryVersion(Long versionId)
			throws AssetCatalogServiceException;

	/* screen shot operations */
	public Screenshot contructScreenshot() throws AssetCatalogServiceException;

	public void addScreenshot(Screenshot screenshot)
			throws AssetCatalogServiceException;

	public void deleteScreenshot(Long screenshotId)
			throws AssetCatalogServiceException;

	/* price method oprations */
	/**
	 * Adds price to an asset. This is not a simple set method but a function.
	 * 
	 * @param assetId
	 * @param versionId
	 * @param currency
	 * @param amount
	 */
	public void addPrice(Long assetId, Long versionId, Currency currency,
			BigDecimal amount) throws AssetCatalogServiceException;

	public void deletePrice(Long assetId, Long versionId, Currency currency)
			throws AssetCatalogServiceException;

	public void addPlatform(Long assetId, String platform)
			throws AssetCatalogServiceException;

	public void deletePlatfrom(Long assetId, String platform)
			throws AssetCatalogServiceException;

	public Long getAssetCommentCount(Long assetId)
			throws AssetCatalogServiceException;

	/**
	 * Return the user comments. Sorted by comment date descend.
	 * 
	 * @param start
	 *            The starting offset when retrieving multiple comments
	 * @param count
	 *            The maximum number of comments to be retrieved
	 */
	public List<AssetComment> getAssetComment(Long assetId, int start, int count)
			throws AssetCatalogServiceException;

	/**
	 * Searches asset comments by given conditions.
	 * 
	 * @param searchExpression
	 * @return asset comments which meet given conditions.
	 */
	public List<AssetComment> searchAssetComments(
			SearchExpression searchExpression)
			throws AssetCatalogServiceException;

	/**
	 * Adds tags to the system.
	 * 
	 * @param tags
	 * @throws AssetCatalogServiceException
	 */
	public void addTags(Set<String> tags) throws AssetCatalogServiceException;

	/**
	 * Removes tags and their cascading relationship with assets.
	 * 
	 * @param tags
	 * @throws AssetCatalogServiceException
	 */
	public void removeTags(Set<String> tags)
			throws AssetCatalogServiceException;

	public void removeTags(Long assetId, Set<String> tags)
			throws AssetCatalogServiceException;

	public void addAssetIntoCategory(Long assetId, Long versionId,
			Long categoryId) throws AssetCatalogServiceException;

	public void addAssetIntoCategories(Long assetId, Long versionId,
			Set<Long> categoryIds) throws AssetCatalogServiceException;

	public void removeAssetFromCategory(Long assetId, Long versionId,
			Long categoryId) throws AssetCatalogServiceException;

	public void removeAssetFromAllCategories(Long assetId, Long versionId)
			throws AssetCatalogServiceException;

	/* other methods go here */
	public List<RestrictionType> getAllRestrictionTypes()
			throws AssetCatalogServiceException;

	public RestrictionType constructRestrictionType();

	public void addRestrictionType(RestrictionType restrictionType)
			throws AssetCatalogServiceException;

	public void deleteRestrictionType(Long restrictionTypeId)
			throws AssetCatalogServiceException;

	/**
	 * Sets restriction types for an asset.
	 * 
	 * The previous restriction types will be removed. New restriction types
	 * will be associated with the asset.
	 * 
	 * @param assetId
	 * @param restrictionTypeIds
	 * @throws AssetCatalogServiceException
	 */
	public void setRestrictionTypes(Long assetId, Long versionId, Set<Long> restrictionTypeIds)
			throws AssetCatalogServiceException;

	/**
	 * Generic asset attribute setting /* There can be multiple
	 * attribute-value-pair with the same attribute name Adding new ones will
	 * not overwite old ones. However removing one attribute with name will
	 * delete all AVP with that attribute name.
	 */
	public void addAttribute(EntityType entityType, Long objectId,
			String attributeName, String value)
			throws AssetCatalogServiceException;

	public AttributeList constructAttributeList()
			throws AssetCatalogServiceException;

	public void addAttribute(EntityType entityType, Long objectId,
			String attributeName, Float value)
			throws AssetCatalogServiceException;

	public void addAttribute(EntityType entityType, Long objectId,
			String attributeName, Date value)
			throws AssetCatalogServiceException;

	public void addAttributes(EntityType entityType, Long objectId,
			AttributeList attributes) throws AssetCatalogServiceException;

	public void removeAttributes(EntityType entityType, Long objectId,
			String attributeName) throws AssetCatalogServiceException;

	public AssetLifecycleActionHistory constructAssetLifecycleActionHistory()
			throws AssetCatalogServiceException;

	public Long addAssetLifecycleActionHistory(
			AssetLifecycleActionHistory assetLifecycleActionHistory)
			throws AssetCatalogServiceException;

	public void updateAssetLifecycleActionHistory(
			AssetLifecycleActionHistory assetLifecycleActionHistory)
			throws AssetCatalogServiceException;

	public List<AssetLifecycleActionHistory> searchAssetLifecycleActionHistory(
			SearchExpression searchExpresstion)
			throws AssetCatalogServiceException;

	/**
	 * Upgrades an asset's current version. Copies temp asset metadata from
	 * candidate version to its asset. A new version may have some new asset
	 * metadata. So when the version is published, the new asset metadata need
	 * to be copied to the asset itself.
	 * 
	 * @param assetId
	 *            the target asset to be update.
	 * @param binaryVersionId
	 *            candidate binary version
	 * @throws AssetCatalogServiceException
	 */
	public void upgradeAssetCurrentVersion(Long assetId, Long binaryVersionId)
			throws AssetCatalogServiceException;

	/**
	 * Constructs an empty user object.
	 * 
	 * @return an empty user object
	 * @throws AssetCatalogServiceException
	 */
	public User constructUser() throws AssetCatalogServiceException;

	/**
	 * Adds a user account into the system.
	 * 
	 * @param user
	 * @throws AssetCatalogServiceException
	 */
	public void addUser(User user) throws AssetCatalogServiceException;

	/**
	 * Updates a user account except the password
	 * 
	 * @param user
	 * @throws AssetCatalogServiceException
	 */
	public void updateUser(User user) throws AssetCatalogServiceException;
	
	/**
	 * Sets the new user password. 
	 * 
	 * Do not encode it. System will do this.
	 * 
	 */
	public void updateUserPassword(String userId, String password) throws AssetCatalogServiceException;

	/**
	 * Deletes a user account.
	 * 
	 * A users related role association and provider association will also be
	 * deleted.
	 * 
	 * @param userId
	 * @throws AssetCatalogServiceException
	 */
	public void deleteUser(String userId) throws AssetCatalogServiceException;

	/**
	 * Returns a user account by user's id.
	 * 
	 * @param userId
	 * @return a user account with the given ID.
	 * @throws AssetCatalogServiceException
	 */
	public User getUser(String userId) throws AssetCatalogServiceException;

	/**
	 * Assigns a role to a user.
	 * 
	 * @param userId
	 * @param roleName
	 * @throws AssetCatalogServiceException
	 *             when userId or role cannot be found.
	 */
	public void assignRole(String userId, String roleName)
			throws AssetCatalogServiceException;

	/**
	 * Unassign a role of a user.
	 * 
	 * If the user does not have the role originally, no exception will be
	 * reported.
	 * 
	 * @param userId
	 * @param roleName
	 * @throws AssetCatalogServiceException
	 *             when userId or role cannot be found.
	 */
	public void unassignRole(String userId, String roleName)
			throws AssetCatalogServiceException;

	/**
	 * Adds a user to a asset provider organization.
	 * 
	 * @param userId
	 * @param providerId
	 */
	public void addUserToProvider(String userId, Long providerId)
			throws AssetCatalogServiceException;

	/**
	 * Removes a user from a asset provider organization.
	 * 
	 * If the user does not belongs to the provider, no exception will be
	 * reported.
	 * 
	 * @param userId
	 * @param providerId
	 */
	public void removeUserFromProvider(String userId, Long providerId)
			throws AssetCatalogServiceException;

	public List<User> getUsersByProvider(Long providerId)
			throws AssetCatalogServiceException;
	
	/**
	 * 	/**
	 * returns the store location prefix. 
	 * 
	 * The prefix could be added before the get location methods, 
	 * such as BinaryVersion.getFileLocation(), to retrive local file from file system.
     *
	 * @return
	 * @throws AssetCatalogServiceException
	 */
	public String getStoreLocationPrefix() throws AssetCatalogServiceException;
	
	/**
	 * Adds a record of asset download history. 
	 * 
	 * Also updates sum of asset download count.
	 * 
	 * @param userId the subscriber Id who download the asset.
	 * @param assetId the asset ID.
	 * @param version the version code .
	 */
	public void recordAssetDownloadHistory(String userId, Long assetId, String version) 
		throws AssetCatalogServiceException;
}
