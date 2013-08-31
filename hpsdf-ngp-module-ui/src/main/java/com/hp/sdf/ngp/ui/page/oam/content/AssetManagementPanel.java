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
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.extensions.breadcrumb.IBreadCrumbModel;
import org.apache.wicket.extensions.breadcrumb.IBreadCrumbParticipant;
import org.apache.wicket.extensions.breadcrumb.panel.BreadCrumbPanel;
import org.apache.wicket.extensions.breadcrumb.panel.BreadCrumbPanelLink;
import org.apache.wicket.extensions.breadcrumb.panel.IBreadCrumbPanelFactory;
import org.apache.wicket.extensions.yui.calendar.DatePicker;
import org.apache.wicket.feedback.ContainerFeedbackMessageFilter;
import org.apache.wicket.markup.html.WebResource;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.HiddenField;
import org.apache.wicket.markup.html.form.Radio;
import org.apache.wicket.markup.html.form.RadioGroup;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.link.ResourceLink;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.protocol.http.WebResponse;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.resource.IResourceStream;
import org.apache.wicket.util.resource.StringResourceStream;

import com.hp.sdf.ngp.api.comon.EntityType;
import com.hp.sdf.ngp.common.annotation.Value;
import com.hp.sdf.ngp.eav.model.AttributeValue;
import com.hp.sdf.ngp.model.Asset;
import com.hp.sdf.ngp.model.Category;
import com.hp.sdf.ngp.model.Provider;
import com.hp.sdf.ngp.model.Status;
import com.hp.sdf.ngp.service.ApplicationService;
import com.hp.sdf.ngp.ui.common.AssetManagementCondition;
import com.hp.sdf.ngp.ui.common.Constant;
import com.hp.sdf.ngp.ui.common.CustomizeDateTextField;
import com.hp.sdf.ngp.ui.common.CustomizePagingNavigator;
import com.hp.sdf.ngp.ui.common.EavConstant;
import com.hp.sdf.ngp.ui.common.PromptPanel;
import com.hp.sdf.ngp.ui.common.SelectOption;
import com.hp.sdf.ngp.ui.common.Tools;
import com.hp.sdf.ngp.ui.common.UIException;
import com.hp.sdf.ngp.ui.provider.AssetManagementDataProvider;

public class AssetManagementPanel extends BreadCrumbPanel {

	private static final long serialVersionUID = -8524244192134679105L;

	private static Log log = LogFactory.getLog(AssetManagementPanel.class);

	@Value("application.itemsperpage")
	private int itemsPerPage = Constant.DEFALUT_PER_PAGE_COUNT;

	@SpringBean
	ApplicationService applicationService;

	private AssetManagementDataView dataView;

	private PromptPanel promptPanel;

	private HiddenField<Integer> hidden;

	private Boolean showDeleteDialog = false;

	private String promptMsg = StringUtils.EMPTY;

	private Map<Long, Asset> selectMap = new HashMap<Long, Asset>();

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

	public AssetManagementPanel(String id, IBreadCrumbModel breadCrumbModel) {
		super(id, breadCrumbModel);

		// default setting
		// add(new BreadCrumbPanelLink("defaultSetting", AssetManagementPanel.this.getBreadCrumbModel(), new IBreadCrumbPanelFactory() {
		//
		// private static final long serialVersionUID = -5113356719217533015L;
		//
		// public BreadCrumbPanel create(String componentId, IBreadCrumbModel breadCrumbModel) {
		// return new DefaultSettingPanel(componentId, breadCrumbModel, AssetManagementPanel.this);
		// }
		// }));

		// new asset
		add(new BreadCrumbPanelLink("newAsset", AssetManagementPanel.this.getBreadCrumbModel(), new IBreadCrumbPanelFactory() {

			private static final long serialVersionUID = -7078020538448932972L;

			public BreadCrumbPanel create(String componentId, IBreadCrumbModel breadCrumbModel) {
				return new AssetManagementNewPanel(componentId, breadCrumbModel, AssetManagementPanel.this);
			}
		}));

		add(new AssetManagementUpdateForm("updateForm"));
		add(new AssetManagementSearchPanel("searchPanel", dataView, applicationService));

		promptPanel = new PromptPanel("promptPanel", getLocalizer().getString("title.prompt", this, "Prompt"), getLocalizer().getString("msg.delete.result", this, "You successfully delete the asset!"), null, StringUtils.EMPTY);
		add(promptPanel);
	}

