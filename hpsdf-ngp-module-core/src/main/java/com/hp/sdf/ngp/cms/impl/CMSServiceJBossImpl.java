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
package com.hp.sdf.ngp.cms.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.annotation.PostConstruct;
import javax.management.MBeanServer;
import javax.management.ObjectName;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.mx.util.MBeanProxy;
import org.jboss.mx.util.MBeanServerLocator;
import org.jboss.portal.cms.CMS;
import org.jboss.portal.cms.Command;
import org.jboss.portal.cms.impl.ContentImpl;
import org.jboss.portal.cms.impl.FileImpl;
import org.jboss.portal.cms.impl.FolderImpl;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.hp.sdf.ngp.cms.CMSException;
import com.hp.sdf.ngp.cms.CMSService;
import com.hp.sdf.ngp.cms.model.Content;
import com.hp.sdf.ngp.cms.model.File;
import com.hp.sdf.ngp.cms.model.Folder;
import com.hp.sdf.ngp.common.annotation.Value;

@Scope("prototype")  
@Component(value = "cmsService")
public class CMSServiceJBossImpl implements CMSService {

	private static final Log log = LogFactory.getLog(CMSServiceJBossImpl.class);

	private CMS jbossCMS = null;

	private boolean runtime = true;

	public boolean isRuntime() {
		return runtime;
	}

	@Value("runtime")
	public void setRuntime(boolean runtime) {
		this.runtime = runtime;
	}

	/**
	 * default JBoss CMSService initial process
	 */
	@PostConstruct
	public void init() {
		try {
			MBeanServer mbeanServer = MBeanServerLocator.locateJBoss();
			jbossCMS = (CMS) MBeanProxy.get(CMS.class, new ObjectName(
					"portal:service=CMS"), mbeanServer);
			if (jbossCMS == null) {
				throw new CMSException("JBoss CMS Service was not found!!");
			}
		} catch (Throwable e) {
			if (runtime) {
				throw new CMSException(e.getMessage());
			} else {
				log.warn("Can't create the CMS instance in non runtime environment", e);
			}
		}
	}

	public CMS getJbossCMS() {
		return jbossCMS;
	}

	public void setJbossCMS(CMS jbossCMS) {
		this.jbossCMS = jbossCMS;
	}

	public void asyncStoreArchive(String rootPath, byte[] archiveBytes,
			String language) throws CMSException {
		log.debug("enter CMSServiceJBossImpl.asyncStoreArchive()");

		try {
			Command command = jbossCMS.getCommandFactory()
					.createAsyncStoreArchiveCommand(rootPath, archiveBytes,
							language);
			jbossCMS.execute(command);
		} catch (Exception e) {
			CMSException cmse = new CMSException(e);
			log.error(cmse);
			cmse.printStackTrace();
		}

		log.debug("leave CMSServiceJBossImpl.asyncStoreArchive()");
	}

	public void copy(String fromPath, String toPath) throws CMSException {
		log.debug("enter CMSServiceJBossImpl.copy()");

		try {
			Command command = jbossCMS.getCommandFactory().createCopyCommand(
					fromPath, toPath);
			jbossCMS.execute(command);
		} catch (Exception e) {
			CMSException cmse = new CMSException(e);
			log.error(cmse);
			cmse.printStackTrace();
		}

		log.debug("leave CMSServiceJBossImpl.copy()");
	}

	public void createContentNewVersion(Content content, boolean makeLive)
			throws CMSException {
		log.debug("enter CMSServiceJBossImpl.createContentNewVersion()");

		try {
			org.jboss.portal.cms.model.Content cmsContent = new ContentImpl();
			BeanUtils.copyProperties(cmsContent, content);
			Command command = jbossCMS.getCommandFactory()
					.createContentCreateNewVersionCommand(cmsContent, makeLive);
			jbossCMS.execute(command);
		} catch (Exception e) {
			CMSException cmse = new CMSException(e);
			log.error(cmse);
			cmse.printStackTrace();
		}

		log.debug("leave CMSServiceJBossImpl.createContentNewVersion()");
	}

