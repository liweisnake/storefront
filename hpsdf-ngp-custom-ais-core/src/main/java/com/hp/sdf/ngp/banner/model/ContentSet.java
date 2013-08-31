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

import java.io.Serializable;
import java.util.Date;
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

@Entity
@Table
public class ContentSet implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private Long id;
	private String name;
	//1000000=Mon, 0100000=Tue, 0010000=Wed, 0001000=Thu, 0000100=Fri, 0000010=Sat, 0000001=Sun
	//1111111=Mon,Tue,Wed,Thu,Fri,Sat,Sun
	private String dow;
	private String hodStart;
	private String hodEnd;
	private Date expiration;
	private RotatingBanner banner;
	private Set<ContentWeight> contentWeights;
	
	public ContentSet(){
		
	}
	
	public ContentSet(String name, String dow, String hodStart, String hodEnd, Date expiration){
		this.name = name;
		this.dow = dow;
		this.hodStart = hodStart;
		this.hodEnd = hodEnd;
		this.expiration = expiration;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(nullable = false, length = 100)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(nullable = false, length = 10)
	public String getDow() {
		return dow;
	}

	public void setDow(String dow) {
		this.dow = dow;
	}

	@Column(nullable = false, length = 10)
	public String getHodStart() {
		return hodStart;
	}

	public void setHodStart(String hodStart) {
		this.hodStart = hodStart;
	}

	@Column(nullable = false, length = 10)
	public String getHodEnd() {
		return hodEnd;
	}

	public void setHodEnd(String hodEnd) {
		this.hodEnd = hodEnd;
	}

	@Column(nullable = false)
	public Date getExpiration() {
		return expiration;
	}

	public void setExpiration(Date expiration) {
		this.expiration = expiration;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "contentSet")
	public Set<ContentWeight> getContentWeights() {
		return contentWeights;
	}

	public void setContentWeights(Set<ContentWeight> contentWeights) {
		this.contentWeights = contentWeights;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "BANNERID")
	public RotatingBanner getBanner() {
		return banner;
	}

	public void setBanner(RotatingBanner banner) {
		this.banner = banner;
	}
	
	

}

// $Id$