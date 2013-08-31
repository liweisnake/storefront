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
package com.hp.sdf.ngp.custom.sbm.storeclient.service;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.hp.sdf.ngp.api.search.SearchExpression;
import com.hp.sdf.ngp.custom.sbm.api.impl.model.HandSetDeviceImpl;
import com.hp.sdf.ngp.custom.sbm.api.storeclient.exception.HandsetServiceException;
import com.hp.sdf.ngp.custom.sbm.api.storeclient.model.HandSetDevice;
import com.hp.sdf.ngp.custom.sbm.api.storeclient.service.HandsetDeviceService;
import com.hp.sdf.ngp.custom.sbm.storeclient.dao.HandSetDeviceDao;

@Component
@Transactional
public class HandsetDeviceServiceImpl implements HandsetDeviceService {

	private final static Log log = LogFactory.getLog(HandsetDeviceServiceImpl.class);

	@Resource
	private HandSetDeviceDao handSetDeviceDao;

	// @Resource
	// private MimeTypeDao mimeTypeDao;

	public void deleteHandSetDeviceById(Long handSetDeviceId) throws HandsetServiceException {
		log.debug("enter deleteHandSetDeviceById:handSetDeviceId=" + handSetDeviceId);
		try {
			handSetDeviceDao.remove(handSetDeviceId);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("HandsetServiceException exception: " + e);
			throw new HandsetServiceException(e);
		}

	}

	public void saveHandSetDevice(HandSetDevice handSetDevice) throws HandsetServiceException {
		log.debug("enter saveHandSetDevice:" + handSetDevice.toString());
		try {
			HandSetDeviceImpl handSetDeviceImpl = (HandSetDeviceImpl) handSetDevice;
			handSetDeviceDao.persist(handSetDeviceImpl.getHandSetDevice());
		} catch (Exception e) {
			e.printStackTrace();
			log.error("HandsetServiceException exception: " + e);
			throw new HandsetServiceException(e);
		}
	}

	public void updateHandSetDevice(HandSetDevice handSetDevice) throws HandsetServiceException {
		log.debug("enter updateHandSetDevice:" + handSetDevice.toString());
		try {
			HandSetDeviceImpl handSetDeviceImpl = (HandSetDeviceImpl) handSetDevice;
			handSetDeviceDao.merge(handSetDeviceImpl.getHandSetDevice());
		} catch (Exception e) {
			e.printStackTrace();
			log.error("HandsetServiceException exception: " + e);
			throw new HandsetServiceException(e);
		}
	}

	@Transactional(readOnly = true)
	public List<HandSetDevice> searchHandsetDevice(SearchExpression searchExpression) throws HandsetServiceException {
		log.debug("enter searchHandsetDevice:searchExpression=" + searchExpression.toString());
		try {
			List<HandSetDevice> handSetDeviceList = new ArrayList<HandSetDevice>();

			List<com.hp.sdf.ngp.custom.sbm.storeclient.model.HandSetDevice> list = handSetDeviceDao.search(searchExpression);
			if (list != null && list.size() > 0) {
				for (com.hp.sdf.ngp.custom.sbm.storeclient.model.HandSetDevice handSetDevice : list) {
					HandSetDeviceImpl handSetDeviceImpl = new HandSetDeviceImpl(handSetDevice);
					// fillHandSetDevice(handSetDeviceImpl);
					handSetDeviceList.add(handSetDeviceImpl);
				}
			}

			log.debug("searchHandsetDevice returns size:" + handSetDeviceList.size());
			return handSetDeviceList;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("HandsetServiceException exception: " + e);
			throw new HandsetServiceException(e);
		}
	}

	public int countHandsetDevice(SearchExpression searchExpression) {
		log.debug("enter countHandsetDevice:searchExpression=" + searchExpression.toString());
		int count = handSetDeviceDao.searchCount(searchExpression);
		log.debug("countHandsetDevice returns:" + count);
		return count;
	}

	public HandSetDevice getHandSetDeviceById(Long handSetDeviceId) {
		log.debug("enter getHandSetDeviceById:handSetDeviceId=" + handSetDeviceId);
		com.hp.sdf.ngp.custom.sbm.storeclient.model.HandSetDevice model = handSetDeviceDao.findById(handSetDeviceId);
		if (model != null) {
			log.debug("handSetDeviceId returns:" + new HandSetDeviceImpl(model).toString());
			return new HandSetDeviceImpl(model);
		}

		log.debug("handSetDeviceId returns null");
		return null;
	}

}
