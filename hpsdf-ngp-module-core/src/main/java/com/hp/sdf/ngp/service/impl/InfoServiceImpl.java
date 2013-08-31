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
package com.hp.sdf.ngp.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.hp.sdf.ngp.api.search.SearchExpression;
import com.hp.sdf.ngp.api.search.Condition.StringComparer;
import com.hp.sdf.ngp.dao.CountryDAO;
import com.hp.sdf.ngp.dao.LanguageDAO;
import com.hp.sdf.ngp.dao.UserLifecycleActionDAO;
import com.hp.sdf.ngp.dao.UserLifecycleActionHistoryDAO;
import com.hp.sdf.ngp.model.Country;
import com.hp.sdf.ngp.model.Language;
import com.hp.sdf.ngp.model.UserLifecycleAction;
import com.hp.sdf.ngp.model.UserLifecycleActionHistory;
import com.hp.sdf.ngp.search.condition.userlifecycleaction.UserLifecycleActionPostRoleCondition;
import com.hp.sdf.ngp.search.condition.userlifecycleaction.UserLifecycleActionPreRoleCondition;
import com.hp.sdf.ngp.search.condition.userlifecycleaction.UserLifecycleActionUseridCondition;
import com.hp.sdf.ngp.search.engine.SearchExpressionImpl;
import com.hp.sdf.ngp.service.InfoService;

@Component
@Transactional
public class InfoServiceImpl implements InfoService {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Resource
	private UserLifecycleActionDAO userLifecycleActionDao;

	@Resource
	private UserLifecycleActionHistoryDAO userLifecycleActionHistoryDao;
	@Resource
	private CountryDAO countryDao;

	@Resource
	private LanguageDAO languageDao;

	public List<UserLifecycleAction> getAllUserLifecycleAction(int start, int max) {
		return userLifecycleActionDao.getAll(start, max);
	}

	public long getAllUserLifecycleActionCount() {
		return userLifecycleActionDao.getAllCount();
	}

	public List<UserLifecycleAction> getUserLifecycleAction(SearchExpression searchExpression) {
		return userLifecycleActionDao.search(searchExpression);
	}

	public UserLifecycleAction getUserLifecycleActionByUserIdAndRoleName(String userId, String preRoleName, String postRoleName) {

		SearchExpression searchExpression = new SearchExpressionImpl();
		searchExpression.addCondition(new UserLifecycleActionUseridCondition(userId, StringComparer.EQUAL, false, false));
		searchExpression.addCondition(new UserLifecycleActionPreRoleCondition(preRoleName, StringComparer.EQUAL, false, false));
		searchExpression.addCondition(new UserLifecycleActionPostRoleCondition(postRoleName, StringComparer.EQUAL, false, false));
		List<UserLifecycleAction> list = userLifecycleActionDao.search(searchExpression);
		if (list != null && list.size() > 0)
			return list.get(0);
		else {
			return null;
		}
	}

	public void removeUserLifecycleAction(long requestId) {
		userLifecycleActionDao.remove(requestId);
	}

	public void saveOrUpdateUserLifecycleAction(UserLifecycleAction userLifecycleAction) {
		if (userLifecycleAction.getId() == null) {
			userLifecycleActionDao.persist(userLifecycleAction);

		} else {
			userLifecycleActionDao.merge(userLifecycleAction);
		}
	}

	public List<UserLifecycleActionHistory> getUserLifecycleActionHistory(SearchExpression searchExpression) {
		return userLifecycleActionHistoryDao.search(searchExpression);
	}

	public void removeUserLifecycleActionHistory(long id) {
		userLifecycleActionHistoryDao.remove(id);
	}

	public void saveUserLifecycleActionHistory(UserLifecycleActionHistory userLifecycleActionHistory) {
		userLifecycleActionHistoryDao.persist(userLifecycleActionHistory);
	}

	public List<Country> getCountrys() {
		return countryDao.getAll(0, Integer.MAX_VALUE);
	}

	public List<Language> getLanguages() {
		return languageDao.getAll(0, Integer.MAX_VALUE);
	}

	public Country getCountryByName(String name) {
		List<Country> countries = this.countryDao.findBy("name", name);
		if (countries == null || countries.size() == 0) {
			return null;
		}
		return countries.get(0);
	}

	public Language getLanguageByName(String name) {
		List<Language> languages = this.languageDao.findBy("name", name);
		if (languages == null || languages.size() == 0) {
			return null;
		}
		return languages.get(0);
	}

}

// $Id$