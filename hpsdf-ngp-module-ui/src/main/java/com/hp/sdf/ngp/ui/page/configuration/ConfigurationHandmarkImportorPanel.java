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
package com.hp.sdf.ngp.ui.page.configuration;

import java.io.InputStream;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.PageParameters;
import org.apache.wicket.extensions.ajax.markup.html.form.upload.UploadProgressBar;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.hp.sdf.ngp.handmark.AssetImportor;
import com.hp.sdf.ngp.ui.common.PromptPanel;
import com.hp.sdf.ngp.ui.common.Tools;

public class ConfigurationHandmarkImportorPanel extends Panel {

	private static final Log log = LogFactory.getLog(ConfigurationHandmarkImportorPanel.class);

	private static final long serialVersionUID = -2942833806212089770L;

	private PromptPanel promptPanel;

	public ConfigurationHandmarkImportorPanel(String id, PageParameters parameters) {
		super(id);

		add(new FeedbackPanel("feedBack"));

		final HandmarkFileUploadForm ajaxUploadForm = new HandmarkFileUploadForm("ajaxUpload");
		ajaxUploadForm.add(new UploadProgressBar("progress", ajaxUploadForm));
		add(ajaxUploadForm);

		promptPanel = new PromptPanel("promptPanel", getLocalizer().getString("promptTitle", this), getLocalizer().getString("successMsg", this), null, StringUtils.EMPTY);
		add(promptPanel);
	}

	private class HandmarkFileUploadForm extends Form<Void> {

		private static final long serialVersionUID = -3118504764589681493L;

		private FileUploadField masterFeedFileUploadField;

		private FileUploadField deviceFeedFileUploadField;

		@SpringBean
		private AssetImportor assetImportor;

		public HandmarkFileUploadForm(String id) {
			super(id);

			// set this form to multipart mode (always needed for uploads!)
			setMultiPart(true);

			masterFeedFileUploadField = new FileUploadField("masterfeedFileInput");
			add(masterFeedFileUploadField);

			deviceFeedFileUploadField = new FileUploadField("devicefeedFileInput");
			add(deviceFeedFileUploadField);

			Button uploadButton = new Button("upload") {
				private static final long serialVersionUID = 8176586206621630586L;

				public void onSubmit() {
					log.debug("Starting upload the master feed File and device feed File.");

					FileUpload masterFeedFileUpload = masterFeedFileUploadField.getFileUpload();
					FileUpload deviceFeedFileUpload = deviceFeedFileUploadField.getFileUpload();
					if (null != masterFeedFileUpload && null != deviceFeedFileUpload) {
						log.debug("masterFeedFileUpload.getClientFileName() :" + masterFeedFileUpload.getClientFileName());
						log.debug("deviceFeedFileUpload.getClientFileName() :" + deviceFeedFileUpload.getClientFileName());

						if (masterFeedFileUpload.getClientFileName().toLowerCase().endsWith(".xml") && deviceFeedFileUpload.getClientFileName().toLowerCase().endsWith(".xml")) {
							try {
								InputStream masterXMLInputStream = masterFeedFileUpload.getInputStream();
								InputStream deviceXMLInputStream = deviceFeedFileUpload.getInputStream();
								if (masterXMLInputStream != null && deviceXMLInputStream != null) {
									assetImportor.importAsset(masterXMLInputStream, deviceXMLInputStream);
								}
							} catch (Exception exception) {
								log.error("Exception : \n" + exception);
								this.error("Upload failed.");
								return;
							}
						} else {
							this.error("The master feed file and device feed file should be xml file.");
							return;
						}

					} else {
						this.error("The master feed file or device feed file can not be null.");
						return;
					}

					promptPanel.show();
				}
			};

			uploadButton.add(Tools.addConfirmJs(getLocalizer().getString("confirmMsg", ConfigurationHandmarkImportorPanel.this)));
			add(uploadButton);
		}
	}

}
