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
import org.apache.wicket.extensions.breadcrumb.panel.BreadCrumbPanelLink;
import org.apache.wicket.extensions.breadcrumb.panel.IBreadCrumbPanelFactory;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.navigation.paging.PagingNavigator;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.hp.sdf.ngp.model.AssetLifecycleAction;
import com.hp.sdf.ngp.service.ApplicationService;
import com.hp.sdf.ngp.ui.provider.AssetHistoryDataProvider;

public class ConfigurationAssetLifecycleHistoryPanel extends BreadCrumbPanel {

	private static final long serialVersionUID = 1L;

	private static final Log log = LogFactory.getLog(ConfigurationAssetLifecycleHistoryPanel.class);

	private int itemsPerPage = 5;

	public int getItemsPerPage() {
		return itemsPerPage;
	}

	public void setItemsPerPage(int itemsPerPage) {
		this.itemsPerPage = itemsPerPage;
	}

	@SpringBean
	private ApplicationService appService;

	/**
	 * current BreadCrumbModel.
	 */
	private IBreadCrumbModel currentBreadCrumbModel = this.getBreadCrumbModel();

	public ConfigurationAssetLifecycleHistoryPanel(String id, IBreadCrumbModel breadCrumbModel, Long appId) {
		super(id, breadCrumbModel);

		AssetHistoryDataProvider appHistoryDataProvider = new AssetHistoryDataProvider(appService, appId);

		AssetDataView dataView = new AssetDataView("appHistory", appHistoryDataProvider, itemsPerPage);
		add(dataView);
		add(new PagingNavigator("navigator", dataView));
	}

	class AssetDataView extends DataView<AssetLifecycleAction> {

		private static final long serialVersionUID = -6514262177740324330L;

		// asset lifecycle post status
		private static final String PUBLISHED = "published";
		private static final String VERIFED = "verifed";

		private SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");

		protected AssetDataView(String id, IDataProvider<AssetLifecycleAction> dataProvider, int itemsPerPage) {
			super(id, dataProvider, itemsPerPage);
		}

		protected void populateItem(Item<AssetLifecycleAction> item) {
			final AssetLifecycleAction appHistoryItem = item.getModelObject();

			item.add(new Label("Date", sdf.format(appHistoryItem.getCreateDate())));
			item.add(new Label("Event", appHistoryItem.getEvent()));
			item.add(new Label("PreStatus", appHistoryItem.getPreStatus() != null ? appHistoryItem.getPreStatus().getStatus() : ""));

			final String postStatus = appHistoryItem.getPostStatus() != null ? appHistoryItem.getPostStatus().getStatus() : "";
			item.add(new Label("PostStatus", postStatus));
			item.add(new Label("Operator", appHistoryItem.getOwnerid()));

			BreadCrumbPanelLink actionLink = getActionLink(appHistoryItem, postStatus);
			item.add(actionLink);
		}

		/**
		 * @param appHistoryItem
		 *            AppLifecycleHistory
		 * @param postStatus
		 *            postStatus
		 * @return BreadCrumbPanelLink
		 */
		private BreadCrumbPanelLink getActionLink(final AssetLifecycleAction appHistoryItem, final String postStatus) {
			BreadCrumbPanelLink actionLink = new BreadCrumbPanelLink("actionLink", currentBreadCrumbModel, new IBreadCrumbPanelFactory() {

				private static final long serialVersionUID = -46404690058535632L;

				public BreadCrumbPanel create(String componentId, IBreadCrumbModel breadCrumbModel) {
					Long asset_id = appHistoryItem.getAsset().getId();
					log.debug("appHistoryItem.getAsset().getId() :" + asset_id);
					log.debug("componentId :" + componentId);

					if (VERIFED.equalsIgnoreCase(postStatus)) {
						return new ConfigurationTestReportDetailPanel(componentId, breadCrumbModel, asset_id);
					} else if (PUBLISHED.equalsIgnoreCase(postStatus)) {
						return new ConfigurationAssetApproveDetailPanel(componentId, breadCrumbModel, asset_id);
					} else {
						return null;
					}

				}
			});

			if (VERIFED.equalsIgnoreCase(postStatus)) {
				actionLink.add(new Label("linkName", "View Test Reports"));
			} else if (PUBLISHED.equalsIgnoreCase(postStatus)) {
				actionLink.add(new Label("linkName", "View Comments"));
			} else {
				actionLink.setVisible(false);
			}

			return actionLink;
		}
	}

	public String getTitle() {
		return this.getLocalizer().getString("title", this, "Asset Lifecycle History");
	}

}
