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
package com.hp.sdf.ngp.ui.provider;

import java.util.Iterator;
import java.util.List;

import com.hp.sdf.ngp.model.Service;
import com.hp.sdf.ngp.service.ApiService;

public class ApiSgfDataProvider extends ApiDataProvider {

	private static final long serialVersionUID = 8297281367835183261L;

	public ApiSgfDataProvider(ApiService apiService){
		super(apiService);
	}
	
	@Override
	public Iterator<? extends Service> iterator(int first, int count) {
		List<Service> list = this.getApiService().getSgfService(first, count);
		if(list != null)
			return list.iterator();
		return null;
	}
	
	@Override
	public int size() {
		return (int)this.getApiService().getSgfServiceCount();
	}
}

// $Id$