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

import com.hp.sdf.ngp.api.search.Condition;
import com.hp.sdf.ngp.search.engine.model.JoinMetaData.JoinPurpose;

public abstract class BaseCondition implements Condition {

	protected JoinPurpose purpose = JoinPurpose.COMMON;

	public enum WildCard {
		more {
			public String toString() {
				return "*";
			}
		},
		one {
			public String toString() {
				return "?";
			}
		},
		escapeChar {
			public String toString() {
				return "$";
			}
		}
	}

	protected ConditionType conditionType = ConditionType.common;

	public boolean isType(ConditionType type) {
		return conditionType == type;
	}

	public ConditionType getConditionType() {
		return conditionType;
	}

	public void setConditionType(ConditionType conditionType) {
		this.conditionType = conditionType;
	}

	public Condition or(Condition right) {
		return LogicCondition.or(this, right);
	}

	public Condition and(Condition right) {
		return LogicCondition.and(this, right);
	}

	public Condition not() {
		return LogicCondition.not(this);
	}

	public JoinPurpose getJoinPurpose() {
		return purpose;
	}

	public void setJoinPurpose(JoinPurpose joinPurpose) {
		this.purpose = joinPurpose;
	}

}

// $Id$