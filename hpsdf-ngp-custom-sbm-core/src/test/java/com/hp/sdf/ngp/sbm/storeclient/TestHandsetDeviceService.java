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
package com.hp.sdf.ngp.sbm.storeclient;

import static junit.framework.Assert.assertTrue;

import java.util.List;

import javax.annotation.Resource;

import org.dbunit.dataset.ITable;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.hp.sdf.ngp.DBEnablerTestBase;
import com.hp.sdf.ngp.api.search.SearchExpression;
import com.hp.sdf.ngp.api.search.Condition.StringComparer;
import com.hp.sdf.ngp.custom.sbm.api.impl.model.HandSetDeviceImpl;
import com.hp.sdf.ngp.custom.sbm.api.storeclient.model.HandSetDevice;
import com.hp.sdf.ngp.custom.sbm.api.storeclient.service.HandsetDeviceService;
import com.hp.sdf.ngp.search.condition.handsetdevice.HandSetDeviceDeviceNameCondition;
import com.hp.sdf.ngp.search.condition.handsetdevice.HandSetDeviceFunctionFilterCondition;
import com.hp.sdf.ngp.search.engine.SearchExpressionImpl;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:META-INF/applicationContext-test.xml" })
public class TestHandsetDeviceService extends DBEnablerTestBase {

	@Resource
	private HandsetDeviceService handsetDeviceService;

	@Override
	public String dataSetFileName() {
		return "/data_init3.xml";
	}

	@Test
	public void testCountHandsetDevice() throws Exception {
	    SearchExpression searchExpression = new SearchExpressionImpl();
	    searchExpression.addCondition(new HandSetDeviceDeviceNameCondition("app",StringComparer.LIKE,false,false));
	    searchExpression.addCondition(new HandSetDeviceFunctionFilterCondition("filter",StringComparer.EQUAL,false,false));
	    
	    int count = handsetDeviceService.countHandsetDevice(searchExpression);
	    assertTrue(count == 2);
	}

	@Test
	public void testSearchHandsetDevice() throws Exception {
	    SearchExpression searchExpression = new SearchExpressionImpl();
        searchExpression.addCondition(new HandSetDeviceDeviceNameCondition("app",StringComparer.LIKE,false,false));
        searchExpression.addCondition(new HandSetDeviceFunctionFilterCondition("filter",StringComparer.EQUAL,false,false));
        
        List<HandSetDevice> handSetDevices = handsetDeviceService.searchHandsetDevice(searchExpression);
        assertTrue(handSetDevices.size() == 2);
	}

	@Test
	public void testSaveHandSetDevice() throws Exception {
		HandSetDevice handSetDevice = new HandSetDeviceImpl();
		handSetDevice.setDisplayName("jiangshaoping");
		handSetDevice.setResolutionFilter(5L);
		handSetDevice.addMimeType("app3");
		handSetDevice.addMimeType("app4");

		handsetDeviceService.saveHandSetDevice(handSetDevice);
		Long id = ((HandSetDeviceImpl) handSetDevice).getHandSetDevice().getId();

		ITable tableValue = databaseTester.getConnection().createQueryTable(
				"result",
				"select a.* from mimeType a where a.HANDSETID = " + id + " and a.type in ('app3','app4')");
		assertTrue(tableValue.getRowCount() == 2);
	}

	@Test
	public void testUpdateHandSetDevice() throws Exception {
		HandSetDevice handSetDevice = new HandSetDeviceImpl();
		handSetDevice.setDisplayName("jiangshaoping");
		handSetDevice.setResolutionFilter(5L);
		handSetDevice.addMimeType("app3");
		handSetDevice.addMimeType("app4");

		handsetDeviceService.saveHandSetDevice(handSetDevice);
		Long id = ((HandSetDeviceImpl) handSetDevice).getHandSetDevice().getId();

		ITable tableValue = databaseTester.getConnection().createQueryTable(
				"result",
				"select a.* from mimeType a where a.HANDSETID = " + id + " and a.type in ('app3','app4')");
		assertTrue(tableValue.getRowCount() == 2);

		HandSetDeviceImpl handSetDeviceImpl = (HandSetDeviceImpl) handSetDevice;
		handSetDeviceImpl.deleteMimeType("app3");
		handSetDeviceImpl.setDisplayName("jiang2");
		handsetDeviceService.updateHandSetDevice(handSetDeviceImpl);

		tableValue = databaseTester.getConnection().createQueryTable(
				"result",
				"select a.* from mimeType a where a.HANDSETID = " + id + " and a.type in ('app3','app4')");
		ITable tableValue2 = databaseTester.getConnection().createQueryTable(
				"result",
				"select a.* from HandSetDevice a where a.ID = " + id);
		assertTrue(tableValue.getRowCount() == 1);
		Assert.assertNotNull(tableValue2.getValue(0, "displayName"));
	}

	@Test
	public void testDeleteHandSetDeviceById() throws Exception {
	    handsetDeviceService.deleteHandSetDeviceById(2L);
	    
	    ITable tableValue = databaseTester.getConnection().createQueryTable(
                "result",
                "select a.* from handsetDevice a where a.id = 2");
        assertTrue(tableValue.getRowCount() == 0);
        
        tableValue = databaseTester.getConnection().createQueryTable(
                "result",
                "select a.* from mimeType a where a.HANDSETID = 2");
        assertTrue(tableValue.getRowCount() == 0);
	}

}
