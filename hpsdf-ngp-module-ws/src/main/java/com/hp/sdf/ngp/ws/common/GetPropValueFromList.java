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
package com.hp.sdf.ngp.ws.common;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.hp.sdf.ngp.model.AssetPrice;
import com.hp.sdf.ngp.model.Platform;

/**
 * get specific pojo's property value from list
 * 
 * @author Anders Zhu
 * 
 */
public final class GetPropValueFromList {

	public final static String getPlatfromNameFromList(final List<Platform> list) {

		StringBuffer sb = new StringBuffer();
		if (null != list) {
			for (Platform platform : list) {
				sb.append(platform.getName() + Constant.SUFFIX);
			}
		}
		return sb.toString();
	}

	public final static BigDecimal getAssetPriceFromListByCurrency(final List<AssetPrice> list, final String currency) {

		if (null != list) {
			for (AssetPrice price : list) {
				if (currency.equalsIgnoreCase(price.getCurrency())) {
					return price.getAmount();
				}
			}
		}
		return null;
	}

	public final static BigDecimal getAssetPriceFromListDollars(final List<AssetPrice> list) {

		return getAssetPriceFromListByCurrency(list, Constant.CURRENCY_DOLLARS);
	}

	public final static AssetPrice getAssetPriceBeanFromListByCurrency(final List<AssetPrice> list, final String currency) {

		if (null != list) {
			for (AssetPrice price : list) {
				if (currency.equalsIgnoreCase(price.getCurrency())) {
					return price;
				}
			}
		}
		return null;
	}

	public final static AssetPrice getAssetPriceBeanFromListDollars(final List<AssetPrice> list) {

		return getAssetPriceBeanFromListByCurrency(list, Constant.CURRENCY_DOLLARS);
	}

	public final static List<Long> getPlatformIdList(final List<Platform> platformList) {

		List<Long> platformIdList = new ArrayList<Long>();
		if (null != platformList) {
			for (Platform platform : platformList) {
				platformIdList.add(platform.getId());
			}
		}
		return platformIdList;
	}

	public final static boolean isPlatformIdEqual(final List<Platform> platformList, Long platformId) {
		if (null != platformList) {
			for (Platform platform : platformList) {
				if (platform.getId().equals(platformId))
					return true;
			}
		}
		return false;
	}
}