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
package com.hp.sdf.ngp.banner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public enum BannerType {
	
	staticBanner {
		public String toString() {
			return "staticBanner";
		}
	},
	rotatingBanner {
		public String toString() {
			return "rotatingBanner";
		}
	},
	tabbedBanner {
		public String toString() {
			return "tabbedBanner";
		}
	};

	public static List<Integer> getBannerTypeIdList() {
		List<Integer> list = new ArrayList<Integer>();
		list.add(BannerType.staticBanner.ordinal());
		list.add(BannerType.rotatingBanner.ordinal());
		list.add(BannerType.tabbedBanner.ordinal());
		return list;
	}

	public static Map<Integer, String> getBannerTypeMap() {
		Map<Integer, String> map = new HashMap<Integer, String>();
		map.put(BannerType.staticBanner.ordinal(), BannerType.staticBanner.toString());
		map.put(BannerType.rotatingBanner.ordinal(), BannerType.rotatingBanner.toString());
		map.put(BannerType.tabbedBanner.ordinal(), BannerType.tabbedBanner.toString());
		return map;
	}
	
	public static Map<BannerType, String> getBannerTypeMap2() {
		Map<BannerType, String> map = new HashMap<BannerType, String>();
		map.put(BannerType.staticBanner, BannerType.staticBanner.toString());
		map.put(BannerType.rotatingBanner, BannerType.rotatingBanner.toString());
		map.put(BannerType.tabbedBanner, BannerType.tabbedBanner.toString());
		return map;
	}
	
	public static BannerType getEnumNameStrByOrdinal(int ordinal) {
		try {
			BannerType[] enumConstants = BannerType.class.getEnumConstants();

			for (BannerType t : enumConstants) {
				if (t.ordinal() == ordinal)
					return t;
			}
		} catch (Exception e) {

		}
		return null;
	}
}

// $Id$