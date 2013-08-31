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

import java.lang.reflect.Field;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang.StringUtils;
import org.apache.wicket.proxy.LazyInitProxyFactory;
import org.apache.wicket.spring.ISpringContextLocator;
import org.apache.wicket.spring.SpringBeanLocator;
import org.apache.wicket.spring.injection.annot.AnnotProxyFieldValueFactory;
import org.apache.wicket.spring.injection.annot.SpringBean;

public class CustomizedAnnotProxyFieldValueFactory extends
		AnnotProxyFieldValueFactory {
	
	private ISpringContextLocator contextLocator;

	private final ConcurrentHashMap<SpringBeanLocator, Object> cache = new ConcurrentHashMap<SpringBeanLocator, Object>();

	@Override
	public Object getFieldValue(Field field, Object fieldOwner) {
		// Check the annotation first

		try {
			return super.getFieldValue(field, fieldOwner);

		} catch (RuntimeException e) {
			if (field.isAnnotationPresent(SpringBean.class)) {
				SpringBean annot = field.getAnnotation(SpringBean.class);
				if (StringUtils.isEmpty(annot.name())) {
					
					String newName = StringUtils.uncapitalize(field.getType()
							.getSimpleName());

					SpringBeanLocator locator = new SpringBeanLocator(newName,
							field.getType(), contextLocator);

					// only check the cache if the bean is a singleton
					if (locator.isSingletonBean() && cache.containsKey(locator)) {
						return cache.get(locator);
					}

					Object proxy = LazyInitProxyFactory.createProxy(field
							.getType(), locator);
					// only put the proxy into the cache if the bean is a
					// singleton
					if (locator.isSingletonBean()) {
						cache.put(locator, proxy);
					}
					return proxy;
				}

			
			}
			throw e;
		}
	}

	public CustomizedAnnotProxyFieldValueFactory(
			ISpringContextLocator contextLocator) {
		super(contextLocator);
		this.contextLocator = contextLocator;


	}

}

// $Id$