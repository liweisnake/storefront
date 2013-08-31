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
package com.hp.sdf.ngp.api.impl.delegate;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;

import com.hp.sdf.ngp.api.comon.util.JndiUtil;
import com.hp.sdf.ngp.api.impl.search.SearchEngineImpl;
import com.hp.sdf.ngp.api.search.Condition;
import com.hp.sdf.ngp.api.search.ConditionDescriptor;
import com.hp.sdf.ngp.api.search.SearchEngine;
import com.hp.sdf.ngp.api.search.SearchExpression;
import com.hp.sdf.ngp.common.delegate.ComponentDelegate;

@Component(value = "searchEngine")
public class SearchEngineDelegate extends ComponentDelegate<SearchEngine> implements SearchEngine {

	@SuppressWarnings("unchecked")
	@Override
	protected Class<SearchEngine> getDefaultComponent() {
		return (Class) SearchEngineImpl.class;

	}

	@PostConstruct
	protected void init() {
		JndiUtil.bind(SearchEngine.class.getCanonicalName(), this);
	}
	
	public Condition createCondition(ConditionDescriptor conditionDescriptor) {
		return component.createCondition(conditionDescriptor);
	}

	public SearchExpression createSearchExpression() {
		return component.createSearchExpression();
	}
	
	
	
}

// $Id$