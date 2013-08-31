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
package com.hp.sdf.ngp.ui.page.configuration;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.PageParameters;
import org.apache.wicket.extensions.markup.html.tabs.AbstractTab;
import org.apache.wicket.extensions.markup.html.tabs.TabbedPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;

import com.hp.sdf.ngp.ui.WicketSession;
import com.hp.sdf.ngp.ui.page.WicketPage;

public class ConfigurationPage extends WicketPage {

	@SuppressWarnings("unchecked")
	public ConfigurationPage(final PageParameters parameters) {
		super(parameters);
		List tabs = new ArrayList();

		tabs.add(new AbstractTab(new Model(this.getLocalizer().getString("category", this, "Category"))) {
			private static final long serialVersionUID = 1L;

			public Panel getPanel(final String panelId) {
				return new ConfigurationCategoryPanel(panelId, parameters);
			}
		});

		tabs.add(new AbstractTab(new Model(this.getLocalizer().getString("platform", this, "Platform"))) {

			private static final long serialVersionUID = -3751433464895501713L;

			public Panel getPanel(String panelId) {
				return new ConfigurationPlatformPanel(panelId, parameters);
			}
		});

		tabs.add(new AbstractTab(new Model(this.getLocalizer().getString("tag", this, "Tag"))) {

			private static final long serialVersionUID = 5345240263141300258L;

			public Panel getPanel(String panelId) {
				return new ConfigurationTagPanel(panelId, parameters);
			}
		});

		TabbedPanel tabPanel = new TabbedPanel("tabs", tabs);
		add(tabPanel);

		String selectTab = null == WicketSession.get().getObject("selectConTab") ? null : WicketSession.get().getObject("selectConTab").toString();
		if (selectTab == null)
			return;
		if (selectTab.equals(ConfigurationCategoryPanel.class.toString())) {
			tabPanel.setSelectedTab(0);
		} else if (selectTab.equals(ConfigurationPlatformPanel.class.toString())) {
			tabPanel.setSelectedTab(1);
		} else if (selectTab.equals(ConfigurationTagPanel.class.toString())) {
			tabPanel.setSelectedTab(2);
		}
	}

}
