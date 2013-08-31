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
package com.hp.sdf.ngp.ui.page.oam.category;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.PageParameters;
import org.apache.wicket.extensions.breadcrumb.IBreadCrumbModel;
import org.apache.wicket.extensions.breadcrumb.panel.BreadCrumbPanel;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.hp.sdf.ngp.api.search.SearchExpression;
import com.hp.sdf.ngp.api.search.Condition.StringComparer;
import com.hp.sdf.ngp.model.Category;
import com.hp.sdf.ngp.search.condition.category.CategoryDisplayNameCondition;
import com.hp.sdf.ngp.search.condition.category.CategoryExternalIdCondition;
import com.hp.sdf.ngp.search.engine.SearchExpressionImpl;
import com.hp.sdf.ngp.service.ApplicationService;
import com.hp.sdf.ngp.ui.common.CheckPanel;
import com.hp.sdf.ngp.ui.common.PromptPanel;

public class NewCategoryPanel extends BreadCrumbPanel {

	private static final long serialVersionUID = -8920226863398352415L;

	private static final Log log = LogFactory.getLog(NewCategoryPanel.class);

	private PromptPanel promptPanel;

	public NewCategoryPanel(String id, IBreadCrumbModel breadCrumbModel) {
		super(id, breadCrumbModel);

		this.add(new NewCategoryForm("newCategoryForm"));

		promptPanel = new PromptPanel("promptPanel", getLocalizer().getString("promptTitle", this), getLocalizer().getString("successMsg", this), null, StringUtils.EMPTY);
		add(promptPanel);
	}

	public final class NewCategoryForm extends Form<Void> {

		private static final long serialVersionUID = 1L;

		Category category = new Category();

		@SpringBean
		private ApplicationService applicationService;

		private String externalId;

		private String parentExternalId;

		private String name = "";

		private String displayName;

		public NewCategoryForm(String id) {
			super(id);

			this.add(new FeedbackPanel("feedBack"));
			TextField<String> externalIdField = new TextField<String>("externalId", new PropertyModel<String>(this, "externalId"));
			externalIdField.setRequired(true);
			add(externalIdField);

			TextField<String> parentExternalIdField = new TextField<String>("parentExternalId", new PropertyModel<String>(this, "parentExternalId"));
			parentExternalIdField.setRequired(true);
			add(parentExternalIdField);

			TextField<String> categoryNameField = new TextField<String>("name", new PropertyModel<String>(this, "name"));
			add(categoryNameField);

			TextField<String> displayNameField = new TextField<String>("displayName", new PropertyModel<String>(this, "displayName"));
			displayNameField.setRequired(true);
			add(displayNameField);

			final CheckPanel checkPanel = new CheckPanel("checkPanel", getLocalizer().getString("promptTitle", NewCategoryPanel.this), getLocalizer().getString("checkPanel.confirmMsg",
					NewCategoryPanel.this)) {

				private static final long serialVersionUID = 7465424124110025236L;

				@Override
				public void howDo() {
					applicationService.saveCategory(category);
					promptPanel.show();
				}
			};
			add(checkPanel);

			final CheckPanel checkPanel2 = new CheckPanel("checkPanel2", getLocalizer().getString("promptTitle", NewCategoryPanel.this), getLocalizer().getString("confirmMsg", NewCategoryPanel.this)) {

				private static final long serialVersionUID = 7465424124110025236L;

				@Override
				public void howDo() {

					log.debug("externalId :" + externalId);
					log.debug("parentExternalId :" + parentExternalId);
					log.debug("name :" + name);
					log.debug("displayName :" + displayName);

					// check ExternalId
					if (!StringUtils.isEmpty(externalId)) {
						SearchExpression searchExpression = new SearchExpressionImpl();
						searchExpression.addCondition(new CategoryExternalIdCondition(externalId, StringComparer.EQUAL, true, false));
						List<Category> list = applicationService.searchCategory(searchExpression);
						if (list != null && list.size() > 0) {
							error(getLocalizer().getString("msg.error.save.duplicatedExternalId", this, "Duplicated ExternalId!"));
							return;
						}
					}
					category.setExternalId(externalId);

					// categoryName
					if (StringUtils.isEmpty(name)) {
						name = " ";
					} else if (null != applicationService.getCategoryByName(name)) {
						error(getLocalizer().getString("msg.error.save.duplicatedCategoryName", this, "Duplicated category name!"));
						return;
					}
					category.setName(name);

					// check parentExternalId
					Category parentCategory = null;
					if (!StringUtils.isEmpty(parentExternalId) && !parentExternalId.equalsIgnoreCase(getLocalizer().getString("cateRoot", NewCategoryPanel.this, "root"))) {
						SearchExpression searchExpression = new SearchExpressionImpl();
						searchExpression.addCondition(new CategoryExternalIdCondition(parentExternalId, StringComparer.EQUAL, true, false));
						List<Category> list = applicationService.searchCategory(searchExpression);
						if (null != list && list.size() > 0) {
							parentCategory = list.get(0);
						} else {
							error(getLocalizer().getString("msg.error.save.parentExternalId", this, "No category found with Parent Category External ID!"));
							return;
						}
					}
					if (parentCategory != null) {
						category.setParentCategory(parentCategory);
					}

					// displayName
					List<Category> cas = null;
					if (!StringUtils.isEmpty(displayName)) {
						SearchExpressionImpl searchExpression = new SearchExpressionImpl();
						searchExpression.addCondition(new CategoryDisplayNameCondition(displayName, StringComparer.EQUAL, true, false));
						cas = applicationService.searchCategory(searchExpression);
					}
					category.setDisplayName(displayName);

					if (cas != null && cas.size() > 0) {
						checkPanel.show();
						return;
					} else {
						applicationService.saveCategory(category);
						promptPanel.show();
					}

				}
			};
			add(checkPanel2);

			Button save = new Button("save") {
				private static final long serialVersionUID = 1510836418186350793L;

				public void onSubmit() {
					checkPanel2.show();
				}
			};
			//save.add(Tools.addConfirmJs(getLocalizer().getString("confirmMsg", NewCategoryPanel.this)));
			add(save);

			Button backBtn = new Button("back") {

				private static final long serialVersionUID = 8116120281966867260L;

				public void onSubmit() {
					PageParameters parameters = new PageParameters();
					CategoryListPage page = new CategoryListPage(parameters);
					setResponsePage(page);
				}
			};
			backBtn.setDefaultFormProcessing(false);
			add(backBtn);
		}
	}

	public String getTitle() {
		return this.getLocalizer().getString("title", this, "New Category");
	}

}
