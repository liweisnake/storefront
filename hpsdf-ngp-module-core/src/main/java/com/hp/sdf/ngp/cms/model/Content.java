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
package com.hp.sdf.ngp.cms.model;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.Locale;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.hp.sdf.ngp.common.util.FileUtil;

/**
 * CMS Version implementation.
 * 
 */
public class Content extends CMSObject implements Serializable {

	/** The serialVersionUID */
	private static final long serialVersionUID = -8874394196323703069L;

	protected final static Log log = LogFactory.getLog(Content.class);

	protected byte[] bytes;

	protected String encoding;

	protected boolean isLive;

	protected String versionNumber;

	protected Locale locale;

	protected String mimeType;

	protected long size;

	protected boolean isWaitingForPublishApproval = false;
	protected String approvalProcessId = null;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Locale getLocale() {
		return locale;
	}

	public void setLocale(Locale locale) {
		this.locale = locale;
	}

	public String getVersionNumber() {
		return versionNumber;
	}

	public void setVersionNumber(String versionNumber) {
		this.versionNumber = versionNumber;
	}

	/** @return Returns the content. */
	public InputStream getStream() {
		return new ByteArrayInputStream(bytes);
	}

	/** @return Returns the content. */
	public byte[] getBytes() {
		return bytes;
	}

	public String getContentAsString() {
		if (encoding != null) {
			try {
				return new String(bytes, encoding);
			} catch (UnsupportedEncodingException e) {
			}
		}
		return new String(bytes);
	}

	/**
	 * @param stream
	 *            The content to set.
	 */
	public void setStream(InputStream stream) {
		bytes = FileUtil.getBytes(stream);
	}

	/**
	 * @param bytes
	 *            The content to set.
	 */
	public void setBytes(byte[] bytes) {
		this.bytes = bytes;
	}

	/** @return Returns the encoding. */
	public String getEncoding() {
		return encoding;
	}

	/**
	 * @param encoding
	 *            The encoding to set.
	 */
	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}

	public boolean isLive() {
		return isLive;
	}

	public void setLive(boolean live) {
		isLive = live;
	}

	public long getSize() {
		return this.size;
	}

	public String getMimeType() {
		return mimeType;
	}

	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}

	public void setSize(long size) {
		this.size = size;
	}

	/**
    *
    */
	public boolean isWaitingForPublishApproval() {
		return this.isWaitingForPublishApproval;
	}

	/** @param isWaitingForPublishApproval */
	public void setWaitingForPublishApproval(boolean isWaitingForPublishApproval) {
		this.isWaitingForPublishApproval = isWaitingForPublishApproval;
	}

	/** @return the approvalProcessId */
	public String getApprovalProcessId() {
		return approvalProcessId;
	}

	/**
	 * @param approvalProcessId
	 *            the approvalProcessId to set
	 */
	public void setApprovalProcessId(String approvalProcessId) {
		this.approvalProcessId = approvalProcessId;
	}
}
