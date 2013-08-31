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
package com.hp.sdf.ngp.ui.page.oam.content;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.feedback.ContainerFeedbackMessageFilter;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.PropertyModel;

import com.hp.sdf.ngp.service.ApplicationService;
import com.hp.sdf.ngp.ui.common.DefaultSettingCondition;
import com.hp.sdf.ngp.ui.page.oam.content.DefaultSettingPanel.DefaultSettingUpdateDataView;

public class DefaultSettingSearchPanel extends Panel {

	private static final long serialVersionUID = 5582241972856443619L;

	private static Log log = LogFactory.getLog(DefaultSettingSearchPanel.class);

	private DefaultSettingUpdateDataView dataView;

	private ApplicationService applicationService;

	public DefaultSettingSearchPanel(String id, DefaultSettingUpdateDataView dataView, ApplicationService applicationService) {
		super(id);
		this.dataView = dataView;
		this.applicationService = applicationService;

		Form<Void> searchForm = new DefaultSettingSearchForm("search.form");
		add(searchForm);

		final FeedbackPanel feedback = new FeedbackPanel("search.feedback", new ContainerFeedbackMessageFilter(searchForm));
		add(feedback);
	}

	public class DefaultSettingSearchForm extends Form<Void> {

		private static final long serialVersionUID = 9154030696591798517L;

		private String providerId;
		private String providerName;

		public DefaultSettingSearchForm(String id) {
			super(id);

			// add pid
			TextField<String> pidField = new TextField<String>("search.pid", new PropertyModel<String>(this, "providerId"));
			add(pidField);

			// add provider
			TextField<String> providerField = new TextField<String>("search.provider", new PropertyModel<String>(this, "providerName"));
			add(providerField);

			// add all button
			// Button allBtn = new Button("btn.all") {
			//
			// private static final long serialVersionUID = 8116120281966867260L;
			//
			// @Override
			// public void onSubmit() {
			// log.debug("clear control value of form.");
			// clearControlValue();
			//
			// }
			// };
			// allBtn.setDefaultFormProcessing(true);
			// add(allBtn);
		}

		@Override
		protected void onSubmit() {

			DefaultSettingCondition condition = new DefaultSettingCondition();

			condition.setProviderExternalId(providerId);
			condition.setProviderName(providerName);

			DefaultSettingSearchPanel.this.dataView.updateModel(condition);

			// clearControlValue();
		}

		@Override
		protected void onError() {
			// this.clearInput();
			super.onError();
		}

		/**
		 * clear control value of form
		 */
		private void clearControlValue() {
			// this.providerId = null;
			// this.providerName = null;
		}
	}
}
