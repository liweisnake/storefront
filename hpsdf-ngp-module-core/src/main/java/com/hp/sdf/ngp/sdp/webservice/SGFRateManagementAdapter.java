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
public class SGFRateManagementAdapter {
	
	public final static Log log = LogFactory.getLog(SGFRateManagementAdapter.class);
	
	private String urlRateMgmt;
	
	public String getUrlRateMgmt() {
		return urlRateMgmt;
	}

	@Value("SGF.WS.url.RateManagement")
	public void setUrlRateMgmt(String urlRateMgmt) {
		this.urlRateMgmt = urlRateMgmt;
	}

	/**
	 * Listing SGF Service Operation Rate
	 * @return
	 * @throws SGFCallingFailureException
	 */
	public List<SGFOperation> listOperationRate() throws SGFCallingFailureException{
		log.debug("Listing SGF Service Operation Rate. Endpoint: " + urlRateMgmt);
		RateManagementServiceStub stub;
		try {
			stub = new RateManagementServiceStub(urlRateMgmt);
			RateManagementServiceStub.ListServiceOperationRate request = new RateManagementServiceStub.ListServiceOperationRate();
			request.setInput(new RateManagementServiceStub.Rate());
			RateManagementServiceStub.ListServiceOperationRateE requestE = new RateManagementServiceStub.ListServiceOperationRateE();
			requestE.setListServiceOperationRate(request);
			RateManagementServiceStub.ListServiceOperationRateResponseE responseE = stub.listServiceOperationRate(requestE);
			RateManagementServiceStub.Rate[] rates = responseE.getListServiceOperationRateResponse().getRate();
			List<SGFOperation> result = null;
			if(rates != null && rates.length > 0){
				result = new ArrayList<SGFOperation>();
				for(int i=0; i<rates.length; i++){
					SGFOperation op = new SGFOperation();
					op.setServiceID(rates[i].getServiceOperation().getServiceId());
					op.setName(rates[i].getServiceOperation().getOperation());
					op.setRate(rates[i].getServiceOperation().getRateUnits().toString());
					result.add(op);
				}
			}
			log.debug("Listing SGF Service Operation Rate successfully.");
			return result;
		} catch (AxisFault e) {
			if(e.getDetail() != null)
				log.error(e.getDetail().toString());
			throw new SGFCallingFailureException(e.getMessage(),e);
		} catch (RemoteException e) {
			throw new SGFCallingFailureException(e.getMessage(),e);
		} catch (ServiceException e) {
			throw new SGFCallingFailureException(e.getMessage(),e);
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		SGFRateManagementAdapter adapter = new SGFRateManagementAdapter();

		try {
			List<SGFOperation> result = adapter.listOperationRate();
			System.out.println(result.size());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

}
