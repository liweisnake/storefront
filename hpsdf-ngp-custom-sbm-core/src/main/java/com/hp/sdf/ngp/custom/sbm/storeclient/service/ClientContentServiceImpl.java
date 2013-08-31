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
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.hp.sdf.ngp.api.exception.StoreClientEntityNotFoundException;
import com.hp.sdf.ngp.api.exception.StoreClientServiceException;
import com.hp.sdf.ngp.api.impl.storeclient.StoreClientServiceImpl;
import com.hp.sdf.ngp.api.search.SearchExpression;
import com.hp.sdf.ngp.api.search.Condition.StringComparer;
import com.hp.sdf.ngp.common.exception.EntityNotFoundException;
import com.hp.sdf.ngp.custom.sbm.api.impl.model.ContentItemImpl;
import com.hp.sdf.ngp.custom.sbm.api.impl.model.HandSetDeviceImpl;
import com.hp.sdf.ngp.custom.sbm.api.impl.model.ParentAssetVersionSummaryImpl;
import com.hp.sdf.ngp.custom.sbm.api.impl.model.PurchaseHistoryExtendImpl;
import com.hp.sdf.ngp.custom.sbm.api.impl.model.SecurityTokenImpl;
import com.hp.sdf.ngp.custom.sbm.api.storeclient.model.ContentItem;
import com.hp.sdf.ngp.custom.sbm.api.storeclient.model.HandSetDevice;
import com.hp.sdf.ngp.custom.sbm.api.storeclient.model.ParentAssetVersionSummary;
import com.hp.sdf.ngp.custom.sbm.api.storeclient.model.PurchaseHistoryExtend;
import com.hp.sdf.ngp.custom.sbm.api.storeclient.model.SecurityToken;
import com.hp.sdf.ngp.custom.sbm.api.storeclient.service.ClientContentService;
import com.hp.sdf.ngp.custom.sbm.storeclient.dao.ContentItemDao;
import com.hp.sdf.ngp.custom.sbm.storeclient.dao.HandSetDeviceDao;
import com.hp.sdf.ngp.custom.sbm.storeclient.dao.ParentAssetVersionSummaryDao;
import com.hp.sdf.ngp.custom.sbm.storeclient.dao.PurchaseHistoryExtendDao;
import com.hp.sdf.ngp.custom.sbm.storeclient.dao.SecurityTokenDao;
import com.hp.sdf.ngp.dao.SubscriberProfileDAO;
import com.hp.sdf.ngp.model.SubscriberProfile;
import com.hp.sdf.ngp.search.condition.contentitem.ContentItemAssetExternalIdCondition;
import com.hp.sdf.ngp.search.condition.contentitem.ContentItemItemIdCondition;
import com.hp.sdf.ngp.search.engine.SearchExpressionImpl;

@Component
@Transactional
public class ClientContentServiceImpl extends StoreClientServiceImpl implements ClientContentService {

	@Resource
	private PurchaseHistoryExtendDao purchaseHistoryExtendDao;

	@Resource
	private SecurityTokenDao securityTokenDao;

	@Resource
	private ContentItemDao contentItemDao;

	@Resource
	private SubscriberProfileDAO subscriberProfileDAO;

	@Resource
	private HandSetDeviceDao handSetDeviceDao;

	@Resource
	private ParentAssetVersionSummaryDao parentAssetVersionSummaryDao;

	private final static Log log = LogFactory.getLog(ClientContentServiceImpl.class);

	protected void init() {
		// Do nothing
		// Don't remove third method, because it overrides the supper class
		// methods to remove the JNDI register
	}

	public void addPurchaseHistory(PurchaseHistoryExtend purchaseHistory) throws StoreClientServiceException {
		log.debug("enter addPurchaseHistory:" + purchaseHistory.toString());
		try {
			if (!(purchaseHistory instanceof PurchaseHistoryExtendImpl)) {
				throw new StoreClientServiceException("The method parameter purchaseHistory is not a instance of PurchaseHistoryExtendImpl");
			}

			PurchaseHistoryExtendImpl purchaseHistoryExtendImpl = (PurchaseHistoryExtendImpl) purchaseHistory;
			com.hp.sdf.ngp.custom.sbm.storeclient.model.PurchaseHistoryExtend purchaseHistoryExtend = purchaseHistoryExtendImpl.getPurchaseHistoryExtend();

			purchaseHistoryExtendDao.persist(purchaseHistoryExtend);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Comment exception: " + e);
			throw new StoreClientServiceException(e);
		}
	}

