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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.ConstructorUtils;
import org.apache.commons.lang.ClassUtils;
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
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.navigation.paging.PagingNavigator;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.hp.sdf.ngp.common.exception.DeleteFileFailureException;
import com.hp.sdf.ngp.model.AssetBinaryVersion;
import com.hp.sdf.ngp.model.Status;
import com.hp.sdf.ngp.service.ApplicationService;
import com.hp.sdf.ngp.ui.common.AssistantActionOpt;
import com.hp.sdf.ngp.ui.common.SearchCondition;
import com.hp.sdf.ngp.ui.common.SelectClassOption;
import com.hp.sdf.ngp.ui.common.SelectOption;
import com.hp.sdf.ngp.ui.provider.BinaryVersionSearchResultProvider;

/**
 * 
 * ConfigurationBinarySearchResultPanel.
 * 
 */
public class ConfigurationBinarySearchResultPanel extends BreadCrumbPanel {

	private static final long serialVersionUID = -7232269054125254507L;

	private static final Log log = LogFactory.getLog(ConfigurationBinarySearchResultPanel.class);

	public ConfigurationBinarySearchResultPanel(String id, IBreadCrumbModel breadCrumbModel, SearchCondition searchCondition) {
		super(id, breadCrumbModel);
		this.add(new FeedbackPanel("feedBack"));
		this.add(new ConfigurationBinarySearchResultForm("configurationBinarySearchResultForm", breadCrumbModel, searchCondition));
	}

	public final class ConfigurationBinarySearchResultForm extends Form<Void> {

		private static final long serialVersionUID = 9129633384837663877L;

		private int itemsPerPage = 5;

		@SpringBean
		ApplicationService appService;

		private List<Status> statuses;

		private Map<String, AssetBinaryVersion> subscribeMap;

		private SelectOption statusOption;

		private RadioGroup<Integer> radioGroup;

		/**
		 * groupSelected.
		 */
		private boolean groupSelected;

		public boolean isGroupSelected() {
			return groupSelected;
		}

		public void setGroupSelected(boolean groupSelected) {
			this.groupSelected = groupSelected;
		}

		private void init() {
			statuses = appService.getAllStatus();
			subscribeMap = new HashMap<String, AssetBinaryVersion>();
		}

		public ConfigurationBinarySearchResultForm(String id, IBreadCrumbModel breadCrumbModel, SearchCondition searchCondition) {
			super(id);
			init();

			BinaryVersionSearchResultProvider binaryVersionSearchResultProvider = new BinaryVersionSearchResultProvider(appService, searchCondition);
			BinaryDataView binaryDataView = new BinaryDataView("listview", breadCrumbModel, binaryVersionSearchResultProvider, itemsPerPage);

			CheckBox groupselector = new CheckBox("groupselector", new PropertyModel<Boolean>(this, "groupSelected"));
			groupselector.setMarkupId("groupselector");
			add(groupselector);

			add(binaryDataView);
			add(new PagingNavigator("navigator", binaryDataView));

			radioGroup = new RadioGroup<Integer>("radioGroup", new Model<Integer>());
			add(radioGroup);

			ListView<Integer> batchOperations = new ListView<Integer>("batch", getOperators()) {

				private static final long serialVersionUID = -7683040209364046262L;

				protected void populateItem(ListItem<Integer> item) {
					item.add(new Radio<Integer>("radio", item.getModel()));
					item.add(new Label("name", Operators.getById(item.getModelObject()).value));
				}
			};

			radioGroup.add(batchOperations);
			radioGroup.setRequired(true);
			add(radioGroup);

			ChoiceRenderer<SelectOption> choiceRenderer = new ChoiceRenderer<SelectOption>("value", "key");
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

			Button submit = new Button("submit");
			add(submit);
		}

		/**
		 * @return operators.
		 */
		private List<Integer> getOperators() {
			List<Integer> operators = new ArrayList<Integer>();
			operators.add(1);
			operators.add(2);
			return operators;
		}

		protected void onSubmit() {
			List<Long> binaryVersionIds = new ArrayList<Long>();
			if (subscribeMap != null && subscribeMap.size() != 0) {
				Collection<AssetBinaryVersion> binaryVersions = subscribeMap.values();
				for (AssetBinaryVersion bv : binaryVersions) {
					binaryVersionIds.add(bv.getId());
				}
			}

			int radioIndex = radioGroup.getModelObject();
			log.debug("radioIndex :" + radioIndex);

			switch (radioIndex) {
			case 1:
				if (statusOption != null) {
					log.debug("Running batch Update Binary Status.");
					appService.batchUpdateAssetBinaryStatus(binaryVersionIds, statusOption.getKey());
				}
				break;

			case 2:
				log.debug("Enter deleteAssetBinary.");
				for (Long binaryVersionId : binaryVersionIds) {
					try {
						appService.deleteAssetBinary(binaryVersionId);
					} catch (DeleteFileFailureException deleteFileFailureException) {
						log.error("deleteFileFailureException for binaryVersionId[" + binaryVersionId + "]: \n" + deleteFileFailureException);
					}
				}
				break;
			}

			log.debug("Set SelectAll checkbox to not checked.");
			setGroupSelected(false);
			log.debug("Clear the subscribeMap.");
			subscribeMap.clear();

			log.debug("setModelObject of radioGroup with null.");
			this.radioGroup.setModelObject(null);
		}

