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
package com.hp.sdf.ngp.service.impl;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.hp.sdf.ngp.api.comon.EntityType;
import com.hp.sdf.ngp.api.search.SearchExpression;
import com.hp.sdf.ngp.api.search.Condition.NumberComparer;
import com.hp.sdf.ngp.api.search.Condition.StringComparer;
import com.hp.sdf.ngp.common.annotation.Value;
import com.hp.sdf.ngp.common.constant.ErrorCode;
import com.hp.sdf.ngp.common.exception.CensoredWordFoundException;
import com.hp.sdf.ngp.common.exception.DeleteFileFailureException;
import com.hp.sdf.ngp.common.exception.EntityNotFoundException;
import com.hp.sdf.ngp.common.exception.NgpRuntimeException;
import com.hp.sdf.ngp.common.exception.SaveFileFailureException;
import com.hp.sdf.ngp.common.log.NgpLog;
import com.hp.sdf.ngp.common.util.Utils;
import com.hp.sdf.ngp.dao.ApiKeyDAO;
import com.hp.sdf.ngp.dao.AssetBinaryVersionDAO;
import com.hp.sdf.ngp.dao.AssetCategoryRelationDAO;
import com.hp.sdf.ngp.dao.AssetDAO;
import com.hp.sdf.ngp.dao.AssetLifecycleActionDAO;
import com.hp.sdf.ngp.dao.AssetLifecycleActionHistoryDAO;
import com.hp.sdf.ngp.dao.AssetPlatformRelationDAO;
import com.hp.sdf.ngp.dao.AssetPriceDAO;
import com.hp.sdf.ngp.dao.AssetRatingDAO;
import com.hp.sdf.ngp.dao.AssetRestrictionRelationDAO;
import com.hp.sdf.ngp.dao.AssetTagRelationDAO;
import com.hp.sdf.ngp.dao.BinaryFileDAO;
import com.hp.sdf.ngp.dao.CategoryDAO;
import com.hp.sdf.ngp.dao.CommentsDAO;
import com.hp.sdf.ngp.dao.CommentsSensorWordDao;
import com.hp.sdf.ngp.dao.ContentProviderOperatorDAO;
import com.hp.sdf.ngp.dao.JpaDao;
import com.hp.sdf.ngp.dao.PlatformDAO;
import com.hp.sdf.ngp.dao.ProviderDAO;
import com.hp.sdf.ngp.dao.RestrictedTypeDAO;
import com.hp.sdf.ngp.dao.ScreenShotsDAO;
import com.hp.sdf.ngp.dao.StatusDAO;
import com.hp.sdf.ngp.dao.SubscriberProfileDAO;
import com.hp.sdf.ngp.dao.SystemConfigDAO;
import com.hp.sdf.ngp.dao.TagDAO;
import com.hp.sdf.ngp.dao.UserDownloadHistoryDAO;
import com.hp.sdf.ngp.dao.UserProfileDAO;
import com.hp.sdf.ngp.eav.AttributeType;
import com.hp.sdf.ngp.eav.EavRepository;
import com.hp.sdf.ngp.eav.model.Attribute;
import com.hp.sdf.ngp.eav.model.AttributeValue;
import com.hp.sdf.ngp.eav.model.Entity;
import com.hp.sdf.ngp.eav.model.EntityAttribute;
import com.hp.sdf.ngp.model.ApiKey;
import com.hp.sdf.ngp.model.Asset;
import com.hp.sdf.ngp.model.AssetBinaryVersion;
import com.hp.sdf.ngp.model.AssetCategoryRelation;
import com.hp.sdf.ngp.model.AssetLifecycleAction;
import com.hp.sdf.ngp.model.AssetLifecycleActionHistory;
import com.hp.sdf.ngp.model.AssetPlatformRelation;
import com.hp.sdf.ngp.model.AssetPrice;
import com.hp.sdf.ngp.model.AssetRating;
import com.hp.sdf.ngp.model.AssetRestrictionRelation;
import com.hp.sdf.ngp.model.AssetTagRelation;
import com.hp.sdf.ngp.model.BinaryFile;
import com.hp.sdf.ngp.model.Category;
import com.hp.sdf.ngp.model.Comments;
import com.hp.sdf.ngp.model.CommentsSensorWord;
import com.hp.sdf.ngp.model.ContentProviderOperator;
import com.hp.sdf.ngp.model.Platform;
import com.hp.sdf.ngp.model.Provider;
import com.hp.sdf.ngp.model.RestrictedType;
import com.hp.sdf.ngp.model.ScreenShots;
import com.hp.sdf.ngp.model.Service;
import com.hp.sdf.ngp.model.ServiceSubscription;
import com.hp.sdf.ngp.model.Status;
import com.hp.sdf.ngp.model.SubscriberProfile;
import com.hp.sdf.ngp.model.SystemConfig;
import com.hp.sdf.ngp.model.Tag;
import com.hp.sdf.ngp.model.UserDownloadHistory;
import com.hp.sdf.ngp.model.UserProfile;
import com.hp.sdf.ngp.search.condition.asset.AssetStatusIdCondition;
import com.hp.sdf.ngp.search.condition.assetbinaryversion.AssetBinaryVersionAssetIdCondition;
import com.hp.sdf.ngp.search.condition.assetbinaryversion.AssetBinaryVersionStatusIdCondition;
import com.hp.sdf.ngp.search.condition.assetbinaryversion.AssetBinaryVersionVersionNameCondition;
import com.hp.sdf.ngp.search.condition.assetlifecycleaction.AssetLifecycleActionAssetIdCondition;
import com.hp.sdf.ngp.search.condition.assetlifecycleaction.AssetLifecycleActionResultCondition;
import com.hp.sdf.ngp.search.condition.assetprice.AssetPriceAssetIdCondition;
import com.hp.sdf.ngp.search.condition.assetrating.AssetRatingAssetIdCondition;
import com.hp.sdf.ngp.search.condition.assetrating.AssetRatingUseridCondition;
import com.hp.sdf.ngp.search.condition.assettagrelation.AssetTagRelationAssetIdCondition;
import com.hp.sdf.ngp.search.condition.assettagrelation.AssetTagRelationTagIdCondition;
import com.hp.sdf.ngp.search.condition.assettagrelation.AssetTagRelationVersionIdCondition;
import com.hp.sdf.ngp.search.condition.category.CategoryAssetCategoryRelationAssetIdCondition;
import com.hp.sdf.ngp.search.condition.category.CategoryNameCondition;
import com.hp.sdf.ngp.search.condition.category.CategoryParentIdCondition;
import com.hp.sdf.ngp.search.condition.comments.CommentsAssetIdCondition;
import com.hp.sdf.ngp.search.condition.platform.PlatformAssetPlatformRelationAssetIdCondition;
import com.hp.sdf.ngp.search.condition.restrictedtype.RestrictedTypeTypeCondition;
import com.hp.sdf.ngp.search.condition.screenshots.ScreenShotsAssetBinaryVersionIdCondition;
import com.hp.sdf.ngp.search.condition.screenshots.ScreenShotsAssetIdCondition;
import com.hp.sdf.ngp.search.condition.status.StatusStatusCondition;
import com.hp.sdf.ngp.search.condition.tag.TagNameCondition;
import com.hp.sdf.ngp.search.condition.userdownloadhistory.UserDownloadHistoryAssetIdCondition;
import com.hp.sdf.ngp.search.condition.userdownloadhistory.UserDownloadHistoryUseridCondition;
import com.hp.sdf.ngp.search.engine.SearchExpressionImpl;
import com.hp.sdf.ngp.service.ApplicationService;
import com.hp.sdf.ngp.service.AssetPictureType;

@Component
@Transactional
public class ApplicationServiceImpl implements ApplicationService {

	private static final long serialVersionUID = 6764552615424196158L;

	private final static Log log = LogFactory.getLog(ApplicationServiceImpl.class);

	private final static Log ngpLog = LogFactory.getLog(NgpLog.class);

	@Resource
	private AssetDAO assetDao;

	@Resource
	private ApiKeyDAO apiKeyDao;

	@Resource
	private CommentsDAO commentsDao;

	@Resource
	private StatusDAO statusDao;

	@Resource
	private AssetRatingDAO assetRatingDao;

	@Resource
	private AssetLifecycleActionDAO assetLifecycleActionDao;

	@Resource
	private UserDownloadHistoryDAO userDownloadHistoryDao;

	@Resource
	private TagDAO tagDao;

	@Resource
	private AssetTagRelationDAO assetTagRelationDao;

	@Resource
	private CategoryDAO categoryDao;

	@Resource
	private AssetCategoryRelationDAO assetCategoryRelationDao;

	@Resource
	private PlatformDAO platformDao;

	@Resource
	private AssetBinaryVersionDAO binaryVersionDao;

	@Resource
	private AssetPlatformRelationDAO assetPlatformRelationDao;

	@Resource
	private RestrictedTypeDAO restrictedTypeDao;

	@Resource
	private AssetRestrictionRelationDAO assetRestrictionRelationDao;

	@Resource
	private ScreenShotsDAO screenShotsDao;

	@Resource
	private AssetPriceDAO assetPriceDao;

	@Resource
	private ProviderDAO providerDao;

	@Resource
	private BinaryFileDAO binaryFileDao;

	@Resource
	private SubscriberProfileDAO subscriberProfileDao;

	@Resource
	private CommentsSensorWordDao commentsSensorWordDao;

	@Resource
	private AssetLifecycleActionHistoryDAO assetLifecycleActionHistoryDao;

	@Resource
	private ContentProviderOperatorDAO contentProviderOperatorDao;

	@Resource
	private SystemConfigDAO systemConfigDao;

	@Resource
	private UserProfileDAO userProfileDao;

	@Resource
	private EavRepository eavRepository;

	private boolean isSaveDB;

	private String filePath;

	public boolean isSaveDB() {
		return isSaveDB;
	}

	@Value("binaryfile.database")
	public void setSaveDB(boolean isSaveDB) {
		this.isSaveDB = isSaveDB;
	}

	public String getFilePath() {
		return filePath;
	}

	@Value("file.path.prefix")
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public void deleteCommentsByAssetId(Long assetId) {
		String sqlStr = "delete from Comments c where c.asset.id = ?";
		commentsDao.executeHQLBySequenceParam(sqlStr, assetId);
	}

	public void deleteCommentsByUserId(String userId) {
		String sqlStr = "delete from Comments c where c.userid = ?";
		commentsDao.executeHQLBySequenceParam(sqlStr, userId);
	}

	public List<Comments> getAllComments(int start, int count) {
		return commentsDao.getAll(start, count);
	}

	public List<Comments> getAllCommentsByAssetId(Long assetId, int start, int count) {
		return commentsDao.findBy("asset.id", assetId, start, count);
	}

	public List<Comments> getAllCommentsByUserId(String userId, int start, int count) {
		return commentsDao.findBy("userid", userId, start, count);
	}

	public long getAllCommentsCountByAssetId(Long assetId) {
		SearchExpression searchExpression = new SearchExpressionImpl();
		searchExpression.addCondition(new CommentsAssetIdCondition(assetId, NumberComparer.EQUAL));
		return commentsDao.searchCount(searchExpression);
	}

	@Transactional(readOnly = true)
	public List<Comments> searchComments(SearchExpression searchExpression) {
		return commentsDao.search(searchExpression);
	}