	public void addSecurityToken(SecurityToken token) throws StoreClientServiceException {
		log.debug("enter addSecurityToken:" + token.toString());
		try {
			if (!(token instanceof SecurityTokenImpl)) {
				throw new StoreClientServiceException("The method parameter token is not a instance of SecurityTokenImpl");
			}

			SecurityTokenImpl securityTokenImpl = (SecurityTokenImpl) token;
			com.hp.sdf.ngp.custom.sbm.storeclient.model.SecurityToken securityToken = securityTokenImpl.getSecurityToken();

			securityTokenDao.persist(securityToken);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Comment exception: " + e);
			throw new StoreClientServiceException(e);
		}
	}

	public PurchaseHistoryExtend constructPurchaseHistoryExtend() throws StoreClientServiceException {
		return new PurchaseHistoryExtendImpl();
	}

	public void deleteSecurityToken(String token) throws StoreClientServiceException {
		log.debug("enter deleteSecurityToken:" + token.toString());
		try {
			securityTokenDao.remove(token);

		} catch (Exception e) {
			e.printStackTrace();
			log.error("Comment exception: " + e);
			throw new StoreClientServiceException(e);
		}
	}

	public ContentItem getContentItem(String itemId, String identifierId) throws StoreClientServiceException {
		log.debug("enter getContentItem:itemId='" + itemId + "',identifierId='" + identifierId + "'");
		try {
			SearchExpression searchExpression = new SearchExpressionImpl();
			searchExpression.addCondition(new ContentItemItemIdCondition(itemId, StringComparer.EQUAL, false, false));
			searchExpression.addCondition(new ContentItemAssetExternalIdCondition(identifierId, StringComparer.EQUAL, false, false));

			List<com.hp.sdf.ngp.custom.sbm.storeclient.model.ContentItem> contentItems = contentItemDao.search(searchExpression);
			if (null != contentItems && contentItems.size() > 0) {
				com.hp.sdf.ngp.custom.sbm.storeclient.model.ContentItem contentItem = contentItems.get(0);

				ContentItemImpl contentItemImpl = new ContentItemImpl(contentItem);
				log.debug("getContentItem returns:" + contentItemImpl.toString());
				return contentItemImpl;
			}
			return null;

		} catch (Exception e) {
			e.printStackTrace();
			log.error("Comment exception: " + e);
			throw new StoreClientServiceException(e);
		}
	}

	public SecurityToken getSecurityToken(String token) throws StoreClientServiceException {
		log.debug("enter getSecurityToken:token='" + token + "'");
		try {
			com.hp.sdf.ngp.custom.sbm.storeclient.model.SecurityToken securityToken = securityTokenDao.findById(token);
			if (null != securityToken) {

				SecurityToken securityToken2 = new SecurityTokenImpl(securityToken);
				log.debug("getSecurityToken return:" + securityToken2.toString());
				return securityToken2;
			} else {
				log.debug("getSecurityToken return null");
				throw new StoreClientEntityNotFoundException("getSecurityToken return null");
			}

		} catch (Exception e) {
			e.printStackTrace();
			log.error("Comment exception: " + e);

			if (e instanceof StoreClientEntityNotFoundException) {
				throw new StoreClientEntityNotFoundException(e);
			}

			throw new StoreClientServiceException(e);
		}
	}

	public boolean isClientTester(String msisdn) throws StoreClientServiceException {
		log.debug("enter isClientTester:msisdn='" + msisdn + "'");
		try {
			SubscriberProfile subscriberProfile = subscriberProfileDAO.findUniqueBy("msisdn", msisdn);
			if (null != subscriberProfile && subscriberProfile.getClientTesterFlag() == 1) {

				log.debug("isClientTester returns true");
				return true;
			}

			log.debug("isClientTester returns false");
			return false;

		} catch (Exception e) {
			e.printStackTrace();
			log.error("Comment exception: " + e);
			throw new StoreClientServiceException(e);
		}
	}

