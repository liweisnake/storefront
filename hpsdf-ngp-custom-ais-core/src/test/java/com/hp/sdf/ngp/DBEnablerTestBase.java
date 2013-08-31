/*
 * Copyright (c) 2007 Hewlett-Packard Company, All Rights Reserved.
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
package com.hp.sdf.ngp;

import org.dbunit.IDatabaseTester;
import org.dbunit.JdbcDatabaseTester;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.XmlDataSet;
import org.junit.After;
import org.junit.Before;

public abstract class DBEnablerTestBase {

	protected IDatabaseTester databaseTester = null;

	public abstract String dataSetFileName();


	protected void onSetUp() {
	};

	protected void onTearDown() {
	};

	@Before
	public void setUp() throws Exception {
		// initialize your dataset here
		databaseTester = new JdbcDatabaseTester("org.hsqldb.jdbcDriver",
				"jdbc:hsqldb:.", "sa", "");
		IDataSet dataSet = new XmlDataSet(getClass().getResourceAsStream(
				dataSetFileName()));
		databaseTester.setDataSet(dataSet);
		databaseTester.onSetup();
		onSetUp();

		

	}

	@After
	public void tearDown() throws Exception {
		onTearDown();

		//used to resolve the lazy initial problem
		
	}
}

// $Id$