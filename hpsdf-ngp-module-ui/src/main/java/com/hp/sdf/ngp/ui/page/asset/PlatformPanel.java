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

import com.hp.sdf.ngp.model.Platform;
import com.hp.sdf.ngp.service.ApplicationService;
import com.hp.sdf.ngp.ui.WicketSession;
import com.hp.sdf.ngp.ui.common.SearchMetaInfo;

@SuppressWarnings( { "unchecked" })
public class PlatformPanel extends Panel {

	private static final long serialVersionUID = 1098734175106159691L;

	@SpringBean
	private ApplicationService applicationService;

	private List<Platform> platforms;

	public PlatformPanel(String id) {
		super(id);

		this.platforms = applicationService.getAllPlatform(0, Integer.MAX_VALUE);

		// add all platform choice
		// Platform all = new Platform(new Long(-1), this.getString("all"), null, null);
		Platform all = new Platform(new Long(-1));
		all.setName(this.getString("all"));
		this.platforms.add(0, all);

		ListView listView = new ListView("listview", platforms) {

			private static final long serialVersionUID = 6121812891935022925L;

			@Override
			protected void populateItem(ListItem item) {
				final Platform platform = (Platform) item.getModelObject();

				final Label label = new Label("name", platform.getName());

				Link link = new Link("link") {

					// Save the search info into session context so that it can
					// be retrieved by other page
					@Override
					public void onClick() {

						// Get the object from session
						List<SearchMetaInfo> searchMetaInfoList = (List<SearchMetaInfo>) WicketSession.get().getObject(WicketSession.AttributeName.SEARCHMETAINFO.name());

						SearchMetaInfo searchMetaInfo = new SearchMetaInfo(SearchMetaInfo.SearchByType.PLATFORM_ID, platform.getId().toString(), platform.getName());
						if (searchMetaInfoList == null) {
							searchMetaInfoList = new ArrayList<SearchMetaInfo>();
							searchMetaInfoList.add(searchMetaInfo);
						} else {
							// check whether there is a old search meta data
							// about platform
							SearchMetaInfo current = null;
							for (SearchMetaInfo value : searchMetaInfoList) {
								if (value.getSearchBy() == SearchMetaInfo.SearchByType.PLATFORM_ID) {
									current = value;
									break;
								}
							}
							if (current != null) {
								searchMetaInfoList.remove(current);
							}
							// the id of "all platforms" is -1
							if (platform.getId().longValue() != -1L) {
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
