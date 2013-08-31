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
 * Apptagrelation entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table
public class AssetTagRelation implements java.io.Serializable {

	// Fields

	private Long id;
	private Tag tag;
	private Asset asset;
	
	private AssetBinaryVersion binaryVersion;

	// Constructors

	/** default constructor */
	public AssetTagRelation() {
	}
	
	public AssetTagRelation(Asset asset, Tag tag){
		this.tag = tag;
		this.asset = asset;
	}

	/** full constructor */
	public AssetTagRelation(Long id, Tag tag, Asset asset) {
		this.id = id;
		this.tag = tag;
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
	@JoinColumn(name = "TAGID", nullable = false)
	public Tag getTag() {
		return this.tag;
	}

	public void setTag(Tag tag) {
		this.tag = tag;
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