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
package com.hp.sdf.ngp.ui.page.configuration;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.extensions.breadcrumb.IBreadCrumbModel;
import org.apache.wicket.extensions.breadcrumb.panel.BreadCrumbPanel;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.navigation.paging.PagingNavigator;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.hp.sdf.ngp.model.Asset;
import com.hp.sdf.ngp.service.ApplicationService;
import com.hp.sdf.ngp.ui.common.SearchCondition;
import com.hp.sdf.ngp.ui.provider.AssetSearchResultProvider;

public class ConfigurationRecommendPanel extends BreadCrumbPanel {

	// @SpringBean
	// private ApplicationService applicationService;

	private static final long serialVersionUID = -5159405158230718321L;

	private static final Log log = LogFactory.getLog(ConfigurationRecommendPanel.class);

	public ConfigurationRecommendPanel(String id, IBreadCrumbModel breadCrumbModel, SearchCondition searchCondition) {
		super(id, breadCrumbModel);
		this.add(new FeedbackPanel("feedBack"));
		this.add(new ConfigurationRecommendForm("configurationRecommendForm", searchCondition));
	}

	class AssetDataView extends DataView<Asset> {

		/**
		 * serialVersionUID.
		 */
		private static final long serialVersionUID = 1L;

		/**
		 * apps subscibeMap.
		 */
		private Map<String, Asset> subscibeMap = new HashMap<String, Asset>();

		/**
		 * apps' new RecommendOrder map.
		 */
		private Map<String, Long> newOrderMap = new HashMap<String, Long>();

		protected AssetDataView(String id, IDataProvider<Asset> dataProvider, int itemsPerPage) {
			super(id, dataProvider, itemsPerPage);
		}

		protected void populateItem(Item<Asset> paramItem) {
			final Asset app = (Asset) paramItem.getModelObject();

			CheckBox check = new CheckBox("checkbox", new IModel<Boolean>() {

				private static final long serialVersionUID = -40187841977803002L;

				public Boolean getObject() {

					Asset o = subscibeMap.get(app.getId().toString());
					return null == o ? false : true;
				}

				public void setObject(Boolean object) {
					if (object) {
						subscibeMap.put(app.getId().toString(), app);
					}
				}

				public void detach() {
				}
			});
			paramItem.add(check);

			final Label labelRecommendOrder;
			// RecommendedAsset recommendedAsset = applicationService.getRecommendedAssetByAssetId(app.getId());
			if (app.getRecommendOrder() != null) {
				labelRecommendOrder = new Label("recommendOrder", "" + app.getRecommendOrder());
			} else {
				labelRecommendOrder = new Label("recommendOrder", "");
			}

			final Label labelName = new Label("name", app.getName() + "");
			final Label labelDeveloper = new Label("userId", app.getAuthorid() + "");
			final Label labelBrief = new Label("brief", app.getBrief() + "");
			paramItem.add(labelRecommendOrder);
			paramItem.add(labelName);
			paramItem.add(labelDeveloper);
			paramItem.add(labelBrief);

			TextField<String> newOrder = new TextField<String>("newOrder", new IModel<String>() {

				private static final long serialVersionUID = 6297714565920521727L;

				public String getObject() {
					Long o = newOrderMap.get(app.getId().toString());

					if (null == o) {
						return "";
					}

					return o + "";
				}

				public void setObject(String object) {
					if (StringUtils.isNotEmpty(object)) {

						boolean flag = false;
						try {
							Long.parseLong(object);
						} catch (Exception exception) {
							flag = true;
						}

						if (!flag) {
							newOrderMap.put(app.getId().toString(), Long.parseLong(object));
						}
					}
				}

				public void detach() {
				}
			});
			paramItem.add(newOrder);
		}
	}

	public final class ConfigurationRecommendForm extends Form<Void> {

		private static final long serialVersionUID = -2911828426140550821L;

		/**
		 * applicationService.
		 */
		@SpringBean
		private ApplicationService applicationService;

		private boolean groupSelected;

		public boolean isGroupSelected() {
			return groupSelected;
		}

		public void setGroupSelected(boolean groupSelected) {
			this.groupSelected = groupSelected;
		}

		private static final int ITEMS_PER_PAGE = 5;

