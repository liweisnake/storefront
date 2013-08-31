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
package com.hp.sdf.ngp.search.condition.handsetdevice;

import com.hp.sdf.ngp.search.engine.model.NumberPropertyCondition;

public class HandSetDeviceResolutionFilterCondition extends NumberPropertyCondition {

	private static final long serialVersionUID = 9054604368036107359L;

	public HandSetDeviceResolutionFilterCondition(Long itemId,
			NumberComparer matchType) {
		super("resolutionFilter", itemId, matchType);
	}
}

// $Id$