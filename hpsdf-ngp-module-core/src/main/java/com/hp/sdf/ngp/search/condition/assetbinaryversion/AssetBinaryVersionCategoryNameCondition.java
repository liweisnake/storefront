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
package com.hp.sdf.ngp.search.condition.assetbinaryversion;

import com.hp.sdf.ngp.search.engine.model.BaseCondition;
import com.hp.sdf.ngp.search.engine.model.JoinPropertyCondition;
import com.hp.sdf.ngp.search.engine.model.NumberPropertyCondition;
import com.hp.sdf.ngp.search.engine.model.ObjectPropertyCondition;
import com.hp.sdf.ngp.search.engine.model.StringPropertyCondition;

@SuppressWarnings("serial")
public class AssetBinaryVersionCategoryNameCondition extends
		ObjectPropertyCondition {

	public AssetBinaryVersionCategoryNameCondition(String value,
			StringComparer matchType, boolean ignorecase, boolean isWildCard) {
		//TDO. by levi. is need currentversion.
		this.conditionType = ConditionType.join;
		JoinPropertyCondition joinCondition1 = new JoinPropertyCondition(
				"asset.assetCategoryRelations.category", new StringPropertyCondition(
						"category.name", value, matchType, ignorecase,
						isWildCard), false);
//		JoinPropertyCondition joinCondition2 = new JoinPropertyCondition(
//				"assetCategoryRelations.category", new StringPropertyCondition(
//						"category.name", value, matchType, ignorecase,
//						isWildCard), false);
		condition = (BaseCondition) (joinCondition1
				.and(new NumberPropertyCondition(
						"assetCategoryRelations.binaryVersion.id", 0,
						NumberComparer.NULL)).or(joinCondition1.and(
				new NumberPropertyCondition(
						"assetCategoryRelations.binaryVersion.id", 0,
						NumberComparer.NOT_NULL))));
	}
}

// $Id$