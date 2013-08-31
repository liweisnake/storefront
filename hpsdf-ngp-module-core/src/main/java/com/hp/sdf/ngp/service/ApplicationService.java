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
package com.hp.sdf.ngp.service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.hp.sdf.ngp.api.comon.EntityType;
import com.hp.sdf.ngp.api.search.SearchExpression;
import com.hp.sdf.ngp.common.exception.DeleteFileFailureException;
import com.hp.sdf.ngp.common.exception.NgpRuntimeException;
import com.hp.sdf.ngp.common.exception.SaveFileFailureException;
import com.hp.sdf.ngp.eav.model.AttributeValue;
import com.hp.sdf.ngp.model.ApiKey;
import com.hp.sdf.ngp.model.Asset;
import com.hp.sdf.ngp.model.AssetBinaryVersion;
import com.hp.sdf.ngp.model.AssetLifecycleAction;
import com.hp.sdf.ngp.model.AssetLifecycleActionHistory;
import com.hp.sdf.ngp.model.AssetPrice;
import com.hp.sdf.ngp.model.AssetRating;
import com.hp.sdf.ngp.model.AssetRestrictionRelation;
import com.hp.sdf.ngp.model.Category;
import com.hp.sdf.ngp.model.Comments;
import com.hp.sdf.ngp.model.CommentsSensorWord;
import com.hp.sdf.ngp.model.ContentProviderOperator;
import com.hp.sdf.ngp.model.Platform;
import com.hp.sdf.ngp.model.Provider;
import com.hp.sdf.ngp.model.RestrictedType;
import com.hp.sdf.ngp.model.ScreenShots;
import com.hp.sdf.ngp.model.Service;
import com.hp.sdf.ngp.model.Status;
import com.hp.sdf.ngp.model.SystemConfig;
import com.hp.sdf.ngp.model.Tag;
import com.hp.sdf.ngp.model.UserDownloadHistory;

public interface ApplicationService extends java.io.Serializable {

	public void saveAsset(Asset asset, List<Long> categoryIds);

	public void saveAsset(Asset asset);

	public void saveContentProviderOperator(ContentProviderOperator contentProviderOperator);

	public void deleteContentProviderOperator(Long id);

	public List<ContentProviderOperator> searchContentProviderOperator(SearchExpression searchExpression);

	public long searchContentProviderOperatorCount(SearchExpression searchExpression);

	public void addAttribute(Object objectId, EntityType entityType, String attributeName, Object value) throws NgpRuntimeException;

	public List<AttributeValue> getAttributeValue(Object objectId, EntityType entityType, String attributeName);

	public Map<String, List<AttributeValue>> getAttributes(Object objectId, EntityType entityType);

	public void removeAttributes(Object objectId, EntityType entityType, String attributeName) throws NgpRuntimeException;

	public void updateAsset(Asset asset);

	public void deleteAsset(Long assetId) throws DeleteFileFailureException;

	public Asset getAsset(Long assetId);

	public Comments getComments(Long commentsId);

	public void associateAssetGroup(long parentAssetId, List<Long> childAssetIds) throws NgpRuntimeException;

	public void disassociateAssetGroup(List<Long> childAssetIds);

	public void saveAssetPicture(Long assetId, byte[] content, AssetPictureType pictureType, String assetPictureName) throws NgpRuntimeException;

	public Asset getAssetByBinaryId(Long binaryId);

	public List<Asset> searchAsset(SearchExpression searchExpression);

	public List<Asset> getAssetByStatus(Long statusId);

	public void saveAssetBinaryPicture(Long binaryId, byte[] content, AssetPictureType pictureType, String assetPictureName) throws NgpRuntimeException;

	public void saveAssetBinary(byte[] content, AssetBinaryVersion binaryVersion) throws SaveFileFailureException;

	public void updateAssetBinary(byte[] newBinary, Long binaryVersionId) throws SaveFileFailureException;

	public void updateBinaryVersion(AssetBinaryVersion binaryVersion);

	public void deleteAssetBinary(Long binaryVersionId) throws NgpRuntimeException;

	public List<AssetBinaryVersion> getAssetBinaryByAssetId(Long assetId);

	public List<AssetBinaryVersion> getAllBinaryVersion(int start, int last);

	public List<AssetBinaryVersion> getAssetBinary(SearchExpression searchExpression);

	public long countAllAssetBinary();

	public long countAssetBinary(SearchExpression searchExpression);

	public AssetBinaryVersion getLatestVersion(Long assetId);

	public AssetBinaryVersion getLatestVersion(Long assetId, Status status);

	public long searchAssetPageCount(SearchExpression searchExpression);

	public long countAssetBinaryByAssetId(Long assetId);

	public List<Asset> getAllAsset(int start, int max);

	public List<Asset> getMyAsset(String userId);

	public int getMyAssetPageCount(String userId);

	public void updateAssetVersion(Long assetId, Long binaryVersionId) throws NgpRuntimeException;

	public void saveComments(Comments comment);

	public void updateComments(Comments comment);

