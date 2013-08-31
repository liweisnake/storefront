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
package com.hp.sdf.ngp.ui.page.myportal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Gender {

	enum GenderType {
		MALE {
			public String toString() {
				return "male";
			}
		},

		FEMALE {
			public String toString() {
				return "female";
			}
		},

		UNSPECIFY {
			public String toString() {
				return "unspecify";
			}
		}

	}

	public static List<Integer> getGenderIdList() {
		List<Integer> list = new ArrayList<Integer>();
		list.add(GenderType.MALE.ordinal());
		list.add(GenderType.FEMALE.ordinal());
//		list.add(GenderType.UNSPECIFY.ordinal());
		return list;
	}

	public static Map<Integer, String> getGenderMap() {
		Map<Integer, String> map = new HashMap<Integer, String>();
		map.put(GenderType.MALE.ordinal(), GenderType.MALE.toString());
		map.put(GenderType.FEMALE.ordinal(), GenderType.FEMALE.toString());
//		map
//				.put(GenderType.UNSPECIFY.ordinal(), GenderType.UNSPECIFY
//						.toString());
		return map;
	}
}

// $Id$