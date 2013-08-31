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

import com.hp.sdf.ngp.api.search.SearchExpression;
import com.hp.sdf.ngp.api.search.Condition.StringComparer;
import com.hp.sdf.ngp.model.SystemConfig;
import com.hp.sdf.ngp.search.condition.systemconfig.SystemConfigConfigKeyCondition;
import com.hp.sdf.ngp.search.engine.SearchExpressionImpl;
import com.hp.sdf.ngp.service.ApplicationService;
import com.hp.sdf.ngp.ui.common.Constant.SYSTEM_CONFIG;

public class SystemConfigDataProvider implements IDataProvider<SystemConfig> {

	private static final long serialVersionUID = -6829478296346665192L;

	private ApplicationService applicationService;

	public SystemConfigDataProvider(ApplicationService applicationService) {
		this.applicationService = applicationService;
	}

	public Iterator<SystemConfig> iterator(int first, int count) {
		SearchExpression searchExpression = new SearchExpressionImpl();
		searchExpression.addCondition(new SystemConfigConfigKeyCondition(SYSTEM_CONFIG.newPromotionDueDate.toString(), StringComparer.NOT_EQUAL, false, false));
		searchExpression.addCondition(new SystemConfigConfigKeyCondition(SYSTEM_CONFIG.defaultCommissionRate.toString(), StringComparer.LIKE, false, true));
		searchExpression.setFirst(first);
		searchExpression.setMax(count);
		List<SystemConfig> list = applicationService.searchSystemConfig(searchExpression);

		if (list != null)
			return list.iterator();

		return null;
	}

	public IModel<SystemConfig> model(SystemConfig object) {
		return new Model<SystemConfig>(object);
	}

	public int size() {
		SearchExpression searchExpression = new SearchExpressionImpl();
		searchExpression.addCondition(new SystemConfigConfigKeyCondition(SYSTEM_CONFIG.newPromotionDueDate.toString(), StringComparer.NOT_EQUAL, false, false));
		searchExpression.addCondition(new SystemConfigConfigKeyCondition(SYSTEM_CONFIG.defaultCommissionRate.toString(), StringComparer.LIKE, false, true));
		searchExpression.setFirst(0);
		searchExpression.setMax(Integer.MAX_VALUE);
		return applicationService.searchSystemConfigCount(searchExpression);
	}

	public void detach() {
	}
}