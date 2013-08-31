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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.wicket.extensions.markup.html.repeater.data.table.filter.IFilterStateLocator;
import org.apache.wicket.extensions.markup.html.repeater.util.SortParam;
import org.apache.wicket.extensions.markup.html.repeater.util.SortableDataProvider;
import org.apache.wicket.model.IModel;
import org.springframework.stereotype.Component;

import com.hp.sdf.ngp.banner.BannerService;
import com.hp.sdf.ngp.banner.BannerStatus;
import com.hp.sdf.ngp.banner.BannerType;
import com.hp.sdf.ngp.banner.QueryParam;
import com.hp.sdf.ngp.banner.model.BaseBanner;

@Component
public class BannerDataProvider extends SortableDataProvider<BannerProxy> implements IFilterStateLocator {

	/**
	 * 
	 */
	private static final long serialVersionUID = 366765421603544832L;

	@Resource
	private BannerService bannerService;

	private QueryParam queryParam;

	private BannerProxy filter = new BannerProxy();

	private Map<Integer, String> typeDisplayNameMap;

	private Map<Integer, String> statusDisplayNameMap;

	public void setTypeDisplayNameMap(Map<Integer, String> typeDisplayNameMap) {
		this.typeDisplayNameMap = typeDisplayNameMap;
	}

	public void setStatusDisplayNameMap(Map<Integer, String> statusDisplayNameMap) {
		this.statusDisplayNameMap = statusDisplayNameMap;
	}

	public BannerDataProvider() {
		this.setSort("name", true);
	}

	public void setQueryParam(QueryParam queryParam) {
		this.queryParam = queryParam;
	}

	public Object getFilterState() {
		return filter;
	}

	public void setFilterState(Object state) {
		filter = (BannerProxy) state;

	}

	public Iterator<BannerProxy> iterator(int first, int count) {
		SortParam sp = getSort();
		if (queryParam == null) {
			queryParam = new QueryParam(first, count, sp.getProperty(), sp.isAscending());
		} else {
			queryParam.setFirst(first);
			queryParam.setCount(count);
			queryParam.setSort(sp.getProperty());
			queryParam.setSortAsc(sp.isAscending());
		}
		BaseBanner banner = new BaseBanner();
		if (filter.getBannerStatus() != null) {
			if (filter.getBannerStatus().equals(statusDisplayNameMap.get(new Integer(-1)))) {
				banner.setStauts("all");
			} else {
				for (Integer key : statusDisplayNameMap.keySet()) {
					if (filter.getBannerStatus().equals(statusDisplayNameMap.get(key))) {
						for (BannerStatus status : BannerStatus.values()) {
							if (status.ordinal() == key.intValue()) {
								banner.setBannerStatus(status);
							}
						}
					}
				}
			}
		} else {
			banner.setBannerStatus(null);
		}
		if (filter.getBannerType() != null) {
			if (filter.getBannerType().equals(typeDisplayNameMap.get(new Integer(-1)))) {
				banner.setType("all");
			} else {
				for (Integer key : typeDisplayNameMap.keySet()) {
					if (filter.getBannerType().equals(typeDisplayNameMap.get(key))) {
						for (BannerType type : BannerType.values()) {
							if (type.ordinal() == key.intValue()) {
								banner.setBannerType(type);
							}
						}
					}
				}
			}
		} else {
			banner.setBannerType(null);
		}
		banner.setDescription(filter.getDescription());
		banner.setName(filter.getName());
		banner.setId(filter.getId());
		List<BaseBanner> baseBanners = bannerService.find(queryParam, banner);
		List<BannerProxy> bannerProxies = new ArrayList<BannerProxy>();
		for (BaseBanner b : baseBanners) {
			BannerProxy elem = new BannerProxy(b);
			bannerProxies.add(elem);
		}
		return bannerProxies.iterator();
	}

	public IModel<BannerProxy> model(BannerProxy object) {
		return new DetachableBannerModel(object);
	}

	public int size() {
		return bannerService.countBanner();
	}
}

// $Id$