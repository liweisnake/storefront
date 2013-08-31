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
package com.hp.sdf.ngp.service.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.hp.sdf.ngp.api.search.SearchExpression;
import com.hp.sdf.ngp.api.search.Condition.NumberComparer;
import com.hp.sdf.ngp.api.search.Condition.StringComparer;
import com.hp.sdf.ngp.dao.OperationDAO;
import com.hp.sdf.ngp.dao.ServiceDAO;
import com.hp.sdf.ngp.dao.ServiceSubscriptionDAO;
import com.hp.sdf.ngp.model.ApiKey;
import com.hp.sdf.ngp.model.Operation;
import com.hp.sdf.ngp.model.Service;
import com.hp.sdf.ngp.model.ServiceSubscription;
import com.hp.sdf.ngp.search.condition.operation.OperationNameCondition;
import com.hp.sdf.ngp.search.condition.operation.OperationServiceIdCondition;
import com.hp.sdf.ngp.search.condition.service.ServiceNameCondition;
import com.hp.sdf.ngp.search.condition.service.ServiceServiceidCondition;
import com.hp.sdf.ngp.search.condition.service.ServiceTypeCondition;
import com.hp.sdf.ngp.search.engine.SearchExpressionImpl;
import com.hp.sdf.ngp.service.ApiService;

/**
 * 
 *
 */
@Component
@Transactional
public class ApiServiceImpl implements ApiService {

	@Resource
	private ServiceDAO serviceDao;

	@Resource
	private OperationDAO operationDao;

	@Resource
	private ServiceSubscriptionDAO serviceSubDao;

	public ServiceDAO getServiceDao() {
		return serviceDao;
	}

	public void setServiceDao(ServiceDAO serviceDao) {
		this.serviceDao = serviceDao;
	}

	public OperationDAO getOperationDao() {
		return operationDao;
	}

	public void setOperationDao(OperationDAO operationDao) {
		this.operationDao = operationDao;
	}

	public ServiceSubscriptionDAO getServiceSubDao() {
		return serviceSubDao;
	}

	public void setServiceSubDao(ServiceSubscriptionDAO serviceSubDao) {
		this.serviceSubDao = serviceSubDao;
	}

	// main methods

	public void deleteService(Long serviceId) {
		Service service = serviceDao.findById(serviceId);
		// for(ServiceSubscription s : service.getServiceSubscriptions()){
		// System.out.println(s.getId());
		// }

		// for (ServiceSubscription ss : service.getServiceSubscriptions()) {
		// serviceSubDao.remove(ss);
		// }
		// service.setServiceSubscriptions(new HashSet());
		// serviceDao.merge(service);
		serviceDao.remove(service);
	}

	public Service findServiceById(Long Id, int start, int max) {
		return serviceDao.findById(Id);
	}

	public List<Service> findServiceListBySGFId(String serviceId, int start,
			int max) {
		SearchExpression expression=new SearchExpressionImpl();
		expression.setFirst(start);
		expression.setMax(max);
		expression.addCondition(new ServiceServiceidCondition(serviceId, StringComparer.EQUAL, false, false));
		return serviceDao.search(expression);
	}

	public List<Service> findServiceListByName(String name, int start, int max) {
		SearchExpression expression=new SearchExpressionImpl();
		expression.setFirst(start);
		expression.setMax(max);
		expression.addCondition(new ServiceNameCondition(name, StringComparer.EQUAL, false, false));
		return serviceDao.search(expression);
	}

	public List<Operation> findServiceOperation(Long serviceId, String opName) {
		SearchExpression expression=new SearchExpressionImpl();
		expression.addCondition(new OperationServiceIdCondition(serviceId, NumberComparer.EQUAL));
		expression.addCondition(new OperationNameCondition(opName, StringComparer.EQUAL, false, false));
		return operationDao.search(expression);
	}

	public List<Service> getAllService(int start, int max) {
		return serviceDao.getAll(start, max);
	}

	public List<Service> getSgfService(int start, int max) {
		SearchExpression expression=new SearchExpressionImpl();
		expression.setFirst(start);
		expression.setMax(max);
		expression.addCondition(new ServiceTypeCondition(Service.SERVICE_TYPE_SGF, StringComparer.EQUAL, false, false));
		return serviceDao.search(expression);
	}

	public long getSgfServiceCount() {
		return serviceDao.findByCount("type", Service.SERVICE_TYPE_SGF);
	}

	public List<Service> getCommonService(int start, int max) {
		SearchExpression expression=new SearchExpressionImpl();
		expression.setFirst(start);
		expression.setMax(max);
		expression.addCondition(new ServiceTypeCondition(Service.SERVICE_TYPE_COMMON, StringComparer.EQUAL, false, false));
		return serviceDao.search(expression);
	}

	public long getCommonServiceCount() {
		return serviceDao.findByCount("type", Service.SERVICE_TYPE_COMMON);
	}

	public List<Service> getServicesByUserId(String userId) {
		List<Service> services = new ArrayList<Service>();
		List<ServiceSubscription> result = this.serviceSubDao.findBy(
				"userid", userId);
		if (result == null) {
			return services;
		}
		for (ServiceSubscription ss : result) {
			services.add(ss.getService());
		}
		return services;
	}
	
	public boolean isServiceSubscribed(Long serviceId){
		List<ServiceSubscription> result = this.serviceSubDao.findBy(
				"service.id", serviceId);
		if(result != null && result.size() > 0)
			return true;
		return false;
	}

	public List<ApiKey> getApiKeyByUserId(String userId) {
		Set<ApiKey> apikeys = new HashSet<ApiKey>();
		List<ServiceSubscription> result = this.serviceSubDao.findBy(
				"userid", userId);
		if (result != null) {
			for (ServiceSubscription ss : result) {
				apikeys.add(ss.getApiKeyRelation());
			}
		}

		return new ArrayList<ApiKey>(apikeys);
	}

	public void saveService(Service service) {
		serviceDao.persist(service);
	}

	public void updateService(Service service) {
		serviceDao.merge(service);
	}

	public void saveServiceOperation(Operation operation) {
		// serviceDao.persist(operation.getService());
		operationDao.persist(operation);
	}


	public int getAllServicePageCount() {
		return (int) this.serviceDao.getAllCount();
	}

}
