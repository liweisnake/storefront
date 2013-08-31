package com.hp.sdf.ngp.ui.action.jbpm;

import java.text.MessageFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.Application;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.model.Model;
import org.jbpm.api.Execution;
import org.jbpm.api.ProcessInstance;
import org.jbpm.api.activity.ActivityBehaviour;
import org.jbpm.api.activity.ActivityExecution;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.hp.sdf.ngp.api.search.SearchExpression;
import com.hp.sdf.ngp.api.search.Condition.NumberComparer;
import com.hp.sdf.ngp.common.constant.UserLifecycleConstants;
import com.hp.sdf.ngp.model.UserLifecycleAction;
import com.hp.sdf.ngp.search.condition.userlifecycleaction.UserLifecycleActionIdCondition;
import com.hp.sdf.ngp.search.engine.SearchExpressionImpl;
import com.hp.sdf.ngp.service.InfoService;
import com.hp.sdf.ngp.ui.WicketApplication;
import com.hp.sdf.ngp.ui.page.myportal.UserInfoPanel;
import com.hp.sdf.ngp.workflow.jbpm.BaseAction;
import com.hp.sdf.ngp.workflow.jbpm.JbpmHelper;
import com.hp.sdf.ngp.workflow.jbpm.JbpmServiceHolder;
import com.hp.sdf.ngp.workflow.jbpm.PromotionRequest;

/**
 * check roles of current User with roles defined in userRole.JPDL.XML,choose UI
 * in button/label/role
 * 
 * @param applyRole
 *            new role can be applied
 *@param preRoles
 *            must condition roles which can transfer to this role
 *@param excepRoles
 *            exceptional roles which can not transfer to this role
 *@param applyFinshedStatus
 *            user apply finished and wait-label displayed[must be the value to
 *            UserLifeCycleAction->getProcessStatus],default <b>applyFinshed</b>
 * 
 */
@Component
@Scope("prototype")
public class RolesLifeCycleAction extends BaseAction implements ActivityBehaviour {

	String applyRole = "";
	String preRoles = "";
	String excepRoles = "";
	String applyFinshedStatus = "applyFinished";
	private static final long serialVersionUID = 1L;
	private static final Log log = LogFactory.getLog(RolesLifeCycleAction.class);

	public void execute(final ActivityExecution execution) {
		log.debug("calldded by:" + execution.getId() + "," + passRoles);

		final String userRoles = execution.getVariable("userRoles").toString();
		final String userId = execution.getVariable("userId").toString();
		String rolePrefix = execution.getVariable("rolePrefix").toString();
		// already have this role
		if (userRoles.indexOf(applyRole) > -1)
			return;

		// pre-condition check
		if (!preRoles.equals("")) {
			boolean preCheck = false;
			for (String userRole : userRoles.split(",")) {
				if (preRoles.indexOf(userRole) > -1) {
					preCheck = true;
					break;
				}
			}
			if (!preCheck)
				return;
		}

		// excepRole check
		if (!excepRoles.equals("")) {
			boolean excepCheck = true;
			for (String userRole : userRoles.split(",")) {
				if (excepRoles.indexOf(userRole) > -1) {
					excepCheck = false;
					break;
				}
			}
			if (!excepCheck)
				return;
		}

		// get label or button
		final UserInfoPanel.UserInfoForm form = (UserInfoPanel.UserInfoForm) execution.getVariable("component");

		if (form != null) {

			Button[] applyBtns = form.getApplyButton();
			Label[] applyLabs = form.getApplyLabel();

			for (int i = 0; i < applyBtns.length; i++) {
				boolean buttoned = true;
				if (applyBtns[i] == null && applyLabs[i] == null) {
					String instanceId = JbpmHelper.genInstanceId("userPromotion", userId, applyRole);
					if (null != JbpmHelper.checkPromotionStatus(instanceId)) {
						ProcessInstance ins = JbpmServiceHolder.executionService.createProcessInstanceQuery().processInstanceKey(instanceId).uniqueResult();

						Long cycleActionId = (Long) JbpmServiceHolder.executionService.getVariable(ins.getId(), "cycleId");
						if (null == cycleActionId) {
							JbpmServiceHolder.executionService.endProcessInstance(ins.getId(),Execution.STATE_ENDED);
						} else {
							InfoService infoService = ((WicketApplication) Application.get()).getInfoService();
							UserLifecycleAction cycle = new UserLifecycleAction();
							SearchExpression searchExpression = new SearchExpressionImpl();
							searchExpression.addCondition(new UserLifecycleActionIdCondition(Long.valueOf(cycleActionId), NumberComparer.EQUAL));
							List<UserLifecycleAction> userLifecycleAction = infoService.getUserLifecycleAction(searchExpression);
							if (userLifecycleAction.size() > 0)
								cycle = userLifecycleAction.get(0);
							else 
								JbpmServiceHolder.executionService.endProcessInstance(ins.getId(),Execution.STATE_ENDED);
							
							if (applyFinshedStatus.equalsIgnoreCase(cycle.getProcessStatus())) {
								buttoned = false;
							}
						}
					
					}

					if (buttoned && !StringUtils.isBlank(applyRole)) {
						log.debug("button displayed.");
						applyBtns[i] = new Button("applyRole" + i) {
							/**
							 * 
							 */
							private static final long serialVersionUID = 1L;

							@Override
							public void onSubmit() {
								Map<String, Object> variables = new HashMap<String, Object>();
								PromotionRequest pr = new PromotionRequest();
								pr.setTrigger(UserLifecycleConstants.USERPROMOTION_EVENT_APPLY);
								pr.setTargetRole(applyRole);
								pr.setUserId(userId);
								pr.setRequestDate(new Date());
								variables.put("request", pr);
								variables.put("userRoles", userRoles);
								variables.put("component", form);
								
								String instanceId = JbpmHelper.genInstanceId("userPromotion", userId, applyRole);
								if (null == JbpmHelper.checkPromotionStatus(instanceId)) {
									JbpmServiceHolder.executionService.startProcessInstanceByKey("userPromotion", variables, instanceId);
								} else {
								}
								super.onSubmit();
							}
						};

						applyBtns[i].setModel(new Model<String>(MessageFormat.format(rolePrefix, applyRole)));
						log.debug("add user apply role button. applyBtn[i]=" + i);
						form.add(applyBtns[i]);
						applyBtns[i].setVisible(true);
						log.debug("display name=" + applyRole + " visiblity" + applyBtns[i].isVisible());
					} else {
						log.debug("label displayed.");

						applyLabs[i] = new Label("waitRole" + i, applyRole + " apply in process");
						form.add(applyLabs[i]);
						applyLabs[i].setVisible(true);
					}
					break;
				}
			}

		}
	}

	@Override
	protected void setDesc() {
		this.desc = "init role";
	}
}
