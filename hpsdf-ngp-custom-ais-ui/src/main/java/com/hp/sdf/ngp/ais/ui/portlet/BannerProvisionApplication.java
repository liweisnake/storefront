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
package com.hp.sdf.ngp.ais.ui.portlet;

import javax.portlet.PortletMode;

import org.apache.wicket.Page;
import org.apache.wicket.RequestContext;
import org.apache.wicket.protocol.http.portlet.PortletRequestContext;
import org.springframework.stereotype.Component;

import com.hp.sdf.ngp.ais.ui.page.banner.management.BannerListPage;
import com.hp.sdf.ngp.ais.ui.page.banner.view.BannerViewPage;
import com.hp.sdf.ngp.ais.ui.WicketApplication;

/**
 * Application object for your web application. If you want to run this
 * application without deploying, run the Start class.
 * 
 * @see com.home.Start#main(String[])
 */

@Component
public class BannerProvisionApplication extends WicketApplication {
	
//	protected void init(){
//		mountBookmarkablePage("/view", BannerListPage.class);
//		mountBookmarkablePage("/edit", BannerViewPage.class);
//	}

	/**
	 * @see org.apache.wicket.Application#getHomePage()
	 */
	@SuppressWarnings("unchecked")
	public Class<Page> getHomePage() {
		PortletRequestContext prc = (PortletRequestContext) RequestContext.get(); 
		     if (PortletMode.EDIT.equals(prc.getPortletRequest().getPortletMode())) 
		     { 
		       return (Class)BannerListPage.class; 
		     } 
		return (Class)BannerListPage.class;
	}

}
