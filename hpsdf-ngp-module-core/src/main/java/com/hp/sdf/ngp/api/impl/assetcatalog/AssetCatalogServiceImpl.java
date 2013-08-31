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
package com.hp.sdf.ngp.api.impl.assetcatalog;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Currency;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.hp.sdf.ngp.api.assetcatalog.AssetCatalogService;
import com.hp.sdf.ngp.api.comon.EntityType;
import com.hp.sdf.ngp.api.exception.AssetCatalogEntityNotFoundException;
import com.hp.sdf.ngp.api.exception.AssetCatalogServiceException;
import com.hp.sdf.ngp.api.exception.StoreClientServiceException;
import com.hp.sdf.ngp.api.impl.common.ModelObjectFiller;
import com.hp.sdf.ngp.api.impl.model.AssetCategoryImpl;
import com.hp.sdf.ngp.api.impl.model.AssetCommentImpl;
import com.hp.sdf.ngp.api.impl.model.AssetLifecycleActionHistoryImpl;
import com.hp.sdf.ngp.api.impl.model.AssetProviderImpl;
import com.hp.sdf.ngp.api.impl.model.AttributeListImpl;
import com.hp.sdf.ngp.api.impl.model.BinaryVersionImpl;
import com.hp.sdf.ngp.api.impl.model.CatalogAssetImpl;
import com.hp.sdf.ngp.api.impl.model.RestrictionTypeImpl;
import com.hp.sdf.ngp.api.impl.model.ScreenshotImpl;
import com.hp.sdf.ngp.api.impl.model.UserImpl;
import com.hp.sdf.ngp.api.model.AssetCategory;
import com.hp.sdf.ngp.api.model.AssetComment;
import com.hp.sdf.ngp.api.model.AssetLifecycleActionHistory;
import com.hp.sdf.ngp.api.model.AssetProvider;
import com.hp.sdf.ngp.api.model.AttributeList;
import com.hp.sdf.ngp.api.model.BinaryVersion;
import com.hp.sdf.ngp.api.model.CatalogAsset;
import com.hp.sdf.ngp.api.model.RestrictionType;
import com.hp.sdf.ngp.api.model.Screenshot;
import com.hp.sdf.ngp.api.model.User;
import com.hp.sdf.ngp.api.search.SearchExpression;
import com.hp.sdf.ngp.api.search.Condition.NumberComparer;
import com.hp.sdf.ngp.api.search.Condition.StringComparer;
import com.hp.sdf.ngp.api.storeclient.StoreClientService;
import com.hp.sdf.ngp.common.exception.EntityNotFoundException;
import com.hp.sdf.ngp.common.exception.NgpRuntimeException;
import com.hp.sdf.ngp.model.Asset;
import com.hp.sdf.ngp.model.AssetBinaryVersion;
import com.hp.sdf.ngp.model.AssetPrice;
import com.hp.sdf.ngp.model.Category;
import com.hp.sdf.ngp.model.Comments;
import com.hp.sdf.ngp.model.ContentProviderOperator;
import com.hp.sdf.ngp.model.Platform;
import com.hp.sdf.ngp.model.Provider;
import com.hp.sdf.ngp.model.RestrictedType;
import com.hp.sdf.ngp.model.ScreenShots;
import com.hp.sdf.ngp.model.Status;
import com.hp.sdf.ngp.model.Tag;
import com.hp.sdf.ngp.model.UserDownloadHistory;
import com.hp.sdf.ngp.model.UserProfile;
import com.hp.sdf.ngp.search.condition.asset.AssetProviderIdCondition;
import com.hp.sdf.ngp.search.condition.assetbinaryversion.AssetBinaryVersionVersionNameCondition;
import com.hp.sdf.ngp.search.condition.assetprice.AssetPriceAssetIdCondition;
import com.hp.sdf.ngp.search.condition.assetprice.AssetPriceCurrencyCondition;
import com.hp.sdf.ngp.search.condition.assetprice.AssetPriceVersionIdCondition;
import com.hp.sdf.ngp.search.condition.contentprovideroperator.ContentProviderOperatorAssetProviderIdCondition;
import com.hp.sdf.ngp.search.condition.contentprovideroperator.ContentProviderOperatorUseridCondition;
import com.hp.sdf.ngp.search.engine.SearchExpressionImpl;
import com.hp.sdf.ngp.service.ApplicationService;
import com.hp.sdf.ngp.service.AssetPictureType;
import com.hp.sdf.ngp.service.UserService;
import com.hp.sdf.ngp.workflow.AssetLifeCycleEngine;

@Component
@Transactional
public class AssetCatalogServiceImpl implements AssetCatalogService {

	private final static Log log = LogFactory.getLog(AssetCatalogServiceImpl.class);

	@Resource
	private ApplicationService applicationService;

	@Resource
	private AssetLifeCycleEngine assetLifeCycleEngine;

	@Resource
	private UserService userService;

	@Resource
	private ModelObjectFiller modelObjectFiller;

	@Resource
	private StoreClientService storeClientService;

	public Long addAsset(CatalogAsset asset) throws AssetCatalogServiceException {
		log.debug("enter method addAsset: " + asset.toString());
		try {
			if (!(asset instanceof CatalogAssetImpl)) {
				throw new AssetCatalogServiceException("The method parameter asset is not a instance of CatalogAssetImpl");
			}

			CatalogAssetImpl assetImpl = (CatalogAssetImpl) asset;
			Asset modelAsset = assetImpl.getAsset();

			Long providerId = assetImpl.getProviderId();
			if (null != providerId) {
				Provider p = applicationService.getAssetProviderById(providerId);
				if (null != p) {
					modelAsset.setAssetProvider(p);
				} else {
					throw new AssetCatalogServiceException("the system cannot find corresponding Provider via providerId + " + providerId);
				}
			}

			String status = asset.getStatus();
			if (null != status) {
				Status modelStatus = applicationService.getStatusByName(status);
				if (null != modelStatus) {
					modelAsset.setStatus(modelStatus);
				} else {
					throw new AssetCatalogServiceException("the system cannot find corresponding Status via status " + status);
				}
			} else {
				Status init_status = assetLifeCycleEngine.getStartupStatus();
				modelAsset.setStatus(init_status);
			}

			// Long parentAssetId = asset.getAssetId();
			// if (null != parentAssetId && 0 != parentAssetId) {
			// Asset parentAsset = applicationService.getAsset(parentAssetId);
			// if (null != parentAsset) {
			// modelAsset.setAsset(parentAsset);
			// } else {
			// throw new
			// AssetCatalogServiceException("the system cannot find corresponding parent's Asset via assetId + "
			// + parentAssetId);
			// }
			// }

			applicationService.saveAsset(modelAsset);

			Long assetId = modelAsset.getId();
			long picName = System.currentTimeMillis();
			byte[] bigThumbnail = assetImpl.getBigThumbnail();
			if (null != bigThumbnail && null != assetImpl.getBigName()) {
				applicationService.saveAssetPicture(assetId, bigThumbnail, AssetPictureType.THUMBNAILBIGIMAGE, picName + "." + assetImpl.getBigName());
			}

			byte[] medThumbnail = assetImpl.getMedThumbnail();
			if (null != medThumbnail && null != assetImpl.getMedName()) {
				applicationService.saveAssetPicture(assetId, medThumbnail, AssetPictureType.THUMBNAILMIDDLEIMAGE, picName + "." + assetImpl.getMedName());
			}

			byte[] thumbnail = assetImpl.getThumbnail();
			if (null != thumbnail && null != assetImpl.getThumName()) {
				applicationService.saveAssetPicture(assetId, thumbnail, AssetPictureType.THUMBNAILIMAGE, picName + "." + assetImpl.getThumName());
			}

			log.debug("addAsset returns : " + assetId);
			return assetId;
		} catch (NgpRuntimeException e) {
			e.printStackTrace();
			log.error("Comment exception: " + e);
			throw new AssetCatalogServiceException(e);
		}
	}

	public void addAssetIntoCategories(Long assetId, Long versionId, Set<Long> categoryIds) throws AssetCatalogServiceException {
		// Asset asset = applicationService.getAsset(assetId);
		// if (null == asset) {
		// throw new
		// AssetCatalogServiceException("the system cannot find corresponding Asset via assetId "
		// + assetId);
		// }
		//
		// AssetBinaryVersion assetBinaryVersion =
		// applicationService.getAssetBinaryById(versionId);
		// if (null == assetBinaryVersion) {
		// throw new
		// AssetCatalogServiceException("the system cannot find corresponding AssetBinaryVersion via versionId "
		// + versionId);
		// }
		//
		// for (Long categoryId : categoryIds) {
		// Category category = applicationService.getCategoryById(categoryId);
		// if (null == category) {
		// throw new
		// AssetCatalogServiceException("the system cannot find corresponding Category via categoryId "
		// + categoryId);
		// }
		// }

		// applicationService.associateCategory(assetId, versionId,
		// categoryIds);
		StringBuffer str = new StringBuffer("");
		if (null != categoryIds) {
			str.append(",categoryIds = [");
			for (Long categoryId : categoryIds) {
				str.append(categoryId).append(",");
			}
			str.deleteCharAt(str.length() - 1).append("]");
		}
		log.debug("enter method addAssetIntoCategories: assetId = " + assetId + ",versionId = " + versionId + str.toString());
		try {
			applicationService.associateCategory(assetId, versionId, categoryIds);

		} catch (Exception e) {
			e.printStackTrace();
			log.error("Comment exception: " + e);
			throw new AssetCatalogServiceException(e);
		}
	}

