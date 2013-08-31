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
package com.hp.sdf.ngp.ws.mobileclient.core;

import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Currency;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

import sun.misc.BASE64Encoder;

import com.hp.sdf.ngp.api.search.OrderEnum;
import com.hp.sdf.ngp.api.search.SearchExpression;
import com.hp.sdf.ngp.api.search.Condition.StringComparer;
import com.hp.sdf.ngp.api.search.orderby.AssetOrderBy;
import com.hp.sdf.ngp.common.annotation.Value;
import com.hp.sdf.ngp.common.constant.AssetLifecycleConstants;
import com.hp.sdf.ngp.common.constant.AssetSubscriptionStatus;
import com.hp.sdf.ngp.common.exception.NgpException;
import com.hp.sdf.ngp.manager.ApplicationManager;
import com.hp.sdf.ngp.model.Asset;
import com.hp.sdf.ngp.model.AssetPrice;
import com.hp.sdf.ngp.model.AssetRating;
import com.hp.sdf.ngp.model.Category;
import com.hp.sdf.ngp.model.Comments;
import com.hp.sdf.ngp.model.Platform;
import com.hp.sdf.ngp.model.PurchaseHistory;
import com.hp.sdf.ngp.model.UserDownloadHistory;
import com.hp.sdf.ngp.model.UserProfile;
import com.hp.sdf.ngp.search.condition.asset.AssetAssetCategoryNameCondition;
import com.hp.sdf.ngp.search.condition.asset.AssetNameCondition;
import com.hp.sdf.ngp.search.condition.asset.AssetPlatformNameCondition;
import com.hp.sdf.ngp.search.condition.asset.AssetStatusNameCondition;
import com.hp.sdf.ngp.search.engine.SearchExpressionImpl;
import com.hp.sdf.ngp.service.ApplicationService;
import com.hp.sdf.ngp.service.PurchaseService;
import com.hp.sdf.ngp.service.UserService;
import com.hp.sdf.ngp.ws.common.GetPropValueFromList;
import com.hp.sdf.ngp.ws.mobileclient.MobileClientService;
import com.hp.sdf.ngp.ws.mobileclient.model.MobileAsset;
import com.hp.sdf.ngp.ws.mobileclient.model.MobileComment;

@Component
public class MobileClientServiceImpl implements MobileClientService {

	private final static Log log = LogFactory
			.getLog(MobileClientServiceImpl.class);

	private static final String ANDROID = "Android";

	@Resource
	private ApplicationManager applicationManager;

	@Resource
	private ApplicationService applicationService;

	@Resource
	private UserService userService;

	@Resource
	private PurchaseService purchaseService;

	private int maxAssetsPerPage = 10;

	public int getMaxAssetsPerPage() {
		return maxAssetsPerPage;
	}

	@Value("max.assets.perpage")
	public void setMaxAssetsPerPage(int maxAssetsPerPage) {
		this.maxAssetsPerPage = maxAssetsPerPage;
	}

	public List<String> getAllCategory() {

		List<String> result = new ArrayList<String>();

		List<Category> categories = applicationService.getAllCategory(0,
				Integer.MAX_VALUE);
		if (categories != null) {
			for (Category category : categories) {
				log.debug("category name:" + category.getName());
				result.add(category.getName());
			}
		}

		return result;
	}

	public List<MobileAsset> getAssetsByCategoryName(String categoryName,
			int start, int count) {

		if (categoryName == null) {
			return null;
		}

		SearchExpression searchExpression = new SearchExpressionImpl();
		searchExpression.addCondition(new AssetAssetCategoryNameCondition(
				categoryName, StringComparer.EQUAL, false, false));
		searchExpression.addCondition(new AssetPlatformNameCondition(ANDROID,
				StringComparer.EQUAL, false, false));
		searchExpression.addCondition(new AssetStatusNameCondition(
				AssetLifecycleConstants.STATUS_PUBLISHED, StringComparer.EQUAL,
				false, false));
		searchExpression.setFirst(start);
		searchExpression.setMax(count);

		List<MobileAsset> mobileAssets = new ArrayList<MobileAsset>();

		List<Asset> assets = applicationService.searchAsset(searchExpression);
		if (assets != null) {
			for (Asset asset : assets) {
				MobileAsset mobileAsset = getMobileAssetByBusinessAsset(asset);
				if (mobileAsset != null) {
					mobileAssets.add(mobileAsset);
				}
			}
		}

		return mobileAssets;
	}

