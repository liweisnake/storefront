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
package com.hp.sdf.ngp.ui.common;

import java.io.File;
import java.io.InputStream;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.model.Model;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.hp.sdf.ngp.model.AssetPrice;
import com.hp.sdf.ngp.model.Category;
import com.hp.sdf.ngp.model.Platform;
import com.hp.sdf.ngp.model.Tag;

/**
 * @author Anders Zhu
 */
public final class Tools {
	public final static String getCategoryNameFromList(final List<Category> list) {
		StringBuffer sb = new StringBuffer();
		if (null != list) {
			for (Category category : list) {
				sb.append(category.getName() + Constant.SUFFIX);
			}
		}
		return StringUtils.chomp(sb.toString(), Constant.SUFFIX);
	}

	public final static String getTagNameFromList(final List<Tag> list) {
		StringBuffer sb = new StringBuffer();
		if (null != list) {
			for (Tag tag : list) {
				sb.append(tag.getName() + ",");
			}
		}
		return StringUtils.chomp(sb.toString(), ",");
	}

	public final static String getPlatfromNameFromList(final List<Platform> list) {
		StringBuffer sb = new StringBuffer();
		if (null != list) {
			for (Platform platform : list) {
				sb.append(platform.getName() + Constant.SUFFIX);
			}
		}
		return StringUtils.chomp(sb.toString(), Constant.SUFFIX);
	}

	public final static BigDecimal getAssetPriceFromListByCurrency(final List<AssetPrice> list, final String currency) {
		if (null != list) {
			for (AssetPrice price : list) {
				if (currency.equalsIgnoreCase(price.getCurrency())) {
					return price.getAmount();
				}
			}
		}
		return new BigDecimal(0);
	}

	public final static BigDecimal getAssetPriceFromListDollars(final List<AssetPrice> list) {

		return getAssetPriceFromListByCurrency(list, Constant.CURRENCY_DOLLARS);
	}

	public final static AssetPrice getAssetPriceBeanFromListByCurrency(final List<AssetPrice> list, final String currency) {
		if (null != list) {
			for (AssetPrice price : list) {
				if (currency.equalsIgnoreCase(price.getCurrency())) {
					return price;
				}
			}
		}
		return null;
	}

	public final static AssetPrice getAssetPriceBeanFromListDollars(final List<AssetPrice> list) {
		return getAssetPriceBeanFromListByCurrency(list, Constant.CURRENCY_DOLLARS);
	}

	public final static List<Long> getPlatformIdList(final List<Platform> platformList) {
		List<Long> platformIdList = new ArrayList<Long>();
		if (null != platformList) {
			for (Platform platform : platformList) {
				platformIdList.add(platform.getId());
			}
		}
		return platformIdList;
	}

	public final static Set<Long> getCategoryIdSet(final List<Category> categoryList) {
		Set<Long> categoryIdSet = new HashSet<Long>();
		if (null != categoryList) {
			for (Category category : categoryList) {
				categoryIdSet.add(category.getId());
			}
		}
		return categoryIdSet;
	}

	public final static boolean isPlatformIdEqual(final List<Platform> platformList, Long platformId) {
		if (null != platformList) {
			for (Platform platform : platformList) {
				if (platform.getId().equals(platformId))
					return true;
			}
		}
		return false;
	}

	public final static String getValue(Object object) {
		String returnValue = StringUtils.EMPTY;
		if (null != object) {
			if (object.getClass().getName().equals(Date.class.getName()) || object.getClass().getName().equals(Timestamp.class.getName()) || object.getClass().getName().equals(java.sql.Date.class.getName()))
				returnValue = new SimpleDateFormat(Constant.DATE_PATTERN).format((Date) object);
			else if (object.getClass().getName().equals(BigDecimal.class.getName()))
				returnValue = object.toString();
			else
				returnValue = String.valueOf(object);
		}
		return returnValue;
	}

	public final static String getValue(Object object, Boolean isFullDate) {
		String returnValue = StringUtils.EMPTY;
		if (null != object) {
			if (object.getClass().getName().equals(Date.class.getName()) || object.getClass().getName().equals(Timestamp.class.getName()) || object.getClass().getName().equals(java.sql.Date.class.getName()))
				if (isFullDate)
					returnValue = new SimpleDateFormat(Constant.FULL_DATE_PATTERN).format((Date) object);
				else
					returnValue = new SimpleDateFormat(Constant.DATE_PATTERN).format((Date) object);
			else if (object.getClass().getName().equals(BigDecimal.class.getName()))
				returnValue = object.toString();
			else
				returnValue = String.valueOf(object);
		}
		return returnValue;
	}

	public final static List<String> getTagList(String value) {
		List<String> tagList = new ArrayList<String>();
		if (StringUtils.isNotEmpty(value)) {
			String[] tagArray = value.split(",");
			for (String tag : tagArray) {
				if (tag.length() > 0)
					tagList.add(tag);
			}
		}
		return tagList;
	}

