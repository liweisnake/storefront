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
package com.hp.sdf.ngp.common.exception;

import java.io.Serializable;

/**
 * This exception represents the exception when sending SMS failure
 * 
 * 
 */
public class SendingSmsFailureException extends NgpRuntimeException implements
		Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8619169672513372284L;

	public SendingSmsFailureException(String message, Throwable cause) {
		super(message, cause);

	}

	public SendingSmsFailureException(String message) {
		super(message);

	}

	public SendingSmsFailureException(Throwable cause) {
		super(cause);
	}
}

// $Id$