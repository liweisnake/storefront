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


public class DatePropertyCondition extends ObjectPropertyCondition {

	

	public DatePropertyCondition() {
		super();
	}

	public DatePropertyCondition(String propertyName, Object value,
			DateComparer matchType) {
		super();
		if (StringUtils.isBlank(propertyName)) {
			throw new IllegalArgumentException(
					"Property name can not be null or \"\".");
		}
		this.propertyName = propertyName;
		setConditionByRestriction(value, matchType);
	}

	

	protected void setConditionByRestriction(Object value,
			DateComparer matchType) {
		if (matchType == DateComparer.NULL) {
			condition = new NullPropertyCondition(propertyName, true);
		} else if (matchType == DateComparer.NOT_NULL) {
			condition = new NullPropertyCondition(propertyName, false);
		} else {
			if (value == null) {
				throw new IllegalArgumentException(
						"Parameter value can not be null.");
			}
			if (matchType == DateComparer.GREAT_THAN) {
				condition = new SimplePropertyCompareCondition(propertyName,
						value, MatchType.GREATER_THAN);
			} else if (matchType == DateComparer.LESS_THAN) {
				condition = new SimplePropertyCompareCondition(propertyName,
						value, MatchType.LESS_THAN);
			}
		}

	}

}

// $Id$