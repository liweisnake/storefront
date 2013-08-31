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
package com.hp.sdf.ngp.custom.sbm.api.storeclient.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Represents the purchase history of both asset and asset-item.
 * 
 *
 */
public interface PurchaseHistoryExtend extends Serializable{

	/** key */
	public String getEventId();
	public void setEventId(String eventId);
	public String getOrderNo();
	public void setOrderNo(String orderNo);
	public String getItemId();
	public void setItemId(String itemId);
	public String getTempPaidResult();
	public void setTempPaidResult(String tempPaidResult);
	public String getReqConfirmResult();
	public void setReqConfirmResult(String reqConfirmResult);
	public Date getReqConfirmDate();
	public void setReqConfirmDate(Date reqConfirmDate);
	public String getMsisdn();
	public void setMsisdn(String msisdn);
	public long getAssetId();
	public void setAssetId(long assetId);
	public String getStatus();
	public void setStatus(String status);
	public BigDecimal getPaidPrice();
	public void setPaidPrice(BigDecimal setPaidPrice);
	public String getUserId();
	public void setUserId(String userId);
	public String getAssetExternalId();
	public void setAssetExternalId(String assetExternalId);
	public Date getTempPaidDate();
	public void setTempPaidDate(Date tempPaidDate);
	
	public String getImsi();
	public void setImsi(String imsi);
	
	public String getTempPaidDetailCode();
	public void setTempPaidDetailCode(String tempPaidDetailCode);
	
	public String getReqConfirmDetailCode();
	public void setReqConfirmDetailCode(String reqConfirmDetailCode);

	public String getRoInstallStatus();
	public void setRoInstallStatus(String roInstallStatus);
	
	public String getVersion();
	public void setVersion(String version);
}

// $Id$