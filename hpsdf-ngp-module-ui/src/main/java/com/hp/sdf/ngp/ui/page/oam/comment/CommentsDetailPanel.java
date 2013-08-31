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

import org.apache.wicket.PageParameters;
import org.apache.wicket.extensions.breadcrumb.IBreadCrumbModel;
import org.apache.wicket.extensions.breadcrumb.panel.BreadCrumbPanel;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;

import com.hp.sdf.ngp.model.Comments;

public class CommentsDetailPanel extends BreadCrumbPanel {

	private static final long serialVersionUID = -8920226863398352415L;

	public CommentsDetailPanel(String id, IBreadCrumbModel breadCrumbModel, Comments comments) {
		super(id, breadCrumbModel);

		this.add(new CommentsDetailForm("detailForm", comments));
	}

	class CommentsDetailForm extends Form<Void> {

		private static final long serialVersionUID = 589870246031105772L;

		public CommentsDetailForm(String id, Comments comments) {
			super(id);

			Label commentsDetail = new Label("commentsDetail", comments.getContent());
			add(commentsDetail);

			Label commentsTitle = new Label("commentsTitle", comments.getTitle());
			add(commentsTitle);

			Label userId = new Label("userId", comments.getUserid());
			add(userId);

			Button backBtn = new Button("back") {

				private static final long serialVersionUID = 8116120281966867260L;

				public void onSubmit() {
					PageParameters parameters = new PageParameters();
					CommentsSearchPage page = new CommentsSearchPage(parameters);
					setResponsePage(page);
				}
			};
			backBtn.setDefaultFormProcessing(false);
			add(backBtn);
		}
	}

	public String getTitle() {
		return this.getLocalizer().getString("title", this, "Comments Detail");
	}

}
