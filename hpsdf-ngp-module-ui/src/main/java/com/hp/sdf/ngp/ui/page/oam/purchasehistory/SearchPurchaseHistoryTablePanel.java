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
package com.hp.sdf.ngp.ui.page.oam.purchasehistory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.hp.sdf.ngp.common.util.Utils;
import com.hp.sdf.ngp.custom.sbm.api.storeclient.model.PurchaseHistoryExtend;
import com.hp.sdf.ngp.custom.sbm.storeclient.service.ClientContentServiceImpl;
import com.hp.sdf.ngp.model.Asset;
import com.hp.sdf.ngp.model.Provider;
import com.hp.sdf.ngp.service.ApplicationService;
import com.hp.sdf.ngp.ui.common.CustomizePagingNavigator;
import com.hp.sdf.ngp.ui.common.PurchaseHistorySearchCondition;
import com.hp.sdf.ngp.ui.provider.PurchaseHistorySearchProvider;

public class SearchPurchaseHistoryTablePanel extends Panel {

	private static final long serialVersionUID = 8006956197490194311L;

	private static final Log log = LogFactory.getLog(SearchPurchaseHistoryTablePanel.class);

	public PurchasesView purchasesView;

	public SearchPurchaseHistoryTablePanel(String id, PurchaseHistorySearchCondition purchaseHistorySearchCondition) {
		super(id);

		this.add(new FeedbackPanel("feedBack"));

		log.debug("purchaseHistorySearchCondition :" + purchaseHistorySearchCondition);
		if (purchaseHistorySearchCondition != null) {
			log.debug("purchaseHistorySearchCondition.getMsisdn :" + purchaseHistorySearchCondition.getMsisdn());
		}

		add(new PurchasesForm("purchasesForm", purchaseHistorySearchCondition));
	}

	class PurchasesForm extends Form<Void> {

		private static final long serialVersionUID = 427248307610664062L;

		@SpringBean
		private ClientContentServiceImpl clientContentServiceImpl;

		private static final int itemsPerPage = 50;

		public PurchasesForm(String id, PurchaseHistorySearchCondition purchaseHistorySearchCondition) {
			super(id);

			PurchaseHistorySearchProvider dataProvider = new PurchaseHistorySearchProvider(clientContentServiceImpl, purchaseHistorySearchCondition);
			purchasesView = new PurchasesView("purchases", dataProvider, itemsPerPage);
			add(purchasesView);

			add(new CustomizePagingNavigator("navigator", purchasesView));
		}

	}

	class PurchasesView extends DataView<PurchaseHistoryExtend> {

		private static final long serialVersionUID = 2738548802166595044L;

		@SpringBean
		private ApplicationService applicationService;

		public PurchasesView(String id, IDataProvider<PurchaseHistoryExtend> dataProvider, int itemsPerPage) {
			super(id, dataProvider, itemsPerPage);
		}

		public void updateModel(PurchaseHistorySearchCondition purchaseHistorySearchCondition) {
			PurchaseHistorySearchProvider dataProvider = (PurchaseHistorySearchProvider) this.getDataProvider();
			dataProvider.setPurchaseHistorySearchCondition(purchaseHistorySearchCondition);
		}

		protected void populateItem(Item<PurchaseHistoryExtend> item) {
			final PurchaseHistoryExtend purchaseHistoryExtend = item.getModelObject();

			Provider assetProvider = null;
			Asset asset = applicationService.getAsset(purchaseHistoryExtend.getAssetId());
			if (asset.getAssetProvider() != null) {
				assetProvider = applicationService.getAssetProviderById(asset.getAssetProvider().getId());
			}

			item.add(new Label("providerID", assetProvider != null ? assetProvider.getExternalId() : ""));

			item.add(new Label("providerName", assetProvider != null ? assetProvider.getName() : ""));

			item.add(new Label("contentId", asset.getId() + ""));

			item.add(new Label("contentName", asset.getName()));

			item.add(new Label("tempPaidDate", Utils.dateToShortString(purchaseHistoryExtend.getTempPaidDate())));

			item.add(new Label("amount", purchaseHistoryExtend.getPaidPrice() != null ? purchaseHistoryExtend.getPaidPrice().doubleValue() + "" : ""));
		}

	}

}
