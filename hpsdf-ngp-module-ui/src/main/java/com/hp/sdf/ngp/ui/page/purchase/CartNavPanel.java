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
package com.hp.sdf.ngp.ui.page.purchase;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.behavior.IBehavior;
import org.apache.wicket.extensions.breadcrumb.IBreadCrumbModel;
import org.apache.wicket.extensions.breadcrumb.panel.BreadCrumbPanel;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.hp.sdf.ngp.common.constant.AssetSubscriptionStatus;
import com.hp.sdf.ngp.common.constant.AssetConstants;
import com.hp.sdf.ngp.model.ShoppingCart;
import com.hp.sdf.ngp.service.PurchaseService;
import com.hp.sdf.ngp.ui.WicketSession;

public class CartNavPanel extends Panel {

	private static final long serialVersionUID = 2287827080550510536L;

	public static final IBehavior STYLE_HIDE = new AttributeModifier("style", true, new Model("display:none"));
	public static final IBehavior STYLE_NONE = new AttributeModifier("style", true, new Model(""));

	@SpringBean
	private PurchaseService purchaseService;

	private double priceSum = 0.0D;

	private Label sumLabel, countLabel;

	private IBreadCrumbModel breadCrumbModel;

	public CartNavPanel(String id, IBreadCrumbModel breadCrumbModel) {
		super(id);
		this.breadCrumbModel = breadCrumbModel;

		Label hint = new Label("message", this.getLocalizer().getString("hint.success", this));
		add(hint);

		String userId = WicketSession.get().getUserId();
		// List<PurchaseHistory> cartItems = new ArrayList<PurchaseHistory>();
		List<ShoppingCart> cartItems = new ArrayList<ShoppingCart>();
		try {
			// cartItems = purchaseService.listSubscriptions(userId, new AppSubscriptionStatus[] { AppSubscriptionStatus.INCART });
			cartItems = purchaseService.getShoppingCartSubscriptions(userId, new AssetSubscriptionStatus[] { AssetSubscriptionStatus.INCART });
		} catch (Exception e) {
			// do nothing
		}
		countLabel = new Label("count", String.valueOf(cartItems.size()));
		add(countLabel);

		String unit = "$ ";
		try {
			priceSum = purchaseService.calculateBillAmount(cartItems);
		} catch (Exception e) {
			// do nothing
		}
		sumLabel = new Label("amount", unit + AssetConstants.priceFormat.format(priceSum));
		add(sumLabel);

		NavForm form = new NavForm("nav.form");
		add(form);

	}

	public void updateModel() {

		String unit = "$ ";
		try {
			String userId = WicketSession.get().getUserId();
			// List<PurchaseHistory> cartItems = new ArrayList<PurchaseHistory>();
			List<ShoppingCart> cartItems = new ArrayList<ShoppingCart>();
			// cartItems = purchaseService.listSubscriptions(userId, new AppSubscriptionStatus[] { AppSubscriptionStatus.INCART });
			cartItems = purchaseService.getShoppingCartSubscriptions(userId, new AssetSubscriptionStatus[] { AssetSubscriptionStatus.INCART });
			priceSum = purchaseService.calculateBillAmount(cartItems);
			sumLabel.setDefaultModelObject(unit + AssetConstants.priceFormat.format(priceSum));
			countLabel.setDefaultModelObject(String.valueOf(cartItems.size()));
		} catch (Exception e) {
			// do nothing
		}

	}

	@SuppressWarnings("unchecked")
	private class NavForm extends Form {

		private static final long serialVersionUID = -5616262418225327556L;

		public NavForm(String id) {
			super(id);

			AjaxButton backButton = new AjaxButton("go.back") {

				private static final long serialVersionUID = 7161862497494634176L;

				@Override
				protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
					CartNavPanel.this.add(STYLE_HIDE);
					target.addComponent(CartNavPanel.this);
				}

			};
			backButton.setDefaultFormProcessing(false);
			add(backButton);

			Button cartButton = new Button("go.cart") {

				private static final long serialVersionUID = -7567133419918483916L;

				@Override
				public void onSubmit() {
					CartNavPanel.this.add(STYLE_HIDE);

					BreadCrumbPanel parent = (BreadCrumbPanel) breadCrumbModel.getActive();
					ShoppingCartPanel cartPanel = new ShoppingCartPanel(parent.getId(), breadCrumbModel);
					cartPanel.setPreviouse(parent);
					parent.activate(cartPanel);
				}

			};
			cartButton.setDefaultFormProcessing(false);
			add(cartButton);
		}

	}

}

// $Id$