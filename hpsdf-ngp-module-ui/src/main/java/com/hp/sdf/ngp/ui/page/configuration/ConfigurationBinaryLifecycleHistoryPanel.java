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

import java.text.SimpleDateFormat;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.extensions.breadcrumb.IBreadCrumbModel;
import org.apache.wicket.extensions.breadcrumb.panel.BreadCrumbPanel;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.navigation.paging.PagingNavigator;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.hp.sdf.ngp.model.AssetLifecycleAction;
import com.hp.sdf.ngp.service.ApplicationService;
import com.hp.sdf.ngp.ui.provider.AssetHistoryDataProvider;

public class ConfigurationBinaryLifecycleHistoryPanel extends BreadCrumbPanel {

	private static final long serialVersionUID = -6485915462897188592L;

	private static final Log log = LogFactory.getLog(ConfigurationBinaryLifecycleHistoryPanel.class);

	private int itemsPerPage = 5;

	public int getItemsPerPage() {
		return itemsPerPage;
	}

	public void setItemsPerPage(int itemsPerPage) {
		this.itemsPerPage = itemsPerPage;
	}

	@SpringBean
	ApplicationService appService;

	public ConfigurationBinaryLifecycleHistoryPanel(String id, IBreadCrumbModel breadCrumbModel, Long appId) {
		super(id, breadCrumbModel);

		log.debug("itemsPerPage :" + itemsPerPage);
		AssetHistoryDataProvider appBinaryHistoryDataProvider = new AssetHistoryDataProvider(appService, appId);

		AppDataView dataView = new AppDataView("appBinaryHistory", appBinaryHistoryDataProvider, itemsPerPage);
		add(dataView);
		add(new PagingNavigator("navigator", dataView));
	}

	public ConfigurationBinaryLifecycleHistoryPanel(String id, IBreadCrumbModel breadCrumbModel) {
		super(id, breadCrumbModel);
	}

	class AppDataView extends DataView<AssetLifecycleAction> {

		private static final long serialVersionUID = -6514262177740324330L;

		protected AppDataView(String id, IDataProvider<AssetLifecycleAction> appBinaryHistoryDataProvider, int itemsPerPage) {
			super(id, appBinaryHistoryDataProvider, itemsPerPage);
		}

		protected void populateItem(Item<AssetLifecycleAction> item) {
			final AssetLifecycleAction appBinaryLifecycleHistory = item.getModelObject();

			SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
			item.add(new Label("Date", sdf.format(appBinaryLifecycleHistory.getCreateDate())));
			item.add(new Label("Event", appBinaryLifecycleHistory.getEvent()));
			item.add(new Label("PreStatus", appBinaryLifecycleHistory.getPreStatus() != null ? appBinaryLifecycleHistory.getPreStatus().getStatus() : ""));
			item.add(new Label("PostStatus", appBinaryLifecycleHistory.getPostStatus() != null ? appBinaryLifecycleHistory.getPostStatus().getStatus() : ""));
			item.add(new Label("Operator", appBinaryLifecycleHistory.getOwnerid()));
		}
	}

	public String getTitle() {
		return this.getLocalizer().getString("title", this, "Binary Lifecycle");
	}

}
