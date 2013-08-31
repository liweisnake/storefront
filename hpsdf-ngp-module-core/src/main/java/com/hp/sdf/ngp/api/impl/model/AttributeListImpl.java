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
package com.hp.sdf.ngp.api.impl.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.hp.sdf.ngp.api.model.AttributeList;

public class AttributeListImpl implements AttributeList {

	private Map<String, List<Object>> objects = new HashMap<String, List<Object>>();

	public void addAttribute(String attributeName, String value) {
		this.putValue(attributeName, value);
	}

	public void addAttribute(String attributeName, Float value) {
		this.putValue(attributeName, value);
	}

	public void addAttribute(String attributeName, Date value) {
		this.putValue(attributeName, value);
	}

	public void removeAttribute(String attributeName) {
		objects.remove(attributeName);
	}

	public List<Object> getAttributeValue(String attributeName) {
		if (null != objects) {
			return objects.get(attributeName);
		}
		return null;
	}

	public Map<String, List<Object>> getAttributes() {
		return objects;
	}

	private void putValue(String attributeName, Object object) {
		List<Object> values = objects.get(attributeName);
		if (values == null) {
			values = new ArrayList<Object>();
			objects.put(attributeName, values);
		}
		values.add(object);

	}

}

// $Id$