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
package com.hp.sdf.ngp.workflow.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

import com.hp.sdf.ngp.common.exception.WorkFlowFailureException;
import com.hp.sdf.ngp.model.RoleCategory;
import com.hp.sdf.ngp.service.UserService;
import com.hp.sdf.ngp.workflow.Privilege;
import com.hp.sdf.ngp.workflow.UserCategoryLifeCycleEngine;
import com.hp.sdf.ngp.workflow.descriptor.AccessDescriptor;
import com.hp.sdf.ngp.workflow.descriptor.ApplicationLifeCycleDescriptor;
import com.hp.sdf.ngp.workflow.descriptor.AssetBinaryVersionLifeCycleDescriptor;
import com.hp.sdf.ngp.workflow.descriptor.AttributeDescriptor;
import com.hp.sdf.ngp.workflow.descriptor.UserCategoryDescriptor;
import com.hp.sdf.ngp.workflow.descriptor.WorkFlowDescriptor;

@Component
public class UserCategoryLifeCycleEngineImpl implements
		UserCategoryLifeCycleEngine {
	private final static Log log = LogFactory
			.getLog(UserCategoryLifeCycleEngineImpl.class);

	@Resource
	private UserService userService;

	private WorkFlowDescriptor workFlowDescriptor;

	public RoleCategory getStartupRoleCategory() {
		UserCategoryDescriptor userCategoryDescriptor = this
				.findUserCategoryDescriptor(null);
		if (userCategoryDescriptor != null) {
			return userService.getRoleByName(userCategoryDescriptor.getName());
		}
		return null;
	}

	private UserCategoryDescriptor findUserCategoryDescriptor(
			String roleCategoryName) {

		if (workFlowDescriptor == null) {
			throw new WorkFlowFailureException(
					"No available work flow descriptor");
		}

		UserCategoryDescriptor[] userCategoryDescriptors = workFlowDescriptor
				.getUserCategoryDescriptors();
		if (userCategoryDescriptors == null
				|| userCategoryDescriptors.length == 0) {
			return null;
		}
		if (roleCategoryName == null) {
			return userCategoryDescriptors[0];
		}
		log.debug("Check the roleCategory[" + roleCategoryName + "]");
		for (UserCategoryDescriptor userCategoryDescriptor : userCategoryDescriptors) {
			log.debug("check the userCategoryDescriptor["
					+ userCategoryDescriptor.getName() + "]");
			if (userCategoryDescriptor.getName().equals(roleCategoryName)) {
				log.debug("found an available userCategoryDescriptor");
				return userCategoryDescriptor;

			}
		}
		return null;
	}

	public String getAttributeValue(String usrCategoryName, String attributeName)
			throws WorkFlowFailureException {
		UserCategoryDescriptor userCategoryDescriptor = findUserCategoryDescriptor(usrCategoryName);
		if (userCategoryDescriptor == null) {
			return null;
		}
		AttributeDescriptor[] attributeDescriptors = userCategoryDescriptor
				.getAttributes();
		if (attributeDescriptors == null || attributeDescriptors.length == 0) {
			return null;
		}
		for (AttributeDescriptor attributeDescriptor : attributeDescriptors) {
			if (attributeDescriptor.getName().equals(attributeName)) {
				return attributeDescriptor.getValue();
			}
		}
		return null;

	}

	private boolean isExistRole(RoleCategory roleCategory,
			List<RoleCategory> roleCategories) {
		if (roleCategory == null || roleCategories == null
				|| roleCategories.size() == 0) {
			return false;
		}
		for (RoleCategory value : roleCategories) {
			if (roleCategory.getRoleName().equals(value.getRoleName())) {
				return true;
			}
		}
		return false;
	}

	

	public String[] getUserPrivileges(String usrId)
			throws WorkFlowFailureException {

		List<RoleCategory> roleCategories = userService
				.getRoleCategoryByUserId(usrId);

		List<String> result = new ArrayList<String>();

		if (roleCategories == null || roleCategories.size() == 0) {
			// No privileges
		} else {
			for (RoleCategory roleCategory : roleCategories) {
				UserCategoryDescriptor userCategoryDescriptor = findUserCategoryDescriptor(roleCategory
						.getRoleName());
				if (userCategoryDescriptor != null) {
					if (userCategoryDescriptor.getPrivileges() != null) {
						result.addAll(Arrays.asList(userCategoryDescriptor
								.getPrivileges()));
					}
				}

			}
		}
		// above is the default privilege in user category description

		// Below is the privilege definition in application category
		// description
		// the format is appstatus_operation_usercategory
		ApplicationLifeCycleDescriptor[] applicationLifeCycleDescriptors = workFlowDescriptor
				.getApplicationLifeCycles();

		if (applicationLifeCycleDescriptors != null
				&& applicationLifeCycleDescriptors.length != 0) {

			for (ApplicationLifeCycleDescriptor applicationLifeCycleDescriptor : applicationLifeCycleDescriptors) {
				AccessDescriptor[] accessDescriptors = applicationLifeCycleDescriptor
						.getAccesses();
				if (accessDescriptors != null && accessDescriptors.length != 0) {
					for (AccessDescriptor accessDescriptor : accessDescriptors) {
						String operation = accessDescriptor.getOperation();
						String[] allows = accessDescriptor.getAllowed();
						if (allows != null && allows.length != 0) {
							for (RoleCategory roleCategory : roleCategories) {
								for (String allow : allows) {
									if (roleCategory.getRoleName()
											.equals(allow)) {
										// if this user role has this access
										// privilege, should add it

										result.add(Privilege.Construct
												.ApplicationPrivilege(
														applicationLifeCycleDescriptor
																.getName(),
														operation, allow));
										break;
									}
								}
							}
						}

					}
				}
			}
		}

		// Below is the privilege definition in application binary category
		// description
		// the format is appbinarystatus_operation_usercategory
		AssetBinaryVersionLifeCycleDescriptor[] applicationBinaryLifeCycleDescriptors = workFlowDescriptor
				.getApplicationBinaryLifeCycles();

		if (applicationBinaryLifeCycleDescriptors != null
				&& applicationBinaryLifeCycleDescriptors.length != 0) {

			for (AssetBinaryVersionLifeCycleDescriptor applicationBinaryLifeCycleDescriptor : applicationBinaryLifeCycleDescriptors) {
				AccessDescriptor[] accessDescriptors = applicationBinaryLifeCycleDescriptor
						.getAccesses();
				if (accessDescriptors != null && accessDescriptors.length != 0) {
					for (AccessDescriptor accessDescriptor : accessDescriptors) {
						String operation = accessDescriptor.getOperation();
						String[] allows = accessDescriptor.getAllowed();
						if (allows != null && allows.length != 0) {
							for (RoleCategory roleCategory : roleCategories) {
								for (String allow : allows) {
									if (roleCategory.getRoleName()
											.equals(allow)) {
										// if this user role has this access
										// privilege, should add it

										result.add(Privilege.Construct
												.ApplicationPrivilege(
														applicationBinaryLifeCycleDescriptor
																.getName(),
														operation, allow));
										break;
									}
								}
							}
						}

					}
				}
			}
		}

		// Add the default privilege for every user category
		result.add(Privilege.DEFAULT);
		// finally, we should add a unique privilege string for each user itself
		result.add(Privilege.Construct.OwnerPrivilege(usrId));

		return result.toArray(new String[0]);
	}

	

	public void setWorkFlowDescriptor(WorkFlowDescriptor workFlowDescriptor) {
		this.workFlowDescriptor = workFlowDescriptor;

	}

}

// $Id$