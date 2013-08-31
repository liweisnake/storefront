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
package com.hp.sdf.ngp.api.search.condition.basecondition;

import com.hp.sdf.ngp.api.search.ConditionDescriptor;
import com.hp.sdf.ngp.api.search.Condition.StringComparer;

/**
 * Condition of string type values. This condition supports Wildcard. "*" can be
 * added to key words string, and you must set isWildCard to true. "*" can be
 * added before or after key words.
 * 
 * 
 */
public abstract class StringConditionDescriptor implements ConditionDescriptor {

	private String value = "";

	private StringComparer matchType;

	private boolean ignorecase;

	private boolean isWildCard;

	public String getValue() {
		return value;
	}

	public StringComparer getMatchType() {
		return matchType;
	}

	public boolean isIgnorecase() {
		return ignorecase;
	}

	public boolean isWildCard() {
		return isWildCard;
	}

	public StringConditionDescriptor(String value, StringComparer matchType,
			boolean ignorecase, boolean isWildCard) {
		if (value != null)
			this.value = value;
		this.matchType = matchType;
		this.ignorecase = ignorecase;
		this.isWildCard = isWildCard;
	}
}

// $Id$