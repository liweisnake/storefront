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

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import sun.misc.BASE64Encoder;

import com.hp.sdf.ngp.api.comon.EntityType;
import com.hp.sdf.ngp.api.search.SearchExpression;
import com.hp.sdf.ngp.api.search.Condition.NumberComparer;
import com.hp.sdf.ngp.api.search.Condition.StringComparer;
import com.hp.sdf.ngp.common.annotation.Value;
import com.hp.sdf.ngp.common.exception.NgpRuntimeException;
import com.hp.sdf.ngp.dao.ContentProviderOperatorDAO;
import com.hp.sdf.ngp.dao.RoleCategoryDAO;
import com.hp.sdf.ngp.dao.UserProfileDAO;
import com.hp.sdf.ngp.dao.UserRoleCategoryDAO;
import com.hp.sdf.ngp.eav.EavRepository;
import com.hp.sdf.ngp.eav.model.Entity;
import com.hp.sdf.ngp.model.ContentProviderOperator;
import com.hp.sdf.ngp.model.Provider;
import com.hp.sdf.ngp.model.RoleCategory;
import com.hp.sdf.ngp.model.UserProfile;
import com.hp.sdf.ngp.model.UserRoleCategory;
import com.hp.sdf.ngp.search.condition.contentprovideroperator.ContentProviderOperatorAssetProviderIdCondition;
import com.hp.sdf.ngp.search.condition.contentprovideroperator.ContentProviderOperatorUseridCondition;
import com.hp.sdf.ngp.search.condition.rolecategory.RoleCategoryRoleNameCondition;
import com.hp.sdf.ngp.search.engine.SearchExpressionImpl;
import com.hp.sdf.ngp.service.ApplicationService;
import com.hp.sdf.ngp.service.UserService;

/**
 * This class is the JPA implementation to control the User Roles
 */

@Component
@Transactional
public class UserServiceImpl implements UserService {

	private static final long serialVersionUID = -4593976748600324488L;

	Logger log = Logger.getLogger(UserServiceImpl.class);

	public static final String SYNCFLG = "SYNCFLG";

	public static final String TEMPPASSWORD = "TEMPPASSWORD";

	private boolean jbossEnv = false;

	private void markSyncFlgInEAV(String userId) {
		if (!this.isJbossEnv()) {
			applicationService.removeAttributes(userId, EntityType.USER, UserServiceImpl.SYNCFLG);
			applicationService.addAttribute(userId, EntityType.USER, UserServiceImpl.SYNCFLG, 0);
		}
	}

	private void markUpdatedPasswordInEAV(String userId, String password) {
		if (!this.isJbossEnv()) {
			applicationService.removeAttributes(userId, EntityType.USER, UserServiceImpl.TEMPPASSWORD);
			if (null != password) {
				applicationService.addAttribute(userId, EntityType.USER, UserServiceImpl.TEMPPASSWORD, password);
			}
		}
	}

	@Resource
	private ApplicationService applicationService;

	@Resource
	private UserProfileDAO userProfileDAO;

	@Resource
	private UserRoleCategoryDAO userRoleCategoryDAO;

	@Resource
	private RoleCategoryDAO roleDao;

	@Resource
	private EavRepository eavRepository;

	@Resource
	private ContentProviderOperatorDAO contentProviderOperatorDAO;

	public boolean isJbossEnv() {
		return jbossEnv;
	}

	@Value("jboss.env.enabled")
	public void setJbossEnv(boolean jbossEnv) {
		this.jbossEnv = jbossEnv;
	}

	// main methods
	public boolean assignRole(String userId, String roleName) {
		UserRoleCategory subscriberRoleCategory = new UserRoleCategory();
		RoleCategory role = getRoleByName(roleName);
		if (role == null) {
			RoleCategory r = new RoleCategory();
			r.setRoleName(roleName);
			roleDao.persist(r);
			subscriberRoleCategory.setRoleCategory(r);
		} else
			subscriberRoleCategory.setRoleCategory(role);
		subscriberRoleCategory.setUserProfile(userProfileDAO.findById(userId));
		userRoleCategoryDAO.persist(subscriberRoleCategory);
		markSyncFlgInEAV(userId);
		return true;
	}

	public boolean assignRole(String userId, Long roleId) {
		UserRoleCategory subscriberRoleCategory = new UserRoleCategory();
		RoleCategory role = roleDao.findById(roleId);
		if (role == null)
			return false;
		subscriberRoleCategory.setRoleCategory(role);
		subscriberRoleCategory.setUserProfile(userProfileDAO.findById(userId));
		userRoleCategoryDAO.persist(subscriberRoleCategory);
		markSyncFlgInEAV(userId);
		return true;
	}

