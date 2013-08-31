package com.hp.sdf.ngp.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Userprofile entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table
public class UserProfile extends BaseEAVModel implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = 579158813141517855L;
	private String userid;
	private Language language;
	private Country country;
	private String password;
	private String email;
	private String idcard;
	private String gender;
	private String cellphone;
	private String company;
	private String address;
	private String zip;
	private Date birthday = new Date();
	private String firstname;
	private String lastname;
	private String preferRole;
	private String verificationCode;
	private Date createDate = new Date();
	private Long onlineTime;
	private Boolean disabled;
	private Date disabledDate;

	private Set<UserRoleCategory> userRoleCategories = new HashSet<UserRoleCategory>(0);
	private Set<ContentProviderOperator> contentProviderOperators = new HashSet<ContentProviderOperator>(0);

	public UserProfile() {
	}

	public UserProfile(String userid, String email) {
		this.userid = userid;
		this.email = email;
	}

	public UserProfile(String userid, String password, String email, Date createDate) {
		this.userid = userid;
		this.password = password;
		this.email = email;
		this.createDate = createDate;
	}

	// Property accessors
	@Id
	@Column(length = 128)
	public String getUserid() {
		return this.userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "LANGUAGEID")
	public Language getLanguage() {
		return language;
	}

	public void setLanguage(Language language) {
		this.language = language;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "COUNTRYID")
	public Country getCountry() {
		return country;
	}

	public void setCountry(Country country) {
		this.country = country;
	}

	@Column(length = 50)
	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Column(nullable = false, length = 256)
	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Column(length = 50)
	public String getIdcard() {
		return this.idcard;
	}

	public void setIdcard(String idcard) {
		this.idcard = idcard;
	}

	@Column(length = 10)
	public String getGender() {
		return this.gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	@Column(length = 50)
	public String getCellphone() {
		return this.cellphone;
	}

	public void setCellphone(String cellphone) {
		this.cellphone = cellphone;
	}

	@Column(length = 100)
	public String getCompany() {
		return this.company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	@Column(length = 256)
	public String getAddress() {
		return this.address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	@Column(length = 50)
	public String getZip() {
		return this.zip;
	}

	public void setZip(String zip) {
		this.zip = zip;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column
	public Date getBirthday() {
		return this.birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	@Column(length = 50)
	public String getFirstname() {
		return this.firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	@Column(length = 50)
	public String getLastname() {
		return this.lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	@Column(length = 50)
	public String getPreferRole() {
		return this.preferRole;
	}

	public void setPreferRole(String preferrole) {
		this.preferRole = preferrole;
	}

	@Column(length = 50)
	public String getVerificationCode() {
		return verificationCode;
	}

	public void setVerificationCode(String verificationCode) {
		this.verificationCode = verificationCode;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column
	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	@Column
	public Long getOnlineTime() {
		return onlineTime;
	}

	public void setOnlineTime(Long onlineTime) {
		this.onlineTime = onlineTime;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "userProfile")
	public Set<UserRoleCategory> getUserRoleCategories() {
		return userRoleCategories;
	}

	public void setUserRoleCategories(Set<UserRoleCategory> userRoleCategories) {
		this.userRoleCategories = userRoleCategories;
	}

	@OneToMany(fetch = FetchType.LAZY)
	@JoinColumn(name = "userid", referencedColumnName = "userid", insertable = false, updatable = false)
	public Set<ContentProviderOperator> getContentProviderOperators() {
		return contentProviderOperators;
	}

	public void setContentProviderOperators(Set<ContentProviderOperator> contentProviderOperators) {
		this.contentProviderOperators = contentProviderOperators;
	}

	@Column
	public Boolean getDisabled() {
		return disabled;
	}

	public void setDisabled(Boolean disabled) {
		this.disabled = disabled;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column
	public Date getDisabledDate() {
		return disabledDate;
	}

	public void setDisabledDate(Date disabledDate) {
		this.disabledDate = disabledDate;
	}

}