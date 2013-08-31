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
package com.hp.sdf.ngp.ui.page.oam;

import org.apache.wicket.PageParameters;
import org.apache.wicket.extensions.breadcrumb.BreadCrumbBar;
import org.apache.wicket.extensions.breadcrumb.panel.BreadCrumbPanel;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;

import com.hp.sdf.ngp.ui.common.Constant.PANEL_TYPE;
import com.hp.sdf.ngp.ui.page.WicketPage;
import com.hp.sdf.ngp.ui.page.oam.account.AccountSearchPanel;
import com.hp.sdf.ngp.ui.page.oam.category.CategoryListPanel;
import com.hp.sdf.ngp.ui.page.oam.comment.CensoredWordListPanel;
import com.hp.sdf.ngp.ui.page.oam.comment.CommentsSearchPanel;
import com.hp.sdf.ngp.ui.page.oam.content.ContentManagementPanel;
import com.hp.sdf.ngp.ui.page.oam.content.RecommendedManagementPanel;
import com.hp.sdf.ngp.ui.page.oam.platform.PlatformSearchPanel;
import com.hp.sdf.ngp.ui.page.oam.storeclient.StoreClientEditPanel;

public class NavigatorPage extends WicketPage {

	private BreadCrumbBar breadCrumbBar;

	private BreadCrumbPanel panel;

	public NavigatorPage(final PageParameters parameters) {
		super(parameters);

		breadCrumbBar = new BreadCrumbBar("breadCrumbBar");
		add(breadCrumbBar);
		breadCrumbBar.setVisible(false);
		panel = new ContentManagementPanel("panel", breadCrumbBar);
		add(panel);
		breadCrumbBar.setActive(panel);

		add(new Label("name1", "Binary Version Management"));
		genNavigator("name1.link1", PANEL_TYPE.ContentManagementPanel, "name1.link1.name", "Binary Version List");
		genNavigator("name1.link2", PANEL_TYPE.RecommendedManagementPanel, "name1.link2.name", "Recommended Management");

		add(new Label("name2", "Category Management"));
		genNavigator("name2.link1", PANEL_TYPE.CategoryListPanel, "name2.link1.name", "Category List");

		add(new Label("name3", "Comments Management"));
		genNavigator("name3.link1", PANEL_TYPE.CensoredWordListPanel, "name3.link1.name", "Censored Word Management");
		genNavigator("name3.link2", PANEL_TYPE.CommentsSearchPanel, "name3.link2.name", "Comments Management");

		add(new Label("name4", "Store Client Management"));
		genNavigator("name4.link1", PANEL_TYPE.StoreClientEditPanel, "name4.link1.name", "Edit Store Client");

		add(new Label("name5", "Account Management"));
		genNavigator("name5.link1", PANEL_TYPE.AccountSearchPanel, "name5.link1.name", "Account List");

		add(new Label("name6", "Platform Management"));
		genNavigator("name6.link1", PANEL_TYPE.PlatformListPanel, "name6.link1.name", "Platform List");
	}

	@SuppressWarnings("unchecked")
	private void genNavigator(String linkName, final PANEL_TYPE panelType, String labelName, String labelValue) {
		Link link = new Link(linkName) {
			private static final long serialVersionUID = -2701774859395076664L;

			public void onClick() {
				NavigatorPage.this.remove(breadCrumbBar);
				breadCrumbBar = new BreadCrumbBar("breadCrumbBar");
				breadCrumbBar.setVisible(false);
				NavigatorPage.this.add(breadCrumbBar);
				NavigatorPage.this.remove(panel);
				switch (panelType) {
				case ContentManagementPanel:
					panel = new ContentManagementPanel("panel", breadCrumbBar);
					break;
				case RecommendedManagementPanel:
					panel = new RecommendedManagementPanel("panel", breadCrumbBar);
					break;
				case CategoryListPanel:
					panel = new CategoryListPanel("panel", breadCrumbBar);
					break;
				case CensoredWordListPanel:
					panel = new CensoredWordListPanel("panel", breadCrumbBar);
					break;
				case CommentsSearchPanel:
					panel = new CommentsSearchPanel("panel", breadCrumbBar);
					break;
				case StoreClientEditPanel:
					panel = new StoreClientEditPanel("panel", breadCrumbBar);
					break;
				case AccountSearchPanel:
					panel = new AccountSearchPanel("panel", breadCrumbBar);
					break;
				case PlatformListPanel:
					panel = new PlatformSearchPanel("panel", breadCrumbBar);
					break;
				}

				NavigatorPage.this.add(panel);
				breadCrumbBar.setActive(panel);
			}
		};
		link.add(new Label(labelName, labelValue));
		// link.setOutputMarkupId(true);
		add(link);
	}
}
