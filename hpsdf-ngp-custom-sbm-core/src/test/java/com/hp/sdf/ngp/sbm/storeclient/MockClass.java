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
package com.hp.sdf.ngp.sbm.storeclient;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.hp.sdf.ngp.api.assetcatalog.AssetCatalogService;
import com.hp.sdf.ngp.api.comon.EntityType;
import com.hp.sdf.ngp.api.exception.AssetCatalogServiceException;
import com.hp.sdf.ngp.api.model.AttributeContainer;
import com.hp.sdf.ngp.api.model.BinaryVersion;
import com.hp.sdf.ngp.api.search.Condition;
import com.hp.sdf.ngp.api.search.SearchEngine;
import com.hp.sdf.ngp.api.search.SearchExpression;
import com.hp.sdf.ngp.api.search.condition.assetbinaryversion.AssetBinaryVersionExternalIdConditionDescriptor;
import com.hp.sdf.ngp.custom.sbm.api.storeclient.service.ClientContentService;
import com.hp.sdf.ngp.service.ApplicationService;

@Component
public class MockClass {

	@Resource
	private TempEavStringOrderExecutor tempEavStringOrderExecutor;

	@Resource
	private ClientContentService clientContentService;

	@Resource
	private ApplicationService applicationService;

	@Resource
	private AssetCatalogService assetCatalogService;

	@Resource
	private SearchEngine searchEngine;

	public Object getEavAttributeValue(AttributeContainer entity, String attributeName) {
		if (null == entity || null == attributeName) {
			return null;
		}

		List<Object> valueList = entity.getAttributeValue(attributeName);
		if (null == valueList || valueList.size() == 0) {
			return null;
		}

		return valueList.get(0);
	}

	private void copyBinaryVersionAttributes(BinaryVersion from, BinaryVersion to) {
		to.setAssetId(from.getAssetId());
		to.setAssetParentId(from.getAssetParentId());
		// to.setBigThumbnail(fileNameSuffix, buf);
		to.setBrief(from.getBrief());
		// to.setCommissionRate(from.getCommissionRate());
		to.setDescription(from.getDescription());
		to.setExpireDate(from.getExpireDate());
		to.setExternalId(from.getExternalId());
		// to.setFile(binaryVersionStream);
		to.setFileName(from.getFileName());
		to.setFileSize(from.getFileSize());
		// to.setMedThumbnail(fileNameSuffix, buf);
		to.setName(from.getName());
		to.setNewArrivalDueDate(from.getNewArrivalDueDate());
		to.setPublishDate(from.getPublishDate());
		to.setRecommendDueDate(from.getRecommendDueDate());
		// to.setRecommendOrder(from.getRecommendOrder());
		to.setRecommendStartDate(from.getRecommendStartDate());
		to.setRecommendUpdateDate(from.getRecommendUpdateDate());
		// to.setStatus(from.getStatus());
		List<String> tags = new ArrayList<String>();
		tags.addAll(from.getTags());
		to.setTags(tags);
		// to.setThumbnail(fileNameSuffix, buf);
		to.setVersion(from.getVersion());
	}