	public long countComments(SearchExpression searchExpression) {
		return commentsDao.searchCount(searchExpression);
	}

	public void saveComments(Comments comment) {
		checkCensoredWords(comment.getTitle(), comment.getContent());
		comment.setCreateDate(Utils.getCurrentDate());
		comment.setUpdateDate(Utils.getCurrentDate());
		commentsDao.persist(comment);
	}

	private void checkCensoredWords(String title, String content) {
		// TODO apply cache

		List<CommentsSensorWord> wordsList = this.getAllCommentsSensorWord(0, Integer.MAX_VALUE);

		if (wordsList == null || wordsList.size() == 0) {
			return;
		}

		String specialChars = "[]\\^$.|?*+()";

		StringBuffer patternBuf = new StringBuffer();
		patternBuf.append(".*(");
		if (wordsList != null && wordsList.size() > 0) {
			for (int i = 0; i < wordsList.size(); i++) {

				String sensorWord = wordsList.get(i).getSensorWord();

				for (int j = 0; j < specialChars.length(); j++) {
					char specialChar = specialChars.charAt(j);
					if (sensorWord.contains(specialChar + "")) {
						sensorWord = StringUtils.replace(sensorWord, "" + specialChar, "\\" + specialChar);
					}
				}

				patternBuf.append("(" + sensorWord + ")");

				if (i < wordsList.size() - 1) {
					patternBuf.append("|");
				}

			}
		}
		patternBuf.append(").*");

		Pattern pattern = Pattern.compile(patternBuf.toString(), Pattern.CASE_INSENSITIVE);

		if (content != null) {
			Matcher matcher = pattern.matcher(content);
			if (matcher.find()) {
				throw new CensoredWordFoundException("Censored words found.");
			}
		}

		if (title != null) {
			Matcher matcher = pattern.matcher(title);
			if (matcher.find()) {
				throw new CensoredWordFoundException("Censored words found.");
			}
		}

	}

	public void updateComments(Comments comment) {
		checkCensoredWords(comment.getTitle(), comment.getContent());
		comment.setUpdateDate(Utils.getCurrentDate());
		commentsDao.merge(comment);
	}

	public List<Platform> getAllPlatform(int start, int max) {
		return platformDao.getAll(start, max);
	}

	public void deletePlatformById(Long platformId) {
		platformDao.remove(platformDao.findById(platformId));
	}

	public void savePlatform(Platform platform) {
		platformDao.persist(platform);
	}

	public void updatePlatform(Platform platform) {
		platformDao.merge(platform);
	}

	public void createCategoryRelationShip(long parentCategoryId, List<Long> childCategoryIds) throws NgpRuntimeException {
		if (childCategoryIds == null || childCategoryIds.size() == 0) {
			throw new NgpRuntimeException("childCategoryIds is null");
		}
		for (int i = 0; i < childCategoryIds.size(); i++) {
			Category category = categoryDao.findById(childCategoryIds.get(i));
			if (category == null) {
				throw new NgpRuntimeException("category not exist");
			}
			category.setParentCategory(new Category(parentCategoryId));
			categoryDao.merge(category);
		}
	}

	public void dropCategoryRelationShip(List<Long> childCategoryIds) {
		if (childCategoryIds == null || childCategoryIds.size() == 0) {
			log.warn("childCategoryIds is null or size equals with zero:childCategoryIds=" + childCategoryIds);
			return;
		}

		for (int i = 0; i < childCategoryIds.size(); i++) {
			Category category = categoryDao.findById(childCategoryIds.get(i));
			category.setParentCategory(null);
			categoryDao.merge(category);
		}
	}

	public void deleteCategoryById(Long categoryId) {
		// Execute SQl to remove the relation
		String sqlStr = "update Category c set c.parentCategory=null where c.parentCategory.id = ?";
		categoryDao.executeHQLBySequenceParam(sqlStr, categoryId);
		Category category = categoryDao.findById(categoryId);
		category.setParentCategory(null);
		categoryDao.remove(category);
	}

	public List<Category> getAllCategory(int start, int max) {
		return categoryDao.getAll(start, max);
	}

	@Transactional(readOnly = true)
	public List<Category> searchCategory(SearchExpression searchExpression) {
		return categoryDao.search(searchExpression);
	}

	public List<Category> getAllCategoryByAssetId(Long assetId, int start, int max) {
		SearchExpression searchExpression = new SearchExpressionImpl();
		searchExpression.addCondition(new CategoryAssetCategoryRelationAssetIdCondition(assetId, NumberComparer.EQUAL));
		searchExpression.setFirst(start);
		searchExpression.setMax(max);
		return categoryDao.search(searchExpression);
	}

	public void saveCategory(Category category) {
		categoryDao.persist(category);
	}

	public Category getCategoryById(Long categoryId) {
		return categoryDao.findById(categoryId);
	}

	public Category getCategoryByName(String name) {
		SearchExpression searchExpression = new SearchExpressionImpl();
		searchExpression.addCondition(new CategoryNameCondition(name, StringComparer.EQUAL, false, false));
		List<Category> categorys = categoryDao.search(searchExpression);
		if (categorys != null && categorys.size() > 0) {
			return categorys.get(0);
		} else {
			return null;
		}
	}

	public int getAllCategoryCount() {
		return categoryDao.getAllCount();
	}

	public List<Category> getCategoryByParentCategoryId(Long parentCategoryId) {
		SearchExpression searchExpression = new SearchExpressionImpl();
		searchExpression.addCondition(new CategoryParentIdCondition(parentCategoryId, NumberComparer.EQUAL));
		List<Category> categorys = categoryDao.search(searchExpression);
		return categorys;
	}

	public void updateCategory(Category category) {
		categoryDao.merge(category);
	}

	public void associateCategory(Long assetId, Long binaryVersionId, Set<Long> categoryIds) throws NgpRuntimeException {
		if (categoryIds == null || categoryIds.size() == 0) {
			throw new NgpRuntimeException("categoryIds is null");
		}

		Iterator iterator = categoryIds.iterator();
		while (iterator.hasNext()) {
			AssetCategoryRelation assetCategoryRealtion = new AssetCategoryRelation();
			assetCategoryRealtion.setAsset(new Asset(assetId));
			assetCategoryRealtion.setBinaryVersion(binaryVersionId == null ? null : new AssetBinaryVersion(binaryVersionId));
			assetCategoryRealtion.setCategory(new Category((Long) iterator.next()));
			assetCategoryRelationDao.persist(assetCategoryRealtion);

			this.updateBinaryVersionUpdateDate(binaryVersionId);
		}

		this.updateAssetUpdateDate(assetId);
	}

	public void associateCategory(Long assetId, Long binaryVersionId, Long categoryId) {
		AssetCategoryRelation assetCategoryRealtion = new AssetCategoryRelation();

		assetCategoryRealtion.setAsset(new Asset(assetId));
		assetCategoryRealtion.setCategory(new Category(categoryId));
		assetCategoryRealtion.setBinaryVersion(binaryVersionId == null ? null : new AssetBinaryVersion(binaryVersionId));
		assetCategoryRelationDao.persist(assetCategoryRealtion);

		this.updateAssetUpdateDate(assetId);
		this.updateBinaryVersionUpdateDate(binaryVersionId);
	}

	public void disassociateCategory(Long assetId, Long binaryVersionId, Long categoryId) throws NgpRuntimeException {
		if (assetId == null || binaryVersionId == null || categoryId == null) {
			log.warn("assetId is null or binaryVersionId is null or category is null:assetId=" + assetId + " , binaryVersionId=" + binaryVersionId
					+ " , categoryId=" + categoryId);
			return;
		}

		String sqlStr = "delete from AssetCategoryRelation acr where acr.asset.id = :assetId and acr.category.id= :categoryId and acr.binaryVersion.id= :binaryVersionId";
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("assetId", assetId);
		map.put("categoryId", categoryId);
		map.put("binaryVersionId", binaryVersionId);
		assetCategoryRelationDao.executeHQL(sqlStr, map);

		this.updateAssetUpdateDate(assetId);
		this.updateBinaryVersionUpdateDate(binaryVersionId);
	}

	public void disassociateCategory(Long assetId, Long binaryVersionId) throws NgpRuntimeException {
		if (assetId == null || binaryVersionId == null) {
			log.warn("assetId is null or binaryVersionId is null or category is null:assetId=" + assetId + " , binaryVersionId=" + binaryVersionId);
			return;
		}

		String sqlStr = "delete from AssetCategoryRelation acr where acr.asset.id = :assetId and acr.binaryVersion.id= :binaryVersionId";
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("assetId", assetId);
		map.put("binaryVersionId", binaryVersionId);
		assetCategoryRelationDao.executeHQL(sqlStr, map);

		this.updateAssetUpdateDate(assetId);
		this.updateBinaryVersionUpdateDate(binaryVersionId);
	}

	public void disassociateCategory(Long assetId) throws NgpRuntimeException {
		if (assetId == null) {
			log.warn("assetId is null or binaryVersionId is null or category is null:assetId=" + assetId);
			return;
		}
		String sqlStr = "delete from AssetCategoryRelation acr where acr.asset.id = ?";
		assetCategoryRelationDao.executeHQLBySequenceParam(sqlStr, assetId);

		this.updateAssetUpdateDate(assetId);
	}

	public void deleteTag(Long assetId, Long tagId, Long versionId) {
		SearchExpression searchExpression = new SearchExpressionImpl();
		if (assetId != null)
			searchExpression.addCondition(new AssetTagRelationAssetIdCondition(assetId, NumberComparer.EQUAL));

		if (tagId != null)
			searchExpression.addCondition(new AssetTagRelationTagIdCondition(tagId, NumberComparer.EQUAL));

		if (versionId != null)
			searchExpression.addCondition(new AssetTagRelationVersionIdCondition(versionId, NumberComparer.EQUAL));

		List<AssetTagRelation> tagRelations = assetTagRelationDao.search(searchExpression);

		for (AssetTagRelation tagR : tagRelations) {
			assetTagRelationDao.remove(tagR);
		}
		searchExpression = new SearchExpressionImpl();
		searchExpression.addCondition(new AssetTagRelationTagIdCondition(tagId, NumberComparer.EQUAL));
		tagRelations = assetTagRelationDao.search(searchExpression);
		if (tagRelations.size() <= 0)
			// there are no relations between asset and
			// tag,
			// just remove tag
			tagDao.remove(tagDao.findById(tagId));

		this.updateAssetUpdateDate(assetId);
		this.updateBinaryVersionUpdateDate(versionId);
	}

	public void deleteTag(Long tagId) {
		tagDao.remove(this.tagDao.findById(tagId));
	}

	public List<Tag> getAllTags(int start, int max) {
		return tagDao.getAll(start, max);
	}

	public List<Tag> getAllTagsByAssetBinary(Long versionId, int start, int max) {
		if (versionId == null) {
			return null;
		}
		SearchExpression searchExpression = new SearchExpressionImpl();
		searchExpression.addCondition(new AssetTagRelationVersionIdCondition(versionId, NumberComparer.EQUAL));
		searchExpression.setFirst(start);
		searchExpression.setMax(max);
		List<AssetTagRelation> assetTagRelations = assetTagRelationDao.search(searchExpression);
		if (assetTagRelations == null || assetTagRelations.size() == 0) {
			return null;
		}
		List<Tag> result = new ArrayList<Tag>();
		for (AssetTagRelation atr : assetTagRelations) {
			result.add(atr.getTag());
			atr.getTag().getName();
		}
		return result;
	}

