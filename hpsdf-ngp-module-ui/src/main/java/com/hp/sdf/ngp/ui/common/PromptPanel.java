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

import org.apache.wicket.Page;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;

public class PromptPanel extends Panel {

	private static final long serialVersionUID = -157990505747550639L;

	private Page page;

	private String buttonValue;

	private Label promptLabel, messageLabel;

	public void show() {
		this.add(Constant.STYLE_SHOW);
	}

	public void hidden() {
		this.add(Constant.STYLE_HIDE);
	}

	public void setMessage(String message) {
		if (messageLabel != null)
			messageLabel.setDefaultModel(new Model<String>(message));
	}

	public void setPrompt(String prompt) {
		if (promptLabel != null)
			promptLabel.setDefaultModel(new Model<String>(prompt));
	}

	public PromptPanel(String id, String prompt, String message, Page page, String buttonValue) {
		super(id);

		this.setOutputMarkupId(true);
		this.add(Constant.STYLE_HIDE);

		this.page = page;
		this.buttonValue = buttonValue;

		promptLabel = new Label("prompt", prompt);
		add(promptLabel);
		messageLabel = new Label("message", message);
		add(messageLabel);
		add(new NavForm("navForm"));
	}

	private class NavForm extends Form<Void> {

		private static final long serialVersionUID = 4822037742842917335L;

		public NavForm(String id) {
			super(id);

			// AjaxButton backButton = new AjaxButton("close") {
			//
			// private static final long serialVersionUID = 7161862497494634176L;
			//
			// @Override
			// protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
			// PromptPanel.this.add(Constant.STYLE_HIDE);
			// target.addComponent(PromptPanel.this);
			// }
			//
			// };
			// backButton.setDefaultFormProcessing(false);
			// add(backButton);

			Button backButton = new Button("close") {

				private static final long serialVersionUID = 7161862497494634176L;

				@Override
				public void onSubmit() {
					PromptPanel.this.add(Constant.STYLE_HIDE);
				}

			};
			// backButton.setDefaultFormProcessing(false);
			add(backButton);

			Button otherButton = new Button("other") {

				private static final long serialVersionUID = -7567133419918483916L;

				@Override
				public void onSubmit() {
					PromptPanel.this.add(Constant.STYLE_HIDE);
					setResponsePage(PromptPanel.this.page);
				}

			};
			// otherButton.setDefaultFormProcessing(false);
			add(otherButton);

			if (null == page)
				otherButton.setVisible(false);

			// otherButton.add(new AttributeModifier("value", true, new Model<String>(buttonValue)));
			otherButton.setDefaultModel(new Model<String>(buttonValue));
		}

		@Override
		protected void onSubmit() {
		}
	}
}