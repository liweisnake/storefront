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
package com.hp.sdf.ngp.api.impl.search;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.beanutils.ConstructorUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

import com.hp.sdf.ngp.api.search.Condition;
import com.hp.sdf.ngp.api.search.ConditionDescriptor;
import com.hp.sdf.ngp.api.search.SearchEngine;
import com.hp.sdf.ngp.api.search.SearchExpression;
import com.hp.sdf.ngp.search.engine.SearchExpressionImpl;

@Component
public class SearchEngineImpl implements SearchEngine {

	private final static Log log = LogFactory.getLog(SearchEngineImpl.class);

	private final String descriptorSuffix = "Descriptor";

	private final String conditionPackage = "com.hp.sdf.ngp.search.condition";

	public Condition createCondition(ConditionDescriptor conditionDescriptor) {
		if (conditionDescriptor == null)
			return null;
		String descClassName = conditionDescriptor.getClass().getSimpleName();
		String lastPackageName = StringUtils.substringAfterLast(StringUtils.substringBeforeLast(conditionDescriptor.getClass().getName(), "."), ".");
		String conditionClassName = conditionPackage + "." + lastPackageName + "." + StringUtils.substringBefore(descClassName, descriptorSuffix);
		Class conditionClass;
		try {
			log.debug("Start construt class:" + conditionClassName);
			conditionClass = Class.forName(conditionClassName);
			Field[] fields = conditionDescriptor.getClass().getSuperclass().getDeclaredFields();
			List<Object> args = new ArrayList<Object>();
			List<Class> parameterTypes = new ArrayList<Class>();
			for (Field m : fields) {
				m.setAccessible(true);
				Object param = m.get(conditionDescriptor);
				if (param != null)
					args.add(param);
				parameterTypes.add(m.getType());
			}
			fields = conditionDescriptor.getClass().getDeclaredFields();
			for (Field m : fields) {
				m.setAccessible(true);
				Object param = m.get(conditionDescriptor);
				if (param != null)
					args.add(param);
				parameterTypes.add(m.getType());
			}

			Constructor[] cs = conditionClass.getConstructors();
			for (Constructor c : cs)
				log.debug("" + c);

			log.debug("Argument is :" + args);

			return (Condition) ConstructorUtils.invokeConstructor(conditionClass, args.toArray(new Object[args.size()]));

		} catch (NoSuchMethodException e) {
			e.printStackTrace();
			log.debug("not found this constructor.");
		} catch (ClassNotFoundException e) {
			log.debug("not found this class: " + conditionClassName + " exception is:" + e);
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			log.debug("Can not access field");
		} catch (InvocationTargetException e) {
			e.printStackTrace();
			log.debug("Can not invoke this constructor.");
		} catch (InstantiationException e) {
			e.printStackTrace();
			log.debug("Can not instance this constructor.");
		} catch (NoClassDefFoundError e) {
			e.printStackTrace();
			log.debug("Can not found the class.Maybe can not found the package : " + conditionClassName);
		}
		return null;
	}

	public SearchExpression createSearchExpression() {
		return new SearchExpressionImpl();
	}

}

// $Id$