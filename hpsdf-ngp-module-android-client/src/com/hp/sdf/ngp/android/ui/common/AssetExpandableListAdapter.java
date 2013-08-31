package com.hp.sdf.ngp.android.ui.common;

import java.text.MessageFormat;
import java.util.concurrent.Future;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.hp.sdf.ngp.android.R;
import com.hp.sdf.ngp.android.model.Page;
import com.hp.sdf.ngp.android.ui.Category;
import com.hp.sdf.ngp.android.ui.MyPortal;
import com.hp.sdf.ngp.android.util.AndroidUtils;
import com.hp.sdf.ngp.android.util.RemoteService;
import com.hp.sdf.ngp.ws.mobileclient.model.MobileAsset;

public class AssetExpandableListAdapter extends BaseExpandableListAdapter {

	public static final int NOCOMMAND = 0;

	public static final int PAGING = 5;

	private int cmd;

	private PagableList<? extends PagableList<MobileAsset>> objs = new PagableList<PagableList<MobileAsset>>();

	private Context context;

	private LayoutInflater mInflater;

	private Handler handler = new Handler();

	public AssetExpandableListAdapter(
			PagableList<? extends PagableList<MobileAsset>> objs,
			Context parent, int command) {
		this.objs = objs;
		this.context = parent;
		this.cmd = command;
		mInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public Object getChild(int groupPosition, int childPosition) {
		return objs.get(groupPosition).get(childPosition);
	}

	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	public View getChildView(final int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {

		Log.d("AssetExpandableListAdapter.getChildView", "enter getChildView.");

		final MobileAsset asset = (MobileAsset) getChild(groupPosition,
				childPosition);
		Log.d("AssetExpandableListAdapter.getChildView", "asset.getName():"
				+ asset.getName());

		if (isLastChild && asset.getId() == null && cmd == PAGING) {
			Log.d("AssetExpandableListAdapter.getChildView",
					"Will get paging view.");

			int previousVisible = Button.VISIBLE;
			int nextVisible = Button.VISIBLE;
			String name = asset.getName();
			if (Page.FIRST_PAGE.equals(name)) {
				previousVisible = Button.INVISIBLE;
			} else if (Page.LAST_PAGE.equals(name)) {
				nextVisible = Button.INVISIBLE;
			}

			return getPagingView(groupPosition, parent, previousVisible,
					nextVisible);
		} else {
			View asetBriefView = getAssetBriefView(parent, asset);
			return asetBriefView;
		}
	}

	private View getAssetBriefView(ViewGroup parent, final MobileAsset asset) {
		View asetBriefView = mInflater.inflate(R.layout.asset_brief, parent,
				false);

		String imgUrl = AndroidUtils.getImageUrlBySuffix(asset
				.getThumbnailUrl());
		Log.d("image path:", imgUrl);
		Future<Drawable> f = RemoteService.getImage(imgUrl);

		ImageView iv = (ImageView) asetBriefView
				.findViewById(R.id.assetBriefImg);
		try {
			iv.setImageDrawable(f.get());
		} catch (Exception e) {
			Log.e("Image Exception", e.toString());
		}

		TextView name = (TextView) asetBriefView
				.findViewById(R.id.assetBriefName);
		name.setText(asset.getName());

		TextView comName = (TextView) asetBriefView
				.findViewById(R.id.assetBriefComName);
		comName.setText(asset.getAuthor());

		RatingBar ratingBar = (RatingBar) asetBriefView
				.findViewById(R.id.assetBriefRatingbar);
		ratingBar.setRating((float) (asset.getRating() == null ? 0 : asset
				.getRating()));

		TextView price = (TextView) asetBriefView
				.findViewById(R.id.assetBriefPrice);
		price.setText(asset.getPrice());

		return asetBriefView;
	}

	private View getPagingView(final int groupPosition, ViewGroup parent,
			int previousVisible, int nextVisible) {

		final View pagingView = mInflater.inflate(R.layout.paging_button,
				parent, false);

		Button previous = (Button) pagingView.findViewById(R.id.previousBtn);
		previous.setVisibility(previousVisible);
		previous.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if (context instanceof Category) {
					final CategoryPagableList ctgList = Category
							.getAppGroupList().get(groupPosition);

					ctgList.previous();

					Log.d("AssetExpandableListAdapter.getPagingView",
							"flush ctgList.");
					ctgList.flush();

					handler.post(new Runnable() {
						public void run() {
							Log.d("AssetExpandableListAdapter.getPagingView",
									"mAdapter.notifyDataSetChanged()");
							Category.getAdapter().notifyDataSetChanged();
						}
					});
				} else if (context instanceof MyPortal) {
					final PurchasePagableList purchaseList = MyPortal
							.getAppGroupList().get(groupPosition);

					purchaseList.previous();

					Log.d("AssetExpandableListAdapter.getPagingView",
							"flush purchaseList.");
					purchaseList.flush();

					handler.post(new Runnable() {
						public void run() {
							Log.d("AssetExpandableListAdapter.getPagingView",
									"mAdapter.notifyDataSetChanged()");
							MyPortal.getAdapter().notifyDataSetChanged();
						}
					});
				}
			}
		});

