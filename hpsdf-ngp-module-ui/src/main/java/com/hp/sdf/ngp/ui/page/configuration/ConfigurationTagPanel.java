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

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.PageParameters;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.navigation.paging.PagingNavigator;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.IValidator;

import com.hp.sdf.ngp.model.Asset;
import com.hp.sdf.ngp.model.Tag;
import com.hp.sdf.ngp.service.ApplicationService;
import com.hp.sdf.ngp.ui.WicketApplication;
import com.hp.sdf.ngp.ui.WicketSession;
import com.hp.sdf.ngp.ui.provider.TagDataProvider;

public class ConfigurationTagPanel extends Panel {

	private static final Log log = LogFactory.getLog(ConfigurationTagPanel.class);

	private static final long serialVersionUID = -3341199475893674789L;

	@SpringBean
	private ApplicationService applicationService;

	@SpringBean(name = "wicketApplication")
	private WicketApplication wicketApplication;

	private String name;

	private String description;

	private String tagID;

	private String error;

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	private int itemsPerPage;

	public int getItemsPerPage() {
		return itemsPerPage;
	}

	public void setItemsPerPage(int itemsPerPage) {
		this.itemsPerPage = itemsPerPage;
	}

	public String getTagID() {
		return tagID;
	}

	public void setTagID(String tagID) {
		this.tagID = tagID;
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

	public ConfigurationTagPanel(String id, PageParameters parameters) {
		super(id);

		this.add(new FeedbackPanel("feedBack"));

		this.setItemsPerPage(wicketApplication.getItemsPerPage());
		WicketSession.get().setObject("selectConTab", ConfigurationTagPanel.class.toString());
		if (parameters != null) {
			this.name = parameters.getString("tagName");
			this.description = parameters.getString("tagDescription");
			this.tagID = parameters.getString("tagID");
			this.error = parameters.getString("error");
		}
		TagDataProvider dataProvider = new TagDataProvider(this.applicationService);
		TagDataView dataView = new TagDataView("listTags", dataProvider, this.applicationService, itemsPerPage);

		this.add(new TagForm("tagForm", this.applicationService));
		this.add(dataView);
		this.add(new PagingNavigator("navigator", dataView));
	}

	/**
	 * TagDataView
	 */
	class TagDataView extends DataView<Tag> {

		private static final long serialVersionUID = 757869080803569521L;
		private ApplicationService applicationService;

		protected TagDataView(String id, IDataProvider<Tag> dataProvider, ApplicationService applicationService, int itemsPerPage) {
			super(id, dataProvider, itemsPerPage);
			this.applicationService = applicationService;
		}

		@SuppressWarnings("unchecked")
		protected void populateItem(Item<Tag> item) {
			final Tag tag = (Tag) item.getModelObject();
			final Label labelName = new Label("name", tag.getName());
			final Label labelDecs = new Label("description", tag.getDescription());

			Link linkModify = new Link("linkModify") {

				private static final long serialVersionUID = 1346707466545001573L;

				public void onClick() {
					PageParameters parameters = new PageParameters();
					parameters.add("tagID", "" + tag.getId());
					parameters.add("tagName", tag.getName());
					parameters.add("tagDescription", tag.getDescription());
					ConfigurationPage page = new ConfigurationPage(parameters);
					setResponsePage(page);
				}

			};
			linkModify.add(new Label("modify", this.getLocalizer().getString("modify", this)));

			Link linkDelete = new Link("linkDelete") {

				private static final long serialVersionUID = -2995073222396513992L;

				public void onClick() {

					List<Asset> apps = applicationService.getAllAsset(0, Integer.MAX_VALUE);
					for (Asset app : apps) {
						if (app != null) {

							List<Tag> tagList = applicationService.getAllTagsByAsset(app.getId(), null,0, Integer.MAX_VALUE);
							for (Tag assetTag : tagList) {
								if (assetTag != null && assetTag.getId().equals(tag.getId())) {
									this.error("Can not delete the Tag.");
									return;
								}
							}
						}
					}

					if (null == applicationService.getTagById(tag.getId())) {
						error("This tag has been delete.");
						return;
					}

					applicationService.deleteTag(tag.getId());
					setResponsePage(ConfigurationPage.class);
				}

			};
			linkDelete.add(new Label("delete", this.getLocalizer().getString("delete", this)));
			item.add(labelName);
			item.add(labelDecs);
			item.add(linkModify);
			item.add(linkDelete);

		}

	}

	/**
	 * tagForm
	 */
	class TagForm extends Form<Void> {

		private static final long serialVersionUID = -6018911062435817105L;
		private ApplicationService applicationService;
		private String operation;

		public String getOperation() {
			return operation;
		}

		public void setOperation(String operation) {
			this.operation = operation;
		}

		public TagForm(String id, ApplicationService applicationService) {
			super(id);

			this.applicationService = applicationService;
			TextField<String> name = new TextField<String>("name", new PropertyModel<String>(ConfigurationTagPanel.this, "name"));
			name.setRequired(true);
			name.add(new IValidator<String>() {
				private static final long serialVersionUID = 2831599311885532978L;

				public void validate(IValidatable<String> validatable) {
					final String value = validatable.getValue();
					if (value.length() > 100) {
						error(TagForm.this.getLocalizer().getString("toLong", TagForm.this));
					}
					if (StringUtils.contains(value, ",")) {
						error(TagForm.this.getLocalizer().getString("invalidChar", TagForm.this));
					}
				}
			});
			add(name);

			TextArea<String> description = new TextArea<String>("description", new PropertyModel<String>(ConfigurationTagPanel.this, "description"));
			add(description);
			add(new Label("error", ConfigurationTagPanel.this.error));

			if (ConfigurationTagPanel.this.tagID == null || ConfigurationTagPanel.this.tagID.equals("")) {
				this.setOperation(this.getLocalizer().getString("add", ConfigurationTagPanel.this));
				add(new Label("title", this.getLocalizer().getString("titleAdd", ConfigurationTagPanel.this)));
			} else {
				this.setOperation(getLocalizer().getString("modify", ConfigurationTagPanel.this));
				add(new Label("title", this.getLocalizer().getString("titleModify", ConfigurationTagPanel.this)));
			}
			Button submit = new Button("submit", new PropertyModel<String>(this, "operation"));
			Button cancel = new Button("cancel") {

				private static final long serialVersionUID = 1135263281799858626L;

				public void onSubmit() {
					setResponsePage(ConfigurationPage.class);
				}
			};
			cancel.setDefaultFormProcessing(false);
			add(submit);
			add(cancel);
		}

		public final void onSubmit() {
			log.debug(ConfigurationTagPanel.this.tagID);
			log.debug(ConfigurationTagPanel.this.name);
			log.debug(ConfigurationTagPanel.this.description);

			Tag tag = new Tag();
			tag.setId(null == ConfigurationTagPanel.this.tagID ? null : new Long(ConfigurationTagPanel.this.tagID));
			tag.setName(ConfigurationTagPanel.this.name);
			tag.setDescription(ConfigurationTagPanel.this.description);

			// if (tag.getName() == null || tag.getName().trim().length() == 0)
			// {
			// setResponsePage(ConfigurationPage.class);
			// return;
			// }

			PageParameters parameters = new PageParameters();
			parameters.add("tagID", "" + tag.getId());
			parameters.add("tagName", tag.getName());
			parameters.add("tagDescription", tag.getDescription());
			parameters.add("error", this.getLocalizer().getString("exist.name", this));
			ConfigurationPage page = new ConfigurationPage(parameters);
			try {
				if (ConfigurationTagPanel.this.tagID != null) {
					Tag tagCheck = applicationService.getTagByName(ConfigurationTagPanel.this.name);
					if (tagCheck != null && !tagCheck.getId().equals(tag.getId())) {
						error(this.getLocalizer().getString("exist.name", ConfigurationTagPanel.this));
						return;
					}
					this.applicationService.updateTag(tag);
				} else {
					if (null != applicationService.getTagByName(ConfigurationTagPanel.this.name)) {
						error(this.getLocalizer().getString("exist.name", ConfigurationTagPanel.this));
						return;
					}
					this.applicationService.saveTag(tag);
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
