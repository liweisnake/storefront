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
package com.hp.sdf.ngp.api.storeclient;

import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.URL;
import java.util.Currency;
import java.util.List;
import java.util.Map;

import com.hp.sdf.ngp.api.exception.AssetCatalogServiceException;
import com.hp.sdf.ngp.api.exception.StoreClientServiceException;
import com.hp.sdf.ngp.api.model.Asset;
import com.hp.sdf.ngp.api.model.AssetCategory;
import com.hp.sdf.ngp.api.model.AssetComment;
import com.hp.sdf.ngp.api.model.AssetPurchaseHistory;
import com.hp.sdf.ngp.api.model.AssetRatingComments;
import com.hp.sdf.ngp.api.model.BinaryVersion;
import com.hp.sdf.ngp.api.model.StoreClientAsset;
import com.hp.sdf.ngp.api.search.SearchExpression;

/**
 * The interface provides end subscriber store operation functions.
 * <p/>
 * This interface can be used by multiple store client, such as the web server
 * layer, custom web service layer, etc. JNDI binding location:
 * /com/hp/sdf/ngp/api/storeclient/StoreClientService
 */
public interface StoreClientService {

	/**
	 * Gets the asset detailed information
	 * 
	 * @param assetId
	 * @return
	 */
	public StoreClientAsset getAsset(Long assetId)
			throws StoreClientServiceException;

	/**
	 * Get a list of top assets
	 * 
	 * If the search condition includes category, it means all binary of its sub
	 * categories need to be fetched.
	 * 
	 * @param assetListType
	 *            The type of top assets
	 * @param start
	 *            The start index of the top asset list. Begins from 0.
	 * @param count
	 *            The max amount of the top assets got once.
	 * @return A list of returned assets. Always sorted in descending order by
	 *         the top type.
	 */
	public List<StoreClientAsset> searchAsset(SearchExpression searchExpression)
			throws StoreClientServiceException;

	public BinaryVersion getBinaryVersion(Long versionId)
			throws StoreClientServiceException;

	/**
	 * Returns asset binary versions which match search conditions. If the
	 * search condition includes category, it means all binary of its sub
	 * categories need to be fetched.
	 * 
	 * @param searchExpression
	 * @return
	 * @throws StoreClientServiceException
	 */
	public List<BinaryVersion> searchBinaryVersions(
			SearchExpression searchExpression)
			throws StoreClientServiceException;

	public Long getBinaryVersionSearchResultCount(
			SearchExpression searchExpression)
			throws StoreClientServiceException;

	/**
	 * Count the total result amount of a search
	 * 
	 * @param filters
	 *            A combination of search criteria, using AND logic
	 * @return the total result amount of a search
	 */
	public Long getAssetSearchResultCount(SearchExpression searchExpression)
			throws StoreClientServiceException;

	/**
	 * Gets all of the top level predefined categories, for the second level
	 * 
	 * @return all top level categories
	 */
	public List<AssetCategory> getAssetCategories()
			throws StoreClientServiceException;

	/**
	 * Get asset category by id
	 * 
	 * @param id
	 *            the category id
	 * @return asset category with the given id
	 */
	public AssetCategory getAssetCategoryById(Long id)
			throws StoreClientServiceException;

	/**
	 * Builds an empty asset comments object.
	 * 
	 * @return an empty asset comments object.
	 */
	public AssetComment constructAssetComment()
			throws StoreClientServiceException;;

	/**
	 * Save or update asset comment object. The comment will be related to the
	 * latest published version automatically.
	 * 
	 */
	public void saveOrUpdateAssetComment(AssetComment assetComment)
			throws StoreClientServiceException;

	/**
	 * Return the number of comments associated with an asset
	 */
	public Long getAssetCommentCount(Long assetId)
			throws StoreClientServiceException;

	/**
	 * Return the user comments. Sorted by comment date descend.
	 * 
	 * @param start
	 *            The starting offset when retrieving multiple comments
	 * @param count
	 *            The maximum number of comments to be retrieved
	 */
	public List<AssetComment> getAssetComment(Long assetId, int start, int count)
			throws StoreClientServiceException;

	/**
	 * Searches asset comments by given conditions. Conditions can comments
	 * update date.
	 * 
	 * @param searchExpression
	 * @return asset comments which meet given conditions.
	 */
	public List<AssetComment> searchAssetComments(
			SearchExpression searchExpression) throws StoreClientServiceException;

	public Long getAssetCommentsSearchResultCount(
			SearchExpression searchExpression)
			throws StoreClientServiceException;

