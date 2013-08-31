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
package com.hp.sdf.ngp.custom.sbm.storeclient.service;

import java.io.File;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.hp.sdf.ngp.api.exception.StoreClientEntityNotFoundException;
import com.hp.sdf.ngp.api.exception.StoreClientServiceException;
import com.hp.sdf.ngp.api.search.SearchExpression;
import com.hp.sdf.ngp.api.search.Condition.StringComparer;
import com.hp.sdf.ngp.common.annotation.Value;
import com.hp.sdf.ngp.common.exception.SaveFileFailureException;
import com.hp.sdf.ngp.custom.sbm.api.impl.model.ClientAppSettingImpl;
import com.hp.sdf.ngp.custom.sbm.api.impl.model.ClientApplicationImpl;
import com.hp.sdf.ngp.custom.sbm.api.impl.model.StoreClientSoftwareImpl;
import com.hp.sdf.ngp.custom.sbm.api.storeclient.model.ClientAppSetting;
import com.hp.sdf.ngp.custom.sbm.api.storeclient.model.StoreClientSoftware;
import com.hp.sdf.ngp.custom.sbm.api.storeclient.service.ClientSoftwareService;
import com.hp.sdf.ngp.custom.sbm.storeclient.dao.ClientAppSettingDao;
import com.hp.sdf.ngp.custom.sbm.storeclient.dao.ClientApplicationDao;
import com.hp.sdf.ngp.custom.sbm.storeclient.model.ClientApplication;
import com.hp.sdf.ngp.search.condition.clientapplication.ClientApplicationClientNameCondition;
import com.hp.sdf.ngp.search.condition.clientappsetting.ClientAppSettingClientNameCondition;
import com.hp.sdf.ngp.search.engine.SearchExpressionImpl;

@Component
@Transactional
public class ClientSoftwareServiceImpl implements ClientSoftwareService {

	private final static Log log = LogFactory.getLog(ClientSoftwareServiceImpl.class);

	@Resource
	private ClientApplicationDao clientApplicationDao;

	@Resource
	private ClientAppSettingDao clientAppSettingDao;

	private String filePath;

	public String getFilePath() {
		return filePath;
	}

	@Value("file.path.prefix")
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public StoreClientSoftware getLatestStoreClientSoftware(String clientName) throws StoreClientServiceException {
		log.debug("enter getLatestStoreClientSoftware:clientName=" + clientName);
		try {
			SearchExpression searchExpression = new SearchExpressionImpl();
			searchExpression.addCondition(new ClientApplicationClientNameCondition(clientName, StringComparer.EQUAL, false, false));
			List<ClientApplication> clientApplications = clientApplicationDao.search(searchExpression);

			SearchExpression searchExpression2 = new SearchExpressionImpl();
			searchExpression2.addCondition(new ClientAppSettingClientNameCondition(clientName, StringComparer.EQUAL, false, false));
			List<com.hp.sdf.ngp.custom.sbm.storeclient.model.ClientAppSetting> clientAppSettings = clientAppSettingDao.search(searchExpression2);

			// if (null != clientApplications && clientAppSettings != null) {
			// log.debug("clientApplications.size() :" +
			// clientApplications.size());
			// log.debug("clientAppSettings.size() :" +
			// clientAppSettings.size());

			ClientApplication clientApplication = null;
			com.hp.sdf.ngp.custom.sbm.storeclient.model.ClientAppSetting clientAppSetting = null;

			if (clientApplications != null && clientApplications.size() > 0)
				clientApplication = clientApplications.get(0);

			if (clientAppSettings != null && clientAppSettings.size() > 0)
				clientAppSetting = clientAppSettings.get(0);

			// if (clientApplications.size() > 0 && clientAppSettings.size() >
			// 0) {

			StoreClientSoftware storeClientSoftware = new StoreClientSoftwareImpl(clientApplication, clientAppSetting);
			log.debug("getLatestStoreClientSoftware returns size:" + storeClientSoftware.toString());
			return storeClientSoftware;
			// }
			// }

			// log.debug("getLatestStoreClientSoftware returns null");
			// return null;

		} catch (Exception e) {
			e.printStackTrace();
			log.error("Comment exception: " + e);
			throw new StoreClientServiceException(e);
		}
	}

	public ClientAppSetting constructClientAppSetting() {
		return new ClientAppSettingImpl();
	}

	public com.hp.sdf.ngp.custom.sbm.api.storeclient.model.ClientApplication constructClientApplication() {
		return new ClientApplicationImpl();
	}

	public void deleteClientAppSetting(Long clientAppSettingId) throws StoreClientServiceException {
		log.debug("enter deleteClientAppSetting:clientAppSettingId=" + clientAppSettingId);
		try {
			clientAppSettingDao.remove(clientAppSettingId);

		} catch (Exception e) {
			e.printStackTrace();
			log.error("Comment exception: " + e);
			throw new StoreClientServiceException(e);
		}
	}

	public void deleteClientApplication(Long clientApplicationId) throws StoreClientServiceException {
		log.debug("enter deleteClientApplication:clientApplicationId=" + clientApplicationId);
		try {
			clientApplicationDao.remove(clientApplicationId);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Comment exception: " + e);
			throw new StoreClientServiceException(e);
		}
	}

