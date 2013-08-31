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
 * Appkeyrelation entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table
public class ApiKey implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = 486377427723311396L;
	private Long id;
	private String sgName;
	private String sgPassword;
	private String description;
	private Set<ServiceSubscription> serviceSubscriptions = new HashSet<ServiceSubscription>(
			0);

	// Constructors

	/** default constructor */
	public ApiKey() {
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

	@Column(length = 512)
	public String getDescription() {
		return this.description;
	}
	
	@Column(nullable = false, length = 100)
	public String getSgName() {
		return sgName;
	}

	public void setSgName(String sgName) {
		this.sgName = sgName;
	}

	@Column(nullable = false, length = 100)
	public String getSgPassword() {
		return sgPassword;
	}

	public void setSgPassword(String sgPassword) {
		this.sgPassword = sgPassword;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "apiKeyRelation")
	public Set<ServiceSubscription> getServiceSubscriptions() {
		return serviceSubscriptions;
	}

	public void setServiceSubscriptions(
			Set<ServiceSubscription> serviceSubscriptions) {
		this.serviceSubscriptions = serviceSubscriptions;
	}
	
	

}