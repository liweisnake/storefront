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
package com.hp.sdf.ngp.ui.page.register;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.extensions.breadcrumb.IBreadCrumbModel;
import org.apache.wicket.extensions.breadcrumb.panel.BreadCrumbPanel;
import org.apache.wicket.extensions.markup.html.tabs.AbstractTab;
import org.apache.wicket.extensions.markup.html.tabs.TabbedPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;

public class UserRegisterPanel extends BreadCrumbPanel {

	@SuppressWarnings("unchecked")
	public UserRegisterPanel(String id, final IBreadCrumbModel breadCrumbModel, int selectTab) {
		super(id, breadCrumbModel);
		
		List tabs = new ArrayList();

		tabs.add(new AbstractTab(new Model(this.getLocalizer().getString("welcome", this, "Welcome"))) {

			private static final long serialVersionUID = -7626164496782615826L;

			public Panel getPanel(String panelId) {
				WelcomePanel welcomePanel = new WelcomePanel(panelId, breadCrumbModel);
				return welcomePanel;
			}
		});

		tabs.add(new AbstractTab(new Model(this.getLocalizer().getString("register", this, "Register"))) {

			private static final long serialVersionUID = 1L;

			public Panel getPanel(String panelId) {
				RegisterPanel registerPanel = new RegisterPanel(panelId, breadCrumbModel);
				return registerPanel;
			}
		});

		TabbedPanel tabbedPanel = new TabbedPanel("tabs", tabs);
		tabbedPanel.setSelectedTab(selectTab);
		add(tabbedPanel);
	}

	private static final long serialVersionUID = -62446153418538501L;

	public String getTitle() {
		return this.getLocalizer().getString("title", this, "");
	}

}

