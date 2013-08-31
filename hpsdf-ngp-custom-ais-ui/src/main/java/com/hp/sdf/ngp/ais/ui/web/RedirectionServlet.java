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
package com.hp.sdf.ngp.ais.ui.web;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hp.sdf.ngp.banner.BannerService;

@Component
public class RedirectionServlet extends HttpServlet implements org.springframework.web.HttpRequestHandler {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5520285437796339071L;

	@Autowired
	private BannerService bannerService;

	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
	}

	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
	}

	public void handleRequest(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

		Long bannerId = null;
		Long contentId = null;
		String userId = null;
		String location = null;
		String url = null;
		location = req.getParameter("location");
		url = req.getParameter("url");
		userId = req.getParameter("userId");

		try {
			bannerId = Long.valueOf(req.getParameter("bannerId"));
			contentId = Long.valueOf(req.getParameter("contentId"));
		} catch (NumberFormatException exception) {
			exception.printStackTrace();
		}
		if (bannerId != null && contentId != null) {
			bannerService.addBannerUsageLog(bannerId, contentId, userId, location);
		}

		if (!StringUtils.isEmpty(url)) {
			res.setStatus(HttpServletResponse.SC_MOVED_TEMPORARILY);
			res.setHeader("Location", url);
			res.setContentType("text/html");
		}
	}

	public BannerService getBannerService() {
		return bannerService;
	}

	public void setBannerService(BannerService bannerService) {
		this.bannerService = bannerService;
	}

}

// $Id$