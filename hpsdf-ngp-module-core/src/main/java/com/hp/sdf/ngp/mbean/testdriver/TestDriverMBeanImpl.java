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
package com.hp.sdf.ngp.mbean.testdriver;

import java.util.List;

import org.jboss.annotation.ejb.Service;
import org.jboss.system.ServiceMBeanSupport;

import com.hp.sdf.ngp.common.exception.SendingEmailFailureException;
import com.hp.sdf.ngp.common.exception.SendingSmsFailureException;
import com.hp.sdf.ngp.mbean.TestDriver;
import com.hp.sdf.ngp.mbean.TestDriverMBean;

@Service(objectName = TestDriver.OBJECT_NAME)
public class TestDriverMBeanImpl extends ServiceMBeanSupport implements
		TestDriverMBean {

	private TestDriver testDriver = null;
	
	public void setTestDriver(TestDriver testDriver) {
		this.testDriver = testDriver;
	}

	public void sendMail(String from, String[] to, String[] cc, String[] bcc,
			String subject, String content, String attachmentName,
			byte[] attachmentObject) throws SendingEmailFailureException {
		testDriver.sendMail(from, to, cc, bcc, subject, content, attachmentName,
				attachmentObject);
	}

	public void sendMail(String[] to, String subject, String content,
			String attachmentName, byte[] attachmentObject)
			throws SendingEmailFailureException {
		testDriver.sendMail(to, subject, content, attachmentName, attachmentObject);
	}

	public void sendMail(String[] to, String subject, String content)
			throws SendingEmailFailureException {
		testDriver.sendMail(to, subject, content);
	}

	public void send(String msisdn, String senderName, String msg)  throws SendingSmsFailureException{
		testDriver.send(msisdn, senderName, msg);
	}

	
	public void sendMail(String to, String subject, String content,
			String attachmentName, byte[] attachmentObject)
			throws SendingEmailFailureException {
		testDriver.sendMail(null, new String[]{to}, null, null, subject, content, attachmentName,
				attachmentObject);
		
	}

	public void sendMail(String to, String subject, String content)
			throws SendingEmailFailureException {
		testDriver.sendMail(to, subject, content, null, null);
		
	}

	public String getFullMsisdn(String tel) {
		
		return testDriver.getFullMsisdn(tel);
	}

	public boolean isValidTel(String tel) {

		return testDriver.isValidTel(tel);
	}

	public void sendGroup(List<String> msisdn, String senderName, String msg)
			throws SendingSmsFailureException {
		testDriver.sendGroup(msisdn, senderName, msg);
		
	}

	public void testJBOSSUser() {
		testDriver.testJBOSSUser();
	}
	

}

// $Id$