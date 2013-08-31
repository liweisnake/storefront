
/**
 * PolicyException.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.4  Built on : Apr 26, 2008 (06:24:30 EDT)
 */

package com.hp.sdf.ngp.sdp.webservice;

public class PolicyException extends java.lang.Exception{
    
    private com.hp.sdf.ngp.sdp.webservice.RateManagementServiceStub.PolicyExceptionE faultMessage;
    
    public PolicyException() {
        super("PolicyException");
    }
           
    public PolicyException(java.lang.String s) {
       super(s);
    }
    
    public PolicyException(java.lang.String s, java.lang.Throwable ex) {
      super(s, ex);
    }
    
    public void setFaultMessage(com.hp.sdf.ngp.sdp.webservice.RateManagementServiceStub.PolicyExceptionE msg){
       faultMessage = msg;
    }
    
    public com.hp.sdf.ngp.sdp.webservice.RateManagementServiceStub.PolicyExceptionE getFaultMessage(){
       return faultMessage;
    }
}
    