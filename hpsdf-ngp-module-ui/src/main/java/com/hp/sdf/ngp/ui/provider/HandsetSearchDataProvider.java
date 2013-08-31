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
package com.hp.sdf.ngp.ui.provider;

import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import com.hp.sdf.ngp.api.search.OrderEnum;
import com.hp.sdf.ngp.api.search.SearchExpression;
import com.hp.sdf.ngp.api.search.Condition.StringComparer;
import com.hp.sdf.ngp.custom.sbm.api.storeclient.exception.HandsetServiceException;
import com.hp.sdf.ngp.custom.sbm.api.storeclient.model.HandSetDevice;
import com.hp.sdf.ngp.custom.sbm.api.storeclient.service.HandsetDeviceService;
import com.hp.sdf.ngp.search.condition.handsetdevice.HandSetDeviceDeviceNameCondition;
import com.hp.sdf.ngp.search.condition.handsetdevice.HandSetDeviceDisplayNameCondition;
import com.hp.sdf.ngp.search.engine.SearchExpressionImpl;
import com.hp.sdf.ngp.search.order.handsetdevice.HandSetDeviceOrderBy;
import com.hp.sdf.ngp.ui.common.HandsetSearchCondition;

public class HandsetSearchDataProvider implements IDataProvider<HandSetDevice> {

	private static final long serialVersionUID = 6080358671456933625L;

	private final static Log log = LogFactory.getLog(HandsetSearchDataProvider.class);

	private HandsetDeviceService handsetDeviceService;

	private HandsetSearchCondition handsetSearchCondition;

	public HandsetSearchCondition getHandsetSearchCondition() {
		return handsetSearchCondition;
	}

	public void setHandsetSearchCondition(HandsetSearchCondition handsetSearchCondition) {
		this.handsetSearchCondition = handsetSearchCondition;
	}

	public HandsetSearchDataProvider(HandsetDeviceService handsetDeviceService, HandsetSearchCondition handsetSearchCondition) {
		this.handsetDeviceService = handsetDeviceService;
		this.handsetSearchCondition = handsetSearchCondition;
	}

	private SearchExpression getSearchExpression(int first, int count) {

		SearchExpression searchExpression = new SearchExpressionImpl();

		if (handsetSearchCondition != null) {

			if (StringUtils.isNotEmpty(handsetSearchCondition.getHandsetName())) {
				searchExpression.addCondition(new HandSetDeviceDisplayNameCondition(handsetSearchCondition.getHandsetName(), StringComparer.LIKE, true, false));
			}

			if (StringUtils.isNotEmpty(handsetSearchCondition.getDeviceName())) {
				searchExpression.addCondition(new HandSetDeviceDeviceNameCondition(handsetSearchCondition.getDeviceName(), StringComparer.LIKE, true, false));
			}
		}
		
		searchExpression.addOrder(HandSetDeviceOrderBy.CREATETIME,OrderEnum.DESC);
		searchExpression.addOrder(HandSetDeviceOrderBy.DEVICENAME,OrderEnum.ASC);
		
		
		searchExpression.setFirst(first);
		searchExpression.setMax(count);
		return searchExpression;
	}

	public Iterator<? extends HandSetDevice> iterator(int first, int count) {
		log.debug("first :" + first);
		log.debug("count :" + count);

		SearchExpression searchExpression = getSearchExpression(first, count);

		try {
			List<HandSetDevice> list = handsetDeviceService.searchHandsetDevice(searchExpression);
			if (list != null) {
				return list.iterator();
			}
		} catch (HandsetServiceException exception) {
			exception.printStackTrace();
			log.error("HandsetServiceException :" + exception);
		}

		return null;
	}

	public IModel<HandSetDevice> model(HandSetDevice object) {
		return new Model<HandSetDevice>(object);
	}

	public int size() {
		SearchExpression searchExpression = getSearchExpression(0, Integer.MAX_VALUE);
		try {
			return handsetDeviceService.countHandsetDevice(searchExpression);
		} catch (HandsetServiceException exception) {
			exception.printStackTrace();
			log.error("HandsetServiceException :" + exception);
		}

		return 0;
	}

	public void detach() {
	}

}
