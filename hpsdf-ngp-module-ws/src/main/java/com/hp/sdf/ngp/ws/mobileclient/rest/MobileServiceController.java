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
package com.hp.sdf.ngp.ws.mobileclient.rest;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.hp.sdf.ngp.ws.mobileclient.MobileClientService;
import com.hp.sdf.ngp.ws.mobileclient.model.MobileAsset;
import com.hp.sdf.ngp.ws.mobileclient.model.MobileComment;

@Controller
public class MobileServiceController {

	private final static Log log = LogFactory
			.getLog(MobileServiceController.class);

	@Autowired
	MobileClientService mobileService;

	@RequestMapping(value = "/category")
	public ModelAndView getAllCategories() {
		List<String> categories = mobileService.getAllCategory();
		Map<String, List<String>> model = new HashMap<String, List<String>>();
		log.debug("getAllCategories>>categories:" + categories);
		model.put("result", categories);
		ModelAndView mav = new ModelAndView("jsonView", model);
		return mav;
	}

	@RequestMapping(value = "/asset/categoryName/{categoryName}/start/{start}/count/{count}")
	public ModelAndView getAssetsByCategoryName(
			@PathVariable String categoryName, @PathVariable int start,
			@PathVariable int count) {
		List<MobileAsset> list = mobileService.getAssetsByCategoryName(
				decodeVariable(categoryName), start, count);
		Map<String, List<MobileAsset>> model = new HashMap<String, List<MobileAsset>>();
		log.debug("getAssetsByCategoryName>>list:" + list.size());
		model.put("result", list);
		ModelAndView mav = new ModelAndView("jsonView", model);
		return mav;
	}

	@RequestMapping(value = "/assetCount/categoryName/{categoryName}")
	public ModelAndView getAssetsCountByCategoryName(
			@PathVariable String categoryName) {
		long result = mobileService
				.getAssetsCountByCategoryName(decodeVariable(categoryName));
		Map<String, Long> model = new HashMap<String, Long>();
		log.debug("getAssetsCountByCategoryName>>result:" + result);
		model.put("result", result);
		ModelAndView mav = new ModelAndView("jsonView", model);
		return mav;
	}

	@RequestMapping(value = "/myAsset/userId/{userId}/start/{start}/count/{count}")
	public ModelAndView getMyPurchasedAsset(@PathVariable String userId,
			@PathVariable int start, @PathVariable int count) {
		List<MobileAsset> list = mobileService.getMyPurchasedAsset(
				decodeVariable(userId), start, count);
		Map<String, List<MobileAsset>> model = new HashMap<String, List<MobileAsset>>();
		log.debug("getMyPurchasedAsset>>list:" + list.size());
		model.put("result", list);
		ModelAndView mav = new ModelAndView("jsonView", model);
		return mav;
	}

	@RequestMapping(value = "/getMyPurchasedAssetCount/userId/{userId}")
	public ModelAndView getMyPurchasedAssetCount(@PathVariable String userId) {
		long result = mobileService.getMyPurchasedAssetCount(userId);
		Map<String, Long> model = new HashMap<String, Long>();
		log.debug("search>>result:" + result);
		model.put("result", result);
		ModelAndView mav = new ModelAndView("jsonView", model);
		return mav;
	}

	@RequestMapping(value = "/recommendedAssets")
	public ModelAndView getRecommendedAssets() {
		List<MobileAsset> list = mobileService.getRecommendedAssets();
		Map<String, List<MobileAsset>> model = new HashMap<String, List<MobileAsset>>();
		log.debug("getRecommendedAssets>>list:" + list.size());
		model.put("result", list);
		ModelAndView mav = new ModelAndView("jsonView", model);
		return mav;
	}

	@RequestMapping(value = "/topAssets")
	public ModelAndView getTopAssets() {
		List<MobileAsset> list = mobileService.getTopAssets();
		Map<String, List<MobileAsset>> model = new HashMap<String, List<MobileAsset>>();
		log.debug("getTopAssets>>list:" + list.size());
		model.put("result", list);
		ModelAndView mav = new ModelAndView("jsonView", model);
		return mav;
	}

	@RequestMapping(value = "/searchCount/keyword/{keyword}")
	public ModelAndView searchCount(@PathVariable String keyword) {
		long result = mobileService.searchCount(keyword);
		Map<String, Long> model = new HashMap<String, Long>();
		log.debug("search>>result:" + result);
		model.put("result", result);
		ModelAndView mav = new ModelAndView("jsonView", model);
		return mav;
	}

	@RequestMapping(value = "/search/keyword/{keyword}/start/{start}/count/{count}")
	public ModelAndView search(@PathVariable String keyword,
			@PathVariable int start, @PathVariable int count) {
		List<MobileAsset> list = mobileService.search(keyword, start, count);
		Map<String, List<MobileAsset>> model = new HashMap<String, List<MobileAsset>>();
		log.debug("search>>list:" + list.size());
		model.put("result", list);
		ModelAndView mav = new ModelAndView("jsonView", model);
		return mav;
	}

