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
package com.hp.sdf.ngp.ais.ui.page.banner.management;

import org.apache.wicket.Page;
import org.apache.wicket.PageParameters;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.hp.sdf.ngp.ais.ui.page.banner.rotatingbanner.RotatingBannerPage;
import com.hp.sdf.ngp.ais.ui.page.banner.staticbanner.StaticBannerPage;
import com.hp.sdf.ngp.ais.ui.page.banner.tabbedbanner.TabbedBannerPage;
import com.hp.sdf.ngp.banner.BannerService;
import com.hp.sdf.ngp.banner.BannerType;
import com.hp.sdf.ngp.banner.model.Banner;

public class BannerListActionPanel extends Panel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4212601738050690148L;

	private BannerProxy selected;

	final ConfirmMessageBox confirmMessageBox = new ConfirmMessageBox("confirmMessageBox");

	@SpringBean
	private BannerService bannerService;

	private void delete() {
		bannerService.deleteBanner(selected.getId());
	}

	public BannerListActionPanel(final Page backPage, String id, IModel<BannerProxy> model) {
		super(id, model);
		add(this.confirmMessageBox);
		add(new Link("edit") {
			/**
			 * 
			 */
			private static final long serialVersionUID = 7607177697047653839L;

			@Override
			public void onClick() {
				selected = (BannerProxy) getParent().getDefaultModelObject();

				PageParameters pageParameters = new PageParameters();
				pageParameters.put("bannerId", selected.getId());
				String type = selected.getBannerType();
				if (type.endsWith(BannerType.staticBanner.toString())) {
					StaticBannerPage staticBannerPage = new StaticBannerPage(pageParameters);
					setResponsePage(staticBannerPage);
				} else if (type.endsWith(BannerType.rotatingBanner.toString())) {
					RotatingBannerPage rotatingBannerPage = new RotatingBannerPage(pageParameters);
					setResponsePage(rotatingBannerPage);
				} else if (type.endsWith(BannerType.tabbedBanner.toString())) {
					TabbedBannerPage tabbedBannerPage = new TabbedBannerPage(pageParameters);
					setResponsePage(tabbedBannerPage);
				}
			}

		});

		// add(new AjaxLink("remove") {
		// /**
		// *
		// */
		// private static final long serialVersionUID = -9873872521343L;
		//
		// @Override
		// public void onClick(final AjaxRequestTarget target) {
		// selected = (Banner) getParent().getDefaultModelObject();
		// MessageBoxCommand confirm = new MessageBoxCommand() {
		//
		// private static final long serialVersionUID = 3225899071373240250L;
		//
		// public void run() {
		// delete();
		// }
		//
		// public Page getResponsePage() {
		// return new BannerListPage();
		// }
		// };
		// confirmMessageBox.show(target, "Confirm",
		// "Are you sure for the operation?", confirm);
		// }
		// });

		add(new Link("remove") {
			/**
			 * 
			 */
			private static final long serialVersionUID = -9873872521343L;

			@Override
			public void onClick() {
				selected = (BannerProxy) getParent().getDefaultModelObject();
				BannerListActionPanel.this.delete();
				setResponsePage(BannerListPage.class);
			}
		});

	}

}

// $Id$