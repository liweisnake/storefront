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
package com.hp.sdf.ngp.handmark.impl;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.apache.axiom.attachments.utils.IOUtils;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

import com.hp.sdf.ngp.api.comon.EntityType;
import com.hp.sdf.ngp.common.annotation.Value;
import com.hp.sdf.ngp.common.constant.AssetLifecycleConstants;
import com.hp.sdf.ngp.common.constant.HandmarkConstant;
import com.hp.sdf.ngp.handmark.AssetImportor;
import com.hp.sdf.ngp.handmark.model.Data;
import com.hp.sdf.ngp.handmark.model.Description;
import com.hp.sdf.ngp.handmark.model.DisplayName;
import com.hp.sdf.ngp.handmark.model.FullDescription;
import com.hp.sdf.ngp.handmark.model.Prices;
import com.hp.sdf.ngp.handmark.model.Product;
import com.hp.sdf.ngp.handmark.model.device.Device;
import com.hp.sdf.ngp.handmark.model.device.Message;
import com.hp.sdf.ngp.model.Asset;
import com.hp.sdf.ngp.model.AssetBinaryVersion;
import com.hp.sdf.ngp.model.AssetPrice;
import com.hp.sdf.ngp.model.Category;
import com.hp.sdf.ngp.model.Platform;
import com.hp.sdf.ngp.model.Status;
import com.hp.sdf.ngp.service.ApplicationService;
import com.hp.sdf.ngp.service.AssetPictureType;

@Component
public class AssetImportorImpl implements AssetImportor {

	private static final Log log = LogFactory.getLog(AssetImportorImpl.class);

	private static final String COM_HP_SDF_NGP_HANDMARK_MASTER = "com.hp.sdf.ngp.handmark.model";

	private static final String COM_HP_SDF_NGP_HANDMARK_DEVICE = "com.hp.sdf.ngp.handmark.model.device";

	private String language = "eng";

	private int descriptionLength = 4000;

	private Status publishedStatus;

	private int threadNum = 50;

	private int capacity = 10000;

	public int getThreadNum() {
		return threadNum;
	}

	@Value("hardmark.threadpool.number")
	public void setThreadNum(int threadNum) {
		this.threadNum = threadNum;
	}

	public int getCapacity() {
		return capacity;
	}

