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
package com.hp.sdf.ngp.api.impl.common;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.hp.sdf.ngp.api.assetcatalog.AssetCatalogService;
import com.hp.sdf.ngp.api.comon.EntityType;
import com.hp.sdf.ngp.api.impl.assetcatalog.AssetCatalogServiceImpl;
import com.hp.sdf.ngp.api.impl.model.AssetCategoryImpl;
import com.hp.sdf.ngp.api.impl.model.AssetCommentImpl;
import com.hp.sdf.ngp.api.impl.model.AssetProviderImpl;
import com.hp.sdf.ngp.api.impl.model.AssetPurchaseHistoryImpl;
import com.hp.sdf.ngp.api.impl.model.BinaryVersionImpl;
import com.hp.sdf.ngp.api.impl.model.CatalogAssetImpl;
import com.hp.sdf.ngp.api.impl.model.RestrictionTypeImpl;
import com.hp.sdf.ngp.api.impl.model.ScreenshotImpl;
import com.hp.sdf.ngp.api.impl.model.StoreClientAssetImpl;
import com.hp.sdf.ngp.api.impl.model.UserImpl;
import com.hp.sdf.ngp.api.model.AssetCategory;
import com.hp.sdf.ngp.api.model.CatalogAsset;
import com.hp.sdf.ngp.api.model.RestrictionType;
import com.hp.sdf.ngp.api.model.Screenshot;
import com.hp.sdf.ngp.api.model.StoreClientAsset;
import com.hp.sdf.ngp.api.storeclient.StoreClientService;
import com.hp.sdf.ngp.eav.model.AttributeValue;
import com.hp.sdf.ngp.model.Asset;
import com.hp.sdf.ngp.model.AssetBinaryVersion;
import com.hp.sdf.ngp.model.AssetCategoryRelation;
import com.hp.sdf.ngp.model.Category;
import com.hp.sdf.ngp.model.Comments;
import com.hp.sdf.ngp.model.Platform;
import com.hp.sdf.ngp.model.Provider;
import com.hp.sdf.ngp.model.PurchaseHistory;
import com.hp.sdf.ngp.model.RestrictedType;
import com.hp.sdf.ngp.model.ScreenShots;
import com.hp.sdf.ngp.model.SubscriberProfile;
import com.hp.sdf.ngp.model.Tag;
import com.hp.sdf.ngp.model.UserProfile;
import com.hp.sdf.ngp.service.ApplicationService;
import com.hp.sdf.ngp.service.SubscriptionService;

@Component
@Transactional
public class ModelObjectFiller {

	private final static Log log = LogFactory.getLog(AssetCatalogServiceImpl.class);

	@Resource
	private ApplicationService applicationService;

	@Resource
	private SubscriptionService subscriptionService;

	@Resource
	private StoreClientService storeClientService;

	@Resource
	private AssetCatalogService assetCatalogService;

	public void fillAssetCategory(AssetCategoryImpl assetAtg) {
		Category category = assetAtg.getCategory();
		if (null != category) {
			// do nothing
		}
	}

	public void fillAssetComment(AssetCommentImpl assetCom) {
		Comments comments = assetCom.getComments();
		if (null != comments) {
			// asset

			assetCom.setAsset(comments.getAsset());

			// subscriberProfile
			SubscriberProfile subscriberProfile = subscriptionService.retrieveSubscriber(comments.getUserid());
			assetCom.setSubscriberProfile(subscriberProfile);
		}
	}

	public void fillAssetProvider(AssetProviderImpl assetPro) {
		Provider provider = assetPro.getAssetProvider();
		if (null != provider) {

			// attributeValuse
			assetPro.setObjects(this.retrieveAttributes(provider.getId(), EntityType.ASSETPROVIDER));

		}
	}

	public void fillUser(UserImpl userImpl) {
		UserProfile userProfile = userImpl.getUserProfile();
		if (null != userProfile) {

			// attributeValuse
			userImpl.setObjects(this.retrieveAttributes(userProfile.getUserid(), EntityType.USER));

		}
	}

