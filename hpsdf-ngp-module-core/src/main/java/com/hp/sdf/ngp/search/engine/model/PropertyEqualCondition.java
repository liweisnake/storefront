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

import com.hp.sdf.ngp.common.util.Utils;

/**
 * 
 * new PropertyEqualJoinCondition("id", "asset.currentVersion");
 * 
 */
public class PropertyEqualCondition extends PropertyCondition {

	private String firstProperty;

	private String secondProperty;

	private boolean isNeedJoin;

	public PropertyEqualCondition(String firstProperty, String secondProperty) {
		this(firstProperty, secondProperty, true);
	}

	public PropertyEqualCondition(String firstProperty, String secondProperty,
			boolean isNeedJoin) {
		this.firstProperty = firstProperty;
		this.secondProperty = secondProperty;
		this.isNeedJoin = isNeedJoin;
		if (isNeedJoin) {
			criterion = Restrictions.eqProperty(Utils.substringAfterLast2(
					firstProperty, "."), Utils.substringAfterLast2(
					secondProperty, "."));
		} else {
			criterion = Restrictions.eqProperty(firstProperty, secondProperty);
		}
	}

	public String getFirstProperty() {
		return firstProperty;
	}

	public String getSecondProperty() {
		return secondProperty;
	}

	public boolean isNeedJoin() {
		return isNeedJoin;
	}

}

// $Id$