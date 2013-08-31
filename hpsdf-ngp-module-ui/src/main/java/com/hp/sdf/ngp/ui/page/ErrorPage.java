/*
 * Copyright (c) 2007 Hewlett-Packard Company, All Rights Reserved.
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
package com.hp.sdf.ngp.ui.page;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.InvocationTargetException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.Application;
import org.apache.wicket.Page;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.MultiLineLabel;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.protocol.http.PageExpiredException;

public class ErrorPage extends WicketPage {
	private static final Log log = LogFactory.getLog(ErrorPage.class);

	private String simpleMessage = "";
	private String detailErrorInfo = "";
	private boolean isVisible = false;

	WebMarkupContainer container;

	public String getSimpleMessage() {
		return simpleMessage;
	}

	public void setSimpleMessage(String simpleMessage) {
		this.simpleMessage = simpleMessage;
	}

	public String getDetailErrorInfo() {
		return detailErrorInfo;
	}

	public void setDetailErrorInfo(String detailErrorInfo) {
		this.detailErrorInfo = detailErrorInfo;
	}

	public ErrorPage() {
		this(null, null);
	}

	public ErrorPage(Page page, RuntimeException e) {

		super(null);

		add(new MultiLineLabel("simpleMessage", new PropertyModel(this,
				"simpleMessage")));

		container = new WebMarkupContainer("container");
		container.add(new MultiLineLabel("detailErrorInfo", new PropertyModel(
				this, "detailErrorInfo")));
		container.setVisible(false);
		add(container);

		Link linkDetail = new Link("detail") {
			public void onClick() {
				// do your action.
				isVisible = !isVisible;
				log.debug("isVisible:" + isVisible);
				container.setVisible(isVisible);
			}
		};

		Link linkBack = new Link("back") {
			public void onClick() {
				this.setResponsePage(Application.get().getHomePage());
			}
		};

		add(linkDetail);
		
		add(linkBack);

		if (e == null || e instanceof PageExpiredException) {
			setSimpleMessage("This page has expired, please login again");
			linkDetail.setVisible(false);
			return;
		} else {
			linkDetail.setVisible(true);
			String szDetailErrorInfo = e.getMessage();

			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			PrintStream printStream = new PrintStream(outputStream);
			e.printStackTrace(printStream);
			szDetailErrorInfo = new String(outputStream.toByteArray());

			String simpleMessage = e.getMessage();

			Throwable finalThrowable = e.getCause();
			while (true) {
				while (finalThrowable != null
						&& finalThrowable.getCause() != null) {
					finalThrowable = finalThrowable.getCause();
				}

				if (finalThrowable != null) {
					if (finalThrowable instanceof InvocationTargetException) {
						if (((InvocationTargetException) finalThrowable)
								.getTargetException() != null) {
							finalThrowable = ((InvocationTargetException) finalThrowable)
									.getTargetException();
							continue;
						}
					}

				}
				break;
			}

			if (finalThrowable != null) {
				simpleMessage = finalThrowable.getMessage();
			}

			setSimpleMessage(simpleMessage);
			setDetailErrorInfo(szDetailErrorInfo);
			log.warn("Error Occur", e);
			return;
		}

	}

}

// $Id$