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

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.Currency;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.hp.sdf.ngp.api.model.AssetCategory;
import com.hp.sdf.ngp.api.model.AssetProvider;
import com.hp.sdf.ngp.api.model.CatalogAsset;
import com.hp.sdf.ngp.api.model.RestrictionType;
import com.hp.sdf.ngp.api.model.Screenshot;
import com.hp.sdf.ngp.common.constant.AssetConstants;
import com.hp.sdf.ngp.model.AssetPrice;
import com.hp.sdf.ngp.model.Platform;
import com.hp.sdf.ngp.model.Provider;
import com.hp.sdf.ngp.model.Status;

public class CatalogAssetImpl implements CatalogAsset {

	public void setRestrictionTypes(Set<RestrictionType> restrictionTypes) {
		this.restrictionTypes = restrictionTypes;
	}

	public String getBigName() {
		return bigName;
	}

	public String getMedName() {
		return medName;
	}

	public String getThumName() {
		return thumName;
	}

	public void setScreenshots(List<Screenshot> screenshots) {
		this.screenshots = screenshots;
	}

	public void setAssetCategorys(Set<AssetCategory> assetCategorys) {
		this.assetCategorys = assetCategorys;
	}

	public void setAssetProvider(AssetProvider assetProvider) {
		this.assetProvider = assetProvider;
	}

	private final static Log log = LogFactory.getLog(CatalogAsset.class);

	public com.hp.sdf.ngp.model.Asset getAsset() {
		if (null == asset) {
			asset = new com.hp.sdf.ngp.model.Asset();
		}
		return asset;
	}

	public byte[] getBigThumbnail() {
		return bigThumbnail;
	}

	public byte[] getMedThumbnail() {
		return medThumbnail;
	}

	public byte[] getThumbnail() {
		return thumbnail;
	}

	public Long getProviderId() {
		return providerId;
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
	}

	public void setTags(List<String> tags) {
		this.tags = tags;
	}

	public void setPlatforms(List<Platform> platforms) {
		this.platforms = platforms;
		if (null != this.platforms) {
			// load information to avoid the lazy load
			for (Platform platform : platforms) {
				if (platform != null) {
					platform.getName();
				}
			}
		}
	}

	public void setCatalogAssets(List<CatalogAsset> catalogAssets) {
		this.catalogAssets = catalogAssets;
	}

	public void setObjects(Map<String, List<Object>> objects) {
		this.objects = objects;
	}

	private com.hp.sdf.ngp.model.Asset asset;

	private Set<AssetPrice> assetPrices;

	// private List<ScreenShots> screenShotss;
	private List<Screenshot> screenshots;

	private Status status;

	private List<String> tags;

	// private List<Category> categorys;
	private Set<AssetCategory> assetCategorys;

	private List<Platform> platforms;

	// private Provider provider;
	private AssetProvider assetProvider;

	private List<CatalogAsset> catalogAssets;

	private byte[] bigThumbnail;

	private byte[] medThumbnail;

	private byte[] thumbnail;

	private String bigName;

	private String medName;

	private String thumName;

	private Long providerId;

	private Map<String, List<Object>> objects;

	private Set<RestrictionType> restrictionTypes;

	public CatalogAssetImpl() {
		asset = new com.hp.sdf.ngp.model.Asset();
	}

	public CatalogAssetImpl(com.hp.sdf.ngp.model.Asset asset) {
		this.asset = asset;
		if (null != this.asset) {
			this.asset.getBrief();// load information to avoid the lazy load
		}
	}

	public Double getAverageUserRating() {
		Double rating = asset.getAverageUserRating();
		if (null != rating && rating < 1) {
			return rating;
		}
		throw new RuntimeException();
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
		// log.debug("currency:"+currency);
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
		// List<Screenshot> screenshot_list = new ArrayList<Screenshot>();
		// if(null != screenShotss){
		// for(ScreenShots screenShots:screenShotss){
		// Screenshot screenshot = new ScreenshotImpl(screenShots);
		//				
		// screenshot_list.add(screenshot);
		// }
		// }
		// return screenshot_list;
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
		Set<String> tags = new HashSet<String>(this.tags);
		return tags;
	}

