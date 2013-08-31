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
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.extensions.breadcrumb.IBreadCrumbModel;
import org.apache.wicket.extensions.breadcrumb.panel.BreadCrumbPanel;
import org.apache.wicket.extensions.breadcrumb.panel.BreadCrumbPanelLink;
import org.apache.wicket.extensions.breadcrumb.panel.IBreadCrumbPanelFactory;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.markup.html.link.ExternalLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.hp.sdf.ngp.model.AssetBinaryVersion;
import com.hp.sdf.ngp.service.ApplicationService;
import com.hp.sdf.ngp.ui.WicketApplication;
import com.hp.sdf.ngp.ui.page.testing.TestResultListPanel;
import com.hp.sdf.ngp.workflow.AssetBinaryVersionLifeCycleEngine;

@SuppressWarnings( { "unchecked" })
public class BinaryControlPanel extends BreadCrumbPanel {

	private static final long serialVersionUID = -4918503633298438L;

	private static final Log log = LogFactory.getLog(BinaryControlPanel.class);

	private final String EMPTY_STATUS = getLocalizer().getString("bin.nostatus", this);

	@SpringBean(name = "wicketApplication")
	private WicketApplication wicketApplication;

	@SpringBean
	private ApplicationService applicationService;

	@SpringBean
	private AssetBinaryVersionLifeCycleEngine assetBinaryVersionLifeCycleEngine;

	private Long appId;

	private long binaryCount;

	private BinaryForm binaryForm;

	private BinRequestPanel requestPanel;

	public long getBinaryCount() {
		return binaryCount;
	}

	public void setBinaryCount(long binaryCount) {
		this.binaryCount = binaryCount;
	}

	public BinaryControlPanel(String id, IBreadCrumbModel breadCrumbModel, Long appId) {
		super(id, breadCrumbModel);
		this.appId = appId;

		binaryForm = new BinaryForm("binaryForm");
		add(binaryForm);

		requestPanel = new BinRequestPanel("requestPanel");
		requestPanel.setOutputMarkupId(true);
		add(requestPanel);
	}

	public String getTitle() {
		return this.getLocalizer().getString("title", this, "Application Binaries");
	}

	public class BinaryForm extends Form {

		private static final long serialVersionUID = -6578579305600140145L;

		private final Log logger = BinaryControlPanel.this.log;

		// The platform value
		private String chosenVersion;

		private BreadCrumbPanelLink updateBinLink;

		private BreadCrumbPanelLink testResultLink;

		private Link delBinLink;

		public String getChosenVersion() {
			return chosenVersion;
		}

		public void setChosenVersion(String chosenVersion) {
			this.chosenVersion = chosenVersion;
		}

		// The binary id
		private Long binaryId;

		private ExternalLink downloadLink = null;

		private Label binaryStatus;

		private List<AssetBinaryVersion> appBinaries;

		// HashMap can't be serialized, so must use a loadable model to wrap it
		private LoadableDetachableModel versionBinaries = new LoadableDetachableModel<List<AssetBinaryVersion>>() {

			@Override
			protected List<AssetBinaryVersion> load() {

				
				log.debug("reloading binary versions...");
				List<AssetBinaryVersion> binVersions = applicationService.getAssetBinaryByAssetId(appId);
				if (log.isDebugEnabled()) {
					log.debug("reloaded bin size=" + (binVersions == null ? null : binVersions.size()));
				}
				return binVersions;
			}

		};

		public Long getBinaryId() {
			return binaryId;
		}

		public void setBinaryId(Long binaryId) {
			this.binaryId = binaryId;
		}

