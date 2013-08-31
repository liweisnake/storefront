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
package com.hp.sdf.ngp.ui.page.register;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.PageParameters;
import org.apache.wicket.extensions.breadcrumb.IBreadCrumbModel;
import org.apache.wicket.extensions.breadcrumb.panel.BreadCrumbPanel;
import org.apache.wicket.extensions.markup.html.captcha.CaptchaImageResource;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.validation.validator.EmailAddressValidator;
import org.apache.wicket.validation.validator.StringValidator;

import com.hp.sdf.ngp.model.RoleCategory;
import com.hp.sdf.ngp.model.UserProfile;
import com.hp.sdf.ngp.service.UserService;
import com.hp.sdf.ngp.workflow.UserCategoryLifeCycleEngine;

public class RegisterPanel extends BreadCrumbPanel {

	private static final long serialVersionUID = 2847203327312187476L;

	private static final Log log = LogFactory.getLog(RegisterPanel.class);

	private RegisterForm form;

	private Label registerSuccessLabel;

	public RegisterPanel(String id, final IBreadCrumbModel breadCrumbModel) {
		super(id, breadCrumbModel);

		this.add(new FeedbackPanel("feedBack"));

		form = new RegisterForm("registerForm");
		add(form);

		registerSuccessLabel = new Label("registerSuccess", this.getLocalizer().getString("registerSuccess", this, "Your account has been successfully created. You may want to login now"));
		registerSuccessLabel.setVisible(false);
		add(registerSuccessLabel);
	}

	class RegisterForm extends Form<Void> {

		private static final long serialVersionUID = 404463636873167639L;

		@SpringBean
		private UserService userService;

		@SpringBean
		private UserCategoryLifeCycleEngine userCategoryLifeCycleEngine;

		private String userId;

		private String email;

		private String verifyCaptcha;

		private String newPassword;

		private String confirmPassword;

		/** Random captcha password to match against. */
		private String imagePass = randomString(6, 6);

		private final CaptchaImageResource captchaImageResource;

		public RegisterForm(String id) {
			super(id);

			TextField<String> userIdField = new TextField<String>("userId", new PropertyModel<String>(this, "userId"));
			userIdField.setRequired(true);
			add(userIdField);

			TextField<String> emailField = new TextField<String>("email", new PropertyModel<String>(this, "email"));
			emailField.setRequired(true);
			emailField.add(EmailAddressValidator.getInstance());
			add(emailField);

			PasswordTextField newPasswordText = new PasswordTextField("newPassword", new PropertyModel<String>(this, "newPassword"));
			newPasswordText.setRequired(true);
			newPasswordText.add(new StringValidator.MaximumLengthValidator(32));
			newPasswordText.add(new StringValidator.MinimumLengthValidator(1));
			add(newPasswordText);

			PasswordTextField confirmPasswordText = new PasswordTextField("confirmPassword", new PropertyModel<String>(this, "confirmPassword"));
			confirmPasswordText.setRequired(true);
			confirmPasswordText.add(new StringValidator.MaximumLengthValidator(32));
			confirmPasswordText.add(new StringValidator.MinimumLengthValidator(1));
			add(confirmPasswordText);

			captchaImageResource = new CaptchaImageResource(imagePass);
			Image captchaImage = new Image("captchaImage", captchaImageResource);
			add(captchaImage);
			add(new RequiredTextField<String>("verifyCaptcha", new PropertyModel<String>(this, "verifyCaptcha")) {
				private static final long serialVersionUID = -5693802624038337190L;

				protected final void onComponentTag(final ComponentTag tag) {
					super.onComponentTag(tag);
					// clear the field after each render
					tag.put("value", "");
				}
			});

			Button submit = new Button("submit") {

				private static final long serialVersionUID = 1510836418186350793L;

				public void onSubmit() {
					log.debug("userId :" + userId);
					log.debug("email :" + email);
					log.debug("verifyCaptcha :" + verifyCaptcha);
					log.debug("newPassword :" + newPassword);
					log.debug("confirmPassword :" + confirmPassword);

					if (StringUtils.isNotEmpty(newPassword) && StringUtils.isNotEmpty(confirmPassword)) {
						if (confirmPassword.equals(newPassword)) {
							if (userService.getUser(userId) != null) {
								this.error("Can not register, because user " + userId + " have existed.");
								return;
							}

							if (!imagePass.equals(verifyCaptcha)) {
								this.error("Can not register, because verify captcha is not correct.");
								return;
							}

							UserProfile userProfile = new UserProfile();
							userProfile.setUserid(userId);
							userProfile.setEmail(email);
							userProfile.setPassword(newPassword);
							userService.saveUser(userProfile);

							userService.updatePassword(userId, newPassword);
							log.debug("Register user successfully.");
							RoleCategory roleCategory = userCategoryLifeCycleEngine.getStartupRoleCategory();
							if (roleCategory != null) {
								userService.assignRole(userId, roleCategory.getRoleName());
							}

							RegisterPanel.this.form.setVisible(false);
							registerSuccessLabel.setVisible(true);
						} else {
							this.error("Can not register, because two password is not same.");
							return;
						}
					} else {
						this.error("Can not register, because password is empty.");
						return;
					}

				}
			};
			add(submit);

			Button cancel = new Button("cancel") {

				private static final long serialVersionUID = 8116120281966867260L;

				public void onSubmit() {
					log.debug("cancel");

					PageParameters parameters = new PageParameters();
					UserRegisterPage page = new UserRegisterPage(parameters);
					setResponsePage(page);
				}
			};
			cancel.setDefaultFormProcessing(false);
			add(cancel);

		}
	}

	private int randomInt(int min, int max) {
		return (int) (Math.random() * (max - min) + min);
	}

	private String randomString(int min, int max) {
		int num = randomInt(min, max);
		byte b[] = new byte[num];
		for (int i = 0; i < num; i++) {
			b[i] = (byte) randomInt('a', 'z');
			log.debug("b[" + i + "] :" + b[i]);
		}
		return new String(b);
	}

	public String getTitle() {
		return this.getLocalizer().getString("title", this, "Register");
	}
}