package com.hp.sdf.ngp.android.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.SocketAddress;
import java.net.URL;
import java.net.URLConnection;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

import org.json.JSONObject;

import android.graphics.drawable.Drawable;
import android.util.Log;

import com.hp.sdf.ngp.android.ui.common.DownloadProgressDialog;
import com.hp.sdf.ngp.ws.mobileclient.model.MobileAsset;
import com.hp.sdf.ngp.ws.mobileclient.model.MobileComment;

/**
 * 
 * RemoteService.
 * 
 */
public class RemoteService {

	private static final String REST_URL_PREFIX = SystemManager
			.getStorefrontUrl()
			+ SystemManager.getWebUrlPrefix();

	public static Future<ArrayList<String>> getAllCategory() {

		final String methodName = "category";
		Log.d("RemoteService.getAllCategory", "methodName :" + methodName);

		FutureTask<ArrayList<String>> ft = new FutureTask<ArrayList<String>>(
				new Callable<ArrayList<String>>() {
					public ArrayList<String> call() throws Exception {
						String getAllCategoryUrl = REST_URL_PREFIX + methodName;
						Log.d("RemoteService.getAllCategory",
								"getAllCategoryUrl :" + getAllCategoryUrl);

						String jsonStr = new RestClient()
								.read(getAllCategoryUrl);
						Log.d("RemoteService.getAllCategory", "jsonStr :"
								+ jsonStr);

						return JsonUtils.getJsonStringList(jsonStr,
								JsonUtils.DEFAULT_JSON_KEY);
					}
				});

		new Thread(ft).start();
		return ft;
	}

	public static Future<Long> searchCount(String keyword) {

		String encodedKeyword = encodeVariable(keyword);
		Log.d("RemoteService.searchCount", "encodedKeyword:" + encodedKeyword);

		final String methodNameTemplate = "searchCount/keyword/{0}";
		final String methodName = MessageFormat.format(methodNameTemplate,
				encodedKeyword);
		Log.d("RemoteService.searchCount", "methodName :" + methodName);

		FutureTask<Long> ft = new FutureTask<Long>(new Callable<Long>() {
			public Long call() throws Exception {

				String getCountUrl = REST_URL_PREFIX + methodName;
				Log.d("RemoteService.searchCount", "getCountUrl :"
						+ getCountUrl);

				String jsonStr = new RestClient().read(getCountUrl);
				Log.d("RemoteService.searchCount", "jsonStr :" + jsonStr);

				if (jsonStr != null) {
					JSONObject json = new JSONObject(jsonStr);
					return json.getLong(JsonUtils.DEFAULT_JSON_KEY);
				}

				return 0L;
			}
		});

		new Thread(ft).start();
		return ft;
	}

	public static Future<ArrayList<MobileAsset>> search(String keyword,
			int start, int count) {
		final String methodNameTemplate = "search/keyword/{0}/start/{1}/count/{2}";
		final String methodName = MessageFormat.format(methodNameTemplate,
				encodeVariable(keyword), start + "", count + "");

		FutureTask<ArrayList<MobileAsset>> ft = new FutureTask<ArrayList<MobileAsset>>(
				new Callable<ArrayList<MobileAsset>>() {
					public ArrayList<MobileAsset> call() throws Exception {
						String searchUrl = REST_URL_PREFIX + methodName;
						Log.d("RemoteService.search", "search :" + searchUrl);

						String jsonStr = new RestClient().read(searchUrl);

						return JsonUtils.getMobileAssetList(jsonStr);

					}
				});

		new Thread(ft).start();
		return ft;
	}

	public static Future<Double> getAssetRating(long assetId, String userId) {
		final String methodNameTemplate = "getAssetRating/assetId/{0}/userId/{1}";
		final String methodName = MessageFormat.format(methodNameTemplate,
				assetId + "", encodeVariable(userId));
		Log.d("RemoteService.getAssetRating", "methodName :" + methodName);

		FutureTask<Double> ft = new FutureTask<Double>(new Callable<Double>() {
			public Double call() throws Exception {
				String getAssetRatingUrl = REST_URL_PREFIX + methodName;
				Log.d("RemoteService.getAssetRating", "getAssetRatingUrl :"
						+ getAssetRatingUrl);

				String jsonStr = new RestClient().read(getAssetRatingUrl);
				Log.d("RemoteService.getAssetRating", "jsonStr :" + jsonStr);

				if (jsonStr != null) {
					JSONObject json = new JSONObject(jsonStr);
					Double ratingResult = json
							.getDouble(JsonUtils.DEFAULT_JSON_KEY);
					Log.d("RemoteService.getAssetRating", "ratingResult :"
							+ ratingResult);

					return ratingResult;
				}

				return 0.0;

			}
		});

		new Thread(ft).start();
		return ft;
	}

