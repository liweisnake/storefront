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

import org.apache.commons.lang.StringUtils;
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
import com.hp.sdf.ngp.ui.WicketSession;
import com.hp.sdf.ngp.ui.common.LocalImageUri;
import com.hp.sdf.ngp.ui.common.SearchMetaInfo;

public class AppBarPanel extends Panel {

	private final static Log log = LogFactory.getLog(AppBarPanel.class);

	private Asset app;

	private AppSearchResultPanel parentPanel;

	private static final long serialVersionUID = 7859653580924192927L;

	@SpringBean
	private ApplicationService applicationService;

	public AppBarPanel(String id, IModel<Asset> model, AppSearchResultPanel parentPanel) {
		super(id, model);
		this.app = model.getObject();
		this.parentPanel = parentPanel;
		populate();
	}

	public void populate() {
		// add image

		final String thumbnail = app.getThumbnailLocation();

		if (StringUtils.isEmpty(thumbnail)) {
			add(new ContextImage("image", "images/null_thumbnail.jpg"));
		} else {

			add(new LocalImageUri("image", thumbnail));
			/*
			 * add(new Image("image", new DynamicImageResource() {
			 * 
			 * private static final long serialVersionUID = -7306013743146559542L;
			 * 
			 * protected byte[] getImageData() { AttributeValue<String> attr = thumbnail.get(0); String filePath = attr.getValue(); File file = new File(filePath); byte[] fileBuffer = null; try { InputStream inputStream = new FileInputStream(file); fileBuffer = new byte[inputStream.available()]; inputStream.read(fileBuffer); } catch (Exception e) { log.error("Unable to read thumbnail pic.", e); } return fileBuffer; } }));
			 */
		}

		// add name
		BreadCrumbPanelLink nameLink = new BreadCrumbPanelLink("nameLink", parentPanel.getBreadCrumbModel(), new IBreadCrumbPanelFactory() {

			private static final long serialVersionUID = -5252330321830019456L;

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

		@SuppressWarnings("unchecked")
		protected void populateItem(ListItem<Tag> item) {
			final Tag tag = (Tag) item.getModelObject();

			// add name
			Link tagLink = new Link("tagLink") {

				private static final long serialVersionUID = 106457466865905826L;

				public void onClick() {
					// clean search conditions
					parentPanel.cleanSearchConditions();

					List<SearchMetaInfo> searchMetaInfoList = new ArrayList<SearchMetaInfo>();
					SearchMetaInfo searchMetaInfo = new SearchMetaInfo(SearchMetaInfo.SearchByType.ASSETTAGRELATION_TAG_ID, tag.getId().toString(), tag.getName());
					searchMetaInfoList.add(searchMetaInfo);

					WicketSession.get().setObject(WicketSession.AttributeName.SEARCHMETAINFO.name(), (Serializable) searchMetaInfoList, true);

					parentPanel.refreshAppDataProvider();
				}
			};

			tagLink.add(new Label("tagName", tag.getName()));
			item.add(tagLink);
		}

	};
}
