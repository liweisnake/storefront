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
package com.hp.sdf.ngp.api.impl.transaction;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;

import com.hp.sdf.ngp.api.comon.util.JndiUtil;

@Component
public class TransactionManagerFactoryImpl {

	private final static Log log = LogFactory
			.getLog(TransactionManagerFactoryImpl.class);

	@Resource(name = "transactionManager")
	private PlatformTransactionManager transactionManager;

	@PostConstruct
	protected void init() {
		log.info("The current transactionManager objecgt is ["
				+ transactionManager + "]");
		log.info("Inject it into the jndi["
				+ PlatformTransactionManager.class.getCanonicalName() + "]");
		JndiUtil.bind(PlatformTransactionManager.class.getCanonicalName(),
				transactionManager);
	}

	public PlatformTransactionManager getTransactionManager() {

		return transactionManager;
	}
}

// $Id$