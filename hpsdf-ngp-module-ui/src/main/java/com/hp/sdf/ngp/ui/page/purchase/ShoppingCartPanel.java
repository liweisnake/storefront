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

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.extensions.breadcrumb.IBreadCrumbModel;
import org.apache.wicket.extensions.breadcrumb.panel.BreadCrumbPanel;
import org.apache.wicket.extensions.breadcrumb.panel.BreadCrumbPanelLink;
import org.apache.wicket.extensions.breadcrumb.panel.IBreadCrumbPanelFactory;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.util.ListModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.hp.sdf.ngp.common.constant.AssetConstants;
import com.hp.sdf.ngp.common.constant.AssetSubscriptionStatus;
import com.hp.sdf.ngp.model.Asset;
import com.hp.sdf.ngp.model.AssetBinaryVersion;
import com.hp.sdf.ngp.model.Platform;
import com.hp.sdf.ngp.model.ShoppingCart;
import com.hp.sdf.ngp.service.ApplicationService;
import com.hp.sdf.ngp.service.PurchaseService;
import com.hp.sdf.ngp.ui.WicketSession;
import com.hp.sdf.ngp.ui.common.Tools;

public class ShoppingCartPanel extends BreadCrumbPanel {

	private static final long serialVersionUID = -2305733159900419864L;

	private static final Log log = LogFactory.getLog(ShoppingCartPanel.class);

	@SpringBean
	private ApplicationService applicationService;

	@SpringBean
	private AssetConstants assetConstants;

	@SpringBean
	private PurchaseService purchaseService;

	private BreadCrumbPanel previouse;

	private double priceSum = 0.0D;

	// private PromptPanel promptPanel;

	private Label sumLabel;

	private BreadCrumbPanelLink checkoutLink;

	ListView<ShoppingCart> itemsView;

	private List<ShoppingCart> cartItems = null;

	private final DecimalFormat priceFormat = new DecimalFormat("#0.00");

	public String getTitle() {
		return this.getLocalizer().getString("title", this, "Shopping Cart");
	}

