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

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

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
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.hp.sdf.ngp.model.Category;
import com.hp.sdf.ngp.service.ApplicationService;
import com.hp.sdf.ngp.ui.common.CheckPanel;
import com.hp.sdf.ngp.ui.common.CustomizePagingNavigator;
import com.hp.sdf.ngp.ui.provider.CategoryDataProvider;
import com.hp.sdf.ngp.workflow.Privilege;

public class CategoryListPanel extends BreadCrumbPanel {

	private static final long serialVersionUID = 2847203327312187476L;

	private static final Log log = LogFactory.getLog(CategoryListPanel.class);

	public CategoryListPanel(String id, final IBreadCrumbModel breadCrumbModel) {
		super(id, breadCrumbModel);

		CategoryListForm categoryListForm = new CategoryListForm("categoryListForm", breadCrumbModel);
		MetaDataRoleAuthorizationStrategy.authorize(categoryListForm, Component.RENDER, Privilege.VIEWCATEGORYLIST);
		this.add(categoryListForm);
	}

	public final class CategoryListForm extends Form<Void> {

		private static final long serialVersionUID = -4951597258253103907L;

		private Map<Long, String> categoryMap = new HashMap<Long, String>();

		@SpringBean
		private ApplicationService applicationService;

		private int itemsPerPage = 50;

		private boolean groupSelected;

		public boolean isGroupSelected() {
			return groupSelected;
		}

		public void setGroupSelected(boolean groupSelected) {
			this.groupSelected = groupSelected;
		}

		public CategoryListForm(String id, IBreadCrumbModel breadCrumbModel) {
			super(id);

			this.add(new FeedbackPanel("feedBack"));
			
			CheckBox groupselector = new CheckBox("groupselector", new PropertyModel<Boolean>(this, "groupSelected"));
			groupselector.setMarkupId("groupselector");
			add(groupselector);

			CategoryDataProvider categoryDataProvider = new CategoryDataProvider(applicationService);
			log.debug("categoryDataProvider.size() :" + categoryDataProvider.size());

			CategoryDataView listView = new CategoryDataView("listview", breadCrumbModel, categoryDataProvider, itemsPerPage);
			add(listView);
			add(new CustomizePagingNavigator("navigator", listView));

			Button newEntry = new Button("newEntry") {
				private static final long serialVersionUID = 8179675420318996436L;

				public void onSubmit() {

					activate(new IBreadCrumbPanelFactory() {
						private static final long serialVersionUID = 3840521373650730173L;

						public BreadCrumbPanel create(String componentId, IBreadCrumbModel breadCrumbModel) {
							return new NewCategoryPanel(componentId, breadCrumbModel);
						}
					});
				}
			};
			MetaDataRoleAuthorizationStrategy.authorize(newEntry, Component.RENDER, Privilege.NEWCATEGORY);
			add(newEntry);
			
			
			final CheckPanel checkPanel = new CheckPanel("checkPanel", getLocalizer().getString("promptTitle", CategoryListPanel.this), getLocalizer().getString("confirmMsg", CategoryListPanel.this)) {
				private static final long serialVersionUID = 7465424124110025236L;
				@Override
				public void howDo() {

					if (categoryMap != null && categoryMap.size() > 0) {
						for (Entry<Long, String> entry : categoryMap.entrySet()) {
							if (null == applicationService.getCategoryById(entry.getKey())) {
								log.error("Can not delete the category because this category is not exist.");
								 error(getLocalizer().getString("msg.error.save.nocategory",  CategoryListPanel.this, "Category does not exist!"));

								log.debug("Set SelectAll checkbox to not checked.");
								setGroupSelected(false);
								categoryMap.clear();
								return;
							}
						}

						for (Entry<Long, String> entry : categoryMap.entrySet()) {
							log.debug("The Category need to be deleted: " + entry.getValue());
							applicationService.deleteCategoryById(entry.getKey());
						}

					} else {
						error(getLocalizer().getString("msg.error.del.noselect",  CategoryListPanel.this, "Please select one Category at least!"));
						return;
					}

					log.debug("Set SelectAll checkbox to not checked.");
					setGroupSelected(false);
					log.debug("Clear the subscribeMap.");
					categoryMap.clear();
				}
			};
			add(checkPanel);
			
			
			Button delete = new Button("delete") {

				private static final long serialVersionUID = 2189543498800077396L;

				public void onSubmit() {
					checkPanel.show();
				}
			};
//			delete.add(Tools.addConfirmJs(getLocalizer().getString("confirmMsg", CategoryListPanel.this)));
			MetaDataRoleAuthorizationStrategy.authorize(delete, Component.RENDER, Privilege.DELETECATEGORY);
			add(delete);
		}

		class CategoryDataView extends DataView<Category> {

			private static final long serialVersionUID = -6514262177740324330L;

			private IBreadCrumbModel breadCrumbModel;

			protected CategoryDataView(String id, IBreadCrumbModel breadCrumbModel, IDataProvider<Category> dataProvider, int itemsPerPage) {
				super(id, dataProvider, itemsPerPage);
				this.breadCrumbModel = breadCrumbModel;
			}

			protected void populateItem(Item<Category> item) {
				final Category category = (Category) item.getModelObject();

				item.add(new CheckBox("select", new IModel<Boolean>() {

					private static final long serialVersionUID = 7865238665776348241L;

					public Boolean getObject() {

						if (categoryMap != null && categoryMap.size() != 0) {
							String o = categoryMap.get(category.getId());
							return null == o ? false : true;
						}

						return false;
					}

					public void setObject(Boolean object) {
						if (object) {
							categoryMap.put(category.getId(), category.getName());
						}
					}

					public void detach() {
					}
				}));

				Label externalIdLabel = new Label("externalId", category.getExternalId());
				item.add(externalIdLabel);

				String parentExternalId = "";
				if (category.getParentCategory() != null) {
					Category parentCategory = applicationService.getCategoryById(category.getParentCategory().getId());
					if (parentCategory != null) {
						parentExternalId = parentCategory.getExternalId();
					}
				}
				log.debug("parentExternalId :" + parentExternalId);
				Label parentExternalIdLabel = new Label("parentExternalId", (parentExternalId!=null && !parentExternalId.equalsIgnoreCase(""))?parentExternalId:getLocalizer().getString("cateRoot", CategoryListPanel.this, "root"));
				item.add(parentExternalIdLabel);

				Label nameLabel = new Label("name", category.getName());
				item.add(nameLabel);

				Label displayNameLabel = new Label("displayName", category.getDisplayName());
				item.add(displayNameLabel);

				BreadCrumbPanelLink edit = new BreadCrumbPanelLink("detail", breadCrumbModel, new IBreadCrumbPanelFactory() {

					private static final long serialVersionUID = 1L;

					public BreadCrumbPanel create(String componentId, IBreadCrumbModel breadCrumbModel) {
						return new CategoryEditPanel(componentId, breadCrumbModel, category);
					}
				});
				MetaDataRoleAuthorizationStrategy.authorize(edit, Component.RENDER, Privilege.EDITCATEGORY);
				item.add(edit);

			}
		}

	}

	public String getTitle() {
		return this.getLocalizer().getString("title", this, "Category List");
	}

}
