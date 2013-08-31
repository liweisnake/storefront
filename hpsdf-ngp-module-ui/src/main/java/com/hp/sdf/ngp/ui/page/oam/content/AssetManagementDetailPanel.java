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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.extensions.breadcrumb.IBreadCrumbModel;
import org.apache.wicket.extensions.breadcrumb.panel.BreadCrumbPanel;
import org.apache.wicket.extensions.breadcrumb.panel.BreadCrumbPanelLink;
import org.apache.wicket.extensions.breadcrumb.panel.IBreadCrumbPanelFactory;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
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
import org.drools.util.StringUtils;

import com.hp.sdf.ngp.common.annotation.Value;
import com.hp.sdf.ngp.model.Asset;
import com.hp.sdf.ngp.model.Provider;
import com.hp.sdf.ngp.model.ScreenShots;
import com.hp.sdf.ngp.service.ApplicationService;
import com.hp.sdf.ngp.service.AssetPictureType;
import com.hp.sdf.ngp.ui.common.Constant;
import com.hp.sdf.ngp.ui.common.PromptPanel;
import com.hp.sdf.ngp.ui.common.Tools;
import com.hp.sdf.ngp.ui.common.Constant.PICTURE_TYPE;
import com.hp.sdf.ngp.ui.provider.AssetScreenShotsDataProvider;

public class AssetManagementDetailPanel extends BreadCrumbPanel {

	private static final long serialVersionUID = -8855725744830146629L;

	public final static Log log = LogFactory.getLog(AssetManagementDetailPanel.class);

	@SpringBean
	private ApplicationService applicationService;

	@Value("application.itemsperpage")
	private int itemsPerPage = Constant.DEFALUT_PER_PAGE_COUNT;

	private BreadCrumbPanel caller;

	private final Long assetId;

	private AssetScreenShotsDataProvider dataProvider;

	private AssetScreenShotsDataView dataView;

	private PromptPanel promptPanel;

	public AssetManagementDetailPanel(String id, IBreadCrumbModel breadCrumbModel, Long assetId, final BreadCrumbPanel caller) {
		super(id, breadCrumbModel);

		this.assetId = assetId;
		this.caller = caller;
		breadCrumbModel.setActive(this);

		// default setting link
		// add(new BreadCrumbPanelLink("defaultSetting", AssetManagementDetailPanel.this.getBreadCrumbModel(), new IBreadCrumbPanelFactory() {
		//
		// private static final long serialVersionUID = -5113356719217533015L;
		//
		// public BreadCrumbPanel create(String componentId, IBreadCrumbModel breadCrumbModel) {
		// return new DefaultSettingPanel(componentId, breadCrumbModel, caller);
		// }
		// }));

		// new binary version
		add(new BreadCrumbPanelLink("newAsset", AssetManagementDetailPanel.this.getBreadCrumbModel(), new IBreadCrumbPanelFactory() {

			private static final long serialVersionUID = -7078020538448932972L;

			public BreadCrumbPanel create(String componentId, IBreadCrumbModel breadCrumbModel) {
				return new AssetManagementNewPanel(componentId, breadCrumbModel, caller);
			}
		}));

		Form<Void> detailForm = new AssetManagementDetailForm("detailForm", applicationService);
		add(detailForm);

		final FeedbackPanel feedback = new FeedbackPanel("feedback");
		add(feedback);

		promptPanel = new PromptPanel("promptPanel", getLocalizer().getString("title.prompt", this, "Prompt"), getLocalizer().getString("msg.update.result", this, "You successfully update the asset!"), null, StringUtils.EMPTY);
		add(promptPanel);
	}

	public String getTitle() {
		return this.getLocalizer().getString("title", this, "Asset Detail");
	}

	public class AssetManagementDetailForm extends Form<Void> {

		private static final long serialVersionUID = -1347352088252117228L;

		ApplicationService applicationService;

		private Asset asset;
		private Provider provider;
		// private Status status;