	public long copyBinaryVersionToDB(BinaryVersion oldBv) throws AssetCatalogServiceException {

		BinaryVersion newBv = assetCatalogService.constructBinaryVersion();

		copyBinaryVersionAttributes(oldBv, newBv);

		// newBv.setStatus("PREPUBLIC");

		Long newBvId = assetCatalogService.addBinaryVersion(newBv);

		newBv = assetCatalogService.getBinaryVersion(newBvId);

		String openFlg = "02";
		String version = newBv.getVersion();
		// addAssetLifecycleActionHistory(newBv.getAssetId(), newBv.getId(),
		// newBv.getExternalId(), null, "PREPUBLIC", openFlg, version);

		String iconurl = (String) getEavAttributeValue(oldBv, "THUMBNAILLOCATION");

		String recommendurl = (String) getEavAttributeValue(oldBv, "RECOMMENDEDIMAGE");

		addEavAttributeToDB(newBvId, "PUBLISHFLG", "02", String.class);

		copyEavAttributeToDB(oldBv, newBvId, "CPPRIORITY", Float.class);
		copyEavAttributeToDB(oldBv, newBvId, "DISPLAYORDER", Float.class);
		copyEavAttributeToDB(oldBv, newBvId, "PICKUP", Float.class);
		copyEavAttributeToDB(oldBv, newBvId, "JAPANESENAME", String.class);
		copyEavAttributeToDB(oldBv, newBvId, "TITLENAME", String.class);
		copyEavAttributeToDB(oldBv, newBvId, "TITLEJPNAME", String.class);
		copyEavAttributeToDB(oldBv, newBvId, "CHILDCONTENTICON", String.class);
		copyEavAttributeToDB(oldBv, newBvId, "PARTINFO", String.class);
		copyEavAttributeToDB(oldBv, newBvId, "LICENSERNAME", String.class);
		copyEavAttributeToDB(oldBv, newBvId, "MUSICLABEL", String.class);
		copyEavAttributeToDB(oldBv, newBvId, "ARTISTNAME", String.class);
		copyEavAttributeToDB(oldBv, newBvId, "ARTISTJPNAME", String.class);
		copyEavAttributeToDB(oldBv, newBvId, "SHORTDESCRIPTION", String.class);
		copyEavAttributeToDB(oldBv, newBvId, "LONGDESCRIPTION", String.class);
		copyEavAttributeToDB(oldBv, newBvId, "COPYRIGHT", String.class);
		copyEavAttributeToDB(oldBv, newBvId, "OTHERDESCRIPTION", String.class);
		copyEavAttributeToDB(oldBv, newBvId, "LMARK", String.class);
		copyEavAttributeToDB(oldBv, newBvId, "PRICETYPE", String.class);
		copyEavAttributeToDB(oldBv, newBvId, "PRICE", Float.class);
		copyEavAttributeToDB(oldBv, newBvId, "BILLINGCOMMENT", String.class);
		copyEavAttributeToDB(oldBv, newBvId, "SALEPRICE", Float.class);
		copyEavAttributeToDB(oldBv, newBvId, "SALECOMMENT", String.class);
		copyEavAttributeToDB(oldBv, newBvId, "CAMPAIGNSTARTDATE", Date.class);
		copyEavAttributeToDB(oldBv, newBvId, "CAMPAIGNENDDATE", Date.class);
		copyEavAttributeToDB(oldBv, newBvId, "PUBLISHEPLANDATE", Date.class);
		copyEavAttributeToDB(oldBv, newBvId, "LASTUPDATEDATE", Date.class);
		copyEavAttributeToDB(oldBv, newBvId, "MCDUPDATEDATE", Date.class);
		copyEavAttributeToDB(oldBv, newBvId, "REDOWNLOADFLAG", String.class);
		copyEavAttributeToDB(oldBv, newBvId, "REDOWNLOADDAYS", Float.class);
		copyEavAttributeToDB(oldBv, newBvId, "PLAYDURATION", String.class);
		copyEavAttributeToDB(oldBv, newBvId, "CPPAGEURL", String.class);
		copyEavAttributeToDB(oldBv, newBvId, "DOWNLOADURL", String.class);
		copyEavAttributeToDB(oldBv, newBvId, "SAMPLEDOWNLOADURL", String.class);
		copyEavAttributeToDB(oldBv, newBvId, "RELEVENTSALESITEURL", String.class);
		copyEavAttributeToDB(oldBv, newBvId, "FILTERFLAG", String.class);
		copyEavAttributeToDB(oldBv, newBvId, "SUPPORTNAME", String.class);
		copyEavAttributeToDB(oldBv, newBvId, "SUPPORTCONTACT", String.class);
		copyEavAttributeToDB(oldBv, newBvId, "SUPPORTDETAIL", String.class);
		copyEavAttributeToDB(oldBv, newBvId, "OFFICIALSITEURL", String.class);
		copyEavAttributeToDB(oldBv, newBvId, "RELEVENTSITEURL", String.class);
		copyEavAttributeToDB(oldBv, newBvId, "FLASHURL", String.class);
		copyEavAttributeToDB(oldBv, newBvId, "TWITTER", String.class);
		copyEavAttributeToDB(oldBv, newBvId, "ENABLECOMMENTFLAG", String.class);
		copyEavAttributeToDB(oldBv, newBvId, "THUMBNAILLOCATION", String.class);
		copyEavAttributeToDB(oldBv, newBvId, "RECOMMENDEDIMAGE", String.class);

		copyEavAttributeToDB(oldBv, newBvId, "LID", String.class);
		copyEavAttributeToDB(oldBv, newBvId, "FUNCTIONFILTER", String.class);
		copyEavAttributeToDB(oldBv, newBvId, "RESOLUTIONFILTER", Float.class);
		copyEavAttributeToDB(oldBv, newBvId, "MIMETYPEFILTER", String.class);
		copyEavAttributeToDB(oldBv, newBvId, "RELEVENTCONTENTSID1", String.class);
		copyEavAttributeToDB(oldBv, newBvId, "RELEVENTCONTENTSID2", String.class);
		copyEavAttributeToDB(oldBv, newBvId, "RELEVENTCONTENTSID3", String.class);
		copyEavAttributeToDB(oldBv, newBvId, "SALEDATEINFO", String.class);
		copyEavAttributeToDB(oldBv, newBvId, "MANAGEMENTCODE", String.class);

		copyEavAttributeToDB(oldBv, newBvId, "SECURITYINFO", String.class);

		copyEavAttributeToDB(oldBv, newBvId, "COMMISSIONRATE", Float.class);

		// updateGetImageStatus(newBv);

		return newBvId;
	}

