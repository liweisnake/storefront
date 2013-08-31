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
package com.hp.sdf.ngp.manager.impl;

import java.io.Serializable;
import java.util.Locale;
import java.util.zip.ZipEntry;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.hp.sdf.ngp.cms.CMSMimeMappings;
import com.hp.sdf.ngp.cms.impl.CMSServiceJBossImpl;
import com.hp.sdf.ngp.cms.model.Content;
import com.hp.sdf.ngp.cms.model.File;

public class CmsUploadThread implements Runnable, Serializable {
	
	private final static Log log = LogFactory.getLog(CmsUploadThread.class);

	@Resource
	private CMSServiceJBossImpl cmsService ;
	
	private static final long serialVersionUID = 1L;

	private byte[] bytes;
	
	private ZipEntry entry;
	
	public static String ROOT_PATH = "/api";
	
	public CmsUploadThread(byte[] bytes, ZipEntry entry){
		this.bytes = bytes;
		this.entry = entry;
	}

	public void run() {
		cmsService = new CMSServiceJBossImpl();
		cmsService.init();
    	//String type = entry.getName().substring(0, entry.getName().indexOf("/"));
    	//String apiName = entry.getName().substring(entry.getName().indexOf("/") + 1, entry.getName().lastIndexOf("/"));
    	String fileName = entry.getName().substring(entry.getName().lastIndexOf("/") + 1);
    	//String path = entry.getName().substring(0,entry.getName().lastIndexOf("/"));
    	
    	log.debug("Begin to upload API document 4: " + fileName);
    	
    	//set api root path
//    	if(this.cMSServiceJBossImpl.getFolder(CmsUploadThread.ROOT_PATH) == null){
//    		Folder folder = new Folder();
//    		folder.setCreationDate(new Date());
//    		folder.setTitle(CmsUploadThread.ROOT_PATH);
//    		folder.setLastModified(new Date());
//    		folder.setName(CmsUploadThread.ROOT_PATH);
//    		this.cmsService.saveFolder(folder);
//    	}

		File file = new File();
		Content content = new Content();
		String fileExt = fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length());	
		CMSMimeMappings mapper = new CMSMimeMappings();			 
		if (mapper.getMimeType(fileExt) != null){
			content.setMimeType(mapper.getMimeType(fileExt));
		}else{
			content.setMimeType("application/octet-stream");
		}
		file.setBasePath(CmsUploadThread.ROOT_PATH + "/" + entry.getName());
		file.setDescription(file.getBasePath());
		content.setTitle(fileName);
		content.setBasePath(CmsUploadThread.ROOT_PATH + "/" + entry.getName() + "/" + new Locale("en"));
		content.setDescription(content.getBasePath());
		content.setBytes(this.bytes);
		content.setLocale(new Locale("en"));
		file.setContent(new Locale("en"), content);
		log.debug("File base path: " + file.getBasePath());
		this.cmsService.createFile(file);		
		
		log.debug("Finish uploading API document: " + fileName);
	}


}

// $Id$