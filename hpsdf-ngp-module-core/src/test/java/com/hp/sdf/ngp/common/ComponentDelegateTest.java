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
package com.hp.sdf.ngp.common;

import javax.annotation.Resource;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.hp.sdf.ngp.common.delegate.ComponentDelegate;

interface DelegateObject {
	public String getName();
}

@Component
class DelegateObject1 implements DelegateObject {
	public String getName() {
		return this.getClass().getSimpleName();
	}
}

@Component
class DelegateObject2 implements DelegateObject {
	public String getName() {
		return this.getClass().getSimpleName();
	}
}

@Component
class DelegateObject3 implements DelegateObject {
	public String getName() {
		return this.getClass().getSimpleName();
	}
}

@Component(value = "delegateObject")
class Delegate extends ComponentDelegate<DelegateObject> implements
		DelegateObject {
	@SuppressWarnings("unchecked")
	@Override
	protected Class<DelegateObject> getDefaultComponent() {
		// TODO Auto-generated method stub
		return (Class) DelegateObject3.class;
	}

	public String getName() {
		return this.component.getName();
	}
}

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:META-INF/applicationContext-test.xml" })
public class ComponentDelegateTest {

	@Resource
	private DelegateObject delegateObject;

	@Test
	public void testDelegate() {
		System.out.print("---------------"
				+ DelegateObject2.class.getCanonicalName());
		Assert.assertTrue(delegateObject instanceof Delegate);
		Assert.assertTrue(delegateObject.getName().equals(
				DelegateObject2.class.getSimpleName()));
	}

}

// $Id$