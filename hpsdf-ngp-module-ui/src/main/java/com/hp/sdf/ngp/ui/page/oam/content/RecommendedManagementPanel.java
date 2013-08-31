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

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.Component;
import org.apache.wicket.authorization.strategies.role.metadata.MetaDataRoleAuthorizationStrategy;
import org.apache.wicket.extensions.breadcrumb.IBreadCrumbModel;
import org.apache.wicket.extensions.breadcrumb.panel.BreadCrumbPanel;
import org.apache.wicket.extensions.breadcrumb.panel.BreadCrumbPanelLink;
import org.apache.wicket.extensions.breadcrumb.panel.IBreadCrumbPanelFactory;
import org.apache.wicket.feedback.ContainerFeedbackMessageFilter;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.hp.sdf.ngp.api.comon.EntityType;
import com.hp.sdf.ngp.common.annotation.Value;
import com.hp.sdf.ngp.eav.model.AttributeValue;
import com.hp.sdf.ngp.model.Asset;
import com.hp.sdf.ngp.model.AssetBinaryVersion;
import com.hp.sdf.ngp.model.Provider;
import com.hp.sdf.ngp.model.Status;
import com.hp.sdf.ngp.service.ApplicationService;
import com.hp.sdf.ngp.ui.WicketSession;
import com.hp.sdf.ngp.ui.common.CheckPanel;
import com.hp.sdf.ngp.ui.common.Constant;
import com.hp.sdf.ngp.ui.common.ContentManagementCondition;
import com.hp.sdf.ngp.ui.common.CustomizePagingNavigator;
import com.hp.sdf.ngp.ui.common.GenerateControls;
import com.hp.sdf.ngp.ui.common.PromptPanel;
import com.hp.sdf.ngp.ui.common.Tools;
import com.hp.sdf.ngp.ui.provider.RecommendContentManagementDataProvider;
import com.hp.sdf.ngp.workflow.Privilege;

public class RecommendedManagementPanel extends BreadCrumbPanel {

	private static final long serialVersionUID = -8524244192134679105L;

	private static Log log = LogFactory.getLog(RecommendedManagementPanel.class);

	@Value("application.itemsperpage")
	private int itemsPerPage = Constant.DEFALUT_PER_PAGE_COUNT;

	@Value("delete.status")
	private String deleteStatusConfig;

	@SpringBean
	ApplicationService applicationService;

	private PromptPanel promptPanel;

	private RecommendedManagementDataView dataView;

	private Map<Long, AssetBinaryVersion> selectMap = new HashMap<Long, AssetBinaryVersion>();
	private Map<Long, Long> recommendOrderMap = new HashMap<Long, Long>();

	public int getItemsPerPage() {
		return itemsPerPage;
	}

	public void setItemsPerPage(int itemsPerPage) {
		this.itemsPerPage = itemsPerPage;
	}

	public ApplicationService getApplicationService() {
		return applicationService;
	}

	public void setApplicationService(ApplicationService applicationService) {
		this.applicationService = applicationService;
	}

	public RecommendedManagementPanel(String id, IBreadCrumbModel breadCrumbModel) {
		super(id, breadCrumbModel);

		// default setting link
		BreadCrumbPanelLink defaultSettingLink = new BreadCrumbPanelLink("defaultSetting", RecommendedManagementPanel.this.getBreadCrumbModel(), new IBreadCrumbPanelFactory() {

			private static final long serialVersionUID = -5113356719217533015L;

			public BreadCrumbPanel create(String componentId, IBreadCrumbModel breadCrumbModel) {
				return new DefaultSettingPanel(componentId, breadCrumbModel, RecommendedManagementPanel.this);
			}
		});
		// add(defaultSettingLink);
		MetaDataRoleAuthorizationStrategy.authorize(defaultSettingLink, Component.RENDER, Privilege.DEFAULTSETTING);

		// new content
		BreadCrumbPanelLink newBinaryVersionLink = new BreadCrumbPanelLink("newBinaryVersion", RecommendedManagementPanel.this.getBreadCrumbModel(), new IBreadCrumbPanelFactory() {

			private static final long serialVersionUID = -7078020538448932972L;

			public BreadCrumbPanel create(String componentId, IBreadCrumbModel breadCrumbModel) {
				return new ContentManagementNewPanel(componentId, breadCrumbModel, RecommendedManagementPanel.this);
			}
		});
		// add(newBinaryVersionLink);
		MetaDataRoleAuthorizationStrategy.authorize(newBinaryVersionLink, Component.RENDER, Privilege.NEWCONTENT);

		add(new RecommendedManagementUpdateForm("updateForm"));

		RecommendedManagementSearchPanel searchPanel = new RecommendedManagementSearchPanel("searchPanel", dataView, applicationService);
		add(searchPanel);
		MetaDataRoleAuthorizationStrategy.authorize(searchPanel, Component.RENDER, Privilege.RECOMMENDSEARCH);

		promptPanel = new PromptPanel("promptPanel", getLocalizer().getString("title.prompt", this, "Prompt"), getLocalizer().getString("msg.result.delete.success", this, "You successfully delete the asset binary version!"), null, StringUtils.EMPTY);
		add(promptPanel);
	}

