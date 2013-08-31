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
import com.hp.sdf.ngp.common.constant.AssetLifecycleConstants;
import com.hp.sdf.ngp.common.constant.UserLifecycleConstants;
import com.hp.sdf.ngp.manager.ApplicationManager;
import com.hp.sdf.ngp.model.AssetLifecycleAction;
import com.hp.sdf.ngp.model.AssetLifecycleActionHistory;
import com.hp.sdf.ngp.model.UserLifecycleAction;
import com.hp.sdf.ngp.model.UserLifecycleActionHistory;
import com.hp.sdf.ngp.search.condition.userlifecycleaction.UserLifecycleActionIdCondition;
import com.hp.sdf.ngp.search.engine.SearchExpressionImpl;
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
public class LifeCycleFinishAction extends BaseAction implements ActivityBehaviour {

	private static final long serialVersionUID = 1L;
	private static final Log log = LogFactory.getLog(LifeCycleFinishAction.class);

	@Resource
	private ApplicationService applicationService;

	@Resource
	private InfoService infoService;

	@Resource
	private ApplicationManager applicationManager;

	public void execute(ActivityExecution execution) throws Exception {
		log.debug("called by:" + execution.getId());

		if (logNeeded) {

			if (null != execution.getVariable("cycleActionId")) {
				Long cycleActionId = Long.valueOf(execution.getVariable(
						"cycleActionId").toString());
				AssetLifecycleAction cycleAction = applicationService
						.getAssetLifecycleActionById(cycleActionId);

				// upd his
				AssetLifecycleActionHistory history = applicationManager
						.genActionHistoryFromAction(cycleAction);
				history
						.setEvent(AssetLifecycleConstants.USERPROMOTION_EVENT_FINISH);
				history.setResult(AssetLifecycleConstants.BIN_TEST_RESULT_PASS);
				applicationService
						.saveOrUpdateAssetLifecycleActionHistory(history);

				// del cycleAction
				applicationService
						.deleteAssetLifecycleActionById(cycleActionId);
			}

			if (null != execution.getVariable("cycleId")) {
				Long cycleId = (Long) execution.getVariable("cycleId");
				UserLifecycleAction cycle;
				SearchExpression searchExpression = new SearchExpressionImpl();
				searchExpression
						.addCondition(new UserLifecycleActionIdCondition(
								cycleId, NumberComparer.EQUAL));
				List<UserLifecycleAction> userLifecycleAction = infoService
						.getUserLifecycleAction(searchExpression);
				if (userLifecycleAction.size() < 1)
					cycle = new UserLifecycleAction();
				else
					cycle = userLifecycleAction.get(0);

				// upd his
				UserLifecycleActionHistory history = applicationManager
						.genActionHistoryFromAction(cycle);
				history
						.setEvent(UserLifecycleConstants.USERPROMOTION_EVENT_FINISH);
				history
						.setResult(UserLifecycleConstants.USERPROMOTION_RESULT_PASS);
				infoService.saveUserLifecycleActionHistory(history);

				// del cycle
				infoService.removeUserLifecycleAction(cycleId);
			}

		}
	}

	@PostConstruct
	@Override
	protected void setDesc() {
		this.desc = "lifecycleEnd";
	}

	public InfoService getInfoService() {
		return infoService;
	}

	public void setInfoService(InfoService infoService) {
		this.infoService = infoService;
	}
}

// $Id$