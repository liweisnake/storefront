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
package com.hp.sdf.ngp.sbm.storeclient;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.hp.sdf.ngp.api.comon.util.JndiUtil;
import com.hp.sdf.ngp.custom.sbm.api.storeclient.service.ClientContentService;
import com.hp.sdf.ngp.custom.sbm.api.storeclient.service.ClientSoftwareService;
import com.hp.sdf.ngp.custom.sbm.api.storeclient.service.ContentCatalogService;
import com.hp.sdf.ngp.custom.sbm.api.storeclient.service.HandsetDeviceService;
import com.hp.sdf.ngp.custom.sbm.api.storeclient.service.MimeTypeService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:META-INF/applicationContext-test.xml" })
public class TestJndiDelegate {
	
	@Test
	public void testLookupClientContentService() {
		ClientContentService object = (ClientContentService) JndiUtil.lookupObject(ClientContentService.class.getCanonicalName());
		Assert.assertTrue(object != null);
	}
	
	@Test
	public void testLookupClientSoftwareService() {
		ClientSoftwareService object = (ClientSoftwareService) JndiUtil.lookupObject(ClientSoftwareService.class.getCanonicalName());
		Assert.assertTrue(object != null);
	}
	
	@Test
	public void testLookupContentCatalogService() {
		ContentCatalogService object = (ContentCatalogService) JndiUtil.lookupObject(ContentCatalogService.class.getCanonicalName());
		Assert.assertTrue(object != null);
	}
	
	@Test
	public void testLookupHandsetDeviceService() {
		HandsetDeviceService object = (HandsetDeviceService) JndiUtil.lookupObject(HandsetDeviceService.class.getCanonicalName());
		Assert.assertTrue(object != null);
	}

	@Test
	public void testLookupMimeTypeService() {
		MimeTypeService object = (MimeTypeService) JndiUtil.lookupObject(MimeTypeService.class.getCanonicalName());
		Assert.assertTrue(object != null);
	}



}

// $Id$