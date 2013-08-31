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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.transaction.TransactionManager;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.portal.common.transaction.Transactions;
import org.jboss.portal.identity.IdentityException;
import org.jboss.portal.identity.MembershipModule;
import org.jboss.portal.identity.NoSuchUserException;
import org.jboss.portal.identity.Role;
import org.jboss.portal.identity.RoleModule;
import org.jboss.portal.identity.User;
import org.jboss.portal.identity.UserModule;
import org.jboss.portal.identity.UserProfileModule;
import org.springframework.stereotype.Component;

import com.hp.sdf.ngp.api.search.SearchExpression;
import com.hp.sdf.ngp.common.exception.UnsupportMethodException;
import com.hp.sdf.ngp.common.exception.UserOperationException;
import com.hp.sdf.ngp.model.Country;
import com.hp.sdf.ngp.model.Language;
import com.hp.sdf.ngp.model.RoleCategory;
import com.hp.sdf.ngp.model.UserProfile;
import com.hp.sdf.ngp.model.UserRoleCategory;
import com.hp.sdf.ngp.service.InfoService;
import com.hp.sdf.ngp.service.UserService;

@Component
public class JbossPortalUserServiceImpl implements UserService {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5498465423261504453L;

	private class ThrowableObject {
		private Throwable throwable;

		public Throwable getThrowable() {
			return throwable;
		}

		public void setThrowable(Throwable throwable) {
			this.throwable = throwable;
		}

	}

	private final static Log log = LogFactory.getLog(JbossPortalUserServiceImpl.class);

	private final String userModuleJNDIName = "java:portal/UserModule";

	private final String roleModuleJNDIName = "java:portal/RoleModule";

	private final String userProfileModuleJNDIName = "java:portal/DBUserProfileModule";

	private final String membershipModuleJNDIName = "java:portal/MembershipModule";

	private final String transactionJNDIName = "java:/TransactionManager";

	private UserModule userDao;

	private RoleModule roleDao;

	private MembershipModule memDao;

	private UserProfileModule userProfileDao;

	@Resource
	private InfoService infoService;

	public JbossPortalUserServiceImpl() {
	}

	public boolean isAvailable() {
		try {
			if (getTransactionManager() != null) {
				return true;
			}

		} catch (Throwable e) {
			;
		}
		return false;
	}

	public List<UserProfile> searchUserProfile(SearchExpression searchExpression) {

		return null;
	}

	public int countUserProfile(SearchExpression searchExpression) {

		return 0;
	}

	public List<RoleCategory> getAllRoles() {

		return null;
	}

	public boolean assignRole(final String userId, final Long roleId) {
		if (userId == null || roleId == null) {
			return false;
		}
		return this.addRole(userId, null, roleId, true);
	}

	public boolean assignRole(final String userId, final String roleName) {
		if (userId == null || roleName == null) {
			return false;
		}
		return this.addRole(userId, roleName, null, false);

	}

	public void deleteUser(final String userId) {
		final ThrowableObject throwableObject = new ThrowableObject();

		try {
			Transactions.required(getTransactionManager(), new Transactions.Runnable() {
				public Object run() {
					try {
						User user = getUserModule().findUserByUserName(userId);
						if (user != null) {
							getUserModule().removeUser(user.getId());
						}
					} catch (NoSuchUserException nsue) {
						log.debug("no such user " + userId);
					} catch (Throwable e) {
						throwableObject.setThrowable(e);
						log.warn("", e);
					}
					return null;
				}
			});

			if (throwableObject.getThrowable() != null) {
				throw throwableObject.getThrowable();
			}

		} catch (Throwable e) {
			throw new UserOperationException(e);
		}
	}

	public RoleCategory getRole(final Long roleId) {
		return this.getRoleCategory(roleId, null, true);

	}

	public RoleCategory getRoleByName(final String roleName) {

		return this.getRoleCategory(null, roleName, false);
	}

