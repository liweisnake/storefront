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

import java.io.Serializable;

import org.apache.wicket.Page;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;

public class ConfirmMessageBox extends ModalWindow implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2826511201590469085L;

	public interface MessageBoxCommand extends Runnable, Serializable {

		public Page getResponsePage();

	}

	public ConfirmMessageBox(String id) {
		super(id);
	}

	@Override
	public void show(AjaxRequestTarget target) {

		String newMessage = ConfirmMessageBox.this.getLocalizer().getString("message", ConfirmMessageBox.this);
		String newTitle = ConfirmMessageBox.this.getLocalizer().getString("title", ConfirmMessageBox.this);
		this.show(target, newTitle, newMessage, null, null);
	}

	public void show(AjaxRequestTarget target, MessageBoxCommand confirm, MessageBoxCommand cancel) {
		String newMessage = ConfirmMessageBox.this.getLocalizer().getString("message", ConfirmMessageBox.this);
		String newTitle = ConfirmMessageBox.this.getLocalizer().getString("title", ConfirmMessageBox.this);
		this.show(target, newTitle, newMessage, confirm, cancel);
	}

	public void show(AjaxRequestTarget target, MessageBoxCommand confirm) {
		String newMessage = ConfirmMessageBox.this.getLocalizer().getString("message", ConfirmMessageBox.this);
		String newTitle = ConfirmMessageBox.this.getLocalizer().getString("title", ConfirmMessageBox.this);
		this.show(target, newTitle, newMessage, confirm, null);
	}

	public void show(AjaxRequestTarget target, String message, MessageBoxCommand confirm) {
		String newTitle = ConfirmMessageBox.this.getLocalizer().getString("title", ConfirmMessageBox.this);
		this.show(target, newTitle, message, confirm, null);
	}

	public void show(AjaxRequestTarget target, String title, String message, MessageBoxCommand confirm) {
		this.show(target, title, message, confirm, null);
	}

	public void show(AjaxRequestTarget target, String message, MessageBoxCommand confirm, MessageBoxCommand cancel) {
		String newTitle = ConfirmMessageBox.this.getLocalizer().getString("title", ConfirmMessageBox.this);
		this.show(target, newTitle, message, confirm, cancel);
	}

	public void show(AjaxRequestTarget target, String title, String message, final MessageBoxCommand confirm, final MessageBoxCommand cancel) {

		final ConfirmMessageBoxPanel confirmMessageBoxPanel;
		this.setContent(confirmMessageBoxPanel = new ConfirmMessageBoxPanel(this, message) {
			private static final long serialVersionUID = -364657889743083504L;

			@Override
			protected void onCancel() {
				if (cancel != null) {
					cancel.run();
				}
			}

			@Override
			protected void onConfirm() {
				if (confirm != null) {
					confirm.run();
				}
			}
		});
		this.setTitle(title);

		this.setCloseButtonCallback(new ModalWindow.CloseButtonCallback() {
			/**
					 * 
					 */
			private static final long serialVersionUID = -1387388963595445219L;

			public boolean onCloseButtonClicked(AjaxRequestTarget target) {
				if (cancel != null) {
					cancel.run();
				}
				return true;
			}
		});

		this.setWindowClosedCallback(new ModalWindow.WindowClosedCallback() {
			/**
					 * 
					 */
			private static final long serialVersionUID = 2883923929941992313L;

			public void onClose(AjaxRequestTarget target) {
				if (confirmMessageBoxPanel.isConfirm()) {
					if (confirm != null && confirm.getResponsePage() != null) {
						setResponsePage(confirm.getResponsePage());
					}
				} else {
					if (cancel != null && cancel.getResponsePage() != null) {
						setResponsePage(cancel.getResponsePage());
					}
				}

			}
		});

		super.show(target);
	}

}

// $Id$