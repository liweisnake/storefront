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

import com.hp.sdf.ngp.model.ParentAssetVersionSummary;

public class ParentAssetVersionSummaryImpl implements com.hp.sdf.ngp.custom.sbm.api.storeclient.model.ParentAssetVersionSummary,Serializable {

	public ParentAssetVersionSummary getParentAssetVersionSummary() {
		if(null == parentAssetVersionSummary){
			parentAssetVersionSummary = new ParentAssetVersionSummary();
		}
		return parentAssetVersionSummary;
	}

//	private final static Log log = LogFactory.getLog(ParentAssetVersionSummaryImpl.class);
	
	private static final long serialVersionUID = -7768556424684068784L;
	
	private ParentAssetVersionSummary parentAssetVersionSummary;
	
	public ParentAssetVersionSummaryImpl(){
		parentAssetVersionSummary = new ParentAssetVersionSummary();
	}
	
	public ParentAssetVersionSummaryImpl(ParentAssetVersionSummary parentAssetVersionSummary){
		this.parentAssetVersionSummary = parentAssetVersionSummary;
		
		if (null != this.parentAssetVersionSummary) {
			this.parentAssetVersionSummary.getLowestPrice();//load information to avoid the lazy load
		}
	}

	public long getDownloadTime() {
		Long downloadTime = parentAssetVersionSummary.getDownloadTime();
		if(null != downloadTime){
			return downloadTime;
		}
		throw new RuntimeException();
	}

	public BigDecimal getLowestPrice() {
		Double lowerPrice = parentAssetVersionSummary.getLowestPrice();
		if(null != lowerPrice){
			return new BigDecimal(lowerPrice);
		}
		return null;
	}

	public int getNewArrivalFlag() {
		Long newArrivalFlag = parentAssetVersionSummary.getNewArrivalFlag();
		if(null != newArrivalFlag){
			return newArrivalFlag.intValue();
		}
		throw new RuntimeException();
	}

	public long getParentAssetId() {
		Long parentAssetId = parentAssetVersionSummary.getParentAssetId();
		if(null != parentAssetId){
			return  parentAssetId;
		}
		throw new RuntimeException();
	}

	public int getPriceEqualFlag() {
		Long priceEqualFlag = parentAssetVersionSummary.getPriceEqualFlg();
		if(null != priceEqualFlag){
			return priceEqualFlag.intValue();
		}
		throw new RuntimeException();
	}

	public Date getPublishDate() {
		return parentAssetVersionSummary.getPublishDate();
	}

	public String getPublishFlag() {
		return parentAssetVersionSummary.getPublishFlag();
	}

	public int getRecommendFlag() {
		Long RelcommendFlag = parentAssetVersionSummary.getRecommendFlag();
		if(null != RelcommendFlag){
			return RelcommendFlag.intValue();
		}
		throw new RuntimeException();
	}

	public int getRecommendOrder() {
		Long recommendOrder = parentAssetVersionSummary.getRecommendOrder();
		if(null != recommendOrder){
			return recommendOrder.intValue();
		}
		throw new RuntimeException();
	}

	public Date getSaleEndDate() {
		return parentAssetVersionSummary.getSaleEnd();
	}

	public int getSaleFlag() {
		Long saleFlag = parentAssetVersionSummary.getSaleFlag();
		if(null != saleFlag){
			return saleFlag.intValue();
		}
		throw new RuntimeException();
	}

	public Date getSaleStartDate() {
		return parentAssetVersionSummary.getSaleStart();
	}

	public int getSampleFlag() {
		Long sampleFlag = parentAssetVersionSummary.getSampleFlag();
		if(null != sampleFlag){
			return sampleFlag.intValue();
		}
		throw new RuntimeException();
	}

	public void setDownloadTime(long downloadTime) {
//		log.debug("downloadTime:"+downloadTime);
		parentAssetVersionSummary.setDownloadTime(downloadTime);
	}

	public void setLowestPrice(BigDecimal lowestPrice) {
//		log.debug("lowestPrice:"+lowestPrice);
		if(null != lowestPrice)
			parentAssetVersionSummary.setLowestPrice(lowestPrice.doubleValue());
	}

	public void setNewArrivalFlag(int newArrivalFlag) {
//		log.debug("newArrivalFlag:"+newArrivalFlag);
		parentAssetVersionSummary.setNewArrivalFlag(new Long(newArrivalFlag));
	}

	public void setParentAssetId(long parentAssetId) {
//		log.debug("parentAssetId:"+parentAssetId);
		parentAssetVersionSummary.setParentAssetId(parentAssetId);
	}

	public void setPriceEqualFlag(int priceEqualFlag) {
//		log.debug("priceEqualFlag:"+priceEqualFlag);
		parentAssetVersionSummary.setPriceEqualFlg(new Long(priceEqualFlag));
	}

	public void setPublishDate(Date publishDate) {
//		log.debug("publishDate:"+publishDate);
		parentAssetVersionSummary.setPublishDate(publishDate);
	}

	public void setPublishFlag(String publishFlag) {
//		log.debug("publishFlag:"+publishFlag);
		parentAssetVersionSummary.setPublishFlag(publishFlag);
	}

	public void setRecommendFlag(int recommendFlag) {
//		log.debug("recommendFlag:"+recommendFlag);
		parentAssetVersionSummary.setRecommendFlag(new Long(recommendFlag));
	}

	public void setRecommendOrder(int recommendOrder) {
//		log.debug("recommendOrder:"+recommendOrder);
		parentAssetVersionSummary.setRecommendOrder(new Long(recommendOrder));
	}

	public void setSaleEndDate(Date saleEndDate) {
//		log.debug("saleEndDate:"+saleEndDate);
		parentAssetVersionSummary.setSaleEnd(saleEndDate);
	}

	public void setSaleFlag(int saleFlag) {
//		log.debug("saleFlag:"+saleFlag);
		parentAssetVersionSummary.setSaleFlag(new Long(saleFlag));
		
	}

	public void setSaleStartDate(Date saleStartDate) {
//		log.debug("saleStartDate:"+saleStartDate);
		parentAssetVersionSummary.setSaleStart(saleStartDate);
	}

	public void setSampleFlag(int sampleFlag) {
//		log.debug("sampleFlag:"+sampleFlag);
		parentAssetVersionSummary.setSampleFlag(new Long(sampleFlag));
	}

	public long getId() {
		Long id = parentAssetVersionSummary.getId();
		if(null != id){
			return id;
		}
		throw new RuntimeException();
	}

	public void setId(long Id) {
//		log.debug("id:"+Id);
		parentAssetVersionSummary.setId(Id);
	}

	@Override
	public String toString(){
		return "ParentAssetVersionSummary[downloadTime="+getDownloadTime()+",lowestPrice="+getLowestPrice()+",newArrivalFlag="+getNewArrivalFlag()
		+",parentAssetId="+getParentAssetId()+",priceEqualFlag="+getPriceEqualFlag()+",publishDate="+getPublishDate()+",publishFlag="+getPublishFlag()
		+",recommendFlag="+getRecommendFlag()+",recommendOrder="+getRecommendOrder()+",saleEndDate="+getSaleEndDate()+",saleFlag="+getSaleFlag()
		+",saleStartDate="+getSaleStartDate()+",sampleFlag="+getSampleFlag()+",id="+getId()+"]";
	}
}

// $Id$