		public BinaryForm(String id) {

			super(id);
			// feedback panel
			// this.add(new FeedbackPanel("feedBack"));

			// renders app platform-version choices
			// initial the binary map
			final Map<Long, AssetBinaryVersion> binaries = new HashMap<Long, AssetBinaryVersion>();

			// The value list for binary
			IModel<List<? extends Long>> versionValues = new AbstractReadOnlyModel<List<? extends Long>>() {
				private static final long serialVersionUID = 1L;

				@Override
				public List<Long> getObject() {

					binaryId = null;

					List<AssetBinaryVersion> models = (List<AssetBinaryVersion>) versionBinaries.getObject();
					if (models == null || models.isEmpty()) {
						return Collections.emptyList();
					}

					List<Long> result = new ArrayList<Long>();

					binaries.clear();

					for (AssetBinaryVersion appBinary : models) {
						result.add(appBinary.getId());
						binaries.put(appBinary.getId(), appBinary);
					}
					logger.debug("versionValues size=" + result.size());
					return result;
				}
			};

			// The value list for binary id rendered to platform Name
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
			downloadLink = new ExternalLink("download", "");
			downloadLink.setOutputMarkupId(true);
			downloadLink.setEnabled(false);
			this.add(downloadLink);

			// Ajax update
			versionChoice.add(new AjaxFormComponentUpdatingBehavior("onchange") {
				private static final long serialVersionUID = 1L;

				@Override
				protected void onUpdate(AjaxRequestTarget target) {
					// do nothing, just use ajax to submit the value
					log.debug("bin id = " + binaryId);
					if (binaryId != null) {
						String binaryURI = wicketApplication.getUriPrefix();
						AssetBinaryVersion bin = applicationService.getAssetBinaryById(binaryId);
						binaryURI += bin.getLocation();
						log.debug("binary URI is: " + binaryURI);
						downloadLink.setDefaultModel(new Model<String>(binaryURI));
						downloadLink.setEnabled(true);
						updateBinLink.setEnabled(true);
						delBinLink.setEnabled(true);
						testResultLink.setEnabled(true);
						AssetBinaryVersion binary = applicationService.getAssetBinaryById(binaryId);
						binaryStatus.setDefaultModel(new Model<String>(binary.getStatus().getStatus()));
						requestPanel.update(binaryId);
					} else {
						downloadLink.setEnabled(false);
						updateBinLink.setEnabled(false);
						delBinLink.setEnabled(false);
						testResultLink.setEnabled(false);
						binaryStatus.setDefaultModel(new Model<String>(EMPTY_STATUS));
						requestPanel.update(null);
					}
					target.addComponent(downloadLink);
					target.addComponent(updateBinLink);
					target.addComponent(binaryStatus);
					target.addComponent(delBinLink);
					target.addComponent(testResultLink);
					target.addComponent(requestPanel);
				}
			});

			binaryStatus = new Label("binaryStatus", EMPTY_STATUS);
			binaryStatus.setOutputMarkupId(true);
			add(binaryStatus);

			BreadCrumbPanelLink newBinLink = new BreadCrumbPanelLink("newBinLink", BinaryControlPanel.this.getBreadCrumbModel(), new IBreadCrumbPanelFactory() {
				public BreadCrumbPanel create(String componentId, IBreadCrumbModel breadCrumbModel) {
					return new AppUploadPanel(componentId, breadCrumbModel, BinaryControlPanel.this.appId, null, BinaryControlPanel.this);
				}
			});
			add(newBinLink);

			updateBinLink = new BreadCrumbPanelLink("updateBinLink", BinaryControlPanel.this.getBreadCrumbModel(), new IBreadCrumbPanelFactory() {
				public BreadCrumbPanel create(String componentId, IBreadCrumbModel breadCrumbModel) {
					return new AppUploadPanel(componentId, breadCrumbModel, BinaryControlPanel.this.appId, binaryId, BinaryControlPanel.this);
				}
			});
			updateBinLink.setOutputMarkupId(true);
			updateBinLink.setEnabled(false);
			add(updateBinLink);
			updateBinLink.setVisible(false);

			delBinLink = new Link("delBinLink") {
				@Override
				public void onClick() {
					log.debug("delete binary id=" + binaryId);
					if (binaryId != null) {
						AssetBinaryVersion bin = applicationService.getAssetBinaryById(binaryId);
						applicationService.deleteAssetBinary(bin.getId());
					}
				}
			};
			delBinLink.setOutputMarkupId(true);
			delBinLink.setEnabled(false);
			add(delBinLink);
			delBinLink.setVisible(false);

			testResultLink = new BreadCrumbPanelLink("testResultLink", BinaryControlPanel.this.getBreadCrumbModel(), new IBreadCrumbPanelFactory() {
				public BreadCrumbPanel create(String componentId, IBreadCrumbModel breadCrumbModel) {
					return new TestResultListPanel(componentId, breadCrumbModel, binaryId);
				}
			});
			testResultLink.setOutputMarkupId(true);
			testResultLink.setEnabled(false);
			add(testResultLink);
			testResultLink.setVisible(false);
		}

		@Override
		protected void onBeforeRender() {
			super.onBeforeRender();
			this.updateModel();
			requestPanel.update(null);
		}

		private void updateModel() {

			// clean the chosenVersion
			chosenVersion = null;
			delBinLink.setEnabled(false);
			updateBinLink.setEnabled(false);
			downloadLink.setEnabled(false);
			testResultLink.setEnabled(false);

			// reload the versionBinaries via detach method
			versionBinaries.detach();

			if (versionBinaries != null && !((List<AssetBinaryVersion>) versionBinaries.getObject()).isEmpty()) {
				BinaryControlPanel.this.binaryCount = ((List<AssetBinaryVersion>) versionBinaries.getObject()).size();
				log.debug("binary versions , count=" + binaryCount);
			}

		}

	}

	@Override
	protected void onBeforeRender() {
		super.onBeforeRender();
		binaryForm.onBeforeRender();
	};

	
}

// $Id$