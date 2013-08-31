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

import com.hp.sdf.ngp.model.AssetBinaryVersion;
import com.hp.sdf.ngp.model.AssetLifecycleAction;
import com.hp.sdf.ngp.service.ApplicationService;
import com.hp.sdf.ngp.workflow.jbpm.BaseAction;

/**
 * sync status of binary according to cycle
 * 
 * @param postStatus
 *            if not provided ,sync to status of cycle->getPostStatus()
 */
@Component
@Scope("prototype")
public class BinaryStatusChangeAction extends BaseAction implements
		ActivityBehaviour {

	private static final long serialVersionUID = 1L;
	private static final Log log = LogFactory
			.getLog(BinaryStatusChangeAction.class);

	private String postStatus = "";

	@Resource
	private ApplicationService applicationService;

	public void execute(ActivityExecution execution) throws Exception {
		log.debug("called by:" + execution.getId());

		if (passThisState)
			return;

		if (execution.getVariable("cycleActionId") == null) {
			log.error("null cycleActionId,end this processInstance:"
					+ execution.getId());
			execution.end();
		}

		Long cycleActionId = Long.valueOf(execution
				.getVariable("cycleActionId").toString());
		AssetLifecycleAction cycleAction = applicationService
				.getAssetLifecycleActionById(cycleActionId);

		Long binId = Long.valueOf(execution.getVariable("binId").toString());
		AssetBinaryVersion bin = applicationService.getAssetBinaryById(binId);
		if (!postStatus.equals("")) {
			bin.setStatus(applicationService.getStatusByName(postStatus));
		} else {
			bin.setStatus(cycleAction.getPostStatus());
		}
		applicationService.updateBinaryVersion(bin);
	}

	@PostConstruct
	@Override
	protected void setDesc() {
		this.desc = "binaryStatusUpdate";
	}

}

// $Id$