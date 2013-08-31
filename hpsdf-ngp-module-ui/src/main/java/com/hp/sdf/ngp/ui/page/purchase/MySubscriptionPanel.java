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
package com.hp.sdf.ngp.ui.page.purchase;

import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.extensions.breadcrumb.IBreadCrumbModel;
import org.apache.wicket.extensions.breadcrumb.panel.BreadCrumbPanel;
import org.apache.wicket.extensions.breadcrumb.panel.BreadCrumbPanelLink;
import org.apache.wicket.extensions.breadcrumb.panel.IBreadCrumbPanelFactory;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.AbstractLink;
import org.apache.wicket.markup.html.link.ExternalLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.navigation.paging.PagingNavigator;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.hp.sdf.ngp.common.annotation.Value;
import com.hp.sdf.ngp.common.constant.AssetConstants;
import com.hp.sdf.ngp.common.constant.AssetSubscriptionStatus;
import com.hp.sdf.ngp.common.util.Utils;
import com.hp.sdf.ngp.manager.ApplicationManager;
import com.hp.sdf.ngp.manager.OTADownloadManager;
import com.hp.sdf.ngp.model.Asset;
import com.hp.sdf.ngp.model.AssetBinaryVersion;
import com.hp.sdf.ngp.model.Platform;
import com.hp.sdf.ngp.model.PurchaseHistory;
import com.hp.sdf.ngp.model.UserDownloadHistory;
import com.hp.sdf.ngp.service.ApplicationService;
import com.hp.sdf.ngp.service.PurchaseService;
import com.hp.sdf.ngp.ui.WicketApplication;
import com.hp.sdf.ngp.ui.WicketSession;
import com.hp.sdf.ngp.ui.common.Tools;
import com.hp.sdf.ngp.ui.page.asset.AppDetailPanel;
import com.hp.sdf.ngp.ui.provider.AppSubscriptionDataProvider;

public class MySubscriptionPanel extends BreadCrumbPanel {

	private static final long serialVersionUID = 1L;

	private final static Log log = LogFactory.getLog(MySubscriptionPanel.class);

	private int itemsPerPage = 10;

	@SpringBean(name = "wicketApplication")
	private WicketApplication wicketApplication;

	@SpringBean
	private ApplicationManager applicationManager;

	@SpringBean
	private OTADownloadManager oTADownloadManager;

	@SpringBean
	ApplicationService appService;

	@SpringBean
	PurchaseService purchaseService;

	private SubscriptionDataView dataView;

	private AppSubscriptionDataProvider dataProvider;

	public int getItemsPerPage() {
		return itemsPerPage;
	}

	@Value("application.itemsperpage")
	public void setItemsPerPage(int itemsPerPage) {
		this.itemsPerPage = itemsPerPage;
	}

	public ApplicationService getAppService() {
		return appService;
	}

	public void setAppService(ApplicationService appService) {
		this.appService = appService;
	}

	public PurchaseService getPurchaseService() {
		return purchaseService;
	}

	public void setPurchaseService(PurchaseService purchaseService) {
		this.purchaseService = purchaseService;
	}

	public MySubscriptionPanel(String id, IBreadCrumbModel breadCrumbModel) {
		super(id, breadCrumbModel);

		String userId = WicketSession.get().getUserId();
		SearchCondition condition = new SearchCondition();
		condition.setUserId(userId);
		dataProvider = new AppSubscriptionDataProvider(purchaseService, condition);

		dataView = new SubscriptionDataView("subscriptions", dataProvider, itemsPerPage);
		add(dataView);
		add(new PagingNavigator("navigator", dataView));

		HistorySearchPanel searchPanel = new HistorySearchPanel("searchPanel", dataView);
		add(searchPanel);

	}

	class SubscriptionDataView extends DataView<PurchaseHistory> {

		private static final long serialVersionUID = -6514262177740324330L;

