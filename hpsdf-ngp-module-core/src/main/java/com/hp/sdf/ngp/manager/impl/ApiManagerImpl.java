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
package com.hp.sdf.ngp.manager.impl;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.annotation.Resource;
import javax.xml.transform.stream.StreamSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.oxm.xstream.XStreamMarshaller;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.hp.sdf.ngp.cms.CMSMimeMappings;
import com.hp.sdf.ngp.cms.impl.CMSServiceJBossImpl;
import com.hp.sdf.ngp.cms.model.Content;
import com.hp.sdf.ngp.common.annotation.Value;
import com.hp.sdf.ngp.common.exception.BatchUploadFailureException;
import com.hp.sdf.ngp.common.exception.SGFCallingFailureException;
import com.hp.sdf.ngp.manager.ApiManager;
import com.hp.sdf.ngp.model.ApiKey;
import com.hp.sdf.ngp.model.Operation;
import com.hp.sdf.ngp.model.Service;
import com.hp.sdf.ngp.sdp.SGFWebService;
import com.hp.sdf.ngp.sdp.model.SGFOperation;
import com.hp.sdf.ngp.sdp.model.SGFSG;
import com.hp.sdf.ngp.sdp.model.SGFService;
import com.hp.sdf.ngp.service.ApiService;
import com.hp.sdf.ngp.service.ApplicationService;

@Component
public class ApiManagerImpl implements ApiManager {

	private final static Log log = LogFactory.getLog(ApiManagerImpl.class);

	@Resource
	private SGFWebService sGFWebService;

	@Resource
	private ApplicationService appService;

	@Resource
	private ApiService apiService;

	private String sdg;

	private String deleteService;

	public String getDeleteService() {
		return deleteService;
	}

	@Value("SGF.NGP.Service.delete")
	public void setDeleteService(String deleteService) {
		this.deleteService = deleteService;
	}

	public String getSdg() {
		return sdg;
	}

	@Value("SGF.NGP.SDG")
	public void setSdg(String sdg) {
		this.sdg = sdg;
	}

	@Transactional
	public void subscribe(ApiKey assetkey, List<Service> services, String userID)
			throws SGFCallingFailureException {
		if (services == null || services.size() == 0)
			return;
		this.appService.saveApiKey(assetkey, services, userID);

		SGFSG sg = new SGFSG();
		List<SGFSG.Service> sers = new ArrayList<SGFSG.Service>();
		for (Service service : services) {
			if (!service.getType().equals(Service.SERVICE_TYPE_SGF))
				continue;
			SGFSG.Service ser = sg.new Service();
			SGFService vo = new SGFService();
			vo.setId(service.getServiceid());
			ser.setServiceVO(vo);
			sers.add(ser);
		}

		SGFSG.WlngDetails wlng = sg.new WlngDetails();
		wlng.setUsername(assetkey.getSgName());
		wlng.setPassword(assetkey.getSgPassword());

		sg.setName(assetkey.getSgName());
		sg.setPartner(userID);
		sg.setUsername(assetkey.getSgName());
		sg.setPassword(assetkey.getSgPassword());
		sg.setServices(sers);
		sg.setWlngDetails(wlng);
		this.sGFWebService.createServiceGroup(sg);
	}

