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
package com.hp.sdf.ngp.ui.page.api;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.PageParameters;
import org.apache.wicket.extensions.markup.html.tabs.AbstractTab;
import org.apache.wicket.extensions.markup.html.tabs.TabbedPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;

import com.hp.sdf.ngp.ui.WicketSession;
import com.hp.sdf.ngp.ui.page.WicketPage;

public class ApiConfigurationPage extends WicketPage {

	public ApiConfigurationPage(final PageParameters parameters) {
		super(parameters);
		List tabs = new ArrayList();
		tabs.add(new AbstractTab(new Model(this.getLocalizer().getString("all", this))) {
			public Panel getPanel(final String panelId) {
				return new ApiAllConfigurationPanel(panelId, parameters);
			}
		});
		// tabs.add(new AbstractTab(new Model(this.getLocalizer().getString("sgf", this))) {
		// public Panel getPanel(final String panelId) { return new ApiSgfConfigurationPanel(panelId, parameters); }
		// });
		// tabs.add(new AbstractTab(new Model(this.getLocalizer().getString("common", this))) {
		// public Panel getPanel(final String panelId) { return new ApiCommonConfigurationPanel(panelId, parameters); }
		// });
		tabs.add(new AbstractTab(new Model(this.getLocalizer().getString("batch", this))) {
			public Panel getPanel(final String panelId) {
				return new ApiBatchUploadPanel(panelId);
			}
		});

		TabbedPanel tabPanel = new TabbedPanel("tabs", tabs);
		String selectTab = null == WicketSession.get().getObject("selectApiConTab") ? null : WicketSession.get().getObject("selectApiConTab").toString();
		add(tabPanel);
		if (selectTab == null)
			return;
		// if (selectTab.equals(ApiSgfConfigurationPanel.class.toString())) {
		// tabPanel.setSelectedTab(0);
		// }
		// else if(selectTab.equals(ApiCommonConfigurationPanel.class.toString())){
		// tabPanel.setSelectedTab(1);
		// }
		if (selectTab.equals(ApiAllConfigurationPanel.class.toString())) {
			tabPanel.setSelectedTab(0);
		} else if (selectTab.equals(ApiBatchUploadPanel.class.toString())) {
			tabPanel.setSelectedTab(1);
		}
	}

}
