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

import java.math.BigDecimal;
import java.util.Date;

public interface ContentItem {
	
	public long getId();
	public void setId(long Id);
	
	public long getAssetId();
	public void setAssetId(long assetId);
	public String getAssetExternalId();
	public void setAssetExternalId(String assetExternalId);
	
	public long getVersionId();
	public void setVersionId(long versionId);
	
	public String getItemId();
	public void setItemId(String itemId);
	
//	public String getRightsIssuerUri();
//	public void setRightsIssuerUri(String rightsIssuerUri);
	
	public String getLid();
	public void setLid(String lid);
	public String getItemNumber();
	public void setItemNumber(String itemNumber);
	public String getItemName();
	public void setItemName(String itemName);
	public BigDecimal getItemPrice();
	public void setItemPrice(BigDecimal itemPrice);
	public String getDisplayText();
	public void setDisplayText(String displayText);
	public Date getStartTime();
	public void setStartTime(Date setStartTime);
	public Date getEndTime();
	public void setEndTime(Date endTime);
	public long getCount();
	public void setCount(long count);
	public long getIntervalDays();
	public void setIntervalDays(long inverval);
	public String getOperatorRefId();
	public void setOperatorRefId(String operatorRefId);
	public String getCurrency();
	public void setCurrency(String currency);
	public String getPriceId();
	public void setPriceId(String priceId);
	public String getPriceType();
	public void setPriceType(String priceType);
	public String getTrialFlag();
	public void setTrialFlag(String trialFlag);
	public long getTrialPeriodInDays();
	public void setTrialPeriodInDays(long trialPeriodInDays);
	public String getSynchronizeFlag();
	public void setSynchronizeFlag(String synchronizeFlag);
	public String getVersion();
	public void setVersion(String version);
	
}

// $Id$