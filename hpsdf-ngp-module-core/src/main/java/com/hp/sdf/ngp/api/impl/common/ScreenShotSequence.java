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
package com.hp.sdf.ngp.api.impl.common;

import java.util.Comparator;

import com.hp.sdf.ngp.api.model.Screenshot;

public class ScreenShotSequence implements Comparator {

	public int compare(Object o1, Object o2) {
		if (o1 instanceof Screenshot && o2 instanceof Screenshot) {
			return (int) (((Screenshot) o1).getSequence() - ((Screenshot) o2).getSequence());
		}
		return 0;
	}

}

// $Id$