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
package com.hp.sdf.ngp.search.engine.model;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.criterion.CriteriaQuery;
import org.hibernate.criterion.Order;


public class SqlOrder extends Order {
    private String sql;
 
    /**
     * Constructor for Order.
     * @param sql an SQL formula that will be appended to the resulting SQL query
     */
    protected SqlOrder(String sql) {
        super(sql, true);
        this.sql = sql;
    }
 
    public String toString() {
        return sql;
    }
 
    @Override
    public String toSqlString(Criteria criteria, CriteriaQuery criteriaQuery) throws HibernateException {
        return sql;
    }
 
    /**
     * Custom order
     *
     * @param sqlFormula an SQL formula that will be appended to the resulting SQL query
     * @return Order
     */
    public static Order sqlOrder(String sqlOrder) {
        return new SqlOrder(sqlOrder);
    }
}

// $Id$