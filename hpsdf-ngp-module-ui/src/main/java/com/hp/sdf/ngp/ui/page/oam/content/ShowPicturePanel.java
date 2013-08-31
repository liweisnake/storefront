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

import com.hp.sdf.ngp.model.AssetBinaryVersion;
import com.hp.sdf.ngp.service.ApplicationService;
import com.hp.sdf.ngp.ui.common.Constant;
import com.hp.sdf.ngp.ui.common.LocalImageUri;

public class ShowPicturePanel extends BreadCrumbPanel {

	private static final long serialVersionUID = -8524244192134679105L;

	private static Log log = LogFactory.getLog(StatusChangeHistoryPanel.class);

	@SpringBean
	ApplicationService applicationService;

	private final BreadCrumbPanel caller;

	private Long assetBinaryVersionId;
	private Constant.PICTURE_TYPE picture_type;

	public ApplicationService getApplicationService() {
		return applicationService;
	}

	public void setApplicationService(ApplicationService applicationService) {
		this.applicationService = applicationService;
	}

	public ShowPicturePanel(String id, IBreadCrumbModel breadCrumbModel, Long assetBinaryVersionId, Constant.PICTURE_TYPE picture_type, final BreadCrumbPanel caller) {
		super(id, breadCrumbModel);

		this.caller = caller;
		this.assetBinaryVersionId = assetBinaryVersionId;
		this.picture_type = picture_type;

		add(new BackForm("form"));
	}

	class BackForm extends Form<Void> {
		private static final long serialVersionUID = 1053492451941171577L;

		public BackForm(String id) {
			super(id);

			String location = StringUtils.EMPTY;

			AssetBinaryVersion assetBinaryVersion = applicationService.getAssetBinaryById(assetBinaryVersionId);
			switch (picture_type) {
			case thumbnail:
				location = assetBinaryVersion.getThumbnailLocation();
				break;
			case thumbnailBig:
				location = assetBinaryVersion.getThumbnailBigLocation();
				break;
			case thumbnailMiddle:
				location = assetBinaryVersion.getThumbnailMiddleLocation();
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