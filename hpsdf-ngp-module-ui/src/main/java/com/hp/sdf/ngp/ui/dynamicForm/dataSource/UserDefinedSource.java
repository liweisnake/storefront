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
package com.hp.sdf.ngp.ui.dynamicForm.dataSource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.hp.sdf.ngp.ui.dynamicForm.model.Pair;

/**
 * Retrieve data from user provided definition as {Id<Long> & value<String>}<br>
 * sample:
 */
@SuppressWarnings("unchecked")
public class UserDefinedSource extends VirtualSource implements GetIdListInterface, GetValueMapInterface {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3906529314272607693L;

	private Pair[] pairs = new Pair[] {};//

	private Log log = LogFactory.getLog(UserDefinedSource.class);

	public UserDefinedSource() {
		super();
	}

	//according to Pair definition
	public String getKeyName() {
		return "id";
	}

	//according to Pair definition
	public String getValueName() {
		return "value";
	}

	public Pair[] getPairs() {
		return pairs;
	}

	public void setPairs(Pair[] pairs) {
		this.pairs = pairs;
	}

	public UserDefinedSource(String holderName, String idMethodName, String valueMethodName) {
		super();
	}

	@Override
	public List<?> getDataList() {
		List dataList = new ArrayList();

		for (Pair p : pairs) {
			dataList.add(p);
		}
		return dataList;
	}

	public List<?> getIdList() {
		List dataList = new ArrayList();

		for (Pair p : pairs) {
			dataList.add(p.getId());
		}
		return dataList;
	}

	public Map<?, ?> getValueMap() {

		Map dataMap = new HashMap();

		for (Pair p : pairs) {
			dataMap.put(p.getId(), p.getValue());
		}
		return dataMap;
	}
}

// $Id$