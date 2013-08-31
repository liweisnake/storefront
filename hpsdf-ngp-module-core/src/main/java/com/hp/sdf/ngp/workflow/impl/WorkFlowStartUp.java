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

import java.util.Iterator;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import com.hp.sdf.ngp.common.exception.WorkFlowFailureException;
import com.hp.sdf.ngp.workflow.WorkFlowEngine;
import com.hp.sdf.ngp.workflow.WorkFlowRepository;
import com.hp.sdf.ngp.workflow.descriptor.WorkFlowDescriptor;

@Component
public class WorkFlowStartUp implements ApplicationContextAware {

	private final static Log log = LogFactory.getLog(WorkFlowStartUp.class);

	private ApplicationContext applicationContext;

	@Resource
	private WorkFlowRepository workFlowRepository;

	@PostConstruct
	public void startUp() throws WorkFlowFailureException {
		log.info("Start up the work flow engine");
		workFlowRepository.load();
		notifyWorkFlowEngine(workFlowRepository.getWorkFlowDescriptor());

	}

	private void notifyWorkFlowEngine(WorkFlowDescriptor workFlowDescriptor) {

		Map<?, ?> components = applicationContext
				.getBeansOfType(WorkFlowEngine.class);
		if (components != null) {
			Iterator<?> iter = components.entrySet().iterator();
			while (iter.hasNext()) {
				Map.Entry<?, ?> entry = (Map.Entry<?, ?>) iter.next();
				WorkFlowEngine object = (WorkFlowEngine) entry.getValue();
				log.info("Notify the work flow engine["
						+ object.getClass().getCanonicalName() + "]");
				object.setWorkFlowDescriptor(workFlowDescriptor);
			}
		}
	}

	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		this.applicationContext = applicationContext;

	}

}

// $Id$