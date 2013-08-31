package com.hp.sdf.ngp.android.ui.common;

import java.util.ArrayList;

import android.util.Log;

import com.hp.sdf.ngp.android.model.Page;
import com.hp.sdf.ngp.android.util.RemoteService;
import com.hp.sdf.ngp.ws.mobileclient.model.MobileAsset;

public class CategoryPagableList extends PagableList<MobileAsset> implements
		PagableDataAccess<MobileAsset> {

	private static final long serialVersionUID = 8636444782000938014L;

	public CategoryPagableList(String categoryName) {
		super();
		this.name = categoryName;
	}

	public void flush() {
		Log.d("CategoryPagableList.flush", "start flush.");

		long totalCount = page.getCount();
		if (totalCount < 0) {
			Integer count = getDataCount().intValue();
			if (count != null) {
				page.setCount(count);
			} else {
				page.setCount(0);
			}
		}

		Log.d("CategoryPagableList.MobileAsset's size", "Clear the assets.");
		clear();

		Log.d("CategoryPagableList.MobileAsset's size",
				"before add the new assets, this.size :" + this.size());
		ArrayList<MobileAsset> mobileAssets = getDatas();
		addAll(mobileAssets);
		Log.d("CategoryPagableList.MobileAsset's size",
				"after add the new assets, this.size :" + this.size());

		if (page.getCount() <= page.getItemsPerPage()) {
			Log.d("Get Asset", "Only have one page assets.");
		} else {
			MobileAsset pagingAsset = new MobileAsset();
			if (page.getCurrentPage() == 1) {
				// First page, only have next button
				pagingAsset.setName(Page.FIRST_PAGE);
			} else if (page.getCurrentPage() == page.getPageCount()) {
				// Last page, only have previous button
				pagingAsset.setName(Page.LAST_PAGE);
			}

			add(pagingAsset);
		}

	}

	public Long getDataCount() {
		Integer i = 0;
		try {
			i = RemoteService.getCountByCategoryName(name).get();
		} catch (Exception e) {
			Log.e("CategoryPagableList.getDataCount", e.toString());
			e.printStackTrace();
		}

		Log.d("CategoryPagableList.getDataCount", "result :" + i);
		return new Long(i);
	}

	public ArrayList<MobileAsset> getDatas() {

		Log.d("CommentPagableList.getDatas", "page.getStartRow() :"
				+ page.getStartRow());

		ArrayList<MobileAsset> mobileAssets = new ArrayList<MobileAsset>();
		try {
			mobileAssets.addAll(RemoteService.getAssetByCategoryName(name,
					page.getStartRow(), page.getItemsPerPage()).get());
		} catch (Exception e) {
			Log.e("CategoryPagableList.getDatas",
					"Exception when RemoteService.getAssetByCategoryName : "
							+ e.toString());
			e.printStackTrace();
		}

		Log.d("CategoryPagableList.getDatas", "getDatas().size :"
				+ mobileAssets.size());
		return mobileAssets;
	}
}