	public void createContentNewVersion(List<Content> contents, boolean makeLive)
			throws CMSException {
		log.debug("enter CMSServiceJBossImpl.createContentNewVersion(List)");

		try {
			List<org.jboss.portal.cms.model.Content> cmsContents = new ArrayList<org.jboss.portal.cms.model.Content>();
			org.jboss.portal.cms.model.Content cmsContent;
			for (Content content : contents) {
				cmsContent = new ContentImpl();
				BeanUtils.copyProperties(cmsContent, content);
				cmsContents.add(cmsContent);
			}

			Command command = jbossCMS
					.getCommandFactory()
					.createContentCreateNewVersionCommand(cmsContents, makeLive);
			jbossCMS.execute(command);
		} catch (Exception e) {
			CMSException cmse = new CMSException(e);
			log.error(cmse);
			cmse.printStackTrace();
		}

		log.debug("leave CMSServiceJBossImpl.createContentNewVersion(List)");
	}

	public void createFile(File file) throws CMSException {
		log.debug("enter CMSServiceJBossImpl.createFile(File file)");

		try {
			org.jboss.portal.cms.model.File cmsFile = this
					.reverseCopyFile(file);
			
            Command existsCMD = jbossCMS.getCommandFactory().createItemExistsCommand(cmsFile.getContent().getBasePath());
            Boolean bExists = (Boolean)jbossCMS.execute(existsCMD);
            if (bExists.booleanValue()) // if file exists, update contentNode
            {
               // force "make live"
               Command cmdUpdate = jbossCMS.getCommandFactory().createUpdateFileCommand(cmsFile, cmsFile.getContent(), true);
               jbossCMS.execute(cmdUpdate);
            }
            else // if file doesn't exist, create the file and the contentNode.
            {
               Command newFileCMD = jbossCMS.getCommandFactory().createNewFileCommand(cmsFile, cmsFile.getContent());
               jbossCMS.execute(newFileCMD);
            }

		} catch (Exception e) {
			CMSException cmse = new CMSException(e);
			log.error(cmse);
			cmse.printStackTrace();
			throw cmse;
		}

		log.debug("leave CMSServiceJBossImpl.createFile(File file)");
	}

	public void delete(String path) throws CMSException {
		log.debug("enter CMSServiceJBossImpl.delete()");

		try {
			Command command = jbossCMS.getCommandFactory().createDeleteCommand(
					path);
			jbossCMS.execute(command);
		} catch (Exception e) {
			CMSException cmse = new CMSException(e);
			log.error(cmse);
			cmse.printStackTrace();
		}

		log.debug("leave CMSServiceJBossImpl.delete()");

	}

	public File downloadArchive(String filePath) throws CMSException {
		log.debug("enter CMSServiceJBossImpl.downloadArchive()");

		File file = null;
		try {
			Command command = jbossCMS.getCommandFactory()
					.createDownloadArchiveCommand(filePath);
			org.jboss.portal.cms.model.File cmsFile = (org.jboss.portal.cms.model.File) jbossCMS
					.execute(command);
			Content content = new Content();
			BeanUtils.copyProperties(content, cmsFile.getContent());
			file = new File();
			file.setContent(content.getLocale(), content);
		} catch (Exception e) {
			CMSException cmse = new CMSException(e);
			log.error(cmse);
			cmse.printStackTrace();
		}

		log.debug("leave CMSServiceJBossImpl.downloadArchive()");

		return file;
	}

	public boolean existItem(String path) throws CMSException {
		log.debug("enter CMSServiceJBossImpl.existItem()");

		boolean exist = false;
		try {
			Command command = jbossCMS.getCommandFactory()
					.createItemExistsCommand(path);
			Boolean cmsExists = (Boolean) jbossCMS.execute(command);
			exist = cmsExists.booleanValue();
		} catch (Exception e) {
			CMSException cmse = new CMSException(e);
			log.error(cmse);
			cmse.printStackTrace();
		}

		log.debug("leave CMSServiceJBossImpl.existItem()");

		return exist;
	}

