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
package com.hp.sdf.ngp.api.search;

public interface Condition extends java.io.Serializable {

	public enum DateComparer {
		GREAT_THAN, LESS_THAN, NULL, NOT_NULL
	}

	public enum NumberComparer {
		EQUAL, NOT_EQUAL, GREAT_THAN, GREAT_EQUAL, LESS_THAN, LESS_EQUAL, NULL, NOT_NULL
	}

	public enum StringComparer {
		EQUAL, NOT_EQUAL, LIKE, NULL, NOT_NULL
	}
	
	public enum ConditionType {
		common, join, eav
	}

	public Condition or(Condition right);

	public Condition and(Condition right);

	public Condition not();

	public ConditionType getConditionType();
}

// $Id$