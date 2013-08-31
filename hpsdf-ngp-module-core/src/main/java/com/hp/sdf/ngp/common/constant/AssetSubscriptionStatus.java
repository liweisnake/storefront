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
package com.hp.sdf.ngp.common.constant;

import java.util.ArrayList;
import java.util.List;

public enum AssetSubscriptionStatus {

	// Status:
	// Shopping cart
	INCART("incart"),
	
	// Purchased Successfully
	PURCHASED("purchased"),
	
	// Purchased failed
	FAILED("failed"),
	
	// Gifting
	GIFT("gift");
	
	private String name;
	
	@Override
	public String toString() {
		return name;
	}

	private AssetSubscriptionStatus(String name) {
		this.name = name;
	}
	public static AssetSubscriptionStatus convert(String name) {
		if (name == null) {
			return null;
		}
		AssetSubscriptionStatus[] members = values();
		for (AssetSubscriptionStatus member : members) {
			if (name.equalsIgnoreCase(member.toString())) {
				return member;
			}
		}
		return null;

	}
	public static List<String> listType() {
		List<String> list = new ArrayList<String>();
		AssetSubscriptionStatus[] members = values();
		for (AssetSubscriptionStatus member : members) {
			list.add(member.name);
		}
		return list;

	}
}

// $Id$