	public void deleteCommentsByAssetId(Long assetId);

	public void deleteComments(Long commentId);

	public void deleteCommentsByUserId(String userId);

	public List<Comments> getAllCommentsByAssetId(Long assetId, int start, int count);

	public List<Comments> getAllCommentsByUserId(String userId, int start, int count);

	public List<Comments> getAllComments(int start, int count);

	public long getAllCommentsCountByAssetId(Long assetId);

	public List<Comments> searchComments(SearchExpression searchExpression);

	public long countComments(SearchExpression searchExpression);

	public void saveTag(Long assetId, Tag tag);

	public void saveTag(Tag tag);

	public void updateTag(Tag tag);

	public void deleteTag(Long assetId, Long tagId, Long versionId);

	public void deleteTag(Long tagId);

	public List<Tag> getAllTags(int start, int max);

	public long getAllTagsCount();

	public List<Tag> getAllTagsByAsset(Long assetId, Long versionId, int start, int max);

	public List<Tag> getAllTagsByAssetBinary(Long versionId, int start, int max);

	public Tag getTagById(Long tagId);

	public Tag getTagByName(String name);

	public void associateTagRelation(Long tagId, Long assetId, Long versionId);

	public void associateTagRelation(Long assetId, Long versionId, Set<Long> tagIds);

	public void disassociateTagRelation(Long assetId, Long tagId, Long versionId) throws NgpRuntimeException;

	public void disassociateTagRelation(Long assetId, Long versionId);

	public void disassociateTagRelation(Long assetId);

	public void createCategoryRelationShip(long parentCategoryId, List<Long> childCategoryIds) throws NgpRuntimeException;

	public void dropCategoryRelationShip(List<Long> childCategoryIds);

	public List<Category> getAllCategory(int start, int max);

	public List<Category> getAllCategoryByAssetId(Long assetId, int start, int max);

	public List<Category> searchCategory(SearchExpression searchExpression);

	public void saveCategory(Category category);

	public Category getCategoryById(Long categoryId);

	public Category getCategoryByName(String name);

	public List<Category> getCategoryByParentCategoryId(Long parentCategoryId);

	public void updateCategory(Category category);

	public void deleteCategoryById(Long categoryId);

	public void associateCategory(Long assetId, Long binaryVersionId, Long categoryId) throws NgpRuntimeException;

	public void associateCategory(Long assetId, Long binaryVersionId, Set<Long> categoryIds);

	public void disassociateCategory(Long assetId, Long binaryVersionId, Long categoryId) throws NgpRuntimeException;

	public void disassociateCategory(Long assetId, Long binaryVersionId) throws NgpRuntimeException;

	public void disassociateCategory(Long assetId) throws NgpRuntimeException;

	public List<Platform> getAllPlatform(int start, int max);

	public long countPlatforms(SearchExpression searchExpression);

	public List<Platform> searchPlatform(SearchExpression searchExpression);

	public void savePlatform(Platform platform);

	public void updatePlatform(Platform platform);

	public void deletePlatformById(Long platformId);

	public List<Status> getAllStatus();

	public Status getStatusById(Long id);

	public Status getStatusByName(String name);

	public void saveStatus(Status status);

	public void saveApiKey(ApiKey apikey, List<Service> services, String userId);

	public AssetRating getAssetRating(String userId, Long assetId);

	public List<AssetRating> getAssetRating(String userId);

	public List<AssetRating> searchAssetRatingByAssetId(Long assetId, int start, int count);

	public void deleteAssetRating(Long assetRatingId);

	public Double saveOrUpdateAssetRating(Long assetId, String userId, Double rating);

	public AssetBinaryVersion getAssetBinaryById(Long binaryVersionId);

	public void saveOrUpdateAssetLifecycleAction(AssetLifecycleAction assetLifecycleAction);

	public long countAssetLifecycleActionResult(Long assetId, boolean positive);

	public AssetLifecycleAction getAssetLifecycleActionById(Long assetLifecycleActionId);

	public List<AssetLifecycleAction> getAssetLifecycleAction(SearchExpression searchExpression);

	public void deleteAssetLifecycleActionById(Long assetLifecycleActionId);

	public long getAssetLifecycleActionCount(Long assetId);

	public List<AssetLifecycleAction> getAssetLifecycleActionByAssetId(Long assetId);

	public void deleteUserDownloadHistoryById(Long userDownloadHistoryId);

	public void saveUserDownloadHistory(UserDownloadHistory userDownloadHistory);

	public UserDownloadHistory getUserDownloadHistoryById(Long userDownloadHistoryId);

	public List<UserDownloadHistory> getUserDownloadHistoryByUserid(String userid);

	public List<UserDownloadHistory> getUserDownloadHistoryByUseridAssetid(String userId, Long assetId);

	public List<UserDownloadHistory> getUserDownloadHistory(SearchExpression searchExpression);

	public void saveOrUpdateUserDownloadHistory(UserDownloadHistory userDownloadHistory);

	public void saveAssetPrice(AssetPrice assetPrice);

