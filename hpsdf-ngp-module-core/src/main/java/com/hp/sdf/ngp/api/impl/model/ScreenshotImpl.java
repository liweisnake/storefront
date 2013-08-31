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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.hp.sdf.ngp.api.model.Screenshot;
import com.hp.sdf.ngp.model.Asset;
import com.hp.sdf.ngp.model.AssetBinaryVersion;
import com.hp.sdf.ngp.model.ScreenShots;

public class ScreenshotImpl implements Screenshot {

	public String getContentName() {
		return contentName;
	}

	private final static Log log = LogFactory.getLog(BinaryVersionImpl.class);

	public void setAsset(Asset asset) {
		this.asset = asset;
		if (null != this.asset) {
			this.asset.getBrief();// load information to avoid the lazy load
		}
	}

	public void setAssetBinaryVersion(AssetBinaryVersion assetBinaryVersion) {
		this.assetBinaryVersion = assetBinaryVersion;

		if (null != this.assetBinaryVersion) {
			this.assetBinaryVersion.getBrief();// load information to avoid the lazy load
		}
	}

	public byte[] getContent() {
		return content;
	}

	public ScreenShots getScreenShot() {
		if (null == screenShot) {
			screenShot = new ScreenShots();
		}
		return screenShot;
	}

	private ScreenShots screenShot;

	private String contentName;

	private byte[] content;

	private Asset asset;

	private AssetBinaryVersion assetBinaryVersion;

	public ScreenshotImpl() {
		screenShot = new ScreenShots();
	}

	public ScreenshotImpl(ScreenShots screenShot) {
		this.screenShot = screenShot;
		if (null != this.screenShot) {
			this.screenShot.getSequence();// load information to avoid the lazy load
		}
	}

	public Long getAssetId() {
		return asset.getId();

	}

	public Long getBinaryVersionId() {
		if (null != assetBinaryVersion) {
			return assetBinaryVersion.getId();
		}
		return null;
	}

	public String getDescription() {
		return screenShot.getDescription();
	}

	public Long getId() {
		return screenShot.getId();
	}

	public String getMimeType() {
		return screenShot.getMediaType();
	}

	public String getPictureLocation() {
		// return AssetConstants.URIPREFIX + screenShot.getStoreLocation();
		return screenShot.getStoreLocation();
	}

	public void setAssetId(Long assetId) {
		// log.debug("assetId:"+assetId);
		if (null == asset) {
			asset = new Asset();
		}
		asset.setId(assetId);
		screenShot.setAsset(asset);
	}

	public void setBinaryVersionId(Long binaryVersionId) {
		// log.debug("binaryVersionId:"+binaryVersionId);
		if (null == assetBinaryVersion) {
			assetBinaryVersion = new AssetBinaryVersion();
		}
		assetBinaryVersion.setId(binaryVersionId);
		screenShot.setBinaryVersion(assetBinaryVersion);
	}

	public void setDescription(String description) {
		// log.debug("description:"+description);
		screenShot.setDescription(description);
	}

	public void setMimeType(String mimeType) {
		// log.debug("mimeType:"+mimeType);
		screenShot.setMediaType(mimeType);
	}

	public void setPicture(String fileNameSuffix, InputStream pictureSteam) {
		// log.debug("fileNameSuffix:"+fileNameSuffix+",pictureSteam"+pictureSteam);
		contentName = fileNameSuffix;
		try {
			content = new byte[pictureSteam.available()];
			pictureSteam.read(content);
		} catch (IOException e) {
			log.error("Comment exception: " + e);
		}
	}

	public Long getSequence() {
		Long sequence = screenShot.getSequence();
		return sequence;

	}

	public void setSequence(Long sequence) {
		// log.debug("sequence="+sequence);
		screenShot.setSequence(sequence);
	}

	@Override
	public String toString() {
		return "Screenshot[assetId=" + getAssetId() + ",binaryVersionId=" + getBinaryVersionId() + ",description=" + getDescription() + ",mimeType=" + getMimeType() + ",fileNameSuffix=" + getContentName() + ",pictureSteam=" + getContent() + ",sequence=" + getSequence() + "]";
	}
}

// $Id$