	public void saveRole(RoleCategory role) {
		roleDao.persist(role);
	}

	public void saveUser(UserProfile sp) {
		Entity entity = new Entity();
		entity.setEntityType(EntityType.USER.ordinal());
		eavRepository.addEntity(entity);
		sp.setEntityId(entity.getEntityID());
		userProfileDAO.persist(sp);

		this.updatePassword(sp.getUserid(), sp.getPassword());
		markSyncFlgInEAV(sp.getUserid());

	}

	public void deleteUser(String userId) {

		SearchExpression searchExpression = new SearchExpressionImpl();
		searchExpression.addCondition(new ContentProviderOperatorUseridCondition(userId, StringComparer.EQUAL, false, false));
		List<ContentProviderOperator> contentProviderOperators = contentProviderOperatorDAO.search(searchExpression);

		if (null != contentProviderOperators && contentProviderOperators.size() > 0) {
			for (ContentProviderOperator contentProviderOperator : contentProviderOperators) {
				contentProviderOperatorDAO.remove(contentProviderOperator);
			}
		}

		UserProfile sp = userProfileDAO.findById(userId);
		Long entityId = sp.getEntityId();

		log.debug("delete user" + sp.getUserid() + sp.getAddress() + sp.getCellphone() + sp.getEmail());
		userProfileDAO.remove(sp);
		eavRepository.deleteEntity(entityId);
	}

	public RoleCategory getRole(Long roleId) {
		return roleDao.findById(roleId);
	}

	public RoleCategory getRoleByName(String roleName) {
		SearchExpression searchExpression = new SearchExpressionImpl();
		searchExpression.addCondition(new RoleCategoryRoleNameCondition(roleName, StringComparer.EQUAL, false, false));
		List<RoleCategory> roleCategories = roleDao.search(searchExpression);
		if (roleCategories != null && roleCategories.size() != 0) {
			return roleCategories.get(0);
		}
		return null;
	}

	public List<RoleCategory> getRoleCategoryByUserId(String userId) {
		List<RoleCategory> roles = new ArrayList<RoleCategory>();
		UserProfile user = userProfileDAO.findById(userId);
		for (UserRoleCategory subscriberRole : user.getUserRoleCategories()) {
			roles.add(subscriberRole.getRoleCategory());
		}
		return roles;
	}

	public UserProfile getUser(String userId) {
		UserProfile user = userProfileDAO.findById(userId);
		return user;
	}

	public void updateRole(RoleCategory role) {
		roleDao.merge(role);
	}

	public void updateUser(UserProfile sp) {
		userProfileDAO.merge(sp);
		markSyncFlgInEAV(sp.getUserid());
	}

	public boolean removeRole(String userId, String roleName) {

		RoleCategory roleCategory = roleDao.findUniqueBy("roleName", roleName);
		List<UserRoleCategory> srcs = userRoleCategoryDAO.findBy("roleCategory.id", roleCategory.getId());
		if (null != srcs) {
			for (UserRoleCategory src : srcs) {
				if (src.getUserProfile().getUserid().equals(userId)) {
					// this.markSyncFlgInEAV(userId);//this statement will cause
					// the delete failure, temporary comment it
					userRoleCategoryDAO.remove(src);

					return true;
				}
			}
		}
		return false;
	}

	public boolean removeRole(String userId, Long roleId) {
		List<UserRoleCategory> srcs = userRoleCategoryDAO.findBy("roleCategory.id", roleId);
		for (UserRoleCategory src : srcs) {
			if (src.getUserProfile().getUserid().equals(userId)) {
				// this.markSyncFlgInEAV(userId); //this statement will cause
				// the delete failure, temporary comment it
				userRoleCategoryDAO.remove(src);
				return true;
			}
		}
		return false;
	}

	@Transactional(readOnly = true)
	public List<UserProfile> searchUserProfile(SearchExpression searchExpression) {
		return userProfileDAO.search(searchExpression);
	}

	public int countUserProfile(SearchExpression searchExpression) {
		return userProfileDAO.searchCount(searchExpression);
	}

	public List<RoleCategory> getAllRoles() {
		return roleDao.getAll(0, Integer.MAX_VALUE);
	}

