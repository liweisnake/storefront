package com.hp.sdf.ngp.android.ui;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.Future;

import android.app.AlertDialog;
import android.app.ExpandableListActivity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnGroupCollapseListener;
import android.widget.ExpandableListView.OnGroupExpandListener;

import com.hp.sdf.ngp.android.R;
import com.hp.sdf.ngp.android.model.AppParcelable;
import com.hp.sdf.ngp.android.ui.common.AssetExpandableListAdapter;
import com.hp.sdf.ngp.android.ui.common.PagableList;
import com.hp.sdf.ngp.android.ui.common.PurchasePagableList;
import com.hp.sdf.ngp.android.util.RemoteService;
import com.hp.sdf.ngp.android.util.SystemManager;
import com.hp.sdf.ngp.ws.mobileclient.model.MobileAsset;

public class MyPortal extends ExpandableListActivity {

	private static final String myPurchase = "My Purchase";

	public static final int DOWNLOAD = 0;

	public static final int INSTALL = 1;

	public static final int UNINSTALL = 2;

	public static final int PURCHASE = 3;

	private Handler handler = new Handler();

	private ProgressDialog progressDialog = null;

	private String packageNameOfActiveFile;

	private static AssetExpandableListAdapter adapter;

	private static PagableList<PurchasePagableList> appGroupList;

	public String getPackageNameOfActiveFile() {
		return packageNameOfActiveFile;
	}

	public void setPackageNameOfActiveFile(String packageNameOfActiveFile) {
		this.packageNameOfActiveFile = packageNameOfActiveFile;
	}

	public static PagableList<PurchasePagableList> getAppGroupList() {
		return appGroupList;
	}

	public static void setAppGroupList(
			PagableList<PurchasePagableList> appGroupList) {
		MyPortal.appGroupList = appGroupList;
	}

	public static AssetExpandableListAdapter getAdapter() {
		return adapter;
	}

	public static void setAdapter(AssetExpandableListAdapter adapter) {
		MyPortal.adapter = adapter;
	}

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		appGroupList = new PagableList<PurchasePagableList>();

		PurchasePagableList nameList1 = new PurchasePagableList();
		nameList1.setName(myPurchase);
		appGroupList.add(nameList1);

		// Set up our adapter
		adapter = new AssetExpandableListAdapter(appGroupList, this,
				AssetExpandableListAdapter.PAGING);
		setListAdapter(adapter);

		OnGroupExpandListener onGroupExpandListener = new OnGroupExpandListener() {
			public void onGroupExpand(int groupPosition) {
				final PagableList<MobileAsset> nameList = appGroupList
						.get(groupPosition);

				if (myPurchase.equals(nameList.getName())) {
					progressDialog = ProgressDialog.show(MyPortal.this,
							"Please wait....", "Getting assets....", true);

					// start new thread to get assets.
					new Thread(new Runnable() {
						public void run() {
							try {
								PurchasePagableList pagableList = (PurchasePagableList) nameList;

								pagableList.flush();
								Log.d("new thread",
										"add purchased assets to nameList.");

								handler.post(new Runnable() {
									public void run() {
										Log
												.i("handler.post()",
														"mAdapter.notifyDataSetChanged().");
										adapter.notifyDataSetChanged();
									}
								});

								progressDialog.dismiss();
							} catch (Exception e) {
								progressDialog.dismiss();
								e.printStackTrace();
								Log.e("error", "when click my purchase.");
							}
						}
					}).start();
				}
			}
		};
		this.getExpandableListView().setOnGroupExpandListener(
				onGroupExpandListener);