	public void updateAssetPrice(AssetPrice assetPrice);

	public List<AssetPrice> getAssetPriceByAssetId(Long assetId);

	public AssetPrice getAssetPriceById(Long id);

	public void deleteAssetPriceByAssetId(Long assetId);

	public List<AssetPrice> searchAssetPrice(SearchExpression searchExpression);

	public void deleteAssetPriceById(Long id);

	public void saveScreenShots(ScreenShots screenShot, byte[] content, String fileName) throws NgpRuntimeException;

	public void updateScreenShots(ScreenShots screenShot, byte[] content, String fileName) throws SaveFileFailureException;

	public void deleteScreenShotsById(Long id) throws NgpRuntimeException;

	public void deleteScreenShotsByAssetId(Long assetId) throws DeleteFileFailureException;

	public ScreenShots getScreenShotsById(Long id);

	public List<ScreenShots> getScreenShotsByAssetId(Long assetId);
	
	public List<ScreenShots> getScreenShotsByAssetBinaryVersionId(Long binaryVersionId);

	public void saveRestrictedType(RestrictedType restrictedType);

	public void updateRestrictedType(RestrictedType restrictedType);

	public void associateRestrictedTypes(Long assetId, Long versionId, Set<Long> restrictionTypeIds);

	public void deleteRestrictedTypeById(Long id);

	public RestrictedType getRestrictedTypeById(Long id);

	public RestrictedType getRestrictedTypeByType(String type);

	public List<Platform> getPlatformByAssetId(Long assetId);

	public Platform getPlatformByName(String name);

	public Platform getPlatformById(Long id);

	public void associatePlatform(Long assetId, Long platformId);

	public void associatePlatform(Long assetId, List<Long> platformIds);

	public void disassociatePlatform(Long assetId, Long platformId) throws NgpRuntimeException;

	public void disassociatePlatform(Long assetId);

	public void saveAssetProvider(Provider provider);

	public void updateAssetProvider(Provider provider);

	public List<Provider> searchAssetProvider(SearchExpression searchExpression);

	public Provider getAssetProviderById(long id);

	public Provider getAssetProviderByName(String name);

	public void deleteAssetProviderById(long id);

	public int searchAssetProviderCount(SearchExpression searchExpression);

	public void batchSaveAssetPrice(List<AssetPrice> assetPrices);

	public void batchUpdateAssetStatus(List<Long> assetIds, long statusId);

	public void batchUpdateAssetBinaryStatus(List<Long> binaryVersionIds, long statusId);

	public void batchAddTags(List<Long> assetIds, String tagName);

	public void batchDeleteTagRelations(List<Long> assetIds, String tagName, Long versionId);

	public void batchUpdateCategory(List<Long> assetIds, Long categoryId);

	public void batchDeleteComments(List<Long> commentId);

	public List<CommentsSensorWord> getAllCommentsSensorWord(int start, int count);

	public void saveOrUpdateCommentsSensorWord(CommentsSensorWord commentsSensorWord);

	public void batchDeleteCommentsSensorWord(List<Long> commentsSensorWordId);

	public int getAllCommentsSensorWordCount();

	public int getAllCategoryCount();

	public boolean checkCommentsSensorWordByName(String commentsSensorWord);

	public List<AssetBinaryVersion> searchAssetBinary(SearchExpression searchExpression);

	public long searchAssetBinaryPageCount(SearchExpression searchExpression);

	public void batchUpdateAssetBinaryRecommend(List<Long> binaryVersionIds, Date recommendStartDate, Date recommendDueDate, Long recommendOrder);

	public void batchUpdateAssetBinaryNewArrivalDueDate(List<Long> binaryVersionIds, Date newArrivalDueDate);

	public void batchUpdateAssetBinaryProviderCommissionRate(List<Long> binaryVersionIds, Double commissionRate);

	public List<ScreenShots> searchScreenShots(SearchExpression searchExpression);

	public int searchScreenShotsCount(SearchExpression searchExpression);

	public void updateAssetRestrictionRelation(AssetRestrictionRelation assetRestrictionRelation);

	public List<RestrictedType> getAllRestrictedType(int first, int max);

	public List<RestrictedType> getAllRestrictedTypesByAssetId(Long assetId, int first, int max) throws NgpRuntimeException;

	public void saveOrUpdateAssetLifecycleActionHistory(AssetLifecycleActionHistory assetLifecycleActionHistory);

	public List<AssetLifecycleActionHistory> searchAssetLifecycleActionHistory(SearchExpression searchExpression);

	public int searchAssetLifecycleActionHistoryCount(SearchExpression searchExpression);

	public List<SystemConfig> searchSystemConfig(SearchExpression searchExpression);

	public int searchSystemConfigCount(SearchExpression searchExpression);

	public SystemConfig getSystemConfigByKey(String key);

	public SystemConfig getSystemConfigById(Long id);

	public void saveOrUpdateSystemConfig(SystemConfig systemConfig);

	public byte[] getFileFromDatabase(String fileLocation);

	public String getFilePath();
}
