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
package com.hp.sdf.ngp.ui.dynamicForm;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Locale;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.util.file.IResourceFinder;
import org.apache.wicket.util.resource.FileResourceStream;
import org.apache.wicket.util.resource.IResourceStream;
import org.apache.wicket.util.resource.locator.ResourceStreamLocator;
import org.apache.wicket.util.string.Strings;
import org.springframework.stereotype.Component;

import com.hp.sdf.ngp.common.annotation.Value;

//load from configed path
@Component
public class MyOwnFinderStreamLocator extends ResourceStreamLocator {
	private final static Log log = LogFactory.getLog(MyOwnFinderStreamLocator.class);

	private String pageFilePath = "/opt/storefront/customize/dynamicform/";

	public String getPageFilePath() {
		return pageFilePath;
	}

	@Value("dynamicForm.pageFilePath")
	public void setPageFilePath(String pageFilePath) {
		this.pageFilePath = pageFilePath;
	}

	private IResourceFinder resourceFinder;

	public void addFinder(IResourceFinder resourceFinder) {
		if (resourceFinder != null) {
			this.resourceFinder = resourceFinder;
		}
	}

	@Override
	public IResourceStream locate(Class<?> clazz, String path, String style, Locale locale, String extension) {
		String className = Strings.lastPathComponent(clazz.getName(), '.');
		String simpleFileName = className + "." + extension;

		// support dynamic form
		final File file = new File(getPageFilePath() + simpleFileName);
		if (file.exists()) {
			return new FileResourceStream(file);
		}

		// support customize localization html file path
		// stream = super.locate(clazz, getPageFilePath() + className, style, locale, extension);
		// if (stream != null)
		// return stream;
		// else
		return super.locate(clazz, path, style, locale, extension);

	}

	@Override
	public IResourceStream locate(Class<?> clazz, String path) {

		String fileName = path.substring(path.lastIndexOf("/") + 1);
		String extName = Strings.lastPathComponent(path, '.');

		// support customize localization xml or property file path
		if (extName.equalsIgnoreCase("xml") || extName.equalsIgnoreCase("properties")) {
			File file = new File(getPageFilePath() + fileName);
			if (file.exists()) {
				return new FileResourceStream(file);
			}
		}

		return super.locate(clazz, path);
	}

	/**
	 * Load the XML descriptor from local file
	 * 
	 * @return
	 * @throws IOException
	 */
	public String loadConfigurationFile(String simpleFileName) throws IOException {

		StringBuffer buffer = new StringBuffer();
		FileInputStream fis = new FileInputStream(getPageFilePath() + simpleFileName);
		InputStreamReader isr = new InputStreamReader(fis, "UTF8");
		Reader in = new BufferedReader(isr);
		int ch;
		while ((ch = in.read()) > -1) {
			buffer.append((char) ch);
		}
		in.close();
		String result = buffer.toString();
		if (log.isDebugEnabled()) {
			log.debug("Load a work flow descriptor file:");
			log.debug(result);
		}
		return result;
	}

	public IResourceFinder getResourceFinder() {
		return resourceFinder;
	}

	public void setResourceFinder(IResourceFinder resourceFinder) {
		this.resourceFinder = resourceFinder;
	}
}
// $Id$