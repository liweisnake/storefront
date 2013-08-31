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
package com.hp.sdf.ngp.manager;

import java.math.BigDecimal;
import java.util.Currency;

import com.hp.sdf.ngp.model.AssetLifecycleAction;
import com.hp.sdf.ngp.model.AssetLifecycleActionHistory;
import com.hp.sdf.ngp.model.UserLifecycleAction;
import com.hp.sdf.ngp.model.UserLifecycleActionHistory;

public interface ApplicationManager {


	public UserLifecycleActionHistory genActionHistoryFromAction(UserLifecycleAction userLifecycleAction);
	
	public void purchase(long assetId, String userId,Currency currency,
			BigDecimal amount) ;

	public void rating(Long assetId, String userId, Double rating);

	public String retrieveDownloadLink(Long assetId, Long versionId,String userId,
			String deviceSerial) ;
	
	public void comment(Long assetId, String userId, String commentContent);
	
	public AssetLifecycleActionHistory genActionHistoryFromAction(AssetLifecycleAction assetLifecycleAction );
	
}

// $Id$