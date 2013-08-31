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
package com.hp.sdf.ngp.ais.ui;

import javax.annotation.Resource;

import org.apache.wicket.Page;
import org.apache.wicket.Request;
import org.apache.wicket.RequestCycle;
import org.apache.wicket.Response;
import org.apache.wicket.Session;
import org.apache.wicket.application.IComponentInstantiationListener;
import org.apache.wicket.authorization.UnauthorizedInstantiationException;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.protocol.http.WebRequest;
import org.apache.wicket.protocol.http.WebRequestCycle;
import org.apache.wicket.protocol.http.WebResponse;
import org.springframework.stereotype.Component;

import com.hp.sdf.ngp.ais.ui.page.HomePage;
import com.hp.sdf.ngp.banner.BannerService;
import com.hp.sdf.ngp.ui.annotation.CustomizedSpringComponentInjector;
import com.hp.sdf.ngp.ui.annotation.PageScanner;

@Component("wicketApplication")
public class WicketApplication extends WebApplication {
	
	@Resource
	private BannerService bannerService;
	
	public BannerService getBannerService() {
		return bannerService;
	}
	public void setBannerService(BannerService bannerService) {
		this.bannerService = bannerService;
	}
	/**
	 * @see org.apache.wicket.Application#getHomePage()
	 */
	@SuppressWarnings("unchecked")
	public Class<Page> getHomePage() {
		return (Class) HomePage.class;
	}
	@Override
	protected void init() {
		super.init();
		addComponentInstantiationListener(new CustomizedSpringComponentInjector(this));
		addComponentInstantiationListener(new IComponentInstantiationListener() {

			public void onInstantiation(org.apache.wicket.Component component) {
				component.setOutputMarkupId(true);

			}
		});
		
		new PageScanner().scanPackage("com.hp.sdf.ngp.ais.ui.page").mount(this);
		getMarkupSettings().setDefaultMarkupEncoding("UTF-8");

	}

	public RequestCycle newRequestCycle(Request request, Response response) {
		return new WebRequestCycle(this, (WebRequest) request, (WebResponse) response) {
			
		};
	}

	

	/**
	 * @see org.apache.wicket.protocol.http.WebApplication#newSession(org.apache.wicket.Request, org.apache.wicket.Response)
	 */
	@Override
	public Session newSession(final Request request, final Response response) {
		return super.newSession(request, response);
	}

	

	

	public void onUnauthorizedInstantiation(org.apache.wicket.Component component) {

		return;

	}

	protected void onUnauthorizedPage(org.apache.wicket.Component component) {
		// TODO

		// The component was not a page, so throw an exception
		throw new UnauthorizedInstantiationException(component.getClass());
	}
}

// $Id$