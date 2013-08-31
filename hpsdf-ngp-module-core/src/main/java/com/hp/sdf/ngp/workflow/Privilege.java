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
package com.hp.sdf.ngp.workflow;

public interface Privilege {

	// Every user will has this privilege without any configurations
	public String DEFAULT = "default";

	public String APISUBSCRIBE = "apisubscribe";

	public String VIEWMYAPPLICATION = "viewmyapplication";

	public String VIEWMYTESTING = "viewmytesting";

	public String VIEWMYAPPAPPAPPROVAL = "viewmyappapproval";

	public String VIEWMYPROMOTIONAPPROVAL = "viewmypromotionapproval";

	public String VIEWMYEWALLET = "viewmyewallet";

	public String VIEWSHOPPINGCART = "viewshoppingcart";

	public String VIEWMYSUBSCRIPTION = "viewmysubscription";

	public String SEARCHBINSTATUS = "searchbinstatus";

	public String MANAGEAPP = "manageapp";
	
	// oam content privileges
	public String CONTENTSEARCH = "contentsearch";
	public String CONTENTLIST = "contentlist";
	public String CONTENTDOWNLOAD = "contentdownload";
	public String CONTENTBATCH = "contentbatch";
	public String CONTENTUPDATE = "contentupdate";
	public String CONTENTDELETE = "contentdelete";
	public String NEWCONTENT = "newcontent";
	public String UPDATECONTENT = "updatecontent";
	public String NOTUPDATECONTENT = "notupdatecontent";
	public String STATUSHISTORY = "statushistory";
	public String NOTSTATUSHISTORY = "notstatushistory";
	public String RECOMMENDSEARCH = "recommendsearch";
	public String DEFAULTSETTING = "defaultsetting";
	public String NEWPROMOTIONDUEDATE = "newpromotionduedate";
	public String DEFAULTRATE = "defaultrate";

	// oam category privileges
	public String NEWCATEGORY = "newcategory";
	public String DELETECATEGORY = "deletecategory";
	public String EDITCATEGORY = "editcategory";
	public String VIEWCATEGORYLIST = "viewcategorylist";

	// oam comments privileges
	public String VIEWCENSOREDWORDLIST = "viewcensoredwordlist";
	public String DELETECENSOREDWORD = "deletecensoredword";
	public String NEWCENSOREDWORD = "newcensoredword";
	public String EDITCENSOREDWORD = "editcensoredword";
	public String SEARCHCOMMENTS = "searchcomments";
	public String DELETECOMMENTS = "deletecomments";
	public String VIEWCOMMENTSLIST = "viewcommentslist";

	// oam client application privileges
	public String VIEWCLIENTAPPLICATION = "viewclientapplication";
	public String UPDATECLIENTAPPLICATION = "updateclientapplication";

	// oam handset privileges
	public String SEARCHHANDSET = "searchhandset";
	public String DELETEHANDSET = "deletehandset";
	public String VIEWHANDSETLIST = "viewhandsetlist";
	public String EDITHANDSET = "edithandset";
	public String NEWHANDSET = "newhandset";

	// oam account privileges
	public String SEARCHACCOUNT = "searchaccount";
	public String DELETEACCOUNT = "deleteaccount";
	public String VIEWACCOUNTLIST = "viewaccountlist";
	public String EDITACCOUNT = "editaccount";
	public String NEWACCOUNT = "newaccount";

	// oam purchase history privileges
	public String SEARCHPURCHASEHISTORY = "searchpurchasehistory";
	public String VIEWPURCHASEHISTORY = "viewpurchasehistory";

	static class Construct {
		public static String OwnerPrivilege(String usrId) {
			return "owner[" + usrId + "]";
		}

		public static String ApplicationPrivilege(String status, String accessType, String userRoleName) {
			return status + "_" + accessType + "_" + userRoleName;
		}
	}

}

// $Id$