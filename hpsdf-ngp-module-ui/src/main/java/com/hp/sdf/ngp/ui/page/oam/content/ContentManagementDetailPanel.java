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

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.authorization.strategies.role.metadata.MetaDataRoleAuthorizationStrategy;
import org.apache.wicket.extensions.breadcrumb.IBreadCrumbModel;
import org.apache.wicket.extensions.breadcrumb.panel.BreadCrumbPanel;
import org.apache.wicket.extensions.breadcrumb.panel.BreadCrumbPanelLink;
import org.apache.wicket.extensions.breadcrumb.panel.IBreadCrumbPanelFactory;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.CheckBox;
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
import com.hp.sdf.ngp.ui.common.EavConstant;
import com.hp.sdf.ngp.ui.common.PromptPanel;
import com.hp.sdf.ngp.ui.common.Tools;
import com.hp.sdf.ngp.ui.common.Constant.PICTURE_TYPE;
import com.hp.sdf.ngp.ui.dynamicForm.DynamicForm;
import com.hp.sdf.ngp.ui.page.oam.category.CategoryEditPanel;
import com.hp.sdf.ngp.ui.page.oam.content.ContentManagementNewPanel.DynamicFormChild;
import com.hp.sdf.ngp.ui.provider.ScreenShotsDataProvider;
import com.hp.sdf.ngp.workflow.AssetBinaryVersionLifeCycleEngine;
import com.hp.sdf.ngp.workflow.Privilege;

public class ContentManagementDetailPanel extends BreadCrumbPanel {

	private static final long serialVersionUID = -8855725744830146629L;

	public final static Log log = LogFactory.getLog(ContentManagementDetailPanel.class);

	@SpringBean
	private ApplicationService applicationService;

	@SpringBean
	private AssetBinaryVersionLifeCycleEngine assetBinaryVersionLifeCycleEngine;

	@Value("application.itemsperpage")
	private int itemsPerPage = Constant.DEFALUT_PER_PAGE_COUNT;

	private BreadCrumbPanel caller;

	private final Long binaryVersionId;

	private ScreenShotsDataProvider dataProvider;

	private ScreenShotsDataView dataView;

	private PromptPanel promptPanel;

	private List<Tag> tagList;

	private Boolean isSave = false;

	public ContentManagementDetailPanel(String id, IBreadCrumbModel breadCrumbModel, Long binaryVersionId, final BreadCrumbPanel caller) {
		super(id, breadCrumbModel);

		this.binaryVersionId = binaryVersionId;
		this.caller = caller;
		breadCrumbModel.setActive(this);

		Form<Void> detailForm = new ContentManagementDetailForm("detailForm", applicationService);
		add(detailForm);

		promptPanel = new PromptPanel("promptPanel", getLocalizer().getString("title.prompt", this, "Prompt"), getLocalizer().getString("msg.warn.update.result", this, "You successfully update the asset binary version!"), null, StringUtils.EMPTY);
		add(promptPanel);
	}

	public String getTitle() {
		return this.getLocalizer().getString("title", this, Constant.CONTENT_DETAIL);
	}

	public class ContentManagementDetailForm extends Form<Void> {

		private static final long serialVersionUID = -1347352088252117228L;

		ApplicationService applicationService;

		private Asset asset;
		private Provider provider;
		private AssetBinaryVersion assetBinaryVersion;
		// private Status status;

		// private BigDecimal price = new BigDecimal(0);
		private String category1, category2;
		// private String tag;

		private Boolean editProvider = false;
		private String providerJpName;
		private String externalIdGenDefault;

		// upload
		private FileUploadField thumbnailBigField;
		private FileUploadField thumbnailMedField;
		private FileUploadField thumbnailField;
		private FileUploadField screenShotField;
		private FileUpload thumbnailBigUpload;
		private FileUpload thumbnailUpload;
		private FileUpload screenShotUpload;

		private DynamicFormChild dynamicFormChild;

		public class TagForm extends Form<Void> {

			private static final long serialVersionUID = 3484880658890330949L;
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

