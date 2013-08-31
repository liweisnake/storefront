package com.hp.sdf.ngp.handmark.purchase;

import java.io.Serializable;

/**
 * handmark purchase request.
 * 
 * @author lusong
 * 
 */
public class PurchaseRequest implements Serializable {

	private static final long serialVersionUID = -7034057075030618449L;

	private String userName;

	private String password;

	private String phoneNumber;

	private Double product_price;

	private String product_id;

	private String email;

	private String msisdn;

	private String currency;

	private String deviceSerial;

	private String device_id;

	/**
	 * @return the userName
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * @param userName
	 *            the userName to set
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password
	 *            the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @return the phoneNumber
	 */
	public String getPhoneNumber() {
		return phoneNumber;
	}

	/**
	 * @param phoneNumber
	 *            the phoneNumber to set
	 */
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	/**
	 * @return the product_price
	 */
	public Double getProduct_price() {
		return product_price;
	}

	/**
	 * @param productPrice
	 *            the product_price to set
	 */
	public void setProduct_price(Double productPrice) {
		product_price = productPrice;
	}

	/**
	 * @return the product_id
	 */
	public String getProduct_id() {
		return product_id;
	}

	/**
	 * @param productId
	 *            the product_id to set
	 */
	public void setProduct_id(String productId) {
		product_id = productId;
	}

	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param email
	 *            the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * @return the msisdn
	 */
	public String getMsisdn() {
		return msisdn;
	}

	/**
	 * @param msisdn
	 *            the msisdn to set
	 */
	public void setMsisdn(String msisdn) {
		this.msisdn = msisdn;
	}

	/**
	 * @return the currency
	 */
	public String getCurrency() {
		return currency;
	}

	/**
	 * @param currency
	 *            the currency to set
	 */
	public void setCurrency(String currency) {
		this.currency = currency;
	}

	/**
	 * @return the deviceSerial
	 */
	public String getDeviceSerial() {
		return deviceSerial;
	}

	/**
	 * @param deviceSerial
	 *            the deviceSerial to set
	 */
	public void setDeviceSerial(String deviceSerial) {
		this.deviceSerial = deviceSerial;
	}

	/**
	 * @return the device_id
	 */
	public String getDevice_id() {
		return device_id;
	}

	/**
	 * @param deviceId
	 *            the device_id to set
	 */
	public void setDevice_id(String deviceId) {
		device_id = deviceId;
	}

	/*
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "PurchaseRequest [currency=" + currency + ", deviceSerial=" + deviceSerial + ", device_id=" + device_id + ", email=" + email + ", msisdn=" + msisdn + ", password=" + password
				+ ", phoneNumber=" + phoneNumber + ", product_id=" + product_id + ", product_price=" + product_price + ", userName=" + userName + "]";
	}

}
