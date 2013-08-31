package com.hp.sdf.ngp.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;


/**
 * Tag entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table
public class ContentProviderOperator  implements java.io.Serializable {

	// Fields

	private Long id;
	private String userid;
	private Provider assetProvider;

	// Constructors

	/** default constructor */
	public ContentProviderOperator() {
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
	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="ASSETPROVIDERID", nullable=false)
	public Provider getAssetProvider() {
		return assetProvider;
	}

	public void setAssetProvider(Provider assetProvider) {
		this.assetProvider = assetProvider;
	}
	

}