	public void updatePassword(String userId, String password) {
		UserProfile user = userProfileDAO.findById(userId);
		if (user == null) {
			return;
		}
		user.setPassword(EncoderByMd5(password));

		userProfileDAO.merge(user);

		markSyncFlgInEAV(userId);
		this.markUpdatedPasswordInEAV(userId, password);

	}

	public boolean validatePassword(String userId, String inputPassword) {
		UserProfile user = userProfileDAO.findById(userId);
		if (user == null) {
			return false;
		}
		return checkpassword(inputPassword, user.getPassword());
	}

	/** */
	/**
	 * @param str
	 *            string to be encrypted
	 * @return encrypted string
	 * @throws NoSuchAlgorithmException
	 * @throws UnsupportedEncodingException
	 */
	private String EncoderByMd5(String str) {
		if (StringUtils.isEmpty(str)) {
			return null;
		}

		try {
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			BASE64Encoder base64en = new BASE64Encoder();
			String newstr = base64en.encode(md5.digest(str.getBytes("utf-8")));
			return newstr;
		} catch (Throwable e) {
			throw new NgpRuntimeException("Can't encode the password", e);
		}
	}

	/** */
	/**
	 * check if the password correct
	 * 
	 * @param input
	 *            password that user provide
	 * @param passwd
	 *            password saved in db
	 * @return
	 * @throws NoSuchAlgorithmException
	 * @throws UnsupportedEncodingException
	 */
	private boolean checkpassword(String input, String passwd) {
		if (StringUtils.isEmpty(input) && StringUtils.isEmpty(passwd)) {
			log.debug("All of two password are empty.");
			return true;
		}
		if (StringUtils.isEmpty(input) || StringUtils.isEmpty(passwd)) {
			log.debug("one of two password is empty.");
			return false;
		}

		if (EncoderByMd5(input).equals(passwd)) {
			return true;
		} else {
			return false;
		}
	}

	public void disableUser(String userId, boolean disabled) {
		UserProfile user = userProfileDAO.findById(userId);
		if (user == null) {
			return;
		}
		user.setDisabled(disabled);
		user.setDisabledDate(new Date());
		userProfileDAO.merge(user);
		markSyncFlgInEAV(userId);
	}

	public void addUserToProvider(String userId, Long providerId) {
		try {
			SearchExpression searchExpression = new SearchExpressionImpl();
			searchExpression.addCondition(new ContentProviderOperatorUseridCondition(userId, StringComparer.EQUAL, false, false));
			searchExpression.addCondition(new ContentProviderOperatorAssetProviderIdCondition(providerId, NumberComparer.EQUAL));
			int count = contentProviderOperatorDAO.searchCount(searchExpression);

			if (count == 0) {
				Provider assetProvider = new Provider(providerId);
				ContentProviderOperator contentProviderOperator = new ContentProviderOperator();
				contentProviderOperator.setUserid(userId);
				contentProviderOperator.setAssetProvider(assetProvider);
				contentProviderOperatorDAO.persist(contentProviderOperator);
			}
		} catch (Exception e) {
			throw new NgpRuntimeException("Failed to add User To Provider", e);
		}

	}

	public List<UserProfile> getUsersByProvider(Long providerId) {
		List<UserProfile> users = new ArrayList<UserProfile>();
		try {

			List<ContentProviderOperator> contentProviderOperators = contentProviderOperatorDAO.findBy("assetProvider.id", providerId);
			if (null != users) {
				for (ContentProviderOperator contentProviderOperator : contentProviderOperators) {
					UserProfile userProfile = this.getUser(contentProviderOperator.getUserid());
					users.add(userProfile);
				}
			}
		} catch (Exception e) {
			throw new NgpRuntimeException("Failed to get Users By Provider", e);
		}
		return users;
	}

	public void removeUserFromProvider(String userId, Long providerId) {
		try {
			SearchExpression searchExpression = new SearchExpressionImpl();
			searchExpression.addCondition(new ContentProviderOperatorUseridCondition(userId, StringComparer.EQUAL, false, false));
			searchExpression.addCondition(new ContentProviderOperatorAssetProviderIdCondition(providerId, NumberComparer.EQUAL));

			List<ContentProviderOperator> contentProviderOperators = contentProviderOperatorDAO.search(searchExpression);
			if (null != contentProviderOperators) {
				for (ContentProviderOperator contentProviderOperator : contentProviderOperators) {
					contentProviderOperatorDAO.remove(contentProviderOperator);
				}
			}
		} catch (Exception e) {
			throw new NgpRuntimeException("Failed to remove user from provider.", e);
		}
	}

}
