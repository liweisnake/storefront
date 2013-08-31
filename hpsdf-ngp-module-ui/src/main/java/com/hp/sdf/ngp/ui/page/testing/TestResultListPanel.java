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

import org.apache.wicket.extensions.breadcrumb.IBreadCrumbModel;
import org.apache.wicket.extensions.breadcrumb.panel.BreadCrumbPanel;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.navigation.paging.PagingNavigator;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.hp.sdf.ngp.api.search.SearchExpression;
import com.hp.sdf.ngp.api.search.Condition.NumberComparer;
import com.hp.sdf.ngp.model.AssetLifecycleAction;
import com.hp.sdf.ngp.model.Status;
import com.hp.sdf.ngp.search.condition.assetbinaryversion.AssetBinaryVersionIdCondition;
import com.hp.sdf.ngp.search.engine.SearchExpressionImpl;
import com.hp.sdf.ngp.service.ApplicationService;
import com.hp.sdf.ngp.ui.provider.BinTestDataProvider;

public class TestResultListPanel extends BreadCrumbPanel {

	private static final long serialVersionUID = -8318404986658936622L;

	private int itemsPerPage = 5;

	private Long binaryId;

	@SpringBean
	private ApplicationService applicationService;

	public TestResultListPanel(String id, IBreadCrumbModel breadCrumbModel, Long binaryId) {
		super(id, breadCrumbModel);
		this.binaryId = binaryId;

		BinTestDataProvider testDataProvider = new BinTestDataProvider(applicationService);
		// List<SearchMetaInfo> searchBy = new ArrayList<SearchMetaInfo>();
		// searchBy.add(new SearchMetaInfo(AppSearchType.SearchByType.BINARYVERSION_ID, this.binaryId.toString()));
		// testDataProvider.setSearchMetaInfo(searchBy);

		SearchExpression searchExpression = new SearchExpressionImpl();
		searchExpression.addCondition(new AssetBinaryVersionIdCondition(this.binaryId, NumberComparer.EQUAL));
		testDataProvider.setSearchExpression(searchExpression);
		BinTestDataView binTestView = new BinTestDataView("tests", testDataProvider, itemsPerPage);
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

			// add tester
			item.add(new Label("tester", binTest.getSubmitterid()));

			item.add(new Label("result", binTest.getResult()));

			SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
			String dateString = "";
			if (binTest.getCompleteDate() != null) {
				dateString = dateFormat.format(binTest.getCompleteDate());
			}
			item.add(new Label("complete.date", dateString));

			item.add(new Label("comments", binTest.getComments()));

		}

	}

	public static Status getTestableStatus() {
		// TODO
		return null;
	}
}

// $Id$