				add(new TagListView("tags", new PropertyModel<List<Tag>>(ContentManagementDetailPanel.this, "tagList")));

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
				MetaDataRoleAuthorizationStrategy.authorize(addTagBtn, Component.RENDER, Privilege.UPDATECONTENT);
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

		public ContentManagementDetailForm(String id, final ApplicationService applicationService) {

			super(id);

			dynamicFormChild = new DynamicFormChild("dynamicForm");
			add(dynamicFormChild);

			final FeedbackPanel feedback = new FeedbackPanel("feedback");
			add(feedback);
			this.applicationService = applicationService;

			dataProvider = new ScreenShotsDataProvider(applicationService, binaryVersionId);

			assetBinaryVersion = applicationService.getAssetBinaryById(binaryVersionId);

			tagList = applicationService.getAllTagsByAssetBinary(binaryVersionId, 0, Integer.MAX_VALUE);
			if (tagList == null)
				tagList = new ArrayList<Tag>();

			if (assetBinaryVersion.getAsset() != null)
				asset = applicationService.getAsset(assetBinaryVersion.getAsset().getId());
			else
				asset = new Asset();

			// status =
			// applicationService.getStatusById(assetBinaryVersion.getStatus().getId());
			// if (null == status)
			// throw new UIException("Status can not be null");

			if (asset.getAssetProvider() != null) {
				provider = applicationService.getAssetProviderById(asset.getAssetProvider().getId());
			} else {
				provider = new Provider();
			}

			// assetName
			TextField<String> assetNameField = new TextField<String>("assetName", new PropertyModel<String>(assetBinaryVersion, "name"));
			assetNameField.setRequired(false);
			add(assetNameField);
			add(new Label("label.assetName", Tools.getValue(assetBinaryVersion.getName())));

			// externalId
			final TextField<String> externalIdField = new TextField<String>("externalId", new PropertyModel<String>(asset, "externalId"));
			externalIdField.setRequired(false);
			add(externalIdField);
			add(new Label("label.externalId", Tools.getValue(asset.getExternalId())));

			// status
			// List<Status> statusAllList = applicationService.getAllStatus();
			// DropDownChoice<Status> statusField = new
			// DropDownChoice<Status>("status", new PropertyModel<Status>(this,
			// "status"), statusAllList, new ChoiceRenderer<Status>("status",
			// "id"));
			// statusField.setRequired(true);
			// add(statusField);
			// add(new Label("label.status",
			// Tools.getValue(status.getStatus())));

			// assetId
			// TextField<Long> assetIdField = new TextField<Long>("assetId", new
			// PropertyModel<Long>(asset, "id"));
			// assetIdField.setRequired(true);
			// add(assetIdField);
			// add(new Label("label.assetId", Tools.getValue(asset.getId())));

			// parentAssetId
			// TextField<Long> parentAssetIdField = new
			// TextField<Long>("parentAssetId", new
			// PropertyModel<Long>(assetBinaryVersion, "ownerAssetParentId"));
			// parentAssetIdField.setRequired(false);
			// add(parentAssetIdField);
			// add(new Label("label.parentAssetId",
			// Tools.getValue(assetBinaryVersion.getOwnerAssetParentId())));

			// fileName
			// TextField<String> fileNameField = new
			// TextField<String>("fileName", new
			// PropertyModel<String>(assetBinaryVersion, "fileName"));
			// fileNameField.setRequired(true);
			// add(fileNameField);
			// add(new Label("label.fileName",
			// Tools.getValue(assetBinaryVersion.getFileName())));

			// providerId
			TextField<Long> providerIdField = new TextField<Long>("providerId", new PropertyModel<Long>(provider, "externalId"));
			providerIdField.setRequired(false);
			add(providerIdField);
			add(new Label("label.providerId", Tools.getValue(provider.getExternalId())));

			// edit provider
			CheckBox checkBox = new CheckBox("editProvider", new PropertyModel<Boolean>(this, "editProvider"));
			add(checkBox);

			// providerName
			TextField<String> providerNameField = new TextField<String>("providerName", new PropertyModel<String>(provider, "name"));
			providerNameField.setOutputMarkupId(false);
			add(providerNameField);
			add(new Label("label.providerName", Tools.getValue(provider.getName())));

			TextField<String> providerJpNameField = new TextField<String>("providerJpName", new PropertyModel<String>(this, "providerJpName"));
			providerJpNameField.setOutputMarkupId(false);
			add(providerJpNameField);
			add(new Label("label.providerJpName", Tools.getValue(StringUtils.EMPTY)));

			List<Category> categoryList = applicationService.getAllCategoryByAssetId(asset.getId(), 0, Integer.MAX_VALUE);
			if (categoryList.size() < 1) {
				category1 = category2 = StringUtils.EMPTY;
			} else if (categoryList.size() == 1) {
				category1 = category2 = categoryList.get(0).getName();
			} else {
				category1 = categoryList.get(0).getName();
				category2 = categoryList.get(1).getName();
			}
			// TextField<String> category1Field = new
			// TextField<String>("category1", new PropertyModel<String>(this,
			// "category1"));
			// category1Field.setRequired(true);
			// add(category1Field);
			add(new Label("label.category1", this.category1));

			// category2
			// TextField<String> category2Field = new
			// TextField<String>("category2", new PropertyModel<String>(this,
			// "category2"));
			// category2Field.setRequired(true);
			// add(category2Field);
			add(new Label("label.category2", this.category2));

			// description
			// TextArea<String> descriptionField = new
			// TextArea<String>("description", new
			// PropertyModel<String>(assetBinaryVersion, "description"));
			// add(descriptionField);
			// add(new Label("label.description",
			// Tools.getValue(assetBinaryVersion.getDescription())));

			// brief
			TextField<String> briefField = new TextField<String>("brief", new PropertyModel<String>(assetBinaryVersion, "brief"));
			briefField.setRequired(false);
			add(briefField);
			add(new Label("label.brief", Tools.getValue(assetBinaryVersion.getBrief())));

			// brief
			add(new Label("label.upDate", Tools.getValue(assetBinaryVersion.getUpdateDate())));

			// price
			// List<AssetPrice> assetPriceList =
			// applicationService.getAssetPriceByAssetId(asset.getId());
			// price = Tools.getAssetPriceFromListDollars(assetPriceList);
			// TextField<BigDecimal> priceField = new
			// TextField<BigDecimal>("price", new
			// PropertyModel<BigDecimal>(this, "price"));
			// priceField.add(new MinimumValidator<BigDecimal>(new
			// BigDecimal(0)));
			// priceField.setRequired(true);
			// add(priceField);
			// add(new Label("label.price", price.toString()));

			// createDate
			// DateTextField createDateField = new DateTextField("createDate",
			// new PropertyModel<Date>(assetBinaryVersion, "createDate"),
			// Constant.DATE_PATTERN);
			// createDateField.setRequired(true);
			// add(createDateField);
			// createDateField.add(new DatePicker());
			// add(new Label("label.createDate",
			// Tools.getValue(assetBinaryVersion.getCreateDate())));

			// expireDate
			// DateTextField expireDateField = new DateTextField("expireDate",
			// new PropertyModel<Date>(assetBinaryVersion, "expireDate"),
			// Constant.DATE_PATTERN);
			// expireDateField.setRequired(true);
			// add(expireDateField);
			// expireDateField.add(new DatePicker());
			// add(new Label("label.expireDate",
			// Tools.getValue(assetBinaryVersion.getExpireDate())));

			// fileSize
			// TextField<BigDecimal> fileSizeField = new
			// TextField<BigDecimal>("fileSize", new
			// PropertyModel<BigDecimal>(assetBinaryVersion, "fileSize"));
			// fileSizeField.setRequired(true);
			// add(fileSizeField);
			// add(new Label("label.fileSize",
			// assetBinaryVersion.getFileSize().toString()));

			// tag
			// List<Tag> tagList =
			// applicationService.getAllTagsByAssetId(asset.getId(), 0,
			// Integer.MAX_VALUE);
			// tag = Tools.getTagNameFromList(tagList);
			// TextField<String> tagField = new TextField<String>("tag", new
			// PropertyModel<String>(this, "tag"));
			// tagField.setRequired(true);
			// add(tagField);
			// add(new Label("label.tag", this.tag));

			// tag
			// final TextField<String> tagsField = new TextField<String>("tag",
			// new PropertyModel<String>(this, "tag"));
			// tagsField.setRequired(false);
			// tagsField.add(new StringValidator.MaximumLengthValidator(100));
			// tagsField.add(new MaximumValidator<String>(StringUtils.EMPTY) {
			//
			// private static final long serialVersionUID =
			// 3765437155300193263L;
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
			// add(new TagListView("tags", new
			// PropertyModel<List<Tag>>(ContentManagementDetailPanel.this,
			// "tagList")));

			add(new TagForm("tagForm"));

			// newArrivalDueDate
			// DateTextField newArrivalDueDateField = new
			// DateTextField("newArrivalDueDate", new
			// PropertyModel<Date>(assetBinaryVersion, "newArrivalDueDate"),
			// Constant.DATE_PATTERN);
			// newArrivalDueDateField.setRequired(true);
			// add(newArrivalDueDateField);
			// newArrivalDueDateField.add(new DatePicker());
			// add(new Label("label.newArrivalDueDate",
			// Tools.getValue(assetBinaryVersion.getNewArrivalDueDate())));

			// recommendStartDate
			// DateTextField recommendStartDateField = new
			// DateTextField("recommendStartDate", new
			// PropertyModel<Date>(assetBinaryVersion, "recommendStartDate"),
			// Constant.DATE_PATTERN);
			// recommendStartDateField.setRequired(true);
			// add(recommendStartDateField);
			// recommendStartDateField.add(new DatePicker());
			// add(new Label("label.recommendStartDate",
			// Tools.getValue(assetBinaryVersion.getRecommendStartDate())));

			// recommendDueDate
			// DateTextField recommendDueDateField = new
			// DateTextField("recommendDueDate", new
			// PropertyModel<Date>(assetBinaryVersion, "recommendDueDate"),
			// Constant.DATE_PATTERN);
			// recommendDueDateField.setRequired(true);
			// add(recommendDueDateField);
			// recommendDueDateField.add(new DatePicker());
			// add(new Label("label.recommendDueDate",
			// Tools.getValue(assetBinaryVersion.getRecommendDueDate())));

			// recommendOrder
			// TextField<Integer> recommendOrderField = new
			// TextField<Integer>("recommendOrder", new
			// PropertyModel<Integer>(assetBinaryVersion, "recommendOrder"));
			// recommendOrderField.setRequired(true);
			// add(recommendOrderField);
			// add(new Label("label.recommendOrder",
			// Tools.getValue(assetBinaryVersion.getRecommendOrder())));

			// thumbnailBig
			thumbnailBigField = new FileUploadField("thumbnailBig", new PropertyModel<FileUpload>(this, "thumbnailBigUpload"));
			thumbnailBigField.setRequired(false);
			thumbnailBigField.setOutputMarkupId(false);
			// thumbnailBigField.add(new AttributeModifier("onchange", true, new
			// Model<String>("document.getElementById('hiddenThumbnailBig').value = document.getElementById('thumbnailBig').value;")));
			add(thumbnailBigField);
			// hiddenThumbnailBig = new
			// HiddenField<String>("hiddenThumbnailBig", new
			// PropertyModel<String>(this, pathThumbnailBig));
			// hiddenThumbnailBig.setOutputMarkupId(false);
			// add(hiddenThumbnailBig);
			add(new BreadCrumbPanelLink("btn.thumbnailBig", ContentManagementDetailPanel.this.getBreadCrumbModel(), new IBreadCrumbPanelFactory() {

				private static final long serialVersionUID = -5113356719217533015L;

				public BreadCrumbPanel create(String componentId, IBreadCrumbModel breadCrumbModel) {
					return new ShowPicturePanel(componentId, breadCrumbModel, assetBinaryVersion.getId(), PICTURE_TYPE.thumbnailBig, ContentManagementDetailPanel.this);
				}
			}));

			// thumbnail
			thumbnailField = new FileUploadField("thumbnail", new PropertyModel<FileUpload>(this, "thumbnailUpload"));
			thumbnailField.setRequired(false);
			thumbnailField.setOutputMarkupId(false);
			add(thumbnailField);
			add(new BreadCrumbPanelLink("btn.thumbnail", ContentManagementDetailPanel.this.getBreadCrumbModel(), new IBreadCrumbPanelFactory() {

				private static final long serialVersionUID = -5113356719217533015L;

				public BreadCrumbPanel create(String componentId, IBreadCrumbModel breadCrumbModel) {
					return new ShowPicturePanel(componentId, breadCrumbModel, assetBinaryVersion.getId(), PICTURE_TYPE.thumbnail, ContentManagementDetailPanel.this);
				}
			}));

			// screenShot
			List<ScreenShots> scs = applicationService.getScreenShotsByAssetBinaryVersionId(assetBinaryVersion.getId());

			for (int i = 1; i < 6; i++) {
				if (null == scs || scs.size() == 0) {
					add(new Label("screenShot" + i, ""));
				} else {

					boolean checked = false;

					for (ScreenShots sc : scs) {
						if ((sc.getSequence() != null) && (sc.getSequence().intValue() == i)) {
							add(new Label("screenShot" + i, Tools.getValue(sc.getStoreLocation())));
							checked = true;
							break;
						}
					}

					if (!checked) {
						add(new Label("screenShot" + i, ""));
					}
				}

			}

			// screenShotField = new FileUploadField("screenShot", new
			// PropertyModel<FileUpload>(this, "screenShotUpload"));
			// screenShotField.setRequired(false);
			// screenShotField.setOutputMarkupId(false);
			// add(screenShotField);

			// dataView = new ScreenShotsDataView("screenShots", dataProvider,
			// itemsPerPage);
			// add(dataView);

			// Button addTagBtn = new Button("addTag") {
			//
			// private static final long serialVersionUID =
			// 8116120281966867260L;
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
					// externalIdField.setDefaultModel(new
					// Model<String>(externalIdGenDefault));
					// asset.setExternalId(externalIdGenDefault);
					externalIdField.add(new AttributeModifier("value", true, new Model<String>(externalIdGenDefault)));
				}
			};
			generateBtn.setDefaultFormProcessing(false);
			add(generateBtn);
			MetaDataRoleAuthorizationStrategy.authorize(generateBtn, Component.RENDER, Privilege.UPDATECONTENT);

