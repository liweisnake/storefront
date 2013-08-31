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
package com.hp.sdf.ngp.administration.impl;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;
import org.springframework.stereotype.Component;

import com.hp.sdf.ngp.administration.AssetEntry;
import com.hp.sdf.ngp.administration.AssetEntryDecoder;
import com.hp.sdf.ngp.administration.BinaryContent;
import com.hp.sdf.ngp.common.annotation.Value;
import com.hp.sdf.ngp.common.exception.UnzipException;

@Component
public class ZipAssetEntryDecoder implements AssetEntryDecoder {

	private static final Log log = LogFactory
			.getLog(ZipAssetEntryDecoder.class);

	private String batchProcessFolder = "/tmp";

	public String getBatchProcessFolder() {
		return batchProcessFolder;
	}

	@Value("batchProcessFolder")
	public void setBatchProcessFolder(String batchProcessFolder) {
		this.batchProcessFolder = batchProcessFolder;
	}

	private File tmpFile;

	private File dirFile;

	public void clean() throws IOException {
		if (tmpFile != null && tmpFile.exists())
			FileUtils.forceDelete(tmpFile);
		if (dirFile != null && dirFile.exists())
			FileUtils.forceDelete(dirFile);
	}

	public List<AssetEntry> decode(InputStream stream) throws IOException,
			UnzipException {
		int count = 0;
		File tmpFile;
		File dir = new File(batchProcessFolder);
		while ((tmpFile = new File(dir, ++count + ".zip")).exists()
				|| new File(dir, ++count + "").exists())
			;

		FileUtils.touch(tmpFile);
		OutputStream output = FileUtils.openOutputStream(tmpFile);
		IOUtils.copy(stream, output);
		IOUtils.closeQuietly(stream);
		IOUtils.closeQuietly(output);
		log.debug("saveZipFile>>write file success. Filename="
				+ tmpFile.getAbsolutePath());
		return decode(tmpFile, dir);

	}

	protected List<AssetEntry> decode(File src, File dest)
			throws UnzipException {
		Map<String, AssetEntry> map = new HashMap<String, AssetEntry>();
		try {
			dirFile = new File(dest, FilenameUtils.getBaseName(src.getName()));
			if (dirFile.exists()) {
				FileUtils.forceDelete(dirFile);
			}
			dirFile.mkdir();
			ZipFile zipFile = new ZipFile(src);
			Enumeration e = zipFile.getEntries();
			ZipEntry zipEntry = null;

			while (e.hasMoreElements()) {
				zipEntry = (ZipEntry) e.nextElement();
				log.debug("decode>>unziping " + zipEntry.getName());

				if (zipEntry.isDirectory()) {
					if (map.get(zipEntry.getName()) == null) {
						AssetEntry entry = new AssetEntry();
						entry.setName(zipEntry.getName());
						log.debug("decode>>Found dir. create entry with name "
								+ entry.getName());
						map.put(entry.getName(), entry);
					}
				} else {
					String path = FilenameUtils.getPath(zipEntry.getName());
					AssetEntry entry = map.get(path);
					if (entry == null) {
						entry = new AssetEntry();
						entry.setName(path);
						log.debug("decode>>Found file. create entry with name "
								+ entry.getName());
						map.put(path, entry);
					}

					if ("xml".equals(FilenameUtils.getExtension(
							zipEntry.getName()).toLowerCase())) {
						entry.setXml(IOUtils.toString(zipFile
								.getInputStream(zipEntry)));
					} else {
						BinaryContent content = new BinaryContent();
						content.setName(FilenameUtils.getName(zipEntry
								.getName()));
						content.setContent(IOUtils.toByteArray(zipFile
								.getInputStream(zipEntry)));
						log.debug("decode>>create binary content " + content);
						entry.put(content.getName(), content);
					}
				}
			}

			zipFile.close();

		} catch (Exception e) {
			log.debug("Unzip error!", e);
			throw new UnzipException(e);
		}
		return new ArrayList<AssetEntry>(map.values());
	}
	// public static void main(String[] args) throws UnzipException {
	// ZipAssetEntryDecoder zed = new ZipAssetEntryDecoder();
	// File src = new File("c:/batchUploadtest.zip");
	// File dest = new File("c:/test");
	// zed.decode(src, dest);
	//
	// // System.out.println(FilenameUtils.getPath("batchUploadtest/app2/"));
	// }

}

// $Id$