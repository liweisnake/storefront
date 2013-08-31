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
package com.hp.sdf.ngp.workflow;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.annotation.Resource;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.hp.sdf.ngp.workflow.descriptor.AccessDescriptor;
import com.hp.sdf.ngp.workflow.descriptor.AssetBinaryVersionLifeCycleDescriptor;
import com.hp.sdf.ngp.workflow.descriptor.AttributeDescriptor;
import com.hp.sdf.ngp.workflow.descriptor.UserCategoryDescriptor;
import com.hp.sdf.ngp.workflow.descriptor.WorkFlowDescriptor;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:META-INF/applicationContext-test.xml" })
public class WorkFlowTransformerTestCase {

	@Resource
	private WorkFlowTransformer workFlowTransformer;

	@Test
	public void testToXML() throws Exception {
		WorkFlowDescriptor workFlowDescriptor = new WorkFlowDescriptor();

		UserCategoryDescriptor userCategoryDescriptor = new UserCategoryDescriptor();
		userCategoryDescriptor.setName("registered");
		userCategoryDescriptor.setDisplay("Registered User");
		userCategoryDescriptor.setDescription("Blah, blah, blah...");
		AttributeDescriptor[] attributes = new AttributeDescriptor[2];
		AttributeDescriptor attribute = new AttributeDescriptor();
		attribute.setName("SGF_domain");
		attribute.setValue("sandbox");
		attributes[0] = attribute;
		attribute = new AttributeDescriptor();
		attribute.setName("billing_type");
		attribute.setValue("BOBO user");
		attributes[1] = attribute;
		userCategoryDescriptor.setAttributes(attributes);

		userCategoryDescriptor.setPrivileges(new String[] {
				"browse_app_gallery", "comment_app", "download_app" });
		
		workFlowDescriptor
				.setUserCategoryDescriptors(new UserCategoryDescriptor[] { userCategoryDescriptor });

		AssetBinaryVersionLifeCycleDescriptor applicationLifeCycleDescriptor = new AssetBinaryVersionLifeCycleDescriptor();
		applicationLifeCycleDescriptor.setName("new");
		applicationLifeCycleDescriptor.setDisplay("new created");
		applicationLifeCycleDescriptor.setDescription("Blah, blah, blah...");

		AccessDescriptor[] accesses = new AccessDescriptor[3];
		AccessDescriptor accessDescriptor = new AccessDescriptor();
		accessDescriptor.setOperation("browse");
		accessDescriptor.setAllowed(new String[] { "self", "admin" });
		accesses[0] = accessDescriptor;
		accessDescriptor = new AccessDescriptor();
		accessDescriptor.setOperation("download");
		accessDescriptor.setAllowed(new String[] { "self", "admin", "tester" });
		accesses[1] = accessDescriptor;
		accessDescriptor = new AccessDescriptor();
		accessDescriptor.setOperation("tagging");
		accessDescriptor.setAllowed(new String[] { "self", "admin" });
		accesses[2] = accessDescriptor;
		applicationLifeCycleDescriptor.setAccesss(accesses);
		

		workFlowDescriptor
				.setApplicationBinaryLifeCycles(new AssetBinaryVersionLifeCycleDescriptor[] { applicationLifeCycleDescriptor });

		System.out.println("----------------------------");
		System.out.println(workFlowTransformer.marshal(workFlowDescriptor));

	}

	@Test
	public void testToObject() throws Exception {
		InputStream inputStream = getClass().getResourceAsStream(
				"/workflowsample.xml");
		BufferedReader bf = new BufferedReader(new InputStreamReader(
				inputStream));

		StringBuffer buffer = new StringBuffer();
		String line = "";
		while ((line = bf.readLine()) != null) {
			buffer.append(line);
		}
		String xml=buffer.toString();
		System.out.println("----------------------------");
		System.out.println(xml);

		WorkFlowDescriptor workFlowDescriptor = workFlowTransformer
				.unmarshal(xml);
		Assert
				.assertTrue(workFlowDescriptor.getUserCategoryDescriptors().length > 0);
	}
}

// $Id$