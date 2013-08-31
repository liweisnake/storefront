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

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.axis.utils.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.authorization.strategies.role.Roles;
import org.apache.wicket.authorization.strategies.role.metadata.MetaDataRoleAuthorizationStrategy;
import org.apache.wicket.extensions.breadcrumb.IBreadCrumbModel;
import org.apache.wicket.extensions.breadcrumb.panel.BreadCrumbPanel;
import org.apache.wicket.extensions.breadcrumb.panel.BreadCrumbPanelLink;
import org.apache.wicket.extensions.breadcrumb.panel.IBreadCrumbPanelFactory;
import org.apache.wicket.extensions.rating.RatingPanel;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.ListMultipleChoice;
import org.apache.wicket.markup.html.image.ContextImage;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.hp.sdf.ngp.common.util.Utils;
import com.hp.sdf.ngp.model.Asset;
import com.hp.sdf.ngp.model.Category;
import com.hp.sdf.ngp.service.ApplicationService;
import com.hp.sdf.ngp.ui.WicketSession;
import com.hp.sdf.ngp.ui.common.LocalImageUri;
import com.hp.sdf.ngp.ui.common.Tools;
import com.hp.sdf.ngp.ui.page.rating.CustomizedRatingPanel;
import com.hp.sdf.ngp.workflow.AccessType;
import com.hp.sdf.ngp.workflow.AssetLifeCycleEngine;
import com.hp.sdf.ngp.workflow.Privilege;

public class MyAppDetailPanel extends BreadCrumbPanel {

	private final static Log log = LogFactory.getLog(MyAppDetailPanel.class);

	@SpringBean
	private ApplicationService applicationService;

	@SpringBean
	AssetLifeCycleEngine assetLifeCycleEngine;

	private BreadCrumbPanel caller;

	// private Category category;
	private List<Category> categoryList = new ArrayList<Category>();
	private List<Category> categoryListDel = new ArrayList<Category>();

	private final Long appId;

	private Label myRankLabel;

	public MyAppDetailPanel(String id, IBreadCrumbModel breadCrumbModel, Long appId, BreadCrumbPanel caller) {
		super(id, breadCrumbModel);
		this.appId = appId;
		this.caller = caller;

		this.populate(appId);
	}

