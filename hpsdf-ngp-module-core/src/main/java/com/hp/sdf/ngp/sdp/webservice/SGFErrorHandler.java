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

import org.apache.axis2.AxisFault;

import com.hp.sdf.ngp.common.exception.SGFCallingFailureException;

public class SGFErrorHandler {

	public static SGFCallingFailureException handleError(Exception e){
		String faultCode = "";
		String faultString = "";
		String detail = "";
		
		if(e instanceof AxisFault){
			if(((AxisFault)e).getFaultCode() != null)
				faultCode = ((AxisFault)e).getFaultCode().toString();
			if(((AxisFault)e).getMessage() != null)
				faultString = ((AxisFault)e).getMessage();
			if(((AxisFault)e).getDetail() != null)
				detail = ((AxisFault)e).getDetail().toString();
		}
		if(e instanceof SGFException){
			detail = ((SGFException)e).getFaultMessage().getSGFException().getStatus().getDetails();
		}
		if(e instanceof ServiceException){
			detail = ((ServiceException)e).getFaultMessage().getServiceException().getVariables()[0];
		}
		
		String message = e.getMessage();
		if(faultString.contains("IllegalArgumentException"))
			message = SGFCallingFailureException.SGF_PARTNER_ILLEGAL_ARGUMENT;
		else if(detail.contains("USER_EXISTS"))
			message = SGFCallingFailureException.SGF_PARTNER_EXISTS;
		else if(detail.contains("ServiceDiscoveryGroup not existed"))
			message = SGFCallingFailureException.SGF_SERVICE_DISCOVERY_GROUP_NOT_EXSIT;
		else if(detail.contains("Service does not exist"))
			message = SGFCallingFailureException.SGF_SERVICE_NOT_EXSIT;
		else if(detail.contains("Service Group Name already exists"))
			message = SGFCallingFailureException.SGF_SERVICE_GROUP_EXISTS;
		else if(detail.contains("Unknown Error Occured"))
			message = SGFCallingFailureException.SGF_SERVICE_NOT_EXSIT;
		else if(detail.contains("203 - partnerId already exists"))
			message = SGFCallingFailureException.SGF_ACCOUNT_EXISTS;
		
		SGFCallingFailureException result = new SGFCallingFailureException(message,e);
		return result;
	}
	
}

// $Id$