	public void fillBinaryVersion(BinaryVersionImpl binaryVersion) {
		AssetBinaryVersion assetBinaryVersion = binaryVersion.getAssetBinaryVersion();
		if (null != assetBinaryVersion) {
			// attributeValue

			binaryVersion.setObjects(this.retrieveAttributes(assetBinaryVersion.getId(), EntityType.BINARYVERSION));

			// status
			binaryVersion.setStatus(assetBinaryVersion.getStatus());

			// tags
			Asset asset = assetBinaryVersion.getAsset();
			if (null != asset) {
				List<Tag> tags = applicationService.getAllTagsByAsset(asset.getId(), assetBinaryVersion.getId(), 0, Integer.MAX_VALUE);
				List<String> tag_list = new ArrayList<String>();
				if (null != tags) {
					for (Tag tag : tags) {
						tag_list.add(tag.getName());
					}
				}
				binaryVersion.setTags(tag_list);
			}

			// screenshots
			List<Screenshot> screenshots = new ArrayList<Screenshot>();
			if (assetBinaryVersion.getScreenShotses() != null) {
				for (ScreenShots screenShots : assetBinaryVersion.getScreenShotses()) {
					ScreenshotImpl screenshotImpl = new ScreenshotImpl(screenShots);
					fillScreenshot(screenshotImpl);
					screenshots.add(screenshotImpl);
				}
				Collections.sort(screenshots, new ScreenShotSequence());
			}
			binaryVersion.setScreenshots(screenshots);

			// assetCategories
			Set<AssetCategory> assetCategories = new HashSet<AssetCategory>();

			if (null != assetBinaryVersion.getAssetCategoryRelations()) {
				for (AssetCategoryRelation assetCategoryRelation : assetBinaryVersion.getAssetCategoryRelations()) {
					Category category = assetCategoryRelation.getCategory();
					AssetCategoryImpl assetCategoryImpl = new AssetCategoryImpl(category, assetCatalogService);
					fillAssetCategory(assetCategoryImpl);
					assetCategories.add(assetCategoryImpl);
				}
			}
			binaryVersion.setAssetCategories(assetCategories);

			// set storeclient asset
			try {
				StoreClientAsset storeClientAsset = this.storeClientService.getAsset(binaryVersion.getAssetId());
				binaryVersion.setStoreClientAsset(storeClientAsset);
			} catch (Throwable e) {
				log.warn("fail to get the store client asset object", e);

			}

		}
	}

	public void fillScreenshot(ScreenshotImpl screnchot) {
		ScreenShots screenShots = screnchot.getScreenShot();
		if (null != screenShots) {
			// assetBinaryVersion
			screnchot.setAssetBinaryVersion(screenShots.getBinaryVersion());

			// asset
			screnchot.setAsset(screenShots.getAsset());
		}
	}

	public void fillCategoryAsset(CatalogAssetImpl ctgAsset) {
		Asset asset = ctgAsset.getAsset();
		if (null != asset) {
			Long id = asset.getId();
			// status
			ctgAsset.setStatus(asset.getStatus());

			// prices
			ctgAsset.setAssetPrices(asset.getAssetPrices());

			// screenShotss
			List<ScreenShots> screenShotss = applicationService.getScreenShotsByAssetId(id);
			List<Screenshot> screenshot_list = new ArrayList<Screenshot>();
			if (null != screenShotss) {
				for (ScreenShots screenShots : screenShotss) {
					ScreenshotImpl screenshotImpl = new ScreenshotImpl(screenShots);
					fillScreenshot(screenshotImpl);
					screenshot_list.add(screenshotImpl);
				}
			}
			ctgAsset.setScreenshots(screenshot_list);

			// tags
			List<Tag> tags = applicationService.getAllTagsByAsset(id, null, 0, Integer.MAX_VALUE);
			List<String> tag_list = new ArrayList<String>();
			if (null != tags) {
				for (Tag tag : tags) {
					tag_list.add(tag.getName());
				}
			}
			ctgAsset.setTags(tag_list);

			// category
			List<Category> categorys = applicationService.getAllCategoryByAssetId(id, 0, Integer.MAX_VALUE);
			Set<AssetCategory> assetCategorys = new HashSet<AssetCategory>();
			if (null != categorys) {
				for (Category ctg : categorys) {
					AssetCategoryImpl catalogAssetCategoryImpl = new AssetCategoryImpl(ctg, assetCatalogService);
					fillAssetCategory(catalogAssetCategoryImpl);
					assetCategorys.add(catalogAssetCategoryImpl);
				}
			}
			ctgAsset.setAssetCategorys(assetCategorys);

			// platforms
			List<Platform> platforms = applicationService.getPlatformByAssetId(id);
			ctgAsset.setPlatforms(platforms);

			// assetProvider
			Provider provider = asset.getAssetProvider();
			if (null != provider) {
				AssetProviderImpl assetProviderImpl = new AssetProviderImpl(provider);
				fillAssetProvider(assetProviderImpl);
				ctgAsset.setAssetProvider(assetProviderImpl);
			}

			// RestrictionType
			List<RestrictedType> restrictedTypes = applicationService.getAllRestrictedTypesByAssetId(id, 0, Integer.MAX_VALUE);
			Set<RestrictionType> restrictionTypes = new HashSet<RestrictionType>();
			if (null != restrictedTypes) {
				for (RestrictedType restrictedType : restrictedTypes) {
					RestrictionType restrictionType = new RestrictionTypeImpl(restrictedType);
					restrictionTypes.add(restrictionType);
				}
			}
			ctgAsset.setRestrictionTypes(restrictionTypes);

			// objects
			ctgAsset.setObjects(this.retrieveAttributes(asset.getId(), EntityType.ASSET));
		}

	}

