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
package com.hp.sdf.ngp.enabler.email.mock;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

import com.hp.sdf.ngp.common.exception.SendingEmailFailureException;
import com.hp.sdf.ngp.enabler.email.EmailSender;

@Component
public class EmailSenderMock implements EmailSender {
	private final static Log log = LogFactory.getLog(EmailSenderMock.class);

	public void sendMail(String from, String[] to, String[] cc, String[] bcc,
			String subject, String content, String attachmentName,
			byte[] attachmentObject) {
		if (log.isDebugEnabled()) {
			log.debug("The from[" + from + "]; the subject[" + subject + "]"
					+ "; the content[" + content + "]"
					+ "; the attachmentName[" + attachmentName
					+ "] to send Email via Mock");

		}

	}

	public void sendMail(String[] to, String subject, String content,
			String attachmentName, byte[] attachmentObject)
			throws SendingEmailFailureException {

		this.sendMail(null, to, null, null, subject, content, attachmentName,
				attachmentObject);
	}

	public void sendMail(String[] to, String subject, String content)
			throws SendingEmailFailureException {

		this.sendMail(to, subject, content, null, null);
	}
	public void sendMail(String to, String subject, String content,
			String attachmentName, byte[] attachmentObject)
			throws SendingEmailFailureException {
		this.sendMail(null, new String[]{to}, null, null, subject, content, attachmentName,
				attachmentObject);
		
	}

	public void sendMail(String to, String subject, String content)
			throws SendingEmailFailureException {
		this.sendMail(to, subject, content, null, null);
		
	}

}

// $Id$