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

import com.hp.sdf.ngp.model.AssetLifecycleAction;
import com.hp.sdf.ngp.service.ApplicationService;

/***
 * AssetHistoryDataProvider.
 * 
 */
public class AssetHistoryDataProvider implements IDataProvider<AssetLifecycleAction> {

	private static final long serialVersionUID = -181096515446520849L;

	private ApplicationService applicationService;

	private Long appId;

	public AssetHistoryDataProvider(ApplicationService applicationService, Long appId) {
		this.applicationService = applicationService;
		this.appId = appId;
	}

	public Iterator<? extends AssetLifecycleAction> iterator(int first, int count) {
		List<AssetLifecycleAction> list = applicationService.getAssetLifecycleActionByAssetId(appId);
		if (list != null) {
			return list.iterator();
		}

		return null;
	}

	public IModel<AssetLifecycleAction> model(AssetLifecycleAction object) {
		return new Model<AssetLifecycleAction>(object);
	}

	public int size() {
		return (int) applicationService.getAssetLifecycleActionCount(appId);
	}

	public void detach() {
	}

}
