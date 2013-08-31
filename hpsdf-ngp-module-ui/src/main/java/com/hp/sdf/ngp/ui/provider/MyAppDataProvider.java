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

import com.hp.sdf.ngp.model.Asset;
import com.hp.sdf.ngp.service.ApplicationService;

public class MyAppDataProvider implements IDataProvider<Asset> {

	private static final long serialVersionUID = -7549190642990710771L;

	private String userId;

	private ApplicationService applicationService;

	public MyAppDataProvider(String userId, ApplicationService applicationService) {
		this.applicationService = applicationService;
		this.userId = userId;

	}

	public Iterator<? extends Asset> iterator(int first, int count) {

		List<Asset> list = applicationService.getMyAsset(userId);

		if (list != null) {
			return list.iterator();
		}

		return null;
	}

	public IModel<Asset> model(Asset object) {
		return new Model<Asset>(object);
	}

	public int size() {
		int size = (int) applicationService.getMyAssetPageCount(userId);
		return size;
	}

	public void detach() {
	}

}