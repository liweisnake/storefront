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
import com.hp.sdf.ngp.sdp.model.SGFOperation;

@Component
public class SGFFlexReportsAdapter {
	
	public final static Log log = LogFactory.getLog(SGFFlexReportsAdapter.class);
	
	private String urlFlexReports;
	
	public String getUrlFlexReports() {
		return urlFlexReports;
	}

	@Value("SGF.WS.url.FlexReports")
	public void setUrlFlexReports(String urlFlexReports) {
		this.urlFlexReports = urlFlexReports;
	}

	/**
	 * Register SGF Partner
	 * @param partner
	 */
	public List<SGFOperation> getOperationsPerService(String serviceID) throws SGFCallingFailureException{
		log.debug("Getting Operations of Service: " + serviceID + ". Endpoint: " + urlFlexReports);
		FlexReportsServiceStub stub;
		try {
			stub = new FlexReportsServiceStub(urlFlexReports);
			FlexReportsServiceStub.GetOperationsPerServiceRequest request = new FlexReportsServiceStub.GetOperationsPerServiceRequest();
			request.setServiceId(serviceID);
			FlexReportsServiceStub.GetOperationsPerServiceRequestE requestE = new FlexReportsServiceStub.GetOperationsPerServiceRequestE();
			requestE.setGetOperationsPerServiceRequest(request);
			FlexReportsServiceStub.GetOperationsPerServiceResponseE responseE = stub.getOperationsPerService(requestE);
			String[] operations = responseE.getGetOperationsPerServiceResponse().getOperations();
			List<SGFOperation> result = null;
			if(operations != null && operations.length > 0){
				result = new ArrayList<SGFOperation>();
				for(int i=0; i<operations.length; i++){
					SGFOperation op = new SGFOperation();
					op.setName(operations[i]);
					result.add(op);
				}
			}
			log.debug("Getting Operations of Service: " + serviceID + " successfully.");
			return result;
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
	 * @param args
	 */
	public static void main(String[] args) {
		SGFFlexReportsAdapter adapter = new SGFFlexReportsAdapter();

		try {
			List<SGFOperation> result = adapter.getOperationsPerService("1bde219-122c45ee325-1615713278");
			System.out.println(result.size());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

}
