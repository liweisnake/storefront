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
package com.hp.sdf.ngp.manager;

import javax.annotation.Resource;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.hp.sdf.ngp.DBEnablerTestBase;
import com.hp.sdf.ngp.manager.impl.UserManagerImpl;
import com.hp.sdf.ngp.model.UserProfile;
import com.hp.sdf.ngp.service.UserService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:META-INF/applicationContext-test.xml" })
public class TestUserManager extends DBEnablerTestBase {

	@Resource
	private UserManager userManager;

	@Resource
	private UserService userService;

	@Override
	public String dataSetFileName() {
		return "/data_init.xml";
	}

	@Test
	public void testValidateLogin() throws Exception {
		UserProfile userProfile = new UserProfile();
		userProfile.setUserid("TesterLiang");
		userProfile.setEmail("sdf@sdfhp.com");
		userProfile.setPassword("liang");
		userService.saveUser(userProfile);
		userManager.validateLogin("TesterLiang", "");
		userManager.validateLogin("TesterLiang", "");
		userManager.validateLogin("TesterLiang", "");
		Assert.assertTrue(userService.getUser("TesterLiang").getDisabled() == true);
		((UserManagerImpl) userManager).setDisabledPeriod(1);
		synchronized (this) {
			this.wait(2000);
		}
		userManager.validateLogin("TesterLiang", "");
		Assert.assertTrue(userService.getUser("TesterLiang").getDisabled() == false);
	}

}

// $Id$