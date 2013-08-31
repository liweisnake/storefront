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
package com.hp.sdf.ngp.ui.provider;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import com.hp.sdf.ngp.api.search.OrderEnum;
import com.hp.sdf.ngp.api.search.SearchExpression;
import com.hp.sdf.ngp.api.search.Condition.DateComparer;
import com.hp.sdf.ngp.api.search.Condition.NumberComparer;
import com.hp.sdf.ngp.api.search.Condition.StringComparer;
import com.hp.sdf.ngp.api.search.orderby.BinaryVersionOrderBy;
import com.hp.sdf.ngp.model.AssetBinaryVersion;
import com.hp.sdf.ngp.model.Category;
import com.hp.sdf.ngp.search.condition.asset.AssetExternalIdCondition;
import com.hp.sdf.ngp.search.condition.assetbinaryversion.AssetBinaryVersionAssetIdCondition;
import com.hp.sdf.ngp.search.condition.assetbinaryversion.AssetBinaryVersionAssetProviderExternalIdCondition;
import com.hp.sdf.ngp.search.condition.assetbinaryversion.AssetBinaryVersionCategoryIdCondition;
import com.hp.sdf.ngp.search.condition.assetbinaryversion.AssetBinaryVersionCategoryNameCondition;
import com.hp.sdf.ngp.search.condition.assetbinaryversion.AssetBinaryVersionExternalIdCondition;
import com.hp.sdf.ngp.search.condition.assetbinaryversion.AssetBinaryVersionNameCondition;
import com.hp.sdf.ngp.search.condition.assetbinaryversion.AssetBinaryVersionOwnerAssetParentIdCondition;
import com.hp.sdf.ngp.search.condition.assetbinaryversion.AssetBinaryVersionProviderCommissionRateCondition;
import com.hp.sdf.ngp.search.condition.assetbinaryversion.AssetBinaryVersionProviderNameCondition;
import com.hp.sdf.ngp.search.condition.assetbinaryversion.AssetBinaryVersionRecommendDueDateCondition;
import com.hp.sdf.ngp.search.condition.assetbinaryversion.AssetBinaryVersionRecommendStartDateCondition;
import com.hp.sdf.ngp.search.condition.assetbinaryversion.AssetBinaryVersionStatusIdCondition;
import com.hp.sdf.ngp.search.engine.SearchExpressionImpl;
import com.hp.sdf.ngp.service.ApplicationService;
import com.hp.sdf.ngp.ui.common.ContentManagementCondition;
import com.hp.sdf.ngp.ui.common.Tools;

public class RecommendContentManagementDataProvider implements IDataProvider<AssetBinaryVersion> {

	private static final long serialVersionUID = -6829478296346665192L;

	private static Log log = LogFactory.getLog(RecommendContentManagementDataProvider.class);

	private ApplicationService applicationService;

	private ContentManagementCondition condition;

	public RecommendContentManagementDataProvider(ApplicationService applicationService, ContentManagementCondition condition) {
		this(applicationService);
		this.condition = condition;
	}

	public RecommendContentManagementDataProvider(ApplicationService applicationService) {
		this.applicationService = applicationService;
	}

	public Iterator<AssetBinaryVersion> iterator(int first, int count) {

		if (null != this.condition && this.condition.getFirst())
			return null;

		SearchExpression searchExpression = genCondition();
		searchExpression.setFirst(first);
		searchExpression.setMax(count);
		searchExpression.addOrder(BinaryVersionOrderBy.RECOMMENDORDER, OrderEnum.ASC);

		List<AssetBinaryVersion> list = applicationService.searchAssetBinary(searchExpression);

		if (list != null)
			return list.iterator();

		return null;
	}

	public IModel<AssetBinaryVersion> model(AssetBinaryVersion object) {
		return new Model<AssetBinaryVersion>(object);
	}

	public int size() {

		if (null != this.condition && this.condition.getFirst())
			return 0;

		SearchExpression searchExpression = genCondition();
		searchExpression.setFirst(0);
		searchExpression.setMax(Integer.MAX_VALUE);
		return (int) applicationService.searchAssetBinaryPageCount(searchExpression);
	}

	public void detach() {
	}

