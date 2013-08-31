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

public class MobileComment implements Serializable {

	private static final long serialVersionUID = -1114044606595036717L;

	private String userId;

	private Long assetId;

	private String assetVersion;

	private String title;

	private String comment;

	private Date date;

	public MobileComment() {

	}

	/**
	 * For android client.
	 * 
	 * @param jsonMap
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public MobileComment(Map<String, String> jsonMap) throws Exception {
		Set<Entry<String, String>> commentStrSet = jsonMap.entrySet();

		for (Entry<String, String> entry : commentStrSet) {

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
			}

			method.invoke(this, param);
		}
	}

	public Long getAssetId() {
		return assetId;
	}

	public void setAssetId(Long assetId) {
		this.assetId = assetId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getAssetVersion() {
		return assetVersion;
	}

	public void setAssetVersion(String assetVersion) {
		this.assetVersion = assetVersion;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}
}