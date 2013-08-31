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
package com.hp.sdf.ngp.ui.page.oam.purchasehistory;

import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.Component;
import org.apache.wicket.authorization.strategies.role.metadata.MetaDataRoleAuthorizationStrategy;
import org.apache.wicket.extensions.breadcrumb.IBreadCrumbModel;
import org.apache.wicket.extensions.breadcrumb.panel.BreadCrumbPanel;
import org.apache.wicket.extensions.yui.calendar.DatePicker;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.PropertyModel;

import com.hp.sdf.ngp.ui.common.Constant;
import com.hp.sdf.ngp.ui.common.CustomizeDateTextField;
import com.hp.sdf.ngp.ui.common.PurchaseHistorySearchCondition;
import com.hp.sdf.ngp.workflow.Privilege;

public class PurchaseHistorySearchPanel extends BreadCrumbPanel {

	private final static Log log = LogFactory.getLog(PurchaseHistorySearchPanel.class);

	private static final long serialVersionUID = 5673531943315003111L;

	private SearchPurchaseHistoryTablePanel searchPurchaseHistoryTablePanel = null;

	private PurchaseHistorySearchCondition purchaseHistorySearchCondition;

	public PurchaseHistorySearchPanel(String id, IBreadCrumbModel breadCrumbModel) {
		super(id, breadCrumbModel);

		SearchCommentForm form = new SearchCommentForm("searchForm");
		MetaDataRoleAuthorizationStrategy.authorize(form, Component.RENDER, Privilege.SEARCHPURCHASEHISTORY);
		add(form);

		searchPurchaseHistoryTablePanel = new SearchPurchaseHistoryTablePanel("purchasesTable", purchaseHistorySearchCondition);
		MetaDataRoleAuthorizationStrategy.authorize(searchPurchaseHistoryTablePanel, Component.RENDER, Privilege.VIEWPURCHASEHISTORY);
		add(searchPurchaseHistoryTablePanel);
	}

	class SearchCommentForm extends Form<Void> {

		private static final long serialVersionUID = 404463636873167639L;

		public SearchCommentForm(String id) {
			super(id);

			purchaseHistorySearchCondition = new PurchaseHistorySearchCondition();

			TextField<String> msisdnField = new TextField<String>("msisdn", new PropertyModel<String>(purchaseHistorySearchCondition, "msisdn"));
			add(msisdnField);

			CustomizeDateTextField paidStartDateField = new CustomizeDateTextField("paidStartDate", new PropertyModel<Date>(purchaseHistorySearchCondition, "paidStartDate"), Constant.DATE_PATTERN);
			add(paidStartDateField);
			paidStartDateField.add(new DatePicker());

			CustomizeDateTextField paidEndDateField = new CustomizeDateTextField("paidEndDate", new PropertyModel<Date>(purchaseHistorySearchCondition, "paidEndDate"), Constant.DATE_PATTERN);
			add(paidEndDateField);
			paidEndDateField.add(new DatePicker());

			CustomizeDateTextField completeStartDateField = new CustomizeDateTextField("completeStartDate", new PropertyModel<Date>(purchaseHistorySearchCondition, "completeStartDate"),
					Constant.DATE_PATTERN);
			add(completeStartDateField);
			completeStartDateField.add(new DatePicker());

			CustomizeDateTextField completeEndDateField = new CustomizeDateTextField("completeEndDate", new PropertyModel<Date>(purchaseHistorySearchCondition, "completeEndDate"),
					Constant.DATE_PATTERN);
			add(completeEndDateField);
			completeEndDateField.add(new DatePicker());

			add(new Button("search") {
				private static final long serialVersionUID = 2263842147860607342L;

				public void onSubmit() {
					log.debug("searchPurchaseHistoryTablePanel.purchasesView.updateModel.");
					if (searchPurchaseHistoryTablePanel.purchasesView != null) {
						searchPurchaseHistoryTablePanel.purchasesView.updateModel(purchaseHistorySearchCondition);
					}
				}

			});
		}
	}

	public String getTitle() {
		return this.getLocalizer().getString("title", this, "Search Purchase History");
	}

}