		OnGroupCollapseListener onGroupCollapseListener = new OnGroupCollapseListener() {
			public void onGroupCollapse(int groupPosition) {
				Log.d("onGroupCollapse", "clear appGroupList.get("
						+ groupPosition + ").");
				appGroupList.get(groupPosition).clear();
				appGroupList.get(groupPosition).getPage().clear();
			}
		};
		this.getExpandableListView().setOnGroupCollapseListener(
				onGroupCollapseListener);
	}

	public boolean onChildClick(ExpandableListView parent, View v,
			int groupPosition, int childPosition, long id) {

		Log.d("MyPortal.onChildClick", "click asset at groupPosition :"
				+ groupPosition + ", childPosition :" + childPosition);

		MobileAsset mobileAsset = appGroupList.get(groupPosition).get(
				childPosition);
		Log.d("MyPortal.onChildClick", "mobileAsset.name :"
				+ (mobileAsset != null ? mobileAsset.getName() : "null"));

		Intent intent = new Intent(this, AssetDetail.class);
		AppParcelable parcelable = new AppParcelable(mobileAsset);
		intent.putExtra("app", parcelable);

		Future<String> downloadUrlTask = RemoteService.getDownloadUrl(
				mobileAsset.getId() + "", SystemManager.getUser(),
				SystemManager.getSerialNumber());
		String downloadUrl = "";
		try {
			downloadUrl = downloadUrlTask.get();
			Log.d("MyPortal.onChildClick", "downloadUrl :" + downloadUrl);
		} catch (Exception e) {
			e.printStackTrace();
		}

		int whichKindOfBtn = getWhichKindOfBtn(downloadUrl);
		Log.d("MyPortal.onChildClick", "whichKindOfBtn :" + whichKindOfBtn);
		intent.putExtra("whichKindOfBtn", whichKindOfBtn);
		intent.putExtra("downloadUrl", downloadUrl);
		intent.putExtra("packageNameOfActiveFile", this
				.getPackageNameOfActiveFile());

		startActivity(intent);

		return super.onChildClick(parent, v, groupPosition, childPosition, id);
	}

	/**
	 * getWhichKindOfBtn.
	 * 
	 * @param downloadUrl
	 * @return whichKindOfBtn
	 */
	private int getWhichKindOfBtn(String downloadUrl) {
		Log.d("getWhichKindOfBtn", "enter getWhichKindOfBtn.");

		int whichKindOfBtn = DOWNLOAD;
		if (null == downloadUrl || "".equals(downloadUrl)) {
			return whichKindOfBtn;
		}

		// like "hello.apk"
//		String apkFileName = downloadUrl.substring(downloadUrl
//				.lastIndexOf(File.separator) + 1);
//		Log.d("mobileAsset downloaded apk file", "apkFileName :" + apkFileName);
//
//		String[] apkFiles = Environment.getExternalStorageDirectory().list();
//		for (int i = 0; i < apkFiles.length; i++) {
//			String apkFile = apkFiles[i];
//			Log.d("Environment.getExternalStorageDirectory files", "apkFiles["
//					+ i + "] 's name :" + apkFile);
//			if (apkFileName.equals(apkFile)) {
//				Log.d("Find the " + apkFileName, "find the " + apkFileName
//						+ " in " + Environment.getExternalStorageDirectory());
//
//				// have downloaded this mobileAsset, display install
//				whichKindOfBtn = INSTALL;
//
//				if (haveInstalled(apkFileName)) {
//					Log.d("installed asset: " + apkFileName, apkFileName
//							+ " have been installed.");
//
//					// have installed this mobileAsset, display uninstall
//					whichKindOfBtn = UNINSTALL;
//				}
//
//				break;
//			}
//
//		}

		return whichKindOfBtn;
	}

	/**
	 * haveInstalled check.
	 * 
	 * @param apkFileName
	 * @return installedFlag
	 */
	private boolean haveInstalled(String apkFileName) {
		Log.d("haveInstalled check", "enter haveInstalled check.");

		PackageManager manager = MyPortal.this.getPackageManager();

		String archiveFilePath = Environment.getExternalStorageDirectory()
				+ "/" + apkFileName;
		Log.d("PackageInfo", "archiveFilePath :" + archiveFilePath);

		PackageInfo packageInfo = manager.getPackageArchiveInfo(
				archiveFilePath, 1);
		String packageNameOfActiveFile = (packageInfo != null) ? packageInfo.packageName
				: "";
		Log.d("PackageInfo", "packageName :" + packageNameOfActiveFile);

		Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
		mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
		final List<ResolveInfo> apps = manager.queryIntentActivities(
				mainIntent, 0);
		Log.d("List<ResolveInfo> apps size", "apps.size :" + apps.size());

		Log.d("Sort apps", "Sort apps by ResolveInfo.DisplayNameComparator.");
		Collections.sort(apps, new ResolveInfo.DisplayNameComparator(manager));

		boolean installedFlag = false;
		if (apps != null) {
			for (ResolveInfo info : apps) {
				Log.d("ResolveInfo detail", "info.activityInfo.packageName :"
						+ info.activityInfo.packageName);
				if (packageNameOfActiveFile
						.equals(info.activityInfo.packageName)) {
					Log.d("Find the have installed APK",
							"Find the have installed asset :" + apkFileName);

					this.setPackageNameOfActiveFile(packageNameOfActiveFile);
					installedFlag = true;
					break;
				}
			}
		}

		return installedFlag;
	}

	public void onGroupExpand(int groupPosition) {
		super.onGroupExpand(groupPosition);
	}

	protected void onResume() {
		super.onResume();
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Log.d("Click back", "Click back.");
			
			new AlertDialog.Builder(this).setIcon(R.drawable.alert_dialog_icon)
					.setTitle(R.string.exit).setMessage(R.string.exitWarning).setNegativeButton(
							R.string.cancel,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {

								}
							}).setPositiveButton(R.string.ok,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int whichButton) {
									Log
											.d("Exit storefront",
													"Exit storefront.");
									System.exit(0);
								}
							}).show();

			return true;
		} else {
			return super.onKeyDown(keyCode, event);
		}
	}
}
