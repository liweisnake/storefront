package com.hp.sdf.ngp.android.ui;

import android.app.AlertDialog;
import android.app.ExpandableListActivity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
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
import com.hp.sdf.ngp.android.ui.common.CategoryPagableList;
import com.hp.sdf.ngp.android.ui.common.PagableList;
import com.hp.sdf.ngp.ws.mobileclient.model.MobileAsset;

public class Category extends ExpandableListActivity {

	private static PagableList<CategoryPagableList> appGroupList = new PagableList<CategoryPagableList>();

	private static AssetExpandableListAdapter adapter;

	private Handler handler = new Handler();

	private ProgressDialog progressDialog = null;

	public static AssetExpandableListAdapter getAdapter() {
		return adapter;
	}

	public static void setAdapter(AssetExpandableListAdapter adapter) {
		Category.adapter = adapter;
	}

	public static PagableList<CategoryPagableList> getAppGroupList() {
		return appGroupList;
	}

	public static void setAppGroupList(
			PagableList<CategoryPagableList> appGroupList) {
		Category.appGroupList = appGroupList;
	}

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d("Category", "oncreate.");

		Log.d("Category", "Set up our adapter.");
		adapter = new AssetExpandableListAdapter(appGroupList, this,
				AssetExpandableListAdapter.PAGING);
		setListAdapter(adapter);

		OnGroupExpandListener onGroupExpandListener = new OnGroupExpandListener() {
			public void onGroupExpand(int groupPosition) {
				final PagableList<MobileAsset> list = appGroupList
						.get(groupPosition);

				progressDialog = ProgressDialog.show(Category.this,
						"Please wait....", "Getting assets....", true);

				// start new thread to get assets.
				new Thread(new Runnable() {
					public void run() {
						try {
							CategoryPagableList pagableList = (CategoryPagableList) list;

							pagableList.flush();
							Log.d("new thread", "add assets to nameList.");

							handler.post(new Runnable() {
								public void run() {
									Log.d("handler.post()",
											"mAdapter.notifyDataSetChanged().");
									adapter.notifyDataSetChanged();
								}
							});

							progressDialog.dismiss();
						} catch (Exception e) {
							progressDialog.dismiss();
							e.printStackTrace();
							Log.e("error", "when click category.");
						}
					}
				}).start();

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

		Log.d("Category", "finish oncreate.");
	}

	public boolean onChildClick(ExpandableListView parent, View v,
			int groupPosition, int childPosition, long id) {

		Intent intent = new Intent(this, AssetDetail.class);
		AppParcelable parcelable = new AppParcelable(appGroupList.get(
				groupPosition).get(childPosition));
		intent.putExtra("app", parcelable);
		startActivity(intent);

		return super.onChildClick(parent, v, groupPosition, childPosition, id);
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
