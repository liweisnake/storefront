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
package com.hp.sdf.ngp.custom.sbm.api.storeclient.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.hp.sdf.ngp.api.assetcatalog.AssetCatalogService;
import com.hp.sdf.ngp.api.exception.AssetCatalogServiceException;
import com.hp.sdf.ngp.api.search.SearchExpression;
import com.hp.sdf.ngp.custom.sbm.api.storeclient.model.ContentItem;
import com.hp.sdf.ngp.custom.sbm.api.storeclient.model.PurchaseHistoryExtend;

/**
 * JNDI binding location:
 * /com/hp/sdf/ngp/custom/sbm/api/storeclient/service/ContentCatalogService
 * 
 * 
 */
public interface ContentCatalogService extends AssetCatalogService {

	public ContentItem constructContentItem()
			throws AssetCatalogServiceException;

	/**
	 * Adds a content item.
	 * 
	 * @param contentItem
	 *            content item.
	 * @return a content item.
	 */
	public Long addContentItem(ContentItem contentItem)
			throws AssetCatalogServiceException;

	/**
	 * Finds purchase histories accroding to search conditions.
	 * 
	 * @param searchExpression
	 *            search conditions.
	 * @return content items matches search conditions.
	 */
	public List<PurchaseHistoryExtend> searchPurchaseHistory(
			SearchExpression searchExpression)
			throws AssetCatalogServiceException;

	/**
	 * Updates a content item.
	 * 
	 * @param contentItem
	 *            a content item
	 */
	public void updateContentItem(ContentItem contentItem)
			throws AssetCatalogServiceException;

	/**
	 * Deletes a content item.
	 * 
	 * @param itemId
	 *            the ID of the item
	 */
	public void deleteContentItem(Long itemId)
			throws AssetCatalogServiceException;

	public void deleteBatchContentItem(SearchExpression searchExpression)
			throws AssetCatalogServiceException;

	/**
	 * Deletes a batch of security tokens.
	 * 
	 * @param searchExpression
	 *            the search condition
	 */
	public void deleteBatchSecurityToken(SearchExpression searchExpression)
			throws AssetCatalogServiceException;

	/**
	 * Deletes a batch of purchase history.
	 * 
	 * @param searchExpression
	 *            the search condition
	 */
	public void deleteBatchPurchaseHistoryExtend(
			SearchExpression searchExpression)
			throws AssetCatalogServiceException;

	// public SbmContentPrice constructSbmContentPrice();

	// public void addSbmContentPrice(SbmContentPrice sbmContentPrice);

	/**
	 * Call database procedure to group some metadata of assets/versions.
	 */
	public int callDatabaseProcedure(String procedureName,
			List<Object> parameters) throws AssetCatalogServiceException;;

	/**
	 * A special method to get the external ID with updated date of the stored
	 * binary versions.
	 * 
	 * Invoker should ensure the external ID is unique and not null.
	 * 
	 * @param excludeStatus
	 * @param source
	 * @param updateDateEavAttrName
	 * @return externalId with updated date.
	 */
	public Map<String, Date> generateBinaryVersionIndex(
			List<String> excludeStatus, String source,
			String updateDateEavAttrName) throws AssetCatalogServiceException;
}

// $Id$