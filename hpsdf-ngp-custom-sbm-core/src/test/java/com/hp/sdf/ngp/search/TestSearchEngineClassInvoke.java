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
package com.hp.sdf.ngp.search;

import java.lang.reflect.Constructor;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javassist.Modifier;

import org.apache.commons.beanutils.ConstructorUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.hp.sdf.ngp.api.comon.EntityType;
import com.hp.sdf.ngp.api.impl.search.SearchEngineImpl;
import com.hp.sdf.ngp.api.search.Condition;
import com.hp.sdf.ngp.api.search.ConditionDescriptor;
import com.hp.sdf.ngp.api.search.SearchEngine;
import com.hp.sdf.ngp.api.search.Condition.DateComparer;
import com.hp.sdf.ngp.api.search.Condition.NumberComparer;
import com.hp.sdf.ngp.api.search.Condition.StringComparer;
import com.hp.sdf.ngp.common.util.Utils;
import com.hp.sdf.ngp.search.engine.model.SqlCondition;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:META-INF/applicationContext-test.xml" })
public class TestSearchEngineClassInvoke {

	private final String[] packageNames = { "com.hp.sdf.ngp.api.search.condition.*", "com.hp.sdf.ngp.custom.sbm.api.search.condition.*" };

	private int passedCount;

	private int failedCount;

	@Test
	public void testSearchEngineConditionClassInvoke() throws Exception {
		List<String> log = new ArrayList<String>();
		SearchEngine engine = new SearchEngineImpl();
		for (String packageName : packageNames) {
			Set<Class> set = Utils.getPackageClasses(packageName);
			for (Class c : set) {
				if (Modifier.isAbstract(c.getModifiers())) {
					log.add("@@@@Found abstract class, discard :" + c.getName());
					continue;
				}
				if ("ContentItemAssetVersionConditionDescriptor".equals(c.getSimpleName())) {
					//This condition is not used but still under API interface
					continue;
				} 

				Condition con = null;
				try {
					ConditionDescriptor conDes = null;
					if ("StringConditionDescriptor".equals(c.getSuperclass().getSimpleName())) {
						conDes = (ConditionDescriptor) ConstructorUtils.invokeConstructor(c,
								new Object[] { "test", StringComparer.EQUAL, true, true }, new Class[] { String.class, StringComparer.class,
										Boolean.class, Boolean.class });
					} else if ("NumberConditionDescriptor".equals(c.getSuperclass().getSimpleName())) {
						String clazzName = Utils.getSuperClassGenricType(c, 0).getSimpleName();
						Object obj = null;
						if (clazzName.indexOf("Long") != -1) {
							obj = 100L;
						} else if (clazzName.indexOf("Float") != -1) {
							obj = 1.1F;
						} else if (clazzName.indexOf("Double") != -1) {
							obj = 1.11;
						} else if (clazzName.indexOf("BigDecimal") != -1) {
							obj = new BigDecimal(10);
						}
						Constructor[] cs = c.getConstructors();
						for (Constructor cc : cs) {
							System.out.println(cc);
						}
						System.out.println("argument: " + obj);
						conDes = (ConditionDescriptor) ConstructorUtils.invokeConstructor(c, new Object[] { obj, NumberComparer.EQUAL });

					} else if ("DateConditionDescriptor".equals(c.getSuperclass().getSimpleName())) {
						conDes = (ConditionDescriptor) ConstructorUtils.invokeConstructor(c, new Object[] { new Date(), DateComparer.GREAT_THAN },
								new Class[] { Date.class, DateComparer.class });
					} else if ("EavStringConditionDescriptor".equals(c.getSimpleName())) {

						conDes = (ConditionDescriptor) ConstructorUtils.invokeConstructor(c, new Object[] { EntityType.ASSET, "test", "String",
								StringComparer.EQUAL, true, true }, new Class[] { EntityType.class, String.class, String.class, StringComparer.class,
								Boolean.class, Boolean.class });
					} else if ("EavNumberConditionDescriptor".equals(c.getSimpleName())) {
						conDes = null;
						// String clazzName = Utils.getSuperClassGenricType(c,
						// 0)
						// .getSimpleName();
						// Object obj = null;
						// if (clazzName.indexOf("Long") != -1) {
						// obj = 100L;
						// } else if (clazzName.indexOf("Float") != -1) {
						// obj = 1.1;
						// } else if (clazzName.indexOf("Double") != -1) {
						// obj = 1.11;
						// } else if (clazzName.indexOf("BigDecimal") != -1) {
						// obj = new BigDecimal(10);
						// }
						// conDes = (ConditionDescriptor) ConstructorUtils
						// .invokeConstructor(c, new Object[] {
						// EntityType.ASSETPROVIDER, "Number",
						// obj, NumberComparer.EQUAL });
					} else if ("EavDateConditionDescriptor".equals(c.getSimpleName())) {
						conDes = (ConditionDescriptor) ConstructorUtils
								.invokeConstructor(c, new Object[] { EntityType.BINARYVERSION, "Date", new Date(), DateComparer.GREAT_THAN },
										new Class[] { EntityType.class, String.class, Date.class, DateComparer.class });
					} else {
						conDes = (ConditionDescriptor) ConstructorUtils.invokeConstructor(c, null);
					}

					if (conDes != null)
						con = engine.createCondition(conDes);
					if (conDes == null && con == null) {
						con = new SqlCondition();
					}
					if (con != null) {
						passedCount++;
						log.add("####Pass:" + c.getName() + " invoke and construct " + con.getClass().getName());
					} else {
						failedCount++;
						log.add("!!!!Fail:" + c.getName() + " can not invoke and construct " + (con == null ? "null" : con.getClass().getName()));
					}
				} catch (Exception e) {
					failedCount++;
					e.printStackTrace();
					log.add("!!!!Fail:" + c.getName() + " exception invoke and construct " + (con == null ? "null" : con.getClass().getName()));
				}
			}
		}
		for (String str : log)
			System.out.println(str);
		System.out.println("Total count: " + (passedCount + failedCount) + "(" + passedCount + " passed. " + failedCount + " failed.)");

		Assert.assertFalse(failedCount > 0);
	}