	public void addAssetIntoCategory(Long assetId, Long versionId, Long categoryId) throws AssetCatalogServiceException {
		// Asset asset = applicationService.getAsset(assetId);
		// if (null == asset) {
		// throw new
		// AssetCatalogServiceException("the system cannot find corresponding Asset via assetId "
		// + assetId);
		// }
		//
		// AssetBinaryVersion assetBinaryVersion =
		// applicationService.getAssetBinaryById(versionId);
		// if (null == assetBinaryVersion) {
		// throw new
		// AssetCatalogServiceException("the system cannot find corresponding AssetBinaryVersion via versionId "
		// + versionId);
		// }
		//
		// Category category = applicationService.getCategoryById(categoryId);
		// if (null == category) {
		// throw new
		// AssetCatalogServiceException("the system cannot find corresponding Category via categoryId "
		// + categoryId);
		// }
		log.debug("enter method addAssetIntoCategory: assetId = " + assetId + ",versionId = " + versionId + ",categoryId = " + categoryId);
		try {
			applicationService.associateCategory(assetId, versionId, categoryId);

		} catch (Exception e) {
			e.printStackTrace();
			log.error("Comment exception: " + e);
			throw new AssetCatalogServiceException(e);
		}
	}

	public void addAssetProvider(AssetProvider provider) throws AssetCatalogServiceException {
		log.debug("enter method addAssetProvider: " + provider.toString());
		try {
			if (!(provider instanceof AssetProviderImpl)) {
				throw new AssetCatalogServiceException("The method parameter provider is not a instance of AssetProviderImpl");
			}

			if (null == provider.getName()) {
				throw new AssetCatalogServiceException("com.hp.sdf.ngp.model.Provider.name is not-null property");
			}

			AssetProviderImpl assetProviderImpl = (AssetProviderImpl) provider;
			Provider modelProvider = assetProviderImpl.getAssetProvider();

			applicationService.saveAssetProvider(modelProvider);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Comment exception: " + e);
			throw new AssetCatalogServiceException(e);
		}

	}

	public void addAttribute(EntityType entityType, Long objectId, String attributeName, String value) throws AssetCatalogServiceException {
		log.debug("enter method String's addAttribute: entityType = '" + entityType.name() + "',attributeName = '" + attributeName + "',value = '" + value + "'");
		try {
			applicationService.addAttribute(objectId, entityType, attributeName, value);

		} catch (Exception e) {
			e.printStackTrace();
			log.error("Comment exception: " + e);
			throw new AssetCatalogServiceException(e);
		}
	}

	public void addAttribute(EntityType entityType, Long objectId, String attributeName, Float value) throws AssetCatalogServiceException {
		log.debug("enter method Number's addAttribute: entityType = '" + entityType.name() + "',attributeName = '" + attributeName + "',value = '" + value + "'");
		try {
			applicationService.addAttribute(objectId, entityType, attributeName, value);

		} catch (Exception e) {
			e.printStackTrace();
			log.error("Comment exception: " + e);
			throw new AssetCatalogServiceException(e);
		}
	}

	public void addAttribute(EntityType entityType, Long objectId, String attributeName, Date value) throws AssetCatalogServiceException {
		log.debug("enter method Date's addAttribute: entityType = '" + entityType.name() + "',attributeName = '" + attributeName + "',value = '" + value + "'");
		try {
			applicationService.addAttribute(objectId, entityType, attributeName, value);

		} catch (Exception e) {
			e.printStackTrace();
			log.error("Comment exception: " + e);
			throw new AssetCatalogServiceException(e);
		}
	}