	public class AssetManagementUpdateForm extends Form<Void> {

		private static final long serialVersionUID = 6364024991342867280L;

		private SelectOption statusOption;
		private DropDownChoice<SelectOption> statusOptionField;

		private String comments;
		private TextArea<String> commentsField;

		private SelectOption commentTypeOption;
		private DropDownChoice<SelectOption> commentTypeOptionField;

		private Double commissionRate;
		private TextField<Double> commissionRateField;

		private Date recommendStartDate;
		private CustomizeDateTextField recommendStartDateField;

		private Date recommendDueDate;
		private CustomizeDateTextField recommendDueDateField;

		private Date newArrivalDueDate;
		private CustomizeDateTextField newArrivalDueDateField;

		@SuppressWarnings("unused")
		private Boolean selectAll = false;

		private Integer updateType = 1;

		public AssetManagementUpdateForm(String id) {
			super(id);

			AssetManagementDataProvider dataProvider = new AssetManagementDataProvider(applicationService, new AssetManagementCondition());

			add(new FeedbackPanel("updateFeedback", new ContainerFeedbackMessageFilter(this)));

			CheckBox checkBox = new CheckBox("selectAll", new PropertyModel<Boolean>(this, "selectAll"));
			checkBox.setMarkupId("selectAll");
			add(checkBox);

			RadioGroup<Integer> radioGroup = new RadioGroup<Integer>("radioGroup", new PropertyModel<Integer>(this, "updateType"));
			radioGroup.add(new Radio<Integer>("radio1", new Model<Integer>(1)));
			radioGroup.add(new Radio<Integer>("radio2", new Model<Integer>(2)));
			radioGroup.add(new Radio<Integer>("radio3", new Model<Integer>(3)));
			radioGroup.add(new Radio<Integer>("radio4", new Model<Integer>(4)));
			radioGroup.setRequired(false);
			add(radioGroup);

			// status
			final List<Status> statusList = applicationService.getAllStatus();
			ChoiceRenderer<SelectOption> choiceRenderer = new ChoiceRenderer<SelectOption>(Constant.SELECT_OPTION.VALUE.toString(), Constant.SELECT_OPTION.KEY.toString());
			statusOptionField = new DropDownChoice<SelectOption>("update.status", new PropertyModel<SelectOption>(this, "statusOption"), new AbstractReadOnlyModel<List<SelectOption>>() {
				private static final long serialVersionUID = 7653635619701254501L;

				public List<SelectOption> getObject() {
					List<SelectOption> selects = new ArrayList<SelectOption>();
					for (Status s : statusList) {
						try {
							selects.add(new SelectOption(s.getId(), s.getStatus()));
						} catch (Exception ex) {
							log.error(ex.getMessage());
						}
					}
					return selects;
				}
			}, choiceRenderer);
			radioGroup.add(statusOptionField);

			// comments
			commentsField = new TextArea<String>("update.comments", new PropertyModel<String>(this, "comments"));
			radioGroup.add(commentsField);

			// commentType
			final List<SelectOption> commentTypeList = new ArrayList<SelectOption>();
			commentTypeList.add(new SelectOption(1L, "DECLINED"));
			commentTypeList.add(new SelectOption(2L, "REVOKED"));
			commentTypeList.add(new SelectOption(3L, "KILL_SWITCH"));
			commentTypeOptionField = new DropDownChoice<SelectOption>("update.commentType", new PropertyModel<SelectOption>(this, "commentTypeOption"), new AbstractReadOnlyModel<List<SelectOption>>() {
				private static final long serialVersionUID = 7653635619701254501L;

				public List<SelectOption> getObject() {
					List<SelectOption> selects = new ArrayList<SelectOption>();
					for (SelectOption s : commentTypeList) {
						try {
							selects.add(new SelectOption(s.getKey(), s.getValue()));
						} catch (Exception ex) {
							log.error(ex.getMessage());
						}
					}
					return selects;
				}
			}, choiceRenderer);
			radioGroup.add(commentTypeOptionField);

			// commissionRate
			commissionRateField = new TextField<Double>("update.commissionRate", new PropertyModel<Double>(this, "commissionRate"));
			radioGroup.add(commissionRateField);

			// recommendStartDate
			recommendStartDateField = new CustomizeDateTextField("update.recommendStartDate", new PropertyModel<Date>(this, "recommendStartDate"), Constant.DATE_PATTERN);
			recommendStartDateField.add(new DatePicker());
			radioGroup.add(recommendStartDateField);

			// recommendDueDate
			recommendDueDateField = new CustomizeDateTextField("update.recommendDueDate", new PropertyModel<Date>(this, "recommendDueDate"), Constant.DATE_PATTERN);
			recommendDueDateField.add(new DatePicker());
			radioGroup.add(recommendDueDateField);

			// newArrivalDueDate
			newArrivalDueDateField = new CustomizeDateTextField("update.newArrivalDueDate", new PropertyModel<Date>(this, "newArrivalDueDate"), Constant.DATE_PATTERN);
			newArrivalDueDateField.add(new DatePicker());
			radioGroup.add(newArrivalDueDateField);

			dataView = new AssetManagementDataView("assets", dataProvider, itemsPerPage);
			add(dataView);

			add(new CustomizePagingNavigator("navigator", dataView));

			Button button = new Button("updateSubmit") {
				private static final long serialVersionUID = 7670145284920200375L;

				@Override
				public void onSubmit() {
					List<Long> assetIdList = new ArrayList<Long>();

					if (selectMap != null && selectMap.size() < 1) {
						error(getLocalizer().getString("error.select.no.asset", AssetManagementPanel.this, "Please select some assets!"));
						return;
					}

					if (selectMap != null && selectMap.size() > 0) {

						for (Iterator<Long> iterator = selectMap.keySet().iterator(); iterator.hasNext();) {
							Long key = iterator.next();
							assetIdList.add(selectMap.get(key).getId());
							if (null == applicationService.getAsset(selectMap.get(key).getId())) {
								AssetManagementPanel.this.promptPanel.setMessage(selectMap.get(key).getName() + getLocalizer().getString("msg.error.batch.delete", AssetManagementPanel.this, " is not exist!"));
								AssetManagementPanel.this.promptPanel.show();
								AssetManagementPanel.this.activate(AssetManagementPanel.this);
								return;
							}
						}
					}

					Boolean errorFlag = false;

					if (null == updateType)
						return;

					switch (updateType) {
					case 1:
						if (null == statusOption) {
							error("Field 'Status' is required.");
							errorFlag = true;
						}
						if (null == comments) {
							error("Field 'Operator Comments' is required.");
							errorFlag = true;
						}
						if (null == commentTypeOption) {
							error("Field 'Status Change Comment Type' is required.");
							errorFlag = true;
						}

						if (errorFlag)
							return;

						synchronized (this) {
							applicationService.batchUpdateAssetStatus(assetIdList, statusOption.getKey());
						}
						break;
					case 2:
						if (null == commissionRate) {
							error("Field 'commission Rate' is required.");
							return;
						}

						synchronized (this) {
							if (selectMap != null && selectMap.size() > 0) {
								for (Iterator<Long> iterator = selectMap.keySet().iterator(); iterator.hasNext();) {
									Asset asset = selectMap.get(iterator.next());
									applicationService.removeAttributes(asset.getId(), EntityType.ASSET, EavConstant.COMMISSION_RATE);
									applicationService.addAttribute(asset.getId(), EntityType.ASSET, EavConstant.COMMISSION_RATE, commissionRate);
								}
							}
						}
						break;
					case 3:
						if (null == recommendStartDate) {
							error("Field 'Recommend Start Date' is required.");
							errorFlag = true;
						}
						if (null == recommendDueDate) {
							error("Field 'Recommend Due Date' is required.");
							errorFlag = true;
						}

						if (errorFlag)
							return;

						synchronized (this) {
							if (selectMap != null && selectMap.size() > 0) {
								for (Iterator<Long> iterator = selectMap.keySet().iterator(); iterator.hasNext();) {
									Asset asset = selectMap.get(iterator.next());
									asset.setRecommendStartDate(recommendStartDate);
									asset.setRecommendDueDate(recommendDueDate);
									asset.setRecommendOrder(Constant.RECOMMEND_ORDER);
									applicationService.updateAsset(asset);
								}
							}
						}
						break;
					case 4:
						if (null == newArrivalDueDate) {
							error("Field 'New Arrival Due Date' is required.");
							return;
						}

						synchronized (this) {
							if (selectMap != null && selectMap.size() > 0) {
								for (Iterator<Long> iterator = selectMap.keySet().iterator(); iterator.hasNext();) {
									Asset asset = selectMap.get(iterator.next());
									asset.setNewArrivalDueDate(newArrivalDueDate);
									applicationService.updateAsset(asset);
								}
							}
						}
						break;
					}

					clearControlValue();
					clearInput();

					promptPanel.setMessage(getLocalizer().getString("msg.update.result", AssetManagementPanel.this, "You successfully batch update the selected assets!"));
					promptPanel.show();
				}

			};
			button.add(Tools.addConfirmJs(getLocalizer().getString("msg.update", AssetManagementPanel.this, "Do you want to batch update the selected assets?")));
			radioGroup.add(button);

			WebResource export = new WebResource() {

				private static final long serialVersionUID = 449871856730754460L;

				@Override
				public IResourceStream getResourceStream() {
					CharSequence cs = getCsvData();
					return new StringResourceStream(cs, "text/csv");
				}

				@Override
				protected void setHeaders(WebResponse response) {
					super.setHeaders(response);
					response.setAttachmentHeader(getLocalizer().getString("download.file.name", AssetManagementPanel.this, "Asset.csv"));
				}
			};
			export.setCacheable(false);
			Link<Void> downLink = new ResourceLink<Void>("downLink", export);
			downLink.add(new AttributeModifier("onclick", true, new Model<String>("if (confirm('" + getLocalizer().getString("msg.download", this, "Do you want to download the csv file?") + "')) { if (parseInt(document.getElementById('hidden').value) > " + Constant.EXCEL_MAX_ROWS + ") { alert('" + getLocalizer().getString("msg.error.download", this, "Search result row number exceeds the maximum in excel!") + "'); return false; } else { return true; } } else {return false;}")));
			add(downLink);

			hidden = new HiddenField<Integer>("hidden", new Model<Integer>(dataProvider.size()));
			hidden.setOutputMarkupId(false);
			radioGroup.add(hidden);
		}

