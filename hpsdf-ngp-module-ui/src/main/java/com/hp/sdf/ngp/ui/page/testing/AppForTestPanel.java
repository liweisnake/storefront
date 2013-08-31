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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

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

import com.hp.sdf.ngp.api.search.SearchExpression;
import com.hp.sdf.ngp.api.search.Condition.NumberComparer;
import com.hp.sdf.ngp.api.search.Condition.StringComparer;
import com.hp.sdf.ngp.common.constant.AssetLifecycleConstants;
import com.hp.sdf.ngp.model.Asset;
import com.hp.sdf.ngp.model.AssetBinaryVersion;
import com.hp.sdf.ngp.model.AssetLifecycleAction;
import com.hp.sdf.ngp.model.Category;
import com.hp.sdf.ngp.model.Status;
import com.hp.sdf.ngp.search.condition.assetlifecycleaction.AssetLifecycleActionAssetBinaryVersionIdCondition;
import com.hp.sdf.ngp.search.condition.assetlifecycleaction.AssetLifecycleActionEventCondition;
import com.hp.sdf.ngp.search.condition.assetlifecycleaction.AssetLifecycleActionResultCondition;
import com.hp.sdf.ngp.search.condition.assetlifecycleaction.AssetLifecycleActionSubmitteridCondition;
import com.hp.sdf.ngp.search.engine.SearchExpressionImpl;
import com.hp.sdf.ngp.service.ApplicationService;
import com.hp.sdf.ngp.ui.WicketSession;
import com.hp.sdf.ngp.ui.page.asset.ViewAssetDetailPanel;
import com.hp.sdf.ngp.ui.provider.VersionActionDataProvider;

public class AppForTestPanel extends BreadCrumbPanel {

	private static final long serialVersionUID = -8318404986658936622L;

	private final static Log log = LogFactory.getLog(AppForTestPanel.class);

	private int itemsPerPage = 5;

	@SpringBean
	private ApplicationService applicationService;

	public AppForTestPanel(String id, IBreadCrumbModel breadCrumbModel) {
		super(id, breadCrumbModel);

		VersionActionDataProvider dataProvider = new VersionActionDataProvider(applicationService);
		dataProvider.setEvent(AssetLifecycleConstants.BIN_ACTION_TYPE_REQUEST_TEST);
		AppDataView appView = new AppDataView("apps", dataProvider, itemsPerPage);
		add(appView);

		add(new PagingNavigator("navigator", appView));
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

	class AppDataView extends DataView<AssetLifecycleAction> {

		private static final long serialVersionUID = -6514262177740324330L;

		protected AppDataView(String id, IDataProvider<AssetLifecycleAction> dataProvider, int itemsPerPage) {
			super(id, dataProvider, itemsPerPage);
		}

		@Override
		protected void populateItem(Item<AssetLifecycleAction> item) {
			final AssetLifecycleAction action = item.getModelObject();
			final AssetBinaryVersion version = applicationService.getAssetBinaryById(action.getBinaryVersion().getId());
			final Asset app = applicationService.getAssetByBinaryId(version.getId());

			// add name
			BreadCrumbPanelLink nameLink = new BreadCrumbPanelLink("nameLink", AppForTestPanel.this.getBreadCrumbModel(), new IBreadCrumbPanelFactory() {

				private static final long serialVersionUID = 1916609673876325757L;

				public BreadCrumbPanel create(String componentId, IBreadCrumbModel breadCrumbModel) {
					return new ViewAssetDetailPanel(componentId, breadCrumbModel, app.getId(), AppForTestPanel.this);
				}
			});

			nameLink.add(new Label("appName", app.getName()));
			item.add(nameLink);

			// adds category
			List<Category> appCategories = applicationService.getAllCategoryByAssetId(app.getId(), 0, Integer.MAX_VALUE);
			Category category = appCategories.get(0);
			item.add(new Label("category", category.getName()));

			// adds version
			item.add(new Label("version", version.getVersion()));

			// add release date
			SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
			Calendar c = Calendar.getInstance();
			c.set(Calendar.YEAR, 1900);
			Date latestVerDate = c.getTime();
			latestVerDate = version.getCreateDate();
			item.add(new Label("releaseDate", dateFormat.format(latestVerDate)));

			// add test link
			BreadCrumbPanelLink actionLink = new BreadCrumbPanelLink("action", AppForTestPanel.this.getBreadCrumbModel(), new IBreadCrumbPanelFactory() {

				private static final long serialVersionUID = -3457530201558566218L;

				public BreadCrumbPanel create(String componentId, IBreadCrumbModel breadCrumbModel) {
					return new BinDownloadPanel(componentId, breadCrumbModel, version.getId());
				}
			}) {

				private static final long serialVersionUID = 2272796720009090978L;

				@Override
				public void onClick() {

					// Save binary test instance.
					String userId = WicketSession.get().getUserId();

					SearchExpression searchExpression = new SearchExpressionImpl();
					searchExpression.addCondition(new AssetLifecycleActionSubmitteridCondition(userId, StringComparer.EQUAL, false, false));
					searchExpression.addCondition(new AssetLifecycleActionEventCondition(AssetLifecycleConstants.BIN_ACTION_TYPE_TEST, StringComparer.EQUAL, false, false));
					searchExpression.addCondition(new AssetLifecycleActionResultCondition(AssetLifecycleConstants.BIN_TEST_RESULT_TESTING, StringComparer.EQUAL, false, false));
					searchExpression.addCondition(new AssetLifecycleActionAssetBinaryVersionIdCondition(version.getId(), NumberComparer.EQUAL));
					searchExpression.setFirst(0);
					searchExpression.setMax(Integer.MAX_VALUE);
					List<AssetLifecycleAction> ongoingTest = applicationService.getAssetLifecycleAction(searchExpression);

					AssetLifecycleAction binTest = null;
					if (ongoingTest != null && ongoingTest.size() > 0) {
						// There should only one test of a binary version for each tester
						binTest = ongoingTest.get(0);
					} else {
						AssetBinaryVersion bin = new AssetBinaryVersion();
						bin.setId(version.getId());
						binTest = new AssetLifecycleAction();
						binTest.setAsset(app);
						binTest.setBinaryVersion(bin);
						binTest.setCreateDate(new Date());
						binTest.setEvent(AssetLifecycleConstants.BIN_ACTION_TYPE_TEST);
						binTest.setOwnerid(userId);
						binTest.setSubmitterid(userId);
						binTest.setResult(AssetLifecycleConstants.BIN_TEST_RESULT_TESTING);
						applicationService.saveOrUpdateAssetLifecycleAction(binTest);

						Status status = applicationService.getStatusByName(AssetLifecycleConstants.BIN_TEST_RESULT_TESTING);
						version.setStatus(status);
						applicationService.updateBinaryVersion(version);
					}
					

					super.onClick();
				}
			};
			item.add(actionLink);
		}

	}

	

	public static Status getTestableStatus() {
		// TODO
		return null;
	}
}

// $Id$