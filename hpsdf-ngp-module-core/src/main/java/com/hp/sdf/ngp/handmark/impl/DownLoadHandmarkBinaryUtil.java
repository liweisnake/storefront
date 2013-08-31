package com.hp.sdf.ngp.handmark.impl;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.axiom.attachments.utils.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * DownLoadHandmarkBinaryUtil.
 * 
 * @author lusong
 * 
 */
public class DownLoadHandmarkBinaryUtil {

	public final static Log log = LogFactory
			.getLog(DownLoadHandmarkBinaryUtil.class);

	/**
	 * download handmark binary to bytes.
	 * 
	 * @param binaryURL
	 * @return binaryBytes
	 */
	public static byte[] downloadHandmarkBinary(String binaryURL) {
		int fileNameStartIndex = binaryURL.lastIndexOf("/");
		String fileName = binaryURL.substring(fileNameStartIndex + 1, binaryURL
				.length());
		log.debug("downloading file Name : " + fileName);

		byte[] binaryBytes = null;
		try {
			InputStream handmarkBinaryInputStream = getContentInputStream(binaryURL);
			binaryBytes = IOUtils
					.getStreamAsByteArray(handmarkBinaryInputStream);

			handmarkBinaryInputStream.close();
		} catch (MalformedURLException exception) {
			exception.printStackTrace();
			log.error(exception);
		} catch (IOException exception) {
			exception.printStackTrace();
			log.error(exception);
		}

		return binaryBytes;
	}

	/**
	 * get InputStream from net.
	 * 
	 * @param contentURL
	 * @return contentInputStream
	 * @throws MalformedURLException
	 * @throws IOException
	 */
	public static InputStream getContentInputStream(String contentURL)
			throws MalformedURLException, IOException {
		URL binaryUrl = new URL(contentURL);
		HttpURLConnection binaryConn = (HttpURLConnection) binaryUrl
				.openConnection();

		InputStream contentInputStream = binaryConn.getInputStream();
		return contentInputStream;
	}

}