		public StringBuilder getCsvData() {

			List<Asset> list = ((AssetManagementDataProvider) dataView.getDataProvider()).getAll();

			StringBuilder sb = new StringBuilder();
			sb.append(getLocalizer().getString("providerId", this, "Provider ID") + Constant.CSV_SUFFIX);
			sb.append(getLocalizer().getString("providerName", this, "Provider Name") + Constant.CSV_SUFFIX);
			sb.append(getLocalizer().getString("parentAssetId", this, "Parent Asset ID") + Constant.CSV_SUFFIX);
			sb.append(getLocalizer().getString("assetId", this, "Asset ID") + Constant.CSV_SUFFIX);
			sb.append(getLocalizer().getString("assetName", this, "Asset Name") + Constant.CSV_SUFFIX);
			sb.append(getLocalizer().getString("publishDate", this, "Publish Date") + Constant.CSV_SUFFIX);
			sb.append(getLocalizer().getString("category1", this, "Genre1") + Constant.CSV_SUFFIX);
			sb.append(getLocalizer().getString("category2", this, "Genre2") + Constant.CSV_SUFFIX);
			sb.append(getLocalizer().getString("newArrivalDueDate", this, "New Arrival Due Date") + Constant.CSV_SUFFIX);
			sb.append(getLocalizer().getString("recommendStartDate", this, "Recommend Start Date") + Constant.CSV_SUFFIX);
			sb.append(getLocalizer().getString("recommendDueDate", this, "Recommend Due Date") + Constant.CSV_SUFFIX);
			sb.append(getLocalizer().getString("recommendOrder", this, "Recommend Order") + Constant.CSV_SUFFIX);
			sb.append(getLocalizer().getString("commissionRate", this, "Commission Rate") + Constant.CSV_SUFFIX);
			sb.append(getLocalizer().getString("status", this, "Status") + "\n");

			if (list != null && list.size() > 0)
				for (Asset asset : list) {
					Status status = applicationService.getStatusById(asset.getStatus().getId());
					if (null == status)
						continue;

					Provider provider = null;
					if (asset.getAssetProvider() != null) {
						provider = applicationService.getAssetProviderById(asset.getAssetProvider().getId());
					} else {
						provider = new Provider();
					}

					// 1 : provider external id
					sb.append(Tools.getValue(provider.getId()) + Constant.CSV_SUFFIX);
					// 2 : provider name
					sb.append(Tools.getValue(provider.getName()) + Constant.CSV_SUFFIX);
					// 3 : parent asset id
					sb.append(Tools.getValue(asset.getAsset() != null ? asset.getAsset().getId() : StringUtils.EMPTY) + Constant.CSV_SUFFIX);
					// 4 : asset id
					sb.append(Tools.getValue(asset.getId()) + Constant.CSV_SUFFIX);
					// 5 : external id
					// sb.append(Tools.getValue(assetBinaryVersion.getExternalId()) + Constant.CSV_SUFFIX);
					// 6 : asset name
					sb.append(Tools.getValue(asset.getName()) + Constant.CSV_SUFFIX);
					// 7 : target publish date
					// List<AttributeValue> attributeValueList = applicationService.getAttributeValue(assetBinaryVersion.getId(), EntityType.BINARYVERSION, "TARGETPUBLISHDATE");
					// if (attributeValueList != null && attributeValueList.size() > 0)
					// sb.append(Tools.getValue(attributeValueList.get(0).getValue()) + Constant.CSV_SUFFIX);
					// else
					// sb.append(Constant.CSV_SUFFIX);
					// 8 : publish date
					sb.append(Tools.getValue(asset.getPublishDate()) + Constant.CSV_SUFFIX);
					// 9 : category1
					List<Category> categoryList = applicationService.getAllCategoryByAssetId(asset.getId(), 0, Integer.MAX_VALUE);
					String category1 = StringUtils.EMPTY;
					String category2 = StringUtils.EMPTY;
					if (categoryList != null && categoryList.size() > 0) {
						for (Category category : categoryList) {
							Category parentCategory = category.getParentCategory();
							if (parentCategory != null) {
								parentCategory = applicationService.getCategoryById(parentCategory.getId());
								category1 += parentCategory == null ? StringUtils.EMPTY : parentCategory.getName() + "(" + category.getName() + ")|";
							}
							category2 += category.getName() + "|";
						}
					}
					sb.append(StringUtils.chomp(category1, "|") + Constant.CSV_SUFFIX);
					// 10 : category2
					sb.append(StringUtils.chomp(category2, "|") + Constant.CSV_SUFFIX);
					// 11 : new arrival due date
					sb.append(Tools.getValue(asset.getNewArrivalDueDate()) + Constant.CSV_SUFFIX);
					// 12 : recommend start date
					sb.append(Tools.getValue(asset.getRecommendStartDate()) + Constant.CSV_SUFFIX);
					// 13 : recommend due date
					sb.append(Tools.getValue(asset.getRecommendDueDate()) + Constant.CSV_SUFFIX);
					// 14 : recommend order
					sb.append(Tools.getValue(asset.getRecommendOrder()) + Constant.CSV_SUFFIX);
					// 15 : commission rate
					Float rate = null;
					List<AttributeValue> rates = applicationService.getAttributeValue(asset.getId(), EntityType.ASSET, EavConstant.COMMISSION_RATE);
					if (rates != null && rates.size() > 0) {
						rate = (Float)rates.get(0).getValue();
					}
					sb.append(Tools.getValue(rate) + Constant.CSV_SUFFIX);
					// 16 : status
					sb.append(status.getStatus() + "\n");
				}

			return sb;
		}

