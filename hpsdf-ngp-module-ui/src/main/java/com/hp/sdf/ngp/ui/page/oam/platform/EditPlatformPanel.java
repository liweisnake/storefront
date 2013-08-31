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
package com.hp.sdf.ngp.ui.page.oam.platform;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.PageParameters;
import org.apache.wicket.extensions.breadcrumb.IBreadCrumbModel;
import org.apache.wicket.extensions.breadcrumb.panel.BreadCrumbPanel;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.hp.sdf.ngp.model.Platform;
import com.hp.sdf.ngp.service.ApplicationService;
import com.hp.sdf.ngp.ui.common.PromptPanel;
import com.hp.sdf.ngp.ui.common.Tools;

public class EditPlatformPanel extends BreadCrumbPanel {

	private static final long serialVersionUID = -8920226863398352415L;

	private static final Log log = LogFactory.getLog(EditPlatformPanel.class);

	private PromptPanel promptPanel;

	public EditPlatformPanel(String id, IBreadCrumbModel breadCrumbModel, Platform platform) {
		super(id, breadCrumbModel);

		this.add(new EditPlatformForm("editPlatformForm", platform));

		promptPanel = new PromptPanel("promptPanel", getLocalizer().getString("promptTitle", this), getLocalizer().getString("successMsg", this), null, StringUtils.EMPTY);
		add(promptPanel);
	}

	public final class EditPlatformForm extends Form<Void> {

		private static final long serialVersionUID = 1L;

		@SpringBean
		private ApplicationService applicationService;

		private String platformName;

		private String description;

		public EditPlatformForm(String id, final Platform platform) {
			super(id);
			this.add(new FeedbackPanel("feedBack"));

			TextField<String> platformNameField = new TextField<String>("platformName", new PropertyModel<String>(this, "platformName"));
			platformNameField.setRequired(true);
			platformNameField.setDefaultModelObject(platform.getName());
			add(platformNameField);

			TextField<String> descriptionField = new TextField<String>("description", new PropertyModel<String>(this, "description"));
			descriptionField.setDefaultModelObject(platform.getDescription());
			add(descriptionField);

			Button save = new Button("save") {
				private static final long serialVersionUID = 1510836418186350793L;

				public void onSubmit() {
					log.debug("platformName :" + platformName);
					log.debug("description :" + description);

					if (!platformName.equals(platform.getName()) && null != applicationService.getPlatformByName(platformName)) {
						error(getLocalizer().getString("error.save.dpplatform", EditPlatformPanel.this));
						return;
					}

					if (null == applicationService.getPlatformById(platform.getId())) {
						error(getLocalizer().getString("error.save.nullplatform", EditPlatformPanel.this));
						return;
					}

					platform.setName(platformName);
					platform.setDescription(description);
					applicationService.updatePlatform(platform);

					promptPanel.show();

					PageParameters parameters = new PageParameters();
					PlatformSearchPage page = new PlatformSearchPage(parameters);
					setResponsePage(page);
				}
			};
			save.add(Tools.addConfirmJs(getLocalizer().getString("confirmMsg", EditPlatformPanel.this)));
			add(save);

			Button backBtn = new Button("back") {

				private static final long serialVersionUID = 8116120281966867260L;

				public void onSubmit() {
					PageParameters parameters = new PageParameters();
					PlatformSearchPage page = new PlatformSearchPage(parameters);
					setResponsePage(page);
				}
			};
			backBtn.setDefaultFormProcessing(false);
			add(backBtn);
		}
	}

	public String getTitle() {
		return this.getLocalizer().getString("title", this, "Edit Platform");
	}

}
