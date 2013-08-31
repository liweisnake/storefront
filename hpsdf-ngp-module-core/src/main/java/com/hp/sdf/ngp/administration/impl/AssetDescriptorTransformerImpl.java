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
package com.hp.sdf.ngp.administration.impl;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.xml.transform.stream.StreamSource;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.oxm.xstream.XStreamMarshaller;
import org.springframework.stereotype.Component;

import com.hp.sdf.ngp.administration.AssetDescriptorTransformer;
import com.hp.sdf.ngp.administration.descriptor.AssetDescriptor;

@Component
public class AssetDescriptorTransformerImpl implements
		AssetDescriptorTransformer {

	private static final Log log = LogFactory
			.getLog(AssetDescriptorTransformerImpl.class);

	public AssetDescriptor load(String xml) throws IOException {
		log.debug("load>>Enter load xml method.");

		if (StringUtils.isBlank(xml))
			return null;

		XStreamMarshaller marshaller = new XStreamMarshaller();
		Map<String, Object> aliases = new HashMap<String, Object>();

		aliases.put("name", String.class);
		aliases.put("author", String.class);
		aliases.put("brief", String.class);
		aliases.put("description", String.class);
		aliases.put("category", String.class);
		aliases.put("preview", String.class);
		aliases.put("thumbnail", String.class);
		aliases.put("document", String.class);
		aliases.put("platform", String.class);
		aliases.put("version", String.class);
		aliases.put("file", String.class);
		aliases.put("asset", AssetDescriptor.class);

		try {
			marshaller.setAliases(aliases);
		} catch (Throwable e) {
			throw new IOException(e.getMessage());
		}

		StreamSource streamSource = new StreamSource(new ByteArrayInputStream(
				xml.getBytes("UTF8")));

		return (AssetDescriptor) marshaller.unmarshal(streamSource);
	}

}

// $Id$