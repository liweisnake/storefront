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
package com.hp.sdf.ngp.banner.impl;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.annotation.Resource;

import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hp.sdf.ngp.banner.BannerService;
import com.hp.sdf.ngp.banner.BannerStatus;
import com.hp.sdf.ngp.banner.BannerType;
import com.hp.sdf.ngp.banner.ContentType;
import com.hp.sdf.ngp.banner.QueryParam;
import com.hp.sdf.ngp.banner.dao.BannerUsageLogDAO;
import com.hp.sdf.ngp.banner.dao.BaseBannerDAO;
import com.hp.sdf.ngp.banner.dao.ContentDAO;
import com.hp.sdf.ngp.banner.dao.ContentSetDAO;
import com.hp.sdf.ngp.banner.dao.ContentWeightDAO;
import com.hp.sdf.ngp.banner.dao.RotatingBannerDAO;
import com.hp.sdf.ngp.banner.dao.StaticBannerDAO;
import com.hp.sdf.ngp.banner.dao.TabbedBannerDAO;
import com.hp.sdf.ngp.banner.model.Banner;
import com.hp.sdf.ngp.banner.model.BannerUsageLog;
import com.hp.sdf.ngp.banner.model.BaseBanner;
import com.hp.sdf.ngp.banner.model.Content;
import com.hp.sdf.ngp.banner.model.ContentSet;
import com.hp.sdf.ngp.banner.model.ContentWeight;
import com.hp.sdf.ngp.banner.model.RotatingBanner;
import com.hp.sdf.ngp.banner.model.StaticBanner;
import com.hp.sdf.ngp.banner.model.TabbedBanner;
import com.hp.sdf.ngp.common.annotation.Value;
import com.hp.sdf.ngp.common.exception.SaveFileFailureException;

/**
 * 
 *
 */
/**
 * 
 *
 */
@Service
public class BannerServiceImpl implements BannerService {

	private static final Log log = LogFactory.getLog(BannerServiceImpl.class);

	@Resource
	private ContentDAO contentDao;

	@Resource
	private BaseBannerDAO bannerDao;

	@Resource
	private StaticBannerDAO staticBannerDao;

	@Resource
	private RotatingBannerDAO rotatingBannerDao;

	@Resource
	private TabbedBannerDAO tabbedBannerDao;

	@Resource
	private ContentWeightDAO contentWeightDao;

	@Resource
	private ContentSetDAO contentSetDao;

	@Resource
	private BannerUsageLogDAO bannerUsageLogDAO;

	private String filePrefix;

	private String servletPrefix;

	private boolean setUrlPrefix;

	@Value("file.path.prefix")
	public String getFilePrefix() {
		return filePrefix;
	}

	public void setFilePrefix(String filePrefix) {
		this.filePrefix = filePrefix;
	}

	@Value("web.servlet.prefix")
	public String getServletPrefix() {
		return this.servletPrefix;
	}

	public void setServletPrefix(String servletPrefix) {
		this.servletPrefix = servletPrefix;
	}

	@Value("web.servlet.prefix.enable")
	public boolean getSetUrlPrefix() {
		return setUrlPrefix;
	}

	public void setSetUrlPrefix(boolean setUrlPrefix) {
		this.setUrlPrefix = setUrlPrefix;
	}

	@Transactional
	public void addBanner(Banner banner) {
		BaseBanner b = new BaseBanner(banner.getId(), banner.getName(), banner
				.getDescription(), banner.getBannerType(), banner.getBannerStatus());
		this.bannerDao.persist(b);
		banner.setId(b.getId());
		if (banner instanceof StaticBanner) {
			this.addStaticBanner((StaticBanner) banner);
		} else if (banner instanceof RotatingBanner) {
			this.addRotatingBanner((RotatingBanner) banner);
		} else if (banner instanceof TabbedBanner) {
			this.addTabbedBanner((TabbedBanner) banner);
		}

	}

	/**
	 * Add StaticBanner
	 * 
	 * @param banner
	 */
	private void addStaticBanner(StaticBanner banner) {
		this.staticBannerDao.persist(banner);
		banner.getContent().setBannerType(BannerType.staticBanner);
		banner.getContent().setReferenceId(banner.getId());
		this.addContent(banner.getContent(), banner.getId());
	}

