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
package com.hp.sdf.ngp.service.administration;

import java.io.InputStream;
import java.sql.SQLException;

import javax.annotation.Resource;

import junit.framework.Assert;

import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.ITable;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.hp.sdf.ngp.DBEnablerTestBase;
import com.hp.sdf.ngp.administration.AssetBatchUploader;
import com.hp.sdf.ngp.service.ApplicationService;

/**
 * 
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:META-INF/applicationContext-test.xml" })
public class AssetBatchUploadTestCase extends DBEnablerTestBase {

	@Resource
	AssetBatchUploader uploader;

	@Resource
	ApplicationService appService;

	@Override
	public String dataSetFileName() {
		return "/data_init.xml";
	}

	/**
	 * Test method for
	 * {@link com.hp.sdf.ngp.service.batchupload.descriptor.BatchUploadRepositoryImpl#load(java.io.File)}
	 * .
	 * 
	 * @throws Exception
	 * @throws SQLException
	 * @throws DataSetException
	 */
	@Test
	public void testCreateAsset() throws DataSetException, SQLException,
			Exception {
		InputStream input = this.getClass().getClassLoader()
				.getResourceAsStream("batchUploadtest.zip");
		uploader.batchUpload(input);

		ITable tableValue = databaseTester.getConnection().createQueryTable(
				"result", "select * from asset where name = 'test'");

		ITable tableValue2 = databaseTester.getConnection()
				.createQueryTable("result",
						"select * from assetbinaryversion where filename = 'app1.exe'");
		Assert.assertTrue(tableValue.getRowCount() > 0
				&& tableValue2.getRowCount() > 0);
	}

}

// $Id$