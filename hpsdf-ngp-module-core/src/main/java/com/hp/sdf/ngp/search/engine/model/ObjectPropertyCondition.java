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

public class ObjectPropertyCondition extends BaseCondition {

	protected BaseCondition condition;

	protected String propertyName;

	public String getPropertyName() {
		return propertyName;
	}

	public void setPropertyName(String propertyName) {
		this.propertyName = propertyName;
	}

	public BaseCondition getCondition() {
		return condition;
	}

	public void setCondition(BaseCondition condition) {
		this.condition = condition;
	}

	public String toString() {
		return this.getClass().getSimpleName();
	}

	protected ObjectPropertyCondition() {
	}

	protected ObjectPropertyCondition(BaseCondition condition) {
		this.condition = condition;
	}

	protected ObjectPropertyCondition(BaseCondition condition, String propertyName) {
		this.condition = condition;
		this.propertyName = propertyName;
	}
}

// $Id$