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

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;

import junit.framework.Assert;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.hp.sdf.ngp.DBEnablerTestBase;
import com.hp.sdf.ngp.common.exception.SGFCallingFailureException;
import com.hp.sdf.ngp.model.Service;
import com.hp.sdf.ngp.service.ApiService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:META-INF/applicationContext-test.xml" })
public class ApiManagerTest extends DBEnablerTestBase {
	
	@Autowired
	private ApiManager apiManager;

	@Autowired
	private ApiService apiService;
	
	@Override
	public String dataSetFileName() {
		return "/data_init.xml";
	}

	public void synchronizeSGFAPI() throws SGFCallingFailureException {
		this.apiManager.synchronizeSGFAPI();
//		synchronized (this) {
//			try {
//				wait(10000);
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
//		}
		Assert.assertTrue(true);
	}
	
	@Test
	@Ignore
	public void batchUpload(){
		FileInputStream fis = null;
		try {
			fis = new FileInputStream("src/test/resources/batchtest.zip");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		this.apiManager.batchUpload(fis);

		List<Service> sgf = this.apiService.findServiceListByName("SGF_API_1", 0, Integer.MAX_VALUE);
		if(sgf == null || sgf.size() == 0){
			Assert.assertTrue(false);
			return;
		}
		List<Service> common = this.apiService.findServiceListByName("COMMON_API", 0, Integer.MAX_VALUE);
		if(common == null || common.size() == 0)
			Assert.assertTrue(false);
		else
			Assert.assertTrue(((Service)common.get(0)).getDescription().equals("updated description"));
	}

}

// $Id$