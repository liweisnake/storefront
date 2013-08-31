/*
 * Copyright (c) 2009 Hewlett-Packard Company, All Rights Reserved.
 *
 * RESTRICTED RIGHTS LEGEND Use, duplication, or disclosure by the U.S.
 * Government is subject to restrictions as set forth in sub-paragraph
 * (c)(1)(ii) of the Rights in Technical Data and Computer Software
 * clause in DFARS 252.227-7013.
 *
 * Hewlett-Packard Company
 * 3000 Hanover Street
 * Palo Alto, CA 94304 U.S.A.
 * Rights for non-DOD U.S. Government Departments and Agencies are as
 * set forth in FAR 52.227-19(c)(1,2).
 */
package com.hp.sdf.ngp.handmark.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.stereotype.Component;

import com.hp.sdf.ngp.api.comon.EntityType;
import com.hp.sdf.ngp.common.constant.HandmarkConstant;
import com.hp.sdf.ngp.common.exception.NgpException;
import com.hp.sdf.ngp.eav.model.AttributeValue;
import com.hp.sdf.ngp.handmark.purchase.PurchaseRequest;
import com.hp.sdf.ngp.handmark.purchase.PurchaseResponse;
import com.hp.sdf.ngp.manager.DownloadConnector;
import com.hp.sdf.ngp.manager.DownloadManager;
import com.hp.sdf.ngp.model.Asset;
import com.hp.sdf.ngp.model.AssetPrice;
import com.hp.sdf.ngp.service.ApplicationService;

@Component
public class DownloadConnectorHandmarkImpl implements DownloadConnector {

	public final static Log log = LogFactory
			.getLog(DownloadConnectorHandmarkImpl.class);

	private static final String DEMO_MSISDN = "8613817661362";

	private static final String DEMO_EMAIL = "lusong19868290@163.com";

	private static final String DEMO_PHONE = "13817661362";

	private static final String DEMO_PASS_WORD = "demoPa$$WorD";

	private static final String DEMOFEED_USER = "demofeed_user";

	private static final String USD = "USD";

	/**
	 * HTTP Status-Code 200: OK.
	 */
	public static final int HTTP_OK = 200;

	@Resource
	private DownloadManager downloadManager;

	@Resource
	private ApplicationService applicationService;

	/**
	 * retrieve binary.
	 * 
	 * @param asset
	 * @param deviceSerial
	 * @return handmark binary Bytes
	 */
	@PostConstruct
	public void init() {
		this.downloadManager.registerConnector(HandmarkConstant.HANDMARK, this);
	}

	public byte[] retrieveBinary(Long assetId, Long versionId,
			String deviceSerial) {

		String binaryUrl = retrievedownloadURI(assetId, versionId, deviceSerial);
		log.info("handmark BinaryUrl from purchaseResponse :" + binaryUrl);

		byte[] binaryBytes = null;
		if (StringUtils.isNotEmpty(binaryUrl)) {
			binaryBytes = DownLoadHandmarkBinaryUtil
					.downloadHandmarkBinary(binaryUrl);
		}

		return binaryBytes;
	}

	/**
	 * retrieve download URI.
	 * 
	 * @param asset
	 * @param deviceSerial
	 * @return handmark binary url
	 */
	public String retrievedownloadURI(Long assetId, Long versionId,
			String deviceSerial) {

		PurchaseRequest purchaseRequest = null;
		Asset asset = this.applicationService.getAsset(assetId);
		try {
			purchaseRequest = setPurchaseRequest(asset, deviceSerial);
		} catch (NgpException e) {
			e.printStackTrace();
			log.error("NgpException :" + e);
		}
		log.info("purchaseRequest :" + purchaseRequest);

		return purchaseHandmarkProduct(purchaseRequest);
	}

