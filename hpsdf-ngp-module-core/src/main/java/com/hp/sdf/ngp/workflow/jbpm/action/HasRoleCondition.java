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
package com.hp.sdf.ngp.workflow.jbpm.action;

import javax.annotation.PostConstruct;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jbpm.api.activity.ActivityExecution;
import org.jbpm.api.jpdl.DecisionHandler;
import org.jbpm.api.model.OpenExecution;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.hp.sdf.ngp.workflow.jbpm.BaseAction;

/**
 * check userRole contains specific role
 * 
 * @param checkRole
 * 
 */
@Component
@Scope("prototype")
public class HasRoleCondition extends BaseAction implements DecisionHandler {

	private static final long serialVersionUID = 1L;
	private static final Log log = LogFactory.getLog(HasRoleCondition.class);
	private String checkRole = "";

	public String getCheckRole() {
		return checkRole;
	}

	public void setCheckRole(String checkRole) {
		this.checkRole = checkRole;
	}

	public void execute(ActivityExecution execution) throws Exception {
	}

	@PostConstruct
	@Override
	protected void setDesc() {
		this.desc = "checkRole";
	}

	public String decide(OpenExecution execution) {
		log.debug("called by:" + execution.getId());

		if (execution.getVariable("userRoles") == null) {
			log.error("null userRoles,end this processInstance:"
					+ execution.getId());
		}

		// scenario: role as super_developer do not need test
		if (!checkRole.equals("")) {
			if ((null != execution.getVariable("userRoles"))) {
				String userRoles = execution.getVariable("userRoles")
						.toString();
				if (userRoles.indexOf(checkRole) > -1)
					return "yes";
				else
					return "no";
			} else
				return "no";
		} else {
			if ((null == execution.getVariable("userRoles"))) {
				return "no";
			} else
				return "yes";
		}

	}
}

// $Id$