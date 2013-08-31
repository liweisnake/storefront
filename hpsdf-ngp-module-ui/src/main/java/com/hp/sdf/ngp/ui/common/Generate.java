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
package com.hp.sdf.ngp.ui.common;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.apache.commons.lang.StringUtils;

import com.hp.sdf.ngp.ui.common.Constant.CONTROL_TYPE;
import com.hp.sdf.ngp.ui.common.Constant.DATA_TYPE;
import com.hp.sdf.ngp.ui.common.Constant.ENABLE;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Generate {
	public String id() default StringUtils.EMPTY;

	public CONTROL_TYPE controlType() default CONTROL_TYPE.TextField;

	public DATA_TYPE dataType() default DATA_TYPE.none;

	public String key() default StringUtils.EMPTY;

	public String value() default StringUtils.EMPTY;

	public String setMarkupId() default StringUtils.EMPTY;

	public ENABLE setEnable() default ENABLE.enable;
}