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
package com.hp.sdf.ngp.manager.impl;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Currency;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.axis.utils.StringUtils;
import org.springframework.stereotype.Component;

import com.hp.sdf.ngp.common.constant.AssetConstants;
import com.hp.sdf.ngp.manager.ApplicationManager;
import com.hp.sdf.ngp.manager.DownloadConnector;
import com.hp.sdf.ngp.manager.DownloadManager;
import com.hp.sdf.ngp.model.Asset;
import com.hp.sdf.ngp.model.AssetLifecycleAction;
import com.hp.sdf.ngp.model.AssetLifecycleActionHistory;
import com.hp.sdf.ngp.model.Comments;
import com.hp.sdf.ngp.model.ShoppingCart;
import com.hp.sdf.ngp.model.UserLifecycleAction;
import com.hp.sdf.ngp.model.UserLifecycleActionHistory;
import com.hp.sdf.ngp.service.ApplicationService;
import com.hp.sdf.ngp.service.PurchaseService;

@Component
public class ApplicationManagerImpl implements ApplicationManager,
		DownloadManager {

	private Map<String, DownloadConnector> downloadConnectors = new HashMap<String, DownloadConnector>();

	@Resource
	private ApplicationService applicationService;

	@Resource
	private PurchaseService purchaseService;

	public void comment(Long assetId, String userId, String commentContent) {
		Comments comment = new Comments();
		Asset asset = new Asset();
		asset.setId(assetId);
		comment.setContent(commentContent);
		comment.setUserid(userId);
		comment.setCreateDate(new Date());
		comment.setAsset(asset);
		applicationService.saveComments(comment);

	}

	public void purchase(long assetId, String userId, Currency currency,
			BigDecimal amount) {

		ShoppingCart shoppingCart = purchaseService.addToShoppingCart(userId,
				assetId, currency, amount);
		purchaseService.completePurchase(Arrays.asList(shoppingCart));

	}

	public void rating(Long assetId, String userId, Double rating) {
		// TODO Auto-generated method stubapplicationService
		applicationService.saveOrUpdateAssetRating(assetId, userId, rating);

	}

	public String retrieveDownloadLink(Long assetId, Long versionId,
			String userId, String deviceSerial) {

		Asset asset = applicationService.getAsset(assetId);
		DownloadConnector downloadConnector = downloadConnectors.get(asset
				.getSource());

		if (downloadConnector == null) {
			downloadConnector = downloadConnectors
					.get(AssetConstants.ASSET_SOURCE_STOREFRONT);
		}
		return downloadConnector.retrievedownloadURI(assetId, versionId,
				deviceSerial);

	}

	public void registerConnector(String key,
			DownloadConnector downloadConnector) {
		downloadConnectors.put(key, downloadConnector);
	}

	public AssetLifecycleActionHistory genActionHistoryFromAction(
			AssetLifecycleAction assetLifecycleAction) {

		AssetLifecycleActionHistory assetLifecycleActionHistory = new AssetLifecycleActionHistory();

		assetLifecycleActionHistory.setComments(assetLifecycleAction
				.getComments());

		if (null != assetLifecycleAction.getCompleteDate()) {
			assetLifecycleActionHistory.setCompleteDate(assetLifecycleAction
					.getCompleteDate());
		} else {
			assetLifecycleActionHistory.setCompleteDate(new Date());
		}

		if (null != assetLifecycleAction.getCreateDate()) {
			assetLifecycleActionHistory.setCreateDate(assetLifecycleAction
					.getCreateDate());
		} else {
			assetLifecycleActionHistory.setCreateDate(new Date());
		}

		assetLifecycleActionHistory.setEvent(assetLifecycleAction.getEvent());

		assetLifecycleActionHistory.setResult(assetLifecycleAction.getResult());

		assetLifecycleActionHistory.setSubmitterId(assetLifecycleAction
				.getSubmitterid());
		assetLifecycleActionHistory
				.setAssetBinaryVersionId(assetLifecycleAction
						.getBinaryVersion().getId());
		assetLifecycleActionHistory.setAssetId(assetLifecycleAction.getAsset()
				.getId());
		assetLifecycleActionHistory.setCommentType(assetLifecycleAction
				.getCommentType());
		assetLifecycleActionHistory.setDescription(assetLifecycleAction
				.getDescription());

		assetLifecycleActionHistory.setNotificationDate(assetLifecycleAction
				.getNotificationDate());

		assetLifecycleActionHistory.setOwnerId(assetLifecycleAction
				.getOwnerid());

		if (null != assetLifecycleAction.getPostStatus())
			assetLifecycleActionHistory.setPostStatus(assetLifecycleAction
					.getPostStatus().getStatus());

		if (null != assetLifecycleAction.getPreStatus())
			assetLifecycleActionHistory.setPrestatus(assetLifecycleAction
					.getPreStatus().getStatus());

		if (!StringUtils.isEmpty(assetLifecycleAction.getProcessStatus()))
			assetLifecycleActionHistory.setProcessStatus(assetLifecycleAction
					.getProcessStatus());

		assetLifecycleActionHistory.setSubmitterId(assetLifecycleAction
				.getSubmitterid());

		return assetLifecycleActionHistory;
	}

	public UserLifecycleActionHistory genActionHistoryFromAction(
			UserLifecycleAction userLifecycleAction) {

		UserLifecycleActionHistory userLifecycleActionHistory = new UserLifecycleActionHistory();

		userLifecycleActionHistory.setComments(userLifecycleAction
				.getComments());

		userLifecycleActionHistory.setCreateDate(new Date());

		userLifecycleActionHistory.setEvent(userLifecycleAction.getEvent());

		userLifecycleActionHistory.setResult("");

		userLifecycleActionHistory.setSubmitterid(userLifecycleAction
				.getSubmitterid());

		userLifecycleActionHistory.setDescription(userLifecycleAction
				.getDescription());

		userLifecycleActionHistory.setUserid(userLifecycleAction.getUserid());

		userLifecycleActionHistory.setOwnerid(userLifecycleAction.getOwnerid());

		if (null != userLifecycleAction.getPostRole())
			userLifecycleActionHistory.setPostRole(userLifecycleAction
					.getPostRole());

		if (null != userLifecycleAction.getPreRole())
			userLifecycleActionHistory.setPreRole(userLifecycleAction
					.getPreRole());

		if (!StringUtils.isEmpty(userLifecycleAction.getProcessStatus()))
			userLifecycleActionHistory.setProcessStatus(userLifecycleAction
					.getProcessStatus());

		return userLifecycleActionHistory;
	}

}

// $Id$