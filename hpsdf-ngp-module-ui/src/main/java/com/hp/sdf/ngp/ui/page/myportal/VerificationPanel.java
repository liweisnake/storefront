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

import java.text.MessageFormat;
import java.util.HashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.extensions.breadcrumb.IBreadCrumbModel;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.hp.sdf.ngp.manager.UserManager;
import com.hp.sdf.ngp.manager.UserManager.VerificationType;
import com.hp.sdf.ngp.model.VerificationCode;
import com.hp.sdf.ngp.ui.WicketSession;
import com.hp.sdf.ngp.workflow.jbpm.JbpmServiceHolder;

public class VerificationPanel extends WorkFlowBreadCrumbPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@SpringBean
	private UserManager userManager;

	private VerificationCode verificationCode;

	private String execId;

	private final static Log log = LogFactory.getLog(VerificationPanel.class);

	public VerificationPanel(String id, IBreadCrumbModel breadCrumbModel) {
		super(id, breadCrumbModel);
		this.add(new FeedbackPanel("feedBack"));
		this.add(new VerificationForm("verificationForm"));
		this.verificationCode = userManager.sentVerificationCode(WicketSession.get().getUserId());
	}

	public VerificationPanel(String id, IBreadCrumbModel breadCrumbModel, String execId) {
		super(id, breadCrumbModel);
		this.add(new FeedbackPanel("feedBack"));
		this.execId = execId;
		this.add(new VerificationForm("verificationForm"));
		this.verificationCode = userManager.sentVerificationCode(WicketSession.get().getUserId());
	}

	public class VerificationForm extends BreadCrumbForm {

		String inputCode;

		TextField<String> veriTextField;

		public VerificationForm(String id) {
			super(id);
			add(new Label("way", MessageFormat.format(VerificationPanel.this.getLocalizer().getString("verification.way", this, "Application needs verification code which is available through {0}"), userManager.getVerificationType() == VerificationType.SMS ? "sms" : "email")));
			add(veriTextField = new TextField<String>("inputCode", new PropertyModel<String>(this, "inputCode")));
			veriTextField.setRequired(true);
		}

		public void onSubmit() {

			if (VerificationPanel.this.verificationCode.equals(inputCode)) {
				HashMap<String, Object> variables = new HashMap<String, Object>();
				variables.put("component", this);
				JbpmServiceHolder.executionService.signalExecutionById(execId, variables);

				activate(ifactory);
			} else {
				error(VerificationPanel.this.getLocalizer().getString("error.verificationcode", this, "The verification code is wrong, please input again"));
			}

		}
	}

	public String getTitle() {
		return this.getLocalizer().getString("title", this, "Input verification code");
	}

}

// $Id$