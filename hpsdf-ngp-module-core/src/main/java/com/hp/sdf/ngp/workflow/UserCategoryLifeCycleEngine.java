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
package com.hp.sdf.ngp.workflow;

import com.hp.sdf.ngp.common.exception.WorkFlowFailureException;
import com.hp.sdf.ngp.model.RoleCategory;

/**
 * This engine is used to retrieve the detailed information from work flow
 * descriptor, which would be related with back-end business logic
 * 
 * 
 */
public interface UserCategoryLifeCycleEngine extends WorkFlowEngine {

	/**
	 * Get his privileges of this user profile, which should include the
	 * definition in user category part and application life cycle part
	 * 
	 * @param userProfile
	 * @return his privileges
	 */
	public String[] getUserPrivileges(String usrId)
			throws WorkFlowFailureException;

	/**
	 * Get the attached attribute value for a user category
	 * 
	 * @param usrCategoryName
	 * @param attributeName
	 * @return null if this attribute doesn't exist
	 * @throws WorkFlowFailureException
	 */
	public String getAttributeValue(String usrCategoryName, String attributeName)
			throws WorkFlowFailureException;

	
	/**
	 * Get the initialized RoleCategory, for example, 'User'
	 * @return
	 */
	public RoleCategory getStartupRoleCategory();
	

	

	

}

// $Id$