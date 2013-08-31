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
package com.hp.sdf.ngp.ui.page.oam.content;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.extensions.breadcrumb.IBreadCrumbModel;
import org.apache.wicket.extensions.breadcrumb.panel.BreadCrumbPanel;
import org.apache.wicket.extensions.yui.calendar.DatePicker;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.ValidationError;
import org.apache.wicket.validation.validator.MaximumValidator;
import org.apache.wicket.validation.validator.StringValidator;

import com.hp.sdf.ngp.api.comon.EntityType;
import com.hp.sdf.ngp.api.search.SearchExpression;
import com.hp.sdf.ngp.api.search.Condition.StringComparer;
import com.hp.sdf.ngp.common.annotation.Value;
import com.hp.sdf.ngp.eav.model.AttributeValue;
import com.hp.sdf.ngp.model.Asset;
import com.hp.sdf.ngp.model.AssetBinaryVersion;
import com.hp.sdf.ngp.model.Category;
import com.hp.sdf.ngp.model.Provider;
import com.hp.sdf.ngp.model.ScreenShots;
import com.hp.sdf.ngp.model.Status;
import com.hp.sdf.ngp.model.SystemConfig;
import com.hp.sdf.ngp.model.Tag;
import com.hp.sdf.ngp.search.condition.asset.AssetExternalIdCondition;
import com.hp.sdf.ngp.search.condition.provider.ProviderExternalIdCondition;
import com.hp.sdf.ngp.search.engine.SearchExpressionImpl;
import com.hp.sdf.ngp.service.ApplicationService;
import com.hp.sdf.ngp.service.AssetPictureType;
import com.hp.sdf.ngp.ui.common.CheckPanel;
import com.hp.sdf.ngp.ui.common.Constant;
import com.hp.sdf.ngp.ui.common.CustomizeDateTextField;
import com.hp.sdf.ngp.ui.common.EavConstant;
import com.hp.sdf.ngp.ui.common.InputValidator;
import com.hp.sdf.ngp.ui.common.PromptPanel;
import com.hp.sdf.ngp.ui.common.SelectOption;
import com.hp.sdf.ngp.ui.common.Tools;
import com.hp.sdf.ngp.ui.common.UIException;
import com.hp.sdf.ngp.ui.dynamicForm.DynamicForm;
import com.hp.sdf.ngp.ui.provider.ScreenShotsDataProvider;
import com.hp.sdf.ngp.workflow.AssetBinaryVersionLifeCycleEngine;

public class ContentManagementNewPanel extends BreadCrumbPanel {

	private static final long serialVersionUID = -8855725744830146629L;

	public final static Log log = LogFactory.getLog(ContentManagementNewPanel.class);

	@SpringBean
	private ApplicationService applicationService;

	@SpringBean
	private AssetBinaryVersionLifeCycleEngine assetBinaryVersionLifeCycleEngine;

	private final BreadCrumbPanel caller;

	@Value("application.itemsperpage")
	private int itemsPerPage = Constant.DEFALUT_PER_PAGE_COUNT;

	private ScreenShotsDataProvider dataProvider;

	private ScreenShotsDataView dataView;

	private Long assetBinaryVersionId;

	private PromptPanel promptPanel;

	private Boolean isSave = false;

	private List<Tag> tagList = new ArrayList<Tag>();

	public ContentManagementNewPanel(String id, IBreadCrumbModel breadCrumbModel, final BreadCrumbPanel caller) {
		super(id, breadCrumbModel);

		this.caller = caller;

		breadCrumbModel.setActive(this);

		Form<Void> newForm = new ContentManagementNewForm("newForm", applicationService);
		add(newForm);

		promptPanel = new PromptPanel("promptPanel", getLocalizer().getString("title.prompt", this, "Prompt"), getLocalizer().getString("msg.result.save.success", this, "You successfully save the asset binary version!"), null, StringUtils.EMPTY);
		add(promptPanel);
	}

	public String getTitle() {
		return this.getLocalizer().getString("title", this, Constant.CONTENT_NEW);
	}

	public class ContentManagementNewForm extends Form<Void> {

		private static final long serialVersionUID = -1347352088252117228L;

		ApplicationService applicationService;

		private Asset asset = new Asset();
		private Provider provider = new Provider();
		private AssetBinaryVersion assetBinaryVersion = new AssetBinaryVersion();
		// private Status status = new Status();

