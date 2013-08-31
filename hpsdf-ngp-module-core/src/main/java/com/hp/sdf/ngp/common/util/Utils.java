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
package com.hp.sdf.ngp.common.util;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.aop.framework.AdvisedSupport;
import org.springframework.beans.DirectFieldAccessor;
import org.springframework.beans.SimpleTypeConverter;

import com.hp.sdf.ngp.common.exception.WorkFlowFailureException;
import com.hp.sdf.ngp.search.engine.model.BaseCondition.WildCard;

public class Utils {
	private final static Log log = LogFactory.getLog(Utils.class);

	private static final String ADVISED_FIELD_NAME = "advised";

	private static final String CLASS_JDK_DYNAMIC_AOP_PROXY = "org.springframework.aop.framework.JdkDynamicAopProxy";

	public static Class getTargetClass(Object candidate) {
		if (!org.springframework.aop.support.AopUtils.isJdkDynamicProxy(candidate)) {
			return org.springframework.aop.support.AopUtils.getTargetClass(candidate);
		}

		return getTargetClassFromJdkDynamicAopProxy(candidate);
	}

	private static Class getTargetClassFromJdkDynamicAopProxy(Object candidate) {
		try {
			InvocationHandler invocationHandler = Proxy.getInvocationHandler(candidate);
			if (!invocationHandler.getClass().getName().equals(CLASS_JDK_DYNAMIC_AOP_PROXY)) {

				return candidate.getClass();
			}
			AdvisedSupport advised = (AdvisedSupport) new DirectFieldAccessor(invocationHandler).getPropertyValue(ADVISED_FIELD_NAME);
			Class targetClass = advised.getTargetClass();
			if (Proxy.isProxyClass(targetClass)) {

				Object target = advised.getTargetSource().getTarget();
				return getTargetClassFromJdkDynamicAopProxy(target);
			}
			return targetClass;
		} catch (Exception e) {

			return candidate.getClass();
		}
	}

	public static String dateToShortString(Date date) {
		if (date == null) {
			return "";
		}
		return new SimpleDateFormat("MM/dd/yyyy").format(date);
	}

	public static String dateToLongString(Date date) {
		if (date == null) {
			return "";
		}
		return new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(date);
	}

	public static int getNumVerificationCode(int n) {
		return new Random().nextInt(n);
	}

