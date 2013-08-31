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

import com.hp.sdf.ngp.custom.sbm.api.storeclient.model.ContentItem;

public class ContentItemImpl implements ContentItem, Serializable {

	private static final long serialVersionUID = 5874922337502444596L;
//	private final static Log log = LogFactory.getLog(ContentItemImpl.class);

	public com.hp.sdf.ngp.custom.sbm.storeclient.model.ContentItem getContentItem() {
		if(null == contentItem){
			contentItem = new com.hp.sdf.ngp.custom.sbm.storeclient.model.ContentItem();
		}
		return contentItem;
	}

	private com.hp.sdf.ngp.custom.sbm.storeclient.model.ContentItem contentItem;

	public ContentItemImpl() {
		contentItem = new com.hp.sdf.ngp.custom.sbm.storeclient.model.ContentItem();
	}

	public ContentItemImpl(
			com.hp.sdf.ngp.custom.sbm.storeclient.model.ContentItem contentItem) {
		this.contentItem = contentItem;
		if (null != this.contentItem) {
			this.contentItem.getCount();//load information to avoid the lazy load
		}
	}

	public long getAssetId() {
		Long assetId = contentItem.getAssetId();
		if (null != assetId) {
			return assetId;
		}
		throw new RuntimeException();
	}

	public long getCount() {
		Long count = contentItem.getCount();
		if (null != count) {
			return count;
		}
		return 0;
//		throw new RuntimeException();
	}

	public String getDisplayText() {
		return contentItem.getDisplayText();
	}

	public Date getEndTime() {
		return contentItem.getEndTime();
	}

	public long getId() {
		Long id = contentItem.getId();
		if (null != id) {
			return id;
		}
		return 0;
//		throw new RuntimeException();
	}

	public long getIntervalDays() {
		Long interval = contentItem.getIntervalDays();
		if (null != interval) {
			return interval;
		}
		return 0;
//		throw new RuntimeException();
	}

	public String getItemId() {
		return contentItem.getItemId();
	}

	public String getItemName() {
		return contentItem.getItemName();
	}

	public String getItemNumber() {
		Long itemNumber = contentItem.getItemNumber();
		if (null != itemNumber) {
			return String.valueOf(itemNumber);
		}
		return null;
	}

	public BigDecimal getItemPricePoints() {
		Long itemPrice = contentItem.getItemPrice();
		if (null != itemPrice) {
			return new BigDecimal(itemPrice);
		}
		return null;
	}

	public String getLid() {
		return contentItem.getLid();
	}

//	public String getRightsIssuerUri() {
//		return contentItem.getRightsissuerURI();
//	}

	public Date getStartTime() {
		return contentItem.getStartTime();
	}

	public void setAssetId(long assetId) {
//		log.debug("assetId:" + assetId);
		contentItem.setAssetId(assetId);
	}

	public void setCount(long count) {
//		log.debug("count:" + count);
		contentItem.setCount(count);
	}

	public void setDisplayText(String displayText) {
//		log.debug("displayText:" + displayText);
		contentItem.setDisplayText(displayText);
	}

	public void setEndTime(Date endTime) {
//		log.debug("endTime:" + endTime);
		contentItem.setEndTime(endTime);
	}

	public void setId(long Id) {
//		log.debug("Id:" + Id);
		contentItem.setId(Id);
	}

	public void setIntervalDays(long inverval) {
//		log.debug("interval:" + inverval);
		contentItem.setIntervalDays(inverval);
	}

	public void setItemId(String itemId) {
//		log.debug("itemId:" + itemId);
		contentItem.setItemId(itemId);
	}

	public void setItemName(String itemName) {
//		log.debug("itemName:" + itemName);
		contentItem.setItemName(itemName);
	}

	public void setItemNumber(String itemNumber) {
//		log.debug("itemNumber:" + itemNumber);
		if (null != itemNumber)
			contentItem.setItemNumber(Long.parseLong(itemNumber));
	}

	public void setItemPricePoints(BigDecimal itemPricePoints) {
		if (null != itemPricePoints)
			contentItem.setItemPrice(itemPricePoints.longValue());
	}

	public void setLid(String lid) {
//		log.debug("lid:" + lid);
		contentItem.setLid(lid);
	}

//	public void setRightsIssuerUri(String rightsIssuerUri) {
//		log.debug("rightsIssuerUri:" + rightsIssuerUri);
//		contentItem.setRightsissuerURI(rightsIssuerUri);
//	}

	public void setStartTime(Date setStartTime) {
//		log.debug("setStartTime:" + setStartTime);
		contentItem.setStartTime(setStartTime);
	}

	public String getAssetExternalId() {
		return contentItem.getAssetExternalId();
	}

	public String getCurrency() {
		return contentItem.getCurrency();
	}

