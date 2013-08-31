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
package com.hp.sdf.ngp.ui.common;

import java.io.Serializable;

public class ContentManagementCondition implements Serializable {

	private static final long serialVersionUID = 2928021430248378181L;

	private String providerExternalId;
	private String providerName;
	private Long parentAssetId;
	private String assetExternalId;
	private String binaryVersionExternalId;
	private Long assetId;
	private String assetName;
	private Double commissionRate;
	private String category1;
	private String category2;
	private Long statusId;
	private String externalId;
	private Boolean recommended = false;
	private Boolean newArrival = false;

	private Boolean first = true;

	public String getAssetExternalId() {
		return assetExternalId;
	}

	public void setAssetExternalId(String assetExternalId) {
		this.assetExternalId = assetExternalId;
	}

	public String getBinaryVersionExternalId() {
		return binaryVersionExternalId;
	}

	public void setBinaryVersionExternalId(String binaryVersionExternalId) {
		this.binaryVersionExternalId = binaryVersionExternalId;
	}

	public ContentManagementCondition() {
	}

	public ContentManagementCondition(Boolean first) {
		this.first = first;
	}

	public String getProviderExternalId() {
		return providerExternalId;
	}

	public void setProviderExternalId(String providerExternalId) {
		this.providerExternalId = providerExternalId;
	}

	public String getProviderName() {
		return providerName;
	}

	public void setProviderName(String providerName) {
		this.providerName = providerName;
	}

	public Long getParentAssetId() {
		return parentAssetId;
	}

	public void setParentAssetId(Long parentAssetId) {
		this.parentAssetId = parentAssetId;
	}

	public Long getAssetId() {
		return assetId;
	}

	public void setAssetId(Long assetId) {
		this.assetId = assetId;
	}

	public String getAssetName() {
		return assetName;
	}

	public void setAssetName(String assetName) {
		this.assetName = assetName;
	}

	public Double getCommissionRate() {
		return commissionRate;
	}

	public void setCommissionRate(Double commissionRate) {
		this.commissionRate = commissionRate;
	}

	public String getCategory1() {
		return category1;
	}

	public void setCategory1(String category1) {
		this.category1 = category1;
	}

	public String getCategory2() {
		return category2;
	}

	public void setCategory2(String category2) {
		this.category2 = category2;
	}

	public Long getStatusId() {
		return statusId;
	}

	public void setStatusId(Long statusId) {
		this.statusId = statusId;
	}

	public String getExternalId() {
		return externalId;
	}

	public void setExternalId(String externalId) {
		this.externalId = externalId;
	}

	public Boolean getRecommended() {
		return recommended;
	}

	public void setRecommended(Boolean recommended) {
		this.recommended = recommended;
	}

	public Boolean getNewArrival() {
		return newArrival;
	}

	public void setNewArrival(Boolean newArrival) {
		this.newArrival = newArrival;
	}

	public Boolean getFirst() {
		return first;
	}

	public void setFirst(Boolean first) {
		this.first = first;
	}
}