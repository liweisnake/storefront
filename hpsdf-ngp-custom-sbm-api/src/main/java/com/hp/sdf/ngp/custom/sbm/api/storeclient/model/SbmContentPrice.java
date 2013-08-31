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

public interface SbmContentPrice {

	public long getId();
	public void setId(long id);
	
	public long getAssetId();
	public void setAssetId(long assetId);
	
	public long getVersionId();
	public void setVersionId(long setVersionId);
	
	public long getItemId();
	public void setItemId(long itemId);
	
	public BigDecimal getAmount();
	public void setAmount(BigDecimal amount);
	
	public String getCurrency();
	public void setCurrency(String currency);
	
	public String getType();
	public void setType(String type);
	
	public int getTrialFlag();
	public void setTrialFlag(int trialFlag);
	
	public long getTrialPeriodInDays();
	public void setTrialPeriodInDays(long trialPeriodInDays);
	
	public String getOperatorRefId();
	public void setOperatorRefId(String operatorRefId);
	
	public String getBillingComment();
	public void setBillingComment(String billingComment);
}

// $Id$