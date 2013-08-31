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
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
@Entity
@Table
public class SBMAssetPrice  implements Serializable{
	private static final long serialVersionUID = -8263943091766918919L;
	
	
	private Long id;
	private Long assetId;
	private Long versionId;
	private Long itemId;
	private BigDecimal amount;
	private String currency;
	private String type;
	private Long trialFlag;
	private Long trialPeriodInDays;
	private String operatorRefId;
	private String billingComment;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
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
	public Long getItemId() {
		return itemId;
	}
	
	public void setItemId(Long itemId) {
		this.itemId = itemId;
	}
	
	@Column
	public BigDecimal getAmount() {
		return amount;
	}
	
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	
	@Column
	public String getCurrency() {
		return currency;
	}
	
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	
	@Column
	public String getType() {
		return type;
	}
	
	
	public void setType(String type) {
		this.type = type;
	}
	
	@Column
	public Long getTrialFlag() {
		return trialFlag;
	}
	
	public void setTrialFlag(Long trialFlag) {
		this.trialFlag = trialFlag;
	}
	
	@Column
	public Long getTrialPeriodInDays() {
		return trialPeriodInDays;
	}
	
	public void setTrialPeriodInDays(Long trialPeriodInDays) {
		this.trialPeriodInDays = trialPeriodInDays;
	}
	
	@Column
	public String getOperatorRefId() {
		return operatorRefId;
	}
	
	public void setOperatorRefId(String operatorRefId) {
		this.operatorRefId = operatorRefId;
	}
	
	@Column
	public String getBillingComment() {
		return billingComment;
	}
	
	public void setBillingComment(String billingComment) {
		this.billingComment = billingComment;
	}
	
	
	
	
}

// $Id$