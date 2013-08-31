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
package com.hp.sdf.ngp.service.administration;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.annotation.Resource;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.hp.sdf.ngp.administration.AssetEntry;
import com.hp.sdf.ngp.administration.AssetEntryDecoder;
import com.hp.sdf.ngp.common.exception.UnzipException;

/**
 * 
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:META-INF/applicationContext-test.xml" })
public class AssetEntryDecoderTestCase {

	@Resource
	AssetEntryDecoder decoder;

	/**
	 * Test method for
	 * {@link com.hp.sdf.ngp.service.batchupload.descriptor.BatchUploadRepositoryImpl#load(java.io.File)}
	 * .
	 * 
	 * @throws IOException
	 * @throws UnzipException
	 */
	@Test
	public void testDecode() throws IOException, UnzipException {
		InputStream input = this.getClass().getClassLoader()
				.getResourceAsStream("batchUploadtest.zip");
		List<AssetEntry> list = decoder.decode(input);
		if (list.size() == 2) {
			Assert.assertTrue(true);
		}
	}
}

// $Id$