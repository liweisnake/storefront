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

public class LogicCondition extends BaseCondition {

	protected Condition left;

	protected Condition right;

	public enum ConnectionLogic {
		AND, OR, NOT
	}

	protected ConnectionLogic connectionLogic = ConnectionLogic.AND;

	private LogicCondition(Condition left, Condition right,
			ConnectionLogic connectionLogic) {
		if (right != null) {
			if ((left.getConditionType() == ConditionType.join && right
					.getConditionType() != ConditionType.eav)
					|| (right.getConditionType() == ConditionType.join && left
							.getConditionType() != ConditionType.eav)) {
				this.conditionType = ConditionType.join;
			}

			// if (left.getConditionType() != right.getConditionType()) {
			// throw new UnsupportedOperationException(
			// "Not support connect different type of condition. left condition type is: "
			// + left.getConditionType()
			// + ", right condition type is: "
			// + (right == null ? "null" : right
			// .getConditionType()));
			// }
		}
//		this.conditionType = left.getConditionType();
		this.left = left;
		this.right = right;
		this.connectionLogic = connectionLogic;
	}

	public static Condition or(Condition left, Condition right) {
		return new LogicCondition(left, right, ConnectionLogic.OR);
	}

	public boolean isOr() {
		return connectionLogic == ConnectionLogic.OR;
	}

	public static Condition and(Condition left, Condition right) {
		return new LogicCondition(left, right, ConnectionLogic.AND);
	}

	public boolean isAnd() {
		return connectionLogic == ConnectionLogic.AND;
	}

	public static BaseCondition not(BaseCondition con) {
		return new LogicCondition(con, null, ConnectionLogic.NOT);
	}

	public boolean isNot() {
		return connectionLogic == ConnectionLogic.NOT;
	}

	public Condition getLeft() {
		return left;
	}

	public void setLeft(BaseCondition left) {
		this.left = left;
	}

	public Condition getRight() {
		return right;
	}

	public void setRight(BaseCondition right) {
		this.right = right;
	}

	public ConnectionLogic getConnectionLogic() {
		return connectionLogic;
	}

	public void setConnectionLogic(ConnectionLogic connectionLogic) {
		this.connectionLogic = connectionLogic;
	}

	public String toString() {
		if (connectionLogic == ConnectionLogic.NOT) {
			return "(" + connectionLogic.toString() + " " + left + ")";
		} else {
			return "(" + left + " " + connectionLogic.toString() + " " + right
					+ ")";
		}
	}

}

// $Id$