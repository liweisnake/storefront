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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.extensions.breadcrumb.IBreadCrumbModel;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.model.PropertyModel;

import com.hp.sdf.ngp.model.UserLifecycleAction;
import com.hp.sdf.ngp.workflow.jbpm.JbpmServiceHolder;

public class ApplyForApprovePanel extends WorkFlowBreadCrumbPanel {

	private static final long serialVersionUID = 1L;

	private final static Log log = LogFactory.getLog(UserInfoPanel.class);

	private String execId="";
	public ApplyForApprovePanel(String id, IBreadCrumbModel breadCrumbModel,String execId) {
		super(id, breadCrumbModel);
		this.execId=execId;
		add(new ApplyForApproveForm("applyForApprove"));
	}

	public class ApplyForApproveForm extends BreadCrumbForm {

		private TextArea textAreaComment;

		private UserLifecycleAction promotionRequest;

		public ApplyForApproveForm(String id) {
			super(id);
			promotionRequest = new UserLifecycleAction();
			textAreaComment = new TextArea("comments", new PropertyModel(promotionRequest, "comments"));
			add(textAreaComment);
		}

		public final void onSubmit() {

			HashMap<String, Object> variables = new HashMap<String, Object>();
			variables.put("component", this);
			variables.put("comment", textAreaComment.getValue());
			
			JbpmServiceHolder.executionService.signalExecutionById(execId, variables);

			activate(ifactory);
			
			

		}
	}

	public String getTitle() {
		return this.getLocalizer().getString("applyForm.title", this, "Apply form");
	}

}

// $Id$