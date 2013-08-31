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
package com.hp.sdf.ngp.ui.portlet;

import org.apache.wicket.Page;
import org.springframework.stereotype.Component;

import com.hp.sdf.ngp.ui.WicketApplication;
import com.hp.sdf.ngp.ui.page.api.ApiListPage;

/**
 * Application object for your web application. If you want to run this
 * application without deploying, run the Start class.
 * 
 * @see com.home.Start#main(String[])
 */

@Component
public class ServiceListApplication extends WicketApplication {

	/**
	 * @see org.apache.wicket.Application#getHomePage()
	 */
	@SuppressWarnings("unchecked")
	public Class<Page> getHomePage() {
		return (Class)ApiListPage.class;
	}

}
