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
package com.hp.sdf.ngp.ui.page.asset;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.behavior.IBehavior;
import org.apache.wicket.extensions.breadcrumb.IBreadCrumbModel;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RadioGroup;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.hp.sdf.ngp.common.constant.AssetConstants;
import com.hp.sdf.ngp.model.Asset;
import com.hp.sdf.ngp.model.AssetBinaryVersion;
import com.hp.sdf.ngp.model.AssetPrice;
import com.hp.sdf.ngp.service.ApplicationService;
import com.hp.sdf.ngp.service.PurchaseService;
import com.hp.sdf.ngp.ui.WicketSession;
import com.hp.sdf.ngp.ui.common.Constant;
import com.hp.sdf.ngp.ui.common.PromptPanel;
import com.hp.sdf.ngp.ui.common.Tools;
import com.hp.sdf.ngp.ui.page.purchase.CartNavPanel;

@SuppressWarnings( { "unchecked" })
public class AppSubscriptionPanel extends Panel {

	private static final long serialVersionUID = -4918503633298438L;

	@SpringBean
	private AssetConstants assetConstants;

	@SpringBean
	private ApplicationService applicationService;

	@SpringBean
	private PurchaseService purchaseService;

	private IBreadCrumbModel breadCrumbModel = null;

	private Asset app;

	private final String PRICE_EMPTY = getLocalizer().getString("bin.noprice", this);
	private final String PRICE_FREE = getLocalizer().getString("bin.free", this);

	public static final IBehavior STYLE_HIDE = new AttributeModifier("style", true, new Model("display:none"));

	public AppSubscriptionPanel(String id, Asset app, IBreadCrumbModel breadCrumbModel) {
		super(id);
		this.app = app;
		this.breadCrumbModel = breadCrumbModel;
		AppPurchaseForm appPurchaseForm = new AppPurchaseForm("appPurchaseForm");
		add(appPurchaseForm);
	}

	public String getTitle() {
		return this.getLocalizer().getString("title", this, "Download Application");
	}

	public class AppPurchaseForm extends Form {

		private static final long serialVersionUID = -6578579305600140145L;

		// The binary id
		private Long binaryId;

		private CartNavPanel cartNavPanel;

		private PromptPanel promptPanel;

		private RadioGroup<AssetBinaryVersion> binaryGroup = null;

		public Long getBinaryId() {
			return binaryId;
		}

		public void setBinaryId(Long binaryId) {
			this.binaryId = binaryId;
		}

		public AppPurchaseForm(String id) {
			super(id);

			// creates Cart Navigation Div
			cartNavPanel = new CartNavPanel("cartNavPanel", AppSubscriptionPanel.this.breadCrumbModel);
			cartNavPanel.setOutputMarkupId(true);
			cartNavPanel.add(STYLE_HIDE);
			add(cartNavPanel);

			promptPanel = new PromptPanel("promptPanel", Constant.PROMPT.warning.toString(), "Please sign in first, thank you!", null, StringUtils.EMPTY);
			promptPanel.setOutputMarkupId(true);
			promptPanel.add(STYLE_HIDE);
			add(promptPanel);
		}

		@Override
		protected void onSubmit() {
			Log log = LogFactory.getLog(AppSubscriptionPanel.class);

			String userId = WicketSession.get().getUserId();
			if (StringUtils.isEmpty(userId)) {
				promptPanel.add(new AttributeModifier("style", true, new Model(StringUtils.EMPTY)));
				return;
			}

			log.debug("subscriber userId = " + userId);
			try {
				List<AssetPrice> assetPriceList = applicationService.getAssetPriceByAssetId(app.getId());
				AssetPrice assetPrice = Tools.getAssetPriceBeanFromListDollars(assetPriceList);
				if (null == assetPrice)
					purchaseService.addToShoppingCart(userId, app.getId(), Currency.getInstance(Constant.CURRENCY_DOLLARS), new BigDecimal(0));
				else
					purchaseService.addToShoppingCart(userId, app.getId(), Currency.getInstance(assetPrice.getCurrency()), assetPrice.getAmount());
			} catch (Exception e) {
				// do nothing
			}

			cartNavPanel.updateModel();
			cartNavPanel.add(new AttributeModifier("style", true, new Model(StringUtils.EMPTY)));
			cartNavPanel.replaceWith(cartNavPanel);
		}

	}

}

// $Id$