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
package com.hp.sdf.ngp.ui.page.configuration;

import java.text.SimpleDateFormat;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.extensions.breadcrumb.IBreadCrumbModel;
import org.apache.wicket.extensions.breadcrumb.panel.BreadCrumbPanel;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.hp.sdf.ngp.api.search.OrderEnum;
import com.hp.sdf.ngp.api.search.SearchExpression;
import com.hp.sdf.ngp.api.search.Condition.NumberComparer;
import com.hp.sdf.ngp.api.search.Condition.StringComparer;
import com.hp.sdf.ngp.api.search.orderby.LifecycleActionHistoryOrderBy;
import com.hp.sdf.ngp.common.constant.AssetLifecycleConstants;
import com.hp.sdf.ngp.model.Asset;
import com.hp.sdf.ngp.model.AssetLifecycleActionHistory;
import com.hp.sdf.ngp.model.Platform;
import com.hp.sdf.ngp.search.condition.assetlifecycleactionhistory.AssetLifecycleActionHistoryAssetIdCondition;
import com.hp.sdf.ngp.search.condition.assetlifecycleactionhistory.AssetLifecycleActionHistoryEventCondition;
import com.hp.sdf.ngp.search.condition.assetlifecycleactionhistory.AssetLifecycleActionHistoryResultCondition;
import com.hp.sdf.ngp.search.engine.SearchExpressionImpl;
import com.hp.sdf.ngp.service.ApplicationService;
import com.hp.sdf.ngp.ui.common.Tools;

public class ConfigurationTestReportDetailPanel extends BreadCrumbPanel {

	private static final long serialVersionUID = 1L;

	private static final Log log = LogFactory.getLog(ConfigurationTestReportDetailPanel.class);

	/**
	 * applicationService injection.
	 */
	@SpringBean
	private ApplicationService applicationService;

	public ConfigurationTestReportDetailPanel(String id, IBreadCrumbModel breadCrumbModel, Long assetId) {
		super(id, breadCrumbModel);

		SearchExpression expression = new SearchExpressionImpl();

		expression.addCondition(new AssetLifecycleActionHistoryAssetIdCondition(assetId, NumberComparer.EQUAL));

		expression.addCondition(new AssetLifecycleActionHistoryEventCondition(AssetLifecycleConstants.BIN_ACTION_TYPE_TEST, StringComparer.EQUAL, false, false));

		expression.addCondition(new AssetLifecycleActionHistoryResultCondition(AssetLifecycleConstants.BIN_TEST_RESULT_TESTING, StringComparer.EQUAL, false, false));
		
		expression.addOrder(LifecycleActionHistoryOrderBy.CREATEDATE, OrderEnum.DESC);
		
		List<AssetLifecycleActionHistory> assetLifecycleActions = applicationService.searchAssetLifecycleActionHistory(expression);

		AssetLifecycleActionHistory assetLifecycleAction = null;
		if (assetLifecycleActions != null && assetLifecycleActions.size() >=1) {
			log.debug("get the first assetLifecycleAction.");
			assetLifecycleAction = assetLifecycleActions.get(0);
		}else{
			log.warn("not sigal column existed on condition:assetId="+assetId+",event="+AssetLifecycleConstants.BIN_ACTION_TYPE_TEST+",result="+AssetLifecycleConstants.BIN_TEST_RESULT_TESTING);
		}

		if (assetLifecycleAction != null) {
			Asset asset = applicationService.getAsset(assetId);
			if (asset != null) {
				// this.add(new Label("Platform", asset.getPlatform() != null ?
				// asset.getPlatform().getName() : ""));
				List<Platform> platformList = applicationService.getPlatformByAssetId(asset.getId());
				this.add(new Label("Platform", Tools.getPlatfromNameFromList(platformList)));
				this.add(new Label("assetName", asset.getName()));
				this.add(new Label("Version", asset.getCurrentVersion()));
			} else {
				this.add(new Label("Platform", ""));
				this.add(new Label("assetName", ""));
				this.add(new Label("Version", ""));
			}

			this.add(new Label("Tester", assetLifecycleAction.getOwnerId()));

			SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
			this.add(new Label("StartDate", assetLifecycleAction.getCreateDate() != null ? sdf.format(assetLifecycleAction.getCreateDate()) : ""));
			this.add(new Label("CompleteDate", assetLifecycleAction.getCompleteDate() != null ? sdf.format(assetLifecycleAction.getCompleteDate()) : ""));
			this.add(new Label("Comments", assetLifecycleAction.getComments()));
		} else {
			this.add(new Label("assetName", ""));
			this.add(new Label("Version", ""));
			this.add(new Label("Platform", ""));
			this.add(new Label("Tester", ""));
			this.add(new Label("StartDate", ""));
			this.add(new Label("CompleteDate", ""));
			this.add(new Label("Comments", ""));
		}

	}

	public String getTitle() {
		return this.getLocalizer().getString("title", this, "Asset Test Report Detail");
	}

}
