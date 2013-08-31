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
package com.hp.sdf.ngp.enabler.sms;

import java.util.List;

import com.hp.sdf.ngp.common.exception.SendingSmsFailureException;

/**
 * This interface is used to send a SMS message to back-end system, the detail
 * implementation may be via Web Service or via SMPP gateway, which counts on
 * the subclass logic
 *  
 */
public interface SmsSender {

	/**
	 * Send SMS to one msisdn
	 * 
	 * @param msisdn
	 * @param senderName
	 * @param msg
	 * @throws SendingSmsFailureException
	 */
	public void send(String msisdn, String senderName, String msg) throws SendingSmsFailureException;
	
	/**
	 * Send SMS to a gourp of msisdn
	 * 
	 * @param msisdn
	 * @param senderName
	 * @param msg
	 * @throws SendSmsFailedException
	 */
	public void sendGroup(List<String> msisdn, String senderName, String msg) throws SendingSmsFailureException;
	
	/**
	 * Whether the telephone number is correct
	 * @param tel
	 * @return
	 */
	public boolean isValidTel(String tel);
	
	/**
	 * Get full msisdn of telephone number
	 * @param tel
	 * @return
	 */
	public String getFullMsisdn(String tel);
	
	

}

// $Id$