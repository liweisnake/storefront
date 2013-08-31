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
package com.hp.sdf.ngp.sdp.webservice;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import org.apache.axis2.AxisFault;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

import com.hp.sdf.ngp.common.annotation.Value;
import com.hp.sdf.ngp.common.exception.SGFCallingFailureException;
import com.hp.sdf.ngp.sdp.model.SGFRestServiceMetadata;
import com.hp.sdf.ngp.sdp.model.SGFService;

@Component
public class SGFServiceAdapter {
	
	public final static Log log = LogFactory.getLog(SGFServiceAdapter.class);
	private String urlService;

	public String getUrlService() {
		return urlService;
	}

	@Value("SGF.WS.url.Service")
	public void setUrlService(String urlService) {
		this.urlService = urlService;
	}

	/**
	 * Get all SGF Services
	 * @return
	 */
	public List<SGFService> getSGFAllServices() throws SGFCallingFailureException{
		log.debug("Get all SGF services from " + urlService);
		ServicemgmtServiceStub stub;
		List<SGFService> result = null;
		try {
			stub = new ServicemgmtServiceStub(urlService);
			ServicemgmtServiceStub.GetAllServices request = new ServicemgmtServiceStub.GetAllServices();
			ServicemgmtServiceStub.GetAllServicesE requestE = new ServicemgmtServiceStub.GetAllServicesE();
			requestE.setGetAllServices(request);
			ServicemgmtServiceStub.GetAllServicesResponseE responseE = stub.getAllServices(requestE);
			ServicemgmtServiceStub.ServiceDetails[] services = responseE.getGetAllServicesResponse().getServices().getService();
			if(services == null || services.length == 0)
				return null;
			result = new ArrayList<SGFService>();
			for(int i = 0; i < services.length; i++){
				result.add(this.mappingServiceVO(services[i]));
			}
			log.debug("Get all SGF services from " + urlService + " finished. Total size is " + result.size());
		} catch (AxisFault e) {
			if(e.getDetail() != null)
				log.error(e.getDetail().toString());
			throw new SGFCallingFailureException(e.getMessage(),e);
		} catch (RemoteException e) {
			throw new SGFCallingFailureException(e.getMessage(),e);
		} catch (SGFException e) {
			throw new SGFCallingFailureException(e.getMessage(),e);
		}
		return result;
	}
	
	/**
	 * get SGF Service by ID
	 * @param id
	 * @return
	 */
	public SGFService getSGFServiceByID(String id) throws SGFCallingFailureException{
		log.debug("Get SGF service by ID from " + urlService + ". The service ID is " + id);
		ServicemgmtServiceStub stub;
		SGFService result = null;
		try {
			stub = new ServicemgmtServiceStub(urlService);
			ServicemgmtServiceStub.GetServiceByID request = new ServicemgmtServiceStub.GetServiceByID();
			request.setServiceID(id);
			ServicemgmtServiceStub.GetServiceByIDE requestE = new ServicemgmtServiceStub.GetServiceByIDE();
			requestE.setGetServiceByID(request);
			ServicemgmtServiceStub.GetServiceByIDResponseE responseE = stub.getServiceByID(requestE);
			ServicemgmtServiceStub.ServiceDetails[] services = responseE.getGetServiceByIDResponse().getServices().getService();
			if(services != null && services.length > 0)
				result = this.mappingServiceVO(services[0]);
			log.debug("Get SGF service by ID from " + urlService + " successfuuly. The service ID is " + id);
		} catch (AxisFault e) {
			if(e.getDetail() != null)
				log.error(e.getDetail().toString());
			throw new SGFCallingFailureException(e.getMessage(),e);
		} catch (RemoteException e) {
			throw new SGFCallingFailureException(e.getMessage(),e);
		} catch (SGFException e) {
			throw SGFErrorHandler.handleError(e);
		}
		return result;
	}
	
	/**
	 * get SGF Service by name
	 * @param serviceName
	 * @return
	 */
	public SGFService getSGFServiceByName(String serviceName, String version, String accessInterface) throws SGFCallingFailureException{
		log.debug("Get SGF service by name from " + urlService + ". The service name is " + serviceName);
		ServicemgmtServiceStub stub;
		SGFService result = null;
		try {
			stub = new ServicemgmtServiceStub(urlService);
			ServicemgmtServiceStub.GetServiceByName request = new ServicemgmtServiceStub.GetServiceByName();
			request.setServiceName(serviceName);
			request.setVersion(version);
			ServicemgmtServiceStub.AccessInterface ai = new ServicemgmtServiceStub.AccessInterface(accessInterface,true);
			request.setAccessInterface(ai);
			ServicemgmtServiceStub.GetServiceByNameE requestE = new ServicemgmtServiceStub.GetServiceByNameE();
			requestE.setGetServiceByName(request);
			ServicemgmtServiceStub.GetServiceByNameResponseE responseE = stub.getServiceByName(requestE);
			ServicemgmtServiceStub.ServiceDetails[] services = responseE.getGetServiceByNameResponse().getServices().getService();
			if(services != null && services.length > 0)
				result = this.mappingServiceVO(services[0]);
			log.debug("Get SGF service by name from " + urlService + " successfuuly. The service name is " + serviceName);
		} catch (AxisFault e) {
			if(e.getDetail() != null)
				log.error(e.getDetail().toString());
			throw new SGFCallingFailureException(e.getMessage(),e);
		} catch (RemoteException e) {
			throw new SGFCallingFailureException(e.getMessage(),e);
		} catch (SGFException e) {
			throw new SGFCallingFailureException(e.getMessage(),e);
		}
		return result;
	}
	