	public class RecommendedManagementUpdateForm extends Form<Void> {

		private static final long serialVersionUID = 6364024991342867280L;

		private Boolean selectAll = false;

		public Boolean getSelectAll() {
			return selectAll;
		}

		public void setSelectAll(Boolean selectAll) {
			this.selectAll = selectAll;
		}

		public RecommendedManagementUpdateForm(String id) {
			super(id);

			ContentManagementCondition condition = new ContentManagementCondition();
			if (!WicketSession.get().getRoles().hasRole(Privilege.RECOMMENDSEARCH)) {
				condition.setFirst(false);
			}
			RecommendContentManagementDataProvider dataProvider = new RecommendContentManagementDataProvider(applicationService, condition);

			final FeedbackPanel feedback = new FeedbackPanel("updateFeedback", new ContainerFeedbackMessageFilter(this));
			add(feedback);

			dataView = new RecommendedManagementDataView("binaryVersions", dataProvider, itemsPerPage);
			add(dataView);

			CheckBox selectAllCheckBox = new CheckBox("selectAll", new PropertyModel<Boolean>(this, "selectAll"));
			selectAllCheckBox.setMarkupId("selectAll");
			add(selectAllCheckBox);

			add(new CustomizePagingNavigator("navigator", dataView));

			// add delete button
			// Button deleteBtn = new Button("delete") {
			//
			// private static final long serialVersionUID =
			// 8116120281966867260L;
			//
			// @Override
			// public void onSubmit() {
			// super.onSubmit();
			// log.debug("delete binary version");
			// if (selectMap != null && selectMap.size() > 0) {
			// for (Iterator<Long> iterator = selectMap.keySet().iterator();
			// iterator.hasNext();) {
			// Long key = iterator.next();
			// applicationService.deleteAssetBinary(key);
			// }
			// }
			// clearControlValue();
			// }
			// };
			// deleteBtn.setDefaultFormProcessing(true);
			// add(deleteBtn);

			final CheckPanel checkPanel1 = new CheckPanel("checkPanel1", getLocalizer().getString("title.prompt", RecommendedManagementPanel.this), getLocalizer().getString("msg.warn.update", RecommendedManagementPanel.this)) {

				private static final long serialVersionUID = 7465424124110025236L;

				@Override
				public void howDo() {
					List<Long> assetBinaryVersionIdList = new ArrayList<Long>();
					if (selectMap != null && selectMap.size() > 0) {
						for (Iterator<Long> iterator = selectMap.keySet().iterator(); iterator.hasNext();) {
							Long key = iterator.next();
							if (null == applicationService.getAssetBinaryById(selectMap.get(key).getId())) {
								RecommendedManagementPanel.this.promptPanel.setMessage(selectMap.get(key).getName() + getLocalizer().getString("msg.error.batch.notexist", RecommendedManagementPanel.this, " is not exist!"));
								RecommendedManagementPanel.this.promptPanel.show();
								RecommendedManagementPanel.this.activate(RecommendedManagementPanel.this);
								return;
							}
							assetBinaryVersionIdList.add(selectMap.get(key).getId());
						}
					} else {
						clearControlValue();
						promptPanel.setMessage(getLocalizer().getString("msg.warn.delete.noselect", this, "no selected record!"));
						promptPanel.show();
						return;
					}

					for (Long id : assetBinaryVersionIdList) {
						Long order = recommendOrderMap.get(id);
						if (null == order) {
							promptPanel.setMessage(getLocalizer().getString("msg.order.input", this, "You should be input the recommend order of the selected asset binary version!"));
							promptPanel.show();
							return;
						}

						if (order < 1 || order > 500) {
							promptPanel.setMessage(getLocalizer().getString("msg.order.range", this, "You should be input the recommend order between 1 and 500!"));
							promptPanel.show();
							return;
						}
					}

					for (Long id : assetBinaryVersionIdList) {
						Long order = recommendOrderMap.get(id);
						AssetBinaryVersion assetBinaryVersion = applicationService.getAssetBinaryById(id);
						if (null != assetBinaryVersion) {
							assetBinaryVersion.setRecommendOrder(order);
							assetBinaryVersion.setRecommendUpdateDate(new Date());
							applicationService.updateBinaryVersion(assetBinaryVersion);
						} else {
							continue;
						}
					}
					clearControlValue();

					promptPanel.setMessage(getLocalizer().getString("msg.result.update.success", this, "You successfully batch update the recommend order of the selected asset binary version!"));
					promptPanel.show();

				}
			};
			add(checkPanel1);

			Button saveBtn = new Button("save") {
				private static final long serialVersionUID = 7899433483131667723L;

				@Override
				public void onSubmit() {
					checkPanel1.show();
				}
			};

			add(saveBtn);

			final CheckPanel checkPanel = new CheckPanel("checkPanel", getLocalizer().getString("title.prompt", RecommendedManagementPanel.this), getLocalizer().getString("msg.warn.delete", RecommendedManagementPanel.this)) {

				private static final long serialVersionUID = 7465424124110025236L;

				@Override
				public void howDo() {
					List<Long> assetBinaryVersionIdList = new ArrayList<Long>();

					if (selectMap == null || selectMap.size() < 1) {
						error(getLocalizer().getString("msg.error.nobinaryselect", RecommendedManagementPanel.this, "Please select some asset binary versions!"));
						return;
					}

					if (selectMap != null && selectMap.size() > 0) {
						List<String> statusList = Tools.getConfigValue(deleteStatusConfig);
						for (Iterator<Long> iterator = selectMap.keySet().iterator(); iterator.hasNext();) {
							Long key = iterator.next();
							if (null == applicationService.getAssetBinaryById(key)) {
								RecommendedManagementPanel.this.promptPanel.setMessage(selectMap.get(key).getName() + getLocalizer().getString("msg.error.batch.notexist", RecommendedManagementPanel.this, " is not exist!"));
								RecommendedManagementPanel.this.promptPanel.show();
								RecommendedManagementPanel.this.activate(RecommendedManagementPanel.this);
								return;
							}

							if (!statusList.contains(selectMap.get(key).getStatus().getStatus())) {
								RecommendedManagementPanel.this.promptPanel.setMessage(selectMap.get(key).getName() + getLocalizer().getString("msg.error.can.not.delete", RecommendedManagementPanel.this, " can not be permited to delete!"));
								RecommendedManagementPanel.this.promptPanel.show();
								return;
							}

							assetBinaryVersionIdList.add(selectMap.get(key).getId());
						}
					}

					for (Long id : assetBinaryVersionIdList)
						applicationService.deleteAssetBinary(id);

					clearControlValue();
					promptPanel.setMessage(getLocalizer().getString("msg.result.delete.success", this, "You successfully batch delete the selected asset binary version!"));
					promptPanel.show();

				}
			};
			add(checkPanel);

			Button deleteBtn = new Button("delete") {
				private static final long serialVersionUID = 7899433483131667723L;

				@Override
				public void onSubmit() {
					checkPanel.show();
				}
			};

			add(deleteBtn);
		}

