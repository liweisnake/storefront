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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import com.hp.sdf.ngp.model.ApiKey;
import com.hp.sdf.ngp.service.ApiService;

public class SubscriptionDataProvider implements IDataProvider<ApiKey> {

	private static final long serialVersionUID = 7033506906083624452L;
	private ApiService apiService;
	private String user;

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public ApiService getApiService() {
		return apiService;
	}

	public void setApiService(ApiService apiService) {
		this.apiService = apiService;
	}

	public SubscriptionDataProvider(ApiService apiService, String user) {
		this.apiService = apiService;
		this.user = user;
	}

	public Iterator<? extends ApiKey> iterator(int first, int count) {
		if (this.user == null)
			return new ArrayList<ApiKey>().iterator();
		List<ApiKey> list = this.apiService.getApiKeyByUserId(this.user);
		if (list != null)
			return list.iterator();
		return null;
	}

	public IModel<ApiKey> model(ApiKey object) {
		return new Model<ApiKey>(object);
	}

	public int size() {
		return Integer.MAX_VALUE;
	}

	public void detach() {
	}
}

// $Id$