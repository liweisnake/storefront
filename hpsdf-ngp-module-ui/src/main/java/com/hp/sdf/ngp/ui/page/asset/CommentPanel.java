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

import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.Component;
import org.apache.wicket.authorization.strategies.role.Roles;
import org.apache.wicket.authorization.strategies.role.metadata.MetaDataRoleAuthorizationStrategy;
import org.apache.wicket.feedback.ContainerFeedbackMessageFilter;
import org.apache.wicket.feedback.FeedbackMessage;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.validation.validator.StringValidator;

import com.hp.sdf.ngp.api.exception.UserCommentsCensorFailException;
import com.hp.sdf.ngp.common.exception.CensoredWordFoundException;
import com.hp.sdf.ngp.model.Asset;
import com.hp.sdf.ngp.model.Comments;
import com.hp.sdf.ngp.service.ApplicationService;
import com.hp.sdf.ngp.service.UserService;
import com.hp.sdf.ngp.ui.WicketSession;
import com.hp.sdf.ngp.workflow.AccessType;
import com.hp.sdf.ngp.workflow.AssetLifeCycleEngine;

public class CommentPanel extends Panel {

	private final static Log log = LogFactory.getLog(CommentPanel.class);

	private static final long serialVersionUID = 5673531943315003111L;

	@SpringBean
	private ApplicationService applicationService;

	@SpringBean
	private UserService userService;

	@SpringBean
	AssetLifeCycleEngine assetLifeCycleEngine;

	private Long appId;

	private Panel commentsTable = null;

	// final FeedbackPanel feedback = new FeedbackPanel("commentsFeedback");

	@SuppressWarnings("unchecked")
	public CommentPanel(String id, Long appId) {
		super(id);
		this.appId = appId;

		// comments input area is invisible in author-edit view
		AddCommentForm form = new AddCommentForm("newCommentsForm", this.applicationService);
		add(form);
		// apply privilege
		String[] privileges = assetLifeCycleEngine.getAccessPrivilege(appId, AccessType.COMMENT);
		Roles roles = new Roles(privileges);
		MetaDataRoleAuthorizationStrategy.authorize(form, Component.RENDER, roles.toString());

		commentsTable = new CommentsTablePanel("commentsTable", appId);
		commentsTable.setOutputMarkupId(true);
		add(commentsTable);

	}

	class AddCommentForm extends Form {

		private static final long serialVersionUID = 404463636873167639L;

		private final ApplicationService appService;

		private String comments;

		TextArea<String> commentsArea;

		final FeedbackPanel feedback;

		public String getComments() {
			return comments;
		}

		public void setComments(String comments) {
			this.comments = comments;
		}

		public AddCommentForm(String id, ApplicationService applicationService) {
			super(id);
			this.appService = applicationService;

			// Create feedback panel and add to page
			feedback = new FeedbackPanel("commentsFeedback", new ContainerFeedbackMessageFilter(this));
			feedback.setOutputMarkupId(true);
			add(feedback);

			commentsArea = new TextArea<String>("comments", new PropertyModel<String>(this, "comments"));
			commentsArea.setOutputMarkupId(true);
			commentsArea.setRequired(true);
			commentsArea.add(new StringValidator.MaximumLengthValidator(500));
			add(commentsArea);

			// add(new AjaxButton("submitComments", this) {
			// @Override
			// protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
			// // save new comments
			// if (log.isDebugEnabled()) {
			// log.debug("new comments: " + AddCommentForm.this.comments);
			// }
			//
			// Comments newComments = new Comments();
			// String userId = WicketSession.get().getUserId();
			//
			// newComments.setUserid(userId);
			// Asset app = appService.getAsset(appId);
			// newComments.setAsset(app);
			// newComments.setContent(comments);
			// newComments.setCreateDate(new Date());
			// newComments.setAssetVersion(app.getCurrentVersion());
			// appService.saveComments(newComments);
			//
			// target.addComponent(commentsTable);
			//
			// AddCommentForm.this.comments = null;
			// target.addComponent(commentsArea);
			//
			// target.addComponent(feedback);
			// }
			//
			// @Override
			// protected void onError(AjaxRequestTarget target, Form<?> form) {
			// // repaint the feedback panel so errors are shown
			// target.addComponent(feedback);
			// }
			// });

		}

		@Override
		protected void onError() {
			// clearInput();
		}

		@Override
		protected void onSubmit() {
			// save new comments
			if (log.isDebugEnabled()) {
				log.debug("new comments: " + AddCommentForm.this.comments);
			}

			Comments newComments = new Comments();
			String userId = WicketSession.get().getUserId();

			newComments.setUserid(userId);
			Asset app = appService.getAsset(appId);
			newComments.setAsset(app);
			newComments.setContent(comments);
			newComments.setCreateDate(new Date());
			newComments.setAssetVersion(app.getCurrentVersion());
			try {
				appService.saveComments(newComments);
			} catch (CensoredWordFoundException e) {
				e.printStackTrace();
				//TODO output message
			}

			comments = null;
		}
	}
}

// $Id$