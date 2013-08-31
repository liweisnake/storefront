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

import java.util.List;

import com.hp.sdf.ngp.api.assetcatalog.AssetCatalogService;
import com.hp.sdf.ngp.api.exception.AssetCatalogServiceException;
import com.hp.sdf.ngp.api.model.AssetCategory;
import com.hp.sdf.ngp.api.search.SearchExpression;
import com.hp.sdf.ngp.api.search.Condition.NumberComparer;
import com.hp.sdf.ngp.model.Category;
import com.hp.sdf.ngp.search.condition.category.CategoryIdCondition;
import com.hp.sdf.ngp.search.condition.category.CategoryParentIdCondition;
import com.hp.sdf.ngp.search.engine.SearchExpressionImpl;

public class AssetCategoryImpl implements AssetCategory {

	// private final static Log log =
	// LogFactory.getLog(AssetCategoryImpl.class);

	private AssetCatalogService assetCatalogService;

	public Category getCategory() {
		if (null == category) {
			return new Category();
		}
		return category;
	}

	public Long getParentId() {
		return parentId;
	}

	private Category category;



	private Long parentId;

	public AssetCategoryImpl() {
		category = new Category();
	}

	public AssetCategoryImpl(Category category, AssetCatalogService assetCatalogService) {
		this.category = category;
		this.assetCatalogService = assetCatalogService;
		if (this.category != null) {
			category.getDisplayName();// load information to avoid the lazy load
			if (null != category.getParentCategory())
				this.parentId=category.getParentCategory().getId();
		}
	}

	public String getDescription() {
		return category.getDescription();
	}

	public String getDisplayName() {
		return category.getDisplayName();
	}

	public Long getId() {
		Long id = category.getId();
		return id;
	}

	public String getName() {
		return category.getName();
	}

	public AssetCategory getParent() {
		if (null == parentId)
			return null;

		SearchExpression searchExpression = new SearchExpressionImpl();
		searchExpression.addCondition(new CategoryIdCondition(parentId, NumberComparer.EQUAL));

		try {
			List<AssetCategory> assetCategorys = assetCatalogService.searchCategory(searchExpression);
			if (null != assetCategorys && assetCategorys.size() > 0)
				return assetCategorys.get(0);
			else
				return null;
		} catch (AssetCatalogServiceException e) {
			e.printStackTrace();
		}

		return null;
	}

	public List<AssetCategory> getSubCategories() {

		SearchExpression searchExpression = new SearchExpressionImpl();
		searchExpression.addCondition(new CategoryParentIdCondition(category.getId(), NumberComparer.EQUAL));
		try {
			return assetCatalogService.searchCategory(searchExpression);
		} catch (AssetCatalogServiceException e) {
			e.printStackTrace();
		}
		return null;
	}

	public void setDescription(String description) {
		// log.debug("description:" + description);
		category.setDescription(description);
	}

	public void setDisplayName(String displayName) {
		// log.debug("displayName:" + displayName);
		category.setDisplayName(displayName);
	}

	public void setName(String name) {
		// log.debug("name:" + name);
		category.setName(name);
	}

	public void setParentId(Long parentId) {
		// log.debug("parentId:" + parentId);
		this.parentId = parentId;
		Category pCategory = new Category();
		pCategory.setId(parentId);
		category.setParentCategory(pCategory);
	}

	@Override
	public String toString() {
		return "AssetCategory[description=" + getDescription() + ",displayName=" + getDisplayName() + ",name=" + getName() + ",parentId="
				+ getParentId() + "]";
	}

	public String getExternalId() {
		return category.getExternalId();
	}

	public void setExternalId(String externalId) {
		category.setExternalId(externalId);
	}
}

// $Id$