	public long getAssetsCountByCategoryName(String categoryName) {

		if (categoryName == null) {
			return 0L;
		}

		SearchExpression searchExpression = new SearchExpressionImpl();
		searchExpression.addCondition(new AssetAssetCategoryNameCondition(
				categoryName, StringComparer.EQUAL, false, false));
		searchExpression.addCondition(new AssetPlatformNameCondition(ANDROID,
				StringComparer.EQUAL, false, false));
		searchExpression.addCondition(new AssetStatusNameCondition(
				AssetLifecycleConstants.STATUS_PUBLISHED, StringComparer.EQUAL,
				false, false));
		searchExpression.setFirst(0);
		searchExpression.setMax(Integer.MAX_VALUE);

		Long count = applicationService.searchAssetPageCount(searchExpression);
		return count;
	}

	public List<MobileAsset> getMyPurchasedAsset(String userId, int start,
			int count) {
		List<MobileAsset> mobileAssets = new ArrayList<MobileAsset>();

		try {
			List<PurchaseHistory> purchaseHistories = purchaseService
					.getPurchaseHistory(userId, start, count);
			if (purchaseHistories != null) {
				for (PurchaseHistory purchaseHistory : purchaseHistories) {
					Asset asset = purchaseHistory.getAsset();
					asset = applicationService.getAsset(asset.getId());

					MobileAsset mobileAsset = getMobileAssetByBusinessAsset(asset);
					if (mobileAsset != null) {
						mobileAssets.add(mobileAsset);
					}
				}
			}
		} catch (NgpException e) {
			e.printStackTrace();
			log.error("NgpException :" + e);
		}

		return mobileAssets;
	}

	public long getMyPurchasedAssetCount(String userId) {
		return purchaseService.getMyPurchasedAssetCount(userId);
	}

	public boolean comment(Long assetId, String userId, String commentContent) {
		try {
			applicationManager.comment(assetId, userId, commentContent);
		} catch (Throwable e) {
			e.printStackTrace();
			log.warn("can't add the comments for asset[" + assetId + "]", e);
			return false;
		}

		return true;
	}

	public boolean purchase(long assetId, String userId) {
		try {
			List<AssetPrice> assetPriceList = applicationService
					.getAssetPriceByAssetId(assetId);
			AssetPrice assetPrice = GetPropValueFromList
					.getAssetPriceBeanFromListDollars(assetPriceList);

			if (assetPrice != null) {
				log.debug("assetPrice is not null, will purchase this asset.");
				applicationManager.purchase(assetId, userId, Currency
						.getInstance(assetPrice.getCurrency()), assetPrice
						.getAmount());
			}

		} catch (Throwable e) {
			e.printStackTrace();
			log.error("can't do the purchase for asset[" + assetId + "]", e);
			return false;
		}

		return true;
	}

	public boolean rating(Long assetId, String userId, Double rating) {
		try {
			applicationManager.rating(assetId, userId, rating);
		} catch (Throwable e) {
			e.printStackTrace();
			log.error("can't add the rating for asset[" + assetId + "]", e);
			return false;
		}

		return true;
	}

	public String retrieveDownloadLink(long assetId, String userId,
			String deviceSerial) {
		try {
			// Don't user OTA download so far

			String downloadLink = this.applicationManager.retrieveDownloadLink(
					assetId, null, userId, deviceSerial);
			log.debug("downloadLink :" + downloadLink);

			Asset asset = applicationService.getAsset(assetId);
			asset.setDownloadCount(asset.getDownloadCount() + 1);
			applicationService.updateAsset(asset);

			UserDownloadHistory userDownloadHistory = new UserDownloadHistory();
			userDownloadHistory.setAsset(asset);
			userDownloadHistory.setDownloadDate(new Date());
			userDownloadHistory.setUserid(userId);
			userDownloadHistory.setVersion("");
			userDownloadHistory.setStatus(AssetSubscriptionStatus.PURCHASED
					.toString());
			applicationService.saveUserDownloadHistory(userDownloadHistory);

			return downloadLink;
		} catch (Throwable e) {
			e.printStackTrace();
			log.error(
					"can't retrieve Download Link for asset[" + assetId + "]",
					e);
			return null;
		}
	}

