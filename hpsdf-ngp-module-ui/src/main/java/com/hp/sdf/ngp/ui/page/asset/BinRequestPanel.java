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

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.hp.sdf.ngp.api.search.SearchExpression;
import com.hp.sdf.ngp.api.search.Condition.NumberComparer;
import com.hp.sdf.ngp.api.search.Condition.StringComparer;
import com.hp.sdf.ngp.common.constant.AssetLifecycleConstants;
import com.hp.sdf.ngp.model.AssetLifecycleAction;
import com.hp.sdf.ngp.search.condition.assetlifecycleaction.AssetLifecycleActionAssetBinaryVersionIdCondition;
import com.hp.sdf.ngp.search.condition.assetlifecycleaction.AssetLifecycleActionEventCondition;
import com.hp.sdf.ngp.search.engine.SearchExpressionImpl;
import com.hp.sdf.ngp.service.ApplicationService;
import com.hp.sdf.ngp.workflow.AssetBinaryVersionLifeCycleEngine;

class BinRequestPanel extends Panel {

	private static final long serialVersionUID = -3780827867970892935L;

	private static final Log log = LogFactory.getLog(BinRequestPanel.class);

	@SpringBean
	private ApplicationService applicationService;

	@SpringBean
	private AssetBinaryVersionLifeCycleEngine assetBinaryVersionLifeCycleEngine;

	Long binaryId;

	List<AssetLifecycleAction> actionRequests;

	private AjaxButton applyBtn;

	private BinApplyView applyView;

	public BinRequestPanel(String id) {
		super(id);

		actionRequests = new ArrayList<AssetLifecycleAction>();
		applyView = new BinApplyView("applies", new PropertyModel(this, "actionRequests"));
		applyView.setOutputMarkupId(true);
		add(applyView);
	}

	public void update(Long binaryId) {
		this.binaryId = binaryId;

		if (binaryId != null) {

			SearchExpression searchExpression = new SearchExpressionImpl();
			searchExpression.addCondition(new AssetLifecycleActionAssetBinaryVersionIdCondition(binaryId, NumberComparer.EQUAL));
			searchExpression.addCondition(new AssetLifecycleActionEventCondition(AssetLifecycleConstants.BIN_ACTION_TYPE_PROPOSE, StringComparer.EQUAL, false, false));
			searchExpression.setFirst(0);
			searchExpression.setMax(Integer.MAX_VALUE);
			actionRequests = applicationService.getAssetLifecycleAction(searchExpression);
		} else {
			actionRequests = new ArrayList<AssetLifecycleAction>();
		}
		log.debug("binary application request size=" + actionRequests.size());
		applyView.setDefaultModel(new PropertyModel(this, "actionRequests"));
		add(applyView);
	}

	public class BinApplyView extends ListView<AssetLifecycleAction> {

		private static final long serialVersionUID = 3426394721250965668L;

		public BinApplyView(String id, IModel<? extends List<? extends AssetLifecycleAction>> model) {
			super(id, model);
		}

		@Override
		protected void populateItem(ListItem<AssetLifecycleAction> item) {

			AssetLifecycleAction request = item.getModelObject();
			ApplyForm applyForm = new ApplyForm("applyForm", request);
			item.add(applyForm);

		}

	}

	public class ApplyForm extends Form<Void> {

		private static final long serialVersionUID = 633788313579665000L;

		private AssetLifecycleAction actionRequest;

		public AssetLifecycleAction getActionRequest() {
			return actionRequest;
		}

		public void setActionRequest(AssetLifecycleAction actionRequest) {
			this.actionRequest = actionRequest;
		}

		public ApplyForm(String id, AssetLifecycleAction actionRequest) {
			super(id);
			this.actionRequest = actionRequest;

			Label status = new Label("status", new PropertyModel(actionRequest, "action"));
			add(status);

			Button submit = new ApplyButton("apply");
			add(submit);
		}

	}

	public class ApplyButton extends AjaxButton {

		public ApplyButton(String id) {
			super(id);
		}

		private static final long serialVersionUID = 1L;

		@Override
		protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
			ApplyForm applyForm = (ApplyForm) form;
			AssetLifecycleAction request = applyForm.getActionRequest();
			log.debug("bin[id=" + binaryId + "] apply approval for " + request.getPostStatus().toString() + ".");

			// clean other proprosals
			cleanAllProposals(binaryId);

			// update the action type to "request approval"
			request.setEvent(AssetLifecycleConstants.BIN_ACTION_TYPE_REQUEST_APPROVAL);
			applicationService.saveOrUpdateAssetLifecycleAction(request);

			BinRequestPanel.this.update(BinRequestPanel.this.binaryId);
			target.addComponent(BinRequestPanel.this);
		}

	}

	private void cleanAllProposals(Long binaryId) {

		// clean all proposals
		// List<PropertyMetaInfo> properties = new ArrayList<PropertyMetaInfo>();
		// PropertyMetaInfo binInfo = new PropertyMetaInfo(
		// AppSearchType.SearchByType.BINARYVERSION_ID.toString(), binaryId, false,
		// ConnectLogic.AND);
		// PropertyMetaInfo actionTypeInfo = new PropertyMetaInfo(
		// "event", AssetLifecycleConstants.BIN_ACTION_TYPE_PROPOSE, false,
		// ConnectLogic.AND);
		// properties.add(binInfo);
		// properties.add(actionTypeInfo);

		// List<AssetLifecycleAction> requests = applicationService.getAssetLifecycleAction(
		// properties, null, 0, Integer.MAX_VALUE);

		SearchExpression searchExpression = new SearchExpressionImpl();
		searchExpression.addCondition(new AssetLifecycleActionAssetBinaryVersionIdCondition(binaryId, NumberComparer.EQUAL));
		searchExpression.addCondition(new AssetLifecycleActionEventCondition(AssetLifecycleConstants.BIN_ACTION_TYPE_PROPOSE, StringComparer.EQUAL, false, false));
		searchExpression.setFirst(0);
		searchExpression.setMax(Integer.MAX_VALUE);
		actionRequests = applicationService.getAssetLifecycleAction(searchExpression);

		List<AssetLifecycleAction> requests = applicationService.getAssetLifecycleAction(searchExpression);

		if (requests != null) {
			log.debug(requests.size() + " proposals to clean");
			for (AssetLifecycleAction request : requests) {
				applicationService.deleteAssetLifecycleActionById(request.getId());
			}
		}
	}
}