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
package com.hp.sdf.ngp.service;

import java.util.List;

import com.hp.sdf.ngp.model.ApiKey;
import com.hp.sdf.ngp.model.Operation;
import com.hp.sdf.ngp.model.Service;

public interface ApiService {

	/**
	 * Save service
	 * 
	 * @param service
	 */
	public void saveService(Service service);
	
	/**
	 * Update service
	 * @param service
	 */
	public void updateService(Service service);

	/**
	 * Find services by service
	 * 
	 * @param service
	 *            example will be find by this example
	 * @return service list
	 */
	public Service findServiceById(Long id, int start, int max);

	public List<Service> findServiceListBySGFId(String serviceId, int start, int max);
	
	/**
	 * Find services based on service name
	 * 
	 * @param name
	 *            sevice name
	 * @param start
	 *            start position of first result
	 * @param max
	 *            max result count
	 * @return
	 */
	public List<Service> findServiceListByName(String name, int start, int max);

	/**
	 * Delete service
	 * 
	 * @param service
	 *            service object will be delete
	 */
	public void deleteService(Long serviceId);

	/**
	 * Find service operation by serviceId and operation name
	 * 
	 * @param serviceId
	 *            serviceId which indicate service associate with operation
	 * @param opName
	 *            operation name
	 * @return operation list
	 */
	public List<Operation> findServiceOperation(Long serviceId, String opName);

	/**
	 * Save service operation
	 * 
	 * @param operation
	 *            operation will be saved
	 */
	public void saveServiceOperation(Operation operation);

	/**
	 * Get all services from service table by userId
	 * 
	 * @param userId
	 *            userId of subscribe service
	 * @return service list
	 */
	public List<Service> getServicesByUserId(String userId);
	
	/**
	 * If the service has been subscribed
	 * @param serviceId
	 * @return
	 */
	public boolean isServiceSubscribed(Long serviceId);
	
	/**
	 * Get all AppKey from appkey table by userId
	 * @param userId
	 * @return appkey list
	 */
	public List<ApiKey> getApiKeyByUserId(String userId);

	/**
	 * Get all services from service table.
	 * 
	 * @return service list
	 */
	public List<Service> getAllService(int start, int max);
	
	/**
	 * Get SGF services from service table
	 * @param start
	 * @param max
	 * @return
	 */
	public List<Service> getSgfService(int start, int max);
	
	/**
	 * Get Common services from service table
	 * @param start
	 * @param max
	 * @return
	 */
	public List<Service> getCommonService(int start, int max);
	
	/**
	 * Get SGF services count;
	 * @return
	 */
	public long getSgfServiceCount();
	
	/**
	 * Get Common services count;
	 * @return
	 */
	public long getCommonServiceCount();
		
	public int getAllServicePageCount();

}

// $Id$