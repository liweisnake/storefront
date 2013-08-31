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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.springframework.oxm.xstream.XStreamMarshaller;
import org.springframework.stereotype.Component;

import com.hp.sdf.ngp.common.exception.ObjectXMLTransformException;
import com.hp.sdf.ngp.workflow.WorkFlowTransformer;
import com.hp.sdf.ngp.workflow.descriptor.AccessDescriptor;
import com.hp.sdf.ngp.workflow.descriptor.ApplicationLifeCycleDescriptor;
import com.hp.sdf.ngp.workflow.descriptor.AssetBinaryVersionLifeCycleDescriptor;
import com.hp.sdf.ngp.workflow.descriptor.AttributeDescriptor;
import com.hp.sdf.ngp.workflow.descriptor.UserCategoryDescriptor;
import com.hp.sdf.ngp.workflow.descriptor.WorkFlowDescriptor;

/**
 * 
 * Transform {$Link WorkFlowDescriptor} between object and XML format string
 * 
 */
@Component
public class WorkFlowTransformerImpl implements WorkFlowTransformer {

	private XStreamMarshaller marshaller = new XStreamMarshaller();

	@SuppressWarnings("unchecked")
	public WorkFlowTransformerImpl() {
		Map aliases = new HashMap();
		// mapping the class type with the user friendly name
		aliases.put("value", String.class);
		aliases.put("category", UserCategoryDescriptor.class);

		aliases.put("attribute", AttributeDescriptor.class);



		aliases.put("access", AccessDescriptor.class);

		aliases.put("applifecycle", ApplicationLifeCycleDescriptor.class);

		aliases.put("binarylifecycle",
				AssetBinaryVersionLifeCycleDescriptor.class);

		aliases.put("storefront", WorkFlowDescriptor.class);



		try {
			marshaller.setAliases(aliases);
		} catch (Throwable e) {
			throw new ObjectXMLTransformException(e);
		}

	}

	public String marshal(WorkFlowDescriptor workFlowDescriptor)
			throws ObjectXMLTransformException {
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		StreamResult result = new StreamResult(byteArrayOutputStream);
		try {
			marshaller.marshal(workFlowDescriptor, result);

			return new String(
					((ByteArrayOutputStream) result.getOutputStream())
							.toByteArray(), "UTF8");

		} catch (Throwable e) {
			throw new ObjectXMLTransformException(
					"can't marshal the workFlowDescriptor["
							+ workFlowDescriptor + "] to xml format");
		}
	}

	public WorkFlowDescriptor unmarshal(String xml)
			throws ObjectXMLTransformException {

		try {
			StreamSource streamSource = new StreamSource(
					new ByteArrayInputStream(xml.getBytes("UTF8")));
			WorkFlowDescriptor workFlowDescriptor = (WorkFlowDescriptor) marshaller
					.unmarshal(streamSource);
			return workFlowDescriptor;
		} catch (Throwable e) {
			throw new ObjectXMLTransformException("can't unmarshal the string["
					+ xml + "] to WorkFlowDescriptor object", e);
		}

	}
}

// $Id$