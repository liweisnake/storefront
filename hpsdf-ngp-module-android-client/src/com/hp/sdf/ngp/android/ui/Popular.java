package com.hp.sdf.ngp.android.ui;

import java.util.ArrayList;
import java.util.concurrent.Future;

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
import com.hp.sdf.ngp.android.ui.common.PagableList;
import com.hp.sdf.ngp.android.util.RemoteService;
import com.hp.sdf.ngp.ws.mobileclient.model.MobileAsset;

public class Popular extends ExpandableListActivity {

	private final String recommendApp = "Recommended Assets";

	private final String topApp = "New Assets";

	private AssetExpandableListAdapter mAdapter;

	private PagableList<PagableList<MobileAsset>> appGroupList;

	private Handler handler = new Handler();

	private ProgressDialog progressDialog = null;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		PagableList<MobileAsset> nameList1 = new PagableList<MobileAsset>();
		nameList1.setName(recommendApp);

		PagableList<MobileAsset> nameList2 = new PagableList<MobileAsset>();
		nameList2.setName(topApp);

		appGroupList = new PagableList<PagableList<MobileAsset>>();
		appGroupList.add(nameList1);
		appGroupList.add(nameList2);

		// Set up our adapter
		mAdapter = new AssetExpandableListAdapter(appGroupList, this,
				AssetExpandableListAdapter.NOCOMMAND);
		setListAdapter(mAdapter);

		OnGroupExpandListener onGroupExpandListener = new OnGroupExpandListener() {
			public void onGroupExpand(int groupPosition) {

				final PagableList<MobileAsset> nameList = appGroupList
						.get(groupPosition);
				if (recommendApp.equals(nameList.getName())) {
					progressDialog = ProgressDialog.show(Popular.this,
							"Please wait....", "Getting assets....", true);

					// start new thread to get assets.
					new Thread(new Runnable() {
						public void run() {
							try {
								Future<ArrayList<MobileAsset>> assets = RemoteService
										.getRecommendAsset();

								nameList.addAll(assets.get());
								Log.d("new thread", "add assets to nameList.");

								handler.post(new Runnable() {
									public void run() {
										Log
												.d("handler.post()",
														"mAdapter.notifyDataSetChanged().");
										mAdapter.notifyDataSetChanged();
									}
								});

								progressDialog.dismiss();

							} catch (Exception e) {
								progressDialog.dismiss();
								e.printStackTrace();
								Log.e("error", "when click recommendApp.");
							}
						}
					}).start();

				} else if (topApp.equals(nameList.getName())) {
					progressDialog = ProgressDialog.show(Popular.this,
							"Please wait....", "Getting assets....", true);

					// start new thread to get assets.
					new Thread(new Runnable() {
						public void run() {
							try {
								Future<ArrayList<MobileAsset>> assets = RemoteService
										.getTopAsset();
								nameList.addAll(assets.get());
								Log.d("new thread", "add assets to nameList.");

								handler.post(new Runnable() {
									public void run() {
										Log
												.d("handler.post()",
														"mAdapter.notifyDataSetChanged().");
										mAdapter.notifyDataSetChanged();
									}
								});

								progressDialog.dismiss();
							} catch (Exception e) {
								progressDialog.dismiss();
								e.printStackTrace();
								Log.e("error", "when click recommendApp.");
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
			}
		};

		this.getExpandableListView().setOnGroupCollapseListener(
				onGroupCollapseListener);
	}

	public boolean onChildClick(ExpandableListView parent, View v,
			int groupPosition, int childPosition, long id) {

		Log.d("Popular.onChildClick", "click asset at groupPosition :"
				+ groupPosition + ", childPosition :" + childPosition);

		MobileAsset mobileAsset = appGroupList.get(groupPosition).get(
				childPosition);

		Log.d("Popular.onChildClick", mobileAsset != null ? mobileAsset
				.getName() : "null");

		Intent intent = new Intent(this, AssetDetail.class);
		AppParcelable parcelable = new AppParcelable(mobileAsset);
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
