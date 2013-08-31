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
import org.apache.wicket.extensions.markup.html.tabs.ITab;
import org.apache.wicket.extensions.markup.html.tabs.TabbedPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;

import com.hp.sdf.ngp.ui.page.WicketPage;

public class ConfigurationAssetSearchPage extends WicketPage {

	@SuppressWarnings("unchecked")
	public ConfigurationAssetSearchPage(PageParameters parameters) {
		super(parameters);
		List<ITab> tabs = new ArrayList<ITab>();

		tabs.add(new AbstractTab(new Model(this.getLocalizer().getString("searchAsset", this, "Search for asset"))) {

			private static final long serialVersionUID = -7899878966983303305L;

			public Panel getPanel(String panelId) {
				ConfigurationAssetSearchContainerPanel asp = new ConfigurationAssetSearchContainerPanel(panelId);
				return asp;
			}
		});

		tabs.add(new AbstractTab(new Model(this.getLocalizer().getString("searchBinary", this, "Search for binary"))) {

			private static final long serialVersionUID = 6774094064802584329L;

			public Panel getPanel(String panelId) {
				ConfigurationBinarySearchContainerPanel bsp = new ConfigurationBinarySearchContainerPanel(panelId);
				return bsp;
			}
		});

		add(new TabbedPanel("tabs", tabs));
	}

}
