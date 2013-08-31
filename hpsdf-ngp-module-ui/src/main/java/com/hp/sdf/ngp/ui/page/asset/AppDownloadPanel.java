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
package com.hp.sdf.ngp.ui.page.asset;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.authorization.strategies.role.Roles;
import org.apache.wicket.authorization.strategies.role.metadata.MetaDataRoleAuthorizationStrategy;
import org.apache.wicket.markup.html.DynamicWebResource;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.markup.html.link.ResourceLink;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.protocol.http.WebResponse;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.hp.sdf.ngp.model.AssetBinaryVersion;
import com.hp.sdf.ngp.service.ApplicationService;
import com.hp.sdf.ngp.workflow.AccessType;
import com.hp.sdf.ngp.workflow.AssetBinaryVersionLifeCycleEngine;

@SuppressWarnings( { "unchecked" })
public class AppDownloadPanel extends Panel {

	private static final long serialVersionUID = -4918503633298438L;

	private static final Log log = LogFactory.getLog(AppDownloadPanel.class);

	@SpringBean
	private ApplicationService applicationService;

	@SpringBean
	private AssetBinaryVersionLifeCycleEngine assetBinaryVersionLifeCycleEngine;

	private Long appId;

	private long binaryCount;

	private final String EMPTY_STATUS = getLocalizer().getString("bin.nostatus", this);

	public long getBinaryCount() {
		return binaryCount;
	}

	public void setBinaryCount(long binaryCount) {
		this.binaryCount = binaryCount;
	}

	public AppDownloadPanel(String id, Long appId) {
		super(id);
		this.appId = appId;
		AppDownloadForm appDownloadForm = new AppDownloadForm("appDownloadForm");
		add(appDownloadForm);
	}

	public String getTitle() {
		return this.getLocalizer().getString("title", this, "Download Application");
	}

	public class AppDownloadForm extends Form {

		private static final long serialVersionUID = -6578579305600140145L;

		private LoadableDetachableModel platformBinaries = new LoadableDetachableModel<List<AssetBinaryVersion>>() {

			@Override
			protected List<AssetBinaryVersion> load() {

				return applicationService.getAssetBinaryByAssetId(appId);
			}

		};

		// The platform value
		private String selectedPlatform;

		public String getSelectedPlatform() {
			return selectedPlatform;
		}

		public void setSelectedPlatform(String selectedPlatform) {
			this.selectedPlatform = selectedPlatform;
		}

		// The binary id
		private Long binaryId;

		private ResourceLink downloadLink = null;

		Label hint = null, binaryStatus = null;

		public Long getBinaryId() {
			return binaryId;
		}

		public void setBinaryId(Long binaryId) {
			this.binaryId = binaryId;
		}

		public AppDownloadForm(String id) {

			super(id);
			// feedback panel
			// this.add(new FeedbackPanel("feedBack"));

			// initial the binary map
			final Map<Long, AssetBinaryVersion> binaries = new HashMap<Long, AssetBinaryVersion>();

			final List binaryList = (List) platformBinaries.getObject();
			if (binaryList != null && binaryList.size() > 0) {
				AppDownloadPanel.this.binaryCount = binaryList.size();
			}

			// The value list for binary
			IModel<List<? extends Long>> versionValues = new AbstractReadOnlyModel<List<? extends Long>>() {
				private static final long serialVersionUID = 1L;

				@Override
				public List<Long> getObject() {

					binaryId = null;

					List<AssetBinaryVersion> models = null;
					if (binaryList == null || binaryList.size() < 1) {
						return Collections.emptyList();
					} else {
						models = (List<AssetBinaryVersion>) binaryList;
					}

					List<Long> result = new ArrayList<Long>();

					binaries.clear();

					for (AssetBinaryVersion appBinary : models) {
						result.add(appBinary.getId());
						binaries.put(appBinary.getId(), appBinary);
					}
					return result;
				}

			};

			// The value list for binary id rendered to version String
			IChoiceRenderer<Long> versionRender = new ChoiceRenderer<Long>() {
				private static final long serialVersionUID = 1L;

				public String getDisplayValue(Long object) {
					AssetBinaryVersion appBinary = binaries.get(object);
					return appBinary.getVersion();
				}
			};

			// create the choice component
			final DropDownChoice<Long> versionChoice = new DropDownChoice<Long>("version", new PropertyModel<Long>(this, "binaryId"), versionValues, versionRender);

			this.add(versionChoice);

			// it is necessary to enable the output markup id for ajax
			versionChoice.setOutputMarkupId(true);

			// The link for download, which aslo works in Portlet
			downloadLink = new ResourceLink("download", new DynamicWebResource() {

				private static final long serialVersionUID = 1L;

				@Override
				protected void setHeaders(WebResponse response) {
					super.setHeaders(response);
					response.setAttachmentHeader(binaries.get(binaryId).getFileName());

				}

				@Override
				protected ResourceState getResourceState() {
					return new DynamicWebResource.ResourceState() {
						@Override
						public String getContentType() {
							// Return a ZIP format?
							return "application/octet-stream";
						}

						@Override
						public byte[] getData() {
							// TODO
							return null;
						}
					};
				}
			}) {

				private static final long serialVersionUID = 1L;

				@Override
				public void onClick() {

					if (binaryId == null) {
						error(this.getLocalizer().getString("error", this, "Please select a binary to download"));
						return;
					}
					final AssetBinaryVersion appBinary = binaries.get(binaryId);

					// TODO: update download count

					super.onClick();
				}
			};
			downloadLink.setOutputMarkupId(true);
			MetaDataRoleAuthorizationStrategy.unauthorizeAll(downloadLink, Component.ENABLE);
			this.add(downloadLink);

			// Ajax update
			versionChoice.add(new AjaxFormComponentUpdatingBehavior("onchange") {
				private static final long serialVersionUID = 1L;

				@Override
				protected void onUpdate(AjaxRequestTarget target) {
					// do nothing, just use ajax to submit the value
					log.debug("bin id = " + binaryId);
					if (binaryId != null) {
						AssetBinaryVersion binary = applicationService.getAssetBinaryById(binaryId);
						binaryStatus.setDefaultModel(new Model<String>(binary.getStatus().getStatus()));

						String[] privileges = assetBinaryVersionLifeCycleEngine.getAccessPrivilege(binaryId, AccessType.DOWNLOAD);
						Roles roles = new Roles(privileges);
						MetaDataRoleAuthorizationStrategy.unauthorizeAll(downloadLink, Component.ENABLE);
						MetaDataRoleAuthorizationStrategy.authorize(downloadLink, Component.ENABLE, roles.toString());
						log.debug("downloadLink.isEnableAllowed=" + downloadLink.isEnableAllowed());

						String hintMsg = downloadLink.isEnableAllowed() ? "" : AppDownloadPanel.this.getLocalizer().getString("msg.noprivilege", AppDownloadPanel.this);
						hint.setDefaultModel(new Model<String>(hintMsg));
					} else {
						binaryStatus.setDefaultModel(new Model<String>(EMPTY_STATUS));
						MetaDataRoleAuthorizationStrategy.unauthorizeAll(downloadLink, Component.ENABLE);
						hint.setDefaultModel(new Model<String>(""));
					}
					target.addComponent(downloadLink);
					target.addComponent(binaryStatus);
					target.addComponent(hint);
				}
			});

			hint = new Label("hint", "");
			hint.setOutputMarkupId(true);
			add(hint);

			binaryStatus = new Label("binaryStatus", EMPTY_STATUS);
			binaryStatus.setOutputMarkupId(true);
			add(binaryStatus);
		}

	}

}

// $Id$