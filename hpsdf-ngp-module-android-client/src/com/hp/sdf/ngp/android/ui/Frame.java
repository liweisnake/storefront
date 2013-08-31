package com.hp.sdf.ngp.android.ui;

import java.util.ArrayList;
import java.util.concurrent.Future;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.TabActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.DialogInterface.OnDismissListener;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;

import com.hp.sdf.ngp.android.R;
import com.hp.sdf.ngp.android.ui.common.CategoryPagableList;
import com.hp.sdf.ngp.android.util.RemoteService;
import com.hp.sdf.ngp.android.util.SystemManager;

public class Frame extends TabActivity {

	private static final int SIGN_IN_DIALOG_ID = 0;

	private boolean isLogin = false;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		init();

		TabHost tabHost = getTabHost();

		LayoutInflater.from(this).inflate(R.layout.frame,
				tabHost.getTabContentView(), true);

		final String popular = getResources().getText(R.string.popular)
				.toString();

		final String category = getResources().getText(R.string.category)
				.toString();

		final String myPortal = getResources().getText(R.string.myPortal)
				.toString();

		final String search = getResources().getText(R.string.search)
				.toString();

		tabHost.addTab(tabHost.newTabSpec(popular).setIndicator(popular,
				getResources().getDrawable(R.drawable.featured_normal))
				.setContent(new Intent(this, Popular.class)));

		tabHost.addTab(tabHost.newTabSpec(category).setIndicator(category,
				getResources().getDrawable(R.drawable.category_normal))
				.setContent(new Intent(this, Category.class)));

		tabHost.addTab(tabHost.newTabSpec(myPortal).setIndicator(myPortal,
				getResources().getDrawable(R.drawable.myportal_normal))
				.setContent(new Intent(this, MyPortal.class)));

		tabHost.addTab(tabHost.newTabSpec(search).setIndicator(search,
				getResources().getDrawable(R.drawable.search_normal))
				.setContent(new Intent(this, Search.class)));

		tabHost.setOnTabChangedListener(new OnTabChangeListener() {
			public void onTabChanged(String tabId) {
				if (tabId.equals(category)) {
					Log.d("Frame.onCreate", "Come to the category part.");

					try {
						SharedPreferences preferences = PreferenceManager
								.getDefaultSharedPreferences(Frame.this);

						String itemsPerPageStr = preferences.getString(
								"itemsPerPageStr", "10");
						Log.d("Frame.onCreate", "itemsPerPageStr :"
								+ itemsPerPageStr);

						// default value
						int itemsPerPage = 10;
						if (null != itemsPerPageStr
								&& "".equals(itemsPerPageStr)) {
							itemsPerPage = Integer.parseInt(itemsPerPageStr);
						}

						Future<ArrayList<String>> ctg = RemoteService
								.getAllCategory();
						ArrayList<String> categories = ctg.get();

						for (String categoryName : categories) {
							if (Category.getAppGroupList().contains(
									categoryName)) {
								continue;
							}

							CategoryPagableList categoryPagableList = new CategoryPagableList(
									categoryName);
							categoryPagableList.getPage().setItemsPerPage(
									itemsPerPage);
							Category.getAppGroupList().add(categoryPagableList);
						}
					} catch (Exception e) {
						e.printStackTrace();
						Log.e("error", "error when click category.");
					}

				}
			}
		});

