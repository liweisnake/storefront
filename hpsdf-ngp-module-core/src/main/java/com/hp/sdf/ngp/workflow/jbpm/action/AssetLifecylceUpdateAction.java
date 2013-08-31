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
package com.hp.sdf.ngp.workflow.jbpm.action;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jbpm.api.activity.ActivityBehaviour;
import org.jbpm.api.activity.ActivityExecution;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.hp.sdf.ngp.common.constant.AssetLifecycleConstants;
import com.hp.sdf.ngp.manager.ApplicationManager;
import com.hp.sdf.ngp.model.AssetLifecycleAction;
import com.hp.sdf.ngp.model.AssetLifecycleActionHistory;
import com.hp.sdf.ngp.service.ApplicationService;
import com.hp.sdf.ngp.service.InfoService;
import com.hp.sdf.ngp.workflow.jbpm.BaseAction;

/**
 * update AssetLifecylce:<br>
 * 1,processStatus;<br>
 * 2,preStatus->postStatus and new postStatus
 * 
 * @param postStatus
 *            [must be the value to RolesLifeCycleAction->postStatus],default
 *            <b>verified</b>
 */
@Component
@Scope("prototype")
public class AssetLifecylceUpdateAction extends BaseAction implements
		ActivityBehaviour {

	private static final long serialVersionUID = 1L;
	private static final Log log = LogFactory
			.getLog(AssetLifecylceUpdateAction.class);
	private String preStatus = "";
	private String postStatus = "";
	private String processStatus = "";

	@Resource
	private ApplicationService applicationService;

	public String getPreStatus() {
		return preStatus;
	}

	public void setPreStatus(String preStatus) {
		this.preStatus = preStatus;
	}

	public String getPostStatus() {
		return postStatus;
	}

	public void setPostStatus(String postStatus) {
		this.postStatus = postStatus;
	}

	@Resource
	private InfoService infoService;

	@Resource
	private ApplicationManager applicationManager;

	public void execute(ActivityExecution execution) throws Exception {
		log.debug("called by:" + execution.getId());

		if (passThisState)
			return;

		Long cycleActionId = Long.valueOf(execution
				.getVariable("cycleActionId").toString());
		AssetLifecycleAction cycleAction = applicationService
				.getAssetLifecycleActionById(cycleActionId);

		if (!this.processStatus.equals("")) {
			cycleAction.setProcessStatus(processStatus);
		}

		if (!this.postStatus.equals("")) {
			if (cycleAction.getPostStatus() != null)
				cycleAction.setPreStatus(cycleAction.getPostStatus());
			else if (!this.preStatus.equals(""))
				cycleAction.setPreStatus(applicationService
						.getStatusByName(preStatus));

			cycleAction.setPostStatus(applicationService
					.getStatusByName(postStatus));
		}

		if (logNeeded) {
			AssetLifecycleActionHistory history = applicationManager
					.genActionHistoryFromAction(cycleAction);
			history.setResult(AssetLifecycleConstants.BIN_TEST_RESULT_PASS);
			applicationService.saveOrUpdateAssetLifecycleActionHistory(history);
		}

		applicationService.saveOrUpdateAssetLifecycleAction(cycleAction);

	}

	@PostConstruct
	@Override
	protected void setDesc() {
		this.desc = "lifecycle-update";
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