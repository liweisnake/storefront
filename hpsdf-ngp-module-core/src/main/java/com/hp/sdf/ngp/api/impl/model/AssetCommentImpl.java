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

import java.util.Date;

import com.hp.sdf.ngp.api.model.AssetComment;
import com.hp.sdf.ngp.model.Asset;
import com.hp.sdf.ngp.model.Comments;
import com.hp.sdf.ngp.model.SubscriberProfile;

public class AssetCommentImpl implements AssetComment {

	// private final static Log log = LogFactory.getLog(AssetCommentImpl.class);

	public void setAsset(Asset asset) {
		this.asset = asset;
		if (this.asset != null) {
			asset.getName();//load information to avoid the lazy load
		}
	}

	public Comments getComments() {
		if (null == comments) {
			comments = new Comments();
		}
		return comments;
	}

	public void setSubscriberProfile(SubscriberProfile subscriberProfile) {
		this.subscriberProfile = subscriberProfile;
		if (this.subscriberProfile != null) {
			subscriberProfile.getUserId();//load information to avoid the lazy load
		}
	}

	private Comments comments;

	private Asset asset;

	private SubscriberProfile subscriberProfile;

	public AssetCommentImpl() {
		comments = new Comments();
	}

	public AssetCommentImpl(Comments comments) {
		super();
		this.comments = comments;
		if (this.comments != null) {
			comments.getContent();//load information to avoid the lazy load
		}

	}

	public Long getAssetId() {
		return asset.getId();
	}

	public String getAssetVersion() {
		return comments.getAssetVersion();
	}

	public String getComment() {
		return comments.getContent();
	}

	public Date getDate() {
		return comments.getCreateDate();
	}

	public String getTitle() {
		return comments.getTitle();
	}

	public String getUserDisplayName() {
		if (null != subscriberProfile) {
			return subscriberProfile.getDisplayName();
		}
		return null;
	}

	public String getUserId() {
		return comments.getUserid();
	}

	public void setUserId(String userId) {

		comments.setUserid(userId);
	}

	public String getExternalId() {
		return 	comments.getExternalId();
	}

	public Date getUpdateDate() {
		return comments.getUpdateDate();
	}

	public void setAssetId(Long assetId) {
	
		
		asset = new Asset();
		asset.setId(assetId);
	}

	public void setComment(String comment) {
		// log.debug("comment:" + comment);
		comments.setContent(comment);
	}

	public void setExternalId(String externalId) {

		comments.setExternalId(externalId);
	}

	public void setTitle(String title) {
		// log.debug("title:" + title);
		comments.setTitle(title);
	}

	public void setAssetVersion(String assetVersion) {
		comments.setAssetVersion(assetVersion);
	}

	@Override
	public String toString() {
		return "AssetComment[userId=" + getUserId() + ",assetId=" + getAssetId() + ",comment=" + getComment() + ",externalId=" + getExternalId()
				+ ",title=" + getTitle() + "]";
	}
}

// $Id$