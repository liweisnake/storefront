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
package com.hp.sdf.ngp.workflow.jbpm;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javax.annotation.PostConstruct;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jbpm.api.ExecutionService;
import org.jbpm.api.HistoryService;
import org.jbpm.api.IdentityService;
import org.jbpm.api.ManagementService;
import org.jbpm.api.ProcessEngine;
import org.jbpm.api.RepositoryService;
import org.jbpm.api.TaskService;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import com.hp.sdf.ngp.common.annotation.Value;

@Component
public class JbpmServiceHolder implements ApplicationContextAware {
	private final static Log log = LogFactory.getLog(JbpmServiceHolder.class);
	private ApplicationContext applicationContext;
	protected ProcessEngine processEngine;
	public static RepositoryService repositoryService;
	public static ExecutionService executionService;
	public static ManagementService managementService;
	public static TaskService taskService;
	public static HistoryService historyService;
	public static IdentityService identityService;
	private String ConfFilePath = "";

	@PostConstruct
	public void startUp() {

		processEngine = (ProcessEngine) applicationContext
				.getBean("processEngine");
		repositoryService = processEngine.get(RepositoryService.class);
		executionService = processEngine.getExecutionService();
		historyService = processEngine.getHistoryService();
		managementService = processEngine.getManagementService();
		taskService = processEngine.getTaskService();
		identityService = processEngine.getIdentityService();

		try {
			String deploymentId = repositoryService.createDeployment()
					.addResourceFromInputStream(
							"userPromotion.jpdl.xml",
							new FileInputStream(ConfFilePath
									+ "userPromotion.jpdl.xml")).deploy();

			String binDeploymentId = repositoryService.createDeployment()
					.addResourceFromInputStream(
							"binaryLifeCycle.jpdl.xml",
							new FileInputStream(ConfFilePath
									+ "binaryLifeCycle.jpdl.xml")).deploy();

			String roleDeploymentId = repositoryService.createDeployment()
					.addResourceFromInputStream(
							"userRole.jpdl.xml",
							new FileInputStream(ConfFilePath
									+ "userRole.jpdl.xml")).deploy();

			log.info("JbpmServiceHolder deployed userPromotion ,id="
					+ deploymentId + ";binaryLifeCycle,id="
					+ binDeploymentId + ";roleDeploymentId,id="
					+ roleDeploymentId);
		} catch (FileNotFoundException e) {

			log.error("no file " + ConfFilePath, e);
		}
	}

	@Value("workflow.jbpm.configPath")
	public void setConfFilePath(String filepath) {
		this.ConfFilePath = filepath;
	}

	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		this.applicationContext = applicationContext;
	}
}
