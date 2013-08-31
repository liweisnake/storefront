package com.hp.sdf.ngp.model;


import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


/**
 * Tag entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table
public class ScreenShots  implements java.io.Serializable {


	/**
	 * 
	 */
	private static final long serialVersionUID = 7621737358523227527L;
	private Long id;
	private String mediaType;
	private Asset asset;
	private String description;
	private String storeLocation;
	private Date createDate=new Date();
	private AssetBinaryVersion binaryVersion;
	private Long sequence;

	// Constructors

	/** default constructor */
	public ScreenShots() {
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
	public String getMediaType() {
		return mediaType;
	}

	public void setMediaType(String mediaType) {
		this.mediaType = mediaType;
	}

	@Column
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ASSETID", nullable = false)
	public Asset getAsset() {
		return this.asset;
	}

	public void setAsset(Asset asset) {
		this.asset = asset;
	}

	@Column
	public String getStoreLocation() {
		return storeLocation;
	}

	public void setStoreLocation(String storeLocation) {
		this.storeLocation = storeLocation;
	}
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column
	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "VERSIONID")
	public AssetBinaryVersion getBinaryVersion() {
		return binaryVersion;
	}

	public void setBinaryVersion(AssetBinaryVersion binaryVersion) {
		this.binaryVersion = binaryVersion;
	}

	@Column
	public Long getSequence() {
		return sequence;
	}

	public void setSequence(Long sequence) {
		this.sequence = sequence;
	}
	
	
}