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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.authorization.strategies.role.metadata.MetaDataRoleAuthorizationStrategy;
import org.apache.wicket.extensions.breadcrumb.IBreadCrumbModel;
import org.apache.wicket.extensions.breadcrumb.panel.BreadCrumbPanel;
import org.apache.wicket.extensions.breadcrumb.panel.BreadCrumbPanelLink;
import org.apache.wicket.extensions.breadcrumb.panel.IBreadCrumbPanelFactory;
import org.apache.wicket.extensions.markup.html.tabs.AbstractTab;
import org.apache.wicket.extensions.markup.html.tabs.TabbedPanel;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.basic.MultiLineLabel;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.link.ExternalLink;
import org.apache.wicket.markup.html.link.PopupSettings;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.hp.sdf.ngp.common.exception.SGFCallingFailureException;
import com.hp.sdf.ngp.manager.ApiManager;
import com.hp.sdf.ngp.model.ApiKey;
import com.hp.sdf.ngp.model.Service;
import com.hp.sdf.ngp.sdp.SGFWebService;
import com.hp.sdf.ngp.sdp.model.SGFRestServiceMetadata;
import com.hp.sdf.ngp.service.ApiService;
import com.hp.sdf.ngp.ui.WicketApplication;
import com.hp.sdf.ngp.ui.WicketSession;
import com.hp.sdf.ngp.ui.provider.ApiCommonDataProvider;
import com.hp.sdf.ngp.ui.provider.ApiDataProvider;
import com.hp.sdf.ngp.ui.provider.ApiSgfDataProvider;
import com.hp.sdf.ngp.workflow.Privilege;

public class ApiSearchResultPanel extends BreadCrumbPanel {

	private static final Log log = LogFactory.getLog(ApiSearchResultPanel.class);

	private static final long serialVersionUID = -243743911127150859L;

	@SpringBean
	private ApiService apiService;

	@SpringBean
	private ApiManager apiManager;

	@SpringBean
	private SGFWebService sGFWebService;

	@SpringBean(name = "wicketApplication")
	private WicketApplication wicketApplication;

	private int itemsPerPage;

	public int getItemsPerPage() {
		return itemsPerPage;
	}

	public void setItemsPerPage(int itemsPerPage) {
		this.itemsPerPage = itemsPerPage;
	}

	// private ApiListForm form;

	/**
	 * ApiSearchResultPanel
	 * 
	 * @param id
	 * @param breadCrumbModel
	 */
	public ApiSearchResultPanel(String id, IBreadCrumbModel breadCrumbModel) {
		super(id, breadCrumbModel);
		ApiDataProvider apiAllDataProvider = new ApiDataProvider(this.apiService);
		ApiSgfDataProvider apiSgfDataProvider = new ApiSgfDataProvider(this.apiService);
		ApiCommonDataProvider apiCommonDataProvider = new ApiCommonDataProvider(this.apiService);
		this.setItemsPerPage(this.wicketApplication.getItemsPerPage());
		this.add(new ApiListForm("appListForm", apiAllDataProvider, apiSgfDataProvider, apiCommonDataProvider, this.apiManager, this.itemsPerPage));
	}

	public String getTitle() {
		return this.getLocalizer().getString("title", this, "Subscribe API");
	}

	/**
	 * ApiListForm
	 */
	class ApiListForm extends Form {

		private static final long serialVersionUID = -6018911062435817105L;
		private ApiDataView apiAllDataView;
		private ApiDataView apiSgfDataView;
		private ApiDataView apiCommonDataView;
		private ApiManager apiManager;

