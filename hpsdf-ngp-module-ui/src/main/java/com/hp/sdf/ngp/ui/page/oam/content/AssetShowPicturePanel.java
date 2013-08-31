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

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.extensions.breadcrumb.IBreadCrumbModel;
import org.apache.wicket.extensions.breadcrumb.panel.BreadCrumbPanel;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.image.ContextImage;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.hp.sdf.ngp.model.Asset;
import com.hp.sdf.ngp.service.ApplicationService;
import com.hp.sdf.ngp.ui.common.Constant;
import com.hp.sdf.ngp.ui.common.LocalImageUri;

public class AssetShowPicturePanel extends BreadCrumbPanel {

	private static final long serialVersionUID = -8524244192134679105L;

	private static Log log = LogFactory.getLog(AssetShowPicturePanel.class);

	@SpringBean
	ApplicationService applicationService;

	private final BreadCrumbPanel caller;

	private Long assetId;
	private Constant.PICTURE_TYPE picture_type;

	public ApplicationService getApplicationService() {
		return applicationService;
	}

	public void setApplicationService(ApplicationService applicationService) {
		this.applicationService = applicationService;
	}

	public AssetShowPicturePanel(String id, IBreadCrumbModel breadCrumbModel, Long assetId, Constant.PICTURE_TYPE picture_type, final BreadCrumbPanel caller) {
		super(id, breadCrumbModel);

		this.caller = caller;
		this.assetId = assetId;
		this.picture_type = picture_type;

		add(new BackForm("form"));
	}

	class BackForm extends Form<Void> {
		private static final long serialVersionUID = 1053492451941171577L;

		public BackForm(String id) {
			super(id);

			String location = StringUtils.EMPTY;

			Asset asset = applicationService.getAsset(assetId);
			switch (picture_type) {
			case thumbnail:
				location = asset.getThumbnailLocation();
				break;
			case thumbnailBig:
				location = asset.getThumbnailBigLocation();
				break;
			case thumbnailMiddle:
				location = asset.getThumbnailMiddleLocation();
				break;
			}

			if (StringUtils.isEmpty(location)) {
				add(new ContextImage("image", "images/null_thumbnail.jpg"));
			} else {
				add(new LocalImageUri("image", location));
			}
		}

		@Override
		protected void onSubmit() {
			log.debug("back");
			caller.activate(caller);
		}
	}

	public String getTitle() {
		return this.getLocalizer().getString("title", this, Constant.SHOW_PICTURE);
	}
}