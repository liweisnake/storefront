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
package com.hp.sdf.ngp.model;

import java.io.Serializable;
import java.util.Calendar;


/**
 * 
 *
 */
public class VerificationCode  implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4793027204795958341L;

	private String code;

	private Calendar createDate;

	// minute
	private int expireTime;

	public VerificationCode(String code, int expireTime) {
		super();
		this.code = code;
		this.expireTime = expireTime;
		this.createDate = Calendar.getInstance();
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Calendar getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Calendar createDate) {
		this.createDate = createDate;
	}

	public int getExpireTime() {
		return expireTime;
	}

	public void setExpireTime(int expireTime) {
		this.expireTime = expireTime;
	}

	public boolean isExpire() {
		Calendar now = Calendar.getInstance();
		Calendar tmp = (Calendar) createDate.clone();
		tmp.add(Calendar.MINUTE, expireTime);
		return tmp.after(now);
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof VerificationCode)
			return ((VerificationCode) o).getCode().equals(code);
		else if(o instanceof String){
			return ((String)o).equals(code);
		}
		return false;
	}

}

// $Id$