	public List<Tag> getAllTagsByAsset(Long assetId, Long versionId, int start, int max) {

		SearchExpression searchExpression = new SearchExpressionImpl();
		searchExpression.addCondition(new AssetTagRelationAssetIdCondition(assetId, NumberComparer.EQUAL));
		if (versionId != null) {
			searchExpression.addCondition(new AssetTagRelationVersionIdCondition(versionId, NumberComparer.EQUAL));
		}
		searchExpression.setFirst(start);
		searchExpression.setMax(max);
		List<AssetTagRelation> assetTagRelations = assetTagRelationDao.search(searchExpression);
		List<Tag> result = new ArrayList<Tag>();
		if (assetTagRelations == null || assetTagRelations.size() == 0) {
			return null;
		}
		for (AssetTagRelation atr : assetTagRelations) {
			result.add(atr.getTag());
			atr.getTag().getName();
		}
		return result;
	}

	public long getAllTagsCount() {
		return tagDao.getAllCount();
	}

	public Tag getTagById(Long tagId) {
		return tagDao.findById(tagId);
	}

	public Tag getTagByName(String name) {
		SearchExpression searchExpression = new SearchExpressionImpl();
		searchExpression.addCondition(new TagNameCondition(name, StringComparer.EQUAL, true, false));
		List<Tag> tags = tagDao.search(searchExpression);
		if (tags != null && tags.size() > 0) {
			return tags.get(0);
		} else
			return null;
	}

	public void saveTag(Long assetId, Tag tag) {
		if (tag != null && assetId != null) {
			// if tag exists, does nothing
			Tag oldTag = this.getTagByName(tag.getName());
			if (oldTag != null) {
				tag = oldTag;
			} else {
				tagDao.persist(tag);
			}

			// if relation exists, do nothing
			SearchExpression searchExpression = new SearchExpressionImpl();
			searchExpression.addCondition(new AssetTagRelationTagIdCondition(tag.getId(), NumberComparer.EQUAL));
			List<AssetTagRelation> relations = assetTagRelationDao.search(searchExpression);
			if (relations != null && relations.size() > 0) {
				// do nothing
			} else {
				assetTagRelationDao.persist(new AssetTagRelation(new Asset(assetId), tag));
			}
		}
	}

	public void saveTag(Tag tag) {
		// if tag exists, does nothing
		Tag oldTag = this.getTagByName(tag.getName());
		if (oldTag != null) {
			tag = oldTag;
		} else {
			tagDao.persist(tag);
		}
	}

	public void updateTag(Tag tag) {
		tagDao.merge(tag);
	}

	public void associateTagRelation(Long tagId, Long assetId, Long versionId) {
		AssetTagRelation assetTagRelation = new AssetTagRelation();
		assetTagRelation.setAsset(new Asset(assetId));
		assetTagRelation.setTag(new Tag(tagId));
		assetTagRelation.setBinaryVersion(versionId == null ? null : new AssetBinaryVersion(versionId));
		assetTagRelationDao.persist(assetTagRelation);

		this.updateAssetUpdateDate(assetId);
		this.updateBinaryVersionUpdateDate(versionId);
	}

	public void associateTagRelation(Long assetId, Long versionId, Set<Long> tagIds) {
		if (tagIds == null || tagIds.size() == 0) {
			return;
		}
		Iterator iterator = tagIds.iterator();
		while (iterator.hasNext()) {
			this.associateTagRelation((Long) iterator.next(), assetId, versionId);
		}

		this.updateAssetUpdateDate(assetId);
		this.updateBinaryVersionUpdateDate(versionId);
	}

	public void disassociateTagRelation(Long assetId, Long tagId, Long versionId) throws NgpRuntimeException {
		if (assetId == null || tagId == null || versionId == null) {
			log.warn("assetId is null or binaryVersionId is null or tagId is null:assetId=" + assetId + " , binaryVersionId=" + versionId
					+ " , tagId=" + tagId);
			return;
		}

		String sqlStr = "delete from AssetTagRelation atr where atr.asset.id = :assetId and atr.tag.id= :tagId and atr.binaryVersion.id= :versionId";
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("assetId", assetId);
		map.put("tagId", tagId);
		map.put("versionId", versionId);
		assetCategoryRelationDao.executeHQL(sqlStr, map);

		this.updateAssetUpdateDate(assetId);
		this.updateBinaryVersionUpdateDate(versionId);

	}

	public void disassociateTagRelation(Long assetId, Long versionId) {
		if (assetId == null || versionId == null) {
			log.warn("assetId is null or binaryVersionId is null or category is null:assetId=" + assetId + " , binaryVersionId=" + versionId);
			return;
		}
		String sqlStr = "delete from AssetTagRelation atr where atr.asset.id = :assetId and atr.binaryVersion.id= :versionId";
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("assetId", assetId);
		map.put("versionId", versionId);
		assetCategoryRelationDao.executeHQL(sqlStr, map);

		this.updateAssetUpdateDate(assetId);
		this.updateBinaryVersionUpdateDate(versionId);
	}

	public void disassociateTagRelation(Long assetId) {
		if (assetId == null) {
			throw new NgpRuntimeException("assetId is null");
		}
		String sqlStr = "delete from AssetTagRelation atr where atr.asset.id = ?";
		assetCategoryRelationDao.executeHQLBySequenceParam(sqlStr, assetId);

		this.updateAssetUpdateDate(assetId);
	}

	public List<Status> getAllStatus() {
		return statusDao.getAll(0, Integer.MAX_VALUE);
	}

	public Status getStatusById(Long id) {
		return statusDao.findById(id);
	}

	public Status getStatusByName(String name) {
		SearchExpression searchExpression = new SearchExpressionImpl();
		searchExpression.addCondition(new StatusStatusCondition(name, StringComparer.EQUAL, false, false));
		List<Status> statuses = statusDao.search(searchExpression);
		if (statuses != null && statuses.size() > 0) {
			return statuses.get(0);
		}
		return null;
	}

	public void saveStatus(Status status) {
		statusDao.persist(status);
	}

	public void saveApiKey(ApiKey apiKey, List<Service> services, String userId) {
		Set<ServiceSubscription> s = new HashSet<ServiceSubscription>();
		for (Service service : services) {
			ServiceSubscription ss = new ServiceSubscription();
			ss.setApiKeyRelation(apiKey);
			ss.setUserid(userId);
			ss.setService(service);
			s.add(ss);
		}
		apiKey.setServiceSubscriptions(s);
		if (apiKey.getId() == null)
			apiKeyDao.persist(apiKey);
		else
			apiKeyDao.merge(apiKey);
	}

	public void deleteAssetRating(Long assetRatingId) {
		assetRatingDao.remove(assetRatingDao.findById(assetRatingId));
	}

	public Double saveOrUpdateAssetRating(Long assetId, String userId, Double rating) {

		AssetRating myRank = null;
		float oldRank = 0f;
		long mycount = 0L;
		SearchExpression searchExpression = new SearchExpressionImpl();
		searchExpression.addCondition(new AssetRatingAssetIdCondition(assetId, NumberComparer.EQUAL));
		searchExpression.addCondition(new AssetRatingUseridCondition(userId, StringComparer.EQUAL, false, false));
		List<AssetRating> assetRatingList = assetRatingDao.search(searchExpression);
		if (assetRatingList != null && assetRatingList.size() > 0) {
			myRank = assetRatingList.get(0);
			oldRank = myRank.getRating().floatValue();
			mycount = 1L;
		} else {
			myRank = new AssetRating();
			myRank.setUserid(userId);
			myRank.setAsset(new Asset(assetId));
		}

		myRank.setCreateDate(new Date());
		myRank.setRating(rating);
		// Save the rating first
		if (myRank.getId() != null) {
			assetRatingDao.merge(myRank);
		} else {
			assetRatingDao.persist(myRank);
		}

		// and then calculate the average rate
		// Calculate new app avg rank
		Asset asset = assetDao.findById(assetId);
		float oldRankSum = (asset.getAverageUserRating() != null) ? asset.getAverageUserRating().floatValue() : 0;
		searchExpression = new SearchExpressionImpl();
		searchExpression.addCondition(new AssetRatingAssetIdCondition(assetId, NumberComparer.EQUAL));
		long count = assetRatingDao.searchCount(searchExpression);
		float avgRank = (oldRankSum * count - oldRank + rating.longValue()) / ((float) (count - mycount + 1));
		asset.setAverageUserRating(new Double(avgRank));

		assetDao.merge(asset);

		return asset.getAverageUserRating();
	}

	public AssetRating getAssetRating(String userId, Long assetId) {
		SearchExpression searchExpression = new SearchExpressionImpl();
		searchExpression.addCondition(new AssetRatingAssetIdCondition(assetId, NumberComparer.EQUAL));
		searchExpression.addCondition(new AssetRatingUseridCondition(userId, StringComparer.EQUAL, false, false));

		List<AssetRating> assetRatings = assetRatingDao.search(searchExpression);

		if (assetRatings != null && assetRatings.size() > 0) {
			return assetRatings.get(0);
		}
		return null;
	}

	@Transactional(readOnly = true)
	public List<AssetRating> searchAssetRatingByAssetId(Long assetId, int start, int count) {
		return assetRatingDao.findBy("asset.id", assetId, start, count);
	}

	public List<AssetRating> getAssetRating(String userId) {
		SearchExpression searchExpression = new SearchExpressionImpl();
		searchExpression.addCondition(new AssetRatingUseridCondition(userId, StringComparer.EQUAL, false, false));

		List<AssetRating> assetRatings = assetRatingDao.search(searchExpression);

		if (assetRatings != null && assetRatings.size() > 0) {
			return assetRatings;
		}
		return null;
	}

	public void associateAssetGroup(long parentAssetId, List<Long> childAssetIds) throws NgpRuntimeException {
		if (childAssetIds.size() == 0 || childAssetIds == null) {
			throw new NgpRuntimeException("childAssetIds is null");
		}

		for (int i = 0; i < childAssetIds.size(); i++) {
			Asset asset = assetDao.findById(childAssetIds.get(i));
			if (asset == null) {
				throw new NgpRuntimeException("the asset is not exist");
			}
			asset.setAsset(new Asset(parentAssetId));

			this.updateAssetUpdateDate(asset);
		}

		this.updateAssetUpdateDate(parentAssetId);
	}

	public void disassociateAssetGroup(List<Long> childAssetIds) {

		for (int i = 0; i < childAssetIds.size(); i++) {
			Asset asset = assetDao.findById(childAssetIds.get(i));
			asset.setAsset(null);
			asset.setUpdateDate(Utils.getCurrentDate());
			assetDao.merge(asset);
		}
		if (childAssetIds != null && childAssetIds.size() > 0) {
			Asset asset = assetDao.findById(childAssetIds.get(0));
			if (asset != null) {
				this.updateAssetUpdateDate(asset.getAsset());
			}
		}
	}

	public List<Asset> getAllAsset(int start, int max) {
		return assetDao.getAll(start, max);
	}

	public Asset getAsset(Long assetId) {
		return assetDao.findById(assetId);
	}

	public Asset getAssetByBinaryId(Long binaryId) {
		AssetBinaryVersion binaryVersion = binaryVersionDao.findById(binaryId);
		Asset asset = assetDao.findById(binaryVersion.getAsset().getId());
		return asset;
	}