	/**
	 * Add Rotating Banner
	 * 
	 * @param banner
	 */
	private void addRotatingBanner(RotatingBanner banner) {
		this.rotatingBannerDao.persist(banner);
		if (banner.getContentSets() != null
				&& banner.getContentSets().size() > 0) {
			Iterator<ContentSet> itCS = banner.getContentSets().iterator();
			while (itCS.hasNext()) {
				ContentSet contentSet = itCS.next();
				if (contentSet.getContentWeights() != null
						&& contentSet.getContentWeights().size() > 0) {
					Iterator<ContentWeight> itCW = contentSet
							.getContentWeights().iterator();
					while (itCW.hasNext()) {
						ContentWeight contentWeight = itCW.next();
						Content content = contentWeight.getContent();
						content.setBannerType(BannerType.rotatingBanner);
						content.setReferenceId(contentWeight.getId());
						this.addContent(content, banner.getId());
					}
				}
			}
		}
	}

	/**
	 * Add Tabbed Banner
	 * 
	 * @param banner
	 */
	private void addTabbedBanner(TabbedBanner banner) {
		this.tabbedBannerDao.persist(banner);
		if (banner.getContents() != null && banner.getContents().size() > 0) {
			Iterator<Content> it = banner.getContents().iterator();
			while (it.hasNext()) {
				Content content = it.next();
				content.setBannerType(BannerType.tabbedBanner);
				content.setReferenceId(banner.getId());
				this.addContent(content, banner.getId());
			}
		}
	}

	@Transactional
	public void deleteBanner(Long id) {
		Banner banner = this.getBanner(id);
		List<Content> contents = this.getAllContentByBanner(banner);
		this.bannerDao.remove(this.bannerDao.findById(id));
		switch (banner.getBannerType()) {
		case staticBanner:
			this.staticBannerDao.remove((StaticBanner) banner);
			break;
		case rotatingBanner:
			this.rotatingBannerDao.remove((RotatingBanner) banner);
			break;
		case tabbedBanner:
			this.tabbedBannerDao.remove((TabbedBanner) banner);
			break;
		}

		Iterator<Content> iterator = contents.iterator();
		while (iterator.hasNext()) {
			Content content = iterator.next();
			this.deleteContent(content);
		}
	}

	@Transactional
	public Banner getBanner(Long id) {
		Banner banner = this.bannerDao.findById(id);
		if (banner == null)
			return null;
		switch (banner.getBannerType()) {
		case staticBanner:
			StaticBanner staticBanner = this.staticBannerDao.findById(id);
			if (staticBanner != null) {
				staticBanner.setBanner(banner);
				List<Content> contents = this.contentDao.findByBanner(id,
						BannerType.staticBanner);
				if (contents != null && contents.size() == 1)
					staticBanner.setContent(contents.get(0));
			}
			return staticBanner;
		case rotatingBanner:
			RotatingBanner rotatingBanner = this.rotatingBannerDao.findById(id);
			if (rotatingBanner != null) {
				rotatingBanner.setBanner(banner);
				Set<ContentSet> listCS = rotatingBanner.getContentSets();
				if (listCS != null && listCS.size() > 0) {
					Iterator<ContentSet> itCS = listCS.iterator();
					while (itCS.hasNext()) {
						ContentSet contentSet = itCS.next();
						if (contentSet != null) {
							Set<ContentWeight> listCW = contentSet
									.getContentWeights();
							if (listCW != null && listCW.size() > 0) {
								Iterator<ContentWeight> itCW = listCW
										.iterator();
								while (itCW.hasNext()) {
									ContentWeight contentWeight = itCW.next();
									List<Content> contents = this.contentDao
											.findByBanner(
													contentWeight.getId(),
													BannerType.rotatingBanner);
									if (contents != null
											&& contents.size() == 1)
										contentWeight.setContent(contents
												.get(0));
								}
							}
						}
					}
				}
			}
			return rotatingBanner;
		case tabbedBanner:
			TabbedBanner tabbedBanner = this.tabbedBannerDao.findById(id);
			if (tabbedBanner != null) {
				tabbedBanner.setBanner(banner);
				List<Content> contents = this.contentDao.findByBanner(id,
						BannerType.tabbedBanner);
				tabbedBanner.setContents(contents);
			}
			return tabbedBanner;
		}
		return banner;
	}

