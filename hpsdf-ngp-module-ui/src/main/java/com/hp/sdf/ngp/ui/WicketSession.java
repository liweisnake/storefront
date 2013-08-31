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
import java.security.Principal;
import java.util.Arrays;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.Application;
import org.apache.wicket.Request;
import org.apache.wicket.RequestContext;
import org.apache.wicket.RequestCycle;
import org.apache.wicket.Session;
import org.apache.wicket.authorization.strategies.role.Roles;
import org.apache.wicket.protocol.http.WebRequest;
import org.apache.wicket.protocol.http.WebSession;
import org.apache.wicket.protocol.http.portlet.PortletRequestContext;

import com.hp.sdf.ngp.model.RoleCategory;
import com.hp.sdf.ngp.model.UserProfile;
import com.hp.sdf.ngp.service.UserService;
import com.hp.sdf.ngp.workflow.Privilege;
import com.hp.sdf.ngp.workflow.UserCategoryLifeCycleEngine;

public final class WicketSession extends WebSession {

	private static Roles DEFAULT_ROLE = new Roles();
	static {
		DEFAULT_ROLE.add(Privilege.DEFAULT);
	}

	public enum AttributeName {

		SEARCHMETAINFO,

		ORDERBY,

		ORDERENUM,

		KEYWORD;

	}

	protected boolean signedIn = false;

	protected Roles roles;

	private String userId = null;

	private ShareEvent shareEvent = new ShareEvent();

	public ShareEvent getShareEvent() {
		return shareEvent;
	}

	public void setShareEvent(ShareEvent shareEvent) {
		this.shareEvent = shareEvent;
	}

	public WicketSession(Request request) {
		super(request);

	}

	private static final long serialVersionUID = 7729401486055735154L;
	private static final Log log = LogFactory.getLog(WicketSession.class);

	private String retrieveUserFromPortlet() {
		String result = null;
		// Retrieve user name from portlet context
		RequestContext requestContext = RequestContext.get();

		if (requestContext instanceof PortletRequestContext) {
			PortletRequestContext portletRequestContext = (PortletRequestContext) requestContext;
			Principal principal = portletRequestContext.getPortletRequest().getUserPrincipal();
			if (principal == null) {
				return null;
			}
			result = principal.getName();
			if (StringUtils.isEmpty(result)) {
				return null;
			}
			log.debug("Get a user name from portlet context:" + result);

		}
		return result;
	}

	@SuppressWarnings("unchecked")
	private Roles retrieveUserRoles(String user) {

		if (user == null) {
			return DEFAULT_ROLE;
		}

		UserCategoryLifeCycleEngine userCategoryLifeCycleEngine = ((WicketApplication) Application.get()).getUserCategoryLifeCycleEngine();
		Roles roles = new Roles();
		UserService userService = ((WicketApplication) Application.get()).getUserService();
		UserProfile userProfile = userService.getUser(user);
		if (userProfile == null) {
			// If this user doesn't exist in the local system, add it
			userProfile = new UserProfile();
			userProfile.setUserid(user);
			userProfile.setEmail(user + "@portal.com");
			userService.saveUser(userProfile);
			// Role[user] is the fixed start-up role for any registered user
			RoleCategory roleCategory = userCategoryLifeCycleEngine.getStartupRoleCategory();
			if (roleCategory != null) {
				userService.assignRole(user, roleCategory.getRoleName());
			}
		}

		roles.addAll(Arrays.asList(userCategoryLifeCycleEngine.getUserPrivileges(user)));

		return roles;

	}

	/**
	 * @return True if the user is signed in to this session
	 */
	public final boolean isSignedIn() {
		if (!signedIn) {
			this.userId = this.retrieveUserFromPortlet();
			this.roles = retrieveUserRoles(userId);

			if (this.userId != null) {
				signedIn = true;
			}
		}
		return signedIn;
	}

	/**
	 * This method is not used in JBOSS Portlet environment
	 * 
	 * @param userName
	 * @param password
	 * @return
	 */
	public boolean authenticate(String userName, String password) {

		this.userId = userName;

		signedIn = true;

		this.roles = retrieveUserRoles(userId);

		return true;
	}

	/**
	 * 
	 * @return null if no login
	 */
	public String getUserId() {
		// Check whether it is signed or not to load the user information from
		// portlet context
		if (!this.isSignedIn()) {
			return null;
		}
		return userId;
	}

	public Roles getRoles() {

		// Check the sign in or not first to retrieve the user info
		if (!isSignedIn()) {
			return DEFAULT_ROLE;
		}
		return roles;
	}

	/**
	 * @return Current authenticated web session
	 */
	public static WicketSession get() {

		return (WicketSession) Session.get();
	}

	/**
	 * Signs user in by authenticating them with a username and password
	 * 
	 * @param username
	 *            The username
	 * @param password
	 *            The password
	 * @return True if the user was signed in successfully
	 */
	public final boolean signIn(final String username, final String password) {
		return signedIn = authenticate(username, password);

	}

	/**
	 * Sign the user out.
	 */
	public void signOut() {
		signedIn = false;
	}

	public void setObject(String name, Serializable value) {
		this.setObject(name, value, false);
	}

	/**
	 * Save the object into HTTP context so that it can be shared by multiple
	 * wicket session in the same HTTP context environment
	 * 
	 * @param name
	 * @param value
	 */
	public void setObject(String name, Serializable value, boolean sharedInPortlet) {

		if (sharedInPortlet) {
			this.shareEvent.setObject(name, value);

			RequestContext requestContext = RequestContext.get();

			log.debug("it is request contexgt[" + requestContext.getClass().getCanonicalName() + "]");

			if (requestContext instanceof PortletRequestContext) {

				log.debug("post a event[" + ShareEvent.class.getCanonicalName() + "] to portlet request");
				((PortletRequestContext) requestContext).getPortletRequest().getPortletSession().setAttribute(ShareEvent.class.getCanonicalName(), shareEvent);
			}

		} else {
			((WebRequest) RequestCycle.get().getRequest()).getHttpServletRequest().getSession().setAttribute(name, value);

		}

	}

	/**
	 * Get the object from HTTP context shared by multiple wicket session
	 * 
	 * @param name
	 * @return
	 */
	public Object getObject(String name) {

		RequestContext requestContext = RequestContext.get();

		log.debug("it is request contexgt[" + requestContext.getClass().getCanonicalName() + "]");

		if (requestContext instanceof PortletRequestContext) {
			ShareEvent newEvent = (ShareEvent) ((PortletRequestContext) requestContext).getPortletRequest().getPortletSession().getAttribute(ShareEvent.class.getCanonicalName());
			if (newEvent != null) {
				log.debug("retrieve a event[" + ShareEvent.class.getCanonicalName() + "] from portlet request");

				this.shareEvent.merge(newEvent);

				// after that, just remove it to avoid merge it again in next
				// method call in same request cycle
				((PortletRequestContext) requestContext).getPortletRequest().getPortletSession().removeAttribute(ShareEvent.class.getCanonicalName());
			}
		}
		Object object = this.shareEvent.gerObject(name);

		return object != null ? object : ((WebRequest) RequestCycle.get().getRequest()).getHttpServletRequest().getSession().getAttribute(name);

	}
}
