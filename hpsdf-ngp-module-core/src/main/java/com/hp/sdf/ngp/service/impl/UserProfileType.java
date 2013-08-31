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
package com.hp.sdf.ngp.service.impl;

public enum UserProfileType {

	IDCARD {
		public String toString() {
			return "user.personal-info.idcard";
		}
	},

	GENDER {
		public String toString() {
			return "user.personal-info.gender";
		}
	},

	CELLPHONE {
		public String toString() {
			return "user.personal-info.cellphone";
		}
	},

	COMPANY {
		public String toString() {
			return "user.business-info.company";
		}
	},

	ZIP{
		public String toString() {
			return "user.personal-info.zip";
		}
	},
	
	BIRTHDAY{
		public String toString() {
			return "user.personal-info.birthday";
		}
	},
	
	CREATEDATE{
		public String toString(){
			return "user.personal-info.createdate";
		}
	},
	
	ONLINETIME{
		public String toString(){
			return "user.personal-info.onlineTime";
		}
	},
	
	COUNTRY{
		public String toString(){
			return "user.personal-info.country";
		} 
	},
	
	LANGUAGE{
		public String toString(){
			return "user.personal-info.language";
		}
	}

}

// $Id$