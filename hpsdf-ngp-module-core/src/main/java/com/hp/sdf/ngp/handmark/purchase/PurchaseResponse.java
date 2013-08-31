package com.hp.sdf.ngp.handmark.purchase;

import java.io.Serializable;

/**
 * * handmark purchase response.
 * 
 * @author lusong
 * 
 */
public class PurchaseResponse implements Serializable {

	private static final long serialVersionUID = 5790278309950693240L;

	private String order_number;

	private String status;

	private String serial_number;

	private String url;

	private double subtotal;

	/**
	 * @return the subtotal
	 */
	public double getSubtotal() {
		return subtotal;
	}

	/**
	 * @param subtotal
	 *            the subtotal to set
	 */
	public void setSubtotal(double subtotal) {
		this.subtotal = subtotal;
	}

	/**
	 * @return the order_number
	 */
	public String getOrder_number() {
		return order_number;
	}

	/**
	 * @param orderNumber
	 *            the order_number to set
	 */
	public void setOrder_number(String orderNumber) {
		order_number = orderNumber;
	}

	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * @param status
	 *            the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * @return the serial_number
	 */
	public String getSerial_number() {
		return serial_number;
	}

	/**
	 * @param serialNumber
	 *            the serial_number to set
	 */
	public void setSerial_number(String serialNumber) {
		serial_number = serialNumber;
	}

	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * @param url
	 *            the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/*
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "PurchaseResponse [order_number=" + order_number + ", serial_number=" + serial_number + ", status=" + status + ", subtotal=" + subtotal + ", url=" + url + "]";
	}

}