	public List<RoleCategory> getRoleCategoryByUserId(final String userId) {
		List<Long> roleIds = getRoleIdByUserId(userId);
		if (roleIds == null || roleIds.size() == 0) {
			return null;
		}
		List<RoleCategory> roleCategories = new ArrayList<RoleCategory>();
		for (Long roleId : roleIds) {
			RoleCategory roleCategory = this.getRoleCategory(roleId, null, true);
			if (roleCategory != null) {
				roleCategories.add(roleCategory);
			}
		}
		return roleCategories;
	}

	public UserProfile getUser(final String userId) {

		return this.jbossUserToUserProfile(userId);
	}

	public boolean removeRole(final String userId, final Long roleId) {

		return this.removeRole(userId, roleId, null, true);
	}

	public boolean removeRole(final String userId, final String roleName) {
		return this.removeRole(userId, null, roleName, false);
	}

	public void saveRole(final RoleCategory role) {

		String displayName = role.getDisplayName();
		if (displayName == null) {
			displayName = role.getRoleName();
		}
		this.createRole(role.getRoleName(), displayName);

	}

	public void saveUser(final UserProfile sp) {

		if (sp == null)
			throw new IllegalArgumentException("user profile is null or roleId < 0");
		this.createUser(sp.getUserid(), sp.getPassword());
		this.saveProfile(sp.getUserid(), sp);
		this.updatePassword(sp.getUserid(), sp.getPassword());

	}

	public void updateRole(RoleCategory role) {

		throw new UnsupportMethodException("This method can't be invoke because jboss API does not support.");
	}

	public void updateUser(final UserProfile sp) {

		if (sp == null) {
			return;
		}
		log.debug("%%%%enter updateUser" + sp.getUserid() + sp.getAddress() + sp.getCellphone() + sp.getEmail() + sp.getGender());

		saveProfile(sp.getUserid(), sp);

	}

	private boolean addRole(final String userId, final String roleName, final Long roleId, final boolean idOrName) throws UserOperationException {
		final ThrowableObject throwableObject = new ThrowableObject();

		Object o = Transactions.required(getTransactionManager(), new Transactions.Runnable() {
			public Object run() {
				try {
					User u = getUserModule().findUserByUserName(userId);
					log.debug("role name is:" + roleName);
					Set oldRoles = getMembershipModule().getRoles(u);

					Set<Role> roles = new HashSet<Role>();

					roles.addAll(oldRoles);

					if (idOrName) {
						roles.add(getRoleModule().findRoleById(roleId));
					} else {
						roles.add(getRoleModule().findRoleByName(roleName));

					}
					log.debug("roles size is:" + roles.size());
					getMembershipModule().assignRoles(u, roles);
					log.debug("assignrole success.");
					return true;

				} catch (NoSuchUserException nsue) {
					log.warn("no such user " + userId);
				} catch (IdentityException ie) {
					log.warn("no such role " + ie.getMessage());
				} catch (Throwable e) {
					throwableObject.setThrowable(e);
					log.warn("", e);
				}
				return false;
			}

		});

		if (throwableObject.getThrowable() != null) {
			throw new UserOperationException(throwableObject.getThrowable());
		}
		return o == null ? null : (Boolean) o;
	}

	private void createRole(final String roleName, final String roleDisplayName) {

		final ThrowableObject throwableObject = new ThrowableObject();

		Transactions.required(getTransactionManager(), new Transactions.Runnable() {
			public Object run() {
				try {
					return getRoleModule().createRole(roleName, roleDisplayName);
				} catch (IdentityException ie) {
					log.debug(ie.getMessage());
				} catch (Throwable e) {
					throwableObject.setThrowable(e);
					log.warn("", e);
				}
				return null;

			}
		});

		if (throwableObject.getThrowable() != null) {
			throw new UserOperationException(throwableObject.getThrowable());
		}

	}

