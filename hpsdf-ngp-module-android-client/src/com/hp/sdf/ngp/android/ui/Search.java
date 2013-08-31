package com.hp.sdf.ngp.android.ui;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.concurrent.Future;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.hp.sdf.ngp.android.R;
import com.hp.sdf.ngp.android.model.AppParcelable;
import com.hp.sdf.ngp.android.model.Page;
import com.hp.sdf.ngp.android.util.AndroidUtils;
import com.hp.sdf.ngp.android.util.RemoteService;
import com.hp.sdf.ngp.ws.mobileclient.model.MobileAsset;

public class Search extends Activity {

	private ProgressDialog progressDialog = null;

	private Handler handler = new Handler();

	private Page page = new Page();

	private String keyword;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search);

		final AutoCompleteTextView searchTxt = (AutoCompleteTextView) findViewById(R.id.searchText);

		final ListView searchResultList = (ListView) findViewById(R.id.searchResultList);
		searchResultList.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> adapt, View view,
					int position, long arg3) {
				Intent intent = new Intent(Search.this, AssetDetail.class);
				AppParcelable parcelable = new AppParcelable(
						(MobileAsset) adapt.getAdapter().getItem(position));
				intent.putExtra("app", parcelable);
				startActivity(intent);
			}
		});

		ImageButton searchBtn = (ImageButton) findViewById(R.id.searchBtn);
		searchBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View view) {
				progressDialog = ProgressDialog.show(Search.this,
						"Please wait....", "Searching assets....", true);

				new Thread(new Runnable() {
					public void run() {
						page.clear();

						keyword = searchTxt.getText().toString();
						Log.d("keyword", "keyword :" + keyword);

						if (".".equals(keyword)) {
							page.setCount(0);

							final SearchResultAdapter adapter = new SearchResultAdapter(
									Search.this, new ArrayList<MobileAsset>());

							handler.post(new Runnable() {
								public void run() {
									Log
											.d("handler.post",
													"set adapter and display paging details.");
									searchResultList.setAdapter(adapter);
									displayPagingDetail();
								}
							});

						} else if (keyword != null && !"".equals(keyword)) {
							setSearchCount();

							Future<ArrayList<MobileAsset>> list = RemoteService
									.search(keyword, page.getStartRow(), page
											.getItemsPerPage());

							try {
								final SearchResultAdapter adapter = new SearchResultAdapter(
										Search.this, list.get());

								handler.post(new Runnable() {
									public void run() {
										Log
												.d("handler.post",
														"set adapter and display paging details.");
										searchResultList.setAdapter(adapter);
										displayPagingDetail();
									}
								});
							} catch (Exception e) {
								e.printStackTrace();
							}
						}

						progressDialog.dismiss();
					}
				}).start();

			}
		});

		final Button previousSearchBtn = (Button) findViewById(R.id.previousSearchBtn);
		previousSearchBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View view) {
				if (searchResultList.getAdapter() == null)
					return;

				if (keyword != null && !"".equals(keyword)) {
					page.previous();

					Future<ArrayList<MobileAsset>> list = RemoteService
							.search(keyword, page.getStartRow(), page
									.getItemsPerPage());

					try {
						SearchResultAdapter adapter = new SearchResultAdapter(
								Search.this, list.get());
						searchResultList.setAdapter(adapter);
					} catch (Exception e) {
						e.printStackTrace();
					}

					displayPagingDetail();
				}
			}

		});

		final Button nextSearchBtn = (Button) findViewById(R.id.nextSearchBtn);
		nextSearchBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View view) {
				if (searchResultList.getAdapter() == null)
					return;

				if (keyword != null && !"".equals(keyword)) {
					page.next();

					Future<ArrayList<MobileAsset>> list = RemoteService
							.search(keyword, page.getStartRow(), page
									.getItemsPerPage());
					try {
						SearchResultAdapter adapter = new SearchResultAdapter(
								Search.this, list.get());
						searchResultList.setAdapter(adapter);
					} catch (Exception e) {
						e.printStackTrace();
					}

					displayPagingDetail();
				}
			}
		});
	}

	private void displayPagingDetail() {

		Log.d("displayPagingDetail", "Enter displayPagingDetail.");
		Log.d("Page.count", "Page.count :" + page.getCount());

		TextView pagingDetail = (TextView) Search.this
				.findViewById(R.id.pagingDetail);

		String template = Search.this.getText(R.string.pagingDetail).toString();
		if (page.getCount() > 0) {
			pagingDetail.setText(MessageFormat
					.format(template, page.getCount(), page.getCurrentPage(),
							page.getPageCount()));
		} else if (page.getCount() == 0) {
			pagingDetail.setText(MessageFormat.format(template,
					page.getCount(), 0, 0));
		}
	}

	private void setSearchCount() {
		Log.d("setSearchCount",
				"set pageCount and count for current search result.");

		long totalCount = page.getCount();
		if (totalCount < 0) {
			Future<Long> searchCountFuture = RemoteService.searchCount(keyword);
			try {
				Integer count = searchCountFuture.get().intValue();
				Log.d("total search count", "total search count :" + count);
				if (count != null) {
					page.setCount(count);
				} else {
					page.setCount(0);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private class SearchResultAdapter extends BaseAdapter {

		private ArrayList<MobileAsset> assetList = new ArrayList<MobileAsset>();

		private LayoutInflater mInflater;

		public SearchResultAdapter(Context context,
				ArrayList<MobileAsset> assetList) {
			// Cache the LayoutInflate to avoid asking for a new one each time.
			mInflater = LayoutInflater.from(context);
			this.assetList = assetList;
		}

		/**
		 * The number of items in the list is determined by the number of
		 * speeches in our array.
		 * 
		 * @see android.widget.ListAdapter#getCount()
		 */
		public int getCount() {
			return assetList.size();
		}

		/**
		 * Since the data comes from an array, just returning the index is
		 * sufficent to get at the data. If we were using a more complex data
		 * structure, we would return whatever object represents one row in the
		 * list.
		 * 
		 * @see android.widget.ListAdapter#getItem(int)
		 */
		public Object getItem(int position) {
			return assetList.get(position);
		}

		/**
		 * Use the array index as a unique id.
		 * 
		 * @see android.widget.ListAdapter#getItemId(int)
		 */
		public long getItemId(int position) {
			return position;
		}

		/**
		 * Make a view to hold each row.
		 * 
		 * @see android.widget.ListAdapter#getView(int, android.view.View,
		 *      android.view.ViewGroup)
		 */
		public View getView(int position, View convertView, ViewGroup parent) {
			// A ViewHolder keeps references to children views to avoid
			// unneccessary calls
			// to findViewById() on each row.
			ViewHolder holder;

			// When convertView is not null, we can reuse it directly, there is
			// no need
			// to reinflate it. We only inflate a new View when the convertView
			// supplied
			// by ListView is null.
			if (convertView == null) {
				convertView = mInflater.inflate(R.layout.search_result_list,
						null);

				// Creates a ViewHolder and store references to the two children
				// views
				// we want to bind data to.
				holder = new ViewHolder();
				holder.img = (ImageView) convertView
						.findViewById(R.id.searchResultAssetBriefImg);
				holder.name = (TextView) convertView
						.findViewById(R.id.searchResultAssetBriefName);
				holder.comName = (TextView) convertView
						.findViewById(R.id.searchResultAssetBriefComName);
				holder.ratingbar = (RatingBar) convertView
						.findViewById(R.id.searchResultAssetBriefRatingbar);
				holder.price = (TextView) convertView
						.findViewById(R.id.searchResultAssetBriefPrice);

				convertView.setTag(holder);
			} else {
				// Get the ViewHolder back to get fast access to the TextView
				// and the ImageView.
				holder = (ViewHolder) convertView.getTag();
			}
			MobileAsset asset = assetList.get(position);
			// Bind the data efficiently with the holder.

			String imgUrl = AndroidUtils.getImageUrlBySuffix(asset
					.getThumbnailUrl());
			Future<Drawable> f = RemoteService.getImage(imgUrl);
			try {
				holder.img.setImageDrawable(f.get());
			} catch (Exception e) {
				e.printStackTrace();
			}
			holder.name.setText(asset.getName());
			holder.comName.setText(asset.getAuthor());
			holder.ratingbar.setRating(asset.getRating() == null ? 0 : asset
					.getRating().floatValue());
			holder.price.setText(asset.getPrice());

			return convertView;
		}

		class ViewHolder {
			ImageView img;
			TextView name;
			TextView comName;
			RatingBar ratingbar;
			TextView price;
		}
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Log.d("Click back", "Click back.");

			new AlertDialog.Builder(this).setIcon(R.drawable.alert_dialog_icon)
					.setTitle(R.string.exit).setMessage(R.string.exitWarning)
					.setNegativeButton(R.string.cancel,
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
