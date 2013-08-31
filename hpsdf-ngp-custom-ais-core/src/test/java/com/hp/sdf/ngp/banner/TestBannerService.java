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
package com.hp.sdf.ngp.banner;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.hp.sdf.ngp.DBEnablerTestBase;
import com.hp.sdf.ngp.banner.impl.BannerServiceImpl;
import com.hp.sdf.ngp.banner.model.Banner;
import com.hp.sdf.ngp.banner.model.BaseBanner;
import com.hp.sdf.ngp.banner.model.Content;
import com.hp.sdf.ngp.banner.model.ContentSet;
import com.hp.sdf.ngp.banner.model.ContentWeight;
import com.hp.sdf.ngp.banner.model.RotatingBanner;
import com.hp.sdf.ngp.banner.model.StaticBanner;
import com.hp.sdf.ngp.banner.model.TabbedBanner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:META-INF/applicationContext-test.xml" })
public class TestBannerService extends DBEnablerTestBase {

	@Resource
	private BannerServiceImpl bannerService;

	@Override
	public String dataSetFileName() {
		return "/data_ais_init.xml";
	}

	private String getTestFiles() {
		return "/test.html";
	}
	
	@Test
	public void testAddBannerUsageLog() {
		bannerService.addBannerUsageLog(1l, 2l,"guoyong", "left");
		Assert.assertEquals(1, bannerService.countLog());
	}

	@Test
	public void testSubstituteURL1() {

		byte[] testHtml = null;
		try {
			URL url = getClass().getResource(getTestFiles());
			File file = new File(url.getFile());
			FileInputStream fin;
			fin = new FileInputStream(file);
			testHtml = new byte[(int) file.length()];
			fin.read(testHtml);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			Assert.fail("Can't fine the test html file");
		} catch (IOException ioe) {
			ioe.printStackTrace();
			Assert.fail("Can't read the test html file");
		}
		byte[] result = bannerService.substituteHtmlLinks(1l, 1l, "guoyong", "right",
				testHtml);
		String html = new String(result);
		int index = html
				.indexOf("http://localhost:8080/storefront/banner_stats?bannerId=1&contentId=1&location=right&userId=guoyong&url=http://www.hp.com");
		Assert.assertTrue(index != -1);
	}

	@Test
	public void testSubstituteURL2() {
		byte[] testHtml = null;
		try {
			URL url = getClass().getResource(getTestFiles());
			File file = new File(url.getFile());
			FileInputStream fin;
			fin = new FileInputStream(file);
			testHtml = new byte[(int) file.length()];
			fin.read(testHtml);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			Assert.fail("Can't fine the test html file");
		} catch (IOException ioe) {
			ioe.printStackTrace();
			Assert.fail("Can't read the test html file");
		}
		byte[] result = bannerService.substituteHtmlLinks(1l, 1l, testHtml);
		String html = new String(result);
		int index = html
				.indexOf("http://localhost:8080/storefront/banner_stats?bannerId=1&contentId=1&url=http://www.hp.com");
		Assert.assertTrue(index != -1);
	}

	@Test
	public void testAddStaticBanner() throws Exception {
		boolean result = true;
		Content content = new Content("Content_testAddStaticBanner",
				ContentType.image, new byte[] { 'a' }, "sample.jpg",
				"http://www.google.com");
		BaseBanner b = new BaseBanner(null, "BaseBanner_testAddStaticBanner",
				"", BannerType.staticBanner, BannerStatus.submit);
		StaticBanner banner = new StaticBanner(b, content);
		this.bannerService.addBanner(banner);
		Banner check = this.bannerService.getBanner(banner.getId());
		result = result && (check != null)
				&& check.getName().equals("BaseBanner_testAddStaticBanner");
		File file = new File(this.bannerService.getFilePrefix()
				+ ((StaticBanner) check).getContent().getPath());
		result = result && file.exists();
		Assert.assertTrue(result);
	}

