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
package com.hp.sdf.ngp.ui.action.jbpm;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jbpm.api.activity.ActivityBehaviour;
import org.jbpm.api.activity.ActivityExecution;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.hp.sdf.ngp.api.search.SearchExpression;
import com.hp.sdf.ngp.api.search.Condition.NumberComparer;
import com.hp.sdf.ngp.common.constant.UserLifecycleConstants;
import com.hp.sdf.ngp.manager.ApplicationManager;
import com.hp.sdf.ngp.model.UserLifecycleAction;
import com.hp.sdf.ngp.model.UserLifecycleActionHistory;
import com.hp.sdf.ngp.search.condition.userlifecycleaction.UserLifecycleActionIdCondition;
import com.hp.sdf.ngp.search.engine.SearchExpressionImpl;
import com.hp.sdf.ngp.service.InfoService;
import com.hp.sdf.ngp.ui.WicketSession;
import com.hp.sdf.ngp.workflow.jbpm.BaseAction;
import com.hp.sdf.ngp.workflow.jbpm.PromotionRequest;

/**
 * user apply finished and update PsotSstatus of userLifeCycle
 * 
 * @param postStatus
 *            [must be the value to RolesLifeCycleAction->postStatus],default
 *            <b>verified</b>
 */
@Component
@Scope("prototype")
public class UserLifecylceUpdateAction extends BaseAction implements ActivityBehaviour {

	private static final long serialVersionUID = 1L;
	private static final Log log = LogFactory.getLog(UserLifecylceUpdateAction.class);

	private String preRole = "";

	private String postRole = "";

	private String submitterid = "";

	private String ownerid = "";

	private String processStatus = "";

	@Resource
	private ApplicationManager applicationManager;

	@Resource
	private InfoService infoService;

	public void execute(ActivityExecution execution) throws Exception {
		log.debug("called by:" + execution.getId());
		final PromotionRequest req = (PromotionRequest) execution.getVariable("request");
		if (passThisRole(req.getTargetRole()))
			return;

		if (passThisState)
			return;

		UserLifecycleAction cycle;
		if (null == execution.getVariable("cycleId")) {
			cycle = new UserLifecycleAction();

			cycle.setCreateDate(req.getRequestDate());
			cycle.setEvent(req.getTrigger());
			cycle.setComments(req.getComment());
			cycle.setPostRole(req.getTargetRole());
			cycle.setSubmitterid(WicketSession.get().getUserId());
			cycle.setUserid(req.getUserId());
			if (null != execution.getVariable("userRoles"))
				cycle.setPreRole("" + execution.getVariable("userRoles"));
		} else {
			Long cycleActionId = Long.valueOf(execution.getVariable("cycleId").toString());
			SearchExpression searchExpression = new SearchExpressionImpl();
			searchExpression.addCondition(new UserLifecycleActionIdCondition(cycleActionId, NumberComparer.EQUAL));
			List<UserLifecycleAction> userLifecycleAction = infoService.getUserLifecycleAction(searchExpression);
			if (userLifecycleAction.size() < 1)
				cycle = new UserLifecycleAction();
			else
				cycle = userLifecycleAction.get(0);
		}

		if (!this.processStatus.equals("")) {
			cycle.setProcessStatus(processStatus);
		}

		if (!this.ownerid.equals("")) {
			cycle.setOwnerid(ownerid);
		}

		if (!this.submitterid.equals("")) {
			cycle.setSubmitterid(submitterid);
		}

		if (!this.postRole.equals("")) {
			cycle.setPostRole(postRole);
		}

		if (!this.preRole.equals("")) {
			cycle.setPreRole(preRole);
		}

		infoService.saveOrUpdateUserLifecycleAction(cycle);

		if (logNeeded) {
			UserLifecycleActionHistory history = applicationManager.genActionHistoryFromAction(cycle);
			history.setEvent(UserLifecycleConstants.USERPROMOTION_EVENT_SGFACCOUNT);
			history.setResult(UserLifecycleConstants.USERPROMOTION_RESULT_PASS);
			infoService.saveUserLifecycleActionHistory(history);
		}

		if (null == execution.getVariable("cycleId")) {
			execution.setVariable("cycleId", cycle.getId());
		}
	}

	@PostConstruct
	@Override
	protected void setDesc() {
		this.desc = "statusChange";
	}

	public String getPreRole() {
		return preRole;
	}

	public void setPreRole(String preRole) {
		this.preRole = preRole;
	}

	public String getPostRole() {
		return postRole;
	}

	public void setPostRole(String postRole) {
		this.postRole = postRole;
	}

	public String getSubmitterid() {
		return submitterid;
	}

	public void setSubmitterid(String submitterid) {
		this.submitterid = submitterid;
	}

	public String getOwnerid() {
		return ownerid;
	}

	public void setOwnerid(String ownerid) {
		this.ownerid = ownerid;
	}

	public String getProcessStatus() {
		return processStatus;
	}

	public void setProcessStatus(String processStatus) {
		this.processStatus = processStatus;
	}

	public InfoService getInfoService() {
		return infoService;
	}

	public void setInfoService(InfoService infoService) {
		this.infoService = infoService;
	}
}

// $Id$