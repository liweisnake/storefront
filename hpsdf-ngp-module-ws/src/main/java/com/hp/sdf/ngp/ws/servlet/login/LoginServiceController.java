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
package com.hp.sdf.ngp.ws.servlet.login;

import java.io.IOException;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;

import com.hp.sdf.ngp.manager.UserManager;

@Controller
public class LoginServiceController extends HttpServlet {

	

	private static final long serialVersionUID = 1L;
	private static final Log log = LogFactory
			.getLog(LoginServiceController.class);
	
	@Resource
	private UserManager UserManager;

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		log.debug("Get a request");
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		log
				.debug("-----------------Get a post request for login service----------------");

		String username = request.getParameter("j_username");
		String password = request.getParameter("j_password");

		String referer = request.getHeader("Referer");
		log.debug("Refer URL" + referer);

		this.UserManager.validateLogin(username, password);

		// Finally, redirect to the actual security check domain
		// 'j_security_check'
		String url = "/portal/auth/j_security_check?j_username=" + username
				+ "&j_password=" + password;
		String redirectUrl = response.encodeRedirectURL(url);
		response.sendRedirect(redirectUrl);

	}
}

// $Id$