	private String getPrefix() {
		if (AssetConstants.URIPREFIX == null) {
			return "";
		}

		return AssetConstants.URIPREFIX.replace("//", "/");
	}

	// private String getPrefix() {
	// Properties prop = new Properties();
	// String url = null;
	// FileInputStream fis = null;
	// try {
	// fis = new FileInputStream("src\\test\\resources\\conf.properties");
	// prop.load(fis);
	// url = (String) prop.get("file.path.prefix");
	// } catch (FileNotFoundException e) {
	// e.printStackTrace();
	// log.debug("Comment Exception : " + e);
	// } catch (IOException e) {
	// e.printStackTrace();
	// log.debug("Comment Exception : " + e);
	// }
	// return url.replace("//", "/");
	// }

	public String getThumbnailBigUrl() {
		// return getPrefix() + asset.getThumbnailBigLocation();
		return asset.getThumbnailBigLocation();
	}

	public String getThumbnailMedUrl() {
		// return getPrefix() + asset.getThumbnailMiddleLocation();
		return asset.getThumbnailMiddleLocation();
	}

	public String getThumbnailUrl() {
		// return getPrefix() + asset.getThumbnailLocation();
		return asset.getThumbnailLocation();
	}

	public Date getUpdateDate() {
		return asset.getUpdateDate();
	}

	public Set<AssetCategory> getCategories() {
		// Set<AssetCategory> assetCategorys = new HashSet<AssetCategory>();
		// if(null != categorys){
		// for (Category ctg : categorys) {
		// AssetCategory catalogAssetCategory = new AssetCategoryImpl(ctg);
		// assetCategorys.add(catalogAssetCategory);
		// }
		// }
		return assetCategorys;
	}

	public Set<String> getPlatforms() {
		Set<String> plats = new HashSet<String>();
		if (null != platforms) {
			for (Platform plat : platforms) {
				plats.add(plat.getName());
			}
		}
		return plats;
	}

	public Map<Currency, BigDecimal> getPrices() {
		Map<Currency, BigDecimal> prices = new HashMap<Currency, BigDecimal>();
		if (null != assetPrices) {
			for (AssetPrice assetPrice : assetPrices) {
				prices.put(Currency.getInstance(assetPrice.getCurrency()), assetPrice.getAmount());
			}
		}
		return prices;
	}

	public AssetProvider getProvider() {
		// return new AssetProviderImpl(provider);
		return assetProvider;
	}

	public Set<RestrictionType> getRestrictionTypes() {
		return restrictionTypes;
	}

	public List<CatalogAsset> getSubAssets() {
		return catalogAssets;
	}

	public void setBrief(String brief) {
		// log.debug("brief:"+brief);
		asset.setBrief(brief);
	}

	public void setCreationDate(Date date) {
		// log.debug("creationDate:"+date);
		asset.setCreateDate(date);
	}

	public void setDescription(String description) {
		// log.debug("description:"+description);
		asset.setDescription(description);
	}

	public void setHomePage(String url) {
		// log.debug("homePage:"+url);
		asset.setAssetHomePage(url);
	}

	public void setName(String name) {
		// log.debug("name:"+name);
		asset.setName(name);
	}

	public void setProvider(Long providerId) {
		// log.debug("providerId:"+providerId);
		if (providerId == null)
			this.asset.setAssetProvider(null);
		else {
			this.providerId = providerId;
			Provider provider = new Provider();
			provider.setId(providerId);
			this.asset.setAssetProvider(provider);
		}
	}

	public void setSource(String sourceName) {
		// log.debug("sourceName:"+sourceName);
		asset.setSource(sourceName);
	}

	public void setStatus(String status) {
		// log.debug("status:"+status);
		Status modelStatus = new Status();
		modelStatus.setStatus(status);
		// this.asset.setStatus(modelStatus);
		this.status = modelStatus;
	}

