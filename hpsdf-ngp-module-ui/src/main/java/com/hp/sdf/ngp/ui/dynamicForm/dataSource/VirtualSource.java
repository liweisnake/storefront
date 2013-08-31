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
package com.hp.sdf.ngp.ui.dynamicForm.dataSource;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class VirtualSource implements ApplicationContextAware, java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3737551462761040433L;

	/**
	 *save spring context for all children getting bean
	 */
	protected static ApplicationContext applicationContext;

	public String sourcId;
	String keyName;

	String valueName;

	public String getKeyName() {
		return keyName;
	}

	public void setKeyName(String keyName) {
		this.keyName = keyName;
	}

	public String getValueName() {
		return valueName;
	}

	public void setValueName(String valueName) {
		this.valueName = valueName;
	}

	public String getSourcId() {
		return sourcId;
	}

	public void setSourcId(String sourcId) {
		this.sourcId = sourcId;
	}

	public ApplicationContext getApplicationContext() {
		return VirtualSource.applicationContext;
	}

	/**
	 * @must implemented by children class <br>
	 *       get dataList from server.method by reflect
	 * 
	 * @return
	 */
	public List<?> getDataList() {
		return new ArrayList<String>();
	}

	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		VirtualSource.applicationContext = applicationContext;
	}

}

// $Id$