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
import com.hp.sdf.ngp.ui.common.Tools;

public class CategoryEditPanel extends BreadCrumbPanel {

	private static final long serialVersionUID = -8920226863398352415L;

	private static final Log log = LogFactory.getLog(CategoryEditPanel.class);

	private PromptPanel promptPanel;

	public CategoryEditPanel(String id, IBreadCrumbModel breadCrumbModel, Category category) {
		super(id, breadCrumbModel);

		this.add(new EditCategoryForm("editCategoryForm", category));

		promptPanel = new PromptPanel("promptPanel", getLocalizer().getString("promptTitle", this), getLocalizer().getString("successMsg", CategoryEditPanel.this, "You successfully update the Category!"), null, StringUtils.EMPTY);
		add(promptPanel);
	}

	public final class EditCategoryForm extends Form<Void> {

		private static final long serialVersionUID = 1L;

		@SpringBean
		private ApplicationService applicationService;

		private String externalId;

		private String parentExternalId;

		private String name;

		private String displayName;

		public EditCategoryForm(String id, final Category category) {
			super(id);

			this.add(new FeedbackPanel("feedBack"));

			TextField<String> externalIdField = new TextField<String>("externalId", new PropertyModel<String>(this, "externalId"));
			externalIdField.setDefaultModelObject(category.getExternalId());
			externalIdField.setRequired(true);
			add(externalIdField);

			TextField<String> parentExternalIdField = new TextField<String>("parentExternalId", new PropertyModel<String>(this, "parentExternalId"));
			parentExternalIdField.setDefaultModelObject(category.getParentCategory() != null ? category.getParentCategory().getExternalId() : getLocalizer().getString("cateRoot", CategoryEditPanel.this, "root"));
			parentExternalIdField.setRequired(true);
			add(parentExternalIdField);

			TextField<String> categoryNameField = new TextField<String>("name", new PropertyModel<String>(this, "name"));
			categoryNameField.setDefaultModelObject(category.getName());
			add(categoryNameField);

			TextField<String> displayNameField = new TextField<String>("displayName", new PropertyModel<String>(this, "displayName"));
			displayNameField.setRequired(true);
			displayNameField.setDefaultModelObject(category.getDisplayName());
			add(displayNameField);
			
			final CheckPanel checkPanel = new CheckPanel("checkPanel", getLocalizer().getString("promptTitle", CategoryEditPanel.this), getLocalizer().getString("checkPanel.confirmMsg", CategoryEditPanel.this)) {

				private static final long serialVersionUID = 7465424124110025236L;

				@Override
				public void howDo() {
					applicationService.updateCategory(category);
					promptPanel.show();
				}
			};
			add(checkPanel);
			
			
			final CheckPanel checkPanel2 = new CheckPanel("checkPanel2", getLocalizer().getString("promptTitle", CategoryEditPanel.this), getLocalizer().getString("confirmMsg", CategoryEditPanel.this)) {

				private static final long serialVersionUID = 7465424124110025236L;

				@Override
				public void howDo() {

					log.debug("externalId :" + externalId);
					log.debug("parentExternalId :" + parentExternalId);
					log.debug("name :" + name);
					log.debug("displayName :" + displayName);

					if (null == applicationService.getCategoryById(category.getId())) {
						promptPanel.setMessage(getLocalizer().getString("msg.error.save.norecord", CategoryEditPanel.this, "Category Does not exist!"));
					}

					// check ExternalId
					if (!StringUtils.isEmpty(externalId) && !externalId.equals(category.getExternalId())) {
						SearchExpression searchExpression = new SearchExpressionImpl();
						searchExpression.addCondition(new CategoryExternalIdCondition(externalId, StringComparer.EQUAL, true, false));
						List<Category> list = applicationService.searchCategory(searchExpression);
						if (list != null && list.size() > 0) {
							error(getLocalizer().getString("msg.error.save.duplicatedExternalId", this, "Duplicated Category External ID!"));
							return;
						}
					}
					category.setExternalId(externalId);
					
					// check name
					if (StringUtils.isEmpty(name)) {
						name = " ";
					} else if (!name.equalsIgnoreCase(category.getName()) && null != applicationService.getCategoryByName(name)) {
						error(getLocalizer().getString("msg.error.save.duplicatedCategoryName", this, "Duplicated category name!"));
						return;
					}
					category.setName(name);
					
					// check parentExternalId
					Category parentCategory = null;
					if (!parentExternalId.equalsIgnoreCase(getLocalizer().getString("cateRoot", CategoryEditPanel.this, "root"))) {
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
					category.setParentCategory(parentCategory);
					
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
						applicationService.updateCategory(category);
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
			//save.add(Tools.addConfirmJs(getLocalizer().getString("confirmMsg", CategoryEditPanel.this)));
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
		return this.getLocalizer().getString("title", this, "Category Detail");
	}

}
