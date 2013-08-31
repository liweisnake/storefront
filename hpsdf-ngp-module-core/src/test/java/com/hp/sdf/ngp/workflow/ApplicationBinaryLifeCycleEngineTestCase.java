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
import com.hp.sdf.ngp.service.ApplicationService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:META-INF/applicationContext-test.xml" })
public class ApplicationBinaryLifeCycleEngineTestCase extends DBEnablerTestBase {

	@Resource
	private WorkFlowRepository workFlowRepository;

	@Resource
	private AssetBinaryVersionLifeCycleEngine applicationBinaryLifeCycleEngine;
	
	@Resource
	private ApplicationService  applicationService;

	protected void onSetUp() {
		workFlowRepository
				.setLocation("src/test/resources/ApplicationBinaryLifeCycleEngineTestCase.xml");
		workFlowRepository.load();
		applicationBinaryLifeCycleEngine
				.setWorkFlowDescriptor(workFlowRepository
						.getWorkFlowDescriptor());
	};

	@Test
	public void testCondition() throws Exception {
		

		

		String[] privileges = applicationBinaryLifeCycleEngine
				.getAccessPrivilege(1, AccessType.DOWNLOAD);
		int match = 0;
		for (String privilege : privileges) {
			if (privilege.equals(Privilege.Construct.OwnerPrivilege("levi"))) {
				match++;
				continue;
			}
			if (privilege.equals(Privilege.Construct.ApplicationPrivilege(
					"testing", AccessType.DOWNLOAD.toString(), "developer"))) {
				match++;
				continue;
			}

		}
		Assert.assertTrue(match == 2);
		

	}

	@Override
	public String dataSetFileName() {
		return "/data_init.xml";
	}

}

// $Id$