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
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.Component;
import org.apache.wicket.authorization.strategies.role.metadata.MetaDataRoleAuthorizationStrategy;
import org.apache.wicket.extensions.breadcrumb.IBreadCrumbModel;
import org.apache.wicket.extensions.breadcrumb.panel.BreadCrumbPanel;
import org.apache.wicket.extensions.breadcrumb.panel.IBreadCrumbPanelFactory;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.hp.sdf.ngp.common.annotation.Value;
import com.hp.sdf.ngp.model.RoleCategory;
import com.hp.sdf.ngp.service.UserService;
import com.hp.sdf.ngp.ui.common.AccountSearchCondition;
import com.hp.sdf.ngp.ui.common.SelectOption;
import com.hp.sdf.ngp.workflow.Privilege;

import edu.emory.mathcs.backport.java.util.Arrays;

public class AccountSearchPanel extends BreadCrumbPanel {

	private static final long serialVersionUID = 2847203327312187476L;

	private static final Log log = LogFactory.getLog(AccountSearchPanel.class);

	private SearchAccountTabelPanel searchAccountTabelPanel = null;

	private AccountSearchCondition accountSearchCondition = null;

	public AccountSearchPanel(String id, final IBreadCrumbModel breadCrumbModel) {
		super(id, breadCrumbModel);

		SearchAccountForm form = new SearchAccountForm("searchAccountForm");
		MetaDataRoleAuthorizationStrategy.authorize(form, Component.RENDER, Privilege.SEARCHACCOUNT);
		add(form);

		searchAccountTabelPanel = new SearchAccountTabelPanel("accountsTable", accountSearchCondition, breadCrumbModel);
		MetaDataRoleAuthorizationStrategy.authorize(searchAccountTabelPanel, Component.RENDER, Privilege.VIEWACCOUNTLIST);
		add(searchAccountTabelPanel);
	}

	class SearchAccountForm extends Form<Void> {

		private static final long serialVersionUID = 404463636873167639L;

		@SpringBean
		private UserService userService;

		private String accountName;

		private String providerName;

		private SelectOption roleOption;

		private String ignoredRoles = "Admin,User";

		@Value("roles.display.ignored")
		public void setIgnoredRoles(String ignoredRoles) {
			this.ignoredRoles = ignoredRoles;
		}

		public SearchAccountForm(String id) {
			super(id);

			TextField<String> accountName = new TextField<String>("accountName", new PropertyModel<String>(this, "accountName"));
			add(accountName);

			TextField<String> providerNameText = new TextField<String>("providerName", new PropertyModel<String>(this, "providerName"));
			add(providerNameText);

			ChoiceRenderer<SelectOption> choiceRenderer = new ChoiceRenderer<SelectOption>("value", "key");
			DropDownChoice<SelectOption> roleNameChoice = new DropDownChoice<SelectOption>("roleName", new PropertyModel<SelectOption>(this, "roleOption"),
					new AbstractReadOnlyModel<List<SelectOption>>() {

						private static final long serialVersionUID = 1L;

						public List<SelectOption> getObject() {
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
			add(roleNameChoice);

			Button newAccount = new Button("newAccount") {
				private static final long serialVersionUID = 8179675420318996436L;

				public void onSubmit() {

					activate(new IBreadCrumbPanelFactory() {
						private static final long serialVersionUID = 3840521373650730173L;

						public BreadCrumbPanel create(String componentId, IBreadCrumbModel breadCrumbModel) {
							return new AccountNewPanel(componentId, breadCrumbModel);
						}
					});
				}
			};
			MetaDataRoleAuthorizationStrategy.authorize(newAccount, Component.RENDER, Privilege.NEWACCOUNT);
			add(newAccount);

			add(new Button("search") {
				private static final long serialVersionUID = 2263842147860607342L;

				public void onSubmit() {
					log.debug("accountName: " + SearchAccountForm.this.accountName);
					log.debug("providerName: " + SearchAccountForm.this.providerName);

					accountSearchCondition = new AccountSearchCondition();
					accountSearchCondition.setAccountName(SearchAccountForm.this.accountName);
					accountSearchCondition.setProviderName(providerName);

					if (roleOption != null) {
						String roleName = roleOption.getValue();
						Long roleId = roleOption.getKey();
						log.debug("roleId :" + roleId);
						log.debug("roleName :" + roleName);

						accountSearchCondition.setAccountRole(roleName);
					}

					log.debug("searchAccountTabelPanel.commentsView.updateModel");
					if (searchAccountTabelPanel.accountsView != null) {
						searchAccountTabelPanel.accountsView.updateModel(accountSearchCondition);
					}

					// set roleOption to null
					roleOption = null;
				}

			});
		}
	}

	public String getTitle() {
		return this.getLocalizer().getString("title", this, "Search Account");
	}
}