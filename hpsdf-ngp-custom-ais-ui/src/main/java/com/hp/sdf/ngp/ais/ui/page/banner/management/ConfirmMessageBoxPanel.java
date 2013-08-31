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
package com.hp.sdf.ngp.ais.ui.page.banner.management;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.basic.MultiLineLabel;
import org.apache.wicket.markup.html.panel.Panel;

@SuppressWarnings("unchecked")
public abstract class ConfirmMessageBoxPanel extends Panel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6071042847317471837L;
	
	private boolean isConfirm = false;

	public boolean isConfirm() {
		return isConfirm;
	}

	public void setConfirm(boolean isConfirm) {
		this.isConfirm = isConfirm;
	}

	public ConfirmMessageBoxPanel(final ModalWindow modalWindow, String message) {
		super(modalWindow.getContentId());
		add(new MultiLineLabel("message", message));

		AjaxLink confirm = new AjaxLink("confirm") {

			private static final long serialVersionUID = 4651558738809662924L;

			@Override
			public void onClick(AjaxRequestTarget target) {

				onConfirm();
				isConfirm = true;
				modalWindow.close(target);
			}

		};
		add(confirm);
		AjaxLink cancel = new AjaxLink("cancel") {
			private static final long serialVersionUID = 4624284528096337889L;

			@Override
			public void onClick(AjaxRequestTarget target) {

				onCancel();
				modalWindow.close(target);

			}

		};
		add(cancel);
	}

	protected abstract void onConfirm();

	protected abstract void onCancel();

}

// $Id$