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
package com.hp.sdf.ngp.ui.page.management;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.extensions.breadcrumb.IBreadCrumbModel;
import org.apache.wicket.extensions.breadcrumb.panel.BreadCrumbPanel;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.navigation.paging.PagingNavigator;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.jbpm.api.ProcessInstance;

import com.hp.sdf.ngp.common.constant.AssetLifecycleConstants;
import com.hp.sdf.ngp.model.Asset;
import com.hp.sdf.ngp.model.AssetBinaryVersion;
import com.hp.sdf.ngp.model.AssetLifecycleAction;
import com.hp.sdf.ngp.model.Platform;
import com.hp.sdf.ngp.service.ApplicationService;
import com.hp.sdf.ngp.ui.WicketSession;
import com.hp.sdf.ngp.ui.common.Tools;
import com.hp.sdf.ngp.ui.common.UIException;
import com.hp.sdf.ngp.ui.provider.VersionActionDataProvider;
import com.hp.sdf.ngp.workflow.AssetBinaryVersionLifeCycleEngine;
import com.hp.sdf.ngp.workflow.jbpm.JbpmHelper;
import com.hp.sdf.ngp.workflow.jbpm.JbpmServiceHolder;

public class BinApprovalPanel extends BreadCrumbPanel {

	private static final long serialVersionUID = -8318404986658936622L;

	Log log = LogFactory.getLog(BinApprovalPanel.class);

	private int itemsPerPage = 5;

	@SpringBean
	private ApplicationService applicationService;

	@SpringBean
	private AssetBinaryVersionLifeCycleEngine assetBinaryVersionLifeCycleEngine;

	public BinApprovalPanel(String id, IBreadCrumbModel breadCrumbModel) {
		super(id, breadCrumbModel);

		Form<Void> form = new ApprovalForm("approvalForm");
		add(form);
	}

	public int getItemsPerPage() {
		return itemsPerPage;
	}

	public void setItemsPerPage(int itemsPerPage) {
		this.itemsPerPage = itemsPerPage;
	}

	public String getTitle() {
		return this.getLocalizer().getString("title", this, "Applications for Test");
	}

	class ApprovalForm extends Form<Void> {

		private static final long serialVersionUID = 920398234621596239L;

		private ActionDataView actionView;

