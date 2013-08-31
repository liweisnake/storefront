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
package com.hp.sdf.ngp.ui.page.oam.storeclient;

import java.io.InputStream;
import java.text.SimpleDateFormat;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.Component;
import org.apache.wicket.authorization.strategies.role.metadata.MetaDataRoleAuthorizationStrategy;
import org.apache.wicket.extensions.breadcrumb.IBreadCrumbModel;
import org.apache.wicket.extensions.breadcrumb.IBreadCrumbParticipant;
import org.apache.wicket.extensions.breadcrumb.panel.BreadCrumbPanel;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.Radio;
import org.apache.wicket.markup.html.form.RadioGroup;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.hp.sdf.ngp.api.exception.StoreClientServiceException;
import com.hp.sdf.ngp.custom.sbm.api.storeclient.model.ClientAppSetting;
import com.hp.sdf.ngp.custom.sbm.api.storeclient.model.ClientApplication;
import com.hp.sdf.ngp.custom.sbm.api.storeclient.model.StoreClientSoftware;
import com.hp.sdf.ngp.custom.sbm.api.storeclient.service.ClientSoftwareService;
import com.hp.sdf.ngp.ui.common.CheckPanel;
import com.hp.sdf.ngp.ui.common.PromptPanel;
import com.hp.sdf.ngp.workflow.Privilege;

public class StoreClientEditPanel extends BreadCrumbPanel {

	private static final long serialVersionUID = -8920226863398352415L;

	private static final Log log = LogFactory.getLog(StoreClientEditPanel.class);

	// TODO : need to be changed
	private static final String CLIENT_NAME = "SBM Client";

	@SpringBean
	private ClientSoftwareService clientSoftwareService;

	private Boolean showDialog = false;

	public Boolean getShowDialog() {
		return showDialog;
	}

	public void setShowDialog(Boolean showDialog) {
		this.showDialog = showDialog;
	}

	private PromptPanel promptPanel;

	public StoreClientEditPanel(String id, IBreadCrumbModel breadCrumbModel) {
		super(id, breadCrumbModel);

		StoreClientSoftware storeClientSoftware = null;
		try {
			storeClientSoftware = clientSoftwareService.getLatestStoreClientSoftware(CLIENT_NAME);
			log.debug("storeClientSoftware :" + storeClientSoftware);
		} catch (StoreClientServiceException exception) {
			log.error("StoreClientServiceException :" + exception);
			exception.printStackTrace();
		}

		promptPanel = new PromptPanel("promptPanel", getLocalizer().getString("promptTitle", this), getLocalizer().getString("successMsg", this), null, StringUtils.EMPTY);
		add(promptPanel);

		StoreClientEditForm storeClientEditForm = new StoreClientEditForm("storeClientEditForm", storeClientSoftware, breadCrumbModel);
		MetaDataRoleAuthorizationStrategy.authorize(storeClientEditForm, Component.RENDER, Privilege.VIEWCLIENTAPPLICATION);
		this.add(storeClientEditForm);
	}

	public final class StoreClientEditForm extends Form<Void> {

		private static final long serialVersionUID = 2121043705949859826L;

		private String newVersion = null;

		private String newFileType = "";

		private InputStream newVersionUpdateInputStream;
		private String clientFileName;
		StoreClientSoftware newStoreClientSoftware = null;
		ClientApplication application;
		ClientAppSetting clientAppSetting;
		ClientApplication clientApplication;

		public String getNewFileType() {
			return newFileType;
		}

		public void setNewFileType(String newFileType) {
			this.newFileType = newFileType;
		}

