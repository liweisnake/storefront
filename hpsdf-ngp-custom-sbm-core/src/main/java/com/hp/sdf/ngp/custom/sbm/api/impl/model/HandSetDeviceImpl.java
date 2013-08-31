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
package com.hp.sdf.ngp.custom.sbm.api.impl.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.drools.util.StringUtils;

import com.hp.sdf.ngp.custom.sbm.storeclient.model.HandSetDevice;
import com.hp.sdf.ngp.custom.sbm.storeclient.model.MimeType;

public class HandSetDeviceImpl implements com.hp.sdf.ngp.custom.sbm.api.storeclient.model.HandSetDevice, Serializable {

	private static final long serialVersionUID = 4388135682687100335L;

//	private final static Log log = LogFactory.getLog(HandSetDeviceImpl.class);

	private HandSetDevice handSetDevice;

	public HandSetDeviceImpl() {
		handSetDevice = new HandSetDevice();
	}

	public HandSetDeviceImpl(HandSetDevice handSetDevice) {
		this.handSetDevice = handSetDevice;
		if (null != this.handSetDevice) {
			this.handSetDevice.getDeviceName();//load information to avoid the lazy load
		}
	}

	public HandSetDevice getHandSetDevice() {
		if (null == handSetDevice) {
			handSetDevice = new HandSetDevice();
		}
		return handSetDevice;
	}

	public void setMimeTypes(List<String> mimeTypes) {

		if (null == mimeTypes) {
			// clear MimeType
			handSetDevice.setMimeTypes(new HashSet<MimeType>(0));
			return;
		}

		Set<MimeType> mimeTypeSet = new HashSet<MimeType>();
		if (mimeTypes != null && mimeTypes.size() > 0) {
			for (String mimeType : mimeTypes) {
				MimeType mime = new MimeType();
				mime.setHandSetDevice(handSetDevice);
				mime.setType(mimeType);

				mimeTypeSet.add(mime);
			}
		}

		handSetDevice.setMimeTypes(mimeTypeSet);
	}

	public List<String> getMimeType() {

		List<String> mimeTypeList = new ArrayList<String>();

		Set<MimeType> mimeTypes = handSetDevice.getMimeTypes();
//		log.debug("mimeTypes.size:" + mimeTypes.size());

		if (mimeTypes != null && mimeTypes.size() > 0) {
			for (MimeType mimeType : mimeTypes) {
//				log.debug("mimeType.getType():" + mimeType.getType());
				mimeTypeList.add(mimeType.getType());
			}
		}

		return mimeTypeList;
	}

	public void addMimeType(String mimeType) {
//		log.debug("Adding mimeType :" + mimeType);
		if (StringUtils.isEmpty(mimeType)) {
			return;
		}

		MimeType mime = new MimeType();
		mime.setHandSetDevice(handSetDevice);
		mime.setType(mimeType);

		Set<MimeType> mimeTypes = handSetDevice.getMimeTypes();
		mimeTypes.add(mime);

		handSetDevice.setMimeTypes(mimeTypes);
	}
	
	public void deleteMimeType(String mimeType) {
//		log.debug("Adding mimeType :" + mimeType);
		if (StringUtils.isEmpty(mimeType)) {
			return;
		}
	
		Set<MimeType> mimeTypes = handSetDevice.getMimeTypes();
		for (MimeType mime : mimeTypes) {
			if (mimeType.equals(mime.getType())) {
				mimeTypes.remove(mime);
				break;
			}
		}
	
		handSetDevice.setMimeTypes(mimeTypes);
	}

	public void setDeviceName(String deviceName) {
//		log.debug("deviceName:" + deviceName);
		handSetDevice.setDeviceName(deviceName);
	}

	public void setDisplayName(String displayName) {
//		log.debug("displayName:" + displayName);
		handSetDevice.setDisplayName(displayName);
	}

	public void setFunctionFilter(String functionFilter) {
//		log.debug("functionFilter:" + functionFilter);
		handSetDevice.setFunctionFilter(functionFilter);
	}

	public void setResolutionFilter(Long resolutionFilter) {
//		log.debug("resolutionFilter:" + resolutionFilter);
		handSetDevice.setResolutionFilter(resolutionFilter);
	}

	public String getDeviceName() {
		return handSetDevice.getDeviceName();
	}

	public String getDisplayName() {
		return handSetDevice.getDisplayName();
	}

	public String getFunctionFilter() {
		return handSetDevice.getFunctionFilter();
	}

	public Long getResolutionFilter() {
		return handSetDevice.getResolutionFilter();
	}
	
	@Override
	public String toString(){
		String desc= "HandSetDeivice[resolutionFilter="+getResolutionFilter()+",functionFilter="+getFunctionFilter()+",displayName="+getDisplayName()
		+",deviceName="+getDeviceName()+",mimeTypeSize=";
		if(getMimeType().size()>0)
		{
			for(String mime:getMimeType()){
				desc +=mime+",";
			}
		}else{
			desc +=",mimeTypeSize=''";
		}
		
		return desc+"]";
	}

}
