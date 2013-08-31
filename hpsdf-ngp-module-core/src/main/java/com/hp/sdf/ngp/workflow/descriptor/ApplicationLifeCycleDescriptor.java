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

import com.hp.sdf.ngp.common.exception.WorkFlowFailureException;

public class ApplicationLifeCycleDescriptor extends BaseDescriptor {
	

	private String name;
	private String display;
	private String description;
	private AccessDescriptor[] accesses;


	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDisplay() {
		return display;
	}

	public void setDisplay(String display) {
		this.display = display;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public AccessDescriptor[] getAccesses() {
		return accesses;
	}

	public void setAccesss(AccessDescriptor[] accesses) {
		this.accesses = accesses;
	}

	


	@Override
	protected BaseDescriptor[] visitArray() {

		return this.merge(accesses);
	}

	@Override
	protected void onInit() throws WorkFlowFailureException {

		//do nothing till now
	}
}

// $Id$