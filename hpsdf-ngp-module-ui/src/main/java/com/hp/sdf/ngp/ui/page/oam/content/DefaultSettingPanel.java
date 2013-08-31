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
package com.hp.sdf.ngp.ui.page.oam.content;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.Component;
import org.apache.wicket.authorization.strategies.role.metadata.MetaDataRoleAuthorizationStrategy;
import org.apache.wicket.extensions.breadcrumb.IBreadCrumbModel;
import org.apache.wicket.extensions.breadcrumb.panel.BreadCrumbPanel;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.hp.sdf.ngp.common.annotation.Value;
import com.hp.sdf.ngp.model.Provider;
import com.hp.sdf.ngp.model.SystemConfig;
import com.hp.sdf.ngp.service.ApplicationService;
import com.hp.sdf.ngp.ui.common.CheckPanel;
import com.hp.sdf.ngp.ui.common.Constant;
import com.hp.sdf.ngp.ui.common.CustomizePagingNavigator;
import com.hp.sdf.ngp.ui.common.DefaultSettingCondition;
import com.hp.sdf.ngp.ui.common.PromptPanel;
import com.hp.sdf.ngp.ui.common.Tools;
import com.hp.sdf.ngp.ui.common.Constant.SYSTEM_CONFIG;
import com.hp.sdf.ngp.ui.provider.DefaultSettingDataProvider;
import com.hp.sdf.ngp.ui.provider.SystemConfigDataProvider;
import com.hp.sdf.ngp.workflow.Privilege;

public class DefaultSettingPanel extends BreadCrumbPanel {

	private static final long serialVersionUID = -8524244192134679105L;

	private static Log log = LogFactory.getLog(DefaultSettingPanel.class);

	private int itemsPerPage = Constant.DEFALUT_PER_PAGE_COUNT;

	private BreadCrumbPanel caller;

	@SpringBean
	ApplicationService applicationService;

	private DefaultSettingDataView dataView;
	private DefaultSettingUpdateDataView updateDataView;

	private Map<Long, String> rateMap = new HashMap<Long, String>();
	private Map<Long, Double> rateUpdateMap = new HashMap<Long, Double>();

	private PromptPanel promptPanel;

	public int getItemsPerPage() {
		return itemsPerPage;
	}

	@Value("application.itemsperpage")
	public void setItemsPerPage(int itemsPerPage) {
		this.itemsPerPage = itemsPerPage;
	}

	public ApplicationService getApplicationService() {
		return applicationService;
	}

	public void setApplicationService(ApplicationService applicationService) {
		this.applicationService = applicationService;
	}

	public DefaultSettingPanel(String id, IBreadCrumbModel breadCrumbModel, final BreadCrumbPanel caller) {
		super(id, breadCrumbModel);

		this.caller = caller;
		breadCrumbModel.setActive(this);

		add(new FeedbackPanel("feedback"));
		add(new DefaultSettingUpdateForm("update.form"));
		add(new DefaultSettingForm("form"));

		DefaultSettingSearchPanel searchPanel = new DefaultSettingSearchPanel("searchPanel", updateDataView, applicationService);
		add(searchPanel);
		MetaDataRoleAuthorizationStrategy.authorize(searchPanel, Component.RENDER, Privilege.DEFAULTRATE);

		promptPanel = new PromptPanel("promptPanel", getLocalizer().getString("title.prompt", this, "Prompt"), getLocalizer().getString("msg.warn.update", this, "Do you want to edit the default setting?"), null, StringUtils.EMPTY);
		add(promptPanel);
	}

	// right
	public class DefaultSettingUpdateForm extends Form<Void> {

		private static final long serialVersionUID = 6364024991342867280L;

		private DefaultSettingDataProvider dataProvider;

