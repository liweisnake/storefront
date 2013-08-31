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

import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.hp.sdf.ngp.banner.BannerType;

@Entity
@Table
public class TabbedBanner extends Banner{
	
	private static final long serialVersionUID = 1L; 
	
	private List<Content> contents;
	private Boolean autoRotation;
	private Long intervalRotation;
	
	public TabbedBanner(){
		
	}
	
	public TabbedBanner(BaseBanner banner, List<Content> contents, Boolean autoRotation, Long intervalRotation){
		super.setBannerType(BannerType.tabbedBanner);
		super.setDescription(banner.getDescription());
		super.setName(banner.getName());
		super.setBannerStatus(banner.getBannerStatus());
		this.setId(banner.getId());
		this.contents = contents;
		this.autoRotation = autoRotation;
		this.intervalRotation = intervalRotation;
	}
	
	@Transient
	public void setBanner(Banner banner){
		super.setBannerType(BannerType.tabbedBanner);
		super.setDescription(banner.getDescription());
		super.setName(banner.getName());
		super.setBannerStatus(banner.getBannerStatus());
	}
	
	@Id
	public Long getId() {
		return super.getId();
	}

	public void setId(Long id) {
		super.setId(id);
	}

	@Transient
	public List<Content> getContents() {
		return contents;
	}

	public void setContents(List<Content> contents) {
		this.contents = contents;
	}

	@Column(nullable = false)
	public Boolean getAutoRotation() {
		return autoRotation;
	}

	public void setAutoRotation(Boolean autoRotation) {
		this.autoRotation = autoRotation;
	}

	@Column(nullable = true, length = 100)
	public Long getIntervalRotation() {
		return intervalRotation;
	}

	public void setIntervalRotation(Long intervalRotation) {
		this.intervalRotation = intervalRotation;
	}
	
	

}

// $Id$