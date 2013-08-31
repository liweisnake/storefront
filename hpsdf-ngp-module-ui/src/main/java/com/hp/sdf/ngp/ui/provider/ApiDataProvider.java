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

import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import com.hp.sdf.ngp.model.Service;
import com.hp.sdf.ngp.service.ApiService;

public class ApiDataProvider implements IDataProvider<Service> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -61650827134769508L;
	
	private ApiService apiService;
	
	public ApiService getApiService() {
		return apiService;
	}

	public void setApiService(ApiService apiService) {
		this.apiService = apiService;
	}

	public ApiDataProvider(ApiService apiService){
		this.apiService = apiService;
	}

	public Iterator<? extends Service> iterator(int first, int count) {
		List<Service> list = apiService.getAllService(first, count);
		if(list != null)
			return list.iterator();
		return null;
	}

	public IModel<Service> model(Service object) {
		return new Model<Service>(object);
	}

	public int size() {
		return apiService.getAllServicePageCount();
	}

	public void detach() {
		// TODO Auto-generated method stub
		
	}

}

// $Id$