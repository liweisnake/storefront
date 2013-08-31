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
package com.hp.sdf.ngp.common.delegate;

import java.lang.reflect.ParameterizedType;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import javax.annotation.PostConstruct;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.hp.sdf.ngp.common.annotation.PropertyAnnotationAndPlaceholderConfigurer;
import com.hp.sdf.ngp.common.util.Utils;
import com.hp.sdf.ngp.enabler.sms.impl.SmsSenderWebServiceImpl;

/**
 * This class will delegate the method call using a configured interface/class
 * <E> implementation to this interface/class <E>. It will check the
 * configuration item named as <code>Class<E>.getCanonicalName()</code> and get
 * it value as the full class name of the particular subclass from the property
 * files to decide which particular subclass would be delegated.
 * <p>
 * For example, if
 * <code>com.hp.sdf.ngp.enabler.sms.SmsSender=com.hp.sdf.ngp.enabler.sms.impl.SmsSenderWebServiceImpl</code>
 * configured, then {@link SmsSenderWebServiceImpl} will be used.
 * 
 * *
 * <p>
 * By default, if no any correct configuration item,
 * {@link SmsSenderWebServiceImpl} will be used.
 * 
 * @param <E>
 */
public abstract class ComponentDelegate<E> implements ApplicationContextAware {

	@Autowired
	private PropertyAnnotationAndPlaceholderConfigurer propertyAnnotationAndPlaceholderConfigurer;

	protected String componentFullName;

	private Class<E> componentClass;

	protected E component;

	private ApplicationContext applicationContext;

	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		this.applicationContext = applicationContext;

	}

	public String getComponentFullName() {
		return this.componentFullName;
	}

	@SuppressWarnings("unchecked")
	public ComponentDelegate() {
		ParameterizedType genericSuperclass = (ParameterizedType) getClass()
				.getGenericSuperclass();
		this.componentClass = (Class<E>) genericSuperclass
				.getActualTypeArguments()[0];
	}

	public final E getComponent() {
		return component;
	}

	private void parseComponentFullName() {
		Properties properties = propertyAnnotationAndPlaceholderConfigurer
				.getProperties();

		this.componentFullName = properties.getProperty(componentClass
				.getCanonicalName());
	}

	protected abstract Class<E> getDefaultComponent();

	@SuppressWarnings( { "unused", "unchecked" })
	@PostConstruct
	private void init() {

		E defaultComponent = null;

		parseComponentFullName();

		Map<?, ?> components = applicationContext
				.getBeansOfType(componentClass);
		if (components != null) {
			Iterator<?> iter = components.entrySet().iterator();
			while (iter.hasNext()) {
				Map.Entry<?, ?> entry = (Map.Entry<?, ?>) iter.next();
				E object = (E) entry.getValue();
				String className = object.getClass().getCanonicalName();
				Class clazz = null;

				if (className.contains("$Proxy")||className.contains("$$EnhancerByCGLIB")) {
					// it is a proxy class wrapped by SPRING
					clazz = Utils.getTargetClass(object);
				} else {
					clazz = object.getClass();
				}

				if (object != null
						&& getComponentFullName() != null
						&& clazz.getCanonicalName().equalsIgnoreCase(
								getComponentFullName())) {
					component = object;
					return;
				} else {
					if (object != null
							&& getDefaultComponent() != null
							&& clazz.getCanonicalName().equalsIgnoreCase(
									getDefaultComponent().getCanonicalName())) {
						defaultComponent = object;
					}
				}
			}
		}
		if (defaultComponent != null) {
			component = defaultComponent;
			return;
		}
		if (component == null && getDefaultComponent() != null) {
			components = applicationContext
					.getBeansOfType(getDefaultComponent());
			if (components != null) {
				Iterator<?> iter = components.entrySet().iterator();
				while (iter.hasNext()) {
					Map.Entry<?, ?> entry = (Map.Entry<?, ?>) iter.next();
					E object = (E) entry.getValue();
					if (object != null) {
						component = object;
						return;
					}
				}
			}
		}
	}
}

// $Id$