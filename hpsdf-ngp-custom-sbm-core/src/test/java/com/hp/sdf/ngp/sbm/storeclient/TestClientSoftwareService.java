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

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;

import java.io.File;
import java.io.FileInputStream;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.annotation.Resource;

import junit.framework.Assert;

import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.ITable;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.hp.sdf.ngp.DBEnablerTestBase;
import com.hp.sdf.ngp.api.exception.StoreClientServiceException;
import com.hp.sdf.ngp.custom.sbm.api.impl.model.ClientAppSettingImpl;
import com.hp.sdf.ngp.custom.sbm.api.impl.model.ClientApplicationImpl;
import com.hp.sdf.ngp.custom.sbm.api.storeclient.model.StoreClientSoftware;
import com.hp.sdf.ngp.custom.sbm.api.storeclient.service.ClientSoftwareService;
import com.hp.sdf.ngp.custom.sbm.storeclient.model.ClientAppSetting;
import com.hp.sdf.ngp.custom.sbm.storeclient.model.ClientApplication;
import com.hp.sdf.ngp.custom.sbm.storeclient.model.PurchaseHistoryExtend;
import com.hp.sdf.ngp.custom.sbm.storeclient.model.SecurityToken;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:META-INF/applicationContext-test.xml" })
public class TestClientSoftwareService extends DBEnablerTestBase {

	@Resource
	private ClientSoftwareService clientSoftwareService;

	private String testFileLocation = "src/test/resources/conf.properties";

	@Override
	public String dataSetFileName() {
		return "/data_init3.xml";
	}

	@Test
	public void testGetStoreClientSoftware() {
		try {
			StoreClientSoftware storeClientSoftware = clientSoftwareService.getLatestStoreClientSoftware("shaoping");

			assertNotNull(storeClientSoftware);

		} catch (StoreClientServiceException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testConstructClientApplication() {
		assertNotNull(clientSoftwareService.constructClientApplication());
	}

	@Test
	public void testSaveClientApplication() throws DataSetException, SQLException, Exception {

		ClientApplication clientApplication = new ClientApplication();
		clientApplication.setClientName("hello");
		clientApplication.setFileLocation("abc");
		clientApplication.setUpdateDate(new Date());
		clientApplication.setVersion("1");
		ClientApplicationImpl clientApplicationImpl = new ClientApplicationImpl(clientApplication);
		clientApplicationImpl.setFile("a", new FileInputStream(new File(testFileLocation)));

		clientSoftwareService.saveClientApplication(clientApplicationImpl);

		ITable tableValue = databaseTester.getConnection().createQueryTable("result", "select a.* from ClientApplication a where a.clientName = 'hello' and a.version = '1' ");
		assertTrue(tableValue.getRowCount() == 1);
	}

	@Test
	public void testDeleteClientApplication() throws DataSetException, SQLException, Exception {
		clientSoftwareService.deleteClientApplication(1L);

		ITable tableValue = databaseTester.getConnection().createQueryTable("result", "select a.* from ClientApplication a where a.ID = 1 ");
		assertTrue(tableValue.getRowCount() == 0);
	}

	@Test
	public void testGetClientApplication() throws StoreClientServiceException {
		com.hp.sdf.ngp.custom.sbm.api.storeclient.model.ClientApplication clientApp = clientSoftwareService.getClientApplication(1L);
		assertEquals(clientApp.getClientName(), "shaoping");
	}

	@Test
	public void testConstructClientAppSetting() {
		assertNotNull(clientSoftwareService.constructClientAppSetting());
	}

	@Test
	public void testSaveClientAppSetting() throws DataSetException, SQLException, Exception {
		ClientAppSetting clientAppSetting = new ClientAppSetting();
		clientAppSetting.setClientName("hello");
		clientAppSetting.setFileLocation("abc");
		clientAppSetting.setUpdateDate(new Date());
		clientAppSetting.setVersion("1");
		ClientAppSettingImpl clientAppSettingImpl = new ClientAppSettingImpl(clientAppSetting);
		clientAppSettingImpl.setFile("b", new FileInputStream(new File(testFileLocation)));

		clientSoftwareService.saveClientAppSetting(clientAppSettingImpl);

		ITable tableValue = databaseTester.getConnection().createQueryTable("result", "select a.* from ClientAppSetting a where a.clientName = 'hello' and a.version = '1' ");
		assertTrue(tableValue.getRowCount() == 1);
	}

	@Test
	public void testDeleteClientAppSetting() throws DataSetException, SQLException, Exception {
		clientSoftwareService.deleteClientAppSetting(1L);

		ITable tableValue = databaseTester.getConnection().createQueryTable("result", "select a.* from ClientAppSetting a where a.ID = 1 ");
		assertTrue(tableValue.getRowCount() == 0);
	}

	@Test
	public void testGetClientAppSetting() throws StoreClientServiceException {
		com.hp.sdf.ngp.custom.sbm.api.storeclient.model.ClientAppSetting clientAppSetting = clientSoftwareService.getClientAppSetting(1L);
		assertEquals(clientAppSetting.getClientName(), "shaoping");
	}

	@Test
	public void testGetStoreClientSoftwareWithWrongName() {
		try {
			StoreClientSoftware storeClientSoftware = clientSoftwareService.getLatestStoreClientSoftware("levi");

			assertNull(storeClientSoftware.getConfigFileLocation());
			assertNull(storeClientSoftware.getFileLocatioin());

		} catch (StoreClientServiceException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testModelInitialization() throws Exception{
		PurchaseHistoryExtend purchaseHistoryExtend=new PurchaseHistoryExtend();
		Date endDate=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse("9999-12-31 23:59:59");
		Assert.assertEquals(endDate, purchaseHistoryExtend.getReqconfirmDate());
		
		SecurityToken securityToken=new SecurityToken();
		Assert.assertEquals(0, securityToken.getLockFlag());
	}

}