	public File getArchive(String rootPath, String language)
			throws CMSException {
		log.debug("enter CMSServiceJBossImpl.getArchive()");

		File file = null;
		try {
			Command command = jbossCMS.getCommandFactory()
					.createGetArchiveCommand(rootPath, language);
			org.jboss.portal.cms.model.File cmsFile = (org.jboss.portal.cms.model.File) jbossCMS
					.execute(command);
			org.jboss.portal.cms.model.Content cmsContent = cmsFile
					.getContent();
			Content content = new Content();
			BeanUtils.copyProperties(content, cmsContent);
			file = new File();
			file.setContent(content.getLocale(), content);
		} catch (Exception e) {
			CMSException cmse = new CMSException(e);
			log.error(cmse);
			cmse.printStackTrace();
		}

		log.debug("leave CMSServiceJBossImpl.getArchive()");

		return file;
	}

	public Content getContent(String filePath, String versionNumber,
			Locale locale) throws CMSException {
		log.debug("enter CMSServiceJBossImpl.getContent()");

		Content content = null;
		try {
			Command command = jbossCMS.getCommandFactory()
					.createContentGetCommand(filePath, versionNumber, locale);
			org.jboss.portal.cms.model.Content cmsContent = (org.jboss.portal.cms.model.Content) jbossCMS
					.execute(command);
			content = new Content();
			BeanUtils.copyProperties(content, cmsContent);
		} catch (Exception e) {
			CMSException cmse = new CMSException(e);
			log.error(cmse);
			cmse.printStackTrace();
		}

		log.debug("leave CMSServiceJBossImpl.getContent()");

		return content;
	}

	public List<Content> getContentVersions(String filePath)
			throws CMSException {
		log.debug("enter CMSServiceJBossImpl.getContentVersions()");

		List<Content> contents = new ArrayList<Content>();
		Content content = null;
		try {
			Command command = jbossCMS.getCommandFactory()
					.createContentGetVersionsCommand(filePath);
			List<org.jboss.portal.cms.model.Content> cmsContents = (List) jbossCMS
					.execute(command);
			for (org.jboss.portal.cms.model.Content cmsContent : cmsContents) {
				content = new Content();
				BeanUtils.copyProperties(content, cmsContent);
				contents.add(content);
			}
		} catch (Exception e) {
			CMSException cmse = new CMSException(e);
			log.error(cmse);
			cmse.printStackTrace();
		}

		log.debug("leave CMSServiceJBossImpl.getContentVersions()");

		return contents;
	}

	public File getFile(String filePath, Locale locale) throws CMSException {
		log.debug("enter CMSServiceJBossImpl.getFile()");

		File file = null;
		try {
			Command command = jbossCMS.getCommandFactory()
					.createFileGetCommand(filePath, locale);
			org.jboss.portal.cms.model.File cmsFile = (org.jboss.portal.cms.model.File) jbossCMS
					.execute(command);
			Content content = new Content();
			BeanUtils.copyProperties(content, cmsFile.getContent());
			file = new File();
			file.setContent(content.getLocale(), content);
		} catch (Exception e) {
			CMSException cmse = new CMSException(e);
			log.error(cmse);
			cmse.printStackTrace();
		}

		log.debug("leave CMSServiceJBossImpl.getFile()");

		return file;
	}

	public File getFile(String filePath, String versionNumber, Locale locale)
			throws CMSException {
		log.debug("enter CMSServiceJBossImpl.getFile()");

		File file = null;
		try {
			Command command = jbossCMS.getCommandFactory()
					.createFileGetCommand(filePath, versionNumber, locale);
			org.jboss.portal.cms.model.File cmsFile = (org.jboss.portal.cms.model.File) jbossCMS
					.execute(command);
			Content content = new Content();
			BeanUtils.copyProperties(content, cmsFile.getContent());
			file = new File();
			file.setContent(content.getLocale(), content);
		} catch (Exception e) {
			CMSException cmse = new CMSException(e);
			log.error(cmse);
			cmse.printStackTrace();
		}

		log.debug("leave CMSServiceJBossImpl.getFile()");

		return file;
	}

