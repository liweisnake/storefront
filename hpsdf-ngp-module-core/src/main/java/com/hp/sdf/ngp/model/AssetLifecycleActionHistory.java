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

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


@Entity
@Table
public class AssetLifecycleActionHistory  implements Serializable {
	private static final long serialVersionUID = -3332222231953336702L;
	
	private Long id;
	private String commentType;
	private String comments;
	private Date createDate;
	private Date completeDate;
	private String event;
	private String result;
	private String submitterId;
	private String ownerId;
	private String processStatus;
	private String description;
	private Long assetBinaryVersionId;
	private String postStatus;
	private Long assetId;
	private String prestatus;
	private Date notificationDate;
	private String externalId;
	private String source;
	private String version;
	
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
	public String getComments() {
		return comments;
	}
	
	public void setComments(String comments) {
		this.comments = comments;
	}
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column
	public Date getCreateDate() {
		return createDate;
	}
	
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column
	public Date getCompleteDate() {
		return completeDate;
	}
	
	
	public void setCompleteDate(Date completeDate) {
		this.completeDate = completeDate;
	}
	
	@Column
	public String getEvent() {
		return event;
	}
	
	public void setEvent(String event) {
		this.event = event;
	}
	
	@Column
	public String getResult() {
		return result;
	}
	
	public void setResult(String result) {
		this.result = result;
	}
	
	@Column
	public String getSubmitterId() {
		return submitterId;
	}
	
	public void setSubmitterId(String submitterId) {
		this.submitterId = submitterId;
	}
	
	@Column
	public String getOwnerId() {
		return ownerId;
	}
	
	public void setOwnerId(String ownerId) {
		this.ownerId = ownerId;
	}
	
	@Column
	public String getProcessStatus() {
		return processStatus;
	}
	
	public void setProcessStatus(String processStatus) {
		this.processStatus = processStatus;
	}
	
	@Column(length = 512)
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	@Column
	public Long getAssetBinaryVersionId() {
		return assetBinaryVersionId;
	}
	
	public void setAssetBinaryVersionId(Long assetBinaryVersionId) {
		this.assetBinaryVersionId = assetBinaryVersionId;
	}
	
	@Column
	public String getPostStatus() {
		return postStatus;
	}
	
	public void setPostStatus(String postStatus) {
		this.postStatus = postStatus;
	}
	
	@Column
	public Long getAssetId() {
		return assetId;
	}
	
	public void setAssetId(Long assetId) {
		this.assetId = assetId;
	}
	
	@Column
	public String getPrestatus() {
		return prestatus;
	}
	
	public void setPrestatus(String prestatus) {
		this.prestatus = prestatus;
	}
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column
	public Date getNotificationDate() {
		return notificationDate;
	}
	
	public void setNotificationDate(Date notificationDate) {
		this.notificationDate = notificationDate;
	}
	
	@Column
	public String getExternalId() {
		return externalId;
	}
	
	public void setExternalId(String externalId) {
		this.externalId = externalId;
	}
	
	@Column
	public String getSource() {
		return source;
	}
	
	public void setSource(String source) {
		this.source = source;
	}
	
	@Column
	public String getVersion() {
		return version;
	}
	
	public void setVersion(String version) {
		this.version = version;
	}
	
	
	
	
}

// $Id$