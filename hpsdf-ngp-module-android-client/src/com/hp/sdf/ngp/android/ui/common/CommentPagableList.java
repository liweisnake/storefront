package com.hp.sdf.ngp.android.ui.common;

import java.util.ArrayList;
import java.util.Collection;

import android.util.Log;

import com.hp.sdf.ngp.android.model.Page;
import com.hp.sdf.ngp.android.util.RemoteService;
import com.hp.sdf.ngp.ws.mobileclient.model.MobileComment;

public class CommentPagableList extends PagableList<MobileComment> implements
		PagableDataAccess<MobileComment> {

	private static final long serialVersionUID = 3032096442957546918L;

	private Long assetId;

	public CommentPagableList(Long assetId,
			Collection<MobileComment> collection, String name) {
		super(collection);
		this.assetId = assetId;
		this.name = name;
	}

	public void flush() {
		Log.d("CommentPagableList.flush", "start flush.");

		long totalCount = page.getCount();
		if (totalCount < 0) {
			Integer count = getDataCount().intValue();
			if (count != null) {
				page.setCount(count);
			} else {
				page.setCount(0);
			}
		}

		clear();

		ArrayList<MobileComment> mobileComments = getDatas();
		addAll(mobileComments);

		if (page.getCount() <= page.getItemsPerPage()) {
			Log.d("Get Comment", "Only have one page comments.");
		} else {
			MobileComment pagingComment = new MobileComment();

			if (page.getCurrentPage() == 1) {
				// First page, only have next button
				pagingComment.setTitle(Page.FIRST_PAGE);
			} else if (page.getCurrentPage() == page.getPageCount()) {
				// Last page, only have previous button
				pagingComment.setTitle(Page.LAST_PAGE);
			}

			add(pagingComment);
		}

	}

	public Long getDataCount() {

		Long i = 0L;
		try {
			i = RemoteService.getAssetCommentCount(assetId + "").get();
		} catch (Exception e) {
			Log.e("CommentPagableList.getDataCount", e.toString());
			e.printStackTrace();
		}

		Log.d("CommentPagableList.getDataCount", "result :" + i);
		return i;
	}

	public ArrayList<MobileComment> getDatas() {

		Log.d("CommentPagableList.getDatas", "page.getStartRow() :"
				+ page.getStartRow());

		ArrayList<MobileComment> mobileComments = new ArrayList<MobileComment>();
		try {
			mobileComments.addAll(RemoteService.getAssetComment(assetId + "",
					page.getStartRow(), page.getItemsPerPage()).get());
		} catch (Exception e) {
			Log.e("CommentPagableList.getDatas",
					"Exception when RemoteService.getAssetComment : "
							+ e.toString());
			e.printStackTrace();
		}

		Log.d("CommentPagableList.getDatas", "getDatas().size :"
				+ mobileComments.size());
		return mobileComments;
	}
}
