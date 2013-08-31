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
package com.hp.sdf.ngp.api.impl.storeclient;

import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.hp.sdf.ngp.api.assetcatalog.AssetCatalogService;
import com.hp.sdf.ngp.api.exception.StoreClientEntityNotFoundException;
import com.hp.sdf.ngp.api.exception.StoreClientServiceException;
import com.hp.sdf.ngp.api.exception.UserCommentsCensorFailException;
import com.hp.sdf.ngp.api.impl.common.ModelObjectFiller;
import com.hp.sdf.ngp.api.impl.model.AssetCategoryImpl;
import com.hp.sdf.ngp.api.impl.model.AssetCommentImpl;
import com.hp.sdf.ngp.api.impl.model.AssetPurchaseHistoryImpl;
import com.hp.sdf.ngp.api.impl.model.AssetRatingCommentsImpl;
import com.hp.sdf.ngp.api.impl.model.BinaryVersionImpl;
import com.hp.sdf.ngp.api.impl.model.StoreClientAssetImpl;
import com.hp.sdf.ngp.api.model.Asset;
import com.hp.sdf.ngp.api.model.AssetCategory;
import com.hp.sdf.ngp.api.model.AssetComment;
import com.hp.sdf.ngp.api.model.AssetPurchaseHistory;
import com.hp.sdf.ngp.api.model.AssetRatingComments;
import com.hp.sdf.ngp.api.model.BinaryVersion;
import com.hp.sdf.ngp.api.model.StoreClientAsset;
import com.hp.sdf.ngp.api.search.SearchExpression;
import com.hp.sdf.ngp.api.search.Condition.NumberComparer;
import com.hp.sdf.ngp.api.search.Condition.StringComparer;
import com.hp.sdf.ngp.api.storeclient.StoreClientService;
import com.hp.sdf.ngp.common.annotation.Value;
import com.hp.sdf.ngp.common.exception.CensoredWordFoundException;
import com.hp.sdf.ngp.common.exception.NgpException;
import com.hp.sdf.ngp.manager.OTADownloadManager;
import com.hp.sdf.ngp.model.AssetBinaryVersion;
import com.hp.sdf.ngp.model.AssetRating;
import com.hp.sdf.ngp.model.Category;
import com.hp.sdf.ngp.model.Comments;
import com.hp.sdf.ngp.model.PurchaseHistory;
import com.hp.sdf.ngp.model.Status;
import com.hp.sdf.ngp.model.UserDownloadHistory;
import com.hp.sdf.ngp.search.condition.comments.CommentsAssetIdCondition;
import com.hp.sdf.ngp.search.condition.comments.CommentsUseridCondition;
import com.hp.sdf.ngp.search.condition.purchasehistory.PurchaseHistoryAssetIdCondition;
import com.hp.sdf.ngp.search.condition.purchasehistory.PurchaseHistoryUseridCondition;
import com.hp.sdf.ngp.search.engine.SearchExpressionImpl;
import com.hp.sdf.ngp.service.ApplicationService;
import com.hp.sdf.ngp.service.PurchaseService;
import com.hp.sdf.ngp.workflow.AssetLifeCycleEngine;

@Component
public class StoreClientServiceImpl implements StoreClientService {

	@Resource
	private ApplicationService applicationService;

	@Resource
	private ModelObjectFiller modelObjectFiller;

	@Resource
	private PurchaseService purchaseService;

	@Resource
	private OTADownloadManager oTADownloadManager;

	@Resource
	private AssetLifeCycleEngine assetLifeCycleEngine;

	@Resource
	private AssetCatalogService assetCatalogService;

	private String filePath;

	public String getFilePath() {
		return filePath;
	}

	@Value("file.path.prefix")
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	private final static Log log = LogFactory.getLog(StoreClientServiceImpl.class);

