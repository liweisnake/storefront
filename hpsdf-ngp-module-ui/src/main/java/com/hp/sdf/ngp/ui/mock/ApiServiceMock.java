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
package com.hp.sdf.ngp.ui.mock;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.hp.sdf.ngp.model.ApiKey;
import com.hp.sdf.ngp.model.Operation;
import com.hp.sdf.ngp.model.Service;
import com.hp.sdf.ngp.model.ServiceSubscription;
import com.hp.sdf.ngp.service.ApiService;

public class ApiServiceMock implements ApiService {

	public void deleteService(Long serviceId) {
		// TODO Auto-generated method stub

	}

	public Service findServiceListById(Long serviceId, int start, int max) {
		// TODO Auto-generated method stub
		return null;
	}

	public int findServiceListByIdPageCount(Long serviceId) {
		// TODO Auto-generated method stub
		return 0;
	}

	public List<Service> findServiceListByName(String name, int start, int max) {
		// TODO Auto-generated method stub
		return null;
	}

	public int findServiceListByNamePageCount(String name) {
		// TODO Auto-generated method stub
		return 0;
	}

	public List<Operation> findServiceOperation(Long serviceId, String opName) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<Service> getAllService(int start, int max) {
		List<Service> list = new ArrayList<Service>();
		Service s1 = new Service();
		s1.setId(1l);
		s1.setServiceid("Mock Service ID1");
		s1.setName("Mock Service 1's name");
		s1.setAccessInterface("webservice");
		s1.setBrokerServiceAuthType("BASIC");
		s1.setBrokerServiceUrl("http://sgfwlng4.chn.hp.com:9032/LB_WLNG_NT_SendSms_0_1v0_WS/SendSmsBinding");
		s1.setDescription("Mock Service 1's description");
		Service s2 = new Service();
		s2.setId(2l);
		s2.setServiceid("Mock Service ID2");
		s2.setName("Mock Service 2's name");
		s2.setAccessInterface("httpService ");
		s2.setBrokerServiceAuthType("BASIC");
		s2.setBrokerServiceUrl("http://usb-vm7.chn.hp.com:6090/rest/LB_WLNG_NT_ReceiveSms/1v0/ES/0/getReceivedSms");
		s2.setDescription("Mock Service 2's description");
		list.add(s1);
		list.add(s2);
		return list;
	}

	public int getAllServicePageCount() {
		return 2;
	}

	public List<Service> getServicesByUserId(String userId) {
		// TODO Auto-generated method stub
		return null;
	}

	public void saveService(Service service) {
		// TODO Auto-generated method stub

	}

	public void saveServiceOperation(Operation operation) {
		// TODO Auto-generated method stub

	}

	public List<ApiKey> getAppkeyByUserId(String userId) {
		List<ApiKey> appkeys = new ArrayList<ApiKey>();
		ApiKey appkey = new ApiKey();
		appkey.setId(1l);
		appkey.setSgName("Mock SG name");
		appkey.setSgPassword("Mock SG Password");
		ServiceSubscription ss = new ServiceSubscription();
		Service s1 = new Service();
		s1.setName("Mock Service name 1");
		s1.setBrokerServiceUrl("http://www.google.com");
		ss.setService(s1);
		ServiceSubscription ss2 = new ServiceSubscription();
		Service s2 = new Service();
		s2.setName("Mock Service name 2");
		s2.setBrokerServiceUrl("http://www.baidu.com");
		ss2.setService(s2);
		Set<ServiceSubscription> sset = new HashSet<ServiceSubscription>();
		sset.add(ss);
		sset.add(ss2);
		appkey.setServiceSubscriptions(sset);
		appkeys.add(appkey);
		return appkeys;
	}

	public List<Service> findServiceListBySGFId(String serviceId, int start, int max) {
		// TODO Auto-generated method stub
		return null;
	}

	public void updateService(Service service) {
		// TODO Auto-generated method stub

	}

	public List<Service> getCommonService(int start, int max) {
		// TODO Auto-generated method stub
		return null;
	}

	public long getCommonServiceCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	public List<Service> getSgfService(int start, int max) {
		// TODO Auto-generated method stub
		return null;
	}

	public long getSgfServiceCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	public boolean isServiceSubscribed(Long serviceId) {
		// TODO Auto-generated method stub
		return false;
	}

	public List<ApiKey> getApiKeyByUserId(String userId) {
		// TODO Auto-generated method stub
		return null;
	}

	public Service findServiceById(Long id, int start, int max) {
		// TODO Auto-generated method stub
		return null;
	}

}

// $Id$