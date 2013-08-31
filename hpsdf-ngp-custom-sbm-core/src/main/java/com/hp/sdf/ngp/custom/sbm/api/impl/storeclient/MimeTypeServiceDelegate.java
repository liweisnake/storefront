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

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;

import com.hp.sdf.ngp.api.comon.util.JndiUtil;
import com.hp.sdf.ngp.common.delegate.ComponentDelegate;
import com.hp.sdf.ngp.custom.sbm.api.storeclient.service.MimeTypeService;
import com.hp.sdf.ngp.custom.sbm.storeclient.service.MimeTypeServiceImpl;

@Component(value = "mimeTypeService")
public class MimeTypeServiceDelegate extends ComponentDelegate<MimeTypeService> implements MimeTypeService {

	@SuppressWarnings("unchecked")
	@Override
	protected Class<MimeTypeService> getDefaultComponent() {
		return (Class) MimeTypeServiceImpl.class;

	}

	@PostConstruct
	protected void init() {
		JndiUtil.bind(MimeTypeService.class.getCanonicalName(), this);
	}

	public void deleteMimeTypeById(Long MimeTypeId) {
		this.component.deleteMimeTypeById(MimeTypeId);

	}

}

// $Id$