	public final static byte[] getFileBuffer(FileUploadField fileUploadField) {
		FileUpload fileUpload = fileUploadField.getFileUpload();
		InputStream inputStream;
		byte[] buffer;
		try {
			inputStream = fileUpload.getInputStream();
			buffer = new byte[inputStream.available()];
			inputStream.read(buffer);
		} catch (Exception ex) {
			throw new UIException(ex.getMessage());
		}
		return buffer;
	}

	public static Boolean regexMatch(String regex, String value) {
		String regexTemp = StringUtils.replace(regex, "?", ".");
		regexTemp = StringUtils.replace(regexTemp, "*", ".*");
		regexTemp = "(" + regexTemp + ")";
		Pattern p = Pattern.compile(regexTemp);
		Matcher m = p.matcher(value);
		return m.matches();
	}

	public static AttributeModifier addConfirmJs(String message) {
		return new AttributeModifier("onclick", true, new Model<String>("if (confirm('" + message + "')) return true; else return false;"));
	}

	public static String getFileSuffix(String fileName) {
		return StringUtils.substring(fileName, fileName.lastIndexOf(".")).toLowerCase();
	}

	public static String getFileName(String fileName) {
		return String.valueOf(new Date().getTime()) + getFileSuffix(fileName);
	}

	public static Boolean checkFilePath(String fileName) {
		File file = new File(fileName);
		if (file.exists())
			return true;
		else
			return false;
	}

	public static String getFormatDateTime(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat(Constant.DATE_PATTERN_NO_SUFFIX);
		return sdf.format(date);
	}

	public static Date parseBeginDate(Date date) {
		try {
			return new SimpleDateFormat(Constant.FULL_DATE_PATTERN).parse(new SimpleDateFormat(Constant.DATE_PATTERN).format(date) + Constant.BEGIN_DATE);
		} catch (ParseException e) {
		}
		return date;
	}

	public static Date parseEndDate(Date date) {
		try {
			return new SimpleDateFormat(Constant.FULL_DATE_PATTERN).parse(new SimpleDateFormat(Constant.DATE_PATTERN).format(date) + Constant.END_DATE);
		} catch (ParseException e) {
		}
		return date;
	}

	public static Map<String, List<String>> getStatusConfig(String configPath) {
		Map<String, List<String>> statusMap = new HashMap<String, List<String>>();
		SAXReader reader = new SAXReader();
		try {
			File file = new File(configPath + "StatusConfig.xml");
			Document doc;
			if (file.exists())
				doc = reader.read(file);
			else
				doc = reader.read(Tools.class.getResourceAsStream("StatusConfig.xml"));
			Element rooElement = doc.getRootElement();
			for (Iterator<?> iterator = rooElement.elementIterator(); iterator.hasNext();) {
				Element itemElement = (Element) iterator.next();

				List<String> newList = new ArrayList<String>();
				statusMap.put(itemElement.elementText("old").toLowerCase(), newList);

				for (Iterator<?> itNew = itemElement.element("new").elementIterator(); itNew.hasNext();) {
					Element newElement = (Element) itNew.next();
					newList.add(newElement.getText().toLowerCase());
				}
			}
		} catch (DocumentException ex) {
			ex.printStackTrace();
		}
		return statusMap;
	}

	public static Boolean checkStatus(List<String> oldStatusList, String newStatus, String configPath) {
		Map<String, List<String>> statusMap = getStatusConfig(configPath);

		if (oldStatusList != null) {
			for (String oldStatus : oldStatusList) {
				List<String> newStatusList = statusMap.get(oldStatus.toLowerCase());
				if (newStatusList == null) {
					return false;
				} else {
					if (newStatusList.contains(newStatus))
						return true;
					else
						return false;
				}
			}
		}
		return false;
	}

	public static Boolean checkDate(Date beginDate, int days) {

		if (beginDate == null)
			return false;

		Date endDate = DateUtils.addDays(beginDate, days);
		Date nowDate = new Date();
		return nowDate.before(endDate);
	}

	public static boolean isHfsz(String value) {
		if (value == null)
			return false;
		if (value.length() == value.getBytes().length) {
			return true;
		}
		return false;
	}

	public static List<String> getConfigValue(String value) {
		String[] strArray = value.split(",");
		List<String> list = new ArrayList<String>();
		for (String string : strArray)
			list.add(StringUtils.trim(string));
		return list;
	}

	public static Double getScale(Double value) {
		return getScale(value, 2);
	}

	public static Double getScale(Double value, int newScale) {
		BigDecimal bd = new BigDecimal(value);
		return bd.setScale(newScale, BigDecimal.ROUND_HALF_UP).doubleValue();
	}

	public static void main(String[] args) {

	}
}