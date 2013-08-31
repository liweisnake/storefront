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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.Component;
import org.apache.wicket.authorization.strategies.role.metadata.MetaDataRoleAuthorizationStrategy;
import org.apache.wicket.extensions.breadcrumb.IBreadCrumbModel;
import org.apache.wicket.extensions.breadcrumb.panel.BreadCrumbPanel;
import org.apache.wicket.extensions.breadcrumb.panel.BreadCrumbPanelLink;
import org.apache.wicket.extensions.breadcrumb.panel.IBreadCrumbPanelFactory;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.hp.sdf.ngp.common.util.Utils;
import com.hp.sdf.ngp.model.Asset;
import com.hp.sdf.ngp.model.Comments;
import com.hp.sdf.ngp.model.Provider;
import com.hp.sdf.ngp.service.ApplicationService;
import com.hp.sdf.ngp.ui.common.CheckPanel;
import com.hp.sdf.ngp.ui.common.CommentSearchCondition;
import com.hp.sdf.ngp.ui.common.CustomizePagingNavigator;
import com.hp.sdf.ngp.ui.provider.CommentSearchDataProvider;
import com.hp.sdf.ngp.workflow.Privilege;

public class SearchCommentsTablePanel extends Panel {

	private static final long serialVersionUID = 8006956197490194311L;

	private static final Log log = LogFactory.getLog(SearchCommentsTablePanel.class);

	public CommentsView commentsView;

	private Map<Long, String> commentsMap = new HashMap<Long, String>();

	public SearchCommentsTablePanel(String id, CommentSearchCondition commentSearchCondition, IBreadCrumbModel breadCrumbModel) {
		super(id);

		this.add(new FeedbackPanel("feedBack"));

		log.debug("commentSearchCondition :" + commentSearchCondition);
		if (commentSearchCondition != null) {
			log.debug("commentSearchCondition.getKeyword :" + commentSearchCondition.getKeyword());
		}

		add(new CommentsForm("commentsForm", commentSearchCondition, breadCrumbModel));
	}

	class CommentsForm extends Form<Void> {

		private static final long serialVersionUID = 427248307610664062L;

		@SpringBean
		private ApplicationService applicationService;

		private static final int itemsPerPage = 50;

		private boolean groupSelected;

		public boolean isGroupSelected() {
			return groupSelected;
		}

		public void setGroupSelected(boolean groupSelected) {
			this.groupSelected = groupSelected;
		}

		public CommentsForm(String id, CommentSearchCondition commentSearchCondition, IBreadCrumbModel breadCrumbModel) {
			super(id);

			CheckBox groupselector = new CheckBox("groupselector", new PropertyModel<Boolean>(this, "groupSelected"));
			groupselector.setMarkupId("groupselector");
			add(groupselector);

			CommentSearchDataProvider dataProvider = new CommentSearchDataProvider(applicationService, commentSearchCondition);

			commentsView = new CommentsView("comments", dataProvider, itemsPerPage, applicationService, breadCrumbModel);
			add(commentsView);

			add(new CustomizePagingNavigator("navigator", commentsView));

			
			
			final CheckPanel checkPanel = new CheckPanel("checkPanel", getLocalizer().getString("promptTitle", SearchCommentsTablePanel.this), getLocalizer().getString("confirmMsg", SearchCommentsTablePanel.this)) {

				private static final long serialVersionUID = 7465424124110025236L;

				@Override
				public void howDo() {

					if (commentsMap != null && commentsMap.size() > 0) {
						for (Entry<Long, String> entry : commentsMap.entrySet()) {
							log.debug("The Comments need to be deleted: " + entry.getValue());

							if (null == applicationService.getComments(entry.getKey())) {
								log.error("Can not delete the comments because this comments is not exists.");
								error(getLocalizer().getString("msg.error.del.nocomments",  SearchCommentsTablePanel.this ));
								return;
							}
						}

						List<Long> commentIds = new ArrayList<Long>();
						commentIds.addAll(commentsMap.keySet());
						applicationService.batchDeleteComments(commentIds);

					} else {
						error(getLocalizer().getString("msg.error.select.noselect",  SearchCommentsTablePanel.this ));
						return;
					}

					log.debug("Set SelectAll checkbox to not checked.");
					setGroupSelected(false);
					log.debug("Clear the commentsMap.");
					commentsMap.clear();
				}
			};
			add(checkPanel);
			
			Button delete = new Button("delete") {
				private static final long serialVersionUID = 2189543498800077396L;

				public void onSubmit() {checkPanel.show();}
			};
			//delete.add(Tools.addConfirmJs(getLocalizer().getString("confirmMsg", SearchCommentsTablePanel.this)));
			MetaDataRoleAuthorizationStrategy.authorize(delete, Component.RENDER, Privilege.DELETECOMMENTS);
			add(delete);
		}

	}

