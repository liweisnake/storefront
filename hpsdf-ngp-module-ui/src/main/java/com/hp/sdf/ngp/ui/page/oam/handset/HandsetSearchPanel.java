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
package com.hp.sdf.ngp.ui.page.oam.handset;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.Component;
import org.apache.wicket.authorization.strategies.role.metadata.MetaDataRoleAuthorizationStrategy;
import org.apache.wicket.extensions.breadcrumb.IBreadCrumbModel;
import org.apache.wicket.extensions.breadcrumb.panel.BreadCrumbPanel;
import org.apache.wicket.extensions.breadcrumb.panel.IBreadCrumbPanelFactory;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.PropertyModel;

import com.hp.sdf.ngp.ui.common.HandsetSearchCondition;
import com.hp.sdf.ngp.workflow.Privilege;

public class HandsetSearchPanel extends BreadCrumbPanel {

	private static final long serialVersionUID = 2847203327312187476L;

	private static final Log log = LogFactory.getLog(HandsetSearchPanel.class);

	private SearchHandsetTabelPanel searchHandsetTabelPanel = null;

	private HandsetSearchCondition handsetSearchCondition = null;

	public HandsetSearchPanel(String id, final IBreadCrumbModel breadCrumbModel) {
		super(id, breadCrumbModel);

		SearchHandsetForm form = new SearchHandsetForm("searchHandsetForm");
		MetaDataRoleAuthorizationStrategy.authorize(form, Component.RENDER, Privilege.SEARCHHANDSET);
		add(form);

		searchHandsetTabelPanel = new SearchHandsetTabelPanel("handsetsTable", handsetSearchCondition, breadCrumbModel);
		MetaDataRoleAuthorizationStrategy.authorize(searchHandsetTabelPanel, Component.RENDER, Privilege.VIEWHANDSETLIST);
		add(searchHandsetTabelPanel);
	}

	class SearchHandsetForm extends Form<Void> {

		private static final long serialVersionUID = 404463636873167639L;

		private String displayName;

		private String deviceName;

		public SearchHandsetForm(String id) {
			super(id);

			TextField<String> displayNameField = new TextField<String>("displayName", new PropertyModel<String>(this, "displayName"));
			add(displayNameField);

			TextField<String> deviceNameField = new TextField<String>("deviceName", new PropertyModel<String>(this, "deviceName"));
			add(deviceNameField);

			Button newHandsetBtn = new Button("newHandset") {
				private static final long serialVersionUID = 8179675420318996436L;

				public void onSubmit() {

					activate(new IBreadCrumbPanelFactory() {
						private static final long serialVersionUID = 3840521373650730173L;

						public BreadCrumbPanel create(String componentId, IBreadCrumbModel breadCrumbModel) {
							return new NewHandsetPanel(componentId, breadCrumbModel);
						}
					});
				}
			};
			MetaDataRoleAuthorizationStrategy.authorize(newHandsetBtn, Component.RENDER, Privilege.NEWHANDSET);
			add(newHandsetBtn);

			add(new Button("search") {
				private static final long serialVersionUID = 2263842147860607342L;

				public void onSubmit() {
					log.debug("displayName: " + displayName);
					log.debug("deviceName: " + deviceName);

					handsetSearchCondition = new HandsetSearchCondition();
					handsetSearchCondition.setHandsetName(displayName);
					handsetSearchCondition.setDeviceName(deviceName);

					log.debug("searchHandsetTabelPanel.handsetsView.updateModel.");
					if (searchHandsetTabelPanel.handsetsView != null) {
						searchHandsetTabelPanel.handsetsView.updateModel(handsetSearchCondition);
					}
				}

			});
		}
	}

	public String getTitle() {
		return this.getLocalizer().getString("title", this, "Search Handset Model");
	}
}