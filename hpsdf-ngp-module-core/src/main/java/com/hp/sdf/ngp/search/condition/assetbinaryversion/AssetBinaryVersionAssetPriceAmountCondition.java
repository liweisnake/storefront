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

import java.math.BigDecimal;

import com.hp.sdf.ngp.api.search.Condition.ConditionType;
import com.hp.sdf.ngp.api.search.Condition.NumberComparer;
import com.hp.sdf.ngp.search.engine.model.BaseCondition;
import com.hp.sdf.ngp.search.engine.model.JoinPropertyCondition;
import com.hp.sdf.ngp.search.engine.model.NumberPropertyCondition;
import com.hp.sdf.ngp.search.engine.model.ObjectPropertyCondition;
import com.hp.sdf.ngp.search.engine.model.PropertyEqualCondition;

@SuppressWarnings("serial")
public class AssetBinaryVersionAssetPriceAmountCondition extends
		ObjectPropertyCondition {

	public AssetBinaryVersionAssetPriceAmountCondition(BigDecimal value,
			NumberComparer matchType) {
//		super("assetPrices", new NumberPropertyCondition("assetPrices.amount",
//				value, matchType));
		this.conditionType = ConditionType.join;
		JoinPropertyCondition joinCondition = new JoinPropertyCondition(
				"assetPrices", new NumberPropertyCondition(
						"assetPrices.amount", value, matchType), false);
		condition = (BaseCondition) (joinCondition
				.and(new NumberPropertyCondition(
						"assetPrices.binaryVersion.id", 0,
						NumberComparer.NULL)).or(joinCondition.and(
				new NumberPropertyCondition(
						"assetPrices.binaryVersion.id", 0,
						NumberComparer.NOT_NULL)).and(
				new PropertyEqualCondition("currentVersionId",
						"binaryVersions.id")).and(
				new PropertyEqualCondition(
						"assetPrices.binaryVersion.id",
						"binaryVersions.id", false))));
	}
}

// $Id$