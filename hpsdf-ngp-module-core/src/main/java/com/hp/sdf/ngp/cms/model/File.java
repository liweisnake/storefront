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
package com.hp.sdf.ngp.cms.model;

import java.io.Serializable;
import java.util.Locale;

public class File extends CMSObject implements Serializable {
	
	/** The serialVersionUID */
	private static final long serialVersionUID = 4182943560695283657L;

	protected Content content;

	public void setContent(Locale locale, Content content) {
		this.content = content;
		this.content.setLocale(locale);
	}

	public Content getContent(Locale locale) {

		return this.content;
	}

	public Content getContent() {

		return this.content;
	}

	public long getSize() {
		return this.content.getSize();
	}
}
