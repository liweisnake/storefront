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
package com.hp.sdf.ngp.search.order.binaryversionext;

import org.springframework.stereotype.Component;

import com.hp.sdf.ngp.api.search.OrderBy;
import com.hp.sdf.ngp.api.search.OrderEnum;
import com.hp.sdf.ngp.custom.sbm.api.search.orderby.BinaryVersionOrderByExt;
import com.hp.sdf.ngp.search.engine.SearchExpressionImpl;
import com.hp.sdf.ngp.search.order.BaseOrderExecutor;
@Component
public class VersionSummaryPublishDate extends BaseOrderExecutor {

	public void execute(OrderBy orderBy, OrderEnum orderEnum,
			SearchExpressionImpl expression) {
		expression.addOrder( orderEnum, "publishDate");
	}

	@Override
	protected OrderBy getOrderBy() {

		return BinaryVersionOrderByExt.VERSIONSUMMARY_PUBLISHDATE;
	}
}

// $Id$