		@Override
		protected void onSubmit() {

		}

		private void clearControlValue() {
			statusOption = null;
			comments = null;
			commentTypeOption = null;
			commissionRate = null;
			recommendStartDate = null;
			recommendDueDate = null;
			newArrivalDueDate = null;
			selectAll = false;
			selectMap.clear();
		}

		@Override
		protected void onError() {
			this.clearInput();
			super.onError();
		}
	}

	class AssetManagementDataView extends DataView<Asset> {

		private static final long serialVersionUID = -6514262177740324330L;

		protected AssetManagementDataView(String id, IDataProvider<Asset> dataProvider, int itemsPerPage) {
			super(id, dataProvider, itemsPerPage);
		}

		@SuppressWarnings("unchecked")
		@Override
		protected void populateItem(Item<Asset> item) {
			final Asset asset = item.getModelObject();

			Status status = applicationService.getStatusById(asset.getStatus().getId());
			if (null == status)
				throw new UIException("Status can not be null");

			Provider provider = null;
			if (asset.getAssetProvider() != null) {
				provider = applicationService.getAssetProviderById(asset.getAssetProvider().getId());
			}

			Asset parentAsset = null;
			if (asset.getAsset() != null) {
				parentAsset = applicationService.getAsset(asset.getAsset().getId());
			}

			// 1 : select
			item.add(new CheckBox("select", new IModel<Boolean>() {

				private static final long serialVersionUID = 4651346966204980877L;

				public Boolean getObject() {
					if (selectMap != null && selectMap.size() > 0) {
						Asset model = selectMap.get(asset.getId());
						return null == model ? false : true;
					}
					return false;
				}

				public void setObject(Boolean value) {
					if (value) {
						selectMap.put(asset.getId(), asset);
					}
				}

				public void detach() {
				}
			}));

			// 2 : provider external id
			item.add(new Label("providerId", Tools.getValue(provider == null ? StringUtils.EMPTY : provider.getId())));

			// 3 : provider name
			item.add(new Label("providerName", Tools.getValue(provider == null ? StringUtils.EMPTY : provider.getName())));

			// 4 : parent asset id
			item.add(new Label("parentAssetId", Tools.getValue(parentAsset == null ? StringUtils.EMPTY : parentAsset.getId())));

			// 5 : asset id
			item.add(new Label("assetId", Tools.getValue(asset.getId())));

			// 6 : external id
			// item.add(new Label("externalId", Tools.getValue(assetBinaryVersion.getExternalId())));

			// 7 : asset name
			BreadCrumbPanelLink nameLink = new BreadCrumbPanelLink("assetNameLink", AssetManagementPanel.this.getBreadCrumbModel(), new IBreadCrumbPanelFactory() {

				private static final long serialVersionUID = 5387594107373306034L;

				public BreadCrumbPanel create(String componentId, IBreadCrumbModel breadCrumbModel) {
					if (null == applicationService.getAsset(asset.getId())) {
						AssetManagementPanel.this.promptPanel.setMessage(getLocalizer().getString("msg.error.delete", AssetManagementPanel.this, "This asset is not exist!"));
						AssetManagementPanel.this.promptPanel.show();
						return AssetManagementPanel.this;
					}
					return new AssetManagementDetailPanel(componentId, breadCrumbModel, asset.getId(), AssetManagementPanel.this);
				}
			});
			nameLink.add(new Label("assetName", asset.getName()));
			item.add(nameLink);

			// 8 : target publish date
			// List<AttributeValue> attributeValueList = applicationService.getAttributeValue(assetBinaryVersion.getId(), EntityType.BINARYVERSION, "TARGETPUBLISHDATE");
			// if (attributeValueList != null && attributeValueList.size() > 0)
			// item.add(new Label("targetPublishDate", Tools.getValue(attributeValueList.get(0).getValue())));
			// else
			// item.add(new Label("targetPublishDate", StringUtils.EMPTY));

			// 9 : publish date
			item.add(new Label("publishDate", Tools.getValue(asset.getPublishDate())));

			// 10 : category1
			List<Category> categoryList = applicationService.getAllCategoryByAssetId(asset.getId(), 0, Integer.MAX_VALUE);
			String category1 = StringUtils.EMPTY;
			String category2 = StringUtils.EMPTY;
			if (categoryList != null && categoryList.size() > 0) {
				for (Category category : categoryList) {
					Category parentCategory = category.getParentCategory();
					if (parentCategory != null) {
						parentCategory = applicationService.getCategoryById(parentCategory.getId());
						category1 += parentCategory == null ? StringUtils.EMPTY : parentCategory.getName() + "(" + category.getName() + "), ";
					}
					category2 += category.getName() + ", ";
				}
			}

			item.add(new Label("category1", StringUtils.chomp(category1, ", ")));

			// 11 : category2
			item.add(new Label("category2", StringUtils.chomp(category2, ", ")));

			// 12 : new arrival due date
			item.add(new Label("newArrivalDueDate", Tools.getValue(asset.getNewArrivalDueDate())));

			// 13 : recommend start date
			item.add(new Label("recommendStartDate", Tools.getValue(asset.getRecommendStartDate())));

			// 14 : recommend due date
			item.add(new Label("recommendDueDate", Tools.getValue(asset.getRecommendDueDate())));

			// 15 : recommend order
			item.add(new Label("recommendOrder", Tools.getValue(asset.getRecommendOrder())));

			// 16 : commission rate
			// item.add(new Label("commissionRate", Tools.getValue(provider == null ? "" : provider.getCommissionRate())));
			Float rate = null;
			List<AttributeValue> rates = applicationService.getAttributeValue(asset.getId(), EntityType.ASSET, EavConstant.COMMISSION_RATE);
			if (rates != null && rates.size() > 0) {
				rate = (Float)rates.get(0).getValue();
			}
			String commissionRate = Tools.getValue(rate);
			if (StringUtils.isNotBlank(commissionRate))
				item.add(new Label("commissionRate", commissionRate + getLocalizer().getString("commissionSymbol", this, "%")));
			else
				item.add(new Label("commissionRate", commissionRate));

			// 17 : status
			item.add(new Label("status", status.getStatus()));
		}

		public void updateModel(AssetManagementCondition condition) {
			AssetManagementDataProvider dataProvider = (AssetManagementDataProvider) this.getDataProvider();
			dataProvider.setCondition(condition);
			hidden.setDefaultModel(new Model<Integer>(dataProvider.size()));
		}
	}

	public String getTitle() {
		return this.getLocalizer().getString("title", this, "Assets List");
	}

	public Boolean getShowDeleteDialog() {
		return showDeleteDialog;
	}

	public void setShowDeleteDialog(Boolean showDeleteDialog) {
		this.showDeleteDialog = showDeleteDialog;
	}

	@Override
	public void activate(IBreadCrumbParticipant participant) {
		if (showDeleteDialog) {
			if (promptMsg.equals(StringUtils.EMPTY))
				promptPanel.setMessage(getLocalizer().getString("msg.delete.result", this, "You successfully delete the asset!"));
			else
				promptPanel.setMessage(promptMsg);
			promptPanel.show();
			showDeleteDialog = false;
		}
		super.activate(participant);
	}

	public String getPromptMsg() {
		return promptMsg;
	}

	public void setPromptMsg(String promptMsg) {
		this.promptMsg = promptMsg;
	}
}