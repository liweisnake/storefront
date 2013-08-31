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

import com.hp.sdf.ngp.common.util.Utils;

public class StringPropertyCondition extends ObjectPropertyCondition {

	protected boolean ignorecase = false;

	protected boolean isWildCard = false;

	public StringPropertyCondition() {
		super();
	}

	public StringPropertyCondition(String propertyName, String value, StringComparer matchType, boolean ignorecase, boolean isWildCard) {
		super();
		if (StringUtils.isBlank(propertyName)) {
			throw new IllegalArgumentException("Property name can not be null or \"\"");
		}
		this.propertyName = propertyName;
		this.ignorecase = ignorecase;
		this.isWildCard = isWildCard;
		if (isWildCard && value != null) {
			value = value.replace("%", WildCard.escapeChar.toString() + "%").replace("_", WildCard.escapeChar.toString() + "_");
			value = Utils.escapeSqlWildCard(value);
			//value=value.replace( WildCard.more.toString(), "%").replace(WildCard.one.toString(), "_");
			condition = new LikePropertyCompareCondition(propertyName, value, null, ignorecase);
		} else {			
			setConditionByRestriction(value, matchType);
		}
	}

	public boolean isIgnorecase() {
		return ignorecase;
	}

	public void setIgnorecase(boolean ignorecase) {
		this.ignorecase = ignorecase;
	}

	protected void setConditionByRestriction(String value, StringComparer matchType) {
		if (value == null) {
			throw new IllegalArgumentException("Parameter value can not be null.");
		}
		if (matchType == StringComparer.NULL) {
			condition = new NullPropertyCondition(propertyName, true);
		} else if (matchType == StringComparer.NOT_NULL) {
			condition = new NullPropertyCondition(propertyName, false);
		} else {
			if (matchType == StringComparer.EQUAL) {
				condition = new SimplePropertyCompareCondition(propertyName, value, ignorecase, MatchType.EQUAL);
			} else if (matchType == StringComparer.NOT_EQUAL) {
				condition = new SimplePropertyCompareCondition(propertyName, value, ignorecase, MatchType.NOT_EQUAL);
			} else if (matchType == StringComparer.LIKE) {
				value = value.replace("%", WildCard.escapeChar.toString() + "%").replace("_", WildCard.escapeChar.toString() + "_");
				//value=value.replace( WildCard.more.toString(), "%").replace(WildCard.one.toString(), "_");
				condition = new LikePropertyCompareCondition(propertyName, value, isWildCard ? null : LikeMatchMode.ANYWHERE, ignorecase);
			}
		}
	}

}

// $Id$