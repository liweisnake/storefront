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
package com.hp.sdf.ngp.common.constant;

import java.text.DecimalFormat;

import org.springframework.stereotype.Component;

import com.hp.sdf.ngp.common.annotation.Value;

/** 
 * The class assembles the asset/application related constants. 
 */
@Component
public class AssetConstants {

	public static final DecimalFormat priceFormat = new DecimalFormat("#0.00");
	
	public static final String ASSET_SOURCE_STOREFRONT = "storefront";
	
	private String unit;
	
	private  String uriPrefix;
	
	public static String URIPREFIX;
	
	public String getUnit() {
		return unit;
	}

	public String getUriPrefix() {
		return uriPrefix;
	}
	@Value("file.path.prefix")
	public void setUriPrefix(String uriPrefix) {
		this.uriPrefix = uriPrefix;
		AssetConstants.URIPREFIX=uriPrefix;
	}

	

	@Value("app.price.unit")
	public void setUnit(String unit) {
		this.unit = unit;
	}
	
	
	
}

// $Id$