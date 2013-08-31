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
package com.hp.sdf.ngp.ui.page.api;

import org.apache.wicket.markup.html.navigation.paging.PagingNavigator;
import org.apache.wicket.markup.html.panel.Panel;

import com.hp.sdf.ngp.ui.page.api.ApiSearchResultPanel.ApiDataView;

public class ApiAllListPanel extends Panel{

	private static final long serialVersionUID = -6636878992606004891L;

	public ApiAllListPanel(String id, ApiDataView apiAllDataView){
		super(id);
		add(apiAllDataView);
		add(new PagingNavigator("navigator", apiAllDataView));
	}

}

// $Id$