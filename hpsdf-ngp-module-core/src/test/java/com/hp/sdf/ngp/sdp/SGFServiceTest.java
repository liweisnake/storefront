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
package com.hp.sdf.ngp.sdp;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.annotation.Resource;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.hp.sdf.ngp.common.exception.SGFCallingFailureException;
import com.hp.sdf.ngp.sdp.model.SGFAccount;
import com.hp.sdf.ngp.sdp.model.SGFPartner;
import com.hp.sdf.ngp.sdp.model.SGFSG;
import com.hp.sdf.ngp.sdp.model.SGFService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:META-INF/applicationContext-test.xml" })
public class SGFServiceTest {

	@Resource
	private SGFWebService sGFWebService;
	
	@Test
	public void testCreatePartner(){
		SGFPartner partner = new SGFPartner();
		SGFPartner.CompanyInfo companyInfo = partner.new CompanyInfo();
		SGFPartner.Address address = partner.new Address();
		address.setCity("Shanghai");
		address.setCountry("China");
		address.setState("Shanghai");
		address.setStreet("Jinke");
		address.setZip("201203");
		companyInfo.setAddress(address);
		companyInfo.setName("eds");
		companyInfo.setUrl("http://www.hp.com");
		
		SGFPartner.ContactDetails contact = partner.new ContactDetails();
		contact.setTitle("Mr.");
		contact.setFirstname("Bruce");
		contact.setLastname("Zhang");
		address = partner.new Address();
		address.setCountry("China");
		contact.setAddress(address);
		contact.setEmail("zhenyu.zhang@hp.com");
		contact.setLanguage("English");
		
		SGFPartner.LoginInfo login = partner.new LoginInfo();
		login.setUsername("testNGPPartner");//unique
		login.setPassword("password");
		login.setPasswordQuestion("Company URL");//enum
		login.setPasswordAnswer("http://www.hp.com");
		
		//List<String> categories = new ArrayList<String>();
		//categories.add("ASP");//enum
		//categories.add("SP");//enum
		//categories.add("CP");//enum
		
		partner.setCompanyInfo(companyInfo);
		partner.setContactDetails(contact);
		partner.setLoginInfo(login);
		//partner.setCategories(categories);
		//partner.setDomainName("DEFAULT");//Fixed
		//partner.setPartnerType("External");//enum
		//partner.setStatus("ENABLED");//Fixed
		
		try {
			this.sGFWebService.createPartner(partner);
		} catch (SGFCallingFailureException e) {
			if(e.getMessage().equalsIgnoreCase(SGFCallingFailureException.SGF_PARTNER_EXISTS)){
				Assert.assertTrue(true);
			}else{
				e.printStackTrace();
				Assert.assertTrue(false);
			}
		}
		Assert.assertTrue(true);
	}
	
	@Test
	public void testGetAllServices(){
		try {
			this.sGFWebService.getAllServices();
		} catch (SGFCallingFailureException e) {
			e.printStackTrace();
			Assert.assertTrue(false);
		}
		Assert.assertTrue(true);
	}
	
	@Test
	public void testGetServicesBySdg(){
		try {
			this.sGFWebService.getServicesBySdg("ngpSDG");
		} catch (SGFCallingFailureException e) {
			if(e.getMessage().equalsIgnoreCase(SGFCallingFailureException.SGF_SERVICE_DISCOVERY_GROUP_NOT_EXSIT)){
				Assert.assertTrue(true);
				return;
			}
			e.printStackTrace();
			Assert.assertTrue(false);
		}
		Assert.assertTrue(true);
	}
	
	@Test
	public void testGetService(){
		try {
			this.sGFWebService.getService("testservice");
		} catch (SGFCallingFailureException e) {
			if(e.getMessage().equalsIgnoreCase(SGFCallingFailureException.SGF_SERVICE_NOT_EXSIT)){
				Assert.assertTrue(true);
				return;
			}
			e.printStackTrace();
			Assert.assertTrue(false);
		}
		Assert.assertTrue(true);
	}
	
