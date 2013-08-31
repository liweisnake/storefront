package com.hp.sdf.ngp.android.ui;

import java.io.File;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.Future;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.hp.sdf.ngp.android.R;
import com.hp.sdf.ngp.android.model.AppParcelable;
import com.hp.sdf.ngp.android.model.CommentsParcelable;
import com.hp.sdf.ngp.android.ui.common.DownloadProgressDialog;
import com.hp.sdf.ngp.android.util.AndroidUtils;
import com.hp.sdf.ngp.android.util.RemoteService;
import com.hp.sdf.ngp.android.util.SystemManager;
import com.hp.sdf.ngp.ws.mobileclient.model.MobileAsset;
import com.hp.sdf.ngp.ws.mobileclient.model.MobileComment;

public class AssetDetail extends Activity {

	private MobileAsset asset;

	private static final int BRIEF_COMENTS_COUNT = 3;

	private static final int PURCHASE_DIALOG_ID = 0;

	private static final int DOWNLOAD_DIALOG_ID = 1;

	private static final int DOWNLOAD_FAIL_DIALOG_ID = 2;

	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.asset_detail);

		AppParcelable p = getIntent().getParcelableExtra("app");
		asset = p.getInfo();

		try {

			if (asset.getThumbnailUrl() != null
					&& !"".equals(asset.getThumbnailUrl())) {
				ImageView thumbnail = (ImageView) findViewById(R.id.detailThumbnail);
				Future<Drawable> f = RemoteService.getImage(AndroidUtils
						.getImageUrlBySuffix(asset.getThumbnailUrl()));
				thumbnail.setImageDrawable(f.get());
			}

			if (asset.getPreview1Url() != null
					&& !"".equals(asset.getPreview1Url())) {
				ImageView preview1 = (ImageView) findViewById(R.id.detailPreview1);
				Future<Drawable> p1 = RemoteService.getImage(AndroidUtils
						.getImageUrlBySuffix(asset.getPreview1Url()));
				preview1.setImageDrawable(p1.get());
			}

			if (asset.getPreview2Url() != null
					&& !"".equals(asset.getPreview2Url())) {
				ImageView preview2 = (ImageView) findViewById(R.id.detailPreview2);
				Future<Drawable> p2 = RemoteService.getImage(AndroidUtils
						.getImageUrlBySuffix(asset.getPreview2Url()));
				preview2.setImageDrawable(p2.get());
			}

		} catch (Exception e) {
			e.printStackTrace();
			Log.e("image get error", e.getMessage());
		}

		TextView detailName = (TextView) findViewById(R.id.detailName);
		detailName.setText(asset.getName());

		TextView detailComName = (TextView) findViewById(R.id.detailCom);
		detailComName.setText(asset.getAuthor());

		TextView detailPrice = (TextView) findViewById(R.id.detailPrice);
		detailPrice.setText(asset.getPrice());

		RatingBar titleRating = (RatingBar) findViewById(R.id.assetTitleRatingbar);
		Log.d("asset.getRating", asset.getRating() + "");
		titleRating.setMax(5);
		titleRating.setRating((float) (asset.getRating() == null ? 0 : asset
				.getRating().doubleValue()));

		Button btn = (Button) findViewById(R.id.purchaseBtn);

		final int whichKindOfBtn = getIntent().getIntExtra("whichKindOfBtn",
				MyPortal.PURCHASE);
		Log.d("AssetDetail.onCreat", "whichKindOfBtn :" + whichKindOfBtn);

		switch (whichKindOfBtn) {
		case MyPortal.PURCHASE:
			btn.setText("Purchase");
			break;

		case MyPortal.DOWNLOAD:
			btn.setText("Download");
			break;

		case MyPortal.INSTALL:
			btn.setText("Install");
			break;

		case MyPortal.UNINSTALL:
			btn.setText("Uninstall");
			break;
		}

		btn.setOnClickListener(new OnClickListener() {
			public void onClick(View view) {

				switch (whichKindOfBtn) {
				case MyPortal.PURCHASE:
					Log.d("AssetDetail.Purchase", "start purchase.");
					showDialog(PURCHASE_DIALOG_ID);
					break;

				case MyPortal.DOWNLOAD:
					Log.d("AssetDetail.Download", "start download.");
					showDialog(DOWNLOAD_DIALOG_ID);
					break;

				case MyPortal.INSTALL:
					Log.d("AssetDetail.Install", "start Install.");
					String downloadUrl = getIntent().getStringExtra(
							"downloadUrl");
					Log.d("AssetDetail install", "downloadUrl :" + downloadUrl);

					if (null != downloadUrl || !"".equals(downloadUrl)) {
						String binaryFileName = downloadUrl
								.substring(downloadUrl
										.lastIndexOf(File.separator));
						Log.d("AssetDetail install", "binaryFileName :"
								+ binaryFileName);

						if (binaryFileName.endsWith(".apk")
								|| binaryFileName.endsWith(".zip")) {
							AndroidUtils.install(AssetDetail.this, Environment
									.getExternalStorageDirectory()
									+ binaryFileName);
						}
					}
					break;

				case MyPortal.UNINSTALL:
					Log.d("AssetDetail.Uninstall", "start Uninstall.");

					String packageNameOfActiveFile = getIntent()
							.getStringExtra("packageNameOfActiveFile");
					Log.d("AssetDetail uninstall", "packageNameOfActiveFile :"
							+ packageNameOfActiveFile);

					AndroidUtils.uninstall(AssetDetail.this,
							packageNameOfActiveFile);
					break;
				}

			}
		});

		TextView detailVer = (TextView) findViewById(R.id.detailVer);
		detailVer.setText(detailVer.getText()
				+ (asset.getVersion() == null ? "" : asset.getVersion()));

		TextView detailLastUpdate = (TextView) findViewById(R.id.detailLastUpdate);

		if (asset.getLastUpdateTime() != null) {
			detailLastUpdate.setText(detailLastUpdate.getText()
					+ sdf.format(asset.getLastUpdateTime()));
		}

		TextView detailDescContent = (TextView) findViewById(R.id.detailDescContent);
		detailDescContent.setText(asset.getDescription());

		// comment and rate
		final ArrayList<MobileComment> mobileComments = new ArrayList<MobileComment>();

		Future<ArrayList<MobileComment>> future = RemoteService
				.getAssetComment(asset.getId() + "", 0, BRIEF_COMENTS_COUNT);
		try {
			mobileComments.addAll(future.get());
		} catch (Exception e) {
			Log.e("AssetDetail.getAssetComment", e.toString());
			e.printStackTrace();
		}

		ArrayList<HashMap<String, Object>> listItem = new ArrayList<HashMap<String, Object>>();
		for (MobileComment mobileComment : mobileComments) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("commentUser", mobileComment.getUserId());
			map.put("briefCommentDate", sdf.format(mobileComment.getDate()));
			map.put("briefCommentContent", mobileComment.getComment());
			listItem.add(map);
		}

		SimpleAdapter listItemAdapter = new SimpleAdapter(this, listItem,
				R.layout.comment_brief, new String[] { "commentUser",
						"briefCommentDate", "briefCommentContent" }, new int[] {
						R.id.commentUser, R.id.briefCommentDate,
						R.id.briefCommentContent });

		ListView commentListView = (ListView) findViewById(R.id.commentList);
		commentListView.setAdapter(listItemAdapter);

		Future<Long> commentsCount = RemoteService.getAssetCommentCount(asset
				.getId()
				+ "");
		Long count = 0L;
		try {
			count = commentsCount.get();
		} catch (Exception e) {
			Log.e("AssetDetail.getAssetCommentsCount", e.toString());
			e.printStackTrace();
		}
		Log.d("AssetDetail.getAssetCommentsCount", "count : " + count);

		TextView commentSizeIntro = (TextView) findViewById(R.id.commentSizeIntro);
		commentSizeIntro.setText(MessageFormat.format(getText(
				R.string.commentsIntro).toString(), count));

		Button seeCommentDetailsBtn = (Button) findViewById(R.id.seeCommentDetailsBtn);
		if (count <= BRIEF_COMENTS_COUNT) {
			seeCommentDetailsBtn.setVisibility(Button.INVISIBLE);
		}

		seeCommentDetailsBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View view) {
				Intent intent = new Intent(getBaseContext(), Comments.class);
				intent.putExtra("commentsParcelable", new CommentsParcelable(
						asset.getId()));
				startActivity(intent);
			}
		});

		Button cmtRateBtn = (Button) findViewById(R.id.cmtRateBtn);
		cmtRateBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				CommentRatingDialog dialog = new CommentRatingDialog(
						AssetDetail.this, asset.getId(), SystemManager
								.getUser());
				dialog.show();
			}
		});

	}

	protected Dialog onCreateDialog(int id) {

		switch (id) {
		case PURCHASE_DIALOG_ID:
			AlertDialog purchaseDialog = getPurchaseDialog();
			return purchaseDialog;

		case DOWNLOAD_DIALOG_ID:
			AlertDialog downloadDialog = getDownloadDialog();
			return downloadDialog;

		case DOWNLOAD_FAIL_DIALOG_ID:
			AlertDialog downloadFailedDialog = getDownloadFailedDialog();
			return downloadFailedDialog;
		}

		return null;
	}

	private AlertDialog getPurchaseDialog() {
		LayoutInflater factory = LayoutInflater.from(this);
		final View textEntryView = factory.inflate(R.layout.main, null);

		String purchaseTitle = "";
		if ("Free".equalsIgnoreCase(asset.getPrice())) {
			purchaseTitle = "This asset is free, confirm to purchase?";
		} else {
			String template = this.getText(R.string.purcharseWarning)
					.toString();
			purchaseTitle = MessageFormat.format(template, asset.getPrice());
		}

		AlertDialog purchaseDialog = new AlertDialog.Builder(AssetDetail.this)
				.setIcon(R.drawable.purchase).setTitle(purchaseTitle).setView(
						textEntryView).setPositiveButton(R.string.ok,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {

								Future<Boolean> result = RemoteService
										.purchase(asset.getId() + "",
												SystemManager.getUser());
								Log
										.i(
												"AssetDetail.getPurchaseDialog",
												SystemManager.getUser()
														+ " will purchase asset [assetId = "
														+ asset.getId() + "].");

								try {
									if (result.get()) {
										Log.d("AssetDetail.getPurchaseDialog",
												"purchase successful.");

										showDialog(DOWNLOAD_DIALOG_ID);
									} else {
										Log.d("AssetDetail.getPurchaseDialog",
												"purchase failed.");
									}
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
						}).setNegativeButton(R.string.cancel,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
							}
						}).create();

		return purchaseDialog;
	}

	private AlertDialog getDownloadFailedDialog() {

		LayoutInflater factory = LayoutInflater.from(this);
		final View textEntryView = factory.inflate(R.layout.main, null);

		AlertDialog downloadFailedDialog = new AlertDialog.Builder(
				AssetDetail.this).setIcon(R.drawable.alert_dialog_icon)
				.setTitle(R.string.downloadFailedWarning)
				.setView(textEntryView).setPositiveButton(R.string.ok,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
							}
						}).create();

		return downloadFailedDialog;
	}

	private AlertDialog getDownloadDialog() {

		LayoutInflater factory = LayoutInflater.from(this);
		final View textEntryView = factory.inflate(R.layout.main, null);

		AlertDialog downloadDialog = new AlertDialog.Builder(AssetDetail.this)
				.setIcon(R.drawable.download)
				.setTitle(R.string.downloadWarning).setView(textEntryView)
				.setPositiveButton(R.string.ok,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {

								try {
									Future<String> downloadUrlTask = RemoteService
											.getDownloadUrl(asset.getId() + "",
													SystemManager.getUser(),
													SystemManager
															.getSerialNumber());

									String downloadUrl = downloadUrlTask.get();
									Log.d("AssetDetail.getDownloadDialog",
											"downloadUrl :" + downloadUrl);

									DownloadProgressDialog downloadDia = new DownloadProgressDialog(
											AssetDetail.this, downloadUrl);
									downloadDia.show();
								} catch (Exception e) {
									Log.e("error when purchare and download", e
											.getMessage());
									e.printStackTrace();

									showDialog(DOWNLOAD_FAIL_DIALOG_ID);
								}

							}
						}).setNegativeButton(R.string.cancel,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
							}
						}).create();

		return downloadDialog;
	}

	private class CommentRatingDialog extends Dialog implements
			RatingBar.OnRatingBarChangeListener {

		private static final int COMMENTS_MAXIMUM_LENGTH = 500;

		private RatingBar ratingBar;

		private EditText commentText;

		private Long assetId;

		private String userId;

		public CommentRatingDialog(Context context, Long assetId, String userId) {
			super(context);
			this.assetId = assetId;
			this.userId = userId;
		}

		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);

			setTitle("Comment and Rating");

			setContentView(R.layout.comment_rating_dialog);

			commentText = (EditText) findViewById(R.id.commentText);
			commentText.addTextChangedListener(new TextWatcher() {

				private CharSequence temp;

				private boolean isEdit = true;

				public void afterTextChanged(Editable s) {
					if (temp.length() > COMMENTS_MAXIMUM_LENGTH) {
						isEdit = false;
						s.delete(temp.length() - 1, temp.length());
						commentText.setText(s);
					} else {
						isEdit = true;
					}
				}

				public void beforeTextChanged(CharSequence s, int arg1,
						int arg2, int arg3) {
					temp = s;
				}

				public void onTextChanged(CharSequence s, int arg1, int arg2,
						int arg3) {
					if (isEdit == false) {
						LayoutInflater factory = LayoutInflater
								.from(AssetDetail.this);
						final View textEntryView = factory.inflate(
								R.layout.main, null);

						AlertDialog commentsMaxLengthDialog = new AlertDialog.Builder(
								AssetDetail.this).setIcon(
								R.drawable.alert_dialog_icon).setTitle(
								R.string.commentsMaxLengthWarning).setView(
								textEntryView).setPositiveButton(R.string.ok,
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int whichButton) {
									}
								}).create();

						commentsMaxLengthDialog.show();
					}

				}

			});

			ratingBar = (RatingBar) findViewById(R.id.ratingBar);

			Button commentRatingSubmit = (Button) findViewById(R.id.commentRatingSubmit);
			commentRatingSubmit.setHeight(3);
			commentRatingSubmit
					.setOnClickListener(new Button.OnClickListener() {

						public void onClick(View v) {
							try {

								Log.d("comment dialog", "commentText text : "
										+ commentText.getText().toString());
								Boolean commentResult = RemoteService.comment(
										assetId, userId,
										commentText.getText().toString()).get();

								Log.d("rating dialog", "ratingBar Rating : "
										+ ratingBar.getRating());
								Boolean ratingResult = RemoteService.rating(
										assetId, userId, ratingBar.getRating())
										.get();

								if (commentResult && ratingResult) {
									LayoutInflater factory = LayoutInflater
											.from(CommentRatingDialog.this
													.getContext());
									final View textEntryView = factory.inflate(
											R.layout.main, null);

									AlertDialog confirmDialog = new AlertDialog.Builder(
											CommentRatingDialog.this
													.getContext())
											.setIcon(
													R.drawable.alert_dialog_icon)
											.setTitle(
													R.string.commentRateSuccess)
											.setView(textEntryView)
											.setPositiveButton(
													R.string.ok,
													new DialogInterface.OnClickListener() {
														public void onClick(
																DialogInterface dialog,
																int whichButton) {
															Log
																	.d(
																			"displayCommentsBriefAgain",
																			"Will get the comments brief again.");
															displayCommentsBriefAgain();
														}

													}).create();

									confirmDialog.show();
								}

								dismiss();

							} catch (Exception e) {
								e.printStackTrace();
								Log.e("Comment or Rating error",
										"Comment or Rating error.");
							}
						}

						private void displayCommentsBriefAgain() {
							Future<Long> commentsCount = RemoteService
									.getAssetCommentCount(asset.getId() + "");
							Long count = 0L;
							try {
								count = commentsCount.get();
							} catch (Exception e) {
								e.printStackTrace();
							}
							Log.d("AssetDetail.getAssetCommentsCount",
									"count : " + count);

							TextView commentSizeIntro = (TextView) AssetDetail.this
									.findViewById(R.id.commentSizeIntro);
							commentSizeIntro.setText(MessageFormat.format(
									getText(R.string.commentsIntro).toString(),
									count));

							Button seeCommentDetailsBtn = (Button) AssetDetail.this
									.findViewById(R.id.seeCommentDetailsBtn);
							if (count > BRIEF_COMENTS_COUNT) {
								seeCommentDetailsBtn
										.setVisibility(Button.VISIBLE);
							}

							final ArrayList<MobileComment> mobileComments = new ArrayList<MobileComment>();

							Future<ArrayList<MobileComment>> future = RemoteService
									.getAssetComment(asset.getId() + "", 0,
											BRIEF_COMENTS_COUNT);
							try {
								mobileComments.addAll(future.get());
							} catch (Exception e) {
								e.printStackTrace();
							}

							ArrayList<HashMap<String, Object>> listItem = new ArrayList<HashMap<String, Object>>();
							for (MobileComment mobileComment : mobileComments) {
								HashMap<String, Object> map = new HashMap<String, Object>();
								map.put("commentUser", mobileComment
										.getUserId());
								map.put("briefCommentDate", sdf
										.format(mobileComment.getDate()));
								map.put("briefCommentContent", mobileComment
										.getComment());
								listItem.add(map);
							}

							SimpleAdapter listItemAdapter = new SimpleAdapter(
									AssetDetail.this, listItem,
									R.layout.comment_brief, new String[] {
											"commentUser", "briefCommentDate",
											"briefCommentContent" }, new int[] {
											R.id.commentUser,
											R.id.briefCommentDate,
											R.id.briefCommentContent });

							ListView commentListView = (ListView) AssetDetail.this
									.findViewById(R.id.commentList);
							commentListView.setAdapter(listItemAdapter);
						}

					});

			ratingBar.setOnRatingBarChangeListener(this);
		}

		public void onRatingChanged(RatingBar rb, float rating,
				boolean fromTouch) {
			final int numStars = rb.getNumStars();

			if (ratingBar.getNumStars() != numStars) {
				ratingBar.setNumStars(numStars);
			}
		}
	}

}
