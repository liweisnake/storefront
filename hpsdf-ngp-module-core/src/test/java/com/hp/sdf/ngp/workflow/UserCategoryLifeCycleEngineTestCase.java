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
package com.hp.sdf.ngp.workflow;

import javax.annotation.Resource;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.hp.sdf.ngp.DBEnablerTestBase;
import com.hp.sdf.ngp.service.UserService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:META-INF/applicationContext-test.xml" })
public class UserCategoryLifeCycleEngineTestCase extends DBEnablerTestBase {

	@Resource
	private WorkFlowRepository workFlowRepository;

	@Resource
	private UserCategoryLifeCycleEngine userCategoryLifeCycleEngine;

	@Resource
	private UserService userService;

	protected void onSetUp() {
		workFlowRepository
				.setLocation("src/test/resources/UserCategoryLifeCycleEngineTestCase.xml");
		workFlowRepository.load();
		userCategoryLifeCycleEngine.setWorkFlowDescriptor(workFlowRepository
				.getWorkFlowDescriptor());
	};

	@Test
	public void testCondition() throws Exception {
	
		
		Assert.assertTrue(userCategoryLifeCycleEngine.getAttributeValue(
				"Seniordeveloper", "SGF_domain").equals("sandbox"));
		String[] privileges = userCategoryLifeCycleEngine
				.getUserPrivileges("levi");
		int match = 0;
		for (String privilege : privileges) {
			if (privilege.equals("onboard")) {
				match++;
				continue;
			}
			if (privilege.equals("new_download_Developer")) {
				match++;
				continue;
			}
			if (privilege.equals("owner[levi]")) {
				match++;
				continue;
			}
		}
		Assert.assertTrue(match == 3);
		Assert.assertTrue(privileges != null);
		

	}

	@Override
	public String dataSetFileName() {
		return "/data_init.xml";
	}

}

// $Id$