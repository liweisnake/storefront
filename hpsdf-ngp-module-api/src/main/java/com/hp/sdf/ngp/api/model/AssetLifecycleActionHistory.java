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
package com.hp.sdf.ngp.api.model;

import java.util.Date;

public interface AssetLifecycleActionHistory {

	public Long getId();
	
	public void setId(Long id);
	
	public Long getAssetId();
	
	public void setAssetId(Long id);
	
	public Long getAssetBinaryVersionId();
	
	public void setAssetBinaryVersionId(Long id);
	
	public Date getCreateDate();
	
	public void setCreateDate(Date createDate);
	
	public Date getCompleteDate();
	
	public void setCompleteDate(Date completeDate);
	
	public String getPreStatus() ;
	
	public void setPreStatus(String preStatus);
	
	public String getPostStatus();
	
	public void setPostStatus(String postStatus);
	
	public String getEvent();
	
	public void setEvent(String event);
	
	public String getResult();
	
	public void setResult(String result);
	
	public String getDescription();
	
	public void setDescription(String description);
	
	public String getComments();
	
	public void setComments(String comments);
	
	public String getSubmitterid();
	
	public void setSubmitterid(String submitterid);
	
	public String getOwnerid();
	
	public void setOwnerid(String ownerid);
	
	public String getProcessStatus();
	
	public void setProcessStatus(String processStatus);
	
	public String getCommentType() ;
	
	public void setCommentType(String commentType);
	
	public Date getNotificationDate();
	
	public void setNotificationDate(Date notificationDate);
	
	public String getExternalId();
	
	public void setExternalId(String externalId);
	
	public String getSource();
	
	public void setSource(String source);
	
	public String getVersion();
	
	public void setVersion(String version);
}

// $Id$