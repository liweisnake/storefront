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
package com.hp.sdf.ngp.dao;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.hp.sdf.ngp.api.search.SearchExpression;
import com.hp.sdf.ngp.common.exception.ConditionNoMatchedException;
import com.hp.sdf.ngp.search.engine.SearchExpressionImpl;

public abstract class JpaDao<PK extends Serializable, T> extends BaseDao<PK, T> {

	private final static Log log = LogFactory.getLog(JpaDao.class);

	public List<T> search(SearchExpression expression) {
		if (expression == null) {
			return getAll(0, Integer.MAX_VALUE);
		}

		if (expression instanceof SearchExpressionImpl) {

			SearchExpressionImpl impl = (SearchExpressionImpl) expression;
			impl.setDao(this);
			try {
				return find(impl);
			} catch (ConditionNoMatchedException e) {
				log.error("Search condition not match: ", e);
			}
		}
		return null;
	}

	public int searchCount(SearchExpression expression) {
		if (expression == null) {
			return getAllCount();
		}
		if (expression instanceof SearchExpressionImpl) {

			SearchExpressionImpl impl = (SearchExpressionImpl) expression;
			impl.setDao(this);
			try {
				return findCount(impl);
			} catch (ConditionNoMatchedException e) {
				log.error("Search condition not match: ", e);
			}
		}
		return 0;
	}

}

// $Id$