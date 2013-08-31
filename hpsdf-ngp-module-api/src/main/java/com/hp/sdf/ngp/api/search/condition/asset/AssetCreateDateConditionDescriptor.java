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
package com.hp.sdf.ngp.api.search.condition.asset;

import java.util.Date;

import com.hp.sdf.ngp.api.search.Condition.DateComparer;
import com.hp.sdf.ngp.api.search.condition.basecondition.DateConditionDescriptor;

public class AssetCreateDateConditionDescriptor extends DateConditionDescriptor {

	public AssetCreateDateConditionDescriptor(Date value, DateComparer matchType) {
		super(value, matchType);
		// TODO Auto-generated constructor stub
	}

	

	
	
}

// $Id$