		public ConfigurationRecommendForm(String id, SearchCondition searchCondition) {
			super(id);

			CheckBox groupselector = new CheckBox("groupselector", new PropertyModel<Boolean>(this, "groupSelected"));
			groupselector.setMarkupId("groupselector");
			this.add(groupselector);

			AssetSearchResultProvider assetSearchResultProvider = new AssetSearchResultProvider(applicationService, searchCondition);
			final AssetDataView dataView = new AssetDataView("appList", assetSearchResultProvider, ITEMS_PER_PAGE);
			this.add(dataView);

			this.add(new PagingNavigator("navigator", dataView));

			Button change = getChangeButton(dataView);
			this.add(change);

			Button clean = getCleanButton(dataView);
			this.add(clean);
		}

		/**
		 * @param dataView
		 * @return
		 */
		private Button getCleanButton(final AssetDataView dataView) {

			Button clean = new Button("clean") {

				private static final long serialVersionUID = 7498410103218179963L;

				public void onSubmit() {

					if (dataView.subscibeMap != null) {
						Set<String> appKeySet = dataView.subscibeMap.keySet();
						log.debug("appKeySet.SIZE : " + appKeySet.size());
						for (String appId : appKeySet) {
							Asset app = dataView.subscibeMap.get(appId);
							app.setRecommendOrder(0L);
							// RecommendedAsset recommendedAsset = applicationService.getRecommendedAssetByAssetId(app.getId());
							//
							// if (null != recommendedAsset) {
							// recommendedAsset.setRecommendedOrder(0L);
							// applicationService.updateRecommendedAsset(recommendedAsset);
							// } else {
							// recommendedAsset = new RecommendedAsset();
							// recommendedAsset.setAsset(app);
							// recommendedAsset.setCreateDate(new Date());
							// recommendedAsset.setDueDate(new Date());
							// recommendedAsset.setRecommendedOrder(0L);
							// applicationService.saveRecommendedAsset(recommendedAsset);
							//
							// Set<RecommendedAsset> recommendedAssetSet = new HashSet<RecommendedAsset>();
							// recommendedAssetSet.add(recommendedAsset);
							// app.setRecommendedAssets(recommendedAssetSet);
							// }

							applicationService.updateAsset(app);
						}

						// clean the groupselector checked status and the data
						setGroupSelected(false);
						dataView.subscibeMap.clear();
						dataView.newOrderMap.clear();
					}
				}
			};
			return clean;
		}

		/**
		 * @param dataView
		 * @return
		 */
		private Button getChangeButton(final AssetDataView dataView) {

			Button change = new Button("change") {

				private static final long serialVersionUID = 1730928004268437272L;

				public void onSubmit() {

					log.debug("groupSelected :" + groupSelected);
					if (dataView.subscibeMap != null) {

						Set<String> appKeySet = dataView.subscibeMap.keySet();
						Set<String> orderKeySet = dataView.newOrderMap.keySet();
						log.debug("appKeySet.SIZE : " + appKeySet.size());
						log.debug("orderKeySet.SIZE : " + orderKeySet.size());

						for (String appId : appKeySet) {
							Asset app = dataView.subscibeMap.get(appId);
							Long newRecOrder = dataView.newOrderMap.get(appId);
							if (null == newRecOrder)
								continue;
							log.debug("Setting new order :" + newRecOrder + " for app : appId = " + appId + " app name =" + app.getName());
							app.setRecommendOrder(newRecOrder);
							// RecommendedAsset recommendedAsset = applicationService.getRecommendedAssetByAssetId(app.getId());
							// if (null != recommendedAsset) {
							// recommendedAsset.setRecommendedOrder(newRecOrder);
							// applicationService.updateRecommendedAsset(recommendedAsset);
							// } else {
							// recommendedAsset = new RecommendedAsset();
							// recommendedAsset.setAsset(app);
							// recommendedAsset.setCreateDate(new Date());
							// recommendedAsset.setDueDate(new Date());
							// recommendedAsset.setRecommendedOrder(newRecOrder);
							// applicationService.saveRecommendedAsset(recommendedAsset);
							//
							// Set<RecommendedAsset> recommendedAssetSet = new HashSet<RecommendedAsset>();
							// recommendedAssetSet.add(recommendedAsset);
							// app.setRecommendedAssets(recommendedAssetSet);
							// }

							applicationService.updateAsset(app);

						}

						// clean the groupselector checked status and the data
						setGroupSelected(false);
						dataView.subscibeMap.clear();
						dataView.newOrderMap.clear();
					}
				}
			};
			return change;
		}

		public final void onSubmit() {
		}
	}

	public String getTitle() {
		return this.getLocalizer().getString("title", this, "Asset management");
	}

}