package com.hp.sdf.ngp.android.util;

import java.io.File;
import java.util.Collections;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.util.Log;

public class AndroidUtils {

	private static final String ANDROID_TYPE = "application/vnd.android.package-archive";

	/**
	 * 
	 * @param suffixUrl
	 * @return
	 */
	public static String getImageUrlBySuffix(String suffixUrl) {

		String imageUrl = "";

		if (suffixUrl != null && !"".equals(suffixUrl)) {
			imageUrl = SystemManager.getStorefrontUrl()
					+ SystemManager.getImageUrlPrefix()
					+ (suffixUrl.startsWith("/") ? suffixUrl : "/" + suffixUrl);
		}

		return imageUrl;
	}

	/**
	 * 
	 * @param context
	 * @param fileName /mnt/sdcard/hello.apk
	 */
	public static void install(Context context, String fileName) {
		Log.d("Apk Install fileName", "fileName :" + fileName);

		Intent apkintent = new Intent(Intent.ACTION_VIEW);

		Uri puri = Uri.fromFile(new File(fileName));
		apkintent.setDataAndType(puri, ANDROID_TYPE);

		context.startActivity(apkintent);
	}

	/**
	 * 
	 * @param context
	 * @param packageName
	 *            like com.hp.sdf.ngp.android
	 */
	public static void uninstall(Context context, String packageName) {
		Log.d("Apk unInstall packageName", "packageName :" + packageName);

		Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
		mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);

		PackageManager manager = context.getPackageManager();
		final List<ResolveInfo> apps = manager.queryIntentActivities(
				mainIntent, 0);
		Log.d("List<ResolveInfo> apps size", "apps.size :" + apps.size());

		Log.d("Sort apps", "Sort apps by ResolveInfo.DisplayNameComparator.");
		Collections.sort(apps, new ResolveInfo.DisplayNameComparator(manager));

		if (apps != null) {
			for (ResolveInfo info : apps) {
				Log.d("ResolveInfo detail", "info.activityInfo.packageName :"
						+ info.activityInfo.packageName);

				if (packageName.equals(info.activityInfo.packageName)) {
					Uri uri = Uri.fromParts("package",
							info.activityInfo.packageName, null);
					Intent intentDelete = new Intent(Intent.ACTION_DELETE, uri);
					context.startActivity(intentDelete);
					break;
				}
			}
		}

	}

}
