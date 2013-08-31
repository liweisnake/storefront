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
package com.hp.sdf.ngp.common.mock;

import javax.annotation.PostConstruct;

import org.dbunit.IDatabaseTester;
import org.dbunit.JdbcDatabaseTester;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.XmlDataSet;

import com.hp.sdf.ngp.common.annotation.Value;

public class HSQLDBLoader {
	private IDatabaseTester databaseTester = null;
	private String fileName;
	
	private String driverClass="org.hsqldb.jdbcDriver";
	private String connectionUrl="jdbc:hsqldb:.";
	private String username="sa";
	private String password="";

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

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	@PostConstruct
	public void load() throws Exception {
		databaseTester = new JdbcDatabaseTester(this.getDriverClass(), this.getConnectionUrl(), this.getUsername(), this.getPassword());
		IDataSet dataSet = new XmlDataSet(getClass().getResourceAsStream(
				fileName));
		databaseTester.setDataSet(dataSet);
		databaseTester.onSetup();
	}
}

// $Id$