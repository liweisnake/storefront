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
package com.hp.sdf.ngp.banner;

import java.io.Serializable;

public class QueryParam implements Serializable {
	private static final long serialVersionUID = 1L;
	private int first;
	private int count;
	private String sort;
	private boolean sortAsc;

	/**
	 * Set to return <tt>count</tt> elements, starting at the <tt>first</tt>
	 * element.
	 * 
	 * @param first
	 *            First element to return.
	 * @param count
	 *            Number of elements to return.
	 */
	public QueryParam(int first, int count) {
		this(first, count, null, true);
	}

	/**
	 * Set to return <tt>count</tt> sorted elements, starting at the
	 * <tt>first</tt> element.
	 * 
	 * @param first
	 *            First element to return.
	 * @param count
	 *            Number of elements to return.
	 * @param sort
	 *            Column to sort on.
	 * @param sortAsc
	 *            Sort ascending or descending.
	 */
	public QueryParam(int first, int count, String sort, boolean sortAsc) {
		this.first = first;
		this.count = count;
		this.sort = sort;
		this.sortAsc = sortAsc;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

	public void setSortAsc(boolean sortAsc) {
		this.sortAsc = sortAsc;
	}

	public int getCount() {
		return count;
	}

	public int getFirst() {
		return first;
	}

	public String getSort() {
		return sort;
	}

	public boolean isSortAsc() {
		return sortAsc;
	}

	public boolean hasSort() {
		return sort != null;
	}

	public void setFirst(int first) {
		this.first = first;
	}

	public void setCount(int count) {
		this.count = count;
	}
}

// $Id$