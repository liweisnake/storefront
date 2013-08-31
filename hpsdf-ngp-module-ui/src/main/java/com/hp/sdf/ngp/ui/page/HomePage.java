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
package com.hp.sdf.ngp.ui.page;

import org.apache.wicket.Component;
import org.apache.wicket.PageParameters;
import org.apache.wicket.authorization.strategies.role.metadata.MetaDataRoleAuthorizationStrategy;
import org.apache.wicket.markup.html.link.Link;

import com.hp.sdf.ngp.ui.page.api.ApiConfigurationPage;
import com.hp.sdf.ngp.ui.page.api.ApiListPage;
import com.hp.sdf.ngp.ui.page.asset.AppListPage;
import com.hp.sdf.ngp.ui.page.configuration.ConfigurationPage;
import com.hp.sdf.ngp.ui.page.myportal.MyPortalPage;
import com.hp.sdf.ngp.ui.page.signin.SignOutPage;


public class HomePage extends WicketPage {

	
	class LogoutLink extends Link {
		public LogoutLink(String id) {
			super(id);
		}

		private static final long serialVersionUID = 4302824445593251745L;

		@Override
		public void onClick() {
			setResponsePage(SignOutPage.class);

		}
	}

	class DeveloperLink extends Link {
		public DeveloperLink(String id) {
			super(id);
		}

		private static final long serialVersionUID = 4302824445593251745L;

		@Override
		public void onClick() {
			

		}
	}	
	
	class OwnerLink extends Link {
		public OwnerLink(String id) {
			super(id);			
		}

		private static final long serialVersionUID = 4302824445593251745L;

		@Override
		public void onClick() {
			

		}
	}

	private Link logoutLink = new LogoutLink("logoutlink");
	private Link developerLink = new DeveloperLink("developerlink");
	private Link ownerLink = new OwnerLink("ownerlink");

	public HomePage(final PageParameters parameters) {
		super(parameters);

		MetaDataRoleAuthorizationStrategy.authorize(ownerLink, Component.RENDER, "7");				
		
		this.add(logoutLink);
		this.add(developerLink);
		this.add(ownerLink);
		
	    final Link apiListLink = new Link("apiList")
        {
            public void onClick() {
                setResponsePage(ApiListPage.class);
            }
        };
        this.add(apiListLink);
        
	    final Link configurationLink = new Link("configuration")
        {
            public void onClick() {
                setResponsePage(ConfigurationPage.class);
            }
        };
        this.add(configurationLink);
        
	    final Link apiConfigurationListLink = new Link("apiConfigurationList")
        {
            public void onClick() {
                setResponsePage(ApiConfigurationPage.class);
            }
        };
        this.add(apiConfigurationListLink);

	    final Link appListLink = new Link("appList")
        {
            public void onClick() {
                setResponsePage(AppListPage.class);
            }
        };
        this.add(appListLink);
        
        final Link myPortalLink = new Link("myPortal")
        {
            public void onClick() {
                setResponsePage(MyPortalPage.class);
            }
        };
        this.add(myPortalLink);
	}

}
