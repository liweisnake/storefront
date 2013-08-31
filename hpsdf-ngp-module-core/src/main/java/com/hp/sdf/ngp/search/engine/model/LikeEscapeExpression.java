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

import org.hibernate.criterion.LikeExpression;
import org.hibernate.criterion.MatchMode;

public class LikeEscapeExpression extends LikeExpression {

	private String propertyName;

	private String value;

	private String escapeChar;

	public LikeEscapeExpression(String propertyName, String value,
			Character escapeChar, boolean ignoreCase) {
		super(propertyName, value, escapeChar, ignoreCase);
		this.propertyName = propertyName;
		this.value = value;
		this.escapeChar = escapeChar.toString();
	}

	public LikeEscapeExpression(String propertyName, String value,
			MatchMode matchMode, Character escapeChar, boolean ignoreCase) {
		super(propertyName, value, matchMode, escapeChar, ignoreCase);
		this.propertyName = propertyName;
		this.value = value;
		this.escapeChar = escapeChar.toString();
	}

	public LikeEscapeExpression(String propertyName, String value,
			MatchMode matchMode) {
		super(propertyName, value, matchMode);
		this.propertyName = propertyName;
		this.value = value;
	}

	public LikeEscapeExpression(String propertyName, String value) {
		super(propertyName, value);
		this.propertyName = propertyName;
		this.value = value;
	}

	public String toString() {
		return propertyName + " like " + value + " escape '" + escapeChar + "'";
	}

}

// $Id$