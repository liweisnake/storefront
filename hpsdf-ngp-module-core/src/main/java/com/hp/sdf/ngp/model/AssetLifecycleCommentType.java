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
package com.hp.sdf.ngp.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table
public class AssetLifecycleCommentType  {
	private static final long serialVersionUID = -1112222241111116702L; 
	
	private Long id;
	private String commentType;
	private String commentTemplate;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	@Column
	public String getCommentType() {
		return commentType;
	}
	
	public void setCommentType(String commentType) {
		this.commentType = commentType;
	}
	
	@Column
	public String getCommentTemplate() {
		return commentTemplate;
	}
	
	public void setCommentTemplate(String commentTemplate) {
		this.commentTemplate = commentTemplate;
	}
	
	
}

// $Id$