package com.hp.sdf.ngp.android.ui.common;

import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.concurrent.Future;

import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import com.hp.sdf.ngp.android.R;
import com.hp.sdf.ngp.android.model.Page;
import com.hp.sdf.ngp.android.ui.Comments;
import com.hp.sdf.ngp.android.util.RemoteService;
import com.hp.sdf.ngp.ws.mobileclient.model.MobileComment;

public class CommentExpandableListAdapter extends BaseExpandableListAdapter {

	private PagableList<PagableList<MobileComment>> comments = new PagableList<PagableList<MobileComment>>();

	private Context context;

	private LayoutInflater mInflater;

	public CommentExpandableListAdapter(
			PagableList<PagableList<MobileComment>> comments, Context parent) {
		this.comments = comments;
		this.context = parent;
		mInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public Object getChild(int groupPosition, int childPosition) {
		return comments.get(groupPosition).get(childPosition);
	}

	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	public View getChildView(final int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {

		Log.d("CommentExpandableListAdapter.getChildView",
				"enter getChildView.");

		final MobileComment mobileComment = (MobileComment) getChild(
				groupPosition, childPosition);
		Log.d("CommentExpandableListAdapter.getChildView",
				"mobileComment.getTitle():" + mobileComment.getTitle()
						+ ", mobileComment.getUserId():"
						+ mobileComment.getUserId());

		if (isLastChild && mobileComment.getUserId() == null) {
			Log.d("CommentExpandableListAdapter.getChildView",
					"Will get paging view.");

			int previousVisible = Button.VISIBLE;
			int nextVisible = Button.VISIBLE;
			String title = mobileComment.getTitle();
			if (Page.FIRST_PAGE.equals(title)) {
				previousVisible = Button.INVISIBLE;
			} else if (Page.LAST_PAGE.equals(title)) {
				nextVisible = Button.INVISIBLE;
			}

			return getPagingView(groupPosition, parent, previousVisible,
					nextVisible);
		} else {
			Log.d("CommentExpandableListAdapter.getChildView",
					"Will get comment content View.");
			return getCommentContentView(parent, mobileComment);
		}
	}

	private View getCommentContentView(ViewGroup parent,
			final MobileComment mobileComment) {

		View detailCommentView = mInflater.inflate(R.layout.detail_comment,
				parent, false);

		TextView detailCommentUser = (TextView) detailCommentView
				.findViewById(R.id.detailCommentUser);
		detailCommentUser.setText(mobileComment.getUserId());

		TextView detailCommentDate = (TextView) detailCommentView
				.findViewById(R.id.detailCommentDate);
		detailCommentDate.setText(new SimpleDateFormat("yyyy-MM-dd")
				.format(mobileComment.getDate()));

		RatingBar detailCommentRate = (RatingBar) detailCommentView
				.findViewById(R.id.detailCommentRate);

		Future<Double> ratingFuture = RemoteService.getAssetRating(
				mobileComment.getAssetId(), mobileComment.getUserId());

		Double rating = 0.0;
		try {
			rating = ratingFuture.get();
		} catch (Exception e) {
			e.printStackTrace();
			Log.e("getAssetRating error", "assetRating error " + e);
		}
		detailCommentRate.setRating(rating.floatValue());

		TextView detailCommentContent = (TextView) detailCommentView
				.findViewById(R.id.detailCommentContent);
		detailCommentContent.setText(mobileComment.getComment());

		return detailCommentView;
	}

	private View getPagingView(final int groupPosition, ViewGroup parent,
			int previousVisible, int nextVisible) {

		View pagingView = mInflater.inflate(R.layout.paging_button, parent,
				false);

		Button previous = (Button) pagingView.findViewById(R.id.previousBtn);
		previous.setVisibility(previousVisible);
		previous.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Log.d("CommentExpandableListAdapter.getPagingView",
						"Click the previous button.");

				if (context instanceof Comments) {
					CommentPagableList detailComments = (CommentPagableList) Comments
							.getCommentsPagableList().get(groupPosition);

					Log.d("CommentExpandableListAdapter.getPagingView",
							"call detailComments.previous().");
					detailComments.previous();

					Log.d("CommentExpandableListAdapter.getPagingView",
							"flush detailComments.");
					detailComments.flush();

					Log.d("CommentExpandableListAdapter.getPagingView",
							"mAdapter.notifyDataSetChanged()");
					Comments.getmAdapter().notifyDataSetChanged();
				}
			}
		});

		Button next = (Button) pagingView.findViewById(R.id.nextBtn);
		next.setVisibility(nextVisible);
		next.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Log.d("CommentExpandableListAdapter.getPagingView",
						"Click the next button.");

				if (context instanceof Comments) {
					CommentPagableList detailComments = (CommentPagableList) Comments
							.getCommentsPagableList().get(groupPosition);

					Log.d("CommentExpandableListAdapter.getPagingView",
							"call detailComments.next().");
					detailComments.next();

					Log.d("CommentExpandableListAdapter.getPagingView",
							"flush detailComments.");
					detailComments.flush();

					Log.d("CommentExpandableListAdapter.getPagingView",
							"mAdapter.notifyDataSetChanged() ");
					Comments.getmAdapter().notifyDataSetChanged();
				}
			}
		});

		CommentPagableList detailComments = (CommentPagableList) Comments
				.getCommentsPagableList().get(groupPosition);
		Page page = detailComments.getPage();
		if (page != null && page.getCount() > 0) {
			TextView pagingInfo = (TextView) pagingView
					.findViewById(R.id.pagingInfo);

			String template = context.getText(R.string.pagingDetail).toString();
			String pagingContent = MessageFormat.format(template, page
					.getCount(), page.getCurrentPage(), page.getPageCount());
			Log.d("CommentExpandableListAdapter.displayPagingDetail",
					"pagingContent :" + pagingContent);
			pagingInfo.setText(pagingContent);
		}

		return pagingView;
	}

	public int getChildrenCount(int groupPosition) {
		return comments.get(groupPosition).size();
	}

	public boolean hasStableIds() {
		return true;
	}

	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}

	public Object getGroup(int groupPosition) {
		return comments.get(groupPosition).getName();
	}

	public int getGroupCount() {
		return comments.size();
	}

	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	public View getGroupView(int groupPosition, boolean paramBoolean,
			View paramView, ViewGroup paramViewGroup) {

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

}
