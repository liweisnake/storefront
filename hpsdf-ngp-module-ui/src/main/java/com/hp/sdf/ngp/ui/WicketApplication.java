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

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.Page;
import org.apache.wicket.Request;
import org.apache.wicket.RequestCycle;
import org.apache.wicket.Response;
import org.apache.wicket.RestartResponseAtInterceptPageException;
import org.apache.wicket.Session;
import org.apache.wicket.application.IComponentInstantiationListener;
import org.apache.wicket.authorization.IUnauthorizedComponentInstantiationListener;
import org.apache.wicket.authorization.UnauthorizedInstantiationException;
import org.apache.wicket.authorization.strategies.role.IRoleCheckingStrategy;
import org.apache.wicket.authorization.strategies.role.RoleAuthorizationStrategy;
import org.apache.wicket.authorization.strategies.role.Roles;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.protocol.http.WebRequest;
import org.apache.wicket.protocol.http.WebRequestCycle;
import org.apache.wicket.protocol.http.WebResponse;
import org.apache.wicket.util.file.WebApplicationPath;
import org.springframework.stereotype.Component;

import com.hp.sdf.ngp.common.annotation.Value;
import com.hp.sdf.ngp.service.ApplicationService;
import com.hp.sdf.ngp.service.InfoService;
import com.hp.sdf.ngp.service.UserService;
import com.hp.sdf.ngp.ui.annotation.CustomizedSpringComponentInjector;
import com.hp.sdf.ngp.ui.annotation.PageScanner;
import com.hp.sdf.ngp.ui.dynamicForm.MyOwnFinderStreamLocator;
import com.hp.sdf.ngp.ui.page.ErrorPage;
import com.hp.sdf.ngp.ui.page.HomePage;
import com.hp.sdf.ngp.ui.page.signin.SignInPage;
import com.hp.sdf.ngp.workflow.UserCategoryLifeCycleEngine;

/**
 * Application object for your web application. If you want to run this application without deploying, run the Start class.
 * 
 * @see com.home.Start#main(String[])
 */

@Component("wicketApplication")
public class WicketApplication extends WebApplication implements IRoleCheckingStrategy, IUnauthorizedComponentInstantiationListener {

	private static final Log log = LogFactory.getLog(WicketApplication.class);

	@Resource
	private UserService userService;

	private String uriPrefix;

	private String saveFilePrefix;

	@Resource
	MyOwnFinderStreamLocator resourceStreamLocator;

	@Resource
	private ApplicationService applicationService;

	@Resource
	private InfoService infoService;

	public InfoService getInfoService() {
		return infoService;
	}

	public void setInfoService(InfoService infoService) {
		this.infoService = infoService;
	}

	public ApplicationService getApplicationService() {
		return applicationService;
	}

	public void setApplicationService(ApplicationService applicationService) {
		this.applicationService = applicationService;
	}

	public String getSaveFilePrefix() {
		return saveFilePrefix;
	}

	@Value("file.path.prefix")
	public void setSaveFilePrefix(String saveFilePrefix) {
		this.saveFilePrefix = saveFilePrefix;
	}

	@Value("uriPrefix")
	public String getUriPrefix() {
		return uriPrefix;
	}

	public void setUriPrefix(String uriPrefix) {
		this.uriPrefix = uriPrefix;
	}

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public UserCategoryLifeCycleEngine getUserCategoryLifeCycleEngine() {
		return userCategoryLifeCycleEngine;
	}

	public void setUserCategoryLifeCycleEngine(UserCategoryLifeCycleEngine userCategoryLifeCycleEngine) {
		this.userCategoryLifeCycleEngine = userCategoryLifeCycleEngine;
	}

	@Resource
	private UserCategoryLifeCycleEngine userCategoryLifeCycleEngine;

	private int itemsPerPage = 5;

	public int getItemsPerPage() {
		return itemsPerPage;
	}

	@Value("Storefront.ItemPerPage")
	public void setItemsPerPage(int itemsPerPage) {
		this.itemsPerPage = itemsPerPage;
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

		// Set authorization strategy and unauthorized instantiation listener
		getSecuritySettings().setAuthorizationStrategy(new RoleAuthorizationStrategy(this));
		getSecuritySettings().setUnauthorizedComponentInstantiationListener(this);

		new PageScanner().scanPackage("com.hp.sdf.ngp.ui.page").mount(this);

		//
		WebApplicationPath webContainerPathFinder = new WebApplicationPath(getServletContext());
		resourceStreamLocator.addFinder(webContainerPathFinder);
		getResourceSettings().setResourceStreamLocator(resourceStreamLocator);

		getMarkupSettings().setDefaultMarkupEncoding("UTF-8");

		getDebugSettings().setAjaxDebugModeEnabled(false);
	}

	public RequestCycle newRequestCycle(Request request, Response response) {
		return new WebRequestCycle(this, (WebRequest) request, (WebResponse) response) {
			public Page onRuntimeException(Page page, RuntimeException e)

			{
				try {
					return new ErrorPage(page, e);
				} catch (Throwable e1) {
					log.error(e1.getMessage());
				}
				return page;
			}
		};
	}

	protected Class<? extends WebPage> getSignInPageClass() {
		return SignInPage.class;
	}

	/**
	 * @see org.apache.wicket.protocol.http.WebApplication#newSession(org.apache.wicket.Request, org.apache.wicket.Response)
	 */
	@Override
	public Session newSession(final Request request, final Response response) {
		return new WicketSession(request);
	}

	/**
	 * @see org.apache.wicket.Application#getHomePage()
	 */
	@SuppressWarnings("unchecked")
	public Class<Page> getHomePage() {
		return (Class) HomePage.class;
	}

	public boolean hasAnyRole(final Roles roles) {

		final Roles sessionRoles = WicketSession.get().getRoles();
		return sessionRoles != null && sessionRoles.hasAnyRole(roles);
	}

	public void onUnauthorizedInstantiation(org.apache.wicket.Component component) {

		if (component instanceof Page) {
			if (!WicketSession.get().isSignedIn()) {
				// Redirect to intercept page to let the user sign in
				throw new RestartResponseAtInterceptPageException(getSignInPageClass());
			} else {
				onUnauthorizedPage(component);
			}
		} else {

			// do nothing
		}

	}

	protected void onUnauthorizedPage(org.apache.wicket.Component component) {
		// TODO

		// The component was not a page, so throw an exception
		throw new UnauthorizedInstantiationException(component.getClass());
	}

}