	@Value("hardmark.threadpool.capacity")
	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}

	public String getLanguage() {
		return language;
	}

	@Value("hardmark.language")
	public void setLanguage(String language) {
		this.language = language;
	}

	public int getDescriptionLength() {
		return descriptionLength;
	}

	@Value("hardmark.description.length")
	public void setDescriptionLength(int descriptionLength) {
		this.descriptionLength = descriptionLength;
	}

	@Resource
	private ApplicationService applicationService;

	private void initPublishedStatus() {
		publishedStatus = applicationService
				.getStatusByName(AssetLifecycleConstants.STATUS_PUBLISHED);
		if (null == publishedStatus) {
			publishedStatus = new Status();
			publishedStatus.setStatus(AssetLifecycleConstants.STATUS_PUBLISHED);
			applicationService.saveStatus(publishedStatus);
		}

	}

	/**
	 * parse the XML provided by handmark and save the asset into database. At
	 * the same time, the category and platform will be stored also
	 * 
	 * @param masterXMLInputStream
	 * @param deviceXMLInputStream
	 * @return import success or fail
	 * @throws JAXBException 
	 */
	public boolean importAsset(InputStream masterXMLInputStream,
			InputStream deviceXMLInputStream) throws JAXBException {

		initPublishedStatus();

		boolean flag = false;


			Data data = getProductData(masterXMLInputStream);

			List<Device> devices = getDevices(deviceXMLInputStream);

			// short means category id from handmark
			Map<Short, Category> categoryMaps = parseAndSaveHandmarkCategories(data);

			// short means device id from handmark
			Map<Short, Platform> platformMaps = parseAndSaveHandmarkPlatforms(devices);

			// get all handmark products
			List<Product> productList = data.getProducts().getProduct();

			if (productList != null) {
				log.info("Start to save handmark product to storefront.");
				InsertAssetThreadPool insertAssetThreadPool = new InsertAssetThreadPool(
						this.threadNum, this.capacity);
				insertAssetThreadPool.executeTasks(productList, categoryMaps,
						platformMaps);

				flag = true;
			}



		return flag;
	}

	/**
	 * saveProductAsAssetAndBinary.
	 * 
	 * @param categoryMaps
	 *            categoryMaps
	 * @param platformMaps
	 *            platformMaps
	 * @param product
	 *            product
	 */
	private void saveProductAsAssetAndBinary(Product product,
			Map<Short, Category> categoryMaps, Map<Short, Platform> platformMaps) {

		Asset asset = parseToAsset(product);

		// get platforms for handmark product
		Map<Platform, List<Short>> handmarkPlatforms = new HashMap<Platform, List<Short>>();
		List<Short> deviceIds = product.getDevices().getDevice();
		if (deviceIds != null) {
			for (Short device_id : deviceIds) {
				Platform platform = platformMaps.get(device_id);
				if (platform != null) {
					List<Short> deviceIdsforPlatform = handmarkPlatforms
							.get(platform);
					if (null == deviceIdsforPlatform) {
						deviceIdsforPlatform = new ArrayList<Short>();
					}
					deviceIdsforPlatform.add(device_id);
					handmarkPlatforms.put(platform, deviceIdsforPlatform);
				}
			}
		}

		Iterator<Entry<Platform, List<Short>>> iter = handmarkPlatforms
				.entrySet().iterator();
		while (iter.hasNext()) {
			log.info("Getting clone asset.");
			Asset clonedAsset = getCloneAsset(asset);
			if (null == clonedAsset) {
				continue;
			}

			log.info("Saving new created Asset.");
			applicationService.saveAsset(clonedAsset);
			Long assetId = clonedAsset.getId();

			List<Prices> allKindsPrice = product.getPrices();
			for (Prices p : allKindsPrice) {
				AssetPrice assetPrice = new AssetPrice();
				assetPrice.setAmount(p.getProdPrice());
				assetPrice.setCurrency(p.getCurrency());
				assetPrice.setAsset(clonedAsset);
				applicationService.saveAssetPrice(assetPrice);
			}

			Entry<Platform, List<Short>> entry = iter.next();
			Platform platform = entry.getKey();
			applicationService.associatePlatform(assetId, platform.getId());

			log.info("Saving Asset Image Attribute.");
			saveAssetImage(product, assetId);

			log.info("Saving Asset ProductId Attribute.");
			addProductIdAttribute(product, assetId);

			log.info("Saving Asset DeviceIds Attribute.");
			addDeviceIdAttribute(entry.getValue(), assetId);

			AssetBinaryVersion binaryVersion = new AssetBinaryVersion();
			binaryVersion.setAsset(clonedAsset);

			if (StringUtils.isNotEmpty(product.getVersion())) {
				binaryVersion.setVersion(product.getVersion());
			} else {
				binaryVersion.setVersion("1.0");
			}
			binaryVersion.setStatus(publishedStatus);

			log.info("Saving Asset binaryVersion.");
			applicationService.saveAssetBinary(null, binaryVersion);

			log.info("Adding Asset Categories.");
			addCategories(product, assetId, binaryVersion.getId(), categoryMaps);

			applicationService.updateAssetVersion(assetId, binaryVersion
					.getId());
		}

	}

	/**
	 * get clone asset.
	 * 
	 * @param asset
	 * @return clonedAsset
	 */
	private Asset getCloneAsset(Asset asset) {

		Asset clonedAsset = null;
		try {
			clonedAsset = (Asset) BeanUtils.cloneBean(asset);
		} catch (Exception exception) {
			exception.printStackTrace();
			log.error("exception :\n" + exception);
		}

		return clonedAsset;
	}

	/**
	 * add deviceId Attribute for asset.
	 * 
	 * @param deviceIds
	 *            handmark deviceIds for one product
	 * @param assetId
	 *            assetId
	 */
	private void addDeviceIdAttribute(List<Short> deviceIds, Long assetId) {

		if (deviceIds != null) {
			for (Short device_id : deviceIds) {
				try {
					applicationService.addAttribute(assetId, EntityType.ASSET,
							HandmarkConstant.DEVICE_ID, new Float(device_id));
				} catch (Throwable e) {
					e.printStackTrace();
					log.error("addAttribute exception for DEVICE_ID :" + e);
				}
			}
		}

	}

	/**
	 * Add categories for asset.
	 * 
	 * @param product
	 *            product
	 * @param assetId
	 *            assetId
	 * @param categoryMaps
	 *            categoryMaps
	 */
	@SuppressWarnings("unchecked")
	private void addCategories(Product product, Long assetId, Long binaryVersionId,
			Map<Short, Category> categoryMaps) {

		List<Object> handmarkCategoryIds = (List<Object>) product
				.getCategories().getContent();

		if (handmarkCategoryIds == null) {
			return;
		}

		Set<Long> categoryIds = new HashSet<Long>();
		for (Object object : handmarkCategoryIds) {
			JAXBElement element = (JAXBElement) object;
			Short categoryId = (Short) element.getValue();

			Category category = categoryMaps.get(categoryId);
			if (category != null) {
				categoryIds.add(category.getId());
			}
		}

		if (categoryIds.size() > 0) {
			applicationService.associateCategory(assetId, binaryVersionId, categoryIds);
		}
	}

	/**
	 * add productId attribute for savedAsset.
	 * 
	 * @param product
	 * @param assetId
	 */
	private void addProductIdAttribute(Product product, Long assetId) {

		log.info("add productId :" + product.getProductId() + " for assetId : "
				+ assetId);

		try {
			applicationService.addAttribute(assetId, EntityType.ASSET,
					HandmarkConstant.PRODUCT_ID, new Float(product
							.getProductId()));
		} catch (Throwable e) {
			e.printStackTrace();
			log.error("addAttribute exception for PRODUCT_ID :" + e);
		}

	}

	/**
	 * saveAssetImage.
	 * 
	 * @param product
	 * @param assetId
	 */
	private void saveAssetImage(Product product, Long assetId) {
		String imageURL = product.getImage();
		if (StringUtils.isNotEmpty(imageURL)) {
			try {
				byte[] contentBytes = IOUtils
						.getStreamAsByteArray(DownLoadHandmarkBinaryUtil
								.getContentInputStream(imageURL));
				applicationService.saveAssetPicture(assetId, contentBytes,
						AssetPictureType.THUMBNAILIMAGE, "THUMBNAILIMAGE.jpg");
			} catch (MalformedURLException exception) {
				exception.printStackTrace();
				log.error("exception :" + exception);
			} catch (IOException exception) {
				exception.printStackTrace();
				log.error("exception :" + exception);
			}
		}

		String primaryImageURL = product.getPrimaryImage();
		if (StringUtils.isNotEmpty(primaryImageURL)) {
			try {
				byte[] contentBytes = IOUtils
						.getStreamAsByteArray(DownLoadHandmarkBinaryUtil
								.getContentInputStream(primaryImageURL));
				applicationService.saveAssetPicture(assetId, contentBytes,
						AssetPictureType.THUMBNAILBIGIMAGE,
						"THUMBNAILBIGIMAGE.jpg");
			} catch (MalformedURLException exception) {
				exception.printStackTrace();
				log.error("exception :" + exception);
			} catch (IOException exception) {
				exception.printStackTrace();
				log.error("exception :" + exception);
			}
		}
	}

	/**
	 * get handmark data from master feed.
	 * 
	 * 
	 * @param deviceXMLInputStream
	 * @return Data
	 * @throws JAXBException
	 */
	private Data getProductData(InputStream masterXMLInputStream)
			throws JAXBException {
		log.info("Get master feed data.");

		JAXBContext jc = JAXBContext
				.newInstance(COM_HP_SDF_NGP_HANDMARK_MASTER);
		Unmarshaller unmarshaller = jc.createUnmarshaller();
		Data data = (Data) unmarshaller.unmarshal(masterXMLInputStream);

		return data;
	}

	/**
	 * get handmark devices from device feed.
	 * 
	 * 
	 * @param deviceXMLInputStream
	 * @return List<Device>
	 * @throws JAXBException
	 */
	private List<Device> getDevices(InputStream deviceXMLInputStream)
			throws JAXBException {
		log.info("Get device feed message.");

		JAXBContext devJc = JAXBContext
				.newInstance(COM_HP_SDF_NGP_HANDMARK_DEVICE);
		Unmarshaller deviceUnmarshaller = devJc.createUnmarshaller();
		Message devicesData = (Message) deviceUnmarshaller
				.unmarshal(deviceXMLInputStream);

		List<Device> devices = devicesData.getDevice();
		return devices;
	}

	/**
	 * parseAndSaveHandmarkCategories.
	 * 
	 * @param data
	 *            Data
	 * @return categoryMaps
	 */
	private Map<Short, Category> parseAndSaveHandmarkCategories(Data data) {

		List<com.hp.sdf.ngp.handmark.model.Category> allHandmarkCategories = new ArrayList<com.hp.sdf.ngp.handmark.model.Category>();

		log.info("Get allHandmarkCategories from data.SubCategories.");
		List<Object> handmarkCategories = data.getCatalog().getCategories()
				.getContent();
		for (Object o : handmarkCategories) {
			com.hp.sdf.ngp.handmark.model.Category superCategory = (com.hp.sdf.ngp.handmark.model.Category) o;
			List<com.hp.sdf.ngp.handmark.model.Category> categoryList = superCategory
					.getSubCategories().getCategory();
			allHandmarkCategories.addAll(categoryList);
		}

		log.info("Map handmark category.id to our Category.");
		Map<Short, Category> categoryMaps = new HashMap<Short, Category>();
		List<Category> categories = applicationService.getAllCategory(0,
				Integer.MAX_VALUE);

		// no categroy with same id in allHandmarkCategories
		for (com.hp.sdf.ngp.handmark.model.Category handmarkCatgory : allHandmarkCategories) {

			Category category = null;
			if (handmarkCatgory.getDisplayName() != null) {
				String handmarkCategoryName = handmarkCatgory.getDisplayName()
						.getValue();
				category = getCategoryByName(handmarkCategoryName, categories);
			}

			if (category != null) {
				categoryMaps.put(handmarkCatgory.getCategoryId(), category);
			}

		}

		return categoryMaps;
	}

	/**
	 * get Category by categoryName.
	 * 
	 * @param categoryName
	 *            categoryName
	 * @param categories
	 *            categories
	 * @return mappedCategory
	 */
	private Category getCategoryByName(String categoryName,
			List<Category> categories) {

		Category mappedCategory = null;
		for (Category category : categories) {
			if (category.getName().equalsIgnoreCase(categoryName)) {
				mappedCategory = category;
				break;
			}
		}

		// can not find the category, will creat a new one.
		if (null == mappedCategory) {
			log.info("new categoryName for save: " + categoryName);
			mappedCategory = new Category();
			mappedCategory.setName(categoryName);
			mappedCategory.setDescription(categoryName);

			applicationService.saveCategory(mappedCategory);
		}

		return mappedCategory;
	}

	/**
	 * save Platform if not exsit in storefront.
	 * 
	 * @param platformName
	 *            platformName
	 * @param platforms
	 *            platforms
	 */
	private void savePlatform(String platformName, List<Platform> platforms) {

		Platform mappedPlatform = null;
		for (Platform platform : platforms) {
			if (platform.getName().equalsIgnoreCase(platformName)) {
				mappedPlatform = platform;
				break;
			}
		}

		// can not find the platform, will creat a new one.
		if (null == mappedPlatform) {
			log.info("new platformName for save: " + platformName);
			mappedPlatform = new Platform();
			mappedPlatform.setDescription(platformName);
			mappedPlatform.setName(platformName);

			applicationService.savePlatform(mappedPlatform);
		}
	}

	/**
	 * Get platform by name.
	 * 
	 * @param handmarkPlatformName
	 *            handmarkPlatformName
	 * @param updatedPlatforms
	 *            updatedPlatforms
	 * @return
	 */
	private Platform getPlatformByName(String handmarkPlatformName,
			List<Platform> updatedPlatforms) {

		if (updatedPlatforms != null) {
			for (Platform platform : updatedPlatforms) {
				if (handmarkPlatformName.equalsIgnoreCase(platform.getName())) {
					return platform;
				}
			}
		}

		return null;
	}

	/**
	 * parse And Save Handmark Platforms.
	 * 
	 * @param devices
	 * @return platformMaps
	 */
	private Map<Short, Platform> parseAndSaveHandmarkPlatforms(
			List<Device> devices) {

		log.info("Get distinct handmark platform names by devices.");
		Set<String> handmarkPlatformNames = new HashSet<String>();
		if (devices != null) {
			for (Device device : devices) {
				String handmarkPlatformName = device.getPlatform();
				handmarkPlatformNames.add(handmarkPlatformName);
			}
		}

		log.info("Save the new handmark platform if not exsit in storefront.");
		List<Platform> platforms = applicationService.getAllPlatform(0,
				Integer.MAX_VALUE);
		for (String distinctHandmarkPlatformName : handmarkPlatformNames) {
			savePlatform(distinctHandmarkPlatformName, platforms);
		}

		log.info("Map handmark deviceId to storefront Platform.");
		Map<Short, Platform> platformMaps = new HashMap<Short, Platform>();
		List<Platform> updatedPlatforms = applicationService.getAllPlatform(0,
				Integer.MAX_VALUE);

		for (Device device : devices) {
			String handmarkPlatformName = device.getPlatform();
			Platform mappedPlatform = getPlatformByName(handmarkPlatformName,
					updatedPlatforms);

			if (null != mappedPlatform) {
				platformMaps.put(device.getDeviceId(), mappedPlatform);
			}
		}

		return platformMaps;
	}

	/**
	 * parse handmark product to Asset.
	 * 
	 * @param product
	 * @return asset
	 */
	private Asset parseToAsset(Product product) {

		log.info("Parsing product to asset.");
		Asset asset = new Asset();

		List<Description> descriptions = product.getDescription();
		for (Description des : descriptions) {
			if (language.equalsIgnoreCase(des.getLanguage())) {

				String briefValue = des.getValue();
				if (StringUtils.isNotEmpty(briefValue)) {

					if (briefValue.length() >= descriptionLength) {
						briefValue = briefValue.substring(0, descriptionLength);
					}

					asset.setBrief(briefValue);
				}
			}
		}

		List<FullDescription> fullDescriptions = product.getFullDescription();
		for (FullDescription fd : fullDescriptions) {
			if (language.equalsIgnoreCase(fd.getLanguage())) {

				String descriptionValue = fd.getValue();
				if (StringUtils.isNotEmpty(descriptionValue)) {
					if (descriptionValue.length() >= descriptionLength) {
						descriptionValue = descriptionValue.substring(0,
								descriptionLength);
					}

					asset.setDescription(descriptionValue);
				}
			}
		}

		List<DisplayName> displayNames = product.getDisplayName();
		for (DisplayName dn : displayNames) {
			if (language.equalsIgnoreCase(dn.getLanguage())) {
				asset.setName(dn.getValue());
			}
		}

		// Provider assetProvider = applicationService
		// .getAssetProviderByName(product.getDeveloper());
		// if (null == assetProvider) {
		// assetProvider = new Provider();
		// assetProvider.setName(product.getDeveloper());
		// applicationService.saveAssetProvider(assetProvider);
		// }
		// asset.setAssetProvider(assetProvider);

		asset.setAuthorid(product.getDeveloper());
		asset.setAverageUserRating(NumberUtils.toDouble(String.valueOf(product
				.getRating())));
		asset.setCurrentVersion(product.getVersion());
		asset.setUpdateDate(product.getVersionLastUpdated()
				.toGregorianCalendar().getTime());
		asset.setCreateDate(new Date());

		asset.setSource(HandmarkConstant.HANDMARK);
		asset.setStatus(publishedStatus);

		return asset;
	}

	class InsertAssetThreadPool {

		private ThreadPoolExecutor threadPoolExecutor;

		public InsertAssetThreadPool(int threadNum, int capacity) {
			threadPoolExecutor = new ThreadPoolExecutor(threadNum, threadNum,
					0L, TimeUnit.MILLISECONDS,
					new LinkedBlockingQueue<Runnable>(capacity), Executors
							.defaultThreadFactory());

		}

		public void executeTasks(final List<Product> productList,
				final Map<Short, Category> categoryMaps,
				final Map<Short, Platform> platformMaps) {

			threadPoolExecutor.submit(new Runnable() {
				public void run() {
					synchronized (threadPoolExecutor) {
						for (int i = 0; i < productList.size(); i++) {
							final Product product = productList.get(i);
							try {
								threadPoolExecutor.submit(new Runnable() {
									public void run() {
										try {
											saveProductAsAssetAndBinary(
													product, categoryMaps,
													platformMaps);
										} catch (Throwable e) {
											e.printStackTrace();
											log.warn(e);
										}

									}
								});
							} catch (RejectedExecutionException e) {
								e.printStackTrace();
								try {
									threadPoolExecutor.wait(6000);
								} catch (Throwable ex) {
									ex.printStackTrace();
								}

								i--;
							}
						}

						threadPoolExecutor.shutdown();
					}
				}

			});

		}

	}

}
