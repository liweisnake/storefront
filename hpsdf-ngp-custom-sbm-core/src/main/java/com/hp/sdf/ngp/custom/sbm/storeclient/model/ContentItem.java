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

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table
public class ContentItem  implements java.io.Serializable {

	private static final long serialVersionUID = -1162221189951116711L;
	private Long id;
	private String itemId;
	private Long assetId;
	
	private Long versionId;
	private String version;
	private String lid;
	private Long itemNumber;
	private String itemName;
	private Long itemPrice;
	private String displayText;
	private Date startTime;
	private Date endTime;
	private Long count;
	private Long intervalDays;
	private String operatorRefId;
	private String currency;
	private String priceType;
	private Long trialFlag;
	private String assetExternalId;
	private String synchronizeFlag;
	private String priceId;
	private Long trialPeriodInDays;
	
	
	// Constructors

	/** default constructor */
	public ContentItem() {
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	@Column
	public String getItemId() {
		return itemId;
	}

	public void setItemId(String itemId) {
		this.itemId = itemId;
	}

	@Column
	public Long getAssetId() {
		return assetId;
	}

	public void setAssetId(Long assetId) {
		this.assetId = assetId;
	}

	@Column
	public Long getVersionId() {
		return versionId;
	}


	public void setVersionId(Long versionId) {
		this.versionId = versionId;
	}

	@Column
	public String getLid() {
		return lid;
	}


	public void setLid(String lid) {
		this.lid = lid;
	}

	@Column
	public Long getItemNumber() {
		return itemNumber;
	}


	public void setItemNumber(Long itemNumber) {
		this.itemNumber = itemNumber;
	}

	@Column
	public String getItemName() {
		return itemName;
	}


	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	@Column
	public Long getItemPrice() {
		return itemPrice;
	}


	public void setItemPrice(Long itemPrice) {
		this.itemPrice = itemPrice;
	}

	@Column
	public String getDisplayText() {
		return displayText;
	}


	public void setDisplayText(String displayText) {
		this.displayText = displayText;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column
	public Date getStartTime() {
		return startTime;
	}


	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column
	public Date getEndTime() {
		return endTime;
	}


	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	@Column(name="itemCount")
	public Long getCount() {
		return count;
	}


	public void setCount(Long count) {
		this.count = count;
	}
	
	
	@Column
	public Long getIntervalDays() {
		return intervalDays;
	}

	public void setIntervalDays(Long intervalDays) {
		this.intervalDays = intervalDays;
	}

	@Column
	public String getOperatorRefId() {
		return operatorRefId;
	}


	public void setOperatorRefId(String operatorRefId) {
		this.operatorRefId = operatorRefId;
	}

	@Column
	public String getCurrency() {
		return currency;
	}


	public void setCurrency(String currency) {
		this.currency = currency;
	}

	@Column
	public String getPriceType() {
		return priceType;
	}


	public void setPriceType(String priceType) {
		this.priceType = priceType;
	}

	@Column
	public Long getTrialFlag() {
		return trialFlag;
	}


	public void setTrialFlag(Long trailFlag) {
		this.trialFlag = trailFlag;
	}

	@Column
	public String getAssetExternalId() {
		return assetExternalId;
	}

	public void setAssetExternalId(String assetExternalId) {
		this.assetExternalId = assetExternalId;
	}

	@Column
	public String getSynchronizeFlag() {
		return synchronizeFlag;
	}

	public void setSynchronizeFlag(String synchronizeFlag) {
		this.synchronizeFlag = synchronizeFlag;
	}

	@Column
	public String getPriceId() {
		return priceId;
	}

	public void setPriceId(String priceId) {
		this.priceId = priceId;
	}

	
	@Column
	public Long getTrialPeriodInDays() {
		return trialPeriodInDays;
	}

	public void setTrialPeriodInDays(Long trialPeriodInDays) {
		this.trialPeriodInDays = trialPeriodInDays;
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