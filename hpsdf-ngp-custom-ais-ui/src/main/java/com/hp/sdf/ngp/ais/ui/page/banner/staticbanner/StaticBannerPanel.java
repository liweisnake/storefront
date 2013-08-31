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
package com.hp.sdf.ngp.ais.ui.page.banner.staticbanner;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.PageParameters;
import org.apache.wicket.RequestContext;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.HiddenField;
import org.apache.wicket.markup.html.form.RadioChoice;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.protocol.http.portlet.PortletRequestContext;
import org.apache.wicket.protocol.http.servlet.ServletWebRequest;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.hp.sdf.ngp.ais.ui.page.WicketPage;
import com.hp.sdf.ngp.ais.ui.page.banner.management.BannerListPage;
import com.hp.sdf.ngp.ais.ui.page.banner.rotatingbanner.RotatingBannerPage;
import com.hp.sdf.ngp.ais.ui.page.banner.tabbedbanner.TabbedBannerPage;
import com.hp.sdf.ngp.banner.BannerService;
import com.hp.sdf.ngp.banner.BannerStatus;
import com.hp.sdf.ngp.banner.BannerType;
import com.hp.sdf.ngp.banner.ContentType;
import com.hp.sdf.ngp.banner.model.BaseBanner;
import com.hp.sdf.ngp.banner.model.Content;
import com.hp.sdf.ngp.banner.model.StaticBanner;
import com.hp.sdf.ngp.banner.model.TabbedBanner;
import com.hp.sdf.ngp.ui.ShareEvent;