	@Transactional
	public Banner getBanner(String name) {
		List<BaseBanner> list = this.bannerDao.findBy("name", name);
		if (list != null && list.size() > 0) {
			return this.getBanner(list.get(0).getId());
		}
		return null;
	}

	@Transactional
	public List<BaseBanner> listBanner(int start, int count) {
		List<BaseBanner> list = this.bannerDao.getAll(start, count);
		return list;
	}
	
	@Transactional
	public List<BaseBanner> find(final QueryParam qp, BaseBanner filter) {
		List<BaseBanner> list = this.bannerDao.find(qp, filter);
		return list;
	}
	
	@Transactional
	public List<BaseBanner> listBanner(BannerStatus status, int start, int count) {
		List<BaseBanner> list = this.bannerDao.findBy("bannerStatus", status, start, count);
		return list;
	}

	@Transactional
	public List<BannerUsageLog> listLog(int start, int count) {
		List<BannerUsageLog> list = bannerUsageLogDAO.getAll(start, count);
		return list;
	}

	@Transactional
	public int countLog() {
		return bannerUsageLogDAO.getAllCount();
	}

	@Transactional
	public int countBanner() {
		return bannerDao.getAllCount();
	}
	
	@Transactional
	public int countBanner(BannerStatus status) {
		return bannerDao.findByCount("bannerStatus", status);
	}

	@Transactional
	public void updateBanner(Banner banner) {
		Banner oldBanner = this.getBanner(banner.getId());
		List<Content> oldContents = this.getAllContentByBanner(oldBanner);
		List<Content> newContents = this.getAllContentByBanner(banner);
		List<Content> cutContents = new ArrayList<Content>();
		this.bannerDao.evict(oldBanner);
		// Update Banner, Content Set, Content Weigth
		BaseBanner b = this.bannerDao.findById(banner.getId());
		b.setName(banner.getName());
		b.setDescription(banner.getDescription());
		b.setBannerType(banner.getBannerType());
		b.setBannerStatus(banner.getBannerStatus());
		this.bannerDao.merge(b);
		switch (banner.getBannerType()) {
		case staticBanner:
			this.staticBannerDao.merge((StaticBanner) banner);
			break;
		case rotatingBanner:
			// Delete Content Weight
			List<Long> newWeightIds = new ArrayList<Long>();
			List<Long> oldWeightIds = new ArrayList<Long>();
			for (ContentSet s : ((RotatingBanner) banner).getContentSets()) {
				for (ContentWeight w : s.getContentWeights()) {
					newWeightIds.add(w.getId());
				}
			}
			for (ContentSet s : ((RotatingBanner) oldBanner).getContentSets()) {
				for (ContentWeight w : s.getContentWeights()) {
					oldWeightIds.add(w.getId());
				}
			}
			for (Long id : oldWeightIds) {
				if (!newWeightIds.contains(id))
					this.contentWeightDao.remove(id);
			}
			// Delete Content Set
			List<Long> newSetIds = new ArrayList<Long>();
			List<Long> oldSetIds = new ArrayList<Long>();
			for (ContentSet s : ((RotatingBanner) banner).getContentSets()) {
				newSetIds.add(s.getId());
			}
			for (ContentSet s : ((RotatingBanner) oldBanner).getContentSets()) {
				oldSetIds.add(s.getId());
			}
			for (Long id : oldSetIds) {
				if (!newSetIds.contains(id))
					this.contentSetDao.remove(id);
			}
			this.rotatingBannerDao.merge((RotatingBanner) banner);
			break;
		case tabbedBanner:
			this.tabbedBannerDao.merge((TabbedBanner) banner);
			break;
		}
		// Update Content
		Iterator<Content> itOldContent = oldContents.iterator();
		while (itOldContent.hasNext()) {
			Content oldContent = itOldContent.next();
			if (newContents.contains(oldContent)) {
				Content newContent = newContents.get(newContents
						.indexOf(oldContent));
				if ((newContent.getBinary() == null || newContent.getBinary().length == 0)
						&& newContent.getName().equals(oldContent.getName())
						&& newContent.getContentType().equals(
								oldContent.getContentType())) {
					cutContents.add(newContent);
				} else {
					this.deleteContent(oldContent);
				}
			} else {
				this.deleteContent(oldContent);
			}
		}

		newContents = this.getAllContentByBanner(banner);
		newContents.removeAll(cutContents);
		for (Content content : newContents) {
			this.addContent(content, banner.getId());
		}
	}

