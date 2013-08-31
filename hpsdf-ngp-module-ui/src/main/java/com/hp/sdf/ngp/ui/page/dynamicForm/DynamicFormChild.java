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
package com.hp.sdf.ngp.ui.page.dynamicForm;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.markup.html.form.Button;

import com.hp.sdf.ngp.ui.dynamicForm.DynamicForm;

public class DynamicFormChild extends DynamicForm {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static Log log = LogFactory.getLog(DynamicFormChild.class);

	private class ModelTest implements java.io.Serializable {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private String entityName = "checkUpdate";

		public String getEntityName() {
			return entityName;
		}

		public void setEntityName(String entityName) {
			this.entityName = entityName;
		}

	}

	public DynamicFormChild(String id) {
		super(id);

		genContent();
		// back button
		Button backBtn = new Button("addNew") {
			private static final long serialVersionUID = 8116120281966867260L;

			@Override
			public void onSubmit() {

				// TODO 分别对三个结构作处理

				log.debug("DynamicFormTest");
				log.warn(eavMap.entrySet().toString());

			}
		};
		add(backBtn);

	}

	/*
	 * must implemented to set domain model
	 */
	@Override
	protected Object getDomainModel() {
		return new ModelTest();
	}

	/*
	 * must implemented to set configuration file
	 */
	@Override
	protected String getDataConfigFile() {
		return "dyForm.xml";
	}

}

// $Id$