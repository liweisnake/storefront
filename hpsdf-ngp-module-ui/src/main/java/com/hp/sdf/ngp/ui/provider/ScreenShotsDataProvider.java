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
import com.hp.sdf.ngp.api.search.Condition.NumberComparer;
import com.hp.sdf.ngp.api.search.orderby.ScreenShotsOrderBy;
import com.hp.sdf.ngp.model.ScreenShots;
import com.hp.sdf.ngp.search.condition.screenshots.ScreenShotsAssetBinaryVersionIdCondition;
import com.hp.sdf.ngp.search.engine.SearchExpressionImpl;
import com.hp.sdf.ngp.service.ApplicationService;

public class ScreenShotsDataProvider implements IDataProvider<ScreenShots> {

	private static final long serialVersionUID = -6829478296346665192L;

	private ApplicationService applicationService;

	private Long assetBinaryVersionId;

	public ScreenShotsDataProvider(ApplicationService applicationService) {
		this.applicationService = applicationService;
	}

	public ScreenShotsDataProvider(ApplicationService applicationService, Long assetBinaryVersionId) {
		this.applicationService = applicationService;
		this.assetBinaryVersionId = assetBinaryVersionId;
	}

	public Iterator<ScreenShots> iterator(int first, int count) {
		if (assetBinaryVersionId != null) {
			SearchExpression searchExpression = new SearchExpressionImpl();
			searchExpression.addCondition(new ScreenShotsAssetBinaryVersionIdCondition(assetBinaryVersionId, NumberComparer.EQUAL));
			searchExpression.setFirst(first);
			searchExpression.setMax(count);
			searchExpression.addOrder(ScreenShotsOrderBy.STORELOCATION, OrderEnum.ASC);
			List<ScreenShots> list = applicationService.searchScreenShots(searchExpression);

			if (list != null)
				return list.iterator();

			return null;
		} else {
			return null;
		}
	}

	public IModel<ScreenShots> model(ScreenShots object) {
		return new Model<ScreenShots>(object);
	}

	public int size() {
		if (assetBinaryVersionId != null) {
			SearchExpression searchExpression = new SearchExpressionImpl();
			searchExpression.addCondition(new ScreenShotsAssetBinaryVersionIdCondition(assetBinaryVersionId, NumberComparer.EQUAL));
			searchExpression.setFirst(0);
			searchExpression.setMax(Integer.MAX_VALUE);
			return applicationService.searchScreenShotsCount(searchExpression);
		} else {
			return 0;
		}
	}

	public void detach() {
	}

	public Long getAssetBinaryVersionId() {
		return assetBinaryVersionId;
	}

	public void setAssetBinaryVersionId(Long assetBinaryVersionId) {
		this.assetBinaryVersionId = assetBinaryVersionId;
	}
}