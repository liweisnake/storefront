package com.hp.sdf.ngp.ais.ui.portlet;

import java.io.IOException;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.Event;
import javax.portlet.EventRequest;
import javax.portlet.EventResponse;
import javax.portlet.PortletException;
import javax.portlet.PortletMode;
import javax.portlet.ReadOnlyException;
import javax.portlet.ValidatorException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.RequestContext;
import org.apache.wicket.protocol.http.portlet.PortletRequestContext;
import org.apache.wicket.protocol.http.portlet.WicketPortlet;

import com.hp.sdf.ngp.ui.ShareEvent;

/**
 * 
 * This portlet will handle {@link ShareEvent}, which means it will set and
 * handle {@link ShareEvent} object to share data in different portlets
 * 
 */
public class ShareEventPortlet extends WicketPortlet {

	private final static Log log = LogFactory.getLog(ShareEventPortlet.class);

	@Override
	public void processAction(ActionRequest request, ActionResponse response)
			throws PortletException, IOException {

		super.processAction(request, response);
		
		String storePreference = request.getParameter("storePreference");
		if(storePreference != null && storePreference.length() > 0){
			log.debug("Store Portlet Preference. Key = bannerId ");
			//PortletRequestContext prc = (PortletRequestContext) RequestContext.get(); 
		    try {
		    	String bannerId = request.getParameter("bannerId");
		    	String bannerName = request.getParameter("bannerName");
		    	String location = request.getParameter("location");
		    	String height = request.getParameter("height");
		    	log.debug("Preference value. bannerId:" + bannerId 
		    			+ ". bannerName: " + bannerName 
		    			+ ". bannerHeight: " + height 
		    			+ ". location: " + location);
		    	request.getPreferences().setValue("bannerId", bannerId);
		    	request.getPreferences().setValue("bannerName", bannerName);
		    	request.getPreferences().setValue("location", location);
		    	request.getPreferences().setValue("height", height);
		    	request.getPreferences().store();
				log.debug("Store Portlet Preference finished.");
				response.setPortletMode(PortletMode.VIEW);
			} catch (ReadOnlyException exception) {
					exception.printStackTrace();
			} catch (ValidatorException exception) {
				exception.printStackTrace();
			} catch (IOException exception) {
				exception.printStackTrace();
			}
		}
		
		ShareEvent shareEvent = (ShareEvent)request.getAttribute(ShareEvent.class.getCanonicalName());
		if(shareEvent != null){			
			// send event
			log.debug("sent a event[" + ShareEvent.class.getCanonicalName()	+ "]");
			response.setEvent(ShareEvent.class.getCanonicalName(), shareEvent);
		}
	

	}

	@Override
	public void processEvent(EventRequest request, EventResponse response)
			throws PortletException, IOException {
		super.processEvent(request, response);
		Event event = request.getEvent();
		if (event != null&& event.getName().equals(ShareEvent.class.getCanonicalName())) {
			log.debug("get a event[" + ShareEvent.class.getCanonicalName() + "]");
			ShareEvent shareEvent = (ShareEvent) event.getValue();
			request.getPortletSession().setAttribute(ShareEvent.class.getCanonicalName(), shareEvent);
		}
	}

}