	@SuppressWarnings("unchecked")
	private Map<String, List<Object>> retrieveAttributes(Object objectId, EntityType entityType) {
		Map<String, List<AttributeValue>> values = applicationService.getAttributes(objectId, entityType);
		Map<String, List<Object>> objects = new HashMap<String, List<Object>>();
		if (null != values) {
			for (Map.Entry<String, List<AttributeValue>> map : values.entrySet()) {
				List<AttributeValue> attributeValues = map.getValue();
				if (attributeValues != null && attributeValues.size() > 0) {
					List<Object> object_list = new ArrayList<Object>();
					for (AttributeValue attributeValue : attributeValues) {
						if (attributeValue != null) {
							object_list.add(attributeValue.getValue());
						}
					}
					objects.put(map.getKey(), object_list);
				}

			}
		}
		return objects;
	}

	public void fillCategorySubAsset(CatalogAssetImpl ctgAsset) {
		Asset asset = ctgAsset.getAsset();
		if (null != asset) {
			// assets
			Set<Asset> assets = asset.getAssets();
			if (null != assets) {
				List<CatalogAsset> catalogAssets = new ArrayList<CatalogAsset>();
				for (Asset subAsset : assets) {
					CatalogAssetImpl catologAssetImpl = new CatalogAssetImpl(subAsset);
					fillCategoryAsset(catologAssetImpl);
					catalogAssets.add(catologAssetImpl);
				}

				ctgAsset.setCatalogAssets(catalogAssets);
			}
		}

	}

	public void fillAsset(StoreClientAssetImpl storeClientAssetImpl) {
		com.hp.sdf.ngp.model.Asset asset = storeClientAssetImpl.getAsset();
		if (null != asset) {
			Long id = asset.getId();
			// asset=assetDao.findById(id);
			// prices
			storeClientAssetImpl.setAssetPrices(asset.getAssetPrices());

			// screenShotss
			List<ScreenShots> screenShotss = applicationService.getScreenShotsByAssetId(id);
			List<Screenshot> screenshot_list = new ArrayList<Screenshot>();
			if (null != screenShotss) {
				for (ScreenShots screenShots : screenShotss) {
					ScreenshotImpl screenshotImpl = new ScreenshotImpl(screenShots);
					fillScreenshot(screenshotImpl);
					screenshot_list.add(screenshotImpl);
				}
			}
			storeClientAssetImpl.setScreenshots(screenshot_list);

			// status
			storeClientAssetImpl.setStatus(asset.getStatus());

			// tags
			List<Tag> tags = applicationService.getAllTagsByAsset(id, null, 0, Integer.MAX_VALUE);
			storeClientAssetImpl.setTags(tags);

			// assetProvider
			storeClientAssetImpl.setProvider(asset.getAssetProvider());

			// objects

			storeClientAssetImpl.setObjects(this.retrieveAttributes(id, EntityType.ASSET));

			// assetCategories
			List<Category> categories = applicationService.getAllCategoryByAssetId(id, 0, Integer.MAX_VALUE);
			Set<AssetCategory> assetCategories = new HashSet<AssetCategory>();
			for (Category category : categories) {
				AssetCategoryImpl assetCategoryImpl = new AssetCategoryImpl(category, assetCatalogService);
				fillAssetCategory(assetCategoryImpl);
				assetCategories.add(assetCategoryImpl);
			}

			storeClientAssetImpl.setAssetCategories(assetCategories);
		}

	}

	public void fillSubAsset(StoreClientAssetImpl storeClientAssetImpl) {
		com.hp.sdf.ngp.model.Asset asset = storeClientAssetImpl.getAsset();
		if (null != asset) {
			// assets
			// asset=assetDao.findById(asset.getId());
			Set<com.hp.sdf.ngp.model.Asset> assets = asset.getAssets();
			if (null != assets) {
				List<StoreClientAsset> subAssets = new ArrayList<StoreClientAsset>();
				for (com.hp.sdf.ngp.model.Asset subAsset : assets) {
					StoreClientAssetImpl assetImpl = new StoreClientAssetImpl(subAsset);
					fillAsset(assetImpl);
					// fillSubAsset(assetImpl);
					subAssets.add(assetImpl);
				}

				storeClientAssetImpl.setStoreClientAssets(subAssets);
			}
		}
	}

	public void fillPurchaseHistory(AssetPurchaseHistoryImpl purchaseHistoryImpl) {
		PurchaseHistory purchaseHistory = purchaseHistoryImpl.getPurchaseHistory();
		if (null != purchaseHistory) {
			// asset
			purchaseHistoryImpl.setAsset(purchaseHistory.getAsset());
		}
	}
}

// $Id$