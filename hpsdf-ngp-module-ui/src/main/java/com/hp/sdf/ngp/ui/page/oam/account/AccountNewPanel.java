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
package com.hp.sdf.ngp.ui.page.oam.account;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.PageParameters;
import org.apache.wicket.extensions.breadcrumb.IBreadCrumbModel;
import org.apache.wicket.extensions.breadcrumb.panel.BreadCrumbPanel;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.ListMultipleChoice;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.validation.validator.StringValidator;

import com.hp.sdf.ngp.api.exception.StoreClientServiceException;
import com.hp.sdf.ngp.api.search.SearchExpression;
import com.hp.sdf.ngp.api.search.Condition.StringComparer;
import com.hp.sdf.ngp.api.thirdparty.SubscriberInfoService;
import com.hp.sdf.ngp.common.annotation.Value;
import com.hp.sdf.ngp.model.ContentProviderOperator;
import com.hp.sdf.ngp.model.Provider;
import com.hp.sdf.ngp.model.RoleCategory;
import com.hp.sdf.ngp.model.SubscriberProfile;
import com.hp.sdf.ngp.model.UserProfile;
import com.hp.sdf.ngp.search.condition.assetprovider.AssetProviderExternalIdCondition;
import com.hp.sdf.ngp.search.condition.subscriberprofile.SubscriberProfileMsisdnCondition;
import com.hp.sdf.ngp.search.engine.SearchExpressionImpl;
import com.hp.sdf.ngp.service.ApplicationService;
import com.hp.sdf.ngp.service.SubscriptionService;
import com.hp.sdf.ngp.service.UserService;
import com.hp.sdf.ngp.ui.common.CheckPanel;
import com.hp.sdf.ngp.ui.common.PromptPanel;
import com.hp.sdf.ngp.ui.common.SelectOption;
import com.hp.sdf.ngp.workflow.UserCategoryLifeCycleEngine;

import edu.emory.mathcs.backport.java.util.Arrays;

public class AccountNewPanel extends BreadCrumbPanel {

	private static final long serialVersionUID = -8920226863398352415L;

	private static final Log log = LogFactory.getLog(AccountNewPanel.class);

	private PromptPanel promptPanel;

	public AccountNewPanel(String id, IBreadCrumbModel breadCrumbModel) {
		super(id, breadCrumbModel);

		this.add(new AccountNewForm("accountNewForm"));

		promptPanel = new PromptPanel("promptPanel", getLocalizer().getString("promptTitle", this), getLocalizer().getString("successMsg", this), null, StringUtils.EMPTY);
		add(promptPanel);
	}

	public final class AccountNewForm extends Form<Void> {

		private static final long serialVersionUID = 1L;

		@SpringBean
		private UserService userService;

		@SpringBean
		private ApplicationService applicationService;

		@SpringBean
		private SubscriptionService subscriptionService;

		/**
		 * to use getSubscriberId(msisdn)
		 */
		@SpringBean
		private SubscriberInfoService subscriberInfoService;

		@SpringBean
		private UserCategoryLifeCycleEngine userCategoryLifeCycleEngine;

		private String ignoredRoles = "Admin,User";

		@Value("roles.display.ignored")
		public void setIgnoredRoles(String ignoredRoles) {
			this.ignoredRoles = ignoredRoles;
		}

		private String userId="";

		private String providerID="";

		public String getUserId() {
			return userId;
		}

		public void setUserId(String userId) {
			this.userId = userId;
		}

		public String getProviderID() {
			return providerID;
		}

		public void setProviderID(String providerID) {
			this.providerID = providerID;
		}

		public String getNewPassword() {
			return newPassword;
		}

		public void setNewPassword(String newPassword) {
			this.newPassword = newPassword;
		}

		public String getConfirmPassword() {
			return confirmPassword;
		}

		public void setConfirmPassword(String confirmPassword) {
			this.confirmPassword = confirmPassword;
		}

		private String newPassword="";

		private String confirmPassword="";

		private SelectOption roleOptions;

		private String newMSISDN="";

		public String getNewMSISDN() {
			return newMSISDN;
		}

		public void setNewMSISDN(String newMSISDN) {
			this.newMSISDN = newMSISDN;
		}

		private ListMultipleChoice<String> originals;

		// The Selected options in the list choices.
		private List<String> selectedOriginals;

