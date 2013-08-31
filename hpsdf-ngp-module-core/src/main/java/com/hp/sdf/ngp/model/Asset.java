package com.hp.sdf.ngp.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
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
 * App entity. @author MyEclipse Persistence Tools
 */
@javax.persistence.Entity
@Table
public class Asset extends BaseEAVModel implements java.io.Serializable {

	private static final long serialVersionUID = -9215610077108665003L;
	private Long id;
	private String authorid;
	private String name;
	private String description;
	private String source;
	private String assetHomePage;
	private Date createDate = new Date();
	private Date updateDate = new Date();
	private String thumbnailLocation;
	private String thumbnailMiddleLocation;
	private String thumbnailBigLocation;
	private String brief;
	private String currentVersion; // latest published version
	private String docUrl;
	private Status status;
	private Double averageUserRating=0D;
	private Long downloadCount = 0L;
	private String externalId;
	private Long currentVersionId;
	private Provider assetProvider;
	private Asset asset;

	
	private Long recommendOrder;
	private Date recommendStartDate;
	private Date recommendDueDate;
	private Date recommendUpdateDate;
	private Date publishDate;
	private Date newArrivalDueDate;
	
	private Set<PurchaseHistory> purchaseHistorys = new HashSet<PurchaseHistory>(0);
	private Set<ShoppingCart> shoppingCarts = new HashSet<ShoppingCart>(0);
	private Set<ScreenShots> screenShotses = new HashSet<ScreenShots>(0);
	private Set<AssetRating> assetRatings = new HashSet<AssetRating>(0);
	private Set<Comments> commentses = new HashSet<Comments>(0);
	private Set<AssetLifecycleAction> assetLifecycleActions = new HashSet<AssetLifecycleAction>(0);
	private Set<AssetTagRelation> assetTagRelations = new HashSet<AssetTagRelation>(0);
	private Set<AssetCategoryRelation> assetCategoryRelations = new HashSet<AssetCategoryRelation>(0);
	private Set<AssetPlatformRelation> assetPlatformRelations = new HashSet<AssetPlatformRelation>(0);
	private Set<AssetPrice> assetPrices = new HashSet<AssetPrice>(0);
	private Set<AssetBinaryVersion> binaryVersions = new HashSet<AssetBinaryVersion>(0);
	private Set<UserDownloadHistory> userDownloadHistorys = new HashSet<UserDownloadHistory>(0);
	private Set<Asset> assets = new HashSet<Asset>(0);
	private Set<AssetRestrictionRelation> assetRestrictionRelations = new HashSet<AssetRestrictionRelation>(
			0);
	
	
	public Asset() {
		try {
			newArrivalDueDate=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse("9999-12-31 23:59:59");
			recommendStartDate=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse("1000-01-01 00:00:00");
			recommendDueDate=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse("9999-12-31 23:59:59");
		} catch (ParseException e) {
			// ignore
		}
	}
	
	public Asset(Long id) {
		this.id = id;
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
	public String getAuthorid() {
		return authorid;
	}

	public void setAuthorid(String authorid) {
		this.authorid = authorid;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(nullable=false)
	public Date getCreateDate() {
		return this.createDate;
	}

	public void setCreateDate(Date createdate) {
		this.createDate = createdate;
	}
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column
	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	@Column(length=4000)
	public String getBrief() {
		return brief;
	}

	public void setBrief(String brief) {
		this.brief = brief;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name="STATUSID", nullable=false)
	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}
	
	
	@Column(length=512)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(length=4000)
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Column(nullable=false)
	public Long getDownloadCount() {
		return downloadCount;
	}

	public void setDownloadCount(Long downloadCount) {
		this.downloadCount = downloadCount;
	}
	
	@Column
	public String getExternalId() {
		return externalId;
	}

	public void setExternalId(String externalId) {
		this.externalId = externalId;
	}

	@Column
	public Long getCurrentVersionId() {
		return currentVersionId;
	}

	public void setCurrentVersionId(Long currentVersionId) {
		this.currentVersionId = currentVersionId;
	}

	@Column
	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}
	
	@Column
	public String getAssetHomePage() {
		return assetHomePage;
	}

	public void setAssetHomePage(String assetHomePage) {
		this.assetHomePage = assetHomePage;
	}
	
	@Column
	public String getThumbnailLocation() {
		return thumbnailLocation;
	}

