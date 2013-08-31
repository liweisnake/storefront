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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.hp.sdf.ngp.common.exception.NgpRuntimeException;
import com.hp.sdf.ngp.sdp.SGFWebService;
import com.hp.sdf.ngp.sdp.model.SGFAccount;

public class EwalletPanel extends Panel {

	@SpringBean(name = "sGFWebService")
	private SGFWebService sgfService;

	public SGFWebService getSgfService() {
		return sgfService;
	}

	public void setSgfService(SGFWebService sgfService) {
		this.sgfService = sgfService;
	}

	public EwalletPanel(String id) {
		super(id);
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
		List<String> list = new ArrayList<String>();
		SGFAccount account;
		try {
			// account = sgfService.getAccount(WicketSession.get().getUserId());
			account = sgfService.getAccount("testNGPPartner");
			if (account == null) {
				add(new Label("balance", ""));
				add(new Label("expireDate", ""));

				ListView listView = new ListView("transactionInfo", list) {
					protected void populateItem(ListItem item) {
						item.add(new Label("transactionDate", ""));
						item.add(new Label("trasactionDetail", ""));
					}
				};
				add(listView);
				
			} else {
				add(new Label("balance", account.getBalanceAmount().toString()));
				add(new Label("expireDate", sdf.format(account.getExpireDate()
						.getTime())));
				final Map<Calendar, String> map = account.getDatedTransaction();

				if (map != null) {
					for (Calendar e : map.keySet()) {
						list.add(sdf.format(e.getTime()));
					}
				}

				ListView listView = new ListView("transactionInfo", list) {
					protected void populateItem(ListItem item) {
						item.add(new Label("transactionDate", (String) item
								.getModelObject()));
						item.add(new Label("trasactionDetail", map.get(item
								.getModelObject())));
					}
				};
				add(listView);
			}
			
		} catch (Throwable e) {
			throw new NgpRuntimeException(e);
		}

	}
}

// $Id$