package com.hp.sdf.ngp.model;

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


/**
 * Category entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table
public class Category implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long id;
	//value zero means category is top category
	private Category parentCategory;
	private String name;
	private String description;
	private String externalId;
	private String displayName;
	private String source;

	private Set<AssetCategoryRelation> assetCategoryRelations = new HashSet<AssetCategoryRelation>(
			0);
	private Set<Category> subCategorys=new HashSet<Category>(0);
	// Constructors

	/** default constructor */
	public Category() {
	}

	/** minimal constructor */
	public Category(Long id, String name) {
		this.id = id;
		this.name = name;
	}
	
	public Category(Long id){
		this.id=id;
	}

	/** full constructor */
	public Category(Long id, String name, String description,
			Set<AssetCategoryRelation> assetCategoryRelations) {
		this.id = id;
		this.name = name;
		this.description = description;
		this.assetCategoryRelations = assetCategoryRelations;
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

	@Column(nullable = false,length = 100)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(length = 512)
	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	
	@Column
	public String getExternalId() {
		return externalId;
	}

	public void setExternalId(String externalId) {
		this.externalId = externalId;
	}

	@Column
	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	@Column
	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PARENTID")
	public Category getParentCategory() {
		return parentCategory;
	}

	public void setParentCategory(Category parentCategory) {
		this.parentCategory = parentCategory;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "category")
	public Set<AssetCategoryRelation> getAssetCategoryRelations() {
		return this.assetCategoryRelations;
	}

	public void setAssetCategoryRelations(
			Set<AssetCategoryRelation> assetCategoryRelations) {
		this.assetCategoryRelations = assetCategoryRelations;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "parentCategory")
	public Set<Category> getSubCategorys() {
		return subCategorys;
	}

	public void setSubCategorys(Set<Category> subCategorys) {
		this.subCategorys = subCategorys;
	}
	
	
}