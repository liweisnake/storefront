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

import com.hp.sdf.ngp.api.search.OrderEnum;
import com.hp.sdf.ngp.api.search.SearchExpression;
import com.hp.sdf.ngp.api.search.Condition.StringComparer;
import com.hp.sdf.ngp.api.search.orderby.ProviderOrderBy;
import com.hp.sdf.ngp.model.Provider;
import com.hp.sdf.ngp.search.condition.provider.ProviderExternalIdCondition;
import com.hp.sdf.ngp.search.condition.provider.ProviderNameCondition;
import com.hp.sdf.ngp.search.engine.SearchExpressionImpl;
import com.hp.sdf.ngp.service.ApplicationService;
import com.hp.sdf.ngp.ui.common.DefaultSettingCondition;

public class DefaultSettingDataProvider implements IDataProvider<Provider> {

	private static final long serialVersionUID = -6829478296346665192L;

	private ApplicationService applicationService;

	private DefaultSettingCondition condition;

	public DefaultSettingDataProvider(ApplicationService applicationService, DefaultSettingCondition condition) {
		this(applicationService);
		this.condition = condition;
	}

	public DefaultSettingDataProvider(ApplicationService applicationService) {
		this.applicationService = applicationService;
	}

	public Iterator<? extends Provider> iterator(int first, int count) {
		SearchExpression searchExpression = GenCondition();
		searchExpression.setFirst(first);
		searchExpression.setMax(count);
		searchExpression.addOrder(ProviderOrderBy.NAME, OrderEnum.ASC);
		List<Provider> list = applicationService.searchAssetProvider(searchExpression);

		if (list != null)
			return list.iterator();

		return null;
	}

	public IModel<Provider> model(Provider object) {
		return new Model<Provider>(object);
	}

	public int size() {
		SearchExpression searchExpression = GenCondition();
		searchExpression.setFirst(0);
		searchExpression.setMax(Integer.MAX_VALUE);
		return applicationService.searchAssetProviderCount(searchExpression);
	}

	public void detach() {
	}

	public DefaultSettingCondition getCondition() {
		return condition;
	}

	public void setCondition(DefaultSettingCondition condition) {
		this.condition = condition;
	}

	public SearchExpression GenCondition() {
		SearchExpression searchExpression = new SearchExpressionImpl();

		if (null == this.condition)
			this.condition = new DefaultSettingCondition();

		String providerExternalId = condition.getProviderExternalId();
		if (providerExternalId != null) {
			searchExpression.addCondition(new ProviderExternalIdCondition(providerExternalId, StringComparer.LIKE, true, true));
		}
		String providerName = condition.getProviderName();
		if (providerName != null) {
			searchExpression.addCondition(new ProviderNameCondition(providerName, StringComparer.LIKE, true, true));
		}

		return searchExpression;
	}
}