			final CheckPanel checkPanel = new CheckPanel("checkPanel", getLocalizer().getString("title.prompt", ContentManagementDetailPanel.this), getLocalizer().getString("msg.warn.delete", ContentManagementDetailPanel.this)) {

				private static final long serialVersionUID = 7465424124110025236L;

				@Override
				public void howDo() {
					if (null == applicationService.getAssetBinaryById(binaryVersionId)) {
						ContentManagementPanel callPanel = (ContentManagementPanel) caller;
						callPanel.setPromptMsg(getLocalizer().getString("msg.error.delete.nobinary", ContentManagementDetailPanel.this, "This asset binary version is not exist!"));
						callPanel.setShowDeleteDialog(true);
						callPanel.activate(caller);
						return;
					}

					applicationService.deleteAssetBinary(binaryVersionId);
					if (caller.getClass().getName().equals(ContentManagementPanel.class.getName())) {
						ContentManagementPanel callPanel = (ContentManagementPanel) caller;
						callPanel.setPromptMsg(getLocalizer().getString("msg.result.delete.success", ContentManagementDetailPanel.this, "You successfully delete the asset binary version!"));
						callPanel.setShowDeleteDialog(true);
						callPanel.activate(caller);
					}
					caller.activate(caller);
				}
			};
			add(checkPanel);
			// delete button
			Button deleteBtn = new Button("btn.delete") {

				private static final long serialVersionUID = 8116120281966867260L;

				@Override
				public void onSubmit() {
					checkPanel.show();
				}
			};
			add(deleteBtn);
			MetaDataRoleAuthorizationStrategy.authorize(deleteBtn, Component.RENDER, Privilege.UPDATECONTENT);

