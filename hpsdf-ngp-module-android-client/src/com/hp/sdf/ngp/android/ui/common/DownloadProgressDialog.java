package com.hp.sdf.ngp.android.ui.common;

import java.io.File;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.hp.sdf.ngp.android.R;
import com.hp.sdf.ngp.android.util.AndroidUtils;
import com.hp.sdf.ngp.android.util.RemoteService;

public class DownloadProgressDialog extends ProgressDialog {

	public static final int FILE_DOWNLOAD_CONNECT = 1;

	public static final int FILE_DOWNLOAD_UPDATE = 2;

	public static final int FILE_DOWNLOAD_COMPLETE = 3;

	public static final int FILE_DOWNLOAD_ERROR = -1;

	private final int MAX_PROGRESS = 100;

	private Context parent;

	private Handler progressHandler;

	private int progressNum;

	private String url;

	private final String sdPath = Environment.getExternalStorageDirectory()
			.toString();

	public DownloadProgressDialog(Context context, String url) {
		super(context);
		this.parent = context;
		this.url = url;

		init();
	}

	public void init() {
		setIcon(R.drawable.download);
		setTitle(R.string.progressTitle);
		setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		setMax(MAX_PROGRESS);

		setButton(parent.getText(R.string.hidden),
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {

					}
				});

		setButton2(parent.getText(R.string.cancel),
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {

					}
				});

		progressHandler = new Handler() {
			public void handleMessage(Message msg) {
				super.handleMessage(msg);

				switch (msg.what) {
				case FILE_DOWNLOAD_CONNECT:
					break;

				case FILE_DOWNLOAD_UPDATE:
					if (progressNum >= MAX_PROGRESS) {
						dismiss();
					} else {
						progressNum++;
						incrementProgressBy((Integer) msg.obj);
					}
					break;

				case FILE_DOWNLOAD_COMPLETE:
					dismiss();

					File file = new File(msg.obj + "");
					Log.d("DownloadProgressDialog>>init>>file size:",
							"file size:" + file.length());
					Log.d("DownloadProgressDialog>>init>>file path:",
							"file path:" + msg.obj);

					String path = url
							.substring(url.lastIndexOf(File.separator));
					Log.d("DownloadProgressDialog>>init:", "url :" + url);
					Log.d("DownloadProgressDialog>>init:", "path :" + path);

					Log.d("DownloadProgressDialog>>init:", "install path :"
							+ (sdPath + path));

					if (path.endsWith(".apk") || path.endsWith(".zip")) {
						AndroidUtils.install(parent, sdPath + path);
					}
					break;

				case FILE_DOWNLOAD_ERROR:
					break;

				default:
					break;
				}

			}
		};
	}

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		progressNum = 0;
		setProgress(0);

		int lastPoint = url.lastIndexOf(File.separator);
		Log.d("DownloadProgressDialog.onCreate", "File.separator :"
				+ File.separator);
		Log.d("DownloadProgressDialog.onCreate", "lastPoint :" + lastPoint);

		String urlStr = url.substring(lastPoint > 0 ? lastPoint : 0);
		Log.d("DownloadProgressDialog.onCreate", "urlStr :" + urlStr);

		Log.d("DownloadProgressDialog.onCreate",
				"Environment.getExternalStorageDirectory :" + sdPath);

		RemoteService.downloadFile(this, url, sdPath + urlStr);
	}

	public void sendMessage(int what, Object obj) {
		if (obj == null)
			progressHandler.sendEmptyMessage(what);
		else {
			Message msg = progressHandler.obtainMessage(what, obj);
			progressHandler.sendMessage(msg);
		}
	}
}
