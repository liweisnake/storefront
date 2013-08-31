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
package com.hp.sdf.ngp.ui.page.testing;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.extensions.breadcrumb.IBreadCrumbModel;
import org.apache.wicket.extensions.breadcrumb.panel.BreadCrumbPanel;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.link.ExternalLink;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.hp.sdf.ngp.model.AssetBinaryVersion;
import com.hp.sdf.ngp.service.ApplicationService;
import com.hp.sdf.ngp.ui.WicketApplication;

@SuppressWarnings( { "unchecked" })
public class BinDownloadPanel extends BreadCrumbPanel {

	private static final long serialVersionUID = -4918503633298438L;

	private final static Log log = LogFactory.getLog(BinDownloadPanel.class);

	@SpringBean(name = "wicketApplication")
	private WicketApplication wicketApplication;

	@SpringBean
	private ApplicationService applicationService;

	private Long binaryId;

	public BinDownloadPanel(String id, IBreadCrumbModel breadCrumbModel, Long binaryId) {
		super(id, breadCrumbModel);
		this.binaryId = binaryId;
		AppDownloadForm appDownloadForm = new AppDownloadForm("appDownloadForm");
		add(appDownloadForm);
	}

	public String getTitle() {
		return this.getLocalizer().getString("title", this, "Download Application");
	}

	public class AppDownloadForm extends Form {

		private static final long serialVersionUID = -6578579305600140145L;

		// The binary id
		private Long binaryId;

		private ExternalLink downloadLink = null;

		private String binaryURI;

		public AppDownloadForm(String id) {

			super(id);
			binaryId = BinDownloadPanel.this.binaryId;
			final AssetBinaryVersion bin = applicationService.getAssetBinaryById(binaryId);

			// feedback panel
			// this.add(new FeedbackPanel("feedBack"));

			// The link for download, which aslo works in Portlet
			binaryURI = wicketApplication.getUriPrefix();
			binaryURI += bin.getLocation();
			log.debug("binary URI is: " + binaryURI);
			downloadLink = new ExternalLink("download", binaryURI);
			downloadLink.setOutputMarkupId(true);
			downloadLink.setEnabled(true);
			this.add(downloadLink);

		}
	}
}

// $Id$