			// back button
			Button backBtn = new Button("btn.back") {

				private static final long serialVersionUID = 8116120281966867260L;

				@Override
				public void onSubmit() {
					caller.activate(caller);
				}
			};
			backBtn.setDefaultFormProcessing(false);
			add(backBtn);

			final CheckPanel checkPanel1 = new CheckPanel("checkPanel1", getLocalizer().getString("title.prompt", ContentManagementDetailPanel.this), getLocalizer().getString("msg.warn.update", ContentManagementDetailPanel.this)) {

				private static final long serialVersionUID = 7465424124110025236L;

				@Override
				public void howDo() {
					// validation
					// if (null == applicationService.getAsset(asset.getId())) {
					// error("Please input valid Asset Id.");
					// return;
					// }

					// if (assetBinaryVersion.getOwnerAssetParentId() != null &&
					// null ==
					// applicationService.getAsset(assetBinaryVersion.getOwnerAssetParentId()))
					// {
					// error("Please input valid Parent Asset Id.");
					// return;
					// }

					if (!isSave) {
						if (null == applicationService.getAssetBinaryById(binaryVersionId)) {
							ContentManagementPanel callPanel = (ContentManagementPanel) caller;
							callPanel.setPromptMsg(getLocalizer().getString("msg.error.delete.nobinary", ContentManagementDetailPanel.this, "This asset binary version is not exist!"));
							callPanel.setShowDeleteDialog(true);
							callPanel.activate(caller);
							return;
						}

						// if (provider.getId() != null && null ==
						// applicationService.getAssetProviderById(provider.getId()))
						// {
						// error("Please input valid Provider Id.");
						// return;
						// }

						Asset assetUpdate;
						if (asset.getId() != null) {
							assetUpdate = applicationService.getAsset(asset.getId());
							if (!Tools.getValue(applicationService.getAsset(asset.getId()).getExternalId()).equals(Tools.getValue(asset.getExternalId()))) {
								if (asset.getExternalId().equals(externalIdGenDefault)) {
									assetUpdate = new Asset();
									assetUpdate.setExternalId(externalIdGenDefault);
									Status status = assetBinaryVersionLifeCycleEngine.getStartupStatus();
									assetUpdate.setStatus(status);
								} else {
									SearchExpression searchExpression = new SearchExpressionImpl();
									searchExpression.addCondition(new AssetExternalIdCondition(asset.getExternalId(), StringComparer.EQUAL, false, false));
									List<Asset> assetList = applicationService.searchAsset(searchExpression);
									if (assetList.size() < 1) {
										error(getLocalizer().getString("invalid.asset.external.id", ContentManagementDetailPanel.this, "Please input valid External Id."));
										externalIdField.add(new AttributeModifier("value", true, new Model<String>(asset.getExternalId())));
										return;
									} else {
										assetUpdate = new Asset();
										assetUpdate.setExternalId(asset.getExternalId());
										Status status = assetBinaryVersionLifeCycleEngine.getStartupStatus();
										assetUpdate.setStatus(status);
									}
								}
							}
						} else {
							if (asset.getExternalId() == null) {
								error(getLocalizer().getString("invalid.asset.external.id", ContentManagementDetailPanel.this, "Please input valid External Id."));
								return;
							} else {
								assetUpdate = new Asset();
								assetUpdate.setExternalId(asset.getExternalId());
								Status status = assetBinaryVersionLifeCycleEngine.getStartupStatus();
								assetUpdate.setStatus(status);
							}
						}

						// System.out.println(thumbnailBigField.getInput());
						// if (!pathThumbnailBig.equals(StringUtils.EMPTY) &&
						// !Tools.checkFilePath(pathThumbnailBig)) {
						// error(getLocalizer().getString("msg.error.fileupload",
						// ContentManagementDetailPanel.this,
						// "Please input valid file path!"));
						// return;
						// }

						Provider providerUpdate;
						if (provider.getId() != null) {
							providerUpdate = applicationService.getAssetProviderById(provider.getId());
							if (editProvider) {
								providerUpdate.setName(Tools.getValue(provider.getName()));
								if (StringUtils.isNotBlank(providerJpName)) {
									applicationService.removeAttributes(assetBinaryVersion.getId(), EntityType.BINARYVERSION, EavConstant.PROVIDER_JP_NAME);
									applicationService.addAttribute(assetBinaryVersion.getId(), EntityType.BINARYVERSION, EavConstant.PROVIDER_JP_NAME, providerJpName);
								}
								applicationService.updateAssetProvider(providerUpdate);
							}
						} else {
							SearchExpression searchProvider = new SearchExpressionImpl();
							SystemConfig systemConfig = applicationService.getSystemConfigByKey(Constant.SBMPID);
							searchProvider.addCondition(new ProviderExternalIdCondition(systemConfig.getValue(), StringComparer.EQUAL, false, false));
							List<Provider> providerList = applicationService.searchAssetProvider(searchProvider);
							if (providerList == null || providerList.size() < 1) {
								error(getLocalizer().getString("msg.error.update.noprovider", ContentManagementDetailPanel.this, "Can not find specify provider!"));
								return;
							}
							providerUpdate = providerList.get(0);
							if (editProvider) {
								providerUpdate.setName(Tools.getValue(provider.getName()));
								if (StringUtils.isNotBlank(providerJpName)) {
									applicationService.removeAttributes(assetBinaryVersion.getId(), EntityType.BINARYVERSION, EavConstant.PROVIDER_JP_NAME);
									applicationService.addAttribute(assetBinaryVersion.getId(), EntityType.BINARYVERSION, EavConstant.PROVIDER_JP_NAME, providerJpName);
								}
								applicationService.updateAssetProvider(providerUpdate);
							}
						}

						// update provider
						// Provider providerUpdate = null;
						// if (provider.getId() != null) {
						// providerUpdate =
						// applicationService.getAssetProviderById(provider.getId());
						// providerUpdate.setName(Tools.getValue(provider.getName()));
						// applicationService.updateAssetProvider(providerUpdate);
						// assetUpdate.setAssetProvider(providerUpdate);
						// }

						assetUpdate.setAssetProvider(providerUpdate);
						if (assetUpdate.getId() != null)
							applicationService.updateAsset(assetUpdate);
						else
							applicationService.saveAsset(assetUpdate);

						// Asset assetUpdate =
						// applicationService.getAsset(asset.getId());

						if (assetBinaryVersion.getAsset() != null) {
							applicationService.disassociateTagRelation(assetBinaryVersion.getAsset().getId());
							applicationService.disassociateTagRelation(assetBinaryVersion.getAsset().getId(), assetBinaryVersion.getId());
						}

						// update binary version
						assetBinaryVersion.setAsset(assetUpdate);
						applicationService.updateBinaryVersion(assetBinaryVersion);

						applicationService.disassociateTagRelation(assetUpdate.getId());
						applicationService.disassociateTagRelation(assetUpdate.getId(), assetBinaryVersion.getId());
						Set<Long> tagSet = new HashSet<Long>();
						for (Tag tag : tagList) {
							applicationService.saveTag(tag);
							tagSet.add(tag.getId());
						}
						applicationService.associateTagRelation(assetUpdate.getId(), assetBinaryVersion.getId(), tagSet);

						if (thumbnailBigUpload != null)
							applicationService.saveAssetBinaryPicture(assetBinaryVersion.getId(), Tools.getFileBuffer(thumbnailBigField), AssetPictureType.THUMBNAILBIGIMAGE, "THUMBNAILBIGIMAGE.jpg");

						if (thumbnailUpload != null)
							applicationService.saveAssetBinaryPicture(assetBinaryVersion.getId(), Tools.getFileBuffer(thumbnailField), AssetPictureType.THUMBNAILIMAGE, "THUMBNAILIMAGE.jpg");

						if (screenShotUpload != null) {
							ScreenShots screenShots = new ScreenShots();
							screenShots.setBinaryVersion(assetBinaryVersion);
							screenShots.setAsset(assetUpdate);
							// applicationService.saveScreenShots(screenShots,
							// Tools.getFileBuffer(screenShotField),
							// screenShotField.getFileUpload().getClientFileName());
							applicationService.saveScreenShots(screenShots, Tools.getFileBuffer(screenShotField), Tools.getFileName(screenShotField.getFileUpload().getClientFileName()));
						}

						// caller.activate(caller);
						promptPanel.setMessage(getLocalizer().getString("msg.result.update.success", ContentManagementDetailPanel.this, "You successfully update the asset binary version!"));
						promptPanel.show();

						editProvider = false;
						isSave = true;
					}
					dynamicFormChild.onFormSubmitted();

				}
			};
			add(checkPanel1);

