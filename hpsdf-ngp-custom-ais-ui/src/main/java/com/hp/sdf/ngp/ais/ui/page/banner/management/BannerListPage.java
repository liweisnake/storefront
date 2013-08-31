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
package com.hp.sdf.ngp.ais.ui.page.banner.management;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.extensions.markup.html.repeater.data.table.AbstractColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.DefaultDataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.filter.ChoiceFilteredPropertyColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.filter.FilterForm;
import org.apache.wicket.extensions.markup.html.repeater.data.table.filter.FilterToolbar;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.hp.sdf.ngp.ais.ui.page.WicketPage;
import com.hp.sdf.ngp.ais.ui.page.banner.staticbanner.StaticBannerPage;
import com.hp.sdf.ngp.banner.BannerStatus;
import com.hp.sdf.ngp.banner.BannerType;

@SuppressWarnings("unchecked")
public class BannerListPage extends WicketPage {

	@SpringBean
	private BannerDataProvider bannerDataProvider;

	private final DefaultDataTable<BannerProxy> bannerTable;

	private Map<Integer, String> typeDisplayNameMap = new HashMap<Integer, String>();

	private Map<Integer, String> statusDisplayNameMap = new HashMap<Integer, String>();

	private PropertyColumn createColumn(String key, String propertyExpression) {
		return new PropertyColumn(new ResourceModel(key), propertyExpression);
	}

	private AbstractColumn<BannerProxy> createActionsColumn() {
		return new AbstractColumn<BannerProxy>(new Model<String>(getString("banner.bannerAction"))) {
			/**
			 * 
			 */
			private static final long serialVersionUID = -4290663190201490396L;

			// add the UserActionsPanel to the cell item
			public void populateItem(Item<ICellPopulator<BannerProxy>> cellItem, String componentId, IModel<BannerProxy> rowModel) {
				cellItem.add(new BannerListActionPanel(BannerListPage.this, componentId, rowModel));
			}
		};
	}

	private List<IColumn<BannerProxy>> createColumns() {
		List<IColumn<BannerProxy>> columns = new ArrayList<IColumn<BannerProxy>>();
		columns.add(createColumn("banner.bannerName", "name"));
		columns.add(new ChoiceFilteredPropertyColumn<BannerProxy, String>(new ResourceModel("banner.bannerType"), "bannerType", new LoadableDetachableModel<List<? extends String>>() {

			/**
			 * 
			 */
			private static final long serialVersionUID = 2796710475645218964L;

			@Override
			protected List<String> load() {
				List<String> uniqueTypes = new ArrayList<String>();
				typeDisplayNameMap.clear();
				String displayName;
				for (BannerType type : BannerType.values()) {
					displayName = BannerListPage.this.getLocalizer().getString(type.toString(), BannerListPage.this);
					typeDisplayNameMap.put(type.ordinal(), displayName);
					uniqueTypes.add(displayName);
				}
				displayName = BannerListPage.this.getLocalizer().getString("all", BannerListPage.this);
				uniqueTypes.add(0, displayName);
				typeDisplayNameMap.put(-1, displayName);
				return uniqueTypes;
			}
		}) {

			/**
			 * 
			 */
			private static final long serialVersionUID = 2796234475645218964L;

			@Override
			public void populateItem(Item<ICellPopulator<BannerProxy>> cellItem, String componentId, IModel<BannerProxy> rowModel) {
				DetachableBannerModel detachableBannerModel = (DetachableBannerModel) rowModel;
				BannerProxy banner = detachableBannerModel.load();
				cellItem.add(new Label(componentId, BannerListPage.this.getLocalizer().getString(banner.getBannerType(), BannerListPage.this)));
			}
		});
		columns.add(new ChoiceFilteredPropertyColumn<BannerProxy, String>(new ResourceModel("banner.bannerStatus"), "bannerStatus", new LoadableDetachableModel<List<? extends String>>() {

			/**
			 * 
			 */
			private static final long serialVersionUID = -7316302778998037626L;

			@Override
			protected List<String> load() {
				statusDisplayNameMap.clear();
				String displayName;
				List<String> uniqueStatus = new ArrayList<String>();
				for (BannerStatus status : BannerStatus.values()) {
					displayName = BannerListPage.this.getLocalizer().getString(status.toString(), BannerListPage.this);
					statusDisplayNameMap.put(status.ordinal(), displayName);
					uniqueStatus.add(displayName);
				}
				displayName = BannerListPage.this.getLocalizer().getString("all", BannerListPage.this);
				uniqueStatus.add(0, displayName);
				statusDisplayNameMap.put(-1, displayName);
				return uniqueStatus;
			}
		}) {
			/**
			 * 
			 */
			private static final long serialVersionUID = -7316303464998037626L;

			@Override
			public void populateItem(Item<ICellPopulator<BannerProxy>> cellItem, String componentId, IModel<BannerProxy> rowModel) {
				DetachableBannerModel detachableBannerModel = (DetachableBannerModel) rowModel;
				BannerProxy banner = detachableBannerModel.load();
				cellItem.add(new Label(componentId, BannerListPage.this.getLocalizer().getString(banner.getBannerStatus(), BannerListPage.this)));
			}
		});
		bannerDataProvider.setStatusDisplayNameMap(statusDisplayNameMap);
		bannerDataProvider.setTypeDisplayNameMap(typeDisplayNameMap);
		columns.add(createActionsColumn());
		return columns;
	}

	public BannerListPage() {
		super(null);

		final FilterForm form = new FilterForm("filter-form", bannerDataProvider) {

			private static final long serialVersionUID = 1L;

			@Override
			protected void onSubmit() {
				bannerTable.setCurrentPage(0);
			}
		};

		bannerTable = new DefaultDataTable<BannerProxy>("bannerlisttable", createColumns(), bannerDataProvider, 10);
		FilterToolbar toolbar = new FilterToolbar(bannerTable, form, bannerDataProvider);
		bannerTable.addTopToolbar(toolbar);
		form.add(bannerTable);
		add(form);
		Button addBanner;
		form.add(addBanner = new Button("addBanner", new Model<String>(this.getLocalizer().getString("addBanner", BannerListPage.this))) {
			private static final long serialVersionUID = -9873872521343L;

			@Override
			public void onSubmit() {
				setResponsePage(StaticBannerPage.class);
			}
		});
		addBanner.setDefaultFormProcessing(false);
	}
}

// $Id$