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
package com.hp.sdf.ngp.ui.dynamicForm.model;

import org.apache.commons.lang.StringUtils;

import com.hp.sdf.ngp.ui.dynamicForm.dataSource.VirtualSource;

public class DyDataModel implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private DataField[] dataFields;//

	private VirtualSource[] dataSources;//
	
	private Boolean labled=false;
	
	public Boolean getLabled() {
		return labled;
	}

	public void setLabled(Boolean labled) {
		this.labled = labled;
	}

	public VirtualSource[] getDataSources() {
		return dataSources;
	}

	public void setDataSources(VirtualSource[] dataSources) {
		this.dataSources = dataSources;
	}

	private String modelClassName;

	public String getModelClassName() {
		return modelClassName;
	}

	public void setModelClassName(String modelClassName) {
		this.modelClassName = modelClassName;
	}

	public DataField[] getDataFields() {
		return dataFields;
	}

	public void setDataFields(DataField[] dataFields) {
		this.dataFields = dataFields;
	}

	/**
	 * @param sourceId
	 * @return null if no source with the sourceId
	 */
	public VirtualSource getSource(String sourceId) {
		for (VirtualSource vs : dataSources) {
			if (vs.getSourcId().equalsIgnoreCase(sourceId))
				return vs;
		}
		return null;
	}

	/**for eav process according data type 
	 * @param keyName
	 * @return
	 */
	public String getDataFieldType(String keyName) {
		if (StringUtils.isEmpty(keyName))
			return "";

		for (int i = 0; i < dataFields.length; i++) {
			if (dataFields[i].getName().equalsIgnoreCase(keyName))
				return dataFields[i].getDataType();
		}

		return "";
	}
}

// $Id$