	public static Future<Boolean> isValidUser(String userId, String password) {
		final String methodNameTemplate = "isValidUser/userId/{0}/password/{1}";
		final String methodName = MessageFormat.format(methodNameTemplate,
				encodeVariable(userId), encodeVariable(password));
		Log.d("RemoteService.isValidUser", "methodName :" + methodName);

		FutureTask<Boolean> ft = new FutureTask<Boolean>(
				new Callable<Boolean>() {
					public Boolean call() throws Exception {
						String getIsValidUserUrl = REST_URL_PREFIX + methodName;
						Log.d("RemoteService.isValidUser",
								"getIsValidUserUrl :" + getIsValidUserUrl);

						String jsonStr = new RestClient()
								.create(getIsValidUserUrl);
						Log.d("RemoteService.isValidUser", "jsonStr :"
								+ jsonStr);

						if (jsonStr != null) {
							JSONObject json = new JSONObject(jsonStr);
							boolean isValidUserResult = json
									.getBoolean(JsonUtils.DEFAULT_JSON_KEY);
							Log.d("RemoteService.isValidUser",
									"isValidUserResult :" + isValidUserResult);
							return isValidUserResult;
						}

						return false;
					}
				});

		new Thread(ft).start();
		return ft;
	}

	public static Future<Boolean> comment(long assetId, String userId,
			String content) {
		final String methodNameTemplate = "comment/assetId/{0}/userId/{1}/content/{2}";
		final String methodName = MessageFormat.format(methodNameTemplate,
				assetId + "", encodeVariable(userId), encodeVariable(content));
		Log.d("RemoteService.comment", "methodName :" + methodName);

		FutureTask<Boolean> ft = new FutureTask<Boolean>(
				new Callable<Boolean>() {
					public Boolean call() throws Exception {
						String getCommentUrl = REST_URL_PREFIX + methodName;
						Log.d("RemoteService.comment", "getCommentUrl :"
								+ getCommentUrl);

						String jsonStr = new RestClient().create(getCommentUrl);
						Log.d("RemoteService.comment", "jsonStr :" + jsonStr);

						if (jsonStr != null) {
							JSONObject json = new JSONObject(jsonStr);
							boolean commentResult = json
									.getBoolean(JsonUtils.DEFAULT_JSON_KEY);
							Log.d("RemoteService.comment", "commentResult :"
									+ commentResult);
							return commentResult;
						}

						return false;
					}
				});

		new Thread(ft).start();
		return ft;
	}

	public static Future<Boolean> rating(long assetId, String userId,
			double rating) {
		final String methodNameTemplate = "rating/assetId/{0}/userId/{1}/content/{2}";
		final String methodName = MessageFormat.format(methodNameTemplate,
				assetId + "", encodeVariable(userId), rating + "");
		Log.d("RemoteService.rating", "methodName :" + methodName);

		FutureTask<Boolean> ft = new FutureTask<Boolean>(
				new Callable<Boolean>() {
					public Boolean call() throws Exception {
						String ratingUrl = REST_URL_PREFIX + methodName;
						Log
								.d("RemoteService.rating", "ratingUrl :"
										+ ratingUrl);

						String jsonStr = new RestClient().create(ratingUrl);
						Log.d("RemoteService.rating", "jsonStr :" + jsonStr);

						if (jsonStr != null) {
							JSONObject json = new JSONObject(jsonStr);
							boolean ratingResult = json
									.getBoolean(JsonUtils.DEFAULT_JSON_KEY);
							Log.d("RemoteService.rating", "ratingResult :"
									+ ratingResult);

							return ratingResult;
						}

						return false;
					}
				});

		new Thread(ft).start();
		return ft;
	}