	public void setUpdateDate(Date date) {
		// log.debug("date:"+date);
		asset.setUpdateDate(date);
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

	public void setNewArrivalDueDate(Date newArrivalDueDate) {
		// log.debug("newArrivalDueDate:"+newArrivalDueDate);
		asset.setNewArrivalDueDate(newArrivalDueDate);
	}

	public void setPublishDate(Date publishDate) {
		// log.debug("publishDate:"+publishDate);
		asset.setPublishDate(publishDate);
	}

	public void setRecommendDueDate(Date recommendDueDate) {
		// log.debug("recommendDueDate:"+recommendDueDate);
		asset.setRecommendDueDate(recommendDueDate);
	}

	public void setRecommendOrder(int recommendOrder) {
		// log.debug("recommendOrder:"+recommendOrder);
		asset.setRecommendOrder(new Long(recommendOrder));
	}

	public void setRecommendStartDate(Date recommendStartDate) {
		// log.debug("recommendStartDate" + recommendStartDate);
		asset.setRecommendStartDate(recommendStartDate);
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
		if (order != null) {
			return order.intValue();
		} else {
			return null;
		}

	}

	public Date getRecommendStartDate() {
		return asset.getRecommendStartDate();
	}

	public void setRecommendUpdateDate(Date recommendUpdateDate) {
		// log.debug("recommendUpdateDate:"+recommendUpdateDate);
		asset.setRecommendUpdateDate(recommendUpdateDate);
	}

	public Date getRecommendUpdateDate() {
		return asset.getRecommendUpdateDate();
	}

	public void setBigThumbnail(String fileNameSuffix, InputStream buf) {
		// log.debug("bigSuffix:"+fileNameSuffix+",buf:"+buf);
		bigName = fileNameSuffix;
		try {
			bigThumbnail = new byte[buf.available()];
			buf.read(bigThumbnail);
		} catch (IOException e) {
			log.error("Comment exception: " + e);
		}
	}

	public void setMedThumbnail(String fileNameSuffix, InputStream buf) {
		// log.debug("medSuffix:"+fileNameSuffix+",buf:"+buf);
		medName = fileNameSuffix;
		try {
			medThumbnail = new byte[buf.available()];
			buf.read(medThumbnail);
		} catch (IOException e) {
			log.error("Comment exception: " + e);
		}
	}

	public void setThumbnail(String fileNameSuffix, InputStream buf) {
		// log.debug("suffix:"+fileNameSuffix+",buf:"+buf);
		thumName = fileNameSuffix;
		try {
			thumbnail = new byte[buf.available()];
			buf.read(thumbnail);
		} catch (IOException e) {
			log.error("Comment exception: " + e);
		}
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

	@Override
	public String toString() {
		return "CatalogAsset[brief=" + getBrief() + ",creationDate=" + getCreateDate() + ",description=" + getDescription() + ",homePage=" + getHomePage() + ",name=" + getName() + ",providerId=" + getProviderId() + ",sourceName=" + getSource() + ",status=" + getStatus() + ",updateDate=" + getUpdateDate() + ",newArrivalDueDate=" + getNewArrivalDueDate() + ",publishDate=" + getPublishDate() + ",recommendDueDate=" + getRecommendDueDate() + ",recommendOrder=" + getRecommendOrder() + ",recommendStartDate=" + getRecommendStartDate() + ",recommendUpdateDate=" + getRecommendUpdateDate() + ",fileBigNameSuffix=" + getBigName() + ",bigBuf=" + getBigThumbnail() + ",fileMedNameSuffix=" + getMedName() + ",medBuf=" + getMedThumbnail() + ",fileNameSuffix=" + getThumName() + ",buf=" + getThumbnail() + "]";
	}

	public String getExternalId() {
		return asset.getExternalId();
	}

	public Long getId() {
		return asset.getId();
	}

	public void setExternalId(String externalId) {
		asset.setExternalId(externalId);
	}

	public Long getAssetId() {
		return asset.getId();
	}
}

// $Id$