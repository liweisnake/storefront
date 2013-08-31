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

import java.util.List;

import org.apache.axis.utils.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.breadcrumb.IBreadCrumbModel;
import org.apache.wicket.extensions.breadcrumb.panel.BreadCrumbPanel;
import org.apache.wicket.extensions.breadcrumb.panel.BreadCrumbPanelLink;
import org.apache.wicket.extensions.breadcrumb.panel.IBreadCrumbPanelFactory;
import org.apache.wicket.extensions.rating.RatingPanel;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.image.ContextImage;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.hp.sdf.ngp.common.util.Utils;
import com.hp.sdf.ngp.model.Asset;
import com.hp.sdf.ngp.model.Tag;
import com.hp.sdf.ngp.service.ApplicationService;
import com.hp.sdf.ngp.ui.common.LocalImageUri;

public class AppTilePanel extends Panel {

	private final static Log log = LogFactory.getLog(AppTilePanel.class);

	private Asset app;

	private AppTileListPanel parentPanel;

	@SpringBean
	private ApplicationService applicationService;

	public AppTilePanel(String id, IModel<Asset> model, AppTileListPanel parentPanel) {
		super(id, model);
		this.app = model.getObject();
		this.parentPanel = parentPanel;

		populate();
	}

	private static final long serialVersionUID = 1725113117191771539L;

	private void populate() {
		// add image
		String thumbnail = app.getThumbnailLocation();

		if (StringUtils.isEmpty(thumbnail)) {
			add(new ContextImage("image", "images/null_thumbnail.jpg"));
		} else {
			add(new LocalImageUri("image", thumbnail));
		}
		// add name
		BreadCrumbPanelLink nameLink = new BreadCrumbPanelLink("nameLink", parentPanel.getBreadCrumbModel(), new IBreadCrumbPanelFactory() {
			public BreadCrumbPanel create(String componentId, IBreadCrumbModel breadCrumbModel) {
				return new AppDetailPanel(componentId, breadCrumbModel, app.getId(), parentPanel);
			}
		});

		nameLink.add(new Label("name", app.getName()));
		add(nameLink);

		// add date time
		add(new Label("datetime", Utils.dateToShortString(app.getCreateDate())));

		// add tags list view
		add(new TagListView("tags", applicationService.getAllTagsByAsset(app.getId(), null,0, Integer.MAX_VALUE)));

		// add rating panel, default 5 stars
		RatingPanel ratingPanel = new RatingPanel("rating", null, new Model<Integer>(5), null, new Model<Boolean>(Boolean.TRUE), false) {
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

		add(ratingPanel);

	}

	class TagListView extends ListView<Tag> {

		public TagListView(String id, List<? extends Tag> list) {
			super(id, list);
		}

		private static final long serialVersionUID = 6121812891935022925L;

		@Override
		protected void populateItem(ListItem<Tag> item) {
			final Tag tag = (Tag) item.getModelObject();
			// add name
			Link tagLink = new Link("tagLink") {

				@Override
				public void onClick() {
					// TODO
				}
			};
			tagLink.add(new Label("tagName", tag.getName()));
			item.add(tagLink);
		}

	};
}

// $Id$