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
package com.hp.sdf.ngp.custom.sbm.api.impl.model;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.hp.sdf.ngp.custom.sbm.storeclient.model.ClientApplication;

public class ClientApplicationImpl implements com.hp.sdf.ngp.custom.sbm.api.storeclient.model.ClientApplication, Serializable {

	private static final long serialVersionUID = 8150235346940739811L;

	private final static Log log = LogFactory.getLog(ClientApplicationImpl.class);

	private ClientApplication clientApplication;

	private byte[] content;

	private String fileName;

	public ClientApplicationImpl() {
		clientApplication = new ClientApplication();
	}

	public ClientApplicationImpl(ClientApplication clientApplication) {
		this.clientApplication = clientApplication;
		if (null != this.clientApplication) {
			this.clientApplication.getClientName();//load information to avoid the lazy load
		}
		
	}

	public String getFileName() {
		return fileName;
	}

	public byte[] getContent() {
		return content;
	}

	public ClientApplication getClientApplication() {
		return clientApplication;
	}

	public String getFileLocation() {
		return clientApplication.getFileLocation();
	}

	public String getClientName() {
		return clientApplication.getClientName();
	}

	public void setClientName(String clientName) {
		// log.debug("clientName:" + clientName);
		clientApplication.setClientName(clientName);
	}

	public long getId() {
		Long id = clientApplication.getId();
		if (null != id) {
			return id;
		}
		return 0;
		// throw new RuntimeException();
	}

	public void setId(long id) {
		// log.debug("id:" + id);
		clientApplication.setId(id);
	}

	public String getVersion() {
		return clientApplication.getVersion();
	}

	public void setVersion(String version) {
		// log.debug("version:" + version);
		clientApplication.setVersion(version);
	}

	public void setFile(String fileName, InputStream inputStream) {
		// log.debug("inputStream:" + inputStream);
		try {
			this.fileName = fileName;
			clientApplication.setFileLocation(fileName);
			if (inputStream == null) {
				log.debug("the inputStream is null");
				return;
			}
			content = new byte[inputStream.available()];
			inputStream.read(content);
		} catch (IOException e) {
			log.error("Comment exception: " + e);
		}
	}

	@Override
	public String toString() {
		return "ClientApplication[clientName=" + getClientName() + ",id=" + getId() + ",version:" + getVersion() + ",fileName=" + getFileName()
				+ ",inputStream=" + getContent() + "]";
	}
}
