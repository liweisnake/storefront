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
import com.hp.sdf.ngp.model.Status;
import com.hp.sdf.ngp.model.UserProfile;
import com.hp.sdf.ngp.service.ApplicationService;
import com.hp.sdf.ngp.service.UserService;
import com.hp.sdf.ngp.workflow.AccessType;
import com.hp.sdf.ngp.workflow.AssetLifeCycleEngine;
import com.hp.sdf.ngp.workflow.Privilege;
import com.hp.sdf.ngp.workflow.descriptor.AccessDescriptor;
import com.hp.sdf.ngp.workflow.descriptor.ApplicationLifeCycleDescriptor;
import com.hp.sdf.ngp.workflow.descriptor.WorkFlowDescriptor;

@Component
public class AssetLifeCycleEngineImpl implements
		AssetLifeCycleEngine {
	private final static Log log = LogFactory
			.getLog(AssetLifeCycleEngineImpl.class);

	private WorkFlowDescriptor workFlowDescriptor;

	@Resource
	private ApplicationService applicationService;

	@Resource
	private UserService userService;

	private ApplicationLifeCycleDescriptor findApplicationLifeCycle(
			String status) {

		if (workFlowDescriptor == null) {
			throw new WorkFlowFailureException(
					"No available work flow descriptor");
		}

		ApplicationLifeCycleDescriptor[] applicationLifeCycleDescriptors = workFlowDescriptor
				.getApplicationLifeCycles();

		if (applicationLifeCycleDescriptors == null
				|| applicationLifeCycleDescriptors.length == 0) {
			return null;
		}
		if (status == null) {
			return applicationLifeCycleDescriptors[0];
		}
		log.debug("Check the application status[" + status + "]");
		for (ApplicationLifeCycleDescriptor applicationLifeCycleDescriptor : applicationLifeCycleDescriptors) {
			log.debug("check the applicationLifeCycleDescriptor["
					+ applicationLifeCycleDescriptor.getName() + "]");
			if (applicationLifeCycleDescriptor.getName().equals(status)) {
				log.debug("found an available applicationLifeCycleDescriptor");
				return applicationLifeCycleDescriptor;

			}
		}
		return null;
	}

	public String[] getAccessPrivilege(long assetId, AccessType accessType)
			throws WorkFlowFailureException {

		Asset asset = this.applicationService.getAsset(assetId);
		if (asset == null || asset.getStatus() == null) {
			log.debug("can't find the app for the app id[" + assetId + "]");
			return new String[] { Privilege.DEFAULT };
		}
		ApplicationLifeCycleDescriptor applicationLifeCycleDescriptor = this
				.findApplicationLifeCycle(asset.getStatus().getStatus());
		if (applicationLifeCycleDescriptor == null) {
			log.debug("can't find the app descriptor for the app name["
					+ asset.getStatus().getStatus() + "]");
			return new String[] { Privilege.DEFAULT };
		}
		AccessDescriptor[] accessDescriptors = applicationLifeCycleDescriptor
				.getAccesses();
		if (accessDescriptors == null || accessDescriptors.length == 0) {
			log
					.debug("No access descriptor definition for the app descriptor name["
							+ asset.getStatus().getStatus() + "]");
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
						.debug("All user is forbidden to access this app under this operation type["
								+ accessDescriptor.getOperation() + "]");
				break;
			}
			for (String allowed : alloweds) {
				if (allowed.equals(AccessDescriptor.OWNER)) {
					// add the privilege for the owner

					UserProfile subscriberProfile = userService.getUser(asset.getAuthorid());
					if (subscriberProfile != null) {
						result.add(Privilege.Construct
								.OwnerPrivilege(subscriberProfile.getUserid()));
					}

				} else {
					// add the privilege for corresponding user category
					result.add(Privilege.Construct.ApplicationPrivilege(
							applicationLifeCycleDescriptor.getName(),
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

	public void setWorkFlowDescriptor(WorkFlowDescriptor workFlowDescriptor) {
		this.workFlowDescriptor = workFlowDescriptor;

	}

	public Status getStartupStatus() {

		ApplicationLifeCycleDescriptor applicationLifeCycleDescriptor = this
				.findApplicationLifeCycle(null);
		if (applicationLifeCycleDescriptor != null) {
			return this.applicationService
					.getStatusByName(applicationLifeCycleDescriptor.getName());
		}
		return null;
	}

	public List<Status> getDefinedStatus() {
		if (workFlowDescriptor == null) {
			throw new WorkFlowFailureException(
					"No available work flow descriptor");
		}

		ApplicationLifeCycleDescriptor[] applicationLifeCycleDescriptors = workFlowDescriptor
				.getApplicationLifeCycles();

		if (applicationLifeCycleDescriptors == null
				|| applicationLifeCycleDescriptors.length == 0) {
			return null;
		}
		List<Status> statuses = new ArrayList<Status>();
		log.debug("Check the application status");
		for (ApplicationLifeCycleDescriptor applicationLifeCycleDescriptor : applicationLifeCycleDescriptors) {
			log.debug("check the applicationLifeCycleDescriptor["
					+ applicationLifeCycleDescriptor.getName() + "]");
			Status status = applicationService
					.getStatusByName(applicationLifeCycleDescriptor.getName());
			if (status != null) {
				statuses.add(status);
			}
			
		}
		return statuses;
	}

}

// $Id$