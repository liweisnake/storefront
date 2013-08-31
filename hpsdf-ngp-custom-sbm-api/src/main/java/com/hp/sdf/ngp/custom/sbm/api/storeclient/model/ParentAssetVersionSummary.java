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

public interface ParentAssetVersionSummary {

	public long getId();
	public void setId(long Id);
	public long getParentAssetId();
	public void setParentAssetId(long parentAssetId);
	public String getPublishFlag();
	public void setPublishFlag(String publishFlag);
	public int getNewArrivalFlag();
	public void setNewArrivalFlag(int newArrivalFlag);
	public int getRecommendFlag();
	public void setRecommendFlag(int recommendFlag);
	public int getSaleFlag();
	public void setSaleFlag(int saleFlag);
	public long getDownloadTime();
	public void setDownloadTime(long downloadTime);
	public Date getPublishDate();
	public void setPublishDate(Date publishDate);
	public Date getSaleStartDate();
	public void setSaleStartDate(Date saleStartDate);
	public Date getSaleEndDate();
	public void setSaleEndDate(Date saleEndDate);
	public BigDecimal getLowestPrice();
	public void setLowestPrice(BigDecimal lowestPrice);
	public int getPriceEqualFlag();
	public void setPriceEqualFlag(int priceEqualFlag);
	public int getRecommendOrder();
	public void setRecommendOrder(int recommendOrder);
	public int getSampleFlag();
	public void setSampleFlag(int sampleFlag);
}

// $Id$