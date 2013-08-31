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
package com.hp.sdf.ngp.ws.servlet.ota;

import java.io.IOException;
import java.util.StringTokenizer;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.hp.sdf.ngp.common.annotation.Value;
import com.hp.sdf.ngp.manager.ApplicationManager;
import com.hp.sdf.ngp.manager.OTADownloadManager;

@Controller("binaryDownloadServiceController")
public class BinaryDownloadServiceController extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private static final Log log = LogFactory
			.getLog(BinaryDownloadServiceController.class);

	@Autowired
	private OTADownloadManager oTADownloadManager;

	@Autowired
	private ApplicationManager applicationManager;

	private String mobileUserAgent;

	public String getMobileUserAgent() {
		return mobileUserAgent;
	}

	@Value("mobile.user.agent")
	public void setMobileUserAgent(String mobileUserAgent) {
		this.mobileUserAgent = mobileUserAgent;
	}

	private boolean isMobileClient(String userAgent) {

		if (StringUtils.isEmpty(this.mobileUserAgent)) {
			return false;
		}

		if (!StringUtils.isEmpty(this.mobileUserAgent)) {
			if (this.mobileUserAgent.equalsIgnoreCase("all")) {
				return true;
			}
			StringTokenizer st = new StringTokenizer(this.getMobileUserAgent());
			while (st.hasMoreTokens()) {
				String str = st.nextToken();
				if (userAgent.contains(str)) {
					return true;
				}
			}
		}
		return false;
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String userAgent = request.getHeader("User-Agent");
		String assetStringId = request.getParameter("assetId");
		String versionStringId = request.getParameter("versionId");
		String userId = request.getParameter("userId");
		String deviceSerial = request.getParameter("deviceSerial");
		Long versionId = null;
		Long assetId = null;
		if (StringUtils.isEmpty(assetStringId) || StringUtils.isEmpty(userId)) {
			return;
		}
		assetId = new Long(assetStringId);

		if (!StringUtils.isEmpty(versionStringId)) {
			versionId = new Long(versionStringId);
		}

		if (isMobileClient(userAgent)) {
			log.debug("this is a request for OTA download");
			// Moblie OTA download
			String dd = this.oTADownloadManager.getDownloadDescriptor(assetId,
					versionId, userId, deviceSerial);
			response.setStatus(HttpServletResponse.SC_OK);
			response
					.setHeader("Content-Type", "application/vnd.oma.drm.dd+xml");
			ServletOutputStream os = response.getOutputStream();
			os.write(dd.getBytes());
			os.flush();
			os.close();
			response.flushBuffer();
		} else {
			log.debug("this is a request for direct download");
			// PC download
			String url = this.applicationManager.retrieveDownloadLink(assetId,
					versionId, userId, deviceSerial);
			log.debug("The actual download link is "+url);
			response.sendRedirect(url);
		}
	}
}

// $Id$