		public ApiListForm(String id, ApiDataProvider apiAllDataProvider, ApiSgfDataProvider apiSgfDataProvider, ApiCommonDataProvider apiCommonDataProvider, ApiManager apiManager, int itemsPerPage) {
			super(id);
			this.setMarkupId("apiListForm");
			this.apiAllDataView = new ApiDataView("apiAllDataView", apiAllDataProvider, itemsPerPage);
			// this.apiSgfDataView = new ApiDataView("apiSgfDataView",apiSgfDataProvider,itemsPerPage);
			// this.apiCommonDataView = new ApiDataView("apiCommonDataView",apiCommonDataProvider,itemsPerPage);

			List tabs = new ArrayList();
			tabs.add(new AbstractTab(new Model(this.getLocalizer().getString("service.type.all", ApiSearchResultPanel.this))) {
				public Panel getPanel(final String panelId) {
					return new ApiAllListPanel(panelId, ApiListForm.this.apiAllDataView);
				}
			});
			// tabs.add(new AbstractTab(new Model(this.getLocalizer().getString("service.type.sgf", ApiSearchResultPanel.this))) {
			// public Panel getPanel(final String panelId) { return new ApiSgfListPanel(panelId, ApiListForm.this.apiSgfDataView); }
			// });
			// tabs.add(new AbstractTab(new Model(this.getLocalizer().getString("service.type.common", ApiSearchResultPanel.this))) {
			// public Panel getPanel(final String panelId) { return new ApiCommonListPanel(panelId, ApiListForm.this.apiCommonDataView); }
			// });
			TabbedPanel tabPanel = new TabbedPanel("tabs", tabs);
			this.add(tabPanel);

			MultiLineLabel metaRequestGet = new MultiLineLabel("metaRequestGet");
			metaRequestGet.setOutputMarkupId(true);
			this.add(metaRequestGet);

			MultiLineLabel metaRequestPost = new MultiLineLabel("metaRequestPost");
			metaRequestPost.setOutputMarkupId(true);
			this.add(metaRequestPost);

			MultiLineLabel metaResponse = new MultiLineLabel("metaResponse");
			metaResponse.setOutputMarkupId(true);
			this.add(metaResponse);

			this.apiManager = apiManager;
			Button btnSubscribe = new Button("btnSubscribe");
			add(btnSubscribe);
			MetaDataRoleAuthorizationStrategy.authorize(btnSubscribe, Component.RENDER, Privilege.APISUBSCRIBE);

			BreadCrumbPanelLink subscribedApi = new BreadCrumbPanelLink("subscribedApi", ApiSearchResultPanel.this.getBreadCrumbModel(), new IBreadCrumbPanelFactory() {
				public BreadCrumbPanel create(String componentId, IBreadCrumbModel breadCrumbModel) {
					return new SubscribedApiPanel(componentId, breadCrumbModel);
				}
			});
			this.add(subscribedApi);

			MetaDataRoleAuthorizationStrategy.authorize(subscribedApi, Component.RENDER, Privilege.APISUBSCRIBE);
		}

		public final void onSubmit() {
			ApiKey appkey = new ApiKey();
			appkey.setSgName(String.valueOf(java.util.Calendar.getInstance().getTimeInMillis()));
			appkey.setSgPassword(UUID.randomUUID().toString());
			log.debug(appkey.getSgName());
			// log.debug(this.apiSgfDataView.subscibeMap.values());
			// log.debug(this.apiCommonDataView.subscibeMap.values());
			List<Service> services = new ArrayList<Service>();
			if (this.apiAllDataView != null && this.apiAllDataView.subscibeMap.values() != null) {
				Iterator<Service> it = this.apiAllDataView.subscibeMap.values().iterator();
				while (it.hasNext()) {
					Service s = it.next();
					if (s != null)
						services.add(s);
				}
			}
			if (this.apiSgfDataView != null && this.apiSgfDataView.subscibeMap.values() != null) {
				Iterator<Service> it = this.apiSgfDataView.subscibeMap.values().iterator();
				while (it.hasNext()) {
					Service s = it.next();
					if (s != null)
						services.add(s);
				}
			}
			if (this.apiCommonDataView != null && this.apiCommonDataView.subscibeMap.values() != null) {
				Iterator<Service> it = this.apiCommonDataView.subscibeMap.values().iterator();
				while (it.hasNext()) {
					Service s = it.next();
					if (s != null)
						services.add(s);
				}
			}
			String userID = WicketSession.get().getUserId();
			if (userID == null) {
				setResponsePage(ApiListPage.class);
				return;
			}

			try {
				this.apiManager.subscribe(appkey, services, userID);
			} catch (SGFCallingFailureException e) {
				e.printStackTrace();
			}
			if (this.apiAllDataView != null)
				this.apiAllDataView.subscibeMap.clear();
			if (this.apiSgfDataView != null)
				this.apiSgfDataView.subscibeMap.clear();
			if (this.apiCommonDataView != null)
				this.apiCommonDataView.subscibeMap.clear();
			activate(new IBreadCrumbPanelFactory() {
				public BreadCrumbPanel create(String componentId, IBreadCrumbModel breadCrumbModel) {
					return new SubscribedApiPanel(componentId, breadCrumbModel);
				}
			});
		}

	}