	/**
	 * Synchronize SGF API to Storefront Portal
	 */
	@Transactional
	public void synchronizeSGFAPI() throws SGFCallingFailureException {
		log.debug("Service synchronization task begins...");
		// Synchronize Service including operation
		List<SGFService> list = this.sGFWebService.getServicesBySdg(sdg);
		List<String> serviceIDs = null;
		if (list != null && list.size() > 0) {
			serviceIDs = new ArrayList<String>();
			Iterator<SGFService> iterator = list.iterator();
			while (iterator.hasNext()) {
				SGFService sgfService = iterator.next();
				serviceIDs.add(sgfService.getId());
				List<Service> searchResult = this.apiService
						.findServiceListBySGFId(sgfService.getId(), 0,
								Integer.MAX_VALUE);
				if (searchResult != null && searchResult.size() > 0) {
					Service service = searchResult.get(0);
					service.setAccessInterface(sgfService.getAccessInterface());
					service
							.setBrokerServiceAuthType(((SGFService.BrokerService) (sgfService
									.getBrokerServices()).get(0)).getAuthType());
					service
							.setBrokerServiceName(((SGFService.BrokerService) (sgfService
									.getBrokerServices()).get(0))
									.getBrokeredServiceName());
					service
							.setBrokerServiceUrl(((SGFService.BrokerService) (sgfService
									.getBrokerServices()).get(0)).getUrl());
					service.setDescription(sgfService.getDescription());
					service.setName(sgfService.getName());
					service.setType(Service.SERVICE_TYPE_SGF);
					if (service.getOperations() == null
							|| service.getOperations().size() == 0) {
						// get operations
						List<SGFOperation> opvos = null;
						try {
							opvos = this.sGFWebService
									.getOperationByService(service
											.getServiceid());
						} catch (SGFCallingFailureException e) {
							log.error("Getting Operation of Service: "
									+ service.getServiceid() + " error!");
						}
						if (opvos != null && opvos.size() > 0) {
							Set<Operation> operations = new HashSet<Operation>(
									opvos.size());
							for (int i = 0; i < opvos.size(); i++) {
								Operation operation = new Operation();
								operation.setName(opvos.get(i).getName());
								operation.setService(service);
								operations.add(operation);
							}
							service.setOperations(operations);
						}
					}
					log.debug("Updating service: " + service.getName());
					apiService.saveService(service);
					log.debug("Updating service: " + service.getName()
							+ " finished.");
				} else {
					Service service = new Service();
					service.setAccessInterface(sgfService.getAccessInterface());
					service
							.setBrokerServiceAuthType(((SGFService.BrokerService) (sgfService
									.getBrokerServices()).get(0)).getAuthType());
					service
							.setBrokerServiceName(((SGFService.BrokerService) (sgfService
									.getBrokerServices()).get(0))
									.getBrokeredServiceName());
					service
							.setBrokerServiceUrl(((SGFService.BrokerService) (sgfService
									.getBrokerServices()).get(0)).getUrl());
					service.setDescription(sgfService.getDescription());
					service.setName(sgfService.getName());
					service.setServiceid(sgfService.getId());
					service.setType(Service.SERVICE_TYPE_SGF);
					if (service.getOperations() == null
							|| service.getOperations().size() == 0) {
						// get operations
						List<SGFOperation> opvos = null;
						try {
							opvos = this.sGFWebService
									.getOperationByService(service
											.getServiceid());
						} catch (SGFCallingFailureException e) {
							log.error("Getting Operation of Service: "
									+ service.getServiceid() + " error!");
						}
						if (opvos != null && opvos.size() > 0) {
							Set<Operation> operations = new HashSet<Operation>(
									opvos.size());
							for (int i = 0; i < opvos.size(); i++) {
								Operation operation = new Operation();
								operation.setName(opvos.get(i).getName());
								operation.setService(service);
								operations.add(operation);
							}
							service.setOperations(operations);
						}
						if (service.getId() != null
								&& service.getId().intValue() == 0) {
							service.setId(null);
						}
					}
					log.debug("Creating service: " + service.getName());
					apiService.saveService(service);
					log.debug("Creating service: " + service.getName()
							+ " finished.");
				}
			}
		}

		// Synchronize Operation Rate
		List<SGFOperation> opRates = this.sGFWebService
				.listServiceOperationRate();
		if (opRates != null && opRates.size() > 0) {
			for (int i = 0; i < opRates.size(); i++) {
				SGFOperation opRate = opRates.get(i);
				if (opRate.getServiceID() == null) {
					continue;
				}
				List<Service> listService = apiService.findServiceListBySGFId(
						opRate.getServiceID(), 0, Integer.MAX_VALUE);
				List<Operation> listOp = null;
				if (listService != null && listService.size() > 0)
					listOp = apiService.findServiceOperation(listService.get(0)
							.getId(), opRate.getName());
				if (listOp != null && listOp.size() > 0) {
					Operation operation = listOp.get(0);
					if (operation.getRate() != null
							&& operation.getRate().equalsIgnoreCase(
									opRate.getRate()))
						continue;
					operation.setRate(opRate.getRate());
					apiService.saveServiceOperation(operation);
				}
			}
		}

		// Delete Services
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		StringTokenizer st = new StringTokenizer(this.deleteService, ",");
		while (st.hasMoreTokens()) {
			String token = st.nextToken();
			String dtService;
			String dtTime;
			StringTokenizer dtsdg = new StringTokenizer(token, "@");
			dtService = dtsdg.nextToken();
			if (dtsdg.hasMoreTokens()) {
				dtTime = dtsdg.nextToken();
			} else {
				dtTime = sdf.format(date);
			}

			List<Service> searchResult = apiService.findServiceListByName(
					dtService, 0, Integer.MAX_VALUE);
			if (searchResult != null && searchResult.size() > 0) {
				for (int i = 0; i < searchResult.size(); i++) {
					if (dtTime.equalsIgnoreCase(sdf.format(date)))
						apiService.deleteService(searchResult.get(i).getId());
				}
			}
		}

		log.debug("Service synchronization task ends...");
	}

