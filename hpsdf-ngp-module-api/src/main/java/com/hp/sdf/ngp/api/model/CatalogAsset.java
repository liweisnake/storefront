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

import java.io.InputStream;
import java.math.BigDecimal;
import java.util.Currency;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Abstract interface for assets in the catalog.
 */
public interface CatalogAsset extends Asset, AttributeContainer {

	/* get methods go here */
	public Set<String> getPlatforms();

	public AssetProvider getProvider();
	
	public void setExternalId(String externalId);

	/**
	 * Sets provider Id. The provider Id should not be null if the asset has
	 * related provider information.
	 * 
	 * @param providerId
	 */
	public void setProvider(Long providerId);

	public Set<RestrictionType> getRestrictionTypes();

	public Map<Currency, BigDecimal> getPrices();

	/**
	 * Get the sub asset belong to this asset
	 * 
	 * @return the sub assets belong to this asset
	 */
	public List<CatalogAsset> getSubAssets();

	/* set methods below */

	/** The latest update date and time */
	public void setUpdateDate(Date date);

	public void setName(String name);

	public void setBrief(String brief);

	public void setDescription(String description);

	/**
	 * Sets status name. The status name must already exist in the status table
	 * and the status type is "ASSET".
	 * 
	 * @param status
	 */
	public void setStatus(String status);

	/** The original content create date and time */
	public void setCreationDate(Date date);

	/** The home page of the asset */
	public void setHomePage(String url);

	/** The source of the asset, e.g. JIL warehouse, Android Marketplace, etc. */
	public void setSource(String sourceName);

	/**
	 * The thumbnail shall always use image MIME type
	 */
	public void setBigThumbnail(String fileNameSuffix, InputStream buf);

	public void setMedThumbnail(String fileNameSuffix, InputStream buf);

	public void setThumbnail(String fileNameSuffix, InputStream buf);

	public void setRecommendOrder(int recommendOrder);

	public void setRecommendStartDate(Date recommendStartDate);

	public void setRecommendDueDate(Date recommendDueDate);

	public void setPublishDate(Date publishDate);

	public void setNewArrivalDueDate(Date newArrivalDueDate);

	public void setRecommendUpdateDate(Date recommendUpdateDate);

	/**
	 * Sets all tags of a binary version.
	 * 
	 * When updating the tags, the old tags shall all be removed from the asset
	 * and all tags will be added to it. If any tag is new to the system, the
	 * system will create it automatically.
	 * 
	 * @param tags
	 */
	public void setTags(List<String> tags);
	
}