public class StaticBannerPanel extends Panel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4270878101227888057L;
	
	private static final Log log = LogFactory.getLog(StaticBannerPanel.class);
	
	@SpringBean
	private BannerService bannerService;
	
	private WicketPage page;

	WebMarkupContainer contentTable;
	
	WebMarkupContainer mainPanel;
	
	WebMarkupContainer submitPanel;
	
	WebMarkupContainer htmlPanel;

	WebMarkupContainer imagePanel;
	
	Button addContentLink;
	
	Button cancelBtn;
	
	Button previewBtn;
	
	RadioChoice contentTypeRadio;
	
	ContentListForm contentListForm;

	List<Content> cList = new ArrayList<Content>();

	Content c = new Content();
	
	StaticBanner OriginalBanner = null;
	
	private BannerStatus bannerStatus = BannerStatus.submit;

	public BannerStatus getBannerStatus() {
		return bannerStatus;
	}

	public void setBannerStatus(BannerStatus bannerStatus) {
		this.bannerStatus = bannerStatus;
	}

	public StaticBannerPanel(String id, PageParameters parameters) {
		super(id);
		
		ServletWebRequest request = (ServletWebRequest)getRequest();
		String prefix = "http://" 
			+ request.getHttpServletRequest().getServerName() 
			+ ":" + request.getHttpServletRequest().getServerPort() + request.getHttpServletRequest().getContextPath();
		HiddenField<String> urlPrefix = new HiddenField<String>("urlPrefix", new Model<String>(prefix));
		add(urlPrefix);

		String bannerId = parameters.getString("bannerId");
		if (bannerId != null) {
			OriginalBanner = (StaticBanner) bannerService.getBanner(parameters.getLong("bannerId"));
			if (OriginalBanner != null) {
				cList.clear();
				cList.add(OriginalBanner.getContent());
			}
			add(new HiddenField<String>("bannerId",new Model<String>(bannerId)));
		}else{
			add(new HiddenField<String>("bannerId",new Model<String>("-1")));
		}
		this.add(new FeedbackPanel("feedback"));
		this.add(new StaticBannerPanelForm("staticBannerPanelForm"));
		this.add(contentListForm = new ContentListForm("contentListForm"));
		contentListForm.setMultiPart(true);
		contentListForm.setVisible(false);
		
		//validation info
		add(new HiddenField<String>("contentNameNeed",new Model<String>(this.getLocalizer().getString("contentNameNeed", StaticBannerPanel.this))));
		add(new HiddenField<String>("contentNameLong",new Model<String>(this.getLocalizer().getString("contentNameLong", StaticBannerPanel.this))));
		add(new HiddenField<String>("urlLong",new Model<String>(this.getLocalizer().getString("urlLong", StaticBannerPanel.this))));
		add(new HiddenField<String>("urlFormat",new Model<String>(this.getLocalizer().getString("urlFormat", StaticBannerPanel.this))));
		add(new HiddenField<String>("ImageNeed",new Model<String>(this.getLocalizer().getString("ImageNeed", StaticBannerPanel.this))));
		add(new HiddenField<String>("selectImage",new Model<String>(this.getLocalizer().getString("selectImage", StaticBannerPanel.this))));
		add(new HiddenField<String>("htmlNeed",new Model<String>(this.getLocalizer().getString("htmlNeed", StaticBannerPanel.this))));
		add(new HiddenField<String>("selectZip",new Model<String>(this.getLocalizer().getString("selectZip", StaticBannerPanel.this))));
		add(new HiddenField<String>("bannerNameNeed",new Model<String>(this.getLocalizer().getString("bannerNameNeed", StaticBannerPanel.this))));
		add(new HiddenField<String>("bannerNameLong",new Model<String>(this.getLocalizer().getString("bannerNameLong", StaticBannerPanel.this))));
		add(new HiddenField<String>("contentNeed",new Model<String>(this.getLocalizer().getString("contentNeed", StaticBannerPanel.this))));
		add(new HiddenField<String>("bannerNameExist",new Model<String>(this.getLocalizer().getString("bannerNameExist", StaticBannerPanel.this))));
	}

	public final class StaticBannerPanelForm extends Form {
		/**
		 * 
		 */
		private static final long serialVersionUID = -8615421846909798315L;

		TabbedBanner b = new TabbedBanner();

		String bannerName = null;

		Integer bannerType = 0;

		String editFormName = null;

		String editFormType = null;

		TextField<String> nameTextField;

		RadioChoice<BannerType> bannerTypeChoice;

		ListView listView;
		
		Button editBtn;
		
		Button delBtn;
		
		String status ="submit";
		
		void init() {
			if (OriginalBanner != null) {
				bannerName = OriginalBanner.getName();
			}
		}

		public StaticBannerPanelForm(String id) {
			super(id);
			
			init();
			
			this.add(new HiddenField<String>("bannerStatus", new PropertyModel<String>(this, "status")));
			
			this.add(mainPanel = new WebMarkupContainer("mainPanel"));
			
			mainPanel.add(nameTextField = new TextField<String>("bannerName", new PropertyModel<String>(this, "bannerName")));

			bannerTypeChoice = new RadioChoice("bannerType", new PropertyModel<Integer>(this, "bannerType"), BannerType.getBannerTypeIdList(), new ChoiceRenderer() {
				public Object getDisplayValue(Object object) {
					Map bannerTypeMap = BannerType.getBannerTypeMap();
					return StaticBannerPanelForm.this.getLocalizer().getString(bannerTypeMap.get(object).toString(), StaticBannerPanel.this);
				}
			}) {
				public void onSelectionChanged() {
					log.debug("onSelectionChanged");
					int iSelected = Integer.parseInt(this.getInput());
					switch (iSelected) {
					case 0:
						break;
					case 1:
						setResponsePage(RotatingBannerPage.class);
						break;
					case 2:
						setResponsePage(TabbedBannerPage.class);
						break;
					}
				}

				protected boolean wantOnSelectionChangedNotifications() {
					return true;
				}
			};
			if(OriginalBanner!=null){
				bannerTypeChoice.setEnabled(false);
			}
			mainPanel.add(bannerTypeChoice);

			listView = new ListView("contentListView", cList) {
				@Override
				protected void populateItem(ListItem item) {
					final Content one = (Content) item.getModelObject();
					item.add(new Label("name", new PropertyModel<String>(item.getModel(), "name")));
					item.add(new Label("type", new Model<String>(this.getLocalizer().getString(one.getContentType().toString(), StaticBannerPanel.this))));
					item.add(editBtn = new Button("editBtn",new Model<String>(this.getLocalizer().getString("editBtn", StaticBannerPanel.this))) {
						@Override
						public void onSubmit() {
							log.debug("editBtn.onSubmit()");
							log.debug("Click Edit button." + one.getName() + "/" + one.getContentType()+"/"+one.getHref());
							contentListForm.setContentName(one.getName());
							contentListForm.setContentType(one.getContentType().ordinal());
							contentListForm.setHref(one.getHref());
							addContentLink.setVisible(false);
							mainPanel.setVisible(false);
							cancelBtn.setVisible(false);
							contentListForm.setVisible(true);
							submitPanel.setVisible(false);
							if(one.getContentType().equals(ContentType.html)){
								htmlPanel.setVisible(true);
								imagePanel.setVisible(false);
							}else if (one.getContentType().equals(ContentType.image)){
								htmlPanel.setVisible(false);
								imagePanel.setVisible(true);
							}
							//set radiochocie disabled.
							//contentTypeRadio.setEnabled(false);
						}
					});
					editBtn.setDefaultFormProcessing(false);
					item.add(delBtn = new Button("delBtn",new Model<String>(this.getLocalizer().getString("deleteBtn", StaticBannerPanel.this))) {
						@Override
						public void onSubmit() {
							log.debug("Click Canel button." + one.getName() + "/" + one.getContentType());
							cList.remove(one);
							if(cList.size()>0){
								addContentLink.setVisible(false);
								submitPanel.setVisible(true);
							}else{
								addContentLink.setVisible(true);
								submitPanel.setVisible(false);
							}
						}
					});
					delBtn.setDefaultFormProcessing(false);
				}
			};
			mainPanel.add(contentTable = new WebMarkupContainer("contentTable"));
			contentTable.add(listView);
			
			this.add(addContentLink = new Button("addContentBtn",new Model<String>(this.getLocalizer().getString("addContentBtn", StaticBannerPanel.this))){
				@Override
				public void onSubmit() {
					log.debug("AddContentBtn.onSubmit()");
					contentListForm.setContentName(null);
					contentListForm.setContentType(ContentType.image.ordinal());
					contentListForm.setHref(null);
					addContentLink.setVisible(false);
					mainPanel.setVisible(false);
					cancelBtn.setVisible(false);
					contentListForm.setVisible(true);
					submitPanel.setVisible(false);
					htmlPanel.setVisible(false);
					imagePanel.setVisible(true);
				}				
			});
			if (OriginalBanner != null) {
				addContentLink.setVisible(false);
			}
			
			addContentLink.setDefaultFormProcessing(false);
			
			this.add(submitPanel = new WebMarkupContainer("submitPanel"));
	        //Submit button
			submitPanel.add(new Button("submitBtn",new Model<String>(this.getLocalizer().getString("submitBtn", StaticBannerPanel.this))));
			//Preview button
			submitPanel.add(previewBtn = new Button("previewBtn",new Model<String>(this.getLocalizer().getString("previewBtn", StaticBannerPanel.this))));

			this.add(cancelBtn = new Button("cancelBtn",new Model<String>(this.getLocalizer().getString("cancelBtn", StaticBannerPanel.this))){
				@Override
				public void onSubmit() {
	        			RequestContext requestContext = RequestContext.get();
	        			if (requestContext instanceof PortletRequestContext) {
	        				ShareEvent shareEvent = new ShareEvent();
	        				((PortletRequestContext) requestContext).getPortletRequest().setAttribute(ShareEvent.class.getCanonicalName(), shareEvent);
	        			}
	            		setResponsePage(BannerListPage.class);
	            	}
			});
			cancelBtn.setDefaultFormProcessing(false);
		}
		
		protected void onSubmit() {
				log.debug("StaticBannerPanelForm.onSubmit");
				log.debug("~~~~~~~bannerName=" + bannerName + "~~~~~~~~");
				log.debug("~~~~~~~bannerType=" + bannerType + "~~~~~~~~");
				log.debug("~~~~~~~bannerStatus=" + status + "~~~~~~~~");
				if(status!=null && status.equalsIgnoreCase("submit")){
					StaticBannerPanel.this.setBannerStatus(BannerStatus.submit);
					log.debug("setBannerStatus(BannerStatus.submit)");
				}else if(status!=null && status.equalsIgnoreCase("preview")){
					StaticBannerPanel.this.setBannerStatus(BannerStatus.preview);
					log.debug("setBannerStatus(BannerStatus.preview)");
				}
				if (bannerName == null) {
					// error("Banner Name couldn't be empty!");
				} else if (cList == null || cList.size() < 1) {
					// error("Conent couldn't be empty!");
				} else if (OriginalBanner == null && bannerService.getBanner(bannerName) != null) {
					// error("The banner Name exist, please change!");
				} else if (OriginalBanner != null && bannerService.getBanner(bannerName) != null && !bannerName.equals(OriginalBanner.getName())) {
					// error("The banner Name exist, please change!");
				} else {
					if (OriginalBanner == null) {
						BaseBanner baseBanner = new BaseBanner(null, bannerName, "static banner", BannerType.staticBanner, StaticBannerPanel.this.getBannerStatus());
						StaticBanner target = new StaticBanner(baseBanner, cList.get(0));
						log.debug("++++++++++++" + target.getName() + "+++++++++++++++");
						log.debug("++++++++++++" + target.getBannerType() + "+++++++++++++++");
						log.debug("++++++++++++" + target.getContent().getName() + "+++++++++++++++");
						log.debug("++++++++++++" + target.getContent().getContentType() + "+++++++++++++++");
						log.debug("++++++++++++" + target.getContent() + "+++++++++++++++");
						bannerService.addBanner(target);
						RequestContext requestContext = RequestContext.get();
						switch(StaticBannerPanel.this.getBannerStatus()){
						case submit:
							if (requestContext instanceof PortletRequestContext) {
								ShareEvent shareEvent = new ShareEvent();
								((PortletRequestContext) requestContext).getPortletRequest().setAttribute(ShareEvent.class.getCanonicalName(), shareEvent);
							}
							setResponsePage(BannerListPage.class);
							break;
						case preview:
							if (requestContext instanceof PortletRequestContext) {
								ShareEvent shareEvent = new ShareEvent();
								shareEvent.setObject("bannerId", target.getId());
								log.debug("post a event[" + ShareEvent.class.getCanonicalName() + "] to portlet request");
								log.debug("bannerId = " + target.getId());
								((PortletRequestContext) requestContext).getPortletRequest().setAttribute(ShareEvent.class.getCanonicalName(), shareEvent);
							}
							PageParameters pageParameters = new PageParameters();
							pageParameters.put("bannerId", target.getId());
							StaticBannerPage staticBannerPage = new StaticBannerPage(pageParameters);
							setResponsePage(staticBannerPage);
							break;
						}
					} else {
						OriginalBanner.setName(bannerName);
						OriginalBanner.getContent().setName(cList.get(0).getName());
						OriginalBanner.getContent().setContentType(cList.get(0).getContentType());
						OriginalBanner.getContent().setFileName(cList.get(0).getFileName());
						OriginalBanner.getContent().setHref(cList.get(0).getHref());
						OriginalBanner.getContent().setBinary(cList.get(0).getBinary());
						OriginalBanner.setBannerStatus(StaticBannerPanel.this.getBannerStatus());
						bannerService.updateBanner(OriginalBanner);
						RequestContext requestContext = RequestContext.get();
						switch(StaticBannerPanel.this.getBannerStatus()){
						case submit:
							if (requestContext instanceof PortletRequestContext) {
								ShareEvent shareEvent = new ShareEvent();
								((PortletRequestContext) requestContext).getPortletRequest().setAttribute(ShareEvent.class.getCanonicalName(), shareEvent);
							}
							setResponsePage(BannerListPage.class);
							break;
						case preview:
							if (requestContext instanceof PortletRequestContext) {
								ShareEvent shareEvent = new ShareEvent();
								shareEvent.setObject("bannerId", OriginalBanner.getId());
								log.debug("post a event[" + ShareEvent.class.getCanonicalName() + "] to portlet request");
								log.debug("bannerId = " + OriginalBanner.getId());
								((PortletRequestContext) requestContext).getPortletRequest().setAttribute(ShareEvent.class.getCanonicalName(), shareEvent);
							}
							PageParameters pageParameters = new PageParameters();
							pageParameters.put("bannerId", OriginalBanner.getId());
							StaticBannerPage staticBannerPage = new StaticBannerPage(pageParameters);
							setResponsePage(staticBannerPage);
							break;
						}
					}

				}
		}
	}

	public class ContentListForm extends Form {

		Integer contentType = 0;

		String contentName;
		
		FileUploadField fileUploadField_html  ;
		
		FileUploadField fileUploadField_image  ;
		
		FileUpload f1;
		
		FileUpload f2;
		
		String href;

		public String getHref() {
			return href;
		}

		public void setHref(String href) {
			this.href = href;
		}

		public Integer getContentType() {
			return contentType;
		}

		public void setContentType(Integer contentType) {
			this.contentType = contentType;
		}

		public String getContentName() {
			return contentName;
		}

		public void setContentName(String contentName) {
			this.contentName = contentName;
		}

		Button save;

	    Button cancel;

		Content editC;

		public ContentListForm(String id) {

			super(id);

			this.add(new TextField<String>("contentName", new PropertyModel<String>(this, "contentName")));

			this.add(imagePanel = new WebMarkupContainer("imagePanel"));
			imagePanel.add(fileUploadField_image= new FileUploadField("image", new PropertyModel(this, "f2")));
			imagePanel.add(new TextField<String>("href", new PropertyModel<String>(this,"href")));
			
			this.add(htmlPanel = new WebMarkupContainer("htmlPanel"));
			htmlPanel.add(fileUploadField_html= new FileUploadField("html", new PropertyModel(this, "f1")));
			
			htmlPanel.setVisible(false);

			this.add(contentTypeRadio = new RadioChoice("contentType", new PropertyModel<Integer>(this, "contentType"), ContentType.getContentTypeIdList(), new ChoiceRenderer() {
				public Object getDisplayValue(Object object) {
					Map contentTypeMap = ContentType.getContentTypeMap();
					return ContentListForm.this.getLocalizer().getString(contentTypeMap.get(object).toString(), StaticBannerPanel.this);
				}
			}) {
				public void onSelectionChanged() {
					log.debug("onSelectionChanged");
					int iSelected = Integer.parseInt(this.getInput());
					switch (iSelected) {
					case 0:
						htmlPanel.setVisible(false);
						imagePanel.setVisible(true);
						break;
					case 1:
						htmlPanel.setVisible(true);
						imagePanel.setVisible(false);
						break;
					}
				}

				protected boolean wantOnSelectionChangedNotifications() {
					return true;
				}
			});

			this.add(save = new Button("save",new Model<String>(this.getLocalizer().getString("saveBtn", StaticBannerPanel.this))));
			this.add(cancel = new Button("cancel",new Model<String>(this.getLocalizer().getString("cancelBtn", StaticBannerPanel.this))) {
				@Override
				public void onSubmit() {
					log.debug("Click Cancel button!");
					contentName = null;
					contentType = 0;
					href = null;
					mainPanel.setVisible(true);
					cancelBtn.setVisible(true);
					contentListForm.setVisible(false);
					if(cList.size()>0){
						addContentLink.setVisible(false);
						submitPanel.setVisible(true);
					}else{
						addContentLink.setVisible(true);
						submitPanel.setVisible(false);
					}
					//set RadioChoice enabled
					//contentTypeRadio.setEnabled(true);
				}
			});
			cancel.setDefaultFormProcessing(false);
		}
		
		protected void onSubmit(){
			log.debug("Click save Button!");
			log.debug("---------------contentType=" + contentType + "----------------------");
			log.debug("---------------contentName=" + contentName + "----------------------");
			if (contentName == null) {
//				error("Content Name could not be empty!");
			} else if (contentName.length() > 100) {
//				error("content Name should be less 100");
//			} else if( contentType == ContentType.image.ordinal()&& href == null){
//				error("For image type, Href is required");
//			} else if( contentType == ContentType.image.ordinal()&& href.length()>100){
//				error("For image type, Href should be shorter than 100.");
			} else if( contentType == ContentType.image.ordinal()&& fileUploadField_image.getFileUpload() == null){
//				error("For image type, file is required");
			} else if( contentType == ContentType.html.ordinal()&& fileUploadField_html.getFileUpload() == null){
//				error("For html type, file is required");
			} else {
				if (contentType == ContentType.html.ordinal()) {
					if (fileUploadField_html.getFileUpload() != null) {
						editC = new Content(contentName, ContentType.getEnumNameStrByOrdinal(contentType), fileUploadField_html.getFileUpload().getBytes(), fileUploadField_html.getFileUpload()
								.getClientFileName(), "");
						String filename = fileUploadField_html.getFileUpload().getClientFileName();
						//if (filename.length()<=4 || !filename.substring(filename.length()-4,filename.length()).equalsIgnoreCase(".zip")){
						//	error("Please select a zip file.");
						//	return;
						//}
					} else {
						editC = new Content(contentName, ContentType.getEnumNameStrByOrdinal(contentType), null, null, null);
					}
				} else if (contentType == ContentType.image.ordinal()) {
					if (fileUploadField_image.getFileUpload() != null) {
						editC = new Content(contentName, ContentType.getEnumNameStrByOrdinal(contentType), fileUploadField_image.getFileUpload().getBytes(), fileUploadField_image.getFileUpload()
								.getClientFileName(), href);
						String filename = fileUploadField_image.getFileUpload().getClientFileName();
						//if (filename.length()<=4 || (!(filename.substring(filename.length()-4,filename.length())).equalsIgnoreCase(".JPG") 
						//		&& !(filename.substring(filename.length()-4,filename.length())).equalsIgnoreCase("JPEG"))){
						//	error("Please select a image.");
						//	return;
						//}
					} else {
						editC = new Content(contentName, ContentType.getEnumNameStrByOrdinal(contentType), null, null, href);
					}
				}
				for (int i = 0; i < cList.size(); i++) {
					log.debug("______cList=" + i + "___" + cList.get(i).getName() + "___" + cList.get(i).getContentType() + "__" + cList.get(i).getFileName());
				}
				if (cList.size() > 0) {
					cList.get(0).setName(editC.getName());
					cList.get(0).setContentType(editC.getContentType());
					cList.get(0).setHref(editC.getHref());
					cList.get(0).setFileName(editC.getFileName());
					cList.get(0).setBinary(editC.getBinary());
				} else {
					cList.add(editC);
				}
				for (int i = 0; i < cList.size(); i++) {
					log.debug("______cList=" + i + "___" + cList.get(i).getName() + "___" + cList.get(i).getContentType() + "__" + cList.get(i).getFileName());
				}
				//setVisable
				contentName = null;
				contentType = 0;
				href = null;
				mainPanel.setVisible(true);
				cancelBtn.setVisible(true);
				contentListForm.setVisible(false);
				submitPanel.setVisible(true);
				//set RadioChoice enabled
				//contentTypeRadio.setEnabled(true);
			}
		
		}
	}

}

// $Id$