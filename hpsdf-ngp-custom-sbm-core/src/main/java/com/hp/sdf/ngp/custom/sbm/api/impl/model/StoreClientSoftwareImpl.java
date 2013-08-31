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

import java.io.Serializable;
import java.util.Date;

import com.hp.sdf.ngp.custom.sbm.storeclient.model.ClientAppSetting;
import com.hp.sdf.ngp.custom.sbm.storeclient.model.ClientApplication;

public class StoreClientSoftwareImpl implements com.hp.sdf.ngp.custom.sbm.api.storeclient.model.StoreClientSoftware, Serializable {

	private static final long serialVersionUID = -547058577922926769L;

	public ClientAppSetting getClientAppSetting() {
		if (null == clientAppSetting) {
			clientAppSetting = new ClientAppSetting();
		}
		return clientAppSetting;
	}

	public ClientApplication getClientApplication() {
		if (null == clientApplication) {
			clientApplication = new ClientApplication();
		}
		return clientApplication;
	}

	private ClientApplication clientApplication;

	private ClientAppSetting clientAppSetting;

	public StoreClientSoftwareImpl() {
		clientApplication = new ClientApplication();
		clientAppSetting = new ClientAppSetting();
	}

	public StoreClientSoftwareImpl(ClientApplication clientApplication, ClientAppSetting clientAppSetting) {
		this.clientApplication = clientApplication;
		this.clientAppSetting = clientAppSetting;

		if (null != this.clientApplication) {
			this.clientApplication.getClientName();// load information to avoid the lazy load
		}
		if (null != this.clientAppSetting) {
			this.clientAppSetting.getClientName();// load information to avoid the lazy load
		}
	}

	public String getConfigFileLocation() {
		return (null == clientAppSetting) ? null : clientAppSetting.getFileLocation();
	}

	public String getFileLocatioin() {
		return (null == clientApplication) ? null : clientApplication.getFileLocation();
	}

	public String getName() {
		return (null == clientApplication) ? null : clientApplication.getClientName();
	}

	public String getVersion() {
		return (null == clientApplication) ? null : clientApplication.getVersion();
	}

	public Date getConfigFileUpdateDate() {
		return (null == clientAppSetting) ? null : clientAppSetting.getUpdateDate();
	}

	public String getConfigFileVersion() {
		return (null == clientAppSetting) ? null : clientAppSetting.getVersion();
	}

	public Date getFileUpdateDate() {
		return (null == clientApplication) ? null : clientApplication.getUpdateDate();
	}

	public long getClientAppSettingId() {
		Long id = (null == clientAppSetting) ? 0L : clientAppSetting.getId();
		if (null != id) {
			return id;
		}
		throw new RuntimeException();
	}

	public long getClientApplicationId() {
		Long id = (null == clientApplication) ? 0L : clientApplication.getId();
		if (null != id) {
			return id;
		}
		throw new RuntimeException();
	}

}

// $Id$