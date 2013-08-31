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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.Component;
import org.apache.wicket.authorization.strategies.role.metadata.MetaDataRoleAuthorizationStrategy;
import org.apache.wicket.extensions.breadcrumb.IBreadCrumbModel;
import org.apache.wicket.extensions.breadcrumb.panel.BreadCrumbPanel;
import org.apache.wicket.extensions.breadcrumb.panel.BreadCrumbPanelLink;
import org.apache.wicket.extensions.breadcrumb.panel.IBreadCrumbPanelFactory;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.hp.sdf.ngp.api.search.SearchExpression;
import com.hp.sdf.ngp.api.search.Condition.StringComparer;
import com.hp.sdf.ngp.common.annotation.Value;
import com.hp.sdf.ngp.model.ContentProviderOperator;
import com.hp.sdf.ngp.model.Provider;
import com.hp.sdf.ngp.model.RoleCategory;
import com.hp.sdf.ngp.model.SubscriberProfile;
import com.hp.sdf.ngp.model.UserProfile;
import com.hp.sdf.ngp.search.condition.contentprovideroperator.ContentProviderOperatorUseridCondition;
import com.hp.sdf.ngp.search.condition.subscriberprofile.SubscriberProfileOwnerTesterUserIdCondition;
import com.hp.sdf.ngp.search.engine.SearchExpressionImpl;
import com.hp.sdf.ngp.service.ApplicationService;
import com.hp.sdf.ngp.service.SubscriptionService;
import com.hp.sdf.ngp.service.UserService;
import com.hp.sdf.ngp.ui.WicketSession;
import com.hp.sdf.ngp.ui.common.AccountSearchCondition;
import com.hp.sdf.ngp.ui.common.CheckPanel;
import com.hp.sdf.ngp.ui.common.CustomizePagingNavigator;
import com.hp.sdf.ngp.ui.provider.AccountSearchDataProvider;
import com.hp.sdf.ngp.workflow.Privilege;

import edu.emory.mathcs.backport.java.util.Arrays;

public class SearchAccountTabelPanel extends Panel {

	private static final long serialVersionUID = 8006956197490194311L;

	private static final Log log = LogFactory.getLog(SearchAccountTabelPanel.class);

	public AccountsView accountsView;

	private Map<String, String> accountsMap = new HashMap<String, String>();

	public SearchAccountTabelPanel(String id, AccountSearchCondition accountSearchCondition, IBreadCrumbModel breadCrumbModel) {
		super(id);

		log.debug("accountSearchCondition :" + accountSearchCondition);
		add(new AccountsForm("accountsForm", accountSearchCondition, breadCrumbModel));

	}

	class AccountsForm extends Form<Void> {

		private static final long serialVersionUID = 427248307610664062L;

		@SpringBean
		private UserService userService;

		@SpringBean
		private ApplicationService applicationService;

		@SpringBean
		private SubscriptionService subscriptionService;

		private static final int itemsPerPage = 50;

		private boolean groupSelected;

		public boolean isGroupSelected() {
			return groupSelected;
		}

		public void setGroupSelected(boolean groupSelected) {
			this.groupSelected = groupSelected;
		}

		public AccountsForm(String id, AccountSearchCondition accountSearchCondition, IBreadCrumbModel breadCrumbModel) {
			super(id);

			this.add(new FeedbackPanel("feedBack"));

			CheckBox groupselector = new CheckBox("groupselector", new PropertyModel<Boolean>(this, "groupSelected"));
			groupselector.setMarkupId("groupselector");
			add(groupselector);

			AccountSearchDataProvider dataProvider = new AccountSearchDataProvider(userService, accountSearchCondition);

			accountsView = new AccountsView("accounts", dataProvider, itemsPerPage, userService, breadCrumbModel);
			add(accountsView);

			add(new CustomizePagingNavigator("navigator", accountsView));

			
			final CheckPanel checkPanel = new CheckPanel("checkPanel", getLocalizer().getString("promptTitle", SearchAccountTabelPanel.this), getLocalizer().getString("confirmMsg", SearchAccountTabelPanel.this)) {
				private static final long serialVersionUID = 7465424124110025236L;
				@Override
				public void howDo() {
					log.debug("delete the users and subscribers.");

					Set<String> userIds = accountsMap.keySet();
					if (userIds != null && userIds.size() > 0) {
						for (String userId : userIds) {

							if (null == userService.getUser(userId)) {
								error(getLocalizer().getString("msg.error.del.noaccount", this, "Account does not exist!"));
								return;
							}

							if (WicketSession.get().getUserId() != null && WicketSession.get().getUserId().equalsIgnoreCase(userId)) {
								error(getLocalizer().getString("msg.error.del.delSelf", this, "Can not delete your login account!"));
								return;
							}

							SearchExpression searchExpression = new SearchExpressionImpl();
							searchExpression.addCondition(new SubscriberProfileOwnerTesterUserIdCondition(userId, StringComparer.EQUAL, false, false));
							List<SubscriberProfile> list = subscriptionService.searchSubscriber(searchExpression);
							if (list != null && list.size() > 0) {
								for (SubscriberProfile subscriberProfile : list) {
									log.debug("delete subscriberProfile whose userId is :" + userId);
									subscriptionService.deleteSubscriber(subscriberProfile.getUserId());
								}
							}

							SearchExpression searchExpression2 = new SearchExpressionImpl();
							searchExpression2.addCondition(new ContentProviderOperatorUseridCondition(userId, StringComparer.EQUAL, false, false));
							List<ContentProviderOperator> contentProviderOperatorList = applicationService.searchContentProviderOperator(searchExpression2);
							if (contentProviderOperatorList != null && contentProviderOperatorList.size() > 0) {
								for (ContentProviderOperator contentProviderOperator : contentProviderOperatorList) {
									log.debug("Delete the contentProviderOperator whose useid is :" + userId);
									applicationService.deleteContentProviderOperator(contentProviderOperator.getId());
								}
							}

							userService.deleteUser(userId);
						}
					} else {
						error(getLocalizer().getString("msg.error.del.noselect", this, "Please select one Account at least!"));
						return;
					}

					log.debug("Set SelectAll checkbox to not checked.");
					setGroupSelected(false);
					log.debug("Clear the accountsMap.");
					accountsMap.clear();

				}
			};
			add(checkPanel);
			
			Button delete = new Button("delete") {
				private static final long serialVersionUID = 2189543498800077396L;

				public void onSubmit() {
					checkPanel.show();
				}
			};
			//delete.add(Tools.addConfirmJs(getLocalizer().getString("confirmMsg", SearchAccountTabelPanel.this)));
			MetaDataRoleAuthorizationStrategy.authorize(delete, Component.RENDER, Privilege.DELETEACCOUNT);
			add(delete);
		}

	}

