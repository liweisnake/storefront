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
import java.util.List;

import org.apache.axis2.AxisFault;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

import com.hp.sdf.ngp.common.annotation.Value;
import com.hp.sdf.ngp.common.exception.SGFCallingFailureException;
import com.hp.sdf.ngp.sdp.model.SGFSG;
import com.hp.sdf.ngp.sdp.model.SGFService;

@Component
public class SGFServiceGroupExAdapter {
	
	public final static Log log = LogFactory.getLog(SGFServiceGroupExAdapter.class);
	
	private String urlServiceGroupEx;
	private String inboundSecurity;
	private String accessMode;
	private String applicationType;
	
	public String getInboundSecurity() {
		return inboundSecurity;
	}

	@Value("SGF.ServiceGroup.InboundSecurity")
	public void setInboundSecurity(String inboundSecurity) {
		this.inboundSecurity = inboundSecurity;
	}
	
	public String getApplicationType() {
		return applicationType;
	}

	@Value("SGF.ServiceGroup.WLNG.ApplicationType")
	public void setApplicationType(String applicationType) {
		this.applicationType = applicationType;
	}

	public String getAccessMode() {
		return accessMode;
	}

	@Value("SGF.ServiceGroup.AccessMode")
	public void setAccessMode(String accessMode) {
		this.accessMode = accessMode;
	}

	public String getUrlServiceGroupEx() {
		return urlServiceGroupEx;
	}

	@Value("SGF.WS.url.ServiceGroupEx")
	public void setUrlServiceGroupEx(String urlServiceGroupEx) {
		this.urlServiceGroupEx = urlServiceGroupEx;
	}

	/**
	 * Register Service Group
	 * @param sg
	 */
	public void registerSGFServiceGroup(SGFSG sg) throws SGFCallingFailureException{
		log.debug("Register Service Group to SGF. Endpoint: " + urlServiceGroupEx + ". Service Group name: " + sg.getName());
		ServiceGroupServiceExStub stub;
		try {
			stub = new ServiceGroupServiceExStub(urlServiceGroupEx);
			ServiceGroupServiceExStub.RegisterServiceGroup request = new ServiceGroupServiceExStub.RegisterServiceGroup();
			request.setServiceGroup(this.mappingServiceGroupVO(sg));
			ServiceGroupServiceExStub.RegisterServiceGroupE requestE = new ServiceGroupServiceExStub.RegisterServiceGroupE();
			requestE.setRegisterServiceGroup(request);
			//ServiceGroupServiceExStub.RegisterServiceGroupResponseE responseE = stub.registerServiceGroup(requestE);
			stub.registerServiceGroup(requestE);
			log.debug("Register Service Group to SGF successfully. Endpoint: " + urlServiceGroupEx + ". Service Group name: " + sg.getName());
		} catch (AxisFault e) {
			if(e.getDetail() != null)
				log.error(e.getDetail().toString());
			throw SGFErrorHandler.handleError(e);
		} catch (RemoteException e) {
			throw new SGFCallingFailureException(e.getMessage(),e);
		} catch (SGFException e) {
			throw new SGFCallingFailureException(e.getMessage(),e);
		}
	}
	
	/**
	 * Modify Service Group
	 * @param sg
	 */
	public void modifySGFServiceGroup(SGFSG sg) throws SGFCallingFailureException{
		log.debug("Modify Service Group to SGF. Endpoint: " + urlServiceGroupEx + ". Service Group name: " + sg.getName());
		ServiceGroupServiceExStub stub;
		try {
			stub = new ServiceGroupServiceExStub(urlServiceGroupEx);
			ServiceGroupServiceExStub.ModifyServiceGroup request = new ServiceGroupServiceExStub.ModifyServiceGroup();
			request.setServiceGroup(this.mappingServiceGroupVO(sg));
			ServiceGroupServiceExStub.ModifyServiceGroupE requestE = new ServiceGroupServiceExStub.ModifyServiceGroupE();
			requestE.setModifyServiceGroup(request);
			ServiceGroupServiceExStub.ModifyServiceGroupResponseE responseE = stub.modifyServiceGroup(requestE);
			log.debug("Modify Service Group to SGF successfully. Endpoint: " + urlServiceGroupEx + ". Service Group name: " + sg.getName());
		} catch (AxisFault e) {
			if(e.getDetail() != null)
				log.error(e.getDetail().toString());
			throw new SGFCallingFailureException(e.getMessage(),e);
		} catch (RemoteException e) {
			throw new SGFCallingFailureException(e.getMessage(),e);
		} catch (SGFException e) {
			throw new SGFCallingFailureException(e.getMessage(),e);
		}
	}
	
