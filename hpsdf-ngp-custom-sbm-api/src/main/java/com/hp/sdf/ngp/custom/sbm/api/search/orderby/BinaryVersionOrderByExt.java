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
package com.hp.sdf.ngp.custom.sbm.api.search.orderby;

import com.hp.sdf.ngp.api.search.OrderBy;

public enum BinaryVersionOrderByExt implements OrderBy{
	
	/**
	 * Sort condition on ParentAssetVersionSummary.
	 */
	VERSIONSUMMARY_DOWNLOADTIME {
		
	},
	
	/**
	 * Sort condition on ParentAssetVersionSummary
	 */
	VERSIONSUMMARY_PUBLISHDATE {
		
	},
	
	/**
	 * Sort condition on ParentAssetVersionSummary.
	 */
	VERSIONSUMMARY_SALESTARTDATE {
		
	},
	
	/**
	 * Sort condition on ParentAssetVersionSummary.
	 */
	VERSIONSUMMARY_SALEENDDATE {
		
	},
	
	/**
	 * Sort condition on ParentAssetVersionSummary.
	 */
	VERSIONSUMMARY_LOWESTPRICE {
		
	},
	
	/**
	 * Sort condition on ParentAssetVersionSummary.
	 */
	VERSIONSUMMARY_RECOMMENDORDER {
		
	},

	/**
	 * A sort order of EAV model when searching binary version.
	 */
	SERIAL {
		
	},
	
	/**
	 * A sort order of EAV model when searching binary version.
	 */
	ASSETPROVIDERPRIORITY {
		
	},
	
	/**
	 * A sort order of EAV model when searching binary version.
	 */
	GETIMAGEDATE {
		
	},
	
	PUBLISHEPLANDATE {
		
	}
}

// $Id$