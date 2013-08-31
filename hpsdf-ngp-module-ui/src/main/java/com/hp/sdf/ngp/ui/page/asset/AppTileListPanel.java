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

import org.apache.wicket.Component;
import org.apache.wicket.authorization.strategies.role.Roles;
import org.apache.wicket.authorization.strategies.role.metadata.MetaDataRoleAuthorizationStrategy;
import org.apache.wicket.extensions.breadcrumb.IBreadCrumbModel;
import org.apache.wicket.extensions.breadcrumb.panel.BreadCrumbPanel;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.hp.sdf.ngp.api.search.OrderEnum;
import com.hp.sdf.ngp.api.search.SearchExpression;
import com.hp.sdf.ngp.api.search.orderby.AssetOrderBy;
import com.hp.sdf.ngp.model.Asset;
import com.hp.sdf.ngp.search.engine.SearchExpressionImpl;
import com.hp.sdf.ngp.service.ApplicationService;
import com.hp.sdf.ngp.workflow.AccessType;
import com.hp.sdf.ngp.workflow.AssetLifeCycleEngine;

@SuppressWarnings( { "unchecked" })
public class AppTileListPanel extends BreadCrumbPanel {

	public enum ListType {
		TOPX,

		RECOMMENDED;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1981415410168710557L;

	@SpringBean
	private ApplicationService applicationService;

	@SpringBean
	AssetLifeCycleEngine assetLifeCycleEngine;

	public AppTileListPanel(String id, ListType listType, IBreadCrumbModel breadCrumbModel) {
		super(id, breadCrumbModel);
		breadCrumbModel.setActive(this);
		// add the data view
		this.add(new AppTileDataView("apps", applicationService, listType));
	}

	public String getTitle() {
		String title = this.getLocalizer().getString("title", this, "Applications");

		return title;

	}

	class AppTileDataView extends ListView<Asset> {

		private static final long serialVersionUID = -6514262177740324330L;

		List<Asset> appList = null;

		private ListType listType = null;

		private ApplicationService applicationService;

		private int maxItemsPerPage = 10;

		protected AppTileDataView(String id, ApplicationService applicationService, ListType listType) {
			super(id);
			this.applicationService = applicationService;
			this.listType = listType;
		}

		@Override
		protected void onBeforeRender() {

			SearchExpression searchExpression = null;

			switch (listType) {
			case TOPX:
				searchExpression = new SearchExpressionImpl();
				searchExpression.addOrder(AssetOrderBy.CREATEDATE, OrderEnum.DESC);
				searchExpression.setFirst(0);
				searchExpression.setMax(maxItemsPerPage);
				appList = applicationService.searchAsset(searchExpression);
				break;
			case RECOMMENDED:
				searchExpression = new SearchExpressionImpl();
				searchExpression.addOrder(AssetOrderBy.RECOMMENDORDER, OrderEnum.DESC);
				searchExpression.setFirst(0);
				searchExpression.setMax(maxItemsPerPage);
				appList = applicationService.searchAsset(searchExpression);
				break;
			}
			this.setList(appList);

			super.onBeforeRender();
		}

		@Override
		protected void populateItem(ListItem<Asset> item) {
			final Asset app = item.getModelObject();
			AppTilePanel appTilePanel = new AppTilePanel("appTilePanel", new Model<Asset>(app), AppTileListPanel.this);
			item.add(appTilePanel);
			// apply privilege
			String[] privileges = assetLifeCycleEngine.getAccessPrivilege(app.getId(), AccessType.VIEW);
			Roles roles = new Roles(privileges);
			MetaDataRoleAuthorizationStrategy.authorize(appTilePanel, Component.RENDER, roles.toString());
		}

	};

}