	@Transactional
	public StoreClientAsset getAsset(Long assetId) throws StoreClientServiceException {
		log.debug("enter method getAsset:assetId = " + assetId);
		try {
			com.hp.sdf.ngp.model.Asset asset = applicationService.getAsset(assetId);
			StoreClientAssetImpl assetImpl = new StoreClientAssetImpl(asset);
			if (assetImpl == null) {
				throw new StoreClientEntityNotFoundException("Entity not found.");
			} else {
				modelObjectFiller.fillAsset(assetImpl);
				modelObjectFiller.fillSubAsset(assetImpl);

				log.debug("getAsset returns:" + assetImpl.toString());
			}
			return assetImpl;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Comment exception: " + e);

			if (e instanceof StoreClientEntityNotFoundException) {
				throw new StoreClientEntityNotFoundException(e);
			}

			throw new StoreClientServiceException(e);
		}
	}

	@Transactional
	public List<AssetCategory> getAssetCategories() throws StoreClientServiceException {
		try {
			List<AssetCategory> assetCategorys = new ArrayList<AssetCategory>();
			List<Category> categorys = applicationService.getAllCategory(0, Integer.MAX_VALUE);
			if (null != categorys) {
				for (Category category : categorys) {
					if (null == category.getParentCategory()) {
						AssetCategoryImpl categoryImpl = new AssetCategoryImpl(category, assetCatalogService);
						modelObjectFiller.fillAssetCategory(categoryImpl);
						assetCategorys.add(categoryImpl);
					}
				}
			}

			log.debug("getAssetCategories returns size:" + assetCategorys.size());
			return assetCategorys;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Comment exception: " + e);
			throw new StoreClientServiceException(e);
		}
	}