		public DefaultSettingUpdateForm(String id) {
			super(id);

			dataProvider = new DefaultSettingDataProvider(applicationService, new DefaultSettingCondition());

			updateDataView = new DefaultSettingUpdateDataView("update.provider", dataProvider, itemsPerPage);
			add(updateDataView);

			add(new CustomizePagingNavigator("update.navigator", updateDataView));

			// back button
			Button backBtn = new Button("update.back") {

				private static final long serialVersionUID = 8116120281966867260L;

				@Override
				public void onSubmit() {
					caller.activate(caller);
				}
			};
			backBtn.setDefaultFormProcessing(false);
			add(backBtn);

			final CheckPanel checkPanel = new CheckPanel("update.savePanel", getLocalizer().getString("title.prompt", DefaultSettingPanel.this), getLocalizer().getString("msg.warn.update.provider", DefaultSettingPanel.this)) {

				private static final long serialVersionUID = 7465424124110025236L;

				@Override
				public void howDo() {
					if (rateUpdateMap != null && rateUpdateMap.size() > 0) {
						for (Iterator<Long> iterator = rateUpdateMap.keySet().iterator(); iterator.hasNext();) {
							Long key = iterator.next();
							Double value = rateUpdateMap.get(key);
							if (null == value) {
								error(getLocalizer().getString("msg.error.empty.commission.rate", this));
								return;
							} else {
								if (value < 0 || value > 100) {
									error(getLocalizer().getString("msg.error.range.commission.rate", this));
									return;
								}
							}
						}
					}

					if (rateUpdateMap != null && rateUpdateMap.size() > 0) {
						for (Iterator<Long> iterator = rateUpdateMap.keySet().iterator(); iterator.hasNext();) {
							Long key = iterator.next();
							Double value = rateUpdateMap.get(key);
							Provider provider = applicationService.getAssetProviderById(key);
							provider.setCommissionRate(Tools.getScale(value / 100));
							applicationService.updateAssetProvider(provider);
						}

						promptPanel.setMessage(getLocalizer().getString("msg.result.update.provider.success", this, "You successfully batch update the rate of the providers!"));
						promptPanel.show();
					}

				}
			};
			add(checkPanel);

			Button saveBtn = new Button("update.save") {
				private static final long serialVersionUID = -2907984789414948492L;

				@Override
				public void onSubmit() {
					checkPanel.show();
				}
			};

			add(saveBtn);
			MetaDataRoleAuthorizationStrategy.authorize(saveBtn, Component.RENDER, Privilege.DEFAULTRATE);
		}

		@Override
		protected void onSubmit() {
		}
	}

	class DefaultSettingUpdateDataView extends DataView<Provider> {

		private static final long serialVersionUID = -6514262177740324330L;

		protected DefaultSettingUpdateDataView(String id, IDataProvider<Provider> dataProvider, int itemsPerPage) {
			super(id, dataProvider, itemsPerPage);
		}

		@Override
		protected void populateItem(Item<Provider> item) {
			final Provider provider = item.getModelObject();

			// provider id
			Label pidField = new Label("update.pid", String.valueOf(provider.getExternalId()));
			item.add(pidField);

			Label rateSymbolField = new Label("rateSymbolUpdate", getLocalizer().getString("rateSymbol", this));
			item.add(rateSymbolField);

			// provider name
			Label providerField = new Label("update.provider", provider.getName());
			item.add(providerField);

			// rate
			TextField<String> rateField = new TextField<String>("update.rate", new IModel<String>() {
				private static final long serialVersionUID = 6297714565920521727L;

				public String getObject() {
					Double rate = rateUpdateMap.get(provider.getId());
					return null == rate ? StringUtils.EMPTY : String.valueOf(rate);
				}

				public void setObject(String value) {
					Double d = null;
					if (value != null && StringUtils.isNotEmpty(value)) {
						try {
							d = Double.parseDouble(value);
						} catch (Exception ex) {
							log.error(ex.getMessage());
						}

					}
					rateUpdateMap.put(provider.getId(), d);
				}

				public void detach() {
				}
			});
			item.add(rateField);
			// set control value
			rateUpdateMap.put(provider.getId(), provider.getCommissionRate());
		}

		public void updateModel(DefaultSettingCondition condition) {
			DefaultSettingDataProvider dataProvider = (DefaultSettingDataProvider) this.getDataProvider();
			dataProvider.setCondition(condition);
		}
	}

	// left
	public class DefaultSettingForm extends Form<Void> {

		private static final long serialVersionUID = 6364024991342867280L;

		private SystemConfigDataProvider dataProvider;

		private SystemConfig systemConfig;

