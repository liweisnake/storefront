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
package com.hp.sdf.ngp.ui.page.statistic;

import java.math.BigDecimal;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.extensions.breadcrumb.IBreadCrumbModel;
import org.apache.wicket.extensions.breadcrumb.panel.BreadCrumbPanel;
import org.apache.wicket.extensions.breadcrumb.panel.BreadCrumbPanelLink;
import org.apache.wicket.extensions.breadcrumb.panel.IBreadCrumbPanelFactory;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.hp.sdf.ngp.common.annotation.Value;
import com.hp.sdf.ngp.model.Asset;
import com.hp.sdf.ngp.service.ApplicationService;
import com.hp.sdf.ngp.service.PurchaseService;
import com.hp.sdf.ngp.ui.common.Constant;
import com.hp.sdf.ngp.ui.common.CustomizePagingNavigator;
import com.hp.sdf.ngp.ui.common.Tools;
import com.hp.sdf.ngp.ui.common.TopSalesCondition;
import com.hp.sdf.ngp.ui.page.asset.AppDetailPanel;
import com.hp.sdf.ngp.ui.provider.TopSalesDataProvider;

public class TopSalesPanel extends BreadCrumbPanel {

	private static final long serialVersionUID = -8524244192134679105L;

	private static Log log = LogFactory.getLog(TopSalesPanel.class);

	@Value("application.itemsperpage")
	private int itemsPerPage = Constant.DEFALUT_PER_PAGE_COUNT;

	@SpringBean
	ApplicationService applicationService;

	@SpringBean
	PurchaseService purchaseService;

	private TopSalesDataView dataView;

	public int getItemsPerPage() {
		return itemsPerPage;
	}

	public void setItemsPerPage(int itemsPerPage) {
		this.itemsPerPage = itemsPerPage;
	}

	public ApplicationService getApplicationService() {
		return applicationService;
	}

	public void setApplicationService(ApplicationService applicationService) {
		this.applicationService = applicationService;
	}

	public TopSalesPanel(String id, IBreadCrumbModel breadCrumbModel) {
		super(id, breadCrumbModel);

		TopSalesDataProvider dataProvider = new TopSalesDataProvider(applicationService, new TopSalesCondition());

		dataView = new TopSalesDataView("assets", dataProvider, itemsPerPage);
		add(dataView);

		TopSalesSearchPanel searchPanel = new TopSalesSearchPanel("searchPanel", dataView, applicationService);
		add(searchPanel);

		add(new CustomizePagingNavigator("navigator", dataView));
	}

	class TopSalesDataView extends DataView<Asset> {

		private static final long serialVersionUID = -5360828448061050211L;

		private Date beginDate;

		private Date endDate;

		protected TopSalesDataView(String id, IDataProvider<Asset> dataProvider, int itemsPerPage) {
			super(id, dataProvider, itemsPerPage);
		}

		@Override
		protected void populateItem(Item<Asset> item) {
			final Asset asset = item.getModelObject();

			BigDecimal totalAmount = purchaseService.calculateAssetPrice(asset.getId(), beginDate, endDate);

			BreadCrumbPanelLink nameLink = new BreadCrumbPanelLink("nameLink", TopSalesPanel.this.getBreadCrumbModel(), new IBreadCrumbPanelFactory() {

				private static final long serialVersionUID = 5387594107373306034L;

				public BreadCrumbPanel create(String componentId, IBreadCrumbModel breadCrumbModel) {
					return new AppDetailPanel(componentId, breadCrumbModel, asset.getId(), TopSalesPanel.this);
				}
			});
			nameLink.add(new Label("name", Tools.getValue(asset.getName())));
			item.add(nameLink);

			item.add(new Label("description", Tools.getValue(asset.getDescription())));

			item.add(new Label("createDate", Tools.getValue(asset.getCreateDate())));

			item.add(new Label("currentVersion", Tools.getValue(asset.getCurrentVersion())));

			item.add(new Label("averageUserRating", Tools.getValue(asset.getAverageUserRating())));

			item.add(new Label("totalAmount", Tools.getValue(totalAmount)));

			item.add(new Label("downloadCount", Tools.getValue(asset.getDownloadCount())));

		}

		public void updateModel(TopSalesCondition condition) {
			TopSalesDataProvider dataProvider = (TopSalesDataProvider) this.getDataProvider();
			dataProvider.setCondition(condition);
			beginDate = condition.getBeginDate();
			endDate = condition.getEndDate();
		}

		public Date getBeginDate() {
			return beginDate;
		}

		public void setBeginDate(Date beginDate) {
			this.beginDate = beginDate;
		}

		public Date getEndDate() {
			return endDate;
		}

		public void setEndDate(Date endDate) {
			this.endDate = endDate;
		}
	}

	public String getTitle() {
		return this.getLocalizer().getString("title", this, "Top Sales List");
	}
}