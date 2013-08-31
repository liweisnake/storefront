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
package com.hp.sdf.ngp.ui.page.configuration;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.PageParameters;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.IValidator;

import com.hp.sdf.ngp.model.Asset;
import com.hp.sdf.ngp.model.Category;
import com.hp.sdf.ngp.service.ApplicationService;
import com.hp.sdf.ngp.ui.WicketSession;

@SuppressWarnings( { "unchecked" })
public class ConfigurationCategoryPanel extends Panel {

	private static final Log log = LogFactory.getLog(ConfigurationCategoryPanel.class);

	private static final long serialVersionUID = -3341199475893674789L;

	@SpringBean
	private ApplicationService applicationService;

	private String name;

	private String description;

	private String categoryID;

	private String error;

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public String getCategoryID() {
		return categoryID;
	}

	public void setCategoryID(String categoryID) {
		this.categoryID = categoryID;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public ConfigurationCategoryPanel(String id, PageParameters parameters) {
		super(id);

		this.add(new FeedbackPanel("feedBack"));

		WicketSession.get().setObject("selectConTab", ConfigurationCategoryPanel.class.toString());
		if (parameters != null) {
			this.name = parameters.getString("categoryName");
			this.description = parameters.getString("categoryDescription");
			this.categoryID = parameters.getString("categoryID");
			this.error = parameters.getString("error");
		}

		List<Category> categorys = applicationService.getAllCategory(0, Integer.MAX_VALUE);
		ListView listView = new ListView("listCategories", categorys) {

			private static final long serialVersionUID = -2082691458083208472L;

			protected void populateItem(ListItem item) {
				final Category category = (Category) item.getModelObject();
				final Label labelName = new Label("name", category.getName());
				final Label labelDecs = new Label("description", category.getDescription());

				Link linkModify = new Link("linkModify") {

					private static final long serialVersionUID = -7522210888799865409L;

					public void onClick() {
						PageParameters parameters = new PageParameters();
						parameters.add("categoryID", "" + category.getId());
						parameters.add("categoryName", category.getName());
						parameters.add("categoryDescription", category.getDescription());
						ConfigurationPage page = new ConfigurationPage(parameters);
						setResponsePage(page);
					}

				};
				linkModify.add(new Label("modify", this.getLocalizer().getString("modify", this)));

				Link linkDelete = new Link("linkDelete") {

					private static final long serialVersionUID = -793831176087971794L;

					public void onClick() {
						List<Asset> apps = applicationService.getAllAsset(0, Integer.MAX_VALUE);
						for (Asset app : apps) {
							if (app != null) {
								List<Category> ctagList = applicationService.getAllCategoryByAssetId(app.getId(), 0, Integer.MAX_VALUE);
								for(Category ctag :ctagList){
									if (ctag != null && ctag.getId().equals(category.getId())) {
										this.error("Can not delete the category.");
										return;
									}
								}
							}
						}

						if (null == applicationService.getCategoryById(category.getId())) {
							error("This category has been delete.");
							return;
						}

						applicationService.deleteCategoryById(category.getId());
						setResponsePage(ConfigurationPage.class);
					}

				};
				linkDelete.add(new Label("delete", this.getLocalizer().getString("delete", this)));

				item.add(labelName);
				item.add(labelDecs);
				item.add(linkModify);
				item.add(linkDelete);
			}

		};

		this.add(new CategoryForm("categoryForm", this.applicationService));
		this.add(listView);

	}

	class CategoryForm extends Form {

		private static final long serialVersionUID = -6018911062435817105L;
		private ApplicationService applicationService;
		private String operation;

		public String getOperation() {
			return operation;
		}

		public void setOperation(String operation) {
			this.operation = operation;
		}

		public CategoryForm(String id, ApplicationService applicationService) {
			super(id);
			this.applicationService = applicationService;
			TextField<String> name = new TextField<String>("name", new PropertyModel<String>(ConfigurationCategoryPanel.this, "name"));
			name.setRequired(true);
			name.add(new IValidator<String>() {

				private static final long serialVersionUID = 6865233393838739096L;

				public void validate(IValidatable<String> validatable) {
					final String value = validatable.getValue();
					if (value.length() > 100) {
						error(CategoryForm.this.getLocalizer().getString("toLong", CategoryForm.this));
					}
				}
			});
			add(name);

			TextArea<String> description = new TextArea<String>("description", new PropertyModel<String>(ConfigurationCategoryPanel.this, "description"));
			add(description);
			add(new Label("error", ConfigurationCategoryPanel.this.error));

			if (ConfigurationCategoryPanel.this.categoryID == null || ConfigurationCategoryPanel.this.categoryID.equals("")) {
				this.setOperation(this.getLocalizer().getString("add", ConfigurationCategoryPanel.this));
				add(new Label("title", this.getLocalizer().getString("titleAdd", ConfigurationCategoryPanel.this)));
			} else {
				this.setOperation(getLocalizer().getString("modify", ConfigurationCategoryPanel.this));
				add(new Label("title", this.getLocalizer().getString("titleModify", ConfigurationCategoryPanel.this)));
			}
			Button submit = new Button("submit", new PropertyModel<String>(this, "operation"));
			Button cancel = new Button("cancel") {

				private static final long serialVersionUID = 4690991134636458588L;

				public void onSubmit() {
					setResponsePage(ConfigurationPage.class);
				}
			};
			cancel.setDefaultFormProcessing(false);
			add(submit);
			add(cancel);
		}

		public final void onSubmit() {

			log.debug(ConfigurationCategoryPanel.this.categoryID);
			log.debug(ConfigurationCategoryPanel.this.name);
			log.debug(ConfigurationCategoryPanel.this.description);

			Category category = new Category();
			category.setId(null == ConfigurationCategoryPanel.this.categoryID ? null : new Long(ConfigurationCategoryPanel.this.categoryID));
			category.setName(ConfigurationCategoryPanel.this.name);
			category.setDescription(ConfigurationCategoryPanel.this.description);

			// if (category.getName() == null || category.getName().trim().length() == 0) {
			// setResponsePage(ConfigurationPage.class);
			// return;
			// }

			PageParameters parameters = new PageParameters();
			parameters.add("categoryID", "" + category.getId());
			parameters.add("categoryName", category.getName());
			parameters.add("categoryDescription", category.getDescription());
			parameters.add("error", this.getLocalizer().getString("exist.name", this));
			ConfigurationPage page = new ConfigurationPage(parameters);

			try {
				if (ConfigurationCategoryPanel.this.categoryID != null) {
					Category categoryCheck = applicationService.getCategoryByName(ConfigurationCategoryPanel.this.name);
					if (categoryCheck != null && !categoryCheck.getId().equals(category.getId())) {
						error(this.getLocalizer().getString("exist.name", ConfigurationCategoryPanel.this));
						return;
					}
					this.applicationService.updateCategory(category);
				} else {
					if (null != applicationService.getCategoryByName(ConfigurationCategoryPanel.this.name)) {
						error(this.getLocalizer().getString("exist.name", ConfigurationCategoryPanel.this));
						return;
					}
					this.applicationService.saveCategory(category);
				}
				setResponsePage(ConfigurationPage.class);
			} catch (javax.persistence.EntityExistsException e) {
				setResponsePage(page);
			} catch (org.springframework.transaction.TransactionSystemException e) {
				setResponsePage(page);
			}
		}

	}
}
