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
public class SGFCallingFailureException extends NgpException implements Serializable {

	private static final long serialVersionUID = -8619169672513372284L;
	
	public static String SGF_GENERAL_EXCEPTION = "SGF0001";
	public static String SGF_PARTNER_EXCEPTION = "SGF0002";
	public static String SGF_SERVICE_EXCEPTION = "SGF0003";
	public static String SGF_SERVICE_GROUP_EXCEPTION = "SGF0004";
	public static String SGF_RMM_EXCEPTION = "SGF0005";
	public static String SGF_PARTNER_EXISTS = "SGF0006";
	public static String SGF_PARTNER_ILLEGAL_ARGUMENT = "SGF0007";
	public static String SGF_SERVICE_NOT_EXSIT = "SGF0008";
	public static String SGF_SERVICE_DISCOVERY_GROUP_NOT_EXSIT = "SGF0009";
	public static String SGF_SERVICE_GROUP_EXISTS = "SGF0010";
	public static String SGF_ACCOUNT_EXISTS = "SGF0011";

	public SGFCallingFailureException(String message, Throwable cause) {
		super(message, cause);

	}

	public SGFCallingFailureException(String message) {
		super(message);

	}

	public SGFCallingFailureException(Throwable cause) {
		super(cause);
	}
}

// $Id$