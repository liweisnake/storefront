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
 * Servicesubscription entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table
public class ServiceSubscription  implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = 5194121388085041209L;
	private Long id;
	private Service service;
	private String userid;
	private Date createDate=new Date();
	private ApiKey apiKeyRelation;

	// Property accessors
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "SERVICEID")
	public Service getService() {
		return this.service;
	}

	public void setService(Service service) {
		this.service = service;
	}

	@Column(length = 128)
	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column
	public Date getCreateDate() {
		return this.createDate;
	}

	public void setCreateDate(Date createdate) {
		this.createDate = createdate;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "APIKEYID", nullable = false)
	public ApiKey getApiKeyRelation() {
		return apiKeyRelation;
	}

	public void setApiKeyRelation(ApiKey apiKeyRelation) {
		this.apiKeyRelation = apiKeyRelation;
	}
	
	

}