		// private BigDecimal price = new BigDecimal(0);
		// private String category1, category2;
		// private String tag;

		// upload
		private FileUploadField thumbnailBigField;
		private FileUploadField thumbnailMedField;
		private FileUploadField thumbnailField;
		private FileUploadField screenShotField;
		private FileUpload thumbnailBigUpload;
		private FileUpload thumbnailMedUpload;
		private FileUpload thumbnailUpload;
		private FileUpload screenShotUpload;

		public AssetManagementDetailForm(String id, final ApplicationService applicationService) {

			super(id);

			this.applicationService = applicationService;

			dataProvider = new AssetScreenShotsDataProvider(applicationService, assetId);

			asset = applicationService.getAsset(assetId);

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
			TextField<String> assetNameField = new TextField<String>("assetName", new PropertyModel<String>(asset, "name"));
			assetNameField.setRequired(true);
			add(assetNameField);
			add(new Label("label.assetName", Tools.getValue(asset.getName())));

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
			// TextField<Long> assetIdField = new TextField<Long>("assetId", new PropertyModel<Long>(asset, "id"));
			// assetIdField.setRequired(true);
			// add(assetIdField);
			// add(new Label("label.assetId", Tools.getValue(asset.getId())));

			// parentAssetId
			// TextField<Long> parentAssetIdField = new TextField<Long>("parentAssetId", new PropertyModel<Long>(assetBinaryVersion, "ownerAssetParentId"));
			// parentAssetIdField.setRequired(false);
			// add(parentAssetIdField);
			// add(new Label("label.parentAssetId", Tools.getValue(assetBinaryVersion.getOwnerAssetParentId())));

			// fileName
			// TextField<String> fileNameField = new
			// TextField<String>("fileName", new
			// PropertyModel<String>(assetBinaryVersion, "fileName"));
			// fileNameField.setRequired(true);
			// add(fileNameField);
			// add(new Label("label.fileName",
			// Tools.getValue(assetBinaryVersion.getFileName())));

			// providerId
			TextField<Long> providerIdField = new TextField<Long>("providerId", new PropertyModel<Long>(provider, "id"));
			providerIdField.setRequired(false);
			add(providerIdField);
			add(new Label("label.providerId", Tools.getValue(provider.getId())));

			// providerName
			// TextField<String> providerNameField = new TextField<String>("providerName", new PropertyModel<String>(provider, "name"));
			// providerNameField.setRequired(false);
			// add(providerNameField);
			// add(new Label("label.providerName", Tools.getValue(provider.getName())));

			// category1
			// List<Category> categoryList =
			// applicationService.getAllCategoryByAssetId(asset.getId(), 0,
			// Integer.MAX_VALUE);
			// if (categoryList.size() < 1) {
			// category1 = category2 = StringUtils.EMPTY;
			// } else if (categoryList.size() == 1) {
			// category1 = category2 = categoryList.get(0).getName();
			// } else {
			// category1 = categoryList.get(0).getName();
			// category2 = categoryList.get(1).getName();
			// }
			// TextField<String> category1Field = new
			// TextField<String>("category1", new PropertyModel<String>(this,
			// "category1"));
			// category1Field.setRequired(true);
			// add(category1Field);
			// add(new Label("label.category1", this.category1));

			// category2
			// TextField<String> category2Field = new
			// TextField<String>("category2", new PropertyModel<String>(this,
			// "category2"));
			// category2Field.setRequired(true);
			// add(category2Field);
			// add(new Label("label.category2", this.category2));

			// description
			// TextArea<String> descriptionField = new
			// TextArea<String>("description", new
			// PropertyModel<String>(assetBinaryVersion, "description"));
			// add(descriptionField);
			// add(new Label("label.description",
			// Tools.getValue(assetBinaryVersion.getDescription())));

			// brief
			TextField<String> briefField = new TextField<String>("brief", new PropertyModel<String>(asset, "brief"));
			briefField.setRequired(false);
			add(briefField);
			add(new Label("label.brief", Tools.getValue(asset.getBrief())));

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
			add(thumbnailBigField);
			add(new BreadCrumbPanelLink("btn.thumbnailBig", AssetManagementDetailPanel.this.getBreadCrumbModel(), new IBreadCrumbPanelFactory() {

				private static final long serialVersionUID = -5113356719217533015L;

				public BreadCrumbPanel create(String componentId, IBreadCrumbModel breadCrumbModel) {
					return new AssetShowPicturePanel(componentId, breadCrumbModel, asset.getId(), PICTURE_TYPE.thumbnailBig, AssetManagementDetailPanel.this);
				}
			}));

			// thumbnailMed
			thumbnailMedField = new FileUploadField("thumbnailMed", new PropertyModel<FileUpload>(this, "thumbnailMedUpload"));
			thumbnailMedField.setRequired(false);
			thumbnailMedField.setOutputMarkupId(false);
			add(thumbnailMedField);
			add(new BreadCrumbPanelLink("btn.thumbnailMed", AssetManagementDetailPanel.this.getBreadCrumbModel(), new IBreadCrumbPanelFactory() {

				private static final long serialVersionUID = -5113356719217533015L;

				public BreadCrumbPanel create(String componentId, IBreadCrumbModel breadCrumbModel) {
					return new AssetShowPicturePanel(componentId, breadCrumbModel, asset.getId(), PICTURE_TYPE.thumbnailMiddle, AssetManagementDetailPanel.this);
				}
			}));

			// thumbnail
			thumbnailField = new FileUploadField("thumbnail", new PropertyModel<FileUpload>(this, "thumbnailUpload"));
			thumbnailField.setRequired(false);
			thumbnailField.setOutputMarkupId(false);
			add(thumbnailField);
			add(new BreadCrumbPanelLink("btn.thumbnail", AssetManagementDetailPanel.this.getBreadCrumbModel(), new IBreadCrumbPanelFactory() {

				private static final long serialVersionUID = -5113356719217533015L;

				public BreadCrumbPanel create(String componentId, IBreadCrumbModel breadCrumbModel) {
					return new AssetShowPicturePanel(componentId, breadCrumbModel, asset.getId(), PICTURE_TYPE.thumbnail, AssetManagementDetailPanel.this);
				}
			}));

			// screenShot
			screenShotField = new FileUploadField("screenShot", new PropertyModel<FileUpload>(this, "screenShotUpload"));
			screenShotField.setRequired(false);
			screenShotField.setOutputMarkupId(false);
			add(screenShotField);

			dataView = new AssetScreenShotsDataView("screenShots", dataProvider, itemsPerPage);
			add(dataView);

			// delete button
			Button deleteBtn = new Button("btn.delete") {

				private static final long serialVersionUID = 8116120281966867260L;

				@Override
				public void onSubmit() {

					if (null == applicationService.getAsset(assetId)) {
						AssetManagementPanel callPanel = (AssetManagementPanel) caller;
						callPanel.setPromptMsg(getLocalizer().getString("msg.error.delete", AssetManagementDetailPanel.this, "This asset is not exist!"));
						callPanel.setShowDeleteDialog(true);
						callPanel.activate(caller);
						return;
					}

					applicationService.deleteAsset(assetId);
					if (caller.getClass().getName().equals(AssetManagementPanel.class.getName())) {
						AssetManagementPanel callPanel = (AssetManagementPanel) caller;
						callPanel.setPromptMsg(getLocalizer().getString("msg.delete.result", AssetManagementDetailPanel.this, "You successfully delete the asset!"));
						callPanel.setShowDeleteDialog(true);
						callPanel.activate(caller);
					}
					caller.activate(caller);
				}
			};
			deleteBtn.add(Tools.addConfirmJs(getLocalizer().getString("msg.delete", AssetManagementDetailPanel.this, "Do you want to delete the asset?")));
			add(deleteBtn);

			// back button
			Button backBtn = new Button("btn.back") {

				private static final long serialVersionUID = 8116120281966867260L;

				@Override
				public void onSubmit() {
					caller.activate(caller);
				}
			};
			add(backBtn);

			Button updateBtn = new Button("btn.update") {
				private static final long serialVersionUID = -6390474205215452323L;

				@Override
				public void onSubmit() {
					// validation
					// if (null == applicationService.getAsset(asset.getId())) {
					// error("Please input valid Asset Id.");
					// return;
					// }

					// if (assetBinaryVersion.getOwnerAssetParentId() != null && null == applicationService.getAsset(assetBinaryVersion.getOwnerAssetParentId())) {
					// error("Please input valid Parent Asset Id.");
					// return;
					// }

					if (null == applicationService.getAsset(assetId)) {
						AssetManagementPanel callPanel = (AssetManagementPanel) caller;
						callPanel.setPromptMsg(getLocalizer().getString("msg.error.delete", AssetManagementDetailPanel.this, "This asset is not exist!"));
						callPanel.setShowDeleteDialog(true);
						callPanel.activate(caller);
						return;
					}

					if (provider.getId() != null && null == applicationService.getAssetProviderById(provider.getId())) {
						error("Please input valid Provider Id.");
						return;
					}

					// System.out.println(thumbnailBigField.getInput());
					// if (!pathThumbnailBig.equals(StringUtils.EMPTY) && !Tools.checkFilePath(pathThumbnailBig)) {
					// error(getLocalizer().getString("msg.error.fileupload", AssetManagementDetailPanel.this, "Please input valid file path!"));
					// return;
					// }

					// update provider
					if (provider.getId() != null) {
						Provider providerUpdate = applicationService.getAssetProviderById(provider.getId());
						// providerUpdate.setName(Tools.getValue(provider.getName()));
						// applicationService.updateAssetProvider(providerUpdate);
						asset.setAssetProvider(providerUpdate);
					}

					// Asset assetUpdate = applicationService.getAsset(asset.getId());

					applicationService.updateAsset(asset);

					// update binary version
					// assetBinaryVersion.setAsset(assetUpdate);
					// applicationService.updateBinaryVersion(assetBinaryVersion);

					if (thumbnailBigUpload != null)
						applicationService.saveAssetPicture(asset.getId(), Tools.getFileBuffer(thumbnailBigField), AssetPictureType.THUMBNAILBIGIMAGE, "THUMBNAILBIGIMAGE.jpg");
					if (thumbnailMedUpload != null)
						applicationService.saveAssetPicture(asset.getId(), Tools.getFileBuffer(thumbnailMedField), AssetPictureType.THUMBNAILMIDDLEIMAGE, "THUMBNAILMIDDLEIMAGE.jpg");
					if (thumbnailUpload != null)
						applicationService.saveAssetPicture(asset.getId(), Tools.getFileBuffer(thumbnailField), AssetPictureType.THUMBNAILIMAGE, "THUMBNAILIMAGE.jpg");

					if (screenShotUpload != null) {
						ScreenShots screenShots = new ScreenShots();
						screenShots.setAsset(asset);
						// screenShots.setAsset(assetUpdate);
						// applicationService.saveScreenShots(screenShots, Tools.getFileBuffer(screenShotField), screenShotField.getFileUpload().getClientFileName());
						applicationService.saveScreenShots(screenShots, Tools.getFileBuffer(screenShotField), Tools.getFileName(screenShotField.getFileUpload().getClientFileName()));
					}

					// caller.activate(caller);
					promptPanel.setMessage(getLocalizer().getString("msg.update.result", AssetManagementDetailPanel.this, "You successfully update the asset!"));
					promptPanel.show();
				}
			};
			updateBtn.add(Tools.addConfirmJs(getLocalizer().getString("msg.update", AssetManagementDetailPanel.this, "Do you want to update the asset?")));
			add(updateBtn);
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