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
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.axis.utils.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.authorization.strategies.role.Roles;
import org.apache.wicket.authorization.strategies.role.metadata.MetaDataRoleAuthorizationStrategy;
import org.apache.wicket.extensions.breadcrumb.IBreadCrumbModel;
import org.apache.wicket.extensions.breadcrumb.panel.BreadCrumbPanel;
import org.apache.wicket.extensions.rating.RatingPanel;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.image.ContextImage;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.target.basic.RedirectRequestTarget;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.hp.sdf.ngp.common.constant.AssetConstants;
import com.hp.sdf.ngp.common.util.Utils;
import com.hp.sdf.ngp.model.Asset;
import com.hp.sdf.ngp.model.AssetPrice;
import com.hp.sdf.ngp.model.AssetRating;
import com.hp.sdf.ngp.model.Category;
import com.hp.sdf.ngp.model.Platform;
import com.hp.sdf.ngp.model.Tag;
import com.hp.sdf.ngp.service.ApplicationService;
import com.hp.sdf.ngp.ui.WicketSession;
import com.hp.sdf.ngp.ui.common.LocalImageUri;
import com.hp.sdf.ngp.ui.common.SearchMetaInfo;
import com.hp.sdf.ngp.ui.common.Tools;
import com.hp.sdf.ngp.ui.page.rating.CustomizedRatingPanel;
import com.hp.sdf.ngp.workflow.AccessType;
import com.hp.sdf.ngp.workflow.AssetLifeCycleEngine;

public class AppDetailPanel extends BreadCrumbPanel {

	private final static Log log = LogFactory.getLog(AppDetailPanel.class);

	@SpringBean
	private ApplicationService applicationService;

	@SpringBean
	AssetLifeCycleEngine assetLifeCycleEngine;

	@SpringBean
	private AssetConstants assetConstants;

	private final String PRICE_EMPTY = getLocalizer().getString("bin.noprice", this);
	private final String PRICE_FREE = getLocalizer().getString("bin.free", this);

	public final static String GALLERY_PAGE = "/portal/portal/storefront/Gallery";
	public final static String GALLERY_AUTH_PAGE = "/portal/auth/portal/storefront/Gallery";

	private BreadCrumbPanel caller;

	private Category category;

	private final Long appId;

	private List tagList;

	public AppDetailPanel(String id, IBreadCrumbModel breadCrumbModel, Long appId, BreadCrumbPanel caller) {
		super(id, breadCrumbModel);
		this.appId = appId;
		this.caller = caller;

		this.populate(appId);
	}

