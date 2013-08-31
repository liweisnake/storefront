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
package com.hp.sdf.ngp.ui.page.api;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.extensions.breadcrumb.IBreadCrumbModel;
import org.apache.wicket.extensions.breadcrumb.panel.BreadCrumbPanel;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.ExternalLink;
import org.apache.wicket.markup.html.link.PopupSettings;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.hp.sdf.ngp.model.ApiKey;
import com.hp.sdf.ngp.model.ServiceSubscription;
import com.hp.sdf.ngp.service.ApiService;
import com.hp.sdf.ngp.ui.WicketApplication;
import com.hp.sdf.ngp.ui.WicketSession;
import com.hp.sdf.ngp.ui.provider.SubscriptionDataProvider;

public class SubscribedApiPanel extends BreadCrumbPanel {

	private static final long serialVersionUID = -243743911127150859L;

	@SpringBean
	private ApiService apiService;

	@SpringBean(name = "wicketApplication")
	private WicketApplication wicketApplication;

	private int itemsPerPage;

	public int getItemsPerPage() {
		return itemsPerPage;
	}

	public void setItemsPerPage(int itemsPerPage) {
		this.itemsPerPage = itemsPerPage;
	}

	/**
	 * SubscribedApiPanel
	 * 
	 * @param id
	 * @param breadCrumbModel
	 */
	public SubscribedApiPanel(String id, IBreadCrumbModel breadCrumbModel) {
		super(id, breadCrumbModel);
		this.setItemsPerPage(wicketApplication.getItemsPerPage());
		String user = WicketSession.get().getUserId();
		SubscriptionDataProvider dataProvider = new SubscriptionDataProvider(this.apiService, user);
		SubscriptionDataView dataView = new SubscriptionDataView("subscriptions", dataProvider, itemsPerPage);
		this.add(dataView);
	}

	public String getTitle() {
		return this.getLocalizer().getString("title", this, "Subscribed API");
	}

	/**
	 * SubscriptionDataView
	 */
	class SubscriptionDataView extends DataView<ApiKey> {

		private static final long serialVersionUID = 757869080803569521L;

		protected SubscriptionDataView(String id, IDataProvider<ApiKey> dataProvider, int itemsPerPage) {
			super(id, dataProvider, itemsPerPage);
		}

		@Override
		protected void populateItem(final Item<ApiKey> item) {
			final ApiKey appKey = item.getModelObject();
			item.add(new Label("appKeyID", appKey.getId().toString()));
			if (appKey.getServiceSubscriptions() == null && appKey.getServiceSubscriptions().isEmpty())
				return;

			item.add(new ServiceListView("apis", new ArrayList<ServiceSubscription>(appKey.getServiceSubscriptions())));
			item.add(new ServiceListView("urls", new ArrayList<ServiceSubscription>(appKey.getServiceSubscriptions())));
			item.add(new Label("username", appKey.getSgName()));
			item.add(new Label("password", appKey.getSgPassword()));
			item.add(new AttributeModifier("class", true, new AbstractReadOnlyModel() {
				public Object getObject() {
					return (item.getIndex() % 2 == 1) ? "even" : "dark";
				}
			}));
		}

	}

	/**
	 * ServiceListView
	 */
	class ServiceListView extends ListView<ServiceSubscription> {

		private static final long serialVersionUID = -2082691458083208472L;
		private String id;

		public ServiceListView(String id, List<ServiceSubscription> list) {
			super(id, list);
			this.id = id;
		}

		@Override
		protected void populateItem(ListItem<ServiceSubscription> item) {
			final ServiceSubscription ss = (ServiceSubscription) item.getModelObject();
			if (id.equals("apis"))
				item.add(new Label("apiName", ss.getService().getName()));
			PopupSettings popupSettings = new PopupSettings(PopupSettings.RESIZABLE | PopupSettings.SCROLLBARS).setHeight(500).setWidth(700);
			if (id.equals("urls"))
				item.add(new ExternalLink("accessURL", ss.getService().getBrokerServiceUrl(), "Access URL").setPopupSettings(popupSettings));
		}

	};

}

// $Id$