	// @SuppressWarnings("unchecked")
	// @Test
	// public void testSearchEngineOrderClassInvoke() throws Exception {
	// List<String> log = new ArrayList<String>();
	// String[] orderEnumPackageNames = { "com.hp.sdf.ngp.api.search.orderby",
	// };
	// final String orderPackageName = "com.hp.sdf.ngp.search.order";
	// for (String orderEnumPackageName : orderEnumPackageNames) {
	// Set<Class> set = Utils.getPackageClasses(orderEnumPackageName);
	// for (Class c : set) {
	// String[] strArray = c.getName().split("\\$");
	// if (strArray.length < 2)
	// continue;
	// String enumClassName = strArray[0];
	// int ordinal = Integer.parseInt(strArray[1]);
	// String lastPackageName = StringUtils.substringBefore(
	// StringUtils.substringAfterLast(enumClassName, "."),
	// "OrderBy").toLowerCase();
	// // Object obj = Class.forName(enumClassName).newInstance();
	// Class enumClass = Class.forName(enumClassName);
	// // List list = EnumUtil.getAllEnumField(enumClass);
	// // for (Object obj : list) {
	// // if (obj instanceof Enum) {
	// String className = orderPackageName
	// + "."
	// + lastPackageName
	// + "."
	// + StringUtils
	// .capitalize(EnumUtil.getEnumNameStrByOrdinal(
	// enumClass, ordinal - 1).toLowerCase());
	// try {
	// Class.forName(className).newInstance();
	// log.add("####Pass: find " + enumClassName + "["
	// + enumClassName + "] and invoke " + className
	// + " success.");
	//
	// passedCount++;
	// } catch (Exception e) {
	// failedCount++;
	// log.add("!!!!Fail: find " + c.getName() + "["
	// + enumClassName + "] and invoke " + className
	// + " failure.");
	// }
	// }
	// }
	//
	// for (String str : log)
	// System.out.println(str);
	// System.out
	// .println(passedCount + " passed. " + failedCount + " failed.");
	//
	// Assert.assertFalse(failedCount > 0);
	// }

}

// $Id$