	@Transactional
	public List<ContentItem> searchContentItem(SearchExpression searchExpression) throws StoreClientServiceException {
		log.debug("enter searchContentItem:searchExpression=" + searchExpression.toString());
		try {
			List<ContentItem> contentItem_list = new ArrayList<ContentItem>();
			List<com.hp.sdf.ngp.custom.sbm.storeclient.model.ContentItem> contentItems = contentItemDao.search(searchExpression);
			if (null != contentItems) {
				for (com.hp.sdf.ngp.custom.sbm.storeclient.model.ContentItem contentItem : contentItems) {
					contentItem_list.add(new ContentItemImpl(contentItem));
				}
			}

			log.debug("searchContentItem returns size:" + contentItem_list.size());
			return contentItem_list;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Comment exception: " + e);
			throw new StoreClientServiceException(e);
		}
	}

	@Transactional
	public List<PurchaseHistoryExtend> searchPurchaseHistory(SearchExpression searchExpression) throws StoreClientServiceException {
		log.debug("enter searchPurchaseHistory:searchExpression=" + searchExpression.toString());
		try {
			List<PurchaseHistoryExtend> purchaseHistoryExtend_list = new ArrayList<PurchaseHistoryExtend>();
			List<com.hp.sdf.ngp.custom.sbm.storeclient.model.PurchaseHistoryExtend> purchaseHistoryExtends = purchaseHistoryExtendDao.search(searchExpression);
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
			throw new StoreClientServiceException(e);
		}
	}

	@Transactional
	public int searchPurchaseHistoryCount(SearchExpression searchExpression) {
		return purchaseHistoryExtendDao.searchCount(searchExpression);
	}

	public void updatePurchaseHistory(PurchaseHistoryExtend purchaseHistory) throws StoreClientServiceException {
		log.debug("enter updatePurchaseHistory:" + purchaseHistory.toString());
		try {
			if (!(purchaseHistory instanceof PurchaseHistoryExtendImpl)) {
				throw new StoreClientServiceException("The method parameter purchaseHistory is not a instance of PurchaseHistoryExtendImpl");
			}

			PurchaseHistoryExtendImpl purchaseHistoryExtendImpl = (PurchaseHistoryExtendImpl) purchaseHistory;
			com.hp.sdf.ngp.custom.sbm.storeclient.model.PurchaseHistoryExtend purchaseHistoryExtend = purchaseHistoryExtendImpl.getPurchaseHistoryExtend();

			purchaseHistoryExtendDao.merge(purchaseHistoryExtend);
		} catch (EntityNotFoundException enfe) {
			log.error("Entity Not Found: " + enfe);
			throw new StoreClientEntityNotFoundException(enfe);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Comment exception: " + e);
			throw new StoreClientServiceException(e);
		}
	}

	public void updateSecurityToken(SecurityToken token) throws StoreClientServiceException {
		log.debug("enter updateSecurityToken:" + token.toString());
		try {
			if (!(token instanceof SecurityTokenImpl)) {
				throw new StoreClientServiceException("The method parameter token is not a instance of SecurityTokenImpl");
			}

			SecurityTokenImpl securityTokenImpl = (SecurityTokenImpl) token;
			com.hp.sdf.ngp.custom.sbm.storeclient.model.SecurityToken securityToken = securityTokenImpl.getSecurityToken();

			securityTokenDao.merge(securityToken);
		} catch (EntityNotFoundException enfe) {
			log.error("Entity Not Found: " + enfe);
			throw new StoreClientEntityNotFoundException(enfe);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Comment exception: " + e);
			throw new StoreClientServiceException(e);
		}
	}

	public SecurityToken constructSecurityToken() throws StoreClientServiceException {
		return new SecurityTokenImpl();
	}

