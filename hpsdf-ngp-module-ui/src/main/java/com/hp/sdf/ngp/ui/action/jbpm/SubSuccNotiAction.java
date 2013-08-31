
package com.hp.sdf.ngp.ui.action.jbpm;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.extensions.breadcrumb.IBreadCrumbModel;
import org.apache.wicket.extensions.breadcrumb.panel.BreadCrumbPanel;
import org.apache.wicket.extensions.breadcrumb.panel.IBreadCrumbPanelFactory;
import org.jbpm.api.activity.ActivityBehaviour;
import org.jbpm.api.activity.ActivityExecution;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.hp.sdf.ngp.ui.page.myportal.BreadCrumbForm;
import com.hp.sdf.ngp.ui.page.myportal.SuccessPanel;
import com.hp.sdf.ngp.workflow.jbpm.BaseAction;
import com.hp.sdf.ngp.workflow.jbpm.PromotionRequest;

/**
 * @author jack
 */
@Component
@Scope("prototype")
public class SubSuccNotiAction extends BaseAction implements ActivityBehaviour {

	private static final long serialVersionUID = 1L;
	private static final Log log = LogFactory.getLog(SubSuccNotiAction.class);
	
	private String promotionInfoKey="promotion.success.msg";

	public String getPromotionInfoKey() {
		return promotionInfoKey;
	}

	public void setPromotionInfoKey(String promotionInfoKey) {
		this.promotionInfoKey = promotionInfoKey;
	}

	public void execute(final ActivityExecution execution) {

		log.debug("called by:" + execution.getId() + "," + passRoles);
		
		final PromotionRequest req = (PromotionRequest) execution.getVariable("request");
		if (passThisRole(req.getTargetRole()))
			return;


		BreadCrumbForm form = (BreadCrumbForm) execution.getVariable("component");

		if (form != null) {

			form.setIfactory(new IBreadCrumbPanelFactory() {
				public BreadCrumbPanel create(String componentId, IBreadCrumbModel breadCrumbModel) {

					return new SuccessPanel(componentId, breadCrumbModel, promotionInfoKey, req.getTargetRole());
				}
			});

		}
		execution.removeVariable("component");
	}
	
	@Override
	protected void setDesc() {
		this.desc="sub-success-notify";
	}
}
