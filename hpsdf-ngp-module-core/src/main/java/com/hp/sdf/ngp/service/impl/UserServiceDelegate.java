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

import org.springframework.stereotype.Component;

import com.hp.sdf.ngp.api.search.SearchExpression;
import com.hp.sdf.ngp.common.delegate.ComponentDelegate;
import com.hp.sdf.ngp.model.RoleCategory;
import com.hp.sdf.ngp.model.UserProfile;
import com.hp.sdf.ngp.service.UserService;

@Component(value = "userService")
public class UserServiceDelegate extends ComponentDelegate<UserService> implements UserService {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@SuppressWarnings("unchecked")
	@Override
	protected Class<UserService> getDefaultComponent() {
		return (Class) UserServiceDecorator.class;

	}

	public boolean assignRole(String userId, String roleName) {
		return this.component.assignRole(userId, roleName);
	}

	public void deleteUser(String userId) {
		this.component.deleteUser(userId);
	}

	public RoleCategory getRoleByName(String roleName) {
		return this.component.getRoleByName(roleName);
	}

	public List<RoleCategory> getRoleCategoryByUserId(String userId) {
		return this.component.getRoleCategoryByUserId(userId);
	}

	public UserProfile getUser(String userId) {
		return this.component.getUser(userId);
	}

	public void saveRole(RoleCategory role) {
		this.component.saveRole(role);
	}

	public void saveUser(UserProfile sp) {
		this.component.saveUser(sp);
	}

	public void updateUser(UserProfile sp) {
		this.component.updateUser(sp);
	}

	public boolean removeRole(String userId, String roleName) {

		return this.component.removeRole(userId, roleName);
	}

	public List<UserProfile> searchUserProfile(SearchExpression searchExpression) {
		return this.component.searchUserProfile(searchExpression);
	}

	public int countUserProfile(SearchExpression searchExpression) {
		return this.component.countUserProfile(searchExpression);
	}

	public List<RoleCategory> getAllRoles() {
		return this.component.getAllRoles();
	}

	public void updatePassword(String userId, String passsword) {
		this.component.updatePassword(userId, passsword);

	}

	public boolean validatePassword(String userId, String inputPassword) {
		return this.component.validatePassword(userId, inputPassword);
	}

	public void addUserToProvider(String userId, Long providerId) {
		this.component.addUserToProvider(userId, providerId);

	}

	public List<UserProfile> getUsersByProvider(Long providerId) {

		return this.component.getUsersByProvider(providerId);
	}

	public void removeUserFromProvider(String userId, Long providerId) {

		this.component.removeUserFromProvider(userId, providerId);
	}

	public void disableUser(String userId, boolean disabled) {
		this.component.disableUser(userId, disabled);
	}

}

// $Id$