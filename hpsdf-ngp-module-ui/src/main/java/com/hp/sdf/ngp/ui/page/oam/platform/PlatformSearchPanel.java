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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.extensions.breadcrumb.IBreadCrumbModel;
import org.apache.wicket.extensions.breadcrumb.panel.BreadCrumbPanel;
import org.apache.wicket.extensions.breadcrumb.panel.IBreadCrumbPanelFactory;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.PropertyModel;

public class PlatformSearchPanel extends BreadCrumbPanel {

	private static final long serialVersionUID = 2847203327312187476L;

	private static final Log log = LogFactory.getLog(PlatformSearchPanel.class);

	private SearchPlatformTabelPanel searchPlatformTabelPanel = null;

	private String  platformName ;

	public PlatformSearchPanel(String id, final IBreadCrumbModel breadCrumbModel) {
		super(id, breadCrumbModel);

		SearchPlatformForm form = new SearchPlatformForm("searchPlatformForm");
		add(form);

		searchPlatformTabelPanel = new SearchPlatformTabelPanel("platformsTable", platformName, breadCrumbModel);
		add(searchPlatformTabelPanel);
	}

	class SearchPlatformForm extends Form<Void> {

		private static final long serialVersionUID = 404463636873167639L;

		public SearchPlatformForm(String id) {
			super(id);

			TextField<String> platformNameField = new TextField<String>("platformName", new PropertyModel<String>(PlatformSearchPanel.this, "platformName"));
			add(platformNameField);

			Button newPlatformBtn = new Button("newPlatform") {
				private static final long serialVersionUID = 8179675420318996436L;

				public void onSubmit() {

					activate(new IBreadCrumbPanelFactory() {
						private static final long serialVersionUID = 3840521373650730173L;

						public BreadCrumbPanel create(String componentId, IBreadCrumbModel breadCrumbModel) {
							return new NewPlatformPanel(componentId, breadCrumbModel);
						}
					});
				}
			};
			add(newPlatformBtn);

			add(new Button("search") {
				private static final long serialVersionUID = 2263842147860607342L;

				public void onSubmit() {
					log.debug("platformName: " + platformName);

					log.debug("searchPlatformTabelPanel.platformsView.updateModel.");
					if (searchPlatformTabelPanel.platformsView != null) {
						searchPlatformTabelPanel.platformsView.updateModel(platformName);
					}
				}

			});
		}
	}

	public String getTitle() {
		return this.getLocalizer().getString("title", this, "Search Platform");
	}
}