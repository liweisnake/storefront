/**
 * SendSms.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.hp.sdf.ngp.enabler.sms.webservice;

public interface SendSms extends java.rmi.Remote {

	public void main(java.lang.String[] args) throws java.rmi.RemoteException, WrongSessionStateException;
    
	public java.lang.String getString(java.lang.String s)
			throws java.rmi.RemoteException;

	public java.lang.String sendSms(
			org.apache.axis.types.URI[] addresses,
			java.lang.String senderName,
			ChargingInformation charging,
			java.lang.String message,
			SimpleReference receiptRequest)
			throws java.rmi.RemoteException,
			WrongSessionStateException;

}