	public <T> void copyEavAttributeToDB(BinaryVersion oldBv, long newBvId, String attributeName, Class<T> type) throws AssetCatalogServiceException {
		T value = (T) getEavAttributeValue(oldBv, attributeName);
		if (value != null) {
			addEavAttributeToDB(newBvId, attributeName, value, type);
		}
	}

	public <T> void addEavAttributeToDB(long bvId, String attributeName, T attributeValue, Class<T> type) throws AssetCatalogServiceException {
		if (type == String.class) {
			assetCatalogService.addAttribute(EntityType.BINARYVERSION, bvId, attributeName, (String) attributeValue);
		} else if (type == Float.class) {
			assetCatalogService.addAttribute(EntityType.BINARYVERSION, bvId, attributeName, (Float) attributeValue);
		} else if (type == Date.class) {
			assetCatalogService.addAttribute(EntityType.BINARYVERSION, bvId, attributeName, (Date) attributeValue);
		} else {
			throw new AssetCatalogServiceException("the type is not correct!");
		}
	}

	@Transactional
	public void processDiffChangeContent(String id, String name) throws Exception {
		// List<BinaryVersion> bvList = searchBinaryVersions(id, "02");
		List<BinaryVersion> bvList = null;
		// if (bvList != null && bvList.size() > 0) {
		// processDiffChangeUpdate(bvList.get(0), content, filePath);
		// } else {
		bvList = searchBinaryVersions(id, "01");
		if (bvList != null && bvList.size() > 0) {
			BinaryVersion bv;
			try {
				long newBvId = copyBinaryVersionToDB(bvList.get(0));
				bv = assetCatalogService.getBinaryVersion(newBvId);
			} catch (AssetCatalogServiceException e) {
				throw new Exception();
			}

			// processDiffChangeUpdate(bv, content, filePath);
			updateEavAttributeToDB(bv, "ARTISTNAME", name, String.class);
		} else {
		}
		// }
	}

	public List<BinaryVersion> searchBinaryVersions(String id, String openFlg) throws Exception {
		try {
			SearchExpression searchExpr = searchEngine.createSearchExpression();
			// AssetBinaryVersionSourceConditionDescriptor abvscDesc = new
			// AssetBinaryVersionSourceConditionDescriptor("02",
			// Condition.StringComparer.EQUAL, false, false);
			// searchExpr.addCondition(searchEngine.createCondition(abvscDesc));

			// AssetBinaryVersionStatusNameConditionDescriptor abvsncDesc = new
			// AssetBinaryVersionStatusNameConditionDescriptor("REVOKED",
			// Condition.StringComparer.NOT_EQUAL, false, false);
			// searchExpr.addCondition(searchEngine.createCondition(abvsncDesc));

			if (id != null && id.length() > 0) {
				AssetBinaryVersionExternalIdConditionDescriptor abveicDesc = new AssetBinaryVersionExternalIdConditionDescriptor(id,
						Condition.StringComparer.EQUAL, false, false);
				searchExpr.addCondition(searchEngine.createCondition(abveicDesc));
			}
			// if (openFlg != null && openFlg.length() > 0) {
			// EavStringConditionDescriptor escDesc = new
			// EavStringConditionDescriptor(EntityType.BINARYVERSION,
			// "PUBLISHFLG", openFlg, Condition.StringComparer.EQUAL, false,
			// false);
			// searchExpr.addCondition(searchEngine.createCondition(escDesc));
			// }

			List<BinaryVersion> bvList = assetCatalogService.searchBinaryVersions(searchExpr);

			return bvList;
		} catch (AssetCatalogServiceException e) {
			throw new Exception();
		}
	}

	public <T> void updateEavAttributeToDB(BinaryVersion bv, String attributeName, T attributeValue, Class<T> type)
			throws AssetCatalogServiceException {
		Object dbValue = getEavAttributeValue(bv, attributeName);
		if (!isObjectEquals(dbValue, attributeValue)) {
			assetCatalogService.removeAttributes(EntityType.BINARYVERSION, bv.getId(), attributeName);
			addEavAttributeToDB(bv.getId(), attributeName, attributeValue, type);
		}
	}

	private boolean isObjectEquals(Object obj1, Object obj2) {
		if (obj1 == obj2) {
			return true;
		}
		if (obj1 != null && obj2 != null) {
			return obj1.equals(obj2);
		} else {
			return false;
		}
	}
}