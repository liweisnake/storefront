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
package com.hp.sdf.ngp.ais.ui.page.banner.management;

import java.io.Serializable;

import com.hp.sdf.ngp.banner.model.Banner;
import com.hp.sdf.ngp.banner.model.BaseBanner;

public class BannerProxy implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7538233284566224969L;

	private Long id;

	private String name;

	private String description;

	private String bannerType;

	private String bannerStatus;

	public BannerProxy() {

	}
	
	public BannerProxy(Banner base) {
		this.setId(base.getId());
		this.setName(base.getName());
		this.setDescription(base.getDescription());
		this.setBannerType(base.getBannerType().toString());
		this.setBannerStatus(base.getBannerStatus().toString());
	}

	public BannerProxy(BaseBanner base) {
		this.setId(base.getId());
		this.setName(base.getName());
		this.setDescription(base.getDescription());
		this.setBannerType(base.getBannerType().toString());
		this.setBannerStatus(base.getBannerStatus().toString());
	}

	public BannerProxy(Long id, String name, String description, String bannerType) {
		this.id = id;
		this.name = name;
		this.description = description;
		this.bannerType = bannerType;
		this.bannerStatus = "preview";
	}

	public BannerProxy(Long id, String name, String description, String bannerType, String status) {
		this.id = id;
		this.name = name;
		this.description = description;
		this.bannerType = bannerType;
		this.bannerStatus = status;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getBannerType() {
		return bannerType;
	}

	public void setBannerType(String bannerType) {
		this.bannerType = bannerType;
	}

	public String getBannerStatus() {
		return bannerStatus;
	}

	public void setBannerStatus(String bannerStatus) {
		this.bannerStatus = bannerStatus;
	}
}

// $Id$