	public List<MobileAsset> getRecommendedAssets() {

		SearchExpression searchExpression = new SearchExpressionImpl();

		searchExpression.addOrder(AssetOrderBy.RECOMMENDORDER, OrderEnum.DESC);

		searchExpression.addCondition(new AssetPlatformNameCondition(ANDROID,
				StringComparer.EQUAL, false, false));
		searchExpression.addCondition(new AssetStatusNameCondition(
				AssetLifecycleConstants.STATUS_PUBLISHED, StringComparer.EQUAL,
				false, false));
		searchExpression.setFirst(0);
		searchExpression.setMax(maxAssetsPerPage);

		List<MobileAsset> mobileAssets = new ArrayList<MobileAsset>();

		List<Asset> assets = applicationService.searchAsset(searchExpression);
		if (assets != null) {
			for (Asset asset : assets) {
				MobileAsset mobileAsset = getMobileAssetByBusinessAsset(asset);
				if (mobileAsset != null) {
					mobileAssets.add(mobileAsset);
				}
			}
		}

		return mobileAssets;
	}

	public List<MobileAsset> getTopAssets() {

		SearchExpression searchExpression = new SearchExpressionImpl();
		searchExpression.addOrder(AssetOrderBy.CREATEDATE, OrderEnum.DESC);
		searchExpression.addCondition(new AssetPlatformNameCondition(ANDROID,
				StringComparer.EQUAL, false, false));
		searchExpression.addCondition(new AssetStatusNameCondition(
				AssetLifecycleConstants.STATUS_PUBLISHED, StringComparer.EQUAL,
				false, false));
		searchExpression.setFirst(0);
		searchExpression.setMax(maxAssetsPerPage);

		List<MobileAsset> mobileAssets = new ArrayList<MobileAsset>();

		List<Asset> assets = applicationService.searchAsset(searchExpression);
		if (assets != null) {
			for (Asset asset : assets) {
				MobileAsset mobileAsset = getMobileAssetByBusinessAsset(asset);
				if (mobileAsset != null) {
					mobileAssets.add(mobileAsset);
				}
			}
		}

		return mobileAssets;
	}

	public long getAssetCommentCount(long assetId) {
		return applicationService.getAllCommentsCountByAssetId(assetId);
	}

	public List<MobileComment> getAssetComment(long assetId, int start,
			int count) {

		List<MobileComment> mobileComments = new ArrayList<MobileComment>();

		List<Comments> comments = applicationService.getAllCommentsByAssetId(
				assetId, start, count);
		if (comments != null) {
			for (Comments comm : comments) {
				MobileComment mobileComment = new MobileComment();
				mobileComment.setAssetId(comm.getAsset().getId());
				mobileComment.setAssetVersion(comm.getAssetVersion());
				mobileComment.setComment(comm.getContent());
				mobileComment.setDate(comm.getCreateDate());
				mobileComment.setTitle(comm.getTitle());
				mobileComment.setUserId(comm.getUserid());

				mobileComments.add(mobileComment);
			}
		}

		return mobileComments;
	}

	public Double getAssetRating(Long assetId, String userId) {

		AssetRating assetRating = applicationService.getAssetRating(userId,
				assetId);
		if (assetRating != null) {
			return assetRating.getRating();
		}

		return new Double(0);
	}

