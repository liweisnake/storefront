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

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.hp.sdf.ngp.model.Category;
import com.hp.sdf.ngp.service.ApplicationService;
import com.hp.sdf.ngp.ui.WicketSession;
import com.hp.sdf.ngp.ui.common.SearchMetaInfo;

@SuppressWarnings( { "unchecked" })
public class CategoryPanel extends Panel {

	@SpringBean
	private ApplicationService applicationService;

	/**
	 * 
	 */
	private static final long serialVersionUID = 1082278365847817197L;

	private List<Category> categorys;

	public CategoryPanel(String id) {
		super(id);

		this.categorys = applicationService.getAllCategory(0, Integer.MAX_VALUE);

		// adds all category choice
		Category all = new Category(new Long(-1), this.getString("all"));
		this.categorys.add(0, all);

		ListView listView = new ListView("listview", categorys) {

			/**
			 * 
			 */
			private static final long serialVersionUID = 6121812891935022925L;

			@Override
			protected void populateItem(ListItem item) {
				final Category category = (Category) item.getModelObject();

				final Label label = new Label("name", category.getName());

				Link link = new Link("link") {

					// Save the search info into session context so that it can
					// be retrieved by other page
					@Override
					public void onClick() {

						// Get the object from session
						List<SearchMetaInfo> searchMetaInfoList = (List<SearchMetaInfo>) WicketSession.get().getObject(WicketSession.AttributeName.SEARCHMETAINFO.name());

						SearchMetaInfo searchMetaInfo = new SearchMetaInfo(SearchMetaInfo.SearchByType.ASSETCATEGORYRELATION_CATEGORY_ID, category.getId().toString(), category.getName());
						if (searchMetaInfoList == null) {
							searchMetaInfoList = new ArrayList<SearchMetaInfo>();
							searchMetaInfoList.add(searchMetaInfo);
						} else {
							// check whether there is a old search meta data
							// about category
							SearchMetaInfo current = null;
							for (SearchMetaInfo value : searchMetaInfoList) {
								if (value.getSearchBy() == SearchMetaInfo.SearchByType.ASSETCATEGORYRELATION_CATEGORY_ID) {
									current = value;
									break;
								}
							}
							if (current != null) {
								searchMetaInfoList.remove(current);
							}

							// the id of "all categories" is -1
							if (category.getId().longValue() != -1L) {
								searchMetaInfoList.add(searchMetaInfo);
							}
						}
						WicketSession.get().setObject(WicketSession.AttributeName.SEARCHMETAINFO.name(), (Serializable) searchMetaInfoList, true);
					}

				};

				link.add(label);
				item.add(link);

			}

		};
		this.add(listView);
	}
}
