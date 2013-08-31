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
package com.hp.sdf.ngp.workflow.descriptor;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.hp.sdf.ngp.common.exception.WorkFlowFailureException;
import com.hp.sdf.ngp.model.RoleCategory;
import com.hp.sdf.ngp.service.UserService;
import com.hp.sdf.ngp.workflow.AccessType;

public class AccessDescriptor extends BaseDescriptor {

	private final static Log log = LogFactory.getLog(AccessDescriptor.class);

	public static final String OWNER = "owner";

	private UserService userService = null;

	private String operation;

	private String[] allowed;

	public String getOperation() {
		return operation;
	}

	public void setOperation(String operation) {
		this.operation = operation;
	}

	public String[] getAllowed() {
		return allowed;
	}

	public void setAllowed(String[] allowed) {
		this.allowed = allowed;
	}

	@Override
	protected BaseDescriptor[] visitArray() {
		return null;
	}

	@Override
	protected void onValidate() throws WorkFlowFailureException {
		// Check the operation value
		if (AccessType.convert(this.operation) == null) {
			throw new WorkFlowFailureException(
					"The operation value in 'access' is illegal, which is not defined in system");
		}
		if (allowed != null) {
			for (String allow : allowed) {
				if (allow.equalsIgnoreCase(OWNER)) {
					// "owner" is a default value representing the application
					// creator
					continue;
				}
				try {
					RoleCategory roleCategory = this.userService
							.getRoleByName(allow);
					if (roleCategory == null) {
						log
								.warn("Can't get the corresponding role category definition in current backend system for ["
										+ allow
										+ "], it may wrong, or this role category will be created soon");
					}
				} catch (Throwable e) {
					log
							.warn("Can't get the corresponding role category definition in current backend system for ["
									+ allow
									+ "], it may wrong, or this role category will be created soon");
				}
			}
		}
	}

	@Override
	protected void onSetUserService(UserService userService) {
		this.userService = userService;
	}

}

// $Id$