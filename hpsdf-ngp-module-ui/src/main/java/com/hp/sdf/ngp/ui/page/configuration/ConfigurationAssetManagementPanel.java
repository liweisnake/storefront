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
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.extensions.breadcrumb.IBreadCrumbModel;
import org.apache.wicket.extensions.breadcrumb.panel.BreadCrumbPanel;
import org.apache.wicket.extensions.markup.html.form.DateTextField;
import org.apache.wicket.extensions.yui.calendar.DatePicker;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.ListMultipleChoice;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
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
import com.hp.sdf.ngp.model.Comments;
import com.hp.sdf.ngp.model.Status;
import com.hp.sdf.ngp.model.Tag;
import com.hp.sdf.ngp.service.ApplicationService;
import com.hp.sdf.ngp.ui.common.Constant;
import com.hp.sdf.ngp.ui.common.PromptPanel;
import com.hp.sdf.ngp.ui.common.SelectOption;
import com.hp.sdf.ngp.ui.common.Tools;

public class ConfigurationAssetManagementPanel extends BreadCrumbPanel {

	private static final long serialVersionUID = 6696014648286290110L;

	private static final Log log = LogFactory.getLog(ConfigurationAssetManagementPanel.class);

	public ConfigurationAssetManagementPanel(String id, IBreadCrumbModel breadCrumbModel, Long appId) {
		super(id, breadCrumbModel);

		this.add(new FeedbackPanel("feedBack"));
		this.add(new ConfigurationAssetManagementForm("configurationAssetManagementForm", appId));
	}

	public final class ConfigurationAssetManagementForm extends Form<Void> {

		private static final long serialVersionUID = 2685916095960706866L;

		// one more blank space
		private static final String BLANK_SPACE_REGEX = "\\s{1,}";

		/**
		 * applicationService injection.
		 */
		@SpringBean
		private ApplicationService applicationService;

		private ChoiceRenderer<SelectOption> choiceRenderer;

		private PromptPanel promptPanel;
		/**
		 * categories.
		 */
		private List<Category> categories;

		/**
		 * allStatus.
		 */
		private List<Status> allStatus;

		/**
		 * categoryOption.
		 */
		private SelectOption categoryOption;

		private List<Category> categoryList = new ArrayList<Category>();
		private List<Category> categoryListDel = new ArrayList<Category>();

		/**
		 * statusOption.
		 */
		private SelectOption statusOption;

		/**
		 * input tags.
		 */
		private String tags;

		/**
		 * userDefinedMetadata1.
		 */
		private String userDefinedMetadata1;

		/**
		 * userDefinedMetadata2.
		 */
		private String userDefinedMetadata2;

		/**
		 * the app need to be managed.
		 */
		private Asset app;

		/**
		 * assetId.
		 */
		private Long assetId;

		/**
		 * commentsDeleteList.
		 */
		private ArrayList<Comments> commentsDeleteList;

		/**
		 * groupSelected.
		 */
		private boolean groupSelected;

		/**
		 * updateStatusLabel.
		 */
		private Label updateStatusLabel = null;

		private void init() {
			choiceRenderer = new ChoiceRenderer<SelectOption>("value", "key");
			commentsDeleteList = new ArrayList<Comments>();
			categories = applicationService.getAllCategory(0, Integer.MAX_VALUE);
			allStatus = applicationService.getAllStatus();
		}

		public boolean isGroupSelected() {
			return groupSelected;
		}

		public void setGroupSelected(boolean groupSelected) {
			this.groupSelected = groupSelected;
		}

