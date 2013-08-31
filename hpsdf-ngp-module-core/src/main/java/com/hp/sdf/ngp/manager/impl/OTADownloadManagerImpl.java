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

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.hp.sdf.ngp.common.annotation.Value;
import com.hp.sdf.ngp.manager.ApplicationManager;
import com.hp.sdf.ngp.manager.DownloadDescriptor;
import com.hp.sdf.ngp.manager.OTADownloadManager;
import com.hp.sdf.ngp.model.Asset;
import com.hp.sdf.ngp.model.AssetBinaryVersion;
import com.hp.sdf.ngp.service.ApplicationService;

@Component
public class OTADownloadManagerImpl implements OTADownloadManager {

	@Resource
	private ApplicationService applicationService;

	@Resource
	private ApplicationManager applicationManager;

	private String otaServerURL;

	public String getOtaServerURL() {
		return otaServerURL;
	}

	@Value("otaserver.url")
	public void setOtaServerURL(String otaServerURL) {
		this.otaServerURL = otaServerURL;
	}

	public String retrieveOTADownloadURI(Long assetId, Long versionId,
			String userId, String deviceSerial) {

		String url = this.otaServerURL + "download/?assetId=" + LongToString(assetId)
				+ "&versionId=" + LongToString(versionId) + "&userId=" + userId
				+ "&deviceSerial=" + deviceSerial;
		return url;
	}

	private String LongToString(Long value) {
		if (value != null) {
			return String.valueOf(value);
		}
		return "";
	}

	public String getDownloadDescriptor(Long assetId, Long versionId,
			String userId, String notifyUrl) {
		Asset asset = applicationService.getAsset(assetId);
		String url = this.applicationManager.retrieveDownloadLink(assetId,
				versionId, userId, null);
		if (versionId == null) {
			versionId = asset.getCurrentVersionId();
		}
		AssetBinaryVersion abv = this.applicationService
				.getAssetBinaryById(versionId);
		DownloadDescriptor dd = new DownloadDescriptor(this.getContentType(abv
				.getFileName()), abv.getFileSize(), url);
		dd.setName(asset.getName());
		dd.setDescription(asset.getDescription());
		if (notifyUrl != null)
			dd.setInstallNotifyURI(notifyUrl + "?assetId=" + LongToString(assetId)
					+ "&versionId=" + LongToString(versionId) + "&userId=" + userId);
		else
			dd.setInstallNotifyURI(this.otaServerURL + "nofity/?assetId="
					+ LongToString(assetId) + "&versionId=" + LongToString(versionId)+ "&userId=" + userId);
		return dd.toXML();
	}

	private String getContentType(String fileName) {
		String result = null;
		String ext = fileName.substring(fileName.indexOf(".") + 1);
		if (ext.equals("jpeg") || ext.equals("jpg") || ext.equals("JPG")) {
			result = "image/jpeg";
		} else if (ext.equals("gif")) {
			result = "image/gif";
		} else if (ext.equals("png")) {
			result = "image/x-png";
		} else if (ext.equals("bmp")) {
			result = "image/x-ms-bmp";
		} else if (ext.equals("mp3")) {
			result = "audio/x-mpeg";
		} else if (ext.equals("mpeg") || ext.equals("mpg") || ext.equals("mpe")) {
			result = "video/mpeg";
		} else if (ext.equals("avi")) {
			result = "video/x-msvideo";
		} else if (ext.equals("zip")) {
			result = "application/zip";
		} else if (ext.equals("exe") || ext.equals("bin")) {
			result = "application/octet-stream";
		} else if (ext.equals("doc")) {
			result = "application/msword";
		} else if (ext.equals("pdf")) {
			result = "application/pdf";
		} else if (ext.equals("xls")) {
			result = "application/vnd.ms-excel";
		} else if (ext.equals("ppt")) {
			result = "application/vnd.ms-powerpoint";
		} else if (ext.equals("swf")) {
			result = "application/x-shockwave-flash";
		} else if (ext.equals("tar")) {
			result = "application/x-tar";
		} else if (ext.equals("rm")) {
			result = "audio/x-pn-realaudio";
		} else if (ext.equals("rpm")) {
			result = "audio/x-pn-realaudio-plugin";
		} else if (ext.equals("")) {
			result = "";
		}
		return result;
	}

}

// $Id$