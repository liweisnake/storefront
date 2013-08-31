package com.hp.sdf.ngp.model;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;


/**
 * Appcategoryrelation entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table
public class AssetCategoryRelation implements java.io.Serializable {

	// Fields

	private Long id;
	private Category category;
	private Asset asset;
	private AssetBinaryVersion binaryVersion;
	

	// Constructors

	/** default constructor */
	public AssetCategoryRelation() {
	}

	public AssetCategoryRelation(Category category, Asset asset) {
		this.category = category;
		this.asset = asset;
	}

	/** full constructor */
	public AssetCategoryRelation(Long id, Category category, Asset asset) {
		this.id = id;
		this.category = category;
		this.asset = asset;
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

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CTGID", nullable = false)
	public Category getCategory() {
		return this.category;
	}

	public void setCategory(Category category) {
		this.category = category;
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