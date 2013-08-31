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
package com.hp.sdf.ngp.ws.catalog.rest;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.hp.sdf.ngp.ws.catalog.AssetCatalogWebService;
import com.hp.sdf.ngp.ws.model.Asset;

@Controller
public class AssetCatalogServiceController {

	@Autowired
	private AssetCatalogWebService assetCatalogService;

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/assets/")
	public ModelAndView getAllAssets() {
		List result = new ArrayList();
		List<Asset> assets = assetCatalogService.getAllAssets();
		if (assets == null || assets.size() == 0)
			result.add("no result matched");
		else
			result = assets;
		ModelAndView mav = new ModelAndView("assetXmlView",
				BindingResult.MODEL_KEY_PREFIX + "assets", result);
		return mav;
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/assets/paging/{start}/{max}")
	public ModelAndView getAssets(@PathVariable int start, @PathVariable int max) {
		List result = new ArrayList();
		List<Asset> assets = assetCatalogService.getAssets(start, max);
		if (assets == null || assets.size() == 0)
			result.add("no result matched");
		else
			result = assets;
		ModelAndView mav = new ModelAndView("assetXmlView",
				BindingResult.MODEL_KEY_PREFIX + "assets", result);
		return mav;
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/categories/")
	public ModelAndView getAllCategories() {
		List<String> categories = assetCatalogService.getAllCategories();
		List result = new ArrayList();
		if (categories == null || categories.size() == 0)
			result.add("no result matched");
		else
			result = categories;
		ModelAndView mav = new ModelAndView("assetXmlView",
				BindingResult.MODEL_KEY_PREFIX + "categories", result);
		return mav;
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/assets/date/{date}")
	public ModelAndView getAssetsByDate(@PathVariable String value) {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		List result = new ArrayList();

		try {
			Date date = df.parse(value);

			List<Asset> assets = assetCatalogService.getAssetsByDate(date);
			if (assets == null || assets.size() == 0)
				result.add("no result matched");
			else
				result = assets;

		} catch (Throwable e) {
			result.add("The date format is wrong, should be yyyy-MM-dd hh:mm:ss");
		}
		ModelAndView mav = new ModelAndView("assetXmlView",
				BindingResult.MODEL_KEY_PREFIX + "assets", result);
		return mav;
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/assets/category/{category}")
	public ModelAndView getAssetsByCategory(@PathVariable String category) {
		List<Asset> assets = assetCatalogService.getAssetsByCategory(category);
		List result = new ArrayList();
		if (assets == null || assets.size() == 0)
			result.add("no result matched");
		else
			result = assets;
		ModelAndView mav = new ModelAndView("assetXmlView",
				BindingResult.MODEL_KEY_PREFIX + "assets", result);
		return mav;
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/developers/{developer}/assets/")
	public ModelAndView getAssetsByDeveloper(@PathVariable String developer) {
		List result = new ArrayList();
		List<Asset> assets = assetCatalogService
				.getAssetsByDeveloper(developer);
		if (assets == null || assets.size() == 0)
			result.add("no result matched");
		else
			result = assets;
		ModelAndView mav = new ModelAndView("assetXmlView",
				BindingResult.MODEL_KEY_PREFIX + "assets", result);
		return mav;
	}

}

// $Id$