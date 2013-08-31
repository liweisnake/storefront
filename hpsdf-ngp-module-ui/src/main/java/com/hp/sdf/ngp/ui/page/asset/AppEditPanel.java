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
package com.hp.sdf.ngp.ui.page.asset;

import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.PageParameters;
import org.apache.wicket.extensions.breadcrumb.IBreadCrumbModel;
import org.apache.wicket.extensions.breadcrumb.panel.BreadCrumbPanel;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.ListMultipleChoice;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.validation.validator.MinimumValidator;

import com.hp.sdf.ngp.common.constant.AssetConstants;
import com.hp.sdf.ngp.model.Asset;
import com.hp.sdf.ngp.model.AssetBinaryVersion;
import com.hp.sdf.ngp.model.AssetPrice;
import com.hp.sdf.ngp.model.Category;
import com.hp.sdf.ngp.model.Platform;
import com.hp.sdf.ngp.model.Provider;
import com.hp.sdf.ngp.model.Status;
import com.hp.sdf.ngp.service.ApplicationService;
import com.hp.sdf.ngp.service.AssetPictureType;
import com.hp.sdf.ngp.ui.WicketSession;
import com.hp.sdf.ngp.ui.common.Constant;
import com.hp.sdf.ngp.ui.common.Tools;
import com.hp.sdf.ngp.workflow.AssetLifeCycleEngine;

@SuppressWarnings( { "unchecked" })
public class AppEditPanel extends BreadCrumbPanel {

	private static final long serialVersionUID = 5893583801905587437L;

	public final static Log log = LogFactory.getLog(AppEditPanel.class);

	@SpringBean
	private ApplicationService applicationService;

	@SpringBean
	private AssetLifeCycleEngine assetLifeCycleEngine;

	private BreadCrumbPanel caller;

	private byte[] previewBuffer;

	private byte[] thumbnailBuffer;

	private byte[] documentBuffer;

	public AppEditPanel(String id, IBreadCrumbModel breadCrumbModel, BreadCrumbPanel caller) {
		super(id, breadCrumbModel);
		breadCrumbModel.setActive(this);
		AppForm appForm = new AppForm("appForm", applicationService);
		add(appForm);
		this.caller = caller;

		// Create feedback panel and add to page
		final FeedbackPanel feedback = new FeedbackPanel("feedback");
		add(feedback);
	}

	public String getTitle() {
		return this.getLocalizer().getString("title", this, "Application Profile");
	}

	public class AppForm extends Form {

		/**
		 * 
		 */
		private static final long serialVersionUID = -1347352088252117228L;

		ApplicationService applicationService;

		Asset app = new Asset();

		private BigDecimal assetPrice = new BigDecimal(0);

		private List<Platform> platformList = new ArrayList<Platform>();

		private FileUploadField previewField;

		private FileUploadField thumbnailField;

		private FileUploadField documentField;

		private FileUpload previewUpload;

		private FileUpload thumbnailUpload;

		private List<Category> categoryList = new ArrayList<Category>();

		public List<Platform> getPlatformList() {
			return platformList;
		}

		public void setPlatformList(List<Platform> platformList) {
			this.platformList = platformList;
		}

		public AppForm(String id, ApplicationService applicationService) {

			super(id);
			this.applicationService = applicationService;

			TextField<String> nameField = new TextField<String>("name", new PropertyModel<String>(app, "name"));
			nameField.setRequired(true);
			add(nameField);
			TextField<String> briefField = new TextField<String>("brief", new PropertyModel<String>(app, "brief"));
			add(briefField);
			// List<AssetPrice> assetPriceList = applicationService.getAssetPriceByAssetId(app.getId());
			// assetPrice = GetPropValueFromList.getAssetPriceFromListDollars(assetPriceList);
			TextField<BigDecimal> priceField = new TextField<BigDecimal>("price", new PropertyModel<BigDecimal>(this, "assetPrice"));
			priceField.add(new MinimumValidator<BigDecimal>(new BigDecimal(0)));
			add(priceField);
			previewField = new FileUploadField("preview", new PropertyModel<FileUpload>(this, "previewUpload"));
			previewField.setRequired(true);
			add(previewField);

			thumbnailField = new FileUploadField("thumbnail", new PropertyModel<FileUpload>(this, "thumbnailUpload"));
			thumbnailField.setRequired(true);
			add(thumbnailField);
			TextArea<String> descriptionField = new TextArea<String>("description", new PropertyModel<String>(app, "description"));
			add(descriptionField);
			// adds category
			List<Category> categories = applicationService.getAllCategory(0, Integer.MAX_VALUE);
			ChoiceRenderer<List<Category>> cagetoryRenderer = new ChoiceRenderer<List<Category>>("name", "id");
			ListMultipleChoice categoryChoice = new ListMultipleChoice("category", new PropertyModel(this, "categoryList"), categories, cagetoryRenderer);
			categoryChoice.setRequired(true);
			add(categoryChoice);
			// adds platform
			List<Platform> platforms = applicationService.getAllPlatform(0, Integer.MAX_VALUE);
			ChoiceRenderer<List<Platform>> choiceRenderer = new ChoiceRenderer<List<Platform>>("name", "id");
			ListMultipleChoice platformChoice = new ListMultipleChoice("platform", new PropertyModel(this, "platformList"), platforms, choiceRenderer);
			platformChoice.setRequired(true);
			add(platformChoice);

			RequiredTextField<String> docField = new RequiredTextField<String>("doc", new PropertyModel<String>(app, "docUrl"));
			add(docField);
		}