		// private BigDecimal price = new BigDecimal(0);
		// private String category1, category2;
		private SelectOption category1, category2;
		private List<SelectOption> sonCategoryList;
		// private String tag;
		private Boolean editProvider = false;
		private String providerJpName;
		private String externalIdGenDefault;

		// upload
		private FileUploadField thumbnailBigField;
		// private FileUploadField thumbnailMedField;
		private FileUploadField thumbnailField;
		private FileUploadField fileField;
		private FileUploadField screenShot1Field;
		private FileUploadField screenShot2Field;
		private FileUploadField screenShot3Field;
		private FileUploadField screenShot4Field;
		private FileUploadField screenShot5Field;
		private FileUpload thumbnailBigUpload;
		// private FileUpload thumbnailMedUpload;
		private FileUpload thumbnailUpload;
		private FileUpload screenShot1Upload;
		private FileUpload screenShot2Upload;
		private FileUpload screenShot3Upload;
		private FileUpload screenShot4Upload;
		private FileUpload screenShot5Upload;
		private FileUpload fileUpload;
		Label fileSizeLabel;

		private DynamicFormChild dynamicFormChild;

		public class TagForm extends Form<Void> {

			private static final long serialVersionUID = -2900339851565489630L;

			private String tag;

			public TagForm(String id) {
				super(id);

				final TextField<String> tagsField = new TextField<String>("tag", new PropertyModel<String>(this, "tag"));
				tagsField.setRequired(false);
				tagsField.add(new StringValidator.MaximumLengthValidator(100));
				tagsField.add(new MaximumValidator<String>(StringUtils.EMPTY) {

					private static final long serialVersionUID = 3765437155300193263L;

					public void validate(IValidatable<String> validatable) {
						String value = validatable.getValue();
						if (value != null && value.trim().length() > 0) {

							int newTagCount = getTagsArray(value).size();

							if ((tagList.size() + newTagCount) > Constant.MAX_TAG_COUNT) {
								ValidationError error = new ValidationError();
								error.addMessageKey("tag.tooMany");
								validatable.error(error);
							}
						}
					}
				});
				add(tagsField);

				add(new TagListView("tags", new PropertyModel<List<Tag>>(ContentManagementNewPanel.this, "tagList")));

				Button addTagBtn = new Button("addTag") {

					private static final long serialVersionUID = 8116120281966867260L;

					@Override
					public void onSubmit() {
						List<String> tags = getTagsArray(tag);
						for (String tag : tags) {
							tagList.add(new Tag(null, tag));
						}
						tag = null;
					}
				};
				add(addTagBtn);
			}

		}

		private List<String> getTagsArray(String input) {
			List<String> tagArray = new ArrayList<String>();
			if (input != null) {
				String[] buffer = input.split(Constant.TAG_SUFFIX);
				for (String temp : buffer) {
					if (temp.length() > 0)
						tagArray.add(temp);
				}
			}
			return tagArray;
		}

