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

import org.apache.wicket.PageParameters;
import org.apache.wicket.extensions.breadcrumb.BreadCrumbBar;

import com.hp.sdf.ngp.ui.page.WicketPage;

public class ContentManagementPage extends WicketPage {
	public ContentManagementPage(PageParameters parameters) {
		super(parameters);
		BreadCrumbBar breadCrumbBar = new BreadCrumbBar("breadCrumbBar");
		add(breadCrumbBar);
		breadCrumbBar.setVisible(false);
		ContentManagementPanel contentManagementPanel = new ContentManagementPanel("contentManagementPanel", breadCrumbBar);
		add(contentManagementPanel);
		breadCrumbBar.setActive(contentManagementPanel);
	}
}