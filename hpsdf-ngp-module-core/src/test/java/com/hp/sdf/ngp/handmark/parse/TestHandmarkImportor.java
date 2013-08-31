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
package com.hp.sdf.ngp.handmark.parse;

import java.io.InputStream;
import java.sql.SQLException;

import javax.annotation.Resource;
import javax.xml.bind.JAXBException;

import junit.framework.Assert;

import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.ITable;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.hp.sdf.ngp.DBEnablerTestBase;
import com.hp.sdf.ngp.handmark.AssetImportor;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:META-INF/applicationContext-test.xml" })
public class TestHandmarkImportor extends DBEnablerTestBase {

	@Resource
	private AssetImportor assetImportor;

	@Test
	public void testHandmarkImport() {

		InputStream masterXMLInputStream = this.getClass().getClassLoader()
				.getResourceAsStream("master_feed_small.xml");
		InputStream deviceXMLInputStream = this.getClass().getClassLoader()
				.getResourceAsStream("device_feed.xml");

		// import the 6 handmark products
		try {
			assetImportor.importAsset(masterXMLInputStream, deviceXMLInputStream);
		} catch (JAXBException e) {
			e.printStackTrace();
		}

		try {
			Thread.sleep(6000);
		} catch (InterruptedException exception) {
			exception.printStackTrace();
		}

		try {
			ITable tableValue = databaseTester.getConnection()
					.createQueryTable("result", "select * from asset ");

			Assert.assertTrue(tableValue.getRowCount() == 9);

		} catch (DataSetException exception) {
			exception.printStackTrace();
		} catch (SQLException exception) {
			exception.printStackTrace();
		} catch (Exception exception) {
			exception.printStackTrace();
		}

	}

	@Override
	public String dataSetFileName() {
		return "/data_init.xml";
	}
}