	@Test
	public void testCreateServiceGroup(){
		SGFSG sg = new SGFSG();
		List<SGFSG.Service> services = new ArrayList<SGFSG.Service>();
		SGFSG.Service service = sg.new Service();
		SGFService vo = new SGFService();
		vo.setId("1bde219-122c45ee325-1615713278");
		service.setServiceVO(vo);
		//service.setInboundSecurity("BASIC");//Fixed
		services.add(service);
		
		SGFSG.WlngDetails wlng = sg.new WlngDetails();
		//wlng.setApplicationType("apptype");
		wlng.setUsername("testNGPAI");//unique
		wlng.setPassword("testNGPAI");//unique
		
		sg.setName("testNGPSG");//unique
		sg.setPartner("testNGPPartner");//Portal must have the partner
		sg.setUsername("testNGPSG");//unique
		sg.setPassword("password");
		sg.setServices(services);
		//sg.setAccessMode("Commercial");//enum
		sg.setWlngDetails(wlng);
		try {
			this.sGFWebService.createServiceGroup(sg);
		} catch (SGFCallingFailureException e) {
			if(e.getMessage().equalsIgnoreCase(SGFCallingFailureException.SGF_SERVICE_GROUP_EXISTS)){
				Assert.assertTrue(true);
				return;
			}
			e.printStackTrace();
			Assert.assertTrue(false);
		}
		
		Assert.assertTrue(true);
	}
	
	@Test
	public void testGetOperationByService(){
		try {
			this.sGFWebService.getOperationByService("testservice");
		} catch (SGFCallingFailureException e) {
			if(e.getMessage().equalsIgnoreCase(SGFCallingFailureException.SGF_SERVICE_NOT_EXSIT)){
				Assert.assertTrue(true);
				return;
			}
			e.printStackTrace();
			Assert.assertTrue(false);
		}
		Assert.assertTrue(true);
	}
	
	@Test
	public void testListServiceOperationRate(){
		try {
			this.sGFWebService.listServiceOperationRate();
		} catch (SGFCallingFailureException e) {
			e.printStackTrace();
			Assert.assertTrue(false);
		}
		Assert.assertTrue(true);
	}
	
	@Test
	public void testCreateAccount(){
		SGFAccount account = new SGFAccount();
		account.setPartnerID("testNGPPartner");
		account.setPartnerName("testNGPPartner");
		Calendar begin = new GregorianCalendar();
		Calendar end = new GregorianCalendar();
		try {
			Date bd = new SimpleDateFormat("yyyy-MM-dd").parse("1900-01-01");
			Date ed = new SimpleDateFormat("yyyy-MM-dd").parse("2200-01-01");
			begin.setTime(bd);
			end.setTime(ed);
		} catch (ParseException e1) {
			e1.printStackTrace();
		}
		account.setVaildFrom(begin);
		//account.setBalanceType("WEB2.0");
		account.setExpireDate(end);
		//account.setBalanceAmount(new BigDecimal("200"));
		//account.setUpdatedBy("admin");
		try {
			this.sGFWebService.createAccount(account);
		} catch (SGFCallingFailureException e) {
			if(e.getMessage().equalsIgnoreCase(SGFCallingFailureException.SGF_ACCOUNT_EXISTS)){
				Assert.assertTrue(true);
				return;
			}
			e.printStackTrace();
			Assert.assertTrue(false);
		}
		Assert.assertTrue(true);
	}
	
	@Test
	public void testGetAccount(){
		String partner = "testpartner";
		try {
			this.sGFWebService.getAccount(partner);
		} catch (SGFCallingFailureException e) {
			e.printStackTrace();
			Assert.assertTrue(false);
		}
		Assert.assertTrue(true);
	}
	
}

// $Id$