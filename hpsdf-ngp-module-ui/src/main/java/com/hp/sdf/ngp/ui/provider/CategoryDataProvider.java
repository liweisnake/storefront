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
import com.hp.sdf.ngp.api.search.orderby.CategoryOrderBy;
import com.hp.sdf.ngp.model.Category;
import com.hp.sdf.ngp.search.engine.SearchExpressionImpl;
import com.hp.sdf.ngp.service.ApplicationService;

/**
 * 
 * CategoryDataProvider.
 * 
 */
public class CategoryDataProvider implements IDataProvider<Category> {

	private static final long serialVersionUID = 6080358671456933625L;

	private ApplicationService applicationService;

	
	public CategoryDataProvider(ApplicationService applicationService) {
		this.applicationService = applicationService;
	}
	
	private SearchExpression getSearchExpression(int first, int count) {

		SearchExpression searchExpression = new SearchExpressionImpl();

//		order:  No-name category first, then parent ID = null categories, then id (asc)
		searchExpression.addOrder(CategoryOrderBy.NAME,OrderEnum.ASC);
		searchExpression.addOrder(CategoryOrderBy.PARENTEXTERNALID,OrderEnum.ASC);
		searchExpression.addOrder(CategoryOrderBy.ID,OrderEnum.ASC);
		
		searchExpression.setFirst(first);
		searchExpression.setMax(count);
		return searchExpression;
	}
	public Iterator<? extends Category> iterator(int first, int count) {

		SearchExpression searchExpression = getSearchExpression(first, count);

		List<Category> list = applicationService.searchCategory(searchExpression);
		if (list != null) {
			return list.iterator();
		}
		return null;
	}

	public IModel<Category> model(Category object) {
		return new Model<Category>(object);
	}

	public int size() {
		return applicationService.getAllCategoryCount();
	}

	public void detach() {
	}

}
