package com.hp.sdf.ngp.model;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Status entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table
public class AssetBinaryVersion extends BaseEAVModel implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = -8263943031966018118L;
	private Long id;
	private Asset asset;
	private String fileName;
	private Date createDate=new Date();
	private Date updateDate=new Date();
	private String version;
	private String location; // with fileName, usage: file_path = context_path + location
	private Status status;
	private BigDecimal fileSize;
	private String externalId;
	private String name;
	private Date expireDate;
	private String thumbnailLocation;
	private String thumbnailMiddleLocation;
	private String thumbnailBigLocation;
	private String brief;
	private String description;

	
	private Long recommendOrder;
	private Date recommendStartDate;
	private Date recommendDueDate;
	private Date recommendUpdateDate;
	private Date publishDate;
	private Date newArrivalDueDate;
	private Long ownerAssetParentId;
	
	private Set<AssetLifecycleAction> assetLifecycleActions = new HashSet<AssetLifecycleAction>(0);
	private Set<AssetCategoryRelation> assetCategoryRelations = new HashSet<AssetCategoryRelation>(0);
	private Set<AssetTagRelation> assetTagRelations = new HashSet<AssetTagRelation>(0);
	private Set<AssetRestrictionRelation> assetRestrictionRelations = new HashSet<AssetRestrictionRelation>(
			0);
	
	private Set<ScreenShots> screenShotses = new HashSet<ScreenShots>(0);
	private Set<AssetPrice> assetPrices = new HashSet<AssetPrice>(0);
	
	
	
	
	public AssetBinaryVersion(){
		try {
			recommendOrder=new Long(Integer.MAX_VALUE);
			newArrivalDueDate=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse("9999-12-31 23:59:59");
			recommendStartDate=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse("1000-01-01 00:00:00");
			recommendDueDate=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse("9999-12-31 23:59:59");
		} catch (ParseException e) {
			// ignore
		}
	}
	
	public AssetBinaryVersion(Long id) {
		this.id = id;
	}
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="ASSETID")
	public Asset getAsset() {
		return asset;
	}
	public void setAsset(Asset asset) {
		this.asset = asset;
	}
	
	@Column
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(nullable=false)
	public Date getCreateDate() {
		return createDate;
	}
	
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	
	@Column
	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	@Column
	public String getLocation() {
		return location;
	}
	
	public void setLocation(String location) {
		this.location = location;
	}
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name="STATUSID")
	public Status getStatus() {
		return status;
	}
	public void setStatus(Status status) {
		this.status = status;
	}
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(nullable=false)
	public Date getUpdateDate() {
		return updateDate;
	}
	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}
	
	@Column
	public BigDecimal getFileSize() {
		return fileSize;
	}

	public void setFileSize(BigDecimal fileSize) {
		this.fileSize = fileSize;
	}

	@Column
	public String getExternalId() {
		return externalId;
	}
	
	public void setExternalId(String externalId) {
		this.externalId = externalId;
	}
	
	@Column(length=512)
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column
	public Date getExpireDate() {
		return expireDate;
	}
	
	public void setExpireDate(Date expireDate) {
		this.expireDate = expireDate;
	}
	
	@Column
	public String getThumbnailMiddleLocation() {
		return thumbnailMiddleLocation;
	}
	
	public void setThumbnailMiddleLocation(String thumbnailMiddleLocation) {
		this.thumbnailMiddleLocation = thumbnailMiddleLocation;
	}
	@Column
	public String getThumbnailBigLocation() {
		return thumbnailBigLocation;
	}
	
	public void setThumbnailBigLocation(String thumbnailBigLocation) {
		this.thumbnailBigLocation = thumbnailBigLocation;
	}
	
	@Column
	public String getThumbnailLocation() {
		return thumbnailLocation;
	}

	public void setThumbnailLocation(String thumbnailLocation) {
		this.thumbnailLocation = thumbnailLocation;
	}
	
	@Column(length=4000)
	public String getBrief() {
		return brief;
	}
	
	public void setBrief(String brief) {
		this.brief = brief;
	}
	
	@Column(length=4000)
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@OneToMany(cascade = CascadeType.REMOVE, fetch = FetchType.LAZY, mappedBy = "binaryVersion")
	public Set<AssetCategoryRelation> getAssetCategoryRelations() {
		return assetCategoryRelations;
	}
	public void setAssetCategoryRelations(
			Set<AssetCategoryRelation> assetCategoryRelations) {
		this.assetCategoryRelations = assetCategoryRelations;
	}
	
	@OneToMany(cascade = CascadeType.REMOVE, fetch = FetchType.LAZY, mappedBy = "binaryVersion")
	public Set<AssetTagRelation> getAssetTagRelations() {
		return assetTagRelations;
	}

	public void setAssetTagRelations(Set<AssetTagRelation> assetTagRelations) {
		this.assetTagRelations = assetTagRelations;
	}

	@OneToMany(cascade = CascadeType.REMOVE, fetch = FetchType.LAZY, mappedBy = "binaryVersion")
	public Set<AssetLifecycleAction> getAssetLifecycleActions() {
		return assetLifecycleActions;
	}

	public void setAssetLifecycleActions(
			Set<AssetLifecycleAction> assetLifecycleActions) {
		this.assetLifecycleActions = assetLifecycleActions;
	}
	
	@OneToMany(cascade = CascadeType.REMOVE, fetch = FetchType.LAZY, mappedBy = "binaryVersion")
	public Set<ScreenShots> getScreenShotses() {
		return screenShotses;
	}
	public void setScreenShotses(Set<ScreenShots> screenShotses) {
		this.screenShotses = screenShotses;
	}
	
	@OneToMany(cascade = CascadeType.REMOVE, fetch = FetchType.LAZY, mappedBy = "binaryVersion")
	public Set<AssetPrice> getAssetPrices() {
		return assetPrices;
	}
	public void setAssetPrices(Set<AssetPrice> assetPrices) {
		this.assetPrices = assetPrices;
	}
	
	@Column
	public Long getRecommendOrder() {
		return recommendOrder;
	}

	public void setRecommendOrder(Long recommendOrder) {
		this.recommendOrder = recommendOrder;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column
	public Date getRecommendStartDate() {
		return recommendStartDate;
	}

	public void setRecommendStartDate(Date recommendStartDate) {
		this.recommendStartDate = recommendStartDate;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column
	public Date getRecommendDueDate() {
		return recommendDueDate;
	}

	public void setRecommendDueDate(Date recommendDueDate) {
		this.recommendDueDate = recommendDueDate;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column
	public Date getPublishDate() {
		return publishDate;
	}

	public void setPublishDate(Date publishDate) {
		this.publishDate = publishDate;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column
	public Date getNewArrivalDueDate() {
		return newArrivalDueDate;
	}

	public void setNewArrivalDueDate(Date newArrivalDueDate) {
		this.newArrivalDueDate = newArrivalDueDate;
	}
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column
	public Date getRecommendUpdateDate() {
		return recommendUpdateDate;
	}

	public void setRecommendUpdateDate(Date recommendUpdateDate) {
		this.recommendUpdateDate = recommendUpdateDate;
	}

	@Column
	public Long getOwnerAssetParentId() {
		return ownerAssetParentId;
	}

	public void setOwnerAssetParentId(Long ownerAssetParentId) {
		this.ownerAssetParentId = ownerAssetParentId;
	}

	
	@OneToMany(cascade = CascadeType.REMOVE, fetch = FetchType.LAZY, mappedBy = "binaryVersion")
	public Set<AssetRestrictionRelation> getAssetRestrictionRelations() {
		return assetRestrictionRelations;
	}

	public void setAssetRestrictionRelations(
			Set<AssetRestrictionRelation> assetRestrictionRelations) {
		this.assetRestrictionRelations = assetRestrictionRelations;
	}	
	
}