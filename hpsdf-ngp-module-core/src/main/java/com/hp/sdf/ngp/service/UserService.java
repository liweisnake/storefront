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

import com.hp.sdf.ngp.api.exception.AssetCatalogServiceException;
import com.hp.sdf.ngp.api.model.User;
import com.hp.sdf.ngp.api.search.SearchExpression;
import com.hp.sdf.ngp.model.RoleCategory;
import com.hp.sdf.ngp.model.UserProfile;

public interface UserService extends java.io.Serializable {

	// Role methods
	/**
	 * Create RoleCategory
	 * 
	 * @param role
	 *            role will be added
	 */
	public void saveRole(RoleCategory role);

	/**
	 * getAllRoles.
	 * 
	 * @return list of RoleCategory
	 */
	public List<RoleCategory> getAllRoles();

	/**
	 * Get role by the role name
	 * 
	 * @param roleName
	 * @return RoleCategory
	 */
	public RoleCategory getRoleByName(String roleName);

	// User methods
	/**
	 * Create User
	 * 
	 * @param up
	 */
	public void saveUser(UserProfile sp);

	/**
	 * Update User
	 * 
	 * @param up
	 *            UserProfile object will be created
	 * @param role
	 *            RoleCategory object will be associated
	 */
	public void updateUser(UserProfile sp);

	/**
	 * Delete User
	 * 
	 * @param role
	 *            RoleCategory object will be associated
	 */
	public void deleteUser(String userId);

	/**
	 * Get UserProfile by userId
	 * 
	 * @param userId
	 *            UserId will identify UserProfile
	 * @return UserProfile Object
	 */
	public UserProfile getUser(String userId);

	/**
	 * searchUserProfile.
	 * 
	 * @param searchExpression
	 * @return
	 */
	public List<UserProfile> searchUserProfile(SearchExpression searchExpression);

	/**
	 * countUserProfile.
	 * 
	 * @param searchExpression
	 * @return
	 */
	public int countUserProfile(SearchExpression searchExpression);

	// Cross methods
	/**
	 * Get role by userId
	 * 
	 * @param userId
	 *            UserId will identify UserProfile
	 * @return RoleCategory object
	 */
	public List<RoleCategory> getRoleCategoryByUserId(String userId);

	/**
	 * Apply role to a user
	 * 
	 * @param userId
	 *            userId will get role
	 * @param roleId
	 *            roleId will assign to user
	 * @return true if success, false if failed
	 */
	public boolean assignRole(String userId, String roleName);

	public boolean removeRole(String userId, String roleName);

	public void updatePassword(String userId, String passsword);

	public boolean validatePassword(String userId, String inputPassword);

	/**
	 * 
	 * @param userId
	 * @param disabled
	 *            if true, this user account will be disabled, otherwise, it
	 *            will be enabled
	 */
	public void disableUser(String userId, boolean disabled);

	/**
	 * Adds a user to a asset provider organization.
	 * 
	 * @param userId
	 * @param providerId
	 */
	public void addUserToProvider(String userId, Long providerId);

	/**
	 * Removes a user from a asset provider organization.
	 * 
	 * If the user does not belongs to the provider, no exception will be
	 * reported.
	 * 
	 * @param userId
	 * @param providerId
	 */
	public void removeUserFromProvider(String userId, Long providerId);

	public List<UserProfile> getUsersByProvider(Long providerId);

}
