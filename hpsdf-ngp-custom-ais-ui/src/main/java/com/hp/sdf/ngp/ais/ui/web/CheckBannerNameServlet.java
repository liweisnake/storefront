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
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hp.sdf.ngp.banner.BannerService;
import com.hp.sdf.ngp.banner.model.Banner;

@Component
public class CheckBannerNameServlet implements org.springframework.web.HttpRequestHandler {

	private static final long serialVersionUID = 1L;
	
	@Autowired
	private BannerService bannerService;

	public void handleRequest(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String bannerName = req.getParameter("bannerName");
		String bannerId = req.getParameter("bannerId");
		Banner banner = null;
		if(bannerName != null && bannerName.length() > 0)
			banner = this.bannerService.getBanner(bannerName);
		if(banner == null){
			resp.getWriter().write("false");
		}else{
			if(bannerId != null && banner.getId().equals(new Long(bannerId)))
				resp.getWriter().write("false");
			else
				resp.getWriter().write("true");
		}
	}

}

// $Id$