	public void addAttributes(EntityType entityType, Long objectId, AttributeList attributes) throws AssetCatalogServiceException {
		log.debug("enter method addAttributes: entityType = '" + entityType.name() + "',objectId = " + objectId);
		try {
			if (null != attributes) {
				Map<String, List<Object>> object_map = attributes.getAttributes();
				if (null != object_map) {
					for (String name : object_map.keySet()) {
						List<Object> value_list = object_map.get(name);
						log.debug("enter method addAttributes: key = '" + name + "'");
						if (null != value_list && value_list.size() > 0) {
							for (int i = 0; i < value_list.size(); i++) {
								Object object = value_list.get(i);
								log.debug("enter method addAttributes for key " + name + " : value = '" + object + "'");
								applicationService.addAttribute(objectId, entityType, name, object);

							}
						}
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			log.error("Comment exception: " + e);
			throw new AssetCatalogServiceException(e);
		}
	}

	public Long addBinaryVersion(BinaryVersion binaryVersion) throws AssetCatalogServiceException {
		log.debug("enter method addBinaryVersion: " + binaryVersion.toString());
		try {
			if (!(binaryVersion instanceof BinaryVersionImpl)) {
				throw new AssetCatalogServiceException("The method parameter binaryVersion is not a instance of BinaryVersionImpl");
			}

			BinaryVersionImpl binaryVersionImpl = (BinaryVersionImpl) binaryVersion;
			AssetBinaryVersion assetBinaryVersion = binaryVersionImpl.getAssetBinaryVersion();

			Long assetId = binaryVersion.getAssetId();
			if (null != assetId && 0 != assetId) {
				Asset asset = applicationService.getAsset(assetId);
				if (null != asset) {
					assetBinaryVersion.setAsset(asset);
				} else {
					throw new AssetCatalogServiceException("the system cannot find corresponding Asset via assetId " + assetId);
				}
			} else {
				throw new AssetCatalogServiceException("assetId can not be null");
			}

			String status = binaryVersion.getStatus();
			if (null != status) {
				Status modelStatus = applicationService.getStatusByName(status);
				if (null != modelStatus) {
					assetBinaryVersion.setStatus(modelStatus);
				} else {
					throw new AssetCatalogServiceException("the system cannot find corresponding Status via status " + status);
				}
			}

			long picName = System.currentTimeMillis();
			String fileName = binaryVersionImpl.getFileName();
			if (null != fileName) {
				binaryVersionImpl.setFileName(picName + fileName);
			}

			byte[] b = binaryVersionImpl.getContent();
			if (null != b) {
				assetBinaryVersion.setFileSize(new BigDecimal(b.length));
			}

			applicationService.saveAssetBinary(b, assetBinaryVersion);

			Set<String> tagList = binaryVersion.getTags();
			if (tagList != null && tagList.size() > 0) {
				Set<Long> tagIdSet = new HashSet<Long>();
				for (String tagName : tagList) {
					Tag tag = applicationService.getTagByName(tagName);
					if (tag == null) {
						tag = new Tag(tagName);
						applicationService.saveTag(tag);
					}
					tagIdSet.add(tag.getId());
				}
				applicationService.associateTagRelation(assetId, assetBinaryVersion.getId(), tagIdSet);
			}

			Long bianryId = assetBinaryVersion.getId();
			byte[] bigThumbnail = binaryVersionImpl.getBigThumbnail();
			if (null != bigThumbnail && null != binaryVersionImpl.getBigName()) {
				applicationService.saveAssetBinaryPicture(bianryId, bigThumbnail, AssetPictureType.THUMBNAILBIGIMAGE, picName + "." + binaryVersionImpl.getBigName());
			}

			byte[] medThumbnail = binaryVersionImpl.getMedThumbnail();
			if (null != medThumbnail && null != binaryVersionImpl.getMedName()) {
				applicationService.saveAssetBinaryPicture(bianryId, medThumbnail, AssetPictureType.THUMBNAILMIDDLEIMAGE, picName + "." + binaryVersionImpl.getMedName());
			}

			byte[] thumbnail = binaryVersionImpl.getThumbnail();
			if (null != thumbnail && null != binaryVersionImpl.getThumName()) {
				applicationService.saveAssetBinaryPicture(bianryId, thumbnail, AssetPictureType.THUMBNAILIMAGE, picName + "." + binaryVersionImpl.getThumName());
			}

			log.debug("addBinaryVersion returns : " + assetBinaryVersion.getId());
			return assetBinaryVersion.getId();
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Comment exception: " + e);
			throw new AssetCatalogServiceException(e);
		}
	}

	public AssetCategory addCategory(AssetCategory category) throws AssetCatalogServiceException {
		log.debug("enter method addCategory: " + category.toString());
		try {
			if (!(category instanceof AssetCategoryImpl)) {
				throw new AssetCatalogServiceException("The method parameter category is not a instance of AssetCategoryImpl");
			}

			AssetCategoryImpl categoryImpl = (AssetCategoryImpl) category;
			Category modelCategory = categoryImpl.getCategory();

			String name = categoryImpl.getName();
			if (null == name) {
				throw new AssetCatalogServiceException("com.hp.sdf.ngp.model.Category.name is not-null property");
			}

			Long parentId = categoryImpl.getParentId();
			if (null != parentId && 0 != parentId) {
				Category pCategory = applicationService.getCategoryById(parentId);
				if (null != pCategory) {
					modelCategory.setParentCategory(pCategory);
				} else {
					throw new AssetCatalogServiceException("the system cannot find corresponding parent Category via categoryId " + parentId);
				}
			}

			applicationService.saveCategory(modelCategory);

			log.debug("addCategory returns : " + category.toString());
			return category;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Comment exception: " + e);
			throw new AssetCatalogServiceException(e);
		}
	}

	public void addPlatform(Long assetId, String platform) throws AssetCatalogServiceException {
		// Asset asset = applicationService.getAsset(assetId);
		// if (null == asset) {
		// throw new
		// AssetCatalogServiceException("the system cannot find corresponding Asset via assetId "
		// + assetId);
		// }
		log.debug("enter method addPlatform: assetId = " + assetId + ",platform = '" + platform + "'");
		try {
			Platform modelPlatform = applicationService.getPlatformByName(platform);
			if (null != modelPlatform) {
				applicationService.associatePlatform(assetId, modelPlatform.getId());
			} else {
				throw new AssetCatalogServiceException("the system cannot find corresponding Platform via platform " + platform);
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Comment exception: " + e);
			throw new AssetCatalogServiceException(e);
		}
	}

	public void addPrice(Long assetId, Long versionId, Currency currency, BigDecimal amount) throws AssetCatalogServiceException {
		log.debug("enter method addPrice: assetId = " + assetId + ",versionId = " + versionId + ",Currency = " + currency.getCurrencyCode() + ",amount = " + amount);
		try {
			Asset asset = applicationService.getAsset(assetId);
			if (null == asset) {
				throw new AssetCatalogServiceException("the system cannot find corresponding Asset via assetId " + assetId);
			}

			AssetBinaryVersion assetBinaryVersion = applicationService.getAssetBinaryById(versionId);
			if (null == assetBinaryVersion) {
				throw new AssetCatalogServiceException("the system cannot find corresponding AssetBinaryVersion via versionId " + versionId);
			}

			AssetPrice modelAssetPrice = new AssetPrice();
			modelAssetPrice.setAmount(amount);
			modelAssetPrice.setCurrency(currency.getCurrencyCode());
			modelAssetPrice.setAsset(asset);
			modelAssetPrice.setBinaryVersion(assetBinaryVersion);

			applicationService.saveAssetPrice(modelAssetPrice);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Comment exception: " + e);
			throw new AssetCatalogServiceException(e);
		}
	}

	public void addScreenshot(Screenshot screenshot) throws AssetCatalogServiceException {
		log.debug("enter method addScreenshot: " + screenshot.toString());
		try {
			if (!(screenshot instanceof ScreenshotImpl)) {
				throw new AssetCatalogServiceException("The method parameter screenshot is not a instance of ScreenshotImpl");
			}

			ScreenshotImpl screenshotImpl = (ScreenshotImpl) screenshot;
			ScreenShots screenShot = screenshotImpl.getScreenShot();

			Long assetId = screenshot.getAssetId();
			if (null != assetId) {
				Asset asset = applicationService.getAsset(assetId);
				if (null != asset) {
					screenShot.setAsset(asset);
				} else {
					throw new AssetCatalogEntityNotFoundException("the system cannot find corresponding Asset via assetId " + assetId);
				}
			} else {
				throw new AssetCatalogServiceException("com.hp.sdf.ngp.model.Asset is not-null property");
			}

			byte[] b = screenshotImpl.getContent();
			if (null == b) {
				throw new AssetCatalogServiceException("com.hp.sdf.ngp.model.Screenshot.content is not-null property");
			}

			// String fileName = new
			// SimpleDateFormat("yyyyMMddHHmm").format(Calendar
			// .getInstance().getTime());
			long picName = System.currentTimeMillis();
			applicationService.saveScreenShots(screenShot, screenshotImpl.getContent(), picName + "." + screenshotImpl.getContentName());
		} catch (EntityNotFoundException enfe) {
			log.error("Entity Not Found: " + enfe);
			throw new AssetCatalogEntityNotFoundException(enfe);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Comment exception: " + e);
			throw new AssetCatalogServiceException(e);
		}
	}

	// public void addTags(Long assetId, Set<String> tags) throws
	// AssetCatalogServiceException {
	// // inspect asset
	//		
	// if (null != tags) {
	// for (String name : tags) {
	// Tag tag = applicationService.getTagByName(name);
	//
	// if (null != tag) {
	// applicationService.associateTagRelation(tag.getId(), assetId);
	// } else {
	// throw new
	// AssetCatalogServiceException("the system cannot find corresponding Tag via name "
	// + name);
	// }
	// }
	// }
	// }

	public AssetCategory constructAssetCategory() throws AssetCatalogServiceException {
		return new AssetCategoryImpl();
	}

	public CatalogAsset constructAssetObject() throws AssetCatalogServiceException {
		return new CatalogAssetImpl();
	}

	public AssetProvider constructAssetProvider() throws AssetCatalogServiceException {
		return new AssetProviderImpl();
	}

	public AttributeList constructAttributeList() {
		return new AttributeListImpl();
	}

	public BinaryVersion constructBinaryVersion() {
		return new BinaryVersionImpl();
	}

	public Screenshot contructScreenshot() {
		return new ScreenshotImpl();
	}

	public void createAssetGroup(Long parentAssetId, List<Long> childAssetIds) throws AssetCatalogServiceException {
		// Asset pAsset = applicationService.getAsset(parentAssetId);
		// if (null == pAsset) {
		// throw new
		// AssetCatalogServiceException("the system cannot find corresponding  parentAsset via assetId "
		// + parentAssetId);
		// }
		//
		// for (Long id : childAssetIds) {
		// Asset asset = applicationService.getAsset(id);
		// if (null == asset) {
		// throw new
		// AssetCatalogServiceException("the system cannot find corresponding Asset via assetId "
		// + id);
		// }
		// }
		StringBuffer str = new StringBuffer("");
		if (null != childAssetIds) {
			str.append(",childAssetIds = [");
			for (Long childAssetId : childAssetIds) {
				str.append(childAssetId).append(",");
			}
			str.deleteCharAt(str.length() - 1).append("]");
		}
		log.debug("enter method createAssetGroup: parentAssetId =" + parentAssetId + ",childAssetIds = " + str);
		try {
			applicationService.associateAssetGroup(parentAssetId, childAssetIds);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Comment exception: " + e);
			throw new AssetCatalogServiceException(e);
		}

	}

	public void createCategoryRelationShip(Long parentCategoryId, List<Long> childCategoryIds) throws AssetCatalogServiceException {
		// Category pCategory =
		// applicationService.getCategoryById(parentCategoryId);
		// if (null == pCategory) {
		// throw new
		// AssetCatalogServiceException("the system cannot find corresponding parentCategory via categoryId "
		// + parentCategoryId);
		// }
		//
		// for (Long categoryId : childCategoryIds) {
		// Category category = applicationService.getCategoryById(categoryId);
		// if (null == category) {
		// throw new
		// AssetCatalogServiceException("the system cannot find corresponding category via categoryId "
		// + categoryId);
		// }
		// }
		StringBuffer str = new StringBuffer("");
		if (null != childCategoryIds) {
			str.append(",childCategoryIds = [");
			for (Long childCategoryId : childCategoryIds) {
				str.append(childCategoryId).append(",");
			}
			str.deleteCharAt(str.length() - 1).append("]");
		}
		log.debug("enter method createCategoryRelationShip: parentCategoryId =" + parentCategoryId + ",childCategoryIds = " + str);
		try {
			applicationService.createCategoryRelationShip(parentCategoryId, childCategoryIds);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Comment exception: " + e);
			throw new AssetCatalogServiceException(e);
		}
	}

	public void deleteAssetProvider(Long providerId) throws AssetCatalogServiceException {
		// Provider provider =
		// applicationService.getAssetProviderById(providerId);
		// if (null == provider) {
		// throw new
		// AssetCatalogServiceException("the system cannot find corresponding Provider via providerId "
		// + providerId);
		// }
		log.debug("enter method deleteAssetProvider:providerId=" + providerId);

		SearchExpression searchExpression = new SearchExpressionImpl();
		searchExpression.addCondition(new AssetProviderIdCondition(providerId, NumberComparer.EQUAL));
		List<Asset> assets = applicationService.searchAsset(searchExpression);
		if (null != assets) {
			for (Asset asset : assets) {
				asset.setAssetProvider(null);
				applicationService.saveAsset(asset);
			}
		}

		try {
			applicationService.deleteAssetProviderById(providerId);

		} catch (Exception e) {
			e.printStackTrace();
			log.error("Comment exception: " + e);
			throw new AssetCatalogServiceException(e);
		}
	}

	public void deleteBinaryVersion(Long versionId) throws AssetCatalogServiceException {
		// AssetBinaryVersion binaryVersion =
		// applicationService.getAssetBinaryById(versionId);
		// if (null == binaryVersion) {
		// throw new
		// AssetCatalogServiceException("the system cannot find corresponding AssetBinaryVersion via versionId "
		// + versionId);
		// }
		log.debug("enter method deleteBinaryVersion:versionId=" + versionId);
		try {
			applicationService.deleteAssetBinary(versionId);

		} catch (Exception e) {
			e.printStackTrace();
			log.error("Comment exception: " + e);
			throw new AssetCatalogServiceException(e);
		}
	}

	public AssetCategory deleteCategory(Long categoryId) throws AssetCatalogServiceException {
		// Category category = applicationService.getCategoryById(categoryId);
		// if (null == category) {
		// throw new
		// AssetCatalogServiceException("the system cannot find corresponding Category via categoryId "
		// + categoryId);
		// }
		log.debug("enter method deleteCategory:categoryId=" + categoryId);
		try {
			applicationService.deleteCategoryById(categoryId);

		} catch (Exception e) {
			e.printStackTrace();
			log.error("Comment exception: " + e);
			throw new AssetCatalogServiceException(e);
		}
		return null;
	}

	public void deletePlatfrom(Long assetId, String platform) throws AssetCatalogServiceException {
		// Asset asset = applicationService.getAsset(assetId);
		// if (null == asset) {
		// throw new
		// AssetCatalogServiceException("the system cannot find corresponding Asset via assetId "
		// + assetId);
		// }
		log.debug("enter method deletePlatfrom:assetId=" + assetId + ",platform = '" + platform + "'");
		try {
			Platform modelPlatForm = applicationService.getPlatformByName(platform);
			if (null != modelPlatForm) {
				applicationService.disassociatePlatform(assetId, modelPlatForm.getId());
			} else {
				throw new AssetCatalogServiceException("the system cannot find corresponding Platform via platform " + platform);
			}

		} catch (NgpRuntimeException e) {
			e.printStackTrace();
			log.error("Comment exception: " + e);
			throw new AssetCatalogServiceException(e);
		}

	}

	public void deletePrice(Long assetId, Long versionId, Currency currency) throws AssetCatalogServiceException {
		log.debug("enter method deletePrice:assetId=" + assetId + ",versionId = " + versionId + ",currency=" + currency.getCurrencyCode());
		try {
			SearchExpression searchExpression = new SearchExpressionImpl();
			searchExpression.addCondition(new AssetPriceAssetIdCondition(assetId, NumberComparer.EQUAL));
			searchExpression.addCondition(new AssetPriceVersionIdCondition(versionId, NumberComparer.EQUAL));
			searchExpression.addCondition(new AssetPriceCurrencyCondition(currency.getCurrencyCode(), StringComparer.EQUAL, false, false));

			List<AssetPrice> assetPrices = applicationService.searchAssetPrice(searchExpression);
			if (null != assetPrices) {
				for (AssetPrice assetPrice : assetPrices) {
					applicationService.deleteAssetPriceById(assetPrice.getId());
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			log.error("Comment exception: " + e);
			throw new AssetCatalogServiceException(e);
		}
	}

	public void deleteScreenshot(Long screenshotId) throws AssetCatalogServiceException {
		// ScreenShots screenShots =
		// applicationService.getScreenShotsById(screenshotId);
		// if (null == screenShots) {
		// throw new
		// AssetCatalogServiceException("the system cannot find corresponding ScreenShots via screenshotId "
		// + screenshotId);
		// }
		log.debug("enter method deleteScreenshot:screenshotId=" + screenshotId);
		try {
			applicationService.deleteScreenShotsById(screenshotId);

		} catch (Exception e) {
			e.printStackTrace();
			log.error("Comment exception: " + e);
			throw new AssetCatalogServiceException(e);
		}
	}

	public CatalogAsset getAsset(Long assetId) throws AssetCatalogServiceException {
		log.debug("enter method getAsset:assetId=" + assetId);
		try {
			Asset asset = applicationService.getAsset(assetId);
			if (null != asset) {
				CatalogAssetImpl ctgAsset = new CatalogAssetImpl(asset);
				this.modelObjectFiller.fillCategoryAsset(ctgAsset);
				this.modelObjectFiller.fillCategorySubAsset(ctgAsset);

				log.debug("CatalogAsset returns : " + ctgAsset.toString());
				return ctgAsset;
			} else {
				log.debug("CatalogAsset returns null.");
				throw new AssetCatalogEntityNotFoundException("CatalogAsset returns null.");
			}

		} catch (Exception e) {
			e.printStackTrace();
			log.error("Comment exception: " + e);

			if (e instanceof AssetCatalogEntityNotFoundException) {
				throw new AssetCatalogEntityNotFoundException(e);
			}
			throw new AssetCatalogServiceException(e);
		}
	}

	public List<AssetComment> getAssetComment(Long assetId, int start, int count) throws AssetCatalogServiceException {
		log.debug("enter method getAssetComment:assetId=" + assetId + ",start = " + start + ",count = " + count);
		try {
			List<Comments> comments_list = applicationService.getAllCommentsByAssetId(assetId, start, count);
			List<AssetComment> assetComments = new ArrayList<AssetComment>();
			if (null != comments_list) {
				for (Comments comments : comments_list) {
					AssetCommentImpl assetCommentImpl = new AssetCommentImpl(comments);
					this.modelObjectFiller.fillAssetComment(assetCommentImpl);
					assetComments.add(assetCommentImpl);
				}
			}
			log.debug("getAssetComment returns size : " + assetComments.size());
			return assetComments;

		} catch (Exception e) {
			e.printStackTrace();
			log.error("Comment exception: " + e);
			throw new AssetCatalogServiceException(e);
		}
	}

	public Long getAssetCommentCount(Long assetId) throws AssetCatalogServiceException {
		log.debug("enter method getAssetCommentCount:assetId=" + assetId);
		try {
			Long count = applicationService.getAllCommentsCountByAssetId(assetId);

			log.debug("getAssetCommentCount returns : " + count);
			return count;

		} catch (Exception e) {
			e.printStackTrace();
			log.error("Comment exception: " + e);
			throw new AssetCatalogServiceException(e);
		}
	}

	public AssetProvider getAssetProvider(Long providerId) throws AssetCatalogServiceException {
		log.debug("enter method getAssetProvider:providerId=" + providerId);
		try {
			Provider provider = applicationService.getAssetProviderById(providerId);
			if (null != provider) {
				AssetProviderImpl assetProviderImpl = new AssetProviderImpl(provider);
				this.modelObjectFiller.fillAssetProvider(assetProviderImpl);

				log.debug("getAssetProvider returns : " + assetProviderImpl.toString());
				return assetProviderImpl;
			} else {
				log.debug("getAssetProvider returns null");
				throw new AssetCatalogEntityNotFoundException("getAssetProvider returns null");
			}

		} catch (Exception e) {
			e.printStackTrace();
			log.error("Comment exception: " + e);

			if (e instanceof AssetCatalogEntityNotFoundException) {
				throw new AssetCatalogEntityNotFoundException(e);
			}

			throw new AssetCatalogServiceException(e);
		}
	}

	public Long getAssetSearchResultCount(SearchExpression searchExpression) throws AssetCatalogServiceException {
		log.debug("enter method getAssetSearchResultCount:searchExpression = " + searchExpression.toString());

		try {
			List<Asset> assets = applicationService.searchAsset(searchExpression);
			if (null != assets) {
				Long count = new Long(assets.size());

				log.debug("getAssetSearchResultCount returns : " + count);
				return count;
			}

			log.debug("getAssetSearchResultCount returns 0 ");
			return new Long(0);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Comment exception: " + e);
			throw new AssetCatalogServiceException(e);
		}
	}

	public BinaryVersion getBinaryVersion(Long versionId) throws AssetCatalogServiceException {
		log.debug("enter method getBinaryVersion:versionId = " + versionId);
		try {
			AssetBinaryVersion assetBinaryVersion = applicationService.getAssetBinaryById(versionId);
			if (null != assetBinaryVersion) {
				BinaryVersionImpl binaryVersionImpl = new BinaryVersionImpl(assetBinaryVersion);
				this.modelObjectFiller.fillBinaryVersion(binaryVersionImpl);

				log.debug("getBinaryVersion returns : " + binaryVersionImpl.toString());
				return binaryVersionImpl;
			} else {
				log.debug("getBinaryVersion returns 0 ");
				throw new AssetCatalogEntityNotFoundException("getBinaryVersion returns 0 ");
			}

		} catch (Exception e) {
			e.printStackTrace();
			log.error("Comment exception: " + e);

			if (e instanceof AssetCatalogEntityNotFoundException) {
				throw new AssetCatalogEntityNotFoundException(e);
			}

			throw new AssetCatalogServiceException(e);
		}
	}

	public Long getBinaryVersionSearchResultCount(SearchExpression searchExpression) throws AssetCatalogServiceException {
		log.debug("enter method getBinaryVersionSearchResultCount:searchExpression = " + searchExpression.toString());
		try {
			List<AssetBinaryVersion> assetBinaryVersions = applicationService.getAssetBinary(searchExpression);
			if (null != assetBinaryVersions) {
				Long count = new Long(assetBinaryVersions.size());

				log.debug("getBinaryVersionSearchResultCount returns : " + count);
				return count;
			}
			log.debug("getBinaryVersionSearchResultCount returns 0 ");
			return new Long(0);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Comment exception: " + e);
			throw new AssetCatalogServiceException(e);
		}
	}

	public AssetCategory getCategory(Long categoryId) throws AssetCatalogServiceException {
		log.debug("enter method getCategory:categoryId = " + categoryId);
		try {
			Category category = applicationService.getCategoryById(categoryId);
			if (null != category) {
				AssetCategoryImpl assetCategoryImpl = new AssetCategoryImpl(category, AssetCatalogServiceImpl.this);
				this.modelObjectFiller.fillAssetCategory(assetCategoryImpl);

				log.debug("getCategory returns : " + assetCategoryImpl.toString());
				return assetCategoryImpl;
			} else {
				log.debug("getCategory returns 0 ");
				throw new AssetCatalogEntityNotFoundException("getCategory returns 0 ");
			}

		} catch (Exception e) {
			e.printStackTrace();
			log.error("Comment exception: " + e);

			if (e instanceof AssetCatalogEntityNotFoundException) {
				throw new AssetCatalogEntityNotFoundException(e);
			}

			throw new AssetCatalogServiceException(e);
		}
	}

	public void removeAssetFromAllCategories(Long assetId, Long versionId) throws AssetCatalogServiceException {
		// Asset asset = applicationService.getAsset(assetId);
		// if (null == asset) {
		// throw new
		// AssetCatalogServiceException("the system cannot find corresponding Asset via assetId "
		// + assetId);
		// }
		//
		// AssetBinaryVersion assetBinaryVersion =
		// applicationService.getAssetBinaryById(versionId);
		// if (null == assetBinaryVersion) {
		// throw new
		// AssetCatalogServiceException("the system cannot find corresponding AssetBinaryVersion via versionId "
		// + versionId);
		// }
		log.debug("enter method removeAssetFromAllCategories:assetId = " + assetId + ",versionId=" + versionId);
		try {
			applicationService.disassociateCategory(assetId, versionId);

		} catch (NgpRuntimeException e) {
			e.printStackTrace();
			log.error("Comment exception: " + e);
			throw new AssetCatalogServiceException(e);
		}
	}

	public void removeAssetFromCategory(Long assetId, Long versionId, Long categoryId) throws AssetCatalogServiceException {
		// Asset asset = applicationService.getAsset(assetId);
		// if (null == asset) {
		// throw new
		// AssetCatalogServiceException("the system cannot find corresponding Asset via assetId "
		// + assetId);
		// }
		//
		// AssetBinaryVersion assetBinaryVersion =
		// applicationService.getAssetBinaryById(versionId);
		// if (null == assetBinaryVersion) {
		// throw new
		// AssetCatalogServiceException("the system cannot find corresponding AssetBinaryVersion via versionId "
		// + versionId);
		// }
		//
		// Category category = applicationService.getCategoryById(categoryId);
		// if (null == category) {
		// throw new
		// AssetCatalogServiceException("the system cannot find corresponding Category via categoryId "
		// + categoryId);
		// }
		log.debug("enter method removeAssetFromCategory:assetId = " + assetId + ",versionId=" + versionId + ",categoryId = " + categoryId);
		try {
			applicationService.disassociateCategory(assetId, versionId, categoryId);

		} catch (NgpRuntimeException e) {
			e.printStackTrace();
			log.error("Comment exception: " + e);
			throw new AssetCatalogServiceException(e);
		}
	}

	public void removeAttributes(EntityType entityType, Long objectId, String attributeName) throws AssetCatalogServiceException {
		log.debug("enter method removeAttributes:entityType = " + entityType.name() + ",objectId=" + objectId + ",attributeName = " + attributeName);
		try {
			applicationService.removeAttributes(objectId, entityType, attributeName);

		} catch (NgpRuntimeException e) {
			e.printStackTrace();
			log.error("Comment exception: " + e);
			throw new AssetCatalogServiceException(e);
		}
	}

	public void removeTags(Long assetId, Set<String> tags) throws AssetCatalogServiceException {
		StringBuffer str = new StringBuffer("");
		if (null != tags) {
			str.append(",tags = [");
			for (String tag : tags) {
				str.append(tag).append(",");
			}
			str.deleteCharAt(str.length() - 1).append("]");
		}
		log.debug("enter method removeTags:assetId = " + assetId + ",tags=" + str.toString());
		// inspect assetId
		try {
			if (null != tags) {
				for (String name : tags) {
					Tag tag = applicationService.getTagByName(name);

					if (null != tag) {
						applicationService.disassociateTagRelation(assetId, tag.getId());
					} else {
						throw new AssetCatalogServiceException("the system cannot find corresponding tag via tag " + tag);
					}
				}
			}

		} catch (NgpRuntimeException e) {
			e.printStackTrace();
			log.error("Comment exception: " + e);
			throw new AssetCatalogServiceException(e);
		}
	}

	public List<CatalogAsset> searchAsset(SearchExpression searchExpression) throws AssetCatalogServiceException {
		log.debug("enter method searchAsset:searchExpression = " + searchExpression.toString());
		try {
			List<CatalogAsset> catalogAssets = new ArrayList<CatalogAsset>();
			List<Asset> assets = applicationService.searchAsset(searchExpression);
			if (null != assets) {
				for (Asset asset : assets) {
					CatalogAssetImpl ctgAsset = new CatalogAssetImpl(asset);
					this.modelObjectFiller.fillCategoryAsset(ctgAsset);
					this.modelObjectFiller.fillCategorySubAsset(ctgAsset);
					catalogAssets.add(ctgAsset);
				}
			}

			log.debug("searchAsset returns size : " + catalogAssets.size());
			return catalogAssets;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Comment exception: " + e);
			throw new AssetCatalogServiceException(e);
		}
	}

	public List<AssetProvider> searchAssetProvider(SearchExpression searchExpression) throws AssetCatalogServiceException {
		log.debug("enter method searchAssetProvider:searchExpression = " + searchExpression.toString());
		try {
			List<Provider> providers = applicationService.searchAssetProvider(searchExpression);
			List<AssetProvider> assetProviders = new ArrayList<AssetProvider>();
			if (null != providers) {
				for (Provider provider : providers) {
					AssetProviderImpl assetProviderImpl = new AssetProviderImpl(provider);
					this.modelObjectFiller.fillAssetProvider(assetProviderImpl);
					assetProviders.add(assetProviderImpl);
				}
			}

			log.debug("searchAssetProvider returns size:" + assetProviders.size());
			return assetProviders;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Comment exception: " + e);
			throw new AssetCatalogServiceException(e);
		}
	}

	@Transactional(isolation = Isolation.READ_UNCOMMITTED, propagation = Propagation.REQUIRES_NEW, rollbackFor = AssetCatalogServiceException.class)
	public List<BinaryVersion> searchBinaryVersions(SearchExpression searchExpression) throws AssetCatalogServiceException {
		log.debug("enter method searchBinaryVersions:searchExpression = " + searchExpression.toString());
		try {
			List<AssetBinaryVersion> assetBinaryVersions = applicationService.getAssetBinary(searchExpression);
			List<BinaryVersion> binaryVersions = new ArrayList<BinaryVersion>();
			if (null != assetBinaryVersions) {
				for (AssetBinaryVersion assetBinaryVersion : assetBinaryVersions) {
					BinaryVersionImpl binaryVersionImpl = new BinaryVersionImpl(assetBinaryVersion);
					this.modelObjectFiller.fillBinaryVersion(binaryVersionImpl);
					binaryVersions.add(binaryVersionImpl);
				}
			}

			log.debug("searchBinaryVersions returns size:" + binaryVersions.size());
			return binaryVersions;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Comment exception: " + e);
			throw new AssetCatalogServiceException(e);
		}
	}

	public void updateAsset(CatalogAsset asset) throws AssetCatalogServiceException {
		log.debug("enter method updateAsset: " + asset.toString());
		try {
			if (!(asset instanceof CatalogAssetImpl)) {
				throw new AssetCatalogServiceException("The method parameter asset is not a instance of CatalogAssetImpl");
			}

			CatalogAssetImpl assetImpl = (CatalogAssetImpl) asset;
			Asset modelAsset = assetImpl.getAsset();

			if (null == modelAsset.getId()) {
				throw new AssetCatalogServiceException("asset's primary key is null");
			}

			Long providerId = assetImpl.getProviderId();
			if (null != providerId) {
				Provider p = applicationService.getAssetProviderById(providerId);
				if (null != p) {
					modelAsset.setAssetProvider(p);
				}
			}

			String status = asset.getStatus();
			if (null != status) {
				Status modelStatus = applicationService.getStatusByName(status);
				if (null != modelStatus) {
					modelAsset.setStatus(modelStatus);
				} else {
					throw new AssetCatalogServiceException("the system cannot find corresponding Status via status " + status);
				}
			}

			// Long parentAssetId = asset.getAssetId();
			// if (null != parentAssetId && 0 != parentAssetId) {
			// Asset parentAsset = applicationService.getAsset(parentAssetId);
			// if (null != parentAsset) {
			// modelAsset.setAsset(parentAsset);
			// } else {
			// throw new
			// AssetCatalogServiceException("the system cannot find corresponding parent's Asset via assetId + "
			// + parentAssetId);
			// }
			// }

			applicationService.updateAsset(modelAsset);

			Long assetId = modelAsset.getId();
			long picName = System.currentTimeMillis();
			byte[] bigThumbnail = assetImpl.getBigThumbnail();
			if (null != bigThumbnail) {
				applicationService.saveAssetPicture(assetId, bigThumbnail, AssetPictureType.THUMBNAILBIGIMAGE, picName + "." + assetImpl.getBigName());
			}

			byte[] medThumbnail = assetImpl.getMedThumbnail();
			if (null != medThumbnail) {
				applicationService.saveAssetPicture(assetId, medThumbnail, AssetPictureType.THUMBNAILMIDDLEIMAGE, picName + "." + assetImpl.getMedName());
			}

			byte[] thumbnail = assetImpl.getThumbnail();
			if (null != thumbnail) {
				applicationService.saveAssetPicture(assetId, thumbnail, AssetPictureType.THUMBNAILIMAGE, picName + "." + assetImpl.getThumName());
			}
		} catch (EntityNotFoundException enfe) {
			log.error("Entity Not Found: " + enfe);
			throw new AssetCatalogEntityNotFoundException(enfe);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Comment exception: " + e);
			throw new AssetCatalogServiceException(e);
		}
	}

	public void updateAssetCurrentVersion(Long binaryVersionId) throws AssetCatalogServiceException {
		log.debug("enter method updateAssetCurrentVersion: binaryVersionId = " + binaryVersionId);
		AssetBinaryVersion assetBinaryVersion = applicationService.getAssetBinaryById(binaryVersionId);

		try {
			if (null != assetBinaryVersion) {
				Asset asset = assetBinaryVersion.getAsset();

				if (null != asset) {
					asset.setCurrentVersionId(binaryVersionId);
					asset.setCurrentVersion(assetBinaryVersion.getVersion());
					applicationService.updateAsset(asset);
				} else {
					throw new AssetCatalogServiceException("the system cannot find corresponding Asset");
				}
			} else {
				throw new AssetCatalogServiceException("the system cannot find corresponding AssetBinaryVersion via binaryVersionId " + binaryVersionId);
			}
		} catch (EntityNotFoundException enfe) {
			log.error("Entity Not Found: " + enfe);
			throw new AssetCatalogEntityNotFoundException(enfe);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Comment exception: " + e);
			throw new AssetCatalogServiceException(e);
		}
	}

	public void updateAssetProvider(AssetProvider provider) throws AssetCatalogServiceException {
		log.debug("enter method updateAssetProvider: " + provider.toString());
		try {
			if (!(provider instanceof AssetProviderImpl)) {
				throw new AssetCatalogServiceException("The method parameter provider is not a instance of AssetProviderImpl");
			}

			AssetProviderImpl assetProviderImpl = (AssetProviderImpl) provider;
			Provider modelProvider = assetProviderImpl.getAssetProvider();

			if (0 == modelProvider.getId()) {
				throw new AssetCatalogServiceException("provider's primary key is null");
			}

			if (null == provider.getName()) {
				throw new AssetCatalogServiceException("com.hp.sdf.ngp.model.Provider.name is not-null property");
			}

			applicationService.updateAssetProvider(modelProvider);
		} catch (EntityNotFoundException enfe) {
			log.error("Entity Not Found: " + enfe);
			throw new AssetCatalogEntityNotFoundException(enfe);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Comment exception: " + e);
			throw new AssetCatalogServiceException(e);
		}
	}

	public void updateBinaryVersion(BinaryVersion binaryVersion) throws AssetCatalogServiceException {
		log.debug("enter method updateBinaryVersion: " + binaryVersion.toString());
		try {
			if (!(binaryVersion instanceof BinaryVersionImpl)) {
				throw new AssetCatalogServiceException("The method parameter binaryVersion is not a instance of BinaryVersionImpl");
			}

			BinaryVersionImpl binaryVersionImpl = (BinaryVersionImpl) binaryVersion;
			AssetBinaryVersion assetBinaryVersion = binaryVersionImpl.getAssetBinaryVersion();

			if (null == assetBinaryVersion.getId()) {
				throw new AssetCatalogServiceException("assetBinaryVersion's primary key is null");
			}

			Long assetId = binaryVersion.getAssetId();
			if (null != assetId && 0 != assetId) {
				Asset asset = applicationService.getAsset(assetId);
				if (null != asset) {
					assetBinaryVersion.setAsset(asset);
				}
			}

			String status = binaryVersion.getStatus();
			if (null != status) {
				Status modelStatus = applicationService.getStatusByName(status);
				if (null != modelStatus) {
					assetBinaryVersion.setStatus(modelStatus);
				} else {
					throw new AssetCatalogServiceException("the system cannot find corresponding Status via status " + status);
				}
			}

			long picName = System.currentTimeMillis();
			String fileName = binaryVersionImpl.getFileName();
			if (null != fileName) {
				binaryVersionImpl.setFileName(picName + fileName);

				AssetBinaryVersion assetBinaryVesrion = binaryVersionImpl.getAssetBinaryVersion();
				String location = assetBinaryVesrion.getLocation();
				String newLocation = location.substring(0, location.lastIndexOf("/") + 1) + binaryVersionImpl.getFileName();
				assetBinaryVesrion.setLocation(newLocation);
			}

			applicationService.updateBinaryVersion(assetBinaryVersion);

			byte[] b = binaryVersionImpl.getContent();
			if (null != b) {
				assetBinaryVersion.setFileSize(new BigDecimal(b.length));
				applicationService.updateAssetBinary(b, assetBinaryVersion.getId());
			}

			Long bianryId = assetBinaryVersion.getId();
			byte[] bigThumbnail = binaryVersionImpl.getBigThumbnail();
			if (null != bigThumbnail) {
				applicationService.saveAssetBinaryPicture(bianryId, bigThumbnail, AssetPictureType.THUMBNAILBIGIMAGE, picName + "." + binaryVersionImpl.getBigName());
			}

			byte[] medThumbnail = binaryVersionImpl.getMedThumbnail();
			if (null != medThumbnail) {
				applicationService.saveAssetBinaryPicture(bianryId, medThumbnail, AssetPictureType.THUMBNAILMIDDLEIMAGE, picName + "." + binaryVersionImpl.getMedName());
			}

			byte[] thumbnail = binaryVersionImpl.getThumbnail();
			if (null != thumbnail) {
				applicationService.saveAssetBinaryPicture(bianryId, thumbnail, AssetPictureType.THUMBNAILIMAGE, picName + "." + binaryVersionImpl.getThumName());
			}

			binaryVersionImpl.cleanAfterUpdate();

		} catch (EntityNotFoundException enfe) {
			log.error("Entity Not Found: " + enfe);
			throw new AssetCatalogEntityNotFoundException(enfe);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Comment exception: " + e);
			throw new AssetCatalogServiceException(e);
		}
	}

	public AssetCategory updateCategory(AssetCategory category) throws AssetCatalogServiceException {
		log.debug("enter method updateCategory: " + category.toString());
		try {
			if (!(category instanceof AssetCategoryImpl)) {
				throw new AssetCatalogServiceException("The method parameter category is not a instance of AssetCategoryImpl");
			}

			AssetCategoryImpl categoryImpl = (AssetCategoryImpl) category;
			Category modelCategory = categoryImpl.getCategory();

			if (null == modelCategory.getId()) {
				throw new AssetCatalogServiceException("modelCategory's primary key is null");
			}

			String name = category.getName();
			if (null == name) {
				throw new AssetCatalogServiceException("com.hp.sdf.ngp.model.Category.name is not-null property");
			}

			Long parentId = categoryImpl.getParentId();
			if (null != parentId && 0 != parentId) {
				Category pCategory = applicationService.getCategoryById(parentId);
				if (null != pCategory) {
					modelCategory.setParentCategory(pCategory);
				} else {
					throw new AssetCatalogServiceException("the system cannot find corresponding parent Category via categoryId " + parentId);
				}
			}

			applicationService.updateCategory(modelCategory);

			log.debug("updateCategory returns : " + category.toString());
			return category;
		} catch (EntityNotFoundException enfe) {
			log.error("Entity Not Found: " + enfe);
			throw new AssetCatalogEntityNotFoundException(enfe);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Comment exception: " + e);
			throw new AssetCatalogServiceException(e);
		}
	}

	public List<AssetCategory> searchCategory(SearchExpression searchExpression) throws AssetCatalogServiceException {
		log.debug("enter method searchCategory: searchExpression " + searchExpression.toString());
		try {
			List<Category> categorys = applicationService.searchCategory(searchExpression);
			List<AssetCategory> assetCategorys = new ArrayList<AssetCategory>();
			for (Category category : categorys) {
				AssetCategoryImpl assetCategoryImpl = new AssetCategoryImpl(category, AssetCatalogServiceImpl.this);
				this.modelObjectFiller.fillAssetCategory(assetCategoryImpl);
				assetCategorys.add(assetCategoryImpl);
			}

			log.debug("searchCategory returns : " + assetCategorys.toString());
			return assetCategorys;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Comment exception: " + e);
			throw new AssetCatalogServiceException(e);
		}
	}

	public Long addAssetLifecycleActionHistory(AssetLifecycleActionHistory assetLifecycleActionHistory) throws AssetCatalogServiceException {
		log.debug("enter method addAssetLifecycleActionHistory: " + assetLifecycleActionHistory.toString());
		try {
			if (!(assetLifecycleActionHistory instanceof AssetLifecycleActionHistoryImpl)) {
				throw new AssetCatalogServiceException("The method parameter assetLifecycleActionHistory is not a instance of AssetLifecycleActionHistoryImpl");
			}

			AssetLifecycleActionHistoryImpl assetLifecycleActionHistoryImpl = (AssetLifecycleActionHistoryImpl) assetLifecycleActionHistory;
			com.hp.sdf.ngp.model.AssetLifecycleActionHistory assetLifecycleAction = assetLifecycleActionHistoryImpl.getAssetLifecycleActionHistory();

			// Long assetId = assetLifecycleActionHistory.getAssetId();
			// if (null != assetId) {
			// Asset asset = applicationService.getAsset(assetId);
			// assetLifecycleAction.setAsset(asset);
			// }
			//
			// Long binaryVersionId = assetLifecycleActionHistory
			// .getAssetBinaryVersionId();
			// if (null != binaryVersionId) {
			// AssetBinaryVersion assetBinaryVersion = applicationService
			// .getAssetBinaryById(binaryVersionId);
			// assetLifecycleAction.setBinaryVersion(assetBinaryVersion);
			// }
			//
			// String postStatus = assetLifecycleActionHistory.getPostStatus();
			// if (null != postStatus) {
			// Status post_status =
			// applicationService.getStatusByName(postStatus);
			// assetLifecycleAction.setPostStatus(post_status);
			// }
			//
			// String preStatus = assetLifecycleActionHistory.getPreStatus();
			// if (null != preStatus) {
			// Status pre_status =
			// applicationService.getStatusByName(preStatus);
			// assetLifecycleAction.setPreStatus(pre_status);
			// }
			applicationService.saveOrUpdateAssetLifecycleActionHistory(assetLifecycleAction);
			// applicationService.
			// .saveOrUpdateAssetLifecycleAction(assetLifecycleAction);

			log.debug("addAssetLifecycleActionHistory returns : " + assetLifecycleAction.getId());
			return assetLifecycleAction.getId();
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Comment exception: " + e);
			throw new AssetCatalogServiceException(e);
		}
	}

	public AssetLifecycleActionHistory constructAssetLifecycleActionHistory() throws AssetCatalogServiceException {
		return new AssetLifecycleActionHistoryImpl();
	}

	public List<AssetLifecycleActionHistory> searchAssetLifecycleActionHistory(SearchExpression searchExpression) throws AssetCatalogServiceException {
		log.debug("enter method searchAssetLifecycleActionHistory:searchExpression " + searchExpression.toString());
		// List<AssetLifecycleAction> assetLifecycleActions = applicationService
		// .getAssetLifecycleAction(searchExpresstion);
		try {
			List<com.hp.sdf.ngp.model.AssetLifecycleActionHistory> assetLifecycleActionHistorys = applicationService.searchAssetLifecycleActionHistory(searchExpression);

			List<AssetLifecycleActionHistory> historyList = new ArrayList<AssetLifecycleActionHistory>();
			for (com.hp.sdf.ngp.model.AssetLifecycleActionHistory assetLifecyclActionHistory : assetLifecycleActionHistorys) {
				AssetLifecycleActionHistoryImpl historyImpl = new AssetLifecycleActionHistoryImpl(assetLifecyclActionHistory);
				// fillAssetLife(historyImpl);
				historyList.add(historyImpl);
			}

			log.debug("searchAssetLifecycleActionHistory returns length : " + historyList.size());
			return historyList;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Comment exception: " + e);
			throw new AssetCatalogServiceException(e);
		}
	}

	public void updateAssetLifecycleActionHistory(AssetLifecycleActionHistory assetLifecycleActionHistory) throws AssetCatalogServiceException {
		log.debug("enter method updateAssetLifecycleActionHistory:assetLifecycleActionHistory " + assetLifecycleActionHistory.toString());
		try {
			if (!(assetLifecycleActionHistory instanceof AssetLifecycleActionHistoryImpl)) {
				throw new AssetCatalogServiceException("The method parameter assetLifecycleActionHistory is not a instance of AssetLifecycleActionHistoryImpl");
			}

			if (null == assetLifecycleActionHistory.getId()) {
				throw new AssetCatalogServiceException("assetLifecycleActionHistory's primary key is null");
			}

			AssetLifecycleActionHistoryImpl assetLifecycleActionHistoryImpl = (AssetLifecycleActionHistoryImpl) assetLifecycleActionHistory;
			com.hp.sdf.ngp.model.AssetLifecycleActionHistory assetLifecycleAction = assetLifecycleActionHistoryImpl.getAssetLifecycleActionHistory();

			// Long assetId = assetLifecycleActionHistory.getAssetId();
			// if (null != assetId) {
			// Asset asset = applicationService.getAsset(assetId);
			// assetLifecycleAction.setAsset(asset);
			// }
			//
			// Long binaryVersionId = assetLifecycleActionHistory
			// .getAssetBinaryVersionId();
			// if (null != binaryVersionId) {
			// AssetBinaryVersion assetBinaryVersion = applicationService
			// .getAssetBinaryById(binaryVersionId);
			// assetLifecycleAction.setBinaryVersion(assetBinaryVersion);
			// }
			//
			// String postStatus = assetLifecycleActionHistory.getPostStatus();
			// if (null != postStatus) {
			// Status post_status =
			// applicationService.getStatusByName(postStatus);
			// assetLifecycleAction.setPostStatus(post_status);
			// }
			//
			// String preStatus = assetLifecycleActionHistory.getPreStatus();
			// if (null != preStatus) {
			// Status pre_status =
			// applicationService.getStatusByName(preStatus);
			// assetLifecycleAction.setPreStatus(pre_status);
			// }

			// applicationService
			// .saveOrUpdateAssetLifecycleAction(assetLifecycleAction);

			applicationService.saveOrUpdateAssetLifecycleActionHistory(assetLifecycleAction);
		} catch (EntityNotFoundException enfe) {
			log.error("Entity Not Found: " + enfe);
			throw new AssetCatalogEntityNotFoundException(enfe);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Comment exception: " + e);
			throw new AssetCatalogServiceException(e);
		}
	}

	public List<AssetComment> searchAssetComments(SearchExpression searchExpression) throws AssetCatalogServiceException {
		log.debug("enter method searchAssetComments:searchExpression= " + searchExpression.toString());
		try {
			List<AssetComment> assetComments = new ArrayList<AssetComment>();
			List<Comments> commentss = applicationService.searchComments(searchExpression);
			if (null != commentss) {
				for (Comments comments : commentss) {
					AssetCommentImpl assetCommentImpl = new AssetCommentImpl(comments);
					this.modelObjectFiller.fillAssetComment(assetCommentImpl);
					assetComments.add(assetCommentImpl);
				}
			}

			log.debug("searchAssetComments returns length:" + assetComments.size());
			return assetComments;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Comment exception: " + e);
			throw new AssetCatalogServiceException(e);
		}
	}

	public void upgradeAssetCurrentVersion(Long assetId, Long binaryVersionId) throws AssetCatalogServiceException {
		log.debug("enter method upgradeAssetCurrentVersion:assetId=" + assetId + ",binaryVersionId=" + binaryVersionId);
		// inspect assetId,binaryVersionId
		try {
			applicationService.updateAssetVersion(assetId, binaryVersionId);
		} catch (EntityNotFoundException enfe) {
			log.error("Entity Not Found: " + enfe);
			throw new AssetCatalogEntityNotFoundException(enfe);
		} catch (NgpRuntimeException e) {
			e.printStackTrace();
			log.error("Comment exception: " + e);
			throw new AssetCatalogServiceException(e);
		}
	}

	public void addTags(Set<String> tags) throws AssetCatalogServiceException {
		StringBuffer str = new StringBuffer("");
		if (null != tags) {
			str.append(",tags = [");
			for (String tag : tags) {
				str.append(tag).append(",");
			}
			str.deleteCharAt(str.length() - 1).append("]");
		}
		log.debug("enter method addTags:tags=" + str.toString());

		try {
			if (null != tags) {
				for (String name : tags) {
					Tag tag = new Tag();
					tag.setName(name);
					applicationService.saveTag(tag);
					// Tag tag = applicationService.getTagByName(name);
					//
					// if (null != tag) {
					// applicationService.associateTagRelation(tag.getId(),
					// assetId);
					// } else {
					// throw new
					// AssetCatalogServiceException("the system cannot find corresponding Tag via name "
					// + name);
					// }
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Comment exception: " + e);
			throw new AssetCatalogServiceException(e);
		}

	}

	public void removeTags(Set<String> tags) throws AssetCatalogServiceException {
		StringBuffer str = new StringBuffer("");
		if (null != tags) {
			str.append(",tags = [");
			for (String tag : tags) {
				str.append(tag).append(",");
			}
			str.deleteCharAt(str.length() - 1).append("]");
		}
		log.debug("enter method addTags:tags=" + str.toString());

		try {
			if (null != tags) {
				for (String name : tags) {
					Tag tag = applicationService.getTagByName(name);
					applicationService.deleteTag(tag.getId());
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			log.error("Comment exception: " + e);
			throw new AssetCatalogServiceException(e);
		}
	}

	// public void setRestrictionTypes(Long assetId, Set<RestrictionType> types)
	// throws AssetCatalogServiceException {
	// try {
	// Asset asset = applicationService.getAsset(assetId);
	//
	// if (null != types) {
	// for (RestrictionType restrictionType : types) {
	// RestrictedType restrictedType = applicationService
	// .getRestrictedTypeByType(restrictionType.getType());
	//
	// AssetRestrictionRelation assetRestrictionRelation = new
	// AssetRestrictionRelation();
	// assetRestrictionRelation.setRestrictedType(restrictedType);
	// assetRestrictionRelation.setAsset(asset);
	//
	// assetRestrictionRelationDAO.persist(assetRestrictionRelation);
	// }
	// }
	// } catch (Exception e) {
	// e.printStackTrace();
	// log.error("Comment exception: " + e);
	// throw new AssetCatalogServiceException(e);
	// }
	// }

	public void addRestrictionType(RestrictionType restrictionType) throws AssetCatalogServiceException {
		log.debug("enter method addRestrictionType:restrictionType=" + restrictionType.toString());

		try {
			if (!(restrictionType instanceof RestrictionTypeImpl)) {
				throw new AssetCatalogServiceException("The method parameter restrictionType is not a instance of RestrictionTypeImpl");
			}

			if (null == restrictionType.getType()) {
				throw new AssetCatalogServiceException("com.hp.sdf.ngp.model.RestrictionType.type is not-null property");
			}

			RestrictedType restrictedType = ((RestrictionTypeImpl) restrictionType).getRestrictedType();

			applicationService.saveRestrictedType(restrictedType);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Comment exception: " + e);
			throw new AssetCatalogServiceException(e);
		}
	}

	public RestrictionType constructRestrictionType() {
		return new RestrictionTypeImpl();
	}

	public void deleteRestrictionType(Long restrictionTypeId) throws AssetCatalogServiceException {
		log.debug("enter method deleteRestrictionType:restrictionTypeId=" + restrictionTypeId);

		try {
			applicationService.deleteRestrictedTypeById(restrictionTypeId);

		} catch (Exception e) {
			e.printStackTrace();
			log.error("Comment exception: " + e);
			throw new AssetCatalogServiceException(e);
		}
	}

	public List<RestrictionType> getAllRestrictionTypes() throws AssetCatalogServiceException {
		try {
			List<RestrictionType> restrictionTypes = new ArrayList<RestrictionType>();
			List<RestrictedType> restrictedTypes = applicationService.getAllRestrictedType(0, Integer.MAX_VALUE);
			if (null != restrictedTypes) {
				for (RestrictedType restrictedType : restrictedTypes) {
					RestrictionTypeImpl restrictionTypeImpl = new RestrictionTypeImpl(restrictedType);
					restrictionTypes.add(restrictionTypeImpl);
				}
			}
			return restrictionTypes;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Comment exception: " + e);
			throw new AssetCatalogServiceException(e);
		}
	}

	public void addUser(User user) throws AssetCatalogServiceException {
		log.debug("enter method addUser:user=" + user.toString());

		try {
			if (!(user instanceof UserImpl)) {
				throw new AssetCatalogServiceException("The method parameter user is not a instance of UserImpl");
			}

			UserProfile userProfile = ((UserImpl) user).getUserProfile();

			if (userProfile == null) {
				throw new AssetCatalogServiceException("The internal user profile object doesn't exist");
			}

			String email = userProfile.getEmail();
			if (null == email) {
				userProfile.setEmail("default.user@default.com");
			}

			userService.saveUser(userProfile);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Comment exception: " + e);
			throw new AssetCatalogServiceException(e);
		}
	}

	public void addUserToProvider(String userId, Long providerId) throws AssetCatalogServiceException {
		log.debug("enter method addUser:userId=" + userId + ",providerId=" + providerId);

		try {
			SearchExpression searchExpression = new SearchExpressionImpl();
			searchExpression.addCondition(new ContentProviderOperatorUseridCondition(userId, StringComparer.EQUAL, false, false));
			searchExpression.addCondition(new ContentProviderOperatorAssetProviderIdCondition(providerId, NumberComparer.EQUAL));
			long count = applicationService.searchContentProviderOperatorCount(searchExpression);
			if (count == 0) {
				Provider assetProvider = new Provider(providerId);
				ContentProviderOperator contentProviderOperator = new ContentProviderOperator();
				contentProviderOperator.setUserid(userId);
				contentProviderOperator.setAssetProvider(assetProvider);
				applicationService.saveContentProviderOperator(contentProviderOperator);
			}

		} catch (Exception e) {
			e.printStackTrace();
			log.error("Comment exception: " + e);
			throw new AssetCatalogServiceException(e);
		}
	}

	public void assignRole(String userId, String roleName) throws AssetCatalogServiceException {
		log.debug("enter method assignRole:userId=" + userId + ",roleName=" + roleName);

		try {
			boolean flag = userService.assignRole(userId, roleName);
			if (!flag) {
				throw new AssetCatalogServiceException("userId or roleId is null");
			}

		} catch (Exception e) {
			e.printStackTrace();
			log.error("Comment exception: " + e);
			throw new AssetCatalogServiceException(e);
		}
	}

	public User constructUser() throws AssetCatalogServiceException {
		return new UserImpl();
	}

	public void deleteUser(String userId) throws AssetCatalogServiceException {
		log.debug("enter method deleteUser:userId=" + userId);

		try {
			userService.deleteUser(userId);

		} catch (Exception e) {
			e.printStackTrace();
			log.error("Comment exception: " + e);
			throw new AssetCatalogServiceException(e);
		}
	}

	public User getUser(String userId) throws AssetCatalogServiceException {
		log.debug("enter method getUser:userId=" + userId);

		try {
			UserProfile userProfile = userService.getUser(userId);
			UserImpl user = new UserImpl(userProfile);
			this.modelObjectFiller.fillUser(user);
			log.debug("getUser returns:" + user.toString());
			if (user == null) {
				log.error("Entity Not Found." + userId);
				throw new AssetCatalogEntityNotFoundException("Entity Not Found." + userId);
			}
			return user;

		} catch (Exception e) {
			e.printStackTrace();
			log.error("Comment exception: " + e);

			if (e instanceof AssetCatalogEntityNotFoundException) {
				throw new AssetCatalogEntityNotFoundException(e);
			}

			throw new AssetCatalogServiceException(e);
		}
	}

	public void removeUserFromProvider(String userId, Long providerId) throws AssetCatalogServiceException {
		log.debug("enter method removeUserFromProvider:userId=" + userId + ",providerId=" + providerId);

		try {
			SearchExpression searchExpression = new SearchExpressionImpl();
			searchExpression.addCondition(new ContentProviderOperatorUseridCondition(userId, StringComparer.EQUAL, false, false));
			searchExpression.addCondition(new ContentProviderOperatorAssetProviderIdCondition(providerId, NumberComparer.EQUAL));

			List<ContentProviderOperator> contentProviderOperators = applicationService.searchContentProviderOperator(searchExpression);
			if (null != contentProviderOperators) {
				for (ContentProviderOperator contentProviderOperator : contentProviderOperators) {
					if (contentProviderOperator != null) {
						applicationService.deleteContentProviderOperator(contentProviderOperator.getId());
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			log.error("Comment exception: " + e);
			throw new AssetCatalogServiceException(e);
		}
	}

	public void unassignRole(String userId, String roleName) throws AssetCatalogServiceException {
		log.debug("enter method unassignRole:userId=" + userId + ",roleName=" + roleName);

		try {
			boolean flag = userService.removeRole(userId, roleName);
			if (!flag) {
				throw new AssetCatalogServiceException("userId or roleId is null");
			}

		} catch (Exception e) {
			e.printStackTrace();
			log.error("Comment exception: " + e);
			throw new AssetCatalogServiceException(e);
		}

	}

	public void updateUser(User user) throws AssetCatalogServiceException {
		log.debug("enter method updateUser:" + user);

		try {
			if (!(user instanceof UserImpl)) {
				throw new AssetCatalogServiceException("The method parameter user is not a instance of UserImpl");
			}

			UserProfile userProfile = ((UserImpl) user).getUserProfile();

			if (userProfile == null) {
				throw new AssetCatalogServiceException("The internal userprofile object doesn't exist");
			}
			String email = userProfile.getEmail();
			if (null == email) {
				userProfile.setEmail("");
			}

			userService.updateUser(userProfile);
		} catch (EntityNotFoundException enfe) {
			log.error("Entity Not Found: " + enfe);
			throw new AssetCatalogEntityNotFoundException(enfe);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Comment exception: " + e);
			throw new AssetCatalogServiceException(e);
		}
	}

	public List<User> getUsersByProvider(Long providerId) throws AssetCatalogServiceException {
		log.debug("enter method getUsersByProvider:providerId=" + providerId);

		try {
			List<User> users = new ArrayList<User>();
			SearchExpression searchExpression = new SearchExpressionImpl();
			searchExpression.addCondition(new ContentProviderOperatorAssetProviderIdCondition(providerId, NumberComparer.EQUAL));

			List<ContentProviderOperator> contentProviderOperators = applicationService.searchContentProviderOperator(searchExpression);
			if (null != users) {
				for (ContentProviderOperator contentProviderOperator : contentProviderOperators) {
					UserProfile userProfile = userService.getUser(contentProviderOperator.getUserid());
					UserImpl userImpl = new UserImpl(userProfile);
					this.modelObjectFiller.fillUser(userImpl);
					users.add(userImpl);
				}
			}

			log.debug("getUsersByProvider returns size:" + users.size());
			return users;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Comment exception: " + e);
			throw new AssetCatalogServiceException(e);
		}
	}

	public void setRestrictionTypes(Long assetId, Long versionId, Set<Long> restrictionTypeIds) throws AssetCatalogServiceException {
		if (log.isDebugEnabled()) {
			StringBuffer str = new StringBuffer("");
			if (null != restrictionTypeIds) {
				str.append(",restrictionTypeIds = [");
				for (Long restrictionTypeId : restrictionTypeIds) {
					str.append(restrictionTypeId).append(",");
				}
				str.deleteCharAt(str.length() - 1).append("]");
			}
			log.debug("enter method setRestrictionTypes:assetId=" + assetId + ",restrictionTypeIds=" + str.toString());
		}
		try {
			applicationService.associateRestrictedTypes(assetId, versionId, restrictionTypeIds);
		} catch (Exception e) {
			throw new AssetCatalogServiceException(e);
		}

	}

	public void updateUserPassword(String userId, String password) throws AssetCatalogServiceException {
		log.debug("enter method updateUserPassword:" + "userId=" + userId + ", password=" + password);

		try {
			if (userId == null || password == null) {
				throw new AssetCatalogServiceException("The method parameter user is not valid.");
			}

			userService.updatePassword(userId, password);
		} catch (EntityNotFoundException enfe) {
			log.error("Entity Not Found: " + enfe);
			throw new AssetCatalogEntityNotFoundException(enfe);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Comment exception: " + e);
			throw new AssetCatalogServiceException(e);
		}

	}

	public String getStoreLocationPrefix() throws AssetCatalogServiceException {
		return applicationService.getFilePath();
	}
	
	@Transactional
	public void recordAssetDownloadHistory(String userId, Long assetId, String version) 
	    throws AssetCatalogServiceException {
		log.debug("enter recordAssetDownloadHistory:userId=" + userId + ",assetId=" + assetId + ",version=" + version);
		try {
			com.hp.sdf.ngp.model.Asset asset = applicationService.getAsset(assetId);

			UserDownloadHistory userDownloadHistory = new UserDownloadHistory();
			userDownloadHistory.setUserid(userId);
			userDownloadHistory.setAsset(asset);

			Status status = asset.getStatus();
			if (null != status) {
				userDownloadHistory.setStatus(status.getStatus());
			} else {
				assetLifeCycleEngine.getStartupStatus();
			}

			userDownloadHistory.setVersion(version);
			
			applicationService.saveOrUpdateUserDownloadHistory(userDownloadHistory);

			long downloadCount = asset.getDownloadCount() + 1;
			asset.setDownloadCount(downloadCount);
			applicationService.updateAsset(asset);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Comment exception: " + e);
			throw new AssetCatalogServiceException(e);
		}
	}
}

// $Id$