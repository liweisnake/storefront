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
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.hp.sdf.ngp.model.Category;
import com.hp.sdf.ngp.model.Platform;
import com.hp.sdf.ngp.model.Status;
import com.hp.sdf.ngp.service.ApplicationService;
import com.hp.sdf.ngp.ui.WicketSession;
import com.hp.sdf.ngp.ui.common.SearchMetaInfo;
import com.hp.sdf.ngp.workflow.AssetBinaryVersionLifeCycleEngine;
import com.hp.sdf.ngp.workflow.AssetLifeCycleEngine;
import com.hp.sdf.ngp.workflow.Privilege;

@SuppressWarnings( { "unchecked" })
public class AppSearchPanel extends Panel {

	private static final long serialVersionUID = -4918503633298438L;

	private static final Log log = LogFactory.getLog(AppSearchPanel.class);

	@SpringBean
	private ApplicationService applicationService;

	@SpringBean
	private AssetLifeCycleEngine assetLifeCycleEngine;

	@SpringBean
	private AssetBinaryVersionLifeCycleEngine assetBinaryVersionLifeCycleEngine;

	public AppSearchPanel(String id) {
		super(id);
		AppSearchForm appSearchForm = new AppSearchForm("appSearchForm", applicationService);
		add(appSearchForm);

		// Create feedback panel and add to page
		final FeedbackPanel feedback = new FeedbackPanel("feedback");
		add(feedback);
	}

	public String getTitle() {
		return this.getLocalizer().getString("title", this, "Search Application");
	}

	public class AppSearchForm extends Form {

		private static final long serialVersionUID = -6578579305600140145L;

		private ApplicationService applicationService;

		private static final int maxSearchCount = 100;

		private String keyword;

		private Category category;

		private Platform platform;

		private Status status;

		public String getKeyword() {
			return keyword;
		}

		public void setKeyword(String keyword) {
			this.keyword = keyword;
		}

		public Category getCategory() {
			return category;
		}

		public void setCategory(Category category) {
			this.category = category;
		}

		public Platform getPlatform() {
			return platform;
		}

		public void setPlatform(Platform platform) {
			this.platform = platform;
		}

		public Status getStatus() {
			return status;
		}

		public void setStatus(Status status) {
			this.status = status;
		}

		public AppSearchForm(String id, ApplicationService applicationService) {

			super(id);
			this.applicationService = applicationService;

			TextField<String> keywordField = new TextField<String>("keyword", new PropertyModel<String>(this, "keyword"));
			add(keywordField);

			// adds category
			List<Category> categories = applicationService.getAllCategory(0, Integer.MAX_VALUE);
			ChoiceRenderer<Category> cagetoryRenderer = new ChoiceRenderer<Category>("name", "id");
			DropDownChoice categoryChoice = new DropDownChoice<Category>("category", new PropertyModel(this, "category"), categories, cagetoryRenderer);
			add(categoryChoice);

			// adds platform
			List<Platform> platforms = applicationService.getAllPlatform(0, Integer.MAX_VALUE);
			ChoiceRenderer<Platform> choiceRenderer = new ChoiceRenderer<Platform>("name", "id");
			DropDownChoice platformChoice = new DropDownChoice<Platform>("platform", new PropertyModel(this, "platform"), platforms, choiceRenderer);
			add(platformChoice);

			// adds binary-status condition
			Label binstatusLabel = new Label("label.bin.status", this.getLocalizer().getString("binstatus", AppSearchPanel.this));
			add(binstatusLabel);
			List<Status> statuses = assetBinaryVersionLifeCycleEngine.getDefinedStatus();
			ChoiceRenderer<Status> statusRenderer = new ChoiceRenderer<Status>("status", "id");
			DropDownChoice statusChoice = new DropDownChoice<Status>("binstatus", new PropertyModel(this, "status"), statuses, statusRenderer);
			add(statusChoice);
			// adds privileges
			if (!WicketSession.get().getRoles().hasRole(Privilege.SEARCHBINSTATUS)) {
				statusChoice.setVisibilityAllowed(false);
			}
		}

		public final void onSubmit() {

			if (log.isDebugEnabled()) {
				log.debug("app search criteria:");
				log.debug("app keyword : " + keyword);
				log.debug("app category ID : " + (category == null ? null : category.getId()));
				log.debug("app platform ID : " + (platform == null ? null : platform.getId()));
				log.debug("app-bin status ID : " + (status == null ? null : status.getId()));
			}

			// Refresh the search conditions
			List<SearchMetaInfo> searchMetaInfoList = new ArrayList<SearchMetaInfo>();
			if (category != null) {
				SearchMetaInfo categoryInfo = new SearchMetaInfo(SearchMetaInfo.SearchByType.ASSETCATEGORYRELATION_CATEGORY_ID, category.getId().toString(), category.getName());
				searchMetaInfoList.add(categoryInfo);
			}
			if (platform != null) {
				SearchMetaInfo platformInfo = new SearchMetaInfo(SearchMetaInfo.SearchByType.PLATFORM_ID, platform.getId().toString(), platform.getName());
				searchMetaInfoList.add(platformInfo);
			}
			if (status != null) {
				SearchMetaInfo statusInfo = new SearchMetaInfo(SearchMetaInfo.SearchByType.STATUS_ID, status.getId().toString(), status.getStatus());
				searchMetaInfoList.add(statusInfo);
			}

			WicketSession.get().setObject(WicketSession.AttributeName.KEYWORD.name(), keyword, true);

			if (searchMetaInfoList.size() > 0) {
				WicketSession.get().setObject(WicketSession.AttributeName.SEARCHMETAINFO.name(), (Serializable) searchMetaInfoList, true);
			} else {
				WicketSession.get().setObject(WicketSession.AttributeName.SEARCHMETAINFO.name(), null, true);
			}

			WicketSession.get().setObject(WicketSession.AttributeName.ORDERBY.name(), null, true);
			WicketSession.get().setObject(WicketSession.AttributeName.ORDERENUM.name(), null, true);

			this.keyword = null;
			this.category = null;
			this.platform = null;
		}
	}
}

// $Id$