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
package com.hp.sdf.ngp.eav;

public class Condition {
	
	private Long attributeID;
	
	private String attributeName;
	
	private Object value;
	
	private SqlComparer comparer;
	
	private boolean isLeaf;
	
	private Condition leftCondition;
	
	private SqlConnector connector;
	
	private Condition rightCondition;
	
	@SuppressWarnings("unused")
	private Condition(){
		
	}
	
	public Condition(Long attributeID, SqlComparer comparer, Object value){
		this.attributeID = attributeID;
		this.comparer = comparer;
		this.value = value;
		this.setLeaf(true);
	}
	
	public Condition(String attributeName, SqlComparer comparer, Object value){
		this.attributeName = attributeName;
		this.comparer = comparer;
		this.value = value;
		this.setLeaf(true);
	}
	
	public Condition(Condition leftCondition, SqlConnector connector, Condition rightCondition){
		this.setLeaf(false);
		this.leftCondition = leftCondition;
		this.rightCondition = rightCondition;
		this.connector = connector;
	}

	public Long getAttributeID() {
		return attributeID;
	}

	public void setAttributeID(Long attributeID) {
		this.attributeID = attributeID;
	}

	public String getAttributeName() {
		return attributeName;
	}

	public void setAttributeName(String attributeName) {
		this.attributeName = attributeName;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	public SqlComparer getComparer() {
		return comparer;
	}

	public void setComparer(SqlComparer comparer) {
		this.comparer = comparer;
	}

	public boolean isLeaf() {
		return isLeaf;
	}

	public void setLeaf(boolean isLeaf) {
		this.isLeaf = isLeaf;
	}

	public Condition getLeftCondition() {
		return leftCondition;
	}

	public void setLeftCondition(Condition leftCondition) {
		this.leftCondition = leftCondition;
	}

	public Condition getRightCondition() {
		return rightCondition;
	}

	public void setRightCondition(Condition rightCondition) {
		this.rightCondition = rightCondition;
	}

	public SqlConnector getConnector() {
		return connector;
	}

	public void setConnector(SqlConnector connector) {
		this.connector = connector;
	}

}

// $Id$