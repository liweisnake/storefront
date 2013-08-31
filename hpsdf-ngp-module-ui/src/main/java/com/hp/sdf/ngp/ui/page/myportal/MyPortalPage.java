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
package com.hp.sdf.ngp.ui.page.myportal;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.Component;
import org.apache.wicket.PageParameters;
import org.apache.wicket.authorization.strategies.role.metadata.MetaDataRoleAuthorizationStrategy;
import org.apache.wicket.extensions.markup.html.tabs.AbstractTab;
import org.apache.wicket.extensions.markup.html.tabs.TabbedPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;

import com.hp.sdf.ngp.ui.WicketSession;
import com.hp.sdf.ngp.ui.page.WicketPage;
import com.hp.sdf.ngp.workflow.Privilege;

public class MyPortalPage extends WicketPage {

	public MyPortalPage(PageParameters parameters) {
		super(parameters);
		List tabs = new ArrayList();

		tabs.add(new AbstractTab(new Model(this.getLocalizer().getString(
				"myProfile", this, "My Profile"))) {
			public Panel getPanel(String panelId) {
				MyProfilePanel mp = new MyProfilePanel(panelId);
				MetaDataRoleAuthorizationStrategy.authorize(mp,
						Component.RENDER, Privilege.DEFAULT);
				return mp;
			}
		});
		if (WicketSession.get().getRoles().hasRole(Privilege.VIEWMYAPPLICATION)) {

			tabs.add(new AbstractTab(new Model(this.getLocalizer().getString(
					"myApplication", this, "My Applications"))) {
				public Panel getPanel(String panelId) {
					MyAppContainerPanel myApp = new MyAppContainerPanel(panelId);
					MetaDataRoleAuthorizationStrategy.authorize(myApp,
							Component.RENDER, Privilege.VIEWMYAPPLICATION);
					return myApp;
				}
			});
		}
		if (WicketSession.get().getRoles().hasRole(Privilege.VIEWMYTESTING)) {

			tabs.add(new AbstractTab(new Model(this.getLocalizer().getString(
					"myTestApplications", this, " My test Applications"))) {
				public Panel getPanel(String panelId) {
					MyTestingContainerPanel myTest = new MyTestingContainerPanel(
							panelId);
					MetaDataRoleAuthorizationStrategy.authorize(myTest,
							Component.RENDER, Privilege.VIEWMYTESTING);
					return myTest;
				}
			});
		}
		if (WicketSession.get().getRoles().hasRole(
				Privilege.VIEWMYAPPAPPAPPROVAL)) {
			tabs.add(new AbstractTab(new Model(this.getLocalizer().getString(
					"myAppApprove", this, "My Application Approvals"))) {
				public Panel getPanel(String panelId) {
					MyApproveContainerPanel myApprove = new MyApproveContainerPanel(
							panelId);
					MetaDataRoleAuthorizationStrategy.authorize(myApprove,
							Component.RENDER, Privilege.VIEWMYAPPAPPAPPROVAL);
					return myApprove;
				}
			});
		}

		if (WicketSession.get().getRoles().hasRole(
				Privilege.VIEWMYPROMOTIONAPPROVAL)) {
			tabs.add(new AbstractTab(new Model(this.getLocalizer().getString(
					"myPromotionApprove", this, "My Promotion Approvals"))) {
				public Panel getPanel(String panelId) {
					MyPromotionApproveContainerPanel myPromotionApprove = new MyPromotionApproveContainerPanel(
							panelId);
					MetaDataRoleAuthorizationStrategy.authorize(
							myPromotionApprove, Component.RENDER,
							Privilege.VIEWMYPROMOTIONAPPROVAL);
					return myPromotionApprove;
				}
			});
		}

		if (WicketSession.get().getRoles().hasRole(Privilege.VIEWMYEWALLET)) {
			tabs.add(new AbstractTab(new Model(this.getLocalizer().getString(
					"myEwallet", this, "My Ewallet"))) {
				public Panel getPanel(String panelId) {
					EwalletPanel myEwallet = new EwalletPanel(panelId);
					MetaDataRoleAuthorizationStrategy.authorize(myEwallet,
							Component.RENDER, Privilege.VIEWMYEWALLET);
					return myEwallet;
				}
			});
		}
		
		if (WicketSession.get().getRoles().hasRole(Privilege.VIEWSHOPPINGCART)) {
			tabs.add(new AbstractTab(new Model(this.getLocalizer().getString(
					"myShoppingCart", this, "My Shopping Cart"))) {
				public Panel getPanel(String panelId) {
					MyShoppingCartContainerPanel shoppingCart = 
						new MyShoppingCartContainerPanel(panelId);
					MetaDataRoleAuthorizationStrategy.authorize(shoppingCart,
							Component.RENDER, Privilege.VIEWSHOPPINGCART);
					return shoppingCart;
				}
			});
		}
		
		if (WicketSession.get().getRoles().hasRole(Privilege.VIEWMYSUBSCRIPTION)) {
			tabs.add(new AbstractTab(new Model(this.getLocalizer().getString(
					"mySubscription", this, "My Purchased Assets"))) {
				public Panel getPanel(String panelId) {
					MySubscriptionContainerPanel subscriptionPanel = 
						new MySubscriptionContainerPanel(panelId);
					MetaDataRoleAuthorizationStrategy.authorize(subscriptionPanel,
							Component.RENDER, Privilege.VIEWMYSUBSCRIPTION);
					return subscriptionPanel;
				}
			});
		}
		add(new TabbedPanel("tabs", tabs));
	}
}

// $Id$