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

import com.hp.sdf.ngp.common.exception.ObjectXMLTransformException;
import com.hp.sdf.ngp.workflow.descriptor.WorkFlowDescriptor;

/**
 * 
 * Transform {$Link WorkFlowDescriptor} between object and XML format string
 * 
 */
public interface WorkFlowTransformer {

	/**
	 * Transform {$Link WorkFlowDescriptor} object to XML string
	 * @param workFlowDescriptor
	 * @return
	 * @throws ObjectXMLTransformException thrown when transform fails
	 */
	public String marshal(WorkFlowDescriptor workFlowDescriptor)
			throws ObjectXMLTransformException;

	/**
	 * Transform a XML format string to {$Link WorkFlowDescriptor} object
	 * @param xml  a string as XML format
	 * @return
	 * @throws ObjectXMLTransformException thrown when transform fails
	 */
	public WorkFlowDescriptor unmarshal(String xml)
			throws ObjectXMLTransformException;
}

// $Id$