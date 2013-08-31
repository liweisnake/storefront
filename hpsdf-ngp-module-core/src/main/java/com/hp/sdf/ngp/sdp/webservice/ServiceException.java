
/**
 * ServiceException.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.4  Built on : Apr 26, 2008 (06:24:30 EDT)
 */

package com.hp.sdf.ngp.sdp.webservice;

public class ServiceException extends java.lang.Exception{
    
    private com.hp.sdf.ngp.sdp.webservice.RateManagementServiceStub.ServiceExceptionE faultMessage;
    
    public ServiceException() {
        super("ServiceException");
    }
           
    public ServiceException(java.lang.String s) {
       super(s);
    }
    
    public ServiceException(java.lang.String s, java.lang.Throwable ex) {
      super(s, ex);
    }
    
    public void setFaultMessage(com.hp.sdf.ngp.sdp.webservice.RateManagementServiceStub.ServiceExceptionE msg){
       faultMessage = msg;
    }
    
    public com.hp.sdf.ngp.sdp.webservice.RateManagementServiceStub.ServiceExceptionE getFaultMessage(){
       return faultMessage;
    }
}
    