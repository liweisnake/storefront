package com.hp.sdf.ngp.model;

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
 * Tag entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name="ASSETPROVIDER")
public class Provider extends BaseEAVModel implements java.io.Serializable {

	private static final long serialVersionUID = -4329312934265469536L;

	private Long id;
	private Double settlementRate;
	private Double commissionRate;
	private String name;
	private String firstName;
	private String lastName;
	private String organization;
	private Status status;
	private String streetAddress;
	private String city;
	private String country;
	private String phone;
	private String email;
	private String locale;
	private Date contractExpireDate;
	private String homePage;
	private Integer priority;
	private String externalId;
	private String source;
	private Date contractStartDate;
	
	private Set<ContentProviderOperator> contentProviderOperators = new HashSet<ContentProviderOperator>(0);
	
	// Constructors

	/** default constructor */
	public Provider() {
	}

	public Provider(Long id) {
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
	public Double getSettlementRate() {
		return settlementRate;
	}

	public void setSettlementRate(Double settlementRate) {
		this.settlementRate = settlementRate;
	}

	@Column
	public Double getCommissionRate() {
		return commissionRate;
	}

	public void setCommissionRate(Double commissionRate) {
		this.commissionRate = commissionRate;
	}

	@Column(nullable = false, length = 30,unique=true)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column
	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	@Column
	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	@Column
	public String getOrganization() {
		return organization;
	}

	public void setOrganization(String organization) {
		this.organization = organization;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name="STATUSID")
	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	@Column
	public String getStreetAddress() {
		return streetAddress;
	}

	public void setStreetAddress(String streetAddress) {
		this.streetAddress = streetAddress;
	}

	@Column
	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	@Column
	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	@Column
	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	@Column
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	@Column
	public String getLocale() {
		return locale;
	}

	public void setLocale(String locale) {
		this.locale = locale;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column
	public Date getContractExpireDate() {
		return contractExpireDate;
	}

	public void setContractExpireDate(Date contractExpireDate) {
		this.contractExpireDate = contractExpireDate;
	}

	@Column
	public String getHomePage() {
		return homePage;
	}

	public void setHomePage(String homePage) {
		this.homePage = homePage;
	}
	
	@Column
	public Integer getPriority() {
		return priority;
	}

	public void setPriority(Integer priority) {
		this.priority = priority;
	}

	@Column
	public String getExternalId() {
		return externalId;
	}

	public void setExternalId(String externalId) {
		this.externalId = externalId;
	}

	@Column
	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "assetProvider")
	public Set<ContentProviderOperator> getContentProviderOperators() {
		return contentProviderOperators;
	}

	public void setContentProviderOperators(
			Set<ContentProviderOperator> contentProviderOperators) {
		this.contentProviderOperators = contentProviderOperators;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column
	public Date getContractStartDate() {
		return contractStartDate;
	}

	public void setContractStartDate(Date contractStartDate) {
		this.contractStartDate = contractStartDate;
	}
	
	
	
}