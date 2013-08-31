package com.hp.sdf.ngp.ui.portlet;

import java.io.IOException;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.Event;
import javax.portlet.EventRequest;
import javax.portlet.EventResponse;
import javax.portlet.PortletException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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
		
		ShareEvent shareEvent = (ShareEvent)request.getPortletSession().getAttribute(ShareEvent.class.getCanonicalName());
		if(shareEvent!=null)
		{			
			// send event
			log.debug("sent a event[" + ShareEvent.class.getCanonicalName()
					+ "]");

			response.setEvent(ShareEvent.class.getCanonicalName(), shareEvent);

		}
	

	}

	@Override
	public void processEvent(EventRequest request, EventResponse response)
			throws PortletException, IOException {
		super.processEvent(request, response);

	
		
		Event event = request.getEvent();
		if (event!=null&& event.getName().equals(ShareEvent.class.getCanonicalName())) {
			log.debug("get a event[" + ShareEvent.class.getCanonicalName()
					+ "]");
			ShareEvent shareEvent = (ShareEvent) event.getValue();
			request.getPortletSession().setAttribute(ShareEvent.class.getCanonicalName(), shareEvent);
		}
	}

}