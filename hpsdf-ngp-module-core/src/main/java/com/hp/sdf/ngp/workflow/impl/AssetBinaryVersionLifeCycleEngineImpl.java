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
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

import com.hp.sdf.ngp.common.exception.WorkFlowFailureException;
import com.hp.sdf.ngp.model.Asset;
import com.hp.sdf.ngp.model.AssetBinaryVersion;
import com.hp.sdf.ngp.model.Status;
import com.hp.sdf.ngp.model.UserProfile;
import com.hp.sdf.ngp.service.ApplicationService;
import com.hp.sdf.ngp.service.UserService;
import com.hp.sdf.ngp.workflow.AccessType;
import com.hp.sdf.ngp.workflow.AssetBinaryVersionLifeCycleEngine;
import com.hp.sdf.ngp.workflow.Privilege;
import com.hp.sdf.ngp.workflow.descriptor.AccessDescriptor;
import com.hp.sdf.ngp.workflow.descriptor.AssetBinaryVersionLifeCycleDescriptor;
import com.hp.sdf.ngp.workflow.descriptor.WorkFlowDescriptor;

@Component
public class AssetBinaryVersionLifeCycleEngineImpl implements
		AssetBinaryVersionLifeCycleEngine {

	private final static Log log = LogFactory
			.getLog(AssetBinaryVersionLifeCycleEngineImpl.class);

	private WorkFlowDescriptor workFlowDescriptor;

	@Resource
	private ApplicationService applicationService;

	@Resource
	private UserService userService;

	public Status getStartupStatus() {
		AssetBinaryVersionLifeCycleDescriptor applicationBinaryLifeCycleDescriptor = this
				.findApplicationBinaryLifeCycle(null);
		if (applicationBinaryLifeCycleDescriptor != null) {
			return this.applicationService
					.getStatusByName(applicationBinaryLifeCycleDescriptor
							.getName());
		}
		return null;
	}

	private AssetBinaryVersionLifeCycleDescriptor findApplicationBinaryLifeCycle(
			String status) {

		if (workFlowDescriptor == null) {
			throw new WorkFlowFailureException(
					"No available work flow descriptor");
		}

		AssetBinaryVersionLifeCycleDescriptor[] applicationBinaryLifeCycleDescriptors = workFlowDescriptor
				.getApplicationBinaryLifeCycles();
		if (applicationBinaryLifeCycleDescriptors == null
				|| applicationBinaryLifeCycleDescriptors.length == 0) {
			return null;
		}
		if (status == null) {
			return applicationBinaryLifeCycleDescriptors[0];
		}
		log.debug("Check the application binary status[" + status + "]");
		for (AssetBinaryVersionLifeCycleDescriptor applicationBinaryLifeCycleDescriptor : applicationBinaryLifeCycleDescriptors) {
			log.debug("check the applicationBinaryLifeCycleDescriptor["
					+ applicationBinaryLifeCycleDescriptor.getName() + "]");
			if (applicationBinaryLifeCycleDescriptor.getName().equals(status)) {
				log
						.debug("found an available applicationBinaryLifeCycleDescriptor");
				return applicationBinaryLifeCycleDescriptor;

			}
		}
		return null;
	}

	public String[] getAccessPrivilege(long binaryId, AccessType accessType)
			throws WorkFlowFailureException {

		AssetBinaryVersion assetBinary = this.applicationService
				.getAssetBinaryById(binaryId);
		if (assetBinary == null || assetBinary.getStatus() == null) {
			log.debug("can't find the binary for the binary id[" + binaryId
					+ "]");
			return new String[] { Privilege.DEFAULT };
		}
		AssetBinaryVersionLifeCycleDescriptor applicationBinaryLifeCycleDescriptor = this
				.findApplicationBinaryLifeCycle(assetBinary.getStatus()
						.getStatus());
		if (applicationBinaryLifeCycleDescriptor == null) {
			log.debug("can't find the status descriptor for the status name["
					+ assetBinary.getStatus().getStatus() + "]");
			return new String[] { Privilege.DEFAULT };
		}
		AccessDescriptor[] accessDescriptors = applicationBinaryLifeCycleDescriptor
				.getAccesses();
		if (accessDescriptors == null || accessDescriptors.length == 0) {
			log
					.debug("No access descriptor definition for the status descriptor name["
							+ assetBinary.getStatus().getStatus() + "]");
			return new String[] { Privilege.DEFAULT };
		}
		List<String> result = new ArrayList<String>();
		boolean found = false;
		for (AccessDescriptor accessDescriptor : accessDescriptors) {
			if (!accessType.toString().equals(accessDescriptor.getOperation())) {
				continue;
			}
			found = true;
			String[] alloweds = accessDescriptor.getAllowed();
			if (alloweds == null) {
				log
						.debug("All user is forbidden to access this app binary under this operation type["
								+ accessDescriptor.getOperation() + "]");
				break;
			}
			for (String allowed : alloweds) {
				if (allowed.equals(AccessDescriptor.OWNER)) {
					// add the privilege for the owner
					Asset asset = this.applicationService
							.getAssetByBinaryId(binaryId);
					if (asset != null) {
						UserProfile userProfile = userService.getUser(asset.getAuthorid());
						if (userProfile != null) {
							result.add(Privilege.Construct
									.OwnerPrivilege(userProfile.getUserid()));
						}
					}

				} else {
					// add the privilege for corresponding user category
					result.add(Privilege.Construct.ApplicationPrivilege(
							applicationBinaryLifeCycleDescriptor.getName(),
							accessDescriptor.getOperation(), allowed));

				}

			}

		}
		if (!found) {
			// All user can access the application with this access type
			result.add(Privilege.DEFAULT);
		}
		return result.toArray(new String[0]);

	}

	

	private boolean isExistStatus(Status status, List<Status> statuses) {
		if (status == null || statuses == null || statuses.size() == 0) {
			return false;
		}
		for (Status value : statuses) {
			if (status.getStatus().equals(value.getStatus())) {
				return true;
			}
		}
		return false;
	}

	
	public void setWorkFlowDescriptor(WorkFlowDescriptor workFlowDescriptor) {
		this.workFlowDescriptor = workFlowDescriptor;
	}

	public List<Status> getDefinedStatus() {
		if (workFlowDescriptor == null) {
			throw new WorkFlowFailureException(
					"No available work flow descriptor");
		}

		AssetBinaryVersionLifeCycleDescriptor[] applicationBinaryLifeCycleDescriptors = workFlowDescriptor
				.getApplicationBinaryLifeCycles();

		if (applicationBinaryLifeCycleDescriptors == null
				|| applicationBinaryLifeCycleDescriptors.length == 0) {
			return null;
		}
		List<Status> statuses = new ArrayList<Status>();
		log.debug("Check the application binary status");
		for (AssetBinaryVersionLifeCycleDescriptor applicationBinaryLifeCycleDescriptor : applicationBinaryLifeCycleDescriptors) {
			log.debug("check the applicationBinaryLifeCycleDescriptor["
					+ applicationBinaryLifeCycleDescriptor.getName() + "]");
			Status status = applicationService
					.getStatusByName(applicationBinaryLifeCycleDescriptor
							.getName());
			if (status != null) {
				statuses.add(status);
			}

		}
		return statuses;
	}

}

// $Id$