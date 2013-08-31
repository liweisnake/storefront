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
import com.hp.sdf.ngp.workflow.descriptor.WorkFlowDescriptor;

/**
 * This class is responsible for loading the work flow descriptor from somewhere
 * and performing necessary operations such as initial the database using loaded
 * work flow. It will provide the interface for others to retrieve the detailed
 * work flow information
 * 
 * 
 */
public interface WorkFlowRepository {

	/**
	 * Load the work flow descriptor and performing necessary operations such as
	 * initial the database using loaded work flow. After the loading, it will
	 * notify the work flow engine about the loaded information
	 * 
	 * @WorkFlowFailureException throw exception if loaded fails
	 */

	public void load() throws WorkFlowFailureException;
	
	/**
	 * Set the location to load the descriptor
	 * @param location
	 */
	public void setLocation(String location);

	public WorkFlowDescriptor getWorkFlowDescriptor();
	
	public void validateWorkFlow(String workFlowDescriptor) throws WorkFlowFailureException;

}

// $Id$