		public ConfigurationAssetManagementForm(String id, Long assetId) {
			super(id);
			init();

			/*
			 * promptPanel = new PromptPanel("promptPanel", Constant.PROMPT.warning.toString(), "You don't select any comment!", null, StringUtils.EMPTY); promptPanel.setOutputMarkupId(true); promptPanel.add(Constant.STYLE_HIDE); add(promptPanel);
			 */

			this.assetId = assetId;
			app = applicationService.getAsset(assetId);

			if (null == app) {
				log.error("The app is null, can not manage the app because can not get the app.");
				return;
			}

			TextField<String> assetName = new TextField<String>("assetName", new PropertyModel<String>(app, "name"));
			add(assetName);

			TextField<String> assetAuthor = new TextField<String>("assetAuthor", new PropertyModel<String>(app, "authorid"));
			assetAuthor.setEnabled(false);
			add(assetAuthor);

			log.debug("app rating :" + app.getAverageUserRating());
			TextField<Double> averageRating = new TextField<Double>("averageRating", new PropertyModel<Double>(app, "averageUserRating"));
			averageRating.setEnabled(false);
			add(averageRating);

			DateTextField createDate = new DateTextField("createDate", new PropertyModel<Date>(app, "createDate"), Constant.DATE_PATTERN);
			createDate.add(new DatePicker());
			add(createDate);

			DateTextField updateDate = new DateTextField("updateDate", new PropertyModel<Date>(app, "updateDate"), Constant.DATE_PATTERN);
			updateDate.add(new DatePicker());
			add(updateDate);

			TextField<String> brief = new TextField<String>("brief", new PropertyModel<String>(app, "brief"));
			add(brief);

			// DropDownChoice<SelectOption> categoryDropdown =
			// getCategoryDropdown(choiceRenderer);
			// add(categoryDropdown);
			// List<Category> categorys =
			// applicationService.getAllCategoryByAssetId(app.getId(), 0,
			// Integer.MAX_VALUE);
			List<Category> categorys = applicationService.getAllCategory(0, Integer.MAX_VALUE);
			categoryList = applicationService.getAllCategoryByAssetId(app.getId(), 0, Integer.MAX_VALUE);
			categoryListDel = applicationService.getAllCategoryByAssetId(app.getId(), 0, Integer.MAX_VALUE);
			ChoiceRenderer<List<Category>> categoryChoiceRenderer = new ChoiceRenderer<List<Category>>("name", "id");
			ListMultipleChoice categoryChoice = new ListMultipleChoice("category", new PropertyModel(this, "categoryList"), categorys, categoryChoiceRenderer);
			add(categoryChoice);

			DropDownChoice<SelectOption> statusDropdown = getStatusDropdown(choiceRenderer);
			add(statusDropdown);

			TextField<String> tags = new TextField<String>("tags", new PropertyModel<String>(this, "tags"));
			setAppTagContent(tags);
			add(tags);

			TextField<String> userDefinedMetadata1 = new TextField<String>("userDefinedMetadata1", new PropertyModel<String>(this, "userDefinedMetadata1"));
			add(userDefinedMetadata1);

			TextField<String> userDefinedMetadata2 = new TextField<String>("userDefinedMetadata2", new PropertyModel<String>(this, "userDefinedMetadata2"));
			add(userDefinedMetadata2);

			TextArea<String> description = new TextArea<String>("description", new PropertyModel<String>(app, "description"));
			add(description);

			AjaxButton update = new AjaxButton("update") {

				private static final long serialVersionUID = -969121572393876675L;

				protected void onSubmit(AjaxRequestTarget paramAjaxRequestTarget, Form<?> paramForm) {
					log.debug("Calling updateApp.");
					updateApp();

					setGroupSelected(false);

					/*
					 * updateStatusLabel.setDefaultModelObject("update successfully." ); paramAjaxRequestTarget.addComponent(updateStatusLabel);
					 */
				}
			};
			add(update);

			updateStatusLabel = new Label("updateStatus", "");
			updateStatusLabel.setOutputMarkupId(true);
			add(updateStatusLabel);

			CheckBox groupselector = new CheckBox("groupselector", new PropertyModel<Boolean>(this, "groupSelected"));
			groupselector.setMarkupId("groupselector");
			add(groupselector);

			AssetCommentsProvider assetCommentsProvider = new AssetCommentsProvider(applicationService, assetId);
			AssetCommentsDataView assetCommentsDataView = new AssetCommentsDataView("assetCommentsDataView", assetCommentsProvider);
			add(assetCommentsDataView);
			add(new PagingNavigator("navigator", assetCommentsDataView));

			Button delete = new Button("delete") {

				private static final long serialVersionUID = 8439670785147921333L;

				public final void onSubmit() {
					log.debug("Calling delete commments.");
					deleteComments();

					setGroupSelected(false);
				}

			};
			add(delete);
		}

		private void deleteComments() {
			log.debug("commentsDeleteList.size :" + commentsDeleteList.size());

			/**
			 * if (commentsDeleteList.size() < 1) { promptPanel.add(Constant.STYLE_SHOW); return; }
			 */

			if (commentsDeleteList.size() < 1) {
				this.error("Please select one comment at least.");
				return;
			}

			ArrayList<Long> commentsDeleteListID = new ArrayList<Long>();
			for (Comments c : commentsDeleteList) {
				commentsDeleteListID.add(c.getId());
			}

			applicationService.batchDeleteComments(commentsDeleteListID);
		}