	@Transactional
	public Content getRotatingContent(RotatingBanner banner) {
		Content result = null;
		List<ContentWeight> contents = new ArrayList<ContentWeight>();
		List<ContentSet> contentSets = new ArrayList<ContentSet>();
		Set<ContentSet> listCS = banner.getContentSets();
		if (listCS != null && listCS.size() > 0) {
			Iterator<ContentSet> itCS = listCS.iterator();
			while (itCS.hasNext()) {
				ContentSet contentSet = itCS.next();
				if (contentSet != null) {
					if (isInRotate(contentSet)) {
						contentSets.add(contentSet);
						Set<ContentWeight> listCW = contentSet
								.getContentWeights();
						if (listCW != null && listCW.size() > 0) {
							Iterator<ContentWeight> itCW = listCW.iterator();
							while (itCW.hasNext()) {
								ContentWeight contentWeight = itCW.next();
								contents.add(contentWeight);
							}
						}
					}
				}
			}
		}
		if (contentSets!=null && contentSets.size()>1){
			return null;
		}
//		ContentSet choicedContentSet = getChoicedContentSet(contentSets);
//		if (choicedContentSet != null) {
//			Set<ContentWeight> listCW = choicedContentSet.getContentWeights();
//			if (listCW != null && listCW.size() > 0) {
//				Iterator<ContentWeight> itCW = listCW.iterator();
//				while (itCW.hasNext()) {
//					ContentWeight contentWeight = itCW.next();
//					contents.add(contentWeight);
//				}
//			}
//		}else {
//			return null;
//		}
		Comparator<ContentWeight> com = new Comparator<ContentWeight>() {
			public int compare(ContentWeight o1, ContentWeight o2) {
				return (int) (o1.getWeight() - o2.getWeight());
			}
		};
		Collections.sort(contents, com);
		result = getContentRandom(contents);
		return result;
	}

	private ContentSet getChoicedContentSet(List<ContentSet> contentSets) {
		if (contentSets != null && contentSets.size() > 0) {
			Iterator<ContentSet> itCS = contentSets.iterator();
			while (itCS.hasNext()) {
				
			}
		}
		return null;
	}

	/*
	 * public static void main(String[] args) { List<ContentWeight> contents =
	 * new ArrayList<ContentWeight>(); ContentWeight cw1 = new ContentWeight();
	 * cw1.setId(new Long("1")); cw1.setWeight(0.5f); Content c1 = new
	 * Content(); c1.setId(new Long("1")); cw1.setContent(c1);
	 * 
	 * ContentWeight cw2 = new ContentWeight(); Content c2 = new Content();
	 * c2.setId(new Long("2")); cw2.setId(new Long("2")); cw2.setWeight(0.2f);
	 * cw2.setContent(c2);
	 * 
	 * ContentWeight cw3 = new ContentWeight(); Content c3 = new Content();
	 * c3.setId(new Long("3")); cw3.setId(new Long("3")); cw3.setWeight(0.7f);
	 * cw3.setContent(c3);
	 * 
	 * contents.add(cw1); contents.add(cw2); contents.add(cw3);
	 * Comparator<ContentWeight> com = new Comparator<ContentWeight>() { public
	 * int compare(ContentWeight o1, ContentWeight o2) { return (int)
	 * (o1.getWeight() - o2.getWeight()); } }; Collections.sort(contents, com);
	 * Content result = getContentRandom(contents); int id1 = 0; int id2 = 0;
	 * int id3 = 0; for (int i = 0; i < 10000; i++) { Long id =
	 * getContentRandom(contents).getId(); if (id == 1) id1++; if (id == 2)
	 * id2++; if (id == 3) id3++; } System.out.println("123:" + id1 + "|" + id2
	 * + "|" + id3);
	 * 
	 * ContentSet ccc = new ContentSet(); ccc.setHodStart("14:00");
	 * ccc.setHodEnd("14:30"); ccc.setExpiration(new Date(2010,7,26,15,07,0));
	 * ccc.setDow("1000001"); System.out.println(isInRotate(ccc)); }
	 */

