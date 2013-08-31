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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.beanutils.ConstructorUtils;
import org.apache.commons.lang.ClassUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.extensions.breadcrumb.IBreadCrumbModel;
import org.apache.wicket.extensions.breadcrumb.panel.BreadCrumbPanel;
import org.apache.wicket.extensions.breadcrumb.panel.BreadCrumbPanelLink;
import org.apache.wicket.extensions.breadcrumb.panel.IBreadCrumbPanelFactory;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.Radio;
import org.apache.wicket.markup.html.form.RadioGroup;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.navigation.paging.PagingNavigator;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.hp.sdf.ngp.model.Asset;
import com.hp.sdf.ngp.model.Category;
import com.hp.sdf.ngp.model.Status;
import com.hp.sdf.ngp.model.Tag;
import com.hp.sdf.ngp.service.ApplicationService;
import com.hp.sdf.ngp.ui.common.AssistantActionOpt;
import com.hp.sdf.ngp.ui.common.PromptPanel;
import com.hp.sdf.ngp.ui.common.SearchCondition;
import com.hp.sdf.ngp.ui.common.SelectClassOption;
import com.hp.sdf.ngp.ui.common.SelectOption;
import com.hp.sdf.ngp.ui.provider.AssetSearchResultProvider;

public class ConfigurationAssetSearchResultPanel extends BreadCrumbPanel {

	private static final long serialVersionUID = 2847203327312187476L;

	private static final Log log = LogFactory.getLog(ConfigurationAssetSearchResultPanel.class);

	private PromptPanel promptPanel;

	public ConfigurationAssetSearchResultPanel(String id, final IBreadCrumbModel breadCrumbModel, SearchCondition searchCondition) {
		super(id, breadCrumbModel);
		this.add(new FeedbackPanel("feedBack"));
		this.add(new ConfigurationAssetSearchResultForm("configurationAssetSearchResultForm", breadCrumbModel, searchCondition));

		promptPanel = new PromptPanel("promptPanel", getLocalizer().getString("title.prompt", this, "Prompt"), StringUtils.EMPTY, null, StringUtils.EMPTY);
		add(promptPanel);
	}

	public final class ConfigurationAssetSearchResultForm extends Form<Void> {

		private static final long serialVersionUID = -4951597258253103907L;

		private List<Category> categoryList;

		private List<Status> statuses;

		private Map<String, Asset> subscribeMap = new HashMap<String, Asset>();

		private SelectOption statusOption;

		private SelectOption categoryOption;

		private String addTags;

		private String deleteTags;

		private RadioGroup<Integer> radioGroup;

		private int itemsPerPage = 5;

		private boolean groupSelected;

		private SearchCondition searchCondition;

		@SpringBean
		ApplicationService appService;

		public boolean isGroupSelected() {
			return groupSelected;
		}

		public void setGroupSelected(boolean groupSelected) {
			this.groupSelected = groupSelected;
		}

		private void init() {
			statuses = appService.getAllStatus();
			log.debug("statuses.size :" + statuses.size());

			categoryList = appService.getAllCategory(0, Integer.MAX_VALUE);
			log.debug("categoryList.size :" + categoryList.size());
		}