	class AccountsView extends DataView<UserProfile> {

		private static final long serialVersionUID = 2738548802166595044L;

		private IBreadCrumbModel breadCrumbModel;

		private UserService userService;

		@SpringBean
		private ApplicationService applicationService;

		private String ignoredRoles = "User";

		@Value("roles.display.ignoredUser")
		public void setIgnoredRoles(String ignoredRoles) {
			this.ignoredRoles = ignoredRoles;
		}

		private List<String> ignoredRoleList = new ArrayList<String>();

		public AccountsView(String id, IDataProvider<UserProfile> dataProvider, int itemsPerPage, UserService userService, IBreadCrumbModel breadCrumbModel) {
			super(id, dataProvider, itemsPerPage);
			this.userService = userService;
			this.breadCrumbModel = breadCrumbModel;

			log.debug("ignoredRoles :" + ignoredRoles);
			if (StringUtils.isNotEmpty(ignoredRoles)) {
				ignoredRoleList = Arrays.asList(ignoredRoles.toUpperCase().split(","));
			}
		}

		public void updateModel(AccountSearchCondition accountSearchCondition) {
			AccountSearchDataProvider dataProvider = (AccountSearchDataProvider) this.getDataProvider();
			dataProvider.setAccountSearchCondition(accountSearchCondition);
		}

		protected void populateItem(Item<UserProfile> item) {

			final UserProfile userProfile = item.getModelObject();

			List<RoleCategory> roles = userService.getRoleCategoryByUserId(userProfile.getUserid());

			item.add(new CheckBox("select", new IModel<Boolean>() {

				private static final long serialVersionUID = 7865238665776348241L;

				public Boolean getObject() {

					if (accountsMap != null && accountsMap.size() != 0) {
						String o = accountsMap.get(userProfile.getUserid());
						return null == o ? false : true;
					}

					return false;
				}

				public void setObject(Boolean object) {
					if (object) {
						accountsMap.put(userProfile.getUserid(), userProfile.getUserid());
					}
				}

				public void detach() {
				}
			}));

			item.add(new Label("accountName", userProfile.getUserid()));

			StringBuilder roleName = new StringBuilder();
			if (roles != null && roles.size() > 0) {
				for (int i = 0; i < roles.size(); i++) {
					RoleCategory roleCategory = roles.get(i);

					if(i !=0 && (i < roles.size() - 1)) {
						roleName.append(",");
					}
					
					if (ignoredRoleList.contains(roleCategory.getRoleName().toUpperCase())) {
						continue;
					}

					roleName.append(roleCategory.getRoleName());
					
				}
			}
			item.add(new Label("accountRole", roleName.toString()));

			/*
			 * BreadCrumbPanelLink userLifeCycleLink = new
			 * BreadCrumbPanelLink("userLifeCycle", breadCrumbModel, new
			 * IBreadCrumbPanelFactory() {
			 * 
			 * private static final long serialVersionUID = 1L;
			 * 
			 * public BreadCrumbPanel create(String componentId,
			 * IBreadCrumbModel breadCrumbModel) { return new
			 * UserLifecycleActionHistoryDetailPanel(componentId,
			 * breadCrumbModel, userProfile.getUserid()); } });
			 * item.add(userLifeCycleLink);
			 */

			SearchExpression searchExpression2 = new SearchExpressionImpl();
			searchExpression2.addCondition(new ContentProviderOperatorUseridCondition(userProfile.getUserid(), StringComparer.EQUAL, false, false));
			List<ContentProviderOperator> contentProviderOperatorList = applicationService.searchContentProviderOperator(searchExpression2);

			String providerName = "";
			if (contentProviderOperatorList != null && contentProviderOperatorList.size() > 0) {
				ContentProviderOperator contentProviderOperator = contentProviderOperatorList.get(0);
				if (contentProviderOperator != null && contentProviderOperator.getAssetProvider() != null) {
					Provider provider = applicationService.getAssetProviderById(contentProviderOperator.getAssetProvider().getId());
					providerName = provider.getName();
					log.debug("providerName :" + providerName);
				}
			}

			item.add(new Label("providerName", providerName));

			BreadCrumbPanelLink detail = new BreadCrumbPanelLink("detail", breadCrumbModel, new IBreadCrumbPanelFactory() {

				private static final long serialVersionUID = 1L;

				public BreadCrumbPanel create(String componentId, IBreadCrumbModel breadCrumbModel) {
					return new AccountDetailPanel(componentId, breadCrumbModel, userProfile);
				}
			});
			MetaDataRoleAuthorizationStrategy.authorize(detail, Component.RENDER, Privilege.EDITACCOUNT);
			item.add(detail);
		}

	}

}
