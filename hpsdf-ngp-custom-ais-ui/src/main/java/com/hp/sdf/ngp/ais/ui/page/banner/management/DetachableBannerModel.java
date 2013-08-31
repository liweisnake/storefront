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
package com.hp.sdf.ngp.ais.ui.page.banner.management;

import org.apache.wicket.RequestCycle;
import org.apache.wicket.model.LoadableDetachableModel;

import com.hp.sdf.ngp.ais.ui.WicketApplication;
import com.hp.sdf.ngp.banner.BannerService;
import com.hp.sdf.ngp.banner.model.Banner;

public class DetachableBannerModel extends LoadableDetachableModel<BannerProxy> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3640166784191477693L;

	private final long id;

	/**
	 * @param banner
	 */
	public DetachableBannerModel(BannerProxy banner) {
		this(banner.getId());
	}

	/**
	 * @param id
	 */
	public DetachableBannerModel(long id) {
		if (id == 0) {
			throw new IllegalArgumentException();
		}
		this.id = id;
	}

	@Override
	protected BannerProxy load() {
		BannerService bannerService = ((WicketApplication) RequestCycle.get().getApplication()).getBannerService();
		return new BannerProxy(bannerService.getBanner(id));
	}

	@Override
	public int hashCode() {
		return Long.valueOf(id).hashCode();
	}

	@Override
	public boolean equals(final Object obj) {

		if (obj == this) {
			return true;
		} else if (obj == null) {
			return false;
		} else if (obj instanceof DetachableBannerModel) {
			DetachableBannerModel other = (DetachableBannerModel) obj;
			return other.id == id;
		}
		return false;
	}

}

// $Id$