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
package com.hp.sdf.ngp.service;

import java.util.List;

import javax.annotation.Resource;

import junit.framework.Assert;

import org.dbunit.dataset.ITable;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.hp.sdf.ngp.DBEnablerTestBase;
import com.hp.sdf.ngp.model.Operation;
import com.hp.sdf.ngp.model.Service;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:META-INF/applicationContext-test.xml" })
public class TestApiService extends DBEnablerTestBase {

	@Resource
	private ApiService apiService;

	public ApiService getApiService() {
		return apiService;
	}

	public void setApiService(ApiService apiService) {
		this.apiService = apiService;
	}

	@Override
	public String dataSetFileName() {
		return "/data_init.xml";
	}

	@Test
	public void testFindServiceListById() throws Exception {
		Service list = apiService.findServiceById(1L, 0,
				Integer.MAX_VALUE);
		// ITable tableValue = databaseTester.getConnection().createQueryTable(
		// "result", "select * from app where name = 'APP5'");
		Assert.assertTrue(list!=null);
	}

	@Test
	public void testFindServiceListByName() {
		List<Service> list = apiService.findServiceListByName("LB_WLNG_NT_SendSms", 0,
				Integer.MAX_VALUE);
		Assert.assertTrue(list.size() == 1);
	}

	@Test
	public void testFindServiceOperation() {
		List<Operation> list = apiService.findServiceOperation(1L,
				"operation 1");
		Assert.assertTrue(list.size() == 1);
	}

	@Test
	public void testGetAllService() {
		List<Service> list = apiService.getAllService(0, Integer.MAX_VALUE);
		Assert.assertTrue(list.size() == 4);
	}
	
	@Test
	public void testGetSgfService() {
		List<Service> list = apiService.getSgfService(0, Integer.MAX_VALUE);
		Assert.assertTrue(list.size() == 3);
	}
	
	@Test
	public void testGetSgfServiceCount() {
		Long count = apiService.getSgfServiceCount();
		Assert.assertTrue(count == 3);
	}
	
	@Test
	public void testGetCommonService() {
		List<Service> list = apiService.getCommonService(0, Integer.MAX_VALUE);
		Assert.assertTrue(list.size() == 1);
	}
	
	@Test
	public void testGetCommonServiceCount() {
		Long count = apiService.getCommonServiceCount();
		Assert.assertTrue(count == 1);
	}

	@Test
	public void testSaveService() throws Exception {
		Service service = new Service();
		service.setName("my service");
		service.setServiceid("service5");
		apiService.saveService(service);
		ITable tableValue = databaseTester.getConnection().createQueryTable(
				"result",
				"select * from service where service.serviceid = 'service5'");
		Assert.assertTrue(tableValue.getRowCount() == 1);
	}
	
	@Test
	public void testUpdateService() throws Exception {
		Service service = new Service();
		service.setName("my service to update");
		service.setServiceid("123");
		apiService.saveService(service);
		Service service2 = apiService.findServiceListByName("my service to update", 0, Integer.MAX_VALUE).get(0);
		service2.setDocUrl("http://www.google.cn/123");
		apiService.updateService(service2);
		ITable tableValue = databaseTester.getConnection().createQueryTable(
				"result",
				"select * from service where service.docUrl = 'http://www.google.cn/123'");
		Assert.assertTrue(tableValue.getRowCount() == 1);
	}

	@Test
	public void testSaveServiceOperation() throws Exception {
		Operation operation = new Operation();
		operation.setName("op1");
		operation.setService(new Service(1L));
		apiService.saveServiceOperation(operation);
		ITable tableValue = databaseTester.getConnection().createQueryTable(
				"result",
				"select * from operation where operation.name = 'op1'");
		Assert.assertTrue(tableValue.getRowCount() == 1);
	}
	
	@Test
	public void testGetServicesByUserId() {
		List<Service> list = apiService.getServicesByUserId("levi");
//		System.out.println(list.size() + " ");
		Assert.assertTrue(list.size() == 1);
	}

	@Test
	public void testDeleteService() throws Exception {
		apiService.deleteService(3L);
		ITable tableValue = databaseTester.getConnection().createQueryTable(
				"result", "select * from service where id = 3");
		Assert.assertTrue(tableValue.getRowCount() <= 0);
	}
}

// $Id$