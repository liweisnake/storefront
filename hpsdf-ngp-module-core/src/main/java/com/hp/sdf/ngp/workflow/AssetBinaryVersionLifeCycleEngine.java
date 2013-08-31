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

import java.util.List;

import com.hp.sdf.ngp.common.exception.WorkFlowFailureException;
import com.hp.sdf.ngp.model.Status;

public interface AssetBinaryVersionLifeCycleEngine extends WorkFlowEngine {

	/**
	 * Get the privilege string for a particular type for a app binary id 
	 * @param accessType
	 * @return
	 * @throws WorkFlowFailureException
	 */
	public String[] getAccessPrivilege(long binaryId,AccessType accessType)throws WorkFlowFailureException;


	/**
	 * Get the initialized application status, for example, 'uploaded'
	 * @return
	 */
	public Status getStartupStatus();
	
	
	/**
	 * 
	 * @return
	 */
	public List<Status> getDefinedStatus();
	
	
}

// $Id$