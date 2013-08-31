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
public class RestrictedType  implements java.io.Serializable {

	// Fields

	private Long id;
	private String type;
	
	private Set<AssetRestrictionRelation> assetRestrictionRelations = new HashSet<AssetRestrictionRelation>(
			0);
	
	// Constructors

	/** default constructor */
	public RestrictedType() {
	}

	/** minimal constructor */
	public RestrictedType(Long id, String type) {
		this.id = id;
		this.type = type;
	}
	
	public RestrictedType(String type){
		this.type=type;
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

	@Column(nullable = false, length = 50,unique=true)
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "restrictedType")
	public Set<AssetRestrictionRelation> getAssetRestrictionRelations() {
		return assetRestrictionRelations;
	}

	public void setAssetRestrictionRelations(
			Set<AssetRestrictionRelation> assetRestrictionRelations) {
		this.assetRestrictionRelations = assetRestrictionRelations;
	}
	
	

}