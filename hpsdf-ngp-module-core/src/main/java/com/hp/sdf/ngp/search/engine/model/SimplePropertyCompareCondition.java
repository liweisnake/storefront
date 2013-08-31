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

import org.hibernate.criterion.Restrictions;

import com.hp.sdf.ngp.search.engine.model.BaseCondition.WildCard;

public class SimplePropertyCompareCondition extends PropertyCondition {

	public SimplePropertyCompareCondition(String propertyName, Object value,
			MatchType matchType) {
		if (matchType == MatchType.EQUAL) {
			criterion = Restrictions.eq(propertyName, value);
		} else if (matchType == MatchType.NOT_EQUAL) {
			criterion = Restrictions.ne(propertyName, value);
		} else if (matchType == MatchType.GREATER_EQUAL) {
			criterion = Restrictions.ge(propertyName, value);
		} else if (matchType == MatchType.GREATER_THAN) {
			criterion = Restrictions.gt(propertyName, value);
		} else if (matchType == MatchType.LESS_EQUAL) {
			criterion = Restrictions.le(propertyName, value);
		} else if (matchType == MatchType.LESS_THAN) {
			criterion = Restrictions.lt(propertyName, value);
		} else if (matchType == MatchType.LIKE) {
			criterion = Restrictions.like(propertyName, value);
		}
	}

	public SimplePropertyCompareCondition(String propertyName, Object value,
			boolean ignoreCase, MatchType matchType) {
		if (matchType == MatchType.EQUAL) {
			criterion = new IgnoreCaseExpression(propertyName, value, "=",
					ignoreCase);
		} else if (matchType == MatchType.NOT_EQUAL) {
			criterion = new IgnoreCaseExpression(propertyName, value, "<>",
					ignoreCase);
		} else if (matchType == MatchType.LIKE) {
			criterion = new LikeEscapeExpression(propertyName,
					value.toString(), WildCard.escapeChar.toString().charAt(0), ignoreCase);
		} else if (matchType == MatchType.GREATER_EQUAL) {
			criterion = Restrictions.ge(propertyName, value);
		} else if (matchType == MatchType.GREATER_THAN) {
			criterion = Restrictions.gt(propertyName, value);
		} else if (matchType == MatchType.LESS_EQUAL) {
			criterion = Restrictions.le(propertyName, value);
		} else if (matchType == MatchType.LESS_THAN) {
			criterion = Restrictions.lt(propertyName, value);
		}
	}

}

// $Id$