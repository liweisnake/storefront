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
package com.hp.sdf.ngp.search.engine.model;

import org.apache.commons.lang.StringUtils;


public class NumberPropertyCondition extends ObjectPropertyCondition {

	

	public NumberPropertyCondition() {
		super();
	}

	public NumberPropertyCondition(String propertyName, Object value,
			NumberComparer matchType) {
		super();
		if (StringUtils.isBlank(propertyName)) {
			throw new IllegalArgumentException(
					"Property name can not be null or \"\".");
		}
		this.propertyName = propertyName;
		setConditionByRestriction(value, matchType);
	}

	protected void setConditionByRestriction(Object value,
			NumberComparer matchType) {
		if (matchType == NumberComparer.NULL) {
			condition = new NullPropertyCondition(propertyName, true);
		} else if (matchType == NumberComparer.NOT_NULL) {
			condition = new NullPropertyCondition(propertyName, false);
		} else {
			if (value == null) {
				throw new IllegalArgumentException(
						"Parameter value can not be null.");
			}
			if (matchType == NumberComparer.EQUAL) {
				condition = new SimplePropertyCompareCondition(propertyName,
						value, MatchType.EQUAL);
			} else if (matchType == NumberComparer.NOT_EQUAL) {
				condition = new SimplePropertyCompareCondition(propertyName,
						value, MatchType.NOT_EQUAL);
			} else if (matchType == NumberComparer.GREAT_THAN) {
				condition = new SimplePropertyCompareCondition(propertyName,
						value, MatchType.GREATER_THAN);
			} else if (matchType == NumberComparer.GREAT_EQUAL) {
				condition = new SimplePropertyCompareCondition(propertyName,
						value, MatchType.GREATER_EQUAL);
			} else if (matchType == NumberComparer.LESS_THAN) {
				condition = new SimplePropertyCompareCondition(propertyName,
						value, MatchType.LESS_THAN);
			} else if (matchType == NumberComparer.LESS_EQUAL) {
				condition = new SimplePropertyCompareCondition(propertyName,
						value, MatchType.LESS_EQUAL);
			}
		}

	}

}

// $Id$