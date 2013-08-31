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

public class PurchaseHistorySearchCondition implements Serializable {

	private static final long serialVersionUID = -8638474817116237481L;

	private String msisdn;

	private Date paidStartDate;

	private Date paidEndDate;

	private Date completeStartDate;

	private Date completeEndDate;

	public String getMsisdn() {
		return msisdn;
	}

	public void setMsisdn(String msisdn) {
		this.msisdn = msisdn;
	}

	public Date getPaidStartDate() {
		return paidStartDate;
	}

	public void setPaidStartDate(Date paidStartDate) {
		this.paidStartDate = paidStartDate;
	}

	public Date getPaidEndDate() {
		return paidEndDate;
	}

	public void setPaidEndDate(Date paidEndDate) {
		this.paidEndDate = paidEndDate;
	}

	public Date getCompleteStartDate() {
		return completeStartDate;
	}

	public void setCompleteStartDate(Date completeStartDate) {
		this.completeStartDate = completeStartDate;
	}

	public Date getCompleteEndDate() {
		return completeEndDate;
	}

	public void setCompleteEndDate(Date completeEndDate) {
		this.completeEndDate = completeEndDate;
	}

}
