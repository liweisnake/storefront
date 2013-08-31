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

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.management.Attribute;
import javax.management.MBeanServer;
import javax.management.ObjectName;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.mx.util.MBeanServerLocator;
import org.springframework.stereotype.Component;

import com.hp.sdf.ngp.common.annotation.Value;
import com.hp.sdf.ngp.common.exception.SendingEmailFailureException;
import com.hp.sdf.ngp.common.exception.SendingSmsFailureException;
import com.hp.sdf.ngp.enabler.email.EmailSender;
import com.hp.sdf.ngp.enabler.sms.SmsSender;
import com.hp.sdf.ngp.mbean.TestDriver;
import com.hp.sdf.ngp.model.Country;
import com.hp.sdf.ngp.model.Language;
import com.hp.sdf.ngp.model.RoleCategory;
import com.hp.sdf.ngp.model.UserProfile;
import com.hp.sdf.ngp.service.UserService;


@Component
public class TestDriverImpl implements TestDriver {

	private final static Log log = LogFactory.getLog(TestDriverImpl.class);

	private boolean runtime = true;

	public boolean isRuntime() {
		return runtime;
	}

	@Value("runtime")
	public void setRuntime(boolean runtime) {
		this.runtime = runtime;
	}

	@PostConstruct
	public void connectService() {

		if (!runtime) {
			log.info("it is a test environment");
			return;
		}
		log.info("connecting to [" + TestDriver.OBJECT_NAME + "]");
		try {
			MBeanServer server = MBeanServerLocator.locateJBoss();
			ObjectName name = new ObjectName(TestDriver.OBJECT_NAME);
			server.setAttribute(name, new Attribute(TestDriver.class
					.getSimpleName(), this));
			log.info("injected " + getClass().getSimpleName() + " into ["
					+ TestDriver.OBJECT_NAME + "] successfully");
		} catch (Throwable e) {
			log.warn("can't connect to [" + TestDriver.OBJECT_NAME + "]", e);
		}

	}

	private SmsSender smsSender;

	private EmailSender emailSender;

	private UserService userService;
	


	public UserService getUserService() {
		return userService;
	}

	@Resource
	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public SmsSender getSmsSender() {
		return smsSender;
	}

	@Resource
	public void setSmsSender(SmsSender smsSender) {
		this.smsSender = smsSender;
	}

	public EmailSender getEmailSender() {
		return emailSender;
	}

	@Resource
	public void setEmailSender(EmailSender emailSender) {
		this.emailSender = emailSender;
	}

	public void sendMail(String from, String[] to, String[] cc, String[] bcc,
			String subject, String content, String attachmentName,
			byte[] attachmentObject) throws SendingEmailFailureException {
		emailSender.sendMail(from, to, cc, bcc, subject, content,
				attachmentName, attachmentObject);
	}

	public void sendMail(String[] to, String subject, String content,
			String attachmentName, byte[] attachmentObject)
			throws SendingEmailFailureException {
		emailSender.sendMail(to, subject, content, attachmentName,
				attachmentObject);
	}

	public void sendMail(String[] to, String subject, String content)
			throws SendingEmailFailureException {
		emailSender.sendMail(to, subject, content);
	}

	public void sendMail(String to, String subject, String content,
			String attachmentName, byte[] attachmentObject)
			throws SendingEmailFailureException {
		emailSender.sendMail(null, new String[] { to }, null, null, subject,
				content, attachmentName, attachmentObject);

	}

	public void sendMail(String to, String subject, String content)
			throws SendingEmailFailureException {
		emailSender.sendMail(to, subject, content, null, null);

	}

	public String getFullMsisdn(String tel) {
		return smsSender.getFullMsisdn(tel);
	}

	public boolean isValidTel(String tel) {

		return smsSender.isValidTel(tel);
	}

	public void send(String msisdn, String senderName, String msg)
			throws SendingSmsFailureException {
		smsSender.send(msisdn, senderName, msg);

	}

	public void sendGroup(List<String> msisdn, String senderName, String msg)
			throws SendingSmsFailureException {
		smsSender.sendGroup(msisdn, senderName, msg);

	}

	private String getNextUserId() {

		String newUserName = "User";
		int i = 1;
		while (userService.getUser(newUserName + i) != null) {
			i++;
		}
		return newUserName + i;
	}

	private String getNextRoleId() {

		String newRoleName = "Ftp";
		int i = 1;
		while (userService.getRoleByName(newRoleName + i) != null) {
			i++;
		}
		return newRoleName + i;
	}