	private SearchExpression genCondition() {
		SearchExpression searchExpression = new SearchExpressionImpl();

		if (null == this.condition)
			this.condition = new ContentManagementCondition();

		String assetExternalId = condition.getAssetExternalId();
		if (assetExternalId != null) {
			// searchExpression.addCondition(new AssetBinaryVersionAssetProviderIdCondition(providerId, NumberComparer.EQUAL));
			searchExpression.addCondition(new AssetExternalIdCondition(assetExternalId, StringComparer.LIKE, true, true));
			log.debug("assetExternalId : " + assetExternalId);
		}
		
		String binaryVersionExternalId = condition.getBinaryVersionExternalId();
		if (binaryVersionExternalId != null) {
			// searchExpression.addCondition(new AssetBinaryVersionAssetProviderIdCondition(providerId, NumberComparer.EQUAL));
			searchExpression.addCondition(new AssetBinaryVersionExternalIdCondition(binaryVersionExternalId, StringComparer.LIKE, true, true));
			log.debug("binaryVersionExternalId : " + binaryVersionExternalId);
		}
		
		String providerExternalId = condition.getProviderExternalId();
		if (providerExternalId != null) {
			// searchExpression.addCondition(new AssetBinaryVersionAssetProviderIdCondition(providerId, NumberComparer.EQUAL));
			searchExpression.addCondition(new AssetBinaryVersionAssetProviderExternalIdCondition(providerExternalId, StringComparer.LIKE, true, true));
			log.debug("providerExternalId : " + providerExternalId);
		}
		String providerName = condition.getProviderName();
		if (providerName != null) {
			searchExpression.addCondition(new AssetBinaryVersionProviderNameCondition(providerName, StringComparer.LIKE, true, true));
			log.debug("providerName : " + providerName);
		}
		Long parentAssetId = condition.getParentAssetId();
		if (parentAssetId != null) {
			searchExpression.addCondition(new AssetBinaryVersionOwnerAssetParentIdCondition(parentAssetId, NumberComparer.EQUAL));
			log.debug("parentAssetId : " + parentAssetId);
		}
		Long assetId = condition.getAssetId();
		if (assetId != null) {
			searchExpression.addCondition(new AssetBinaryVersionAssetIdCondition(assetId, NumberComparer.EQUAL));
			log.debug("assetId : " + assetId);
		}
		String assetName = condition.getAssetName();
		if (assetName != null) {
			searchExpression.addCondition(new AssetBinaryVersionNameCondition(assetName, StringComparer.LIKE, true, true));
			log.debug("assetName : " + assetName);
		}
		Double commissionRate = condition.getCommissionRate();
		if (commissionRate != null) {
			 searchExpression.addCondition(new AssetBinaryVersionProviderCommissionRateCondition(Tools.getScale(commissionRate / 100), NumberComparer.EQUAL));
			log.debug("commissionRate : " + commissionRate);
		}
		// TODO Anders Zhu : 还有问题，等待levi开发完
		String category1 = condition.getCategory1();
		if (category1 != null) {
			List<Category> categoryList = applicationService.getAllCategory(0, Integer.MAX_VALUE);
			if (categoryList != null) {
				List<Long> categoryIdList = new ArrayList<Long>();
				for (Category category : categoryList) {
					if (category.getParentCategory() != null) {
						Category parentCategory = applicationService.getCategoryById(category.getParentCategory().getId());
						if (Tools.regexMatch(category1, parentCategory.getName()))
							categoryIdList.add(category.getId());
					}
				}

				for (Long id : categoryIdList) {
					searchExpression.addCondition(new AssetBinaryVersionCategoryIdCondition(id, NumberComparer.EQUAL));
				}
			}
			log.debug("category1 : " + category1);
		}
		String category2 = condition.getCategory2();
		if (category2 != null) {
			searchExpression.addCondition(new AssetBinaryVersionCategoryNameCondition(category2, StringComparer.LIKE, true, true));
			log.debug("category2 : " + category2);
		}
		Long statusId = condition.getStatusId();
		if (statusId != null) {
			searchExpression.addCondition(new AssetBinaryVersionStatusIdCondition(statusId, NumberComparer.EQUAL));
			log.debug("statusId : " + statusId);
		}

		searchExpression.addCondition(new AssetBinaryVersionRecommendStartDateCondition(new Date(), DateComparer.LESS_THAN));
		searchExpression.addCondition(new AssetBinaryVersionRecommendDueDateCondition(new Date(), DateComparer.GREAT_THAN));

		return searchExpression;
	}

	public ContentManagementCondition getCondition() {
		return condition;
	}

	public void setCondition(ContentManagementCondition condition) {
		this.condition = condition;
	}
}