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
package com.hp.sdf.ngp.ui.page.myportal;

import java.text.SimpleDateFormat;

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
import com.hp.sdf.ngp.ui.WicketSession;
import com.hp.sdf.ngp.ui.page.asset.AppEditPanel;
import com.hp.sdf.ngp.ui.page.asset.MyAppDetailPanel;
import com.hp.sdf.ngp.ui.provider.MyAppDataProvider;

public class MyAppPanel extends BreadCrumbPanel {

	private int itemsPerPage = 5;

	public int getItemsPerPage() {
		return itemsPerPage;
	}

	@Value("application.itemsperpage")
	public void setItemsPerPage(int itemsPerPage) {
		this.itemsPerPage = itemsPerPage;
	}

	@SpringBean
	ApplicationService appService;

	public ApplicationService getAppService() {
		return appService;
	}

	public void setAppService(ApplicationService appService) {
		this.appService = appService;
	}

	private SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");

	public MyAppPanel(String id, IBreadCrumbModel breadCrumbModel) {
		super(id, breadCrumbModel);

		MyAppDataProvider appDataProvider = new MyAppDataProvider(WicketSession.get().getUserId(),appService);

		add(new BreadCrumbPanelLink("createAppLink", 
				MyAppPanel.this.getBreadCrumbModel(),
				new IBreadCrumbPanelFactory() {
					public BreadCrumbPanel create(String componentId,
							IBreadCrumbModel breadCrumbModel) {
						return new AppEditPanel(componentId,
								breadCrumbModel, 
								MyAppPanel.this);
					}
				}));
		
		add(new AppDataView("apps", appDataProvider, itemsPerPage));
	}

	class AppDataView extends DataView<Asset> {

		private static final long serialVersionUID = -6514262177740324330L;

		protected AppDataView(String id, IDataProvider<Asset> dataProvider,
				int itemsPerPage) {
			super(id, dataProvider, itemsPerPage);
		}

		@Override
		protected void populateItem(Item<Asset> item) {
			final Asset asset = item.getModelObject();
			// add name
			BreadCrumbPanelLink nameLink = new BreadCrumbPanelLink("nameLink",
					MyAppPanel.this.getBreadCrumbModel(),
					new IBreadCrumbPanelFactory() {
						public BreadCrumbPanel create(String componentId,
								IBreadCrumbModel breadCrumbModel) {
							return new MyAppDetailPanel(componentId,
									breadCrumbModel, asset.getId(), MyAppPanel.this);
						}
					});

			nameLink.add(new Label("appName", asset.getName()));
			item.add(nameLink);

			// add date time
			item.add(new Label("createDate", sdf.format(asset.getCreateDate())));

			item.add(new Label("brief", asset.getBrief()));

		}

	}

	public String getTitle() {
		return this.getLocalizer().getString("title", this, "");
	}
}

// $Id$