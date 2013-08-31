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
package com.hp.sdf.ngp.workflow.impl;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import com.hp.sdf.ngp.common.annotation.Value;
import com.hp.sdf.ngp.common.exception.WorkFlowFailureException;
import com.hp.sdf.ngp.service.ApplicationService;
import com.hp.sdf.ngp.service.UserService;
import com.hp.sdf.ngp.workflow.WorkFlowRepository;
import com.hp.sdf.ngp.workflow.WorkFlowTransformer;
import com.hp.sdf.ngp.workflow.descriptor.WorkFlowDescriptor;

@Component
public class WorkFlowRepositoryImpl implements WorkFlowRepository,
		ApplicationContextAware {

	private final static Log log = LogFactory
			.getLog(WorkFlowRepositoryImpl.class);

	private WorkFlowDescriptor workFlowDescriptor = null;

	@Resource
	private WorkFlowTransformer workFlowTransformer;

	@Resource
	private ApplicationService applicationService;

	@Resource
	private UserService userService;

	private String filePath = "/opt/storefront/customize/workflow.xml";

	public String getFilePath() {
		return filePath;
	}

	@Value("workflow.filepath")
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public void setLocation(String location) {
		this.filePath = location;
	}

	private ApplicationContext applicationContext;

	/**
	 * Load the XML descriptor from local file
	 * 
	 * @return
	 */
	private String loadConfigurationFile() throws WorkFlowFailureException {

		StringBuffer buffer = new StringBuffer();
		try {
			FileInputStream fis = new FileInputStream(filePath);
			InputStreamReader isr = new InputStreamReader(fis, "UTF8");
			Reader in = new BufferedReader(isr);
			int ch;
			while ((ch = in.read()) > -1) {
				buffer.append((char) ch);
			}
			in.close();
			String result = buffer.toString();
			if (log.isDebugEnabled()) {
				log.debug("Load a work flow descriptor file:");
				log.debug(result);
			}
			return result;
		} catch (IOException e) {
			throw new WorkFlowFailureException(
					"cant read the work flow descriptor[" + filePath + "]", e);
		}

	}

	private void validateWorkFlow(WorkFlowDescriptor workFlowDescriptor)
			throws WorkFlowFailureException {
		
		workFlowDescriptor.setApplicationContext(applicationContext);
		workFlowDescriptor.setApplicationService(applicationService);
		workFlowDescriptor.setUserService(userService);
		
		workFlowDescriptor.validate();

	}

	private void initWorkFlow(WorkFlowDescriptor workFlowDescriptor) {
		workFlowDescriptor.init();
	}

	/**
	 * Load the work flow descriptor and performing necessary operations such as
	 * initial the database using loaded work flow. After the loading, it will
	 * notify the work flow engine about the loaded information
	 * 
	 * @WorkFlowFailureException throw exception if loaded fails
	 */
	public void load() throws WorkFlowFailureException {

		this.workFlowDescriptor = null;

		String xmlString = null;

		xmlString = loadConfigurationFile();

		WorkFlowDescriptor descriptor = workFlowTransformer
				.unmarshal(xmlString);

		// check the work flow descriptor
		try {
			this.validateWorkFlow(descriptor);

		} catch (Throwable e) {
			log.warn("The work flow descriptor is wrong", e);
			return;
		}

		this.workFlowDescriptor = descriptor;
		// initialize the database
		this.initWorkFlow(workFlowDescriptor);

	}

	public void validateWorkFlow(String workFlowDescriptor)
			throws WorkFlowFailureException {

		WorkFlowDescriptor descriptor = workFlowTransformer
				.unmarshal(workFlowDescriptor);

		
		// check the work flow descriptor
		this.validateWorkFlow(descriptor);
	}

	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		this.applicationContext = applicationContext;

	}

	public WorkFlowDescriptor getWorkFlowDescriptor() {
		return this.workFlowDescriptor;
	}

}

// $Id$