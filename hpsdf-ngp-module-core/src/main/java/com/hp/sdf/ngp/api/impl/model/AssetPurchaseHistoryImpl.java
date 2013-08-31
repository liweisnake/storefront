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
package com.hp.sdf.ngp.api.impl.model;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.hp.sdf.ngp.api.model.AssetPurchaseHistory;
import com.hp.sdf.ngp.model.Asset;
import com.hp.sdf.ngp.model.PurchaseHistory;

public class AssetPurchaseHistoryImpl implements AssetPurchaseHistory {
	
	public PurchaseHistory getPurchaseHistory() {
		if(null == purchaseHistory){
			purchaseHistory = new PurchaseHistory();
		}
		return purchaseHistory;
	}

	public void setAsset(Asset asset) {
		this.asset = asset;
	}

	private PurchaseHistory purchaseHistory;
	
	private Asset asset;
	
	public AssetPurchaseHistoryImpl(){
		purchaseHistory = new PurchaseHistory();
	}
	
	public AssetPurchaseHistoryImpl(PurchaseHistory purchaseHistory){
		this.purchaseHistory = purchaseHistory;
		
		if (null != this.purchaseHistory) {
			this.purchaseHistory.getPaidPrice();//load information to avoid the lazy load
		}
	}

	public Long getAssetId() {
			return asset.getId();
	}

	public Long getBinaryVersionId() {
		if(null != asset){
			return asset.getCurrentVersionId();
		}
		throw new RuntimeException();
	}

	public String getCompleteDate() {
		Date date = purchaseHistory.getPurchaseDate();
		if(null != date){
			new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
		}
		return null;
	}

	public Long getId() {
		Long id = purchaseHistory.getId();
		return id;
	}

	public BigDecimal getPaidPrice() {
		return purchaseHistory.getPaidPrice();
	}

	public String getStatus() {
		return purchaseHistory.getStatus();
	}

	public String getTransactionid() {
		return purchaseHistory.getTransactionid();
	}

	public String getUserid() {
		return purchaseHistory.getUserid();
	}

}

// $Id$