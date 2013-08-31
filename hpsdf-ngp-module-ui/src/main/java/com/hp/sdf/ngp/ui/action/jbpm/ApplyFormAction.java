package com.hp.sdf.ngp.ui.action.jbpm;

import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.extensions.breadcrumb.IBreadCrumbModel;
import org.apache.wicket.extensions.breadcrumb.panel.BreadCrumbPanel;
import org.apache.wicket.extensions.breadcrumb.panel.IBreadCrumbPanelFactory;
import org.jbpm.api.activity.ActivityExecution;
import org.jbpm.api.activity.ExternalActivityBehaviour;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.hp.sdf.ngp.ui.page.myportal.ApplyForApprovePanel;
import com.hp.sdf.ngp.ui.page.myportal.BreadCrumbForm;
import com.hp.sdf.ngp.workflow.jbpm.BaseAction;
import com.hp.sdf.ngp.workflow.jbpm.PromotionRequest;

/**
 * @author jack
 */
@Component
@Scope("prototype")
public class ApplyFormAction extends BaseAction implements ExternalActivityBehaviour {

	private static final long serialVersionUID = 1L;
	private static final Log log = LogFactory.getLog(ApplyFormAction.class);

	public void execute(final ActivityExecution execution) {

		log.debug("called by:" + execution.getId() + "," + passRoles);

		final PromotionRequest req = (PromotionRequest) execution.getVariable("request");
		if (passThisRole(req.getTargetRole()))
			return;

		BreadCrumbForm form = (BreadCrumbForm) execution.getVariable("component");

		if (form != null) {
			form.setIfactory(new IBreadCrumbPanelFactory() {
				public BreadCrumbPanel create(String componentId, IBreadCrumbModel breadCrumbModel) {
					return new ApplyForApprovePanel(componentId, breadCrumbModel, execution.getId());
				}
			});
		}

		execution.removeVariable("component");
		execution.waitForSignal();
	}

	/**
	 * when codeVerifyAction finished,call signal method to resume execution of
	 * this instance,like <br>
	 * executionService.signalExecutionById(executionId,"toApprove");
	 * 
	 * @param signalName
	 *            {toApprove} <br>
	 *            parameters {component}
	 */
	public void signal(final ActivityExecution execution, String signalName, Map<String, ?> parameters) {
		log.debug("applyFormAction signaled by:" + signalName);

		final PromotionRequest req = (PromotionRequest) execution.getVariable("request");
		if (passThisRole(req.getTargetRole()))
			return;

		if (null == parameters.get("comment")) {
			req.setComment("");
		} else {
			req.setComment(parameters.get("comment").toString());
		}

		execution.setVariable("request", req);
		execution.setVariable("component", (BreadCrumbForm) parameters.get("component"));
		execution.take(signalName);
	}

	@Override
	protected void setDesc() {
		// TODO Auto-generated method stub

	}
}