		protected SubscriptionDataView(String id, IDataProvider<PurchaseHistory> dataProvider, int itemsPerPage) {
			super(id, dataProvider, itemsPerPage);
		}

		@Override
		protected void populateItem(Item<PurchaseHistory> item) {
			final PurchaseHistory subscription = (PurchaseHistory) item.getModelObject();
			long assetId = subscription.getAsset().getId();
			final Asset asset = appService.getAsset(assetId);
			AssetBinaryVersion binary = null;
			binary = appService.getAssetBinaryById(asset.getCurrentVersionId());

			// add name
			BreadCrumbPanelLink nameLink = new BreadCrumbPanelLink("nameLink", MySubscriptionPanel.this.getBreadCrumbModel(), new IBreadCrumbPanelFactory() {
				public BreadCrumbPanel create(String componentId, IBreadCrumbModel breadCrumbModel) {
					return new AppDetailPanel(componentId, breadCrumbModel, asset.getId(), MySubscriptionPanel.this);
				}
			});

			nameLink.add(new Label("app.name", asset.getName()));
			item.add(nameLink);

			Label binPlatform, binVersion;
			if (binary != null) {
				List<Platform> list = appService.getPlatformByAssetId(asset.getId());
				binPlatform = new Label("platform", Tools.getPlatfromNameFromList(list));
				binVersion = new Label("version", binary.getVersion());

			} else {
				binPlatform = new Label("platform", "");
				binVersion = new Label("version", "");
			}
			item.add(binPlatform);
			item.add(binVersion);

			if (binary != null) {
				final AssetBinaryVersion downloadBinary = binary;
				// The link for download, which aslo works in Portlet
				AbstractLink downloadLink = null;
				String userId = WicketSession.get().getUserId();
				// TODO update device serial
				// String binaryURI =
				// applicationManager.retrieveDownloadLink(assetId, userId,
				// "PC");
				String binaryURI = oTADownloadManager.retrieveOTADownloadURI(assetId, null, userId, null);
				log.debug("binary URI is: " + binaryURI);

				// update asset download count
				asset.setDownloadCount(asset.getDownloadCount() + 1);
				appService.updateAsset(asset);

				// add userDownloadHistory
				UserDownloadHistory userDownloadHistory = new UserDownloadHistory();
				userDownloadHistory.setAsset(asset);
				userDownloadHistory.setDownloadDate(new Date());
				userDownloadHistory.setUserid(userId);
				userDownloadHistory.setVersion("");
				userDownloadHistory.setStatus(AssetSubscriptionStatus.PURCHASED.toString());
				appService.saveUserDownloadHistory(userDownloadHistory);

				downloadLink = new ExternalLink("binaryLink", binaryURI);
				item.add(downloadLink);
				if (!AssetSubscriptionStatus.PURCHASED.toString().equals(subscription.getStatus())) {
					downloadLink.setEnabled(false);
				}
			} else {
				Link downloadLink = new Link("binaryLink") {

					@Override
					public void onClick() {
						// TODO Auto-generated method stub

					}
				};
				item.add(downloadLink);
				downloadLink.setEnabled(false);
			}

			// adds price
			Label price = new Label("price", AssetConstants.priceFormat.format(subscription.getPaidPrice()));
			item.add(price);

			String date = Utils.dateToLongString(subscription.getPurchaseDate());
			// adds purchase date
			Label purchaseDate = new Label("purchase.date", date);
			item.add(purchaseDate);

			// adds status
			Label status = new Label("purchase.status", subscription.getStatus());
			item.add(status);
		}

		public void updateModel(SearchCondition condition) {
			Log log = LogFactory.getLog(MySubscriptionPanel.class);
			log.debug("Updates application subscription view model. conditions: ");
			log.debug(condition.toString());
			AppSubscriptionDataProvider dataProvider = (AppSubscriptionDataProvider) this.getDataProvider();
			dataProvider.setSearchCondition(condition);
		}
	}

	public String getTitle() {
		return this.getLocalizer().getString("title", this, "");
	}
}

// $Id$