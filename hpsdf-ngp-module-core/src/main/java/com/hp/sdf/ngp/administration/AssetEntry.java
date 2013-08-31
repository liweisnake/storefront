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
package com.hp.sdf.ngp.administration;

import java.util.HashMap;

public class AssetEntry extends HashMap<String, BinaryContent> {

	private String xml;

	private String name;

	public String getXml() {
		return xml;
	}

	public void setXml(String xml) {
		this.xml = xml;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("name=").append(name).append(" ");
		sb.append("xml=").append(xml).append(" ");
		for (BinaryContent bc : this.values()) {
			sb.append("binary name=").append(bc.getName()).append(" ");
			sb.append("binary size=").append(bc.getContent().length).append(" ");
		}
		return sb.toString();
	}

}

// $Id$