	@Transactional(readOnly = true)
	public List<Asset> searchAsset(SearchExpression searchExpression) {
		return assetDao.search(searchExpression);
	}

	public List<Asset> getAssetByStatus(Long statusId) {
		SearchExpression searchExpression = new SearchExpressionImpl();
		searchExpression.addCondition(new AssetStatusIdCondition(statusId, NumberComparer.EQUAL));
		return assetDao.search(searchExpression);
	}

	public List<Asset> getMyAsset(String userId) {
		return assetDao.findBy("authorid", userId);
	}

	public int getMyAssetPageCount(String userId) {
		return (int) assetDao.findByCount("authorid", userId);
	}

	public void deleteAsset(Long assetId) throws DeleteFileFailureException {
		// Execute SQl to remove the relation
		String sqlStr = "update Asset atr set atr.asset=null where atr.asset.id = ?";
		assetDao.executeHQLBySequenceParam(sqlStr, assetId);

		Asset asset = assetDao.findById(assetId);
		if (asset == null) {
			return;
		}
		String location = filePath + this.getAssetDirectory(asset);
		this.deleteFile(location);
		long entityId = asset.getEntityId();

		assetDao.remove(asset);
		eavRepository.deleteEntity(entityId);
	}

	public void saveAsset(Asset asset, List<Long> categoryIds) {
		Entity entity = new Entity();
		entity.setEntityType(EntityType.ASSET.ordinal());
		eavRepository.addEntity(entity);
		asset.setEntityId(entity.getEntityID());
		assetDao.persist(asset);
		if (categoryIds != null && categoryIds.size() != 0) {
			for (Long ctgId : categoryIds)
				assetCategoryRelationDao.persist(new AssetCategoryRelation(new Category(ctgId), asset));
		}
	}

	public void saveAsset(Asset asset) {
		Entity entity = new Entity();
		entity.setEntityType(EntityType.ASSET.ordinal());
		eavRepository.addEntity(entity);
		asset.setEntityId(entity.getEntityID());
		asset.setCreateDate(Utils.getCurrentDate());
		assetDao.persist(asset);
	}

	@Transactional(readOnly = true)
	public long searchAssetPageCount(SearchExpression searchExpression) {
		return assetDao.searchCount(searchExpression);
	}

	public void updateAsset(Asset asset) {
		asset.setUpdateDate(Utils.getCurrentDate());
		assetDao.merge(asset);
	}

	public void updateAssetVersion(Long assetId, Long binaryVersionId) {
		AssetBinaryVersion binary = binaryVersionDao.findById(binaryVersionId);
		Asset asset = this.getAsset(assetId);
		if (binary == null || asset == null) {
			throw new NgpRuntimeException("binary or asset is not exist.");
		}
		asset.setCurrentVersionId(binaryVersionId);
		asset.setCurrentVersion(binary.getVersion());

		if (binary.getName() != null) {
			asset.setName(binary.getName());
		}
		if (binary.getThumbnailBigLocation() != null) {
			asset.setThumbnailBigLocation(binary.getThumbnailBigLocation());
		}
		if (binary.getThumbnailMiddleLocation() != null) {
			asset.setThumbnailMiddleLocation(binary.getThumbnailMiddleLocation());
		}
		if (binary.getThumbnailLocation() != null) {
			asset.setThumbnailLocation(binary.getThumbnailLocation());
		}
		if (binary.getBrief() != null) {
			asset.setBrief(binary.getBrief());
		}
		if (binary.getDescription() != null) {
			asset.setDescription(binary.getDescription());
		}

		if (binary.getRecommendDueDate() != null) {
			asset.setRecommendDueDate(binary.getRecommendDueDate());
		}

		if (binary.getRecommendOrder() != null) {
			asset.setRecommendOrder(binary.getRecommendOrder());
		}

		if (binary.getRecommendStartDate() != null) {
			asset.setRecommendStartDate(binary.getRecommendStartDate());
		}

		if (binary.getRecommendUpdateDate() != null) {
			asset.setRecommendUpdateDate(binary.getRecommendUpdateDate());
		}

		if (binary.getPublishDate() != null) {
			asset.setPublishDate(binary.getPublishDate());
		}

		if (binary.getNewArrivalDueDate() != null) {
			asset.setNewArrivalDueDate(binary.getNewArrivalDueDate());
		}
		if (binary.getOwnerAssetParentId() != null && !binary.getOwnerAssetParentId().equals(assetId)) {
			Asset parentAsset = new Asset();
			parentAsset.setId(binary.getOwnerAssetParentId());
			asset.setAsset(parentAsset);
		}
		asset.setUpdateDate(Utils.getCurrentDate());
		assetDao.merge(asset);
	}

	public void saveAssetPicture(Long assetId, byte[] content, AssetPictureType pictureType, String assetPictureName) throws NgpRuntimeException {
		if (content == null || content.length == 0) {
			throw new NgpRuntimeException("The File content can't be null !");
		}

		Asset asset = assetDao.findById(assetId);
		String location = this.getAssetDirectory(asset) + "/picture/" + pictureType.getAttributeName() + "/" + assetPictureName;
		switch (pictureType)

		{
		case THUMBNAILIMAGE:
			asset.setThumbnailLocation(location);
			break;
		case THUMBNAILMIDDLEIMAGE:
			asset.setThumbnailMiddleLocation(location);
			break;
		case THUMBNAILBIGIMAGE:
			asset.setThumbnailBigLocation(location);
			break;

		}

		this.saveFile(filePath + location, content);
		this.updateAsset(asset);

		BinaryFile binaryFile = new BinaryFile();
		binaryFile.setFileLocation(location);
		binaryFile.setFileBinary(content);
		binaryFileDao.persist(binaryFile);
	}

	// public void deleteAssetAttribute(Long entityAttributeID) {
	// eavRepository.deleteEntityAttribute(entityAttributeID);
	// }
	//
	// public void deleteAssetAttributeAsFile(Long assetid, String
	// attributeName) {
	// Asset asset=assetDao.findById(assetid);
	// List<AttributeValue>
	// attributeValues=eavRepository.getEntityAttribute(asset.getEntityId(),
	// attributeName);
	// for (int i=0;i<attributeValues.size();i++){
	// this.deleteFile(filePath+attributeValues.get(i).getValue());
	// }
	//		
	// }

	public void addAttribute(Object objectId, EntityType entityType, String attributeName, Object value) throws NgpRuntimeException {
		Object attributeValue;
		AttributeType attributeType = null;
		if (value instanceof Float || value instanceof Integer || value instanceof Double || value instanceof BigDecimal || value instanceof Long) {
			if (value instanceof Integer) {
				attributeValue = ((Integer) value).floatValue();
			} else if (value instanceof Double) {
				attributeValue = ((Double) value).floatValue();
			} else if (value instanceof BigDecimal) {
				attributeValue = ((BigDecimal) value).floatValue();
			} else if (value instanceof Long) {
				attributeValue = ((Long) value).floatValue();
			} else {
				attributeValue = (Float) value;
			}
			attributeType = AttributeType.number;
		} else if (value instanceof Date) {
			attributeValue = (Date) value;
			attributeType = AttributeType.date;
		} else if (value instanceof String) {
			attributeValue = (String) value;
			attributeType = AttributeType.varchar;
		} else {
			throw new NgpRuntimeException("unkown value type=" + value);
		}

		Long entityId = this.getEntityId(objectId, entityType);
		if (entityId == null) {
			log.warn("can't find the corresponding entity in EAV repository for this object");
			throw new NgpRuntimeException("wrong entity id" + entityId);
		}

		Long result = eavRepository.addEntityAttribute(entityId, attributeName, attributeType, attributeValue);

		if (result == null) {
			Attribute attribute = new Attribute();
			attribute.setAttributeName(attributeName);
			attribute.setAttributeType(attributeType);

			eavRepository.addAttributeDefine(attribute);
			eavRepository.addEntityAttribute(entityId, attributeName, attributeType, attributeValue);
		}

	}

	@SuppressWarnings("unchecked")
	private Long getEntityId(Object objectId, EntityType entityType) {
		JpaDao dao = null;
		switch (entityType) {
		case ASSET: {
			dao = assetDao;
			break;
		}
		case BINARYVERSION: {

			dao = binaryVersionDao;
			break;
		}
		case ASSETPROVIDER: {
			dao = providerDao;
			break;
		}
		case SUBSCRIBER: {
			dao = subscriberProfileDao;
			break;
		}
		case USER: {
			dao = userProfileDao;
			break;
		}
		default:
			throw new NgpRuntimeException("unknown entity type" + entityType.toString());
		}

		return dao.findEntityIdById((Serializable) objectId);
	}

	public Map<String, List<AttributeValue>> getAttributes(Object objectId, EntityType entityType) {

		Map<String, List<AttributeValue>> maps = new HashMap<String, List<AttributeValue>>();
		Long entityId = this.getEntityId(objectId, entityType);
		if (entityId == null) {
			log.warn("can't find the corresponding entity in EAV repository for this object");
			throw new NgpRuntimeException("wrong entity id" + entityId);
		}
		List<AttributeValue> attributeValues = this.eavRepository.getEntityAttributes(entityId);
		if (attributeValues == null || attributeValues.size() == 0) {
			return maps;
		}

		for (AttributeValue attributeValue : attributeValues) {
			if (attributeValue.getAttribute() == null) {
				continue;
			}
			String attributeName = attributeValue.getAttribute().getAttributeName();
			List<AttributeValue> attributeValueNameList = maps.get(attributeName);
			if (attributeValueNameList == null) {
				attributeValueNameList = new ArrayList<AttributeValue>();
				maps.put(attributeName, attributeValueNameList);
			}
			attributeValueNameList.add(attributeValue);
		}

		return maps;
	}

	public List<AttributeValue> getAttributeValue(Object objectId, EntityType entityType, String attributeName) {
		Long entityId = this.getEntityId(objectId, entityType);
		if (entityId == null) {
			log.warn("can't find the corresponding entity in EAV repository for this object");
			throw new NgpRuntimeException("wrong entity id" + entityId);
		}
		return eavRepository.getEntityAttributes(entityId, attributeName);

	}

	public void removeAttributes(Object objectId, EntityType entityType, String attributeName) throws NgpRuntimeException {
		Long entityId = this.getEntityId(objectId, entityType);
		if (entityId == null) {
			log.warn("can't find the corresponding entity in EAV repository for this object");
			throw new NgpRuntimeException("wrong entity id" + entityId);
		}
		eavRepository.deleteEntityAttribute(entityId, attributeName);

	}

	public long countAssetLifecycleActionResult(Long assetId, boolean positive) {

		SearchExpression searchExpression = new SearchExpressionImpl();
		searchExpression.addCondition(new AssetLifecycleActionAssetIdCondition(assetId, NumberComparer.EQUAL));
		searchExpression.addCondition(new AssetLifecycleActionResultCondition(positive ? "pass" : "fail", StringComparer.EQUAL, false, false));
		return assetLifecycleActionDao.searchCount(searchExpression);
	}

	public void deleteAssetLifecycleActionById(Long assetLifecycleActionId) {
		assetLifecycleActionDao.remove(assetLifecycleActionDao.findById(assetLifecycleActionId));
	}