	public static String getCharVerificationCode(int n) {

		Random random = new Random();
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < n; i++) {
			boolean numOrChar = random.nextBoolean();
			// 48-57, 97-122
			char c;
			if (numOrChar) {
				c = (char) (48 + random.nextInt(9));
			} else {
				c = (char) (97 + random.nextInt(25));
			}
			sb.append(c);
		}
		return sb.toString();
	}

	/**
	 * Visit a method in a array object if it match a dedicated class type
	 * 
	 * @param objs
	 * @param clazz
	 * @param methodName
	 * @param args
	 * @throws WorkFlowFailureException
	 */
	public static void visitMethod(Object[] objs, Class<?> clazz, String methodName, Object... args) throws WorkFlowFailureException {
		try {
			if (objs != null) {
				for (Object obj : objs) {
					if (clazz.isAssignableFrom(obj.getClass())) {
						Method method = null;
						Method methods[] = clazz.getDeclaredMethods();
						if (methods != null) {
							for (Method temp : methods) {
								if (temp.getName().equals(methodName)) {
									method = temp;
									break;
								}
							}
						}
						if (method == null) {
							throw new WorkFlowFailureException("Can't find the corresponding method according to the method name[" + methodName + "]");
						}
						method.setAccessible(true);
						method.invoke(obj, args);
					}
				}
			}
		} catch (Throwable e) {
			throw new WorkFlowFailureException(e);
		}
	}

	/**
	 * Inject the property value into a bean object
	 * 
	 * @param object
	 * @param name
	 * @param value
	 * @throws WorkFlowFailureException
	 */
	public static void inJectProperty(Object object, String name, String value) throws WorkFlowFailureException {

		PropertyDescriptor[] pds = null;
		try {
			BeanInfo bi = Introspector.getBeanInfo(object.getClass());
			pds = bi.getPropertyDescriptors();
		} catch (Throwable e) {
			throw new WorkFlowFailureException("Cant inject the setter method for the object bean[" + object.getClass().getCanonicalName() + "]");
		}
		try {
			Method method = null;
			for (PropertyDescriptor propertyDescriptor : pds) {
				if (name.equals(propertyDescriptor.getName())) {
					method = propertyDescriptor.getWriteMethod();
					break;
				}
			}
			if (method == null) {
				throw new WorkFlowFailureException("No setter method for property[" + name + "]");
			}
			SimpleTypeConverter simpleTypeConverter = new SimpleTypeConverter();
			Object propertyValue = simpleTypeConverter.convertIfNecessary(value, method.getParameterTypes()[0]);
			method.setAccessible(true);
			method.invoke(object, propertyValue);

		} catch (Throwable e) {
			throw new WorkFlowFailureException("Cant inject the setter method for the property[" + name + "]", e);
		}
	}

	/**
	 * Converts date string from UI to date string used in SQL.
	 * 
	 * @param uiDateString
	 * @return date string to be used in SQL.
	 */
	public static String toSqlDate(String uiDate) {

		String sqlDate = null;

		try {
			SimpleDateFormat uiFormat = new SimpleDateFormat("MM/dd/yyyy");
			Date date = uiFormat.parse(uiDate);
			SimpleDateFormat sqlFormat = new SimpleDateFormat("yyyy-MM-dd");
			sqlDate = "to_date('" + sqlFormat.format(date) + "','yyyy-MM-dd')";
			;

		} catch (ParseException e) {
			e.printStackTrace();
		}

		return sqlDate;
	}

	public static Set<Class> getPackageClasses(String packageName) throws ClassNotFoundException, IOException {
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		boolean isRecursive = false;
		if (packageName.endsWith(".*"))
			isRecursive = true;
		packageName = packageName.replace(".*", "");
		String path = packageName.replace('.', '/');
		Enumeration<URL> resources = classLoader.getResources(path);
		List<File> dirs = new ArrayList<File>();
		while (resources.hasMoreElements()) {
			URL resource = resources.nextElement();
			dirs.add(new File(resource.getFile()));
		}
		Set<Class> classes = new LinkedHashSet<Class>();
		for (File directory : dirs) {
			if (directory.isDirectory()) {
				classes.addAll(findClasses(directory, packageName));
				if (isRecursive) {
					File[] files = directory.listFiles();
					for (File f : files) {
						if (f.isDirectory())
							classes.addAll(findClasses(directory, packageName));
					}
				}
			}
		}
		return classes;
	}

	public static Set<Class> findClasses(File directory, String packageName) throws ClassNotFoundException {
		Set<Class> classes = new LinkedHashSet<Class>();
		if (!directory.exists()) {
			return classes;
		}
		File[] files = directory.listFiles();
		for (File file : files) {
			if (file.isDirectory()) {
				assert !file.getName().contains(".");
				classes.addAll(findClasses(file, packageName + "." + file.getName()));
			} else if (file.getName().endsWith(".class")) {
				Class tempObj = Class.forName(packageName + '.' + file.getName().substring(0, file.getName().length() - 6));
				// if (file.getName().equals(className + ".class")) {
				// searchClassObj = tempObj;
				// }
				classes.add(tempObj);
			}
		}
		return classes;
	}

	public static Class getSuperClassGenricType(final Class clazz, final int index) {

		Type genType = clazz.getGenericSuperclass();

		if (!(genType instanceof ParameterizedType)) {
			log.warn(clazz.getSimpleName() + "'s superclass not ParameterizedType");
		}

		Type[] params = ((ParameterizedType) genType).getActualTypeArguments();

		if (index >= params.length || index < 0) {
			log.warn("Index: " + index + ", Size of " + clazz.getSimpleName() + "'s Parameterized Type: " + params.length);
			return Object.class;
		}
		if (!(params[index] instanceof Class)) {
			log.warn(clazz.getSimpleName() + " not set the actual class on superclass generic parameter");
			return Object.class;
		}

		return (Class) params[index];
	}

	@SuppressWarnings("unchecked")
	public static String getPKName(Class entityClass) {
		Field[] fields = entityClass.getDeclaredFields();

		for (Field f : fields) {
			Annotation[] annotations = f.getAnnotations();

			for (Annotation anno : annotations) {
				if (anno.toString().contains("@javax.persistence.Id()"))
					return f.getName();
			}
		}

		Method[] methods = entityClass.getDeclaredMethods();
		for (Method m : methods) {
			Annotation[] annotations = m.getAnnotations();
			for (Annotation anno : annotations) {
				if (anno.toString().contains("@javax.persistence.Id()") && m.getName().startsWith("get")) {
					return StringUtils.uncapitalize(StringUtils.substringAfter(m.getName(), "get"));
				}
			}
		}

		return null;
	}

	public static String substringAfterLast2(String str, String seperator) {
		if (str == null)
			return null;
		if ("".equals(str))
			return "";
		String result;
		if (!str.contains(".")) {
			result = str;
		} else {
			result = StringUtils.substringBeforeLast(str, ".");
			String last = StringUtils.substringAfterLast(result, ".");
			if (!"".equals(last))
				result = last;
			result += "." + StringUtils.substringAfterLast(str, ".");
		}
		return result;
	}

	public static String escapeWildCard(String orginal) {
		return orginal.replace(WildCard.more.toString(), WildCard.escapeChar.toString() + WildCard.more).replace(WildCard.one.toString(),
				WildCard.escapeChar.toString() + WildCard.one);
	}

	public static String escapeSqlWildCard(String orginal) {
		return orginal.replace(WildCard.more.toString(), "%").replace(WildCard.one.toString(), "_");
	}

	public static Date getCurrentDate() {
		return new Date();
	}

	public static void main(String[] args) {
		System.out.println(StringUtils.substringAfter("getString", "get"));
	}
}

// $Id$