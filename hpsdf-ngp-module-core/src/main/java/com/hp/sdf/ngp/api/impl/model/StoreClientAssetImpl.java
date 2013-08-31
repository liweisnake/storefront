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

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.hp.sdf.ngp.api.model.AssetCategory;
import com.hp.sdf.ngp.api.model.Screenshot;
import com.hp.sdf.ngp.api.model.StoreClientAsset;
import com.hp.sdf.ngp.model.Asset;
import com.hp.sdf.ngp.model.AssetPrice;
import com.hp.sdf.ngp.model.Provider;
import com.hp.sdf.ngp.model.Status;
import com.hp.sdf.ngp.model.Tag;

public class StoreClientAssetImpl implements StoreClientAsset {

	public void setAssetCategories(Set<AssetCategory> assetCategories) {
		this.assetCategories = assetCategories;
	}

	public void setStoreClientAssets(List<StoreClientAsset> storeClientAssets) {
		this.storeClientAssets = storeClientAssets;
	}

	public void setProvider(Provider provider) {
		this.provider = provider;
		if (null != this.provider) {
			this.provider.getCity();//load information to avoid the lazy load
		}
	}

	public void setScreenshots(List<Screenshot> screenshots) {
		this.screenshots = screenshots;
	}

	public Asset getAsset() {
		if(null == asset){
			asset = new Asset();
		}
		return asset;
	}

	public Long getProviderId() {
		return provider.getId();
	}

	public void setAssetPrices(Set<AssetPrice> assetPrices) {
		this.assetPrices = assetPrices;
		
		if (null != this.assetPrices) {
			// load information to avoid the lazy load
			for (AssetPrice assetPrice : assetPrices) {
				if (assetPrice != null) {
					assetPrice.getAmount();
				}
			}
		}
	}

	public void setStatus(Status status) {
		this.status = status;
		if (null != this.status) {
			this.status.getStatus();//load information to avoid the lazy load
		}
	}

	public void setTags(List<Tag> tags) {
		this.tags = tags;
		if (null != this.tags) {
			// load information to avoid the lazy load
			for (Tag tag : tags) {
				if (tag != null) {
					tag.getName();
				}
			}
		}
		
	}

	public void setObjects(Map<String, List<Object>> objects) {
		this.objects = objects;
	}

	private Asset asset;

	private Set<AssetPrice> assetPrices;

	private List<Screenshot> screenshots;

	private Status status;

	private List<Tag> tags;

//	private Long providerId;

	private Map<String, List<Object>> objects;

	private Provider provider;

	private List<StoreClientAsset> storeClientAssets;

	private Set<AssetCategory> assetCategories;

	public StoreClientAssetImpl() {
		asset = new Asset();
	}

	public StoreClientAssetImpl(Asset asset) {
		this.asset = asset;
		if (null != this.asset) {
			this.asset.getBrief();//load information to avoid the lazy load
		}
	}

	public String getProviderHomePage() {
		if (null != provider) {
			return provider.getHomePage();
		}
		return null;
	}

	public String getProviderName() {
		if (null != provider) {
			return provider.getName();
		}
		return null;
	}

	public List<StoreClientAsset> getSubAssets() {
		return storeClientAssets;
	}

	public Long getAssetId() {
		Long assetId = asset.getId();
		return assetId;
		
	}

	public Double getAverageUserRating() {
		Double rating = asset.getAverageUserRating();
		return rating;
		
	}

	public String getBrief() {
		return asset.getBrief();
	}

	public Date getCreateDate() {
		return asset.getCreateDate();
	}

	public String getCurrentVersion() {
		return asset.getCurrentVersion();
	}

	public Long getCurrentVersionId() {
		Long currentVersionId = asset.getCurrentVersionId();
		return currentVersionId;
		
	}

	public String getDescription() {
		return asset.getDescription();
	}

	public Long getDownloadCount() {
		return asset.getDownloadCount();
	}

	public String getHomePage() {
		return asset.getAssetHomePage();
	}

	public String getName() {
		return asset.getName();
	}

	public BigDecimal getPrice(Currency currency) {
		if (null != assetPrices) {
			for (AssetPrice assetPrice : assetPrices) {
				if (currency.getCurrencyCode().equals(assetPrice.getCurrency())) {
					return assetPrice.getAmount();
				}
			}
		}
		return null;
	}

	public List<Screenshot> getScreenShots() {
		return screenshots;
	}

	public String getSource() {
		return asset.getSource();
	}

	public String getStatus() {
		if (null != status) {
			return status.getStatus();
		}
		return null;
	}

	public String getStatusDisplayName() {
		if (null != status) {
			return status.getDisplayName();
		}
		return null;
	}

	public Set<String> getTags() {
		Set<String> tags = new HashSet<String>();
		if (null != this.tags) {
			for (Tag tag : this.tags) {
				tags.add(tag.getName());
			}
		}
		return tags;
	}

	public String getThumbnailBigUrl() {
		return asset.getThumbnailBigLocation();
	}

	public String getThumbnailMedUrl() {
		return asset.getThumbnailMiddleLocation();
	}

	public String getThumbnailUrl() {
		return asset.getThumbnailLocation();
	}

	public Date getUpdateDate() {
		return asset.getUpdateDate();
	}

	public List<Object> getAttributeValue(String attributeName) {
		if (null != objects) {
			return objects.get(attributeName);
		}
		return null;
	}

	public Map<String, List<Object>> getAttributes() {
		return objects;
	}

	public Date getNewArrivalDueDate() {
		return asset.getNewArrivalDueDate();
	}

	public Date getPublishDate() {
		return asset.getPublishDate();
	}

	public Date getRecommendDueDate() {
		return asset.getRecommendDueDate();
	}

	public Integer getRecommendOrder() {
		Long order = asset.getRecommendOrder();
		if (null != order) {
			return order.intValue();
		}
		return null;
	}

	public Date getRecommendStartDate() {
		return asset.getRecommendStartDate();
	}

	public Date getRecommendUpdateDate() {
		return asset.getRecommendUpdateDate();
	}

	public Set<AssetCategory> getCategories() {
		return assetCategories;
	}

	public String getThumbnailBigLocation() {
		return asset.getThumbnailBigLocation();
	}

	public String getThumbnailLocation() {
		return asset.getThumbnailLocation();
	}

	public String getThumbnailMedLocation() {
		return asset.getThumbnailMiddleLocation();
	}

	public String getExternalId() {
		return asset.getExternalId();
	}

	public Long getId() {
		return asset.getId();
	}

}

// $Id$