	public List<AssetLifecycleAction> getAssetLifecycleAction(SearchExpression searchExpression) {
		return assetLifecycleActionDao.search(searchExpression);
	}

	public List<AssetLifecycleAction> getAssetLifecycleActionByAssetId(Long assetId) {
		SearchExpression searchExpression = new SearchExpressionImpl();
		searchExpression.addCondition(new AssetLifecycleActionAssetIdCondition(assetId, NumberComparer.EQUAL));
		List<AssetLifecycleAction> assetLifecycleActions = assetLifecycleActionDao.search(searchExpression);

		return assetLifecycleActions;
	}

	public AssetLifecycleAction getAssetLifecycleActionById(Long assetLifecycleActionId) {
		return assetLifecycleActionDao.findById(assetLifecycleActionId);
	}

	public long getAssetLifecycleActionCount(Long assetId) {
		SearchExpression searchExpression = new SearchExpressionImpl();
		searchExpression.addCondition(new AssetLifecycleActionAssetIdCondition(assetId, NumberComparer.EQUAL));

		return assetLifecycleActionDao.searchCount(searchExpression);
	}

	public void saveOrUpdateAssetLifecycleAction(AssetLifecycleAction assetLifecycleAction) {
		if (assetLifecycleAction.getId() != null) {
			assetLifecycleActionDao.merge(assetLifecycleAction);
		} else {
			assetLifecycleActionDao.persist(assetLifecycleAction);
		}
	}

	public long countAllAssetBinary() {
		return binaryVersionDao.getAllCount();
	}

	public long countAssetBinary(SearchExpression searchExpression) {
		return binaryVersionDao.searchCount(searchExpression);
	}

	public long countAssetBinaryByAssetId(Long assetId) {
		SearchExpression searchExpression = new SearchExpressionImpl();
		searchExpression.addCondition(new AssetBinaryVersionAssetIdCondition(assetId, NumberComparer.EQUAL));
		return binaryVersionDao.searchCount(searchExpression);
	}

	public void deleteAssetBinary(Long binaryVersionId) throws NgpRuntimeException {
		AssetBinaryVersion binaryVersion = binaryVersionDao.findById(binaryVersionId);
		if (binaryVersion == null) {
			log.warn("binaryVersion not exist:" + binaryVersionId);
		}

		String location = binaryVersion.getLocation();
		long entityId = binaryVersion.getEntityId();

		if (location != null) {
			this.deleteFile(filePath + location.substring(0, location.lastIndexOf("/")));

			List<BinaryFile> binaryFiles = binaryFileDao.findBy("fileLocation", location);
			if (binaryFiles != null && binaryFiles.size() > 0) {
				binaryFileDao.remove(binaryFiles.get(0));
			}

		}

		delBinaryFile(binaryVersion.getThumbnailBigLocation());
		delBinaryFile(binaryVersion.getThumbnailLocation());
		delBinaryFile(binaryVersion.getThumbnailMiddleLocation());

		if (binaryVersion.getAssetPrices() != null) {
			for (AssetPrice ap : binaryVersion.getAssetPrices()) {
				if (binaryVersion.getAsset() != null && binaryVersion.getAsset().getAssetPrices() != null) {
					binaryVersion.getAsset().getAssetPrices().remove(ap);
					assetPriceDao.remove(ap.getId());
				}
			}
			binaryVersion.setAssetPrices(null);
		}

		if (binaryVersion.getAssetLifecycleActions() != null) {
			for (AssetLifecycleAction ap : binaryVersion.getAssetLifecycleActions()) {
				if (binaryVersion.getAsset() != null && binaryVersion.getAsset().getAssetLifecycleActions() != null) {
					binaryVersion.getAsset().getAssetLifecycleActions().remove(ap);
					assetLifecycleActionDao.remove(ap);
				}
			}
			binaryVersion.setAssetLifecycleActions(null);
		}

		if (binaryVersion.getAssetRestrictionRelations() != null) {
			for (AssetRestrictionRelation ap : binaryVersion.getAssetRestrictionRelations()) {
				if (binaryVersion.getAsset() != null && binaryVersion.getAsset().getAssetRestrictionRelations() != null) {
					binaryVersion.getAsset().getAssetRestrictionRelations().remove(ap);
					assetRestrictionRelationDao.remove(ap);
				}
			}
			binaryVersion.setAssetRestrictionRelations(null);
		}

		if (binaryVersion.getAssetTagRelations() != null) {
			for (AssetTagRelation ap : binaryVersion.getAssetTagRelations()) {
				if (binaryVersion.getAsset() != null && binaryVersion.getAsset().getAssetTagRelations() != null) {
					binaryVersion.getAsset().getAssetTagRelations().remove(ap);
					assetTagRelationDao.remove(ap);
				}
			}
			binaryVersion.setAssetTagRelations(null);
		}

		if (binaryVersion.getAssetCategoryRelations() != null) {
			for (AssetCategoryRelation ap : binaryVersion.getAssetCategoryRelations()) {
				if (binaryVersion.getAsset() != null && binaryVersion.getAsset().getAssetCategoryRelations() != null) {
					binaryVersion.getAsset().getAssetCategoryRelations().remove(ap);
					assetCategoryRelationDao.remove(ap);
				}
			}
			binaryVersion.setAssetCategoryRelations(null);
		}

		if (binaryVersion.getScreenShotses() != null) {
			for (ScreenShots screenShot : binaryVersion.getScreenShotses()) {
				if (binaryVersion.getAsset() != null && binaryVersion.getAsset().getScreenShotses() != null) {

					String loc = screenShot.getStoreLocation();
					if (loc != null) {
						if (loc.lastIndexOf("/") >= 0)
							this.deleteFile(filePath + loc.substring(0, loc.lastIndexOf("/")));

						List<BinaryFile> binaryFiles = binaryFileDao.findBy("fileLocation", loc);
						if (binaryFiles != null && binaryFiles.size() > 0) {
							binaryFileDao.remove(binaryFiles.get(0));
						}
					}

					binaryVersion.getAsset().getScreenShotses().remove(screenShot);
					screenShotsDao.remove(screenShot);
				}
			}
			binaryVersion.setScreenShotses(null);
		}

		eavRepository.deleteEntity(entityId);
		binaryVersionDao.remove(binaryVersion);

	}

	private void delBinaryFile(String path) {
		if (path != null) {
			try {
				this.deleteFile(filePath + path.substring(0, path.lastIndexOf("/")));
			} catch (Exception e) {
				log.warn(e.getMessage());
			}

			List<BinaryFile> binaryFiles = binaryFileDao.findBy("fileLocation", path);
			if (binaryFiles != null && binaryFiles.size() > 0) {
				binaryFileDao.remove(binaryFiles.get(0));
			}

		}
	}

	public void saveAssetBinaryPicture(Long binaryId, byte[] content, AssetPictureType pictureType, String assetPictureName)
			throws NgpRuntimeException {
		if (content == null || content.length == 0) {
			throw new NgpRuntimeException("File content can't be null !");
		}
		AssetBinaryVersion binary = binaryVersionDao.findById(binaryId);
		Asset asset = assetDao.findById(binary.getAsset().getId());
		String location = this.getAssetDirectory(asset) + "/picture/binary/" + pictureType.getAttributeName() + "/" + assetPictureName;
		switch (pictureType)

		{
		case THUMBNAILIMAGE:
			binary.setThumbnailLocation(location);
			break;
		case THUMBNAILMIDDLEIMAGE:
			binary.setThumbnailMiddleLocation(location);
			break;
		case THUMBNAILBIGIMAGE:
			binary.setThumbnailBigLocation(location);
			break;
		}

		this.saveFile(filePath + location, content);
		binary.setUpdateDate(Utils.getCurrentDate());
		binaryVersionDao.merge(binary);

		BinaryFile binaryFile = new BinaryFile();
		binaryFile.setFileLocation(location);
		binaryFile.setFileBinary(content);
		binaryFileDao.persist(binaryFile);
	}

	public List<AssetBinaryVersion> getAssetBinary(SearchExpression searchExpression) {
		return binaryVersionDao.search(searchExpression);
	}

	public List<AssetBinaryVersion> getAllBinaryVersion(int start, int last) {
		return binaryVersionDao.getAll(start, last);
	}

	public AssetBinaryVersion getAssetBinaryById(Long binaryVersionId) {
		return binaryVersionDao.findById(binaryVersionId);
	}

	public List<AssetBinaryVersion> getAssetBinaryByAssetId(Long assetId) {
		SearchExpression searchExpression = new SearchExpressionImpl();
		searchExpression.addCondition(new AssetBinaryVersionAssetIdCondition(assetId, NumberComparer.EQUAL));

		return binaryVersionDao.search(searchExpression);
	}

	public List<AssetBinaryVersion> getAssetBinaryByAssetIdAndVersion(Long assetId, String version) {
		SearchExpression searchExpression = new SearchExpressionImpl();
		searchExpression.addCondition(new AssetBinaryVersionAssetIdCondition(assetId, NumberComparer.EQUAL));
		searchExpression.addCondition(new AssetBinaryVersionVersionNameCondition(version, StringComparer.EQUAL, false, false));
		return binaryVersionDao.search(searchExpression);
	}

	public AssetBinaryVersion getLatestVersion(Long assetId) {
		AssetBinaryVersion latestBinaryVersion = null;
		List<AssetBinaryVersion> binaryVersions = new ArrayList<AssetBinaryVersion>();
		SearchExpression searchExpression = new SearchExpressionImpl();
		searchExpression.addCondition(new AssetBinaryVersionAssetIdCondition(assetId, NumberComparer.EQUAL));
		binaryVersions = binaryVersionDao.search(searchExpression);
		Calendar c = Calendar.getInstance();
		c.set(Calendar.YEAR, 1900);
		Date latestVerDate = c.getTime();
		for (AssetBinaryVersion binaryVersion : binaryVersions) {
			if (latestVerDate.getTime() < binaryVersion.getUpdateDate().getTime()) {
				latestVerDate = binaryVersion.getUpdateDate();
				latestBinaryVersion = binaryVersion;
			}
		}
		return latestBinaryVersion;
	}

	public AssetBinaryVersion getLatestVersion(Long assetId, Status status) {
		AssetBinaryVersion latestBinaryVersion = null;
		SearchExpression searchExpression = new SearchExpressionImpl();
		searchExpression.addCondition(new AssetBinaryVersionAssetIdCondition(assetId, NumberComparer.EQUAL));
		searchExpression.addCondition(new AssetBinaryVersionStatusIdCondition(status.getId(), NumberComparer.EQUAL));
		List<AssetBinaryVersion> binaryVersions = new ArrayList<AssetBinaryVersion>();
		binaryVersions = binaryVersionDao.search(searchExpression);
		Calendar c = Calendar.getInstance();
		c.set(Calendar.YEAR, 1900);
		Date latestVerDate = c.getTime();
		for (AssetBinaryVersion binaryVersion : binaryVersions) {
			if (latestVerDate.getTime() < binaryVersion.getUpdateDate().getTime()) {
				latestVerDate = binaryVersion.getUpdateDate();
				latestBinaryVersion = binaryVersion;
			}
		}
		return latestBinaryVersion;
	}

