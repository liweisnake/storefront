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
import javax.persistence.OneToMany;
import javax.persistence.Table;


/**
 * Platform entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table
public class Platform implements java.io.Serializable {

	// Fields

	private Long id;
	private String name;
	private String description;
	private Set<AssetPlatformRelation> assetPlatformRelations = new HashSet<AssetPlatformRelation>(0);

	// Constructors

	/** default constructor */
	public Platform() {
	}

	/** minimal constructor */
	public Platform(Long id) {
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

	@Column(nullable = false,unique = true, length = 100)
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

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "platform")
	public Set<AssetPlatformRelation> getAssetPlatformRelations() {
		return assetPlatformRelations;
	}

	public void setAssetPlatformRelations(
			Set<AssetPlatformRelation> assetPlatformRelations) {
		this.assetPlatformRelations = assetPlatformRelations;
	}

}