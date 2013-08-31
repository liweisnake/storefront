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

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.Component;
import org.apache.wicket.authorization.strategies.role.Roles;
import org.apache.wicket.authorization.strategies.role.metadata.MetaDataRoleAuthorizationStrategy;
import org.apache.wicket.extensions.breadcrumb.panel.BreadCrumbPanel;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.hp.sdf.ngp.model.Asset;
import com.hp.sdf.ngp.model.Status;
import com.hp.sdf.ngp.service.ApplicationService;
import com.hp.sdf.ngp.workflow.AccessType;
import com.hp.sdf.ngp.workflow.AssetLifeCycleEngine;

@SuppressWarnings( { "unchecked" })
public class AppControlPanel extends Panel {

	private static final long serialVersionUID = -170627425748594809L;

	private final static Log log = LogFactory.getLog(AppControlPanel.class);

	@SpringBean
	private ApplicationService applicationService;
	
	@SpringBean
	private AssetLifeCycleEngine assetLifeCycleEngine;
	
	private Asset app;
	
	private BreadCrumbPanel parent, parentCaller;

	public AppControlPanel(String id, Asset app, BreadCrumbPanel parent,
			BreadCrumbPanel parentCaller) {
		super(id);
		this.app = app;
		this.parent = parent;
		this.parentCaller = parentCaller;
		
		// add app delete form  
		DeleteForm deleteForm = new DeleteForm("deleteAppForm");
		add(deleteForm);
		// apply privilege
		String[] delAuth = 
			assetLifeCycleEngine.getAccessPrivilege(app.getId(), 
					AccessType.DELETE);
		Roles delRoles = new Roles(delAuth);
		MetaDataRoleAuthorizationStrategy.authorize(deleteForm, 
				Component.RENDER, delRoles.toString());
		
		// add app status update form  
		StatuUpdateForm statuUpdateForm = new StatuUpdateForm("statuUpdateForm");
		add(statuUpdateForm);
		// apply privilege
		String[] statusAuth = 
			assetLifeCycleEngine.getAccessPrivilege(app.getId(), 
					AccessType.STATUS);
		Roles statusRoles = new Roles(statusAuth);
		MetaDataRoleAuthorizationStrategy.authorize(statuUpdateForm, 
				Component.RENDER, statusRoles.toString());
	}

	public String getTitle() {
		return this.getLocalizer().getString("title", this,
				"Application Control");
	}

	public class DeleteForm extends Form {

		private static final long serialVersionUID = -2059998566061785695L;

		public DeleteForm(String id) {
			super(id);
		}
		
		@Override
		protected void onSubmit() {
			log.debug("delete app[appId=" + app.getId() + "]");
			applicationService.deleteAsset(app.getId());
			if (parent != null) {
				parent.activate(parentCaller);
			}
		}
	}
	
	public class StatuUpdateForm extends Form {

		private static final long serialVersionUID = 1L;

		public StatuUpdateForm(String id) {
			super(id);
			List<Status> allStatuses = assetLifeCycleEngine.getDefinedStatus();
			ChoiceRenderer<Status> statusRenderer = new ChoiceRenderer<Status>("status", "id");
			DropDownChoice<Status> statusChoice = new DropDownChoice<Status>("appstatus",
					new PropertyModel(app, "status"),
					allStatuses,
					statusRenderer);
			add(statusChoice);
		}
		
		@Override
		protected void onSubmit() {
			if (log.isDebugEnabled()) {
				log.debug("update app[appId=" + app.getId() + "] status to " + app.getStatus().getStatus());
			}
			applicationService.updateAsset(app);
		}
	}
}

// $Id$