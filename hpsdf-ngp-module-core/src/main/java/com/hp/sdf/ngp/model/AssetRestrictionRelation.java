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
public class AssetRestrictionRelation  implements java.io.Serializable {

	// Fields

	private Long id;
	private RestrictedType restrictedType;
	private Asset asset;
	private AssetBinaryVersion binaryVersion;

	// Constructors

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "VERSIONID")
	public AssetBinaryVersion getBinaryVersion() {
		return binaryVersion;
	}

	public void setBinaryVersion(AssetBinaryVersion binaryVersion) {
		this.binaryVersion = binaryVersion;
	}

	/** default constructor */
	public AssetRestrictionRelation() {
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
	@JoinColumn(name = "RESTRICTIONID", nullable = false)
	public RestrictedType getRestrictedType() {
		return restrictedType;
	}

	public void setRestrictedType(RestrictedType restrictedType) {
		this.restrictedType = restrictedType;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ASSETID", nullable = false)
	public Asset getAsset() {
		return this.asset;
	}

	public void setAsset(Asset asset) {
		this.asset = asset;
	}

}