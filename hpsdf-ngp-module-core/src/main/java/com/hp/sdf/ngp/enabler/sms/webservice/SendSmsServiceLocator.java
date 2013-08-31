/**
 * SendSmsServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.hp.sdf.ngp.enabler.sms.webservice;

public class SendSmsServiceLocator extends org.apache.axis.client.Service implements SendSmsService {

    public SendSmsServiceLocator() {
    }


    public SendSmsServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public SendSmsServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for SendSms
    private java.lang.String SendSms_address = "localhost";

    public java.lang.String getSendSmsAddress() {
        return SendSms_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String SendSmsWSDDServiceName = "SendSms";

    public java.lang.String getSendSmsWSDDServiceName() {
        return SendSmsWSDDServiceName;
    }

    public void setSendSmsWSDDServiceName(java.lang.String name) {
        SendSmsWSDDServiceName = name;
    }

    public SendSms getSendSms() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(SendSms_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getSendSms(endpoint);
    }
    
    public SendSms getSendSms(String _endpoint) throws javax.xml.rpc.ServiceException {
        java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(_endpoint);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getSendSms(endpoint);
    }

    public SendSms getSendSms(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            SendSmsSoapBindingStub _stub = new SendSmsSoapBindingStub(portAddress, this);
            _stub.setPortName(getSendSmsWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setSendSmsEndpointAddress(java.lang.String address) {
        SendSms_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (SendSms.class.isAssignableFrom(serviceEndpointInterface)) {
                SendSmsSoapBindingStub _stub = new SendSmsSoapBindingStub(new java.net.URL(SendSms_address), this);
                _stub.setPortName(getSendSmsWSDDServiceName());
                return _stub;
            }
        }
        catch (java.lang.Throwable t) {
            throw new javax.xml.rpc.ServiceException(t);
        }
        throw new javax.xml.rpc.ServiceException("There is no stub implementation for the interface:  " + (serviceEndpointInterface == null ? "null" : serviceEndpointInterface.getName()));
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(javax.xml.namespace.QName portName, Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        if (portName == null) {
            return getPort(serviceEndpointInterface);
        }
        java.lang.String inputPortName = portName.getLocalPart();
        if ("SendSms".equals(inputPortName)) {
            return getSendSms();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName(SendSms_address, "SendSmsService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName(SendSms_address, "SendSms"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("SendSms".equals(portName)) {
            setSendSmsEndpointAddress(address);
        }
        else 
{ // Unknown Port Name
            throw new javax.xml.rpc.ServiceException(" Cannot set Endpoint Address for Unknown Port" + portName);
        }
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(javax.xml.namespace.QName portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        setEndpointAddress(portName.getLocalPart(), address);
    }

}
