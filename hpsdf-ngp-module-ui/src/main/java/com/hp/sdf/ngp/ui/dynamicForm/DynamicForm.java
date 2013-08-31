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
package com.hp.sdf.ngp.ui.dynamicForm;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.hp.sdf.ngp.api.comon.EntityType;
import com.hp.sdf.ngp.eav.model.AttributeValue;
import com.hp.sdf.ngp.service.ApplicationService;
import com.hp.sdf.ngp.ui.common.Tools;
import com.hp.sdf.ngp.ui.dynamicForm.model.DyDataModel;
import com.hp.sdf.ngp.ui.dynamicForm.parser.FiledListParser;
import com.hp.sdf.ngp.ui.page.myportal.BreadCrumbForm;

public abstract class DynamicForm extends BreadCrumbForm {
	private static Log log = LogFactory.getLog(DynamicForm.class);
	/**
	 * holder for all EAV file name<->value<br>
	 * get precise dataType while processing onSubmit()
	 */
	protected Map<String, String> eavMap = new HashMap<String, String>();

	/**
	 * to save temporary Value like SelectOption list,<br>
	 * process each value while processing onSubmit()
	 */
	private Map<String, Object> temporaryValueMap = new HashMap<String, Object>();

	private String dataConfigFile;

	private Object domainModel = new HashMap<String, Object>();

	protected DyDataModel dyDataModel;

	private Object objectId;// for eav

	private EntityType entityType;// for eav

	@SpringBean
	private MyOwnFinderStreamLocator myOwnFinderStreamLocator;

	@SpringBean
	private ApplicationService applicationService;

	

	protected abstract Object getDomainModel();

	protected abstract String getDataConfigFile();

	/**
	 * generate dynamic content
	 */
	protected void genContent() {

		if (dataConfigFile == null)
			return;

		FiledListParser fp = new FiledListParser();
		String result;
		try {
			result = myOwnFinderStreamLocator.loadConfigurationFile(dataConfigFile);

			//
			dyDataModel = fp.unmarshal(result);

			//
			fp.parser(dyDataModel, DynamicForm.this, domainModel, eavMap, temporaryValueMap);

		} catch (Exception exception) {
			log.error(exception.getStackTrace());
		}

	}

	public DyDataModel getDyDataModel() {
		return dyDataModel;
	}

	

	public DynamicForm(String id) {
		super(id);
		// must set model&file first
		this.domainModel=getDomainModel();
		this.dataConfigFile=getDataConfigFile();
		
	}

	public String getAttributeValue(String key) {

		if (null == getObjectId() || null == getEntityType())
			return null;

		List<AttributeValue> attributeList = applicationService.getAttributeValue(getObjectId(), getEntityType(), key.toUpperCase());
		String value = StringUtils.EMPTY;
		if (attributeList != null && attributeList.size() > 0) {
			value = Tools.getValue(attributeList.get(0).getValue());
		}

		return value;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Object getObjectId() {
		return objectId;
	}

	public void setObjectId(Object objectId) {
		this.objectId = objectId;
	}

	public EntityType getEntityType() {
		return entityType;
	}

	public void setEntityType(EntityType entityType) {
		this.entityType = entityType;
	}

}

// $Id$