package com.hp.sdf.ngp.ws.mobileclient.model;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

public class MobileAsset implements Serializable {

	private static final long serialVersionUID = 6003682346348279139L;

	private Long id;

	private String name;

	private String downloadUrl;

	private String brief;

	private String author;

	private String description;

	private String version;

	private Date lastUpdateTime;

	private String thumbnailUrl;

	private String preview1Url;

	private String preview2Url;

	private Double rating;

	private String price;

	public MobileAsset() {
		super();
	}

	public MobileAsset(Long id, String name, String downloadUrl, String brief,
			String author, String description, String version,
			Date lastUpdateTime, String thumbnailUrl, String preview1Url,
			String preview2Url, Double rating, String price) {
		super();
		this.id = id;
		this.name = name;
		this.downloadUrl = downloadUrl;
		this.brief = brief;
		this.author = author;
		this.description = description;
		this.version = version;
		this.lastUpdateTime = lastUpdateTime;
		this.thumbnailUrl = thumbnailUrl;
		this.preview1Url = preview1Url;
		this.preview2Url = preview2Url;
		this.rating = rating;
		this.price = price;
	}

	/**
	 * For android client.
	 * 
	 * @param jsonMap
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public MobileAsset(Map<String, String> jsonMap) throws Exception {
		Set<Entry<String, String>> assetStrSet = jsonMap.entrySet();

		for (Entry<String, String> entry : assetStrSet) {
			if (entry.getValue() == null || "".equals(entry.getValue())) {
				continue;
			}

			String key = entry.getKey();

			Field field = this.getClass().getDeclaredField(key);
			Type t = field.getType();
			String classStr = t.toString().split(" ")[1];
			Class fieldClass = Class.forName(classStr);

			String methodStr = "set" + key.substring(0, 1).toUpperCase()
					+ key.substring(1);

			Method method = this.getClass().getMethod(methodStr, fieldClass);

			Object param = entry.getValue();
			if ("java.util.Date".equals(classStr)) {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				param = sdf.parse(entry.getValue());
			} else if ("java.lang.Long".equals(classStr)) {
				param = Long.parseLong(entry.getValue());
			} else if ("java.lang.Double".equals(classStr)) {
				param = Double.parseDouble(entry.getValue());
			}

			method.invoke(this, param);
		}
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDownloadUrl() {
		return downloadUrl;
	}

	public void setDownloadUrl(String downloadUrl) {
		this.downloadUrl = downloadUrl;
	}

	public String getBrief() {
		return brief;
	}

	public void setBrief(String brief) {
		this.brief = brief;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public Date getLastUpdateTime() {
		return lastUpdateTime;
	}

	public void setLastUpdateTime(Date lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}

	public String getThumbnailUrl() {
		return thumbnailUrl;
	}

	public void setThumbnailUrl(String thumbnailUrl) {
		this.thumbnailUrl = thumbnailUrl;
	}

	public String getPreview1Url() {
		return preview1Url;
	}

	public void setPreview1Url(String preview1Url) {
		this.preview1Url = preview1Url;
	}

	public String getPreview2Url() {
		return preview2Url;
	}

	public void setPreview2Url(String preview2Url) {
		this.preview2Url = preview2Url;
	}

	public Double getRating() {
		return rating;
	}

	public void setRating(Double rating) {
		this.rating = rating;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	// public static void main(String[] args) throws Exception {
	// Map<String, String> map = new HashMap<String, String>();
	// map.put("name", "demo");
	// map.put("author", "levi");
	// map.put("description", "abcdfe");
	// map.put("rating", "1.5");
	// map.put("lastUpdateTime", "2010-03-20");
	//
	// MobileAsset asset = new MobileAsset(map);
	//
	// System.out.println(asset.getLastUpdateTime());
	// }
}