	private void createUser(final String userId, final String password) {
		final ThrowableObject throwableObject = new ThrowableObject();

		Transactions.required(getTransactionManager(), new Transactions.Runnable() {
			public Object run() {
				User user = null;
				try {
					user = getUserModule().findUserByUserName(userId);

				} catch (NoSuchUserException nsue) {
					log.debug("No such user ");
				} catch (IdentityException ie) {
					log.debug(ie.getMessage());
				} catch (Throwable e) {
				}
				try {
					if (user == null) {
						user = getUserModule().createUser(userId, password);

						log.debug("create user success.");
					}
				} catch (Throwable e) {
					throwableObject.setThrowable(e);
					log.warn("", e);
				}
				return null;
			}
		});
		if (throwableObject.getThrowable() != null) {
			throw new UserOperationException(throwableObject.getThrowable());
		}

	}

	private RoleCategory getRoleCategory(final Long roleId, final String roleName, final boolean isIdOrName) {

		final ThrowableObject throwableObject = new ThrowableObject();

		try {
			Object o = Transactions.required(getTransactionManager(), new Transactions.Runnable() {
				public Object run() {
					try {
						Role role = null;
						if (isIdOrName) {
							role = getRoleModule().findRoleById(roleId);
						} else {
							role = getRoleModule().findRoleByName(roleName);
						}
						RoleCategory rc = null;

						if (role != null) {
							log.debug("get role role=" + role);
							rc = new RoleCategory();
							log.debug("roleId is " + role.getId());
							rc.setId((Long) role.getId());
							rc.setRoleName(role.getName());
							rc.setDisplayName(role.getDisplayName());
							log.debug("jbossRoleToRoleCategory role set finished");
						}
						return rc;
					} catch (IdentityException ie) {
						log.debug(ie.getMessage());
					} catch (Throwable e) {
						throwableObject.setThrowable(e);
						log.warn("", e);
					}
					return null;
				}
			});

			if (throwableObject.getThrowable() != null) {
				throw throwableObject.getThrowable();
			}

			return o == null ? null : (RoleCategory) o;
		} catch (Throwable e) {
			throw new UserOperationException(e);
		}
	}

	private List<Long> getRoleIdByUserId(final String userId) {

		final ThrowableObject throwableObject = new ThrowableObject();

		final List<Long> roleIds = new ArrayList<Long>();

		Transactions.required(getTransactionManager(), new Transactions.Runnable() {
			public Object run() {
				try {
					User u = getUserModule().findUserByUserName(userId);
					Set<Role> roles = getMembershipModule().getRoles(u);
					if (roles != null && roles.size() != 0) {
						for (Iterator iterator = roles.iterator(); iterator.hasNext();) {
							Role r = (Role) iterator.next();
							roleIds.add((Long) r.getId());
						}
					}

				} catch (NoSuchUserException nsue) {
					log.debug("no such user " + userId);
				} catch (IdentityException ie) {
					log.debug(ie.getMessage());
				} catch (Throwable e) {
					throwableObject.setThrowable(e);
					log.warn("", e);
				}

				return roleIds;
			}
		});

		if (throwableObject.getThrowable() != null) {
			throw new UserOperationException(throwableObject.getThrowable());
		}

		return roleIds;

	}

