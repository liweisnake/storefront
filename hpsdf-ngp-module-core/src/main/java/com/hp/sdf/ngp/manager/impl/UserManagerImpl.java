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
package com.hp.sdf.ngp.manager.impl;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

import com.hp.sdf.ngp.api.comon.EntityType;
import com.hp.sdf.ngp.common.annotation.Value;
import com.hp.sdf.ngp.common.exception.NgpRuntimeException;
import com.hp.sdf.ngp.common.util.Utils;
import com.hp.sdf.ngp.eav.model.AttributeValue;
import com.hp.sdf.ngp.enabler.email.EmailSender;
import com.hp.sdf.ngp.enabler.sms.SmsSender;
import com.hp.sdf.ngp.manager.UserManager;
import com.hp.sdf.ngp.model.RoleCategory;
import com.hp.sdf.ngp.model.UserProfile;
import com.hp.sdf.ngp.model.VerificationCode;
import com.hp.sdf.ngp.sdp.SGFWebService;
import com.hp.sdf.ngp.sdp.model.SGFAccount;
import com.hp.sdf.ngp.sdp.model.SGFPartner;
import com.hp.sdf.ngp.service.ApplicationService;
import com.hp.sdf.ngp.service.UserService;
import com.hp.sdf.ngp.service.impl.JbossPortalUserServiceImpl;
import com.hp.sdf.ngp.service.impl.UserServiceImpl;

@Component
public class UserManagerImpl implements UserManager {

	private final static Log log = LogFactory.getLog(UserManagerImpl.class);

	private static final String PASSWORD_FAILURE_COUNT = "passwordFailureCount";
	private static final String PASSWORD_FAILURE_DATE = "passwordFailureDate";

	private long disabledPeriod = 3600;

	private long passwordValidatePeriod = 600;

	private long passwordValidateCount = 3;

	public long getDisabledPeriod() {
		return disabledPeriod;
	}

	@Value("login.disabled.period")
	public void setDisabledPeriod(long disabledPeriod) {
		this.disabledPeriod = disabledPeriod;
	}

	public long getPasswordValidatePeriod() {
		return passwordValidatePeriod;
	}

	@Value("login.passwordvalidate.period")
	public void setPasswordValidatePeriod(long passwordValidatePeriod) {
		this.passwordValidatePeriod = passwordValidatePeriod;
	}

	public long getPasswordValidateCount() {
		return passwordValidateCount;
	}

	@Value("login.passwordvalidate.count")
	public void setPasswordValidateCount(long passwordValidateCount) {
		this.passwordValidateCount = passwordValidateCount;
	}

	@Resource
	private ApplicationService applicationService;

	@Resource
	private SGFWebService sGFWebService;

	@Resource
	private UserService userService;

	@Resource
	private UserServiceImpl userServiceImpl;

	@Resource
	private SmsSender smsSender;

	@Resource
	private EmailSender emailSender;

	@Resource
	private JbossPortalUserServiceImpl jbossPortalUserServiceImpl;

	private boolean isSMSOrEmail = true;

	private String verificationCodeSubject = "store front portal";

	private int expireTime = 30;

	public String getVerificationCodeSubject() {
		return verificationCodeSubject;
	}

	@Value("verificationCodeSubject")
	public void setVerificationCodeSubject(String verificationCodeSubject) {
		this.verificationCodeSubject = verificationCodeSubject;
	}

	public int getExpireTime() {
		return expireTime;
	}

	@Value("expireTime")
	public void setExpireTime(int expireTime) {
		this.expireTime = expireTime;
	}

	public boolean isSMSOrEmail() {
		return isSMSOrEmail;
	}

	@Value("isSMSOrEmail")
	public void setSMSOrEmail(boolean isSMSOrEmail) {
		this.isSMSOrEmail = isSMSOrEmail;
	}

