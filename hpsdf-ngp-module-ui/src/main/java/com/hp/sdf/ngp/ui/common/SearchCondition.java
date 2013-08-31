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
package com.hp.sdf.ngp.ui.common;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.hp.sdf.ngp.common.util.EnumUtil;

/**
 * SearchCondition.
 * 
 */
public class SearchCondition implements Serializable {

	/**
	 * serialVersionUID.
	 */
	private static final long serialVersionUID = 1L;

	private String keyword = "";

	private Long categoryId;

	private Long platformId;

	private Long statusId;

	private Date beginDate;

	private Date endDate;

	public Date getBeginDate() {
		return beginDate;
	}

	public void setBeginDate(Date beginDate) {
		this.beginDate = beginDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	private AdvanceSearchCondition advanceSearchCondition;

	public AdvanceSearchCondition getAdvanceSearchCondition() {
		return advanceSearchCondition;
	}

	public void setAdvanceSearchCondition(AdvanceSearchCondition advanceSearchCondition) {
		this.advanceSearchCondition = advanceSearchCondition;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}

	public Long getPlatformId() {
		return platformId;
	}

	public void setPlatformId(Long platformId) {
		this.platformId = platformId;
	}

	public Long getStatusId() {
		return statusId;
	}

	public void setStatusId(Long statusId) {
		this.statusId = statusId;
	}

	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("keyword: " + keyword + ", ");
		sb.append("categoryId: " + categoryId + ", ");
		sb.append("platformId: " + platformId + ", ");
		sb.append("statusId: " + statusId + ". ");

		if (advanceSearchCondition != null) {
			sb.append("advanceSearchCondition.uploadTime: " + advanceSearchCondition.getUploadTime() + ", ");
			sb.append("advanceSearchCondition.CompareCondition: " + advanceSearchCondition.getUploadTimeCompareCondition() + ". ");
		}

		return sb.toString();
	}

	public static List<CompareCondition> getAllCompareCondition() {
		return EnumUtil.getAllEnumField(CompareCondition.class);
	}

	public enum CompareCondition {

		BEFORE {
			public String toString() {
				return "before";
			}
		},

		AFTER {
			public String toString() {
				return "after";
			}
		},

		BETWEEN {
			public String toString() {
				return "between";
			}
		}

	}

	public class AdvanceSearchCondition implements Serializable {

		private static final long serialVersionUID = 1L;

		private String uploadTime;

		private CompareCondition uploadTimeCompareCondition;

		public String getUploadTime() {

			return uploadTime;
		}

		public void setUploadTime(String uploadTime) {
			this.uploadTime = uploadTime;
		}

		public CompareCondition getUploadTimeCompareCondition() {
			return uploadTimeCompareCondition;
		}

		public void setUploadTimeCompareCondition(CompareCondition uploadTimeCompareCondition) {
			this.uploadTimeCompareCondition = uploadTimeCompareCondition;
		}

	}
}
