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

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import com.hp.sdf.ngp.api.search.SearchExpression;
import com.hp.sdf.ngp.api.search.Condition.StringComparer;
import com.hp.sdf.ngp.model.Platform;
import com.hp.sdf.ngp.search.condition.platform.PlatformNameCondition;
import com.hp.sdf.ngp.search.engine.SearchExpressionImpl;
import com.hp.sdf.ngp.service.ApplicationService;

public class PlatformSearchDataProvider implements IDataProvider<Platform> {

	private static final long serialVersionUID = 6080358671456933625L;

	private final static Log log = LogFactory.getLog(PlatformSearchDataProvider.class);

	private ApplicationService applicationService;

	private String platformName;

	public String getPlatformName() {
		return platformName;
	}

	public void setPlatformName(String platformName) {
		this.platformName = platformName;
	}

	public PlatformSearchDataProvider(ApplicationService applicationService, String platformName) {
		this.applicationService = applicationService;
		this.platformName = platformName;
	}

	private SearchExpression getSearchExpression(int first, int count) {

		SearchExpression searchExpression = new SearchExpressionImpl();

		if (StringUtils.isNotEmpty(platformName)) {
			searchExpression.addCondition(new PlatformNameCondition(platformName, StringComparer.EQUAL, true, false));
		}

		searchExpression.setFirst(first);
		searchExpression.setMax(count);
		return searchExpression;
	}

	public Iterator<? extends Platform> iterator(int first, int count) {
		log.debug("first :" + first);
		log.debug("count :" + count);

		SearchExpression searchExpression = getSearchExpression(first, count);
		List<Platform> list = applicationService.searchPlatform(searchExpression);
		if (list != null) {
			return list.iterator();
		}

		return null;
	}

	public IModel<Platform> model(Platform object) {
		return new Model<Platform>(object);
	}

	public int size() {
		SearchExpression searchExpression = getSearchExpression(0, Integer.MAX_VALUE);
		return (int) applicationService.countPlatforms(searchExpression);
	}

	public void detach() {
	}

}
