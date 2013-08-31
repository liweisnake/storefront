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
package com.hp.sdf.ngp.custom.sbm.api.impl.storeclient;

import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;

import com.hp.sdf.ngp.api.comon.util.JndiUtil;
import com.hp.sdf.ngp.api.search.SearchExpression;
import com.hp.sdf.ngp.common.delegate.ComponentDelegate;
import com.hp.sdf.ngp.custom.sbm.api.storeclient.exception.HandsetServiceException;
import com.hp.sdf.ngp.custom.sbm.api.storeclient.model.HandSetDevice;
import com.hp.sdf.ngp.custom.sbm.api.storeclient.service.HandsetDeviceService;
import com.hp.sdf.ngp.custom.sbm.storeclient.service.HandsetDeviceServiceImpl;

@Component(value = "handsetDeviceService")
public class HandsetDeviceServiceDelegate extends ComponentDelegate<HandsetDeviceService> implements HandsetDeviceService {

	@SuppressWarnings("unchecked")
	@Override
	protected Class<HandsetDeviceService> getDefaultComponent() {
		return (Class) HandsetDeviceServiceImpl.class;

	}

	@PostConstruct
	protected void init() {
		JndiUtil.bind(HandsetDeviceService.class.getCanonicalName(), this);
	}

	public int countHandsetDevice(SearchExpression searchExpression) throws HandsetServiceException {
		return this.component.countHandsetDevice(searchExpression);
	}

	public void deleteHandSetDeviceById(Long handSetDeviceId) throws HandsetServiceException {
		this.component.deleteHandSetDeviceById(handSetDeviceId);
	}

	public void saveHandSetDevice(HandSetDevice handSetDevice) throws HandsetServiceException {
		this.component.saveHandSetDevice(handSetDevice);
	}

	public List<HandSetDevice> searchHandsetDevice(SearchExpression searchExpression) throws HandsetServiceException {
		return this.component.searchHandsetDevice(searchExpression);
	}

	public void updateHandSetDevice(HandSetDevice handSetDevice) throws HandsetServiceException {
		this.component.updateHandSetDevice(handSetDevice);
	}

	public HandSetDevice getHandSetDeviceById(Long handSetDeviceId) {
		return component.getHandSetDeviceById(handSetDeviceId);
	}

}