	@Transactional
	public AssetCategory getAssetCategoryById(Long id) throws StoreClientServiceException {
		log.debug("enter getAssetCategoryById:id=" + id);
		try {
			Category category = applicationService.getCategoryById(id);
			if (null != category) {
				AssetCategoryImpl categoryImpl = new AssetCategoryImpl(category, assetCatalogService);
				modelObjectFiller.fillAssetCategory(categoryImpl);

				log.debug("getAssetCategoryById returns:" + categoryImpl.toString());
				return categoryImpl;
			} else {
				log.debug("getAssetCategoryById returns null");
				throw new StoreClientEntityNotFoundException("Entity not found with ID: " + id);
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Comment exception: " + e);

			if (e instanceof StoreClientEntityNotFoundException) {
				throw new StoreClientEntityNotFoundException(e);
			}

			throw new StoreClientServiceException(e);
		}
	}

	@Transactional
	public List<AssetComment> getAssetComment(Long assetId, int start, int count) throws StoreClientServiceException {
		log.debug("enter getAssetComment:assetId=" + assetId + ",start=" + start + ",count=" + count);
		try {
			List<AssetComment> assetComments = new ArrayList<AssetComment>();
			List<Comments> comments_list = applicationService.getAllCommentsByAssetId(assetId, start, count);
			if (null != comments_list) {
				for (Comments comments : comments_list) {
					AssetCommentImpl commentImpl = new AssetCommentImpl(comments);
					modelObjectFiller.fillAssetComment(commentImpl);
					assetComments.add(commentImpl);
				}
			}

			log.debug("getAssetComment returns size:" + assetComments.size());
			return assetComments;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Comment exception: " + e);
			throw new StoreClientServiceException(e);
		}
	}

	@Transactional
	public List<AssetComment> getAssetCommentByUserId(Long assetId, String userId) throws StoreClientServiceException {
		log.debug("enter getAssetCommentByUserId:assetId=" + assetId + ",userId='" + userId + "'");
		try {
			SearchExpression searchExpression = new SearchExpressionImpl();
			searchExpression.addCondition(new CommentsAssetIdCondition(assetId, NumberComparer.EQUAL));
			searchExpression.addCondition(new CommentsUseridCondition(userId, StringComparer.EQUAL, false, false));

			List<AssetComment> assetComments = new ArrayList<AssetComment>();
			List<Comments> commentss = applicationService.searchComments(searchExpression);
			if (null != commentss) {
				for (Comments comments : commentss) {
					AssetCommentImpl commentImpl = new AssetCommentImpl(comments);
					modelObjectFiller.fillAssetComment(commentImpl);
					assetComments.add(commentImpl);
				}
			}

			log.debug("getAssetCommentByUserId returns size:" + assetComments.size());
			return assetComments;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Comment exception: " + e);
			throw new StoreClientServiceException(e);
		}
	}

	@Transactional
	public Long getAssetCommentCount(Long assetId) throws StoreClientServiceException {
		log.debug("enter getAssetCommentCount:assetId=" + assetId);
		try {
			Long count = applicationService.getAllCommentsCountByAssetId(assetId);

			log.debug("getAssetCommentCount returns:" + count);
			return count;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Comment exception: " + e);
			throw new StoreClientServiceException(e);
		}
	}

	@Transactional
	public Long getAssetSearchResultCount(SearchExpression searchExpression) throws StoreClientServiceException {
		log.debug("enter getAssetSearchResultCount:searcheExpression=" + searchExpression.toString());
		try {
			List<com.hp.sdf.ngp.model.Asset> assets = applicationService.searchAsset(searchExpression);
			if (null != assets) {
				log.debug("getAssetSearchResultCount returns size:" + new Long(assets.size()));
				return new Long(assets.size());
			}
			log.debug("getAssetSearchResultCount returns 0");
			return new Long(0);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Comment exception: " + e);
			throw new StoreClientServiceException(e);
		}
	}

	@Transactional
	public BinaryVersion getBinaryVersion(Long versionId) throws StoreClientServiceException {
		log.debug("enter getBinaryVersion:versionId=" + versionId);
		try {
			AssetBinaryVersion assetBinaryVersion = applicationService.getAssetBinaryById(versionId);
			if (null != assetBinaryVersion) {
				BinaryVersionImpl binaryVersionImpl = new BinaryVersionImpl(assetBinaryVersion);
				modelObjectFiller.fillBinaryVersion(binaryVersionImpl);

				log.debug("getBinaryVersion returns:" + binaryVersionImpl.toString());
				return binaryVersionImpl;
			} else {

				log.debug("getBinaryVersion returns null");
				throw new StoreClientEntityNotFoundException("getBinaryVersion returns null");
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Comment exception: " + e);

			if (e instanceof StoreClientEntityNotFoundException) {
				throw new StoreClientEntityNotFoundException(e);
			}

			throw new StoreClientServiceException(e);
		}
	}

	@Transactional
	public List<Asset> getPurchasedAsset(String userId, int start, int count) throws StoreClientServiceException {
		log.debug("enter getPurchasedAsset:userId" + userId + ",start=" + start + ",count=" + count);

		try {
			List<Asset> assets = new ArrayList<Asset>();
			List<PurchaseHistory> purchaseHistorys = null;
			try {
				purchaseHistorys = purchaseService.getPurchaseHistory(userId, start, count);
			} catch (NgpException e) {
				log.error("Comment exception: " + e);
			}

			if (null != purchaseHistorys) {
				for (PurchaseHistory purchaseHistory : purchaseHistorys) {
					com.hp.sdf.ngp.model.Asset asset = purchaseHistory.getAsset();
					StoreClientAssetImpl assetImpl = new StoreClientAssetImpl(asset);
					modelObjectFiller.fillAsset(assetImpl);
					modelObjectFiller.fillSubAsset(assetImpl);
					assets.add(assetImpl);
				}
			}

			log.debug("getPurchasedAsset returns size:" + assets.size());
			return assets;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Comment exception: " + e);
			throw new StoreClientServiceException(e);
		}
	}

	@Transactional
	public Long getPurchasedAssetCount(String userId) throws StoreClientServiceException {
		log.debug("enter getPurchasedAssetCount:userId=" + userId);

		try {
			List<PurchaseHistory> purchaseHistorys = null;
			try {
				purchaseHistorys = purchaseService.getPurchaseHistory(userId, 0, Integer.MAX_VALUE);
			} catch (NgpException e) {
				log.error("Comment exception: " + e);
			}

			if (null != purchaseHistorys) {
				log.debug("getPurchasedAssetCount returns userId=" + userId);
				return new Long(purchaseHistorys.size());
			}

			log.debug("getPurchasedAssetCount returns 0");
			return new Long(0);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Comment exception: " + e);
			throw new StoreClientServiceException(e);
		}
	}

	@Transactional
	public List<AssetPurchaseHistory> getStorePurchaseHistory(Long assetId, String userId) throws StoreClientServiceException {
		log.debug("enter getStorePurchaseHistory:assetId=" + assetId + ",userId='" + userId + "'");
		try {
			SearchExpression searchExpression = new SearchExpressionImpl();
			searchExpression.addCondition(new PurchaseHistoryAssetIdCondition(assetId, NumberComparer.EQUAL));
			searchExpression.addCondition(new PurchaseHistoryUseridCondition(userId, StringComparer.EQUAL, false, false));

			List<AssetPurchaseHistory> assetPurchaseHistorys = new ArrayList<AssetPurchaseHistory>();
			List<PurchaseHistory> purchaseHistorys = purchaseService.searchPurchaseHistory(searchExpression);
			if (null != purchaseHistorys) {
				for (PurchaseHistory purchaseHistory : purchaseHistorys) {
					AssetPurchaseHistoryImpl historyImpl = new AssetPurchaseHistoryImpl(purchaseHistory);
					modelObjectFiller.fillPurchaseHistory(historyImpl);
					assetPurchaseHistorys.add(historyImpl);
				}
			}

			log.debug("getStorePurchaseHistory returns size:" + assetPurchaseHistorys.size());
			return assetPurchaseHistorys;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Comment exception: " + e);
			throw new StoreClientServiceException(e);
		}
	}

	@Transactional
	public List<AssetPurchaseHistory> getStorePurchaseHistoryByUser(String userId) throws StoreClientServiceException {
		log.debug("enter getStorePurchaseHistoryByUser:userId='" + userId + "'");
		try {
			List<AssetPurchaseHistory> assetPurchaseHistorys = new ArrayList<AssetPurchaseHistory>();
			List<PurchaseHistory> purchaseHistorys = null;
			try {
				purchaseHistorys = purchaseService.getPurchaseHistory(userId, 0, Integer.MAX_VALUE);
			} catch (NgpException e) {
				log.error("Comment exception: " + e);
			}
			if (null != purchaseHistorys) {
				for (PurchaseHistory purchaseHistory : purchaseHistorys) {
					AssetPurchaseHistoryImpl historyImpl = new AssetPurchaseHistoryImpl(purchaseHistory);
					modelObjectFiller.fillPurchaseHistory(historyImpl);
					assetPurchaseHistorys.add(historyImpl);
				}
			}

			log.debug("getStorePurchaseHistoryByUser return sizes:" + assetPurchaseHistorys.size());
			return assetPurchaseHistorys;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Comment exception: " + e);
			throw new StoreClientServiceException(e);
		}
	}

	@Transactional
	public List<AssetRatingComments> getUserCommentsWithRating(Long assetId, int start, int count) throws StoreClientServiceException {
		log.debug("enter getUserCommentsWithRating:assetId=" + assetId + ",start=" + start + ",count=" + count);
		try {
			List<AssetRatingComments> assetRatingComments_list = new ArrayList<AssetRatingComments>();
			List<Comments> commentss = applicationService.getAllCommentsByAssetId(assetId, start, count);
			if (null != commentss) {
				for (Comments comments : commentss) {
					AssetRating assetRating = applicationService.getAssetRating(comments.getUserid(), assetId);
					AssetCommentImpl assetComment = new AssetCommentImpl(comments);
					modelObjectFiller.fillAssetComment(assetComment);
					AssetRatingCommentsImpl assetRatingCommentsImpl = new AssetRatingCommentsImpl();
					assetRatingCommentsImpl.setAssetComment(assetComment);
					assetRatingCommentsImpl.setUserRating(assetRating.getRating().floatValue());
					assetRatingComments_list.add(assetRatingCommentsImpl);
				}
			}

			log.debug("getUserCommentsWithRating returns:" + assetRatingComments_list.size());
			return assetRatingComments_list;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Comment exception: " + e);
			throw new StoreClientServiceException(e);
		}
	}

	@Transactional
	public float getUserRating(Long assetId, String userId) throws StoreClientServiceException {
		log.debug("enter getUserRating:assetId=" + assetId + ",userId='" + userId + "'");
		try {
			AssetRating assetRating = applicationService.getAssetRating(userId, assetId);
			if (null != assetRating) {
				float f = assetRating.getRating().floatValue();

				log.debug("getUserRating returns:" + f);
				return f;
			}

			log.debug("getUserRating returns 0");
			return 0;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Comment exception: " + e);
			throw new StoreClientServiceException(e);
		}
	}

	@Transactional
	public void purchase(String userId, Long assetId, Currency currency, BigDecimal amount) throws StoreClientServiceException {
		// TODO Auto-generated method stub
	}

	@Transactional
	public String retrieveDownloadDescriptor(Long assetId, Long versionId, String userId, String finishURL) throws StoreClientServiceException {
		log.debug("enter retrieveDownloadDescriptor:assetId=" + assetId + ",versionId=" + versionId + ",userId='" + userId + "',finishURL='" + finishURL + "'");
		try {
			String str = oTADownloadManager.getDownloadDescriptor(assetId, versionId, userId, finishURL);

			log.debug("retrieveDownloadDescriptor returns:" + str);
			return str;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Comment exception: " + e);
			throw new StoreClientServiceException(e);
		}
	}

	@Transactional
	public void saveOrUpdateUserRating(Long assetId, String userId, float rating) throws StoreClientServiceException {
		log.debug("enter saveOrUpdateUserRating:assetId=" + assetId + ",userId='" + userId + "',rating=" + rating);
		try {
			applicationService.saveOrUpdateAssetRating(assetId, userId, new Double(rating));

		} catch (Exception e) {
			e.printStackTrace();
			log.error("Comment exception: " + e);
			throw new StoreClientServiceException(e);
		}
	}

	@Transactional
	public List<StoreClientAsset> searchAsset(SearchExpression searchExpression) throws StoreClientServiceException {
		log.debug("enter searchAsset:searchExpression=" + searchExpression.toString());
		try {
			List<StoreClientAsset> storeClientAssets = new ArrayList<StoreClientAsset>();
			List<com.hp.sdf.ngp.model.Asset> assets = applicationService.searchAsset(searchExpression);
			if (null != assets) {
				for (com.hp.sdf.ngp.model.Asset asset : assets) {
					StoreClientAssetImpl assetImpl = new StoreClientAssetImpl(asset);
					modelObjectFiller.fillAsset(assetImpl);
					modelObjectFiller.fillSubAsset(assetImpl);
					storeClientAssets.add(assetImpl);
				}
			}

			log.debug("searchAsset returns:" + storeClientAssets.size());
			return storeClientAssets;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Comment exception: " + e);
			throw new StoreClientServiceException(e);
		}
	}

	@Transactional
	public List<BinaryVersion> searchBinaryVersions(SearchExpression searchExpression) throws StoreClientServiceException {
		log.debug("enter searchBinaryVersions:searchExpression=" + searchExpression.toString());
		try {
			List<BinaryVersion> binaryVersions = new ArrayList<BinaryVersion>();
			List<AssetBinaryVersion> assetBinaryVersions = applicationService.getAssetBinary(searchExpression);
			if (null != assetBinaryVersions) {
				for (AssetBinaryVersion assetBinaryVersion : assetBinaryVersions) {
					BinaryVersionImpl versionImpl = new BinaryVersionImpl(assetBinaryVersion);
					modelObjectFiller.fillBinaryVersion(versionImpl);
					binaryVersions.add(versionImpl);
				}
			}

			log.debug("searchBinaryVersions returns size:" + binaryVersions.size());
			return binaryVersions;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Comment exception: " + e);
			throw new StoreClientServiceException(e);
		}
	}

	@Transactional
	public List<AssetComment> searchAssetComments(SearchExpression searchExpression) throws StoreClientServiceException {
		log.debug("enter searchAssetComments:searchExpression=" + searchExpression.toString());

		try {
			List<AssetComment> assetComments = new ArrayList<AssetComment>();
			List<Comments> commentss = applicationService.searchComments(searchExpression);
			if (null != commentss) {
				for (Comments comments : commentss) {
					AssetCommentImpl assetCommentImpl = new AssetCommentImpl(comments);
					modelObjectFiller.fillAssetComment(assetCommentImpl);
					assetComments.add(assetCommentImpl);
				}
			}

			log.debug("searchAssetComments returns:" + assetComments.size());
			return assetComments;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Comment exception: " + e);
			throw new StoreClientServiceException(e);
		}
	}

	@Transactional
	public AssetComment constructAssetComment() throws StoreClientServiceException {
		return new AssetCommentImpl();
	}

	public void saveOrUpdateAssetComment(AssetComment assetComment) throws StoreClientServiceException {
		log.debug("enter saveOrUpdateAssetComment:" + assetComment.toString());

		try {
			if (!(assetComment instanceof AssetCommentImpl)) {
				throw new StoreClientServiceException("The method parameter assetComment is not a instance of AssetCommentImpl");
			}

			AssetCommentImpl assetCommentImpl = (AssetCommentImpl) assetComment;
			Comments comments = assetCommentImpl.getComments();

			Long assetId = assetComment.getAssetId();
			if (null != assetId) {
				com.hp.sdf.ngp.model.Asset asset = applicationService.getAsset(assetId);
				if (asset == null) {
					throw new StoreClientServiceException("The corresponding asset[" + assetId + "] doesn't exists");
				}
				comments.setAsset(asset);
				comments.setAssetVersion(assetComment.getAssetVersion());
			} else {
				throw new StoreClientServiceException("com.hp.sdf.ngp.model.Asset is not-null property");
			}

			if (null == comments.getId()) {
				SearchExpression expression=new SearchExpressionImpl();
				if(comments.getAsset()!=null && comments.getAsset().getId()!=null){
					expression.addCondition(new CommentsAssetIdCondition(comments.getAsset().getId(), NumberComparer.EQUAL));
				}
				if(comments.getUserid()!=null){
					expression.addCondition(new CommentsUseridCondition(comments.getUserid(), StringComparer.EQUAL, false, false));
				}
				List <Comments> commentses=applicationService.searchComments(expression);
				if(commentses!=null && commentses.size()>0){
					for(Comments comment: commentses){
						applicationService.deleteComments(comment.getId());
					}
				}
				applicationService.saveComments(comments);
			} else {
				applicationService.updateComments(comments);
			}

		} catch (CensoredWordFoundException cwfe) {
			log.error("UserCommentsCensorFailException exception: " + cwfe);
			throw new UserCommentsCensorFailException(cwfe);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Comment exception: " + e);
			throw new StoreClientServiceException(e);
		}
	}

	@Transactional
	public Long getAssetCommentsSearchResultCount(SearchExpression searchExpression) throws StoreClientServiceException {
		log.debug("enter getAssetCommentsSearchResultCount:searchExpression=" + searchExpression.toString());

		try {
			List<Comments> commentss = applicationService.searchComments(searchExpression);
			if (null != commentss) {
				log.debug("getAssetCommentsSearchResultCount returns:" + new Long(commentss.size()));
				return new Long(commentss.size());
			}

			log.debug("getAssetCommentsSearchResultCount returns 0");
			return new Long(0);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Comment exception: " + e);
			throw new StoreClientServiceException(e);
		}
	}

	@Transactional
	public Long getBinaryVersionSearchResultCount(SearchExpression searchExpression) throws StoreClientServiceException {
		log.debug("enter getBinaryVersionSearchResultCount:searchExpression:" + searchExpression.toString());
		try {
			List<AssetBinaryVersion> assetBinaryVersions = applicationService.getAssetBinary(searchExpression);
			if (null != assetBinaryVersions) {
				log.debug("getBinaryVersionSearchResultCount returns " + new Long(assetBinaryVersions.size()));
				return new Long(assetBinaryVersions.size());
			}

			log.debug("getBinaryVersionSearchResultCount returns 0");
			return new Long(0);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Comment exception: " + e);
			throw new StoreClientServiceException(e);
		}
	}

	@Transactional
	public void recordAssetDownloadHistory(String userId, Long assetId, Long versionId) throws StoreClientServiceException {
		log.debug("enter recordAssetDownloadHistory:userId=" + userId + ",assetId=" + assetId + ",versionId=" + versionId);
		try {
			com.hp.sdf.ngp.model.Asset asset = applicationService.getAsset(assetId);

			UserDownloadHistory userDownloadHistory = new UserDownloadHistory();
			userDownloadHistory.setUserid(userId);
			userDownloadHistory.setAsset(asset);

			Status status = asset.getStatus();
			if (null != status) {
				userDownloadHistory.setStatus(status.getStatus());
			} else {
				assetLifeCycleEngine.getStartupStatus();
			}

			AssetBinaryVersion assetBinaryVersion = applicationService.getAssetBinaryById(versionId);
			if (null != assetBinaryVersion) {
				userDownloadHistory.setVersion(assetBinaryVersion.getVersion());
			}

			applicationService.saveOrUpdateUserDownloadHistory(userDownloadHistory);

			long downloadCount = asset.getDownloadCount() + 1;
			asset.setDownloadCount(downloadCount);
			applicationService.updateAsset(asset);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Comment exception: " + e);
			throw new StoreClientServiceException(e);
		}
	}

	@Transactional
	public URL retriveBinaryVersionURL(Long assetId, Long versionId, String userId) throws StoreClientServiceException {
		log.debug("enter retriveBinaryVersionURL:assetId=" + assetId + ",versionId=" + versionId + ",userId='" + userId + "'");

		AssetBinaryVersion assetBinaryVesrion = applicationService.getAssetBinaryById(versionId);
		if (null != assetBinaryVesrion) {
			String str = filePath.replace("//", "/") + assetBinaryVesrion.getLocation();
			try {

				log.debug("retriveBinaryVersionURL returns:" + new URL("file:///" + str));
				return new URL("file:///" + str);
			} catch (MalformedURLException e) {
				e.printStackTrace();
				log.error("Comment exception: " + e);
				throw new StoreClientServiceException(e);
			}
		}
		return null;
	}

	@Transactional
	public byte[] getFileFromDatabase(String fileLocation) throws StoreClientServiceException {
		log.debug("getFile " + fileLocation + "FromDatabase");
		if (null == fileLocation) {
			throw new StoreClientServiceException("file path is null.");
		}

		return applicationService.getFileFromDatabase(fileLocation);
	}

	public String getStoreLocationPrefix() throws StoreClientServiceException {
		return applicationService.getFilePath();
	}

}

// $Id$