	public BigDecimal getItemPrice() {
		Long price = contentItem.getItemPrice();
		if (null != price) {
			return new BigDecimal(price);
		}
		return null;
	}

	public String getOperatorRefId() {
		return contentItem.getOperatorRefId();
	}

	public String getPriceType() {
		return contentItem.getPriceType();
	}

	// public int getSynchronizeFlag() {
	// Long flag = contentItem.getSynchronizeFlag();
	// if(null != flag){
	// flag.intValue();
	// }
	// throw new RuntimeException();
	// }
	//
	// public boolean getTrialFlag() {
	// Long flag = contentItem.getTrailFlag();
	// if(null != flag && flag > 0){
	// return true;
	// }
	// return false;
	// }

	public long getVersionId() {
		Long versionId = contentItem.getVersionId();
		if (null != versionId) {
			return versionId;
		}
		return 0;
//		throw new RuntimeException();
	}

	public void setAssetExternalId(String assetExternalId) {
//		log.debug("assetExternalId:" + assetExternalId);
		contentItem.setAssetExternalId(assetExternalId);
	}

	public void setCurrency(String currency) {
//		log.debug("currency:" + currency);
		contentItem.setCurrency(currency);
	}

	public void setItemPrice(BigDecimal itemPrice) {
//		log.debug("itemPrice:" + itemPrice);
		if (null != itemPrice)
			contentItem.setItemPrice(itemPrice.longValue());
	}

	public void setOperatorRefId(String operatorRefId) {
//		log.debug("operatorRefId:" + operatorRefId);
		contentItem.setOperatorRefId(operatorRefId);
	}

	public void setPriceType(String priceType) {
//		log.debug("priceType:" + priceType);
		contentItem.setPriceType(priceType);
	}

	public void setSynchronizeFlag(boolean synchronizeFlag) {
		if (synchronizeFlag)
			contentItem.setSynchronizeFlag("1");
		else
			contentItem.setSynchronizeFlag("0");
	}

	public void setTrialFlag(boolean trialFlag) {
//		log.debug("trialFlag:" + trialFlag);
		if (trialFlag)
			contentItem.setTrialFlag(1L);
		else
			contentItem.setTrialFlag(0L);
	}

	public void setVersionId(long versionId) {
//		log.debug("versionId:" + versionId);
		contentItem.setVersionId(versionId);
	}

//	public void setSynchronizeFlag(int synchronizeFlag) {
//		log.debug("synchronizeFlag:" + synchronizeFlag);
//		contentItem.setSynchronizeFlag(String.valueOf(synchronizeFlag));
//	}

	public String getSynchronizeFlag() {
		return contentItem.getSynchronizeFlag();
	}

	public String getTrialFlag() {
		Long flag = contentItem.getTrialFlag();
		if (null != flag) {
			return String.valueOf(flag);
		}
		return null;
	}

	public void setSynchronizeFlag(String synchronizeFlag) {
//		log.debug("synchronizeFlag:" + synchronizeFlag);
		contentItem.setSynchronizeFlag(synchronizeFlag);

	}

	public void setTrialFlag(String trialFlag) {
//		log.debug("trialFlag:" + trialFlag);
		if (null != trialFlag)
			contentItem.setTrialFlag(Long.parseLong(trialFlag));
	}

	public String getPriceId() {
		return contentItem.getPriceId();
	}

	public long getTrialPeriodInDays() {
		Long days = contentItem.getTrialPeriodInDays();
		if(null != days){
			return days;
		}
		return 0;
//		throw new RuntimeException();
	}

	public void setPriceId(String priceId) {
//		log.debug("priceId="+priceId);
		contentItem.setPriceId(priceId);
	}

	public void setTrialPeriodInDays(long trialPeriodInDays) {
//		log.debug("trialPeriodInDays="+trialPeriodInDays);
		contentItem.setTrialPeriodInDays(trialPeriodInDays);
	}

	@Override
	public String toString(){
		return "ContentItem[assetId="+getAssetId()+",count="+getCount()+",displayText="+getDisplayText()+",endTime="+getEndTime()+"id="+getId()
		+",interval="+getIntervalDays()+",itemId="+getItemId()+",itemName="+getItemName()+",itemNumber="+getItemNumber()+",lid="+getLid()
		+",startTime="+getStartTime()+",assetExternalId="+getAssetExternalId()+",currency="+getCurrency()+",itemPrice="+ getItemPrice()
		+",operatorRefId="+getOperatorRefId()+",priceType="+getPriceType()+",trialFlag="+getTrialFlag()+",versionId="+getVersionId()
		+",synchronizeFlag ="+getSynchronizeFlag()+",trialFlag="+getTrialFlag()+",priceId="+getPriceId()+",trialPeriodInDays="+getTrialPeriodInDays()+"]";
	}

	public String getVersion() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setVersion(String version) {
		// TODO Auto-generated method stub
		
	}
}

// $Id$