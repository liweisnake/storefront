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
package com.hp.sdf.ngp.ui.page.api;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.hp.sdf.ngp.manager.ApiManager;
import com.hp.sdf.ngp.ui.WicketSession;

@SuppressWarnings( { "unchecked" })
public class ApiBatchUploadPanel extends Panel {

	private static final long serialVersionUID = 1L;

	private final static Log log = LogFactory.getLog(ApiBatchUploadPanel.class);

	@SpringBean
	private ApiManager apiManager;

	public ApiManager getApiManager() {
		return apiManager;
	}

	public void setApiManager(ApiManager apiManager) {
		this.apiManager = apiManager;
	}

	public ApiBatchUploadPanel(String id) {
		super(id);
		WicketSession.get().setObject("selectApiConTab", ApiBatchUploadPanel.class.toString());
		add(new ApiForm("batchUploadForm", this.apiManager));
	}

	/**
	 * Api Batch Upload Form
	 */
	class ApiForm extends Form {

		private static final long serialVersionUID = -6018911062435817105L;
		private ApiManager apiManager;
		FileUpload fileUpload;

		public FileUpload getFileUpload() {
			return fileUpload;
		}

		public void setFileUpload(FileUpload fileUpload) {
			this.fileUpload = fileUpload;
		}

		public ApiForm(String id, ApiManager apiManager) {
			super(id);
			this.apiManager = apiManager;

			final FeedbackPanel feedback = new FeedbackPanel("feedback");
			add(feedback);

			FileUploadField fileUploadField = new FileUploadField("file", new PropertyModel<FileUpload>(this, "fileUpload"));
			fileUploadField.setRequired(true);
			add(fileUploadField);

			Button submit = new Button("submit");

			Button cancel = new Button("cancel") {

				private static final long serialVersionUID = 1L;

				public void onSubmit() {
					setResponsePage(ApiConfigurationPage.class);
				}
			};
			cancel.setDefaultFormProcessing(false);
			add(submit);
			add(cancel);
		}

		public final void onSubmit() {
			if (this.fileUpload == null)
				return;
			try {
				InputStream is = this.fileUpload.getInputStream();
				this.apiManager.batchUpload(is);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}
}
