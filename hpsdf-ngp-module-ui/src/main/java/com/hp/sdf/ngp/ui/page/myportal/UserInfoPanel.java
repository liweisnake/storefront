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
package com.hp.sdf.ngp.ui.page.myportal;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.axis.utils.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.extensions.breadcrumb.IBreadCrumbModel;
import org.apache.wicket.extensions.breadcrumb.IBreadCrumbParticipant;
import org.apache.wicket.extensions.breadcrumb.panel.BreadCrumbPanel;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.hp.sdf.ngp.model.RoleCategory;
import com.hp.sdf.ngp.service.UserService;
import com.hp.sdf.ngp.ui.WicketSession;
import com.hp.sdf.ngp.workflow.jbpm.JbpmHelper;
import com.hp.sdf.ngp.workflow.jbpm.JbpmServiceHolder;

public class UserInfoPanel extends BreadCrumbPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public void onActivate(IBreadCrumbParticipant previous) {

		UserInfoForm uif = new UserInfoForm("userInfoForm");
		this.addOrReplace(uif);

		super.onActivate(previous);

	}

	private final static Log log = LogFactory.getLog(UserInfoPanel.class);

	@SpringBean
	private UserService userService;

	public UserInfoPanel(String id, IBreadCrumbModel breadCrumbModel) {
		super(id, breadCrumbModel);
		log.debug("((()))" + WicketSession.get().getUserId());

		UserInfoForm uif = new UserInfoForm("userInfoForm");
		add(uif);
	}

	public class UserInfoForm extends BreadCrumbForm {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		private final int linkLen = 5;

		private String role;

		private Label userId;

		private Label userRole;

		private Button[] applyButton = new Button[linkLen];

		private Label[] applyLabel = new Label[linkLen];

		public String getRole() {
			return role;
		}

		public void setRole(String role) {
			this.role = role;
		}

		public Label getUserId() {
			return userId;
		}

		public void setUserId(Label userId) {
			this.userId = userId;
		}

		public Label getUserRole() {
			return userRole;
		}

		public void setUserRole(Label userRole) {
			this.userRole = userRole;
		}

		public Button[] getApplyButton() {
			return applyButton;
		}

		public void setApplyButton(Button[] applyButton) {
			this.applyButton = applyButton;
		}

		public Label[] getApplyLabel() {
			return applyLabel;
		}

		public void setApplyLabel(Label[] applyLabel) {
			this.applyLabel = applyLabel;
		}

		public int getLinkLen() {
			return linkLen;
		}

		private void promote(String userId, String userRoles) {
			log.debug("Enter promote.");
			Map<String, Object> variables = new HashMap<String, Object>();
			variables.put("component", this);
			variables.put("userId", String.valueOf(userId));
			variables.put("userRoles", userRoles);
			variables.put("rolePrefix", UserInfoPanel.this.getLocalizer().getString("userinfo.applyrole", UserInfoPanel.this));

			String instanceId = JbpmHelper.genInstanceId("userRole", "", String.valueOf(userId));
			if (null == JbpmHelper.checkPromotionStatus(instanceId)) {
				JbpmServiceHolder.executionService.startProcessInstanceByKey("userRole", variables, instanceId);
			} else {
				// TODO
				log.warn("userRole flow not end as expaired:userId=" + userId + ",userRoles=" + userRoles + ",instanceId=" + instanceId);
			}
		}

		public UserInfoForm(String id) {
			super(id);
			userId = new Label("userId", WicketSession.get().getUserId());
			add(userId);
			List<RoleCategory> roleCategories = userService.getRoleCategoryByUserId(WicketSession.get().getUserId());

			String userRoleString = "";
			if (roleCategories != null) {
				for (RoleCategory roleCategory : roleCategories) {
					if (StringUtils.isEmpty(userRoleString)) {
						userRoleString = roleCategory.getRoleName();
					} else {
						userRoleString = userRoleString + "," + roleCategory.getRoleName();
					}

				}
			}

			userRole = new Label("userRole", userRoleString);
			add(userRole);

			promote(WicketSession.get().getUserId(), userRoleString);
			for (int i = 0; i < linkLen; i++) {
				if (applyButton[i] == null) {
					applyButton[i] = new Button("applyRole" + i);
					applyButton[i].setVisible(false);
					log.debug("add apply role i = " + i);
					add(applyButton[i]);
				}
				if (applyLabel[i] == null) {
					applyLabel[i] = new Label("waitRole" + i);
					applyLabel[i].setVisible(false);
					log.debug("add waitRole i =" + i);
					add(applyLabel[i]);
				}
			}
		}

		public final void onSubmit() {
			activate(ifactory);
		}
	}

	public String getTitle() {
		return this.getLocalizer().getString("title", this, "My Status");
	}
}

// $Id$