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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import com.hp.sdf.ngp.model.UserLifecycleAction;
import com.hp.sdf.ngp.service.InfoService;
import com.hp.sdf.ngp.ui.page.myportal.UserPromotionApprovePanel;

public class UserPromotionApproveDataProvider implements IDataProvider<UserLifecycleAction> {

	private static final long serialVersionUID = 1L;

	private final static Log log = LogFactory.getLog(UserPromotionApprovePanel.class);

	private InfoService infoService;

	public UserPromotionApproveDataProvider(InfoService infoService) {
		this.infoService = infoService;
	}

	public Iterator<? extends UserLifecycleAction> iterator(int first, int count) {

		log.debug("Enter promotion.");

		List<UserLifecycleAction> list = infoService.getAllUserLifecycleAction(first, count);

		log.debug("^^^ size=" + list.size());

		if (list != null) {
			return list.iterator();
		}

		return null;
	}

	public int size() {
		return (int) infoService.getAllUserLifecycleActionCount();
	}

	public void detach() {
	}

	public IModel<UserLifecycleAction> model(UserLifecycleAction object) {
		return new Model<UserLifecycleAction>(object);
	}
}

// $Id$