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

public class SGFSG extends SGFBase {
	
	/**
	 * Partner
	 */
	private String partner;
	
	/**
	 * Service Group Username
	 */
	private String username;
	
	/**
	 * Service Group Password
	 */
	private String password;
	
	/**
	 * Duration StartDate
	 */
	private String startDate;
	
	/**
	 * Duration EndDate;
	 */
	private String endDate;
	
	/**
	 * Source IP Address
	 */
	private List<String> sourceIPAddresses;
	
	/**
	 * White or Black List
	 */
	private WhiteOrBlackList whiteOrBlackList;
	
	/**
	 * Associated Services
	 */
	private List<Service> services;
	
	/**
	 * CoS Category
	 */
	private String coSCategory;
	
	/**
	 * Access Mode
	 */
	private String accessMode;
	
	/**
	 * WLNG/OCSG Details
	 */
	private WlngDetails wlngDetails;
	
	public class WhiteOrBlackList{
		private String mode;
		private List<String> msisdns;
		public String getMode() {
			return mode;
		}
		public void setMode(String mode) {
			this.mode = mode;
		}
		public List<String> getMsisdns() {
			return msisdns;
		}
		public void setMsisdns(List<String> msisdns) {
			this.msisdns = msisdns;
		}
	}
	
	public class Service{
		private SGFService serviceVO;
		private String inboundSecurity;
		public SGFService getServiceVO() {
			return serviceVO;
		}
		public void setServiceVO(SGFService serviceVO) {
			this.serviceVO = serviceVO;
		}
		public String getInboundSecurity() {
			return inboundSecurity;
		}
		public void setInboundSecurity(String inboundSecurity) {
			this.inboundSecurity = inboundSecurity;
		}
	}
	
	public class WlngDetails{
		private String applicationType;
		private String shortCode;
		private String username;
		private String password;
		private String prefix;
		private String mailboxId;
		private String mailboxPassword;
		public String getApplicationType() {
			return applicationType;
		}
		public void setApplicationType(String applicationType) {
			this.applicationType = applicationType;
		}
		public String getShortCode() {
			return shortCode;
		}
		public void setShortCode(String shortCode) {
			this.shortCode = shortCode;
		}
		public String getUsername() {
			return username;
		}
		public void setUsername(String username) {
			this.username = username;
		}
		public String getPassword() {
			return password;
		}
		public void setPassword(String password) {
			this.password = password;
		}
		public String getPrefix() {
			return prefix;
		}
		public void setPrefix(String prefix) {
			this.prefix = prefix;
		}
		public String getMailboxId() {
			return mailboxId;
		}
		public void setMailboxId(String mailboxId) {
			this.mailboxId = mailboxId;
		}
		public String getMailboxPassword() {
			return mailboxPassword;
		}
		public void setMailboxPassword(String mailboxPassword) {
			this.mailboxPassword = mailboxPassword;
		}
	}
	
	@Override
	public String toString(){
		StringBuffer sb = new StringBuffer();
		sb.append("AccessMode = " + this.accessMode);
		sb.append(", CoSCategory = " + this.coSCategory);
		sb.append(", EndDate = " + this.endDate);
		sb.append(", Partner = " + this.partner);
		sb.append(", Password = " + this.password);
		sb.append(", StartDate = " + this.startDate);
		sb.append(", Service = " + this.services);
		sb.append(", Wlng.AppType = " + this.wlngDetails.applicationType);
		sb.append(", Wlng.Password = " + this.wlngDetails.password);
		sb.append(", Wlng.Prefix = " + this.wlngDetails.prefix);
		sb.append(", Wlng.Username = " + this.wlngDetails.username);
		return sb.toString();
	}

	public String getPartner() {
		return partner;
	}

	public void setPartner(String partner) {
		this.partner = partner;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public List<String> getSourceIPAddresses() {
		return sourceIPAddresses;
	}

	public void setSourceIPAddresses(List<String> sourceIPAddresses) {
		this.sourceIPAddresses = sourceIPAddresses;
	}

	public WhiteOrBlackList getWhiteOrBlackList() {
		return whiteOrBlackList;
	}

	public void setWhiteOrBlackList(WhiteOrBlackList whiteOrBlackList) {
		this.whiteOrBlackList = whiteOrBlackList;
	}

	public List<Service> getServices() {
		return services;
	}

	public void setServices(List<Service> services) {
		this.services = services;
	}

	public String getCoSCategory() {
		return coSCategory;
	}

	public void setCoSCategory(String coSCategory) {
		this.coSCategory = coSCategory;
	}

	public String getAccessMode() {
		return accessMode;
	}

	public void setAccessMode(String accessMode) {
		this.accessMode = accessMode;
	}

	public WlngDetails getWlngDetails() {
		return wlngDetails;
	}

	public void setWlngDetails(WlngDetails wlngDetails) {
		this.wlngDetails = wlngDetails;
	}

}