	public List<Content> getFileList(String filePath) throws CMSException {
		log.debug("enter CMSServiceJBossImpl.getFileList()");

		List<Content> contents = new ArrayList<Content>();
		Content content = null;
		try {
			Command command = jbossCMS.getCommandFactory()
					.createFileGetListCommand(filePath);
			List<org.jboss.portal.cms.model.Content> cmsContents = (List) jbossCMS
					.execute(command);
			for (org.jboss.portal.cms.model.Content cmsContent : cmsContents) {
				content = new Content();
				BeanUtils.copyProperties(content, cmsContent);
				contents.add(content);
			}
		} catch (Exception e) {
			CMSException cmse = new CMSException(e);
			log.error(cmse);
			cmse.printStackTrace();
		}

		log.debug("leave CMSServiceJBossImpl.getFileList()");

		return contents;
	}

	public Folder getFolder(String folderPath) throws CMSException {
		log.debug("enter CMSServiceJBossImpl.getFolder()");

		Folder folder = null;
		try {
			Command command = jbossCMS.getCommandFactory()
					.createFolderGetCommand(folderPath);
			org.jboss.portal.cms.model.Folder cmsFolder = (org.jboss.portal.cms.model.Folder) jbossCMS
					.execute(command);
			folder = copyFolder(cmsFolder);
		} catch (Exception e) {
			CMSException cmse = new CMSException(e);
			log.error(cmse);
			cmse.printStackTrace();
		}

		log.debug("leave CMSServiceJBossImpl.getFolder()");

		return folder;
	}

	public Folder getFolderList(String folderPath) throws CMSException {
		log.debug("enter CMSServiceJBossImpl.getFolderList()");

		Folder folder = null;
		try {
			Command command = jbossCMS.getCommandFactory()
					.createFolderGetListCommand(folderPath);
			org.jboss.portal.cms.model.Folder cmsFolder = (org.jboss.portal.cms.model.Folder) jbossCMS
					.execute(command);
			folder = copyFolder(cmsFolder);
		} catch (Exception e) {
			CMSException cmse = new CMSException(e);
			log.error(cmse);
			cmse.printStackTrace();
		}

		log.debug("leave CMSServiceJBossImpl.getFolderList()");

		return folder;
	}

	public String makeLiveVersion(String filePath, String language,
			String version) throws CMSException {
		log.debug("enter CMSServiceJBossImpl.makeLiveVersion()");

		String cmsVersion = null;
		try {
			Command command = jbossCMS.getCommandFactory()
					.createMakeLiveVersionCommand(filePath, language, version);
			cmsVersion = (String) jbossCMS.execute(command);
		} catch (Exception e) {
			CMSException cmse = new CMSException(e);
			log.error(cmse);
			cmse.printStackTrace();
			throw cmse;
		}

		log.debug("leave CMSServiceJBossImpl.makeLiveVersion()");

		return cmsVersion;
	}

	public void move(String fromPath, String toPath) throws CMSException {
		log.debug("enter CMSServiceJBossImpl.move()");

		try {
			Command command = jbossCMS.getCommandFactory().createMoveCommand(
					fromPath, toPath);
			jbossCMS.execute(command);
		} catch (Exception e) {
			CMSException cmse = new CMSException(e);
			log.error(cmse);
			cmse.printStackTrace();
			throw cmse;
		}

		log.debug("leave CMSServiceJBossImpl.move()");
	}

	public void rename(String path, String newName) throws CMSException {
		log.debug("enter CMSServiceJBossImpl.rename()");

		try {
			Command command = jbossCMS.getCommandFactory().createRenameCommand(
					path, newName);
			jbossCMS.execute(command);
		} catch (Exception e) {
			CMSException cmse = new CMSException(e);
			log.error(cmse);
			cmse.printStackTrace();
			throw cmse;
		}

		log.debug("leave CMSServiceJBossImpl.rename()");

	}

