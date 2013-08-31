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
package com.hp.sdf.ngp.cms.model;

import java.io.Serializable;
import java.util.Date;

import com.hp.sdf.ngp.common.util.FileUtil;

public class CMSObject implements Serializable {

	/** The serialVersionUID */
	private static final long serialVersionUID = -911498391656553183L;

	protected String parentUUID;

	protected String name;
	protected String versionPath;
	protected String versionUUID;
	protected String basePath;
	protected String baseUUID;

	protected String description;
	protected String title;
	protected Date creationDate;
	protected Date lastModified;

	public String getParentUUID() {
		return parentUUID;
	}

	public void setParentUUID(String parentUUID) {
		this.parentUUID = parentUUID;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getVersionPath() {
		return versionPath;
	}

	public void setVersionPath(String versionPath) {
		this.versionPath = versionPath;
	}

	public String getVersionUUID() {
		return versionUUID;
	}

	public void setVersionUUID(String versionUUID) {
		this.versionUUID = versionUUID;
	}

	public String getBasePath() {
		return FileUtil.cleanDoubleSlashes(basePath);
	}

	public void setBasePath(String basePath) {
		this.basePath = FileUtil.cleanDoubleSlashes(basePath);
	}

	public String getBaseUUID() {
		return baseUUID;
	}

	public void setBaseUUID(String baseUUID) {
		this.baseUUID = baseUUID;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public Date getLastModified() {
		return lastModified;
	}

	public void setLastModified(Date lastModified) {
		this.lastModified = lastModified;
	}
}
