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
package com.hp.sdf.ngp.custom.sbm.storeclient.model;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.hp.sdf.ngp.model.Asset;

@Entity
@Table
public class PurchaseHistoryExtend implements Serializable{
	private static final long serialVersionUID = -2162222241951116702L;
	
	private String eventId;
	private String orderNo;
	private Long itemId;
	
	private String tempPaidResult;
	private String reqconfirmResult;
	private Date reqconfirmDate;
	private String msisdn;
	private Long assetId;
	private String status;
	private Long paidPrice;
	private String userId;
	private String assetExternalId;
	private Date tempPaidDate;
	private String imsi;
	private String tempPaidDetailCode;
	private String reqConfirmDetailCode;
	private String roinStallStatus;
	private String version;
	// Constructors

	/** default constructor */
	public PurchaseHistoryExtend() {
		try {
			reqconfirmDate=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse("9999-12-31 23:59:59");
		} catch (Exception e) {
			// ignore
		}
	}

	
	@Id
	@Column
	public String getEventId() {
		return eventId;
	}

	public void setEventId(String eventId) {
		this.eventId = eventId;
	}

	@Column
	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	@Column
	public Long getItemId() {
		return itemId;
	}

	public void setItemId(Long itemId) {
		this.itemId = itemId;
	}

	@Column
	public String getTempPaidResult() {
		return tempPaidResult;
	}

	public void setTempPaidResult(String tempPaidResult) {
		this.tempPaidResult = tempPaidResult;
	}

	@Column
	public String getReqconfirmResult() {
		return reqconfirmResult;
	}

	public void setReqconfirmResult(String reqconfirmResult) {
		this.reqconfirmResult = reqconfirmResult;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column
	public Date getReqconfirmDate() {
		return reqconfirmDate;
	}

	public void setReqconfirmDate(Date reqconfirmDate) {
		this.reqconfirmDate = reqconfirmDate;
	}
	
	@Column
	public String getMsisdn() {
		return msisdn;
	}

	public void setMsisdn(String msisdn) {
		this.msisdn = msisdn;
	}

	@Column
	public Long getAssetId() {
		return assetId;
	}

	public void setAssetId(Long assetId) {
		this.assetId = assetId;
	}
	
	protected Asset asset;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "assetId", referencedColumnName = "id", insertable = false, updatable = false)
	protected Asset getAsset() {
		return asset;
	}

	protected void setAsset(Asset asset) {
		this.asset = asset;
	}

	@Column
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Column
	public Long getPaidPrice() {
		return paidPrice;
	}

	public void setPaidPrice(Long paidPrice) {
		this.paidPrice = paidPrice;
	}

	
	@Column
	public String getUserId() {
		return userId;
	}


	public void setUserId(String userId) {
		this.userId = userId;
	}


	@Column
	public String getAssetExternalId() {
		return assetExternalId;
	}


	public void setAssetExternalId(String assetExternalId) {
		this.assetExternalId = assetExternalId;
	}


	@Temporal(TemporalType.TIMESTAMP)
	@Column
	public Date getTempPaidDate() {
		return tempPaidDate;
	}


	public void setTempPaidDate(Date tempPaidDate) {
		this.tempPaidDate = tempPaidDate;
	}


	@Column
	public String getImsi() {
		return imsi;
	}


	public void setImsi(String imsi) {
		this.imsi = imsi;
	}

	@Column
	public String getTempPaidDetailCode() {
		return tempPaidDetailCode;
	}


	public void setTempPaidDetailCode(String tempPaidDetailCode) {
		this.tempPaidDetailCode = tempPaidDetailCode;
	}

	@Column
	public String getReqConfirmDetailCode() {
		return reqConfirmDetailCode;
	}


	public void setReqConfirmDetailCode(String reqConfirmDetailCode) {
		this.reqConfirmDetailCode = reqConfirmDetailCode;
	}

	@Column
	public String getRoinStallStatus() {
		return roinStallStatus;
	}


	public void setRoinStallStatus(String roinStallStatus) {
		this.roinStallStatus = roinStallStatus;
	}


	@Column
	public String getVersion() {
		return version;
	}


	public void setVersion(String version) {
		this.version = version;
	}
	
	

	
}

// $Id$