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

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.extensions.markup.html.form.DateTextField;
import org.apache.wicket.extensions.yui.calendar.DatePicker;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RadioChoice;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.hp.sdf.ngp.model.Country;
import com.hp.sdf.ngp.model.Language;
import com.hp.sdf.ngp.model.UserProfile;
import com.hp.sdf.ngp.service.InfoService;
import com.hp.sdf.ngp.service.UserService;
import com.hp.sdf.ngp.ui.WicketSession;
import com.hp.sdf.ngp.ui.common.Constant;
import com.hp.sdf.ngp.ui.common.SelectOption;
import com.hp.sdf.ngp.ui.page.WicketPage;

/**
 * 
 *
 */
public class MyProfilePanel extends Panel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -827900743318962040L;

	private final static Log log = LogFactory.getLog(MyProfilePanel.class);

	@SpringBean
	private UserService userService;

	@SpringBean
	private InfoService infoService;

	private WicketPage page;

	private UserProfile user;

	public MyProfilePanel(String id) {
		super(id);
		String userId = WicketSession.get().getUserId();

		user = userService.getUser(userId);
		this.add(new FeedbackPanel("feedBack"));
		this.add(new MyProfileForm("myProfileForm"));
	}

	public MyProfilePanel(String id, WicketPage page) {
		this(id);
		this.page = page;
	}

	public final class MyProfileForm extends Form {

		DateFormat df;

		SelectOption country;

		SelectOption language;

		Integer gender;

		List<Country> countryList = infoService.getCountrys();

		List<Language> languageList = infoService.getLanguages();

		TextField<String> userNameTextField;

		TextField<String> passwordTextField;

		TextField<String> emailTextField;

		TextField<String> idCardTextField;

		RadioChoice<String> genderRadioChoice;

		TextField<String> cellphoneTextField;

		TextField<String> companyTextField;

		TextField<String> addressTextField;

		TextField<String> zipTextField;

		DateTextField birthdayTextField;

		TextField<String> lastNameTextField;

		TextField<String> firstNameTextField;

		DropDownChoice<String> countrys;

		DropDownChoice<String> languages;

		public MyProfileForm(String id) {
			super(id);

			add(userNameTextField = new TextField<String>("userName", new PropertyModel<String>(user, "userid")));
			userNameTextField.setRequired(true);

			add(emailTextField = new TextField<String>("email", new PropertyModel<String>(user, "email")));
			emailTextField.setRequired(true);

			add(idCardTextField = new TextField<String>("idCard", new PropertyModel<String>(user, "idcard")));
			//idCardTextField.setRequired(true);

			try {
				gender = user.getGender() == null ? null : Integer.parseInt(user.getGender());
			} catch (Throwable e) {
			}

			add(genderRadioChoice = new RadioChoice("gender", new PropertyModel<Integer>(this, "gender"), Gender.getGenderIdList(), new ChoiceRenderer() {
				public Object getDisplayValue(Object object) {
					Map genderMap = Gender.getGenderMap();
					return genderMap.get(object);
				}
			}));
			genderRadioChoice.setRequired(true);

			add(cellphoneTextField = new TextField<String>("cellphone", new PropertyModel<String>(user, "cellphone")));
			cellphoneTextField.setRequired(true);

			add(companyTextField = new TextField<String>("company", new PropertyModel<String>(user, "company")));
			companyTextField.setRequired(true);

			add(addressTextField = new TextField<String>("address", new PropertyModel<String>(user, "address")));
			addressTextField.setRequired(true);

			add(zipTextField = new TextField<String>("zip", new PropertyModel<String>(user, "zip")));
			zipTextField.setRequired(true);

			birthdayTextField = new DateTextField("birthday", new PropertyModel<Date>(user, "birthday"), Constant.DATE_PATTERN);
			birthdayTextField.add(new DatePicker());
			birthdayTextField.setRequired(true);

			add(birthdayTextField);

			add(lastNameTextField = new TextField<String>("lastName", new PropertyModel<String>(user, "lastname")));
			lastNameTextField.setRequired(true);

			add(firstNameTextField = new TextField<String>("firstName", new PropertyModel<String>(user, "firstname")));
			firstNameTextField.setRequired(true);

			ChoiceRenderer<String> choiceRenderer = new ChoiceRenderer<String>("value", "key");

			if (user.getCountry() != null) {
				for (Country c : countryList) {
					if (c.getId() == user.getCountry().getId().longValue()) {
						country = new SelectOption(c.getId(), c.getName());
						log.debug("value set :" + country.getKey() + country.getValue());
						break;
					}
				}
			}

			countrys = new DropDownChoice("country", new PropertyModel<String>(this, "country"), new AbstractReadOnlyModel() {
				public Object getObject() {
					List<SelectOption> selects = new ArrayList<SelectOption>();
					for (Country o : countryList) {
						selects.add(new SelectOption(o.getId(), o.getName()));
					}
					return selects;
				}
			}, choiceRenderer);
			add(countrys);

			if (user.getLanguage() != null) {
				List<Language> ls = infoService.getLanguages();
				for (Language l : ls) {
					if (l.getId() == user.getLanguage().getId().longValue()) {
						language = new SelectOption(l.getId(), l.getName());
						break;
					}
				}
			}

			languages = new DropDownChoice("language", new PropertyModel<String>(this, "language"), new AbstractReadOnlyModel() {
				public Object getObject() {
					List<SelectOption> selects = new ArrayList<SelectOption>();
					for (Language o : languageList) {
						selects.add(new SelectOption(o.getId(), o.getName()));
					}
					return selects;
				}
			}, choiceRenderer);
			add(languages);

			Button updateBtn = new Button("update") {
				public void onSubmit() {
					// update();
					log.debug("button1 on submit");
//					if (page == null)
//						setResponsePage(MyProfilePage.class);
//					else
//						setResponsePage(page);
				}
			};
			add(updateBtn);
			// updateBtn.setDefaultFormProcessing(false);
			// Button applyRoleBtn = new Button("applyRole") {
			// public void onSubmit() {
			// // update();
			// log.debug("button2 on submit");
			// this.setResponsePage(OnboardPage.class);
			// }
			// };
			// add(applyRoleBtn);
			// updateBtn.setDefaultFormProcessing(false);
		}

		public void update() {
			genderRadioChoice.setMarkupId(gender + "");
			user.setGender(gender + "");
			for (Country c : countryList) {
				if (c.getId() == country.getKey()) {
					user.setCountry(c);
					break;
				}
			}
			for (Language l : languageList) {
				if (l.getId() == language.getKey()) {
					user.setLanguage(l);
					break;
				}
			}
			userService.updateUser(user);
		}

		public boolean isValidFormData() {
			final String zipValid = "\\d+";
			final String emailValid = ".+@.+";
			if (!Pattern.matches(zipValid, user.getZip())) {
				error("Zip code format error.");
				return false;
			}
			if (!Pattern.matches(emailValid, user.getEmail())) {
				error("Email format error.");
				return false;
			}
			return true;
		}

		public final void onSubmit() {
			if (!isValidFormData())
				return;
			update();
			// int veri = VerificationGenerator.getNumVerificationCode(1000000);
			// log.debug("form on submit");
			// update();
			//			
			// log.info("Send verification code. The code is:" + veri);
			// sms.send(user.getCellphone(), "store front portal", veri + "");
			// setResponsePage(VerificationPage.class);

		}
	}

}

// $Id$