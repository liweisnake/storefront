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
package com.hp.sdf.ngp.ui.page.purchase;

import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.extensions.markup.html.form.DateTextField;
import org.apache.wicket.extensions.yui.calendar.DatePicker;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.PropertyModel;

import com.hp.sdf.ngp.common.constant.AssetSubscriptionStatus;
import com.hp.sdf.ngp.ui.WicketSession;
import com.hp.sdf.ngp.ui.common.Constant;
import com.hp.sdf.ngp.ui.page.purchase.MySubscriptionPanel.SubscriptionDataView;

public class HistorySearchPanel extends Panel {

	private static final long serialVersionUID = 5582241972856443619L;

	private SubscriptionDataView dataView;

	public HistorySearchPanel(String id, SubscriptionDataView dataView) {
		super(id);
		this.dataView = dataView;

		// Create feedback panel and add to page
		final FeedbackPanel feedback = new FeedbackPanel("feedback");
		add(feedback);

		Form searchForm = new SubscriptionSearchForm("searchForm");
		add(searchForm);
	}

	public class SubscriptionSearchForm extends Form {

		private static final long serialVersionUID = 9154030696591798517L;

		private SearchCondition condition = new SearchCondition();

		private TextField<String> keywordField;

		private DateTextField startDateField, endDateField;

		private DropDownChoice<String> statusChoice;

		private final String STATUS_ALL = HistorySearchPanel.this.getLocalizer().getString("all", HistorySearchPanel.this);

		public SubscriptionSearchForm(String id) {
			super(id);

			String userId = WicketSession.get().getUserId();
			condition.setUserId(userId);

			// adds keyword
			keywordField = new TextField<String>("keyword", new PropertyModel<String>(condition, "keyword"));
			add(keywordField);
			keywordField.updateModel();

			// adds status
			List<String> statuses = AssetSubscriptionStatus.listType();
			statusChoice = new DropDownChoice<String>("status", new PropertyModel(condition, "status"), statuses);
			add(statusChoice);

			startDateField = new DateTextField("startDate", new PropertyModel<Date>(condition, "startDate"), Constant.DATE_PATTERN);
			add(startDateField);
			startDateField.add(new DatePicker());

			endDateField = new DateTextField("endDate", new PropertyModel<Date>(condition, "endDate"), Constant.DATE_PATTERN);
			add(endDateField);
			endDateField.add(new DatePicker());

			Button allBtn = new Button("btn.all") {

				private static final long serialVersionUID = 8116120281966867260L;

				@Override
				public void onSubmit() {
					Log log = LogFactory.getLog(HistorySearchPanel.class);
					log.debug("Clean search conditions.");
					condition.setKeyword(null);
					condition.setStartDate(null);
					condition.setEndDate(null);
					condition.setStatus(null);
					keywordField.setDefaultModel(new PropertyModel<String>(condition, "keyword"));
					startDateField.setDefaultModel(new PropertyModel<Date>(condition, "startDate"));
					endDateField.setDefaultModel(new PropertyModel<Date>(condition, "endDate"));
					statusChoice.setDefaultModel(new PropertyModel<String>(condition, "status"));
					SubscriptionSearchForm.this.updateFormComponentModels();
					SubscriptionSearchForm.this.onSubmit();
				}
			};
			allBtn.setDefaultFormProcessing(false);
			add(allBtn);
		}

		@Override
		protected void onSubmit() {
			List<String> statuses = AssetSubscriptionStatus.listType();
			if (condition.getStatus() != null) {
				if (condition.getStatus().equals(STATUS_ALL)) {
					condition.setStatus(null);
				} else {
					statuses.add(0, STATUS_ALL);
				}
			}
			;
			statusChoice.setChoices(statuses);

			HistorySearchPanel.this.dataView.updateModel(condition);
		}
	}

}

// $Id$