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

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.PageParameters;
import org.apache.wicket.extensions.ajax.markup.html.form.upload.UploadProgressBar;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.lang.Bytes;

import com.hp.sdf.ngp.administration.AssetBatchUploader;
import com.hp.sdf.ngp.ui.common.PromptPanel;
import com.hp.sdf.ngp.ui.common.Tools;

public class ConfigurationBatchUploadPanel extends Panel {

	private static final long serialVersionUID = -2187270743567844716L;

	private static final Log log = LogFactory.getLog(ConfigurationBatchUploadPanel.class);
	
	private PromptPanel promptPanel;

	public ConfigurationBatchUploadPanel(String id, PageParameters parameters) {
		super(id);
		
		final FileUploadForm ajaxUploadForm = new FileUploadForm("ajaxUpload");
		ajaxUploadForm.add(new UploadProgressBar("progress", ajaxUploadForm));
		add(new FeedbackPanel("feedBack"));
		add(ajaxUploadForm);
		
		promptPanel = new PromptPanel("promptPanel", getLocalizer().getString("promptTitle", this), getLocalizer().getString("successMsg", this), null, StringUtils.EMPTY);
		add(promptPanel);
	}

	private class FileUploadForm extends Form<Void> {

		private static final long serialVersionUID = 8875628976967871848L;

		private FileUploadField fileUploadField;

		@SpringBean
		private AssetBatchUploader afu;

		/**
		 * Construct.
		 * 
		 * @param name
		 *            Component name
		 */
		public FileUploadForm(String name) {
			super(name);

			// set this form to multipart mode (allways needed for uploads!)
			setMultiPart(true);

			// Add one file input field
			add(fileUploadField = new FileUploadField("fileInput"));

			// Set maximum size to 100K for demo purposes
			setMaxSize(Bytes.megabytes(100));
			
			
			Button uploadButton = new Button("upload") {
				private static final long serialVersionUID = 8176586206621630586L;

				public void onSubmit() {
					if (fileUploadField.getFileUpload() == null) {
						this.error("Input file cannot be null.");
						return;
					}

					if (!fileUploadField.getFileUpload().getClientFileName().toLowerCase().endsWith(".zip")) {
						this.error("Input file should be zip file.");
						return;
					}

					try {
						afu.batchUpload(fileUploadField.getFileUpload().getInputStream());
					} catch (Exception e) {
						log.error("File upload error", e);
						this.error(e.getMessage());
						return;
					}
					
					promptPanel.show();
					
				}
			};
			uploadButton.add(Tools.addConfirmJs(getLocalizer().getString("confirmMsg", ConfigurationBatchUploadPanel.this)));
			add(uploadButton);
			
		}

	}

}
