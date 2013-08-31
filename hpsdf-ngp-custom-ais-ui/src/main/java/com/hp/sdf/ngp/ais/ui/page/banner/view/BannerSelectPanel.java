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
package com.hp.sdf.ngp.ais.ui.page.banner.view;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.PageParameters;
import org.apache.wicket.RequestContext;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.protocol.http.portlet.PortletRequestContext;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.hp.sdf.ngp.ais.ui.page.banner.management.BannerListActionPanel;
import com.hp.sdf.ngp.ais.ui.page.banner.management.BannerListPage;
import com.hp.sdf.ngp.ais.ui.page.banner.rotatingbanner.RotatingBannerPanel;
import com.hp.sdf.ngp.banner.BannerService;
import com.hp.sdf.ngp.banner.BannerStatus;
import com.hp.sdf.ngp.banner.model.Banner;
import com.hp.sdf.ngp.banner.model.BaseBanner;

public class BannerSelectPanel  extends Panel {
	
	private static final Log log = LogFactory.getLog(BannerSelectPanel.class);
	
	private static final long serialVersionUID = 1L;
	
	private String bannerId;
	
	private String bannerName;
	
	private String location;
	
	private String height = "-1";
	
	private List<BaseBanner> banners;
	
	@SpringBean
	private BannerService bannerService;

	public String getBannerId() {
		return bannerId;
	}

	public void setBannerId(String bannerId) {
		this.bannerId = bannerId;
	}

	public String getBannerName() {
		return bannerName;
	}

	public void setBannerName(String bannerName) {
		this.bannerName = bannerName;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public List<BaseBanner> getBanners() {
		return banners;
	}

	public void setBanners(List<BaseBanner> banners) {
		this.banners = banners;
	}

	public String getHeight() {
		return height;
	}

	public void setHeight(String height) {
		this.height = height;
	}

	public BannerSelectPanel(String id, PageParameters parameters) {
		super(id);
		PortletRequestContext prc = (PortletRequestContext) RequestContext.get(); 
		String bannerId = prc.getPortletRequest().getPreferences().getValue("bannerId", "bannerId");
		String bannerName = prc.getPortletRequest().getPreferences().getValue("bannerName", "bannerName");
		String location = prc.getPortletRequest().getPreferences().getValue("location", "location");
		String height = prc.getPortletRequest().getPreferences().getValue("height", "height");
		this.setBannerId(bannerId);
		this.setBannerName(bannerName);
		this.setLocation(location);
		if(height != null && height != "height")
			this.setHeight(height);
		log.debug("Begin to Select Banner. Current Banner ID: " 
				+ this.getBannerId() + "; Name: " 
				+ this.getBannerName() + "; Location: " 
				+ this.getLocation() + "; Height: "
				+ this.getHeight());
		this.banners = BannerSelectPanel.this.bannerService.listBanner(BannerStatus.submit, 0, Integer.MAX_VALUE);
		BannerSelectForm form = new BannerSelectForm("bannerSelectForm");
		add(form);
	}
	
	public class BannerSelectForm extends Form {
		
		private static final long serialVersionUID = 1L;

		public BannerSelectForm(String id) {
			super(id);
			//Banner Location
			TextField<String> location = new TextField<String>("location", new PropertyModel<String>(BannerSelectPanel.this, "location"));
			location.setRequired(true);
			add(location);
			//Banner Height
			TextField<String> height = new TextField<String>("height", new PropertyModel<String>(BannerSelectPanel.this, "height"));
			height.setRequired(true);
			add(height);
            //Refresh link
    		add(new Link<String>("bnRefresh") {
  
    			private static final long serialVersionUID = 1L;

    			@Override
    			public void onClick() {
            		BannerSelectPanel.this.setBanners(BannerSelectPanel.this.bannerService.listBanner(0, Integer.MAX_VALUE));
            		log.debug("Banner List refreshed.");
            		setResponsePage(BannerSelectPage.class);
    			}
    		});
            
			//List View of Banner
			ListView<BaseBanner> listView = new ListView<BaseBanner>("listBanner", banners) {

				private static final long serialVersionUID = 1L;

				@Override
				protected void populateItem(ListItem<BaseBanner> item) {
					final BaseBanner banner = (BaseBanner) item.getModelObject();
					final Label labelName = new Label("name", banner.getName());
					String l = null;
					switch(banner.getBannerType()){
					case staticBanner:
						l = this.getLocalizer().getString("staticBanner", BannerSelectPanel.this);
						break;
					case rotatingBanner:
						l = this.getLocalizer().getString("rotatingBanner", BannerSelectPanel.this);
						break;
					case tabbedBanner:
						l = this.getLocalizer().getString("tabbedBanner", BannerSelectPanel.this);
						break;
					}
					final Label labelType = new Label("type", l);
	
		            Button bnSelect = new Button("bnSelect",new Model<String>(this.getLocalizer().getString("select", BannerSelectPanel.this))){

						private static final long serialVersionUID = 1L;
						
						protected String getOnClickScript(){
							return "onSelect('" + banner.getId() + "','" + banner.getName() + "');";
						}
		            };
		            
					item.add(labelName);
					item.add(labelType);
					item.add(bnSelect);
				}

			};
			add(listView);
		}

		public final void onSubmit() {
			String bannerId = getRequest().getParameter("bannerId");
			String bannerName = getRequest().getParameter("bannerName");
			String location = getRequest().getParameter("location");
			BannerSelectPanel.this.setBannerId(bannerId);
			BannerSelectPanel.this.setBannerName(bannerName);
			log.debug("Submit Portlet Preference. bannerId = " + bannerId 
					+ ". bannerName = " + bannerName 
					+ ". location = " + location);
		}
		
	}

}

// $Id$