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
package com.hp.sdf.ngp.ui.annotation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.Page;
import org.springframework.core.io.Resource;
import org.springframework.core.type.ClassMetadata;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.core.type.classreading.SimpleMetadataReaderFactory;
import org.wicketstuff.config.MatchingResources;

public class MatchingClassResources extends MatchingResources {

	public MatchingClassResources(String sPattern) {
		super(sPattern);
	}

	public List<Class<?>> getClassMatches(Class<? extends Page> pageClass) {
		List<Class<?>> matches = new ArrayList<Class<?>>();
		MetadataReaderFactory f = new SimpleMetadataReaderFactory();
		for (Resource r : getAllMatches()) {
			MetadataReader meta = null;
			try {
				meta = f.getMetadataReader(r);
			} catch (IOException e) {
				throw new RuntimeException("Unable to get MetadataReader for "
						+ r, e);
			}
			ClassMetadata data = meta.getClassMetadata();
			Class<?> object;
			try {
				object = Class.forName(data.getClassName());

			} catch (ClassNotFoundException e) {
				throw new RuntimeException(e);
			}

			if (pageClass.isAssignableFrom(object)) {
				matches.add(object);
			}
		}
		return matches;
	}
}

// $Id$