	public static Future<ArrayList<MobileAsset>> getAssetByCategoryName(
			String categoryName, int start, int count) {

		String encodedCategoryName = encodeVariable(categoryName);
		Log.d("RemoteService.getAssetByCategoryName", "encodedCategoryName:"
				+ encodedCategoryName);

		final String methodNameTemplate = "asset/categoryName/{0}/start/{1}/count/{2}";
		final String methodName = MessageFormat.format(methodNameTemplate,
				encodedCategoryName, start + "", count + "");
		Log.d("RemoteService.getAssetByCategoryName", "methodName :"
				+ methodName);

		FutureTask<ArrayList<MobileAsset>> ft = new FutureTask<ArrayList<MobileAsset>>(
				new Callable<ArrayList<MobileAsset>>() {
					public ArrayList<MobileAsset> call() throws Exception {

						String getAssetByCategoryNameUrl = REST_URL_PREFIX
								+ methodName;
						Log.d("RemoteService.getAssetByCategoryName",
								"getAssetByCategoryNameUrl :"
										+ getAssetByCategoryNameUrl);

						String jsonStr = new RestClient()
								.read(getAssetByCategoryNameUrl);

						return JsonUtils.getMobileAssetList(jsonStr);
					}
				});

		new Thread(ft).start();
		return ft;
	}

	public static Future<Integer> getCountByCategoryName(String categoryName) {

		String encodedCategoryName = encodeVariable(categoryName);
		Log.d("RemoteService.getAssetByCategoryName", "encodedCategoryName:"
				+ encodedCategoryName);

		final String methodNameTemplate = "assetCount/categoryName/{0}";
		final String methodName = MessageFormat.format(methodNameTemplate,
				encodedCategoryName);
		Log.d("RemoteService.getCountByCategoryName", "methodName :"
				+ methodName);

		FutureTask<Integer> ft = new FutureTask<Integer>(
				new Callable<Integer>() {
					public Integer call() throws Exception {

						String getCountByCategoryNameUrl = REST_URL_PREFIX
								+ methodName;
						Log.d("RemoteService.getCountByCategoryName",
								"getCountByCategoryNameUrl :"
										+ getCountByCategoryNameUrl);

						String jsonStr = new RestClient()
								.read(getCountByCategoryNameUrl);
						Log.d("RemoteService.getCountByCategoryName",
								"jsonStr :" + jsonStr);

						if (jsonStr != null) {
							JSONObject json = new JSONObject(jsonStr);
							return json.getInt(JsonUtils.DEFAULT_JSON_KEY);
						}

						return 0;
					}
				});

		new Thread(ft).start();
		return ft;
	}

	public static Future<Long> getMyPurchasedAssetCount(String userId) {

		String encodedUserId = encodeVariable(userId);
		Log.d("RemoteService.getMyPurchasedAssetCount", "encodedUserId:"
				+ encodedUserId);

		final String methodNameTemplate = "getMyPurchasedAssetCount/userId/{0}";
		final String methodName = MessageFormat.format(methodNameTemplate,
				encodedUserId);
		Log.d("RemoteService.getMyPurchasedAssetCount", "methodName :"
				+ methodName);

		FutureTask<Long> ft = new FutureTask<Long>(new Callable<Long>() {
			public Long call() throws Exception {

				String getCountUrl = REST_URL_PREFIX + methodName;
				Log.d("RemoteService.getMyPurchasedAssetCount", "getCountUrl :"
						+ getCountUrl);

				String jsonStr = new RestClient().read(getCountUrl);
				Log.d("RemoteService.getMyPurchasedAssetCount", "jsonStr :"
						+ jsonStr);

				if (jsonStr != null) {
					JSONObject json = new JSONObject(jsonStr);
					return json.getLong(JsonUtils.DEFAULT_JSON_KEY);
				}

				return 0L;
			}
		});

		new Thread(ft).start();
		return ft;
	}

	public static Future<ArrayList<MobileAsset>> getMyPurchasedAsset(
			String userId, int start, int count) {
		final String methodNameTemplate = "myAsset/userId/{0}/start/{1}/count/{2}";
		final String methodName = MessageFormat.format(methodNameTemplate,
				encodeVariable(userId), start + "", count + "");
		Log.d("RemoteService.getMyPurchasedAsset", "methodName :" + methodName);

		FutureTask<ArrayList<MobileAsset>> ft = new FutureTask<ArrayList<MobileAsset>>(
				new Callable<ArrayList<MobileAsset>>() {
					public ArrayList<MobileAsset> call() throws Exception {
						String getMyPurchasedAssetUrl = REST_URL_PREFIX
								+ methodName;
						Log.d("RemoteService.getMyPurchasedAsset",
								"getMyPurchasedAssetUrl:"
										+ getMyPurchasedAssetUrl);

						String jsonStr = new RestClient()
								.read(getMyPurchasedAssetUrl);

						return JsonUtils.getMobileAssetList(jsonStr);
					}
				});

		new Thread(ft).start();
		return ft;
	}

