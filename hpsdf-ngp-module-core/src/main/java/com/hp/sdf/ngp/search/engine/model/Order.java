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

import java.io.Serializable;

import com.hp.sdf.ngp.api.search.OrderEnum;

public class Order implements Serializable {

	private static final long serialVersionUID = 539367390623790123L;

	protected String orderBy;

	protected OrderEnum order = OrderEnum.ASC;
	
	public Order(){
		
	}

	public Order(String orderBy) {
		super();
		this.orderBy = orderBy;
	}

	public Order(String orderBy, OrderEnum order) {
		super();
		this.orderBy = orderBy;
		this.order = order;
	}
	
	public String getOrderBy() {
		return orderBy;
	}

	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}

	public OrderEnum getOrder() {
		return order;
	}

	public void setOrder(OrderEnum order) {
		this.order = order;
	}

}

// $Id$