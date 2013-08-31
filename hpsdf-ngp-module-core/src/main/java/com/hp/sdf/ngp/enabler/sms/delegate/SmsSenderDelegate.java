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
package com.hp.sdf.ngp.enabler.sms.delegate;

import java.util.List;

import org.springframework.stereotype.Component;

import com.hp.sdf.ngp.common.delegate.ComponentDelegate;
import com.hp.sdf.ngp.common.exception.SendingSmsFailureException;
import com.hp.sdf.ngp.enabler.sms.SmsSender;
import com.hp.sdf.ngp.enabler.sms.impl.SmsSenderWebServiceImpl;

/**
 * This class will delegate the method call to a particular {@link SmsSender}
 * implementation. It will check the configuration item
 * <code>com.hp.sdf.ngp.sms.SmsSender</code> from the property files to decide which
 * particular subclass would be delegated.
 * <p>
 * For example, if
 * <code>com.hp.sdf.ngp.enabler.sms.SmsSender=com.hp.sdf.ngp.enabler.sms.impl.SmsSenderWebServiceImpl</code>
 * configured, then {@link SmsSenderWebServiceImpl} will be used.
 * 
 * *
 * <p>
 * By default, if no any correct configuration item, {@link SmsSenderWebServiceImpl}
 * will be used.
 * 
 */
@Component(value = "smsSender")
public class SmsSenderDelegate extends ComponentDelegate<SmsSender> implements
		SmsSender {

	@SuppressWarnings("unchecked")
	@Override
	protected Class<SmsSender> getDefaultComponent() {
		return (Class) SmsSenderWebServiceImpl.class;
	}

	public String getFullMsisdn(String tel) {
		return this.component.getFullMsisdn(tel);
	}

	public boolean isValidTel(String tel) {
		return this.component.isValidTel(tel);
	}

	public void send(String msisdn, String senderName, String msg)
			throws SendingSmsFailureException {
		this.component.send(msisdn, senderName, msg);
	}

	public void sendGroup(List<String> msisdn, String senderName, String msg)
			throws SendingSmsFailureException {
		this.component.sendGroup(msisdn, senderName, msg);
	}

}

// $Id$