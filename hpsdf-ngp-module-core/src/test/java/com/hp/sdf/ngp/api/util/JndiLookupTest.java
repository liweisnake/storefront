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
package com.hp.sdf.ngp.api.util;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.PlatformTransactionManager;

import com.hp.sdf.ngp.api.assetcatalog.AssetCatalogService;
import com.hp.sdf.ngp.api.comon.util.JndiUtil;
import com.hp.sdf.ngp.api.search.SearchEngine;
import com.hp.sdf.ngp.api.storeclient.StoreClientService;
import com.hp.sdf.ngp.api.subscribercatalog.SubscriberCatalogService;
import com.hp.sdf.ngp.api.system.SystemConfigService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:META-INF/applicationContext-test.xml" })
public class JndiLookupTest {

	@Test
	public void testLookupTransactionManager() {
		Object object = JndiUtil.lookupObject(PlatformTransactionManager.class.getCanonicalName());
		Assert.assertTrue(object!=null);
	}
	@Test
	public void testLookupAssetCatelogService() {
		Object object = JndiUtil.lookupObject(AssetCatalogService.class.getCanonicalName());
		Assert.assertTrue(object!=null);
	}
	@Test
	public void testLookupSearchEngine() {
		Object object = JndiUtil.lookupObject(SearchEngine.class.getCanonicalName());
		Assert.assertTrue(object!=null);
	}
	@Test
	public void testLookupStoreClientService() {
		Object object = JndiUtil.lookupObject(StoreClientService.class.getCanonicalName());
		Assert.assertTrue(object!=null);
	}
	@Test
	public void testLookupSubscriberCatalogService() {
		Object object = JndiUtil.lookupObject(SubscriberCatalogService.class.getCanonicalName());
		Assert.assertTrue(object!=null);
	}
	@Test
	public void testLookupSystemConfigService() {
		Object object = JndiUtil.lookupObject(SystemConfigService.class.getCanonicalName());
		Assert.assertTrue(object!=null);
	}
}

// $Id$