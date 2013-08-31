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
package com.hp.sdf.ngp.ui.page.register;

import org.apache.wicket.PageParameters;
import org.apache.wicket.extensions.breadcrumb.IBreadCrumbModel;
import org.apache.wicket.extensions.breadcrumb.panel.BreadCrumbPanel;
import org.apache.wicket.markup.html.link.Link;

public class WelcomePanel extends BreadCrumbPanel {

	private static final long serialVersionUID = -6090809177607451179L;

	@SuppressWarnings("unchecked")
	public WelcomePanel(String id, final IBreadCrumbModel breadCrumbModel) {
		super(id, breadCrumbModel);

//		BreadCrumbPanelLink createLink = new BreadCrumbPanelLink("createLink", breadCrumbModel, new IBreadCrumbPanelFactory() {
//
//			private static final long serialVersionUID = 1L;
//
//			public BreadCrumbPanel create(String componentId, IBreadCrumbModel breadCrumb) {
//				
//				PageParameters parameters = new PageParameters();
//				parameters.put("selectTab", 1);
//				UserRegisterPage page = new UserRegisterPage(parameters);
//				setResponsePage(page);
//			}
//		});
//		this.add(createLink);
		
		Link createLink = new Link("createLink") {

			private static final long serialVersionUID = 8116120281966867260L;

			public void onClick() {
				PageParameters parameters = new PageParameters();
				parameters.put("selectTab", 1);
				UserRegisterPage page = new UserRegisterPage(parameters);
				setResponsePage(page);
			}
		};
		add(createLink);

	}

	public String getTitle() {
		return this.getLocalizer().getString("title", this, "Welcome");
	}
}