	private static Content getContentRandom(List<ContentWeight> listCW) {
		int sumWeight = 0;
		for (ContentWeight content : listCW) {
			sumWeight += content.getWeight() * 100;
		}
		Random rand = new Random();
		int x = rand.nextInt(sumWeight) + 1;
		Float start = 0f;
		Float end = 0f;
		for (int i = 1; i < listCW.size(); i++) {
			if (i == 1) {
				start = listCW.get(i - 1).getWeight() * 100;
				end = listCW.get(i - 1).getWeight() * 100
						+ listCW.get(i).getWeight() * 100;
			} else {
				start = end;
				end = start + listCW.get(i).getWeight() * 100;
			}
			if (x > start && x <= end) {
				return listCW.get(i).getContent();
			} else if (x <= start) {
				return listCW.get(i - 1).getContent();
			} else {
				continue;
			}
		}
		if (listCW != null && listCW.size() > 0) {
			return listCW.get(0).getContent();
		}
		return listCW.get(0).getContent();
	}

	private static boolean isInRotate(ContentSet contentSet) {
		boolean checkWeek = false;
		boolean checkHour = false;
		boolean checkDate = false;
		// check expriration
		if (new Date().before(contentSet.getExpiration())) {
			checkDate = true;
		} else {
			return false;
		}
		// check rotating in week
		Calendar c = Calendar.getInstance();
		c.setTime(new Date(System.currentTimeMillis()));
		int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
		String dow = contentSet.getDow();
		int strDow = -1;
		if (dow.charAt(0) == '1') {
			strDow = 2;// Mon
			if (dayOfWeek == strDow)
				checkWeek = true;
		}
		if (dow.charAt(1) == '1') {
			strDow = 3;// Tue
			if (dayOfWeek == strDow)
				checkWeek = true;
		}
		if (dow.charAt(2) == '1') {
			strDow = 4;// Wed
			if (dayOfWeek == strDow)
				checkWeek = true;
		}
		if (dow.charAt(3) == '1') {
			strDow = 5;// The
			if (dayOfWeek == strDow)
				checkWeek = true;
		}
		if (dow.charAt(4) == '1') {
			strDow = 6;// Fri
			if (dayOfWeek == strDow)
				checkWeek = true;
		}
		if (dow.charAt(5) == '1') {
			strDow = 7;// Sat
			if (dayOfWeek == strDow)
				checkWeek = true;
		}
		if (dow.charAt(6) == '1') {
			strDow = 1;// Sun
			if (dayOfWeek == strDow)
				checkWeek = true;
		}
		if (!checkWeek) {
			return false;
		}
		// check rotationg in hours

		String[] start = contentSet.getHodStart().split(":");
		int startTime = Integer.parseInt(start[0]) * 60
				+ Integer.parseInt(start[1]);
		String[] end = contentSet.getHodEnd().split(":");
		int endTime = Integer.parseInt(end[0]) * 60 + Integer.parseInt(end[1]);
		int nowTime = c.get(Calendar.HOUR_OF_DAY) * 60 + c.get(Calendar.MINUTE);
		if (nowTime >= startTime && nowTime <= endTime) {
			checkHour = true;
		} else {
			return false;
		}

		if (checkWeek && checkHour && checkDate) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Fetch all Contents of Banner. Just from model, not from DB.
	 * 
	 * @param banner
	 * @return
	 */
	private List<Content> getAllContentByBanner(Banner banner) {
		List<Content> contents = new ArrayList<Content>();
		if (banner instanceof StaticBanner) {
			Content content = ((StaticBanner) banner).getContent();
			content.setBannerType(BannerType.staticBanner);
			content.setReferenceId(banner.getId());
			contents.add(content);
		} else if (banner instanceof RotatingBanner) {
			RotatingBanner rBanner = (RotatingBanner) banner;
			if (rBanner.getContentSets() != null
					&& rBanner.getContentSets().size() > 0) {
				Iterator<ContentSet> itCS = rBanner.getContentSets().iterator();
				while (itCS.hasNext()) {
					ContentSet contentSet = itCS.next();
					if (contentSet.getContentWeights() != null
							&& contentSet.getContentWeights().size() > 0) {
						Iterator<ContentWeight> itCW = contentSet
								.getContentWeights().iterator();
						while (itCW.hasNext()) {
							ContentWeight contentWeight = itCW.next();
							Content content = contentWeight.getContent();
							content.setBannerType(BannerType.rotatingBanner);
							content.setReferenceId(contentWeight.getId());
							contents.add(content);
						}
					}
				}
			}
		} else if (banner instanceof TabbedBanner) {
			TabbedBanner tBanner = (TabbedBanner) banner;
			if (tBanner.getContents() != null) {
				for (Content content : tBanner.getContents()) {
					content.setBannerType(BannerType.tabbedBanner);
					content.setReferenceId(banner.getId());
					contents.add(content);
				}
			}
		}
		return contents;
	}

	/**
	 * Provision Content to DB and file system
	 * 
	 * @param content
	 * @throws SaveFileFailureException
	 */
	private void addContent(Content content, Long bannerId)
			throws SaveFileFailureException {
		// Parse file name of Content
		String fileName = content.getFileName();
		String s = "\\";
		try {
			fileName = URLEncoder.encode(content.getFileName(), "utf-8");
			s = URLEncoder.encode(s, "utf-8");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		if (fileName.lastIndexOf(s) > -1)
			fileName = fileName.substring(fileName.lastIndexOf(s) + 3);
		if (content.getFileName().lastIndexOf("/") > -1)
			fileName = fileName.substring(fileName.lastIndexOf("/") + 1);
		try {
			content.setFileName(URLDecoder.decode(fileName, "utf-8"));
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		log.debug("Content File Name: " + content.getFileName());
		String path = this.getContentPath();
		if (content.getContentType().equals(ContentType.html)) {
			content.setPath(path
					+ content.getFileName().substring(0,
							content.getFileName().lastIndexOf("."))
					+ "/index.html");
		} else if (content.getContentType().equals(ContentType.image))
			content.setPath(path + content.getFileName());
		this.contentDao.persist(content);
		if (content.getContentType().equals(ContentType.image)) {
			String href = this.substituteImageLinks(bannerId, content.getId(), content.getHref());
			content.setHref(href);
			this.contentDao.merge(content);
		}

		File directory = new File(this.filePrefix + path);
		if (!directory.exists())
			directory.mkdirs();
		File file = new File(this.filePrefix + path + content.getFileName());
		try {
			FileUtils.writeByteArrayToFile(file, content.getBinary());
			// if the content type is html, unzip the zip
			if (content.getContentType().equals(ContentType.html)) {
				String zipPath = content.getFileName().substring(0,
						content.getFileName().lastIndexOf("."))
						+ "/";
				ZipInputStream zin = new ZipInputStream(new FileInputStream(
						file));
				ZipEntry entry;
				while ((entry = zin.getNextEntry()) != null) {
					if (entry.isDirectory()) {
						File zipDirectory = new File(directory, zipPath
								+ entry.getName());
						if (!zipDirectory.exists())
							directory.mkdirs();
						zin.closeEntry();
					} else {
						File d = new File(directory + "/" + zipPath);
						if (!d.exists())
							d.mkdirs();
						File f = new File(directory + "/" + zipPath
								+ entry.getName());
						f.createNewFile();

						ByteArrayOutputStream baout = new ByteArrayOutputStream();
						byte[] b = new byte[1024];
						int len = 0;
						while ((len = zin.read(b)) != -1)
							baout.write(b, 0, len);
						byte[] data;
						if (this.getSetUrlPrefix())
							data = this.substituteHtmlLinks(bannerId, content
									.getId(), baout.toString("utf-8").getBytes(
									"utf-8"));
						else
							data = baout.toString("utf-8").getBytes("utf-8");

						FileOutputStream fout = new FileOutputStream(f);
						DataOutputStream dout = new DataOutputStream(fout);
						// byte[] b = new byte[1024];
						// int len = 0;
						// while ((len = zin.read(b)) != -1)
						// dout.write(b, 0, len);
						dout.write(data);
						dout.close();
						fout.close();
						zin.closeEntry();
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new SaveFileFailureException("save file error:"
					+ e.toString());
		}
	}

	/**
	 * Delete Content
	 * 
	 * @param content
	 */
	private void deleteContent(Content content) {
		String path = this.filePrefix + content.getPath();
		File file = new File(path);
		if (file.exists()) {
			switch (content.getContentType()) {
			case image:
				this.deleteDirectory(file.getParentFile());
				break;
			case html:
				this.deleteDirectory(file.getParentFile().getParentFile());
				break;
			}
		}
		this.contentDao.remove(content);
	}

	private boolean deleteDirectory(File path) {
		if (path.exists()) {
			File[] files = path.listFiles();
			for (int i = 0; i < files.length; i++) {
				if (files[i].isDirectory()) {
					deleteDirectory(files[i]);
				} else {
					files[i].delete();
				}
			}
		}
		return path.delete();
	}

	/**
	 * Generate Content path
	 * 
	 * @param fileName
	 * @return
	 */
	private String getContentPath() {
		Date date = Calendar.getInstance().getTime();
		String path = "/banner/"
				+ new SimpleDateFormat("yyyyMMdd").format((date)) + "/"
				+ new SimpleDateFormat("HHmm").format((date)) + "/"
				+ UUID.randomUUID().toString() + "/";
		return path;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.hp.sdf.ngp.banner.BannerService#processHtmlLinks(byte[])
	 */
	public byte[] substituteHtmlLinks(Long bannerId, Long contentId,
			String userId, String location, byte[] input) {
		String html = new String(input);
		String regex = "<a\\s+href\\s*=\\s*([^>]*)";
		Pattern p = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(html);
		while (m.find()) {
			int length = m.group(1).trim().length();
			String url = m.group(1).trim().substring(1, length - 1);
			if(url.indexOf("http://") == -1) {
				url = "http://" + url;
			}
			String newUrl = servletPrefix.trim() + "/" + bannerId + "/"
					+ contentId + "/" + userId + "/" + location + "/" + url;
			newUrl = servletPrefix.trim() + "?" + "bannerId=" + bannerId
					+ "&contentId=" + contentId + "&location=" + location
					+ "&userId=" + userId + "&url=" + url;
			html = html.replaceAll(url, newUrl);
		}

		return html.getBytes();
	}

	public String substituteImageLinks(Long bannerId, Long contentId, String url) {
		if(url == null || url.length() == 0 )
			return null;
		if(url.indexOf("http://") == -1) {
			url = "http://" + url;
		}
		return servletPrefix.trim() + "?" + "bannerId=" + bannerId
				+ "&contentId=" + contentId + "&url=" + url;
	}

	public byte[] substituteHtmlLinks(Long bannerId, Long contentId,
			byte[] input) {
		String html = new String(input);
		String regex = "<a\\s+href\\s*=\\s*([^>]*)";
		Pattern p = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(html);
		while (m.find()) {
			int length = m.group(1).trim().length();
			String url = m.group(1).trim().substring(1, length - 1);
			if(url.indexOf("http://") == -1) {
				url = "http://" + url;
			}
			String newUrl = servletPrefix.trim() + "?" + "bannerId=" + bannerId
					+ "&contentId=" + contentId + "&url=" + url;
			html = html.replaceAll(url, newUrl);
		}

		return html.getBytes();
	}

	@Transactional
	public void addBannerUsageLog(Long bannerId, Long contentId, String userId,
			String location) {
		Calendar dateTime = Calendar.getInstance();
		BannerUsageLog usageLog = new BannerUsageLog();
		usageLog.setBannerId(bannerId);
		usageLog.setContentId(contentId);
		usageLog.setUserId(userId);
		usageLog.setLocation(location);
		usageLog.setDateTime(dateTime);
		bannerUsageLogDAO.persist(usageLog);
	}
}

// $Id$