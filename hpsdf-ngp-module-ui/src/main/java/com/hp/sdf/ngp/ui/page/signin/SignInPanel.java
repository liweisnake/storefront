/*
 * Copyright (c) 2007 Hewlett-Packard Company, All Rights Reserved.
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
package com.hp.sdf.ngp.ui.page.signin;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.PropertyModel;

public abstract class SignInPanel extends Panel {
	private static final Log log = LogFactory.getLog(SignInPanel.class);

	/** True if the panel should display a remember-me checkbox */
	private boolean includeRememberMe = true;

	/** True if the user should be remembered via form persistence (cookies) */
	private boolean rememberMe = true;

	/** String for password. */
	private String password;

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	/** String for user name. */
	private String username;

	/**
	 * Sign in form.
	 */
	public final class SignInForm extends Form {

		/**
		 * 
		 */
		private static final long serialVersionUID = -3500779972982080215L;

		/**
		 * Constructor.
		 * 
		 * @param id
		 *            id of the form component
		 */
		public SignInForm(final String id) {
			super(id);

			// Attach textfield components that edit properties map
			// in lieu of a formal beans model
			TextField<String> userNameTextField;
			add(userNameTextField = new TextField<String>("username",
					new PropertyModel<String>(SignInPanel.this, "username")));
			add(new PasswordTextField("password", new PropertyModel<String>(
					SignInPanel.this, "password")));

			// MarkupContainer row for remember me checkbox
			WebMarkupContainer rememberMeRow = new WebMarkupContainer(
					"rememberMeRow");
			add(rememberMeRow);

			// Add rememberMe checkbox
			rememberMeRow
					.add(new CheckBox("rememberMe", new PropertyModel<Boolean>(
							SignInPanel.this, "rememberMe")));

			userNameTextField.setRequired(true);
			// Make form values persistent
			userNameTextField.setPersistent(rememberMe);

			// Show remember me checkbox?
			rememberMeRow.setVisible(includeRememberMe);
		}

		public final void onSubmit() {

			if (signIn(SignInPanel.this.username, SignInPanel.this.password)) {
				// If login has been called because the user was not yet
				// logged in, then continue to the original destination,
				// otherwise to the Home page
				if (!continueToOriginalDestination()) {
					setResponsePage(getApplication().getHomePage());
				}
			} else {

				error(getLocalizer().getString("login.error", this,
						"Unable to sign you in"));
			}
		}
	}

	public SignInPanel(final String id) {
		this(id, true);

	}

	public SignInPanel(final String id, final boolean includeRememberMe) {
		super(id);

		this.includeRememberMe = includeRememberMe;

		// Create feedback panel and add to page
		final FeedbackPanel feedback = new FeedbackPanel("feedback");
		add(feedback);
		// Add sign-in form to page, passing feedback panel as
		// validation error handler
		add(new SignInForm("signInForm"));
	}

	/**
	 * Removes persisted form data for the signin panel (forget me)
	 */
	public final void forgetMe() {
		// Remove persisted user data. Search for child component
		// of type SignInForm and remove its related persistence values.
		getPage().removePersistedFormData(SignInPanel.SignInForm.class, true);
	}

	/**
	 * Get model object of the rememberMe checkbox
	 * 
	 * @return True if user should be remembered in the future
	 */
	public boolean getRememberMe() {
		return rememberMe;
	}

	/**
	 * Set model object for rememberMe checkbox
	 * 
	 * @param rememberMe
	 */
	public void setRememberMe(boolean rememberMe) {

		this.rememberMe = rememberMe;
	}

	/**
	 * Sign in user if possible.
	 * 
	 * @param username
	 *            The username
	 * @param password
	 *            The password
	 * @return True if signin was successful
	 */
	public abstract boolean signIn(final String username, final String password);
}
