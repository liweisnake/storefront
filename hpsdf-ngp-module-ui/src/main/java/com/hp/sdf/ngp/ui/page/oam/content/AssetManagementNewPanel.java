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
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.extensions.breadcrumb.IBreadCrumbModel;
import org.apache.wicket.extensions.breadcrumb.panel.BreadCrumbPanel;
import org.apache.wicket.extensions.yui.calendar.DatePicker;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.hp.sdf.ngp.common.annotation.Value;
import com.hp.sdf.ngp.model.Asset;
import com.hp.sdf.ngp.model.AssetBinaryVersion;
import com.hp.sdf.ngp.model.Category;
import com.hp.sdf.ngp.model.Provider;
import com.hp.sdf.ngp.model.ScreenShots;
import com.hp.sdf.ngp.model.Status;
import com.hp.sdf.ngp.service.ApplicationService;
import com.hp.sdf.ngp.service.AssetPictureType;
import com.hp.sdf.ngp.ui.common.Constant;
import com.hp.sdf.ngp.ui.common.CustomizeDateTextField;
import com.hp.sdf.ngp.ui.common.PromptPanel;
import com.hp.sdf.ngp.ui.common.SelectOption;
import com.hp.sdf.ngp.ui.common.Tools;
import com.hp.sdf.ngp.ui.provider.AssetScreenShotsDataProvider;
import com.hp.sdf.ngp.workflow.AssetBinaryVersionLifeCycleEngine;

public class AssetManagementNewPanel extends BreadCrumbPanel {

	private static final long serialVersionUID = -8855725744830146629L;

	public final static Log log = LogFactory.getLog(AssetManagementNewPanel.class);

	@SpringBean
	private ApplicationService applicationService;

	@SpringBean
	private AssetBinaryVersionLifeCycleEngine assetBinaryVersionLifeCycleEngine;

	private final BreadCrumbPanel caller;

	@Value("application.itemsperpage")
	private int itemsPerPage = Constant.DEFALUT_PER_PAGE_COUNT;

	private AssetScreenShotsDataProvider dataProvider;

	private AssetScreenShotsDataView dataView;

	private PromptPanel promptPanel;

	public AssetManagementNewPanel(String id, IBreadCrumbModel breadCrumbModel, final BreadCrumbPanel caller) {
		super(id, breadCrumbModel);

		this.caller = caller;

		breadCrumbModel.setActive(this);

		// default setting link
		// add(new BreadCrumbPanelLink("defaultSetting", AssetManagementNewPanel.this.getBreadCrumbModel(), new IBreadCrumbPanelFactory() {
		//
		// private static final long serialVersionUID = -5113356719217533015L;
		//
		// public BreadCrumbPanel create(String componentId, IBreadCrumbModel breadCrumbModel) {
		// return new DefaultSettingPanel(componentId, breadCrumbModel, caller);
		// }
		// }));

		// new binary version
		// add(new BreadCrumbPanelLink("newBinaryVersion", AssetManagementNewPanel.this.getBreadCrumbModel(), new IBreadCrumbPanelFactory() {
		//
		// private static final long serialVersionUID = -7078020538448932972L;
		//
		// public BreadCrumbPanel create(String componentId, IBreadCrumbModel breadCrumbModel) {
		// return new AssetManagementNewPanel(componentId, breadCrumbModel, caller);
		// }
		// }));

		Form<Void> newForm = new AssetManagementNewForm("newForm", applicationService);
		add(newForm);

		final FeedbackPanel feedback = new FeedbackPanel("feedback");
		add(feedback);

		promptPanel = new PromptPanel("promptPanel", getLocalizer().getString("title.prompt", this, "Prompt"), getLocalizer().getString("msg.save.result", this, "You successfully save the asset!"), null, StringUtils.EMPTY);
		add(promptPanel);
	}

	public String getTitle() {
		return this.getLocalizer().getString("title", this, "New Asset Detail");
	}

	public class AssetManagementNewForm extends Form<Void> {

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
		private Long parentId;

		// upload
		private FileUploadField thumbnailBigField;
		private FileUploadField thumbnailMedField;
		private FileUploadField thumbnailField;
		private FileUploadField fileField;
		private FileUploadField screenShotField;
		private FileUpload thumbnailBigUpload;
		private FileUpload thumbnailMedUpload;
		private FileUpload thumbnailUpload;
		private FileUpload screenShotUpload;
		private FileUpload fileUpload;

