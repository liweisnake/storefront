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
package com.hp.sdf.ngp.enabler.email;

import com.hp.sdf.ngp.common.exception.SendingEmailFailureException;

public interface EmailSender {

	public void sendMail(String from, String to[], String[] cc, String[] bcc,
			String subject, String content, String attachmentName,
			byte[] attachmentObject) throws SendingEmailFailureException;

	public void sendMail(String to[], String subject, String content,
			String attachmentName, byte[] attachmentObject) throws SendingEmailFailureException;
	
	public void sendMail(String to, String subject, String content,
			String attachmentName, byte[] attachmentObject) throws SendingEmailFailureException;

	public void sendMail(String to[], String subject, String content) throws SendingEmailFailureException;
	
	public void sendMail(String to, String subject, String content) throws SendingEmailFailureException;

}

// $Id$