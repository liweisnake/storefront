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

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.PageParameters;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.hp.sdf.ngp.model.Service;
import com.hp.sdf.ngp.service.ApiService;
import com.hp.sdf.ngp.ui.WicketApplication;

import edu.emory.mathcs.backport.java.util.Arrays;


@SuppressWarnings( { "unchecked" })
public abstract class ApiConfigurationPanel extends Panel {
	
	private static final Log log = LogFactory.getLog(ApiConfigurationPanel.class);

	private static final long serialVersionUID = -3341199475893674789L;

	@SpringBean
	private ApiService apiService;
	
	@SpringBean(name="wicketApplication")
	private WicketApplication wicketApplication;
	
	private String apiID;

	private String name;

	private String description;
	
	private String accessInterface;
	
	private String authType;
	
	private String brokerServiceName;
	
	private String brokerServiceUrl;
	
	private String docUrl;
	
	private String sdkUrl;
	
	private String serviceId;

	private String error;
	
	private String type;

	private int itemsPerPage;
	
	public ApiService getApiService() {
		return apiService;
	}

	public void setApiService(ApiService apiService) {
		this.apiService = apiService;
	}
	
	public String getApiID() {
		return apiID;
	}

	public void setApiID(String apiID) {
		this.apiID = apiID;
	}
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}
	
	public String getBrokerServiceName() {
		return brokerServiceName;
	}

	public void setBrokerServiceName(String brokerServiceName) {
		this.brokerServiceName = brokerServiceName;
	}

	public String getSdkUrl() {
		return sdkUrl;
	}

	public void setSdkUrl(String sdkUrl) {
		this.sdkUrl = sdkUrl;
	}

	public String getServiceId() {
		return serviceId;
	}

	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}

	public int getItemsPerPage() {
		return itemsPerPage;
	}

	public void setItemsPerPage(int itemsPerPage) {
		this.itemsPerPage = itemsPerPage;
	}

	public String getAccessInterface() {
		return accessInterface;
	}

	public void setAccessInterface(String accessInterface) {
		this.accessInterface = accessInterface;
	}

	public String getAuthType() {
		return authType;
	}

	public void setAuthType(String authType) {
		this.authType = authType;
	}

	public String getBrokerServiceUrl() {
		return brokerServiceUrl;
	}

	public void setBrokerServiceUrl(String url) {
		this.brokerServiceUrl = url;
	}

	public String getDocUrl() {
		return docUrl;
	}

	public void setDocUrl(String docUrl) {
		this.docUrl = docUrl;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public ApiConfigurationPanel(String id) {
		super(id);
		this.setItemsPerPage(wicketApplication.getItemsPerPage());
	}
	
	/**
	 * ApiDataView
	 */
	class ApiDataView<T extends ApiConfigurationPanel> extends DataView<Service> {
		
		private static final long serialVersionUID = 757869080803569521L;
		private ApiService apiService;
		private T t;

		protected ApiDataView(String id, IDataProvider<Service> dataProvider,ApiService apiService, int itemsPerPage, T t) {
			super(id, dataProvider, itemsPerPage);
			this.apiService = apiService;
			this.t = t;
		}

		@Override
		protected void populateItem(Item<Service> item) {
			final Service service = (Service) item.getModelObject();
			final Label labelName = new Label("name", service.getName());
			final Label labelDecs = new Label("description", service.getDescription());
			
			Link linkModify = new Link("linkModify") {

				private static final long serialVersionUID = 1411927945957148671L;

				@Override
				public void onClick() {
					PageParameters parameters = new PageParameters();
					parameters.add("id" + t.getClass(), service.getId().toString());
					parameters.add("name" + t.getClass(), service.getName());
					parameters.add("description" + t.getClass(), service.getDescription());
					parameters.add("accessInterface" + t.getClass(), service.getAccessInterface());
					parameters.add("authType" + t.getClass(), service.getBrokerServiceAuthType());
					parameters.add("brokerServiceName" + t.getClass(), service.getBrokerServiceName());
					parameters.add("brokerServiceUrl" + t.getClass(), service.getBrokerServiceUrl());
					parameters.add("docUrl" + t.getClass(), service.getDocUrl());
					parameters.add("sdkUrl" + t.getClass(), service.getSdkUrl());
					parameters.add("serviceId" + t.getClass(), service.getServiceid());
					parameters.add("type" + t.getClass(), service.getType());
					ApiConfigurationPage page = new ApiConfigurationPage(parameters);
					setResponsePage(page);
				}

			};
			linkModify.add(new Label("modify",this.getLocalizer().getString("modify", this)));

			Link linkDelete = new Link("linkDelete") {

				private static final long serialVersionUID = 3151822884415541213L;

				@Override
				public void onClick() {
					ApiDataView.this.apiService.deleteService(service.getId());
					setResponsePage(ApiConfigurationPage.class);
				}

			};
			linkDelete.add(new Label("delete",this.getLocalizer().getString("delete", this)));
			if(ApiDataView.this.apiService.isServiceSubscribed(service.getId()))
				linkDelete.setEnabled(false);
			item.add(labelName);
			item.add(labelDecs);
			item.add(linkModify);
			item.add(linkDelete);

		}
		
	}
	
	/**
	 * Api Form 
	 */
	class ApiForm<T extends ApiConfigurationPanel> extends Form {
		
		private static final long serialVersionUID = -6018911062435817105L;
		private ApiService apiService;
		private String operation;
		private T t;

		public String getOperation() {
			return operation;
		}

		public void setOperation(String operation) {
			this.operation = operation;
		}

		public ApiForm(String id, ApiService apiService, T t) {
			super(id);
			this.t = t;
			this.apiService = apiService;
			TextField<String> name = new TextField<String>("name", new PropertyModel<String>(t, "name"));
			TextArea<String> description  = new TextArea<String>("description", new PropertyModel<String>(t, "description"));
			TextField<String> serviceId = new TextField<String>("serviceId", new PropertyModel<String>(t, "serviceId"));
			TextField<String> sdkUrl = new TextField<String>("sdkUrl", new PropertyModel<String>(t, "sdkUrl"));
			TextField<String> docUrl = new TextField<String>("docUrl", new PropertyModel<String>(t, "docUrl"));
			TextField<String> brokerServiceName = new TextField<String>("brokerServiceName", new PropertyModel<String>(t, "brokerServiceName"));
			TextField<String> accessInterface = new TextField<String>("accessInterface", new PropertyModel<String>(t, "accessInterface"));
			TextField<String> authType = new TextField<String>("authType", new PropertyModel<String>(t, "authType"));
			TextField<String> url = new TextField<String>("brokerServiceUrl", new PropertyModel<String>(t, "brokerServiceUrl"));
			List ltType = Arrays.asList(new String[]{"SGF","COMMON"});
			DropDownChoice<String> type = new DropDownChoice<String>("type", new PropertyModel<String>(t, "type"), ltType);
			add(name);
			add(type);
			add(description);
			add(serviceId);
			add(sdkUrl);
			add(docUrl);
			add(brokerServiceName);
			add(accessInterface);
			add(authType);
			add(url);
			add(new Label("error",t.getError()));
			
			if(t.getApiID() == null || t.getApiID().equals("")){
				this.setOperation(this.getLocalizer().getString("add", t));
				add(new Label("title",this.getLocalizer().getString("titleAdd", t)));
			}
			else{
				this.setOperation(getLocalizer().getString("modify", t));
				add(new Label("title",this.getLocalizer().getString("titleModify", t)));
			}
			Button submit = new Button("submit", new PropertyModel<String>(this, "operation"));
			
			Button cancel = new Button("cancel"){

				private static final long serialVersionUID = 5619226352164254359L;

				public void onSubmit(){
					setResponsePage(ApiConfigurationPage.class);
				}
			};
			cancel.setDefaultFormProcessing(false);
			add(submit);
			add(cancel);
		}
		
		public final void onSubmit() {
			log.debug(t.getApiID());
			log.debug(t.getName());
			log.debug(t.getDescription());
			
			Service service = new Service();
			service.setId(null==t.getApiID()?null:new Long(t.getApiID()));
			service.setName(t.getName());
			service.setDescription(t.getDescription());
			service.setAccessInterface(t.getAccessInterface());
			service.setBrokerServiceAuthType(t.getAuthType());
			service.setBrokerServiceName(t.getBrokerServiceName());
			service.setBrokerServiceUrl(t.getBrokerServiceUrl());
			service.setDocUrl(t.getDocUrl());
			service.setSdkUrl(t.getSdkUrl());
			service.setServiceid(t.getServiceId());
			service.setType(t.getType());
			
			PageParameters parameters = new PageParameters();
			parameters.add("id" + t.getClass(), null==service.getId()?null:service.getId().toString());
			parameters.add("name" + t.getClass(), service.getName());
			parameters.add("description" + t.getClass(), service.getDescription());
			parameters.add("accessInterface" + t.getClass(), service.getAccessInterface());
			parameters.add("authType" + t.getClass(), service.getBrokerServiceAuthType());
			parameters.add("type" + t.getClass(), service.getType());
			parameters.add("brokerServiceUrl" + t.getClass(), service.getBrokerServiceUrl());
			parameters.add("docUrl" + t.getClass(), service.getDocUrl());
			parameters.add("brokerServiceName" + t.getClass(), service.getBrokerServiceName());
			parameters.add("sdkUrl" + t.getClass(), service.getSdkUrl());
			parameters.add("serviceId" + t.getClass(), service.getServiceid());
			
			if(service.getName() == null || service.getName().trim().length() == 0){
				parameters.add("error" + t.getClass(), this.getLocalizer().getString("null.name", this));
				setResponsePage(new ApiConfigurationPage(parameters));
				return;
			}
			
			if(service.getType() == null || service.getType().trim().length() == 0){
				parameters.add("error" + t.getClass(), this.getLocalizer().getString("null.type", this));
				setResponsePage(new ApiConfigurationPage(parameters));
				return;
			}

			try{
				if(t.getApiID() != null){
					this.apiService.updateService(service);
				}else{
					this.apiService.saveService(service);
				}
				setResponsePage(ApiConfigurationPage.class);
			}catch(javax.persistence.EntityExistsException e){
				parameters.add("error" + t.getClass(), this.getLocalizer().getString("exist.name", this));
				e.printStackTrace();
				setResponsePage(new ApiConfigurationPage(parameters));
			}catch(org.springframework.transaction.TransactionSystemException e){
				parameters.add("error" + t.getClass(), this.getLocalizer().getString("exist.name", this));
				e.printStackTrace();
				setResponsePage(new ApiConfigurationPage(parameters));
			}
		}
		
	}
}
