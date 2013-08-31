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
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;

import com.hp.sdf.ngp.service.ApplicationService;
import com.hp.sdf.ngp.ui.common.AssetManagementCondition;
import com.hp.sdf.ngp.ui.common.Generate;
import com.hp.sdf.ngp.ui.common.GenerateControls;
import com.hp.sdf.ngp.ui.common.SelectOption;
import com.hp.sdf.ngp.ui.common.Constant.CONTROL_TYPE;
import com.hp.sdf.ngp.ui.common.Constant.DATA_TYPE;
import com.hp.sdf.ngp.ui.page.oam.content.AssetManagementPanel.AssetManagementDataView;

public class AssetManagementSearchPanel extends Panel {

	private static final long serialVersionUID = 5582241972856443619L;

	private static Log log = LogFactory.getLog(AssetManagementSearchPanel.class);

	private AssetManagementDataView dataView;

	private ApplicationService applicationService;

	public AssetManagementSearchPanel(String id, AssetManagementDataView dataView, ApplicationService applicationService) {
		super(id);
		this.dataView = dataView;
		this.applicationService = applicationService;

		Form<Void> searchForm = new AssetManagementSearchForm("searchForm");
		add(searchForm);

		add(new FeedbackPanel("feedback", new ContainerFeedbackMessageFilter(searchForm)));
	}

	public class AssetManagementSearchForm extends Form<Void> {

		private static final long serialVersionUID = 9154030696591798517L;

		@Generate
		private Long providerId;

		@Generate
		private String providerName;

		@Generate
		private Long parentAssetId;

		@Generate
		private Long assetId;

		@Generate
		private String assetName;

		@Generate(id = "status", controlType = CONTROL_TYPE.DropDownChoice, dataType = DATA_TYPE.status, key = "id", value = "status")
		private SelectOption statusOption;

		public AssetManagementSearchForm(String id) {
			super(id);

			// auto generate controls
			new GenerateControls(this, applicationService).Generate();

			// add all button
			// Button allBtn = new Button("btn.all") {
			//
			// private static final long serialVersionUID = 8116120281966867260L;
			//
			// @Override
			// public void onSubmit() {
			// clearControlValue();
			// AssetManagementSearchForm.this.onSubmit();
			// }
			// };
			// allBtn.setDefaultFormProcessing(false);
			// add(allBtn);
		}

		@Override
		protected void onSubmit() {

			AssetManagementCondition condition = new AssetManagementCondition();

			condition.setProviderExternalId(providerId);
			condition.setProviderName(providerName);
			condition.setParentAssetId(parentAssetId);
			condition.setAssetId(assetId);
			condition.setAssetName(assetName);
			if (statusOption != null)
				condition.setStatusId(statusOption.getKey());

			AssetManagementSearchPanel.this.dataView.updateModel(condition);

			// clearControlValue();
		}

		@Override
		protected void onError() {
			// this.clearInput();
			// new GenerateControls(this).ClearControlsValue();
			super.onError();
		}

		private void clearControlValue() {
			// new GenerateControls(this).ClearControlsValue();
		}
	}
}
