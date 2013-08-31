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

public enum ContentType {
	
	image{
		public String toString() {
			return "image";
		}
	},
	html{
		public String toString() {
			return "html";
		}
	};
	
	public static List<Integer> getContentTypeIdList() {
		List<Integer> list = new ArrayList<Integer>();
		list.add(ContentType.image.ordinal());
		list.add(ContentType.html.ordinal());
		return list;
	}

	public static Map<Integer, String> getContentTypeMap() {
		Map<Integer, String> map = new HashMap<Integer, String>();
		map.put(ContentType.image.ordinal(), ContentType.image.toString());
		map.put(ContentType.html.ordinal(), ContentType.html.toString());
		return map;
	}
	
	public static ContentType getEnumNameStrByOrdinal(int ordinal) {
		try {
			ContentType[] enumConstants = ContentType.class.getEnumConstants();

			for (ContentType t : enumConstants) {
				if (t.ordinal() == ordinal)
					return t;
			}
		} catch (Exception e) {

		}
		return null;
	}
}

// $Id$