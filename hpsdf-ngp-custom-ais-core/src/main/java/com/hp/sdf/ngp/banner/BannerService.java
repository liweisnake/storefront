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

import java.util.List;

import com.hp.sdf.ngp.banner.model.Banner;
import com.hp.sdf.ngp.banner.model.BaseBanner;
import com.hp.sdf.ngp.banner.model.Content;
import com.hp.sdf.ngp.banner.model.RotatingBanner;

public interface BannerService {
	/**
	 * Provision Banner
	 * 
	 * @param banner
	 */
	public void addBanner(Banner banner);

	/**
	 * Update Banner
	 * 
	 * @param banner
	 */
	public void updateBanner(Banner banner);

	/**
	 * Delete Banner
	 * 
	 * @param id
	 */
	public void deleteBanner(Long id);

	/**
	 * List Banner
	 * 
	 * @param start
	 * @param count
	 * @return
	 */
	public List<BaseBanner> listBanner(int start, int count);
	
	/**
	 * List Banner by Status
	 * 
	 * @param start
	 * @param count
	 * @return
	 */
	public List<BaseBanner> listBanner(BannerStatus status, int start, int count);

	/**
	 * Count Banner
	 * 
	 * @return
	 */
	public int countBanner();
	
	/**
	 * Count Banner by Status
	 * 
	 * @return
	 */
	public int countBanner(BannerStatus status);

	/**
	 * Get Banner details
	 * 
	 * @param id
	 * @return
	 */
	public Banner getBanner(Long id);

	/**
	 * Get Banner details by name
	 * 
	 * @param name
	 * @return
	 */
	public Banner getBanner(String name);

	/**
	 * Get current Content of Rotating Banner to display
	 * 
	 * @param banner
	 * @return
	 */
	public Content getRotatingContent(RotatingBanner banner);

	/**
	 * Replace the links in HTML contents with redirection
	 * 
	 * @param html
	 * @return
	 */
	public byte[] substituteHtmlLinks(Long bannerId, Long contentId,
			String userId, String location, byte[] input);

	/**
	 * @param bannerId
	 * @param contentId
	 */
	public void addBannerUsageLog(Long bannerId, Long contentId, String userId,
			String location);

	/**
	 * @param bannerId
	 * @param contentId
	 * @param input
	 * @return
	 */
	public byte[] substituteHtmlLinks(Long bannerId, Long contentId,
			byte[] input);
	
	/**
	 * @param bannerId
	 * @param contentId
	 * @param url
	 * @return
	 */
	public String substituteImageLinks(Long bannerId, Long contentId, String url);
	
	/**
	 * @param qp
	 * @param filter
	 * @return
	 */
	public List<BaseBanner> find(final QueryParam qp, BaseBanner filter);
}

// $Id$