	/**
	 * 
	 * 
	 * @param assetId
	 * @param userId
	 * @return
	 */
	public List<AssetComment> getAssetCommentByUserId(Long assetId,
			String userId) throws StoreClientServiceException;

	/**
	 * Saves or updates a numeric rating for an asset by a user.
	 * 
	 * If the rating is from a test client, such rating value will not be counted
	 * in a overall average rating.
	 * 
	 * @param userId
	 * @param rating
	 *            Must be an integration between 1 and 5
	 * @param test
	 *            flag of rating from test client. Such rating value will not be
	 *            counted in a overall average rating.
	 * @exception StoreClientServiceException
	 */
	public void saveOrUpdateUserRating(Long assetId, String userId, float rating
			) throws StoreClientServiceException;

	/**
	 * Retrieves the numeric rating for the asset by a user
	 * 
	 * @param userId
	 */
	public float getUserRating(Long assetId, String userId)
			throws StoreClientServiceException;

	/**
	 * Returns a list of user rating and comments pair. The pair is put in a
	 * map. The order is newer comments first.
	 * 
	 * @param assetId
	 * @param start
	 * @param count
	 * @return a list of user rating and comments pair.
	 */
	public List<AssetRatingComments> getUserCommentsWithRating(Long assetId,
			int start, int count) throws StoreClientServiceException;

	/**
	 * Get the download descriptor for a indicated asset The system will add
	 * download count when this method is invoked.
	 * <p/>
	 * If an asset has parent asset, the count will also be added up to its
	 * parent. An exception is thrown if the user is not authorized to download
	 * the asset
	 * 
	 * @param userId
	 * @param finishURL
	 *            The URL used to notify that the download is finished
	 * @return The download descriptor (DD) as per OMA OTA specification, in xml
	 *         format.
	 * @exception StoreClientServiceException
	 */
	public String retrieveDownloadDescriptor(Long assetId, Long versionId,
			String userId, String finishURL) throws StoreClientServiceException;

	/**
	 * Returns the binary file URL of an asset version. 
	 * 
	 * @param assetId
	 * @param versionId
	 * @param userId
	 * @return
	 * @throws StoreClientServiceException
	 */
	public URL retriveBinaryVersionURL(Long assetId, Long versionId,
			String userId) throws StoreClientServiceException;
	
	/**
	 * 
	 * @param assetId
	 * @param versionId
	 * @param userId
	 * @return
	 * @throws StoreClientServiceException
	 */
	public byte[] getFileFromDatabase(String fileLocation) throws StoreClientServiceException;
	
	/**
	 * Purchase an asset Not implemented.
	 * 
	 * @param assetId
	 *            The asset to be purchased
	 * @throws StoreClientServiceException
	 *             if the purchase request fails.
	 */
	public void purchase(String userId, Long assetId, Currency currency,
			BigDecimal amount) throws StoreClientServiceException;
	
	/**
	 * Adds a record of asset download history. 
	 * 
	 * Also updates sum of asset download count.
	 * 
	 * @param userId the subscriber Id who download the asset.
	 * @param assetId the asset ID.
	 * @param versionId the version ID been downloaded.
	 */
	public void recordAssetDownloadHistory(String userId, Long assetId, Long versionId) throws StoreClientServiceException;

	/**
	 * Return a set of asset of my purchased asset
	 * 
	 * @param userId
	 * @param start
	 * @param count
	 * @return
	 * @throws StoreClientServiceException
	 */
	public List<Asset> getPurchasedAsset(String userId, int start, int count)
			throws StoreClientServiceException;

	/**
	 * Get the number of the purchased assets
	 */
	public Long getPurchasedAssetCount(String userId)
			throws StoreClientServiceException;

	/**
	 * Gets the asset purchase history.
	 * 
	 * @param assetId
	 * @param userId
	 * @return
	 * @throws StoreClientServiceException
	 */
	public List<AssetPurchaseHistory> getStorePurchaseHistory(Long assetId,
			String userId) throws StoreClientServiceException;

	public List<AssetPurchaseHistory> getStorePurchaseHistoryByUser(
			String userId) throws StoreClientServiceException;

	/**
	 * returns the store location prefix. 
	 * 
	 * The prefix could be added before the get location methods, 
	 * such as BinaryVersion.getFileLocation(), to retrive local file from file system.
	 * @return
	 * @throws StoreClientServiceException
	 */
	public String getStoreLocationPrefix() throws StoreClientServiceException;
}

// $Id$