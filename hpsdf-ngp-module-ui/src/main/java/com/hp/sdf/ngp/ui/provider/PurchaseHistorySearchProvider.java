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
package com.hp.sdf.ngp.ui.provider;

import java.util.Iterator;
import java.util.List;

import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import com.hp.sdf.ngp.api.exception.StoreClientServiceException;
import com.hp.sdf.ngp.api.search.OrderEnum;
import com.hp.sdf.ngp.api.search.SearchExpression;
import com.hp.sdf.ngp.api.search.Condition.DateComparer;
import com.hp.sdf.ngp.api.search.Condition.StringComparer;
import com.hp.sdf.ngp.custom.sbm.api.search.orderby.PurchaseHistoryExtendOrderBy;
import com.hp.sdf.ngp.custom.sbm.api.storeclient.model.PurchaseHistoryExtend;
import com.hp.sdf.ngp.custom.sbm.storeclient.service.ClientContentServiceImpl;
import com.hp.sdf.ngp.search.condition.purchasehistoryextend.PurchaseHistoryExtendCompleteDateCondition;
import com.hp.sdf.ngp.search.condition.purchasehistoryextend.PurchaseHistoryExtendMsisdnCondition;
import com.hp.sdf.ngp.search.condition.purchasehistoryextend.PurchaseHistoryExtendTempPaidDateCondition;
import com.hp.sdf.ngp.search.engine.SearchExpressionImpl;
import com.hp.sdf.ngp.ui.common.PurchaseHistorySearchCondition;

public class PurchaseHistorySearchProvider implements IDataProvider<PurchaseHistoryExtend> {

	private static final long serialVersionUID = 2301670351366205007L;

	private ClientContentServiceImpl clientContentServiceImpl;

	private PurchaseHistorySearchCondition purchaseHistorySearchCondition;

	public PurchaseHistorySearchCondition getPurchaseHistorySearchCondition() {
		return purchaseHistorySearchCondition;
	}

	public void setPurchaseHistorySearchCondition(PurchaseHistorySearchCondition purchaseHistorySearchCondition) {
		this.purchaseHistorySearchCondition = purchaseHistorySearchCondition;
	}

	public PurchaseHistorySearchProvider(ClientContentServiceImpl clientContentServiceImpl, PurchaseHistorySearchCondition purchaseHistorySearchCondition) {
		this.clientContentServiceImpl = clientContentServiceImpl;
		this.purchaseHistorySearchCondition = purchaseHistorySearchCondition;
	}

	public SearchExpression getCondition(int first, int count) {

		SearchExpression searchExpression = new SearchExpressionImpl();
		if (purchaseHistorySearchCondition != null) {

			if (purchaseHistorySearchCondition.getMsisdn() != null) {
				searchExpression.addCondition(new PurchaseHistoryExtendMsisdnCondition(purchaseHistorySearchCondition.getMsisdn(), StringComparer.EQUAL, true, false));
			}

			if (purchaseHistorySearchCondition.getPaidStartDate() != null) {
				searchExpression.addCondition(new PurchaseHistoryExtendTempPaidDateCondition(purchaseHistorySearchCondition.getPaidStartDate(), DateComparer.GREAT_THAN));
			}

			if (purchaseHistorySearchCondition.getPaidEndDate() != null) {
				searchExpression.addCondition(new PurchaseHistoryExtendTempPaidDateCondition(purchaseHistorySearchCondition.getPaidEndDate(), DateComparer.LESS_THAN));
			}

			if (purchaseHistorySearchCondition.getCompleteStartDate() != null) {
				searchExpression.addCondition(new PurchaseHistoryExtendCompleteDateCondition(purchaseHistorySearchCondition.getCompleteStartDate(), DateComparer.GREAT_THAN));
			}

			if (purchaseHistorySearchCondition.getCompleteEndDate() != null) {
				searchExpression.addCondition(new PurchaseHistoryExtendCompleteDateCondition(purchaseHistorySearchCondition.getCompleteEndDate(), DateComparer.LESS_THAN));
			}

		}
		
		// Order: 1, paid date, 2. providerId, 3. contentId
		searchExpression.addOrder(PurchaseHistoryExtendOrderBy.REQCONFIRMDATE, OrderEnum.ASC);
		searchExpression.addOrder(PurchaseHistoryExtendOrderBy.ASSETPROVIDERID, OrderEnum.ASC);
		searchExpression.addOrder(PurchaseHistoryExtendOrderBy.ASSETEXTERNALID, OrderEnum.ASC);
		
		return searchExpression;

	}

	public Iterator<? extends PurchaseHistoryExtend> iterator(int first, int count) {

		SearchExpression searchExpression = getCondition(first, count);
		
		try {
			List<PurchaseHistoryExtend> list = clientContentServiceImpl.searchPurchaseHistory(searchExpression);
			if (list != null) {
				return list.iterator();
			}

		} catch (StoreClientServiceException exception) {
			exception.printStackTrace();
		}

		return null;
	}

	public IModel<PurchaseHistoryExtend> model(PurchaseHistoryExtend paramT) {
		return new Model<PurchaseHistoryExtend>(paramT);
	}

	public int size() {
		SearchExpression searchExpression = getCondition(0, Integer.MAX_VALUE);
		return clientContentServiceImpl.searchPurchaseHistoryCount(searchExpression);
	}

	public void detach() {

	}

}
