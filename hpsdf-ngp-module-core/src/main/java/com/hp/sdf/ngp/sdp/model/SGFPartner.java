/*
 * Copyright (c) 2009 Hewlett-Packard Company, All Rights Reserved.
 *
 * RESTRICTED RIGHTS LEGEND Use, duplication, or disclosure by the U.S.
 * Government is subject to restrictions as set forth in sub-paragraph
 * (c)(1)(ii) of the Rights in Technical Data and Computer Software
 * clause in DFARS 252.227-7013.
 *
 * Hewlett-Packard Company
 * 3000 Hanover Street
 * Palo Alto, CA 94304 U.S.A.
 * Rights for non-DOD U.S. Government Departments and Agencies are as
 * set forth in FAR 52.227-19(c)(1,2).
 */
package com.hp.sdf.ngp.sdp.model;

import java.util.List;

public class SGFPartner extends SGFBase {

	/**
	 * Company Information
	 */
	private CompanyInfo companyInfo;

	/**
	 * Contact Information
	 */
	private ContactDetails contactDetails;

	/**
	 * Login Information
	 */
	private LoginInfo loginInfo;

	/**
	 * Categories: ASP, SP, CP
	 */
	private List<String> categories;

	/**
	 * Associated Service Didcovery Group Name
	 */
	private String serviceDiscoveryGroup;

	/**
	 * Domain Name
	 */
	private String domainName;

	/**
	 * Partner Type
	 */
	private String partnerType;

	/**
	 * Modification Date
	 */
	private String modificationDate;

	/**
	 * Comment
	 */
	private String comment;

	public class Address {
		private String street;
		private String city;
		private String state;
		private String zip;
		private String country;

		public String getStreet() {
			return street;
		}

		public void setStreet(String street) {
			this.street = street;
		}

		public String getCity() {
			return city;
		}

		public void setCity(String city) {
			this.city = city;
		}

		public String getState() {
			return state;
		}

		public void setState(String state) {
			this.state = state;
		}

		public String getZip() {
			return zip;
		}

		public void setZip(String zip) {
			this.zip = zip;
		}

		public String getCountry() {
			return country;
		}

		public void setCountry(String country) {
			this.country = country;
		}
	}

	public class CompanyInfo {
		private String name;
		private Address address;
		private String url;
		private String description;

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public Address getAddress() {
			return address;
		}

		public void setAddress(Address address) {
			this.address = address;
		}

		public String getUrl() {
			return url;
		}

		public void setUrl(String url) {
			this.url = url;
		}

		public String getDescription() {
			return description;
		}

		public void setDescription(String description) {
			this.description = description;
		}
	}

	public class ContactDetails {
		private String title;
		private String firstname;
		private String lastname;
		private String responsibility;
		private Address address;
		private String fax;
		private String phone;
		private String mobile;
		private String email;
		private String language;

		public String getTitle() {
			return title;
		}

		public void setTitle(String title) {
			this.title = title;
		}

		public String getFirstname() {
			return firstname;
		}

		public void setFirstname(String firstname) {
			this.firstname = firstname;
		}

		public String getLastname() {
			return lastname;
		}

		public void setLastname(String lastname) {
			this.lastname = lastname;
		}

		public String getResponsibility() {
			return responsibility;
		}

		public void setResponsibility(String responsibility) {
			this.responsibility = responsibility;
		}

		public Address getAddress() {
			return address;
		}

		public void setAddress(Address address) {
			this.address = address;
		}

		public String getFax() {
			return fax;
		}

		public void setFax(String fax) {
			this.fax = fax;
		}

		public String getPhone() {
			return phone;
		}

		public void setPhone(String phone) {
			this.phone = phone;
		}

		public String getMobile() {
			return mobile;
		}

		public void setMobile(String mobile) {
			this.mobile = mobile;
		}

		public String getEmail() {
			return email;
		}

		public void setEmail(String email) {
			this.email = email;
		}

		public String getLanguage() {
			return language;
		}

		public void setLanguage(String language) {
			this.language = language;
		}
	}

	public class LoginInfo {
		private String username;
		private String password;
		private String passwordQuestion;
		private String passwordAnswer;

		public String getUsername() {
			return username;
		}

		public void setUsername(String username) {
			this.username = username;
		}

		public String getPassword() {
			return password;
		}

		public void setPassword(String password) {
			this.password = password;
		}

		public String getPasswordQuestion() {
			return passwordQuestion;
		}

		public void setPasswordQuestion(String passwordQuestion) {
			this.passwordQuestion = passwordQuestion;
		}

		public String getPasswordAnswer() {
			return passwordAnswer;
		}

		public void setPasswordAnswer(String passwordAnswer) {
			this.passwordAnswer = passwordAnswer;
		}
	}

	public CompanyInfo getCompanyInfo() {
		return companyInfo;
	}

	public void setCompanyInfo(CompanyInfo companyInfo) {
		this.companyInfo = companyInfo;
	}

	public ContactDetails getContactDetails() {
		return contactDetails;
	}

	public void setContactDetails(ContactDetails contactDetails) {
		this.contactDetails = contactDetails;
	}

	public LoginInfo getLoginInfo() {
		return loginInfo;
	}

	public void setLoginInfo(LoginInfo loginInfo) {
		this.loginInfo = loginInfo;
	}

	public List<String> getCategories() {
		return categories;
	}

	public void setCategories(List<String> categories) {
		this.categories = categories;
	}

	public String getServiceDiscoveryGroup() {
		return serviceDiscoveryGroup;
	}

	public void setServiceDiscoveryGroup(String serviceDiscoveryGroup) {
		this.serviceDiscoveryGroup = serviceDiscoveryGroup;
	}

	public String getDomainName() {
		return domainName;
	}

	public void setDomainName(String domainName) {
		this.domainName = domainName;
	}

	public String getPartnerType() {
		return partnerType;
	}

	public void setPartnerType(String partnerType) {
		this.partnerType = partnerType;
	}

	public String getModificationDate() {
		return modificationDate;
	}

	public void setModificationDate(String modificationDate) {
		this.modificationDate = modificationDate;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("companyInfo=").append(companyInfo).append(" ,");
		sb.append("contactDetails=").append(contactDetails).append(" ,");
		sb.append("loginInfo=").append(loginInfo).append(" ,");
		sb.append("categories=").append(categories).append(" ,");
		sb.append("serviceDiscoveryGroup=").append(serviceDiscoveryGroup)
				.append(" ,");
		sb.append("domainName=").append(domainName).append(" ,");
		sb.append("partnerType=").append(partnerType).append(" ,");
		sb.append("modificationDate=").append(modificationDate).append(" ,");
		sb.append("comment=").append(comment).append(" ");
		return sb.toString();
	}
}