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
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.collections.ListUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.PageParameters;
import org.apache.wicket.extensions.breadcrumb.IBreadCrumbModel;
import org.apache.wicket.extensions.breadcrumb.panel.BreadCrumbPanel;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.ListMultipleChoice;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.hp.sdf.ngp.api.exception.StoreClientServiceException;
import com.hp.sdf.ngp.api.search.SearchExpression;
import com.hp.sdf.ngp.api.search.Condition.NumberComparer;
import com.hp.sdf.ngp.api.search.Condition.StringComparer;
import com.hp.sdf.ngp.api.thirdparty.SubscriberInfoService;
import com.hp.sdf.ngp.model.ContentProviderOperator;
import com.hp.sdf.ngp.model.Provider;
import com.hp.sdf.ngp.model.RoleCategory;
import com.hp.sdf.ngp.model.SubscriberProfile;
import com.hp.sdf.ngp.model.UserProfile;
import com.hp.sdf.ngp.search.condition.contentprovideroperator.ContentProviderOperatorAssetProviderIdCondition;
import com.hp.sdf.ngp.search.condition.contentprovideroperator.ContentProviderOperatorUseridCondition;
import com.hp.sdf.ngp.search.condition.subscriberprofile.SubscriberProfileMsisdnCondition;
import com.hp.sdf.ngp.search.condition.subscriberprofile.SubscriberProfileOwnerTesterUserIdCondition;
import com.hp.sdf.ngp.search.engine.SearchExpressionImpl;
import com.hp.sdf.ngp.service.ApplicationService;
import com.hp.sdf.ngp.service.SubscriptionService;
import com.hp.sdf.ngp.service.UserService;
import com.hp.sdf.ngp.ui.common.CheckPanel;
import com.hp.sdf.ngp.ui.common.PromptPanel;

public class AccountDetailPanel extends BreadCrumbPanel {

	private static final long serialVersionUID = -8920226863398352415L;

	private static final Log log = LogFactory.getLog(AccountDetailPanel.class);

	private PromptPanel promptPanel;

	public AccountDetailPanel(String id, IBreadCrumbModel breadCrumbModel, UserProfile userProfile) {
		super(id, breadCrumbModel);

		this.add(new AccountDetailForm("accountDetailForm", userProfile));

		promptPanel = new PromptPanel("promptPanel", getLocalizer().getString("promptTitle", this), getLocalizer().getString("successMsg", this), null, StringUtils.EMPTY);
		add(promptPanel);
	}

	public final class AccountDetailForm extends Form<Void> {

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

		private String providerID;

		private String oldPassword;

		private String newPassword;

		private String confirmPassword;

		private String newMSISDN;

		private ListMultipleChoice<String> originals;

		private String oldProviderID = "";

		// The Selected options in the list choices.
		private List<String> selectedOriginals;