	/**
	 * Delete Service Group
	 * @param sg
	 */
	public void deleteSGFServiceGroup(SGFSG sg, String reason) throws SGFCallingFailureException{
		log.debug("Delete Service Group from SGF. Endpoint: " + urlServiceGroupEx + ". Service Group name: " + sg.getName());
		ServiceGroupServiceExStub stub;
		try {
			stub = new ServiceGroupServiceExStub(urlServiceGroupEx);
			ServiceGroupServiceExStub.DeleteServiceGroup request = new ServiceGroupServiceExStub.DeleteServiceGroup();
			request.setReason(reason);
			request.setServiceGroupName(sg.getName());
			request.setUsername(sg.getUsername());
			ServiceGroupServiceExStub.DeleteServiceGroupE requestE = new ServiceGroupServiceExStub.DeleteServiceGroupE();
			requestE.setDeleteServiceGroup(request);
			ServiceGroupServiceExStub.DeleteServiceGroupResponseE responseE = stub.deleteServiceGroup(requestE);
			log.debug("Delete Service Group from SGF successfully. Endpoint: " + urlServiceGroupEx + ". Service Group name: " + sg.getName());
		} catch (AxisFault e) {
			if(e.getDetail() != null)
				log.error(e.getDetail().toString());
			throw new SGFCallingFailureException(e.getMessage(),e);
		} catch (RemoteException e) {
			throw new SGFCallingFailureException(e.getMessage(),e);
		} catch (SGFException e) {
			throw new SGFCallingFailureException(e.getMessage(),e);
		}
	}
	
