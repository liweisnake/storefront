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

import java.io.Serializable;

public class JoinMetaData implements Serializable {

	private static final long serialVersionUID = 510068785583066049L;

	public enum JoinType {
		FULL, LEFTOUT, INNER
	}

	public enum JoinPurpose {
		COMMON, ORDER, EAV, EAV_ORDER, ALL
	}

	private JoinType type = JoinType.LEFTOUT;

	private JoinPurpose purpose = JoinPurpose.COMMON;

	
	
	public JoinMetaData() {
	}

	public JoinMetaData(JoinPurpose purpose) {
		this.purpose = purpose;
	}

	public JoinPurpose getPurpose() {
		return purpose;
	}

	public void setPurpose(JoinPurpose purpose) {
		this.purpose = purpose;
	}

	public JoinType getType() {
		return type;
	}

	public void setType(JoinType type) {
		this.type = type;
	}

	

}

// $Id$