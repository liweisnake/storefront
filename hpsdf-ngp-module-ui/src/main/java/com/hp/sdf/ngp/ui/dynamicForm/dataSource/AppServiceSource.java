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

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Retrieve data from service bean as { Id<Long> & value<String> }<br>
 * sample: countryList=inforServiceImpl.getContrys()
 * 
 */

@SuppressWarnings("unchecked")
public class AppServiceSource extends VirtualSource  implements GetIdListInterface{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3906529314272607693L;

	String methodName;
	
	String idMethodName;
	
	public String getIdMethodName() {
		return idMethodName;
	}

	public void setIdMethodName(String idMethodName) {
		this.idMethodName = idMethodName;
	}

	String className;

	private Log log = LogFactory.getLog(AppServiceSource.class);

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	public AppServiceSource() {
		super();
	}

	public AppServiceSource(String className, String methodName) {
		super();
		setMethodName(methodName);
		setClassName(className);
	}

	@Override
	public List<?> getDataList() {
		Object service = this.getApplicationContext().getBean(className.trim());

		try {
			Method keyMethod = service.getClass().getMethod(methodName.trim(), new Class[0]);
			Object keyObject = keyMethod.invoke(service, new Object[0]);
			return (List<?>) keyObject;
		} catch (Exception ex) {
			log.error(ex);
		}
		return new ArrayList();
	}

	public List<?> getIdList() {
		Object service = this.getApplicationContext().getBean(className.trim());

		try {
			Method keyMethod = service.getClass().getMethod(idMethodName.trim(), new Class[0]);
			Object keyObject = keyMethod.invoke(service, new Object[0]);
			return (List<?>) keyObject;
		} catch (Exception ex) {
			log.error(ex);
		}
		return new ArrayList();
	}

}

// $Id$