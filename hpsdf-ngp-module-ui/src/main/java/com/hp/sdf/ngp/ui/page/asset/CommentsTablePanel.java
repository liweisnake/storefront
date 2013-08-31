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
package com.hp.sdf.ngp.ui.page.asset;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.navigation.paging.PagingNavigator;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.hp.sdf.ngp.common.util.Utils;
import com.hp.sdf.ngp.model.Comments;
import com.hp.sdf.ngp.service.ApplicationService;
import com.hp.sdf.ngp.service.UserService;
import com.hp.sdf.ngp.ui.provider.CommentDataProvider;

public class CommentsTablePanel extends Panel {

	private static final long serialVersionUID = 8006956197490194311L;

	@SpringBean
	private ApplicationService applicationService;

	@SpringBean
	private UserService userService;

	private Long appId;

	private CommentsView commentsView;

	private static final int itemsPerPage = 5;

	public CommentsTablePanel(String id, Long appId) {
		super(id);

		CommentDataProvider dataProvider = new CommentDataProvider(applicationService, appId);
		commentsView = new CommentsView("comments", dataProvider, itemsPerPage);
		commentsView.setOutputMarkupId(true);
		add(commentsView);
		add(new PagingNavigator("navigator", commentsView));
	}

	class CommentsView extends DataView<Comments> {

		private static final long serialVersionUID = 2738548802166595044L;

		public CommentsView(String id, IDataProvider<Comments> dataProvider, int itemsPerPage) {
			super(id, dataProvider, itemsPerPage);
		}

		@Override
		protected void populateItem(Item<Comments> item) {

			Comments comments = item.getModelObject();

			// adds user name
			String userName = null;
			userName = comments.getUserid();

			item.add(new Label("username", userName));

			item.add(new Label("assetversion", comments.getAssetVersion()));

			// adds comments
			String content = comments.getContent();
			item.add(new Label("content", content));

			// adds date
			item.add(new Label("date", Utils.dateToLongString(comments.getCreateDate())));
		}

	}

}