	public boolean onboard(String userId) {
		UserProfile user = userService.getUser(userId);
		try {

			SGFPartner partner = new SGFPartner();
			SGFPartner.CompanyInfo companyInfo = partner.new CompanyInfo();
			SGFPartner.Address address = partner.new Address();
			address.setCity("shanghai");
			if (user.getCountry() == null || user.getCountry().getName() == null) {
				address.setCountry("China");
			} else {
				address.setCountry(user.getCountry().getName());
			}
			address.setState("shanghai");
			address.setStreet("shanghai");
			address.setZip("200000");
			companyInfo.setAddress(address);
			companyInfo.setName(user.getCompany());
			companyInfo.setUrl("http://www.hp.com");

			SGFPartner.ContactDetails contact = partner.new ContactDetails();
			String gender = user.getGender();
			if (gender != null)
				contact.setTitle(gender.equals("male") ? "Mr." : "Mrs.");
			contact.setFirstname(user.getFirstname());
			contact.setLastname(user.getLastname());
			address = partner.new Address();
			if (user.getCountry() == null || user.getCountry().getName() == null) {
				address.setCountry("China");
			} else {
				address.setCountry(user.getCountry().getName());
			}

			contact.setAddress(address);
			contact.setEmail(user.getEmail());
			if (user.getLanguage() == null || user.getLanguage().getName() == null) {
				contact.setLanguage("English");
			} else {
				contact.setLanguage(user.getLanguage().getName());
			}

			SGFPartner.LoginInfo login = partner.new LoginInfo();
			login.setUsername(user.getUserid());// unique
			login.setPassword("password");
			login.setPasswordQuestion("Company URL");// enum
			login.setPasswordAnswer("http://www.hp.com");

			partner.setCompanyInfo(companyInfo);
			partner.setContactDetails(contact);
			partner.setLoginInfo(login);

			log.debug(partner);

			sGFWebService.createPartner(partner);

			SGFAccount account = new SGFAccount();
			account.setPartnerID(user.getUserid());
			account.setPartnerName(user.getUserid());

			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

			String startDate = "now";

			String expireDate = "2099-12-31";

			Calendar now = Calendar.getInstance();
			if (!startDate.equals("now"))
				now.setTime(df.parse(startDate));

			account.setVaildFrom(now);
			account.setBalanceType("WEB2.0");

			now = Calendar.getInstance();
			now.setTime(df.parse(expireDate));

			account.setExpireDate(now);
			account.setUpdatedBy("admin");

			sGFWebService.createAccount(account);

			return true;
		} catch (Exception e) {
			throw new NgpRuntimeException(e);
		}
	}

	public void assignRole(String userId, String roleName) {
		UserProfile user = userService.getUser(userId);
		userService.assignRole(user.getUserid(), roleName);
	}

	public VerificationCode sentVerificationCode(String userId) {

		UserProfile user = userService.getUser(userId);

		String verificationCode = String.valueOf(Utils.getNumVerificationCode(1000000));

		log.debug("the verification code is " + verificationCode);

		if (isSMSOrEmail) {
			smsSender.send(user.getCellphone(), verificationCodeSubject, verificationCode);
		} else {
			emailSender.sendMail(user.getEmail(), verificationCodeSubject, verificationCode);
		}

		VerificationCode vc = new VerificationCode(verificationCode, expireTime);
		return vc;
	}

	public VerificationType getVerificationType() {
		if (isSMSOrEmail) {
			return VerificationType.SMS;
		}
		return VerificationType.EMAIL;
	}

	/**
	 * Check whether the user information is udpated in storefront database and
	 * whether need to sync from storefront database to JBOSS portal database
	 * 
	 * @param userProfile
	 */
	private void syncUserAndRole(String userId) {
		// Copy the user & password from UserProfile eav to JBoss, if not
		// synchronized before

		log
				.debug("Check whether the user information is udpated in storefront database and whether need to sync from storefront database to JBOSS portal database");
		if (!this.jbossPortalUserServiceImpl.isAvailable()) {
			log.debug("The jboss user service is not available, so cancel the sync");
			return;
		}
		// Sync deleted user
		UserProfile profile = this.userServiceImpl.getUser(userId);
		if (profile == null) {
			log.debug("The user[" + userId + "] is deleted");
			if (jbossPortalUserServiceImpl.getUser(userId) != null) {
				log.debug("The user[" + userId + "] is deleted, but still in jboss database");
				jbossPortalUserServiceImpl.deleteUser(userId);
				return;
			} else {
				log.debug("The user[" + userId + "] does not exist");
				return;
			}

		}
		// Check whether this used is udpated or not
		List<AttributeValue> syncAttributes = applicationService.getAttributeValue(userId, EntityType.USER, UserServiceImpl.SYNCFLG);
		if (syncAttributes != null && syncAttributes.size() > 0) {
			log.debug("The user[" + userId + "] is udpated only in storefront database, need to sync with jboss portal database");
			UserProfile profileInJBoss = jbossPortalUserServiceImpl.getUser(userId);
			if (profileInJBoss == null) {
				log.debug("add a new user in jboss portal database");
				this.jbossPortalUserServiceImpl.saveUser(profile);
			} else {
				log.debug("update this user in jboss portal database");
				this.jbossPortalUserServiceImpl.updateUser(profile);
			}
			// Check the role for user
			// remove all roles from jboss database for this user
			List<RoleCategory> roleCategories = jbossPortalUserServiceImpl.getRoleCategoryByUserId(userId);
			if (roleCategories != null) {
				log.debug("remove all role relationship in jboss portal database first");
				for (RoleCategory roleCategory : roleCategories) {
					if (roleCategory != null) {
						jbossPortalUserServiceImpl.removeRole(userId, roleCategory.getRoleName());
					}
				}
			}
			roleCategories = this.userServiceImpl.getRoleCategoryByUserId(userId);

			if (roleCategories != null) {
				log.debug("add role relationship in jboss portal database first due to the relationship in storefront database");
				for (RoleCategory roleCategory : roleCategories) {
					if (roleCategory != null) {
						log.debug("add  role relationship[" + roleCategory.getRoleName() + "] in jboss portal database first");
						jbossPortalUserServiceImpl.assignRole(userId, roleCategory.getRoleName());
					}
				}
			}
			// Check the password updated or not

			log.debug("Check the password updated or not");
			syncAttributes = applicationService.getAttributeValue(userId, EntityType.USER, UserServiceImpl.TEMPPASSWORD);
			if (syncAttributes != null && syncAttributes.size() > 0) {
				log.debug("the password is updated, so need to sync to jboss portal database");
				String password = (String) syncAttributes.get(0).getValue();
				this.jbossPortalUserServiceImpl.updatePassword(userId, password);
			}

		}

	}