	public ClientAppSetting getClientAppSetting(Long clientAppSettingId) throws StoreClientServiceException {
		log.debug("enter getClientAppSetting:clientAppSettingId=" + clientAppSettingId);
		try {
			com.hp.sdf.ngp.custom.sbm.storeclient.model.ClientAppSetting clientAppSetting = clientAppSettingDao.findById(clientAppSettingId);
			if (clientAppSetting != null) {
				log.debug("getClientAppSetting returns:" + new ClientAppSettingImpl(clientAppSetting).toString());
				return new ClientAppSettingImpl(clientAppSetting);
			} else {
				log.debug("getClientAppSetting returns null");
				throw new StoreClientEntityNotFoundException("getClientAppSetting returns null");
			}

		} catch (Exception e) {
			e.printStackTrace();
			log.error("Comment exception: " + e);

			if (e instanceof StoreClientEntityNotFoundException) {
				throw new StoreClientEntityNotFoundException(e);
			}

			throw new StoreClientServiceException(e);
		}
	}

	public com.hp.sdf.ngp.custom.sbm.api.storeclient.model.ClientApplication getClientApplication(Long clientApplicationId)
			throws StoreClientServiceException {
		log.debug("enter getClientApplication:clientApplicationId=" + clientApplicationId);
		try {
			ClientApplication clientApplication = clientApplicationDao.findById(clientApplicationId);
			if (clientApplication != null) {

				log.debug("getClientApplication returns: " + new ClientApplicationImpl(clientApplication).toString());
				return new ClientApplicationImpl(clientApplication);
			} else {
				log.debug("getClientApplication returns null");
				throw new StoreClientEntityNotFoundException("getClientApplication returns null");
			}

		} catch (Exception e) {
			e.printStackTrace();
			log.error("Comment exception: " + e);

			if (e instanceof StoreClientEntityNotFoundException) {
				throw new StoreClientEntityNotFoundException(e);
			}

			throw new StoreClientServiceException(e);
		}
	}

	public void saveClientAppSetting(ClientAppSetting clientAppSetting) throws StoreClientServiceException {
		log.debug("enter saveClientAppSetting:" + clientAppSetting.toString());
		try {
			ClientAppSettingImpl clientAppSettingImpl = (ClientAppSettingImpl) clientAppSetting;

			com.hp.sdf.ngp.custom.sbm.storeclient.model.ClientAppSetting clientAppSettingModel = clientAppSettingImpl.getClientAppSetting();
			clientAppSettingModel.setUpdateDate(new Date());
			if (clientAppSettingModel.getId() == null) {
				clientAppSettingDao.persist(clientAppSettingModel);
			} else {
				clientAppSettingDao.merge(clientAppSettingModel);
			}

			if (clientAppSettingImpl.getContent() != null) {
				saveFile(filePath + clientAppSettingImpl.getFileName(), clientAppSettingImpl.getContent());
			}

		} catch (SaveFileFailureException e) {
			e.printStackTrace();
			log.error("Comment exception: " + e);
			throw new StoreClientServiceException(e);
		}
	}

	public void saveClientApplication(com.hp.sdf.ngp.custom.sbm.api.storeclient.model.ClientApplication clientApplication)
			throws StoreClientServiceException {
		log.debug("enter saveClientApplication:" + clientApplication.toString());

		try {
			ClientApplicationImpl clientApplicationImpl = (ClientApplicationImpl) clientApplication;

			ClientApplication clientApplicationModel = clientApplicationImpl.getClientApplication();
			clientApplicationModel.setUpdateDate(new Date());
			if (clientApplicationModel.getId() == null) {
				clientApplicationDao.persist(clientApplicationModel);
			} else {
				clientApplicationDao.merge(clientApplicationModel);
			}

			if (clientApplicationImpl.getContent() != null) {
				saveFile(filePath + clientApplicationImpl.getFileName(), clientApplicationImpl.getContent());
			}
		} catch (SaveFileFailureException e) {
			e.printStackTrace();
			log.error("Comment exception: " + e);
			throw new StoreClientServiceException(e);
		}
	}

	private void saveFile(String path, byte[] content) throws SaveFileFailureException, StoreClientServiceException {
		log.debug("enter saveFile:path='" + path + "',content=" + content);
		try {
			log.debug("save file path :" + path);

			File directory = new File(path.substring(0, path.lastIndexOf("/")));

			if (!directory.exists()) {
				directory.mkdirs();
			}

			File file = new File(path);
			try {
				FileUtils.writeByteArrayToFile(file, content);
			} catch (Exception e) {
				e.printStackTrace();
				throw new SaveFileFailureException("save file error:" + e.toString());
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Comment exception: " + e);
			throw new StoreClientServiceException(e);
		}
	}

	public String getStoreClientSoftwareConfigFileLatestVersion(String clientName) throws StoreClientServiceException {
		log.debug("enter getStoreClientSoftwareConfigFileLatestVersion:clientName='" + clientName + "'");
		String version = null;
		StoreClientSoftware software = this.getLatestStoreClientSoftware(clientName);
		if (software != null) {
			version = software.getConfigFileVersion();
		}

		log.debug("getStoreClientSoftwareConfigFileLatestVersion returns:" + clientName);
		return version;
	}

	public String getStoreClientSoftwareLatestVersion(String clientName) throws StoreClientServiceException {
		log.debug("enter getStoreClientSoftwareLatestVersion:clientName=" + clientName);
		String version = null;
		StoreClientSoftware software = this.getLatestStoreClientSoftware(clientName);
		if (software != null) {
			version = software.getVersion();
		}

		log.debug("getStoreClientSoftwareLatestVersion returns:" + version);
		return version;
	}

}