		public ContentManagementNewForm(String id, final ApplicationService applicationService) {

			super(id);

			this.applicationService = applicationService;

			final FeedbackPanel feedback = new FeedbackPanel("feedback");
			add(feedback);

			dynamicFormChild = new DynamicFormChild("dynamicForm");
			add(dynamicFormChild);

			dataProvider = new ScreenShotsDataProvider(applicationService);

			// assetName
			TextField<String> assetNameField = new TextField<String>("assetName", new PropertyModel<String>(assetBinaryVersion, "name"));
			assetNameField.setRequired(true);
			add(assetNameField);

			// externalId
			final TextField<String> externalIdField = new TextField<String>("externalId", new PropertyModel<String>(asset, "externalId"));
			externalIdField.setRequired(true);
			add(externalIdField);

			// status
			// List<Status> statusAllList = applicationService.getAllStatus();
			// DropDownChoice<Status> statusField = new
			// DropDownChoice<Status>("status", new PropertyModel<Status>(this,
			// "status"), statusAllList, new ChoiceRenderer<Status>("status",
			// "id"));
			// statusField.setRequired(false);
			// add(statusField);

			// assetId
			// TextField<Long> assetIdField = new TextField<Long>("assetId", new PropertyModel<Long>(asset, "id"));
			// assetIdField.setRequired(false);
			// add(assetIdField);

			// parentAssetId
			// TextField<Long> parentAssetIdField = new TextField<Long>("parentAssetId", new PropertyModel<Long>(assetBinaryVersion, "ownerAssetParentId"));
			// parentAssetIdField.setRequired(false);
			// add(parentAssetIdField);

			// fileName
			// TextField<String> fileNameField = new
			// TextField<String>("fileName", new
			// PropertyModel<String>(assetBinaryVersion, "fileName"));
			// fileNameField.setRequired(false);
			// add(fileNameField);

			// providerId
			SystemConfig systemConfig = applicationService.getSystemConfigByKey(Constant.SBMPID);
			TextField<String> providerIdField = new TextField<String>("providerId", new Model<String>(systemConfig.getValue()));
			add(providerIdField);

			// edit provider
			CheckBox checkBox = new CheckBox("editProvider", new PropertyModel<Boolean>(this, "editProvider"));
			add(checkBox);

			// providerName
			TextField<String> providerNameField = new TextField<String>("providerName", new PropertyModel<String>(provider, "name"));
			providerNameField.setRequired(false);
			providerNameField.setOutputMarkupId(false);
			add(providerNameField);

			// providerJpName
			SearchExpression searchProvider = new SearchExpressionImpl();
			searchProvider.addCondition(new ProviderExternalIdCondition(systemConfig.getValue(), StringComparer.EQUAL, false, false));
			List<Provider> providerList = applicationService.searchAssetProvider(searchProvider);
			if (providerList == null || providerList.size() < 1) {
				throw new UIException("Can not find SBM-PID Provider!");
			}
			Provider providerGet = providerList.get(0);

			List<AttributeValue> listTemp = applicationService.getAttributeValue(providerGet.getId(), EntityType.ASSETPROVIDER, EavConstant.PROVIDER_JP_NAME);
			if (listTemp != null && listTemp.size() > 0)
				providerJpName = Tools.getValue(listTemp.get(0).getValue());
			else
				providerJpName = StringUtils.EMPTY;
			TextField<String> providerJpNameField = new TextField<String>("providerJpName", new PropertyModel<String>(this, "providerJpName"));
			providerJpNameField.setRequired(false);
			providerJpNameField.setOutputMarkupId(false);
			add(providerJpNameField);

			// priority
			// TextField<Integer> priorityField = new TextField<Integer>("priority", new PropertyModel<Integer>(provider, "priority"));
			// priorityField.setOutputMarkupId(false);
			// add(priorityField);

			// providerExternalId
			// TextField<String> providerExternalIdField = new TextField<String>("providerExternalId", new PropertyModel<String>(provider, "externalId"));
			// providerExternalIdField.setRequired(false);
			// add(providerExternalIdField);

			// category1
			// TextField<String> category1Field = new TextField<String>("category1", new PropertyModel<String>(this, "category1"));
			// category1Field.setRequired(true);
			// add(category1Field);

			List<Category> categoryList = applicationService.getAllCategory(0, Integer.MAX_VALUE);
			List<SelectOption> parentCategoryList = new ArrayList<SelectOption>();
			sonCategoryList = new ArrayList<SelectOption>();
			for (Category category : categoryList) {
				if (category.getParentCategory() == null)
					parentCategoryList.add(new SelectOption(category.getId(), category.getName() + "(" + category.getId() + ")"));
			}

			ChoiceRenderer<SelectOption> choiceRenderer = new ChoiceRenderer<SelectOption>("value", "key");
			final DropDownChoice<SelectOption> category1Field = new DropDownChoice<SelectOption>("category1", new PropertyModel<SelectOption>(this, "category1"), parentCategoryList, choiceRenderer);
			category1Field.setOutputMarkupId(true);
			category1Field.setRequired(true);
			add(category1Field);

			// category2
			// TextField<String> category2Field = new TextField<String>("category2", new PropertyModel<String>(this, "category2"));
			// category2Field.setRequired(true);
			// add(category2Field);

			final DropDownChoice<SelectOption> category2Field = new DropDownChoice<SelectOption>("category2", new PropertyModel<SelectOption>(this, "category2"), sonCategoryList, choiceRenderer);
			category2Field.setOutputMarkupId(true);
			category2Field.setRequired(true);

			category1Field.add(new AjaxFormComponentUpdatingBehavior("onChange") {
				private static final long serialVersionUID = 932430235427953354L;

				@Override
				protected void onUpdate(AjaxRequestTarget target) {
					sonCategoryList.clear();
					SelectOption parentCategory = category1Field.getModelObject();
					if (parentCategory != null && parentCategory.getKey() != null) {
						List<Category> categoryList = applicationService.getCategoryByParentCategoryId(parentCategory.getKey());
						if (categoryList != null) {
							for (Category category : categoryList) {
								sonCategoryList.add(new SelectOption(category.getId(), category.getName() + "(" + category.getId() + ")"));
							}
						}
					}

					target.addComponent(category2Field);
				}
			});
			add(category2Field);

			// description
			// TextArea<String> descriptionField = new
			// TextArea<String>("description", new
			// PropertyModel<String>(assetBinaryVersion, "description"));
			// add(descriptionField);

			// brief
			TextField<String> briefField = new TextField<String>("brief", new PropertyModel<String>(assetBinaryVersion, "brief"));
			briefField.setRequired(false);
			add(briefField);

			// price
			// TextField<BigDecimal> priceField = new
			// TextField<BigDecimal>("price", new
			// PropertyModel<BigDecimal>(this, "price"));
			// priceField.add(new MinimumValidator<BigDecimal>(new
			// BigDecimal(0)));
			// priceField.setRequired(true);
			// add(priceField);

			// createDate
			// DateTextField createDateField = new DateTextField("createDate",
			// new PropertyModel<Date>(assetBinaryVersion, "createDate"),
			// Constant.DATE_PATTERN);
			// createDateField.setRequired(true);
			// add(createDateField);
			// createDateField.add(new DatePicker());

			// publishDate
			CustomizeDateTextField publishDateField = new CustomizeDateTextField("publishDate", new PropertyModel<Date>(assetBinaryVersion, "publishDate"), Constant.DATE_PATTERN);
			publishDateField.setRequired(true);
			add(publishDateField);
			publishDateField.add(new DatePicker());

			// expireDate
			CustomizeDateTextField expireDateField = new CustomizeDateTextField("expireDate", new PropertyModel<Date>(assetBinaryVersion, "expireDate"), Constant.DATE_PATTERN);
			expireDateField.setRequired(true);
			add(expireDateField);
			expireDateField.add(new DatePicker());

			// fileSize
			// TextField<BigDecimal> fileSizeField = new TextField<BigDecimal>("fileSize", new PropertyModel<BigDecimal>(assetBinaryVersion, "fileSize"));
			// fileSizeField.setRequired(true);
			// add(fileSizeField);
			fileSizeLabel = new Label("fileSize", StringUtils.EMPTY);
			add(fileSizeLabel);

			// version
			TextField<String> versionField = new TextField<String>("version", new PropertyModel<String>(assetBinaryVersion, "version"));
			versionField.setRequired(true);
			versionField.add(new InputValidator(com.hp.sdf.ngp.ui.common.Constant.VALID_TYPE.isAll));
			add(versionField);

			// file
			fileField = new FileUploadField("file", new PropertyModel<FileUpload>(this, "fileUpload"));
			fileField.setRequired(true);
			add(fileField);

			// tag
			// final TextField<String> tagsField = new TextField<String>("tag", new PropertyModel<String>(this, "tag"));
			// tagsField.setRequired(false);
			// tagsField.add(new StringValidator.MaximumLengthValidator(100));
			// tagsField.add(new MaximumValidator<String>(StringUtils.EMPTY) {
			//
			// private static final long serialVersionUID = 3765437155300193263L;
			//
			// public void validate(IValidatable<String> validatable) {
			// String value = validatable.getValue();
			// if (value != null && value.trim().length() > 0) {
			//
			// int newTagCount = getTagsArray(value).size();
			//
			// if ((tagList.size() + newTagCount) > Constant.MAX_TAG_COUNT) {
			// ValidationError error = new ValidationError();
			// error.addMessageKey("tag.tooMany");
			// validatable.error(error);
			// }
			// }
			// }
			// });
			// add(tagsField);
			//
			// add(new TagListView("tags", new PropertyModel<List<Tag>>(ContentManagementNewPanel.this, "tagList")));

			add(new TagForm("tagForm"));

			// newArrivalDueDate
			// DateTextField newArrivalDueDateField = new
			// DateTextField("newArrivalDueDate", new
			// PropertyModel<Date>(assetBinaryVersion, "newArrivalDueDate"),
			// Constant.DATE_PATTERN);
			// newArrivalDueDateField.setRequired(true);
			// add(newArrivalDueDateField);
			// newArrivalDueDateField.add(new DatePicker());

			// recommendStartDate
			// DateTextField recommendStartDateField = new
			// DateTextField("recommendStartDate", new
			// PropertyModel<Date>(assetBinaryVersion, "recommendStartDate"),
			// Constant.DATE_PATTERN);
			// recommendStartDateField.setRequired(true);
			// add(recommendStartDateField);
			// recommendStartDateField.add(new DatePicker());

			// recommendDueDate
			// DateTextField recommendDueDateField = new
			// DateTextField("recommendDueDate", new
			// PropertyModel<Date>(assetBinaryVersion, "recommendDueDate"),
			// Constant.DATE_PATTERN);
			// recommendDueDateField.setRequired(true);
			// add(recommendDueDateField);
			// recommendDueDateField.add(new DatePicker());

			// recommendOrder
			// TextField<Integer> recommendOrderField = new
			// TextField<Integer>("recommendOrder", new
			// PropertyModel<Integer>(assetBinaryVersion, "recommendOrder"));
			// recommendOrderField.setRequired(true);
			// add(recommendOrderField);

			// thumbnailBig
			thumbnailBigField = new FileUploadField("thumbnailBig", new PropertyModel<FileUpload>(this, "thumbnailBigUpload"));
			thumbnailBigField.setRequired(false);
			add(thumbnailBigField);

			// thumbnailMed
			// thumbnailMedField = new FileUploadField("thumbnailMed", new PropertyModel<FileUpload>(this, "thumbnailMedUpload"));
			// thumbnailMedField.setRequired(false);
			// add(thumbnailMedField);

			// thumbnail
			thumbnailField = new FileUploadField("thumbnail", new PropertyModel<FileUpload>(this, "thumbnailUpload"));
			thumbnailField.setRequired(false);
			add(thumbnailField);

			// screenShot
			screenShot1Field = new FileUploadField("screenShot1", new PropertyModel<FileUpload>(this, "screenShot1Upload"));
			add(screenShot1Field);

			screenShot2Field = new FileUploadField("screenShot2", new PropertyModel<FileUpload>(this, "screenShot2Upload"));
			add(screenShot2Field);

			screenShot3Field = new FileUploadField("screenShot3", new PropertyModel<FileUpload>(this, "screenShot3Upload"));
			add(screenShot3Field);

			screenShot4Field = new FileUploadField("screenShot4", new PropertyModel<FileUpload>(this, "screenShot4Upload"));
			add(screenShot4Field);

			screenShot5Field = new FileUploadField("screenShot5", new PropertyModel<FileUpload>(this, "screenShot5Upload"));
			add(screenShot5Field);

			// dataView = new ScreenShotsDataView("screenShots", dataProvider, itemsPerPage);
			// add(dataView);

			// add(new CustomizePagingNavigator("navigator", dataView));

			// TODO
			// Button addTagBtn = new Button("addTag") {
			//
			// private static final long serialVersionUID = 8116120281966867260L;
			//
			// @Override
			// public void onSubmit() {
			// List<String> tags = getTagsArray(tag);
			// for (String tag : tags) {
			// tagList.add(new Tag(null, tag));
			// }
			// tag = null;
			// }
			// };
			// add(addTagBtn);

			// generate button
			Button generateBtn = new Button("generateId") {

				private static final long serialVersionUID = 8116120281966867260L;

				@Override
				public void onSubmit() {
					SystemConfig systemConfig = applicationService.getSystemConfigByKey(Constant.SBMPID);
					externalIdGenDefault = systemConfig.getValue() + Tools.getFormatDateTime(new Date());
					// externalIdField.setDefaultModel(new Model<String>(externalIdGenDefault));
					// asset.setExternalId(externalIdGenDefault);
					externalIdField.add(new AttributeModifier("value", true, new Model<String>(externalIdGenDefault)));
				}
			};
			generateBtn.setDefaultFormProcessing(false);
			add(generateBtn);

			// back button
			Button backBtn = new Button("btn.back") {

				private static final long serialVersionUID = 8116120281966867260L;

				@Override
				public void onSubmit() {
					log.debug("back");
					caller.activate(caller);
				}
			};
			backBtn.setDefaultFormProcessing(false);
			add(backBtn);

			final CheckPanel checkPanel = new CheckPanel("checkPanel", getLocalizer().getString("title.prompt", ContentManagementNewPanel.this), getLocalizer().getString("msg.warn.save", ContentManagementNewPanel.this)) {

				private static final long serialVersionUID = 7465424124110025236L;

				@Override
				public void howDo() {
					// ---------- validation begin ----------
					// asset id
					// if (asset.getId() != null) {
					// if (null == applicationService.getAsset(asset.getId())) {
					// error("Please input valid Asset Id.");
					// return;
					// } else {
					// if (!StringUtils.equals(applicationService.getAsset(asset.getId()).getSource(), Constant.ASSET_SOURCE)) {
					// error("Please input valid Asset Id.");
					// return;
					// }
					// }
					// }

					// parent asset id
					// if (assetBinaryVersion.getOwnerAssetParentId() != null && null == applicationService.getAsset(assetBinaryVersion.getOwnerAssetParentId())) {
					// error("Please input valid Parent Asset Id.");
					// return;
					// }

					if (!isSave) {

						Asset assetUpdate;
						if (asset.getExternalId().equals(externalIdGenDefault)) {
							assetUpdate = new Asset();
							assetUpdate.setExternalId(externalIdGenDefault);
						} else {
							SearchExpression searchExpression = new SearchExpressionImpl();
							searchExpression.addCondition(new AssetExternalIdCondition(asset.getExternalId(), StringComparer.EQUAL, false, false));
							List<Asset> assetList = applicationService.searchAsset(searchExpression);
							if (assetList.size() < 1) {
								error(getLocalizer().getString("invalid.asset.external.id", ContentManagementNewPanel.this, "Please input valid External Id."));
								externalIdField.add(new AttributeModifier("value", true, new Model<String>(asset.getExternalId())));
								return;
							} else {
								assetUpdate = new Asset();
								assetUpdate.setExternalId(asset.getExternalId());
							}
						}

						// if (provider.getId() != null && null == applicationService.getAssetProviderById(provider.getId())) {
						// error("Please input valid Provider Id.");
						// return;
						// }

						// parent category and parent son category
						// List<Category> categoryList = applicationService.getAllCategory(0, Integer.MAX_VALUE);
						// List<String> parentCategoryList = new ArrayList<String>();
						// List<String> sonCategoryList = new ArrayList<String>();
						// for (Category category : categoryList) {
						// if (category.getParentCategory() == null)
						// parentCategoryList.add(category.getName());
						// else
						// sonCategoryList.add(category.getName());
						// }
						// Category parentCategory = applicationService.getCategoryByName(category1);
						// if (parentCategory == null || !parentCategoryList.contains(parentCategory.getName())) {
						// error("Please input valid Genre1.");
						// return;
						// }
						// Category sonCategory = applicationService.getCategoryByName(category2);
						// if (sonCategory == null || !sonCategoryList.contains(sonCategory.getName())) {
						// error("Please input valid Genre2.");
						// return;
						// } else {
						// Category pCategory = applicationService.getCategoryById(sonCategory.getParentCategory().getId());
						// if (!pCategory.getName().equals(category1)) {
						// error("Please input valid Genre2.");
						// return;
						// }
						// }

						// date
						if (assetBinaryVersion.getPublishDate().compareTo(assetBinaryVersion.getExpireDate()) >= 0) {
							error(getLocalizer().getString("invalid.date", ContentManagementNewPanel.this, "Publish Date can not greater than Expire Date."));
							return;
						}
						// ---------- validation end ----------

						// update provider
						SearchExpression searchProvider = new SearchExpressionImpl();
						SystemConfig systemConfig = applicationService.getSystemConfigByKey(Constant.SBMPID);
						searchProvider.addCondition(new ProviderExternalIdCondition(systemConfig.getValue(), StringComparer.EQUAL, false, false));
						List<Provider> providerList = applicationService.searchAssetProvider(searchProvider);
						if (providerList == null || providerList.size() < 1) {
							error(getLocalizer().getString("msg.error.save.noprovider", ContentManagementNewPanel.this, "Can not find specify provider!"));
							return;
						}
						Provider providerUpdate = providerList.get(0);
						if (editProvider) {
							// providerUpdate.setPriority(provider.getPriority());
							providerUpdate.setName(Tools.getValue(provider.getName()));
							// if (StringUtils.isNotBlank(providerJpName)) {
							// applicationService.addAttribute(assetBinaryVersion.getId(), EntityType.BINARYVERSION, EavConstant.PROVIDER_JP_NAME, providerJpName);
							// }
							applicationService.updateAssetProvider(providerUpdate);
						}

						// get initial status
						Status status = assetBinaryVersionLifeCycleEngine.getStartupStatus();

						// if (asset.getId() != null) {
						// assetUpdate = applicationService.getAsset(asset.getId());
						// if (provider.getId() != null)
						// assetUpdate.setAssetProvider(providerUpdate);
						// applicationService.updateAsset(assetUpdate);
						// } else {
						// if (provider.getId() != null)

						// save asset
						assetUpdate.setAssetProvider(providerUpdate);
						assetUpdate.setStatus(status);
						assetUpdate.setSource(Constant.ASSET_SOURCE);
						applicationService.saveAsset(assetUpdate);

						// save binary version
						assetBinaryVersion.setFileName(fileField.getFileUpload().getClientFileName());
						assetBinaryVersion.setAsset(assetUpdate);
						assetBinaryVersion.setStatus(status);
						assetBinaryVersion.setPublishDate(Tools.parseBeginDate(assetBinaryVersion.getPublishDate()));
						assetBinaryVersion.setExpireDate(Tools.parseEndDate(assetBinaryVersion.getExpireDate()));
						applicationService.saveAssetBinary(Tools.getFileBuffer(fileField), assetBinaryVersion);
						fileSizeLabel.setDefaultModel(new Model<Long>(fileField.getSizeInBytes()));

						Set<Long> tagSet = new HashSet<Long>();
						for (Tag tag : tagList) {
							applicationService.saveTag(tag);
							tagSet.add(tag.getId());
						}
						applicationService.associateTagRelation(assetUpdate.getId(), assetBinaryVersion.getId(), tagSet);

						ContentManagementNewPanel.this.assetBinaryVersionId = assetBinaryVersion.getId();

						applicationService.associateCategory(assetUpdate.getId(), assetBinaryVersion.getId(), category2.getKey());

						if (thumbnailBigUpload != null)
							applicationService.saveAssetBinaryPicture(assetBinaryVersion.getId(), Tools.getFileBuffer(thumbnailBigField), AssetPictureType.THUMBNAILBIGIMAGE, "THUMBNAILBIGIMAGE.jpg");
						// if (thumbnailMedUpload != null)
						// applicationService.saveAssetBinaryPicture(assetBinaryVersion.getId(), Tools.getFileBuffer(thumbnailMedField), AssetPictureType.THUMBNAILMIDDLEIMAGE, "THUMBNAILMIDDLEIMAGE.jpg");
						if (thumbnailUpload != null)
							applicationService.saveAssetBinaryPicture(assetBinaryVersion.getId(), Tools.getFileBuffer(thumbnailField), AssetPictureType.THUMBNAILIMAGE, "THUMBNAILIMAGE.jpg");

						if (screenShot1Upload != null) {
							ScreenShots screenShots = new ScreenShots();
							screenShots.setBinaryVersion(assetBinaryVersion);
							screenShots.setAsset(assetUpdate);
							screenShots.setSequence(1L);
							// applicationService.saveScreenShots(screenShots, Tools.getFileBuffer(screenShotField), screenShotField.getFileUpload().getClientFileName());
							applicationService.saveScreenShots(screenShots, Tools.getFileBuffer(screenShot1Field), Tools.getFileName(screenShot1Field.getFileUpload().getClientFileName()));
						}

						if (screenShot2Upload != null) {
							ScreenShots screenShots = new ScreenShots();
							screenShots.setBinaryVersion(assetBinaryVersion);
							screenShots.setAsset(assetUpdate);
							screenShots.setSequence(2L);
							// applicationService.saveScreenShots(screenShots, Tools.getFileBuffer(screenShotField), screenShotField.getFileUpload().getClientFileName());
							applicationService.saveScreenShots(screenShots, Tools.getFileBuffer(screenShot2Field), Tools.getFileName(screenShot2Field.getFileUpload().getClientFileName()));
						}

						if (screenShot3Upload != null) {
							ScreenShots screenShots = new ScreenShots();
							screenShots.setBinaryVersion(assetBinaryVersion);
							screenShots.setAsset(assetUpdate);
							screenShots.setSequence(3L);
							// applicationService.saveScreenShots(screenShots, Tools.getFileBuffer(screenShotField), screenShotField.getFileUpload().getClientFileName());
							applicationService.saveScreenShots(screenShots, Tools.getFileBuffer(screenShot3Field), Tools.getFileName(screenShot3Field.getFileUpload().getClientFileName()));
						}

						if (screenShot4Upload != null) {
							ScreenShots screenShots = new ScreenShots();
							screenShots.setBinaryVersion(assetBinaryVersion);
							screenShots.setAsset(assetUpdate);
							screenShots.setSequence(4L);
							// applicationService.saveScreenShots(screenShots, Tools.getFileBuffer(screenShotField), screenShotField.getFileUpload().getClientFileName());
							applicationService.saveScreenShots(screenShots, Tools.getFileBuffer(screenShot4Field), Tools.getFileName(screenShot4Field.getFileUpload().getClientFileName()));
						}

						if (screenShot5Upload != null) {
							ScreenShots screenShots = new ScreenShots();
							screenShots.setBinaryVersion(assetBinaryVersion);
							screenShots.setAsset(assetUpdate);
							screenShots.setSequence(5L);
							// applicationService.saveScreenShots(screenShots, Tools.getFileBuffer(screenShotField), screenShotField.getFileUpload().getClientFileName());
							applicationService.saveScreenShots(screenShots, Tools.getFileBuffer(screenShot5Field), Tools.getFileName(screenShot5Field.getFileUpload().getClientFileName()));
						}

						try {
							BeanUtils.copyProperties(asset, assetUpdate);
						} catch (Exception ex) {
							log.error(ex.getMessage());
						}

						// promptPanel.setMessage(getLocalizer().getString("msg.result.save.success", ContentManagementNewPanel.this, "You successfully save the asset binary version!"));
						// promptPanel.show();

						editProvider = false;
						isSave = true;
					}

					// setResponsePage(ContentManagementPage.class);
					// caller.activate(caller);
					// newEavBtn.onSubmit();
					dynamicFormChild.onFormSubmitted();

				}
			};
			add(checkPanel);

			Button saveBtn = new Button("btn.save") {

				private static final long serialVersionUID = 8116120281966867260L;

				@Override
				public void onSubmit() {
					checkPanel.show();
				}
			};
			add(saveBtn);
		}