		public class BinaryDataView extends DataView<AssetBinaryVersion> {

			private static final long serialVersionUID = 1L;

			private IBreadCrumbModel breadCrumbModel;

			private List<SelectClassOption> actionSelects = getSelectClassOption();

			/**
			 * appBinaryid and action class map.
			 */
			private Map<Long, AssistantActionOpt> assistantMap = new HashMap<Long, AssistantActionOpt>();

			public BinaryDataView(String id, IBreadCrumbModel breadCrumbModel, BinaryVersionSearchResultProvider binaryVersionSearchResultProvider, int itemsPerPage) {
				super(id, binaryVersionSearchResultProvider, itemsPerPage);
				this.breadCrumbModel = breadCrumbModel;
			}

			protected void populateItem(Item<AssetBinaryVersion> item) {
				final AssetBinaryVersion binaryVersion = (AssetBinaryVersion) item.getModelObject();

				if (assistantMap.get(binaryVersion.getId()) == null) {
					assistantMap.put(binaryVersion.getId(), new AssistantActionOpt(actionSelects.get(0)));
				}

				CheckBox binaryCheck = new CheckBox("select", new IModel<Boolean>() {

					private static final long serialVersionUID = -9075665234282358349L;

					public Boolean getObject() {

						if (subscribeMap != null & subscribeMap.size() > 0) {
							if (subscribeMap.containsValue(binaryVersion)) {
								return true;
							}
						}

						return false;
					}

					public void setObject(Boolean object) {
						if (object) {
							subscribeMap.put(binaryVersion.getId().toString(), binaryVersion);
						}
					}

					public void detach() {
					}
				});
				item.add(binaryCheck);

				Label labelAssetName = new Label("assetName", binaryVersion.getFileName());
				item.add(labelAssetName);

				Label labelVersion = new Label("version", binaryVersion.getVersion());
				item.add(labelVersion);

				// need to change the binaryPlatform
				String binaryPlatform = "";
				Label labelPlatform = new Label("platform", binaryPlatform);
				item.add(labelPlatform);

				Label labelDate = new Label("date", new SimpleDateFormat("yyyy-MM-dd").format(binaryVersion.getCreateDate()));
				item.add(labelDate);

				Label labelStatus = new Label("status", null == binaryVersion.getStatus() ? "null" : binaryVersion.getStatus().getStatus());
				item.add(labelStatus);

				ChoiceRenderer<SelectClassOption> choiceRenderer = new ChoiceRenderer<SelectClassOption>("value", "className");
				DropDownChoice<SelectClassOption> action = new DropDownChoice<SelectClassOption>("action", new PropertyModel<SelectClassOption>(assistantMap.get(binaryVersion.getId()), "actionOpt"), new AbstractReadOnlyModel<List<? extends SelectClassOption>>() {

					private static final long serialVersionUID = 5985111754685248886L;

					public List<? extends SelectClassOption> getObject() {
						return actionSelects;
					}
				}, choiceRenderer) {

					private static final long serialVersionUID = 8015651209038835538L;

					protected boolean wantOnSelectionChangedNotifications() {
						return true;
					}

					protected void onSelectionChanged(final SelectClassOption newSelection) {
						assistantMap.get(binaryVersion.getId()).setActionOpt(newSelection);
						log.debug("select option changed :" + newSelection.getClassName());
					}

				};

				action.add(new AjaxFormComponentUpdatingBehavior("onchange") {

					private static final long serialVersionUID = 1505320914130936546L;

					protected void onUpdate(AjaxRequestTarget target) {
					}
				});
				item.add(action);

				BreadCrumbPanelLink link = new BreadCrumbPanelLink("go", breadCrumbModel, new IBreadCrumbPanelFactory() {

					private static final long serialVersionUID = 1L;

					@SuppressWarnings("unchecked")
					public BreadCrumbPanel create(String componentId, IBreadCrumbModel breadCrumbModel) {
						String className = ClassUtils.getPackageName(this.getClass()) + "." + assistantMap.get(binaryVersion.getId()).getActionOpt().getClassName();

						Class[] classes = new Class[] { String.class, IBreadCrumbModel.class, Long.class };

						Object[] objs = new Object[] { componentId, breadCrumbModel, binaryVersion.getId() };

						log.debug("invoke method start, classname is " + className + ", appId is " + binaryVersion.getId());
						try {
							return (BreadCrumbPanel) ConstructorUtils.invokeExactConstructor(ClassUtils.getClass(className), objs, classes);
						} catch (Exception e) {
							log.error("create panel error. Panel name is " + className, e);
						}
						return null;
					}
				});
				item.add(link);
			}
		}
	}

	public String getTitle() {
		return this.getLocalizer().getString("title", this, "Binary search result");
	}

	public List<SelectClassOption> getSelectClassOption() {
		List<SelectClassOption> selects = new ArrayList<SelectClassOption>();
		selects.add(new SelectClassOption("ConfigurationBinaryLifecycleHistoryPanel", "View history"));
		return selects;
	}

	public enum Operators {
		Status(1, "Update Status to:"), DelTag(2, "Delete");

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