	public ShoppingCartPanel(String id, IBreadCrumbModel breadCrumbModel) {
		super(id, breadCrumbModel);

		String userId = WicketSession.get().getUserId();
		log.debug("userID = " + userId);

		// promptPanel = new PromptPanel("promptPanel", Constant.PROMPT.warning.toString(), "This asset has been removed.", null, StringUtils.EMPTY);
		// promptPanel.setOutputMarkupId(true);
		// promptPanel.add(Constant.STYLE_HIDE);
		// add(promptPanel);

		add(new FeedbackPanel("feedback"));

		try {
			// cartItems = purchaseService.listSubscriptions(userId, new
			// AppSubscriptionStatus[] { AppSubscriptionStatus.INCART });
			cartItems = purchaseService.getShoppingCartSubscriptions(userId, new AssetSubscriptionStatus[] { AssetSubscriptionStatus.INCART });
		} catch (Exception e) {
			// do nothing
		}

		itemsView = new ListView<ShoppingCart>("items", new ListModel<ShoppingCart>(cartItems)) {
			@Override
			protected void populateItem(final ListItem<ShoppingCart> item) {
				ShoppingCart subscription = (ShoppingCart) item.getModelObject();
				Asset app = subscription.getAsset();
				if (app != null) {
					app = applicationService.getAsset(subscription.getAsset().getId());
				}

				AssetBinaryVersion binary = null;
				if (app.getCurrentVersionId() != null)
					binary = applicationService.getAssetBinaryById(app.getCurrentVersionId());

				Label appName = new Label("appName", app.getName());
				item.add(appName);

				Label binPlatform, binVersion;
				if (binary != null) {
					List<Platform> list = applicationService.getPlatformByAssetId(app.getId());
					binPlatform = new Label("binPlatform", Tools.getPlatfromNameFromList(list));

					binVersion = new Label("binVersion", binary.getVersion());

				} else {
					binPlatform = new Label("binPlatform", "");
					binVersion = new Label("binVersion", "");
				}
				item.add(binPlatform);
				item.add(binVersion);

				Label binPrice = new Label("binPrice", priceFormat.format(subscription.getItemPrice()));
				item.add(binPrice);

				Link removeLink = new Link("removeItem") {

					private static final long serialVersionUID = 1L;

					@Override
					public void onClick() {
						List<ShoppingCart> list = new ArrayList<ShoppingCart>();
						ShoppingCart subscription = (ShoppingCart) item.getModelObject();
						list.add(subscription);

						for (ShoppingCart shoppingCart : list) {
							if (null == purchaseService.getShoppingCart(shoppingCart.getId())) {
								// promptPanel.setMessage("This asset has been removed.");
								// promptPanel.add(Constant.STYLE_SHOW);
								error("This asset has been removed.");
								ShoppingCartPanel.this.updateCartItems();
								return;
							}
						}

						purchaseService.removeFromShoppingCart(list);

						ShoppingCartPanel.this.updateCartItems();
					}

				};
				item.add(removeLink);
			}
		};
		add(itemsView);

		try {
			priceSum = purchaseService.calculateBillAmount(cartItems);
		} catch (Exception e) {
			// do nothing
		}
		sumLabel = new Label("priceSum", priceFormat.format(priceSum));
		add(sumLabel);

		checkoutLink = new BreadCrumbPanelLink("checkoutLink", ShoppingCartPanel.this.getBreadCrumbModel(), new IBreadCrumbPanelFactory() {
			public BreadCrumbPanel create(String componentId, IBreadCrumbModel breadCrumbModel) {
				String bill = assetConstants.getUnit();
				bill += priceFormat.format(ShoppingCartPanel.this.priceSum);

				for (ShoppingCart shoppingCart : cartItems) {
					if (null == purchaseService.getShoppingCart(shoppingCart.getId())) {
						// promptPanel.setMessage("There are not any assets in your Shopping Cart.");
						// promptPanel.add(Constant.STYLE_SHOW);
						error("There are not any assets in your Shopping Cart.");
						ShoppingCartPanel.this.updateCartItems();
						return ShoppingCartPanel.this;
					}
				}

				CheckoutPanel checkoutPanel = new CheckoutPanel(componentId, breadCrumbModel, cartItems, bill);
				if (previouse == null) {
					previouse = ShoppingCartPanel.this;
				}
				checkoutPanel.setPreviouse(previouse);
				return checkoutPanel;
			}
		});

		if (cartItems == null || cartItems.size() == 0) {
			checkoutLink.setEnabled(false);
		}
		add(checkoutLink);
	}

	public BreadCrumbPanel getPreviouse() {
		return previouse;
	}

	public void setPreviouse(BreadCrumbPanel previouse) {
		this.previouse = previouse;
	}

	private void updateCartItems() {
		try {
			String userId = WicketSession.get().getUserId();
			// cartItems = purchaseService.listSubscriptions(userId, new
			// AppSubscriptionStatus[] { AppSubscriptionStatus.INCART });
			cartItems = purchaseService.getShoppingCartSubscriptions(userId, new AssetSubscriptionStatus[] { AssetSubscriptionStatus.INCART });
		} catch (Exception e) {
			// do nothing
		}
		ShoppingCartPanel.this.itemsView.setModelObject(cartItems);

		// update price sum label
		try {
			priceSum = purchaseService.calculateBillAmount(cartItems);
		} catch (Exception e) {
			// do nothing
		}
		sumLabel = new Label("priceSum", priceFormat.format(priceSum));
		ShoppingCartPanel.this.replace(sumLabel);

		if (cartItems == null || cartItems.size() == 0 || priceSum < 0.0d) {
			checkoutLink.setEnabled(false);
		}
	}
}

// $Id$