	public static Future<ArrayList<MobileAsset>> getRecommendAsset() {
		final String methodName = "recommendedAssets";
		Log.d("RemoteService.getRecommendAsset", "methodName :" + methodName);

		FutureTask<ArrayList<MobileAsset>> ft = new FutureTask<ArrayList<MobileAsset>>(
				new Callable<ArrayList<MobileAsset>>() {
					public ArrayList<MobileAsset> call() throws Exception {

						String getRecommendAssetUrl = REST_URL_PREFIX
								+ methodName;
						Log.d("RemoteService.getRecommendAsset",
								"getRecommendAssetUrl:" + getRecommendAssetUrl);

						String jsonStr = new RestClient()
								.read(getRecommendAssetUrl);

						return JsonUtils.getMobileAssetList(jsonStr);
					}
				});

		new Thread(ft).start();
		return ft;
	}

	public static Future<ArrayList<MobileAsset>> getTopAsset() {
		final String methodName = "topAssets";
		Log.d("RemoteService.getTopAsset", "methodName :" + methodName);

		FutureTask<ArrayList<MobileAsset>> ft = new FutureTask<ArrayList<MobileAsset>>(
				new Callable<ArrayList<MobileAsset>>() {
					public ArrayList<MobileAsset> call() throws Exception {

						String getTopAssetUrl = REST_URL_PREFIX + methodName;
						Log.d("RemoteService.getTopAsset", "getTopAssetUrl :"
								+ getTopAssetUrl);

						String jsonStr = new RestClient().read(getTopAssetUrl);
						return JsonUtils.getMobileAssetList(jsonStr);
					}
				});

		new Thread(ft).start();
		return ft;
	}

	public static Future<Boolean> purchase(String assetId, String userId) {
		final String methodNameTemplate = "purchase/assetId/{0}/userId/{1}";
		final String methodName = MessageFormat.format(methodNameTemplate,
				assetId, encodeVariable(userId));
		Log.d("RemoteService.purchase", "methodName :" + methodName);

		FutureTask<Boolean> ft = new FutureTask<Boolean>(
				new Callable<Boolean>() {
					public Boolean call() throws Exception {

						String purchaseUrl = REST_URL_PREFIX + methodName;
						Log.d("RemoteService.purchase", "purchaseUrl :"
								+ purchaseUrl);

						String jsonStr = new RestClient().read(purchaseUrl);
						Log.d("RemoteService.purchase", "jsonStr :" + jsonStr);

						if (jsonStr != null) {
							JSONObject json = new JSONObject(jsonStr);
							return json.getBoolean(JsonUtils.DEFAULT_JSON_KEY);
						}

						return false;
					}
				});

		new Thread(ft).start();
		return ft;
	}

	public static Future<ArrayList<MobileComment>> getAssetComment(
			String assetId, int start, int count) {
		final String methodNameTemplate = "getAssetComment/assetId/{0}/start/{1}/count/{2}";
		final String methodName = MessageFormat.format(methodNameTemplate,
				assetId, start + "", count + "");
		Log.d("RemoteService.getAssetComment", "methodName :" + methodName);

		FutureTask<ArrayList<MobileComment>> ft = new FutureTask<ArrayList<MobileComment>>(
				new Callable<ArrayList<MobileComment>>() {
					public ArrayList<MobileComment> call() throws Exception {

						String getAssetCommentsUrl = REST_URL_PREFIX
								+ methodName;
						Log.d("RemoteService.getAssetComment",
								"getAssetCommentsUrl :" + getAssetCommentsUrl);

						String jsonStr = new RestClient()
								.read(getAssetCommentsUrl);
						Log.d("RemoteService.getAssetComment", "jsonStr :"
								+ jsonStr);

						return JsonUtils.getMobileCommentList(jsonStr);
					}
				});

		new Thread(ft).start();
		return ft;
	}

	public static Future<Long> getAssetCommentCount(String assetId) {
		final String methodNameTemplate = "getAssetCommentCount/assetId/{0}";
		final String methodName = MessageFormat.format(methodNameTemplate,
				assetId);
		Log
				.d("RemoteService.getAssetCommentCount", "methodName :"
						+ methodName);

		FutureTask<Long> ft = new FutureTask<Long>(new Callable<Long>() {
			public Long call() throws Exception {

				String getAssetCommentCountUrl = REST_URL_PREFIX + methodName;
				Log.d("RemoteService.getAssetCommentCount",
						"getAssetCommentCountUrl :" + getAssetCommentCountUrl);

				String jsonStr = new RestClient().read(getAssetCommentCountUrl);
				Log.d("RemoteService.getAssetCommentCount", "jsonStr :"
						+ jsonStr);

				if (jsonStr != null) {
					JSONObject json = new JSONObject(jsonStr);
					return json.getLong(JsonUtils.DEFAULT_JSON_KEY);
				}

				return 0L;
			}
		});

		new Thread(ft).start();
		return ft;
	}

