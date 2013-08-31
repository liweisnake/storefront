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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.hp.sdf.ngp.common.exception.WorkFlowFailureException;
import com.hp.sdf.ngp.service.ApplicationService;
import com.hp.sdf.ngp.service.UserService;

public abstract class BaseDescriptor implements ApplicationContextAware
		 {

	protected abstract BaseDescriptor[] visitArray();

	protected final BaseDescriptor[] merge(BaseDescriptor[]... baseDescriptors) {
		List<BaseDescriptor> list = new ArrayList<BaseDescriptor>();

		for (int i = 0, j = baseDescriptors.length; i < j; i++) {
			BaseDescriptor[] value = baseDescriptors[i];
			if (value != null) {
				list.addAll(Arrays.asList(value));
			}
		}
		return list.toArray(new BaseDescriptor[0]);

	}

	protected void onSetApplicationContext(ApplicationContext applicationContext) {
		// Do nothing
		// May be overrided by sub class;
	}

	protected void onValidate() throws WorkFlowFailureException {
		// Do nothing
		// May be overrided by sub class;
	}

	protected void onInit() throws WorkFlowFailureException {
		// Do nothing
		// May be overrided by sub class;
	}

	protected void onSetApplicationService(ApplicationService applicationService) {
		// Do nothing
		// May be overrided by sub class;
	}

	protected void onSetUserService(UserService userService) {
		// Do nothing
		// May be overrided by sub class;
	}

	public final void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		this.onSetApplicationContext(applicationContext);
		BaseDescriptor[] visitors = visitArray();
		if (visitors != null) {
			for (BaseDescriptor visitor : visitors) {
				visitor.setApplicationContext(applicationContext);
			}
		}

	}

	public final void validate() throws WorkFlowFailureException {
		this.onValidate();
		BaseDescriptor[] visitors = visitArray();
		if (visitors != null) {
			for (BaseDescriptor visitor : visitors) {
				visitor.validate();
			}
		}

	}

	public final void init() throws WorkFlowFailureException {
		this.onInit();
		BaseDescriptor[] visitors = visitArray();
		if (visitors != null) {
			for (BaseDescriptor visitor : visitors) {
				visitor.init();
			}
		}
	}

	public final void setApplicationService(ApplicationService applicationService) {
		this.onSetApplicationService(applicationService);

		BaseDescriptor[] visitors = visitArray();
		if (visitors != null) {
			for (BaseDescriptor visitor : visitors) {
				visitor.setApplicationService(applicationService);
			}
		}

	}

	public final void setUserService(UserService userService) {

		this.onSetUserService(userService);

		BaseDescriptor[] visitors = visitArray();
		if (visitors != null) {
			for (BaseDescriptor visitor : visitors) {
				visitor.setUserService(userService);
			}
		}

	}

}

// $Id$