	/**
	 * Mapping Service Group data model from NGP to SGF
	 * @param sg
	 * @return
	 */
	private ServiceGroupServiceExStub.ServiceGroup mappingServiceGroupVO(SGFSG sg){
		ServiceGroupServiceExStub.ServiceGroup result = null;
		if(sg != null)
			result = new ServiceGroupServiceExStub.ServiceGroup();
		
		log.debug(sg.toString());
		
		ServiceGroupServiceExStub.LoginCredentials login = new ServiceGroupServiceExStub.LoginCredentials();
		login.setPassword(sg.getPassword());
		login.setUsername(sg.getUsername());
		
		ServiceGroupServiceExStub.Duration duration = null;
		if(sg.getStartDate() != null && sg.getEndDate() != null){
			duration = new ServiceGroupServiceExStub.Duration();
			duration.setEndDate(sg.getEndDate());
			duration.setStartDate(sg.getStartDate());
		}
		
		ServiceGroupServiceExStub.SourceIPAddresses sourceIP = null;
		if(sg.getSourceIPAddresses() != null && sg.getSourceIPAddresses().size() > 0){
			sourceIP = new ServiceGroupServiceExStub.SourceIPAddresses();
			sourceIP.setIp(new String[sg.getSourceIPAddresses().size()]);
			for(int i = 0; i < sg.getSourceIPAddresses().size(); i++){
				sourceIP.getIp()[i] = sg.getSourceIPAddresses().get(i);
			}
		}
		
		ServiceGroupServiceExStub.WhiteOrBlackList wab = null;
		if(sg.getWhiteOrBlackList() != null){
			wab = new ServiceGroupServiceExStub.WhiteOrBlackList();
			wab.setMode(new ServiceGroupServiceExStub.Mode(sg.getWhiteOrBlackList().getMode(),true));
			if(sg.getWhiteOrBlackList().getMsisdns() != null && sg.getWhiteOrBlackList().getMsisdns().size() > 0){
				ServiceGroupServiceExStub.MSISDNs msisdns = new ServiceGroupServiceExStub.MSISDNs();
				msisdns.setMsisdn(new String[sg.getWhiteOrBlackList().getMsisdns().size()]);
				for(int i = 0; i < sg.getWhiteOrBlackList().getMsisdns().size(); i++){
					msisdns.getMsisdn()[i] = sg.getWhiteOrBlackList().getMsisdns().get(i);
				}
				wab.setMsisdns(msisdns);
			}
		}
		
		ServiceGroupServiceExStub.Services services = null;
		if(sg.getServices() != null && sg.getServices().size() > 0){
			services = new ServiceGroupServiceExStub.Services();
			services.setService(new ServiceGroupServiceExStub.Service[sg.getServices().size()]);
			for(int i = 0; i < sg.getServices().size(); i++){
				ServiceGroupServiceExStub.Service service = new ServiceGroupServiceExStub.Service();
				service.setId(sg.getServices().get(i).getServiceVO().getId());
				//ServiceGroupServiceExStub.InboundValue inboundValue = new ServiceGroupServiceExStub.InboundValue(sg.getServices().get(i).getInboundSecurity(),true);
				ServiceGroupServiceExStub.InboundValue inboundValue = new ServiceGroupServiceExStub.InboundValue(this.inboundSecurity,true);
				ServiceGroupServiceExStub.InboundSecurity inboundSecurity = new ServiceGroupServiceExStub.InboundSecurity();
				inboundSecurity.setValue(inboundValue);
				service.setInboundSecurity(inboundSecurity);
				services.getService()[i] = service;
			}
		}
		
		ServiceGroupServiceExStub.WLNG wlng = null;
		if(sg.getWlngDetails() != null){
			wlng = new ServiceGroupServiceExStub.WLNG();
			ServiceGroupServiceExStub.LoginCredentials login2 = new ServiceGroupServiceExStub.LoginCredentials();
			login2.setPassword(sg.getWlngDetails().getPassword());
			login2.setUsername(sg.getWlngDetails().getUsername());
			wlng.setLoginCredentials(login2);
			//wlng.setApplicationType(sg.getWlngDetails().getApplicationType());
			wlng.setApplicationType(this.applicationType);
			wlng.setMailboxId(sg.getWlngDetails().getMailboxId());
			wlng.setMailboxPassword(sg.getWlngDetails().getMailboxPassword());
			wlng.setPrefix(sg.getWlngDetails().getPrefix());
			wlng.setShortCode(sg.getWlngDetails().getShortCode());
		}
				
		result.setNewName(sg.getName());
		result.setOldName(sg.getName());
		result.setPartner(sg.getPartner());
		result.setLoginInfo(login);
		result.setDescription(sg.getDescription());
		result.setDuration(duration);
		result.setSourceIPAddresses(sourceIP);
		result.setWhiteOrBlackList(wab);
		result.setServices(services);
		result.setCoSCategory(sg.getCoSCategory());
		//result.setAccessMode(new ServiceGroupServiceExStub.AccessMode(sg.getAccessMode(),true));
		result.setAccessMode(new ServiceGroupServiceExStub.AccessMode(this.accessMode,true));
		result.setWlngDetails(wlng);
		return result;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		SGFServiceGroupExAdapter adapter = new SGFServiceGroupExAdapter();
		SGFSG sg = new SGFSG();
		List<SGFSG.Service> services = new ArrayList<SGFSG.Service>();
		SGFSG.Service service = sg.new Service();
		SGFService vo = new SGFService();
		vo.setId("1bde219-122c45ee325-1615713278");
		service.setServiceVO(vo);
		service.setInboundSecurity("BASIC");//Fixed
		services.add(service);
		
		SGFSG.WlngDetails wlng = sg.new WlngDetails();
		wlng.setApplicationType("apptype");
		wlng.setUsername("testAI080803");//unique
		wlng.setPassword("testAI080803");//unique
		
		sg.setName("testSG080803");//unique
		sg.setPartner("Web20TestPartner");//Portal must have the partner
		sg.setUsername("testSG080803");//unique
		sg.setPassword("password");
		sg.setServices(services);
		sg.setAccessMode("Commercial");//enum
		sg.setWlngDetails(wlng);
		try {
			//adapter.registerSGFServiceGroup(sg);
			//adapter.modifySGFServiceGroup(sg);
			adapter.deleteSGFServiceGroup(sg, "no reason");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
