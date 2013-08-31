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
package com.hp.sdf.ngp.enabler.email.impl;

import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import com.hp.sdf.ngp.common.annotation.Value;
import com.hp.sdf.ngp.common.exception.SendingEmailFailureException;
import com.hp.sdf.ngp.enabler.email.EmailSender;

@Component
public class EmailSenderImpl implements EmailSender {

	private final static Log log = LogFactory.getLog(EmailSenderImpl.class);

	private String host = "localhost";
	private String user = "anonymous";
	private String password = "";
	private String auth = "false";
	private String from = "admin@localhost";
	private String encoding = "UTF-8";
	private String subject = "[NOTIFICATION]";
	private String content = "FYI";
	private int port = 25;
	private String attachmentName = "attachment";

	private String socketFactoryPort = null;// "25";
	private String socketFactoryClass = null;// "javax.net.ssl.SSLSocketFactory";
	private String socketFactoryFallback = null;// "false";
	private String starttlsEnable = null;// "true";

	public String getAttachmentName() {
		return attachmentName;
	}

	@Value("mail.smtp.attachment.name")
	public void setAttachmentName(String attachmentName) {
		this.attachmentName = attachmentName;
	}

	public String getSocketFactoryPort() {
		return socketFactoryPort;
	}

	@Value("mail.smtp.socketFactory.port")
	public void setSocketFactoryPort(String socketFactoryPort) {
		this.socketFactoryPort = socketFactoryPort;
	}

	public String getSocketFactoryClass() {
		return socketFactoryClass;
	}

	@Value("mail.smtp.socketFactory.class")
	public void setSocketFactoryClass(String socketFactoryClass) {
		this.socketFactoryClass = socketFactoryClass;
	}

	public String getSocketFactoryFallback() {
		return socketFactoryFallback;
	}

	@Value("mail.smtp.socketFactory.fallback")
	public void setSocketFactoryFallback(String socketFactoryFallback) {
		this.socketFactoryFallback = socketFactoryFallback;
	}

	public String getStarttlsEnable() {
		return starttlsEnable;
	}

	@Value("mail.smtp.starttls.enable")
	public void setStarttlsEnable(String starttlsEnable) {
		this.starttlsEnable = starttlsEnable;
	}

	public int getPort() {
		return port;
	}

	@Value("mail.smtp.port")
	public void setPort(int port) {
		this.port = port;
	}

	public String getContent() {
		return content;
	}

	@Value("mail.smtp.content")
	public void setContent(String content) {
		this.content = content;
	}

	public String getSubject() {
		return subject;
	}

	@Value("mail.smtp.subject")
	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getHost() {
		return host;
	}

	@Value("mail.smtp.host")
	public void setHost(String host) {
		this.host = host;
	}

	public String getUser() {
		return user;
	}

	@Value("mail.smtp.user")
	public void setUser(String user) {
		this.user = user;
	}

	public String getPassword() {
		return password;
	}

	@Value("mail.smtp.password")
	public void setPassword(String password) {
		this.password = password;
	}

	public String getAuth() {
		return auth;
	}

	@Value("mail.smtp.auth")
	public void setAuth(String auth) {
		this.auth = auth;
	}

	public String getFrom() {
		return from;
	}

	@Value("mail.smtp.from")
	public void setFrom(String from) {
		this.from = from;
	}

	public String getEncoding() {
		return encoding;
	}

	@Value("mail.smtp.encoding")
	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}

	@PostConstruct
	public void init() {
		if (log.isDebugEnabled()) {
			log.debug("The user name[" + this.user + "]; the password["
					+ this.password + "]" + "; the host[" + this.host + "]"
					+ "; the port[" + this.port + "]");

			log.debug("The auth[" + this.auth + "]; the socketFactoryClass["
					+ this.socketFactoryClass + "]"
					+ "; the socketFactoryPort[" + this.socketFactoryPort + "]"
					+ "; the socketFactoryFallback["
					+ this.socketFactoryFallback + "]"
					+ "; the starttlsEnable[" + this.starttlsEnable + "]");
		}
	}

	public void sendMail(String from, String[] to, String[] cc, String[] bcc,
			String subject, String content, String attachmentName,
			byte[] attachmentObject) throws SendingEmailFailureException {

		if (log.isDebugEnabled()) {
			log.debug("The user name[" + this.user + "]; the password["
					+ this.password + "]" + "; the host[" + this.host + "]"
					+ "; the port[" + this.port + "]" + "; the from["
					+ this.from + "]");

			log.debug("The auth[" + this.auth + "]; the socketFactoryClass["
					+ this.socketFactoryClass + "]"
					+ "; the socketFactoryPort[" + this.socketFactoryPort + "]"
					+ "; the socketFactoryFallback["
					+ this.socketFactoryFallback + "]"
					+ "; the starttlsEnable[" + this.starttlsEnable + "]");
		}
		JavaMailSenderImpl javaMail = new JavaMailSenderImpl();
		try {
			javaMail.setHost(host);
			javaMail.setPort(port);
			javaMail.setPassword(password);
			javaMail.setUsername(user);
			Properties prop = new Properties();

			prop.setProperty("mail.smtp.auth", this.auth);
			if (log.isDebugEnabled()) {
				prop.setProperty("mail.smtp.debug", "true");
			}
			if (!StringUtils.isEmpty(this.socketFactoryClass)) {
				prop.setProperty("mail.smtp.socketFactory.class",
						this.socketFactoryClass);

			}
			if (!StringUtils.isEmpty(this.socketFactoryPort)) {
				prop.setProperty("mail.smtp.socketFactory.port",
						this.socketFactoryPort);

			}
			if (!StringUtils.isEmpty(this.socketFactoryFallback)) {
				prop.setProperty("mail.smtp.socketFactory.fallback",
						this.socketFactoryFallback);

			}
			if (!StringUtils.isEmpty(this.starttlsEnable)) {
				prop.setProperty("mail.smtp.starttls.enable",
						this.starttlsEnable);

			}
			javaMail.setJavaMailProperties(prop);

			MimeMessage message = javaMail.createMimeMessage();
			MimeMessageHelper messageHelp = new MimeMessageHelper(message,
					true, encoding);

			if (StringUtils.isEmpty(from)) {
				from = this.from;
			}
			messageHelp.setFrom(from);
			if (to == null || to.length == 0) {
				throw new SendingEmailFailureException("Can't send the mail["
						+ subject + "] due to empty 'to'");
			}
			messageHelp.setTo(to);

			if (cc != null && cc.length != 0) {
				messageHelp.setCc(cc);
			}
			if (bcc != null && bcc.length != 0) {
				messageHelp.setBcc(bcc);
			}
			if (subject == null) {
				subject = this.subject;
			}
			messageHelp.setSubject(subject);
			if (content == null) {
				content = this.content;
			}
			messageHelp.setText(content, false);

			if (attachmentObject != null) {
				if (StringUtils.isEmpty(attachmentName)) {
					attachmentName = this.attachmentName;
				}

				messageHelp.addAttachment(MimeUtility
						.encodeWord(attachmentName), new ByteArrayResource(
						attachmentObject));

			}

			javaMail.send(message);

			return;

		} catch (Throwable e) {
			throw new SendingEmailFailureException("Can't send the mail["
					+ subject + "]", e);
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
	public static void main(String[] args) throws Exception {

	}

}

// $Id$