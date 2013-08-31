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

public class SearchMetaInfo implements Serializable {

	private static final long serialVersionUID = 3325556761931312609L;
	
	public enum SearchByType {

		ASSETCATEGORYRELATION_CATEGORY_ID, 
		ASSETTAGRELATION_TAG_ID, 
		PLATFORM_ID, 
		STATUS_ID;
	};

	private SearchByType searchBy;

	private String id;

	private String name;

	private String value;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public SearchMetaInfo() {
		super();
	}

	public SearchMetaInfo(SearchByType searchBy, String id) {
		super();
		this.searchBy = searchBy;
		this.id = id;
	}

	public SearchMetaInfo(SearchByType searchBy, String id, String name) {
		super();
		this.searchBy = searchBy;
		this.id = id;
		this.name = name;
	}

	public SearchByType getSearchBy() {
		return searchBy;
	}

	public void setSearchBy(SearchByType searchBy) {
		this.searchBy = searchBy;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

}