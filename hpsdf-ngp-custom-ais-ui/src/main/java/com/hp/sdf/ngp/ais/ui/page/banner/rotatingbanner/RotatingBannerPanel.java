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
package com.hp.sdf.ngp.ais.ui.page.banner.rotatingbanner;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.PageParameters;
import org.apache.wicket.Request;
import org.apache.wicket.RequestContext;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.MarkupStream;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.HiddenField;
import org.apache.wicket.markup.html.form.RadioChoice;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.protocol.http.portlet.PortletRequestContext;
import org.apache.wicket.protocol.http.servlet.MultipartServletWebRequest;
import org.apache.wicket.protocol.http.servlet.ServletWebRequest;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.upload.FileItem;
import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.IValidator;

import com.hp.sdf.ngp.ais.ui.page.banner.management.BannerListPage;
import com.hp.sdf.ngp.ais.ui.page.banner.staticbanner.StaticBannerPage;
import com.hp.sdf.ngp.ais.ui.page.banner.tabbedbanner.TabbedBannerPage;
import com.hp.sdf.ngp.banner.BannerService;
import com.hp.sdf.ngp.banner.BannerStatus;
import com.hp.sdf.ngp.banner.BannerType;
import com.hp.sdf.ngp.banner.ContentType;
import com.hp.sdf.ngp.banner.model.Banner;
import com.hp.sdf.ngp.banner.model.BaseBanner;
import com.hp.sdf.ngp.banner.model.Content;
import com.hp.sdf.ngp.banner.model.ContentSet;
import com.hp.sdf.ngp.banner.model.ContentWeight;
import com.hp.sdf.ngp.banner.model.RotatingBanner;
import com.hp.sdf.ngp.ui.ShareEvent;

public class RotatingBannerPanel  extends Panel {
	
	private static final Log log = LogFactory.getLog(RotatingBannerPanel.class);
	
	private static final long serialVersionUID = 1L;
	
	@SpringBean
	private BannerService bannerService;
	
	private String bannerName;
	
	private RotatingBanner banner;
	
	private String error;
	
	private BannerStatus bannerStatus = BannerStatus.submit;

	public BannerStatus getBannerStatus() {
		return bannerStatus;
	}

	public void setBannerStatus(BannerStatus bannerStatus) {
		this.bannerStatus = bannerStatus;
	}

	public RotatingBanner getBanner() {
		return banner;
	}

	public void setBanner(RotatingBanner banner) {
		this.banner = banner;
	}

	public String getBannerName() {
		return bannerName;
	}

