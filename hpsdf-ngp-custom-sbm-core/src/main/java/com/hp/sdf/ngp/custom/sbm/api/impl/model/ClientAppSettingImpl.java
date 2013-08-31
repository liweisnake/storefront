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

import com.hp.sdf.ngp.custom.sbm.storeclient.model.ClientAppSetting;

public class ClientAppSettingImpl implements com.hp.sdf.ngp.custom.sbm.api.storeclient.model.ClientAppSetting, Serializable {

	private static final long serialVersionUID = 1186859008841727269L;

	private final static Log log = LogFactory.getLog(ClientAppSettingImpl.class);

	private ClientAppSetting clientAppSetting;

	private byte[] content;

	private String fileName;

	public String getFileName() {
		return fileName;
	}

	public byte[] getContent() {
		return content;
	}

	public ClientAppSettingImpl() {
		clientAppSetting = new ClientAppSetting();
	}

	public ClientAppSettingImpl(ClientAppSetting clientAppSetting) {
		this.clientAppSetting = clientAppSetting;
		if (null != this.clientAppSetting) {
			this.clientAppSetting.getClientName();//load information to avoid the lazy load
		}
	}

	public String getClientName() {
		return clientAppSetting.getClientName();
	}

	public ClientAppSetting getClientAppSetting() {
		return clientAppSetting;
	}

	public String getFileLocation() {
		return clientAppSetting.getFileLocation();
	}

	public long getId() {
		return clientAppSetting.getId();
	}

	public String getVersion() {
		return clientAppSetting.getVersion();
	}

	public void setClientName(String clientName) {
		// log.debug("clientName:" + clientName);
		clientAppSetting.setClientName(clientName);
	}

	public void setVersion(String version) {
		// log.debug("version:" + version);
		clientAppSetting.setVersion(version);
	}

	public void setFile(String fileName, InputStream inputStream) {
		// log.debug("inputStream:" + inputStream);
		try {
			this.fileName = fileName;
			clientAppSetting.setFileLocation(fileName);
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
		return "ClientAppSetting[clientName=" + getClientName() + ",version=" + getVersion() + ",fileName=" + getFileName() + ",inputStream="
				+ getContent() + "]";
	}

}
