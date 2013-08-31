/*
 * JBoss, Home of Professional Open Source
 * Copyright 2005, JBoss Inc., and individual contributors as indicated
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package com.hp.sdf.ngp.workflow.jbpm;

import javax.annotation.Resource;

import junit.framework.Assert;

import org.jbpm.api.ExecutionService;
import org.jbpm.api.ProcessEngine;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.hp.sdf.ngp.DBEnablerTestBase;
import com.hp.sdf.ngp.service.ApplicationService;

/**
 * @author zhoude
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:META-INF/applicationContext-test.xml" })
public class AssetLifeCycleFlowTest extends DBEnablerTestBase {
	String taskId;
	String insId;
	String deploymentId;
	@Resource
	ProcessEngine processEngine;

	public void setUp() throws Exception {
		super.setUp();
	}

	@Test
	public void testJbpmInited() {
		ExecutionService executionService = processEngine.getExecutionService();

		Assert.assertNotNull(executionService);

	}

 

	@Override
	public String dataSetFileName() {
		return "/data_init.xml";
	}

}
