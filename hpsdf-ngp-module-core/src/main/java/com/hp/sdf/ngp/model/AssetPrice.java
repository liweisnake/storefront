package com.hp.sdf.ngp.model;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;


/**
 * Tag entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table
public class AssetPrice  implements java.io.Serializable {

	// Fields

	private Long id;
	private BigDecimal amount;
	private String currency;
	private Asset asset;
	private AssetBinaryVersion binaryVersion;

	// Constructors

	/** default constructor */
	public AssetPrice() {
	}

	// Property accessors
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
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
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ASSETID", nullable = false)
	public Asset getAsset() {
		return this.asset;
	}

	public void setAsset(Asset asset) {
		this.asset = asset;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "VERSIONID")
	public AssetBinaryVersion getBinaryVersion() {
		return binaryVersion;
	}

	public void setBinaryVersion(AssetBinaryVersion binaryVersion) {
		this.binaryVersion = binaryVersion;
	}
	
}