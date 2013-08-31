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
package com.hp.sdf.ngp.api.exception;

public class SubscriberCatalogEntityNotFoundException extends SubscriberCatalogServiceException {
	
	private static final long serialVersionUID = -1;

	public SubscriberCatalogEntityNotFoundException(String msg) {
		super(msg);
	}
	
	public SubscriberCatalogEntityNotFoundException(Exception e) {
		super(e);
	}

	public SubscriberCatalogEntityNotFoundException(String msg, Exception e) {
		super(msg, e);
	}

	/**
	 * Return the detailed explanation for the failure of the store operation
	 */
	@Override
	public String getMessage() {
		return super.getMessage();
	}
}

// $Id$