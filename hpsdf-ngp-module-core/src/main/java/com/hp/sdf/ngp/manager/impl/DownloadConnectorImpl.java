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
package com.hp.sdf.ngp.manager.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

import com.hp.sdf.ngp.common.annotation.Value;
import com.hp.sdf.ngp.common.constant.AssetConstants;
import com.hp.sdf.ngp.manager.DownloadConnector;
import com.hp.sdf.ngp.manager.DownloadManager;
import com.hp.sdf.ngp.model.Asset;
import com.hp.sdf.ngp.model.AssetBinaryVersion;
import com.hp.sdf.ngp.service.ApplicationService;

@Component
public class DownloadConnectorImpl implements DownloadConnector {

	Log log = LogFactory.getLog(DownloadConnectorImpl.class);

	private String uriPrefix;

	private String saveFilePrefix;

	public String getSaveFilePrefix() {
		return saveFilePrefix;
	}

	@Value("file.path.prefix")
	public void setSaveFilePrefix(String saveFilePrefix) {
		this.saveFilePrefix = saveFilePrefix;
	}

	@Resource
	private DownloadManager downloadManager;

	@Resource
	private ApplicationService assetService;

	public String getUriPrefix() {
		return uriPrefix;
	}

	@Value("uriPrefix")
	public void setUriPrefix(String uriPrefix) {
		this.uriPrefix = uriPrefix;
	}

	@PostConstruct
	public void init() {
		this.downloadManager.registerConnector(
				AssetConstants.ASSET_SOURCE_STOREFRONT, this);
	}

	public byte[] retrieveBinary(Long assetId,Long versionId, String deviceSerial) {
		byte[] bytes = null;
		AssetBinaryVersion binary = null;

		String binaryFile = null;
		if(versionId==null)
		{
			Asset asset=assetService.getAsset(assetId);
			versionId=asset.getCurrentVersionId();
		}
		binary = assetService.getAssetBinaryById(versionId);

		if (binary != null) {
			// The link for download, which aslo works in Portlet
			binaryFile = this.getSaveFilePrefix();
			binaryFile += binary.getLocation();
			log.debug("binary file URI is: " + binaryFile);
			InputStream stream = null;
			try {
				stream = new FileInputStream(new File(binaryFile));
				bytes = new byte[stream.available()];
				stream.read(bytes);
			} catch (Exception e) {
				log.error("Unable to read binary file.", e);
			} finally {
				try {
					if (stream != null) {
						stream.close();
					}
				} catch (Exception e) {
					log.warn("Unable to close file input stream.", e);
				}
			}
		}

		return bytes;
	}

	public String retrievedownloadURI(Long assetId,Long versionId,String deviceSerial) {
		String downloadURI = null;
		AssetBinaryVersion binary = null;
		if(versionId==null)
		{
			Asset asset=assetService.getAsset(assetId);
			versionId=asset.getCurrentVersionId();
		}
		binary = assetService.getAssetBinaryById(versionId);

		if (binary != null) {
			final AssetBinaryVersion downloadBinary = binary;
			// The link for download, which aslo works in Portlet
			downloadURI = this.getUriPrefix();
			downloadURI += downloadBinary.getLocation();
		}
		log.debug("binary URI is: " + downloadURI);

		return downloadURI;
	}

}

// $Id$