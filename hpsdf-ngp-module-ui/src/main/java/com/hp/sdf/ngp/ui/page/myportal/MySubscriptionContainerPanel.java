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
package com.hp.sdf.ngp.ui.page.myportal;

import org.apache.wicket.extensions.breadcrumb.BreadCrumbBar;
import org.apache.wicket.markup.html.panel.Panel;

import com.hp.sdf.ngp.ui.page.purchase.MySubscriptionPanel;

public class MySubscriptionContainerPanel extends Panel {

	public MySubscriptionContainerPanel(String id) {
		super(id);
		BreadCrumbBar breadCrumbBar = new BreadCrumbBar("breadCrumbBar");
		add(breadCrumbBar);
		MySubscriptionPanel mySubscriptionPanel = new MySubscriptionPanel("mySubscriptionPanel",
				breadCrumbBar);
		breadCrumbBar.setActive(mySubscriptionPanel);
		add(mySubscriptionPanel);
	}

}

// $Id$