		private void clearControlValue() {
			new GenerateControls(this).ClearControlsValue();
			selectAll = false;
			selectMap.clear();
		}

		@Override
		public void onSubmit() {
		}

		// @Override
		// protected void delegateSubmit(IFormSubmittingComponent
		// submittingComponent) {
		// if (submittingComponent != null) {
		// submittingComponent.onSubmit();
		// }
		// }

		// @Override
		// protected void onSubmit() {
		//
		// List<Long> assetBinaryVersionIdList = new ArrayList<Long>();
		// if (selectMap != null && selectMap.size() > 0) {
		// for (Iterator<Long> iterator = selectMap.keySet().iterator();
		// iterator.hasNext();) {
		// Long key = iterator.next();
		// assetBinaryVersionIdList.add(selectMap.get(key).getId());
		// }
		// }
		//
		// for (Long id : assetBinaryVersionIdList) {
		// Long order = recommendOrderMap.get(id);
		// if (null == order)
		// order = 0L;
		// AssetBinaryVersion assetBinaryVersion =
		// applicationService.getAssetBinaryById(id);
		// if (null != assetBinaryVersion) {
		// assetBinaryVersion.setRecommendOrder(order);
		// applicationService.updateBinaryVersion(assetBinaryVersion);
		// } else {
		// continue;
		// }
		// }
		// clearControlValue();
		// }
	}