		/**
		 * @see org.apache.wicket.markup.html.form.Form#onSubmit()
		 */
		public final void onSubmit() {

			if (log.isDebugEnabled()) {
				log.debug("app profile submitted.");
				log.debug("app name : " + app.getName());
				// log.debug("category id : " + category.getId());
				// log.debug("app price : " + app.getAssetPrice());
				// List<AssetPrice> assetPriceList = applicationService.getAssetPriceByAssetId(app.getId());
				log.debug("app price : " + assetPrice);
				log.debug("app brief : " + app.getBrief());
				log.debug("preview pic : " + previewField.getInput());
				log.debug("thumbnail pic : " + thumbnailField.getInput());
				log.debug("app description : " + app.getDescription());
				log.debug("app doc : " + app.getDocUrl());
			}

			previewUpload = previewField.getFileUpload();

			thumbnailUpload = thumbnailField.getFileUpload();

			// preivew and thumbnail pic is required
			try {
				previewBuffer = null;
				InputStream inputStream = previewUpload.getInputStream();
				previewBuffer = new byte[inputStream.available()];
				inputStream.read(previewBuffer);

				thumbnailBuffer = null;
				inputStream = thumbnailUpload.getInputStream();
				thumbnailBuffer = new byte[inputStream.available()];
				inputStream.read(thumbnailBuffer);

			} catch (Exception e) {
				throw new IllegalStateException("Unable to write file");
			}

			// sets init status
			Status status = assetLifeCycleEngine.getStartupStatus();
			app.setStatus(status);

			// List<Long> categoryIds = new ArrayList<Long>();
			// categoryIds.add(category.getId());
			// app.setUserid(WicketSession.get().getUserId());
			app.setAuthorid(WicketSession.get().getUserId());

			Provider provider = applicationService.getAssetProviderByName(WicketSession.get().getUserId());
			app.setAssetProvider(provider);

			app.setCreateDate(new Date());

			// app.setPlatform(platform);
			app.setSource(AssetConstants.ASSET_SOURCE_STOREFRONT);
			applicationService.saveAsset(app);
			// AppEditPanel.this.applicationService.saveAsset(app, categoryIds);
			// save the price of asset
			AssetPrice assetPriceModel = new AssetPrice();
			assetPriceModel.setCurrency(Constant.CURRENCY_DOLLARS);
			assetPriceModel.setAmount(assetPrice);
			assetPriceModel.setAsset(app);
			List<AssetBinaryVersion> assetBinaryVersionList = applicationService.getAssetBinaryByAssetId(app.getId());
			if (assetBinaryVersionList != null && assetBinaryVersionList.size() > 0)
				assetPriceModel.setBinaryVersion(assetBinaryVersionList.get(0));
			AppEditPanel.this.applicationService.saveAssetPrice(assetPriceModel);
			applicationService.associatePlatform(app.getId(), Tools.getPlatformIdList(platformList));
			applicationService.associateCategory(app.getId(), app.getCurrentVersionId(), Tools.getCategoryIdSet(categoryList));
			AppEditPanel.this.applicationService.saveAssetPicture(app.getId(), previewBuffer, AssetPictureType.THUMBNAILBIGIMAGE,"THUMBNAILBIGIMAGE.jpg");
			AppEditPanel.this.applicationService.saveAssetPicture(app.getId(), thumbnailBuffer, AssetPictureType.THUMBNAILIMAGE,"THUMBNAILIMAGE.jpg");

			PageParameters params = new PageParameters();
			params.add("appId", app.getId().toString());

			AppEditPanel.this.caller.activate(caller);
			MyAppDetailPanel detailPanel = new MyAppDetailPanel(AppEditPanel.this.getId(), AppEditPanel.this.caller.getBreadCrumbModel(), app.getId(), AppEditPanel.this.caller);
			AppEditPanel.this.caller.activate(detailPanel);
		}
	}

}

// $Id$