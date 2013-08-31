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
package com.hp.sdf.ngp.ws.mobileclient;

import java.util.List;

import com.hp.sdf.ngp.ws.mobileclient.model.MobileAsset;
import com.hp.sdf.ngp.ws.mobileclient.model.MobileComment;

public interface MobileClientService {

	/**
	 * Comment an asset
	 * 
	 * @param assetId
	 * @param userId
	 * @param commentContent
	 */
	public boolean comment(Long assetId, String userId, String commentContent);

	/**
	 * Rating an asset
	 * 
	 * @param assetId
	 * @param userId
	 * @param rating
	 */
	public boolean rating(Long assetId, String userId, Double rating);

	/**
	 * Get all of predefined Category
	 * 
	 * @return
	 */
	public List<String> getAllCategory();

	/**
	 * Get the count of asset via its category name updated time
	 * 
	 * @param categoryName
	 * @return
	 */
	public long getAssetsCountByCategoryName(String categoryName);

	/**
	 * Get a set of asset according to its category name
	 * 
	 * @param categoryName
	 * @param start
	 * @param count
	 * @return
	 */
	public List<MobileAsset> getAssetsByCategoryName(String categoryName,
			int start, int count);

	/**
	 * Get the count of my purchased asset
	 * 
	 * @param userId
	 * @return
	 */
	public long getMyPurchasedAssetCount(String userId);

	/**
	 * Return a set of asset of my purchased asset
	 * 
	 * @param userId
	 * @param start
	 * @param count
	 * @return
	 */
	public List<MobileAsset> getMyPurchasedAsset(String userId, int start,
			int count);

	/**
	 * Get the download URL for a indicated asset
	 * 
	 * @param assetId
	 * @param userId
	 * @param deviceSerial
	 *            the device serial number
	 * @return
	 */
	public String retrieveDownloadLink(long assetId, String userId,
			String deviceSerial);

	/**
	 * Purchase asset
	 * 
	 * @param assetId
	 * @param userId
	 * @return
	 */
	public boolean purchase(long assetId, String userId);

	/**
	 * get Recommended Assets.
	 * 
	 * @return Recommended Assets
	 */
	public List<MobileAsset> getRecommendedAssets();

	/**
	 * get Top Assets.
	 * 
	 * @return top Assets
	 */
	public List<MobileAsset> getTopAssets();

	/**
	 * Return the user comments
	 * 
	 * @param start
	 *            The starting offset when retrieving multiple comments
	 * @param count
	 *            The maximum number of comments to be retrieved
	 */
	public List<MobileComment> getAssetComment(long assetId, int start,
			int count);

	/**
	 * getAssetCommentCount.
	 * 
	 * @param assetId
	 * @return
	 */
	public long getAssetCommentCount(long assetId);

	/**
	 * getAssetRating.
	 * 
	 * @param assetId
	 * @param userId
	 * @return
	 */
	public Double getAssetRating(Long assetId, String userId);

	/**
	 * search.
	 * 
	 * @param keyword
	 * @param start
	 * @param count
	 * @return
	 */
	public List<MobileAsset> search(String keyword, int start, int count);

	/**
	 * searchCount.
	 * 
	 * @param keyword
	 * @return
	 */
	public long searchCount(String keyword);
	
	
	/**
	 * is valid user.
	 * 
	 * @param userId
	 * @param password
	 * @return
	 */
	public boolean isValidUser(String userId, String password);

}