	public void saveContent(File file) throws CMSException {
		log.debug("enter CMSServiceJBossImpl.saveContent()");

		try {
			org.jboss.portal.cms.model.File cmsFile = this
					.reverseCopyFile(file);

			Command command = jbossCMS.getCommandFactory()
					.createContentSaveCommand(cmsFile);
			jbossCMS.execute(command);
		} catch (Exception e) {
			CMSException cmse = new CMSException(e);
			log.error(cmse);
			cmse.printStackTrace();
			throw cmse;
		}

		log.debug("leave CMSServiceJBossImpl.saveContent()");
	}

	public void saveFile(File file) throws CMSException {
		log.debug("enter CMSServiceJBossImpl.saveFile()");

		try {
			org.jboss.portal.cms.model.File cmsFile = this
					.reverseCopyFile(file);

			Command command = jbossCMS.getCommandFactory()
					.createFileSaveCommand(cmsFile);
			jbossCMS.execute(command);

		} catch (Exception e) {
			CMSException cmse = new CMSException(e);
			log.error(cmse);
			cmse.printStackTrace();
			throw cmse;
		}

		log.debug("leave CMSServiceJBossImpl.saveFile()");
	}

	public void saveFolder(Folder folder) throws CMSException {
		log.debug("enter CMSServiceJBossImpl.saveFolder()");

		try {
			org.jboss.portal.cms.model.Folder cmsFolder = this
					.reverseCopyFolder(folder);

			Command command = jbossCMS.getCommandFactory()
					.createFolderSaveCommand(cmsFolder);
			jbossCMS.execute(command);
		} catch (Exception e) {
			CMSException cmse = new CMSException(e);
			log.error(cmse);
			cmse.printStackTrace();
			throw cmse;
		}

		log.debug("leave CMSServiceJBossImpl.saveFolder()");

	}

	public List<File> search(String query) throws CMSException {
		// TODO Auto-generated method stub
		return null;
	}

	public void storeArchive(String rootPath, byte[] archiveBytes,
			String language) throws CMSException {
		log.debug("enter CMSServiceJBossImpl.storeArchive()");

		try {
			Command command = jbossCMS
					.getCommandFactory()
					.createStoreArchiveCommand(rootPath, archiveBytes, language);
			jbossCMS.execute(command);
		} catch (Exception e) {
			CMSException cmse = new CMSException(e);
			log.error(cmse);
			cmse.printStackTrace();
			throw cmse;
		}

		log.debug("leave CMSServiceJBossImpl.storeArchive()");
	}

	public void updateFile(File file, boolean makeLive) throws CMSException {
		log.debug("enter CMSServiceJBossImpl.updateFile()");

		try {
			org.jboss.portal.cms.model.File cmsFile = reverseCopyFile(file);

			Command command = jbossCMS.getCommandFactory()
					.createUpdateFileCommand(cmsFile, cmsFile.getContent(),
							makeLive);
			jbossCMS.execute(command);
		} catch (Exception e) {
			CMSException cmse = new CMSException(e);
			log.error(cmse);
			cmse.printStackTrace();
			throw cmse;
		}

		log.debug("leave CMSServiceJBossImpl.updateFile()");
	}

	public void updateFile(File file) throws CMSException {
		log.debug("enter CMSServiceJBossImpl.updateFile()");

		try {
			org.jboss.portal.cms.model.File cmsFile = reverseCopyFile(file);

			Command command = jbossCMS.getCommandFactory()
					.createFileUpdateCommand(cmsFile);
			jbossCMS.execute(command);
		} catch (Exception e) {
			CMSException cmse = new CMSException(e);
			log.error(cmse);
			cmse.printStackTrace();
			throw cmse;
		}

		log.debug("leave CMSServiceJBossImpl.updateFile()");
	}

