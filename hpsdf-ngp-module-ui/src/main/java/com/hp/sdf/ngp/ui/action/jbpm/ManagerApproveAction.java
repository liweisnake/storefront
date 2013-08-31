package com.hp.sdf.ngp.ui.action.jbpm;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.extensions.breadcrumb.IBreadCrumbModel;
import org.apache.wicket.extensions.breadcrumb.panel.BreadCrumbPanel;
import org.apache.wicket.extensions.breadcrumb.panel.IBreadCrumbPanelFactory;
import org.jbpm.api.activity.ActivityExecution;
import org.jbpm.api.activity.ExternalActivityBehaviour;
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
import com.hp.sdf.ngp.ui.page.myportal.BreadCrumbForm;
import com.hp.sdf.ngp.ui.page.myportal.UserPromotionApprovePanel;
import com.hp.sdf.ngp.workflow.jbpm.BaseAction;
import com.hp.sdf.ngp.workflow.jbpm.PromotionRequest;

/**
 * @author jack
 */
@Component
@Scope("prototype")
public class ManagerApproveAction extends BaseAction implements ExternalActivityBehaviour {
	private static final Log log = LogFactory.getLog(ManagerApproveAction.class);
	private static final long serialVersionUID = 1L;

	@Resource
	private InfoService infoService;
	@Resource
	private ApplicationManager applicationManager;

	/*
	 * manual task defined for manager
	 */
	public void execute(final ActivityExecution execution) {
		log.debug("execute by:" + execution.getId());
		final PromotionRequest req = (PromotionRequest) execution.getVariable("request");
		if (passThisRole(req.getTargetRole()))
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

			infoService.saveOrUpdateUserLifecycleAction(cycle);
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
		
		
		
		BreadCrumbForm form = (BreadCrumbForm) execution.getVariable("component");
		if (form != null) {
			form.setIfactory(new IBreadCrumbPanelFactory() {
				public BreadCrumbPanel create(String componentId, IBreadCrumbModel breadCrumbModel) {
					return new UserPromotionApprovePanel(componentId, breadCrumbModel);
				}
			});
		}

		if (null == execution.getVariable("cycleId")) {
			execution.setVariable("cycleId", cycle.getId());
		}

		execution.removeVariable("component");
		execution.waitForSignal();
	}

	public void signal(ActivityExecution execution, String signalName, Map<String, ?> parameters) {
		log.debug("signaled by:" + execution.getId() + ",signal=" + signalName);

		PromotionRequest req = (PromotionRequest) execution.getVariable("request");

		if (passThisRole(req.getTargetRole()))
			return;

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

			cycle.setOwnerid(WicketSession.get().getUserId());

			infoService.saveOrUpdateUserLifecycleAction(cycle);

			if (logNeeded) {
				UserLifecycleActionHistory history = applicationManager.genActionHistoryFromAction(cycle);
				history.setEvent(UserLifecycleConstants.USERPROMOTION_EVENT_APPROVE);
				history.setResult(UserLifecycleConstants.USERPROMOTION_RESULT_PASS);
				infoService.saveUserLifecycleActionHistory(history);
			}
		}

		execution.take(signalName);
	}

	@Override
	protected void setDesc() {
		this.desc = "manager approve";
	}
}
