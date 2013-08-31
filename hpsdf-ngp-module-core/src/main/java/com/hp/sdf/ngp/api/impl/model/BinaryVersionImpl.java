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
package com.hp.sdf.ngp.api.impl.model;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.hp.sdf.ngp.api.model.AssetCategory;
import com.hp.sdf.ngp.api.model.BinaryVersion;
import com.hp.sdf.ngp.api.model.Screenshot;
import com.hp.sdf.ngp.api.model.StoreClientAsset;
import com.hp.sdf.ngp.common.constant.AssetConstants;
import com.hp.sdf.ngp.model.Asset;
import com.hp.sdf.ngp.model.AssetBinaryVersion;
import com.hp.sdf.ngp.model.Status;

public class BinaryVersionImpl implements BinaryVersion {

	private final static Log log = LogFactory.getLog(BinaryVersionImpl.class);

	private Asset asset;

	private AssetBinaryVersion assetBinaryVersion;

	private Set<AssetCategory> assetCategories;

	private String bigName;

	private byte[] bigThumbnail;

	private byte[] content;

	private String medName;

	private byte[] medThumbnail;

	private Map<String, List<Object>> objects;

	private List<Screenshot> screenshots;

	private Status status;

	private StoreClientAsset storeClientAsset;

	private List<String> tags;

	private byte[] thumbnail;

	private String thumName;

	public BinaryVersionImpl() {
		assetBinaryVersion = new AssetBinaryVersion();
	}

	public BinaryVersionImpl(AssetBinaryVersion assetBinaryVersion) {
		this.assetBinaryVersion = assetBinaryVersion;
		if (null != this.assetBinaryVersion) {
			this.assetBinaryVersion.getBrief();// load information to avoid the lazy load
		}
	}

	public void cleanAfterUpdate() {
		this.content = null;
		this.bigThumbnail = null;
		this.medThumbnail = null;
		this.thumbnail = null;
	}

	public AssetBinaryVersion getAssetBinaryVersion() {
		if (null == assetBinaryVersion) {
			assetBinaryVersion = new AssetBinaryVersion();
		}
		return assetBinaryVersion;
	}

	public Long getAssetId() {
		if (null != asset) {
			return asset.getId();
		}

		// TODO check lazy load problem
		if (null != this.getAssetBinaryVersion() && null != this.getAssetBinaryVersion().getAsset()) {
			return this.getAssetBinaryVersion().getAsset().getId();
		}

		return null;
	}

	public Long getAssetParentId() {
		Long assetParentId = assetBinaryVersion.getOwnerAssetParentId();
		return assetParentId;
		// if(null != asset && null != asset.getAsset()){
		// return asset.getAsset().getId();
		// }
	}

	public Map<String, List<Object>> getAttributes() {
		return objects;
	}

	public List<Object> getAttributeValue(String attributeName) {
		if (null != objects) {
			return objects.get(attributeName);
		}
		return null;
	}

	public String getBigName() {
		return bigName;
	}

	public byte[] getBigThumbnail() {
		return bigThumbnail;
	}

	public String getBrief() {
		return assetBinaryVersion.getBrief();
	}

	public Set<AssetCategory> getCategories() {
		return assetCategories;
	}

	public byte[] getContent() {
		return content;
	}

	public Date getCreateDate() {
		return assetBinaryVersion.getCreateDate();
	}

	public String getDescription() {
		return assetBinaryVersion.getDescription();
	}

	public Date getExpireDate() {
		return assetBinaryVersion.getExpireDate();
	}

	public String getExternalId() {
		return assetBinaryVersion.getExternalId();
	}

	public String getFileLocation() {
		// return AssetConstants.URIPREFIX + assetBinaryVersion.getLocation();
		return assetBinaryVersion.getLocation();
	}

	public String getFileName() {
		return assetBinaryVersion.getFileName();
	}

	public Long getFileSize() {
		BigDecimal bigDecimal = assetBinaryVersion.getFileSize();
		if (null != bigDecimal) {
			return bigDecimal.longValue();
		}
		return 0L;
	}

	public String getFileUrl() {
		// return getPrefix() + assetBinaryVersion.getLocation();
		return assetBinaryVersion.getLocation();
	}

	public Long getId() {
		return assetBinaryVersion.getId();
	}

	public String getMedName() {
		return medName;
	}

	public byte[] getMedThumbnail() {
		return medThumbnail;
	}

	public String getName() {
		return assetBinaryVersion.getName();
	}

	public Date getNewArrivalDueDate() {
		return assetBinaryVersion.getNewArrivalDueDate();
	}

	public Date getPublishDate() {
		return assetBinaryVersion.getPublishDate();
	}

	public Date getRecommendDueDate() {
		return assetBinaryVersion.getRecommendDueDate();
	}

