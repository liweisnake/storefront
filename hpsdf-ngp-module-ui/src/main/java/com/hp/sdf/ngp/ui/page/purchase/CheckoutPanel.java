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

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.extensions.breadcrumb.BreadCrumbBar;
import org.apache.wicket.extensions.breadcrumb.IBreadCrumbModel;
import org.apache.wicket.extensions.breadcrumb.panel.BreadCrumbPanel;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.hp.sdf.ngp.model.ShoppingCart;
import com.hp.sdf.ngp.service.PurchaseService;

public class CheckoutPanel extends BreadCrumbPanel {

	private static final long serialVersionUID = 6176934749907176498L;

	private static Log log = LogFactory.getLog(CheckoutPanel.class);

	@SpringBean
	private PurchaseService purchaseService;

	private String bill;

	private List<ShoppingCart> buyingItems;

	private BreadCrumbPanel previouse;

	public String getTitle() {
		return this.getLocalizer().getString("title", this, "Check Out");
	}

	public CheckoutPanel(String id, IBreadCrumbModel breadCrumbModel, List<ShoppingCart> buyingItems, String bill) {
		super(id, breadCrumbModel);

		this.bill = bill;
		this.buyingItems = buyingItems;

		Form paymentForm = new PaymentForm("paymentForm");
		add(paymentForm);
	}

	public BreadCrumbPanel getPreviouse() {
		return previouse;
	}

	public void setPreviouse(BreadCrumbPanel previouse) {
		this.previouse = previouse;
	}

	private class PaymentForm extends Form {

		private static final long serialVersionUID = 3156057141435493886L;

		public PaymentForm(String id) {
			super(id);

			add(new Label("bill.amount", bill));
		}

		@Override
		protected void onSubmit() {
			// saves purchased application binaries
			log.debug("buyingItems.size()=" + buyingItems.size());
			try {
				purchaseService.completePurchase(buyingItems);
			} catch (Exception e) {
				// do nothing
			}

			BreadCrumbPanel successPanel;
			if (previouse != null) {
				successPanel = new CheckoutSuccessPanel(CheckoutPanel.this.getId(), previouse.getBreadCrumbModel());
				previouse.activate(previouse);
				if (previouse instanceof ShoppingCartPanel) {
					previouse.replaceWith(successPanel);
				} else {
					previouse.activate(successPanel);
				}
			} else {
				BreadCrumbBar breadCrumbBar = new BreadCrumbBar("breadCrumbBar");
				successPanel = new CheckoutSuccessPanel(CheckoutPanel.this.getId(), breadCrumbBar);
				CheckoutPanel.this.replaceWith(successPanel);
			}

		}

	}

}

// $Id$