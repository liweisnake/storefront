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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.hp.sdf.ngp.banner.BannerType;
import com.hp.sdf.ngp.banner.ContentType;

@Entity
@Table
public class Content implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Long id;
	private String name;
	private ContentType contentType;
	private String path;
	private Long referenceId;
	private BannerType bannerType;
	private byte[] binary;
	private String fileName;
	private String href;
	
	public Content(){
		
	}
	
	public Content(String name, ContentType type, byte[] binary, String fileName, String href){
		this.name = name;
		this.contentType = type;
		this.binary = binary;
		this.fileName = fileName;
		this.href = href;
	}
	
	public boolean equals(Object object){
		if(object == this)
			return true;
		if(!(object instanceof Content))
			return false;
		Content content = (Content)object;
		if(content.getId() == null || this.getId() == null)
			return false;
		return content.getId().equals(this.getId());
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

	@Column(nullable = false)
	public ContentType getContentType() {
		return contentType;
	}

	public void setContentType(ContentType contentType) {
		this.contentType = contentType;
	}

	@Column(nullable = false,unique = true, length = 255)
	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	@Column(nullable = false)
	public Long getReferenceId() {
		return referenceId;
	}

	public void setReferenceId(Long referenceId) {
		this.referenceId = referenceId;
	}

	@Column(nullable = false)
	public BannerType getBannerType() {
		return bannerType;
	}

	public void setBannerType(BannerType bannerType) {
		this.bannerType = bannerType;
	}

	@Transient
	public byte[] getBinary() {
		return binary;
	}

	public void setBinary(byte[] binary) {
		this.binary = binary;
	}

	@Transient
	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	@Column(length = 225)
	public String getHref() {
		return href;
	}

	public void setHref(String href) {
		this.href = href;
	}
}

// $Id$