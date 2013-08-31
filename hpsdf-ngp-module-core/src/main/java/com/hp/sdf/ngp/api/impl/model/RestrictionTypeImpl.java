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
package com.hp.sdf.ngp.api.impl.model;

import com.hp.sdf.ngp.api.model.RestrictionType;
import com.hp.sdf.ngp.model.RestrictedType;

public class RestrictionTypeImpl implements RestrictionType {
	
//	private final static Log log = LogFactory.getLog(RestrictionTypeImpl.class);
	
	public RestrictedType getRestrictedType() {
		if(null == restrictedType){
			restrictedType = new RestrictedType();
		}
		return restrictedType;
	}

	private RestrictedType restrictedType;
	
	public RestrictionTypeImpl(){
		restrictedType = new RestrictedType();
	}
	
	public RestrictionTypeImpl(RestrictedType restrictedType){
		this.restrictedType = restrictedType;
		if (null != this.restrictedType) {
			this.restrictedType.getType();//load information to avoid the lazy load
		}
	}

	public Long getId() {
		Long id = restrictedType.getId();
		return id;
	}

	public String getType() {
		return restrictedType.getType();
	}

	public void setId(Long id) {
//		log.debug("id="+id);
		restrictedType.setId(id);
	}

	public void setType(String type) {
//		log.debug("type="+type);
		restrictedType.setType(type);
	}

	@Override
	public String toString(){
		return "RestrictionType[id="+getId()+",type="+getType()+"]";
	}
}

// $Id$