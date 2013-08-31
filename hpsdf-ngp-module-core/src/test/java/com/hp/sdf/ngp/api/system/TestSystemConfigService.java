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
package com.hp.sdf.ngp.api.system;

import javax.annotation.Resource;

import static junit.framework.Assert.*;
import org.dbunit.dataset.ITable;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.hp.sdf.ngp.DBEnablerTestBase;
import com.hp.sdf.ngp.api.exception.StoreClientEntityNotFoundException;
import com.hp.sdf.ngp.api.exception.StoreClientServiceException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:META-INF/applicationContext-test.xml" })
public class TestSystemConfigService extends DBEnablerTestBase {
	@Resource
	private SystemConfigService systemConfigService;

	@Override
	public String dataSetFileName() {
		return "/data_init.xml";
	}
	
	@Test
	public void testGetConfigValue() throws StoreClientServiceException {
		String value = systemConfigService.getConfigValue("default.pid.sbm");
		assertEquals(value, "SBM-PID");
	}
	
	@Test
	public void testSetConfigValue() throws Exception{
		systemConfigService.setConfigValue("key3", "value3");
		
		ITable tableValue = databaseTester.getConnection().createQueryTable(
				"result",
				"select a.* from SystemConfig a where a.configKey = 'key3' and value = 'value3' ");
		assertTrue(tableValue.getRowCount() == 1);
	}
	@Test(expected=StoreClientEntityNotFoundException.class)
	public void testGetConfigValueWithNull() throws Exception{
		 systemConfigService.getConfigValue("mytest");
	}
}

// $Id$