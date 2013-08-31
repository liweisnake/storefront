package com.hp.sdf.ngp.workflow.jbpm;

import java.io.Serializable;
import java.util.Date;

/**
 * function as parameter of user promotion flow
 * 
 * @author zhoude
 * 
 */
public class PromotionRequest implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * who take the responsibility to approve the request<br>
	 * for separating different type of tasks for the same person,format content<br>
	 * like {managerName_rquestType}<br>
	 * yulin_userPromotion<br>
	 * yulin_appPromotion
	 */
	private String manager;

	/**
	 * if need manger's Approve[means create task]
	 */
	private Boolean isWaitingForApprove = true;

	public Boolean getIsWaitingForApprove() {
		return isWaitingForApprove;
	}

	public void setIsWaitingForApprove(Boolean isWaitingForApprove) {
		this.isWaitingForApprove = isWaitingForApprove;
	}

	/**
	 * who initiate the promotion request
	 */
	private String userId;

	/**
	 * request Date
	 */
	private Date requestDate = new Date();

	/**
	 * trigger type of this flow
	 * 
	 * invite || apply || assign
	 */
	private String trigger;

	/**
	 * target role of this promotion Request tester or developer
	 */
	private String targetRole;
	
	/**
	 * holder for comment inputed in applyForm 
	 */
	private String comment;
	
	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getTargetRole() {
		return targetRole;
	}

	public void setTargetRole(String targetRole) {
		this.targetRole = targetRole;
	}

	public PromotionRequest() {

	}

	public String getTrigger() {
		return trigger;
	}

	public void setTrigger(String trigger) {
		this.trigger = trigger;
	}

	public String getManager() {
		return manager;
	}

	public void setManager(String manager) {
		this.manager = manager;
	}

	public Date getRequestDate() {
		return requestDate;
	}

	public void setRequestDate(Date requestDate) {
		this.requestDate = requestDate;
	}

}
