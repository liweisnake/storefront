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

package com.hp.sdf.ngp.api.model;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * The lite asset interface to retrieve basic asset information for the simple end
 * subscribers. 
 * The simple end means the client device with poor calculation resource, 
 * usually mobile clients. So the asset metadata provided need to be kept simple. 
 * 
 */
public interface Asset {
	

	/**
	 * Returns asset ID.
	 * @return asset ID
	 */
	public Long getId();
	
	/**
	 * Returns asset ID.
	 * @return asset ID
	 * @deprecated 
	 *  	use getId() instead
	 */
	public Long getAssetId();
	
	public String getExternalId();

	/**
	 * Returns asset name.
	 * @return asset name
	 */
	public String getName();

	/**
	 * Gets shot description.
	 * Usually used as one sentence to discribe an asset in some asset list.
	 * @return shot description.
	 */
	public String getBrief();
	
	/**
	 * Gets long description.
	 * Usually used as detailed information of an asset.
	 * @return detailed description.
	 */
	public String getDescription();

	/** @return tag names of an asset */
	public Set<String> getTags();
	
	public Set<AssetCategory> getCategories();
	
	public List<Screenshot> getScreenShots();

	/** Returns the thumbnail URLs */
	public String getThumbnailBigUrl();

	public String getThumbnailMedUrl();

	public String getThumbnailUrl();
	
	/**
	 * Gets file location of big thumbnail.
	 * @return
	 */
	public String getThumbnailBigLocation();

	/**
	 * Returns file location of medium thumbnail.
	 * @return
	 */
	public String getThumbnailMedLocation();

	/**
	 * Returns file location of thumbnail.
	 * @return
	 */
	public String getThumbnailLocation();

	/** Returns the asset price for a given currency type */
	public BigDecimal getPrice(Currency currency);

	/** @return current asset version ID. */
	public Long getCurrentVersionId();
	
	/** @return the current published version code of an asset */
	public String getCurrentVersion();

	/** The source of the asset, e.g. JIL warehouse, Android Marketplace, etc. */
	public String getSource();

//	public String getVersionDescription();

	/** Retrieves the publish date of the current version */
	public Date getUpdateDate();

	/** Retrieves the original asset creation date */
	public Date getCreateDate();

	
	/** 
	 * Returns the URL of the asset home page.
	 * Note that a provider may set different home pages for its different assets.
	 * @return the URL of the asset provider 
	 */
	public String getHomePage();

	/** Returns the average user rating, a decimal between 1 and 5 */
	public Double getAverageUserRating();
	
		
	/** @return the status */
	public String getStatus();
	
	/** the status display name	 */
	public String getStatusDisplayName();
	
	/** @return the download count of the asset */
	public Long getDownloadCount();
	
	public Integer getRecommendOrder();
	
	public Date getRecommendStartDate();

	public Date getRecommendDueDate();
	
	public Date getPublishDate();
	
	public Date getNewArrivalDueDate();
	
	public Date getRecommendUpdateDate();
}
