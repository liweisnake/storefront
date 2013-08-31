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
package com.hp.sdf.ngp.common.annotation;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

public class PropertyAnnotationAndPlaceholderConfigurer extends
		PropertyPlaceholderConfigurer {

	private final static Log log = LogFactory
			.getLog(PropertyAnnotationAndPlaceholderConfigurer.class);

	private Properties properties;

	public Properties getProperties() {
		return properties;
	}

	@Override
	protected void processProperties(
			ConfigurableListableBeanFactory beanFactory, Properties properties)
			throws BeansException {
		
		this.properties = properties;
		
		super.processProperties(beanFactory, properties);

		for (String name : beanFactory.getBeanDefinitionNames()) {
			MutablePropertyValues mpv = beanFactory.getBeanDefinition(name)
					.getPropertyValues();
			Class<?> clazz = beanFactory.getType(name);

			if (log.isDebugEnabled()) {
				log.debug("Configuring properties for bean=" + name + "["
						+ clazz + "]");
			}

			if (clazz != null) {
				for (PropertyDescriptor property : BeanUtils
						.getPropertyDescriptors(clazz)) {
					Method setter = property.getWriteMethod();
					Method getter = property.getReadMethod();
					Value annotation = null;
					if (setter != null
							&& setter.isAnnotationPresent(Value.class)) {
						annotation = setter.getAnnotation(Value.class);
					} else if (setter != null && getter != null
							&& getter.isAnnotationPresent(Value.class)) {
						annotation = getter.getAnnotation(Value.class);
					}
					if (annotation != null) {
						String value = resolvePlaceholder(annotation.value(),
								properties, SYSTEM_PROPERTIES_MODE_FALLBACK);

						if (value != null) {
							if (log.isDebugEnabled()) {
								log.debug("setting property=["
										+ clazz.getName() + "."
										+ property.getName() + "] value=["
										+ annotation.value() + "=" + value
										+ "]");
							}
							mpv.addPropertyValue(property.getName(), value);
						} else {
							if (log.isDebugEnabled()) {
								log.debug("don't setting property=["
										+ clazz.getName() + "."
										+ property.getName()
										+ "] due to value absent");
							}
						}

					}
				}

				for (Field field : clazz.getDeclaredFields()) {
					if (log.isDebugEnabled()) {
						log.debug("examining field=[" + clazz.getName() + "."
								+ field.getName() + "]");
					}
					if (field.isAnnotationPresent(Value.class)) {
						Value annotation = field.getAnnotation(Value.class);
						PropertyDescriptor property = BeanUtils
								.getPropertyDescriptor(clazz, field.getName());

						if (property.getWriteMethod() == null) {
							throw new BeanConfigurationException(
									"setter for property=[" + clazz.getName()
											+ "." + field.getName()
											+ "] not available.");
						}

						Object value = resolvePlaceholder(annotation.value(),
								properties, SYSTEM_PROPERTIES_MODE_FALLBACK);

						if (value != null) {
							if (log.isDebugEnabled()) {
								log.debug("setting property=["
										+ clazz.getName() + "."
										+ field.getName() + "] value=["
										+ annotation.value() + "=" + value
										+ "]");
							}
							mpv.addPropertyValue(property.getName(), value);
						} else {
							if (log.isDebugEnabled()) {
								log.debug("don't setting property=["
										+ clazz.getName() + "."
										+ field.getName()
										+ "] due to value absent");
							}
						}

					}
				}
			}
		}
	}

}

// $Id$