	/**
	 * set purchaseRequest by asset and deviceSerial.
	 * 
	 * @param asset
	 * @param deviceSerial
	 * @return purchaseRequest
	 * @throws NgpException
	 */
	@SuppressWarnings("unchecked")
	private PurchaseRequest setPurchaseRequest(Asset asset, String deviceSerial)
			throws NgpException {

		PurchaseRequest purchaseRequest = new PurchaseRequest();
		purchaseRequest.setUserName(DEMOFEED_USER);
		purchaseRequest.setPassword(DEMO_PASS_WORD);
		purchaseRequest.setPhoneNumber(DEMO_PHONE);
		purchaseRequest.setEmail(DEMO_EMAIL);
		purchaseRequest.setMsisdn(DEMO_MSISDN);

		purchaseRequest.setDeviceSerial(deviceSerial);
		purchaseRequest.setCurrency(USD);

		List<AssetPrice> assetPrices = applicationService
				.getAssetPriceByAssetId(asset.getId());
		if (assetPrices != null) {
			for (AssetPrice price : assetPrices) {
				if (USD.equalsIgnoreCase(price.getCurrency())) {
					BigDecimal amount = price.getAmount();
					purchaseRequest.setProduct_price(amount.doubleValue());
				}
			}

		}

		List<AttributeValue> productIdAttributeValues = applicationService
				.getAttributeValue(asset.getId(), EntityType.ASSET,
						HandmarkConstant.PRODUCT_ID);
		if (productIdAttributeValues != null
				&& productIdAttributeValues.size() > 0) {
			AttributeValue firstProductIdAttributeValue = productIdAttributeValues
					.get(0);
			if (firstProductIdAttributeValue != null) {
				Float productId = (Float) firstProductIdAttributeValue
						.getValue();

				if (productId != null) {
					purchaseRequest.setProduct_id(String.valueOf(productId
							.intValue()));
				}
			}
		}

		List<AttributeValue> deviceIdAttributeValues = applicationService
				.getAttributeValue(asset.getId(), EntityType.ASSET,
						HandmarkConstant.DEVICE_ID);
		if (deviceIdAttributeValues != null
				&& deviceIdAttributeValues.size() > 0) {
			AttributeValue firstDeviceIdAttributeValue = deviceIdAttributeValues
					.get(0);
			if (firstDeviceIdAttributeValue != null) {
				Float deviceId = (Float) firstDeviceIdAttributeValue.getValue();

				if (deviceId != null) {
					purchaseRequest.setDevice_id(String.valueOf(deviceId
							.intValue()));
				}
			}
		}

		return purchaseRequest;
	}

	/**
	 * get Handmark Product URL.
	 * 
	 * @param purchaseRequest
	 *            PurchaseRequest
	 * @return binary url
	 */
	private String purchaseHandmarkProduct(PurchaseRequest purchaseRequest) {

		PurchaseResponse purchaseResponse = null;
		try {
			HttpURLConnection conn = getPurchaseConnectResponse(purchaseRequest);

			if (conn.getContentLength() > 0
					&& HTTP_OK == conn.getResponseCode()) {

				// Get the response
				BufferedReader rd = new BufferedReader(new InputStreamReader(
						conn.getInputStream()));
				StringBuilder xmlResponse = new StringBuilder();
				String line;
				while ((line = rd.readLine()) != null) {
					xmlResponse.append(line);
				}

				log.info("Handmark Response : " + xmlResponse);
				purchaseResponse = parseToResponse(xmlResponse.toString());

				rd.close();
			}

		} catch (MalformedURLException exception) {
			exception.printStackTrace();
			log.error("MalformedURLException :\n" + exception);
		} catch (IOException exception) {
			exception.printStackTrace();
			log.error("IOException :\n" + exception);
		}

		String binaryUrl = "";
		if (purchaseResponse != null) {
			binaryUrl = purchaseResponse.getUrl();
		}

		return binaryUrl;
	}