		/**
		 * update app.
		 */
		private void updateApp() {

			// updateAppCategory();
			Set<Long> categoryIdSet = Tools.getCategoryIdSet(categoryList);
			applicationService.associateCategory(app.getId(), app.getCurrentVersionId(), categoryIdSet);

			updateAppStatus();

			updateAppTags();

			log.debug("userDefinedMetadata1 :" + userDefinedMetadata1);
			log.debug("userDefinedMetadata2 :" + userDefinedMetadata2);

			StringBuffer sb = new StringBuffer();
			sb.append("name :" + app.getName() + ",");
			sb.append("rating :" + app.getAverageUserRating() + ",");
			sb.append("createDate :" + app.getCreateDate() + ",");
			sb.append("updateDate :" + app.getUpdateDate() + ",");
			sb.append("brief :" + app.getBrief() + ",");
			sb.append("description :" + app.getDescription() + ",");

			log.debug("Updated App information :\n" + sb);

			applicationService.updateAsset(app);

			// delete the category
			for (Category category : categoryListDel) {
				if (!categoryIdSet.contains(category.getId()))
					applicationService.disassociateCategory(app.getId(), app.getCurrentVersionId(), category.getId());
			}

		}

		/**
		 * @param tags
		 */
		private void setAppTagContent(TextField<String> tags) {
			StringBuffer sb = new StringBuffer();

			List<Tag> allTags = applicationService.getAllTagsByAsset(assetId, null,0, Integer.MAX_VALUE);

			log.debug("allTags size :" + allTags.size());
			for (Tag tag : allTags) {
				log.debug("tag.name :" + tag.getName());
				sb.append(tag.getName() + " ");
			}

			tags.setDefaultModelObject(sb);
		}

		/**
		 * updateAppTags.
		 */
		private void updateAppTags() {
			// update tags
			log.debug("Input tags is :" + tags);

			// delete the old tags
			List<Tag> tagList = applicationService.getAllTagsByAsset(assetId,null, 0, Integer.MAX_VALUE);
			if (tagList != null && tagList.size() > 0) {
				for (Tag tag : tagList) {
					applicationService.deleteTag(assetId, null,tag.getId());
				}
			}

			// add the new tags
			if (StringUtils.isNotEmpty(tags)) {
				String[] tagArray = tags.split(BLANK_SPACE_REGEX);
				log.debug("tagArray size is :" + tagArray.length);

				for (String tag : tagArray) {
					if (StringUtils.isNotEmpty(tag)) {
						Tag newTag = new Tag();
						newTag.setName(tag);
						newTag.setDescription(tag);

						// save the new tag
						applicationService.saveTag(assetId, newTag);
					}
				}
			}
		}

		/**
		 * updateAppStatus.
		 */
		private void updateAppStatus() {
			// update status
			if (statusOption != null) {
				log.debug("Selected status id is :" + statusOption.getKey());

				Status selectedStatus = null;
				for (Status status : allStatus) {
					if (status.getId().equals(statusOption.getKey())) {
						selectedStatus = status;
						break;
					}
				}

				app.setStatus(selectedStatus);
			}
		}

		/**
		 * updateAppCategory.
		 */
		private void updateAppCategory() {
			// update category
			if (categoryOption != null) {
				log.debug("Selected categroy id is :" + categoryOption.getKey());

				Category selectedCategory = null;
				for (Category category : categories) {
					if (category.getId().equals(categoryOption.getKey())) {
						selectedCategory = category;
						break;
					}
				}

				if (selectedCategory != null) {
					applicationService.associateCategory(assetId, null, selectedCategory.getId());
				}
			}
		}