		public AccountDetailForm(String id, final UserProfile userProfile) {
			super(id);

			this.add(new FeedbackPanel("feedBack"));

			Label userIdLabel = new Label("userId", userProfile.getUserid());
			add(userIdLabel);

			List<RoleCategory> roles = userService.getRoleCategoryByUserId(userProfile.getUserid());
			StringBuilder roleName = new StringBuilder();
			if (roles != null && roles.size() > 0) {
				for (int i = 0; i < roles.size(); i++) {
					RoleCategory roleCategory = roles.get(i);
					roleName.append(roleCategory.getRoleName());

					if (i < roles.size() - 1) {
						roleName.append(",");
					}
				}
			}

			Label roleNameLabel = new Label("roleName", roleName.toString());
			add(roleNameLabel);

			SearchExpression searchExpression = new SearchExpressionImpl();
			searchExpression.addCondition(new ContentProviderOperatorUseridCondition(userProfile.getUserid(), StringComparer.EQUAL, false, false));
			List<ContentProviderOperator> contentProviderOperatorList = applicationService.searchContentProviderOperator(searchExpression);

			if (contentProviderOperatorList != null && contentProviderOperatorList.size() > 0) {
				ContentProviderOperator contentProviderOperator = contentProviderOperatorList.get(0);
				if (contentProviderOperator != null && contentProviderOperator.getAssetProvider() != null) {
					Provider provider = applicationService.getAssetProviderById(contentProviderOperator.getAssetProvider().getId());
					oldProviderID = provider.getId() + "";
				}
			}

			TextField<String> providerIDText = new TextField<String>("providerID", new PropertyModel<String>(this, "providerID"));
			log.debug("oldProviderID :" + oldProviderID);
			providerIDText.setDefaultModelObject(oldProviderID);
			add(providerIDText);

			PasswordTextField oldPasswordText = new PasswordTextField("oldPassword", new PropertyModel<String>(this, "oldPassword"));
			/**
			 * need to add required =false, otherwise ajaxButton can not work.
			 */
			oldPasswordText.setRequired(true);
			log.debug("userProfile.getPassword() :" + userProfile.getPassword());
			add(oldPasswordText);

			final PasswordTextField newPasswordText = new PasswordTextField("newPassword", new PropertyModel<String>(this, "newPassword"));
			newPasswordText.setRequired(true);
			add(newPasswordText);

			final PasswordTextField confirmPasswordText = new PasswordTextField("confirmPassword", new PropertyModel<String>(this, "confirmPassword"));
			confirmPasswordText.setRequired(true);
			add(confirmPasswordText);

			TextField<String> newMSISDNField = new TextField<String>("newMSISDN", new PropertyModel<String>(this, "newMSISDN"));
			add(newMSISDNField);

			Button add = new Button("add") {

				private static final long serialVersionUID = 1124467472760934679L;

				public void onSubmit() {
					log.debug("newMSISDN :" + newMSISDN);

					if (StringUtils.isNotEmpty(newMSISDN)) {

						Pattern pattern = Pattern.compile("\\d{11}");
						Matcher matcher = pattern.matcher(newMSISDN);
						if (!matcher.matches()) {
							error(AccountDetailPanel.this.getLocalizer().getString("msg.error.save.invalidmsisdn", AccountDetailPanel.this, "msisdn is 11 number."));
							return;
						}

						List<String> originalChoices = getChoices(originals);
						if (!originalChoices.contains(newMSISDN)) {
							SearchExpression searchExpression = new SearchExpressionImpl();
							searchExpression.addCondition(new SubscriberProfileMsisdnCondition(newMSISDN, StringComparer.EQUAL, false, false));
							if (subscriptionService.searchSubscriberProfileCount(searchExpression) <= 0) {
								originalChoices.add(newMSISDN);
							} else {
								error(getLocalizer().getString("msg.error.save.noSubscriberForMsisdn", AccountDetailPanel.this, "Subscriber does not exist this msisdn!"));
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
					return getMsisdnList(userProfile.getUserid());
				}

			});

			// make sure we can update this wicket component.
			originals.setOutputMarkupId(true);
			add(originals);

			Button delete = new Button("delete") {

				private static final long serialVersionUID = -2943734077697127565L;

				public void onSubmit() {
					List<String> originalChoices = getChoices(originals);
					originalChoices.removeAll(selectedOriginals);

					originals.setChoices(originalChoices);
				}

			};
			add(delete);

			

			final CheckPanel checkPanel = new CheckPanel("checkPanel", getLocalizer().getString("promptTitle", AccountDetailPanel.this), getLocalizer().getString("confirmMsg",
					AccountDetailPanel.this)) {
				private static final long serialVersionUID = 7465424124110025236L;
				@Override
				public void howDo() {
					log.debug("oldPassword :" + oldPassword);
					log.debug("providerID :" + providerID);

					if (null != providerID) {
						if (NumberUtils.isNumber(providerID)) {
							Provider assetProvider = applicationService.getAssetProviderById(new Long(providerID));
							if (null == assetProvider) {
								error(getLocalizer().getString("msg.error.save.noProviderID", this, "providerID is not existed."));
								return;
							}
						} else {
							error(getLocalizer().getString("msg.error.save.wrongProviderID", this, "providerID is wrong format."));
							return;
						}
					}

					// only update roles ,subscriber and provider when no
					// password input
					if (StringUtils.isEmpty(oldPassword) && StringUtils.isEmpty(newPassword) && StringUtils.isEmpty(confirmPassword)) {
						if (null == userService.getUser(userProfile.getUserid())) {
							error(getLocalizer().getString("msg.error.save.nullUserID", this, "The user can not be null."));
							return;
						}
						try {
							updateSubscriber(userProfile);
						} catch (Exception exception) {
							error(getLocalizer().getString("msg.error.save.noSubscriberForMsisdn", AccountDetailPanel.this, "Subscriber does not exist this msisdn!"));
							return;
						}
					}

					if (userService.validatePassword(userProfile.getUserid(), oldPassword)) {
						try {
							validateNewPasswordAndUpdateUser(userProfile);
						} catch (Exception exception) {
							error(getLocalizer().getString("msg.error.save.noSubscriberForMsisdn", AccountDetailPanel.this, "Subscriber does not exist this msisdn!"));
							return;
						}
					} else {
						error(getLocalizer().getString("msg.error.save.oldPasswordProng", this, "The old password is wrong."));
						return;
					}

					promptPanel.show();
				}
			};
			add(checkPanel);
			
			Button save = new Button("save") {

				private static final long serialVersionUID = 1510836418186350793L;

				public void onSubmit() {
					checkPanel.show();
				}

			};
			//save.add(Tools.addConfirmJs(getLocalizer().getString("confirmMsg", AccountDetailPanel.this)));
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

		private List<String> getChoices(ListMultipleChoice<String> choice) {
			List<String> choices = new ArrayList<String>();
			choices.addAll(choice.getChoices());
			return choices;
		}
		
		private void validateNewPasswordAndUpdateUser(final UserProfile userProfile) throws StoreClientServiceException {
			if (StringUtils.isNotEmpty(newPassword) && StringUtils.isNotEmpty(confirmPassword)) {

				if (!between(newPassword.length(), 1, 32) || !between(confirmPassword.length(), 1, 32)) {
					error(getLocalizer().getString("msg.error.save.passwordLength", this, "The password length should be between 1 and 32."));
					return;
				}

				if (confirmPassword.equals(newPassword)) {
					if (null == userService.getUser(userProfile.getUserid())) {
						error(getLocalizer().getString("msg.error.save.nullUserID", this, "The user can not be null."));
						return;
					}

					log.debug("Start updating this user.");
					userService.updatePassword(userProfile.getUserid(), newPassword);
					userService.updateUser(userProfile);

					updateSubscriber(userProfile);
				} else {
					error(getLocalizer().getString("msg.error.save.passwordNotSame", this, "The two new password is not same."));
					return;
				}
			} else {
				error(getLocalizer().getString("msg.error.save.passwordEmpty", this, "The two new password are empty."));
				return;
			}
		}
		private void updateSubscriber(final UserProfile userProfile) throws StoreClientServiceException {
			List<String> originalChoices = getChoices(originals);

			log.debug("Adding SubscriberProfile By Msisdn.");
			addSubscriberProfileByMsisdn(originalChoices, userProfile.getUserid());

			log.debug("Deleting SubscriberProfile By Msisdn.");
			deleteSubscriberProfileByMsisdn(userProfile.getUserid(), originalChoices);

			log.debug("Updating providerID for this user.");
			updateProviderID(userProfile.getUserid());
		}

		private void updateProviderID(final String userid) {
			if (!oldProviderID.equals(providerID)) {

				// save new contentProviderOperator
				if (StringUtils.isNotEmpty(providerID) && NumberUtils.isNumber(providerID)) {
					Provider assetProvider = applicationService.getAssetProviderById(new Long(providerID));
					if (assetProvider != null) {
						log.debug("Save new contentProviderOperator by new providerID.");
						ContentProviderOperator contentProviderOperator = new ContentProviderOperator();
						contentProviderOperator.setAssetProvider(assetProvider);
						contentProviderOperator.setUserid(userid);
						applicationService.saveContentProviderOperator(contentProviderOperator);
					}
				}

				// find the old contentProviderOperator and delete it
				if (StringUtils.isNotEmpty(oldProviderID)) {
					SearchExpression searchExpression = new SearchExpressionImpl();
					searchExpression.addCondition(new ContentProviderOperatorUseridCondition(userid, StringComparer.EQUAL, false, false));

					Provider oldAssetProvider = applicationService.getAssetProviderById(new Long(oldProviderID));
					if (oldAssetProvider != null) {
						searchExpression.addCondition(new ContentProviderOperatorAssetProviderIdCondition(oldAssetProvider.getId(), NumberComparer.EQUAL));
					} else {
						// no ContentProviderOperator whose provider is not
						// exist
						return;
					}

					List<ContentProviderOperator> contentProviderOperatorList = applicationService.searchContentProviderOperator(searchExpression);
					if (contentProviderOperatorList != null && contentProviderOperatorList.size() > 0) {
						for (ContentProviderOperator contentProviderOperator : contentProviderOperatorList) {
							log.debug("Delete the old contentProviderOperator whose id is : " + contentProviderOperator.getId());
							applicationService.deleteContentProviderOperator(contentProviderOperator.getId());
						}
					}
				}
			}
		}

		private void addSubscriberProfileByMsisdn(List<String> originalChoices, String userid) throws StoreClientServiceException {
			for (String msisdn : originalChoices) {
				log.debug("msisdn :" + msisdn);
				String subscriberId = subscriberInfoService.getSubscriberId(msisdn);
				log.debug("subscriberId :" + subscriberId + " is for msisdn [" + msisdn + "].");
				if (StringUtils.isNotEmpty(subscriberId) && null == subscriptionService.retrieveSubscriber(subscriberId)) {
					SubscriberProfile subscriberProfile = new SubscriberProfile();
					subscriberProfile.setUserId(subscriberId);
					subscriberProfile.setMsisdn(msisdn);
					subscriberProfile.setOwnerTesterUserId(userid);
					// subscriberProfile.setClientOwnerProviderId(providerID);

					subscriptionService.saveSubscriber(subscriberProfile);
				}
			}
		}

		@SuppressWarnings("unchecked")
		private void deleteSubscriberProfileByMsisdn(String userid, List<String> originalChoices) throws StoreClientServiceException {
			// DELETE SubscriberProfile whose msisdn is deleted
			// from msisdn list
			ArrayList<String> msisdnList = getMsisdnList(userid);
			List<String> deletedMsisdnList = ListUtils.subtract(msisdnList, ListUtils.intersection(msisdnList, originalChoices));
			if (deletedMsisdnList != null && deletedMsisdnList.size() > 0) {

				for (String deletedMsisdn : deletedMsisdnList) {
					log.debug("deletedMsisdn : " + deletedMsisdn);
					String subscriberId = subscriberInfoService.getSubscriberId(deletedMsisdn);
					log.debug("subscriberId : " + subscriberId);

					// delete the SubscriberProfile by
					// subscriberId
					if (StringUtils.isNotEmpty(subscriberId)) {
						subscriptionService.deleteSubscriber(subscriberId);
					}

				}
			}
		}

		private ArrayList<String> getMsisdnList(final String userid) {
			ArrayList<String> msisdnList = new ArrayList<String>();

			SearchExpression searchExpression = new SearchExpressionImpl();
			searchExpression.addCondition(new SubscriberProfileOwnerTesterUserIdCondition(userid, StringComparer.EQUAL, false, false));
			List<SubscriberProfile> list = subscriptionService.searchSubscriber(searchExpression);
			if (list != null && list.size() > 0) {
				for (SubscriberProfile subscriberProfile : list) {
					msisdnList.add(subscriberProfile.getMsisdn());
				}
			}
			return msisdnList;
		}

		private boolean between(int passwordLength, int min, int max) {
			return passwordLength >= min && passwordLength <= max;
		}

	}

	public String getTitle() {
		return this.getLocalizer().getString("title", this, "Edit Account");
	}

}
