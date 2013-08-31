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

import javax.annotation.Resource;

import org.dbunit.IDatabaseTester;
import org.dbunit.JdbcDatabaseTester;
import org.dbunit.dataset.DefaultDataSet;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.XmlDataSet;
import org.junit.After;
import org.junit.Before;
import org.springframework.stereotype.Component;

import com.hp.sdf.ngp.common.annotation.Value;

@Component
class TestBaseConfigure {

	private String driverClass = "org.hsqldb.jdbcDriver";
	private String connectionUrl = "jdbc:hsqldb:.";
	private String username = "sa";
	private String password = "";

	private boolean loadinitialdata = true;

	public boolean isLoadinitialdata() {
		return loadinitialdata;
	}

	@Value("hibernate.loadinitialdata")
	public void setLoadinitialdata(boolean loadinitialdata) {
		this.loadinitialdata = loadinitialdata;
	}

	public String getConnectionUrl() {
		return connectionUrl;
	}

	@Value("test.database.connectionurl")
	public void setConnectionUrl(String connectionUrl) {
		this.connectionUrl = connectionUrl;
	}

	public String getDriverClass() {
		return driverClass;
	}

	@Value("test.database.driverclass")
	public void setDriverClass(String driverClass) {
		this.driverClass = driverClass;
	}

	public String getUsername() {
		return username;
	}

	@Value("test.database.username")
	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	@Value("test.database.password")
	public void setPassword(String password) {
		this.password = password;
	}
}

public abstract class DBEnablerTestBase {

	@Resource
	private TestBaseConfigure testBaseConfigure;

	protected IDatabaseTester databaseTester = null;

	public abstract String dataSetFileName();

	protected void onSetUp() {
	};

	protected void onTearDown() {
	};

	@Before
	public void setUp() throws Exception {
		// initialize your dataset here
		databaseTester = new JdbcDatabaseTester(testBaseConfigure.getDriverClass(), testBaseConfigure.getConnectionUrl(), testBaseConfigure
				.getUsername(), testBaseConfigure.getPassword());
		if (testBaseConfigure.isLoadinitialdata()) {
			IDataSet dataSet = new XmlDataSet(getClass().getResourceAsStream(dataSetFileName()));
			databaseTester.setDataSet(dataSet);
		}
		else
		{
			IDataSet dataSet = new DefaultDataSet();
			databaseTester.setDataSet(dataSet);
		}
		databaseTester.onSetup();
		onSetUp();

	}

	@After
	public void tearDown() throws Exception {
		onTearDown();

		// used to resolve the lazy initial problem

	}
}

// $Id$