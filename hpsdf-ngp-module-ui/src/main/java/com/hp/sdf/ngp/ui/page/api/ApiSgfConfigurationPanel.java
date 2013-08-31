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
package com.hp.sdf.ngp.ui.page.api;

import org.apache.wicket.PageParameters;
import org.apache.wicket.markup.html.navigation.paging.PagingNavigator;

import com.hp.sdf.ngp.model.Service;
import com.hp.sdf.ngp.ui.WicketSession;
import com.hp.sdf.ngp.ui.provider.ApiSgfDataProvider;

public class ApiSgfConfigurationPanel extends ApiConfigurationPanel {

	private static final long serialVersionUID = 1373758075849114576L;
	
	@Override
	public String getType() {
		return Service.SERVICE_TYPE_SGF;
	}

	public ApiSgfConfigurationPanel(String id, PageParameters parameters) {
		super(id);
		WicketSession.get().setObject("selectApiConTab", ApiSgfConfigurationPanel.class.toString());
		if(parameters != null){
			this.setApiID(parameters.getString("id" + this.getClass()));
			this.setName(parameters.getString("name" + this.getClass()));
			this.setDescription(parameters.getString("description" + this.getClass()));
			this.setAccessInterface(parameters.getString("accessInterface" + this.getClass()));
			this.setAuthType(parameters.getString("authType" + this.getClass()));
			this.setType(parameters.getString("type" + this.getClass()));
			this.setBrokerServiceUrl(parameters.getString("brokerServiceUrl" + this.getClass()));
			this.setDocUrl(parameters.getString("docUrl" + this.getClass()));
			this.setError(parameters.getString("error" + this.getClass()));
			this.setBrokerServiceName(parameters.getString("brokerServiceName" + this.getClass()));
			this.setSdkUrl(parameters.getString("sdkUrl" + this.getClass()));
			this.setServiceId(parameters.getString("serviceId" + this.getClass()));
		}
		ApiSgfDataProvider dataProvider = new ApiSgfDataProvider(this.getApiService());
		ApiDataView<ApiSgfConfigurationPanel> dataView = new ApiDataView<ApiSgfConfigurationPanel>("sgfApiList",dataProvider,this.getApiService(),this.getItemsPerPage(), this);
		ApiForm<ApiSgfConfigurationPanel> form = new ApiForm<ApiSgfConfigurationPanel>("sgfApiForm", this.getApiService(), this);
		this.add(form);
		this.add(dataView);
		this.add(new PagingNavigator("navigator", dataView));
	}

}

// $Id$