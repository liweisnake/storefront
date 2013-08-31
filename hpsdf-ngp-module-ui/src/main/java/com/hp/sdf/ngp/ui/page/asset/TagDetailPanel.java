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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.Component;
import org.apache.wicket.authorization.strategies.role.Roles;
import org.apache.wicket.authorization.strategies.role.metadata.MetaDataRoleAuthorizationStrategy;
import org.apache.wicket.feedback.ContainerFeedbackMessageFilter;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.target.basic.RedirectRequestTarget;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.ValidationError;
import org.apache.wicket.validation.validator.MaximumValidator;
import org.apache.wicket.validation.validator.StringValidator;

import com.hp.sdf.ngp.model.Tag;
import com.hp.sdf.ngp.service.ApplicationService;
import com.hp.sdf.ngp.ui.WicketSession;
import com.hp.sdf.ngp.ui.common.SearchMetaInfo;
import com.hp.sdf.ngp.workflow.AccessType;
import com.hp.sdf.ngp.workflow.AssetLifeCycleEngine;

/**
 * 
 *
 */
public class TagDetailPanel extends Panel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7934202304139731318L;

	private static final Log log = LogFactory.getLog(TagDetailPanel.class);

	public final static int MAX_APP_TAG_COUNT = 10;

	public final static String GALLERY_PAGE = "/portal/portal/storefront/Gallery";

	public final static String GALLERY_AUTH_PAGE = "/portal/auth/portal/storefront/Gallery";

	@SpringBean
	private ApplicationService applicationService;

	@SpringBean
	AssetLifeCycleEngine assetLifeCycleEngine;

	private long appId;

	private String newTags;

	private List<Tag> tagList;

	public String getNewTags() {
		return newTags;
	}

	public void setNewtags(String newtags) {
		this.newTags = newtags;
	}

	@SuppressWarnings("unchecked")
	public TagDetailPanel(String id, Long appId) {
		super(id);
		this.appId = appId;
		tagList = applicationService.getAllTagsByAsset(appId, null,0, Integer.MAX_VALUE);
		add(new TagListView("tags", new PropertyModel(this, "tagList")));

		AddTagForm form = new AddTagForm("addTagsForm", applicationService);
		add(form);
		// apply privilege
		String[] privileges = assetLifeCycleEngine.getAccessPrivilege(appId, AccessType.TAG);
		Roles roles = new Roles(privileges);
		MetaDataRoleAuthorizationStrategy.authorize(form, Component.RENDER, roles.toString());
		log.debug("tagInputForm.isRenderAllowed=" + form.isRenderAllowed());
	}

	private void refreshTagsModel() {
		tagList = applicationService.getAllTagsByAsset(appId, null,0, Integer.MAX_VALUE);
	}

	class TagListView extends ListView<Tag> {

		public TagListView(final String id, List<? extends Tag> list) {
			super(id, list);
		}

		public TagListView(final String id, final IModel<? extends List<? extends Tag>> model) {
			super(id, model);
		}

		private static final long serialVersionUID = 6121812891935022925L;

		@Override
		protected void populateItem(ListItem<Tag> item) {
			final Tag tag = (Tag) item.getModelObject();
			// add name
			Link tagLink = new Link("tagLink") {

				@Override
				public void onClick() {
					// Searches app by tag
					WicketSession.get().setObject(WicketSession.AttributeName.KEYWORD.name(), null);
					WicketSession.get().setObject(WicketSession.AttributeName.ORDERBY.name(), null);
					WicketSession.get().setObject(WicketSession.AttributeName.ORDERENUM.name(), null);
					// Refresh the search conditions
					List<SearchMetaInfo> searchMetaInfoList = new ArrayList<SearchMetaInfo>();
					SearchMetaInfo tagInfo = new SearchMetaInfo(SearchMetaInfo.SearchByType.ASSETTAGRELATION_TAG_ID, tag.getId().toString(), tag.getName());
					searchMetaInfoList.add(tagInfo);

					WicketSession.get().setObject(WicketSession.AttributeName.SEARCHMETAINFO.name(), (Serializable) searchMetaInfoList, true);

					setResponsePage(AppListPage.class);

					String galleryPage;
					if (WicketSession.get().getUserId() != null) {
						galleryPage = GALLERY_PAGE;
					} else {
						galleryPage = GALLERY_AUTH_PAGE;
					}
					this.getRequestCycle().setRequestTarget(new RedirectRequestTarget(galleryPage));
				}
			};
			tagLink.add(new Label("tagName", tag.getName()));
			item.add(tagLink);

			Link delLink = new Link("delLink") {
				@Override
				public void onClick() {
					// only remove app-tag relationship
					if (log.isDebugEnabled()) {
						log.debug("del tag[id=" + tag.getId() + ",name=" + tag.getName() + "] for app[id=" + TagDetailPanel.this.appId + "]");
					}

					applicationService.deleteTag(null,tag.getId(), null);

					refreshTagsModel();
				}
			};
			item.add(delLink);
			// apply privilege
			String[] privileges = assetLifeCycleEngine.getAccessPrivilege(appId, AccessType.TAG);
			Roles roles = new Roles(privileges);
			MetaDataRoleAuthorizationStrategy.authorize(delLink, Component.RENDER, roles.toString());
			log.debug("tag delLink.isRenderAllowed=" + delLink.isRenderAllowed());
		}

	};

	/**
	 * 
	 * 
	 *
	 */
	class AddTagForm extends Form {

		/**
		 * 
		 */
		private static final long serialVersionUID = -3384988136851861345L;

		private ApplicationService applicationService;

		final FeedbackPanel feedback = new FeedbackPanel("tagFeedback", new ContainerFeedbackMessageFilter(this));

		public AddTagForm(String id, ApplicationService applicationService) {
			super(id);
			this.applicationService = applicationService;
			TextField<String> newTagsField = new TextField<String>("newTags", new PropertyModel<String>(TagDetailPanel.this, "newTags"));
			newTagsField.setRequired(true);
			newTagsField.add(new StringValidator.MaximumLengthValidator(100));
			newTagsField.add(new MaximumValidator<String>("") {
				public void validate(IValidatable<String> validatable) {
					String value = validatable.getValue();

					if (value != null && value.trim().length() > 0) {
						// check max tag counts
						List oldTags = AddTagForm.this.applicationService.getAllTagsByAsset(appId, null,0, Integer.MAX_VALUE);
						ArrayList<String> tags = AddTagForm.this.getTagsArray(value);
						int tagCount = 0;
						if (oldTags != null) {
							tagCount = tags.size() + oldTags.size();
						} else {
							tagCount = tags.size();
						}
						if (tagCount > MAX_APP_TAG_COUNT) {
							ValidationError error = new ValidationError();
							error.addMessageKey("tag.tooMany");
							validatable.error(error);
						}
					}
				}
			});

			add(newTagsField);

			add(feedback);
		}

		@Override
		protected void onSubmit() {
			log.debug("new tags: " + TagDetailPanel.this.newTags);
			ArrayList<String> tags = getTagsArray(TagDetailPanel.this.newTags);
			for (String tagstr : tags) {
				applicationService.saveTag(appId, new Tag(null, tagstr));
			}

			// refresh the tagList model
			refreshTagsModel();

			// clean input box
			TagDetailPanel.this.newTags = null;
		}

		private ArrayList<String> getTagsArray(String input) {
			ArrayList<String> tagArray = new ArrayList<String>();
			if (input != null) {
				String[] buffer = input.split(",");
				for (String temp : buffer) {
					if (temp.length() > 0)
						tagArray.add(temp);
				}
			}
			return tagArray;
		}

	}

}

// $Id$