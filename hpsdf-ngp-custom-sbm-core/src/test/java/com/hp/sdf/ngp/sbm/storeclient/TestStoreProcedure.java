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

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

import java.math.BigInteger;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.hp.sdf.ngp.DBEnablerTestBase;
import com.hp.sdf.ngp.custom.sbm.api.storeclient.service.ContentCatalogService;
import com.hp.sdf.ngp.custom.sbm.storeclient.dao.ContentItemDao;

@Transactional
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:META-INF/applicationContext-test.xml" })
public class TestStoreProcedure extends DBEnablerTestBase {

	@Resource
	private ContentItemDao contentItemDao;
	
	@Resource
	private ContentCatalogService contentCatalogService;

	@Override
	public String dataSetFileName() {
		return "/data_init3.xml";
	}


	@Test
	@Ignore
	public void testNewArrivalFlag() {
		String sql = " select count(*) from assetbinaryversion bv where bv.ownerassetparentid = 1 and bv.newarrivalduedate < now() ";
		List<Object[]> values = contentItemDao.findSql(sql, 0, Integer.MAX_VALUE);

		assertTrue(values.size() == 1);
//		assertEquals(values.get(0),new BigInteger("1"));
		assertEquals(values.get(0),new Integer("1"));
	}

	@Test
	@Ignore
	public void testRecommendFlag() {
		String sql = "select count(*) from assetbinaryversion bv where bv.ownerassetparentid = 3 "
				+ "and bv.recommendstartdate <  now() and now() < bv.recommendduedate";

		List<Object[]> values = contentItemDao.findSql(sql, 0, Integer.MAX_VALUE);

		assertTrue(values.size() == 1);
//		assertEquals(values.get(0), new BigInteger("1"));
		assertEquals(values.get(0), new Integer("1"));
	}

		
	// error in hsqldb
	@Ignore
	@Test
	public void testRecommendorderAndPublishdate(){
		String sql = "select max(bv.publishdate),min(bv.recommendorder) from assetbinaryversion bv " +
				"where bv.ownerassetparentid is not null";

		List<Object[]> values = contentItemDao.findSql(sql, 0, Integer.MAX_VALUE);
	
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String str = dateFormat.format(values.get(0)[0]);
		
		assertTrue(values.size() == 1);
		assertEquals(str,"2009-12-13 10:11:00");
//		assertEquals(values.get(0)[1],new BigInteger("1"));
		assertEquals(values.get(0)[1],new Integer("1"));
	}
	
	// error in hsqldb
	@Ignore
	@Test
	public void testDownloadTime(){
		String sql = "select distinct count(*) from userdownloadhistory where userdownloadhistory.downloaddate + INTERVAL 7 DAY > Now() " +
				"and userdownloadhistory.assetid in (select asset.id from asset where userdownloadhistory.assetid = 1 " +
				"or userdownloadhistory.assetid = asset.id and asset.parentid = 1)";

		List<Object[]> values = contentItemDao.findSql(sql, 0, Integer.MAX_VALUE);
		
		assertTrue(values.size() == 1);
		assertEquals(values.get(0),new BigInteger("1"));
	}
	
	
	
	
	
		
		
	@Test
	@Ignore
	public void testInsertOrUpdate(){
		String sql = "select count(*) from parentassetversionsummary summary where summary.id = 1";

		List<Object[]> values = contentItemDao.findSql(sql, 0, Integer.MAX_VALUE);
		
		assertTrue(values.size() == 1);
//		assertEquals(values.get(0),new BigInteger("1"));
		assertEquals(values.get(0),new Integer("1"));
	}
	
	@Test
	public void testGenerateBinaryVersionIndex() throws Exception{
		List<String> excludeStatus = new ArrayList<String>();
		excludeStatus.add("provided");
		excludeStatus.add("invalid");
		Map<String, Date> maps = contentCatalogService.generateBinaryVersionIndex(excludeStatus, "test_source", "sampleAttributeDate");
		
		assertTrue(maps.size() == 1);
	}
}

// $Id$