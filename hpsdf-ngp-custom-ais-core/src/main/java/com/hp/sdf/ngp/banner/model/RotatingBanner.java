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

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.hp.sdf.ngp.banner.BannerType;

@Entity
@Table
public class RotatingBanner extends Banner{
	
	private static final long serialVersionUID = 1L; 
	
	private Set<ContentSet> contentSets;
	
	public RotatingBanner(){
		
	}
	
	public RotatingBanner(BaseBanner banner, Set<ContentSet> contentSets){
		super.setBannerType(BannerType.rotatingBanner);
		super.setDescription(banner.getDescription());
		super.setName(banner.getName());
		super.setBannerStatus(banner.getBannerStatus());
		this.setId(banner.getId());
		this.contentSets = contentSets;
	}
	
	@Transient
	public void setBanner(Banner banner){
		super.setBannerType(BannerType.rotatingBanner);
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

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "banner")
	public Set<ContentSet> getContentSets() {
		return contentSets;
	}

	public void setContentSets(Set<ContentSet> contentSets) {
		this.contentSets = contentSets;
	}
	
	

}

// $Id$