	/**
	 * get SGF Services by SDG
	 * @param sdgName
	 * @return
	 */
	public List<SGFService> getSGFServicesBySDG(String sdgName) throws SGFCallingFailureException{
		log.debug("Get SGF services by SDG from " + urlService + ". The SDG name is " + sdgName);
		ServicemgmtServiceStub stub;
		List<SGFService> result = null;
		try {
			stub = new ServicemgmtServiceStub(urlService);
			ServicemgmtServiceStub.GetAllServicesForServiceDiscoveryGroup request = new ServicemgmtServiceStub.GetAllServicesForServiceDiscoveryGroup();
			request.setServiceDiscoveryGroupName(sdgName);
			ServicemgmtServiceStub.GetAllServicesForServiceDiscoveryGroupE requestE = new ServicemgmtServiceStub.GetAllServicesForServiceDiscoveryGroupE();
			requestE.setGetAllServicesForServiceDiscoveryGroup(request);
			ServicemgmtServiceStub.GetAllServicesForServiceDiscoveryGroupResponseE responseE = stub.getAllServicesForServiceDiscoveryGroup(requestE);
			ServicemgmtServiceStub.ServiceDetails[] services = responseE.getGetAllServicesForServiceDiscoveryGroupResponse().getServices().getService();
			
			if(services != null && services.length > 0){
				result = new ArrayList<SGFService>();
				for(int i = 0; i < services.length; i++)
					result.add(this.mappingServiceVO(services[i]));
			}
			log.debug("Get SGF services by SDG from " + urlService + " successfully. The SDG name is " + sdgName);
		} catch (AxisFault e) {
			if(e.getDetail() != null)
				log.error(e.getDetail().toString());
			throw new SGFCallingFailureException(e.getMessage(),e);
		} catch (RemoteException e) {
			throw new SGFCallingFailureException(e.getMessage(),e);
		} catch (SGFException e) {
			if(e.getFaultMessage().getSGFException().getStatus().getText().toString().equalsIgnoreCase(ServicemgmtServiceStub.ExceptionType._value11)){
				throw SGFErrorHandler.handleError(e);
			}
			throw new SGFCallingFailureException(e.getMessage(),e);
		}
		return result;
	}
	
	/**
	 * Get Metadata of REST Service from SGF
	 * @param id
	 * @return
	 * @throws SGFCallingFailureException
	 */
	public SGFRestServiceMetadata getMetadataForRESTService(String id) throws SGFCallingFailureException{
		log.debug("Get Metadata of SGF Rest Service. The Service ID is " + id);
		ServicemgmtServiceStub stub;
		SGFRestServiceMetadata result = null;
		try {
			stub = new ServicemgmtServiceStub(urlService);
			ServicemgmtServiceStub.GetMetadataForRESTService request = new ServicemgmtServiceStub.GetMetadataForRESTService();
			request.setServiceID(id);
			ServicemgmtServiceStub.GetMetadataForRESTServiceE requestE = new ServicemgmtServiceStub.GetMetadataForRESTServiceE();
			requestE.setGetMetadataForRESTService(request);
			ServicemgmtServiceStub.GetMetadataForRESTServiceResponseE responseE = stub.getMetadataForRESTService(requestE);
			ServicemgmtServiceStub.MetadataDetails metadataDetails = responseE.getGetMetadataForRESTServiceResponse().getMetadataDetails();
			result = this.mappingRestServiceMetadata(metadataDetails);
			log.debug("Get Metadata of SGF Rest Service successfully. The Service ID is " + id);
		} catch (AxisFault e) {
			if(e.getDetail() != null)
				log.error(e.getDetail().toString());
			throw new SGFCallingFailureException(e.getMessage(),e);
		} catch (RemoteException e) {
			throw new SGFCallingFailureException(e.getMessage(),e);
		} catch (SGFException e) {
			throw new SGFCallingFailureException(e.getMessage(),e);
		}
		return result;
	}
	
