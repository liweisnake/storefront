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
package com.hp.sdf.ngp.ui.page.statistic;

import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.feedback.ContainerFeedbackMessageFilter;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.Radio;
import org.apache.wicket.markup.html.form.RadioGroup;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;

import com.hp.sdf.ngp.service.ApplicationService;
import com.hp.sdf.ngp.ui.common.Generate;
import com.hp.sdf.ngp.ui.common.GenerateControls;
import com.hp.sdf.ngp.ui.common.TopSalesCondition;
import com.hp.sdf.ngp.ui.page.statistic.TopSalesPanel.TopSalesDataView;

public class TopSalesSearchPanel extends Panel {

	private static final long serialVersionUID = 5582241972856443619L;

	private static Log log = LogFactory.getLog(TopSalesSearchPanel.class);

	private TopSalesDataView dataView;

	private ApplicationService applicationService;

	public TopSalesSearchPanel(String id, TopSalesDataView dataView, ApplicationService applicationService) {
		super(id);
		this.dataView = dataView;
		this.applicationService = applicationService;

		Form<Void> searchForm = new TopSalesSearchForm("searchForm");
		add(searchForm);

		add(new FeedbackPanel("feedback", new ContainerFeedbackMessageFilter(searchForm)));
	}

	public class TopSalesSearchForm extends Form<Void> {

		private static final long serialVersionUID = 9154030696591798517L;

		@Generate
		private Date beginDate;

		@Generate
		private Date endDate;

		@Generate
		private String assetName;

		private Integer orderBy;

		public TopSalesSearchForm(String id) {
			super(id);

			// auto generate controls
			new GenerateControls(this, applicationService).Generate();

			RadioGroup<Integer> radioGroup = new RadioGroup<Integer>("radioGroup", new PropertyModel<Integer>(this, "orderBy"));
			radioGroup.add(new Radio<Integer>("radio1", new Model<Integer>(1)));
			radioGroup.add(new Radio<Integer>("radio2", new Model<Integer>(2)));
			radioGroup.add(new Radio<Integer>("radio3", new Model<Integer>(3)));
			radioGroup.add(new Radio<Integer>("radio4", new Model<Integer>(4)));
			add(radioGroup);

			// add all button
			// Button allBtn = new Button("btn.all") {
			//
			// private static final long serialVersionUID = 8116120281966867260L;
			//
			// @Override
			// public void onSubmit() {
			// clearControlValue();
			// TopSalesSearchForm.this.onSubmit();
			// }
			// };
			// allBtn.setDefaultFormProcessing(false);
			// add(allBtn);
		}

		@Override
		protected void onSubmit() {
			TopSalesCondition condition = new TopSalesCondition();
			condition.setBeginDate(beginDate);
			condition.setEndDate(endDate);
			condition.setAssetName(assetName);
			condition.setOrderBy(orderBy);
			dataView.updateModel(condition);

			clearControlValue();
		}

		@Override
		protected void onError() {
			this.clearInput();
			super.onError();
		}

		private void clearControlValue() {
			new GenerateControls(this).ClearControlsValue();
			orderBy = null;
		}
	}
}
