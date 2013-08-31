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
package com.hp.sdf.ngp.ui.page.oam.comment;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.Component;
import org.apache.wicket.authorization.strategies.role.metadata.MetaDataRoleAuthorizationStrategy;
import org.apache.wicket.extensions.breadcrumb.IBreadCrumbModel;
import org.apache.wicket.extensions.breadcrumb.panel.BreadCrumbPanel;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.PropertyModel;

import com.hp.sdf.ngp.ui.common.CommentSearchCondition;
import com.hp.sdf.ngp.workflow.Privilege;

public class CommentsSearchPanel extends BreadCrumbPanel {

	private final static Log log = LogFactory.getLog(CommentsSearchPanel.class);

	private static final long serialVersionUID = 5673531943315003111L;

	private SearchCommentsTablePanel searchCommentsTablePanel = null;

	private CommentSearchCondition commentSearchCondition = null;

	public CommentsSearchPanel(String id, IBreadCrumbModel breadCrumbModel) {
		super(id, breadCrumbModel);

		SearchCommentForm form = new SearchCommentForm("newCommentsForm");
		MetaDataRoleAuthorizationStrategy.authorize(form, Component.RENDER, Privilege.SEARCHCOMMENTS);
		add(form);

		searchCommentsTablePanel = new SearchCommentsTablePanel("commentsTable", commentSearchCondition, breadCrumbModel);
		MetaDataRoleAuthorizationStrategy.authorize(searchCommentsTablePanel, Component.RENDER, Privilege.VIEWCOMMENTSLIST);
		add(searchCommentsTablePanel);
	}

	class SearchCommentForm extends Form<Void> {

		private static final long serialVersionUID = 404463636873167639L;

		private String keyword;
		private String pid;
		private String providerName;
		private String assetID; // assetID
		private String assetName; // assetName
		private String subscriberName;

		public SearchCommentForm(String id) {
			super(id);

			TextField<String> keyword = new TextField<String>("keyword", new PropertyModel<String>(this, "keyword"));
			add(keyword);

			TextField<String> pid = new TextField<String>("pid", new PropertyModel<String>(this, "pid"));
			add(pid);

			TextField<String> providerName = new TextField<String>("providerName", new PropertyModel<String>(this, "providerName"));
			add(providerName);

			TextField<String> assetID = new TextField<String>("assetID", new PropertyModel<String>(this, "assetID"));
			add(assetID);

			TextField<String> assetName = new TextField<String>("assetName", new PropertyModel<String>(this, "assetName"));
			add(assetName);

			//USERID of comments
			TextField<String> subscriberName = new TextField<String>("subscriberName", new PropertyModel<String>(this, "subscriberName"));
			add(subscriberName);

			add(new Button("search") {
				private static final long serialVersionUID = 2263842147860607342L;

				public void onSubmit() {
					log.debug("keyword: " + SearchCommentForm.this.keyword);
					log.debug("pid: " + SearchCommentForm.this.pid);
					log.debug("providerName: " + SearchCommentForm.this.providerName);
					log.debug("assetID: " + SearchCommentForm.this.assetID);
					log.debug("subscriberName: " + SearchCommentForm.this.subscriberName);
					log.debug("assetName: " + SearchCommentForm.this.assetName);

					commentSearchCondition = new CommentSearchCondition();
					commentSearchCondition.setAssetID(SearchCommentForm.this.assetID);
					commentSearchCondition.setAssetName(SearchCommentForm.this.assetName);
					commentSearchCondition.setKeyword(SearchCommentForm.this.keyword);
					commentSearchCondition.setPid(SearchCommentForm.this.pid);
					commentSearchCondition.setProviderName(SearchCommentForm.this.providerName);
					commentSearchCondition.setSubscriberName(SearchCommentForm.this.subscriberName);

					log.debug("searchCommentsTablePanel.commentsView.updateModel");
					if (searchCommentsTablePanel.commentsView != null) {
						searchCommentsTablePanel.commentsView.updateModel(commentSearchCondition);
					}
				}

			});
		}
	}

	public String getTitle() {
		return this.getLocalizer().getString("title", this, "Search Comments");
	}

}
