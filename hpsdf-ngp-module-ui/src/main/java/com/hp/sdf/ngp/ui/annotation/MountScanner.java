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

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.Page;
import org.apache.wicket.request.target.coding.IRequestTargetUrlCodingStrategy;
import org.wicketstuff.annotation.mount.MountDefinition;
import org.wicketstuff.annotation.mount.MountPath;
import org.wicketstuff.annotation.scan.AnnotatedMountList;
import org.wicketstuff.annotation.scan.AnnotatedMountScanner;
import org.wicketstuff.config.MatchingResources;

/**
 * This class is used to scan the indicated package to search all of sub class
 * of {@Link Page} and annotated by {@link Mount} in order to mount it
 * using its class simple name as URL
 * 
 * 
 */
public class MountScanner extends AnnotatedMountScanner {

	private String prefix = "";

	// private static Logger logger =
	// LoggerFactory.getLogger(AnnotatedMountScanner.class);

	public AnnotatedMountScanner setPrefix(String prefix) {
		this.prefix = prefix;
		return this;
	}

	@Override
	@SuppressWarnings( { "unchecked" })
	protected AnnotatedMountList scanList(List<Class<?>> mounts) {
		AnnotatedMountList list = new AnnotatedMountList();
		for (Class<?> mount : mounts) {
			Class<? extends Page> page = (Class<? extends Page>) mount;
			list.addAll(scanClass(page));
		}
		return list;
	}

	@Override
	public List<Class<?>> getPatternMatches(String pattern) {
		MatchingResources resources = new MatchingResources(pattern);
		List<Class<?>> mountPaths = resources
				.getAnnotatedMatches(MountPath.class);
		for (Class<?> mount : mountPaths) {
			if (!(Page.class.isAssignableFrom(mount))) {
				throw new RuntimeException(
						"@MountPath annotated class should subclass Page: "
								+ mount);
			}
		}
		List<Class<?>> mounts = resources.getAnnotatedMatches(Mount.class);
		for (Class<?> mount : mounts) {
			if (!(Page.class.isAssignableFrom(mount))) {
				throw new RuntimeException(
						"@Mount annotated class should subclass Page: " + mount);
			}
		}
		List<Class<?>> result = new ArrayList<Class<?>>();
		result.addAll(mountPaths);
		result.addAll(mounts);
		return result;
	}

	@Override
	public AnnotatedMountList scanClass(Class<? extends Page> pageClass) {

		MountPath mountPath = pageClass.getAnnotation(MountPath.class);
		if (mountPath != null) {
			return super.scanClass(pageClass);
		}
		AnnotatedMountList list = new AnnotatedMountList();
		scanMountClass(pageClass, list);
		return list;
	}

	/**
	 * Magic of all this is done here.
	 * 
	 * @param pageClass
	 * @param list
	 */
	private void scanMountClass(Class<? extends Page> pageClass,
			AnnotatedMountList list) {
		Mount mount = pageClass.getAnnotation(Mount.class);
		if (mount == null)
			return;

		// find first annotation that is annotated with @MountDefinition
		Annotation pageSpecificMountDetails = null;
		Class<? extends Annotation> mountStrategyAnnotationClass = null;
		MountDefinition mountDefinition = null;
		Annotation[] annos = pageClass.getAnnotations();
		for (Annotation anno : annos) {
			mountDefinition = anno.annotationType().getAnnotation(
					MountDefinition.class);
			if (mountDefinition != null) {
				pageSpecificMountDetails = anno;
				mountStrategyAnnotationClass = anno.getClass();
				break;
			}
		}

		// default if no @MountDefinition annotated annotation is available
		if (pageSpecificMountDetails == null) {
			// primary

			list.add(getDefaultStrategy(prefix + pageClass.getSimpleName(),
					pageClass));
			return;
		}

		// get the actual strategy we'll be creating
		Class<? extends IRequestTargetUrlCodingStrategy> strategyClass = mountDefinition
				.strategyClass();

		// need to determine the constructor - we support constructors that
		// take a String (mount path) and a Class (page)
		int STANDARD_ARGS = 2;
		String[] argOrder = mountDefinition.argOrder();
		Class<?>[] paramTypes = new Class<?>[argOrder.length + STANDARD_ARGS];
		Object[] initArgs = new Object[paramTypes.length];

		// deafult first two arguments - mount path and page class
		paramTypes[0] = String.class;
		paramTypes[1] = Class.class;
		initArgs[0] = null; // provided below
		initArgs[1] = pageClass;

		// get remaining constructor args which match those specified by
		// 'argOrder'
		for (int i = 0; i < argOrder.length; i++) {
			int index = i + STANDARD_ARGS;
			Method method = null;

			try {
				method = mountStrategyAnnotationClass
						.getDeclaredMethod(argOrder[i]);
				paramTypes[index] = method.getReturnType();
				initArgs[index] = method.invoke(pageSpecificMountDetails);

				// can't default an annotation to null, so use this as a
				// workaround
				if (initArgs[index].equals(MountDefinition.NULL))
					initArgs[index] = null;
			} catch (NoSuchMethodException e) {
				throw new RuntimeException("argOrder[" + i + "] = "
						+ argOrder[i] + " not found in annotation "
						+ mountStrategyAnnotationClass.getName(), e);
			} catch (Exception e) {
				throw new RuntimeException("Unable to invoke method " + method
						+ " on annotation "
						+ pageSpecificMountDetails.getClass().getName(), e);
			}
		}

		// get matching constructor
		Constructor<? extends IRequestTargetUrlCodingStrategy> ctx = null;
		try {
			ctx = strategyClass.getConstructor(paramTypes);
		} catch (NoSuchMethodException e) {
			throw new RuntimeException(
					"No constructor matching parameters defined by 'argOrder' found for "
							+ strategyClass, e);
		}

		// create new instances
		try {
			// primary mount path

			initArgs[0] = prefix + pageClass.getSimpleName();
			list.add(ctx.newInstance(initArgs));

		} catch (Exception e) {
			throw new RuntimeException("Unable to invoke constructor " + ctx
					+ " for " + strategyClass, e);
		}
	}
}

// $Id$