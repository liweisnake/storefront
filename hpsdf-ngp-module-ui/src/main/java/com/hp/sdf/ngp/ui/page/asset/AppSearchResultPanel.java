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
package com.hp.sdf.ngp.ui.page.asset;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.Component;
import org.apache.wicket.authorization.strategies.role.Roles;
import org.apache.wicket.authorization.strategies.role.metadata.MetaDataRoleAuthorizationStrategy;
import org.apache.wicket.extensions.breadcrumb.IBreadCrumbModel;
import org.apache.wicket.extensions.breadcrumb.panel.BreadCrumbPanel;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.HiddenField;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.hp.sdf.ngp.api.search.OrderEnum;
import com.hp.sdf.ngp.api.search.orderby.AssetOrderBy;
import com.hp.sdf.ngp.common.annotation.Value;
import com.hp.sdf.ngp.model.Asset;
import com.hp.sdf.ngp.service.ApplicationService;
import com.hp.sdf.ngp.ui.WicketSession;
import com.hp.sdf.ngp.ui.common.CustomizePagingNavigator;
import com.hp.sdf.ngp.ui.common.SearchMetaInfo;
import com.hp.sdf.ngp.ui.provider.AppDataProvider;
import com.hp.sdf.ngp.workflow.AccessType;
import com.hp.sdf.ngp.workflow.AssetLifeCycleEngine;

public class AppSearchResultPanel extends BreadCrumbPanel {

	private static final Log log = LogFactory.getLog(AppSearchResultPanel.class);

	@SpringBean
	private ApplicationService applicationService;

	@SpringBean
	AssetLifeCycleEngine assetLifeCycleEngine;

	private int itemsPerPage = 5;

	private AppDataProvider appDataProvider;

	private String criteria;

	public int getItemsPerPage() {
		return itemsPerPage;
	}

	@Value("application.itemsperpage")
	public void setItemsPerPage(int itemsPerPage) {
		this.itemsPerPage = itemsPerPage;
	}

	class AppDataView extends DataView<Asset> {

		private static final long serialVersionUID = -6514262177740324330L;

		protected AppDataView(String id, IDataProvider<Asset> dataProvider, int itemsPerPage) {
			super(id, dataProvider, itemsPerPage);
		}

		@Override
		protected void populateItem(Item<Asset> item) {
			final Asset app = item.getModelObject();
			AppBarPanel appBarPanel = new AppBarPanel("appBarPanel", new Model<Asset>(app), AppSearchResultPanel.this);
			item.add(appBarPanel);
			// apply privilege
			String[] privileges = assetLifeCycleEngine.getAccessPrivilege(app.getId(), AccessType.VIEW);
			Roles roles = new Roles(privileges);
			MetaDataRoleAuthorizationStrategy.authorize(appBarPanel, Component.RENDER, roles.toString());
		}

	};

	@SuppressWarnings("unchecked")
	public AppSearchResultPanel(String id, IBreadCrumbModel breadCrumbModel) {
		super(id, breadCrumbModel);

		appDataProvider = new AppDataProvider(applicationService);
		// initial the AppDataProvider's search criteria
		refreshAppDataProvider();

		Label label = new Label("criteria", new PropertyModel(this, "criteria"));
		label.setEscapeModelStrings(false);
		this.add(label);

		// add sort form
		SortForm sortForm = new SortForm("sortForm");
		sortForm.setMarkupId("sortForm");
		add(sortForm);

		// add the data view
		AppDataView appListView = new AppDataView("apps", appDataProvider, itemsPerPage);
		this.add(appListView);
		add(new CustomizePagingNavigator("navigator", appListView));
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -9055279831986699744L;

	public void refreshAppDataProvider() {

		// update the AppDataProvider's search criteria
		List<SearchMetaInfo> searchMetaInfoList = (List<SearchMetaInfo>) WicketSession.get().getObject(WicketSession.AttributeName.SEARCHMETAINFO.name());
		appDataProvider.setSearchMetaInfo(searchMetaInfoList);

		String keyword = (String) WicketSession.get().getObject(WicketSession.AttributeName.KEYWORD.name());
		appDataProvider.setKeyword(keyword);

		AssetOrderBy assetOrderBy = (AssetOrderBy) WicketSession.get().getObject(WicketSession.AttributeName.ORDERBY.name());
		appDataProvider.setAssetOrderBy(assetOrderBy);
		OrderEnum orderEnum = (OrderEnum) WicketSession.get().getObject(WicketSession.AttributeName.ORDERENUM.name());
		appDataProvider.setOrderEnum(orderEnum);

		criteria = "";

		if (!StringUtils.isEmpty(keyword)) {
			criteria = this.getLocalizer().getString("criteria.keyword", this, "KEYWORD") + ":" + keyword + "&nbsp;|&nbsp;";
		}

		if (searchMetaInfoList != null) {
			for (SearchMetaInfo searchMetaInfo : searchMetaInfoList) {
				String label = this.getString("criteria." + searchMetaInfo.getSearchBy().name());
				criteria += label + ":" + searchMetaInfo.getName() + "&nbsp;|&nbsp;";
			}
		}

		// add the search criteria label
		if (StringUtils.isEmpty(criteria)) {
			criteria = this.getLocalizer().getString("criteria.all", this, "ALL");
		} else {
			criteria = StringUtils.chomp(criteria, "&nbsp;|&nbsp;");
		}

	}

	public String getTitle() {

		return this.getLocalizer().getString("title", this, "Applications");
	}

	@SuppressWarnings("unchecked")
	class SortForm extends Form {

		private static final long serialVersionUID = -1054016753889441641L;

		private String sortBy;

		public SortForm(String id) {
			super(id);

			HiddenField<String> sortField = new HiddenField<String>("sortby", new PropertyModel<String>(this, "sortBy"));
			sortField.setMarkupId("sortby");
			add(sortField);
		}

		@Override
		protected void onSubmit() {
			log.debug("new sort by : " + this.sortBy);

			AssetOrderBy assetOrderBy = null;
			OrderEnum orderEnum = null;
			if (sortBy != null) {
				if (sortBy.equals("name")) {
					assetOrderBy = AssetOrderBy.NAME;
					orderEnum = OrderEnum.ASC;
				} else if (sortBy.equals("rate")) {
					assetOrderBy = AssetOrderBy.RATING;
					orderEnum = OrderEnum.DESC;
				} else if (sortBy.equals("date")) {
					assetOrderBy = AssetOrderBy.CREATEDATE;
					orderEnum = OrderEnum.DESC;
				}

				WicketSession.get().setObject(WicketSession.AttributeName.ORDERBY.name(), assetOrderBy, true);
				WicketSession.get().setObject(WicketSession.AttributeName.ORDERENUM.name(), orderEnum, true);
			}

			appDataProvider.setAssetOrderBy(assetOrderBy);
			appDataProvider.setOrderEnum(orderEnum);

		}

	}

	/**
	 * Cleans search conditions from session
	 */
	public void cleanSearchConditions() {
		WicketSession.get().setObject(WicketSession.AttributeName.KEYWORD.name(), null);
		WicketSession.get().setObject(WicketSession.AttributeName.ORDERBY.name(), null);
		WicketSession.get().setObject(WicketSession.AttributeName.ORDERENUM.name(), null);
		WicketSession.get().setObject(WicketSession.AttributeName.SEARCHMETAINFO.name(), null);
	}
}
