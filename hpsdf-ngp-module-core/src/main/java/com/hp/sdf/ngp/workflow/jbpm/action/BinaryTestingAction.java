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

import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.apache.axis.utils.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jbpm.api.activity.ActivityExecution;
import org.jbpm.api.activity.ExternalActivityBehaviour;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.hp.sdf.ngp.api.search.SearchExpression;
import com.hp.sdf.ngp.api.search.Condition.NumberComparer;
import com.hp.sdf.ngp.api.search.Condition.StringComparer;
import com.hp.sdf.ngp.common.constant.AssetLifecycleConstants;
import com.hp.sdf.ngp.manager.ApplicationManager;
import com.hp.sdf.ngp.model.AssetBinaryVersion;
import com.hp.sdf.ngp.model.AssetLifecycleAction;
import com.hp.sdf.ngp.search.condition.assetlifecycleaction.AssetLifecycleActionAssetBinaryVersionIdCondition;
import com.hp.sdf.ngp.search.condition.assetlifecycleaction.AssetLifecycleActionEventCondition;
import com.hp.sdf.ngp.search.condition.assetlifecycleaction.AssetLifecycleActionResultCondition;
import com.hp.sdf.ngp.search.engine.SearchExpressionImpl;
import com.hp.sdf.ngp.service.ApplicationService;
import com.hp.sdf.ngp.workflow.jbpm.BaseAction;

@Component
@Scope("prototype")
public class BinaryTestingAction extends BaseAction implements
		ExternalActivityBehaviour {

	private static final long serialVersionUID = 1L;
	private static final Log log = LogFactory.getLog(BinaryTestingAction.class);
	private String postStatus = "";

	public String getPostStatus() {
		return postStatus;
	}

	public void setPostStatus(String postStatus) {
		this.postStatus = postStatus;
	}

	@Resource
	private ApplicationManager applicationManager;

	@Resource
	private ApplicationService applicationService;

	public void signal(ActivityExecution execution, String signalName,
			Map<String, ?> parameters) throws Exception {
		// String nextStep = (String) parameters.get("nextStep");

		log.debug("signaled" + execution.getId() + " by:" + signalName);

		// clean testing action requests
		if (parameters.get("testResult") != null) {
			execution.setVariable("testResult", parameters.get("testResult"));
		}

		Long binId = Long.valueOf(execution.getVariable("binId").toString());
		SearchExpression expression = new SearchExpressionImpl();
		expression
				.addCondition(new AssetLifecycleActionAssetBinaryVersionIdCondition(
						binId, NumberComparer.EQUAL));
		expression.addCondition(new AssetLifecycleActionEventCondition(
				AssetLifecycleConstants.BIN_ACTION_TYPE_TEST,
				StringComparer.EQUAL, false, false));
		expression.addCondition(new AssetLifecycleActionResultCondition(
				AssetLifecycleConstants.BIN_TEST_RESULT_TESTING,
				StringComparer.EQUAL, false, false));

		List<AssetLifecycleAction> requests = applicationService
				.getAssetLifecycleAction(expression);
		if (requests != null) {
			for (AssetLifecycleAction request : requests) {

				if (logNeeded) {
					applicationService
							.saveOrUpdateAssetLifecycleActionHistory(applicationManager
									.genActionHistoryFromAction(request));
				}

				applicationService.deleteAssetLifecycleActionById(request
						.getId());
			}
		}

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

		// update cycle status
		if (!postStatus.equals("")) {

			if (cycleAction.getPostStatus() != null)
				cycleAction.setPreStatus(cycleAction.getPostStatus());

			cycleAction.setPostStatus(applicationService
					.getStatusByName(postStatus));

			Long binId = Long
					.valueOf(execution.getVariable("binId").toString());
			AssetBinaryVersion bin = applicationService
					.getAssetBinaryById(binId);
			bin.setStatus(applicationService.getStatusByName(postStatus));
			applicationService.updateBinaryVersion(bin);
		}

		cycleAction
				.setEvent(AssetLifecycleConstants.BIN_ACTION_TYPE_REQUEST_TEST);
		applicationService.saveOrUpdateAssetLifecycleAction(cycleAction);

		execution.waitForSignal();
	}

	@PostConstruct
	@Override
	protected void setDesc() {
		this.desc = "testing";
	}

}

// $Id$