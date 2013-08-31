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

import com.hp.sdf.ngp.ui.dynamicForm.dataSource.VirtualSource;

public class DataField implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String name;
	private String modelType;
	private String dataType;
	private String compnentType;
	private Boolean required;
	private String sourcId;
	private VirtualSource dataSource;
	private String minLength;
	private String maxLength;
	private String imCheck;
	public String getMinLength() {
		return minLength;
	}

	public void setMinLength(String minLength) {
		this.minLength = minLength;
	}

	public String getMaxLength() {
		return maxLength;
	}

	public void setMaxLength(String maxLength) {
		this.maxLength = maxLength;
	}

	public String getSourcId() {
		return sourcId;
	}

	public void setSourcId(String sourcId) {
		this.sourcId = sourcId;
	}
	public VirtualSource getDataSource() {
		return dataSource;
	}

	public void setDataSource(VirtualSource dataSource) {
		this.dataSource = dataSource;
	}

	public Boolean getRequired() {
		return required;
	}

	public void setRequired(Boolean required) {
		this.required = required;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getModelType() {
		return modelType;
	}

	public void setModelType(String modelType) {
		this.modelType = modelType;
	}

	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	public String getCompnentType() {
		return compnentType;
	}

	public void setCompnentType(String compnentType) {
		this.compnentType = compnentType;
	}

	public String getImCheck() {
		return imCheck;
	}

	public void setImCheck(String imCheck) {
		this.imCheck = imCheck;
	}

}

// $Id$