	public String validateLogin(String userId, String password) {
		String result = "";
		log.debug("vlidate the user[" + userId + "]");

		this.syncUserAndRole(userId);

		UserProfile userProfile = userService.getUser(userId);

		if (userProfile == null) {
			log.debug("can't find the user");
			return result;
		}
		// First, to check whether this account has been disabled over one
		// hour
		Boolean disabled = userProfile.getDisabled();
		if (disabled != null && disabled.booleanValue()) {
			log.debug("the user is disabled");
			// this account is disabled
			Date date = userProfile.getDisabledDate();
			if (date != null) {
				Calendar canlendar = Calendar.getInstance();
				canlendar.setTime(date);
				Calendar canlendarNow = Calendar.getInstance();
				if ((canlendarNow.getTimeInMillis() - canlendar.getTimeInMillis()) > (this.disabledPeriod * 1000)) {
					log.debug("the disabled duration is quite enough, over[" + disabledPeriod + "s], we just try to enable it automatically");
					// the disabled account is over the setting period, so
					// enable it

					userService.disableUser(userId, false);
				} else {
					log.debug("the disabled duration is not enough, less than[" + disabledPeriod + "s], we don't enable it automatically");
					// Since this account is disabled so far, dont need to
					// check the password again
					return result;
				}
			}

		}

		// Secondly, let system to check the password
		log.debug("Check the user's password is correct or not");
		if (userService.validatePassword(userId, password)) {
			// password is correct
			// remove the old attribute value
			log.debug("the password is correct, just remove the old info in system");
			this.applicationService.removeAttributes(userId, EntityType.USER, PASSWORD_FAILURE_COUNT);
			this.applicationService.removeAttributes(userId, EntityType.USER, PASSWORD_FAILURE_DATE);
			return result;
		}
		log.debug("the password is incorrect, try to get the history information");
		// check the attribute
		long passwordFailureCount = 0;
		Date passwordFailureDate = new Date();

		List<AttributeValue> attributeValues = this.applicationService.getAttributeValue(userId, EntityType.USER, PASSWORD_FAILURE_COUNT);
		if (attributeValues != null && attributeValues.size() != 0) {

			// Get the stored value
			AttributeValue attributeValue = attributeValues.get(0);
			log.debug("got the history information[" + attributeValue + "]");
			//
			List<AttributeValue> attributeDateValues = this.applicationService.getAttributeValue(userId, EntityType.USER, PASSWORD_FAILURE_DATE);

			if (attributeDateValues != null && attributeDateValues.size() != 0) {
				// Check the stored date
				AttributeValue attributeDateValue = attributeDateValues.get(0);
				log.debug("got the history information[" + attributeDateValue + "]");
				Date dateValue = (Date) attributeDateValue.getValue();
				if (dateValue != null) {
					Date current = new Date();
					// if the current date is in the setting period
					if ((current.getTime() - dateValue.getTime()) < this.passwordValidatePeriod * 1000) {
						if (attributeValue.getValue() != null) {
							passwordFailureCount = ((Float) attributeValue.getValue()).longValue();
							log.debug("since the check period is inner [" + passwordValidatePeriod + "s], just mark the failure count as ["
									+ passwordFailureCount + "]");
						}
						passwordFailureDate = dateValue;
					}
				}
			}
			this.applicationService.removeAttributes(userId, EntityType.USER, PASSWORD_FAILURE_COUNT);
			this.applicationService.removeAttributes(userId, EntityType.USER, PASSWORD_FAILURE_DATE);

		}
		passwordFailureCount = passwordFailureCount + 1;
		this.applicationService.addAttribute(userId, EntityType.USER, PASSWORD_FAILURE_COUNT, passwordFailureCount);
		this.applicationService.addAttribute(userId, EntityType.USER, PASSWORD_FAILURE_DATE, passwordFailureDate);

		log.debug("The current failure count is [" + passwordFailureCount + "]");
		log.debug("The begining failure date is [" + passwordFailureDate + "]");
		log.debug("check whether need to disable the account");
		// Check whether need to disable the account
		if (passwordFailureCount >= this.passwordValidateCount) {
			// disable this account
			log.debug("disable this account since the failure count is over[" + this.passwordValidateCount + "]");
			userService.disableUser(userId, true);
			// Once the account is disabled, reset the count
			log.debug("Once the account is disabled, reset the count");
			this.applicationService.removeAttributes(userId, EntityType.USER, PASSWORD_FAILURE_COUNT);
			this.applicationService.removeAttributes(userId, EntityType.USER, PASSWORD_FAILURE_DATE);
		}

		return result;
	}
}

// $Id$