	public void testJBOSSUser() {
		log.debug("%====%");
		
		
		
		
		RoleCategory role = new RoleCategory();
		role.setCreateDate(new Date());

		String roleName = getNextRoleId();
		role.setRoleName(roleName);
		log.debug("###save role.");
		userService.saveRole(role);

		role = userService.getRoleByName(roleName);
		if (role != null) {
			log.debug("***save role success!");
		}

		UserProfile sp = new UserProfile();
		String userId = getNextUserId();
		sp.setUserid(userId);

		sp.setAddress("abc");
		sp.setCellphone("1123445");
		sp.setCompany("def");
		sp.setEmail("wei.li20@hp.com");
		sp.setFirstname("li");
		sp.setGender("male");
		sp.setIdcard("111111111");
		sp.setLastname("wei");
		sp.setOnlineTime(1L);
		sp.setPassword("apple");
		sp.setZip("1234");
		// List<Country> cs = userService.getCountrys();

		// if (cs != null)
		Country cc = new Country();
		cc.setName("China");
		sp.setCountry(cc);
//		List<Language> ls = userService.getLanguages();
//		if (ls != null)
//			up.setLanguage(ls.get(0));
		Language ll = new Language();
		ll.setName("chinese");
		sp.setLanguage(ll);
		log.debug("###save user");
		userService.saveUser(sp);

		log.debug("###get user");

		UserProfile user = userService.getUser(userId);

		Field[] fields = user.getClass().getDeclaredFields(); //
		AccessibleObject.setAccessible(fields, true); // 
		log.debug("$$$start to iterator");
		for (Field field : fields) {

			try {
				Class type = field.getType();
				if (field.getName().equals("zip")
						&& field.get(user).equals("1234")) {
					log.debug("***getUser success!");
				}
				log.debug(field.getName() + field.get(user));
			} catch (Exception e) {
				e.printStackTrace();
			}

		}

		log.debug("###assignRole");
		userService.assignRole(userId, roleName);

		

		log.debug("###getRoleCategoryByUserId");
		List<RoleCategory> roles = userService.getRoleCategoryByUserId(userId);
		for (RoleCategory r : roles) {
			if (r.getId() == role.getId()) {
				log.debug("***getRoleCategoryByUserId success!");
			}
		}

	

		log.debug("###updateUser");
		sp.setEmail("liwei_snake@yahoo.com.cn");
		userService.updateUser(sp);
		sp = userService.getUser(userId);
		if (sp.getEmail().equals("liwei_snake@yahoo.com.cn")) {
			log.debug("***updateUser success!");
		}

	

	

	

		log.debug("###deleteRole");
		userService.deleteUser(userId);
		UserProfile spTmp = userService.getUser(userId);
		if (spTmp == null) {
			log.debug("***deleteUser success!");
		}

		// log.debug("%=====%");
		// UserProfile user = userService.getUser("admin");
		//
		// log.debug("###get role by name");
		// RoleCategory rc = userService.getRoleByName("Admin");
		//
		// log.debug("###get role");
		// userService.getRole(rc.getId());
		//
		// log.debug("###getRoleCategoryByUserId");
		// userService.getRoleCategoryByUserId(user.getUserid());
		//
		// log.debug("###getUser");
		// userService.getUser(user.getUserid());
		//
		// log.debug("###getUserByRoleId");
		// userService.getUserByRoleId(rc.getId());
		//
		// log.debug("###Auth " + user.getUserid());
		// userService.auth(user.getUserid(), rc.getId());
		//
		// UserProfile up = new UserProfile();
		// BeanUtils.copyProperties(user, up);
		// RoleCategory role = new RoleCategory();
		// String newRoleName = "Ftp";
		// String name = newRoleName;
		// int i = 1;
		// while (userService.getRoleByName(newRoleName + i) != null) {
		// name = newRoleName + i;
		// i++;
		// }
		// role.setRoleName(name);
		// role.setCreateDate(new Date());
		// log.debug("###save role" + role.getRoleName());
		// userService.saveRole(role);
		//
		// up.setUserid("ttt");
		// log.debug("###saveUser ");
		// userService.saveUser(up, role.getId());
		//
		// log.debug("###assignRole ");
		// userService.assignRole(up.getUserid(), role.getId());
		//
		// log.debug("###removeRole ");
		// userService.removeRole(up.getUserid(), role.getId());
		//
		// log.debug("###getRoleCategoryByUserId ");
		// List<RoleCategory> roles =
		// userService.getRoleCategoryByUserId("admin");
		// for (RoleCategory r : roles) {
		// log.debug(r.getId() + " " + r.getRoleName());
		// }
		//
		// log.debug("###delete user ");
		// userService.deleteUser(up.getUserid());
		// log.debug("%=====%");

	}
}

// $Id$