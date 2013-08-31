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

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


@Entity
@Table
public class AssetLifecycleAction implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4162211141954746700L;

	private Long id;
	private Asset asset;
	private AssetBinaryVersion binaryVersion;
	private Date createDate = new Date();
	private Date completeDate;
	private Status preStatus;
	private Status postStatus;
	private String event;
	private String result;
	private String description;
	private String comments;
	private String submitterid;
	private String ownerid;
	private String processStatus;
	private String commentType;
	private Date   notificationDate;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ASSETID", nullable = false)
	public Asset getAsset() {
		return this.asset;
	}

	public void setAsset(Asset asset) {
		this.asset = asset;
	}
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column
	public Date getCompleteDate() {
		return completeDate;
	}

	public void setCompleteDate(Date completeDate) {
		this.completeDate = completeDate;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column
	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	@Column(length = 200)
	public String getEvent() {
		return event;
	}

	public void setEvent(String event) {
		this.event = event;
	}

	@Column(length = 200)
	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	@Column(length = 512)
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Column(length = 512)
	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name="PRESTATUSID")
	public Status getPreStatus() {
		return preStatus;
	}

	public void setPreStatus(Status preStatus) {
		this.preStatus = preStatus;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name="POSTSTATUSID")
	public Status getPostStatus() {
		return postStatus;
	}

	public void setPostStatus(Status postStatus) {
		this.postStatus = postStatus;
	}

	@Column(length = 512, nullable=false)
	public String getSubmitterid() {
		return submitterid;
	}

	public void setSubmitterid(String submitterid) {
		this.submitterid = submitterid;
	}

	@Column(length = 512, nullable=false)
	public String getOwnerid() {
		return ownerid;
	}

	public void setOwnerid(String ownerid) {
		this.ownerid = ownerid;
	}

	@Column(length = 512)
	public String getProcessStatus() {
		return processStatus;
	}

	public void setProcessStatus(String processStatus) {
		this.processStatus = processStatus;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ASSETBINARYVERSIONID")
	public AssetBinaryVersion getBinaryVersion() {
		return binaryVersion;
	}

	public void setBinaryVersion(AssetBinaryVersion binaryVersion) {
		this.binaryVersion = binaryVersion;
	}

	@Column
	public String getCommentType() {
		return commentType;
	}

	public void setCommentType(String commentType) {
		this.commentType = commentType;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column
	public Date getNotificationDate() {
		return notificationDate;
	}

	public void setNotificationDate(Date notificationDate) {
		this.notificationDate = notificationDate;
	}
	
	
	
	
}

// $Id$