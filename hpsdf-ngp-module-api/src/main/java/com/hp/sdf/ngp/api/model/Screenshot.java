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

public interface Screenshot {

	public Long getId();

	public String getMimeType();

	public Long getAssetId();

	/**
	 * Sets the asset ID.
	 * The asset ID should NOT be null.
	 * @param assetId
	 */
	public void setAssetId(Long assetId);

	/**
	 * Sets the binary version ID.
	 * The binary version ID should NOT be null if it's related to a specific version.
	 * @return
	 */
	public Long getBinaryVersionId();

	public void setBinaryVersionId(Long binaryVersionId);

	/**
	 * Sets MIME type defined by RFC 2045 and 2046
	 * It should not be NULL.
	 * @param mimeType
	 *            the MIME type of the screenshot picture
	 */
	public void setMimeType(String mimeType);

	public String getDescription();

	public void setDescription(String description);

	/**
	 * Gets file location of the picture.
	 *
	 * Returns the shared file location in cluster environment.
	 * @return
	 */
	public String getPictureLocation();

	/**
	 * Sets the pictyure steam.
	 * The pictureSteam should NOT be null.
	 * @param pictureSteam
	 */
	public void setPicture(String fileName, InputStream pictureSteam);
	
	public Long getSequence();
	
	public void setSequence(Long sequence);
	
}

// $Id$