	class RecommendedManagementDataView extends DataView<AssetBinaryVersion> {

		private static final long serialVersionUID = -6514262177740324330L;

		protected RecommendedManagementDataView(String id, IDataProvider<AssetBinaryVersion> dataProvider, int itemsPerPage) {
			super(id, dataProvider, itemsPerPage);
		}

		@Override
		protected void populateItem(Item<AssetBinaryVersion> item) {
			final AssetBinaryVersion assetBinaryVersion = (AssetBinaryVersion) item.getModelObject();

			Asset asset = null;
			if (assetBinaryVersion.getAsset() == null || applicationService.getAsset(assetBinaryVersion.getAsset().getId()) == null)
				asset = new Asset();
			else
				asset = applicationService.getAsset(assetBinaryVersion.getAsset().getId());

			Status status = null;
			if (null == assetBinaryVersion.getStatus() || applicationService.getStatusById(assetBinaryVersion.getStatus().getId()) == null)
				status = new Status();
			else
				status = applicationService.getStatusById(assetBinaryVersion.getStatus().getId());

			Provider provider = null;
			if (asset.getAssetProvider() == null || applicationService.getAssetProviderById(asset.getAssetProvider().getId()) == null)
				provider = new Provider();
			else
				provider = applicationService.getAssetProviderById(asset.getAssetProvider().getId());

			// 1 : select
			item.add(new CheckBox("select", new IModel<Boolean>() {

				private static final long serialVersionUID = 4651346966204980877L;

				public Boolean getObject() {
					if (selectMap != null && selectMap.size() > 0) {
						AssetBinaryVersion a = selectMap.get(assetBinaryVersion.getId());
						return null == a ? false : true;
					}
					return false;
				}

				public void setObject(Boolean value) {
					if (null != value && value) {
						selectMap.put(assetBinaryVersion.getId(), assetBinaryVersion);
					}
				}

				public void detach() {
				}
			}));

			// 2 : provider id
			item.add(new Label("providerId", Tools.getValue(provider.getExternalId())));

			// 3 : provider name
			item.add(new Label("providerName", Tools.getValue(provider.getName())));

			// 4 : parent asset id
			item.add(new Label("parentAssetId", Tools.getValue(assetBinaryVersion.getOwnerAssetParentId())));

			// 5 : asset id
			item.add(new Label("assetId", Tools.getValue(asset.getId())));

			// 5.6 : asset External id
			item.add(new Label("assetExternalId", Tools.getValue(asset.getExternalId())));

			// 5.6 : asset External id
			item.add(new Label("binaryExternalId", Tools.getValue(assetBinaryVersion.getExternalId())));

			// 6 : asset name
			// BreadCrumbPanelLink nameLink = new BreadCrumbPanelLink("assetNameLink", RecommendedManagementPanel.this.getBreadCrumbModel(), new IBreadCrumbPanelFactory() {
			//
			// private static final long serialVersionUID = 5387594107373306034L;
			//
			// public BreadCrumbPanel create(String componentId, IBreadCrumbModel breadCrumbModel) {
			// return new ContentManagementDetailPanel(componentId, breadCrumbModel, assetBinaryVersion.getId(), RecommendedManagementPanel.this);
			// }
			// });
			// nameLink.add(new Label("assetName", assetBinaryVersion.getName()));
			// item.add(nameLink);
			item.add(new Label("assetName", Tools.getValue(assetBinaryVersion.getName())));

			// 7 : target publish date
			List<AttributeValue> attributeValueList = applicationService.getAttributeValue(assetBinaryVersion.getId(), EntityType.BINARYVERSION, "TARGETPUBLISHDATE");
			if (attributeValueList != null && attributeValueList.size() > 0)
				item.add(new Label("targetPublishDate", Tools.getValue(attributeValueList.get(0).getValue())));
			else
				item.add(new Label("targetPublishDate", StringUtils.EMPTY));

			// 8 : publish date
			item.add(new Label("publishDate", Tools.getValue(assetBinaryVersion.getPublishDate())));

			// 9 : category1
			// String category1 = StringUtils.EMPTY;
			// String category2 = StringUtils.EMPTY;
			// if (asset.getId() != null) {
			// List<Category> categoryList = applicationService.getAllCategoryByAssetId(asset.getId(), 0, Integer.MAX_VALUE);
			//
			// if (categoryList != null && categoryList.size() > 0) {
			// for (Category category : categoryList) {
			// Category parentCategory = category.getParentCategory();
			// if (parentCategory != null) {
			// parentCategory = applicationService.getCategoryById(parentCategory.getId());
			// category1 += parentCategory == null ? StringUtils.EMPTY : parentCategory.getName() + "(" + category.getName() + "), ";
			// }
			// category2 += category.getName() + ", ";
			// }
			// }
			// }

			// item.add(new Label("category1", category1));

			// 10 : category2
			// item.add(new Label("category2", category2));

			// 11 : new arrival due date
			// item.add(new Label("newArrivalDueDate", Tools.getValue(assetBinaryVersion.getNewArrivalDueDate())));

			// 12 : recommend start date
			item.add(new Label("recommendStartDate", Tools.getValue(assetBinaryVersion.getRecommendStartDate())));

			// 13 : recommend due date
			item.add(new Label("recommendDueDate", Tools.getValue(assetBinaryVersion.getRecommendDueDate())));

			// 14 : recommend order
			TextField<String> recommendOrderField = new TextField<String>("recommendOrder", new IModel<String>() {
				private static final long serialVersionUID = 6297714565920521727L;

				public String getObject() {
					Long order = recommendOrderMap.get(assetBinaryVersion.getId());
					if (order != null)
						return order.toString();
					else
						return StringUtils.EMPTY;
				}

				public void setObject(String value) {
					Long longValue = null;
					try {
						longValue = Long.parseLong(value);
					} catch (Exception exception) {
						log.error(exception);
					}
					recommendOrderMap.put(assetBinaryVersion.getId(), longValue);
					/**
					 * promptPanel.setMessage(getLocalizer().getString("msg.order.n", RecommendedManagementPanel.this, "You successfully batch delete the selected asset binary version!")); promptPanel.show();
					 **/
				}

				public void detach() {
				}
			});
			item.add(recommendOrderField);
			Long order = assetBinaryVersion.getRecommendOrder() != null ? assetBinaryVersion.getRecommendOrder() : 0L;
			recommendOrderMap.put(assetBinaryVersion.getId(), order);

			// 15 : commission rate
			// item.add(new Label("commissionRate",
			// Tools.getValue(assetBinaryVersion.getCommissionRate())));
			// Float rate = null;
			// List<AttributeValue> rates = applicationService.getAttributeValue(assetBinaryVersion.getId(), EntityType.BINARYVERSION, EavConstant.COMMISSION_RATE);
			// if (rates != null && rates.size() > 0) {
			// rate = (Float) rates.get(0).getValue();
			// }
			// String commissionRate = Tools.getValue(rate);
			// if (StringUtils.isNotBlank(commissionRate))
			// item.add(new Label("commissionRate", commissionRate + getLocalizer().getString("commissionRateSymbol", this, " %")));
			// else
			// item.add(new Label("commissionRate", commissionRate));

			// 16 : status
			item.add(new Label("status", status.getStatus()));

			item.add(new Label("updateTime", Tools.getValue(assetBinaryVersion.getRecommendUpdateDate(), true)));
		}

		public void updateModel(ContentManagementCondition condition) {
			RecommendContentManagementDataProvider dataProvider = (RecommendContentManagementDataProvider) this.getDataProvider();
			dataProvider.setCondition(condition);
		}
	}

	public String getTitle() {
		return this.getLocalizer().getString("title", this, Constant.RECOMMENDED_CONTENT);
	}
}