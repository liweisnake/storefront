package com.hp.sdf.ngp.android.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.hp.sdf.ngp.ws.mobileclient.model.MobileAsset;
import com.hp.sdf.ngp.ws.mobileclient.model.MobileComment;

public class JsonUtils {

	public static final String DEFAULT_JSON_KEY = "result";

	/**
	 * 
	 * @param jsonStr
	 * @return MobileAsset List
	 * @throws Exception
	 */
	public static ArrayList<MobileAsset> getMobileAssetList(String jsonStr)
			throws Exception {
		Log.d("JsonUtils.getMobileAssetList", "jsonStr = " + jsonStr);

		ArrayList<MobileAsset> assetList = new ArrayList<MobileAsset>();

		if (jsonStr != null) {
			ArrayList<JSONObject> list = JsonUtils.getJsonObjectList(jsonStr);
			for (JSONObject jsonObj : list) {
				HashMap<String, String> map = getMapByJsonObject(jsonObj);
				MobileAsset asset = new MobileAsset(map);
				assetList.add(asset);
			}
		}

		Log.d("JsonUtils.getMobileAssetList", "assetList.size : "
				+ assetList.size());
		Log.d("JsonUtils.getMobileAssetList", "finished getMobileAssetList.");
		return assetList;
	}

	/**
	 * Parse a map with key is 'result' and value is a list.
	 * 
	 * @param jsonStr
	 * @param arrayName
	 * @return a list of String
	 * @throws JSONException
	 */
	public static ArrayList<String> getJsonStringList(String jsonStr,
			String arrayName) throws JSONException {
		Log.d("JsonUtils.getJsonStringList", "jsonStr = " + jsonStr);
		Log.d("JsonUtils.getJsonStringList", "arrayName = " + arrayName);

		ArrayList<String> jsonObjs = new ArrayList<String>();
		if (jsonStr != null) {
			JSONObject jsonObj = new JSONObject(jsonStr);
			JSONArray jobsArray = jsonObj.getJSONArray(arrayName);
			for (int i = 0; i < jobsArray.length(); i++) {
				String str = jobsArray.getString(i);
				jsonObjs.add(str);
			}
		}

		Log.d("JsonUtils.getJsonStringList", "jsonObjs.size :"
				+ jsonObjs.size());
		Log.d("JsonUtils.getJsonStringList", "finished getJsonStringList.");

		return jsonObjs;
	}

	@SuppressWarnings("unchecked")
	private static HashMap<String, String> getMapByJsonObject(JSONObject jsonObj)
			throws JSONException {
		HashMap<String, String> map = new HashMap<String, String>();

		if (jsonObj != null) {
			Iterator<String> iter = jsonObj.keys();
			while (iter.hasNext()) {
				String key = iter.next();
				map.put(key, jsonObj.getString(key));
			}
		}

		return map;
	}

	/**
	 * 
	 * @param jsonObj
	 * @param arrayName
	 * @return
	 * @throws JSONException
	 */
	private static ArrayList<JSONObject> getJsonObjectList(JSONObject jsonObj,
			String arrayName) throws JSONException {

		ArrayList<JSONObject> jsonObjs = new ArrayList<JSONObject>();

		if (jsonObj != null) {
			JSONArray jobsArray = jsonObj.getJSONArray(arrayName);
			for (int i = 0; i < jobsArray.length(); i++) {
				JSONObject jobitem = jobsArray.getJSONObject(i);
				jsonObjs.add(jobitem);
			}
		}

		return jsonObjs;
	}

	/**
	 * 
	 * @param jsonStr
	 * @return
	 */
	private static ArrayList<JSONObject> getJsonObjectList(String jsonStr) {
		if (jsonStr != null) {
			try {
				JSONObject json = new JSONObject(jsonStr);
				return getJsonObjectList(json, DEFAULT_JSON_KEY);
			} catch (JSONException e) {
				Log.e("JSON", "There was an error parsing the JSON", e);
			}
		}

		return null;
	}

	public static ArrayList<MobileComment> getMobileCommentList(String jsonStr)
			throws Exception {
		Log.d("JsonUtils.getMobileCommentList", "jsonStr = " + jsonStr);

		ArrayList<MobileComment> commentList = new ArrayList<MobileComment>();

		if (jsonStr != null) {
			ArrayList<JSONObject> jsonObjectList = JsonUtils
					.getJsonObjectList(jsonStr);
			Log.d("JsonUtils.getMobileCommentList", "jsonObjectList.size : "
					+ jsonObjectList.size());

			for (JSONObject jsonObj : jsonObjectList) {
				HashMap<String, String> map = getMapByJsonObject(jsonObj);
				Log.d("JsonUtils.getMobileCommentList",
						"map.keySet().size() : " + map.keySet().size());

				MobileComment mobileComment = new MobileComment(map);
				commentList.add(mobileComment);
			}
		}

		Log.d("JsonUtils.getMobileCommentList", "commentList.size : "
				+ commentList.size());
		Log.d("JsonUtils.getMobileCommentList",
				"finished getMobileCommentList.");
		return commentList;
	}

}