		public AssetManagementNewForm(String id, final ApplicationService applicationService) {

			super(id);

			this.applicationService = applicationService;

			dataProvider = new AssetScreenShotsDataProvider(applicationService);

			// assetName
			TextField<String> assetNameField = new TextField<String>("assetName", new PropertyModel<String>(asset, "name"));
			assetNameField.setRequired(true);
			add(assetNameField);

			// externalId
			// TextField<String> externalIdField = new TextField<String>("externalId", new PropertyModel<String>(assetBinaryVersion, "externalId"));
			// externalIdField.setRequired(true);
			// add(externalIdField);

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
			TextField<Long> parentAssetIdField = new TextField<Long>("parentAssetId", new PropertyModel<Long>(this, "parentId"));
			parentAssetIdField.setRequired(false);
			add(parentAssetIdField);

			// priority
			// TextField<Integer> priorityField = new TextField<Integer>("priority", new PropertyModel<Integer>(provider, "priority"));
			// add(priorityField);

			// fileName
			// TextField<String> fileNameField = new
			// TextField<String>("fileName", new
			// PropertyModel<String>(assetBinaryVersion, "fileName"));
			// fileNameField.setRequired(false);
			// add(fileNameField);

			// providerId
			TextField<Long> providerIdField = new TextField<Long>("providerId", new PropertyModel<Long>(provider, "id"));
			providerIdField.setRequired(false);
			add(providerIdField);

			TextField<Long> binaryIdField = new TextField<Long>("binaryId", new PropertyModel<Long>(assetBinaryVersion, "id"));
			binaryIdField.setRequired(false);
			add(binaryIdField);

			// providerName
			// TextField<String> providerNameField = new TextField<String>("providerName", new PropertyModel<String>(provider, "name"));
			// providerNameField.setRequired(false);
			// add(providerNameField);

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
			TextField<String> briefField = new TextField<String>("brief", new PropertyModel<String>(asset, "brief"));
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
			CustomizeDateTextField publishDateField = new CustomizeDateTextField("publishDate", new PropertyModel<Date>(asset, "publishDate"), Constant.DATE_PATTERN);
			publishDateField.setRequired(false);
			add(publishDateField);
			publishDateField.add(new DatePicker());

			// expireDate
			// CustomizeDateTextField expireDateField = new CustomizeDateTextField("expireDate", new PropertyModel<Date>(asset, "expireDate"), Constant.DATE_PATTERN);
			// expireDateField.setRequired(true);
			// add(expireDateField);
			// expireDateField.add(new DatePicker());

			// fileSize
			// TextField<BigDecimal> fileSizeField = new TextField<BigDecimal>("fileSize", new PropertyModel<BigDecimal>(asset, "fileSize"));
			// fileSizeField.setRequired(true);
			// add(fileSizeField);

			// version
			// TextField<String> versionField = new TextField<String>("version", new PropertyModel<String>(asset, "version"));
			// versionField.setRequired(true);
			// add(versionField);

			// file
			// fileField = new FileUploadField("file", new PropertyModel<FileUpload>(this, "fileUpload"));
			// fileField.setRequired(true);
			// add(fileField);

			// tag
			// TextField<String> tagField = new TextField<String>("tag", new PropertyModel<String>(this, "tag"));
			// tagField.setRequired(true);
			// add(tagField);

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
			thumbnailMedField = new FileUploadField("thumbnailMed", new PropertyModel<FileUpload>(this, "thumbnailMedUpload"));
			thumbnailMedField.setRequired(false);
			add(thumbnailMedField);

			// thumbnail
			thumbnailField = new FileUploadField("thumbnail", new PropertyModel<FileUpload>(this, "thumbnailUpload"));
			thumbnailField.setRequired(false);
			add(thumbnailField);

			// screenShot
			screenShotField = new FileUploadField("screenShot", new PropertyModel<FileUpload>(this, "screenShotUpload"));
			screenShotField.setRequired(false);
			add(screenShotField);

			dataView = new AssetScreenShotsDataView("screenShots", dataProvider, itemsPerPage);
			add(dataView);

			// add(new CustomizePagingNavigator("navigator", dataView));

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

			Button saveBtn = new Button("btn.save") {

				private static final long serialVersionUID = 8116120281966867260L;

				@Override
				public void onSubmit() {
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

					if (parentId != null && null == applicationService.getAsset(parentId)) {
						error("Please input valid Parent Asset Id.");
						return;
					}

					if (provider.getId() != null && null == applicationService.getAssetProviderById(provider.getId())) {
						error("Please input valid Provider Id.");
						return;
					}

					if (assetBinaryVersion.getId() != null && null == applicationService.getAssetBinaryById(assetBinaryVersion.getId())) {
						error("Please input valid Asset Binary Version Id.");
						return;
					}

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
					// if (asset.getPublishDate().compareTo(asset.getExpireDate()) >= 0) {
					// error("Publish Date can not greater than Expire Date.");
					// return;
					// }
					// ---------- validation end ----------

					// update or save provider
					Provider providerUpdate = null;
					if (provider.getId() != null) {
						providerUpdate = applicationService.getAssetProviderById(provider.getId());
						// providerUpdate.setExternalId(provider.getExternalId());
						// providerUpdate.setPriority(provider.getPriority());
						// providerUpdate.setName(Tools.getValue(provider.getName()));
						// applicationService.updateAssetProvider(providerUpdate);
					}
					// else {
					// providerUpdate = new Provider();
					// applicationService.saveAssetProvider(providerUpdate);
					// }

					// get initial status
					Status status = assetBinaryVersionLifeCycleEngine.getStartupStatus();

					// save asset
					Asset assetUpdate;
					// if (asset.getId() != null) {
					// assetUpdate = applicationService.getAsset(asset.getId());
					// if (provider.getId() != null)
					// assetUpdate.setAssetProvider(providerUpdate);
					// applicationService.updateAsset(assetUpdate);
					// } else {
					assetUpdate = new Asset();
					if (provider.getId() != null)
						assetUpdate.setAssetProvider(providerUpdate);
					assetUpdate.setStatus(status);
					assetUpdate.setSource(Constant.ASSET_SOURCE);
					assetUpdate.setName(asset.getName());
					assetUpdate.setBrief(asset.getBrief());
					assetUpdate.setPublishDate(asset.getPublishDate());
					if (parentId != null) {
						Asset parentAsset = applicationService.getAsset(parentId);
						assetUpdate.setAsset(parentAsset);
					}
					applicationService.saveAsset(assetUpdate);

					AssetBinaryVersion assetBinaryVersionNew = null;
					if (assetBinaryVersion.getId() != null) {
						assetBinaryVersionNew = applicationService.getAssetBinaryById(assetBinaryVersion.getId());
						assetBinaryVersionNew.setAsset(assetUpdate);
						applicationService.updateBinaryVersion(assetBinaryVersionNew);
					} else {
						assetBinaryVersionNew = new AssetBinaryVersion();
					}
					// }

					// save binary version
					// assetBinaryVersion.setFileName(fileField.getFileUpload().getClientFileName());
					// assetBinaryVersion.setAsset(assetUpdate);
					// assetBinaryVersion.setStatus(status);
					// applicationService.saveAssetBinaryVersion(assetBinaryVersion);
					// applicationService.saveAssetBinary(Tools.getFileBuffer(fileField), assetBinaryVersion);

					applicationService.associateCategory(assetUpdate.getId(), assetBinaryVersionNew.getId(), category2.getKey());

					if (thumbnailBigUpload != null)
						applicationService.saveAssetPicture(assetUpdate.getId(), Tools.getFileBuffer(thumbnailBigField), AssetPictureType.THUMBNAILBIGIMAGE, "THUMBNAILBIGIMAGE.jpg");
					if (thumbnailMedUpload != null)
						applicationService.saveAssetPicture(assetUpdate.getId(), Tools.getFileBuffer(thumbnailMedField), AssetPictureType.THUMBNAILMIDDLEIMAGE, "THUMBNAILMIDDLEIMAGE.jpg");
					if (thumbnailUpload != null)
						applicationService.saveAssetPicture(assetUpdate.getId(), Tools.getFileBuffer(thumbnailField), AssetPictureType.THUMBNAILIMAGE, "THUMBNAILIMAGE.jpg");

					if (screenShotUpload != null) {
						ScreenShots screenShots = new ScreenShots();
						// screenShots.setBinaryVersion(assetBinaryVersion);
						screenShots.setAsset(assetUpdate);
						// applicationService.saveScreenShots(screenShots, Tools.getFileBuffer(screenShotField), screenShotField.getFileUpload().getClientFileName());
						applicationService.saveScreenShots(screenShots, Tools.getFileBuffer(screenShotField), Tools.getFileName(screenShotField.getFileUpload().getClientFileName()));
					}

					try {
						BeanUtils.copyProperties(asset, assetUpdate);
					} catch (Exception ex) {
						log.error(ex.getMessage());
					}

					promptPanel.setMessage(getLocalizer().getString("msg.save.result", AssetManagementNewPanel.this, "You successfully save the asset!"));
					promptPanel.show();
					// setResponsePage(ContentManagementPage.class);
					// caller.activate(caller);
				}
			};
			saveBtn.setDefaultFormProcessing(true);
			saveBtn.add(Tools.addConfirmJs(getLocalizer().getString("msg.save", AssetManagementNewPanel.this, "Do you want to save the asset?")));
			add(saveBtn);
		}

		public final void onSubmit() {
		}
	}

	class AssetScreenShotsDataView extends DataView<ScreenShots> {

		private static final long serialVersionUID = -6154935955544130768L;

		protected AssetScreenShotsDataView(String id, IDataProvider<ScreenShots> dataProvider, int itemsPerPage) {
			super(id, dataProvider, itemsPerPage);
		}

		@Override
		protected void populateItem(Item<ScreenShots> item) {
			ScreenShots screenShots = item.getModelObject();

			item.add(new Label("pictureUrl", Tools.getValue(screenShots.getStoreLocation())));
		}
	}
}