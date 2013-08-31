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

public class WorkFlowDescriptor extends BaseDescriptor{

	private UserCategoryDescriptor[] userCategories;
	private AssetBinaryVersionLifeCycleDescriptor[] applicationBinaryLifeCycles;
	
	private ApplicationLifeCycleDescriptor[] applicationLifeCycles;

	public ApplicationLifeCycleDescriptor[] getApplicationLifeCycles() {
		return applicationLifeCycles;
	}

	public void setApplicationLifeCycles(
			ApplicationLifeCycleDescriptor[] applicationLifeCycles) {
		this.applicationLifeCycles = applicationLifeCycles;
	}

	public AssetBinaryVersionLifeCycleDescriptor[] getApplicationBinaryLifeCycles() {
		return applicationBinaryLifeCycles;
	}

	public void setApplicationBinaryLifeCycles(
			AssetBinaryVersionLifeCycleDescriptor[] applicationBinaryLifeCycles) {
		this.applicationBinaryLifeCycles = applicationBinaryLifeCycles;
	}

	public UserCategoryDescriptor[] getUserCategoryDescriptors() {
		return userCategories;
	}

	public void setUserCategoryDescriptors(
			UserCategoryDescriptor[] userCategories) {
		this.userCategories = userCategories;
	}

	

	@Override
	protected void onValidate() throws WorkFlowFailureException {
		
		if (userCategories == null) {
			throw new WorkFlowFailureException(
					"There is no user category definition in work flow descriptor");
		}
		
		if (applicationLifeCycles == null) {
			throw new WorkFlowFailureException(
					"There is no application lifecycle definition in work flow descriptor");
		}
		
		if (applicationBinaryLifeCycles == null) {
			throw new WorkFlowFailureException(
					"There is no application binary lifecycle definition in work flow descriptor");
		}
		
	}	

	@Override
	protected BaseDescriptor[] visitArray() {
		return this.merge(userCategories,applicationLifeCycles,applicationBinaryLifeCycles);
	}

}

// $Id$