			Button updateBtn = new Button("btn.update") {
				private static final long serialVersionUID = -6390474205215452323L;

				@Override
				public void onSubmit() {
					checkPanel1.show();
				}
			};
			add(updateBtn);
			MetaDataRoleAuthorizationStrategy.authorize(updateBtn, Component.RENDER, Privilege.UPDATECONTENT);
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

			this.setEntityType(EntityType.BINARYVERSION);
			this.setObjectId(binaryVersionId);

			genContent();

			Button updateEavBtn = new Button("addNew") {
				private static final long serialVersionUID = 8116120281966867260L;
			};
			/**
			 * business game ContentManagementNewPanel ContentManagementPage ContentManagementDetailPanel
			 **/
			// updateEavBtn.setDefaultFormProcessing(false);
			add(updateEavBtn);
			MetaDataRoleAuthorizationStrategy.authorize(updateEavBtn, Component.RENDER, Privilege.UPDATECONTENT);
		}

		@Override
		protected String getDataConfigFile() {
			return "ContentManagementDetail.xml";

		}

		@Override
		public Object getDomainModel() {
			return new HashedMap();
		}

		@Override
		public void onSubmit() {

			if (null == applicationService.getAssetBinaryById(binaryVersionId)) {
				ContentManagementPanel callPanel = (ContentManagementPanel) caller;
				callPanel.setPromptMsg(getLocalizer().getString("msg.error.delete.nobinary", ContentManagementDetailPanel.this, "This asset binary version is not exist!"));
				callPanel.setShowDeleteDialog(true);
				callPanel.activate(caller);
				return;
			}

			String key = "";
			try {
				for (Iterator<String> iterator = eavMap.keySet().iterator(); iterator.hasNext();) {
					key = iterator.next();
					String value = eavMap.get(key);
					if (null == value)
						continue;

					applicationService.removeAttributes(binaryVersionId, EntityType.BINARYVERSION, key);
					String getDataFieldType = DynamicFormChild.this.dyDataModel.getDataFieldType(key);
					if (StringUtils.isEmpty(getDataFieldType))
						continue;

					if (getDataFieldType.equals("Date"))
						applicationService.addAttribute(binaryVersionId, EntityType.BINARYVERSION, key, new SimpleDateFormat(Constant.DATE_PATTERN).format(value));
					else if (getDataFieldType.equals("String"))
						applicationService.addAttribute(binaryVersionId, EntityType.BINARYVERSION, key, String.valueOf(value));
					else if (getDataFieldType.equals("Long"))
						applicationService.addAttribute(binaryVersionId, EntityType.BINARYVERSION, key, Long.valueOf(value));
					else if (getDataFieldType.equals("Float"))
						applicationService.addAttribute(binaryVersionId, EntityType.BINARYVERSION, key, Float.valueOf(value));
					else if (getDataFieldType.equals("Integer"))
						applicationService.addAttribute(binaryVersionId, EntityType.BINARYVERSION, key, Float.valueOf(value).intValue());
					else if (getDataFieldType.equals("Double"))
						applicationService.addAttribute(binaryVersionId, EntityType.BINARYVERSION, key, Double.valueOf(value));
					else if (getDataFieldType.equals("BigDecimal"))
						applicationService.addAttribute(binaryVersionId, EntityType.BINARYVERSION, key, BigDecimal.valueOf(Long.valueOf(value)));

				}
				promptPanel.setMessage(getLocalizer().getString("msg.result.eavupdate.success", ContentManagementDetailPanel.this, "You successfully update the eav properties of the asset binary version!"));
				promptPanel.show();

			} catch (Exception e) {
				error(getLocalizer().getString("field.wrongType", ContentManagementDetailPanel.this) + key);
			}
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
			MetaDataRoleAuthorizationStrategy.authorize(delLink, Component.RENDER, Privilege.UPDATECONTENT);
		}
	};
}