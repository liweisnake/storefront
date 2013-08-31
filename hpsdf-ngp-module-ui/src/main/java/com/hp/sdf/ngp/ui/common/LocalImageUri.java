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
package com.hp.sdf.ngp.ui.common;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.WebComponent;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.protocol.http.WebApplication;

import com.hp.sdf.ngp.ui.WicketApplication;

public class LocalImageUri extends WebComponent {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public LocalImageUri(String id, final String uri) {
		super(id, new AbstractReadOnlyModel<String>() {
			private static final long serialVersionUID = 1L;

			public String getObject() {

				WicketApplication wicketApplication = (WicketApplication) WebApplication.get();
				return wicketApplication.getUriPrefix() + uri;

			}
		});
		add(new AttributeModifier("src", true, new AbstractReadOnlyModel<String>() {
			private static final long serialVersionUID = 1L;

			public String getObject() {
				WicketApplication wicketApplication = (WicketApplication) WebApplication.get();
				return wicketApplication.getUriPrefix() + uri;
			}
		}));
	}

	@Override
	protected void onComponentTag(ComponentTag tag) {
		super.onComponentTag(tag);
		checkComponentTag(tag, "img");
	}

}

// $Id$