	public void updateAssetBinary(byte[] newBinary, Long binaryVersionId) throws SaveFileFailureException {

		if (newBinary == null || newBinary.length == 0) {
			throw new SaveFileFailureException("File content can't be null !");
		}
		AssetBinaryVersion binaryVersion = binaryVersionDao.findById(binaryVersionId);

		String tmp = filePath + binaryVersion.getLocation();
		this.deleteFile(tmp);
		saveFile(tmp, newBinary);
		binaryVersion.setUpdateDate(new Date());

		List<BinaryFile> binaryFiles = binaryFileDao.findBy("fileLocation", binaryVersion.getLocation());
		if (binaryFiles != null && binaryFiles.size() > 0) {
			BinaryFile binaryFile = binaryFiles.get(0);
			binaryFile.setFileBinary(newBinary);

			binaryFileDao.merge(binaryFile);
		}
	}

	public void updateBinaryVersion(AssetBinaryVersion binaryVersion) {
		binaryVersion.setUpdateDate(Utils.getCurrentDate());
		binaryVersionDao.merge(binaryVersion);
	}

	public void saveAssetBinary(byte[] content, AssetBinaryVersion binaryVersion) throws SaveFileFailureException {

		if (binaryVersion.getAsset() == null) {
			throw new NgpRuntimeException("Should indicate the asset for this binary");
		}
		Asset asset = this.getAsset(binaryVersion.getAsset().getId());

		if (binaryVersion.getName() == null) {
			binaryVersion.setName(asset.getName());
		}
		if (binaryVersion.getThumbnailBigLocation() == null) {
			binaryVersion.setThumbnailBigLocation(asset.getThumbnailBigLocation());
		}

		if (binaryVersion.getThumbnailMiddleLocation() == null) {
			binaryVersion.setThumbnailMiddleLocation(asset.getThumbnailMiddleLocation());
		}

		if (binaryVersion.getThumbnailLocation() == null) {
			binaryVersion.setThumbnailLocation(asset.getThumbnailLocation());
		}

		if (binaryVersion.getBrief() == null) {
			binaryVersion.setBrief(asset.getBrief());
		}

		if (binaryVersion.getDescription() == null) {
			binaryVersion.setDescription(asset.getDescription());
		}

		if (binaryVersion.getRecommendDueDate() == null) {
			binaryVersion.setRecommendDueDate(asset.getRecommendDueDate());
		}

		if (binaryVersion.getRecommendOrder() == null) {
			binaryVersion.setRecommendOrder(asset.getRecommendOrder());
		}

		if (binaryVersion.getRecommendStartDate() == null) {
			binaryVersion.setRecommendStartDate(asset.getRecommendStartDate());
		}

		if (binaryVersion.getRecommendUpdateDate() == null) {
			binaryVersion.setRecommendUpdateDate(asset.getRecommendUpdateDate());
		}

		if (binaryVersion.getPublishDate() == null) {
			binaryVersion.setPublishDate(asset.getPublishDate());
		}

		if (binaryVersion.getNewArrivalDueDate() == null) {
			binaryVersion.setNewArrivalDueDate(asset.getNewArrivalDueDate());
		}

		if (binaryVersion.getOwnerAssetParentId() == null && asset.getAsset() != null) {
			binaryVersion.setOwnerAssetParentId(asset.getAsset().getId());
		}
		if (binaryVersion.getOwnerAssetParentId() == null && asset.getAsset() == null) {
			binaryVersion.setOwnerAssetParentId(asset.getId());
		}

		Entity entity = new Entity();
		entity.setEntityType(EntityType.BINARYVERSION.ordinal());
		eavRepository.addEntity(entity);
		binaryVersion.setEntityId(entity.getEntityID());
		binaryVersion.setCreateDate(Utils.getCurrentDate());
		binaryVersionDao.persist(binaryVersion);

		String location = this.getAssetDirectory(asset) + "/binary/" + binaryVersion.getId() + "/" + binaryVersion.getFileName();

		if (content != null) {
			saveFile(filePath + location, content);
			binaryVersion.setLocation(location);
			binaryVersionDao.merge(binaryVersion);
			BinaryFile binaryFile = new BinaryFile();
			binaryFile.setFileLocation(location);
			binaryFile.setFileBinary(content);
			binaryFileDao.persist(binaryFile);
		}

	}

	public void saveUserDownloadHistory(UserDownloadHistory userDownloadHistory) {
		if (userDownloadHistory.getId() == null) {
			userDownloadHistoryDao.persist(userDownloadHistory);
		} else {
			userDownloadHistoryDao.merge(userDownloadHistory);
		}
	}

	public void deleteUserDownloadHistoryById(Long userDownloadHistoryId) {
		userDownloadHistoryDao.remove(userDownloadHistoryDao.findById(userDownloadHistoryId));
	}

	public List<UserDownloadHistory> getUserDownloadHistory(SearchExpression searchExpression) {
		return userDownloadHistoryDao.search(searchExpression);
	}

	public UserDownloadHistory getUserDownloadHistoryById(Long userDownloadHistoryId) {
		return userDownloadHistoryDao.findById(userDownloadHistoryId);
	}

	public List<UserDownloadHistory> getUserDownloadHistoryByUserid(String userid) {
		return userDownloadHistoryDao.findBy("userid", userid);
	}

	public List<UserDownloadHistory> getUserDownloadHistoryByUseridAssetid(String userId, Long assetId) {

		SearchExpression searchExpression = new SearchExpressionImpl();
		searchExpression.addCondition(new UserDownloadHistoryAssetIdCondition(assetId, NumberComparer.EQUAL));
		searchExpression.addCondition(new UserDownloadHistoryUseridCondition(userId, StringComparer.EQUAL, false, false));
		return userDownloadHistoryDao.search(searchExpression);
	}

	public void saveOrUpdateUserDownloadHistory(UserDownloadHistory userDownloadHistory) {
		if (userDownloadHistory.getId() != null) {
			userDownloadHistoryDao.merge(userDownloadHistory);
		} else {
			Asset asset = assetDao.findById(userDownloadHistory.getAsset().getId());
			asset.setDownloadCount(asset.getDownloadCount() + 1);
			assetDao.merge(asset);
			userDownloadHistoryDao.persist(userDownloadHistory);
		}
	}

	public void associatePlatform(Long assetId, Long platformId) {
		AssetPlatformRelation assetPlatformRelation = new AssetPlatformRelation();

		assetPlatformRelation.setAsset(new Asset(assetId));
		assetPlatformRelation.setPlatform(new Platform(platformId));
		assetPlatformRelationDao.persist(assetPlatformRelation);

		this.updateAssetUpdateDate(assetId);
	}

	public void associatePlatform(Long assetId, List<Long> platformIds) {
		if (platformIds == null || platformIds.size() == 0) {
			return;
		}

		for (int i = 0; i < platformIds.size(); i++) {
			AssetPlatformRelation assetPlatformRelation = new AssetPlatformRelation();
			assetPlatformRelation.setAsset(new Asset(assetId));
			assetPlatformRelation.setPlatform(new Platform(platformIds.get(i)));
			assetPlatformRelationDao.persist(assetPlatformRelation);
		}

		this.updateAssetUpdateDate(assetId);
	}

	public void disassociatePlatform(Long assetId, Long platformId) throws NgpRuntimeException {
		if (assetId == null || platformId == null) {
			log.warn("assetId is null or platformId is null or category is null:assetId=" + assetId + " , platformId=" + platformId);
			return;
		}

		String sqlStr = "delete from AssetPlatformRelation apr where apr.asset.id = :assetId and apr.platform.id= :platformId";
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("assetId", assetId);
		map.put("platformId", platformId);
		assetPlatformRelationDao.executeHQL(sqlStr, map);

		this.updateAssetUpdateDate(assetId);
	}

	public void disassociatePlatform(Long assetId) {
		String sqlStr = "delete from AssetPlatformRelation apr where apr.asset.id = ?";
		assetPlatformRelationDao.executeHQLBySequenceParam(sqlStr, assetId);

		this.updateAssetUpdateDate(assetId);
	}

	public List<Platform> getPlatformByAssetId(Long assetId) {
		SearchExpression searchExpression = new SearchExpressionImpl();
		searchExpression.addCondition(new PlatformAssetPlatformRelationAssetIdCondition(assetId, NumberComparer.EQUAL));
		return platformDao.search(searchExpression);
	}

	public Platform getPlatformByName(String name) {
		List<Platform> platforms = platformDao.findBy("name", name);
		if (platforms == null || platforms.size() == 0) {
			return null;
		}
		return platforms.get(0);
	}

	public Platform getPlatformById(Long id) {
		return platformDao.findById(id);
	}

	public void deleteRestrictedTypeById(Long id) {
		restrictedTypeDao.remove(id);
	}

	public RestrictedType getRestrictedTypeById(Long id) {
		return restrictedTypeDao.findById(id);
	}

	public RestrictedType getRestrictedTypeByType(String type) {
		SearchExpression searchExpression = new SearchExpressionImpl();
		searchExpression.addCondition(new RestrictedTypeTypeCondition(type, StringComparer.EQUAL, false, false));
		List<RestrictedType> restrictedTypes = restrictedTypeDao.search(searchExpression);
		if (restrictedTypes != null && restrictedTypes.size() > 0) {
			return restrictedTypes.get(0);
		}
		return null;
	}

	public void saveRestrictedType(RestrictedType restrictedType) {
		restrictedTypeDao.persist(restrictedType);
	}

	public void updateRestrictedType(RestrictedType restrictedType) {
		restrictedTypeDao.merge(restrictedType);
	}

	public void deleteScreenShotsById(Long id) throws NgpRuntimeException {
		ScreenShots screenShots = screenShotsDao.findById(id);
		String location = screenShots.getStoreLocation();
		screenShotsDao.remove(screenShots);
		this.deleteFile(filePath + location);

	}

	public ScreenShots getScreenShotsById(Long id) {
		return screenShotsDao.findById(id);
	}

	public List<ScreenShots> getScreenShotsByAssetId(Long assetId) {
		SearchExpression searchExpression = new SearchExpressionImpl();
		searchExpression.addCondition(new ScreenShotsAssetIdCondition(assetId, NumberComparer.EQUAL));
		return screenShotsDao.search(searchExpression);
	}

	public List<ScreenShots> getScreenShotsByAssetBinaryVersionId(Long binaryVersionId) {
		SearchExpression expression = new SearchExpressionImpl();
		expression.addCondition(new ScreenShotsAssetBinaryVersionIdCondition(binaryVersionId, NumberComparer.EQUAL));
		return screenShotsDao.search(expression);
	}

	public void saveScreenShots(ScreenShots screenShot, byte[] content, String fileName) throws NgpRuntimeException {
		if (content == null || content.length == 0) {
			throw new SaveFileFailureException("File content can't be null !");
		}

		Asset asset = this.getAsset(screenShot.getAsset().getId());
		String location;
		if (screenShot.getBinaryVersion() != null && screenShot.getBinaryVersion().getId() != null) {
			location = this.getAssetDirectory(asset) + "/binary/" + screenShot.getBinaryVersion().getId() + "/screenshots/" + fileName;
		} else {
			location = this.getAssetDirectory(asset) + "/screenshots/" + fileName;
		}

		List<ScreenShots> screenShots = screenShotsDao.findBy("storeLocation", location);
		if (screenShots != null && screenShots.size() > 0) {
			throw new NgpRuntimeException("the file is exist");
		}

		saveFile(filePath + location, content);
		screenShot.setStoreLocation(location);

		screenShotsDao.persist(screenShot);

		BinaryFile binaryFile = new BinaryFile();
		binaryFile.setFileLocation(location);
		binaryFile.setFileBinary(content);
		binaryFileDao.persist(binaryFile);

		this.updateAssetUpdateDate(asset);
		if (screenShot.getBinaryVersion() != null && screenShot.getBinaryVersion().getId() != null) {
			long versionId = screenShot.getBinaryVersion().getId();
			this.updateBinaryVersionUpdateDate(versionId);
		}
	}

