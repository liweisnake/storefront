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
package com.hp.sdf.ngp.sdp.model;

import java.util.List;

public class SGFService extends SGFBase {
	
	/**
	 * Service Provider
	 */
	private String serviceProvider;
	
	/**
	 * Version
	 */
	private String version;
	
	/**
	 * Service Indication
	 */
	private String serviceIndication;
	
	/**
	 * Traffic Type
	 */
	private String trafficType;
	
	/**
	 * Access Interface
	 */
	private String accessInterface;
	
	/**
	 * Outbound Security
	 */
	private OutboundSecurity outboundSecurity;
	
	/**
	 * Service Category
	 */
	private String serviceCategory;
	
	/**
	 * Keywords
	 */
	private String keywords;
	
	/**
	 * Enable Name
	 */
	private String enablerName;
	
	/**
	 * Broker Services List
	 */
	private List<BrokerService> brokerServices;
	
	/**
	 * Associated Service Discovery Groups
	 */
	private List<SGFSDG> serviceDiscoveryGroups;
	
	/**
	 * Associated Service Groups
	 */
	private List<SGFSG> serviceGroups;
	
	/**
	 * Operations
	 */
	private List<SGFOperation> operations;
	
	public class OutboundSecurity{		
		private String authentication;		
		private String transport;
		public String getAuthentication() {
			return authentication;
		}
		public void setAuthentication(String authentication) {
			this.authentication = authentication;
		}
		public String getTransport() {
			return transport;
		}
		public void setTransport(String transport) {
			this.transport = transport;
		}		
	}	
		
	public class BrokerService{		
		private String brokeredServiceName;		
		private String url;		
		private String authType;
		public String getBrokeredServiceName() {
			return brokeredServiceName;
		}
		public void setBrokeredServiceName(String brokeredServiceName) {
			this.brokeredServiceName = brokeredServiceName;
		}
		public String getUrl() {
			return url;
		}
		public void setUrl(String url) {
			this.url = url;
		}
		public String getAuthType() {
			return authType;
		}
		public void setAuthType(String authType) {
			this.authType = authType;
		}
	}

	public List<SGFSDG> getServiceDiscoveryGroups() {
		return serviceDiscoveryGroups;
	}

	public List<SGFSG> getServiceGroups() {
		return serviceGroups;
	}

	public String getVersion() {
		return version;
	}

	public void setServiceDiscoveryGroups(List<SGFSDG> serviceDiscoveryGroups) {
		this.serviceDiscoveryGroups = serviceDiscoveryGroups;
	}

	public void setServiceGroups(List<SGFSG> serviceGroups) {
		this.serviceGroups = serviceGroups;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getServiceIndication() {
		return serviceIndication;
	}

	public void setServiceIndication(String serviceIndication) {
		this.serviceIndication = serviceIndication;
	}

	public String getServiceProvider() {
		return serviceProvider;
	}

	public void setServiceProvider(String serviceProvider) {
		this.serviceProvider = serviceProvider;
	}

	public String getTrafficType() {
		return trafficType;
	}

	public void setTrafficType(String trafficType) {
		this.trafficType = trafficType;
	}

	public String getAccessInterface() {
		return accessInterface;
	}

	public void setAccessInterface(String accessInterface) {
		this.accessInterface = accessInterface;
	}

	public OutboundSecurity getOutboundSecurity() {
		return outboundSecurity;
	}

	public void setOutboundSecurity(OutboundSecurity outboundSecurity) {
		this.outboundSecurity = outboundSecurity;
	}

	public String getServiceCategory() {
		return serviceCategory;
	}

	public void setServiceCategory(String serviceCategory) {
		this.serviceCategory = serviceCategory;
	}

	public String getKeywords() {
		return keywords;
	}

	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}

	public String getEnablerName() {
		return enablerName;
	}

	public void setEnablerName(String enablerName) {
		this.enablerName = enablerName;
	}

	public List<BrokerService> getBrokerServices() {
		return brokerServices;
	}

	public void setBrokerServices(List<BrokerService> brokerServices) {
		this.brokerServices = brokerServices;
	}

	public List<SGFOperation> getOperations() {
		return operations;
	}

	public void setOperations(List<SGFOperation> operations) {
		this.operations = operations;
	}

}