		showDialog(SIGN_IN_DIALOG_ID);
	}

	protected Dialog onCreateDialog(int id) {

		switch (id) {
		case SIGN_IN_DIALOG_ID:
			Dialog signInDialog = getSignInDialog();
			return signInDialog;
		}

		return null;
	}

	private Dialog getSignInDialog() {
		LayoutInflater factory1 = LayoutInflater.from(this);
		final View textEntryView = factory1.inflate(R.layout.sign_in, null);

		AlertDialog.Builder builder = new AlertDialog.Builder(Frame.this);
		builder.setView(textEntryView);
		builder.setIcon(R.drawable.signin);
		builder.setTitle(R.string.signIn);

		builder.setPositiveButton(R.string.ok,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {

						EditText editUserName = (EditText) textEntryView
								.findViewById(R.id.username_edit);
						EditText editPassword = (EditText) textEntryView
								.findViewById(R.id.password_edit);

						String userName = editUserName.getText().toString();
						String password = editPassword.getText().toString();
						Log.d("userName", "userName :" + userName);
						Log.d("password", "password :" + password);

						if (userName != null && !"".equals(userName)
								&& password != null && !"".equals(password)) {
							try {
								isLogin = true;
									/*
									RemoteService.isValidUser(userName,
										password).get();
										*/

								SystemManager.setUser(userName);
								SystemManager.setPassword(password);
							} catch (Exception e) {
								e.printStackTrace();
								Log.e("RemoteService.isValidUser error",
										"exeception :" + e);
								
								Animation shake = AnimationUtils.loadAnimation(
										Frame.this, R.anim.shake);
								textEntryView.startAnimation(shake);
							}

						} else {
							Animation shake = AnimationUtils.loadAnimation(
									Frame.this, R.anim.shake);
							textEntryView.startAnimation(shake);
						}
					}
				});

		builder.setNegativeButton(R.string.cancel,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						Log.d("Cancel Input", "Cancel Input.");
						System.exit(0);
					}
				});

		Dialog dia = builder.create();

		dia.setOnDismissListener(new OnDismissListener() {
			public void onDismiss(DialogInterface dia) {
				if (!isLogin) {
					showDialog(SIGN_IN_DIALOG_ID);
					Animation shake = AnimationUtils.loadAnimation(
							Frame.this, R.anim.shake);
					textEntryView.startAnimation(shake);
				}
			}
		});
		return dia;
	}

	private void init() {
		CharSequence charStr = this.getResources().getText(
				R.string.webUrlPrefix);
		if (charStr != null) {
			SystemManager.setWebUrlPrefix(charStr.toString());
		}
		Log.d("webUrlPrefix :", "" + SystemManager.getWebUrlPrefix());

		charStr = this.getResources().getText(R.string.imageUrlPrefix);
		if (charStr != null) {
			SystemManager.setImageUrlPrefix(charStr.toString());
		}
		Log.d("imageUrlPrefix :", "" + SystemManager.getImageUrlPrefix());

		charStr = this.getResources().getText(R.string.downloadFilePath);
		if (charStr != null) {
			SystemManager.setDownloadFilePath(charStr.toString());
		}
		Log.d("downloadFilePath :", "" + SystemManager.getDownloadFilePath());

		charStr = this.getResources().getText(R.string.downloadProxyHost);
		if (charStr != null) {
			SystemManager.setDownloadProxyHost(charStr.toString());
		}
		Log.d("downloadProxyHost :", "" + SystemManager.getDownloadProxyHost());

		charStr = this.getResources().getText(R.string.downloadProxyPort);
		if (charStr != null) {
			try {
				SystemManager.setDownloadProxyPort(Integer.parseInt(charStr
						.toString()));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		Log.d("downloadProxyPort :", "" + SystemManager.getDownloadProxyPort());

		charStr = this.getResources().getText(R.string.storefrontUrl);
		if (charStr != null) {
			SystemManager.setStorefrontUrl(charStr.toString());
		}
		Log.d("storefrontUrl :", "" + SystemManager.getStorefrontUrl());

		TelephonyManager tm = (TelephonyManager) this
				.getSystemService(Context.TELEPHONY_SERVICE);
		String simSerialNumber = tm.getSimSerialNumber();
		if (simSerialNumber != null) {
			SystemManager.setSerialNumber(simSerialNumber);
		}
		Log.d("simSerialNumber :", "" + SystemManager.getSerialNumber());
	}

}