	@Test
	public void testAddRotatingBanner() throws Exception {
		boolean result = true;
		Content content = new Content("Content_testAddRotatingBanner",
				ContentType.image, new byte[] { 'a' }, "sample.jpg",
				"http://www.google.com");
		BaseBanner b = new BaseBanner(null, "BaseBanner_testAddRotatingBanner",
				"", BannerType.staticBanner, BannerStatus.submit);
		RotatingBanner banner = new RotatingBanner(b, null);
		ContentSet set = new ContentSet("ContentSet_testAddRotatingBanner",
				"1111111", "9", "18", new Date());
		ContentWeight weight = new ContentWeight(set, content, new Float(0.2));
		Set<ContentWeight> weights = new HashSet<ContentWeight>();
		weights.add(weight);
		set.setBanner(banner);
		set.setContentWeights(weights);
		Set<ContentSet> sets = new HashSet<ContentSet>();
		sets.add(set);
		// RotatingBanner banner = new RotatingBanner(b, sets);
		banner.setContentSets(sets);
		this.bannerService.addBanner(banner);
		Banner check = this.bannerService.getBanner(banner.getId());
		result = result && (check != null)
				&& check.getName().equals("BaseBanner_testAddRotatingBanner");
		RotatingBanner checkRotating = (RotatingBanner) check;
		ContentSet checkSet = (ContentSet) checkRotating.getContentSets()
				.toArray()[0];
		ContentWeight checkWeight = (ContentWeight) checkSet
				.getContentWeights().toArray()[0];
		Content checkContent = (Content) checkWeight.getContent();
		result = result
				&& checkSet.getName()
						.equals("ContentSet_testAddRotatingBanner")
				&& checkWeight.getWeight().equals(new Float(0.2))
				&& checkContent.getName().equals(
						"Content_testAddRotatingBanner");
		Assert.assertTrue(result);
	}

	@Test
	public void testGetRotatingContent() throws Exception {
		boolean result = true;
		Content content1 = new Content("Content1_testAddRotatingBanner",
				ContentType.image, new byte[] { 'a' }, "sample.jpg",
				"http://www.google.com");
		Content content2 = new Content("Content2_testAddRotatingBanner",
				ContentType.image, new byte[] { 'a' }, "sample.jpg",
				"http://www.google.com");
		BaseBanner b = new BaseBanner(null, "BaseBanner_testAddRotatingBanner",
				"", BannerType.staticBanner, BannerStatus.submit);
		RotatingBanner banner = new RotatingBanner(b, null);
		Calendar c = Calendar.getInstance();
		c.setTime(new Date(System.currentTimeMillis()));
		c.set(Calendar.DATE, c.get(Calendar.DATE) + 1);
		ContentSet set = new ContentSet("ContentSet_testAddRotatingBanner",
				"1111111", "00:00", "24:00", c.getTime());
		ContentWeight weight1 = new ContentWeight(set, content1,
				new Float(0.2f));
		ContentWeight weight2 = new ContentWeight(set, content2,
				new Float(0.8f));
		Set<ContentWeight> weights = new HashSet<ContentWeight>();
		weights.add(weight1);
		weights.add(weight2);
		set.setBanner(banner);
		set.setContentWeights(weights);
		Set<ContentSet> sets = new HashSet<ContentSet>();
		sets.add(set);
		// RotatingBanner banner = new RotatingBanner(b, sets);
		banner.setContentSets(sets);
		this.bannerService.addBanner(banner);

		int id1 = 0;
		int id2 = 0;
		for (int i = 0; i < 100; i++) {
			String name = this.bannerService.getRotatingContent(banner)
					.getName();
			if (name.equals("Content1_testAddRotatingBanner"))
				id1++;
			if (name.equals("Content2_testAddRotatingBanner"))
				id2++;
		}
		System.out.println("12:" + id1 + "|" + id2);

		int check = id2 - id1;
		result = result && (check > 0);
		Assert.assertTrue(result);
	}

	@Test
	public void testListBanner() throws Exception {
		boolean result = true;
		List<BaseBanner> list = this.bannerService.listBanner(0,
				Integer.MAX_VALUE);
		result = result && (list != null) && (list.size() > 0);
		Assert.assertTrue(result);
	}