	private UserProfile jbossUserToUserProfile(final String userId) {

		final ThrowableObject throwableObject = new ThrowableObject();

		final SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss");

		Object result = Transactions.required(getTransactionManager(), new Transactions.Runnable() {
			public Object run() {
				try {

					User u = getUserModule().findUserByUserName(userId);

					final UserProfile sp = new UserProfile();
					sp.setUserid(userId);

					Object obj = getUserProfileModule().getProperty(u, User.INFO_USER_EMAIL_REAL);
					sp.setEmail((String) (obj == null ? "" : obj));

					obj = getUserProfileModule().getProperty(u, User.INFO_USER_NAME_FAMILY);
					sp.setFirstname((String) (obj == null ? "" : obj));

					obj = getUserProfileModule().getProperty(u, User.INFO_USER_NAME_GIVEN);
					sp.setLastname((String) (obj == null ? "" : obj));

					obj = getUserProfileModule().getProperty(u, UserProfileType.IDCARD.toString());
					sp.setIdcard((String) (obj == null ? "" : obj));

					obj = getUserProfileModule().getProperty(u, User.INFO_USER_LOCATION);
					sp.setAddress((String) (obj == null ? "" : obj));

					obj = getUserProfileModule().getProperty(u, User.INFO_USER_ENABLED);
					sp.setDisabled(Boolean.valueOf(!((Boolean) (obj == null ? Boolean.TRUE : obj)).booleanValue()));

					obj = getUserProfileModule().getProperty(u, UserProfileType.COMPANY.toString());
					sp.setCompany((String) (obj == null ? "" : obj));

					obj = getUserProfileModule().getProperty(u, UserProfileType.GENDER.toString());
					sp.setGender((String) (obj == null ? "0" : obj));

					obj = getUserProfileModule().getProperty(u, UserProfileType.CELLPHONE.toString());
					sp.setCellphone((String) (obj == null ? "" : obj));

					obj = getUserProfileModule().getProperty(u, UserProfileType.ZIP.toString());
					sp.setZip((String) (obj == null ? "" : obj));

					obj = getUserProfileModule().getProperty(u, UserProfileType.COUNTRY.toString());
					if (obj != null) {
						Country c = new Country();
						c.setName((String) obj);
						sp.setCountry(c);
					}

					obj = getUserProfileModule().getProperty(u, UserProfileType.LANGUAGE.toString());

					if (obj != null) {
						Language l = new Language();
						l.setName((String) obj);
						sp.setLanguage(l);
					}
					try {
						obj = getUserProfileModule().getProperty(u, UserProfileType.BIRTHDAY.toString());
						sp.setBirthday(obj == null ? null : sdf.parse((String) obj));

						obj = getUserProfileModule().getProperty(u, UserProfileType.CREATEDATE.toString());
						sp.setCreateDate(obj == null ? null : sdf.parse((String) obj));
					} catch (Throwable e) {
						log.warn("Parse date time error", e);
					}

					obj = getUserProfileModule().getProperty(u, UserProfileType.ONLINETIME.toString());
					try {
						sp.setOnlineTime(obj == null ? 0L : Long.parseLong((String) obj));
					} catch (Throwable e) {
						sp.setOnlineTime(0L);

					}

					log.debug("**** userId" + sp.getUserid());

					return sp;
				} catch (NoSuchUserException nsue) {
					log.debug("No such user " + userId);
				} catch (IdentityException ie) {
					log.debug(ie.getMessage());
				} catch (Throwable e) {
					throwableObject.setThrowable(e);
					log.warn("", e);
				}
				return null;

			}
		});
		if (throwableObject.getThrowable() != null) {
			throw new UserOperationException(throwableObject.getThrowable());
		}
		if (result == null) {
			return null;
		}
		UserProfile sp = (UserProfile) result;

		// Set country and language
		try {
			if (sp.getCountry() != null) {
				sp.setCountry(infoService.getCountryByName(sp.getCountry().getName()));
			}
			if (sp.getLanguage() != null) {
				sp.setLanguage(infoService.getLanguageByName(sp.getLanguage().getName()));
			}
		} catch (Throwable e) {
			log.warn(e);
		}
		List<Long> roleIds = getRoleIdByUserId(userId);
		if (roleIds != null && roleIds.size() > 0) {

			Set<UserRoleCategory> srcs = new HashSet<UserRoleCategory>();

			for (Long roleId : roleIds) {
				RoleCategory rc = this.getRoleCategory(roleId, null, true);
				if (rc != null) {
					UserRoleCategory src = new UserRoleCategory();
					src.setUserProfile(sp);
					src.setRoleCategory(rc);
					srcs.add(src);
				}
			}

			sp.setUserRoleCategories(srcs);
		}

		log.debug(sp.getAddress() + " " + sp.getCellphone() + " " + sp.getCompany() + " " + sp.getEmail() + " " + sp.getFirstname());
		return sp;

	}

