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
package com.hp.sdf.ngp.ui.page.myportal;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.extensions.breadcrumb.IBreadCrumbModel;
import org.apache.wicket.extensions.breadcrumb.panel.BreadCrumbPanel;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.jbpm.api.ProcessInstance;

import com.hp.sdf.ngp.common.annotation.Value;
import com.hp.sdf.ngp.model.UserLifecycleAction;
import com.hp.sdf.ngp.service.InfoService;
import com.hp.sdf.ngp.ui.provider.UserPromotionApproveDataProvider;
import com.hp.sdf.ngp.workflow.jbpm.JbpmHelper;
import com.hp.sdf.ngp.workflow.jbpm.JbpmServiceHolder;

public class UserPromotionApprovePanel extends BreadCrumbPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private final static Log log = LogFactory.getLog(UserPromotionApprovePanel.class);

	private int itemsPerPage = 10;

	private SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");

	@SpringBean
	private InfoService infoService;

	@Value("application.itemsperpage")
	public void setItemsPerPage(int itemsPerPage) {
		this.itemsPerPage = itemsPerPage;
	}

	public UserPromotionApprovePanel(String id, IBreadCrumbModel breadCrumbModel) {
		super(id, breadCrumbModel);
		UserPromotionApproveDataProvider userPromotionApproveProvider = new UserPromotionApproveDataProvider(infoService);
		add(new UserPromotionView("userPromotion", userPromotionApproveProvider, itemsPerPage));
	}

	class UserPromotionView extends DataView<UserLifecycleAction> {

		private static final long serialVersionUID = 1L;

		protected UserPromotionView(String id, IDataProvider<UserLifecycleAction> dataProvider, int itemsPerPage) {
			super(id, dataProvider, itemsPerPage);
		}

		@Override
		protected void populateItem(Item<UserLifecycleAction> item) {
			log.debug("find item : " + item);
			final UserLifecycleAction cycle = item.getModelObject();
			item.add(new Label("user", cycle.getUserid()));

			item.add(new Label("role", cycle.getPostRole()));

			item.add(new Label("remark", cycle.getComments()));

			// add date time
			Date createDate = cycle.getCreateDate();
			item.add(new Label("createDate", createDate == null ? "" : sdf.format(createDate)));

			item.add(new Link("approve") {
				public void onClick() {
					log.debug("UserPromotionApprovePanel>>assign " + cycle.getUserid() + " role " + cycle.getPostRole());
					
					// trigger the process instance
					String instanceId = JbpmHelper.genInstanceId("userPromotion", cycle.getUserid(), cycle.getPostRole());
					ProcessInstance ins = JbpmServiceHolder.executionService.createProcessInstanceQuery().processInstanceKey(instanceId).uniqueResult();
					if (null != ins) {
						//function as the final step as definition yet,so no need parameter any more 
						JbpmServiceHolder.executionService.signalExecutionById(ins.getId());
					} else {
						// TODO
						log.warn("no process instance for this promotion approve:userId=" + cycle.getUserid() + ",postRole=" + cycle.getPostRole() + ",date="
								+ cycle.getCreateDate());
					}

				}
			});

		}

	}

	public String getTitle() {
		return this.getLocalizer().getString("title", this, "User promotion approve");
	}

}

// $Id$