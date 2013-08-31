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
package com.hp.sdf.ngp.model;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


@Entity
@Table
public class ParentAssetVersionSummary implements Serializable {
	private static final long serialVersionUID = -7165224241951116792L;
	private Long id;
	private String publishFlag;
	private Long newArrivalFlag;
	private Long recommendFlag;
	private Long saleFlag;
	private Long downloadTime;
	private Date saleStart;
	private Date saleEnd;

	private Double lowestPrice;
	private Long priceEqualFlg;

	private Long recommendOrder;
	private Long sampleFlag;

	private Date publishDate;
	
	private Long parentAssetId;
	
	public ParentAssetVersionSummary() {
		newArrivalFlag=0L;
		recommendFlag=0L;
		saleFlag=0L;
		downloadTime = 0L;
		priceEqualFlg =1L;
		sampleFlag =0L;
		recommendOrder=new Long(Integer.MAX_VALUE);
		lowestPrice=new Double(Float.MAX_VALUE);
		try {
			publishDate=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse("1000-01-01 00:00:00");
			saleStart=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse("1000-01-01 00:00:00");
			saleEnd=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse("9999-12-31 23:59:59");
		} catch (ParseException e) {
			// ignore
		}
		
	}
	

	@Column
	public Long getParentAssetId() {
		return parentAssetId;
	}

	public void setParentAssetId(Long parentAssetId) {
		this.parentAssetId = parentAssetId;
	}

	private Set<AssetBinaryVersion> binaryVersions = new HashSet<AssetBinaryVersion>(0);


	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column
	public String getPublishFlag() {
		return publishFlag;
	}

	public void setPublishFlag(String publishFlag) {
		this.publishFlag = publishFlag;
	}

	@Column
	public Long getNewArrivalFlag() {
		return newArrivalFlag;
	}

	public void setNewArrivalFlag(Long newArrivalFlag) {
		this.newArrivalFlag = newArrivalFlag;
	}

	@Column
	public Long getRecommendFlag() {
		return recommendFlag;
	}

	public void setRecommendFlag(Long recommendFlag) {
		this.recommendFlag = recommendFlag;
	}

	@Column
	public Long getSaleFlag() {
		return saleFlag;
	}

	public void setSaleFlag(Long saleFlag) {
		this.saleFlag = saleFlag;
	}

	@Column
	public Long getDownloadTime() {
		return downloadTime;
	}

	public void setDownloadTime(Long downloadTime) {
		this.downloadTime = downloadTime;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column
	public Date getSaleStart() {
		return saleStart;
	}

	public void setSaleStart(Date saleStart) {
		this.saleStart = saleStart;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column
	public Date getSaleEnd() {
		return saleEnd;
	}

	public void setSaleEnd(Date saleEnd) {
		this.saleEnd = saleEnd;
	}

	@Column
	public Double getLowestPrice() {
		return lowestPrice;
	}

	public void setLowestPrice(Double lowestPrice) {
		this.lowestPrice = lowestPrice;
	}

	@Column
	public Long getPriceEqualFlg() {
		return priceEqualFlg;
	}

	public void setPriceEqualFlg(Long priceEqualFlg) {
		this.priceEqualFlg = priceEqualFlg;
	}

	@Column
	public Long getRecommendOrder() {
		return recommendOrder;
	}

	public void setRecommendOrder(Long recommendOrder) {
		this.recommendOrder = recommendOrder;
	}

	@Column
	public Long getSampleFlag() {
		return sampleFlag;
	}

	public void setSampleFlag(Long sampleFlag) {
		this.sampleFlag = sampleFlag;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column
	public Date getPublishDate() {
		return publishDate;
	}

	public void setPublishDate(Date publishDate) {
		this.publishDate = publishDate;
	}

	@OneToMany( fetch = FetchType.LAZY)
	@JoinColumn(name = "ownerAssetParentId", referencedColumnName = "parentAssetId", insertable = false, updatable = false)
	public Set<AssetBinaryVersion> getBinaryVersions() {
		return binaryVersions;
	}

	public void setBinaryVersions(Set<AssetBinaryVersion> binaryVersions) {
		this.binaryVersions = binaryVersions;
	}

	
	

	
}

// $Id$