	public static Future<String> getDownloadUrl(final String assetId,
			final String userId, final String serialNumber) {
		final String methodNameTemplate = "retrieveDownloadLink/assetId/{0}/userId/{1}/deviceSerial/{2}";
		final String methodName = MessageFormat.format(methodNameTemplate,
				assetId, encodeVariable(userId), serialNumber);
		Log.d("RemoteService.getDownloadUrl", "methodName :" + methodName);

		FutureTask<String> ft = new FutureTask<String>(new Callable<String>() {
			public String call() throws Exception {
				String getDownloadLinkUrl = REST_URL_PREFIX + methodName;
				Log.d("RemoteService.getDownloadUrl", "getDownloadLinkUrl :"
						+ getDownloadLinkUrl);

				String jsonStr = new RestClient().read(getDownloadLinkUrl);
				Log.d("RemoteService.getDownloadUrl", "jsonStr :" + jsonStr);

				if (jsonStr != null) {
					JSONObject json = new JSONObject(jsonStr);
					return json.getString(JsonUtils.DEFAULT_JSON_KEY);
				}

				return "";
			}
		});

		new Thread(ft).start();
		return ft;
	}

	public static void downloadFile(final DownloadProgressDialog dialog,
			final String url, final String filePath) {

		Log.d("RemoteService.downloadFile", "download file url: " + url);
		Log.d("RemoteService.downloadFile", "download filePath: " + filePath);

		new Thread(new Runnable() {
			public void run() {
				try {
					dialog.sendMessage(
							DownloadProgressDialog.FILE_DOWNLOAD_CONNECT, null);
					URL sourceUrl = new URL(url);
					URLConnection conn;

					String proxyHost = SystemManager.getDownloadProxyHost();
					if (proxyHost != null || !"".equals(proxyHost)) {
						SocketAddress addr = new InetSocketAddress(proxyHost,
								SystemManager.getDownloadProxyPort());
						Proxy proxy = new Proxy(Proxy.Type.HTTP, addr);
						conn = sourceUrl.openConnection(proxy);
					} else {
						conn = sourceUrl.openConnection();
					}

					InputStream inputStream = conn.getInputStream();
					int fileSize = conn.getContentLength();
					Log.d("RemoteService.downloadFile", "orginal file size:"
							+ fileSize + "bytes");

					File savefile = new File(filePath);
					if (savefile.exists()) {
						savefile.delete();
					}
					savefile.createNewFile();

					FileOutputStream outputStream = new FileOutputStream(
							filePath, true);

					int readCount = 0;
					int prevPercent = 0;
					while (readCount < fileSize) {
						int availSize = inputStream.available();
						if (availSize > 0) {
							byte[] buffer = new byte[availSize];
							inputStream.read(buffer);
							if (availSize > -1) {
								outputStream.write(buffer);
								readCount += availSize;

								int percent = (int) (readCount * 100 / fileSize);
								if (percent > prevPercent) {
									dialog
											.sendMessage(
													DownloadProgressDialog.FILE_DOWNLOAD_UPDATE,
													percent - prevPercent);

									prevPercent = percent;
								}
							}
						}
					}

					outputStream.close();

					dialog.sendMessage(
							DownloadProgressDialog.FILE_DOWNLOAD_COMPLETE,
							filePath);
				} catch (Exception e) {
					e.printStackTrace();
					Log.e("error when download.", e.getMessage());
				}
			}
		}).start();

	}

	public static Future<Drawable> getImage(final String urlString) {
		Log.d("RemoteService.getImage", "urlString :" + urlString);

		FutureTask<Drawable> ft = new FutureTask<Drawable>(
				new Callable<Drawable>() {
					public Drawable call() throws Exception {
						URL url = new URL(urlString);
						URLConnection connection = url.openConnection();
						InputStream is = connection.getInputStream();

						Drawable d = Drawable.createFromStream(is, "src");
						return d;
					}
				});

		new Thread(ft).start();
		return ft;
	}

	private static String encodeVariable(String pathVariable) {
		String encodedPathVariable = "";
		try {
			encodedPathVariable = java.net.URLEncoder.encode(pathVariable,
					"UTF-8");
		} catch (UnsupportedEncodingException e) {
			Log.e("URLEncoder", e.toString());
		}
		return encodedPathVariable;
	}

}
