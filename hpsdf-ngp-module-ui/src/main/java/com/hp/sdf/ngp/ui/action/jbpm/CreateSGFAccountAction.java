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
import com.hp.sdf.ngp.manager.UserManager;
import com.hp.sdf.ngp.model.UserLifecycleAction;
import com.hp.sdf.ngp.model.UserLifecycleActionHistory;
import com.hp.sdf.ngp.search.condition.userlifecycleaction.UserLifecycleActionIdCondition;
import com.hp.sdf.ngp.search.engine.SearchExpressionImpl;
import com.hp.sdf.ngp.service.InfoService;
import com.hp.sdf.ngp.workflow.jbpm.BaseAction;
import com.hp.sdf.ngp.workflow.jbpm.PromotionRequest;

@Component
@Scope("prototype")
public class CreateSGFAccountAction extends BaseAction implements ActivityBehaviour {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Resource
	private ApplicationManager applicationManager;

	@Resource
	private UserManager userManager;

	@Resource
	private InfoService infoService;
	private final static Log log = LogFactory.getLog(CreateSGFAccountAction.class);

	@Override
	protected void setDesc() {
		this.desc = "createSgfAccount";
	}

	public void execute(ActivityExecution execution) throws Exception {
		log.debug("execute by:" + execution.getId());
		PromotionRequest req = (PromotionRequest) execution.getVariable("request");
		userManager.onboard(req.getUserId());
		UserLifecycleAction cycle;
		if (null != execution.getVariable("cycleId")) {

			Long cycleActionId = Long.valueOf(execution.getVariable("cycleId").toString());
			SearchExpression searchExpression = new SearchExpressionImpl();
			searchExpression.addCondition(new UserLifecycleActionIdCondition(cycleActionId, NumberComparer.EQUAL));
			List<UserLifecycleAction> userLifecycleAction = infoService.getUserLifecycleAction(searchExpression);
			if (userLifecycleAction.size() < 1)
				cycle = new UserLifecycleAction();
			else
				cycle = userLifecycleAction.get(0);

			if (logNeeded) {
				UserLifecycleActionHistory history = applicationManager.genActionHistoryFromAction(cycle);
				history.setEvent(UserLifecycleConstants.USERPROMOTION_EVENT_SGFACCOUNT);
				infoService.saveUserLifecycleActionHistory(history);
			}
		}

	}
}

// $Id$