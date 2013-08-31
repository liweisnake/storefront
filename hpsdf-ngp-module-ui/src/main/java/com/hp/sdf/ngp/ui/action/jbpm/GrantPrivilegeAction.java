package com.hp.sdf.ngp.ui.action.jbpm;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jbpm.api.activity.ActivityBehaviour;
import org.jbpm.api.activity.ActivityExecution;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.hp.sdf.ngp.model.UserProfile;
import com.hp.sdf.ngp.model.UserRoleCategory;
import com.hp.sdf.ngp.service.UserService;
import com.hp.sdf.ngp.workflow.jbpm.BaseAction;
import com.hp.sdf.ngp.workflow.jbpm.PromotionRequest;

/**
 * @author jack
 */
@Component
@Scope("prototype")
public class GrantPrivilegeAction extends BaseAction implements ActivityBehaviour {
	private static final Log log = LogFactory.getLog(GrantPrivilegeAction.class);
	@Resource
	private UserService userService;

	private static final long serialVersionUID = 1L;

	public void execute(ActivityExecution execution) {
		PromotionRequest promotionRequest = (PromotionRequest) execution.getVariable("request");
		if (passThisRole(promotionRequest.getTargetRole()))
			return;

		userService.assignRole(promotionRequest.getUserId(), promotionRequest.getTargetRole());

		UserProfile up = userService.getUser(promotionRequest.getUserId());

		for (UserRoleCategory urc : up.getUserRoleCategories()) {
			log.debug("each role:" + urc.getRoleCategory().getRoleName());
		}


		
		execution.takeDefaultTransition();
	}

	@Override
	protected void setDesc() {
		this.desc = "grant Privilege";

	}

}
