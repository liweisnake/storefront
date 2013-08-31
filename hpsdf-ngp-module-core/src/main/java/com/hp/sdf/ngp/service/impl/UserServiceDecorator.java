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

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

import com.hp.sdf.ngp.api.search.SearchExpression;
import com.hp.sdf.ngp.common.annotation.Value;
import com.hp.sdf.ngp.model.RoleCategory;
import com.hp.sdf.ngp.model.UserProfile;
import com.hp.sdf.ngp.service.UserService;

@Component
public class UserServiceDecorator implements UserService {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final static Log log = LogFactory.getLog(UserServiceDecorator.class);

	private boolean runtime = true;

	public boolean isRuntime() {
		return runtime;
	}

	@Value("runtime")
	public void setRuntime(boolean runtime) {
		this.runtime = runtime;
	}

	@Resource
	private UserServiceImpl userServiceImpl;

	@Resource
	private JbossPortalUserServiceImpl jbossPortalUserServiceImpl;

	@Resource
	private UserService userService;

	@PostConstruct
	protected void init() {

		if (!runtime) {
			log.info("it is a test environment");
			return;
		}
		if (userService != null) {
			if (userService instanceof UserServiceDelegate) {
				if (((UserServiceDelegate) userService).getComponent() instanceof UserServiceDecorator) {
					log.info("Set JBOSS Env to true for UserServiceImpl");
					this.userServiceImpl.setJbossEnv(true);
				}
			}
		}
		// Check the provision users in jboss portal
		String userIds[] = new String[] { "user", "admin" };

		for (String userId : userIds) {
			UserProfile userProfileInJboss = jbossPortalUserServiceImpl.getUser(userId);
			UserProfile userProfile = userServiceImpl.getUser(userId);
			if (userProfile == null && userProfileInJboss != null) {
				log.info("find a user[" + userId + "] exists in JBOSS system, will sync it with internal user db");
				userProfileInJboss.setUserRoleCategories(null);
				userServiceImpl.saveUser(userProfileInJboss);
				userServiceImpl.updatePassword(userId, userId);
				List<RoleCategory> roleCategoris = jbossPortalUserServiceImpl.getRoleCategoryByUserId(userId);
				if (roleCategoris != null && roleCategoris.size() > 0) {
					for (RoleCategory roleCategoryInJboss : roleCategoris) {
						log.info("assign role[" + roleCategoryInJboss.getRoleName() + "] to this user");
						userServiceImpl.assignRole(userId, roleCategoryInJboss.getRoleName());
					}
				}
			}
		}
		// Check whether there are some roles in backend db, but not in JBoss
		// system
		List<RoleCategory> roleCategories = userServiceImpl.getAllRoles();
		if (roleCategories != null && roleCategories.size() > 0) {
			for (RoleCategory roleCategory : roleCategories) {
				RoleCategory roleCategoryInJBoss = this.jbossPortalUserServiceImpl.getRoleByName(roleCategory.getRoleName());
				if (roleCategoryInJBoss == null) {
					log.info("no role[" + roleCategory.getRoleName() + "] in JBOSS, but in backend database, will add it");
					roleCategoryInJBoss = new RoleCategory();
					roleCategoryInJBoss.setCreateDate(roleCategory.getCreateDate());
					roleCategoryInJBoss.setDescription(roleCategory.getDescription());
					roleCategoryInJBoss.setDisplayName(roleCategory.getDisplayName());
					roleCategoryInJBoss.setRoleName(roleCategory.getRoleName());
					jbossPortalUserServiceImpl.saveRole(roleCategory);
				}
			}
		}

	}

	public boolean assignRole(String userId, String roleName) {
		if (jbossPortalUserServiceImpl.assignRole(userId, roleName)) {
			return userServiceImpl.assignRole(userId, roleName);
		}
		return false;
	}

	public void deleteUser(String userId) {
		jbossPortalUserServiceImpl.deleteUser(userId);
		userServiceImpl.deleteUser(userId);
	}

	public boolean removeRole(String userId, String roleName) {
		if (userServiceImpl.removeRole(userId, roleName)) {
			return jbossPortalUserServiceImpl.removeRole(userId, roleName);
		}
		return false;
	}

	public void saveRole(RoleCategory role) {
		userServiceImpl.saveRole(role);
		jbossPortalUserServiceImpl.saveRole(role);
	}

	public void saveUser(UserProfile sp) {
		userServiceImpl.saveUser(sp);
		jbossPortalUserServiceImpl.saveUser(sp);
	}

	public List<UserProfile> searchUserProfile(SearchExpression searchExpression) {
		return userServiceImpl.searchUserProfile(searchExpression);
	}

	public int countUserProfile(SearchExpression searchExpression) {
		return userServiceImpl.countUserProfile(searchExpression);
	}

	public void updateRole(RoleCategory role) {
		userServiceImpl.updateRole(role);
		jbossPortalUserServiceImpl.updateRole(role);
	}

	public void updateUser(UserProfile sp) {
		userServiceImpl.updateUser(sp);
		jbossPortalUserServiceImpl.updateUser(sp);
	}

	public List<RoleCategory> getAllRoles() {
		return userServiceImpl.getAllRoles();
	}

	public RoleCategory getRole(Long roleId) {

		return userServiceImpl.getRole(roleId);
	}

	public RoleCategory getRoleByName(String roleName) {
		RoleCategory roleCategory = userServiceImpl.getRoleByName(roleName);
		RoleCategory roleCategoryInJBoss = this.jbossPortalUserServiceImpl.getRoleByName(roleName);

		if (roleCategoryInJBoss == null && roleCategory != null) {
			log.debug("get role from backend database, but no corresponding one from jboss portal, will add it in jboss portal");
			try {
				roleCategoryInJBoss = new RoleCategory();
				roleCategoryInJBoss.setCreateDate(roleCategory.getCreateDate());
				roleCategoryInJBoss.setDescription(roleCategory.getDescription());
				roleCategoryInJBoss.setDisplayName(roleCategory.getDisplayName());
				roleCategoryInJBoss.setRoleName(roleCategory.getRoleName());
				jbossPortalUserServiceImpl.saveRole(roleCategoryInJBoss);

			} catch (Throwable e) {
				log.warn("cant add role into jboss portal");
			}
		}
		return roleCategory;
	}

	public List<RoleCategory> getRoleCategoryByUserId(String userId) {
		return userServiceImpl.getRoleCategoryByUserId(userId);
	}

	public UserProfile getUser(String userId) {
		return userServiceImpl.getUser(userId);
	}

	public void updatePassword(String userId, String password) {
		this.jbossPortalUserServiceImpl.updatePassword(userId, password);
		userServiceImpl.updatePassword(userId, password);

	}

	public boolean validatePassword(String userId, String inputPassword) {

		if (jbossPortalUserServiceImpl.validatePassword(userId, inputPassword)) {
			return userServiceImpl.validatePassword(userId, inputPassword);
		}
		return false;
	}

	public void addUserToProvider(String userId, Long providerId) {
		userServiceImpl.addUserToProvider(userId, providerId);

	}

	public List<UserProfile> getUsersByProvider(Long providerId) {
		return userServiceImpl.getUsersByProvider(providerId);
	}

	public void removeUserFromProvider(String userId, Long providerId) {
		userServiceImpl.removeUserFromProvider(userId, providerId);
	}

	public void disableUser(String userId, boolean disabled) {
		userServiceImpl.disableUser(userId, disabled);
		this.jbossPortalUserServiceImpl.disableUser(userId, disabled);
	}

}

// $Id$