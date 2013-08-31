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

import org.springframework.util.Assert;

import com.hp.sdf.ngp.api.search.ConditionDescriptor;
import com.hp.sdf.ngp.api.search.Condition.NumberComparer;

public abstract class NumberConditionDescriptor<T> implements
		ConditionDescriptor {

	private T value;

	private NumberComparer matchType;

	public T getValue() {
		return value;
	}

	public NumberComparer getMatchType() {
		return matchType;
	}

	public NumberConditionDescriptor(T value, NumberComparer matchType) {
		super();
		Assert.notNull(value);
		this.value = value;
		this.matchType = matchType;

	}

}

// $Id$