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
package com.hp.sdf.ngp.ais.ui.page;

import org.apache.wicket.PageParameters;
import org.apache.wicket.markup.html.link.Link;

import com.hp.sdf.ngp.ais.ui.page.banner.management.BannerListPage;
import com.hp.sdf.ngp.ais.ui.page.banner.rotatingbanner.RotatingBannerPage;
import com.hp.sdf.ngp.ais.ui.page.banner.staticbanner.StaticBannerPage;
import com.hp.sdf.ngp.ais.ui.page.banner.tabbedbanner.TabbedBannerPage;

public class HomePage extends WicketPage {

	@SuppressWarnings( { "unchecked", "serial" })
	public HomePage(final PageParameters parameters) {
		super(parameters);

		final Link lindStaticBanner = new Link("lindStaticBanner") {
			public void onClick() {
				setResponsePage(StaticBannerPage.class);
			}
		};
		this.add(lindStaticBanner);

		final Link lindRotatingBanner = new Link("lindRotatingBanner") {
			public void onClick() {
				setResponsePage(RotatingBannerPage.class);
			}
		};
		this.add(lindRotatingBanner);
		
		final Link listBanner = new Link("BannerList") {
			public void onClick() {
				setResponsePage(BannerListPage.class);
			}
		};
		this.add(listBanner);
		
		final Link lindTabbedBanner = new Link("lindTabbedBanner") {
			public void onClick() {
				setResponsePage(TabbedBannerPage.class);
			}
		};
		this.add(lindTabbedBanner);
		
	}

}
