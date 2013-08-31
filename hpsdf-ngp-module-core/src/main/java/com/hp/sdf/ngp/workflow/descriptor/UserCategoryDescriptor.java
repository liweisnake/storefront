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

import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.hp.sdf.ngp.common.exception.WorkFlowFailureException;
import com.hp.sdf.ngp.model.RoleCategory;
import com.hp.sdf.ngp.service.UserService;

public class UserCategoryDescriptor extends BaseDescriptor {

	private final static Log log = LogFactory
			.getLog(UserCategoryDescriptor.class);

	private String name;
	private String display;

	private String description;
	private AttributeDescriptor[] attributes;
	private String[] privileges;


	private UserService userService = null;

	public String getDisplay() {
		return display;
	}

	public void setDisplay(String display) {
		this.display = display;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public AttributeDescriptor[] getAttributes() {
		return attributes;
	}

	public void setAttributes(AttributeDescriptor[] attributes) {
		this.attributes = attributes;
	}

	public String[] getPrivileges() {
		return privileges;
	}

	public void setPrivileges(String[] privileges) {
		this.privileges = privileges;
	}


	@Override
	protected void onInit() throws WorkFlowFailureException {
		// TODOListert the user role into database
		RoleCategory roleCategory = null;
		try {
			roleCategory = userService.getRoleByName(name);
		} catch (Throwable e) {
			log.warn(e);
			return;
		}
		if (roleCategory != null) {
			log.info("Skipped the role[" + name
					+ "] creation since it has existed in the backend system");
			return;
		}
		log.debug("try to create role["+name+"]");
		roleCategory = new RoleCategory();
		roleCategory.setRoleName(name);
		roleCategory.setDisplayName(display);
		roleCategory.setDescription(display);
		roleCategory.setCreateDate(new Date());
		userService.saveRole(roleCategory);

	}

	@Override
	protected void onSetUserService(UserService userService) {
		this.userService = userService;
	}

	@Override
	protected BaseDescriptor[] visitArray() {

		return this.merge(attributes);
	}

}

// $Id$