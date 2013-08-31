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

import javax.servlet.ServletContext;

import org.apache.wicket.Application;
import org.apache.wicket.IClusterable;
import org.apache.wicket.MetaDataKey;
import org.apache.wicket.injection.ComponentInjector;
import org.apache.wicket.injection.web.InjectorHolder;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.spring.ISpringContextLocator;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

public class CustomizedSpringComponentInjector extends ComponentInjector {



	/**
	 * Metadata key used to store application context holder in application's
	 * metadata
	 */
	private static MetaDataKey<ApplicationContextHolder> CONTEXT_KEY = new MetaDataKey<ApplicationContextHolder>() {

		private static final long serialVersionUID = 1L;

	};

	/**
	 * Constructor used when spring application context is declared in the
	 * spring standard way and can be located through
	 * {@link WebApplicationContextUtils#getRequiredWebApplicationContext(ServletContext)}
	 * 
	 * @param webapp
	 *            wicket web application
	 */
	public CustomizedSpringComponentInjector(WebApplication webapp) {
		// locate application context through spring's default location
		// mechanism and pass it on to the proper constructor
		this(webapp, WebApplicationContextUtils
				.getRequiredWebApplicationContext(webapp.getServletContext()));
	}

	/**
	 * Constructor
	 * 
	 * @param webapp
	 *            wicket web application
	 * @param ctx
	 *            spring's application context
	 */
	public CustomizedSpringComponentInjector(WebApplication webapp,
			ApplicationContext ctx) {
		if (webapp == null) {
			throw new IllegalArgumentException(
					"Argument [[webapp]] cannot be null");
		}

		if (ctx == null) {
			throw new IllegalArgumentException(
					"Argument [[ctx]] cannot be null");
		}

		// store context in application's metadata ...
		webapp.setMetaData(CONTEXT_KEY, new ApplicationContextHolder(ctx));

		// ... and create and register the annotation aware injector
		InjectorHolder
				.setInjector(new CustomizedAnnotSpringInjector(new ContextLocator(),ctx));
	}

	/**
	 * This is a holder for the application context. The reason we need a holder
	 * is that metadata only supports storing serializable objects but
	 * application context is not. The holder acts as a serializable wrapper for
	 * the context. Notice that although holder implements IClusterable it
	 * really is not because it has a reference to non serializable context -
	 * but this is ok because metadata objects in application are never
	 * serialized.
	 * 
	 * @author ivaynberg
	 * 
	 */
	private static class ApplicationContextHolder implements IClusterable {
		private static final long serialVersionUID = 1L;

		private final ApplicationContext context;

		/**
		 * Constructor
		 * 
		 * @param context
		 */
		public ApplicationContextHolder(ApplicationContext context) {
			this.context = context;
		}

		/**
		 * @return the context
		 */
		public ApplicationContext getContext() {
			return context;
		}
	}

	/**
	 * A context locator that locates the context in application's metadata.
	 * This locator also keeps a transient cache of the lookup.
	 * 
	 * @author ivaynberg
	 * 
	 */
	private static class ContextLocator implements ISpringContextLocator {
		private transient ApplicationContext context;

		private static final long serialVersionUID = 1L;

		public ApplicationContext getSpringContext() {
			if (context == null) {
				context = (Application.get().getMetaData(CONTEXT_KEY))
						.getContext();
			}
			return context;
		}

	}

}

// $Id$