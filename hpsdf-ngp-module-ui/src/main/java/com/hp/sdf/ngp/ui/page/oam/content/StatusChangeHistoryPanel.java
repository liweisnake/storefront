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
package com.hp.sdf.ngp.ui.page.oam.content;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.extensions.breadcrumb.IBreadCrumbModel;
import org.apache.wicket.extensions.breadcrumb.panel.BreadCrumbPanel;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.hp.sdf.ngp.common.annotation.Value;
import com.hp.sdf.ngp.model.Asset;
import com.hp.sdf.ngp.model.AssetBinaryVersion;
import com.hp.sdf.ngp.model.AssetLifecycleActionHistory;
import com.hp.sdf.ngp.service.ApplicationService;
import com.hp.sdf.ngp.ui.common.Constant;
import com.hp.sdf.ngp.ui.common.CustomizePagingNavigator;
import com.hp.sdf.ngp.ui.common.Tools;
import com.hp.sdf.ngp.ui.provider.StatusChangeHistoryDataProvider;

public class StatusChangeHistoryPanel extends BreadCrumbPanel {

	private static final long serialVersionUID = -8524244192134679105L;

	private static Log log = LogFactory.getLog(StatusChangeHistoryPanel.class);

	@Value("application.itemsperpage")
	private int itemsPerPage = Constant.DEFALUT_PER_PAGE_COUNT;

	@SpringBean
	ApplicationService applicationService;

	private final BreadCrumbPanel caller;

	private StatusChangeHistoryDataView dataView;

	private Long assetId;

	private StatusChangeHistoryDataProvider dataProvider;

	private Map<Long, AssetBinaryVersion> selectMap = new HashMap<Long, AssetBinaryVersion>();

	public int getItemsPerPage() {
		return itemsPerPage;
	}

	public void setItemsPerPage(int itemsPerPage) {
		this.itemsPerPage = itemsPerPage;
	}

	public ApplicationService getApplicationService() {
		return applicationService;
	}

	public void setApplicationService(ApplicationService applicationService) {
		this.applicationService = applicationService;
	}

	public StatusChangeHistoryPanel(String id, IBreadCrumbModel breadCrumbModel, Long assetId, final BreadCrumbPanel caller) {
		super(id, breadCrumbModel);

		this.assetId = assetId;
		this.caller = caller;

		Asset asset = applicationService.getAsset(assetId);

		add(new Label("assetId", String.valueOf(assetId)));
		add(new Label("assetName", asset.getName()));

		dataProvider = new StatusChangeHistoryDataProvider(applicationService, assetId);

		dataView = new StatusChangeHistoryDataView("statusHistories", dataProvider, itemsPerPage);
		add(dataView);

		add(new CustomizePagingNavigator("navigator", dataView));

		add(new FeedbackPanel("feedback"));

		add(new BackForm("backForm"));
	}

	class BackForm extends Form<Void> {
		private static final long serialVersionUID = 1053492451941171577L;

		public BackForm(String id) {
			super(id);
		}

		@Override
		protected void onSubmit() {
			log.debug("back");
			caller.activate(caller);
		}
	}

	class StatusChangeHistoryDataView extends DataView<AssetLifecycleActionHistory> {

		private static final long serialVersionUID = -6514262177740324330L;

		protected StatusChangeHistoryDataView(String id, IDataProvider<AssetLifecycleActionHistory> dataProvider, int itemsPerPage) {
			super(id, dataProvider, itemsPerPage);
		}

		@Override
		protected void populateItem(Item<AssetLifecycleActionHistory> item) {
			final AssetLifecycleActionHistory assetLifecycleActionHistory = (AssetLifecycleActionHistory) item.getModelObject();

			item.add(new Label("preStatus", assetLifecycleActionHistory.getPrestatus()));

			item.add(new Label("afterStatus", assetLifecycleActionHistory.getPostStatus()));

			item.add(new Label("updateDate", Tools.getValue(assetLifecycleActionHistory.getCompleteDate())));

			item.add(new Label("operatorId", assetLifecycleActionHistory.getOwnerId()));

			item.add(new Label("commentType", assetLifecycleActionHistory.getCommentType()));

			item.add(new Label("comments", assetLifecycleActionHistory.getComments()));

			item.add(new Label("notificationDate", Tools.getValue(assetLifecycleActionHistory.getNotificationDate())));
		}
	}

	public String getTitle() {
		return this.getLocalizer().getString("title", this, Constant.STATUS_CHANGE_HISTORY);
	}
}