		public DefaultSettingForm(String id) {
			super(id);

			dataProvider = new SystemConfigDataProvider(applicationService);

			dataView = new DefaultSettingDataView("provider", dataProvider, itemsPerPage);
			add(dataView);

			systemConfig = applicationService.getSystemConfigByKey(SYSTEM_CONFIG.newPromotionDueDate.toString());
			if (null == systemConfig) {
				systemConfig = new SystemConfig();
				systemConfig.setConfigKey(SYSTEM_CONFIG.newPromotionDueDate.toString());
			}

			TextField<String> daysField = new TextField<String>("days", new PropertyModel<String>(systemConfig, "value"));
			add(daysField);

			add(new CustomizePagingNavigator("navigator", dataView));

			final CheckPanel checkPanel1 = new CheckPanel("savePanel", getLocalizer().getString("title.prompt", DefaultSettingPanel.this), getLocalizer().getString("msg.warn.update", DefaultSettingPanel.this)) {

				private static final long serialVersionUID = 7465424124110025236L;

				@Override
				public void howDo() {
					if (rateMap != null && rateMap.size() > 0) {
						for (Iterator<Long> iterator = rateMap.keySet().iterator(); iterator.hasNext();) {
							Long key = iterator.next();
							String value = rateMap.get(key);
							if (null == value) {
								error(getLocalizer().getString("msg.error.empty.default.rate", this));
								return;
							} else {
								Double d;
								try {
									d = Double.parseDouble(value);
								} catch (Exception exception) {
									error(getLocalizer().getString("msg.error.error.default.rate", this));
									return;
								}

								if (d < 0 || d > 100) {
									error(getLocalizer().getString("msg.error.range.default.rate", this));
									return;
								}
							}
						}
					}

					if (rateMap != null && rateMap.size() > 0) {
						for (Iterator<Long> iterator = rateMap.keySet().iterator(); iterator.hasNext();) {
							Long id = iterator.next();
							String value = rateMap.get(id);
							Double d = Double.parseDouble(value);
							value = String.valueOf(Tools.getScale(d / 100));
							SystemConfig config = applicationService.getSystemConfigById(id);
							config.setValue(value);
							applicationService.saveOrUpdateSystemConfig(config);
						}

						promptPanel.setMessage(getLocalizer().getString("msg.result.update.success", this, "You successfully edit the default setting!"));
						promptPanel.show();
					}

				}
			};
			add(checkPanel1);

			Button saveButton = new Button("saveButton") {
				private static final long serialVersionUID = -429956203675572222L;

				@Override
				public void onSubmit() {
					checkPanel1.show();
				}
			};

			add(saveButton);
			MetaDataRoleAuthorizationStrategy.authorize(saveButton, Component.RENDER, Privilege.DEFAULTSETTING);

			final CheckPanel checkPanel2 = new CheckPanel("saveDaysPanel", getLocalizer().getString("title.prompt", DefaultSettingPanel.this), getLocalizer().getString("msg.warn.update.days", DefaultSettingPanel.this)) {

				private static final long serialVersionUID = 7465424124110025236L;

				@Override
				public void howDo() {
					try {
						if (0 > Integer.parseInt(systemConfig.getValue())) {
							error(getLocalizer().getString("msg.error.update.days", this, "value invalid!"));
							return;
						}

						applicationService.saveOrUpdateSystemConfig(systemConfig);
						promptPanel.setMessage(getLocalizer().getString("msg.result.update.days.success", this, "You successfully edit the default setting!"));
						promptPanel.show();
					} catch (Exception exception) {
						error(getLocalizer().getString("msg.error.update.days", this, "value invalid!"));
					}
				}
			};
			add(checkPanel2);

			Button saveDaysButton = new Button("saveDays") {

				private static final long serialVersionUID = -8697329369616317547L;

				@Override
				public void onSubmit() {
					checkPanel2.show();
				}
			};

			add(saveDaysButton);
			MetaDataRoleAuthorizationStrategy.authorize(saveDaysButton, Component.RENDER, Privilege.NEWPROMOTIONDUEDATE);
		}

		@Override
		protected void onSubmit() {
		}
	}

	class DefaultSettingDataView extends DataView<SystemConfig> {

		private static final long serialVersionUID = -6514262177740324330L;

		protected DefaultSettingDataView(String id, IDataProvider<SystemConfig> dataProvider, int itemsPerPage) {
			super(id, dataProvider, itemsPerPage);
		}

		@Override
		protected void populateItem(Item<SystemConfig> item) {
			final SystemConfig systemConfig = item.getModelObject();

			// content
			Label contentField = new Label("content", systemConfig.getConfigKey());
			item.add(contentField);

			Label rateSymbolField = new Label("rateSymbol", getLocalizer().getString("rateSymbol", this));
			item.add(rateSymbolField);

			// rate
			TextField<String> valueField = new TextField<String>("rate", new IModel<String>() {
				private static final long serialVersionUID = 6297714565920521727L;

				public String getObject() {
					String value = rateMap.get(systemConfig.getId());
					return value == null ? StringUtils.EMPTY : value;
				}

				public void setObject(String value) {
					// if (value != null && StringUtils.isNotEmpty(value)) {
					rateMap.put(systemConfig.getId(), value);
					// }
				}

				public void detach() {
				}
			});
			item.add(valueField);
			// set control value
			rateMap.put(systemConfig.getId(), systemConfig.getValue());
		}
	}

	public String getTitle() {
		return this.getLocalizer().getString("title", this, Constant.DEFAULT_SETTING);
	}
}