		/**
		 * @param choiceRenderer
		 *            choiceRenderer
		 * @return DropDownChoice<SelectOption>
		 */
		private DropDownChoice<SelectOption> getStatusDropdown(ChoiceRenderer<SelectOption> choiceRenderer) {
			DropDownChoice<SelectOption> statusDropdown = new DropDownChoice<SelectOption>("status", new PropertyModel<SelectOption>(this, "statusOption"), new AbstractReadOnlyModel<List<SelectOption>>() {

				private static final long serialVersionUID = 3186943561433837625L;

				public List<SelectOption> getObject() {
					List<SelectOption> selects = new ArrayList<SelectOption>();
					for (Status status : allStatus) {
						selects.add(new SelectOption(status.getId(), status.getStatus()));
					}
					return selects;
				}
			}, choiceRenderer);

			Status appStatus = app.getStatus();
			if (appStatus != null) {
				log.debug("appStatus is not null. set the statusDropdown.");
				SelectOption defaultAppStatus = new SelectOption(appStatus.getId(), appStatus.getStatus());
				statusDropdown.setDefaultModelObject(defaultAppStatus);
			} else {
				log.debug("appStatus is null.");
			}

			return statusDropdown;
		}

		/**
		 * @param choiceRenderer
		 *            choiceRenderer
		 * @return DropDownChoice<SelectOption>
		 */
		private DropDownChoice<SelectOption> getCategoryDropdown(ChoiceRenderer<SelectOption> choiceRenderer) {
			DropDownChoice<SelectOption> categoryDropdown = new DropDownChoice<SelectOption>("category", new PropertyModel<SelectOption>(this, "categoryOption"), new AbstractReadOnlyModel<List<SelectOption>>() {

				private static final long serialVersionUID = -4689374385246682488L;

				public List<SelectOption> getObject() {
					List<SelectOption> selects = new ArrayList<SelectOption>();
					for (Category category : categories) {
						selects.add(new SelectOption(category.getId(), category.getName()));
					}
					return selects;
				}
			}, choiceRenderer);

			List<Category> appCategories = applicationService.getAllCategoryByAssetId(assetId, 0, Integer.MAX_VALUE);
			log.debug("appCategories.size:" + appCategories.size());

			if (appCategories != null && appCategories.size() > 0) {
				Category firstAppCategory = appCategories.get(0);
				if (firstAppCategory != null) {
					log.debug("firstAppCategory is not null, set the categoryDropdown.");
					SelectOption defaultAppCategory = new SelectOption(firstAppCategory.getId(), firstAppCategory.getName());
					categoryDropdown.setDefaultModelObject(defaultAppCategory);
				}
			}

			return categoryDropdown;
		}

		public final class AssetCommentsDataView extends DataView<Comments> {

			private static final long serialVersionUID = -3702978379166261336L;

			private static final int ITEMS_PER_PAGE = 5;

			private SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");

			protected AssetCommentsDataView(String id, IDataProvider<Comments> dataProvider) {
				super(id, dataProvider, ITEMS_PER_PAGE);
			}

			protected void populateItem(Item<Comments> commentItem) {

				final Comments c = commentItem.getModelObject();

				CheckBox commentSelect = new CheckBox("commentSelect", new IModel<Boolean>() {

					private static final long serialVersionUID = -4143177072852496573L;

					public Boolean getObject() {
						if (commentsDeleteList != null && commentsDeleteList.size() > 0) {
							if (commentsDeleteList.contains(c)) {
								return true;
							}
						}

						return false;
					}

					public void detach() {
					}

					public void setObject(Boolean paramT) {
						if (paramT) {
							commentsDeleteList.add(c);
						}
					}
				});

				commentItem.add(commentSelect);
				commentItem.add(new Label("userid", c.getUserid()));
				commentItem.add(new Label("assetversion", c.getAssetVersion()));
				commentItem.add(new Label("commentDescription", c.getContent()));
				commentItem.add(new Label("commentDate", sdf.format(c.getCreateDate())));
			}
		}

		public final class AssetCommentsProvider implements IDataProvider<Comments> {

			private static final long serialVersionUID = -1257728018326450220L;

			private ApplicationService applicationService;

			private Long appId;

			public AssetCommentsProvider(ApplicationService applicationService, Long appId) {
				this.applicationService = applicationService;
				this.appId = appId;
			}

			public Iterator<? extends Comments> iterator(int first, int count) {

				List<Comments> allComments = applicationService.getAllCommentsByAssetId(appId, first, count);

				if (allComments != null) {
					return allComments.iterator();
				}

				return null;
			}

			public IModel<Comments> model(Comments object) {
				return new Model<Comments>(object);
			}

			public int size() {
				return (int) applicationService.getAllCommentsCountByAssetId(appId);
			}

			public void detach() {
			}
		}
	}

	public String getTitle() {
		return this.getLocalizer().getString("title", this, "Asset management");
	}
}
