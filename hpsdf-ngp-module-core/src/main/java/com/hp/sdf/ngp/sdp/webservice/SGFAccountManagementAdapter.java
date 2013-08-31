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

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import org.apache.axis2.AxisFault;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

import com.hp.sdf.ngp.common.annotation.Value;
import com.hp.sdf.ngp.common.exception.SGFCallingFailureException;
import com.hp.sdf.ngp.sdp.model.SGFAccount;

@Component
public class SGFAccountManagementAdapter {
	
	public final static Log log = LogFactory.getLog(SGFAccountManagementAdapter.class);
	
	private String urlAccountMgmt;
	private String balanceType;
	private String balanceAmount;
	private String updatedBy;
	
	public String getBalanceType() {
		return balanceType;
	}

	@Value("SGF.Account.BalanceType")
	public void setBalanceType(String balanceType) {
		this.balanceType = balanceType;
	}

	public String getBalanceAmount() {
		return balanceAmount;
	}

	@Value("SGF.Account.BalanceAmount")
	public void setBalanceAmount(String balanceAmount) {
		this.balanceAmount = balanceAmount;
	}

	public String getUpdatedBy() {
		return updatedBy;
	}

	@Value("SGF.Account.UpdatedBy")
	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public String getUrlAccountMgmt() {
		return urlAccountMgmt;
	}

	@Value("SGF.WS.url.AccountManagement")
	public void setUrlAccountMgmt(String urlAccountMgmt) {
		this.urlAccountMgmt = urlAccountMgmt;
	}

	/**
	 * Create Account in SGF
	 * @param vo
	 * @throws SGFCallingFailureException
	 */
	public void createAccount(SGFAccount vo) throws SGFCallingFailureException{
		log.debug("Create account: " + vo.getPartnerID() + ". Endpoint: " + urlAccountMgmt);
		AccountManagementServiceStub stub;
		try {
			stub = new AccountManagementServiceStub(urlAccountMgmt);
			AccountManagementServiceStub.CreateAccount request = new AccountManagementServiceStub.CreateAccount();
			AccountManagementServiceStub.Account account = new AccountManagementServiceStub.Account();
			AccountManagementServiceStub.Partner partner = new AccountManagementServiceStub.Partner();
			partner.setPartnerId(vo.getPartnerID());
			partner.setPartnerName(vo.getPartnerName());
			account.setPartner(partner);
			account.setValidFrom(vo.getVaildFrom());
			AccountManagementServiceStub.BalanceExpireDetails bed = new AccountManagementServiceStub.BalanceExpireDetails();
			//bed.setBalanceType(vo.getBalanceType());
			bed.setBalanceType(this.balanceType);
			bed.setDate(vo.getExpireDate());
			account.setBalanceExpireDetails(bed);
			AccountManagementServiceStub.Balance balance = new AccountManagementServiceStub.Balance();
			//balance.setBalanceType(vo.getBalanceType());
			balance.setBalanceType(this.balanceType);
			//balance.setAmount(vo.getBalanceAmount());
			balance.setAmount(new BigDecimal(this.balanceAmount));
			account.setBalance(balance);
			//account.setUpdatedBy(vo.getUpdatedBy());
			account.setUpdatedBy(this.updatedBy);
			account.setLastUpdated(vo.getLastUpdated());			
			request.setAccount(account);
			AccountManagementServiceStub.CreateAccountE requestE = new AccountManagementServiceStub.CreateAccountE();
			requestE.setCreateAccount(request);
			AccountManagementServiceStub.CreateAccountResponseE responseE = stub.createAccount(requestE);
			log.debug("Create account: " + vo.getPartnerID() + " successfully.");
		} catch (ServiceException e) {
			throw SGFErrorHandler.handleError(e);
		} catch (AxisFault e) {
			if(e.getDetail() != null)
				log.error(e.getDetail().toString());
			throw SGFErrorHandler.handleError(e);
		} catch (RemoteException e) {
			throw new SGFCallingFailureException(e.getMessage(),e);
		} 
	}
	
	/**
	 * Get Account details in SGF
	 * @param partner
	 * @return
	 * @throws SGFCallingFailureException
	 */
	public SGFAccount getAccount(String partner) throws SGFCallingFailureException{
		log.debug("Get account details: " + partner + ". Endpoint: " + urlAccountMgmt);
		SGFAccount result = null;
		AccountManagementServiceStub stub;
		try {
			stub = new AccountManagementServiceStub(urlAccountMgmt);
			AccountManagementServiceStub.ListAccount request = new AccountManagementServiceStub.ListAccount();
			request.setPartnerId(partner);
			request.setPartnerName(partner);
			AccountManagementServiceStub.ListAccountE requestE = new AccountManagementServiceStub.ListAccountE();
			requestE.setListAccount(request);
			AccountManagementServiceStub.ListAccountResponseE responseE = stub.listAccount(requestE);
			AccountManagementServiceStub.Account account = responseE.getListAccountResponse().getAccount()[0];
			if(account == null || account.getPartner() == null || account.getPartner().getPartnerId() == null || account.getPartner().getPartnerId().length() == 0)
				return null;
			result = new SGFAccount();
			result.setPartnerID(partner);
			result.setPartnerName(partner);
			result.setVaildFrom(account.getValidFrom());
			result.setExpireDate(account.getBalanceExpireDetails().getDate());
			AccountManagementServiceStub.DatedTransaction[] dts = account.getDatedTransaction();
			if(dts != null && dts.length > 0){
				Map<Calendar, String> map = new HashMap<Calendar, String>();
				for(AccountManagementServiceStub.DatedTransaction dt: dts){
					map.put(dt.getTransactionDate(), dt.getTransactionDetails());
				}
				result.setDatedTransaction(map);
			}
			result.setBalanceAmount(account.getBalance().getAmount());
			result.setBalanceType(account.getBalance().getBalanceType());
			result.setUpdatedBy(account.getUpdatedBy());
			result.setLastUpdated(account.getLastUpdated());
		} catch (AxisFault e) {
			if(e.getDetail() != null)
				log.error(e.getDetail().toString());
			throw new SGFCallingFailureException(e.getMessage(),e);
		} catch (RemoteException e) {
			throw new SGFCallingFailureException(e.getMessage(),e);
		} catch (ServiceException e) {
			throw new SGFCallingFailureException(e.getMessage(),e);
		}
		return result;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		SGFAccountManagementAdapter adapter = new SGFAccountManagementAdapter();

		try {
			SGFAccount vo = new SGFAccount();
			vo.setPartnerID("testPartner080801");
			vo.setPartnerName("testPartner080801");
			vo.setVaildFrom(Calendar.getInstance());
			vo.setBalanceType("WEB2.0");
			vo.setExpireDate(Calendar.getInstance());
			vo.setBalanceAmount(new BigDecimal("200"));
			vo.setUpdatedBy("admin");
			adapter.createAccount(vo);
			System.out.println("ok");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

}
