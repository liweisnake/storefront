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
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.MarkupStream;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.protocol.http.portlet.PortletRequestContext;
import org.apache.wicket.protocol.http.servlet.ServletWebRequest;
import org.apache.wicket.spring.injection.annot.SpringBean;
import com.hp.sdf.ngp.banner.BannerService;
import com.hp.sdf.ngp.banner.model.Banner;
import com.hp.sdf.ngp.banner.model.Content;
import com.hp.sdf.ngp.banner.model.RotatingBanner;
import com.hp.sdf.ngp.banner.model.StaticBanner;
import com.hp.sdf.ngp.banner.model.TabbedBanner;

public class BannerViewPanel  extends Panel {
	
	private static final Log log = LogFactory.getLog(BannerViewPanel.class);
	
	private static final long serialVersionUID = 1L;
	
	@SpringBean
	private BannerService bannerService;
	
	private String bannerId;
	
	private Banner banner;
	
	private String height = "-1";
	
	public Banner getBanner() {
		return banner;
	}

	public void setBanner(Banner banner) {
		this.banner = banner;
	}

	public String getBannerId() {
		return bannerId;
	}

	public void setBannerId(String bannerId) {
		this.bannerId = bannerId;
	}

	public String getHeight() {
		return height;
	}

	public void setHeight(String height) {
		this.height = height;
	}

	public BannerViewPanel(String id, PageParameters parameters) {
		super(id);
		
		PortletRequestContext prc = (PortletRequestContext) RequestContext.get(); 
		this.setBannerId(prc.getPortletRequest().getPreferences().getValue("bannerId", "bannerId"));
		String height = prc.getPortletRequest().getPreferences().getValue("height", "height");
		if(height != null && !height.equals("height"))
			this.setHeight(prc.getPortletRequest().getPreferences().getValue("height", "height"));
//		this.setBannerId("3");

		if(this.getBannerId() != null && !this.getBannerId().equals("bannerId"))
			this.setBanner(this.bannerService.getBanner(new Long(this.getBannerId())));
		
        //Div area of Content
        WebMarkupContainer contentContainer = new WebMarkupContainer("contentContainer"){

			private static final long serialVersionUID = 1L;

			protected void onComponentTagBody(MarkupStream markupStream, ComponentTag openTag) {
        		if(banner == null)
        			super.onComponentTagBody(markupStream, openTag);
        		else
        			this.replaceComponentTagBody(markupStream, openTag, BannerViewPanel.this.getDiv(banner));
        	}
        };
        add(contentContainer);
	}
	
