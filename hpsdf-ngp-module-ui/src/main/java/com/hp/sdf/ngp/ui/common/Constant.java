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

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.behavior.IBehavior;
import org.apache.wicket.model.Model;

public final class Constant {

	public final static String SUFFIX = ";";
	public final static String CSV_SUFFIX = ",";

	/**
	 * dollars
	 */
	public final static String CURRENCY_DOLLARS = "USD";

	/**
	 * date pattern
	 */
	public final static String DATE_PATTERN = "yyyy-MM-dd";
	public final static String FULL_DATE_PATTERN = "yyyy-MM-dd HH:mm:ss";
	public final static String DATE_PATTERN_NO_SUFFIX = "yyyyMMddHHmmss";

	public final static String BEGIN_DATE = " 00:00:00";
	public final static String END_DATE = " 23:59:59";

	/**
	 * page title name
	 */
	public final static String CONTENT_MANAGEMENT = "Binary Versions List";
	public final static String CONTENT_DETAIL = "Binary Version Detail";
	public final static String CONTENT_NEW = "New Binary Version Upload Detail";
	public final static String RECOMMENDED_CONTENT = "Recommended Binary Versions List";
	public final static String DEFAULT_SETTING = "Default Setting";
	public final static String STATUS_CHANGE_HISTORY = "Binary Version Status Change History";
	public final static String SHOW_PICTURE = "Show Picture";

	/**
	 * default per page is 50
	 */
	public final static int DEFALUT_PER_PAGE_COUNT = 2;

	/**
	 * select option
	 */
	public enum SELECT_OPTION {
		VALUE {
			@Override
			public String toString() {
				return "value";
			}
		},
		KEY {
			@Override
			public String toString() {
				return "key";
			}
		}
	}

	public enum CONTROL_TYPE {
		CheckBox, TextField, DropDownChoice, TextArea, Radio
	}

	public enum DATA_TYPE {
		none, status, platform, category
	}

	public enum PROMPT {
		prompt {
			@Override
			public String toString() {
				return "Prompt";
			}
		},
		warning {
			@Override
			public String toString() {
				return "Warning";
			}
		},
		error {
			@Override
			public String toString() {
				return "Error";
			}
		}
	}

	public static final Long RECOMMEND_ORDER = 400L;

	public static final IBehavior STYLE_HIDE = new AttributeModifier("style", true, new Model<String>("visibility:hidden;"));
	public static final IBehavior STYLE_SHOW = new AttributeModifier("style", true, new Model<String>("visibility:visible;"));

	public enum ENABLE {
		enable {
			@Override
			public String toString() {
				return "enable";
			}
		},
		disenable {
			@Override
			public String toString() {
				return "disenable";
			}
		}
	}

	public enum SYSTEM_CONFIG {
		newPromotionDueDate {
			@Override
			public String toString() {
				return "NEWASSETDURATION";
			}
		},
		defaultCommissionRate {
			@Override
			public String toString() {
				return "default.commission.rate.*";
			}
		}
	}

	public enum PICTURE_TYPE {
		thumbnail, thumbnailMiddle, thumbnailBig;
	}

	public enum PANEL_TYPE {
		ContentManagementPanel, RecommendedManagementPanel, CategoryListPanel, PlatformListPanel, CensoredWordListPanel, CommentsSearchPanel, StoreClientEditPanel, AccountSearchPanel
	}

	public static int EXCEL_MAX_ROWS = 65535;

	public static String ASSET_SOURCE = "SBM";

	public static String STRING_TRUE = "true";

	public static String STATUS_PUBLISHED = "published";
	public static String STATUS_KILL_SWITCH = "kill_switch";

	public static int MAX_TAG_COUNT = 10;

	public static String TAG_SUFFIX = ",";

	public static String SBMPID = "default.pid.sbm";

	public final static String PUBLISH_FLG_DEFAULT = "01";

	public final static int NEW_PROMOTION_DUE_DATE_DEFAULT = 14;

	public enum VALID_TYPE {
		isNumber, isEnglish, isAll, isDbc
	}

	public static String UTF8 = "UTF-8";
}