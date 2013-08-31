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

import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.apache.axis.utils.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jbpm.api.activity.ActivityExecution;
import org.jbpm.api.activity.ExternalActivityBehaviour;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.hp.sdf.ngp.common.constant.AssetLifecycleConstants;
import com.hp.sdf.ngp.manager.ApplicationManager;
import com.hp.sdf.ngp.model.AssetLifecycleAction;
import com.hp.sdf.ngp.service.ApplicationService;
import com.hp.sdf.ngp.workflow.jbpm.BaseAction;

@Service
@Scope("prototype")
public class AdminProveAction extends BaseAction implements
		ExternalActivityBehaviour {

	private static final long serialVersionUID = 1L;
	private static final Log log = LogFactory.getLog(AdminProveAction.class);

	@Resource
	private ApplicationManager applicationManager;

	@Resource
	private ApplicationService applicationService;

	public void signal(ActivityExecution execution, String signalName,
			Map<String, ?> parameters) throws Exception {
		log.debug("signaled" + execution.getId() + " by:" + signalName);

		// update request type
		Long cycleActionId = Long.valueOf(execution
				.getVariable("cycleActionId").toString());
		AssetLifecycleAction cycleAction = applicationService
				.getAssetLifecycleActionById(cycleActionId);
		
		// approve success
		cycleAction.setResult(AssetLifecycleConstants.BIN_ACTION_TYPE_APPROVE);

		// save admin approve history
		if (logNeeded) {
			applicationService
					.saveOrUpdateAssetLifecycleActionHistory(applicationManager
							.genActionHistoryFromAction(cycleAction));
		}

		// update to next
		cycleAction.setEvent("");

		applicationService.saveOrUpdateAssetLifecycleAction(cycleAction);

		/**
		 * // update bin Long binId =
		 * Long.valueOf(execution.getVariable("binId").toString());
		 * AssetBinaryVersion bin =
		 * applicationService.getAssetBinaryById(binId);
		 * bin.setStatus(cycleAction.getPostStatus());
		 * applicationService.updateBinaryVersion(bin);
		 **/

		execution.take(signalName);
	}

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

		// scenario: role as super_developer do not need testing
		if (!passRoles.equals("")
				&& (null != execution.getVariable("userRoles"))) {
			String[] userRoles = execution.getVariable("userRoles").toString()
					.split(",");
			for (String role : userRoles) {
				if (!StringUtils.isEmpty(role)) {
					if (passThisRole(role)) {
						return;
					}
				}
			}
		}

		cycleAction
				.setEvent(AssetLifecycleConstants.BIN_ACTION_TYPE_REQUEST_APPROVAL);

		applicationService.saveOrUpdateAssetLifecycleAction(cycleAction);

		execution.waitForSignal();
	}

	@PostConstruct
	@Override
	protected void setDesc() {
		this.desc = "manager approve";
	}

}

// $Id$