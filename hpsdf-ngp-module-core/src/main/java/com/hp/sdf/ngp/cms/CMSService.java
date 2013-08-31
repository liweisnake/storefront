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
package com.hp.sdf.ngp.cms;

import java.util.List;
import java.util.Locale;

import com.hp.sdf.ngp.cms.model.Content;
import com.hp.sdf.ngp.cms.model.File;
import com.hp.sdf.ngp.cms.model.Folder;

/**
 * This interface <b>CMSService</b> provides operations on contents of CMS repository. 
 * 
 * Usage:
 * To create a folder 
 *              Folder folder = new Folder();
 *              folder.setCreationDate(new Date());
 *              folder.setDescription(sFolderDescription);
 *              folder.setTitle(sFolderName);
 *              folder.setLastModified(new Date());
 *              folder.setName(sFolderName);
 *              folder.setBasePath(sNewPath);
 *              // create a service, or you can inject the service object
 *              CMSService service = new CMSServiceJBossImpl();
 *              service.saveFolder(folder);
 *              
 * To create a file or upload a content
 * 				File file = new FileImpl();
 * 				Content content = new ContentImpl();
 * 				String fileExt = sFilename.substring(sFilename.lastIndexOf(".") + 1, sFilename.length());	
 *				CMSMimeMappings mapper = new CMSMimeMappings();			 
 * 				if (mapper.getMimeType(fileExt) != null)
 *	            {
 *                         content.setMimeType(mapper.getMimeType(fileExt));
 *              }
 *              else
 *              {
 *                 content.setMimeType("application/octet-stream");
 *              }
 *              String sBasePath = FileUtil.cleanDoubleSlashes(sPath + SLASH + sFilename);
 *              file.setBasePath(sBasePath);
 *              content.setTitle(sTitle);
 *              content.setDescription(sDescription);
 *              content.setBasePath(sBasePath + SLASH + new Locale(sLanguage));
 *              content.setBytes(item.get());
 *              file.setContent(new Locale(sLanguage), content);
 *              // create a service, or you can inject the service object
 *              CMSService service = new CMSServiceJBossImpl();
 *              service.saveFile(file);
 *
 */
public interface CMSService {

	public void createFile(File sFile) throws CMSException;

	public void updateFile(File sFile, boolean makeLive)
			throws CMSException;

	public void copy(String sFromPath, String sToPath) throws CMSException;

	public void move(String sFromPath, String sToPath) throws CMSException;

	public void rename(String sPath, String sNewName) throws CMSException;

	public void delete(String sPath) throws CMSException;

	public void saveFile(File file) throws CMSException;

	public File getFile(String sFilePath, Locale locale) throws CMSException;

	public File getFile(String sFilePath, String sVersionNumber, Locale locale)
			throws CMSException;

	public void updateFile(File file) throws CMSException;

	public void saveFolder(Folder folder) throws CMSException;

	public Folder getFolder(String sFolderPath) throws CMSException;

	public void updateFolder(Folder folder) throws CMSException;

	public List<Content> getContentVersions(String sFilePath)
			throws CMSException;

	/**
	 * Gets folder list. 
	 * The list can be retrieved by folder.getFolders().
	 * 
	 * @param sFolderPath
	 *            the target folder path
	 * @return the folder with a folder list
	 * @throws CMSException
	 *             when exception occurs while retrieve folder list
	 */
	public Folder getFolderList(String sFolderPath) throws CMSException;

	public void createContentNewVersion(Content content, boolean bMakeLive)
			throws CMSException;

	public void createContentNewVersion(List<Content> contents,
			boolean bMakeLive) throws CMSException;

	public void storeArchive(String sRootPath, byte[] archiveBytes,
			String sLanguage) throws CMSException;

	public void asyncStoreArchive(String sRootPath, byte[] archiveBytes,
			String sLanguage) throws CMSException;

	public boolean existItem(String sPath) throws CMSException;

	public void saveContent(File file) throws CMSException;

	public Content getContent(String sFilePath, String sVersionNumber,
			Locale locale) throws CMSException;

	public List<Content> getFileList(String sFilePath) throws CMSException;

	public File getArchive(String sRootPath, String sLanguage)
			throws CMSException;

	public List<File> search(String query) throws CMSException;

	public String makeLiveVersion(String filePath, String language,
			String version) throws CMSException;

	public File downloadArchive(String filePath) throws CMSException;

}

// $Id$