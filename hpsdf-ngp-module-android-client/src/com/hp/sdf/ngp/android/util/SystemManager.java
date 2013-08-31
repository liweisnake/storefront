package com.hp.sdf.ngp.android.util;

public class SystemManager {

	private static String user;

	private static String password;

	private static String downloadProxyHost;

	private static int downloadProxyPort;

	private static String storefrontUrl;

	private static String downloadFilePath;

	private static String serialNumber;

	private static String webUrlPrefix;

	private static String imageUrlPrefix;

	private static String pageItem;

	public static String getUser() {
		return user;
	}

	public static void setUser(String user) {
		SystemManager.user = user;
	}

	public static String getPassword() {
		return password;
	}

	public static void setPassword(String password) {
		SystemManager.password = password;
	}

	public static String getDownloadProxyHost() {
		return downloadProxyHost;
	}

	public static void setDownloadProxyHost(String downloadProxyHost) {
		SystemManager.downloadProxyHost = downloadProxyHost;
	}

	public static int getDownloadProxyPort() {
		return downloadProxyPort;
	}

	public static void setDownloadProxyPort(int downloadProxyPort) {
		SystemManager.downloadProxyPort = downloadProxyPort;
	}

	public static String getStorefrontUrl() {
		return storefrontUrl;
	}

	public static void setStorefrontUrl(String storefrontUrl) {
		SystemManager.storefrontUrl = storefrontUrl;
	}

	public static String getDownloadFilePath() {
		return downloadFilePath;
	}

	public static void setDownloadFilePath(String downloadFilePath) {
		SystemManager.downloadFilePath = downloadFilePath;
	}

	public static String getSerialNumber() {
		return serialNumber;
	}

	public static void setSerialNumber(String serialNumber) {
		SystemManager.serialNumber = serialNumber;
	}

	public static String getWebUrlPrefix() {
		return webUrlPrefix;
	}

	public static void setWebUrlPrefix(String webUrlPrefix) {
		SystemManager.webUrlPrefix = webUrlPrefix;
	}

	public static String getImageUrlPrefix() {
		return imageUrlPrefix;
	}

	public static void setImageUrlPrefix(String imageUrlPrefix) {
		SystemManager.imageUrlPrefix = imageUrlPrefix;
	}

	public static String getPageItem() {
		return pageItem;
	}

	public static void setPageItem(String pageItem) {
		SystemManager.pageItem = pageItem;
	}

}