	@Test
	public void testGetBanner() throws Exception {
		boolean result = true;
		StaticBanner staticBanner = (StaticBanner) this.bannerService
				.getBanner(new Long(1));
		result = result && staticBanner != null
				&& staticBanner.getName().equals("sample static banner")
				&& staticBanner.getContent().getName().equals("sample content");
		RotatingBanner rotatingBanner = (RotatingBanner) this.bannerService
				.getBanner(new Long(2));
		result = result
				&& rotatingBanner != null
				&& rotatingBanner.getName().equals("sample rotating banner")
				&& rotatingBanner.getContentSets().size() == 1
				&& ((ContentSet) rotatingBanner.getContentSets().toArray()[0])
						.getDow().equals("1111111")
				&& ((ContentSet) rotatingBanner.getContentSets().toArray()[0])
						.getContentWeights().size() == 2
				&& ((ContentWeight) ((ContentSet) rotatingBanner
						.getContentSets().toArray()[0]).getContentWeights()
						.toArray()[0]).getContent() != null
				&& ((ContentWeight) ((ContentSet) rotatingBanner
						.getContentSets().toArray()[0]).getContentWeights()
						.toArray()[0]).getContent().getReferenceId().equals(
						((ContentWeight) ((ContentSet) rotatingBanner
								.getContentSets().toArray()[0])
								.getContentWeights().toArray()[0]).getId());
		TabbedBanner tabbedBanner = (TabbedBanner) this.bannerService
				.getBanner(new Long(3));
		result = result
				&& tabbedBanner != null
				&& tabbedBanner.getContents().size() == 2
				&& tabbedBanner.getContents().get(0).getReferenceId().equals(
						tabbedBanner.getId());
		Assert.assertTrue(result);
	}

	@Test
	public void testGetBanner2() throws Exception {
		boolean result = true;
		StaticBanner staticBanner = (StaticBanner) this.bannerService
				.getBanner("sample static banner");
		result = result && staticBanner != null
				&& staticBanner.getName().equals("sample static banner")
				&& staticBanner.getContent().getName().equals("sample content");
		RotatingBanner rotatingBanner = (RotatingBanner) this.bannerService
				.getBanner(new Long(2));
		result = result
				&& rotatingBanner != null
				&& rotatingBanner.getName().equals("sample rotating banner")
				&& rotatingBanner.getContentSets().size() == 1
				&& ((ContentSet) rotatingBanner.getContentSets().toArray()[0])
						.getDow().equals("1111111")
				&& ((ContentSet) rotatingBanner.getContentSets().toArray()[0])
						.getContentWeights().size() == 2
				&& ((ContentWeight) ((ContentSet) rotatingBanner
						.getContentSets().toArray()[0]).getContentWeights()
						.toArray()[0]).getContent() != null
				&& ((ContentWeight) ((ContentSet) rotatingBanner
						.getContentSets().toArray()[0]).getContentWeights()
						.toArray()[0]).getContent().getReferenceId().equals(
						((ContentWeight) ((ContentSet) rotatingBanner
								.getContentSets().toArray()[0])
								.getContentWeights().toArray()[0]).getId());
		TabbedBanner tabbedBanner = (TabbedBanner) this.bannerService
				.getBanner(new Long(3));
		result = result
				&& tabbedBanner != null
				&& tabbedBanner.getContents().size() == 2
				&& tabbedBanner.getContents().get(0).getReferenceId().equals(
						tabbedBanner.getId());
		Assert.assertTrue(result);
	}

	@Test
	public void testDeleteBanner() throws Exception {
		boolean result = true;
		Content content = new Content("Content_testDeleteBanner_1",
				ContentType.image, new byte[] { 'a' }, "sample.jpg",
				"http://www.google.com");
		BaseBanner b = new BaseBanner(null, "BaseBanner_testDeleteBanner_1",
				"", BannerType.staticBanner, BannerStatus.submit);
		StaticBanner staticBanner = new StaticBanner(b, content);
		this.bannerService.addBanner(staticBanner);
		this.bannerService.deleteBanner(staticBanner.getId());
		Banner check = this.bannerService.getBanner(staticBanner.getId());
		result = result && (check == null);
		Assert.assertTrue(result);
	}

}

// $Id$