		public ApprovalForm(String id) {
			super(id);

			VersionActionDataProvider actionDataProvider = new VersionActionDataProvider(applicationService);
			actionDataProvider.setEvent(AssetLifecycleConstants.BIN_ACTION_TYPE_REQUEST_APPROVAL);
			actionView = new ActionDataView("bins", actionDataProvider, itemsPerPage);
			add(actionView);

			add(new PagingNavigator("navigator", actionView));

			Button rejectButton = new Button("reject") {
				private static final long serialVersionUID = -4462045639730597850L;

				@Override
				public void onSubmit() {

					System.out.println(actionView.approvals.size());

					Map<Long, AssetLifecycleAction> map = actionView.approvals;
					for (Iterator<Long> iterator = map.keySet().iterator(); iterator.hasNext();) {

						Long id = iterator.next();
						AssetLifecycleAction action = map.get(id);

						if (action.getBinaryVersion() == null)
							throw new UIException("AssetBinaryVersion can not be null.");

						log.debug("request id " + action.getId());
						log.debug("approved status to " + action.getPostStatus());

						String instanceId = JbpmHelper.genInstanceId("binaryLifeCycle", "", String.valueOf(action.getBinaryVersion().getId()));
						if (null != JbpmHelper.checkPromotionStatus(instanceId)) {
							ProcessInstance ins = JbpmServiceHolder.executionService.createProcessInstanceQuery().processInstanceKey(instanceId).uniqueResult();
							JbpmServiceHolder.executionService.signalExecutionById(ins.getId(), "fail");
							actionView.approvals.remove(action.getId());
						} else {
							// TODO
						}
					}
				}

			};
			// rejectButton.setDefaultFormProcessing(true);
			add(rejectButton);

			Button approveButton = new Button("approve") {

				@Override
				public void onSubmit() {
					if (log.isDebugEnabled()) {
						log.debug("bin status update approved for " + actionView.approvals.size() + " binaries");
					}

					Map<Long, AssetLifecycleAction> map = actionView.approvals;
					for (Iterator<Long> iterator = map.keySet().iterator(); iterator.hasNext();) {

						Long id = iterator.next();
						AssetLifecycleAction action = map.get(id);
						action.setOwnerid(WicketSession.get().getUserId());
						applicationService.saveOrUpdateAssetLifecycleAction(action);
						
						actionView.approvals.remove(action.getId());
						
						log.debug("request id " + action.getId());
						log.debug("approved status to " + action.getPostStatus());

						String instanceId = JbpmHelper.genInstanceId("binaryLifeCycle", "", String.valueOf(action.getBinaryVersion().getId()));
						if (null != JbpmHelper.checkPromotionStatus(instanceId)) {
							ProcessInstance ins = JbpmServiceHolder.executionService.createProcessInstanceQuery().processInstanceKey(instanceId).uniqueResult();
							JbpmServiceHolder.executionService.signalExecutionById(ins.getId(), "pass");

						} else {
							// TODO
						}

					}
				}
			};
			// approveButton.setDefaultFormProcessing(true);
			add(approveButton);
		}
		// public final void onSubmit() {
		// if (log.isDebugEnabled()) {
		// log.debug("bin status update approved for " +
		// actionView.approvals.size() + " binaries");
		// }
		//
		// Collection<AssetLifecycleAction> values =
		// actionView.approvals.values();
		// if (values != null) {
		// for (AssetLifecycleAction action : values) {
		//
		// log.debug("request id " + action.getId());
		// log.debug("approved status to " + action.getPostStatus());
		//
		// String instanceId = JbpmHelper.genInstanceId("binaryLifeCycle", "",
		// String.valueOf(action.getBinaryVersion().getId()));
		// if (null != JbpmHelper.checkPromotionStatus(instanceId)) {
		// ProcessInstance ins =
		// JbpmServiceHolder.executionService.createProcessInstanceQuery().processInstanceKey(instanceId).uniqueResult();
		// JbpmServiceHolder.executionService.signalExecutionById(ins.getId());
		// } else {
		// // TODO
		// }
		// }
		// }
		// }
	}

	class ActionDataView extends DataView<AssetLifecycleAction> {

		private static final long serialVersionUID = -7377648080534121488L;

		private Map<Long, AssetLifecycleAction> approvals = new HashMap<Long, AssetLifecycleAction>();

		protected ActionDataView(String id, IDataProvider<AssetLifecycleAction> dataProvider, int itemsPerPage) {
			super(id, dataProvider, itemsPerPage);
		}

		@Override
		protected void populateItem(Item<AssetLifecycleAction> item) {
			final AssetLifecycleAction action = item.getModelObject();
			long versionId = action.getBinaryVersion().getId();
			final AssetBinaryVersion bin = applicationService.getAssetBinaryById(versionId);

			// add name
			Asset app = applicationService.getAsset(bin.getAsset().getId());

			item.add(new Label("appName", app.getName()));

			item.add(new Label("version", bin.getVersion()));

			List<Platform> list = applicationService.getPlatformByAssetId(app.getId());
			item.add(new Label("platform", Tools.getPlatfromNameFromList(list)));

			item.add(new Label("brief", app.getBrief()));

			item.add(new Label("currentStatus", action.getPreStatus().getStatus()));

			item.add(new Label("nextStatus", action.getPostStatus().getStatus()));

			CheckBox check = new CheckBox("checkbox", new IModel<Boolean>() {

				private static final long serialVersionUID = 1L;

				public Boolean getObject() {
					return false;
				}

				public void setObject(Boolean object) {
					if (object) {
						ActionDataView.this.approvals.put(action.getId(), action);
					}
				}

				public void detach() {
				}
			});
			item.add(check);
		}

	}

}
