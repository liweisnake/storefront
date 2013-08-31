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
package com.hp.sdf.ngp.cms;

public class CMSException extends RuntimeException {

	/** The serialVersionUID */
	private static final long serialVersionUID = 5497731412015918410L;

	public static final int INVALID_ARCHIVE = 1;

	private int errorCode = 0;

	public CMSException() {
	}

	public CMSException(String message) {
		super(message);
	}

	public CMSException(String message, Throwable cause) {
		super(message, cause);
	}

	public CMSException(Throwable cause) {
		super(cause);
	}

	public CMSException(int errorCode) {
		this.errorCode = errorCode;
	}

	public boolean hasPathFormatFailure() {
		boolean pathFormatFailure = false;

		if (this.getMessage().indexOf("is not a legal path element") != -1
				|| this.getMessage().indexOf("is not a valid path") != -1) {
			pathFormatFailure = true;
		}

		return pathFormatFailure;
	}

	public String getMessageKey() {
		String key = "";

		// TODO: add message key into message file
		switch (this.errorCode) {
		case INVALID_ARCHIVE:
			key = "INVALID_ARCHIVE_MESSAGE";
			break;

		default:
			key = "";
			break;
		}

		return key;
	}
}

// $Id$