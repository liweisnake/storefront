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
package com.hp.sdf.ngp.service;

import java.util.List;

import com.hp.sdf.ngp.api.search.SearchExpression;
import com.hp.sdf.ngp.model.Country;
import com.hp.sdf.ngp.model.Language;
import com.hp.sdf.ngp.model.UserLifecycleAction;
import com.hp.sdf.ngp.model.UserLifecycleActionHistory;

public interface InfoService  extends java.io.Serializable{
	
	public void saveOrUpdateUserLifecycleAction(UserLifecycleAction userLifecycleAction);
	
	public List<UserLifecycleAction> getUserLifecycleAction(SearchExpression searchExpression);

	public List<UserLifecycleAction> getAllUserLifecycleAction(int start, int max);

	public UserLifecycleAction getUserLifecycleActionByUserIdAndRoleName(String userId, String preRoleName,String postRoleName);

	public long getAllUserLifecycleActionCount();

	public void removeUserLifecycleAction(long requestId);
	
	public void saveUserLifecycleActionHistory(UserLifecycleActionHistory userLifecycleActionHistory);
	
	public void removeUserLifecycleActionHistory(long id);
	
	public List<UserLifecycleActionHistory> getUserLifecycleActionHistory(SearchExpression searchExpression);
	
	// Countrys methods
	/**
	 * Get all countrys of contry table
	 * 
	 * @return all country list
	 */
	public List<Country> getCountrys();

	// Languages methods
	/**
	 * Get all languages of language table
	 * 
	 * @return all language list
	 */
	public List<Language> getLanguages();
	
	public Language getLanguageByName(String name);
	
	public Country getCountryByName(String name);

}

// $Id$