	/**
	 * Transfer Service data model from SGF to NGP
	 * @param service
	 * @return
	 */
	private SGFService mappingServiceVO(ServicemgmtServiceStub.ServiceDetails service){
		
		SGFService result = new SGFService();
		result.setName(service.getName());
		result.setId(service.getId());
		result.setVersion(service.getVersion());
		result.setDescription(service.getDescription());
		result.setServiceProvider(service.getServiceProvider());
		result.setTrafficType(service.getTrafficType().getValue());
		result.setServiceIndication(service.getServiceIndication().getValue());
		result.setAccessInterface(service.getAccessInterface().getValue());
		if(service.getOutboundSecurity() != null){
			SGFService.OutboundSecurity outboundSecurity = result.new OutboundSecurity();
			outboundSecurity.setAuthentication(service.getOutboundSecurity().getAuthentication());
			outboundSecurity.setTransport(service.getOutboundSecurity().getTransport());
			result.setOutboundSecurity(outboundSecurity);
		}
		result.setServiceCategory(service.getServiceCategory().getValue());
		result.setKeywords(service.getKeywords());
		result.setEnablerName(service.getEnablerName());
		result.setStatus(service.getStatus());
		List<SGFService.BrokerService> brokerServices = new ArrayList<SGFService.BrokerService>();
		SGFService.BrokerService brokerService = result.new BrokerService();
		brokerService.setAuthType(service.getBrokerServices().getBrokerService()[0].getAuthType());
		brokerService.setBrokeredServiceName(service.getBrokerServices().getBrokerService()[0].getBrokeredServiceName());
		brokerService.setUrl(service.getBrokerServices().getBrokerService()[0].getUrl());
		brokerServices.add(brokerService);
		result.setBrokerServices(brokerServices);		
		
		return result;
	}
	
	/**
	 * Transfer Metadata data model from SGF to NGP
	 * @param meta
	 * @return
	 */
	private SGFRestServiceMetadata mappingRestServiceMetadata(ServicemgmtServiceStub.MetadataDetails meta){
		SGFRestServiceMetadata result = new SGFRestServiceMetadata();
		if(meta == null)
			return null;
		result.setServiceId(meta.getServiceID());
		ServicemgmtServiceStub.MethodE[] methodEs = meta.getMethod();
		if(methodEs == null || methodEs.length == 0)
			return null;
		result.setSampleRequestGet(new HashSet<SGFRestServiceMetadata.SampleRequestGet>());
		result.setSampleRequestPost(new HashSet<SGFRestServiceMetadata.SampleRequestPost>());
		result.setSampleResponse(new HashMap<String, SGFRestServiceMetadata.SampleResponse>());
		for(int i=0; i<methodEs.length; i++){
			ServicemgmtServiceStub.MethodE methodE = methodEs[i];
			ServicemgmtServiceStub.InputParameters input = methodE.getInput();
			//get request
			if(methodE.getName().equals(ServicemgmtServiceStub.Method._GET)){
				SGFRestServiceMetadata.SampleRequestGet request = result.new SampleRequestGet();
				request.setContentType(input.getContentType());
				request.setMetadata(input.getMetadata());
				result.getSampleRequestGet().add(request);
			}else if(methodE.getName().equals(ServicemgmtServiceStub.Method._POST)){
				SGFRestServiceMetadata.SampleRequestPost request = result.new SampleRequestPost();
				request.setContentType(input.getContentType());
				request.setMetadata(input.getMetadata());
				result.getSampleRequestPost().add(request);
			}
			//get response
			ServicemgmtServiceStub.OutputParameters[] outputs = input.getOutput();
			if(outputs != null && outputs.length > 0){
				for(int j=0; j<outputs.length; j++){
					SGFRestServiceMetadata.SampleResponse response = result.new SampleResponse();
					response.setContentType(outputs[j].getContentType());
					response.setResponseMeta(new HashSet<SGFRestServiceMetadata.SampleResponse.ResponseMeta>());
					if(!result.getSampleResponse().containsKey(outputs[j].getContentType()))
						result.getSampleResponse().put(outputs[j].getContentType(), response);
					SGFRestServiceMetadata.SampleResponse.ResponseMeta rm = response.new ResponseMeta();
					rm.setMetadata(outputs[j].getMetadata());
					rm.setStatus(String.valueOf(outputs[j].getStatus()));
					result.getSampleResponse().get(outputs[j].getContentType()).getResponseMeta().add(rm);
					
				}
			}
		}
		return result;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		SGFServiceAdapter adapter = new SGFServiceAdapter();
		try {
			//adapter.getSGFServicesBySDG("testSDG");
			//adapter.getSGFAllServices();
			//adapter.getSGFServiceByID("1bde219-122c4609699-1615713278");
			//adapter.getSGFServiceByName("LB_WLNG_NT_SendMessage", "1.0", "webservice");
			adapter.getMetadataForRESTService("1c12c2f-12319b36a80-1615713278");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