	/**
	 * @param purchaseRequest
	 * @return
	 * @throws MalformedURLException
	 * @throws IOException
	 */
	private HttpURLConnection getPurchaseConnectResponse(
			PurchaseRequest purchaseRequest) throws MalformedURLException,
			IOException {
		URL url = new URL(HandmarkConstant.PURCHASE_HANDMARK_ENDPOINT);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setDoOutput(true);

		// send data
		OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
		StringBuilder request = generateXMLRequest(purchaseRequest);
		wr.write(request.toString());
		wr.flush();
		wr.close();

		log.info("\ncon.getContentLength():[" + conn.getContentLength() + "]");
		log.info("con.getResponseCode():[" + conn.getResponseCode() + "]\n");
		return conn;
	}

	/**
	 * @param purchaseRequest
	 * @return request string
	 */
	private StringBuilder generateXMLRequest(PurchaseRequest purchaseRequest) {

		StringBuilder request = new StringBuilder();
		request.append("<PurchaseRequest>");

		request.append("<PhoneNumber>");
		request.append(null == purchaseRequest.getPhoneNumber() ? ""
				: purchaseRequest.getPhoneNumber());
		request.append("</PhoneNumber>");

		request.append("<product_price>");
		request.append(null == purchaseRequest.getProduct_price() ? ""
				: purchaseRequest.getProduct_price());
		request.append("</product_price>");

		request.append("<product_id>");
		request.append(null == purchaseRequest.getProduct_id() ? ""
				: purchaseRequest.getProduct_id());
		request.append("</product_id>");

		request.append("<Email>");
		request.append(null == purchaseRequest.getEmail() ? ""
				: purchaseRequest.getEmail());
		request.append("</Email>");

		request.append("<MSISDN>");
		request.append(null == purchaseRequest.getMsisdn() ? ""
				: purchaseRequest.getMsisdn());
		request.append("</MSISDN>");

		request.append("<Currency>");
		request.append(null == purchaseRequest.getCurrency() ? ""
				: purchaseRequest.getCurrency());
		request.append("</Currency>");

		request.append("<UserName>");
		request.append(null == purchaseRequest.getUserName() ? ""
				: purchaseRequest.getUserName());
		request.append("</UserName>");

		request.append("<Password>");
		request.append(null == purchaseRequest.getPassword() ? ""
				: purchaseRequest.getPassword());
		request.append("</Password>");

		request.append("<DeviceSerial>");
		request.append(null == purchaseRequest.getDeviceSerial() ? ""
				: purchaseRequest.getDeviceSerial());
		request.append("</DeviceSerial>");

		request.append("<device_id>");
		request.append(null == purchaseRequest.getDevice_id() ? ""
				: purchaseRequest.getDevice_id());
		request.append("</device_id>");

		request.append("</PurchaseRequest>");
		log.info("PurchaseRequest : \n" + request);
		return request;
	}

	/**
	 * 
	 * @param xmlResponse
	 *            string response
	 * @return PurchaseResponse
	 */
	@SuppressWarnings("unchecked")
	private PurchaseResponse parseToResponse(String xmlResponse) {
		PurchaseResponse purchaseResponse = new PurchaseResponse();

		try {
			Document doc = DocumentHelper.parseText(xmlResponse);
			Element rootElement = doc.getRootElement();
			Iterator<Element> iter = rootElement.elementIterator();
			while (iter.hasNext()) {
				Element e = iter.next();
				if ("order_number".equalsIgnoreCase(e.getName())) {
					purchaseResponse.setOrder_number((String) e.getData());
				}

				if ("status".equalsIgnoreCase(e.getName())) {
					purchaseResponse.setStatus((String) e.getData());
				}

				if ("serial_number".equalsIgnoreCase(e.getName())) {
					purchaseResponse.setSerial_number((String) e.getData());
				}

				if ("url".equalsIgnoreCase(e.getName())) {
					purchaseResponse.setUrl((String) e.getData());
				}

				if ("subtotal".equalsIgnoreCase(e.getName())) {
					purchaseResponse.setSubtotal(Double.parseDouble((String) e
							.getData()));
				}
			}

		} catch (DocumentException exception) {
			exception.printStackTrace();
			log.error("DocumentException :\n" + exception);
		}

		return purchaseResponse;
	}

}
