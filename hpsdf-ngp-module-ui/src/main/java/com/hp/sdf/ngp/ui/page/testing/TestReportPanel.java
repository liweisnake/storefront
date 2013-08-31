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
package com.hp.sdf.ngp.ui.page.testing;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.extensions.breadcrumb.IBreadCrumbModel;
import org.apache.wicket.extensions.breadcrumb.panel.BreadCrumbPanel;
import org.apache.wicket.extensions.breadcrumb.panel.IBreadCrumbPanelFactory;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RadioChoice;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.jbpm.api.ProcessInstance;

import com.hp.sdf.ngp.api.search.SearchExpression;
import com.hp.sdf.ngp.api.search.Condition.NumberComparer;
import com.hp.sdf.ngp.api.search.Condition.StringComparer;
import com.hp.sdf.ngp.common.constant.AssetLifecycleConstants;
import com.hp.sdf.ngp.model.Asset;
import com.hp.sdf.ngp.model.AssetBinaryVersion;
import com.hp.sdf.ngp.model.AssetLifecycleAction;
import com.hp.sdf.ngp.model.Platform;
import com.hp.sdf.ngp.search.condition.assetlifecycleaction.AssetLifecycleActionAssetBinaryVersionIdCondition;
import com.hp.sdf.ngp.search.condition.assetlifecycleaction.AssetLifecycleActionEventCondition;
import com.hp.sdf.ngp.search.condition.assetlifecycleaction.AssetLifecycleActionResultCondition;
import com.hp.sdf.ngp.search.condition.assetlifecycleaction.AssetLifecycleActionSubmitteridCondition;
import com.hp.sdf.ngp.search.engine.SearchExpressionImpl;
import com.hp.sdf.ngp.service.ApplicationService;
import com.hp.sdf.ngp.ui.WicketSession;
import com.hp.sdf.ngp.ui.common.Tools;
import com.hp.sdf.ngp.workflow.jbpm.JbpmHelper;
import com.hp.sdf.ngp.workflow.jbpm.JbpmServiceHolder;

@SuppressWarnings( { "unchecked" })
public class TestReportPanel extends BreadCrumbPanel {

	private static final long serialVersionUID = -3341232723323741567L;

	private final static Log log = LogFactory.getLog(TestReportPanel.class);

	@SpringBean
	private ApplicationService applicationService;

	static final String[] TEST_RESULT = new String[] { AssetLifecycleConstants.BIN_TEST_RESULT_PASS, AssetLifecycleConstants.BIN_TEST_RESULT_FAIL };

	public TestReportPanel(String id, IBreadCrumbModel breadCrumbModel, AssetBinaryVersion bin) {
		super(id, breadCrumbModel);
		breadCrumbModel.setActive(this);
		TestReportForm reportForm = new TestReportForm("testReportForm", applicationService, bin);
		add(reportForm);

		// Create feedback panel and add to page
		final FeedbackPanel feedback = new FeedbackPanel("feedback");
		add(feedback);
	}

	public String getTitle() {
		return this.getLocalizer().getString("title", this, "Application Test Report");
	}

	public class TestReportForm extends Form {

		private static final long serialVersionUID = -5125591709076353399L;

		private ApplicationService applicationService;

		private AssetBinaryVersion bin;

		private AssetLifecycleAction binTest = null;

		private Result choice;

		private List<Result> RESULTS = new ArrayList();

		public TestReportForm(String id, ApplicationService applicationService, AssetBinaryVersion bin) {

			super(id);
			this.applicationService = applicationService;
			this.bin = bin;

			String userId = WicketSession.get().getUserId();

			SearchExpression searchExpression = new SearchExpressionImpl();
			searchExpression.addCondition(new AssetLifecycleActionSubmitteridCondition(userId, StringComparer.EQUAL, false, false));
			searchExpression.addCondition(new AssetLifecycleActionEventCondition(AssetLifecycleConstants.BIN_ACTION_TYPE_TEST, StringComparer.EQUAL, false, false));
			searchExpression.addCondition(new AssetLifecycleActionResultCondition(AssetLifecycleConstants.BIN_TEST_RESULT_TESTING, StringComparer.EQUAL, false, false));
			searchExpression.addCondition(new AssetLifecycleActionAssetBinaryVersionIdCondition(bin.getId(), NumberComparer.EQUAL));
			searchExpression.setFirst(0);
			searchExpression.setMax(Integer.MAX_VALUE);
			List<AssetLifecycleAction> binTests = applicationService.getAssetLifecycleAction(searchExpression);

			if (binTests != null && binTests.size() > 0) {
				binTest = binTests.get(0);
			}

			Asset asset = applicationService.getAssetByBinaryId(bin.getId());
			bin.setAsset(asset);
			Label name = new Label("name", asset.getName());
			add(name);
			Label version = new Label("version", bin.getVersion());
			add(version);
			List<Platform> list = applicationService.getPlatformByAssetId(asset.getId());
			Label platform = new Label("platform", Tools.getPlatfromNameFromList(list));
			add(platform);

			Result result = new Result(TEST_RESULT[0], this.getLocalizer().getString("label.pass", TestReportPanel.this, "Pass"));
			RESULTS.add(result);
			result = new Result(TEST_RESULT[1], this.getLocalizer().getString("label.fail", TestReportPanel.this, "Fail"));
			RESULTS.add(result);

			RadioChoice<Result> resultChoice = new RadioChoice<Result>("resultChoice", new PropertyModel<Result>(this, "choice"), RESULTS, new ChoiceRenderer<Result>("label", "key"));
			resultChoice.setSuffix("");
			resultChoice.setRequired(true);
			add(resultChoice);

			TextArea<String> description = new TextArea<String>("comments", new PropertyModel<String>(binTest, "comments"));
			add(description);
		}

		/**
		 * @see org.apache.wicket.markup.html.form.Form#onSubmit()
		 */
		public final void onSubmit() {

			if (log.isDebugEnabled()) {
				log.debug("test result submitted: " + choice.getKey());
				log.debug("comments: " + binTest.getComments());
			}
			
			binTest.setCompleteDate(new Date());
			binTest.setSubmitterid(WicketSession.get().getUserId());
			
			applicationService.saveOrUpdateAssetLifecycleAction(binTest);
			
			HashMap<String, Object> variables = new HashMap<String, Object>();
			variables.put("testResult", choice.getKey());

			String instanceId = JbpmHelper.genInstanceId("binaryLifeCycle", "", String.valueOf(bin.getId()));

			if (null != JbpmHelper.checkPromotionStatus(instanceId)) {
				ProcessInstance ins = JbpmServiceHolder.executionService.createProcessInstanceQuery().processInstanceKey(instanceId).uniqueResult();
				JbpmServiceHolder.executionService.signalExecutionById(ins.getId(), variables);
			} else {
				log.error("no instance of this id:" + instanceId);
			}

			activate(new IBreadCrumbPanelFactory() {
				public BreadCrumbPanel create(String componentId, IBreadCrumbModel breadCrumbModel) {
					return new SuccessPanel(componentId, breadCrumbModel);
				}
			});

		}

		public class Result implements Serializable {

			private static final long serialVersionUID = 1L;
			private String key;
			private String label;

			public Result(String key, String label) {
				this.key = key;
				this.label = label;
			}

			public String getKey() {
				return key;
			}

			public void setKey(String key) {
				this.key = key;
			}

			public String getLabel() {
				return label;
			}

			public void setLabel(String label) {
				this.label = label;
			}

		}
	}

}

// $Id$