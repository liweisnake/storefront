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
package com.hp.sdf.ngp.enabler.sms.impl;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.PostConstruct;

import org.apache.axis.types.URI;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import com.hp.sdf.ngp.common.annotation.Value;
import com.hp.sdf.ngp.common.exception.SendingSmsFailureException;
import com.hp.sdf.ngp.enabler.sms.SmsSender;
import com.hp.sdf.ngp.enabler.sms.webservice.SendSmsProxy;

@Service(value = "smsSenderImpl")
public class SmsSenderWebServiceImpl implements SmsSender{
	
	public final static Log log = LogFactory.getLog(SmsSenderWebServiceImpl.class);
	
	private String host;
	private String port;
	private int nationalCode;
	private String sender;
	private String endpoint;
		
	public String getEndpoint() {
		return endpoint;
	}

	@Value("SMS.Endpoint")
	public void setEndpoint(String endpoint) {
		this.endpoint = endpoint;
	}

	public String getSender() {
		return sender;
	}

	@Value("SMS.Sender")
	public void setSender(String sender) {
		this.sender = sender;
	}

	public int getNationalCode() {
		return nationalCode;
	}

	@Value("SMS.National.Code")
	public void setNationalCode(int nationalCode) {
		this.nationalCode = nationalCode;
	}

	public String getHost() {
		return host;
	}

	@Value("SMS.Http.ProxyHost")
	public void setHost(String host) {
		this.host = host;
	}

	public String getPort() {
		return port;
	}

	@Value("SMS.Http.ProxyPort")
	public void setPort(String port) {
		this.port = port;
	}

	@PostConstruct
	public void init(){
		if(!StringUtils.isBlank(host) && !StringUtils.isBlank(port)){
			System.getProperties().put("http.proxyHost", host);
			System.getProperties().put("http.proxyPort", port);
		}
	}

	/**
	 * Send SMS to one msisdn
	 * 
	 * @param msisdn
	 * @param senderName
	 * @param msg
	 * @throws SendingSmsFailureException
	 */
	public void send(String msisdn, String senderName, String msg) throws SendingSmsFailureException {
		SendSmsProxy proxy = new SendSmsProxy(this.endpoint);
		String sd = senderName;
		if(sd == null || sd.length() == 0)
			sd = this.sender;
		try {
			URI[] addresses = new URI[1];
			addresses[0] = new URI("tel:+" + msisdn);
			log.debug("Start send sms, addresses is : " + addresses[0]
					+ ", senderName: " + sd + ", msg: " + msg);
			proxy.sendSms(addresses, sd, null, msg, null);
		} catch (Exception e) {
			log.error(e);
			throw new SendingSmsFailureException("Send SMS failed.", e);
		}
	}

	/**
	 * Send SMS to a gourp of msisdn
	 * 
	 * @param msisdn
	 * @param senderName
	 * @param msg
	 * @throws SendSmsFailedException
	 */
	public void sendGroup(List<String> msisdn, String senderName, String msg) throws SendingSmsFailureException {
		SendSmsProxy proxy = new SendSmsProxy(this.endpoint);
		String sd = senderName;
		if(sd == null || sd.length() == 0)
			sd = this.sender;
		try {
			URI[] addresses = new URI[msisdn.size()];
			for (int i = 0; i < msisdn.size(); i++) {
				addresses[i] = new URI(getFullMsisdn(msisdn.get(i)));
			}
			proxy.sendSms(addresses, sd, null, msg, null);
		} catch (Exception e) {
			throw new SendingSmsFailureException("Send SMS failed.", e);
		}
	}

	/**
	 * Whether the telephone number is correct
	 * @param tel
	 * @return
	 */
	public boolean isValidTel(String tel) {
		Pattern p = Pattern.compile("\\d{11}");
		Matcher m = p.matcher(tel);
		return m.matches();
	}

	/**
	 * Get full msisdn of telephone number
	 * @param tel
	 * @return
	 */
	public String getFullMsisdn(String tel) {
		final String telPattern = "\\d{11}";
		final String natPattern = "\\+" + nationalCode;
		final String prefix = "tel:";
		String result;
		Pattern p = Pattern.compile(prefix + natPattern + telPattern);
		Matcher m = p.matcher(tel);
		if (m.matches()) {
			result = tel;
			log.debug(result);
			return result;
		}
		p = Pattern.compile(natPattern + telPattern);
		m = p.matcher(tel);
		if (m.matches()) {
			result = prefix + tel;
			log.debug(result);
			return result;
		}
		p = Pattern.compile(telPattern);
		m = p.matcher(tel);
		if (m.matches()) {
			result = prefix + natPattern.substring(1) + tel;
			log.debug(result);
			return result;
		}

		return null;
	}
	
//	public static void main(String [] args) throws Exception{
//		System.out.println(ConfigUtil.getConfig().getString("sms.address"));
//		send("8613564710901", "hp", "hello");
//		System.out.println(getFullMsisdn("13564710901"));
//		
//	}
}

// $Id$