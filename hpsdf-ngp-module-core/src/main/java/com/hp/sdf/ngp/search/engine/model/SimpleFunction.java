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

import org.hibernate.criterion.Projections;


public class SimpleFunction extends Function implements Serializable {

	private static final long serialVersionUID = -1877371004605609016L;

	public SimpleFunction(FunctionCollection function, String operationObject) {
		if (function == FunctionCollection.AVG) {
			projection = Projections.avg(operationObject);
		} else if (function == FunctionCollection.MAX) {
			projection = Projections.max(operationObject);
		} else if (function == FunctionCollection.MIN) {
			projection = Projections.min(operationObject);
		} else if (function == FunctionCollection.SUM) {
			projection = Projections.sum(operationObject);
		} else if (function == FunctionCollection.COUNT) {
			projection = Projections.countDistinct(operationObject);
		} else if (function == FunctionCollection.GROUP) {
			projection = Projections.groupProperty(operationObject);
		} else if (function == FunctionCollection.PROPERTY){
			projection = Projections.property(operationObject);
		} 
	}

}

// $Id$