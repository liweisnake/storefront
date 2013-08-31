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
package com.hp.sdf.ngp.ws.assetcatalog;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.hp.sdf.ngp.api.assetcatalog.AssetCatalogService;
import com.hp.sdf.ngp.api.exception.AssetCatalogServiceException;
import com.hp.sdf.ngp.api.model.Asset;
import com.hp.sdf.ngp.api.model.AssetCategory;
import com.hp.sdf.ngp.api.model.AssetProvider;

@Controller
public class AssetCatalogServicesController {
	private final static Log log = LogFactory.getLog(AssetCatalogServicesController.class);

	@Resource
	private AssetCatalogService assetCatalogService;

	@RequestMapping(value = "/constructAsset", method = RequestMethod.POST)
	public ModelAndView constructAssetObject() {
		Asset result = null;
		try {
			result = assetCatalogService.constructAssetObject();
		} catch (AssetCatalogServiceException e) {
			log.error("Comment exception: " + e);
		}
		Map<String, Asset> model = new HashMap<String, Asset>();
		log.debug("comment>>result:" + result);
		model.put("result", result);
		ModelAndView mav = new ModelAndView("jsonView", model);
		return mav;
	}

	@RequestMapping(value = "/constructProvider", method = RequestMethod.POST)
	public ModelAndView constructProviderObject() {
		AssetProvider result = null;
//		result = assetCatalogService.constructProviderObject();
		Map<String, AssetProvider> model = new HashMap<String, AssetProvider>();
		log.debug("comment>>result:" + result);
		model.put("result", result);
		ModelAndView mav = new ModelAndView("jsonView", model);
		return mav;
	}

	@RequestMapping(value = "/assetGroup/parentAssetId/{parentAssetId}/childAssetIds/{childAssetIds}", method = RequestMethod.POST)
	public ModelAndView createAssetGroup(@PathVariable long parentAssetId, @PathVariable List<Long> childAssetIds) {
//		assetCatalogService.createAssetGroup(parentAssetId, childAssetIds);
		log.debug("comment>>result:successful");
		ModelAndView mav = new ModelAndView("jsonView");
		return mav;
	}
	
	@RequestMapping(value = "/createCategory/name/{name}/description/{description}", method = RequestMethod.POST)
	public ModelAndView createCategory(@PathVariable String name,@PathVariable String description) {
		AssetCategory result = null;
//		result = assetCatalogService.createCategory(name,description);
		Map<String, AssetCategory> model = new HashMap<String, AssetCategory>();
		log.debug("comment>>result:" + result);
		model.put("result", result);
		ModelAndView mav = new ModelAndView("jsonView", model);
		return mav;
	}
	
	@RequestMapping(value = "/categoryRelation/parentCategoryId/{parentCategoryId}/childCategoryIds/{childCategoryIds}", method = RequestMethod.POST)
	public ModelAndView createCategoryRelationShip(@PathVariable long parentCategoryId, @PathVariable List<Long> childCategoryIds) {
//		assetCatalogService.createCategoryRelationShip(parentCategoryId, childCategoryIds);
		log.debug("comment>>result:successful");
		ModelAndView mav = new ModelAndView("jsonView");
		return mav;
	}
	
	@RequestMapping(value = "/assetProvider/id/{id}", method = RequestMethod.POST)
	public ModelAndView getAssetProviderById(@PathVariable String id) {
		AssetProvider result = null;
//		result = assetCatalogService.getAssetProviderById(id);
		Map<String, AssetProvider> model = new HashMap<String, AssetProvider>();
		log.debug("comment>>result:" + result);
		model.put("result", result);
		ModelAndView mav = new ModelAndView("jsonView", model);
		return mav;
	}
	
	@RequestMapping(value = "/assetProvider/name/{name}", method = RequestMethod.POST)
	public ModelAndView getAssetProviderByName(@PathVariable String name) {
		AssetProvider result = null;
//		result = assetCatalogService.getAssetProviderByName(name);
		Map<String, AssetProvider> model = new HashMap<String, AssetProvider>();
		log.debug("comment>>result:" + result);
		model.put("result", result);
		ModelAndView mav = new ModelAndView("jsonView", model);
		return mav;
	}
	
	@RequestMapping(value = "/getCategory/name/{name}", method = RequestMethod.POST)
	public ModelAndView getCategoryByName(@PathVariable String name) {
		AssetCategory result = null;
//		result = assetCatalogService.getCategoryByName(name);
		Map<String, AssetCategory> model = new HashMap<String, AssetCategory>();
		log.debug("comment>>result:" + result);
		model.put("result", result);
		ModelAndView mav = new ModelAndView("jsonView", model);
		return mav;
	}
	
	@RequestMapping(value = "/retrieveAsset/id/{id}", method = RequestMethod.POST)
	public ModelAndView retrieveAssetbyID(@PathVariable long id) {
		Asset result = null;
//		result = assetCatalogService.retrieveAssetbyID(id);
		Map<String, Asset> model = new HashMap<String, Asset>();
		log.debug("comment>>result:" + result);
		model.put("result", result);
		ModelAndView mav = new ModelAndView("jsonView", model);
		return mav;
	}
	
	@RequestMapping(value = "/mergeAsset/asset/{asset}", method = RequestMethod.POST)
	public ModelAndView saveOrUpdateAsset(@PathVariable Asset asset) {
//		try {
//			assetCatalogService.saveOrUpdateAsset(asset);
//		} catch (AssetCatalogServiceException e) {
//			log.error("Comment exception: " + e);
//		}
		log.debug("comment>>result:successful");
		ModelAndView mav = new ModelAndView("jsonView");
		return mav;
	}
	
	@RequestMapping(value = "/mergeProvider/assetProvider/{assetProvider}", method = RequestMethod.POST)
	public ModelAndView updateAssetProvider(@PathVariable AssetProvider assetProvider) {
		try {
			assetCatalogService.updateAssetProvider(assetProvider);
		} catch (AssetCatalogServiceException e) {
			log.error("Comment exception: " + e);
		}
		log.debug("comment>>result:successful");
		ModelAndView mav = new ModelAndView("jsonView");
		return mav;
	}
}

// $Id$