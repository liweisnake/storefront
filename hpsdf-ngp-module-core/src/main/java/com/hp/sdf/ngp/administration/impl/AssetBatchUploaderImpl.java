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

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

import com.hp.sdf.ngp.administration.AssetBatchUploader;
import com.hp.sdf.ngp.administration.AssetCreator;
import com.hp.sdf.ngp.administration.AssetDescriptorTransformer;
import com.hp.sdf.ngp.administration.AssetEntry;
import com.hp.sdf.ngp.administration.AssetEntryDecoder;
import com.hp.sdf.ngp.administration.descriptor.AssetDescriptor;
import com.hp.sdf.ngp.common.exception.BatchUploadFailureException;
import com.hp.sdf.ngp.common.exception.UnzipException;

@Component
public class AssetBatchUploaderImpl implements AssetBatchUploader {

	private static final Log log = LogFactory
			.getLog(AssetBatchUploaderImpl.class);

	@Resource
	AssetDescriptorTransformer transformer;

	@Resource
	AssetEntryDecoder decoder;

	@Resource
	AssetCreator creator;

	public void batchUpload(InputStream stream)
			throws BatchUploadFailureException {
		log.debug("batchUpload>>start upload.");
		try {
			log.debug("batchUpload>>decode the stream.");
			List<AssetEntry> assetEntries = decoder.decode(stream);

			for (AssetEntry entry : assetEntries) {
				if (StringUtils.isBlank(entry.getXml()))
					continue;
				log.debug("batchUpload>>process entry " + entry.getName());
				AssetDescriptor assetDescriptor = transformer.load(entry
						.getXml());
				log.debug("batchUpload>>going to create asset");
				creator.createAsset(assetDescriptor, entry);
				
				decoder.clean();
			}

		} catch (UnzipException ue) {
			throw new BatchUploadFailureException(
					"Unzip file error. Check xml config file.", ue);
		} catch (IOException ioe) {
			throw new BatchUploadFailureException(
					"IO error. System read or write error.", ioe);
		} catch (BatchUploadFailureException bufe) {
			throw bufe;
		} catch (Exception e) {
			throw new BatchUploadFailureException(
					"Error. System cannot process this command.", e);
		}

	}
}

// $Id$