	public void updateScreenShots(ScreenShots screenShot, byte[] content, String fileName) throws SaveFileFailureException {
		if (content == null || content.length == 0) {
			throw new SaveFileFailureException("File content can't be null !");
		}

		String location = screenShot.getStoreLocation();
		location = location.substring(0, location.lastIndexOf("/")) + "/" + fileName;
		String tmp = filePath + screenShot.getStoreLocation();
		this.deleteFile(tmp);
		tmp = filePath + location;
		saveFile(tmp, content);
		screenShot.setStoreLocation(location);

		screenShotsDao.merge(screenShot);

		List<BinaryFile> binaryFiles = binaryFileDao.findBy("fileLocation", location);
		if (binaryFiles != null && binaryFiles.size() > 0) {
			BinaryFile binaryFile = binaryFiles.get(0);
			binaryFile.setFileBinary(content);

			binaryFileDao.merge(binaryFile);
		}
	}

	public void deleteAssetPriceByAssetId(Long assetId) {
		String sqlStr = "delete from AssetPrice ap where ap.asset.id = ?";
		assetPriceDao.executeHQLBySequenceParam(sqlStr, assetId);
	}

	public void deleteAssetPriceById(Long id) {
		assetPriceDao.remove(assetPriceDao.findById(id));
	}

	@Transactional(readOnly = true)
	public List<AssetPrice> searchAssetPrice(SearchExpression searchExpression) {
		return assetPriceDao.search(searchExpression);
	}

	public List<AssetPrice> getAssetPriceByAssetId(Long assetId) {
		SearchExpression searchExpression = new SearchExpressionImpl();
		searchExpression.addCondition(new AssetPriceAssetIdCondition(assetId, NumberComparer.EQUAL));

		return assetPriceDao.search(searchExpression);
	}

	public AssetPrice getAssetPriceById(Long id) {
		return assetPriceDao.findById(id);
	}

	public void saveAssetPrice(AssetPrice assetPrice) {
		assetPriceDao.persist(assetPrice);
	}

	public void updateAssetPrice(AssetPrice assetPrice) {
		assetPriceDao.merge(assetPrice);
	}

	public void deleteAssetProviderById(long id) {
		Provider provider = providerDao.findById(id);
		long entityId = provider.getEntityId();
		providerDao.remove(provider);
		eavRepository.deleteEntity(entityId);
	}

	public Provider getAssetProviderById(long id) {
		return providerDao.findById(id);
	}

	public void saveAssetProvider(Provider provider) {
		Entity entity = new Entity();
		entity.setEntityType(EntityType.ASSETPROVIDER.ordinal());
		eavRepository.addEntity(entity);
		provider.setEntityId(entity.getEntityID());
		providerDao.persist(provider);
	}

	public void updateAssetProvider(Provider provider) {
		providerDao.merge(provider);
	}

	@Transactional(readOnly = true)
	public List<Provider> searchAssetProvider(SearchExpression searchExpression) {
		return providerDao.search(searchExpression);
	}

	public Provider getAssetProviderByName(String name) {
		List<Provider> providers = providerDao.findBy("name", name);
		if (providers != null && providers.size() > 0) {
			return providers.get(0);
		} else {
			return null;
		}
	}

	@Transactional(readOnly = true)
	public int searchAssetProviderCount(SearchExpression searchExpression) {
		return providerDao.searchCount(searchExpression);
	}

	public void batchAddTags(List<Long> assetIds, String tagName) {
		Tag tag = new Tag();
		if (tagName != null && assetIds != null) {
			Tag oldTag = this.getTagByName(tagName);
			if (oldTag != null) {
				tag = oldTag;
			} else {
				tag.setName(tagName);
				tagDao.persist(tag);
			}

			for (int i = 0; i < assetIds.size(); i++) {
				SearchExpression searchExpression = new SearchExpressionImpl();
				searchExpression.addCondition(new AssetTagRelationTagIdCondition(tag.getId(), NumberComparer.EQUAL));
				searchExpression.addCondition(new AssetTagRelationAssetIdCondition(assetIds.get(i), NumberComparer.EQUAL));
				List<AssetTagRelation> relations = assetTagRelationDao.search(searchExpression);
				if (relations != null && relations.size() > 0) {
					// do nothing
				} else {
					assetTagRelationDao.persist(new AssetTagRelation(new Asset(assetIds.get(i)), tag));
				}

				this.updateAssetUpdateDate(assetIds.get(i));
			}
		}
	}

	public void batchDeleteComments(List<Long> commentId) {
		if (commentId == null || commentId.size() == 0) {
			return;
		}
		String sqlStr = "delete from Comments c where c.id = ";
		Map<String, Object> map = new HashMap<String, Object>();
		for (int i = 0; i < commentId.size(); i++) {
			sqlStr = sqlStr + ":id" + i;
			map.put("id" + i, commentId.get(i));
			if (i < commentId.size() - 1) {
				sqlStr = sqlStr + " or c.id = ";
			}
		}
		commentsDao.executeHQL(sqlStr, map);
	}

	public void batchDeleteTagRelations(List<Long> assetIds, String tagName, Long versionid) {
		Tag tag = this.getTagByName(tagName);

		if (null == tag) {
			return;
		}

		for (int i = 0; i < assetIds.size(); i++) {
			SearchExpression searchExpression = new SearchExpressionImpl();
			searchExpression.addCondition(new AssetTagRelationTagIdCondition(tag.getId(), NumberComparer.EQUAL));
			searchExpression.addCondition(new AssetTagRelationAssetIdCondition(assetIds.get(i), NumberComparer.EQUAL));
			searchExpression.addCondition(new AssetTagRelationVersionIdCondition(versionid, NumberComparer.EQUAL));
			List<AssetTagRelation> relations = assetTagRelationDao.search(searchExpression);
			if (relations != null && relations.size() > 0) {
				for (int k = 0; k < relations.size(); k++) {
					assetTagRelationDao.remove(relations.get(k));
				}
			}
			this.updateAssetUpdateDate(assetIds.get(i));
		}
	}

	public void batchUpdateAssetStatus(List<Long> assetIds, long statusId) {
		if (assetIds == null || assetIds.size() == 0) {
			return;
		}
		String sqlStr = "update Asset asset set statusid = :statusId, updateDate=:updateDate  where asset.id = ";
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("statusId", statusId);
		for (int i = 0; i < assetIds.size(); i++) {
			sqlStr = sqlStr + ":assetId" + i;
			map.put("assetId" + i, assetIds.get(i));
			map.put("updateDate", Utils.getCurrentDate());
			if (i < assetIds.size() - 1) {
				sqlStr = sqlStr + " or asset.id = ";
			}
		}
		assetDao.executeHQL(sqlStr, map);
	}

	public void batchUpdateAssetBinaryStatus(List<Long> binaryVersionIds, long statusId) {
		if (binaryVersionIds == null || binaryVersionIds.size() == 0) {
			return;
		}
		String sqlStr = "update AssetBinaryVersion abv set statusid = :statusId, updateDate=:updateDate where abv.id = ";
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("statusId", statusId);
		for (int i = 0; i < binaryVersionIds.size(); i++) {
			sqlStr = sqlStr + ":binaryVersionId" + i;
			map.put("binaryVersionId" + i, binaryVersionIds.get(i));
			map.put("updateDate", Utils.getCurrentDate());
			if (i < binaryVersionIds.size() - 1) {
				sqlStr = sqlStr + " or abv.id = ";
			}
		}
		binaryVersionDao.executeHQL(sqlStr, map);

	}

	public void batchUpdateCategory(List<Long> assetIds, Long categoryId) {
		if (assetIds.size() < 1) {
			return;
		}
		String sqlStr = "update AssetCategoryRelation acr set ctgid = :categoryId where acr.asset.id = ";
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("categoryId", categoryId);
		for (int i = 0; i < assetIds.size(); i++) {
			sqlStr = sqlStr + ":assetId" + i;
			map.put("assetId" + i, assetIds.get(i));
			if (i < assetIds.size() - 1) {
				sqlStr = sqlStr + " or acr.asset.id = ";
			}
		}
		assetCategoryRelationDao.executeHQL(sqlStr, map);
	}

	public void batchSaveAssetPrice(List<AssetPrice> assetPrices) {
		for (AssetPrice assetPrice : assetPrices) {
			assetPriceDao.persist(assetPrice);
		}
	}

	protected String getAssetDirectory(Asset asset) {
		Date date = asset.getCreateDate();

		String location = "assetcatalog/" + new SimpleDateFormat("yyyyMMdd").format((date)) + "/" + new SimpleDateFormat("HHmm").format((date)) + "/"
				+ asset.getId();

		return location;
	}

	protected void saveFile(String path, byte[] content) throws SaveFileFailureException {
		File directory = new File(path.substring(0, path.lastIndexOf("/")));

		if (!directory.exists()) {
			directory.mkdirs();
		}

		File file = new File(path);
		try {
			FileUtils.writeByteArrayToFile(file, content);
		} catch (Exception e) {
			e.printStackTrace();
			ngpLog.error(NgpLog.getLogMessage(ErrorCode.FAIL_SAVE_FILE, "Fail to save file: " + path));
			throw new SaveFileFailureException("save file error:" + e.toString());
		}
	}

	protected void deleteFile(String path) throws NgpRuntimeException {
		File file = new File(path);

		if (file.exists()) {
			try {
				FileUtils.forceDelete(file);
			} catch (IOException e) {
				ngpLog.error(NgpLog.getLogMessage(ErrorCode.FAIL_DELETE_FILE, "Fail to delete file: " + path));
				throw new NgpRuntimeException("delete file failure:" + e);
			}
		}

	}

	public List<CommentsSensorWord> getAllCommentsSensorWord(int start, int count) {
		return commentsSensorWordDao.getAll(start, count);
	}

	public int getAllCommentsSensorWordCount() {
		return commentsSensorWordDao.getAllCount();
	}

	public void saveOrUpdateCommentsSensorWord(CommentsSensorWord commentsSensorWord) {
		if (commentsSensorWord.getId() == null) {
			commentsSensorWordDao.persist(commentsSensorWord);
		} else {
			commentsSensorWordDao.merge(commentsSensorWord);
		}

	}

	public void batchDeleteCommentsSensorWord(List<Long> commentsSensorWordId) {
		if (commentsSensorWordId != null && commentsSensorWordId.size() > 0) {
			for (int i = 0; i < commentsSensorWordId.size(); i++) {
				commentsSensorWordDao.remove(commentsSensorWordId.get(i));
			}
		}

	}

	public boolean checkCommentsSensorWordByName(String commentsSensorWord) {
		List<CommentsSensorWord> csws = commentsSensorWordDao.findBy("sensorWord", commentsSensorWord);
		if (csws.size() > 0)
			return true;
		else
			return false;
	}

	@Transactional(readOnly = true)
	public List<AssetBinaryVersion> searchAssetBinary(SearchExpression searchExpression) {
		return binaryVersionDao.search(searchExpression);
	}