	@Transactional
	public List<SecurityToken> searchSecurityToken(SearchExpression searchExpression) throws StoreClientServiceException {
		log.debug("enter searchSecurityToken:searchExpression=" + searchExpression.toString());
		try {
			List<SecurityToken> securityToken_list = new ArrayList<SecurityToken>();
			List<com.hp.sdf.ngp.custom.sbm.storeclient.model.SecurityToken> securityTokens = securityTokenDao.search(searchExpression);
			for (com.hp.sdf.ngp.custom.sbm.storeclient.model.SecurityToken securityToken : securityTokens) {
				SecurityToken security_token = new SecurityTokenImpl(securityToken);
				securityToken_list.add(security_token);
			}

			log.debug("searchSecurityToken returns size:" + searchExpression.toString());
			return securityToken_list;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Comment exception: " + e);
			throw new StoreClientServiceException(e);
		}
	}

	@Transactional
	public List<HandSetDevice> searchHandSetDevice(SearchExpression searchExpression) throws StoreClientServiceException {
		log.debug("enter searchHandSetDevice:searchExpression=" + searchExpression.toString());
		try {
			List<HandSetDevice> handSetDevice_list = new ArrayList<HandSetDevice>();
			List<com.hp.sdf.ngp.custom.sbm.storeclient.model.HandSetDevice> handSetDevices = handSetDeviceDao.search(searchExpression);
			if (null != handSetDevices) {
				for (com.hp.sdf.ngp.custom.sbm.storeclient.model.HandSetDevice handSetDevice : handSetDevices) {
					HandSetDeviceImpl handSetDeviceImpl = new HandSetDeviceImpl(handSetDevice);
					// fillHandSetDevice(handSetDeviceImpl);
					handSetDevice_list.add(handSetDeviceImpl);
				}
			}

			log.debug("searchHandSetDevice returns size:" + handSetDevice_list.size());
			return handSetDevice_list;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Comment exception: " + e);
			throw new StoreClientServiceException(e);
		}
	}

	// private void fillHandSetDevice(HandSetDeviceImpl handSetDeviceImpl)
	// throws StoreClientServiceException {
	// try {
	// com.hp.sdf.ngp.custom.sbm.storeclient.model.HandSetDevice handSetDevice =
	// handSetDeviceImpl.getHandSetDevice();
	// if (null != handSetDevice) {
	// Set<MimeType> mimeTypes = handSetDevice.getMimeTypes();
	// List<String> strs = new ArrayList<String>();
	// if (null != mimeTypes) {
	// for (MimeType mimeType : mimeTypes) {
	// strs.add(mimeType.getType());
	// }
	// }
	// handSetDeviceImpl.setMimeTypes(strs);
	// }
	// } catch (Exception e) {
	// e.printStackTrace();
	// log.error("Comment exception: " + e);
	// throw new StoreClientServiceException(e);
	// }
	// }
	@Transactional
	public List<ParentAssetVersionSummary> searchParentAssetVersionSummary(SearchExpression searchExpression) throws StoreClientServiceException {
		log.debug("enter searchParentAssetVersionSummary:searchExpression=" + searchExpression.toString());
		try {
			List<ParentAssetVersionSummary> parentAssetVersionSummary_list = new ArrayList<ParentAssetVersionSummary>();
			List<com.hp.sdf.ngp.model.ParentAssetVersionSummary> parentAssetVersionSummarys = parentAssetVersionSummaryDao.search(searchExpression);
			if (null != parentAssetVersionSummarys) {
				for (com.hp.sdf.ngp.model.ParentAssetVersionSummary parentAssetVersionSummary : parentAssetVersionSummarys) {
					ParentAssetVersionSummaryImpl parentAssetVersionSummaryImpl = new ParentAssetVersionSummaryImpl(parentAssetVersionSummary);
					parentAssetVersionSummary_list.add(parentAssetVersionSummaryImpl);
				}
			}

			log.debug("searchParentAssetVersionSummary returns size:" + parentAssetVersionSummary_list.size());
			return parentAssetVersionSummary_list;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Comment exception: " + e);
			throw new StoreClientServiceException(e);
		}
	}

}

// $Id$