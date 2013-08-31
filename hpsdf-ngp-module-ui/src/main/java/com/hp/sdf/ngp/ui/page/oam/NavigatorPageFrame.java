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
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;

import com.hp.sdf.ngp.ui.page.WicketPage;
import com.hp.sdf.ngp.ui.page.oam.account.AccountSearchPage;
import com.hp.sdf.ngp.ui.page.oam.category.CategoryListPage;
import com.hp.sdf.ngp.ui.page.oam.comment.CensoredWordListPage;
import com.hp.sdf.ngp.ui.page.oam.comment.CommentsSearchPage;
import com.hp.sdf.ngp.ui.page.oam.content.ContentManagementPage;
import com.hp.sdf.ngp.ui.page.oam.content.RecommendedManagementPage;
import com.hp.sdf.ngp.ui.page.oam.storeclient.StoreClientEditPage;

public class NavigatorPageFrame extends WebPage {
	public NavigatorPageFrame(final PageParameters parameters) {
		super(parameters);

		add(new Label("name1", "Binary Version Management"));
		// genNavigator("name1.link1", ContentManagementPage.class, "name1.link1.name", "Binary Version List");

		Link link = new Link("name1.link1") {
			private static final long serialVersionUID = -2701774859395076664L;

			public void onClick() {
				setResponsePage(ContentManagementPage.class);
			}
		};
		link.add(new Label("name1.link1.name", "Binary Version List"));
		add(link);

		genNavigator("name1.link2", RecommendedManagementPage.class, "name1.link2.name", "Recommended Management");

		add(new Label("name2", "Category Management"));
		genNavigator("name2.link1", CategoryListPage.class, "name2.link1.name", "Category List");

		add(new Label("name3", "Comments Management"));
		genNavigator("name3.link1", CensoredWordListPage.class, "name3.link1.name", "Censored Word Management");
		genNavigator("name3.link2", CommentsSearchPage.class, "name3.link2.name", "Comments Management");

		add(new Label("name4", "Store Client Management"));
		genNavigator("name4.link1", StoreClientEditPage.class, "name4.link1.name", "Edit Store Client");

		add(new Label("name5", "Account Management"));
		genNavigator("name5.link1", AccountSearchPage.class, "name5.link1.name", "Account List");

	}

	@SuppressWarnings("unchecked")
	private <C extends WicketPage> void genNavigator(String linkName, final Class<C> clazz, String labelName, String labelValue) {
		Link link = new Link(linkName) {
			private static final long serialVersionUID = -2701774859395076664L;

			public void onClick() {
				setResponsePage(clazz);
			}
		};
		link.add(new Label(labelName, labelValue));
		link.setOutputMarkupId(true);
		add(link);
	}
}
