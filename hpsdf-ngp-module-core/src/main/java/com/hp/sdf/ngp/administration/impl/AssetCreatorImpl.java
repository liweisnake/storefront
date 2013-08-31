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
package com.hp.sdf.ngp.administration.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

import com.hp.sdf.ngp.administration.AssetCreator;
import com.hp.sdf.ngp.administration.AssetEntry;
import com.hp.sdf.ngp.administration.BinaryContent;
import com.hp.sdf.ngp.administration.descriptor.AssetDescriptor;
import com.hp.sdf.ngp.common.exception.BatchUploadFailureException;
import com.hp.sdf.ngp.model.Asset;
import com.hp.sdf.ngp.model.AssetBinaryVersion;
import com.hp.sdf.ngp.model.Category;
import com.hp.sdf.ngp.model.Platform;
import com.hp.sdf.ngp.model.Status;
import com.hp.sdf.ngp.service.ApplicationService;
import com.hp.sdf.ngp.service.AssetPictureType;

@Component
public class AssetCreatorImpl implements AssetCreator {

	private static final Log log = LogFactory.getLog(AssetCreatorImpl.class);

	private String initBatchUploadStatus = "published";

	@Resource
	private ApplicationService appService;

	public String getInitBatchUploadStatus() {
		return initBatchUploadStatus;
	}

	public void setInitBatchUploadStatus(String initBatchUploadStatus) {
		this.initBatchUploadStatus = initBatchUploadStatus;
	}

	private Category getCategoryByName(String name) {
		if (StringUtils.isBlank(name))
			return null;
		List<Category> categories = appService.getAllCategory(0, Integer.MAX_VALUE);
		for (Category ctg : categories) {
			if (ctg.getName().equalsIgnoreCase(name))
				return ctg;
		}
		return null;
	}

	private Platform getPlatformByName(String name) {
		if (StringUtils.isBlank(name))
			return null;
		List<Platform> platforms = appService.getAllPlatform(0, Integer.MAX_VALUE);
		for (Platform plat : platforms) {
			if (plat.getName().equalsIgnoreCase(name))
				return plat;
		}
		return null;
	}

	private Status getStatusByName(String name) {
		if (StringUtils.isBlank(name))
			return null;
		List<Status> statuses = appService.getAllStatus();
		for (Status status : statuses) {
			if (status.getStatus().equalsIgnoreCase(name))
				return status;
		}
		return null;
	}

	public void createAsset(AssetDescriptor assetDescriptor, AssetEntry entry) {
		log.debug("createAsset>>Enter createAsset ");
		Asset asset = new Asset();
		Date now = new Date();
		asset.setCreateDate(now);
		asset.setDownloadCount(0L);
		asset.setAverageUserRating(0.0);

		Platform platform = getPlatformByName(assetDescriptor.getPlatform());
		if (platform == null) {
			throw new BatchUploadFailureException("Can not find indicate platform. platform name=" + assetDescriptor.getPlatform());
		}

		asset.setUpdateDate(now);
		asset.setStatus(getStatusByName(initBatchUploadStatus));
		asset.setAuthorid(assetDescriptor.getAuthor());
		asset.setName(assetDescriptor.getName());
		asset.setBrief(assetDescriptor.getBrief());
		asset.setDescription(assetDescriptor.getDescription());

		Category ctg = getCategoryByName(assetDescriptor.getCategory());
		if (ctg == null) {
			throw new BatchUploadFailureException("Can not find category. category name=" + assetDescriptor.getCategory());
		}

		List<Long> categoryList = new ArrayList<Long>();
		categoryList.add(ctg.getId());
		log.debug("createAsset>>save app " + asset.getName());
		appService.saveAsset(asset, categoryList);
		appService.associatePlatform(asset.getId(), platform.getId());

		BinaryContent bin = entry.get(assetDescriptor.getPreview());
		if (bin != null) {
			appService.saveAssetPicture(asset.getId(), bin.getContent(), AssetPictureType.THUMBNAILBIGIMAGE, "THUMBNAILBIGIMAGE.jpg");

		}
		bin = entry.get(assetDescriptor.getThumbnail());
		if (bin != null) {
			appService.saveAssetPicture(asset.getId(), bin.getContent(), AssetPictureType.THUMBNAILIMAGE, "THUMBNAILIMAGE.jpg");
		}
		AssetBinaryVersion binaryVersion = new AssetBinaryVersion();

		binaryVersion.setAsset(asset);

		binaryVersion.setCreateDate(new Date());

		binaryVersion.setStatus(getStatusByName(initBatchUploadStatus));

		binaryVersion.setVersion(assetDescriptor.getVersion());

		String fileName = assetDescriptor.getFile();
		if (fileName.contains("\\")) {
			int beginIndex = fileName.lastIndexOf("\\");
			binaryVersion.setFileName(fileName.substring(beginIndex + 1));
		} else {
			binaryVersion.setFileName(fileName);
		}

		bin = entry.get(fileName);

		log.debug("createAssetBinary>>save assetBinary " + asset.getName());
		appService.saveAssetBinary(bin.getContent(), binaryVersion);
		appService.updateAssetVersion(asset.getId(), binaryVersion.getId());

	}

}

// $Id$