	class CommentsView extends DataView<Comments> {

		private static final int COMMENTS_DISPLAY_LENGTH = 30;

		private static final long serialVersionUID = 2738548802166595044L;

		private IBreadCrumbModel breadCrumbModel;

		private ApplicationService applicationService;

		public CommentsView(String id, IDataProvider<Comments> dataProvider, int itemsPerPage, ApplicationService applicationService, IBreadCrumbModel breadCrumbModel) {
			super(id, dataProvider, itemsPerPage);
			this.applicationService = applicationService;
			this.breadCrumbModel = breadCrumbModel;
		}

		public void updateModel(CommentSearchCondition commentSearchCondition) {
			CommentSearchDataProvider dataProvider = (CommentSearchDataProvider) this.getDataProvider();
			dataProvider.setCommentSearchCondition(commentSearchCondition);
		}

		protected void populateItem(Item<Comments> item) {

			final Comments comments = item.getModelObject();

			item.add(new CheckBox("select", new IModel<Boolean>() {

				private static final long serialVersionUID = 7865238665776348241L;

				public Boolean getObject() {

					if (commentsMap != null && commentsMap.size() != 0) {
						String o = commentsMap.get(comments.getId());
						return null == o ? false : true;
					}

					return false;
				}

				public void setObject(Boolean object) {
					if (object) {
						commentsMap.put(comments.getId(), comments.getContent());
					}
				}

				public void detach() {
				}
			}));

			Asset asset = this.applicationService.getAsset(comments.getAsset().getId());
			Provider assetProvider = null;
			if (asset.getAssetProvider() != null) {
				assetProvider = this.applicationService.getAssetProviderById(asset.getAssetProvider().getId());
			}

			item.add(new Label("providerID", assetProvider != null ? (assetProvider.getId() + "") : ""));

			item.add(new Label("providerName", assetProvider != null ? assetProvider.getName() : ""));

			item.add(new Label("assetId", asset.getId() + ""));

			item.add(new Label("assetName", asset.getName()));

			item.add(new Label("version", comments.getAssetVersion()));

			StringBuilder titleIntro = new StringBuilder();
			String title = comments.getTitle();
			if (StringUtils.isNotEmpty(title)) {
				if (title.length() > COMMENTS_DISPLAY_LENGTH) {
					titleIntro.append(title.substring(0, COMMENTS_DISPLAY_LENGTH));
					titleIntro.append(".......");
				} else {
					titleIntro.append(title);
				}
			}
			item.add(new Label("commentTitle", titleIntro.toString()));

			StringBuilder contentIntro = new StringBuilder();
			String content = comments.getContent();
			if (StringUtils.isNotEmpty(content)) {
				if (content.length() > COMMENTS_DISPLAY_LENGTH) {
					contentIntro.append(content.substring(0, COMMENTS_DISPLAY_LENGTH));
					contentIntro.append(".......");
				} else {
					contentIntro.append(content);
				}
			}

			item.add(new Label("comment", contentIntro.toString()));

			item.add(new Label("updateTime", Utils.dateToLongString(comments.getUpdateDate())));

			BreadCrumbPanelLink detail = new BreadCrumbPanelLink("detail", breadCrumbModel, new IBreadCrumbPanelFactory() {

				private static final long serialVersionUID = 1L;

				public BreadCrumbPanel create(String componentId, IBreadCrumbModel breadCrumbModel) {
					return new CommentsDetailPanel(componentId, breadCrumbModel, comments);
				}
			});
			item.add(detail);
		}

	}

}
