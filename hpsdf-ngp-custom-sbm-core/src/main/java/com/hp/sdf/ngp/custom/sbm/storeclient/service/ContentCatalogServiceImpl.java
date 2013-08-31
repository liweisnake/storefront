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
package com.hp.sdf.ngp.custom.sbm.storeclient.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.hp.sdf.ngp.api.exception.AssetCatalogEntityNotFoundException;
import com.hp.sdf.ngp.api.exception.AssetCatalogServiceException;
import com.hp.sdf.ngp.api.impl.assetcatalog.AssetCatalogServiceImpl;
import com.hp.sdf.ngp.api.search.SearchExpression;
import com.hp.sdf.ngp.api.search.Condition.StringComparer;
import com.hp.sdf.ngp.common.exception.EntityNotFoundException;
import com.hp.sdf.ngp.custom.sbm.api.impl.model.ContentItemImpl;
import com.hp.sdf.ngp.custom.sbm.api.impl.model.PurchaseHistoryExtendImpl;
import com.hp.sdf.ngp.custom.sbm.api.storeclient.model.ContentItem;
import com.hp.sdf.ngp.custom.sbm.api.storeclient.model.PurchaseHistoryExtend;
import com.hp.sdf.ngp.custom.sbm.api.storeclient.service.ContentCatalogService;
import com.hp.sdf.ngp.custom.sbm.storeclient.dao.ContentItemDao;
import com.hp.sdf.ngp.custom.sbm.storeclient.dao.PurchaseHistoryExtendDao;
import com.hp.sdf.ngp.custom.sbm.storeclient.dao.SecurityTokenDao;
import com.hp.sdf.ngp.search.condition.contentitem.ContentItemItemIdCondition;
import com.hp.sdf.ngp.search.engine.SearchExpressionImpl;

@Component
@Transactional
public class ContentCatalogServiceImpl extends AssetCatalogServiceImpl implements ContentCatalogService {
	@Resource
	private ContentItemDao contentItemDao;

	@Resource
	private PurchaseHistoryExtendDao purchaseHistoryExtendDao;

	@Resource
	private SecurityTokenDao securityTokenDao;

	protected void init() {
		// Do nothing
		// Don't remove third method, because it overrides the supper class
		// methods to remove the JNDI register
	}

	private final static Log log = LogFactory.getLog(ContentCatalogServiceImpl.class);

	public Long addContentItem(ContentItem contentItem) throws AssetCatalogServiceException {
		log.debug("enter addContentItem:contentItem=" + contentItem.toString());
		try {
			if (!(contentItem instanceof ContentItemImpl)) {
				throw new AssetCatalogServiceException("The method parameter purchaseHistory is not a instance of PurchaseHistoryExtendImpl");
			}

			ContentItemImpl contentItemImpl = (ContentItemImpl) contentItem;
			com.hp.sdf.ngp.custom.sbm.storeclient.model.ContentItem content = contentItemImpl.getContentItem();

			contentItemDao.persist(content);
			log.debug("addContentItem returns:" + content.getId());
			return content.getId();
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Comment exception: " + e);
			throw new AssetCatalogServiceException(e);
		}
	}

	public ContentItem constructContentItem() throws AssetCatalogServiceException {
		return new ContentItemImpl();
	}

