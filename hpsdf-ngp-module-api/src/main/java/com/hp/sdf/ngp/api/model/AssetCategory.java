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

import java.util.List;

/**
 * Asset category. Each asset can belong to multiple category
 * 
 */
public interface AssetCategory {
	
	/** returns the Id of the Category */
	public Long getId();

	/**
	 * Returns the name of the category.
	 * @return the name of the category
	 */
	public String getName();
	
	public void setName(String name);
	
	public String getExternalId();
	
	public void setExternalId(String externalId);
	
	/**
	 * Returns the display name of the category.
	 * For some language with has two kinds of spelling of one word. e.g. Japanese
	 * @return the name of the category
	 */
	public String getDisplayName();
	
	public void setDisplayName(String displayName);

	/**
	 * Returns the description of the category.
	 * @return
	 */
	public String getDescription();
	
	public void setDescription(String description);

	/**
	 * Returns all child categories of this category.
	 * Not includes its grand children categories.
	 * @return all child categories of this category.
	 */
	public List<AssetCategory> getSubCategories();

	/**
	 * Returns the parent category if this category.
	 * @return the parent category if this category.
	 */
	public AssetCategory getParent();
	
	public void setParentId(Long parentId);

}