		public ConfigurationAssetSearchResultForm(String id, IBreadCrumbModel breadCrumbModel, SearchCondition searchCondition) {
			super(id);
			this.searchCondition = searchCondition;
			init();

			CheckBox groupselector = new CheckBox("groupselector", new PropertyModel<Boolean>(this, "groupSelected"));
			groupselector.setMarkupId("groupselector");
			add(groupselector);

			AssetSearchResultProvider appProvider = new AssetSearchResultProvider(appService, searchCondition);
			log.debug("AssetSearchResultProvider.size() :" + appProvider.size());

			AssetDataView listView = new AssetDataView("listview", breadCrumbModel, appProvider, itemsPerPage);
			add(listView);
			add(new PagingNavigator("navigator", listView));

			radioGroup = new RadioGroup<Integer>("radioGroup", new Model<Integer>());
			add(radioGroup);

			ListView<Integer> batchOperations = new ListView<Integer>("batch", getOperators()) {

				private static final long serialVersionUID = 1449545631564595323L;

				protected void populateItem(ListItem<Integer> item) {
					item.add(new Radio<Integer>("radio", item.getModel()));
					item.add(new Label("name", Operators.getById(item.getModelObject()).value));
				}
			};
			radioGroup.add(batchOperations);
			radioGroup.setRequired(true);

			ChoiceRenderer<SelectOption> choiceRenderer = new ChoiceRenderer<SelectOption>("value", "key");
			DropDownChoice<SelectOption> categoryOperator = new DropDownChoice<SelectOption>("categoryOperator", new PropertyModel<SelectOption>(this, "categoryOption"), new AbstractReadOnlyModel<List<? extends SelectOption>>() {

				private static final long serialVersionUID = 2240894787528668989L;

				public List<? extends SelectOption> getObject() {
					List<SelectOption> selects = new ArrayList<SelectOption>();
					for (Category category : categoryList) {
						selects.add(new SelectOption(category.getId(), category.getName()));
					}
					return selects;
				}
			}, choiceRenderer);

			add(categoryOperator);

			DropDownChoice<SelectOption> statusOperator = new DropDownChoice<SelectOption>("statusOperator", new PropertyModel<SelectOption>(this, "statusOption"), new AbstractReadOnlyModel<List<? extends SelectOption>>() {

				private static final long serialVersionUID = 870155208273057654L;

				public List<? extends SelectOption> getObject() {
					List<SelectOption> selects = new ArrayList<SelectOption>();
					for (Status status : statuses) {
						selects.add(new SelectOption(status.getId(), status.getStatus()));
					}
					return selects;
				}
			}, choiceRenderer);
			add(statusOperator);

			TextField<String> addTagOperator = new TextField<String>("addTagOperator", new PropertyModel<String>(this, "addTags"));
			add(addTagOperator);

			TextField<String> deleteTagOperator = new TextField<String>("deleteTagOperator", new PropertyModel<String>(this, "deleteTags"));
			add(deleteTagOperator);

			Button submit = new Button("submit");
			add(submit);
		}

		/**
		 * @return operators
		 */
		private List<Integer> getOperators() {
			List<Integer> operators = new ArrayList<Integer>();

			operators.add(1);
			operators.add(2);
			operators.add(3);
			operators.add(4);
			operators.add(5);
			operators.add(6);
			operators.add(7);

			return operators;
		}

		protected void onSubmit() {

			int radioIndex = (Integer) this.radioGroup.getModelObject();
			log.debug("radioIndex :" + radioIndex);

			List<Long> assetIds = new ArrayList<Long>();
			log.debug("subscribeMap size :" + subscribeMap.size());
			if (subscribeMap != null && subscribeMap.size() != 0) {
				Set<String> keys = subscribeMap.keySet();
				for (String key : keys) {
					assetIds.add(subscribeMap.get(key).getId());
				}
			}

			switch (radioIndex) {
			case 1:
				if (categoryOption != null) {
					log.debug("Enter batchUpdate category, assetIds size : " + assetIds.size());
					appService.batchUpdateCategory(assetIds, categoryOption.getKey());
				}
				break;
			case 2:
				if (statusOption != null) {
					log.debug("Enter batchUpdateAppStatus.......");
					appService.batchUpdateAssetStatus(assetIds, statusOption.getKey());
				}
				break;
			case 3:
				log.debug("Enter batchAddTags.......");
				appService.batchAddTags(assetIds, addTags);
				break;
			case 4:
				log.debug("Enter batchDeleteTagRelations.......");
				appService.batchDeleteTagRelations(assetIds, deleteTags, null);
				break;
			case 5:
				log.debug("Enter deleteAsset.......");
				for (Long assetID : assetIds) {
					appService.deleteAsset(assetID);
				}
				break;
			case 6:
				log.debug("Enter export.......");
				break;
			case 7:
				log.debug("Enter Recommend.......");
				activate(new IBreadCrumbPanelFactory() {

					private static final long serialVersionUID = 3840521373650730173L;

					public BreadCrumbPanel create(String componentId, IBreadCrumbModel breadCrumbModel) {
						return new ConfigurationRecommendPanel(componentId, breadCrumbModel, searchCondition);
					}
				});
				break;
			}

			log.debug("Set SelectAll checkbox to not checked.");
			setGroupSelected(false);
			log.debug("Clear the subscribeMap.");
			subscribeMap.clear();

			log.debug("setModelObject of radioGroup with null.");
			this.radioGroup.setModelObject(null);
		}

