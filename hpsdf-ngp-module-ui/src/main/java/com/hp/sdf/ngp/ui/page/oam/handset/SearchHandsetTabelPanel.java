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
package com.hp.sdf.ngp.ui.page.oam.handset;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

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

import com.hp.sdf.ngp.custom.sbm.api.impl.model.HandSetDeviceImpl;
import com.hp.sdf.ngp.custom.sbm.api.storeclient.exception.HandsetServiceException;
import com.hp.sdf.ngp.custom.sbm.api.storeclient.model.HandSetDevice;
import com.hp.sdf.ngp.custom.sbm.api.storeclient.service.HandsetDeviceService;
import com.hp.sdf.ngp.ui.common.CheckPanel;
import com.hp.sdf.ngp.ui.common.CustomizePagingNavigator;
import com.hp.sdf.ngp.ui.common.HandsetSearchCondition;
import com.hp.sdf.ngp.ui.common.Tools;
import com.hp.sdf.ngp.ui.provider.HandsetSearchDataProvider;
import com.hp.sdf.ngp.workflow.Privilege;

public class SearchHandsetTabelPanel extends Panel {

	private static final long serialVersionUID = 8006956197490194311L;

	private static final Log log = LogFactory.getLog(SearchHandsetTabelPanel.class);

	public HandsetsView handsetsView;

	private Map<Long, Long> handsetsMap = new HashMap<Long, Long>();

	public SearchHandsetTabelPanel(String id, HandsetSearchCondition handsetSearchCondition, IBreadCrumbModel breadCrumbModel) {
		super(id);

		this.add(new FeedbackPanel("feedBack"));

		add(new HandsetsForm("handsetsForm", handsetSearchCondition, breadCrumbModel));
	}

	class HandsetsForm extends Form<Void> {

		private static final long serialVersionUID = 427248307610664062L;

		@SpringBean
		private HandsetDeviceService handsetDeviceService;

		private static final int itemsPerPage = 50;

		private boolean groupSelected;

		public boolean isGroupSelected() {
			return groupSelected;
		}

		public void setGroupSelected(boolean groupSelected) {
			this.groupSelected = groupSelected;
		}

		public HandsetsForm(String id, HandsetSearchCondition handsetSearchCondition, IBreadCrumbModel breadCrumbModel) {
			super(id);

			CheckBox groupselector = new CheckBox("groupselector", new PropertyModel<Boolean>(this, "groupSelected"));
			groupselector.setMarkupId("groupselector");
			add(groupselector);

			HandsetSearchDataProvider dataProvider = new HandsetSearchDataProvider(handsetDeviceService, handsetSearchCondition);

			handsetsView = new HandsetsView("handsetsView", dataProvider, itemsPerPage, handsetDeviceService, breadCrumbModel);
			add(handsetsView);

			add(new CustomizePagingNavigator("navigator", handsetsView));

			final CheckPanel checkPanel = new CheckPanel("checkPanel", getLocalizer().getString("promptTitle", SearchHandsetTabelPanel.this), getLocalizer().getString("confirmMsg", SearchHandsetTabelPanel.this)) {
				private static final long serialVersionUID = 7465424124110025236L;
				@Override
				public void howDo() {
					log.debug("delete the handsetDeviceIds.");

					Set<Long> handsetDeviceIds = handsetsMap.keySet();
					if (handsetDeviceIds != null && handsetDeviceIds.size() > 0) {
						for (Long handSetDeviceId : handsetDeviceIds) {

							if (null == handsetDeviceService.getHandSetDeviceById(handSetDeviceId)) {
								error(getLocalizer().getString("msg.error.save.nohandset",  this, "Handset does not exist!"));
								return;
							}

							try {
								handsetDeviceService.deleteHandSetDeviceById(handSetDeviceId);
							} catch (HandsetServiceException exception) {
								log.error("HandsetServiceException :" + exception);
								exception.printStackTrace();
							}
						}
					} else {
						error(getLocalizer().getString("msg.error.del.noselect",  this, "Please select one handset device at least."));
						return;
					}

					log.debug("Set SelectAll checkbox to not checked.");
					setGroupSelected(false);
					log.debug("Clear the handsetsMap.");
					handsetsMap.clear();
				}
			};
			add(checkPanel);
			
			Button delete = new Button("delete") {
				private static final long serialVersionUID = 2189543498800077396L;

				public void onSubmit() {
					checkPanel.show();
					
				}
			};
//			delete.add(Tools.addConfirmJs(getLocalizer().getString("confirmMsg", SearchHandsetTabelPanel.this)));
			MetaDataRoleAuthorizationStrategy.authorize(delete, Component.RENDER, Privilege.DELETEHANDSET);
			add(delete);
		}

	}

	class HandsetsView extends DataView<HandSetDevice> {

		private static final long serialVersionUID = 2738548802166595044L;

		private IBreadCrumbModel breadCrumbModel;

		public HandsetsView(String id, IDataProvider<HandSetDevice> dataProvider, int itemsPerPage, HandsetDeviceService handsetDeviceService, IBreadCrumbModel breadCrumbModel) {
			super(id, dataProvider, itemsPerPage);
			this.breadCrumbModel = breadCrumbModel;
		}

		public void updateModel(HandsetSearchCondition handsetSearchCondition) {
			HandsetSearchDataProvider dataProvider = (HandsetSearchDataProvider) this.getDataProvider();
			dataProvider.setHandsetSearchCondition(handsetSearchCondition);
		}

		protected void populateItem(Item<HandSetDevice> item) {

			final HandSetDeviceImpl handSetDevice = (HandSetDeviceImpl) item.getModelObject();

			item.add(new CheckBox("select", new IModel<Boolean>() {

				private static final long serialVersionUID = 7865238665776348241L;

				public Boolean getObject() {

					if (handsetsMap != null && handsetsMap.size() != 0) {
						com.hp.sdf.ngp.custom.sbm.storeclient.model.HandSetDevice handSetDeviceModel = handSetDevice.getHandSetDevice();
						if (handSetDeviceModel != null) {
							Long o = handsetsMap.get(handSetDeviceModel.getId());
							return null == o ? false : true;
						} else {
							return false;
						}
					}

					return false;
				}

				public void setObject(Boolean object) {
					if (object) {
						com.hp.sdf.ngp.custom.sbm.storeclient.model.HandSetDevice handSetDeviceModel = handSetDevice.getHandSetDevice();
						if (handSetDeviceModel != null) {
							handsetsMap.put(handSetDeviceModel.getId(), handSetDeviceModel.getId());
						}
					}
				}

				public void detach() {
				}
			}));

			item.add(new Label("displayName", handSetDevice.getDisplayName()));

			item.add(new Label("deviceName", handSetDevice.getDeviceName()));

			//TODO : add createTime
			item.add(new Label("createTime", ""));

			BreadCrumbPanelLink detail = new BreadCrumbPanelLink("detail", breadCrumbModel, new IBreadCrumbPanelFactory() {

				private static final long serialVersionUID = 1L;

				public BreadCrumbPanel create(String componentId, IBreadCrumbModel breadCrumbModel) {
					return new EditHandsetPanel(componentId, breadCrumbModel, handSetDevice);
				}
			});
			MetaDataRoleAuthorizationStrategy.authorize(detail, Component.RENDER, Privilege.EDITHANDSET);
			item.add(detail);
		}

	}

}