		public final void onSubmit() {
		}

		@Override
		protected void onError() {
			editProvider = false;
			super.onError();
		}
	}

	class ScreenShotsDataView extends DataView<ScreenShots> {

		private static final long serialVersionUID = -6154935955544130768L;

		protected ScreenShotsDataView(String id, IDataProvider<ScreenShots> dataProvider, int itemsPerPage) {
			super(id, dataProvider, itemsPerPage);
		}

		@Override
		protected void populateItem(Item<ScreenShots> item) {
			ScreenShots screenShots = item.getModelObject();

			item.add(new Label("pictureUrl", Tools.getValue(screenShots.getStoreLocation())));
		}
	}

	class DynamicFormChild extends DynamicForm {

		private static final long serialVersionUID = -7170269132842739280L;

		public DynamicFormChild(String id) {
			super(id);

			genContent();

			Button newEavBtn = new Button("addNew") {
				private static final long serialVersionUID = 6108396813496866446L;
			};
			add(newEavBtn);
		}

		@Override
		protected String getDataConfigFile() {
			return "ContentManagementNew.xml";

		}

		@Override
		protected Object getDomainModel() {
			return new HashedMap();
		}

		@Override
		protected void onSubmit() {

			if (assetBinaryVersionId == null) {
				error(getLocalizer().getString("input.version.first", ContentManagementNewPanel.this, "Please add a new binary version first."));
				return;
			}

			for (Iterator<String> iterator = eavMap.keySet().iterator(); iterator.hasNext();) {
				String key = iterator.next();
				Object value = eavMap.get(key);
				if (null == value)
					continue;
				applicationService.addAttribute(assetBinaryVersionId, EntityType.BINARYVERSION, key.toUpperCase(), value);
			}

			promptPanel.setMessage(getLocalizer().getString("msg.result.eavsave.success", ContentManagementNewPanel.this, "You successfully save the eav properties of the asset binary version!"));
			promptPanel.show();

			super.onSubmit();
		}

	}

	class TagListView extends ListView<Tag> {

		public TagListView(final String id, List<? extends Tag> list) {
			super(id, list);
		}

		public TagListView(final String id, final IModel<? extends List<? extends Tag>> model) {
			super(id, model);
		}

		@Override
		protected void populateItem(ListItem<Tag> item) {
			final Tag tag = (Tag) item.getModelObject();

			item.add(new Label("tagName", tag.getName()));

			Link delLink = new Link("delTag") {
				@Override
				public void onClick() {
					tagList.remove(tag);
				}
			};
			item.add(delLink);
		}
	};
}