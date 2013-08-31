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
package com.hp.sdf.ngp.api.model;

import java.util.List;
import java.util.Map;

/**
 * Operates extended attributes.
 * The extended attributes can only be one of Date, Float and String.
 * Invoker should know the type by attribute name or use "instanceof" 
 * to test the value type.
 *
 */
public interface AttributeContainer {

	public List<Object> getAttributeValue(String attributeName);

	public Map<String, List<Object>> getAttributes();
	
}

// $Id$