	private boolean removeRole(final String userId, final Long roleId, final String roleName, final boolean isIdOrName) {

		final ThrowableObject throwableObject = new ThrowableObject();

		Object o = Transactions.required(getTransactionManager(), new Transactions.Runnable() {
			public Object run() {
				try {
					User u = getUserModule().findUserByUserName(userId);
					Set<Role> newRs = new HashSet<Role>();

					Set<Role> rs = getMembershipModule().getRoles(u);

					Role rmRole = null;

					for (Role r : rs) {
						if (isIdOrName) {
							if (r.getId().equals(r.getId())) {
								rmRole = r;
								continue;
							} else {
								newRs.add(r);
							}
						} else {
							if (r.getName().equals(roleName)) {
								rmRole = r;
								continue;
							} else {
								newRs.add(r);
							}
						}
					}
					if (rmRole != null) {
						log.debug("find role to be remove " + rmRole.getName());
						log.debug("delete role relationship.");
						getMembershipModule().assignRoles(u, newRs);
						return true;
					}
				} catch (NoSuchUserException nsue) {
					log.debug("no such user " + userId);
				} catch (IdentityException ie) {
					log.debug(ie.getMessage());
				} catch (Throwable e) {
					throwableObject.setThrowable(e);
					log.warn("", e);
				}

				return false;
			}
		});
		if (throwableObject.getThrowable() != null) {
			throw new UserOperationException(throwableObject.getThrowable());
		}

		return o == null ? null : (Boolean) o;

	}

	private void saveProfile(final String userId, final UserProfile sp) {

		final ThrowableObject throwableObject = new ThrowableObject();

		final SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss");

		Transactions.required(getTransactionManager(), new Transactions.Runnable() {
			public Object run() {
				try {
					User user = getUserModule().findUserByUserName(userId);
					log.debug("start save user profile.");
					getUserProfileModule().setProperty(user, User.INFO_USER_ENABLED, Boolean.TRUE);
					getUserProfileModule().setProperty(user, User.INFO_USER_EMAIL_REAL, sp.getEmail());
					getUserProfileModule().setProperty(user, User.INFO_USER_NAME_FAMILY, sp.getFirstname());
					getUserProfileModule().setProperty(user, User.INFO_USER_NAME_GIVEN, sp.getLastname());
					getUserProfileModule().setProperty(user, User.INFO_USER_LOCATION, sp.getAddress());
					getUserProfileModule().setProperty(user, UserProfileType.IDCARD.toString(), sp.getIdcard());
					getUserProfileModule().setProperty(user, UserProfileType.COMPANY.toString(), sp.getCompany());
					getUserProfileModule().setProperty(user, UserProfileType.GENDER.toString(), sp.getGender());
					getUserProfileModule().setProperty(user, UserProfileType.CELLPHONE.toString(), sp.getCellphone());

					getUserProfileModule().setProperty(user, UserProfileType.BIRTHDAY.toString(),
							sdf.format(sp.getBirthday() == null ? new Date() : sp.getBirthday()));

					if (sp.getCountry() != null) {
						getUserProfileModule().setProperty(user, UserProfileType.COUNTRY.toString(), sp.getCountry().getName());
					}
					if (sp.getLanguage() != null) {
						getUserProfileModule().setProperty(user, UserProfileType.LANGUAGE.toString(), sp.getLanguage().getName());
					}
					getUserProfileModule().setProperty(user, UserProfileType.ZIP.toString(), sp.getZip());
					getUserProfileModule().setProperty(user, UserProfileType.CELLPHONE.toString(), sp.getCellphone());

					getUserProfileModule().setProperty(user, UserProfileType.CREATEDATE.toString(),
							sdf.format(sp.getCreateDate() == null ? new Date() : sp.getCreateDate()));

					getUserProfileModule().setProperty(user, UserProfileType.ONLINETIME.toString(), sp.getOnlineTime() + "");

					log.debug("save user profile success.");
				} catch (IdentityException ie) {
					log.debug(ie.getMessage());
				} catch (Throwable e) {
					throwableObject.setThrowable(e);
					log.warn("", e);
				}
				return null;
			}
		});

		if (throwableObject.getThrowable() != null) {
			throw new UserOperationException(throwableObject.getThrowable());
		}

	}

	protected MembershipModule getMembershipModule() throws UserOperationException {

		if (memDao == null) {
			try {
				memDao = (MembershipModule) new InitialContext().lookup(membershipModuleJNDIName);
			} catch (NamingException e) {
				throw new UserOperationException(e);
			}
		}
		return memDao;
	}

