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
package com.hp.sdf.ngp.api.impl.model;

import java.util.Date;

import com.hp.sdf.ngp.model.AssetLifecycleActionHistory;

public class AssetLifecycleActionHistoryImpl implements com.hp.sdf.ngp.api.model.AssetLifecycleActionHistory {
	
//	private final static Log log = LogFactory.getLog(AssetLifecycleActionHistoryImpl.class);
	
	public AssetLifecycleActionHistory getAssetLifecycleActionHistory() {
		if(null == assetLifecycleActionHistory){
			assetLifecycleActionHistory = new AssetLifecycleActionHistory();
		}
		return assetLifecycleActionHistory;
	}

//	public void setPostStatus(Status postStatus) {
//		this.postStatus = postStatus;
//	}
//
//	public void setPreStatus(Status preStatus) {
//		this.preStatus = preStatus;
//	}
//
//
//	public void setAsset(Asset asset) {
//		this.asset = asset;
//	}
//
//	public void setAssetBinaryVersion(AssetBinaryVersion assetBinaryVersion) {
//		this.assetBinaryVersion = assetBinaryVersion;
//	}

	private AssetLifecycleActionHistory assetLifecycleActionHistory;
	
//	private Asset asset;
//	
//	private AssetBinaryVersion assetBinaryVersion;
//	
//	private Status postStatus;
//	
//	private Status preStatus;
	
//	private String version;
	
	public AssetLifecycleActionHistoryImpl(){
		assetLifecycleActionHistory = new AssetLifecycleActionHistory();
	}
	
	public AssetLifecycleActionHistoryImpl(AssetLifecycleActionHistory assetLifecycleActionHistory){
		this.assetLifecycleActionHistory = assetLifecycleActionHistory;
		if (null != assetLifecycleActionHistory) {
			this.assetLifecycleActionHistory.getComments();//load information to avoid the lazy load
		}
	}

	public Long getAssetBinaryVersionId() {
		return assetLifecycleActionHistory.getAssetBinaryVersionId();
	}

	public Long getAssetId() {
		return assetLifecycleActionHistory.getAssetId();
	}

	public String getCommentType() {
		return assetLifecycleActionHistory.getCommentType();
	}

	public String getComments() {
		return assetLifecycleActionHistory.getComments();
	}

	public Date getCompleteDate() {
		return assetLifecycleActionHistory.getCompleteDate();
	}

	public Date getCreateDate() {
		return assetLifecycleActionHistory.getCreateDate();
	}

	public String getDescription() {
		return assetLifecycleActionHistory.getDescription();
	}

	public String getEvent() {
		return assetLifecycleActionHistory.getDescription();
	}

	public Long getId() {
		return assetLifecycleActionHistory.getId();
	}

	public Date getNotificationDate() {
		return assetLifecycleActionHistory.getNotificationDate();
	}

	public String getOwnerid() {
		return assetLifecycleActionHistory.getOwnerId();
	}

	public String getPostStatus() {
		return assetLifecycleActionHistory.getPostStatus();
	}

	public String getPreStatus() {
		return assetLifecycleActionHistory.getPrestatus();
	}

	public String getProcessStatus() {
		return assetLifecycleActionHistory.getProcessStatus();
	}

	public String getResult() {
		return assetLifecycleActionHistory.getResult();
	}

	public String getSubmitterid() {
		return assetLifecycleActionHistory.getSubmitterId();
	}

	public void setAssetBinaryVersionId(Long id) {
//		log.debug("assetBinaryVersionId:" + id);
		assetLifecycleActionHistory.setAssetBinaryVersionId(id);
	}

	public void setAssetId(Long id) {
//		log.debug("assetId:" + id);
		assetLifecycleActionHistory.setAssetId(id);
	}

	public void setCommentType(String commentType) {
//		log.debug("commentType:"+commentType);
		assetLifecycleActionHistory.setCommentType(commentType);
	}

	public void setComments(String comments) {
//		log.debug("comments:"+comments);
		assetLifecycleActionHistory.setComments(comments);
	}

	public void setCompleteDate(Date completeDate) {
//		log.debug("completeDate:" + completeDate);
		assetLifecycleActionHistory.setCompleteDate(completeDate);
	}

	public void setCreateDate(Date createDate) {
//		log.debug("createDate:" + createDate);
		assetLifecycleActionHistory.setCreateDate(createDate);
	}

	public void setDescription(String description) {
//		log.debug("description:"+description);
		assetLifecycleActionHistory.setDescription(description);
	}

	public void setEvent(String event) {
//		log.debug("event:"+event);
		assetLifecycleActionHistory.setEvent(event);
	}

	public void setId(Long id) {
//		log.debug("id:" + id);
		assetLifecycleActionHistory.setId(id);
	}

	public void setNotificationDate(Date notificationDate) {
//		log.debug("notificationDate:"+notificationDate);
		assetLifecycleActionHistory.setNotificationDate(notificationDate);
	}

	public void setOwnerid(String ownerid) {
//		log.debug("ownerid:"+ownerid);
		assetLifecycleActionHistory.setOwnerId(ownerid);
	}

	public void setPostStatus(String postStatus) {
//		log.debug("postStatus:"+postStatus);
		if(null != postStatus){
			assetLifecycleActionHistory.setPostStatus(postStatus);
		}
	}

	public void setPreStatus(String preStatus) {
//		log.debug("preStatus:"+preStatus);
		if(null != preStatus){
			assetLifecycleActionHistory.setPrestatus(preStatus);
		}
	}

	public void setProcessStatus(String processStatus) {
//		log.debug("processStatus:"+processStatus);
		assetLifecycleActionHistory.setProcessStatus(processStatus);
	}

	public void setResult(String result) {
//		log.debug("result:"+result);
		assetLifecycleActionHistory.setResult(result);
	}

	public void setSubmitterid(String submitterid) {
//		log.debug("submitterid:"+submitterid);
		assetLifecycleActionHistory.setSubmitterId(submitterid);
	}

	public String getExternalId() {
		return assetLifecycleActionHistory.getExternalId();
	}

	public String getSource() {
		return assetLifecycleActionHistory.getSource();
	}

	public void setExternalId(String externalId) {
//		log.debug("externalId:"+externalId);
		assetLifecycleActionHistory.setExternalId(externalId);
	}

	public void setSource(String source) {
//		log.debug("source:"+source);
		assetLifecycleActionHistory.setSource(source);
	}

	public String getVersion() {
		return assetLifecycleActionHistory.getVersion();
	}

	public void setVersion(String version) {
//		log.debug("version:"+version);
		assetLifecycleActionHistory.setVersion(version);
	}
	
	@Override
	public String toString(){
		return "AssetLIfecycleActionHistory[assetBinaryVersionId=" + getAssetBinaryVersionId() + ",assetId="+getAssetId()+",commentType="+getCommentType()+"," +
				"comments="+getComments()+",completeDate="+getCompleteDate()+",createDate=" + getCreateDate() + ",description="+getDescription()+",event="+getEvent()
				+",id="+getId()+",notificationDate="+getNotificationDate()+",ownerid="+ getOwnerid() +",postStatus="+getPostStatus()+",preStatus="+getPreStatus()
				+",processStatus="+getProcessStatus()+",result="+getResult()+",submitterid="+getSubmitterid()+",externalId=" + getExternalId()+ 
				",source="+getSource()+",version="+getVersion()+"]";
	}
}

// $Id$