	@Transactional(readOnly = true)
	public long searchAssetBinaryPageCount(SearchExpression searchExpression) {
		return binaryVersionDao.searchCount(searchExpression);
	}

	public void batchUpdateAssetBinaryRecommend(List<Long> binaryVersionIds, Date recommendStartDate, Date recommendDueDate, Long recommendOrder) {

		if (binaryVersionIds == null || binaryVersionIds.size() < 1) {
			return;
		}

		String sqlStr = "update AssetBinaryVersion abv set recommendOrder= :recommendOrder, recommendStartDate= :recommendStartDate, "
				+ "updateDate=:updateDate, " + "recommendDueDate= :recommendDueDate, recommendUpdateDate= :recommendUpdateDate where abv.id= ";
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("recommendOrder", recommendOrder);
		map.put("recommendStartDate", recommendStartDate);
		map.put("recommendDueDate", recommendDueDate);
		map.put("recommendUpdateDate", Utils.getCurrentDate());
		map.put("updateDate", Utils.getCurrentDate());

		for (int i = 0; i < binaryVersionIds.size(); i++) {
			sqlStr = sqlStr + ":id" + i;
			map.put("id" + i, binaryVersionIds.get(i));

			if (i < binaryVersionIds.size() - 1) {
				sqlStr = sqlStr + " or abv.id = ";
			}
		}

		binaryVersionDao.executeHQL(sqlStr, map);
	}

	public void batchUpdateAssetBinaryNewArrivalDueDate(List<Long> binaryVersionIds, Date newArrivalDueDate) {
		if (binaryVersionIds == null || binaryVersionIds.size() < 1) {
			return;
		}

		String sqlStr = "update AssetBinaryVersion abv set abv.newArrivalDueDate= :newArrivalDueDate, abv.updateDate=:updateDate where abv.id= ";

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("newArrivalDueDate", newArrivalDueDate);
		map.put("updateDate", Utils.getCurrentDate());

		for (int i = 0; i < binaryVersionIds.size(); i++) {
			sqlStr = sqlStr + ":id" + i;
			map.put("id" + i, binaryVersionIds.get(i));

			if (i < binaryVersionIds.size() - 1) {
				sqlStr = sqlStr + " or abv.id = ";
			}
		}

		binaryVersionDao.executeHQL(sqlStr, map);

	}

	public void batchUpdateAssetBinaryProviderCommissionRate(List<Long> binaryVersionIds, Double commissionRate) {

		if (binaryVersionIds == null || binaryVersionIds.size() < 1) {
			return;
		}

		String sqlStr = "update Provider provider set provider.commissionRate= :commissionRate where provider.id in (select asset.assetProvider.id from Asset as asset,"
				+ "AssetBinaryVersion as abv where asset.id=abv.asset.id and abv.id= ";
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("commissionRate", commissionRate);
		for (int i = 0; i < binaryVersionIds.size(); i++) {
			sqlStr = sqlStr + ":id" + i;
			map.put("id" + i, binaryVersionIds.get(i));

			if (i < binaryVersionIds.size() - 1) {
				sqlStr = sqlStr + " or abv.id = ";
			}
		}

		sqlStr += ")";

		binaryVersionDao.executeHQL(sqlStr, map);
	}

	@Transactional(readOnly = true)
	public List<ScreenShots> searchScreenShots(SearchExpression searchExpression) {
		return screenShotsDao.search(searchExpression);
	}

	@Transactional(readOnly = true)
	public int searchScreenShotsCount(SearchExpression searchExpression) {
		return screenShotsDao.searchCount(searchExpression);
	}

	public List<RestrictedType> getAllRestrictedType(int first, int max) {
		return restrictedTypeDao.getAll(first, max);
	}

	public List<RestrictedType> getAllRestrictedTypesByAssetId(Long assetId, int first, int max) throws NgpRuntimeException {

		if (assetId == null || assetId < 1) {
			throw new NgpRuntimeException("assetId can't be null or less than one");
		}
		String sqlStr = "select restrictedType from RestrictedType as restrictedType where restrictedType.id in (select "
				+ "assetRestrictionRelation.restrictedType.id from AssetRestrictionRelation as assetRestrictionRelation where assetRestrictionRelation.asset.id = ?)";

		return restrictedTypeDao.findHql(sqlStr, first, max, assetId);
	}

	public void updateAssetRestrictionRelation(AssetRestrictionRelation assetRestrictionRelation) {
		assetRestrictionRelationDao.merge(assetRestrictionRelation);

		AssetBinaryVersion version = assetRestrictionRelation.getBinaryVersion();
		if (version != null) {
			this.updateBinaryVersionUpdateDate(version.getId());
		}
		Asset asset = assetRestrictionRelation.getAsset();
		if (asset != null) {
			this.updateAssetUpdateDate(asset.getId());
		}

	}

	public void saveOrUpdateAssetLifecycleActionHistory(AssetLifecycleActionHistory assetLifecycleActionHistory) {
		if (null != assetLifecycleActionHistory.getId()) {
			assetLifecycleActionHistoryDao.merge(assetLifecycleActionHistory);
		} else {
			assetLifecycleActionHistoryDao.persist(assetLifecycleActionHistory);
		}
	}

	@Transactional(readOnly = true)
	public List<AssetLifecycleActionHistory> searchAssetLifecycleActionHistory(SearchExpression searchExpression) {
		return assetLifecycleActionHistoryDao.search(searchExpression);
	}

	@Transactional(readOnly = true)
	public int searchAssetLifecycleActionHistoryCount(SearchExpression searchExpression) {
		return assetLifecycleActionHistoryDao.searchCount(searchExpression);
	}

	@Transactional(readOnly = true)
	public List<SystemConfig> searchSystemConfig(SearchExpression searchExpression) {
		return systemConfigDao.search(searchExpression);
	}

	@Transactional(readOnly = true)
	public int searchSystemConfigCount(SearchExpression searchExpression) {
		return systemConfigDao.searchCount(searchExpression);
	}

	public SystemConfig getSystemConfigByKey(String key) {
		List<SystemConfig> systemConfigs = systemConfigDao.findBy("configKey", key);
		if (systemConfigs != null && systemConfigs.size() > 0)
			return systemConfigs.get(0);
		else
			return null;
	}

	public void saveOrUpdateSystemConfig(SystemConfig systemConfig) {
		if (null != systemConfig.getId()) {
			systemConfigDao.merge(systemConfig);
		} else {
			systemConfigDao.persist(systemConfig);
		}

	}

	public SystemConfig getSystemConfigById(Long id) {
		return systemConfigDao.findById(id);
	}

	public void saveContentProviderOperator(ContentProviderOperator contentProviderOperator) {
		if (null != contentProviderOperator.getId()) {
			contentProviderOperatorDao.merge(contentProviderOperator);
		} else {
			contentProviderOperatorDao.persist(contentProviderOperator);
		}

	}

	@Transactional(readOnly = true)
	public List<ContentProviderOperator> searchContentProviderOperator(SearchExpression searchExpression) {
		return contentProviderOperatorDao.search(searchExpression);
	}

	public void deleteContentProviderOperator(Long id) {
		contentProviderOperatorDao.remove(contentProviderOperatorDao.findById(id));
	}

	@Transactional(readOnly = true)
	public List<Platform> searchPlatform(SearchExpression searchExpression) {
		return platformDao.search(searchExpression);
	}

	public long countPlatforms(SearchExpression searchExpression) {
		return platformDao.searchCount(searchExpression);
	}

	public void associateRestrictedTypes(Long assetId, Long versionId, Set<Long> restrictionTypeIds) {
		if (assetId == null || restrictionTypeIds == null || restrictionTypeIds.size() == 0) {
			throw new NgpRuntimeException("invalid assetId or restrictionTypeIds.");
		}
		Asset asset = assetDao.findById(assetId);
		if (asset == null) {
			throw new EntityNotFoundException("asset not found.");
		} else {
			Set<AssetRestrictionRelation> relations = null;
			if (versionId != null) {
				AssetBinaryVersion binaryVersion = binaryVersionDao.findById(versionId);
				if (binaryVersion == null) {
					throw new EntityNotFoundException("binaryVersion not found.");
				} else {
					relations = binaryVersion.getAssetRestrictionRelations();
				}
			} else {
				relations = asset.getAssetRestrictionRelations();
			}

			if (relations != null) {
				Iterator<AssetRestrictionRelation> iter = relations.iterator();
				while (iter.hasNext()) {
					AssetRestrictionRelation relation = iter.next();
					assetRestrictionRelationDao.remove(relation);
				}
			}
			Iterator<Long> iterator = restrictionTypeIds.iterator();
			while (iterator.hasNext()) {
				Long restrictionTypeId = iterator.next();
				AssetRestrictionRelation assetRestrictionRelation = new AssetRestrictionRelation();
				assetRestrictionRelation.setAsset(new Asset(assetId));
				if (versionId != null) {
					assetRestrictionRelation.setBinaryVersion(new AssetBinaryVersion(versionId));
				}
				RestrictedType restrictedType = restrictedTypeDao.findById(restrictionTypeId);
				assetRestrictionRelation.setRestrictedType(restrictedType);
				assetRestrictionRelationDao.persist(assetRestrictionRelation);
			}

			this.updateAssetUpdateDate(asset);
			this.updateBinaryVersionUpdateDate(versionId);
		}
	}

	public void deleteComments(Long commentId) {
		commentsDao.remove(commentId);
	}

	public Comments getComments(Long commentsId) {
		return commentsDao.findById(commentsId);
	}

	public byte[] getFileFromDatabase(String fileLocation) {
		BinaryFile binaryFile = binaryFileDao.findUniqueBy("fileLocation", fileLocation);
		if (null != binaryFile) {
			return binaryFile.getFileBinary();
		} else {
			return null;
		}
	}

	@Transactional(readOnly = true)
	public long searchContentProviderOperatorCount(SearchExpression searchExpression) {
		return contentProviderOperatorDao.searchCount(searchExpression);
	}

	private void updateAssetUpdateDate(Long assetId) {
		if (null != assetId) {
			Asset asset = assetDao.findById(assetId);
			this.updateAssetUpdateDate(asset);
		}
	}

	private void updateAssetUpdateDate(Asset asset) {
		if (asset != null) {
			asset.setUpdateDate(Utils.getCurrentDate());
			assetDao.merge(asset);
		}
	}

	private void updateBinaryVersionUpdateDate(Long versionId) {
		if (versionId != null) {
			AssetBinaryVersion binaryVersion = binaryVersionDao.findById(versionId);
			this.updateBinaryVersionUpdateDate(binaryVersion);
		}
	}

	private void updateBinaryVersionUpdateDate(AssetBinaryVersion binaryVersion) {
		if (binaryVersion != null) {
			binaryVersion.setUpdateDate(Utils.getCurrentDate());
			binaryVersionDao.merge(binaryVersion);
		}
	}

	public void deleteScreenShotsByAssetId(Long assetId) throws DeleteFileFailureException {
		String location = this.getAssetDirectory(assetDao.findById(assetId)) + "/screenshots";
		String sqlStr = "delete from ScreenShots ss where ss.asset.id = ?";
		this.deleteFile(filePath + location);
		screenShotsDao.executeHQLBySequenceParam(sqlStr, assetId);

	}
}