	public Integer getRecommendOrder() {
		Long order = assetBinaryVersion.getRecommendOrder();
		if (null != order) {
			return order.intValue();
		} else {
			return null;
		}

	}

	public Date getRecommendStartDate() {
		return assetBinaryVersion.getRecommendStartDate();
	}

	public Date getRecommendUpdateDate() {
		return assetBinaryVersion.getRecommendUpdateDate();
	}

	public List<Screenshot> getScreenShots() {
		return screenshots;
	}

	public String getStatus() {
		if (null != status) {
			return status.getStatus();
		}
		return null;
	}

	public StoreClientAsset getStoreClientAsset() {
		return storeClientAsset;

	}

	public Set<String> getTags() {
		// TODO Anders Zhu
		Set<String> tag_set;
		if (tags != null)
			tag_set = new HashSet<String>(tags);
		else
			tag_set = new HashSet<String>();
		return tag_set;
	}

	public byte[] getThumbnail() {
		return thumbnail;
	}

	public String getThumbnailBigLocation() {
		return assetBinaryVersion.getThumbnailBigLocation();
	}

	public String getThumbnailBigUrl() {
		// return getPrefix() + assetBinaryVersion.getThumbnailBigLocation();
		return assetBinaryVersion.getThumbnailBigLocation();
	}

	public String getThumbnailLocation() {
		return assetBinaryVersion.getThumbnailLocation();
	}

	public String getThumbnailMedLocation() {
		return assetBinaryVersion.getThumbnailMiddleLocation();
	}

	public String getThumbnailMedUrl() {
		// return getPrefix() + assetBinaryVersion.getThumbnailMiddleLocation();
		return assetBinaryVersion.getThumbnailMiddleLocation();
	}

	public String getThumbnailUrl() {
		// return getPrefix() + assetBinaryVersion.getThumbnailLocation();
		return assetBinaryVersion.getThumbnailLocation();
	}

	public String getThumName() {
		return thumName;
	}

	public Date getUpdateDate() {
		return assetBinaryVersion.getUpdateDate();
	}

	public String getVersion() {
		return assetBinaryVersion.getVersion();
	}

	public void setAsset(Asset asset) {
		this.asset = asset;
		if (null != this.asset) {
			this.asset.getBrief();// load information to avoid the lazy load
		}
	}

	public void setAssetCategories(Set<AssetCategory> assetCategories) {
		this.assetCategories = assetCategories;
	}

	public void setAssetId(long assetId) {
		// log.debug("assetId:"+assetId);
		if (null == asset) {
			asset = new Asset();
		}
		asset.setId(assetId);
		assetBinaryVersion.setAsset(asset);
	}

	public void setAssetParentId(long assetParentId) {
		// log.debug("assetParentId:"+assetParentId);
		// if(null == asset ){
		// asset = new Asset();
		// }
		// Asset pAsset = new Asset();
		// pAsset.setId(assetParentId);
		// asset.setAsset(pAsset);
		assetBinaryVersion.setOwnerAssetParentId(assetParentId);
	}

	public void setBigThumbnail(String fileNameSuffix, InputStream buf) {
		// log.debug("bigFileNameSuffix:"+fileNameSuffix+",buf:"+buf);
		bigName = fileNameSuffix;
		try {
			bigThumbnail = new byte[buf.available()];
			buf.read(bigThumbnail);
		} catch (IOException e) {
			log.error("Comment exception: " + e);
		}
	}

	public void setBrief(String brief) {
		// log.debug("brief:"+brief);
		assetBinaryVersion.setBrief(brief);
	}

	public void setDescription(String description) {
		// log.debug("descriptiion:"+description);
		assetBinaryVersion.setDescription(description);
	}

	public void setExpireDate(Date expireDate) {
		// log.debug("expireDate:"+expireDate);
		assetBinaryVersion.setExpireDate(expireDate);
	}

	public void setExternalId(String externalId) {
		// log.debug("externalId:"+externalId);
		assetBinaryVersion.setExternalId(externalId);
	}

	public void setFile(InputStream binaryVersionStream) {
		// log.debug("file:"+binaryVersionStream);
		try {
			content = new byte[binaryVersionStream.available()];
			binaryVersionStream.read(content);
		} catch (IOException e) {
			log.error("Comment exception: " + e);
		}
	}

	public void setFileName(String fileName) {
		// log.debug("fileName:"+fileName);
		assetBinaryVersion.setFileName(fileName);
	}

	public void setFileSize(long fileSize) {
		// log.debug("fileSize:"+fileSize);
		assetBinaryVersion.setFileSize(new BigDecimal(fileSize));
	}

