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

import org.apache.wicket.extensions.breadcrumb.IBreadCrumbModel;

public class ApplyPendingPanel extends WorkFlowBreadCrumbPanel {

	private static final long serialVersionUID = 1095109691778668378L;

	public ApplyPendingPanel(String id, IBreadCrumbModel breadCrumbModel) {
		super(id, breadCrumbModel);
	}

	public String getTitle() {
		return this.getLocalizer().getString("title", this, "Apply pending");
	}

}

// $Id$