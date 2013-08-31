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
package com.hp.sdf.ngp.ui.page.testing;

import java.util.List;

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

import com.hp.sdf.ngp.api.search.SearchExpression;
import com.hp.sdf.ngp.api.search.Condition.StringComparer;
import com.hp.sdf.ngp.common.constant.AssetLifecycleConstants;
import com.hp.sdf.ngp.model.Asset;
import com.hp.sdf.ngp.model.AssetBinaryVersion;
import com.hp.sdf.ngp.model.AssetLifecycleAction;
import com.hp.sdf.ngp.model.Platform;
import com.hp.sdf.ngp.model.Status;
import com.hp.sdf.ngp.search.condition.assetlifecycleaction.AssetLifecycleActionOwneridCondition;
import com.hp.sdf.ngp.search.condition.assetlifecycleaction.AssetLifecycleActionResultCondition;
import com.hp.sdf.ngp.search.engine.SearchExpressionImpl;
import com.hp.sdf.ngp.service.ApplicationService;
import com.hp.sdf.ngp.ui.WicketSession;
import com.hp.sdf.ngp.ui.common.Tools;
import com.hp.sdf.ngp.ui.provider.BinTestDataProvider;

public class MyTestingPanel extends BreadCrumbPanel {

	private static final long serialVersionUID = -8318404986658936622L;

	private int itemsPerPage = 5;

	@SpringBean
	private ApplicationService applicationService;

	public MyTestingPanel(String id, IBreadCrumbModel breadCrumbModel) {
		super(id, breadCrumbModel);

		add(new BreadCrumbPanelLink("appForTestLink", MyTestingPanel.this, AppForTestPanel.class));

		BinTestDataProvider testDataProvider = new BinTestDataProvider(applicationService);
		String userId = WicketSession.get().getUserId();
		// Map<String, String> properties = new HashMap<String, String>();
		// properties.put("result", AssetLifecycleConstants.BIN_TEST_RESULT_TESTING);
		// properties.put("ownerid", userId);
		// testDataProvider.setSearchProperties(properties);

		SearchExpression searchExpression = new SearchExpressionImpl();
		searchExpression.addCondition(new AssetLifecycleActionResultCondition(AssetLifecycleConstants.BIN_TEST_RESULT_TESTING, StringComparer.EQUAL, false, false));
		searchExpression.addCondition(new AssetLifecycleActionOwneridCondition(userId, StringComparer.EQUAL, false, false));
		testDataProvider.setSearchExpression(searchExpression);
		BinTestDataView binTestView = new BinTestDataView("bins", testDataProvider, itemsPerPage);
		add(binTestView);

		add(new PagingNavigator("navigator", binTestView));
	}

	public int getItemsPerPage() {
		return itemsPerPage;
	}

	public void setItemsPerPage(int itemsPerPage) {
		this.itemsPerPage = itemsPerPage;
	}

	public String getTitle() {
		return this.getLocalizer().getString("title", this, "Applications for Test");
	}

	class BinTestDataView extends DataView<AssetLifecycleAction> {

		private static final long serialVersionUID = -7377648080534121488L;

		protected BinTestDataView(String id, IDataProvider<AssetLifecycleAction> dataProvider, int itemsPerPage) {
			super(id, dataProvider, itemsPerPage);
		}

		@Override
		protected void populateItem(Item<AssetLifecycleAction> item) {
			final AssetLifecycleAction binTest = item.getModelObject();
			final AssetBinaryVersion bin = applicationService.getAssetBinaryById(binTest.getBinaryVersion().getId());

			// add name
			BreadCrumbPanelLink nameLink = new BreadCrumbPanelLink("nameLink", MyTestingPanel.this.getBreadCrumbModel(), new IBreadCrumbPanelFactory() {
				public BreadCrumbPanel create(String componentId, IBreadCrumbModel breadCrumbModel) {
					return new TestReportPanel(componentId, breadCrumbModel, bin);
				}
			});
			Asset app = applicationService.getAssetByBinaryId(bin.getId());
			nameLink.add(new Label("appName", app.getName()));
			item.add(nameLink);

			item.add(new Label("version", bin.getVersion()));

			List<Platform> list = applicationService.getPlatformByAssetId(app.getId());
			item.add(new Label("platform", Tools.getPlatfromNameFromList(list)));

			item.add(new Label("brief", app.getBrief()));

		}
	}

	public static Status getTestableStatus() {
		// TODO
		return null;
	}
}

// $Id$