	public void setMedThumbnail(String fileNameSuffix, InputStream buf) {
		// log.debug(" medFileNameSuffix:"+fileNameSuffix+",buf:"+buf);
		medName = fileNameSuffix;
		try {
			medThumbnail = new byte[buf.available()];
			buf.read(medThumbnail);
		} catch (IOException e) {
			log.error("Comment exception: " + e);
		}
	}

	public void setName(String name) {
		// log.debug("name:"+name);
		assetBinaryVersion.setName(name);
	}

	public void setNewArrivalDueDate(Date newArrivalDueDate) {
		// log.debug("newArrivalDueDate:"+newArrivalDueDate);
		assetBinaryVersion.setNewArrivalDueDate(newArrivalDueDate);
	}

	public void setObjects(Map<String, List<Object>> objects) {
		this.objects = objects;
	}

	public void setPublishDate(Date publishDate) {
		// log.debug("publishDate:"+publishDate);
		assetBinaryVersion.setPublishDate(publishDate);
	}

	public void setRecommendDueDate(Date recommendDueDate) {
		// log.debug("recommendDueDate:"+recommendDueDate);
		assetBinaryVersion.setRecommendDueDate(recommendDueDate);
	}

	public void setRecommendOrder(Integer recommendOrder) {
		// log.debug("recommendOrder:"+recommendOrder);
		assetBinaryVersion.setRecommendOrder(new Long(recommendOrder));
	}

	public void setRecommendStartDate(Date recommendStartDate) {
		// log.debug("recommendStartDate:"+recommendStartDate);
		assetBinaryVersion.setRecommendStartDate(recommendStartDate);
	}

	public void setRecommendUpdateDate(Date recommendUpdateDate) {
		// log.debug("recommendUpdateDate:"+recommendUpdateDate);
		assetBinaryVersion.setRecommendUpdateDate(recommendUpdateDate);
	}

	public void setScreenshots(List<Screenshot> screenshots) {
		this.screenshots = screenshots;
	}

	// public long getOwnerAssetParentId() {
	// Long ownerId = assetBinaryVersion.getOwnerAssetParentId();
	// if (null != ownerId) {
	// return ownerId;
	// }
	// throw new RuntimeException();
	// }

	// public void setOwnerAssetParentId(long ownerAssetParentId) {
	// // log.debug("ownerAssetParentId:"+ownerAssetParentId);
	// assetBinaryVersion.setOwnerAssetParentId(ownerAssetParentId);
	// }

	public void setStatus(Status status) {
		this.status = status;
	}

	public void setStatus(String status) {
		// log.debug("status:"+status);
		Status modelStatus = new Status();
		modelStatus.setStatus(status);
		// this.assetBinaryVersion.setStatus(modelStatus);
		this.status = modelStatus;
	}

	public void setStoreClientAsset(StoreClientAsset storeClientAsset) {
		this.storeClientAsset = storeClientAsset;
	}

	public void setTags(List<String> tags) {
		this.tags = tags;
	}

	public void setThumbnail(String fileNameSuffix, InputStream buf) {
		// log.debug("fileNameSuffix:"+fileNameSuffix+",buf:"+buf);
		thumName = fileNameSuffix;
		try {
			thumbnail = new byte[buf.available()];
			buf.read(thumbnail);
		} catch (IOException e) {
			log.error("Comment exception: " + e);
		}
	}

	public void setVersion(String version) {
		// log.debug("version:"+version);
		assetBinaryVersion.setVersion(version);
	}

	@Override
	public String toString() {
		return "BinaryVersion:[assetId=" + getAssetId() + ",brief=" + getBrief() + ",description=" + getDescription() + ",file=" + getContent() + ",fileName=" + getFileName() + ",fileSize=" + getFileSize() + ",name=" + getName() + ",status=" + getStatus() + ",version=" + getVersion() + ",externalId=" + getExternalId() + ",expireDate=" + getExpireDate() + ",newArrivalDueDate=" + getNewArrivalDueDate() + ",publishDate=" + getPublishDate() + ",recommendDueDate=" + getRecommendDueDate() + ",recommendOrder=" + getRecommendOrder() + ",recommendStartDate=" + getRecommendStartDate() + ",recommendUpdateDate=" + getRecommendUpdateDate() + ",assetParentId=" + getAssetParentId() + ",fileBigNameSuffix=" + getBigName() + ",bigBuf=" + getBigThumbnail() + ",fileMedNameSuffix=" + getMedName() + ",medBuf="
				+ getMedThumbnail() + ",fileNameSuffix=" + getThumName() + ",buf=" + getThumbnail() + "]";
	}

	private String getPrefix() {
		if (AssetConstants.URIPREFIX == null) {
			return "";
		}

		return AssetConstants.URIPREFIX.replace("//", "/");
	}

}

// $Id$