	@RequestMapping(value = "/purchase/assetId/{assetId}/userId/{userId}")
	public ModelAndView purchase(@PathVariable Long assetId,
			@PathVariable String userId) {
		boolean result = mobileService
				.purchase(assetId, decodeVariable(userId));
		Map<String, Boolean> model = new HashMap<String, Boolean>();
		log.debug("purchase>>result:" + result);
		model.put("result", result);
		ModelAndView mav = new ModelAndView("jsonView", model);
		return mav;
	}

	@RequestMapping(value = "/getAssetComment/assetId/{assetId}/start/{start}/count/{count}")
	public ModelAndView getAssetComment(@PathVariable Long assetId,
			@PathVariable int start, @PathVariable int count) {

		List<MobileComment> result = mobileService.getAssetComment(assetId,
				start, count);
		log.debug("purchase>>result:" + result);

		Map<String, List<MobileComment>> model = new HashMap<String, List<MobileComment>>();
		model.put("result", result);
		ModelAndView mav = new ModelAndView("jsonView", model);
		return mav;
	}

	@RequestMapping(value = "/getAssetCommentCount/assetId/{assetId}")
	public ModelAndView getAssetCommentCount(@PathVariable Long assetId) {
		long result = mobileService.getAssetCommentCount(assetId);
		log.debug("purchase>>result:" + result);

		Map<String, Long> model = new HashMap<String, Long>();
		model.put("result", result);
		ModelAndView mav = new ModelAndView("jsonView", model);
		return mav;
	}

	@RequestMapping(value = "/comment/assetId/{assetId}/userId/{userId}/content/{content}", method = RequestMethod.POST)
	public ModelAndView comment(@PathVariable Long assetId,
			@PathVariable String userId, @PathVariable String content) {
		boolean result = mobileService.comment(assetId, decodeVariable(userId),
				decodeVariable(content));
		Map<String, Boolean> model = new HashMap<String, Boolean>();
		log.debug("comment>>result:" + result);
		model.put("result", result);
		ModelAndView mav = new ModelAndView("jsonView", model);
		return mav;
	}

	@RequestMapping(value = "/getAssetRating/assetId/{assetId}/userId/{userId}")
	public ModelAndView getAssetRating(@PathVariable Long assetId,
			@PathVariable String userId) {
		Double result = mobileService.getAssetRating(assetId,
				decodeVariable(userId));
		Map<String, Double> model = new HashMap<String, Double>();
		log.debug("comment>>result:" + result);
		model.put("result", result);
		ModelAndView mav = new ModelAndView("jsonView", model);
		return mav;
	}

	@RequestMapping(value = "/rating/assetId/{assetId}/userId/{userId}/content/{content}", method = RequestMethod.POST)
	public ModelAndView rating(@PathVariable Long assetId,
			@PathVariable String userId, @PathVariable Double content) {
		boolean result = mobileService.rating(assetId, decodeVariable(userId),
				content);
		Map<String, Boolean> model = new HashMap<String, Boolean>();
		log.debug("rating>>result:" + result);
		model.put("result", result);
		ModelAndView mav = new ModelAndView("jsonView", model);
		return mav;
	}

	@RequestMapping(value = "/retrieveDownloadLink/assetId/{assetId}/userId/{userId}/deviceSerial/{deviceSerial}")
	public ModelAndView retrieveDownloadLink(@PathVariable Long assetId,
			@PathVariable String userId, @PathVariable String deviceSerial) {
		String url = mobileService.retrieveDownloadLink(assetId,
				decodeVariable(userId), deviceSerial);
		Map<String, String> model = new HashMap<String, String>();
		log.debug("retrieveDownloadLink>>url:" + url);
		model.put("result", url);
		ModelAndView mav = new ModelAndView("jsonView", model);
		return mav;
	}

	@RequestMapping(value = "/isValidUser/userId/{userId}/password/{password}")
	public ModelAndView isValidUser(@PathVariable String userId,
			@PathVariable String password) {
		boolean result = mobileService.isValidUser(decodeVariable(userId),
				decodeVariable(password));
		Map<String, Boolean> model = new HashMap<String, Boolean>();
		log.debug("isValidUser>>result:" + result);
		model.put("result", result);
		ModelAndView mav = new ModelAndView("jsonView", model);
		return mav;
	}

	private static String decodeVariable(String encodedPathVariable) {
		String pathVariable = "";
		try {
			pathVariable = java.net.URLDecoder.decode(encodedPathVariable,
					"UTF-8");
		} catch (UnsupportedEncodingException e) {
			log.error("URLEncoder:" + e);
		}

		log.debug("pathVariable after decode : " + pathVariable);
		return pathVariable;
	}

}