		Button next = (Button) pagingView.findViewById(R.id.nextBtn);
		next.setVisibility(nextVisible);
		next.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if (context instanceof Category) {
					final CategoryPagableList ctgList = Category
							.getAppGroupList().get(groupPosition);

					ctgList.next();

					Log.d("AssetExpandableListAdapter.getPagingView",
							"flush ctgList.");
					ctgList.flush();

					handler.post(new Runnable() {
						public void run() {
							Log.d("AssetExpandableListAdapter.getPagingView",
									"mAdapter.notifyDataSetChanged()");
							Category.getAdapter().notifyDataSetChanged();
						}
					});
				} else if (context instanceof MyPortal) {
					final PurchasePagableList purchaseList = MyPortal
							.getAppGroupList().get(groupPosition);

					purchaseList.next();

					Log.d("AssetExpandableListAdapter.getPagingView",
							"flush purchaseList.");
					purchaseList.flush();

					handler.post(new Runnable() {
						public void run() {
							Log.d("AssetExpandableListAdapter.getPagingView",
									"mAdapter.notifyDataSetChanged()");
							MyPortal.getAdapter().notifyDataSetChanged();
						}
					});
				}
			}

		});

		PagableList<MobileAsset> pagableList = null;
		if (context instanceof Category) {
			pagableList = Category.getAppGroupList().get(groupPosition);
		} else if (context instanceof MyPortal) {
			pagableList = MyPortal.getAppGroupList().get(groupPosition);
		}

		Page page = pagableList.getPage();
		if (page != null && page.getCount() > 0) {
			TextView pagingInfo = (TextView) pagingView
					.findViewById(R.id.pagingInfo);

			String template = context.getText(R.string.pagingDetail).toString();
			String pagingContent = MessageFormat.format(template, page
					.getCount(), page.getCurrentPage(), page.getPageCount());
			Log.d("AssetExpandableListAdapter.displayPagingDetail",
					"pagingContent :" + pagingContent);
			pagingInfo.setText(pagingContent);
		}

		return pagingView;
	}

	public int getChildrenCount(int groupPosition) {
		return objs.get(groupPosition).size();
	}

	public Object getGroup(int groupPosition) {
		return objs.get(groupPosition).getName();
	}

	public int getGroupCount() {
		return objs.size();
	}

	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		AbsListView.LayoutParams lp = new AbsListView.LayoutParams(
				ViewGroup.LayoutParams.FILL_PARENT, 64);

		TextView textView = new TextView(context);
		textView.setLayoutParams(lp);
		// Center the text vertically
		textView.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
		// Set the text starting position
		textView.setPadding(36, 0, 0, 0);
		textView.setTextSize(28);
		textView.setText(getGroup(groupPosition).toString());
		return textView;
	}

	public boolean hasStableIds() {
		return true;
	}

	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}

}
