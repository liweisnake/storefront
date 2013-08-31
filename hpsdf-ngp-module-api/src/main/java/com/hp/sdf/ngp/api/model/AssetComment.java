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

import java.util.Date;

public interface AssetComment {

	/**
	 * The id of the asset to which this comment belongs The asset ID should NOT
	 * be null.
	 */
	public Long getAssetId();

	public void setAssetId(Long assetId);

	/** The id of the user who posted the comment */
	public String getUserId();

	/**
	 * Set user ID. It should not be null.
	 * 
	 * @param userId
	 */
	public void setUserId(String userId);

	/**
	 * Returns user display name.
	 * 
	 * @return user display name.
	 */
	public String getUserDisplayName();

	/**
	 * The version code to which this comment is applicable.
	 * <p/>
	 * The applicable version is just the active one when the comment is
	 * submitted.
	 */
	public String getAssetVersion();

	/**
	 * Sets the asset version for which this comment is.
	 * 
	 * The system will set the current latest published version if it is null
	 * when submitting the user comments.
	 * 
	 * @param assetVersion
	 */
	public void setAssetVersion(String assetVersion);

	/**
	 * Returns comment update date.
	 * 
	 * @return comment update date.
	 */
	public Date getUpdateDate();

	public String getTitle();

	public void setTitle(String title);

	public String getComment();

	public void setComment(String comment);

	public String getExternalId();

	public void setExternalId(String externalId);

}
