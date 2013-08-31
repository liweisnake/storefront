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
package com.hp.sdf.ngp.search.condition.eav;

import com.hp.sdf.ngp.api.comon.EntityRelationShipType;
import com.hp.sdf.ngp.api.comon.EntityType;
import com.hp.sdf.ngp.search.engine.model.BaseCondition;
import com.hp.sdf.ngp.search.engine.model.EavCondition;
import com.hp.sdf.ngp.search.engine.model.StringPropertyCondition;

public class EavStringCondition extends EavCondition {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2015850673766937430L;

	private String value;

	private StringComparer matchType;

	private boolean ignorecase;

	private boolean isWildCard;
	
	public  String getEavJoinName()
	{
		return "attributeValueChars";
	}
	public BaseCondition createEavPropertyCondition(String eavPropertyName)
	{
		return new StringPropertyCondition(eavPropertyName, value, matchType, ignorecase, isWildCard);
	}
	public EavStringCondition(EntityType entityType, String attributeName, String value, StringComparer matchType, boolean ignorecase,
			boolean isWildCard) {
		this(null, entityType, attributeName, value, matchType, ignorecase, isWildCard);
		
	}

	public EavStringCondition(EntityRelationShipType entityRelationShipType, EntityType entityType, String attributeName, String value,
			StringComparer matchType, boolean ignorecase, boolean isWildCard) {
		super(entityRelationShipType, entityType, attributeName);
		this.value = value;
		this.matchType = matchType;
		this.ignorecase = ignorecase;
		this.isWildCard = isWildCard;		
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public StringComparer getMatchType() {
		return matchType;
	}

	public void setMatchType(StringComparer matchType) {
		this.matchType = matchType;
	}

	public boolean isIgnorecase() {
		return ignorecase;
	}

	public void setIgnorecase(boolean ignorecase) {
		this.ignorecase = ignorecase;
	}

	public boolean isWildCard() {
		return isWildCard;
	}

	public void setWildCard(boolean isWildCard) {
		this.isWildCard = isWildCard;
	}

	

}

// $Id$