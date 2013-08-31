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
import com.hp.sdf.ngp.model.Platform;
import com.hp.sdf.ngp.service.ApplicationService;
import com.hp.sdf.ngp.ui.WicketSession;
import com.hp.sdf.ngp.ui.common.Tools;

public class ConfigurationPlatformPanel extends Panel {

	private static final Log log = LogFactory.getLog(ConfigurationPlatformPanel.class);

	private static final long serialVersionUID = -3341199475893674789L;

	@SpringBean
	private ApplicationService applicationService;

	private List<Platform> platforms;

	private String name;

	private String description;

	private String platformID;

	private String error;

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public String getPlatformID() {
		return platformID;
	}

	public void setPlatformID(String platformID) {
		this.platformID = platformID;
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

	@SuppressWarnings("unchecked")
	public ConfigurationPlatformPanel(String id, PageParameters parameters) {
		super(id);

		this.add(new FeedbackPanel("feedBack"));

		WicketSession.get().setObject("selectConTab", ConfigurationPlatformPanel.class.toString());
		if (parameters != null) {
			this.name = parameters.getString("platformName");
			this.description = parameters.getString("platformDescription");
			this.platformID = parameters.getString("platformID");
			this.error = parameters.getString("error");
		}

		this.platforms = applicationService.getAllPlatform(0, Integer.MAX_VALUE);
		ListView listView = new ListView("listPlatforms", platforms) {

			private static final long serialVersionUID = -2082691458083208472L;

			@Override
			protected void populateItem(ListItem item) {
				final Platform platform = (Platform) item.getModelObject();
				final Label labelName = new Label("name", platform.getName());
				final Label labelDecs = new Label("description", platform.getDescription());

				Link linkModify = new Link("linkModify") {

					private static final long serialVersionUID = 293882401971154971L;

					public void onClick() {
						PageParameters parameters = new PageParameters();
						parameters.add("platformID", "" + platform.getId());
						parameters.add("platformName", platform.getName());
						parameters.add("platformDescription", platform.getDescription());
						ConfigurationPage page = new ConfigurationPage(parameters);
						setResponsePage(page);
					}

				};
				linkModify.add(new Label("modify", this.getLocalizer().getString("modify", this)));

				Link linkDelete = new Link("linkDelete") {

					private static final long serialVersionUID = 8557639946731724703L;

					// Save the search info into session context so that it can
					// be retrieved by other page
					public void onClick() {
						List<Asset> allBinaries = applicationService.getAllAsset(0, Integer.MAX_VALUE);

						// for (Asset asset : allBinaries) {
						// if (asset.getPlatform() != null)
						// if (asset.getPlatform().getId().equals(platform.getId())) {
						// this.error("Can not delete the platform.");
						// return;
						// }
						// }
						// }

						for (Asset asset : allBinaries) {
							List<Platform> platformList = applicationService.getPlatformByAssetId(asset.getId());
							if (Tools.isPlatformIdEqual(platformList, platform.getId())) {
								this.error("Can not delete the platform.");
								return;
							}
						}

						if (null == applicationService.getPlatformById(platform.getId())) {
							error("This platform has been delete.");
							return;
						}

						ConfigurationPlatformPanel.this.applicationService.deletePlatformById(platform.getId());
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

		this.add(new PlatformForm("platformForm", this.applicationService));
		this.add(listView);
	}

	/**
	 * PlatformForm
	 */
	class PlatformForm extends Form<Void> {

		private static final long serialVersionUID = -6018911062435817105L;
		private ApplicationService applicationService;
		private String operation;

		public String getOperation() {
			return operation;
		}

		public void setOperation(String operation) {
			this.operation = operation;
		}

		public PlatformForm(String id, ApplicationService applicationService) {
			super(id);
			this.applicationService = applicationService;
			TextField<String> name = new TextField<String>("name", new PropertyModel<String>(ConfigurationPlatformPanel.this, "name"));
			name.setRequired(true);
			name.add(new IValidator<String>() {
				private static final long serialVersionUID = -107748030336113369L;

				public void validate(IValidatable<String> validatable) {
					final String value = validatable.getValue();
					if (value.length() > 100) {
						error(PlatformForm.this.getLocalizer().getString("toLong", PlatformForm.this));
					}
				}
			});
			add(name);

			TextArea<String> description = new TextArea<String>("description", new PropertyModel<String>(ConfigurationPlatformPanel.this, "description"));
			add(description);
			add(new Label("error", ConfigurationPlatformPanel.this.error));

			if (ConfigurationPlatformPanel.this.platformID == null || ConfigurationPlatformPanel.this.platformID.equals("")) {
				this.setOperation(this.getLocalizer().getString("add", ConfigurationPlatformPanel.this));
				add(new Label("title", this.getLocalizer().getString("titleAdd", ConfigurationPlatformPanel.this)));
			} else {
				this.setOperation(getLocalizer().getString("modify", ConfigurationPlatformPanel.this));
				add(new Label("title", this.getLocalizer().getString("titleModify", ConfigurationPlatformPanel.this)));
			}
			Button submit = new Button("submit", new PropertyModel<String>(this, "operation"));
			Button cancel = new Button("cancel") {

				private static final long serialVersionUID = -9063463950115992197L;

				public void onSubmit() {
					setResponsePage(ConfigurationPage.class);
				}
			};
			cancel.setDefaultFormProcessing(false);
			add(submit);
			add(cancel);
		}

		public final void onSubmit() {
			log.debug(ConfigurationPlatformPanel.this.platformID);
			log.debug(ConfigurationPlatformPanel.this.name);
			log.debug(ConfigurationPlatformPanel.this.description);

			Platform platform = new Platform();
			platform.setId(null == ConfigurationPlatformPanel.this.platformID ? null : new Long(ConfigurationPlatformPanel.this.platformID));
			platform.setName(ConfigurationPlatformPanel.this.name);
			platform.setDescription(ConfigurationPlatformPanel.this.description);

			// if (platform.getName() == null || platform.getName().trim().length() == 0) {
			// setResponsePage(ConfigurationPage.class);
			// return;
			// }

			PageParameters parameters = new PageParameters();
			parameters.add("platformID", "" + platform.getId());
			parameters.add("platformName", platform.getName());
			parameters.add("platformDescription", platform.getDescription());
			parameters.add("error", this.getLocalizer().getString("exist.name", this));
			ConfigurationPage page = new ConfigurationPage(parameters);

			try {
				if (ConfigurationPlatformPanel.this.platformID != null) {
					Platform platformCheck = applicationService.getPlatformByName(ConfigurationPlatformPanel.this.name);
					if (platformCheck != null && !platformCheck.getId().equals(platform.getId())) {
						error(this.getLocalizer().getString("exist.name", ConfigurationPlatformPanel.this));
						return;
					}
					this.applicationService.updatePlatform(platform);
				} else {
					if (null != applicationService.getPlatformByName(ConfigurationPlatformPanel.this.name)) {
						error(this.getLocalizer().getString("exist.name", ConfigurationPlatformPanel.this));
						return;
					}
					this.applicationService.savePlatform(platform);
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
