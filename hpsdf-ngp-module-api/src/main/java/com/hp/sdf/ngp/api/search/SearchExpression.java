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
package com.hp.sdf.ngp.api.search;


/**
 * Search Expression of searching an entity.
 * 
 * The example of using the expression is here:
 * 
 * ConditionDescriptor descriptor = new
 * AssetDownloadCountConditionDescriptor(...); Condition condition =
 * searchEngine.createCondition(descriptor); condition = condition.and(...);
 * condition = condition.or(...); SearchExpression.addCondition(condition);
 * AssetCatalogService service = ... service.searchAssets(SearchExpression);
 * 
 */
public interface SearchExpression extends java.io.Serializable {

	/**
	 * <pre>
	 * Add condition to expression.
	 * 
	 * Basically, search asset use conditions under 
	 * com.hp.sdf.ngp.search.condition.asset package
	 * 
	 * Once use addCondition,it default use &quot;and&quot; to connect 
	 * previous one.  
	 * 
	 * So if want to use &quot;or&quot;, the condition must be constructed 
	 * manually like this condition.or(condition)
	 * 
	 * example:
	 * addCondition(new AssetAssetPriceAmountCondition(new BigDecimal(20),
	 * 						NumberComparer.GREAT_THAN)
	 * 						.or(new AssetAverageUserRatingCondition(10.0,
	 * 								NumberComparer.EQUAL)));
	 * also, Condition can do complex  &quot;and&quot;, &quot;or&quot;, &quot;not&quot; connection
	 * 
	 * </pre>
	 * 
	 * @param condition
	 *            condition will be search
	 */
	public void addCondition(Condition condition);

	/**
	 * Order of this search
	 * 
	 * @param order
	 *            order of this search
	 */
	public void addOrder(OrderBy orderBy, OrderEnum orderEnum);

	/**
	 * @param first
	 *            first result
	 */
	public void setFirst(int first);

	/**
	 * @param max
	 *            items get each time
	 */
	public void setMax(int max);

}

// $Id$