	protected RoleModule getRoleModule() throws UserOperationException {

		if (roleDao == null) {
			try {
				roleDao = (RoleModule) new InitialContext().lookup(roleModuleJNDIName);
			} catch (NamingException e) {
				throw new UserOperationException(e);
			}
		}
		return roleDao;
	}

	protected TransactionManager getTransactionManager() throws UserOperationException {
		try {
			return (TransactionManager) new InitialContext().lookup(transactionJNDIName);
		} catch (NamingException e) {
			throw new UserOperationException(e);
		}
	}

	protected UserModule getUserModule() throws UserOperationException {

		if (userDao == null) {
			try {
				userDao = (UserModule) new InitialContext().lookup(userModuleJNDIName);
			} catch (NamingException e) {
				throw new UserOperationException(e);
			}
		}

		return userDao;
	}

	protected UserProfileModule getUserProfileModule() throws UserOperationException {

		if (userProfileDao == null) {
			try {
				userProfileDao = (UserProfileModule) new InitialContext().lookup(userProfileModuleJNDIName);
			} catch (NamingException e) {
				throw new UserOperationException(e);
			}
		}
		return userProfileDao;
	}

	public void updatePassword(final String userId, final String password) {
		final ThrowableObject throwableObject = new ThrowableObject();

		Object o = Transactions.required(getTransactionManager(), new Transactions.Runnable() {
			public Object run() {
				try {
					User u = getUserModule().findUserByUserName(userId);
					if (u == null) {
						return false;
					}
					u.updatePassword(password);

				} catch (NoSuchUserException nsue) {
					log.debug("no such user " + userId);
				} catch (IdentityException ie) {
					log.debug(ie.getMessage());
				} catch (Throwable e) {
					throwableObject.setThrowable(e);
					log.warn("", e);
				}

				return false;
			}
		});
		if (throwableObject.getThrowable() != null) {
			throw new UserOperationException(throwableObject.getThrowable());
		}

	}

	public boolean validatePassword(final String userId, final String inputPassword) {
		final ThrowableObject throwableObject = new ThrowableObject();

		Object o = Transactions.required(getTransactionManager(), new Transactions.Runnable() {
			public Object run() {
				try {
					User u = getUserModule().findUserByUserName(userId);
					if (u == null) {
						return false;
					}
					return u.validatePassword(inputPassword);

				} catch (NoSuchUserException nsue) {
					log.debug("no such user " + userId);
				} catch (IdentityException ie) {
					log.debug(ie.getMessage());
				} catch (Throwable e) {
					throwableObject.setThrowable(e);
					log.warn("", e);
				}

				return false;
			}
		});
		if (throwableObject.getThrowable() != null) {
			throw new UserOperationException(throwableObject.getThrowable());
		}
		return o == null ? false : (Boolean) o;
	}

	public void disableUser(final String userId, final boolean disabled) {

		final ThrowableObject throwableObject = new ThrowableObject();

		Transactions.required(getTransactionManager(), new Transactions.Runnable() {
			public Object run() {
				try {
					User user = getUserModule().findUserByUserName(userId);
					if (user == null) {
						return false;
					}
					log.debug("disable user to[" + disabled + "]");
					getUserProfileModule().setProperty(user, User.INFO_USER_ENABLED, Boolean.valueOf(!disabled));
				} catch (NoSuchUserException nsue) {
					log.debug("no such user " + userId);
				} catch (IdentityException ie) {
					log.debug(ie.getMessage());
				} catch (Throwable e) {
					throwableObject.setThrowable(e);
					log.warn("", e);
				}

				return false;
			}
		});
		if (throwableObject.getThrowable() != null) {
			throw new UserOperationException(throwableObject.getThrowable());
		}
	}

	public void addUserToProvider(String userId, Long providerId) {
		// do nothing

	}

	public List<UserProfile> getUsersByProvider(Long providerId) {
		// do nothing
		return null;
	}

	public void removeUserFromProvider(String userId, Long providerId) {
		// do nothing

	}

}

// $Id$