		public StoreClientEditForm(String id, final StoreClientSoftware storeClientSoftware, final IBreadCrumbModel breadCrumbModel) {
			super(id);

			this.add(new FeedbackPanel("feedBack"));

			Label name = new Label("name", storeClientSoftware != null ? storeClientSoftware.getName() : "");
			add(name);

			Label version = new Label("version", storeClientSoftware != null ? storeClientSoftware.getVersion() : "");
			add(version);

			SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			String fileUpdateString = "";
			if (storeClientSoftware != null && storeClientSoftware.getFileUpdateDate() != null) {
				fileUpdateString = sdf.format(storeClientSoftware.getFileUpdateDate());
			}

			Label fileUpdateDate = new Label("fileUpdateDate", fileUpdateString);
			add(fileUpdateDate);

			Label configFileNameLabel = new Label("configFileName", storeClientSoftware != null ? storeClientSoftware.getConfigFileLocation() : "");
			add(configFileNameLabel);

			Label configFileVersion = new Label("configFileVersion", storeClientSoftware != null ? storeClientSoftware.getConfigFileVersion() : "");
			add(configFileVersion);

			String configFileUpdateDateString = "";
			if (storeClientSoftware != null && storeClientSoftware.getConfigFileUpdateDate() != null) {
				configFileUpdateDateString = sdf.format(storeClientSoftware.getConfigFileUpdateDate());
			}
			Label configFileUpdateDate = new Label("configFileUpdateDate", configFileUpdateDateString);
			add(configFileUpdateDate);

			final RadioGroup<String> group = new RadioGroup<String>("uploadNewFile", new PropertyModel<String>(this, "newFileType"));
			group.add(new Radio<String>("binaryFile", new Model<String>("0")));
			group.add(new Radio<String>("configFile", new Model<String>("1")));
			group.setRequired(true);
			add(group);

			final FileUploadField newVersionUpdate = new FileUploadField("newVersionUpdate");
			newVersionUpdate.setRequired(true);
			add(newVersionUpdate);

			TextField<String> newVersion = new TextField<String>("newVersion", new PropertyModel<String>(this, "newVersion"));
			newVersion.setRequired(true);
			add(newVersion);

			final CheckPanel checkPanel = new CheckPanel("checkPanel", getLocalizer().getString("promptTitle", StoreClientEditPanel.this), getLocalizer().getString("confirmMsg",
					StoreClientEditPanel.this)) {
				private static final long serialVersionUID = 7465424124110025236L;

				@Override
				public void howDo() {

					try {

						if (newStoreClientSoftware != null) {
							if ("0".equals(newFileType)) {
								clientSoftwareService.saveClientApplication(clientApplication);
							} else if ("1".equals(newFileType)) {
								clientSoftwareService.saveClientAppSetting(clientAppSetting);
							}
						}
						promptPanel.show();
					} catch (Exception exception) {
						exception.printStackTrace();
						log.error("Exception :" + exception);
						error(getLocalizer().getString("error.save.failed", StoreClientEditPanel.this));
						return;
					}
					// StoreClientEditPanel newPanel = new
					// StoreClientEditPanel("storeClientEditPanel",
					// breadCrumbModel);
					// newPanel.setShowDialog(true);
					// StoreClientEditPanel.this.activate(newPanel);
				}
			};
			add(checkPanel);

			Button save = new Button("save") {

				private static final long serialVersionUID = 1510836418186350793L;

				public void onSubmit() {

					log.debug("click save.");
					log.debug("newVersion :" + StoreClientEditForm.this.newVersion);

					if (newVersionUpdate.getFileUpload() != null) {
						try {
							newVersionUpdateInputStream = newVersionUpdate.getFileUpload().getInputStream();
							if (newVersionUpdateInputStream != null) {
								clientFileName = newVersionUpdate.getFileUpload().getClientFileName();
								if (StringUtils.isNotEmpty(clientFileName)) {
									if (clientFileName.contains("\\")) {
										int beginIndex = clientFileName.lastIndexOf("\\");
										clientFileName = clientFileName.substring(beginIndex + 1);
									}
								}

								if (null == storeClientSoftware || (0L == storeClientSoftware.getClientApplicationId() && 0L == storeClientSoftware.getClientAppSettingId())) {
									log.debug("storeClientSoftware is null or no Application & ClientAppSetting. Will create ClientApplication and ClientAppSetting.");
									// create ClientApplication and
									// ClientAppSetting
									application = clientSoftwareService.constructClientApplication();
									application.setClientName(CLIENT_NAME);
									clientSoftwareService.saveClientApplication(application);

									clientAppSetting = clientSoftwareService.constructClientAppSetting();
									clientAppSetting.setClientName(CLIENT_NAME);
									clientSoftwareService.saveClientAppSetting(clientAppSetting);

									// create a new store client software
									newStoreClientSoftware = clientSoftwareService.getLatestStoreClientSoftware(CLIENT_NAME);
								} else {
									newStoreClientSoftware = storeClientSoftware;
								}

								if (newStoreClientSoftware != null) {
									if ("0".equals(newFileType)) {
										log.debug("Will upload Binary file.");
										// delete the old clientApplication
										log.debug("newStoreClientSoftware.getClientApplicationId() :" + newStoreClientSoftware.getClientApplicationId());
										try {
											if (clientSoftwareService.getClientApplication(newStoreClientSoftware.getClientApplicationId()) != null) {
												clientSoftwareService.deleteClientApplication(newStoreClientSoftware.getClientApplicationId());
											}
										} catch (Exception exception) {
											exception.printStackTrace();
										}
										// save the new clientApplication
										clientApplication = clientSoftwareService.constructClientApplication();
										clientApplication.setClientName(newStoreClientSoftware.getName());
										clientApplication.setFile(clientFileName, newVersionUpdateInputStream);
										clientApplication.setVersion(StoreClientEditForm.this.newVersion);
									} else if ("1".equals(newFileType)) {
										log.debug("Will upload Config file.");

										// delete the old clientAppSetting
										log.debug("newStoreClientSoftware.getClientAppSettingId() :" + newStoreClientSoftware.getClientAppSettingId());
										
										try {
											if (clientSoftwareService.getClientAppSetting(newStoreClientSoftware.getClientAppSettingId()) != null) {
												clientSoftwareService.deleteClientAppSetting(newStoreClientSoftware.getClientAppSettingId());
											}
										} catch (Exception exception) {
											exception.printStackTrace();
										}

										// save the new clientAppSetting
										clientAppSetting = clientSoftwareService.constructClientAppSetting();
										clientAppSetting.setClientName(newStoreClientSoftware.getName());
										clientAppSetting.setFile(clientFileName, newVersionUpdateInputStream);
										clientAppSetting.setVersion(StoreClientEditForm.this.newVersion);
									}
								}

								log.debug("client file name : " + clientFileName);
								checkPanel.show();
							} else {
								error(getLocalizer().getString("error.save.nofile", StoreClientEditPanel.this));
								return;
							}
						} catch (Exception exception) {
							exception.printStackTrace();
							log.error("Exception :" + exception);
							error(getLocalizer().getString("error.save.failed", StoreClientEditPanel.this));
							return;
						}
					} else {
						error(getLocalizer().getString("error.save.nofile", StoreClientEditPanel.this));
						return;
					}
				}
			};
			// save.add(Tools.addConfirmJs(getLocalizer().getString("confirmMsg",
			// StoreClientEditPanel.this)));
			MetaDataRoleAuthorizationStrategy.authorize(save, Component.RENDER, Privilege.UPDATECLIENTAPPLICATION);
			add(save);
		}
	}

	@Override
	public void activate(IBreadCrumbParticipant participant) {
		log.debug("Come to active method.");

		StoreClientEditPanel newPanel = (StoreClientEditPanel) participant;
		if (newPanel.getShowDialog()) {
			log.debug("showDialog is true..");
			newPanel.promptPanel.setMessage(getLocalizer().getString("successMsg", this));

			newPanel.promptPanel.show();
			newPanel.setShowDialog(false);
		}

		super.activate(participant);
	}

	public String getTitle() {
		return this.getLocalizer().getString("title", this, "Edit Client Application");
	}

}
