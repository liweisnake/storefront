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
package com.hp.sdf.ngp.enabler.sms.mock;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

import com.hp.sdf.ngp.common.exception.SendingSmsFailureException;
import com.hp.sdf.ngp.enabler.sms.SmsSender;

@Component
public class SmsSenderMock implements SmsSender {

	private final static Log log = LogFactory.getLog(SmsSenderMock.class);

	public String getFullMsisdn(String tel) {
		log.debug("Start Send SMS Mock service, get full msisdn");
		return "tel:+8613500000000";
	}

	public boolean isValidTel(String tel) {
		log.debug("Start Send SMS Mock service, validation telephone");
		return true;
	}

	public void send(String msisdn, String senderName, String msg)
			throws SendingSmsFailureException {
		log.debug("Start Send SMS Mock service, send sms to: " + msisdn + ", sender name: " + senderName + ", message: " + msg);
		return;
	}

	public void sendGroup(List<String> msisdn, String senderName, String msg)
			throws SendingSmsFailureException {
		log.debug("Start Send SMS Mock service, send sms to group: " + msisdn + ", sender name: " + senderName + ", message: " + msg);
		return;
	}

}

// $Id$