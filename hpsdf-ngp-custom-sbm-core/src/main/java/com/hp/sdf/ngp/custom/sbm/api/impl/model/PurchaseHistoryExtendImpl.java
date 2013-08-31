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
package com.hp.sdf.ngp.custom.sbm.api.impl.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.hp.sdf.ngp.custom.sbm.api.storeclient.model.PurchaseHistoryExtend;

public class PurchaseHistoryExtendImpl implements PurchaseHistoryExtend,
		Serializable {

	private static final long serialVersionUID = -2029586102171994315L;

//	private final static Log log = LogFactory
//			.getLog(PurchaseHistoryExtendImpl.class);

	public com.hp.sdf.ngp.custom.sbm.storeclient.model.PurchaseHistoryExtend getPurchaseHistoryExtend() {
		if(null == purchaseHistoryExtend){
			purchaseHistoryExtend = new com.hp.sdf.ngp.custom.sbm.storeclient.model.PurchaseHistoryExtend();
		}
		return purchaseHistoryExtend;
	}

	private com.hp.sdf.ngp.custom.sbm.storeclient.model.PurchaseHistoryExtend purchaseHistoryExtend;

	public PurchaseHistoryExtendImpl() {
		purchaseHistoryExtend = new com.hp.sdf.ngp.custom.sbm.storeclient.model.PurchaseHistoryExtend();
	}

	public PurchaseHistoryExtendImpl(
			com.hp.sdf.ngp.custom.sbm.storeclient.model.PurchaseHistoryExtend purchaseHistoryExtend) {
		this.purchaseHistoryExtend = purchaseHistoryExtend;
		
		if (null != this.purchaseHistoryExtend) {
			this.purchaseHistoryExtend.getEventId();//load information to avoid the lazy load
		}
	}

	public String getAssetExternalId() {
		return purchaseHistoryExtend.getAssetExternalId();
	}

	public long getAssetId() {
		return purchaseHistoryExtend.getAssetId();
	}

	public String getEventId() {
		return purchaseHistoryExtend.getEventId();
	}

	public String getItemId() {
		Long ItemId = purchaseHistoryExtend.getItemId();
		if (null != ItemId) {
			return String.valueOf(ItemId);
		}
		return null;
	}

	public String getMsisdn() {
		return purchaseHistoryExtend.getMsisdn();
	}

	public String getOrderNo() {
		String orderNo = purchaseHistoryExtend.getOrderNo();
			return orderNo;
	}

	public BigDecimal getPaidPrice() {
		Long paidPrice = purchaseHistoryExtend.getPaidPrice();
		if (null != paidPrice) {
			return new BigDecimal(paidPrice);
		}
		return null;
	}

	public Date getReqConfirmDate() {
		return purchaseHistoryExtend.getReqconfirmDate();
	}

	public String getReqConfirmResult() {
		return purchaseHistoryExtend.getReqconfirmResult();
	}

	public String getStatus() {
		return purchaseHistoryExtend.getStatus();
	}

	public Date getTempPaidDate() {
		return purchaseHistoryExtend.getTempPaidDate();
	}

	public String getTempPaidResult() {
		return purchaseHistoryExtend.getTempPaidResult();
	}

//	public String getUid() {
//		return purchaseHistoryExtend.getUid();
//	}

	public void setAssetExternalId(String assetExternalId) {
//		log.debug("assetExternalId:" + assetExternalId);
		purchaseHistoryExtend.setAssetExternalId(assetExternalId);
	}

	public void setAssetId(long assetId) {
//		log.debug("assetId:" + assetId);
		purchaseHistoryExtend.setAssetId(assetId);
	}

//	public void setEventId(Long eventId) {
//		log.debug("eventId:" + eventId);
//		purchaseHistoryExtend.setEventId(eventId);
//	}

	public void setItemId(String itemId) {
//		log.debug("itemId:" + itemId);
		if (null != itemId)
			purchaseHistoryExtend.setItemId(Long.parseLong(itemId));
	}

	public void setMsisdn(String msisdn) {
//		log.debug("msisdn:" + msisdn);
		purchaseHistoryExtend.setMsisdn(msisdn);
	}

	public void setOrderNo(String orderNo) {
//		log.debug("orderNo:" + orderNo);
		if (null != orderNo)
			purchaseHistoryExtend.setOrderNo(orderNo);
	}

	public void setPaidPrice(BigDecimal setPaidPrice) {
//		log.debug("setPaidPrice:" + setPaidPrice);
		if (null != setPaidPrice)
			purchaseHistoryExtend.setPaidPrice(setPaidPrice.longValue());
	}

	public void setReqConfirmDate(Date reqConfirmDate) {
//		log.debug("reqConfirmDate:" + reqConfirmDate);
		purchaseHistoryExtend.setReqconfirmDate(reqConfirmDate);
	}

	public void setReqConfirmResult(String reqConfirmResult) {
//		log.debug("reqConfirmResult:" + reqConfirmResult);
		purchaseHistoryExtend.setReqconfirmResult(reqConfirmResult);
	}

	public void setStatus(String status) {
//		log.debug("status:" + status);
		purchaseHistoryExtend.setStatus(status);
	}

	public void setTempPaidDate(Date tempPaidDate) {
//		log.debug("tempPaidDate:" + tempPaidDate);
		purchaseHistoryExtend.setTempPaidDate(tempPaidDate);
	}

	public void setTempPaidResult(String tempPaidResult) {
//		log.debug("tempPaidResult:" + tempPaidResult);
		purchaseHistoryExtend.setTempPaidResult(tempPaidResult);
	}

//	public void setUid(String uid) {
//		log.debug("uid:" + uid);
//		purchaseHistoryExtend.setUid(uid);
//	}

	public String getUserId() {
		return purchaseHistoryExtend.getUserId();
	}

	public void setUserId(String userId) {
//		log.debug("userId:" + userId);
		purchaseHistoryExtend.setUserId(userId);
	}

	public String getImsi() {
		return purchaseHistoryExtend.getImsi();
	}

	public String getReqConfirmDetailCode() {
		return purchaseHistoryExtend.getReqConfirmDetailCode();
	}

	public String getRoInstallStatus() {
		return purchaseHistoryExtend.getRoinStallStatus();
	}

	public String getTempPaidDetailCode() {
		return purchaseHistoryExtend.getTempPaidDetailCode();
	}

	public void setImsi(String imsi) {
//		log.debug("imsi="+imsi);
		purchaseHistoryExtend.setImsi(imsi);
	}

	public void setReqConfirmDetailCode(String reqConfirmDetailCode) {
//		log.debug("reqConfirmDetailCode="+reqConfirmDetailCode);
		purchaseHistoryExtend.setReqConfirmDetailCode(reqConfirmDetailCode);
	}

	public void setRoInstallStatus(String roInstallStatus) {
//		log.debug("roInstallStatus="+roInstallStatus);
		purchaseHistoryExtend.setRoinStallStatus(roInstallStatus);
	}

	public void setTempPaidDetailCode(String tempPaidDetailCode) {
//		log.debug("tempPaidDetailCode="+tempPaidDetailCode);
		purchaseHistoryExtend.setTempPaidDetailCode(tempPaidDetailCode);
	}

	public void setEventId(String eventId) {
//		log.debug("eventId:"+eventId);
		purchaseHistoryExtend.setEventId(eventId);
	}

	public String getVersion() {
		return purchaseHistoryExtend.getVersion();
	}

	public void setVersion(String version) {
//		log.debug("version:"+version);
		purchaseHistoryExtend.setVersion(version);
		
	}
	
	@Override
	public String toString(){
		return "PurchaseHistoryExtend[assetExternalId="+getAssetExternalId()+",assetId="+getAssetId()+",itemId="+getItemId()+",msisdn="+getMsisdn()
		+",orderNo="+getOrderNo()+",paidPrice="+getPaidPrice()+",reqConfirmDate="+getReqConfirmDate()+",reqConfirmResult="+getReqConfirmResult()+
		",status="+getStatus()+",tempPaidDate="+getTempPaidDate()+",tempPaidResult="+getTempPaidResult()+",userId="+getUserId()+",imsi="+
		getImsi()+",reqConfirmDetailCode="+getReqConfirmDetailCode()+",roInstallStatus="+getRoInstallStatus()+",tempPaidDetailCode="+
		getTempPaidDetailCode()+",roInstallStatus="+getRoInstallStatus()+",tempPaidDetailCode="+getTempPaidDetailCode()+",eventId="+getEventId()
		+",version="+getVersion()+"]";
	}

}

// $Id$