	public void batchUpload(InputStream is) throws BatchUploadFailureException {
		ZipInputStream zipIs = new ZipInputStream(is);
		ZipEntry entry = null;
		Reader in = null;
		ExecutorService pool = Executors.newFixedThreadPool(10);
		try {
			InputStreamReader isr = new InputStreamReader(zipIs, "UTF8");
			in = new BufferedReader(isr);

			while ((entry = zipIs.getNextEntry()) != null) {
				if (entry.isDirectory())
					continue;
				StringBuffer buffer = new StringBuffer();
				int ch;
				while ((ch = in.read()) > -1) {
					buffer.append((char) ch);
				}

				String path = entry.getName();
				String type = path.substring(0, path.indexOf("/"));
				String apiName = path.substring(path.indexOf("/") + 1, path
						.lastIndexOf("/"));
				String fileName = path.substring(path.lastIndexOf("/") + 1);
				if (!(type.equals("SGF") || type.equals("COMMON")))
					throw new BatchUploadFailureException(
							"Batch upload zip format error");

				if (fileName.indexOf(apiName) >= 0) {
					byte[] bytes = buffer.toString().getBytes();
					// pool.execute(new CmsUploadThread(bytes, entry));
					// this.cmsUploadThread(bytes, entry);
					this.cmsUpload(bytes, entry);
				} else if (!fileName.equals("api_meta.xml")) {
					throw new BatchUploadFailureException(
							"Batch upload zip format error");
				}

				if (fileName.equals("api_meta.xml")) {
					String str = buffer.toString();
					XStreamMarshaller marshaller = new XStreamMarshaller();
					Map<String, Object> aliases = new HashMap<String, Object>();
					aliases.put("api", Service.class);

					try {
						marshaller.setAliases(aliases);
					} catch (Throwable e) {
						throw new BatchUploadFailureException(e.getMessage());
					}

					StreamSource streamSource = new StreamSource(
							new ByteArrayInputStream(str.getBytes("UTF8")));

					Service service = (Service) marshaller
							.unmarshal(streamSource);
					if (!service.getName().equals(apiName))
						throw new BatchUploadFailureException(
								"Batch upload zip format error");
					List<Service> list = this.apiService.findServiceListByName(
							service.getName(), 0, Integer.MAX_VALUE);
					if (list == null || list.size() == 0) {
						service.setType(type);
						this.apiService.saveService(service);
					} else {
						Service uService = list.get(0);
						uService.setName(service.getName());
						uService.setAccessInterface(service
								.getAccessInterface());
						uService.setBrokerServiceAuthType(service
								.getBrokerServiceAuthType());
						uService.setBrokerServiceName(service
								.getBrokerServiceName());
						uService.setBrokerServiceUrl(service
								.getBrokerServiceUrl());
						uService.setDescription(service.getDescription());
						uService.setDocUrl(service.getDocUrl());
						uService.setSdkUrl(service.getSdkUrl());
						uService.setServiceid(service.getServiceid());
						this.apiService.updateService(uService);
					}
				}
			}
			in.close();
		} catch (IOException e) {
			throw new BatchUploadFailureException(
					"Batch upload zip format error", e);
		}
	}

	private void cmsUpload(byte[] bytes, ZipEntry entry) {
		CMSServiceJBossImpl cmsService = new CMSServiceJBossImpl();
		cmsService.init();
		String fileName = entry.getName().substring(
				entry.getName().lastIndexOf("/") + 1);

		log.debug("Begin to upload API document 3: " + fileName);

		com.hp.sdf.ngp.cms.model.File file = new com.hp.sdf.ngp.cms.model.File();
		Content content = new Content();
		String fileExt = fileName.substring(fileName.lastIndexOf(".") + 1,
				fileName.length());
		CMSMimeMappings mapper = new CMSMimeMappings();
		if (mapper.getMimeType(fileExt) != null) {
			content.setMimeType(mapper.getMimeType(fileExt));
		} else {
			content.setMimeType("application/octet-stream");
		}
		file.setBasePath(CmsUploadThread.ROOT_PATH + "/" + entry.getName());
		file.setDescription(file.getBasePath());
		content.setTitle(fileName);
		content.setBasePath(CmsUploadThread.ROOT_PATH + "/" + entry.getName()
				+ "/" + new Locale("en"));
		content.setDescription(content.getBasePath());
		content.setBytes(bytes);
		content.setLocale(new Locale("en"));
		file.setContent(new Locale("en"), content);
		log.debug("File base path: " + file.getBasePath());
		cmsService.createFile(file);

		log.debug("Finish uploading API document: " + fileName);
	}

	private void cmsUploadThread(byte[] bytes, ZipEntry entry) {
		ExecutorService executor = Executors.newSingleThreadExecutor();
		executor.submit(new CmsUploadThread(bytes, entry));
	}

}

// $Id$