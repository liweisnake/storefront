package com.hp.sdf.ngp.android.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;

import android.util.Log;

/**
 * 
 * RestClient.
 * 
 */
public class RestClient {

	public String create(String url) {
		Log.d("create url : ", url);

		HttpPost post = new HttpPost(url);
		return convertStreamToString(connect(post));
	}

	public String read(String url) {
		Log.d("read url : ", url);

		HttpGet get = new HttpGet(url);
		return convertStreamToString(connect(get));
	}

	public String update(String url) {
		Log.d("update url : ", url);

		HttpPut put = new HttpPut(url);
		return convertStreamToString(connect(put));
	}

	public String delete(String url) {
		Log.d("delete url : ", url);

		HttpDelete del = new HttpDelete(url);
		return convertStreamToString(connect(del));
	}

	private static String convertStreamToString(InputStream is) {
		if (null == is) {
			return null;
		}

		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();

		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		String streamContent = sb.toString();
		Log.d("RestClient.convertStreamToString", streamContent);

		return streamContent;
	}

	private InputStream connect(HttpUriRequest request) {

		HttpClient httpclient = new DefaultHttpClient();
		try {
			HttpResponse response = httpclient.execute(request);
			Log.d("Connect success! response status is: ", response
					.getStatusLine().toString());

			HttpEntity entity = response.getEntity();
			if (entity != null) {
				InputStream instream = entity.getContent();
				return instream;
			}

		} catch (ClientProtocolException e) {
			e.printStackTrace();
			Log.e("Client protocol exception:", e.getMessage());
		} catch (IOException e) {
			e.printStackTrace();
			Log.e("IO exception:", e.getMessage());
		}

		return null;
	}
}