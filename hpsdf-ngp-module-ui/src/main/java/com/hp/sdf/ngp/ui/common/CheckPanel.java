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
package com.hp.sdf.ngp.ui.common;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;

public abstract class CheckPanel extends Panel {

	private static final long serialVersionUID = -157990505747550639L;

	private Label promptLabel, messageLabel;

	private Boolean success = true;

	public static final String BUTTON_NAME = "ok";

	public void setMessage(String message) {
		if (messageLabel != null)
			messageLabel.setDefaultModel(new Model<String>(message));
	}

	public void setPrompt(String prompt) {
		if (promptLabel != null)
			promptLabel.setDefaultModel(new Model<String>(prompt));
	}

	public CheckPanel(String id, String prompt, String message) {
		super(id);

		this.setOutputMarkupId(true);
		this.add(Constant.STYLE_HIDE);

		promptLabel = new Label("prompt", prompt);
		add(promptLabel);
		messageLabel = new Label("message", message);
		add(messageLabel);

		Button okButton = new Button("ok") {
			private static final long serialVersionUID = 7161862497494634176L;

			@Override
			public void onSubmit() {
				howDo();
				CheckPanel.this.add(Constant.STYLE_HIDE);
				success = true;
			}
		};
		okButton.setDefaultFormProcessing(false);
		add(okButton);

		Button cancelButton = new Button("cancel") {
			private static final long serialVersionUID = -7567133419918483916L;

			@Override
			public void onSubmit() {
				CheckPanel.this.add(Constant.STYLE_HIDE);
				success = false;
			}
		};
		// cancelButton.setDefaultFormProcessing(false);
		add(cancelButton);
	}

	public void show() {
		this.add(Constant.STYLE_SHOW);
	}

	public void hidden() {
		this.add(Constant.STYLE_HIDE);
	}

	public Boolean isOk() {
		return success;
	}

	public Boolean getSuccess() {
		return success;
	}

	public void setSuccess(Boolean success) {
		this.success = success;
	}

	public abstract void howDo();
}