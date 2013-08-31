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
package com.hp.sdf.ngp.ui.page.oam.platform;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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

import com.hp.sdf.ngp.model.Platform;
import com.hp.sdf.ngp.service.ApplicationService;
import com.hp.sdf.ngp.ui.common.CustomizePagingNavigator;
import com.hp.sdf.ngp.ui.common.Tools;
import com.hp.sdf.ngp.ui.provider.PlatformSearchDataProvider;

public class SearchPlatformTabelPanel extends Panel {

	private static final long serialVersionUID = 8006956197490194311L;

	private static final Log log = LogFactory.getLog(SearchPlatformTabelPanel.class);

	public PlatformsView platformsView;

	private Map<Long, Long> platformsMap = new HashMap<Long, Long>();

	public SearchPlatformTabelPanel(String id, String platformName, IBreadCrumbModel breadCrumbModel) {
		super(id);

		this.add(new FeedbackPanel("feedBack"));

		add(new PlatformsForm("platformsForm", platformName, breadCrumbModel));
	}

	class PlatformsForm extends Form<Void> {

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

		public PlatformsForm(String id, String platformName, IBreadCrumbModel breadCrumbModel) {
			super(id);

			CheckBox groupselector = new CheckBox("groupselector", new PropertyModel<Boolean>(this, "groupSelected"));
			groupselector.setMarkupId("groupselector");
			add(groupselector);

			PlatformSearchDataProvider dataProvider = new PlatformSearchDataProvider(applicationService, platformName);
			platformsView = new PlatformsView("platformsView", dataProvider, itemsPerPage, applicationService, breadCrumbModel);
			add(platformsView);

			add(new CustomizePagingNavigator("navigator", platformsView));

			Button delete = new Button("delete") {
				private static final long serialVersionUID = 2189543498800077396L;

				public void onSubmit() {
					log.debug("delete the handsetDeviceIds.");

					Set<Long> platformIds = platformsMap.keySet();
					if (platformIds != null && platformIds.size() > 0) {
						for (Long platformId : platformIds) {
							if (null == applicationService.getPlatformById(platformId)) {
								error(getLocalizer().getString("error.nullplatform", SearchPlatformTabelPanel.this));
								return;
							}
							applicationService.deletePlatformById(platformId);
						}
					} else {
						error(getLocalizer().getString("error.noplatform", SearchPlatformTabelPanel.this));
						return;
					}

					log.debug("Set SelectAll checkbox to not checked.");
					setGroupSelected(false);
					log.debug("Clear the platformIds.");
					platformIds.clear();

				}
			};
			delete.add(Tools.addConfirmJs(getLocalizer().getString("confirmMsg", SearchPlatformTabelPanel.this)));
			add(delete);
		}

	}

	class PlatformsView extends DataView<Platform> {

		private static final long serialVersionUID = 2738548802166595044L;

		private IBreadCrumbModel breadCrumbModel;

		public PlatformsView(String id, IDataProvider<Platform> dataProvider, int itemsPerPage, ApplicationService applicationService, IBreadCrumbModel breadCrumbModel) {
			super(id, dataProvider, itemsPerPage);
			this.breadCrumbModel = breadCrumbModel;
		}

		public void updateModel(String platformName) {
			PlatformSearchDataProvider dataProvider = (PlatformSearchDataProvider) this.getDataProvider();
			dataProvider.setPlatformName(platformName);
		}

		protected void populateItem(Item<Platform> item) {

			final Platform platform = (Platform) item.getModelObject();

			item.add(new CheckBox("select", new IModel<Boolean>() {

				private static final long serialVersionUID = 7865238665776348241L;

				public Boolean getObject() {

					if (platformsMap != null && platformsMap.size() != 0) {
						Long o = platformsMap.get(platform.getId());
						return null == o ? false : true;
					}

					return false;
				}

				public void setObject(Boolean object) {
					if (object) {
						platformsMap.put(platform.getId(), platform.getId());
					}
				}

				public void detach() {
				}
			}));

			item.add(new Label("platformName", platform.getName()));

			item.add(new Label("description", platform.getDescription()));

			BreadCrumbPanelLink detail = new BreadCrumbPanelLink("detail", breadCrumbModel, new IBreadCrumbPanelFactory() {

				private static final long serialVersionUID = 1L;

				public BreadCrumbPanel create(String componentId, IBreadCrumbModel breadCrumbModel) {
					return new EditPlatformPanel(componentId, breadCrumbModel, platform);
				}
			});
			item.add(detail);
		}

	}

}
