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
package com.hp.sdf.ngp.ui.mock;

import java.io.InputStream;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.hp.sdf.ngp.common.exception.BatchUploadFailureException;
import com.hp.sdf.ngp.common.exception.SGFCallingFailureException;
import com.hp.sdf.ngp.manager.ApiManager;
import com.hp.sdf.ngp.model.ApiKey;
import com.hp.sdf.ngp.model.Service;

public class ApiManagerMock implements ApiManager {

	public final static Log log = LogFactory.getLog(ApiManagerMock.class);

	public void synchronizeSGFAPI() throws SGFCallingFailureException {
		return;
	}

	public void batchUpload(InputStream is) throws BatchUploadFailureException {
		// TODO Auto-generated method stub

	}

	public void subscribe(ApiKey apiKey, List<Service> services, String userID) throws SGFCallingFailureException {
		log.debug("Start ApiManager Mock");
	}

}

// $Id$