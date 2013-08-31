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

import java.util.List;

import com.hp.sdf.ngp.api.exception.StoreClientServiceException;
import com.hp.sdf.ngp.api.search.SearchExpression;
import com.hp.sdf.ngp.api.storeclient.StoreClientService;
import com.hp.sdf.ngp.custom.sbm.api.storeclient.model.ContentItem;
import com.hp.sdf.ngp.custom.sbm.api.storeclient.model.HandSetDevice;
import com.hp.sdf.ngp.custom.sbm.api.storeclient.model.ParentAssetVersionSummary;
import com.hp.sdf.ngp.custom.sbm.api.storeclient.model.PurchaseHistoryExtend;
import com.hp.sdf.ngp.custom.sbm.api.storeclient.model.SecurityToken;

/**
 * Operations customized for SBM. Operates PurchaseHistoryExtend and
 * ContentItem. JNDI binding location:
 * /com/hp/sdf/ngp/custom/sbm/api/storeclient/service/ClientContentService
 * 
 */
public interface ClientContentService extends StoreClientService {

	/**
	 * Returns a record of SBM purchase history.
	 * 
	 * @return a record of SBM purchase history.
	 */
	public PurchaseHistoryExtend constructPurchaseHistoryExtend()
			throws StoreClientServiceException;

	/**
	 * Adds a record of SBM purchase history.
	 * 
	 * @param purchaseHistory
	 *            the purchase history log
	 */
	public void addPurchaseHistory(PurchaseHistoryExtend purchaseHistory)
			throws StoreClientServiceException;

	/**
	 * Updates a record of SBM purchase history.
	 * 
	 * @param purchaseHistory
	 *            the purchase history log
	 */
	public void updatePurchaseHistory(PurchaseHistoryExtend purchaseHistory)
			throws StoreClientServiceException;

	/**
	 * Finds records of SBM purchase history according to search conditions.
	 * 
	 * @param searchExpression
	 *            the search conditions
	 * @return purchase history logs which meet the search conditions.
	 */
	public List<PurchaseHistoryExtend> searchPurchaseHistory(
			SearchExpression searchExpression)
			throws StoreClientServiceException;

	/**
	 * Finds a ContentItem by itemId and identifierId. The parameter name are
	 * relavent to the column names in DB.
	 * 
	 * @param itemId
	 *            the ID of the content item
	 * @param identifierId
	 *            the content's identifier ID
	 * @return a ContentItem with the given itemId and identifierId.
	 */
	public ContentItem getContentItem(String itemId, String identifierId)
			throws StoreClientServiceException;

	/**
	 * Finds records of SBM content item according to search conditions.
	 * 
	 * @param searchExpression
	 *            search conditions.
	 * @return records of SBM content item according to search conditions.
	 */
	public List<ContentItem> searchContentItem(SearchExpression searchExpression)
			throws StoreClientServiceException;

	/* other methods */
	/**
	 * Checks if a client phone number is a tester number.
	 * 
	 * @param msisdn
	 *            the msisdn code of a client
	 * @return true if the client is a tester phone.
	 */
	public boolean isClientTester(String msisdn)
			throws StoreClientServiceException;

	/**
	 * Constructs a security token.
	 * 
	 * @param token
	 *            the security token
	 */
	public SecurityToken constructSecurityToken()
			throws StoreClientServiceException;

	/**
	 * Adds a security token.
	 * 
	 * @param token
	 *            the security token
	 */
	public void addSecurityToken(SecurityToken token)
			throws StoreClientServiceException;

	/**
	 * Updates a security token.
	 * 
	 * @param token
	 *            the security token
	 */
	public void updateSecurityToken(SecurityToken token)
			throws StoreClientServiceException;

	/**
	 * Deletes a security token for a user.
	 * 
	 * @param token
	 *            the user ID
	 */
	public void deleteSecurityToken(String token)
			throws StoreClientServiceException;

	/**
	 * Gets a user security token.
	 * 
	 * @param token
	 *            the security token
	 * @return security token
	 */
	public SecurityToken getSecurityToken(String token)
			throws StoreClientServiceException;

	/**
	 * Finds a user security token by IMSI.
	 * 
	 * @param msisdn
	 *            the user msisdn
	 * @return security token
	 */
	public List<SecurityToken> searchSecurityToken(
			SearchExpression searchExpression)
			throws StoreClientServiceException;

	/**
	 * Finds Handset device for its filter strings. The only condition is the
	 * Device_Name which is the key of the handset.
	 * 
	 * @param searchExpression
	 * @return
	 * @throws StoreClientServiceException
	 */
	public List<HandSetDevice> searchHandSetDevice(
			SearchExpression searchExpression)
			throws StoreClientServiceException;

	public List<ParentAssetVersionSummary> searchParentAssetVersionSummary(
			SearchExpression searchExpression)
			throws StoreClientServiceException;
}

// $Id$