		class AssetDataView extends DataView<Asset> {

			private static final long serialVersionUID = -6514262177740324330L;

			private IBreadCrumbModel breadCrumbModel;

			private List<SelectClassOption> selects;

			/**
			 * appid and Action map.
			 */
			private Map<Long, AssistantActionOpt> assistantMap;

			private void init() {
				assistantMap = new HashMap<Long, AssistantActionOpt>();
				selects = getSelectClassOption();
			}

			protected AssetDataView(String id, IBreadCrumbModel breadCrumbModel, IDataProvider<Asset> dataProvider, int itemsPerPage) {
				super(id, dataProvider, itemsPerPage);
				this.breadCrumbModel = breadCrumbModel;
				init();
			}

			protected void populateItem(Item<Asset> item) {
				final Asset asset = (Asset) item.getModelObject();

				if (assistantMap.get(asset.getId()) == null) {
					assistantMap.put(asset.getId(), new AssistantActionOpt(selects.get(0)));
				}

				Label labelAssetName = new Label("assetName", asset.getName());
				item.add(labelAssetName);

				Label labelRecommendOrder;
				// if (appService.getRecommendedAssetByAssetId(asset.getId()) != null) {
				// labelRecommendOrder = new Label("order", appService.getRecommendedAssetByAssetId(asset.getId()).getRecommendedOrder() + "");
				// } else {
				// labelRecommendOrder = new Label("order", "");
				// }
				if (asset.getRecommendOrder() != null) {
					labelRecommendOrder = new Label("order", asset.getRecommendOrder() + "");
				} else {
					labelRecommendOrder = new Label("order", "");
				}

				item.add(labelRecommendOrder);

				StringBuilder sb = new StringBuilder();
				List<Tag> tags = appService.getAllTagsByAsset(asset.getId(),null, 0, Integer.MAX_VALUE);
				if (tags != null) {
					for (Tag tag : tags) {
						sb.append(tag.getName()).append(" ");
					}
				}

				Label labelTag = new Label("tag", sb.toString());
				item.add(labelTag);

				List<Category> appCategories = appService.getAllCategoryByAssetId(asset.getId(), 0, Integer.MAX_VALUE);
				// Category appCategory = null;
				StringBuilder sbCategory = new StringBuilder();
				if (appCategories != null && appCategories.size() > 0) {
					// appCategory = appCategories.get(0);
					for (Category category : appCategories) {
						sbCategory.append(category.getName() + " | ");
					}
				}
				String categoryName = StringUtils.chomp(sbCategory.toString(), " | ");
				// Label categoryLabel = new Label("category", categoryName);
				item.add(new Label("category", categoryName));

				item.add(new Label("status", asset.getStatus() != null ? asset.getStatus().getStatus() : "null"));

				ChoiceRenderer<SelectClassOption> actionChoiceRenderer = new ChoiceRenderer<SelectClassOption>("value", "className");
				DropDownChoice<SelectClassOption> action = new DropDownChoice<SelectClassOption>("action", new PropertyModel<SelectClassOption>(assistantMap.get(asset.getId()), "actionOpt"), new AbstractReadOnlyModel<List<? extends SelectClassOption>>() {

					private static final long serialVersionUID = 5985111754685248886L;

					public List<? extends SelectClassOption> getObject() {
						return selects;
					}
				}, actionChoiceRenderer) {

					private static final long serialVersionUID = 478761583678673005L;

					protected boolean wantOnSelectionChangedNotifications() {
						return true;
					}

					protected void onSelectionChanged(final SelectClassOption newSelection) {
						assistantMap.get(asset.getId()).setActionOpt(newSelection);
						log.debug("select option changed :" + newSelection.getClassName());
					}

				};

				action.add(new AjaxFormComponentUpdatingBehavior("onchange") {

					private static final long serialVersionUID = -2732213220978826475L;

					protected void onUpdate(AjaxRequestTarget target) {
					}
				});

				item.add(action);

				BreadCrumbPanelLink link = new BreadCrumbPanelLink("go", breadCrumbModel, new IBreadCrumbPanelFactory() {

					private static final long serialVersionUID = 1L;

					@SuppressWarnings("unchecked")
					public BreadCrumbPanel create(String componentId, IBreadCrumbModel breadCrumbModel) {

						if (null == appService.getAsset(asset.getId())) {
							ConfigurationAssetSearchResultPanel.this.promptPanel.setMessage(getLocalizer().getString("msg.error.delete", ConfigurationAssetSearchResultPanel.this, "This asset is not exist!"));
							ConfigurationAssetSearchResultPanel.this.promptPanel.show();
							return ConfigurationAssetSearchResultPanel.this;
						}

						String className = ClassUtils.getPackageName(this.getClass()) + "." + assistantMap.get(asset.getId()).getActionOpt().getClassName();
						log.debug("invoke method start, classname is " + className + ", appId is " + asset.getId());

						Class[] classes = new Class[] { String.class, IBreadCrumbModel.class, Long.class };
						Object[] objs = new Object[] { componentId, breadCrumbModel, asset.getId() };

						try {
							return (BreadCrumbPanel) ConstructorUtils.invokeExactConstructor(ClassUtils.getClass(className), objs, classes);
						} catch (Exception e) {
							log.error("create panel error. Panel name is " + className, e);
						}
						return null;
					}
				});
				item.add(link);

				item.add(new CheckBox("select", new IModel<Boolean>() {

					private static final long serialVersionUID = 7865238665776348241L;

					public Boolean getObject() {

						if (subscribeMap != null && subscribeMap.size() != 0) {
							Asset o = subscribeMap.get(asset.getId().toString());
							return null == o ? false : true;
						}

						return false;
					}

					public void setObject(Boolean object) {
						if (object) {
							subscribeMap.put(asset.getId().toString(), asset);
						}
					}

					public void detach() {
					}
				}));

			}
		}

	}

