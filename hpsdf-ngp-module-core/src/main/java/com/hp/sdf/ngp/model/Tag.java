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
 * Tag entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table
public class Tag implements java.io.Serializable {

	// Fields

	private Long id;
	private String name;
	private String description;
	private Set<AssetTagRelation> assetTagRelations = new HashSet<AssetTagRelation>(0);

	// Constructors

	/** default constructor */
	public Tag() {
	}

	public Tag(Long id) {
		this.id = id;
	}

	/** minimal constructor */
	public Tag(Long id, String name) {
		this.id = id;
		this.name = name;
	}
	
	public Tag(String name){
		this.name=name;
	}

	/** full constructor */
	public Tag(Long id, String name, String description,
			Set<AssetTagRelation> AssetTagRelations) {
		this.id = id;
		this.name = name;
		this.description = description;
		this.assetTagRelations = AssetTagRelations;
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

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "tag")
	public Set<AssetTagRelation> getAssetTagRelations() {
		return this.assetTagRelations;
	}

	public void setAssetTagRelations(Set<AssetTagRelation> assetTagRelations) {
		this.assetTagRelations = assetTagRelations;
	}

}