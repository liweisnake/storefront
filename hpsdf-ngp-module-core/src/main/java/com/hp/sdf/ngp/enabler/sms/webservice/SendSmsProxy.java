package com.hp.sdf.ngp.enabler.sms.webservice;

public class SendSmsProxy implements SendSms {
  private String _endpoint = null;
  private SendSms sendSms = null;
  
  public SendSmsProxy() {
    _initSendSmsProxy();
  }
  
  public SendSmsProxy(String endpoint) {
    _endpoint = endpoint;
    _initSendSmsProxy();
  }
  
  private void _initSendSmsProxy() {
    try {
      sendSms = (new SendSmsServiceLocator()).getSendSms(_endpoint);
      if (sendSms != null) {
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)sendSms)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)sendSms)._getProperty("javax.xml.rpc.service.endpoint.address");
      }
      
    }
    catch (javax.xml.rpc.ServiceException serviceException) {}
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (sendSms != null)
      ((javax.xml.rpc.Stub)sendSms)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public SendSms getSendSms() {
    if (sendSms == null)
      _initSendSmsProxy();
    return sendSms;
  }
  
  public void main(java.lang.String[] args) throws java.rmi.RemoteException, WrongSessionStateException{
    if (sendSms == null)
      _initSendSmsProxy();
    sendSms.main(args);
  }
  
  public java.lang.String getString(java.lang.String s) throws java.rmi.RemoteException{
    if (sendSms == null)
      _initSendSmsProxy();
    return sendSms.getString(s);
  }
  
  public java.lang.String sendSms(org.apache.axis.types.URI[] addresses, java.lang.String senderName, ChargingInformation charging, java.lang.String message, SimpleReference receiptRequest) throws java.rmi.RemoteException, WrongSessionStateException{
    if (sendSms == null)
      _initSendSmsProxy();
    return sendSms.sendSms(addresses, senderName, charging, message, receiptRequest);
  }
  
  
}