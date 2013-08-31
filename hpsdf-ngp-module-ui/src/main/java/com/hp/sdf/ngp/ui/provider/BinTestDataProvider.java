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
import java.util.Map;

import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import com.hp.sdf.ngp.api.search.SearchExpression;
import com.hp.sdf.ngp.model.AssetLifecycleAction;
import com.hp.sdf.ngp.search.engine.SearchExpressionImpl;
import com.hp.sdf.ngp.service.ApplicationService;
import com.hp.sdf.ngp.ui.common.SearchMetaInfo;

public class BinTestDataProvider implements IDataProvider<AssetLifecycleAction> {

	private static final long serialVersionUID = 7757043236983277667L;

	private ApplicationService applicationService;

	private Map<String, String> searchProperties;

	private SearchExpression searchExpression = new SearchExpressionImpl();

	public SearchExpression getSearchExpression() {
		return searchExpression;
	}

	public void setSearchExpression(SearchExpression searchExpression) {
		this.searchExpression = searchExpression;
	}

	private List<SearchMetaInfo> searchMetaInfo;

	public ApplicationService getApplicationService() {
		return applicationService;
	}

	public void setApplicationService(ApplicationService applicationService) {
		this.applicationService = applicationService;
	}

	public BinTestDataProvider(ApplicationService applicationService) {
		this.applicationService = applicationService;
	}

	public List<SearchMetaInfo> getSearchMetaInfo() {
		return searchMetaInfo;
	}

	public void setSearchMetaInfo(List<SearchMetaInfo> searchMetaInfo) {
		this.searchMetaInfo = searchMetaInfo;
	}

	public Map<String, String> getSearchProperties() {
		return searchProperties;
	}

	public void setSearchProperties(Map<String, String> searchProperties) {
		this.searchProperties = searchProperties;
	}

	public Iterator<? extends AssetLifecycleAction> iterator(int first, int count) {
		if (searchExpression != null) {
			searchExpression.setFirst(first);
			searchExpression.setMax(count);
		}
		List<AssetLifecycleAction> list = applicationService.getAssetLifecycleAction(searchExpression);

		if (list != null)
			return list.iterator();
		return null;
	}

	public int size() {
		if (searchExpression != null) {
			searchExpression.setFirst(0);
			searchExpression.setMax(Integer.MAX_VALUE);
		}
		List<AssetLifecycleAction> list = applicationService.getAssetLifecycleAction(searchExpression);
		if (list != null) {
			return list.size();
		} else {
			return 0;
		}
	}

	public void detach() {

	}

	public IModel<AssetLifecycleAction> model(AssetLifecycleAction object) {
		return new Model<AssetLifecycleAction>(object);
	}

}