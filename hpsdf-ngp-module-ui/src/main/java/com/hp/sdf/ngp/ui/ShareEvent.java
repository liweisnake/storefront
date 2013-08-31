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
package com.hp.sdf.ngp.ui;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * This class is used to share those data need to be shared by more than one
 * portlet. {$link @XmlRootElement} is used to declare its default name space
 * 
 * 
 */
@XmlRootElement
public class ShareEvent implements Serializable {

	private static final long serialVersionUID = -5020721177849949958L;

	private Map<String, Serializable> map = new HashMap<String, Serializable>();

	private String id = UUID.randomUUID().toString();

	public void setObject(String name, Serializable value) {

		map.put(name, value);
	}

	public Object gerObject(String name) {
		return map.get(name);
	}

	/**
	 * Merge the object from input share event into itself
	 * 
	 * @param shareEvent
	 */
	public void merge(ShareEvent shareEvent) {
		if (shareEvent == null) {
			return;
		}
		// check if it is the same object
		if (this.id.equals(shareEvent.id)) {
			return;
		}
		Iterator<Map.Entry<String, Serializable>> iter = shareEvent.map
				.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry<String, Serializable> entry = iter.next();
			this.map.put(entry.getKey(), entry.getValue());

		}
	}

	
}

// $Id$