	public List<MobileAsset> search(String keyword, int start, int count) {

		if (keyword == null) {
			return null;
		}

		SearchExpression searchExpression = new SearchExpressionImpl();
		searchExpression.addCondition(new AssetNameCondition(keyword,
				StringComparer.LIKE, true, false));
		searchExpression.addCondition(new AssetPlatformNameCondition(ANDROID,
				StringComparer.EQUAL, false, false));
		searchExpression.addCondition(new AssetStatusNameCondition(
				AssetLifecycleConstants.STATUS_PUBLISHED, StringComparer.EQUAL,
				false, false));
		searchExpression.setFirst(start);
		searchExpression.setMax(count);

		List<MobileAsset> mobileAssets = new ArrayList<MobileAsset>();

		List<Asset> assets = applicationService.searchAsset(searchExpression);
		if (assets != null) {
			for (Asset asset : assets) {
				MobileAsset mobileAsset = getMobileAssetByBusinessAsset(asset);
				if (mobileAsset != null) {
					mobileAssets.add(mobileAsset);
				}
			}
		}

		return mobileAssets;
	}

	public long searchCount(String keyword) {
		log.warn("keyword :" + keyword);

		SearchExpression searchExpression = new SearchExpressionImpl();
		searchExpression.addCondition(new AssetNameCondition(keyword,
				StringComparer.LIKE, true, false));
		searchExpression.addCondition(new AssetPlatformNameCondition(ANDROID,
				StringComparer.EQUAL, false, false));
		searchExpression.addCondition(new AssetStatusNameCondition(
				AssetLifecycleConstants.STATUS_PUBLISHED, StringComparer.EQUAL,
				false, false));
		searchExpression.setFirst(0);
		searchExpression.setMax(Integer.MAX_VALUE);

		long searchAssetPageCount = applicationService
				.searchAssetPageCount(searchExpression);
		log.warn("searchAssetPageCount :" + searchAssetPageCount);

		return searchAssetPageCount;
	}

	private MobileAsset getMobileAssetByBusinessAsset(Asset asset) {
		MobileAsset mobileAsset = null;

		List<Platform> platforms = applicationService
				.getPlatformByAssetId(asset.getId());
		if (platforms != null) {
			for (Platform platform : platforms) {
				if (ANDROID.equalsIgnoreCase(platform.getName())) {
					mobileAsset = new MobileAsset();
					mobileAsset.setId(asset.getId());
					mobileAsset.setName(asset.getName());
					mobileAsset.setBrief(asset.getBrief());
					mobileAsset.setDescription(asset.getDescription());
					mobileAsset.setVersion(asset.getCurrentVersion());
					mobileAsset.setRating(asset.getAverageUserRating());
					mobileAsset.setThumbnailUrl(asset.getThumbnailLocation());
					mobileAsset.setPreview1Url(asset.getThumbnailBigLocation());
					mobileAsset.setPreview2Url(asset
							.getThumbnailMiddleLocation());
					mobileAsset.setLastUpdateTime(asset.getUpdateDate());
					mobileAsset.setAuthor(asset.getAuthorid());

					Set<AssetPrice> prices = asset.getAssetPrices();
					for (AssetPrice assetPrice : prices) {
						if ("USD".equalsIgnoreCase(assetPrice.getCurrency())) {
							mobileAsset.setPrice((null == assetPrice
									.getAmount() || assetPrice.getAmount()
									.doubleValue() <= 0) ? "Free" : "$ "
									+ assetPrice.getAmount().toPlainString());
						}
					}

					break;
				}
			}
		}

		return mobileAsset;
	}

	public boolean isValidUser(String userId, String password) {
		log.info("userId :" + userId);
		log.info("input password :" + password);

		UserProfile userProfile = userService.getUser(userId);
		log.info("userProfile :" + userProfile);
		if (userProfile != null) {
			log.info("userProfile.password (EncoderedByMd5):"
					+ userProfile.getPassword());

			try {
				MessageDigest md5 = MessageDigest.getInstance("MD5");
				BASE64Encoder base64en = new BASE64Encoder();
				String newstr = base64en.encode(md5.digest(password
						.getBytes("utf-8")));
				log.info("Encode our input password By Md5 , result is :"
						+ newstr);
			} catch (Exception e) {
				e.printStackTrace();
			}

			if (userService.validatePassword(userId, password)) {
				log.info("the password for the userId is correct.");
				return true;
			}
		}

		return false;
	}
}
