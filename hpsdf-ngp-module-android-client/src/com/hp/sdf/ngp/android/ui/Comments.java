package com.hp.sdf.ngp.android.ui;

import java.util.ArrayList;

import android.app.ExpandableListActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnGroupClickListener;

import com.hp.sdf.ngp.android.model.CommentsParcelable;
import com.hp.sdf.ngp.android.ui.common.CommentExpandableListAdapter;
import com.hp.sdf.ngp.android.ui.common.CommentPagableList;
import com.hp.sdf.ngp.android.ui.common.PagableList;
import com.hp.sdf.ngp.ws.mobileclient.model.MobileComment;

public class Comments extends ExpandableListActivity {

	private static PagableList<PagableList<MobileComment>> commentsPagableList = new PagableList<PagableList<MobileComment>>();

	private static CommentExpandableListAdapter mAdapter;

	public static CommentExpandableListAdapter getmAdapter() {
		return mAdapter;
	}

	public static void setmAdapter(CommentExpandableListAdapter mAdapter) {
		Comments.mAdapter = mAdapter;
	}

	public static PagableList<PagableList<MobileComment>> getCommentsPagableList() {
		return commentsPagableList;
	}

	public static void setCommentsPagableList(
			PagableList<PagableList<MobileComment>> commentsPagableList) {
		Comments.commentsPagableList = commentsPagableList;
	}

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Log.d("Comments.onCreate", "enter Comments.");

		CommentsParcelable commentsParcelable = getIntent().getParcelableExtra(
				"commentsParcelable");
		Long assetId = commentsParcelable.getAssetId();
		Log.d("Comments.assetId", assetId + "");

		CommentPagableList detailComments = new CommentPagableList(assetId,
				new ArrayList<MobileComment>(), "Detail Comments");
		commentsPagableList.clear();
		commentsPagableList.add(detailComments);

		mAdapter = new CommentExpandableListAdapter(commentsPagableList, this);
		setListAdapter(mAdapter);

		this.getExpandableListView().setOnGroupClickListener(
				new OnGroupClickListener() {
					public boolean onGroupClick(ExpandableListView parent,
							View v, int groupPosition, long id) {
						Log
								.d("Comments.onGroupClick",
										"Click detail comments.");

						PagableList<MobileComment> list = commentsPagableList
								.get(groupPosition);
						CommentPagableList detailComments = (CommentPagableList) list;

						Log.d("Comments.onGroupClick", "flush detailComments.");
						detailComments.flush();

						return false;
					}
				});
	}

	public boolean onChildClick(ExpandableListView parent, View v,
			int groupPosition, int childPosition, long id) {
		Log.d("Comments.onChildClick", "enter Comments.");

		return super.onChildClick(parent, v, groupPosition, childPosition, id);
	}

}