	public String getDiv(Banner banner){
		String result = "";
		Content content = null;
		List<Content> contents = null;
		
		ServletWebRequest request = (ServletWebRequest)getRequest();
		String prefix = "http://" 
			+ request.getHttpServletRequest().getServerName() 
			+ ":" + request.getHttpServletRequest().getServerPort();
		
		switch(banner.getBannerType()){
		case staticBanner:
			content = ((StaticBanner)banner).getContent();
			switch(content.getContentType()){
			case image:
				if(content.getHref() != null)
					result += "<a href='" + content.getHref() + "' target='_blank'>";
				result += "<img src='" + prefix + content.getPath() + "' ";
				if(!BannerViewPanel.this.getHeight().equals("-1"))
					result += " height='" + BannerViewPanel.this.getHeight() + "px'";
				result += ">";
				if(content.getHref() != null)
					result += "</a>";
				break;
			case html:
				result = "<iframe width='100%' frameborder=0 scrolling=no " 
					+ " src='" + prefix + content.getPath() + "' "
					+ " id='content_" + content.hashCode() + "'" 
					+ " name='content_" + content.hashCode() + "'" ;
				if(!BannerViewPanel.this.getHeight().equals("-1"))
					result += " height='" + BannerViewPanel.this.getHeight() + "px'";
				else
					result += " onload='this.height=content_" + content.hashCode() + ".document.body.scrollHeight'";
				result += " ></iframe>";
				break;
			}
			break;
		case rotatingBanner:
			content = BannerViewPanel.this.bannerService.getRotatingContent((RotatingBanner)banner);
			switch(content.getContentType()){
			case image:
				if(content.getHref() != null)
					result += "<a href='" + content.getHref() + "' target='_blank'>";
				result += "<img src='" + prefix + content.getPath() + "' ";
				if(!BannerViewPanel.this.getHeight().equals("-1"))
					result += " height='" + BannerViewPanel.this.getHeight() + "px'";
				result += ">";
				if(content.getHref() != null)
					result += "</a>";
				break;
			case html:
				result = "<iframe width='100%' frameborder=0 scrolling=no " 
					+ " src='" + prefix + content.getPath() + "' "
					+ " id='content_" + content.hashCode() + "'" 
					+ " name='content_" + content.hashCode() + "'" ;
				if(!BannerViewPanel.this.getHeight().equals("-1"))
					result += " height='" + BannerViewPanel.this.getHeight() + "px'";
				else
					result += " onload='this.height=content_" + content.hashCode() + ".document.body.scrollHeight'";
				result += " ></iframe>";
				break;
			}
			break;
		case tabbedBanner:
			contents = ((TabbedBanner)banner).getContents();
			if(contents == null || contents.size() == 0)
				break;
			boolean bFirst = true;
			String tab = "";
			String contentIds = "";
			String contentIdxs = "";
			int index = 0;
			for(Content c : contents){
				index++;
				String item = "";
				switch(c.getContentType()){
				case image:
					if(c.getHref() != null)
						item += "<a href='" + c.getHref() + "' target='_blank'>";
					item += "<img src='" + prefix + c.getPath() + "' ";
//					item = "<a href='" + c.getHref() + "' target='_blank'><img src='http://ocmpbuild3.chn.hp.com:8180/banner/20100727/1229/c68fd4da-8130-4ab4-a7af-56ac78da5bcf/image022.jpg' ";
					if(!BannerViewPanel.this.getHeight().equals("-1"))
						item += " height='" + BannerViewPanel.this.getHeight() + "px'";
					item += ">";
					if(c.getHref() != null)
						item += "</a>";
					break;
				case html:
					item = "<iframe width='100%' frameborder=0 scrolling=no " 
						+ " src='" + prefix + c.getPath() + "' "
//						+ " src='http://ocmpbuild3.chn.hp.com:8180/banner/20100727/1229/9b778ecc-1a1b-4a69-aa70-610863746239/aaa/index.html' "
						+ " id='content_" + c.hashCode() + "'" 
						+ " name='content_" + c.hashCode() + "'";
					if(!BannerViewPanel.this.getHeight().equals("-1"))
						item += " height='" + BannerViewPanel.this.getHeight() + "px'";
					else
						item += " onload='this.height=content_" + c.hashCode() + ".document.body.scrollHeight'";
					item += " ></iframe>";
					break;
				}
				result += "<div id='tabbedContent_" + c.hashCode() + "'";
				if(bFirst)
					bFirst = false;
				else
					result += " style='display:none'";
				result += ">";
				result += item;
				result += "</div>";
				
				contentIds += "'tabbedContent_" + c.hashCode() + "'";
				if(index < contents.size())
					contentIds += ",";
				contentIdxs += "'tabbedContentIdx_" + c.hashCode() + "'";
				if(index < contents.size())
					contentIdxs += ",";
				tab += "<a href=javascript:onSelectTab_" + banner.hashCode() + "(" + (index - 1) + ")>"
					+  "	<font size=3 id='tabbedContentIdx_" + c.hashCode() + "'>" + index 	+ "</font>"
					+  "</a>&nbsp;&nbsp;";
			}
			result 	+= "<br>" + tab + "<br>";
			result 	+= "<script>"
				   	+ "			var contents_" + banner.hashCode() + "=new Array(" + contentIds + ");"
				   	+ "			var contentIdxs_" + banner.hashCode() + "=new Array(" + contentIdxs + ");"
				   	+ "			var index_" + banner.hashCode() + "=0;";
			if(((TabbedBanner)banner).getAutoRotation())
				result 	+= "	autoChangeTab_" + banner.hashCode() + "();";
			result	+= "		function onSelectTab_" + banner.hashCode() + "(idx){"	
					+ "				var id=contents_" + banner.hashCode() + "[idx];"
					+ "				var idIdx=contentIdxs_" + banner.hashCode() + "[idx];"
					+ "				for(var i=0; i<contents_" + banner.hashCode() + ".length; i++){"
					+ "					document.getElementById(contents_" + banner.hashCode() + "[i]).style.display = 'none';"
					+ "					document.getElementById(contentIdxs_" + banner.hashCode() + "[i]).size = '1';"
					+ "				}"
					+ "				document.getElementById(id).style.display = 'block';"
					+ "				document.getElementById(idIdx).size = '3';"
					+ "				index_" + banner.hashCode() + "=idx;"
					+ "			}"
					+ "			function autoChangeTab_" + banner.hashCode() + "(){"
					+ "				var i = index_" + banner.hashCode() + "%(contents_" + banner.hashCode() + ".length);"
					+ "				onSelectTab_" + banner.hashCode() + "(i);"
					+ "				index_" + banner.hashCode() + "=index_" + banner.hashCode() + "+1;"
					+ "				setTimeout('autoChangeTab_" + banner.hashCode() + "()'," + ((TabbedBanner)banner).getIntervalRotation()*1000 + ");"
					+ "			}"
					+ "	   </script>";
			break;
		}
		return result;
	}

}

// $Id$