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
import com.hp.sdf.ngp.api.search.Condition.DateComparer;
import com.hp.sdf.ngp.api.search.Condition.NumberComparer;
import com.hp.sdf.ngp.api.search.Condition.StringComparer;
import com.hp.sdf.ngp.model.AssetBinaryVersion;
import com.hp.sdf.ngp.search.condition.assetbinaryversion.AssetBinaryVersionCreateDateCondition;
import com.hp.sdf.ngp.search.condition.assetbinaryversion.AssetBinaryVersionFileNameCondition;
import com.hp.sdf.ngp.search.condition.assetbinaryversion.AssetBinaryVersionStatusIdCondition;
import com.hp.sdf.ngp.search.engine.SearchExpressionImpl;
import com.hp.sdf.ngp.service.ApplicationService;
import com.hp.sdf.ngp.ui.common.SearchCondition;

public class BinaryVersionSearchResultProvider implements IDataProvider<AssetBinaryVersion> {

	private static final long serialVersionUID = 2958748149741417503L;

	private ApplicationService applicationService;

	private SearchExpression searchExpression = new SearchExpressionImpl();

	public BinaryVersionSearchResultProvider(ApplicationService applicationService, SearchCondition searchCondition) {
		this.applicationService = applicationService;

		if (searchCondition != null) {
			if (searchCondition.getKeyword() != null) {
				searchExpression.addCondition(new AssetBinaryVersionFileNameCondition(searchCondition.getKeyword(), StringComparer.LIKE, true, false));
			}
			if (searchCondition.getStatusId() != null) {
				searchExpression.addCondition(new AssetBinaryVersionStatusIdCondition(searchCondition.getStatusId(), NumberComparer.EQUAL));
			}
			if (searchCondition.getBeginDate() != null) {
				searchExpression.addCondition(new AssetBinaryVersionCreateDateCondition(searchCondition.getBeginDate(), DateComparer.GREAT_THAN));
			}
			if (searchCondition.getEndDate() != null) {
				searchExpression.addCondition(new AssetBinaryVersionCreateDateCondition(searchCondition.getEndDate(), DateComparer.LESS_THAN));
			}
		}
	}

	public Iterator<? extends AssetBinaryVersion> iterator(int first, int count) {
		searchExpression.setFirst(first);
		searchExpression.setMax(count);

		List<AssetBinaryVersion> list = applicationService.getAssetBinary(searchExpression);

		if (list != null) {
			return list.iterator();
		}

		return null;
	}

	public IModel<AssetBinaryVersion> model(AssetBinaryVersion object) {
		return new Model<AssetBinaryVersion>(object);
	}

	public int size() {
		searchExpression.setFirst(0);
		searchExpression.setMax(Integer.MAX_VALUE);
		return (int) applicationService.countAssetBinary(searchExpression);
	}

	public void detach() {
	}

}