	public void deleteBatchContentItem(SearchExpression searchExpression) throws AssetCatalogServiceException {
		log.debug("enter deleteBatchContentItem:searchExpression=" + searchExpression.toString());
		try {
			List<com.hp.sdf.ngp.custom.sbm.storeclient.model.ContentItem> contentItems = contentItemDao.search(searchExpression);
			if (null != contentItems) {
				for (com.hp.sdf.ngp.custom.sbm.storeclient.model.ContentItem contentItem : contentItems) {
					contentItemDao.remove(contentItem);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Comment exception: " + e);
			throw new AssetCatalogServiceException(e);
		}
	}

	public void deleteBatchPurchaseHistoryExtend(SearchExpression searchExpression) throws AssetCatalogServiceException {
		log.debug("enter deleteBatchPurchaseHistoryExtend:searchExpression=" + searchExpression.toString());
		try {
			List<com.hp.sdf.ngp.custom.sbm.storeclient.model.PurchaseHistoryExtend> purchaseHistoryExtends = purchaseHistoryExtendDao
					.search(searchExpression);
			if (null != purchaseHistoryExtends) {
				for (com.hp.sdf.ngp.custom.sbm.storeclient.model.PurchaseHistoryExtend purchaseHistoryExtend : purchaseHistoryExtends) {
					purchaseHistoryExtendDao.remove(purchaseHistoryExtend);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Comment exception: " + e);
			throw new AssetCatalogServiceException(e);
		}
	}

	public void deleteBatchSecurityToken(SearchExpression searchExpression) throws AssetCatalogServiceException {
		log.debug("enter deleteBatchSecurityToken:searchExpression=" + searchExpression.toString());
		try {
			List<com.hp.sdf.ngp.custom.sbm.storeclient.model.SecurityToken> securityTokens = securityTokenDao.search(searchExpression);
			if (null != securityTokens) {
				for (com.hp.sdf.ngp.custom.sbm.storeclient.model.SecurityToken securityToken : securityTokens) {
					securityTokenDao.remove(securityToken);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Comment exception: " + e);
			throw new AssetCatalogServiceException(e);
		}
	}

	public void deleteContentItem(Long itemId) throws AssetCatalogServiceException {
		log.debug("enter deleteContentItem:itemId=" + itemId);
		try {
			SearchExpression searchExpression = new SearchExpressionImpl();
			searchExpression.addCondition(new ContentItemItemIdCondition(String.valueOf(itemId), StringComparer.EQUAL, false, false));

			List<com.hp.sdf.ngp.custom.sbm.storeclient.model.ContentItem> contentItems = contentItemDao.search(searchExpression);
			if (null != contentItems && contentItems.size() > 0) {
				for (com.hp.sdf.ngp.custom.sbm.storeclient.model.ContentItem contentItem : contentItems) {
					contentItemDao.remove(contentItem);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			log.error("Comment exception: " + e);
			throw new AssetCatalogServiceException(e);
		}
	}

	@Transactional(readOnly = true)
	public List<PurchaseHistoryExtend> searchPurchaseHistory(SearchExpression searchExpression) throws AssetCatalogServiceException {
		log.debug("enter searchPurchaseHistory:searchExpression=" + searchExpression.toString());
		try {
			List<PurchaseHistoryExtend> purchaseHistoryExtend_list = new ArrayList<PurchaseHistoryExtend>();
			List<com.hp.sdf.ngp.custom.sbm.storeclient.model.PurchaseHistoryExtend> purchaseHistoryExtends = purchaseHistoryExtendDao
					.search(searchExpression);
			if (null != purchaseHistoryExtends) {
				for (com.hp.sdf.ngp.custom.sbm.storeclient.model.PurchaseHistoryExtend purchaseHistoryExtend : purchaseHistoryExtends) {
					purchaseHistoryExtend_list.add(new PurchaseHistoryExtendImpl(purchaseHistoryExtend));
				}
			}

			log.debug("searchPurchaseHistory returns size:" + purchaseHistoryExtend_list.size());
			return purchaseHistoryExtend_list;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Comment exception: " + e);
			throw new AssetCatalogServiceException(e);
		}
	}

	public void updateContentItem(ContentItem contentItem) throws AssetCatalogServiceException {
		log.debug("enter updateContentItem:" + contentItem.toString());
		try {
			if (!(contentItem instanceof ContentItemImpl)) {
				throw new AssetCatalogServiceException("The method parameter purchaseHistory is not a instance of PurchaseHistoryExtendImpl");
			}

			ContentItemImpl contentItemImpl = (ContentItemImpl) contentItem;
			com.hp.sdf.ngp.custom.sbm.storeclient.model.ContentItem content = contentItemImpl.getContentItem();

			contentItemDao.merge(content);
		} catch (EntityNotFoundException enfe) {
			log.error("Entity Not Found: " + enfe);
			throw new AssetCatalogEntityNotFoundException(enfe);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Comment exception: " + e);
			throw new AssetCatalogServiceException(e);
		}
	}

	/**
	 * parameter1 is assetId parameter2 is publish_flag
	 */
	public int callDatabaseProcedure(String procedureName, List<Object> parameters) throws AssetCatalogServiceException {
		StringBuilder params = new StringBuilder();
		try {
			StringBuffer str = new StringBuffer("");
			if (null != parameters) {
				str.append(",parameters = [");
				for (Object parameter : parameters) {
					str.append(parameter).append(",");
				}
				str.deleteCharAt(str.length() - 1).append("]");
			}
			log.debug("enter callDatabaseProcedure:procedureName='" + procedureName + "',paramters= " + str);

			for (int i = 0; i < parameters.size(); i++) {
				if (i == 0) {
					params.append("?");
				} else {
					params.append(",?");
				}
			}
			return (Integer) contentItemDao.sqlExecute("select " + procedureName + "(" + params.toString() + ")", parameters);
		} catch (Exception e) {
			// throw new AssetCatalogServiceException("Call procedure" +
			// procedureName + " failed. Parameter is: " + params.toString(),
			// e);
			return -1;
		}
	    
	}
	
	

	public Map<String, Date> generateBinaryVersionIndex(List<String> excludeStatus, String source, String updateDateEavAttrName)
			throws AssetCatalogServiceException {
		StringBuffer str = new StringBuffer("");
		if (null != excludeStatus) {
			str.append(",excludeStatus = [");
			for (String excludeStatu : excludeStatus) {
				str.append(excludeStatu).append(",");
			}
			str.deleteCharAt(str.length() - 1).append("]");
		}
		log.debug("enter generateBinaryVersionIndex:source='" + source + "',updateDateEavAttrName='" + updateDateEavAttrName + "',excludeStatus="
				+ str);

		try {
			Assert.notNull(excludeStatus);
			Assert.notNull(source);
			Assert.notNull(updateDateEavAttrName);
			StringBuilder statusStr = new StringBuilder();
			if (excludeStatus.size() > 0) {
				statusStr.append("and bv.STATUSID = s.id and (1 = 1");
				for (int i = 0; i < excludeStatus.size(); i++) {
					statusStr.append(" and ").append("s.status <> '").append(excludeStatus.get(i)).append("'");
				}
				statusStr.append(")");
			}
			String sql = "select distinct a.externalId, attrdate.value from assetbinaryversion bv, asset a, attribute attr, entityattribute ea, attributevaluedate attrdate, status s where bv.assetid = a.id and a.source = '"
					+ source
					+ "' and attr.attributeName = '"
					+ updateDateEavAttrName
					+ "' and attrdate.attributeID = attr.attributeID and ea.attributeValueID = attrdate.attributeValueID and ea.ENTITYID = bv.entityid "
					+ statusStr;
			List<Object[]> list = contentItemDao.findSql(sql, 0, Integer.MAX_VALUE);
			Map<String, Date> map = new HashMap<String, Date>();
			for (Object[] objArray : list) {
				map.put((String) objArray[0], (Date) objArray[1]);
			}
			return map;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Comment exception: " + e);
			throw new AssetCatalogServiceException(e);
		}
	}
}

// $Id$