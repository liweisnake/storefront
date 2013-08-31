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

import org.hibernate.criterion.Projection;

public abstract class Function {

	public enum FunctionCollection {
		AVG, MAX, MIN, SUM, COUNT, GROUP, PROPERTY
	}
	
	public enum EavFunctionName {
		eavvaluenumber{
			public String toString(){
				return "eavvaluenumber({0}, {1})";
			}
		}, 
		
		eavvaluedate{
			public String toString(){
				return "eavvaluedate({0}, {1})";
			}
		}, 
		
		eavvaluechar{
			public String toString(){
				return "eavvaluechar({0}, {1})";
			}
		}
	}

	protected Projection projection;

	public Projection getProjection() {
		return projection;
	}

	public void setProjection(Projection projection) {
		this.projection = projection;
	}

	public Function() {
		super();
	}

}

// $Id$