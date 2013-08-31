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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.extensions.breadcrumb.IBreadCrumbModel;
import org.apache.wicket.extensions.breadcrumb.panel.IBreadCrumbPanelFactory;
import org.apache.wicket.extensions.yui.calendar.DatePicker;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.RadioChoice;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.hp.sdf.ngp.model.Country;
import com.hp.sdf.ngp.model.Language;
import com.hp.sdf.ngp.model.UserProfile;
import com.hp.sdf.ngp.service.InfoService;
import com.hp.sdf.ngp.service.UserService;
import com.hp.sdf.ngp.ui.WicketSession;
import com.hp.sdf.ngp.ui.common.SelectOption;
import com.hp.sdf.ngp.ui.dynamicForm.DynamicForm;
import com.hp.sdf.ngp.workflow.jbpm.JbpmServiceHolder;

/**
 * 
 *
 */
public class CollectUserDataPanel extends WorkFlowBreadCrumbPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4648678882705760473L;

	private final static Log log = LogFactory.getLog(CollectUserDataPanel.class);

	@SpringBean
	private InfoService infoService;

	@SpringBean
	private UserService userService;

	private UserProfile user;

	private String execId = "";

	public CollectUserDataPanel(String id, IBreadCrumbModel breadCrumbModel, String execId) {
		super(id, breadCrumbModel);
		this.execId = execId;
		user = userService.getUser(WicketSession.get().getUserId());
		// log.debug(action + " " + workFlowContext);
		this.add(new FeedbackPanel("feedBack"));
		this.add(new MyProfileForm("myProfileForm"));
	}

	public class MyProfileForm extends DynamicForm {
		/**
		 * 
		 */
		private static final long serialVersionUID = 2796937167497116739L;

		DateFormat df;

		String birthday;

		SelectOption country;

		SelectOption language;

		Integer gender;

		List<Country> countryList = infoService.getCountrys();

		List<Language> languageList = infoService.getLanguages();

		TextField<String> userNameTextField;

		TextField<String> passwordTextField;

		TextField<String> emailTextField;

		RadioChoice<String> genderRadioChoice;

		TextField<String> cellphoneTextField;

		TextField<String> companyTextField;

		TextField<String> addressTextField;

		TextField<String> zipTextField;

		TextField<String> birthdayTextField;

		TextField<String> lastNameTextField;

		TextField<String> firstNameTextField;

		DropDownChoice<String> countrys;

		DropDownChoice<String> languages;

		IBreadCrumbPanelFactory ifactory;

		public IBreadCrumbPanelFactory getIfactory() {
			return ifactory;
		}

		public void setIfactory(IBreadCrumbPanelFactory ifactory) {
			this.ifactory = ifactory;
		}

		public MyProfileForm(String id) {
			super(id);
			genContent();
			Map<Integer, String> genderMap = Gender.getGenderMap();
			for (Entry<Integer, String> e : genderMap.entrySet()) {
				log.debug("gender key=" + e.getKey() + " value=" + e.getValue());
			}
			try {
				gender = user.getGender() == null ? null : Integer.parseInt(user.getGender());
			} catch (Throwable e) {

			}

			log.debug("user gender=" + gender);

			add(userNameTextField = new TextField<String>("userName", new PropertyModel<String>(user, "userid")));
			userNameTextField.setRequired(true);

			add(emailTextField = new TextField<String>("email", new PropertyModel<String>(user, "email")));
			emailTextField.setRequired(true);

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

			DatePicker dp = new DatePicker();
			df = new SimpleDateFormat(dp.FORMAT_DATE);
			birthday = user.getBirthday() == null ? "" : df.format(user.getBirthday());

			add(birthdayTextField = new TextField<String>("birthday", new PropertyModel<String>(this, "birthday")));
			birthdayTextField.setRequired(true);

			birthdayTextField.add(dp);

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
			countrys.setRequired(true);
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
			languages.setRequired(true);
			add(languages);
		}

		public void updateUserProfile() {
			genderRadioChoice.setMarkupId(gender + "");
			try {
				log.debug(gender);
				user.setGender(gender + "");
				user.setBirthday(df.parse(birthday));
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

			} catch (ParseException pe) {
				log.error(pe);
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

		public void onSubmit() {
			if (!isValidFormData())
				return;
			updateUserProfile();

			HashMap<String, Object> variables = new HashMap<String, Object>();
			variables.put("component", this);
			JbpmServiceHolder.executionService.signalExecutionById(execId, variables);

			activate(ifactory);

		}

		@Override
		protected Object getDomainModel() {
			return user;
		}

		@Override
		protected String getDataConfigFile() {
			return "userInfoForm.xml";
		}

	}

	public String getTitle() {
		return this.getLocalizer().getString("title", this, "Collect user information");
	}

	public static void main(String[] args) {
		final String zipValid = "\\d+";
		final String emailValid = ".+@.+";
		System.out.println(Pattern.matches(zipValid, "d23"));
		System.out.println(Pattern.matches(emailValid, "liwei_snake@yahoo.com.cn"));
	}

}

// $Id$