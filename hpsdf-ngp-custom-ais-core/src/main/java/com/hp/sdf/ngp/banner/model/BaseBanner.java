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
package com.hp.sdf.ngp.banner.model;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.hp.sdf.ngp.banner.BannerStatus;
import com.hp.sdf.ngp.banner.BannerType;

@Entity
@Table
public class BaseBanner extends Banner {
	
	private static final long serialVersionUID = 1L;
	
	private String stauts;
	
	private String type;
	
	public BaseBanner(){
		
	}
	
	public BaseBanner(Long id, String name, String description, BannerType bannerType){
		super.setId(id);
		super.setName(name);
		super.setDescription(description);
		super.setBannerType(bannerType);
		super.setBannerStatus(BannerStatus.submit);
	}
	
	public BaseBanner(Long id, String name, String description, BannerType bannerType, BannerStatus status){
		super.setId(id);
		super.setName(name);
		super.setDescription(description);
		super.setBannerType(bannerType);
		super.setBannerStatus(status);
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Long getId() {
		return super.getId();
	}

	public void setId(Long id) {
		super.setId(id);
	}

	@Column(nullable = false, unique = true, length = 100)
	public String getName() {
		return super.getName();
	}

	public void setName(String name) {
		super.setName(name);
	}

	@Column(length = 225)
	public String getDescription() {
		return super.getDescription();
	}

	public void setDescription(String description) {
		super.setDescription(description);
	}

	@Column(nullable = false)
	public BannerType getBannerType() {
		return super.getBannerType();
	}

	public void setBannerType(BannerType bannerType) {
		super.setBannerType(bannerType);
	}

	@Column(nullable = false)
	public BannerStatus getBannerStatus() {
		return super.getBannerStatus();
	}

	public void setBannerStatus(BannerStatus bannerStatus) {
		super.setBannerStatus(bannerStatus);
	}

	@Transient
	public String getStauts() {
		return stauts;
	}

	public void setStauts(String stauts) {
		this.stauts = stauts;
	}

	@Transient
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
}

// $Id$