		public AccountNewForm(String id) {
			super(id);

			this.add(new FeedbackPanel("feedBack"));
			TextField<String> userIdText = new TextField<String>("userId", new PropertyModel<String>(this, "userId"));
			add(userIdText);

			TextField<String> providerIDText = new TextField<String>("providerIDInput", new PropertyModel<String>(this, "providerID"));
			providerIDText.add(new StringValidator.ExactLengthValidator(20));
			add(providerIDText);

			ChoiceRenderer<SelectOption> choiceRenderer = new ChoiceRenderer<SelectOption>("value", "key");
			DropDownChoice<SelectOption> roleNameChoice = new DropDownChoice<SelectOption>("roleName", new PropertyModel<SelectOption>(this, "roleOptions"),
					new LoadableDetachableModel<List<SelectOption>>() {
						private static final long serialVersionUID = 1L;

						@SuppressWarnings("unchecked")
						protected List<SelectOption> load() {
							List<SelectOption> selects = new ArrayList<SelectOption>();

							List<RoleCategory> roles = userService.getAllRoles();
							if (roles != null && roles.size() > 0) {

								List<String> ignoredRoleList = new ArrayList<String>();
								log.debug("ignoredRoles :" + ignoredRoles);
								if (StringUtils.isNotEmpty(ignoredRoles)) {
									ignoredRoleList = Arrays.asList(ignoredRoles.toUpperCase().split(","));
								}

								for (RoleCategory o : roles) {
									if (!ignoredRoleList.contains(o.getRoleName().toUpperCase())) {
										selects.add(new SelectOption(o.getId(), o.getRoleName()));
									}
								}
							}

							return selects;
						}

					}, choiceRenderer);
			roleNameChoice.setRequired(true);
			add(roleNameChoice);

			/**
			 * need to add required =false, otherwise ajaxButton can not work.
			 */
			final PasswordTextField newPasswordText = new PasswordTextField("newPasswordInput", new PropertyModel<String>(this, "newPassword"));
			newPasswordText.setRequired(false);
			add(newPasswordText);

			final PasswordTextField confirmPasswordText = new PasswordTextField("confirmPassword", new PropertyModel<String>(this, "confirmPassword"));
			confirmPasswordText.setRequired(false);
			add(confirmPasswordText);

			TextField<String> newMSISDNField = new TextField<String>("newMSISDN", new PropertyModel<String>(this, "newMSISDN"));
			add(newMSISDNField);

			
				
			Button add = new Button("add") {

				private static final long serialVersionUID = 1124467472760934679L;
				
				public void onSubmit(){

					log.debug("newMSISDN :" + newMSISDN);

					if (StringUtils.isNotEmpty(newMSISDN)) {
						
						Pattern pattern = Pattern.compile("\\d{11}");
						Matcher matcher = pattern.matcher(newMSISDN);
						if (!matcher.matches()) {
							error(AccountNewPanel.this.getLocalizer().getString("msg.error.save.invalidmsisdn", AccountNewPanel.this, "msisdn is 11 number.") );
							return;
						}

						List<String> originalChoices = getChoices(originals);
						if (!originalChoices.contains(newMSISDN)) {
							SearchExpression searchExpression = new SearchExpressionImpl();
							searchExpression.addCondition(new SubscriberProfileMsisdnCondition(newMSISDN, StringComparer.EQUAL, false, false));
							if (subscriptionService.searchSubscriberProfileCount(searchExpression) <= 0) {
								originalChoices.add(newMSISDN);
							} else {
								error(getLocalizer().getString("msg.error.save.noSubscriberForMsisdn", AccountNewPanel.this, "Subscriber does not exist this msisdn!"));
							}
						}
						originals.setChoices(originalChoices);
					}
				
				}
			};
			add(add);

			originals = new ListMultipleChoice<String>("originals", new PropertyModel<Collection<String>>(this, "selectedOriginals"), new LoadableDetachableModel<List<String>>() {
				private static final long serialVersionUID = 3246034607367521265L;

				protected List<String> load() {
					return new ArrayList<String>();
				}
			});

			// make sure we can update this wicket component.
			originals.setOutputMarkupId(true);
			add(originals);

			Button delete = new Button("delete") {

				private static final long serialVersionUID = -2943734077697127565L;
				
				public void onSubmit(){
					List<String> originalChoices = getChoices(originals);
					originalChoices.removeAll(selectedOriginals);
					originals.setChoices(originalChoices);
				}
			};
			add(delete);

			final CheckPanel checkPanel = new CheckPanel("checkPanel", getLocalizer().getString("promptTitle", AccountNewPanel.this), getLocalizer().getString("confirmMsg",
					AccountNewPanel.this)) {
				private static final long serialVersionUID = 7465424124110025236L;
				@Override
				public void howDo() {

					log.debug("userId :" + userId);
					log.debug("providerID :" + providerID);

					if (StringUtils.isEmpty(userId)) {
						error(getLocalizer().getString("msg.error.save.nouser", this, "user is empty."));
						return;
					}

					if (userService.getUser(userId) != null) {
						error(getLocalizer().getString("msg.error.save.existeduser", this, "user have existed."));
						return;
					}

					if (null != providerID) {
						if (NumberUtils.isNumber(providerID)) {
							SearchExpression searchExpression =new SearchExpressionImpl();
							searchExpression.addCondition(new AssetProviderExternalIdCondition(providerID,StringComparer.EQUAL,true,false));
							
							List<Provider> aps = applicationService.searchAssetProvider(searchExpression);
							
							if (null == aps || aps.size()==0) {
								error(getLocalizer().getString("msg.error.save.noproviderID", this, "providerID is not existed."));
								return;
							}
						} else {
							error(getLocalizer().getString("msg.error.save.wrongproviderID", this, "providerID is wrong format."));
							return;
						}
					}

					if (StringUtils.isNotEmpty(newPassword) && StringUtils.isNotEmpty(confirmPassword)) {

						if (!between(newPassword.length(), 1, 32) || !between(confirmPassword.length(), 1, 32)) {
							error(getLocalizer().getString("msg.error.save.passwordLength", this, "The password length should be between 1 and 32."));
							return;
						}

						if (confirmPassword.equals(newPassword)) {
							log.debug("Start saving new userProfile.");


							List<String> originalChoices = getChoices(originals);

							log.debug("Adding SubscriberProfile By Msisdn.");

							try {
								addSubscriberProfileByMsisdn(originalChoices);
							} catch (Exception exception) {
								error(getLocalizer().getString("msg.error.save.noSubscriberForMsisdn", AccountNewPanel.this, "Subscriber does not exist this msisdn!"));
								return;
							}


							UserProfile userProfile = new UserProfile();
							userProfile.setUserid(userId);
							userProfile.setEmail(userId + "@portal.com");
							userProfile.setCreateDate(new Date());
							userProfile.setPassword(newPassword);
							userService.saveUser(userProfile);
							userService.updatePassword(userId, newPassword);

							log.debug("Start assigning user roles.");
							assignUserRoles();
							
							if (null != providerID && NumberUtils.isNumber(providerID)) {
								Provider assetProvider = applicationService.getAssetProviderById(new Long(providerID));
								if (assetProvider != null) {
									log.debug("Save contentProviderOperator with related providerID.");
									ContentProviderOperator contentProviderOperator = new ContentProviderOperator();
									contentProviderOperator.setAssetProvider(assetProvider);
									contentProviderOperator.setUserid(userProfile.getUserid());
									applicationService.saveContentProviderOperator(contentProviderOperator);
								}
							}

						} else {
							error(getLocalizer().getString("msg.error.save.passwordNotSame", this, "The two new password is not same."));
							return;
						}
					} else {
						error(getLocalizer().getString("msg.error.save.passwordEmpty", this, "The two new password are empty."));
						return;
					}

					promptPanel.show();

//					PageParameters parameters = new PageParameters();
//					AccountSearchPage page = new AccountSearchPage(parameters);
//					setResponsePage(page);
				}
			};
			add(checkPanel);
			
			Button save = new Button("save") {
				private static final long serialVersionUID = 1510836418186350793L;
				public void onSubmit() {
					checkPanel.show();
				}
			};
			//save.add(Tools.addConfirmJs(getLocalizer().getString("confirmMsg", AccountNewPanel.this)));
			add(save);

			Button backBtn = new Button("back") {
				private static final long serialVersionUID = 8116120281966867260L;

				public void onSubmit() {
					PageParameters parameters = new PageParameters();
					AccountSearchPage page = new AccountSearchPage(parameters);
					setResponsePage(page);
				}
			};
			backBtn.setDefaultFormProcessing(false);
			add(backBtn);
		}

