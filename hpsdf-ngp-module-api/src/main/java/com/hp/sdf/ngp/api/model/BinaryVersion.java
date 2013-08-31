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
import java.util.Date;
import java.util.List;
import java.util.Set;

public interface BinaryVersion extends AttributeContainer {

	public Long getId();

	public String getVersion();

	/**
	 * Sets the version code of the binary. The version code should NOT be null.
	 * 
	 * @param version
	 *            version code
	 */
	public void setVersion(String version);

	public String getFileName();

	/**
	 * The simple file name of the binary file. The file name should NOT be
	 * null.
	 * 
	 * @param fileName
	 */
	public void setFileName(String fileName);

	public Long getFileSize();

	public void setFileSize(long fileSize);

	/**
	 * Get's the file location of the binary file.
	 * 
	 * @return the file location in the file system.
	 */
	public String getFileLocation();

	/**
	 * returns the file url from the perspective of a web server.
	 * 
	 * @deprecated use retriveBinaryVersionURL() instead
	 * @return the file url
	 */
	public String getFileUrl();

	/**
	 * Sets the binary file input stream. The file inputsteam should NOT be null
	 * when save or update binary version.
	 * 
	 * @param binaryVersionStream
	 */
	public void setFile(InputStream binaryVersionStream);

	public Date getCreateDate();

	// public void setCreateDate(Date createDate);
	public Date getUpdateDate();

	// public void setUpdateDate(Date updateDate);
	public String getStatus();

	public void setStatus(String status);

	/**
	 * Returns asset name.
	 * 
	 * @return asset name
	 */
	public String getName();

	/**
	 * Gets shot description. Usually used as one sentence to discribe an asset
	 * in some asset list.
	 * 
	 * @return shot description.
	 */
	public String getBrief();

	/**
	 * Gets long description. Usually used as detailed information of an asset.
	 * 
	 * @return detailed description.
	 */
	public String getDescription();

	/**
	 * Gets the temporary asset name.
	 * 
	 * Asset binary version may change asset name, this is the duplication of
	 * asset.name. This value should be copied to asset object when this new
	 * version get published. API invoker needs to maintain the consistency.
	 * Product will not guarantee this copy.
	 * 
	 * @return
	 */
	public void setName(String name);

	public void setBrief(String brief);

	public void setDescription(String description);

	/** Returns the big thumbnail URL */
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

	/**
	 * The thumbnail file name is predefined by the system. But the file name
	 * suffix could be jpg or png.
	 */
	public void setBigThumbnail(String fileNameSuffix, InputStream buf);

	public void setMedThumbnail(String fileNameSuffix, InputStream buf);

	public void setThumbnail(String fileNameSuffix, InputStream buf);

	public Long getAssetId();

	/**
	 * Lazy loads StoreClientAsset.
	 * 
	 * @return
	 */
	public StoreClientAsset getStoreClientAsset();
	
	/**
	 * Sets the asset ID. The asset ID should NOT be null when save or update
	 * binary version.
	 * 
	 * @param assetId
	 */
	public void setAssetId(long assetId);

	public String getExternalId();

	public void setExternalId(String externalId);

	public Date getExpireDate();

	public void setExpireDate(Date expireDate);

	/**
	 * Gets the temporary asset recommend order.
	 * 
	 * Asset binary version may change asset recommend order, this is the
	 * duplication of asset.recommendOrder. This value should be copied to asset
	 * object when this new version get published. API invoker needs to maintain
	 * the consistency. Product will not guarantee this copy.
	 * 
	 * @return
	 */
	public Integer getRecommendOrder();

	public void setRecommendOrder(Integer recommendOrder);

	public Date getRecommendStartDate();

	public void setRecommendStartDate(Date recommendStartDate);

	public Date getRecommendDueDate();

	public void setRecommendDueDate(Date recommendDueDate);

	public Date getPublishDate();

	public void setPublishDate(Date publishDate);

	public Date getNewArrivalDueDate();

	public void setNewArrivalDueDate(Date newArrivalDueDate);

	public Date getRecommendUpdateDate();

	public void setRecommendUpdateDate(Date recommendUpdateDate);
	
	/**
	 * Gets the temporary asset parent ID.
	 * 
	 * Asset binary version may change asset parent, this is the duplication of
	 * asset.parentId. This value should be copied to asset object when this new
	 * version get published. API invoker needs to maintain the consistency.
	 * Product will not guarantee this copy.
	 * 
	 * @return
	 */
	public Long getAssetParentId();

	public void setAssetParentId(long assetParentId);

	/**
	 * @return all screenshots belong to this version.
	 */
	public List<Screenshot> getScreenShots();

	public Set<String> getTags();

	/**
	 * Sets all tags of a binary version.
	 * 
	 * When updating the tags, the old tags shall all be removed from the
	 * version and all tags will be added to it. If any tag is new to the
	 * system, the system will create it automatically.
	 * 
	 * @param tags
	 */
	public void setTags(List<String> tags);

	/**
	 * Gets binary version directly associated categories.
	 * 
	 * The returned categories do not include the parents of directly
	 * associated categories.
	 * 
	 * @return associated categories
	 */
	public Set<AssetCategory> getCategories();
}

// $Id$