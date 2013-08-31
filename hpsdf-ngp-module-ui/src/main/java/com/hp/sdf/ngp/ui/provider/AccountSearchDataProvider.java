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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import com.hp.sdf.ngp.api.search.OrderEnum;
import com.hp.sdf.ngp.api.search.SearchExpression;
import com.hp.sdf.ngp.api.search.Condition.StringComparer;
import com.hp.sdf.ngp.api.search.orderby.UserProfileOrderBy;
import com.hp.sdf.ngp.common.annotation.Value;
import com.hp.sdf.ngp.model.UserProfile;
import com.hp.sdf.ngp.search.condition.userprofile.UserProfileContentProviderOperatorAssetProviderNameCondition;
import com.hp.sdf.ngp.search.condition.userprofile.UserProfileRoleNameCondition;
import com.hp.sdf.ngp.search.condition.userprofile.UserProfileUseridCondition;
import com.hp.sdf.ngp.search.engine.SearchExpressionImpl;
import com.hp.sdf.ngp.service.UserService;
import com.hp.sdf.ngp.ui.common.AccountSearchCondition;

import edu.emory.mathcs.backport.java.util.Arrays;

public class AccountSearchDataProvider implements IDataProvider<UserProfile> {

	private static final long serialVersionUID = 6080358671456933625L;

	private final static Log log = LogFactory.getLog(AccountSearchDataProvider.class);

	private UserService userService;

	private AccountSearchCondition accountSearchCondition;

	private String ignoredRoles = "Admin";

	@Value("roles.display.ignoredAdmin")
	public void setIgnoredRoles(String ignoredRoles) {
		this.ignoredRoles = ignoredRoles;
	}

	public AccountSearchCondition getAccountSearchCondition() {
		return accountSearchCondition;
	}

	public void setAccountSearchCondition(AccountSearchCondition accountSearchCondition) {
		this.accountSearchCondition = accountSearchCondition;
	}

	public AccountSearchDataProvider(UserService userService, AccountSearchCondition accountSearchCondition) {
		this.userService = userService;
		this.accountSearchCondition = accountSearchCondition;
	}

	private SearchExpression getSearchExpression(int first, int count) {

		SearchExpression searchExpression = new SearchExpressionImpl();

		if (accountSearchCondition != null) {

			if (StringUtils.isNotEmpty(accountSearchCondition.getAccountName())) {
				searchExpression.addCondition(new UserProfileUseridCondition(accountSearchCondition.getAccountName(), StringComparer.LIKE, true, false));
			}

			if (StringUtils.isNotEmpty(accountSearchCondition.getProviderName())) {
				searchExpression.addCondition(new UserProfileContentProviderOperatorAssetProviderNameCondition(accountSearchCondition.getProviderName(), StringComparer.LIKE, true, false));
			}

			if (StringUtils.isNotEmpty(accountSearchCondition.getAccountRole())) {
				searchExpression.addCondition(new UserProfileRoleNameCondition(accountSearchCondition.getAccountRole(), StringComparer.EQUAL, true, false));
			}
		}
		
		List<String> ignoredRoleList = new ArrayList<String>();
		log.debug("ignoredRoles :" + ignoredRoles);
		if (StringUtils.isNotEmpty(ignoredRoles)) {
			ignoredRoleList = Arrays.asList(ignoredRoles.split(","));

			for(String ro:ignoredRoleList){
				searchExpression.addCondition(new UserProfileRoleNameCondition(ro, StringComparer.NOT_EQUAL, true, false));
			}
		}
		
		searchExpression.addOrder(UserProfileOrderBy.USERID, OrderEnum.ASC);

		searchExpression.setFirst(first);
		searchExpression.setMax(count);
		return searchExpression;
	}

	public Iterator<? extends UserProfile> iterator(int first, int count) {
		log.debug("first :" + first);
		log.debug("count :" + count);

		SearchExpression searchExpression = getSearchExpression(first, count);

		List<UserProfile> userProfiles = userService.searchUserProfile(searchExpression);
		if (userProfiles != null) {
			return userProfiles.iterator();
		}

		return null;
	}

	public IModel<UserProfile> model(UserProfile object) {
		return new Model<UserProfile>(object);
	}

	public int size() {
		SearchExpression searchExpression = getSearchExpression(0, Integer.MAX_VALUE);
		int size = userService.countUserProfile(searchExpression);
		
		if(size>=2){
			size-=2;
		}
		
		return size;
	}

	public void detach() {
	}

}