		private void assignUserRoles() {

			// Default account role to "User"
			RoleCategory roleCategory = userCategoryLifeCycleEngine.getStartupRoleCategory();
			if (roleCategory != null) {
				log.debug("Default account role : " + roleCategory.getRoleName());
				userService.assignRole(userId, roleCategory.getRoleName());
			}

			if (roleOptions != null) {
				String roleName = roleOptions.getValue();
				log.debug("roleName :" + roleName);

				userService.assignRole(userId, roleName);
			}
		}

		private void addSubscriberProfileByMsisdn(List<String> originalChoices) throws StoreClientServiceException {

			for (String msisdn : originalChoices) {
				log.debug("msisdn :" + msisdn);

				String subscriberId = subscriberInfoService.getSubscriberId(msisdn);
				log.debug("subscriberId :" + subscriberId + " is for msisdn [" + msisdn + "].");
				if (StringUtils.isNotEmpty(subscriberId) && null == subscriptionService.retrieveSubscriber(subscriberId)) {
					SubscriberProfile subscriberProfile = new SubscriberProfile();
					subscriberProfile.setUserId(subscriberId);
					subscriberProfile.setMsisdn(msisdn);
					subscriberProfile.setOwnerTesterUserId(userId);
					// subscriberProfile.setClientOwnerProviderId(providerID);

					subscriptionService.saveSubscriber(subscriberProfile);
				}

			}
		}

		private List<String> getChoices(ListMultipleChoice<String> choice) {
			List<String> choices = new ArrayList<String>();
			choices.addAll(choice.getChoices());
			return choices;
		}

		private boolean between(int passwordLength, int min, int max) {
			return passwordLength >= min && passwordLength <= max;
		}

	}

	public String getTitle() {
		return this.getLocalizer().getString("title", this, "New Account");
	}

}