	/**
	 * ApiDataView
	 */
	class ApiDataView extends DataView<Service> {

		private static final long serialVersionUID = 757869080803569521L;
		private Map<String, Service> subscibeMap = new HashMap<String, Service>();

		protected ApiDataView(String id, IDataProvider<Service> dataProvider, int itemsPerPage) {
			super(id, dataProvider, itemsPerPage);
		}

		@Override
		protected void populateItem(Item<Service> item) {
			final Service service = item.getModelObject();
			CheckBox check = new CheckBox("checkBox", new IModel<Boolean>() {

				private static final long serialVersionUID = 1L;

				public Boolean getObject() {
					return null == ApiDataView.this.subscibeMap.get(service.getServiceid()) ? false : true;
				}

				public void setObject(Boolean object) {
					ApiDataView.this.subscibeMap.put(service.getServiceid(), object ? service : null);
				}

				public void detach() {
				}
			}) {

				private static final long serialVersionUID = 1L;

				// @Override
				// protected boolean wantOnSelectionChangedNotifications(){
				// return true;
				// }
			};

			item.add(check);

			MetaDataRoleAuthorizationStrategy.authorize(check, Component.RENDER, Privilege.APISUBSCRIBE);

			item.add(new Label("serviceName", service.getName() == null ? "" : service.getName()));
			item.add(new Label("accessInterface", service.getAccessInterface() == null ? "" : service.getAccessInterface()));
			item.add(new Label("authenticationType", service.getBrokerServiceAuthType() == null ? "" : service.getBrokerServiceAuthType()));
			item.add(new Label("serviceUrl", service.getBrokerServiceUrl() == null ? "" : service.getBrokerServiceUrl()));
			item.add(new Label("serviceDescription", service.getDescription() == null ? "" : service.getDescription()));

			final Label meta = new Label("txtMeta");
			meta.setOutputMarkupId(true);
			meta.setVisible(false);
			item.add(meta);

			HttpServletRequest request = this.getWebRequest().getHttpServletRequest();
			String docUrl = service.getDocUrl();
			if (docUrl != null && !((docUrl.indexOf("http://") >= 0) || (docUrl.indexOf("https://") >= 0)))
				docUrl = "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/content" + docUrl;

			PopupSettings popupSettings = new PopupSettings(PopupSettings.RESIZABLE | PopupSettings.SCROLLBARS).setHeight(800).setWidth(800);
			ExternalLink details = new ExternalLink("details", docUrl, this.getLocalizer().getString("details", this));
			details.setPopupSettings(popupSettings);
			details.setOutputMarkupId(true);
			if (service.getDocUrl() == null || service.getDocUrl().length() == 0)
				details.setVisible(false);
			item.add(details);

			AjaxLink lnMeta = new AjaxLink("lnMeta") {
				private static final long serialVersionUID = 1L;

				@Override
				public void onClick(AjaxRequestTarget target) {
					meta.setVisible(true);
					SGFRestServiceMetadata metadata = null;
					try {
						metadata = ApiSearchResultPanel.this.sGFWebService.getMetadataForRESTService(service.getServiceid());
					} catch (SGFCallingFailureException e) {
						e.printStackTrace();
					}

					StringBuffer requestGet = new StringBuffer();
					StringBuffer requestPost = new StringBuffer();
					StringBuffer response = new StringBuffer();
					if (metadata != null) {
						if (metadata.getSampleRequestGet() != null && metadata.getSampleRequestGet().size() > 0) {
							for (Object obj : metadata.getSampleRequestGet().toArray()) {
								requestGet.append("<br><table border=\"1\"><tbody>");
								SGFRestServiceMetadata.SampleRequestGet request = (SGFRestServiceMetadata.SampleRequestGet) obj;
								requestGet.append("<tr><td width=\"180px\">Content Type:</td><td width=\"600px\">" + request.getContentType() + "</td></tr>");
								requestGet.append("<tr><td colspan=\"2\">" + request.getMetadata() + "</td></tr>");
								requestGet.append("</tbody></table>");
							}
						}
						if (metadata.getSampleRequestPost() != null && metadata.getSampleRequestPost().size() > 0) {
							for (Object obj : metadata.getSampleRequestPost().toArray()) {
								requestPost.append("<br><table border=\"1\"><tbody>");
								SGFRestServiceMetadata.SampleRequestPost request = (SGFRestServiceMetadata.SampleRequestPost) obj;
								requestPost.append("<tr><td width=\"180px\">Content Type:</td><td width=\"600px\">" + request.getContentType() + "</td></tr>");
								requestPost.append("<tr><td colspan=\"2\">" + request.getMetadata() + "</td></tr>");
								requestPost.append("</tbody></table>");
							}
						}
						if (metadata.getSampleResponse() != null && metadata.getSampleResponse().keySet().size() > 0) {
							for (Object obj : metadata.getSampleResponse().values().toArray()) {
								SGFRestServiceMetadata.SampleResponse res = (SGFRestServiceMetadata.SampleResponse) obj;
								if (res.getResponseMeta() != null && res.getResponseMeta().size() > 0) {
									for (Object o : res.getResponseMeta().toArray()) {
										SGFRestServiceMetadata.SampleResponse.ResponseMeta meta = (SGFRestServiceMetadata.SampleResponse.ResponseMeta) o;
										response.append("<br><table border=\"1\"><tbody>");
										response.append("<tr><td width=\"180px\">Content Type:</td><td width=\"600px\">" + res.getContentType() + "</td></tr>");
										response.append("<tr><td>Status:</td><td>" + meta.getStatus() + "</td></tr>");
										response.append("<tr><td colspan=\"2\">" + meta.getMetadata() + "</td></tr>");
										response.append("</tbody></table>");
									}
								}
							}
						}
					}

					((MultiLineLabel) this.getParent().getParent().getParent().getParent().getParent().get(1)).setEscapeModelStrings(false);
					((MultiLineLabel) this.getParent().getParent().getParent().getParent().getParent().get(1)).setDefaultModel(new Model<String>(requestGet.toString()));
					((MultiLineLabel) this.getParent().getParent().getParent().getParent().getParent().get(2)).setEscapeModelStrings(false);
					((MultiLineLabel) this.getParent().getParent().getParent().getParent().getParent().get(2)).setDefaultModel(new Model<String>(requestPost.toString()));
					((MultiLineLabel) this.getParent().getParent().getParent().getParent().getParent().get(3)).setEscapeModelStrings(false);
					((MultiLineLabel) this.getParent().getParent().getParent().getParent().getParent().get(3)).setDefaultModel(new Model<String>(response.toString()));
					target.addComponent(this.getParent());
					target.addComponent(this.getParent().getParent().getParent().getParent().getParent().get(1));
					target.addComponent(this.getParent().getParent().getParent().getParent().getParent().get(2));
					target.addComponent(this.getParent().getParent().getParent().getParent().getParent().get(3));
				}

			};

			if (service.getAccessInterface() == null || service.getType() == null || service.getAccessInterface().equals(Service.ACCESS_INTERFACE_WEBSERVICE) || service.getType().equals(Service.SERVICE_TYPE_COMMON))
				lnMeta.setVisible(false);
			item.add(lnMeta);

		}

	}

}

// $Id$