	public void updateFolder(Folder folder) throws CMSException {
		log.debug("enter CMSServiceJBossImpl.updateFolder()");

		try {
			org.jboss.portal.cms.model.Folder cmsFolder = reverseCopyFolder(folder);

			Command command = jbossCMS.getCommandFactory()
					.createFolderUpdateCommand(cmsFolder);
			jbossCMS.execute(command);
		} catch (Exception e) {
			CMSException cmse = new CMSException(e);
			log.error(cmse);
			cmse.printStackTrace();
			throw cmse;
		}

		log.debug("leave CMSServiceJBossImpl.updateFolder()");
	}

	private Folder copyFolder(org.jboss.portal.cms.model.Folder cmsFolder) {
		Folder folder = null;
		if (cmsFolder != null) {
			folder = new Folder();

			List<org.jboss.portal.cms.model.Folder> cmsSubFolders = cmsFolder
					.getFolders();
			if (cmsSubFolders != null) {
				List<Folder> subFolders = new ArrayList<Folder>();
				Folder subFolder;
				for (org.jboss.portal.cms.model.Folder cmsSubFolder : cmsSubFolders) {
					subFolder = copyFolder(cmsSubFolder);
					subFolders.add(subFolder);
				}
				folder.setFolders(subFolders);
			}

			List<org.jboss.portal.cms.model.File> cmsFiles = cmsFolder
					.getFiles();
			if (cmsFiles != null) {
				List<File> files = new ArrayList<File>();
				File file;
				for (org.jboss.portal.cms.model.File cmsFile : cmsFiles) {
					file = copyFile(cmsFile);
					files.add(file);
				}
				folder.setFiles(files);
			}
		}
		return folder;
	}

	private File copyFile(org.jboss.portal.cms.model.File cmsFile) {

		File file = null;

		try {
			if (cmsFile != null) {
				file = new File();
				org.jboss.portal.cms.model.Content cmsContent = cmsFile
						.getContent();
				if (cmsContent != null) {
					Content content = new Content();
					BeanUtils.copyProperties(content, cmsContent);
					file.setContent(content.getLocale(), content);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return file;
	}

	private org.jboss.portal.cms.model.Folder reverseCopyFolder(Folder folder) {
		org.jboss.portal.cms.model.Folder cmsFolder = null;
		if (folder != null) {
			cmsFolder = new FolderImpl();

			List<Folder> subFolders = folder.getFolders();
			if (subFolders != null) {
				List<org.jboss.portal.cms.model.Folder> cmsSubFolders = new ArrayList<org.jboss.portal.cms.model.Folder>();
				org.jboss.portal.cms.model.Folder cmsSubFolder;
				for (Folder subFolder : subFolders) {
					cmsSubFolder = reverseCopyFolder(subFolder);
					cmsSubFolders.add(cmsSubFolder);
				}
				folder.setFolders(subFolders);
			}

			List<File> files = folder.getFiles();
			if (files != null) {
				List<org.jboss.portal.cms.model.File> cmsFiles = new ArrayList<org.jboss.portal.cms.model.File>();
				org.jboss.portal.cms.model.File cmsFile;
				for (File file : files) {
					cmsFile = reverseCopyFile(file);
					cmsFiles.add(cmsFile);
				}
				folder.setFiles(files);
			}
		}
		return cmsFolder;
	}

	private org.jboss.portal.cms.model.File reverseCopyFile(File file) {

		org.jboss.portal.cms.model.File cmsFile = null;

		try {
			if (file != null) {
				cmsFile = new FileImpl();
				cmsFile.setBasePath(file.getBasePath());
				cmsFile.setDescription(file.getDescription());
				Content content = file.getContent();
				if (content != null) {
					org.jboss.portal.cms.model.Content cmsContent = new ContentImpl();
					BeanUtils.copyProperties(cmsContent, content);
					log.debug("content base path: " + content.getBasePath());
					log.debug("cmsContent base path: " + cmsContent.getBasePath());
					cmsFile.setContent(cmsContent.getLocale(), cmsContent);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return cmsFile;
	}
}

// $Id$