	public void setBannerName(String name) {
		this.bannerName = name;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public RotatingBannerPanel(String id, PageParameters parameters) {
		super(id);

		ServletWebRequest request = (ServletWebRequest)getRequest();
		String prefix = "http://" 
			+ request.getHttpServletRequest().getServerName() 
			+ ":" + request.getHttpServletRequest().getServerPort() + request.getHttpServletRequest().getContextPath();
		HiddenField<String> urlPrefix = new HiddenField<String>("urlPrefix", new Model<String>(prefix));
		add(urlPrefix);
		
		String bannerId = parameters.getString("bannerId");
		if(bannerId != null){
			log.debug("Update Rotating Banner. Banner ID: " + bannerId);
			Banner b = this.bannerService.getBanner(new Long(bannerId));
			this.setBanner( (null== b)? null:(RotatingBanner)b);
			this.setBannerName(b.getName());
			add(new HiddenField<String>("bannerId",new Model<String>(bannerId)));
		}else{
			add(new HiddenField<String>("bannerId",new Model<String>("-1")));
		}
		
		add(new FeedbackPanel("feedBack"));
		add(new RotatingBannerForm("rotatingBannerForm"));
	}
	
	public class RotatingBannerForm extends Form {
		
		private static final long serialVersionUID = 1L;
		
		private BannerType choise = BannerType.rotatingBanner;

		public BannerType getChoise() {
			return choise;
		}

		public void setChoise(BannerType choise) {
			this.choise = choise;
		}

		public RotatingBannerForm(String id) {
			super(id);
			//Banner Name
			TextField<String> bannerName = new TextField<String>("bannerName", new PropertyModel<String>(RotatingBannerPanel.this, "bannerName"));
			bannerName.setRequired(true);
			bannerName.add(new IValidator<String>() {

				private static final long serialVersionUID = 1L;

				public void validate(IValidatable<String> validatable) {
					final String value = validatable.getValue();
					//Banner name too long
					if(value.length() > 100)
						error(RotatingBannerPanel.this.getLocalizer().getString("tooLong", RotatingBannerPanel.this));
					//Banner name exists
					if(RotatingBannerPanel.this.getBanner() == null && RotatingBannerPanel.this.bannerService.getBanner(value) != null)
						error(RotatingBannerPanel.this.getLocalizer().getString("exists", RotatingBannerPanel.this));
				}
			});
			add(bannerName);	
						
			//Banner Type
			RadioChoice<BannerType> rc = new RadioChoice<BannerType>("bannerTypeChoice",
															new PropertyModel<BannerType>(RotatingBannerForm.this, "choise"), 
															Arrays.asList(BannerType.class.getEnumConstants()),
															new ChoiceRenderer() {
																public Object getDisplayValue(Object object) {
																	Map bannerTypeMap = BannerType.getBannerTypeMap2();
																	return RotatingBannerForm.this.getLocalizer().getString(bannerTypeMap.get(object).toString(), RotatingBannerPanel.this);
																}
															}){

				private static final long serialVersionUID = 1L;
				
				protected void onSelectionChanged(Object newSelection){
					BannerType t = (BannerType)newSelection;
					switch(t){
					case staticBanner:
						setResponsePage(StaticBannerPage.class);
						break;
					case rotatingBanner:
						break;
					case tabbedBanner:
						setResponsePage(TabbedBannerPage.class);
						break;
					}
				}
				
				protected boolean wantOnSelectionChangedNotifications() {
					return true;
				}
				
			};
			rc.setRequired(true);
			if(RotatingBannerPanel.this.getBanner() != null)
				rc.setEnabled(false);
            add(rc);
            
            //Add Content Set button
            Button bnAddSet = new Button("bnAddSet",new Model<String>(this.getLocalizer().getString("bnAddSet", RotatingBannerPanel.this))){

				private static final long serialVersionUID = 1L;

				protected String getOnClickScript(){
            		return "addContentSet();";
            	}
            	
            };
            add(bnAddSet);
            
            //Banner Status
//			HiddenField<String> status = new HiddenField<String>("bannerStatus", new PropertyModel<String>(RotatingBannerPanel.this, "bannerStatus"));
//			add(status);
            
            //Submit button
            Button submit = new Button("submit",new Model<String>(this.getLocalizer().getString("bnSubmit", RotatingBannerPanel.this)));
            add(submit);
            
            //Preview button
            Button preview = new Button("preview",new Model<String>(this.getLocalizer().getString("bnPreview", RotatingBannerPanel.this)));
            add(preview);
            
            //Cancel button
            Button cancel = new Button("cancel",new Model<String>(this.getLocalizer().getString("bnCancel", RotatingBannerPanel.this))){
            	public void onSubmit(){
        			RequestContext requestContext = RequestContext.get();
        			if (requestContext instanceof PortletRequestContext) {
        				ShareEvent shareEvent = new ShareEvent();
        				((PortletRequestContext) requestContext).getPortletRequest().setAttribute(ShareEvent.class.getCanonicalName(), shareEvent);
        			}
            		setResponsePage(BannerListPage.class);
            	}
            };
            cancel.setDefaultFormProcessing(false);
            add(cancel);
            
            //Div area of Content Set table
            WebMarkupContainer tableContainer = new WebMarkupContainer("tableContainer"){
            	
            	protected void onComponentTagBody(MarkupStream markupStream, ComponentTag openTag) {
            		if(RotatingBannerPanel.this.getBanner() == null)
            			super.onComponentTagBody(markupStream, openTag);
            		else
            			this.replaceComponentTagBody(markupStream, openTag, RotatingBannerForm.this.getDivTable(RotatingBannerPanel.this.getBanner()));
            	}
            };
            add(tableContainer);
            
            //Div area of Content Set and Content
            WebMarkupContainer areaContainer = new WebMarkupContainer("areaContainer"){
            	
            	protected void onComponentTagBody(MarkupStream markupStream, ComponentTag openTag) {
            		if(RotatingBannerPanel.this.getBanner() == null)
            			super.onComponentTagBody(markupStream, openTag);
            		else
            			this.replaceComponentTagBody(markupStream, openTag, RotatingBannerForm.this.getDivContentArea(RotatingBannerPanel.this.getBanner()));
            	}
            };
            add(areaContainer);
            
            add(new HiddenField<String>("msgContentNameNeeded",new Model<String>(this.getLocalizer().getString("msgContentNameNeeded", RotatingBannerPanel.this))));
            add(new HiddenField<String>("msgContentWeightNeeded",new Model<String>(this.getLocalizer().getString("msgContentWeightNeeded", RotatingBannerPanel.this))));
            add(new HiddenField<String>("msgContentWeightError",new Model<String>(this.getLocalizer().getString("msgContentWeightError", RotatingBannerPanel.this))));
            add(new HiddenField<String>("msgContentFileNeeded",new Model<String>(this.getLocalizer().getString("msgContentFileNeeded", RotatingBannerPanel.this))));
            add(new HiddenField<String>("msgContentImageNeeded",new Model<String>(this.getLocalizer().getString("msgContentImageNeeded", RotatingBannerPanel.this))));
            add(new HiddenField<String>("msgContentZipNeeded",new Model<String>(this.getLocalizer().getString("msgContentZipNeeded", RotatingBannerPanel.this))));
            add(new HiddenField<String>("msgSetNameNeeded",new Model<String>(this.getLocalizer().getString("msgSetNameNeeded", RotatingBannerPanel.this))));
            add(new HiddenField<String>("msgSetNameTooLong",new Model<String>(this.getLocalizer().getString("msgSetNameTooLong", RotatingBannerPanel.this))));
            add(new HiddenField<String>("msgTimeStartNeeded",new Model<String>(this.getLocalizer().getString("msgTimeStartNeeded", RotatingBannerPanel.this))));
            add(new HiddenField<String>("msgTimeStartError",new Model<String>(this.getLocalizer().getString("msgTimeStartError", RotatingBannerPanel.this))));
            add(new HiddenField<String>("msgTimeEndNeeded",new Model<String>(this.getLocalizer().getString("msgTimeEndNeeded", RotatingBannerPanel.this))));
            add(new HiddenField<String>("msgTimeEndError",new Model<String>(this.getLocalizer().getString("msgTimeEndError", RotatingBannerPanel.this))));
            add(new HiddenField<String>("msgTimeEndBigger",new Model<String>(this.getLocalizer().getString("msgTimeEndBigger", RotatingBannerPanel.this))));
            add(new HiddenField<String>("msgDueNeeded",new Model<String>(this.getLocalizer().getString("msgDueNeeded", RotatingBannerPanel.this))));
            add(new HiddenField<String>("msgDueError",new Model<String>(this.getLocalizer().getString("msgDueError", RotatingBannerPanel.this))));
            add(new HiddenField<String>("msgNoContent",new Model<String>(this.getLocalizer().getString("msgNoContent", RotatingBannerPanel.this))));
            add(new HiddenField<String>("msgBannerNameNeeded",new Model<String>(this.getLocalizer().getString("msgBannerNameNeeded", RotatingBannerPanel.this))));
            add(new HiddenField<String>("msgBannerNameTooLong",new Model<String>(this.getLocalizer().getString("msgBannerNameTooLong", RotatingBannerPanel.this))));
            add(new HiddenField<String>("msgBannerNameExists",new Model<String>(this.getLocalizer().getString("msgBannerNameExists", RotatingBannerPanel.this))));
            add(new HiddenField<String>("msgSetConflict",new Model<String>(this.getLocalizer().getString("msgSetConflict", RotatingBannerPanel.this))));
            add(new HiddenField<String>("contentTypeImage",new Model<String>(this.getLocalizer().getString("contentTypeImage", RotatingBannerPanel.this))));
            add(new HiddenField<String>("contentTypeHtml",new Model<String>(this.getLocalizer().getString("contentTypeHtml", RotatingBannerPanel.this))));
            add(new HiddenField<String>("bnEdit",new Model<String>(this.getLocalizer().getString("bnEdit", RotatingBannerPanel.this))));
            add(new HiddenField<String>("bnDelete",new Model<String>(this.getLocalizer().getString("bnDelete", RotatingBannerPanel.this))));
            add(new HiddenField<String>("dowMon",new Model<String>(this.getLocalizer().getString("dowMon", RotatingBannerPanel.this))));
            add(new HiddenField<String>("dowTue",new Model<String>(this.getLocalizer().getString("dowTue", RotatingBannerPanel.this))));
            add(new HiddenField<String>("dowWed",new Model<String>(this.getLocalizer().getString("dowWed", RotatingBannerPanel.this))));
            add(new HiddenField<String>("dowThu",new Model<String>(this.getLocalizer().getString("dowThu", RotatingBannerPanel.this))));
            add(new HiddenField<String>("dowFri",new Model<String>(this.getLocalizer().getString("dowFri", RotatingBannerPanel.this))));
            add(new HiddenField<String>("dowSat",new Model<String>(this.getLocalizer().getString("dowSat", RotatingBannerPanel.this))));
            add(new HiddenField<String>("dowSun",new Model<String>(this.getLocalizer().getString("dowSun", RotatingBannerPanel.this))));
		}

		public final void onSubmit() {
			RotatingBannerPanel.this.submitRotatingBanner(
					(null == RotatingBannerPanel.this.getBanner())?null:RotatingBannerPanel.this.getBanner().getId(),
					RotatingBannerPanel.this.getBannerName(), 
					getRequest(), 
					RotatingBannerPanel.this.bannerService);
		}
		
		private String getDivTable(RotatingBanner banner){
			String str = "<tbody>";
			if(banner == null || banner.getContentSets() == null || banner.getContentSets().size() == 0)
				return str + "</tbody>";
			for(int i = 0; i < banner.getContentSets().size(); i++){
				str += "<tr>";
				ContentSet set = (ContentSet)banner.getContentSets().toArray()[i];
				str += "<td>" + set.getName() + "</td>";
				
				String dow = set.getDow();
				String strDow = "";
				if(dow.charAt(0) == '1')
					strDow += this.getLocalizer().getString("dowMon", RotatingBannerPanel.this) + ",";
				if(dow.charAt(1) == '1')
					strDow += this.getLocalizer().getString("dowTue", RotatingBannerPanel.this) + ",";
				if(dow.charAt(2) == '1')
					strDow += this.getLocalizer().getString("dowWed", RotatingBannerPanel.this) + ",";
				if(dow.charAt(3) == '1')
					strDow += this.getLocalizer().getString("dowThu", RotatingBannerPanel.this) + ",";
				if(dow.charAt(4) == '1')
					strDow += this.getLocalizer().getString("dowFri", RotatingBannerPanel.this) + ",";
				if(dow.charAt(5) == '1')
					strDow += this.getLocalizer().getString("dowSat", RotatingBannerPanel.this) + ",";
				if(dow.charAt(6) == '1')
					strDow += this.getLocalizer().getString("dowSun", RotatingBannerPanel.this) + ",";
				if(strDow.lastIndexOf(",") > 0)
					strDow = strDow.substring(0, strDow.lastIndexOf(","));
				str += "<td align=center>" + strDow + "</td>";
				
				str += "<td align=center>" + set.getHodStart() + " - " + set.getHodEnd() + "</td>";
				str += "<td align=center>" + new SimpleDateFormat("yyyy-MM-dd").format(set.getExpiration()) + "</td>";
				str += "<td align=center><a href='javascript:editContentSet(" + (1000 + i) + "," + (i + 1) + ")'>" 
					+ this.getLocalizer().getString("bnEdit", RotatingBannerPanel.this) 
					+ "</a>&nbsp;"
					+ "<a href='javascript:deleteContentSet(" + (1000 + i) + "," + (i + 1) + ")'>" 
					+ this.getLocalizer().getString("bnDelete", RotatingBannerPanel.this) 
					+ "</a>&nbsp;</td>";
				str += "</tr>";
			}
			str += "</tbody>";
			return str;
		}
		
		private String getDivContentArea(RotatingBanner banner){
			String strSet = "<div id=divAddContentSetArea>";
			String strContent = "<div id=divAddContentArea>";
			if(banner == null || banner.getContentSets() == null || banner.getContentSets().size() == 0)
				return strSet + "</div>" + strContent + "</div>";

			for(int i = 0; i < banner.getContentSets().size(); i++){
				ContentSet set = (ContentSet)banner.getContentSets().toArray()[i];
				strSet += "" 
					+	"<div id=divAddContentSet__index_ style=DISPLAY:none>"
					+	"<div style=font-size:20px>" + this.getLocalizer().getString("contentSet", RotatingBannerPanel.this) + "</div>"
					+	"<input type=hidden id=contentSetId__index_ name=contentSetId__index_ value=" + set.getId() + ">"
					+	"<br>"
					+	"<div>" + this.getLocalizer().getString("contentSetName", RotatingBannerPanel.this) + ":</div>"
					+	"<div><input type=text id=contentSetName__index_ name=contentSetName__index_ size=30 value='" + set.getName() + "'></div>"
					+	"<br>"
					+	"<div>" + this.getLocalizer().getString("contentSetPeriod", RotatingBannerPanel.this) + ":</div>"
					+	"<div>"
					+		this.getLocalizer().getString("dow", RotatingBannerPanel.this) + ":"
					+	"	<input type=hidden id=contentSetDow__index_ name=contentSetDow__index_ value=" + set.getDow() + ">"
					+	"	<input type=checkbox id=contentSetDowMon__index_ name=contentSetDowMon__index_" + ((set.getDow().charAt(0)=='1')?" checked=checked":"") + ">"
					+		this.getLocalizer().getString("dowMon", RotatingBannerPanel.this)
					+	"	<input type=checkbox id=contentSetDowTue__index_ name=contentSetDowTue__index_" + ((set.getDow().charAt(1)=='1')?" checked=checked":"") + ">"
					+		this.getLocalizer().getString("dowTue", RotatingBannerPanel.this)
					+	"	<input type=checkbox id=contentSetDowWed__index_ name=contentSetDowWed__index_" + ((set.getDow().charAt(2)=='1')?" checked=checked":"") + ">"
					+		this.getLocalizer().getString("dowWed", RotatingBannerPanel.this)
					+	"	<input type=checkbox id=contentSetDowThu__index_ name=contentSetDowThu__index_" + ((set.getDow().charAt(3)=='1')?" checked=checked":"") + ">"
					+		this.getLocalizer().getString("dowThu", RotatingBannerPanel.this)
					+	"	<input type=checkbox id=contentSetDowFri__index_ name=contentSetDowFri__index_" + ((set.getDow().charAt(4)=='1')?" checked=checked":"") + ">"
					+		this.getLocalizer().getString("dowFri", RotatingBannerPanel.this)
					+	"	<input type=checkbox id=contentSetDowSat__index_ name=contentSetDowSat__index_" + ((set.getDow().charAt(5)=='1')?" checked=checked":"") + ">"
					+		this.getLocalizer().getString("dowSat", RotatingBannerPanel.this)
					+	"	<input type=checkbox id=contentSetDowSun__index_ name=contentSetDowSun__index_" + ((set.getDow().charAt(6)=='1')?" checked=checked":"") + ">"
					+		this.getLocalizer().getString("dowSun", RotatingBannerPanel.this)
					+	"</div>"
					+	"<div>"
					+		this.getLocalizer().getString("time", RotatingBannerPanel.this) + ":"
					+		this.getLocalizer().getString("timeStart", RotatingBannerPanel.this) + ":"
					+	"	<input type=text id=contentSetTimeStart__index_ name=contentSetTimeStart__index_ size=10 value=" + set.getHodStart() + ">&nbsp;"
					+		this.getLocalizer().getString("timeEnd", RotatingBannerPanel.this) + ":"
					+	"	<input type=text id=contentSetTimeEnd__index_ name=contentSetTimeEnd__index_ size=10 value=" + set.getHodEnd() + ">&nbsp;"
					+	this.getLocalizer().getString("exTime", RotatingBannerPanel.this)
					+	"</div>"
					+	"<br>"
					+	"<div>"
					+		this.getLocalizer().getString("expiration", RotatingBannerPanel.this) + ":"
					+	"</div>"
					+	"<div>"
					+	"	<input type=text id=contentSetDue__index_ name=contentSetDue__index_ size=30 value=" + set.getExpiration() + ">"
					+	this.getLocalizer().getString("exDue", RotatingBannerPanel.this)
					+	"</div>"
					+	"<br>"
					+	"<div>"
					+		this.getLocalizer().getString("content", RotatingBannerPanel.this) + ":&nbsp;&nbsp;"
					+	"	<a class='btnBlueShort' href='javascript:addContent(_index_);'>" + this.getLocalizer().getString("bnAddContent", RotatingBannerPanel.this) + "</a>"
//					+	"	<input type=button value='Add Content' id=bnAddContent onclick=addContent(_index_);>"
					+	"</div>"
					+	"<div class=panelContent>"
					+	"<table id=tableContent__index_ width=600px cellpadding=0 cellspacing=0 border=1 class=formTableSet>"
					+	"	<thead>"
					+	"		<tr>"
					+	"			<td align=center>" + this.getLocalizer().getString("name", RotatingBannerPanel.this) + "</td>"
					+	"			<td align=center>" + this.getLocalizer().getString("type", RotatingBannerPanel.this) + "</td>"
					+	"			<td align=center>" + this.getLocalizer().getString("weight", RotatingBannerPanel.this) + "</td>"
					+	"			<td align=center>" + this.getLocalizer().getString("operation", RotatingBannerPanel.this) + "</td>"
					+	"		</tr>"
					+	"	</thead>"
					+	"	<tbody>";
				
				for(int j = 0; j < set.getContentWeights().size(); j++ ){
					ContentWeight weight = (ContentWeight)set.getContentWeights().toArray()[j];
					strSet += "<tr>";
					strSet += "<td>" + weight.getContent().getName() + "</td>";
					switch(weight.getContent().getContentType()){
					case image:
						strSet += "<td align=center>" + this.getLocalizer().getString("contentTypeImage", RotatingBannerPanel.this) + "</td>";
						break;
					case html:
						strSet += "<td align=center>" + this.getLocalizer().getString("contentTypeHtml", RotatingBannerPanel.this) + "</td>";
						break;
					}
					strSet += "<td align=center>" + weight.getWeight() + "</td>";
					strSet += "<td align=center><a href='javascript:editContent(_index_,_indexContent_," + (j + 1) + ")'>" 
							+ this.getLocalizer().getString("bnEdit", RotatingBannerPanel.this) 
							+ "</a>&nbsp;"
							+ "<a href='javascript:deleteContent(_index_,_indexContent_," + (j + 1) + ")'>" 
							+ this.getLocalizer().getString("bnDelete", RotatingBannerPanel.this) 
							+ "</a>&nbsp;</td>";
					strSet += "</tr>";
					strSet = strSet.replaceAll("_indexContent_", String.valueOf(1000 + j));
					
					strContent += ""
						+	"<div id=divAddContent__index___indexContent_ style=DISPLAY:none>"
						+	"	<div style=font-size:20px>" + this.getLocalizer().getString("content", RotatingBannerPanel.this) + "</div>"
						+	"	<input type=hidden id=contentSet__index_::contentWeightId__indexContent_ " 
						+	"		name=contentSet__index_::contentWeightId__indexContent_ value=" + weight.getId() + ">"
						+	"	<input type=hidden id=contentSet__index_::contentId__indexContent_ " 
						+	"		name=contentSet__index_::contentId__indexContent_ value=" + weight.getContent().getId() + ">"
						+	"	<br>"
						+	"	<div>" + this.getLocalizer().getString("contentName", RotatingBannerPanel.this) + ":</div>"
						+	"	<div>"
						+	"		<input type=text id=contentSet__index_::contentName__indexContent_ "
						+	"			name=contentSet__index_::contentName__indexContent_ size=30 value='" + weight.getContent().getName() + "'>" 
						+	"	</div>"
						+	"	<br>"
						+	"	<div>" + this.getLocalizer().getString("contentType", RotatingBannerPanel.this) + ":</div>"
						+	"	<div>"
						+	"		<input type=radio id=contentSet__index_::contentType__indexContent_ " 
						+	"			name=contentSet__index_::contentType__indexContent_ value=0 " + (weight.getContent().getContentType().equals(ContentType.image)?"checked ":" ")
						+	"			onclick=document.getElementById('divContentClickLink__indexContent_').style.display='block'>"
						+					this.getLocalizer().getString("contentTypeImage", RotatingBannerPanel.this)
						+	"	</div>"
						+	"	<div>"
						+	"		<input type=radio id=contentSet__index_::contentType__indexContent_ " 
						+	"			name=contentSet__index_::contentType__indexContent_ value=1 " + (weight.getContent().getContentType().equals(ContentType.html)?"checked ":" ")
						+	"			onclick=document.getElementById('divContentClickLink__indexContent_').style.display='none'>"
						+					this.getLocalizer().getString("contentTypeHtml", RotatingBannerPanel.this)
						+	"	</div>"
						+	"	<br>"
						+	"	<div>" + this.getLocalizer().getString("uploadFile", RotatingBannerPanel.this) + ":</div>"
						+	"	<div>" 
						+	"		<input type=file id=contentSet__index_::contentFile__indexContent_ name=contentSet__index_::contentFile__indexContent_>" 
						+	"	</div>"
						+	"	<br>"
						+	"	<div id=divContentClickWeight__indexContent_>"
						+			this.getLocalizer().getString("weight", RotatingBannerPanel.this) + ":<br>"
						+	"		<input type=text id=contentSet__index_::contentWeight__indexContent_ " 
						+	"			name=contentSet__index_::contentWeight__indexContent_ size=30 value=" + weight.getWeight() + ">"
						+			this.getLocalizer().getString("exWeight", RotatingBannerPanel.this) 
//						+	"		Ex: 0.25"
						+	"	</div>"
						+	"	<br>"
						+	"	<div id=divContentClickLink__indexContent_ " + (weight.getContent().getContentType().equals(ContentType.html)?"style='display:none'":"") + ">"
						+			this.getLocalizer().getString("clickLink", RotatingBannerPanel.this) + ":<br>"
						+	"		<input type=text id=contentSet__index_::contentLink__indexContent_ " 
						+	"			name=contentSet__index_::contentLink__indexContent_ size=30 value=" + weight.getContent().getHref() + ">"
						+			this.getLocalizer().getString("exLink", RotatingBannerPanel.this) 
//						+	"		Ex: http://www.google.com"
						+	"	</div>"
						+	"	<br>"
						+	"	<a class='btnBlueShort' href='javascript:saveContent(_index_,_indexContent_);'>" + this.getLocalizer().getString("bnSave", RotatingBannerPanel.this) + "</a>"
//						+	"	<input type=button value=Save onclick=saveContent(_index_,_indexContent_);>&nbsp;"
						+	"	<!-- "
						+	"	<input type=button value=Cancel onclick=cancelContent(_index_,_indexContent_);>"
						+	"	-->"
						+	"</div>";
					strContent = strContent.replaceAll("_indexContent_", String.valueOf(1000 + j));
				}
				
				strSet += ""
					+	"	</tbody>"
					+	"</table>"
					+	"</div>"
					+	"<br>"
					+	"<a class='btnBlueShort' href='javascript:saveContentSet(_index_);'>" + this.getLocalizer().getString("bnSave", RotatingBannerPanel.this) + "</a>"
//					+	"<input type=button value=Save onclick=saveContentSet(_index_);>&nbsp;"
					+	"<!-- "
					+	"<input type=button value=Cancel onclick=cancelContentSet();>"
					+	"-->"
					+	"</div>";
				strSet = strSet.replaceAll("_index_", String.valueOf(1000 + i));
				strContent = strContent.replaceAll("_index_", String.valueOf(1000 + i));
			}
			
			strSet += "</div>";
			strContent += "</div>";
			return strSet + strContent;
		}
	
	}

	/**
	 * Submit Rotating Banner
	 * @param name
	 * @param req
	 * @param service
	 */
	public void submitRotatingBanner(Long id, String name, Request req, BannerService service){
		String status = req.getParameter("bannerStatus");
		if(status.equals("0"))
			RotatingBannerPanel.this.setBannerStatus(BannerStatus.submit);
		else if(status.equals("1"))
			RotatingBannerPanel.this.setBannerStatus(BannerStatus.preview);
		log.debug("Submit Ratating Banner. Status = " + RotatingBannerPanel.this.getBannerStatus().toString());
		
		Map<String, String[]> mapParameter = req.getParameterMap();
		Map<String, String> mapFileName = new HashMap<String, String>();
		Map<String, byte[]> mapFileBinary = new HashMap<String, byte[]>();
		List<String> keyContentSetName = new ArrayList<String>();
		List<String> keyContentSetId = new ArrayList<String>();
		List<String> keyContentSetDow = new ArrayList<String>();
		List<String> keyContentSetTimeStart = new ArrayList<String>();
		List<String> keyContentSetTimeEnd = new ArrayList<String>();
		List<String> keyContentSetDue = new ArrayList<String>();
		List<String> keyContentName = new ArrayList<String>();
		List<String> keyContentId = new ArrayList<String>();
		List<String> keyContentWeightId = new ArrayList<String>();
		List<String> keyContentType = new ArrayList<String>();
		List<String> keyContentLink = new ArrayList<String>();
		List<String> keyContentWeight = new ArrayList<String>();
		
		//Fetch parameters
		Iterator<String> itParameter = mapParameter.keySet().iterator();
		while(itParameter.hasNext()){
			String key = itParameter.next();
			if(key.indexOf("contentSetName_") > -1 && key.indexOf("contentSetName__index_") < 0)
				keyContentSetName.add(key);
			else if(key.indexOf("contentSetId_") > -1 && key.indexOf("contentSetId__index_") < 0)
				keyContentSetId.add(key);
			else if(key.indexOf("contentSetDow_") > -1 && key.indexOf("contentSetDow__index_") < 0)
				keyContentSetDow.add(key);
			else if(key.indexOf("contentSetTimeStart_") > -1 && key.indexOf("contentSetTimeStart__index_") < 0)
				keyContentSetTimeStart.add(key);
			else if(key.indexOf("contentSetTimeEnd_") > -1 && key.indexOf("contentSetTimeEnd__index_") < 0)
				keyContentSetTimeEnd.add(key);
			else if(key.indexOf("contentSetDue_") > -1 && key.indexOf("contentSetDue__index_") < 0)
				keyContentSetDue.add(key);
			else if(key.indexOf("::contentName_") > -1 && key.indexOf("::contentName__index_") < 0)
				keyContentName.add(key);
			else if(key.indexOf("::contentId_") > -1 && key.indexOf("::contentId__index_") < 0)
				keyContentId.add(key);
			else if(key.indexOf("::contentWeightId_") > -1 && key.indexOf("::contentWeightId__index_") < 0)
				keyContentWeightId.add(key);
			else if(key.indexOf("::contentType_") > -1 && key.indexOf("::contentType__index_") < 0)
				keyContentType.add(key);
			else if(key.indexOf("::contentLink_") > -1 && key.indexOf("::contentLink__index_") < 0)
				keyContentLink.add(key);
			else if(key.indexOf("::contentWeight_") > -1 && key.indexOf("::contentWeight__index_") < 0)
				keyContentWeight.add(key);
		}
		
		//Fetch upload files
		MultipartServletWebRequest request = (MultipartServletWebRequest)req;
		Collection<String> strFiles = request.getFiles().keySet();
		for(String strFile : strFiles){
			try {
				FileItem file = request.getFiles().get(strFile);
				InputStream is = file.getInputStream();
				java.io.ByteArrayOutputStream os = new java.io.ByteArrayOutputStream();
		        
		        byte[] buffer = new byte[8192];
		        int count = 0;
		        while((count = is.read(buffer)) >0){
		        	os.write(buffer, 0, count);
		        }
		        byte[] binary = os.toByteArray();
		        mapFileName.put(strFile, file.getName());
		        mapFileBinary.put(strFile, binary);
		        os.close();
			} catch (IOException exception) {
				exception.printStackTrace();
			}
		}
		
		//Construct Rotating Banner model
		RotatingBanner banner = new RotatingBanner(
				new BaseBanner(null,name,"",BannerType.rotatingBanner, RotatingBannerPanel.this.getBannerStatus()), 
				null);
		
		
		//Construct Content Set model
		Set<ContentSet> contentSets = new HashSet<ContentSet>();
		
		for(int i = 0; i < keyContentSetName.size(); i++){
			String strContentSet = "contentSet_" + keyContentSetName.get(i).substring(keyContentSetName.get(i).indexOf("_") + 1);
			ContentSet contentSet = null;
			try {
				contentSet = new ContentSet(
						req.getParameters(keyContentSetName.get(i))[0],
						req.getParameters(keyContentSetDow.get(i))[0],
						req.getParameters(keyContentSetTimeStart.get(i))[0],
						req.getParameters(keyContentSetTimeEnd.get(i))[0],
						new SimpleDateFormat("yyyy-MM-dd").parse(req.getParameters(keyContentSetDue.get(i))[0].trim())
						);
				contentSet.setBanner(banner);
				if(!req.getParameters(keyContentSetId.get(i))[0].equals("-1"))
					contentSet.setId(new Long(req.getParameters(keyContentSetId.get(i))[0]));
			} catch (ParseException exception) {
				log.error(keyContentSetDue.get(i));
				log.error(req.getParameters(keyContentSetDue.get(i))[0]);
				exception.printStackTrace();
			}
			
			//Construct Content model
			Set<ContentWeight> weights = new HashSet<ContentWeight>();
			List<String> subListKeyContentName = new ArrayList<String>();
			for(String key : keyContentName){
				if(key.indexOf(strContentSet + "::") > -1)
					subListKeyContentName.add(key);
			}
			for(int j = 0; j < subListKeyContentName.size(); j++){
				String idx = subListKeyContentName.get(j).substring(subListKeyContentName.get(j).lastIndexOf("_") + 1);
				log.debug(idx);
				Content content = null;
				ContentWeight weight = null;
				content = new Content(
						req.getParameters(strContentSet + "::contentName_" + idx)[0],
						ContentType.getEnumNameStrByOrdinal(new Integer(req.getParameters(strContentSet + "::contentType_" + idx)[0]).intValue()),
						mapFileBinary.get(strContentSet + "::contentFile_" + idx),
						mapFileName.get(strContentSet + "::contentFile_" + idx),
						req.getParameters(strContentSet + "::contentLink_" + idx)[0]
						);
				weight = new ContentWeight(
						contentSet, 
						content, 
						new Float(req.getParameters(strContentSet + "::contentWeight_" + idx)[0]));
				if(!req.getParameters(strContentSet + "::contentId_" + idx)[0].equals("-1")){
					content.setId(new Long(req.getParameters(strContentSet + "::contentId_" + idx)[0]));
					content.setReferenceId(new Long(req.getParameters(strContentSet + "::contentWeightId_" + idx)[0]));
					weight.setId(new Long(req.getParameters(strContentSet + "::contentWeightId_" + idx)[0]));
				}
				weights.add(weight);
			}
			contentSet.setContentWeights(weights);
			contentSets.add(contentSet);
			
		}

		banner.setContentSets(contentSets);
		if(id == null){
			service.addBanner(banner);
		}
		else{
			banner.setId(id);
			service.updateBanner(banner);
		}
		
		RequestContext requestContext = RequestContext.get();
		switch(RotatingBannerPanel.this.getBannerStatus()){
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
				shareEvent.setObject("bannerId", banner.getId());
				log.debug("post a event[" + ShareEvent.class.getCanonicalName() + "] to portlet request");
				log.debug("bannerId = " + banner.getId());
				((PortletRequestContext) requestContext).getPortletRequest().setAttribute(ShareEvent.class.getCanonicalName(), shareEvent);
			}
			PageParameters pageParameters = new PageParameters();
			pageParameters.put("bannerId", banner.getId());
			RotatingBannerPage rotatingBannerPage = new RotatingBannerPage(pageParameters);
			setResponsePage(rotatingBannerPage);
			break;
		}
	}

}

// $Id$