	@SuppressWarnings("unchecked")
	private void populate(final Long appId) {
		final Asset app = applicationService.getAsset(appId);

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

		// adds category
		List<Category> appCategories = applicationService.getAllCategoryByAssetId(appId, 0, Integer.MAX_VALUE);
		if (appCategories != null && appCategories.size() > 0) {
			category = appCategories.get(0);
		} else {
			category = new Category();
		}
		Label catname = new Label("catext", category.getName());
		add(catname);

		// add brief
		add(new Label("brief", app.getBrief()));

		// add tags list view
		List tagList = applicationService.getAllTagsByAsset(appId, null,0, Integer.MAX_VALUE);
		add(new TagListView("tags", new PropertyModel(this, "tagList")));

		// add rating panel, default 5 stars
		RatingPanel myratingPanel = new CustomizedRatingPanel("myrating", null, 5, false) {
			private static final long serialVersionUID = -1011276676677817249L;

			protected boolean onIsStarActive(int star) {
				String userId = WicketSession.get().getUserId();
				if (userId != null) {
					AssetRating myrating = applicationService.getAssetRating(userId, appId);
					if (myrating != null) {
						return (myrating.getRating() - 0.5D) >= star;
					}
				}

				return false;
			}

			protected void onRated(int rating, AjaxRequestTarget target) {
				log.debug("my rating=" + rating);
				// rating in panel is from 1-5
				// rating in backend database is from 1-5 (change by lusong)
				String userId = WicketSession.get().getUserId();
				Double avgRank = applicationService.saveOrUpdateAssetRating(AppDetailPanel.this.appId, userId, new Double(rating));
				app.setAverageUserRating(avgRank);
				log.debug("avg rating=" + avgRank.doubleValue());

				// update average rating
				/*
				 * AppDetailPanel.this.remove("avgrating"); if (avgRank == null
				 * || avgRank.isNaN()) { avgRank = 0.0D; } DecimalFormat
				 * ratingFormat = new DecimalFormat("#0.0"); Label
				 * avgRatingLabel = new Label("avgrating",
				 * ratingFormat.format(avgRank));
				 * AppDetailPanel.this.add(avgRatingLabel);
				 */
			}
		}.setRatingLabelVisible(false);

		// apply rating privilege
		String[] ratingPrivileges = assetLifeCycleEngine.getAccessPrivilege(appId, AccessType.RATING);
		Roles ratingRoles = new Roles(ratingPrivileges);
		MetaDataRoleAuthorizationStrategy.authorize(myratingPanel, Component.ENABLE, ratingRoles.toString());
		log.debug("ratingPanel.isEnableAllowed=" + myratingPanel.isEnableAllowed());

		if (!myratingPanel.isEnabled()) {
			myratingPanel.setVisible(false);
		}
		add(myratingPanel);

		// average rating display
		RatingPanel ratingPanel = new CustomizedRatingPanel("rating", null, 5, false) {
			private static final long serialVersionUID = -1011276676677817249L;

			protected boolean onIsStarActive(int star) {
				// star is from 0 to 4
				if (app.getAverageUserRating() == null || app.getAverageUserRating() == 0) {
					return false;
				}

				return (app.getAverageUserRating() - 0.5D) >= star;
			}

			protected void onRated(int rating, AjaxRequestTarget target) {
			}
		}.setRatingLabelVisible(false);
		ratingPanel.setEnabled(false);
		add(ratingPanel);

		// add digital rating view
		/*
		 * Double avgRating = app.getAverageUserRating(); if (avgRating == null
		 * || avgRating.isNaN()) { avgRating = 0.0D; } DecimalFormat
		 * ratingFormat = new DecimalFormat("#0.0"); Label avgRatingLabel = new
		 * Label("avgrating", ratingFormat.format(avgRating));
		 * add(avgRatingLabel);
		 */

		// adds version
		Label version = new Label("version", app.getCurrentVersion());
		add(version);

		// adds price
		String price = assetConstants.getUnit();
		// BigDecimal priceValue = app.getAssetPrice();
		List<AssetPrice> assetPriceList = applicationService.getAssetPriceByAssetId(app.getId());
		BigDecimal priceValue = Tools.getAssetPriceFromListDollars(assetPriceList);

		if (priceValue == null || priceValue.floatValue() <= 0) {
			price = PRICE_FREE;
		} else {
			price += AssetConstants.priceFormat.format(priceValue.doubleValue());
		}
		add(new Label("price", price));

		// adds platform
		// Label platform = new Label("platform", app.getPlatform().getName());
		List<Platform> platformList = applicationService.getPlatformByAssetId(app.getId());
		Label platform = new Label("platform", Tools.getPlatfromNameFromList(platformList));
		add(platform);

		// add preview image
		String preview = app.getThumbnailBigLocation();
		log.debug("asset preview pic location: " + preview);
		if (StringUtils.isEmpty(preview)) {
			add(new ContextImage("preview", "images/appPreview.jpg"));
		} else {
			add(new LocalImageUri("preview", preview));
		}

		// adds download panel
		AppSubscriptionPanel subscriptionPanel = new AppSubscriptionPanel("subscriptionPanel", app, this.getBreadCrumbModel());
		add(subscriptionPanel);

		// add app description
		add(new Label("description", app.getDescription()));

		// adds comments
		CommentPanel comments = new CommentPanel("commentsPanel", appId);
		add(comments);
	}

	private static final long serialVersionUID = 7467406950181254876L;

	class TagListView extends ListView<Tag> {

		public TagListView(final String id, List<? extends Tag> list) {
			super(id, list);
		}

		public TagListView(final String id, final IModel<? extends List<? extends Tag>> model) {
			super(id, model);
		}

		private static final long serialVersionUID = 6121812891935022925L;

		@SuppressWarnings("unchecked")
		protected void populateItem(ListItem<Tag> item) {
			final Tag tag = (Tag) item.getModelObject();
			// add name
			Link tagLink = new Link("tagLink") {

				private static final long serialVersionUID = -1030901440473624433L;

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
		}

	};

	public String getTitle() {

		return this.getLocalizer().getString("title", this, "Application Details");
	}
}
