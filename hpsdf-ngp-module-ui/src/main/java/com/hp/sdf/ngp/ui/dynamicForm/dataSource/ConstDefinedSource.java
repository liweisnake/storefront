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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Retrieve data from const-map definition as {Id<Long> & value<String>}<br>
 * sample: Map<Integer, String> genderMap = Gender.getGenderMap();
 */
@SuppressWarnings("unchecked")
public class ConstDefinedSource extends VirtualSource implements GetIdListInterface, GetValueMapInterface {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3906529314272607693L;

	String idMethodName;

	String methodName;
	
	String valueMethodName;

	String holderName;

	private Log log = LogFactory.getLog(ConstDefinedSource.class);

	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	public String getHolderName() {
		return holderName;
	}

	public void setHolderName(String holderName) {
		this.holderName = holderName;
	}

	public ConstDefinedSource() {
		super();
	}

	public ConstDefinedSource(String holderName, String idMethodName, String valueMethodName) {
		super();
		setIdMethodName(idMethodName);
		setHolderName(holderName);
		setValueMethodName(valueMethodName);
	}

	public String getIdMethodName() {
		return idMethodName;
	}

	public void setIdMethodName(String idMethodName) {
		this.idMethodName = idMethodName;
	}

	public String getValueMethodName() {
		return valueMethodName;
	}

	public void setValueMethodName(String valueMethodName) {
		this.valueMethodName = valueMethodName;
	}

	@Override
	public List<?> getDataList() {
		try {
			Method keyMethod = holderName.trim().getClass().getMethod(methodName.trim(), new Class[0]);
			Object keyObject = keyMethod.invoke(holderName.trim(), new Object[0]);
			return (List<?>) keyObject;
		} catch (Exception ex) {
			log.error(ex);
		}
		return new ArrayList();
	}

	public List<?> getIdList() {

		try {
			Class clazz= Class.forName(holderName.trim());
			Method keyMethod = clazz.getMethod(idMethodName.trim(), new Class[0]);
			Object keyObject = keyMethod.invoke(clazz, new Object[0]);
			return (List<?>) keyObject;
		} catch (Exception ex) {
			log.error(ex);
		}
		return new ArrayList();
	}

	
	public Map<?, ?> getValueMap() {
		try {
			Class clazz= Class.forName(holderName.trim());
			Method keyMethod = clazz.getMethod(valueMethodName.trim(), new Class[0]);
			Object keyObject = keyMethod.invoke(clazz, new Object[0]);
			return (Map) keyObject;
		} catch (Exception ex) {
			log.error(ex);
		}
		return new HashMap();
	}
}

// $Id$