	public String getTitle() {
		return this.getLocalizer().getString("title", this, "Asset search result");
	}

	public List<SelectClassOption> getSelectClassOption() {

		List<SelectClassOption> selects = new ArrayList<SelectClassOption>();

		selects.add(new SelectClassOption("ConfigurationAssetManagementPanel", "View detail"));
		selects.add(new SelectClassOption("ConfigurationAssetLifecycleHistoryPanel", "View history"));
		selects.add(new SelectClassOption("ConfigurationAssetApproveDetailPanel", "View approve detail"));
		selects.add(new SelectClassOption("ConfigurationTestReportDetailPanel", "View test report"));

		return selects;
	}

	public enum Operators {
		Status(1, "Update Category:"), Category(2, "Update Status to:"), AddTag(3, "Add Tag:"), DelTag(4, "Delete Tag"), Del(5, "Delete"), Export(6, "Export"), Recommend(7, "Recommend");

		public final int id;
		public final String value;

		Operators(int id, String value) {
			this.id = id;
			this.value = value;
		}

		public int getId() {
			return id;
		}

		public String getValue() {
			return value;
		}

		public static Operators getById(int id) {
			for (Operators o : Operators.values()) {
				if (o.getId() == id) {
					return o;
				}
			}
			return null;
		}
	}

}