	private void populate(Long appId) {
		final Asset app = applicationService.getAsset(appId);
		// add image
		String thumbnail = app.getThumbnailLocation();
		log.debug("thumbnail location: " + thumbnail);
		if (StringUtils.isEmpty(thumbnail)) {
			add(new ContextImage("thumbnail", "images/null_thumbnail.jpg"));
		} else {
			add(new LocalImageUri("thumbnail", thumbnail));
		}

		// add name
		add(new Label("name", app.getName()));

		// adds auther
		String auther = app.getAuthorid();
		add(new Label("auther", auther));

		// add update date
		add(new Label("updatetime", Utils.dateToShortString(app.getUpdateDate())));

		String[] privileges = assetLifeCycleEngine.getAccessPrivilege(appId, AccessType.CATEGORY);
		Roles roles = new Roles(privileges);
		Form<Void> categoryForm = new Form<Void>("categoryForm");
		categoryForm.setMarkupId("categoryForm");
		add(categoryForm);
		MetaDataRoleAuthorizationStrategy.authorize(categoryForm, Component.RENDER, roles.toString());
		MetaDataRoleAuthorizationStrategy.authorize(categoryForm, Component.ENABLE, roles.toString());
		log.debug("categoryChoice.isEnableAllowed=" + categoryForm.isEnableAllowed());
		boolean labelVisible = !categoryForm.isEnableAllowed();

		// adds category
		categoryList = applicationService.getAllCategoryByAssetId(appId, 0, Integer.MAX_VALUE);
		categoryListDel = applicationService.getAllCategoryByAssetId(appId, 0, Integer.MAX_VALUE);
		Label catname = new Label("catext", Tools.getCategoryNameFromList(categoryList));
		catname.setVisibilityAllowed(labelVisible);
		add(catname);

		List<Category> categories = applicationService.getAllCategory(0, Integer.MAX_VALUE);
		ChoiceRenderer<List<Category>> cagetoryRenderer = new ChoiceRenderer<List<Category>>("name", "id");
		ListMultipleChoice<List<Category>> categoryChoice = new ListMultipleChoice("category", new PropertyModel<List<Category>>(this, "categoryList"), categories, cagetoryRenderer);
		categoryChoice.setRequired(true);
		categoryForm.add(categoryChoice);
		// apply category privilege
		log.debug(roles.toString());
		MetaDataRoleAuthorizationStrategy.authorize(categoryChoice, Component.RENDER, roles.toString());

		// add category form buttons
		Button updateLink = new Button("updateLink") {
			private static final long serialVersionUID = 6270538959294109060L;

			@Override
			public void onSubmit() {
				if (categoryList != null && categoryList.size() > 0) {
					Asset app = applicationService.getAsset(MyAppDetailPanel.this.appId);
					applicationService.disassociateCategory(app.getId());

					Set<Long> categoryIdSet = Tools.getCategoryIdSet(categoryList);
					applicationService.associateCategory(app.getId(), null, categoryIdSet);

					// AssetCategoryRelation relation = new
					// AssetCategoryRelation(category, app);
					// Set<AssetCategoryRelation> relations = new
					// HashSet<AssetCategoryRelation>();
					// relations.add(relation);
					// app.setAssetCategoryRelations(relations);
					// applicationService.updateAsset(app);

					// List<Category> appCategories =
					// applicationService.getAllCategoryByAssetId(MyAppDetailPanel.this.appId,
					// 0, Integer.MAX_VALUE);
					// if (appCategories != null && appCategories.size() > 0) {
					// MyAppDetailPanel.this.category = appCategories.get(0);
					// }
				}
			}
		};
		categoryForm.add(updateLink);
		// Label updateLabel = new Label("catsubmit",
		// this.getLocalizer().getString("btn.update", this));
		// updateLink.add(updateLabel);

		// Link<Void> resetLink = new Link<Void>("resetLink") {
		// private static final long serialVersionUID = -796131625126670514L;
		//
		// @Override
		// public void onClick() {
		// }
		// };
		// categoryForm.add(resetLink);
		// Label resetLabel = new Label("catreset",
		// this.getLocalizer().getString("btn.reset", this));
		// resetLink.add(resetLabel);

		// add brief
		add(new Label("brief", app.getBrief()));

		// add tags list view
		TagDetailPanel tags = new TagDetailPanel("tagDetailPanel", appId);
		add(tags);

		// add rating panel, default 5 stars
		RatingPanel ratingPanel = new CustomizedRatingPanel("rating", null, 5, false) {
			private static final long serialVersionUID = -1011276676677817249L;

			protected boolean onIsStarActive(int star) {
				if (app.getAverageUserRating() == null || app.getAverageUserRating() == 0) {
					return false;
				}
				return (app.getAverageUserRating() - 0.5D) >= star;
			}

			protected void onRated(int rating, AjaxRequestTarget target) {
				log.debug("my rating=" + rating);
				// rating in panel is from 1-5
				// rating in backend database is from 0-4
				String userId = WicketSession.get().getUserId();
				Double avgRank = applicationService.saveOrUpdateAssetRating(MyAppDetailPanel.this.appId, userId, new Double(rating));
				log.debug("avg rating=" + avgRank.doubleValue());
				app.setAverageUserRating(avgRank);

				// update average rating
				MyAppDetailPanel.this.remove("avgrating");
				if (avgRank == null || avgRank.isNaN()) {
					avgRank = 0.0D;
				}
				DecimalFormat ratingFormat = new DecimalFormat("#0.0");
				Label avgRatingLabel = new Label("avgrating", ratingFormat.format(avgRank));
				MyAppDetailPanel.this.add(avgRatingLabel);

				// update my rating
				MyAppDetailPanel.this.remove("myrating");
				myRankLabel = new Label("myrating", Integer.toString(rating));
				MyAppDetailPanel.this.add(myRankLabel);
			}
		}.setRatingLabelVisible(false);
		add(ratingPanel);
		// apply rating privilege
		String[] ratingPrivileges = assetLifeCycleEngine.getAccessPrivilege(appId, AccessType.RATING);
		Roles ratingRoles = new Roles(ratingPrivileges);
		MetaDataRoleAuthorizationStrategy.authorize(ratingPanel, Component.ENABLE, ratingRoles.toString());
		log.debug("ratingPanel.isEnableAllowed=" + ratingPanel.isEnableAllowed());

		// add digital rating view
		Double avgRating = app.getAverageUserRating();
		if (avgRating == null || avgRating.isNaN()) {
			avgRating = 0.0D;
		}
		
		/**
		DecimalFormat ratingFormat = new DecimalFormat("#0.0");
		Label avgRatingLabel = new Label("avgrating", ratingFormat.format(avgRating));
		add(avgRatingLabel);
		**/

		// add preview image
		// add image
		String preview = app.getThumbnailBigLocation();
		if (StringUtils.isEmpty(preview)) {
			add(new ContextImage("preview", "images/appPreview.jpg"));
		} else {
			add(new LocalImageUri("preview", preview));
		}

		// adds download panel
		AppDownloadPanel downloadPanel = new AppDownloadPanel("downloadPanel", appId);
		downloadPanel.setVisibilityAllowed(downloadPanel.getBinaryCount() > 0);
		add(downloadPanel);

		// add link to binary control panel
		BreadCrumbPanelLink binControlLink = new BreadCrumbPanelLink("binControlLink", this.getBreadCrumbModel(), new IBreadCrumbPanelFactory() {
			private static final long serialVersionUID = -3554621309637784933L;

			public BreadCrumbPanel create(String componentId, IBreadCrumbModel breadCrumbModel) {
				return new BinaryControlPanel(componentId, breadCrumbModel, app.getId());
			}
		});
		add(binControlLink);
		// control bin privilege
		String[] binAuth = assetLifeCycleEngine.getAccessPrivilege(appId, AccessType.MNG_BIN);
		Roles binRoles = new Roles(binAuth);
		MetaDataRoleAuthorizationStrategy.authorize(binControlLink, Component.RENDER, binRoles.toString());

		// add app control panel
		AppControlPanel appControlPanel = new AppControlPanel("appControlPanel", app, this, caller);
		add(appControlPanel);
		// adds privileges
		if (!WicketSession.get().getRoles().hasRole(Privilege.MANAGEAPP)) {
			appControlPanel.setVisibilityAllowed(false);
		}

		// add app description
		add(new Label("description", app.getDescription()));

		// adds comments
		CommentPanel comments = new CommentPanel("commentsPanel", appId);
		add(comments);
	}

	private static final long serialVersionUID = 7467406950181254876L;

	public String getTitle() {

		return this.getLocalizer().getString("title", this, "Application Details");
	}

	// public class CategoryForm extends Form<Void> {
	//
	// private static final long serialVersionUID = -8136271863464056278L;
	//
	// public CategoryForm(String id) {
	// super(id);
	// }
	// }
}