	public void setThumbnailLocation(String thumbnailLocation) {
		this.thumbnailLocation = thumbnailLocation;
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
	public String getCurrentVersion() {
		return currentVersion;
	}

	public void setCurrentVersion(String currentVersion) {
		this.currentVersion = currentVersion;
	}

	@Column
	public String getDocUrl() {
		return docUrl;
	}

	public void setDocUrl(String docUrl) {
		this.docUrl = docUrl;
	}

	@Column
	public Double getAverageUserRating() {
		return averageUserRating;
	}

	public void setAverageUserRating(Double averageUserRating) {
		this.averageUserRating = averageUserRating;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PROVIDERID")
	public Provider getAssetProvider() {
		return assetProvider;
	}

	public void setAssetProvider(Provider assetProvider) {
		this.assetProvider = assetProvider;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PARENTID")
	public Asset getAsset() {
		return asset;
	}

	public void setAsset(Asset asset) {
		this.asset = asset;
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
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "asset")
	public Set<PurchaseHistory> getPurchaseHistorys() {
		return purchaseHistorys;
	}

	public void setPurchaseHistorys(Set<PurchaseHistory> purchaseHistorys) {
		this.purchaseHistorys = purchaseHistorys;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "asset")
	public Set<AssetRating> getAssetRatings() {
		return assetRatings;
	}

	public void setAssetRatings(Set<AssetRating> assetRatings) {
		this.assetRatings = assetRatings;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "asset")
	public Set<Comments> getCommentses() {
		return commentses;
	}

	public void setCommentses(Set<Comments> commentses) {
		this.commentses = commentses;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "asset")
	public Set<AssetLifecycleAction> getAssetLifecycleActions() {
		return assetLifecycleActions;
	}

	public void setAssetLifecycleActions(
			Set<AssetLifecycleAction> assetLifecycleActions) {
		this.assetLifecycleActions = assetLifecycleActions;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "asset")
	public Set<AssetTagRelation> getAssetTagRelations() {
		return assetTagRelations;
	}

	public void setAssetTagRelations(Set<AssetTagRelation> assetTagRelations) {
		this.assetTagRelations = assetTagRelations;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "asset")
	public Set<AssetCategoryRelation> getAssetCategoryRelations() {
		return assetCategoryRelations;
	}

	public void setAssetCategoryRelations(
			Set<AssetCategoryRelation> assetCategoryRelations) {
		this.assetCategoryRelations = assetCategoryRelations;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "asset")
	public Set<AssetBinaryVersion> getBinaryVersions() {
		return binaryVersions;
	}

	public void setBinaryVersions(Set<AssetBinaryVersion> binaryVersions) {
		this.binaryVersions = binaryVersions;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "asset")
	public Set<UserDownloadHistory> getUserDownloadHistorys() {
		return userDownloadHistorys;
	}

	public void setUserDownloadHistorys(
			Set<UserDownloadHistory> userDownloadHistorys) {
		this.userDownloadHistorys = userDownloadHistorys;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "asset")
	public Set<AssetPlatformRelation> getAssetPlatformRelations() {
		return assetPlatformRelations;
	}

	public void setAssetPlatformRelations(
			Set<AssetPlatformRelation> assetPlatformRelations) {
		this.assetPlatformRelations = assetPlatformRelations;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "asset")
	public Set<AssetPrice> getAssetPrices() {
		return assetPrices;
	}

	public void setAssetPrices(Set<AssetPrice> assetPrices) {
		this.assetPrices = assetPrices;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "asset")
	public Set<ScreenShots> getScreenShotses() {
		return screenShotses;
	}

	public void setScreenShotses(Set<ScreenShots> screenShotses) {
		this.screenShotses = screenShotses;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "asset")
	public Set<ShoppingCart> getShoppingCarts() {
		return shoppingCarts;
	}

	public void setShoppingCarts(Set<ShoppingCart> shoppingCarts) {
		this.shoppingCarts = shoppingCarts;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "asset")
	public Set<Asset> getAssets() {
		return assets;
	}

	public void setAssets(Set<Asset> assets) {
		this.assets = assets;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "asset")
	public Set<AssetRestrictionRelation> getAssetRestrictionRelations() {
		return assetRestrictionRelations;
	}

	public void setAssetRestrictionRelations(
			Set<AssetRestrictionRelation> assetRestrictionRelations) {
		this.assetRestrictionRelations = assetRestrictionRelations;
	}

	
	
	
}