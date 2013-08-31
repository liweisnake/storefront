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

import java.nio.charset.Charset;
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
import org.apache.wicket.Component;
import org.apache.wicket.authorization.strategies.role.metadata.MetaDataRoleAuthorizationStrategy;
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
import org.apache.wicket.util.resource.IStringResourceStream;
import org.apache.wicket.util.resource.StringResourceStream;
import org.apache.wicket.validation.validator.RangeValidator;

import com.hp.sdf.ngp.api.comon.EntityType;
import com.hp.sdf.ngp.api.exception.AssetCatalogServiceException;
import com.hp.sdf.ngp.common.annotation.Value;
import com.hp.sdf.ngp.custom.sbm.api.storeclient.service.ContentCatalogService;
import com.hp.sdf.ngp.eav.model.AttributeValue;
import com.hp.sdf.ngp.model.Asset;
import com.hp.sdf.ngp.model.AssetBinaryVersion;
import com.hp.sdf.ngp.model.AssetLifecycleActionHistory;
import com.hp.sdf.ngp.model.Category;
import com.hp.sdf.ngp.model.Provider;
import com.hp.sdf.ngp.model.Status;
import com.hp.sdf.ngp.service.ApplicationService;
import com.hp.sdf.ngp.ui.WicketSession;
import com.hp.sdf.ngp.ui.common.CheckPanel;
import com.hp.sdf.ngp.ui.common.Constant;
import com.hp.sdf.ngp.ui.common.ContentManagementCondition;
import com.hp.sdf.ngp.ui.common.CustomizeDateTextField;
import com.hp.sdf.ngp.ui.common.CustomizePagingNavigator;
import com.hp.sdf.ngp.ui.common.EavConstant;
import com.hp.sdf.ngp.ui.common.PromptPanel;
import com.hp.sdf.ngp.ui.common.SelectOption;
import com.hp.sdf.ngp.ui.common.Tools;
import com.hp.sdf.ngp.ui.provider.ContentManagementDataProvider;
import com.hp.sdf.ngp.workflow.Privilege;

public class ContentManagementPanel extends BreadCrumbPanel {

	private static final long serialVersionUID = -8524244192134679105L;

	private static Log log = LogFactory.getLog(ContentManagementPanel.class);

	@Value("application.itemsperpage")
	private int itemsPerPage = Constant.DEFALUT_PER_PAGE_COUNT;

	@Value("content.management.procedure.name")
	private String procedureName = "recordProcessor";

	@Value("content.management.procedure.run.flag")
	private String procedureRunFlag = "false";

	@SpringBean
	ApplicationService applicationService;

	@Value("dynamicForm.pageFilePath")
	private String statusConfigPath;

	@Value("delete.status")
	private String deleteStatusConfig = "declined,revoked,kill_switch";

	@Value("comment.type")
	private String commentTypeConfig = "COMMONFOR_DECLINED,COMMONFOR_REVOKED,COMMONFOR_KILL_SWITCH";

	@Value("recommend.order")
	private String recommendOrder = "400";

	@SpringBean
	ContentCatalogService contentCatalogService;

	private ContentManagementDataView dataView;

	private PromptPanel promptPanel;

	private HiddenField<Integer> hidden;

	private Boolean showDeleteDialog = false;

	private String promptMsg = StringUtils.EMPTY;

	private Map<Long, AssetBinaryVersion> selectMap = new HashMap<Long, AssetBinaryVersion>();

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

	public ContentManagementPanel(String id, IBreadCrumbModel breadCrumbModel) {
		super(id, breadCrumbModel);

		// default setting
		BreadCrumbPanelLink defaultSettingLink = new BreadCrumbPanelLink("defaultSetting", ContentManagementPanel.this.getBreadCrumbModel(), new IBreadCrumbPanelFactory() {

			private static final long serialVersionUID = -5113356719217533015L;

			public BreadCrumbPanel create(String componentId, IBreadCrumbModel breadCrumbModel) {
				return new DefaultSettingPanel(componentId, breadCrumbModel, ContentManagementPanel.this);
			}
		});
		add(defaultSettingLink);
		MetaDataRoleAuthorizationStrategy.authorize(defaultSettingLink, Component.RENDER, Privilege.DEFAULTSETTING);

		// new binary version
		BreadCrumbPanelLink newBinaryVersionLink = new BreadCrumbPanelLink("newBinaryVersion", ContentManagementPanel.this.getBreadCrumbModel(), new IBreadCrumbPanelFactory() {

			private static final long serialVersionUID = -7078020538448932972L;

			public BreadCrumbPanel create(String componentId, IBreadCrumbModel breadCrumbModel) {
				return new ContentManagementNewPanel(componentId, breadCrumbModel, ContentManagementPanel.this);
			}
		});
		add(newBinaryVersionLink);
		MetaDataRoleAuthorizationStrategy.authorize(newBinaryVersionLink, Component.RENDER, Privilege.NEWCONTENT);

		add(new ContentManagementUpdateForm("updateForm"));

		ContentManagementSearchPanel searchPanel = new ContentManagementSearchPanel("searchPanel", dataView, applicationService);
		add(searchPanel);
		MetaDataRoleAuthorizationStrategy.authorize(searchPanel, Component.RENDER, Privilege.CONTENTSEARCH);

		promptPanel = new PromptPanel("promptPanel", getLocalizer().getString("title.prompt", this, "Prompt"), getLocalizer().getString("msg.result.delete.success", this, "You successfully delete the asset binary version!"), null, StringUtils.EMPTY);
		add(promptPanel);
	}

	public class ContentManagementUpdateForm extends Form<Void> {

		private static final long serialVersionUID = 6364024991342867280L;

		// @Generate(id = "update.status", controlType =
		// CONTROL_TYPE.DropDownChoice, dataType = DATA_TYPE.status, key = "id",
		// value = "status")
		private SelectOption statusOption;
		private DropDownChoice<SelectOption> statusOptionField;

		// @Generate(id = "update.comments", controlType =
		// CONTROL_TYPE.TextArea)
		private String comments;
		private TextArea<String> commentsField;

		// @Generate(id = "update.commentType")
		private SelectOption commentTypeOption;
		private DropDownChoice<SelectOption> commentTypeOptionField;

		// @Generate(id = "update.commissionRate")
		private Double commissionRate;
		private TextField<Double> commissionRateField;

		// @Generate(id = "update.recommendStartDate")
		private Date recommendStartDate;
		private CustomizeDateTextField recommendStartDateField;

		// @Generate(id = "update.recommendDueDate")
		private Date recommendDueDate;
		private CustomizeDateTextField recommendDueDateField;

		// @Generate(id = "update.newArrivalDueDate")
		private Date newArrivalDueDate;
		private CustomizeDateTextField newArrivalDueDateField;

		@SuppressWarnings("unused")
		private Boolean selectAll = false;

		private Integer updateType = 1;

		public ContentManagementUpdateForm(String id) {
			super(id);

			ContentManagementDataProvider dataProvider = new ContentManagementDataProvider(applicationService, new ContentManagementCondition());

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
			MetaDataRoleAuthorizationStrategy.authorize(radioGroup, Component.RENDER, Privilege.CONTENTBATCH);
			// new GenerateControls(this, radioGroup,
			// applicationService).Generate();

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
			List<String> listTemp = Tools.getConfigValue(commentTypeConfig);
			Long numTemp = 1L;
			for (String value : listTemp)
				commentTypeList.add(new SelectOption(numTemp++, value));
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
			commissionRateField.add(new RangeValidator<Double>(0D, 100D));

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

			dataView = new ContentManagementDataView("binaryVersions", dataProvider, itemsPerPage);
			add(dataView);

			add(new CustomizePagingNavigator("navigator", dataView));

			final CheckPanel checkPanel = new CheckPanel("checkPanel", getLocalizer().getString("title.prompt", ContentManagementPanel.this), getLocalizer().getString("msg.warn.delete", ContentManagementPanel.this)) {

				private static final long serialVersionUID = 7465424124110025236L;

				@Override
				public void howDo() {
					List<Long> assetBinaryVersionIdList = new ArrayList<Long>();

					if (selectMap == null || selectMap.size() < 1) {
						error(getLocalizer().getString("msg.error.nobinaryselect", ContentManagementPanel.this, "Please select some asset binary versions!"));
						return;
					}

					if (selectMap != null && selectMap.size() > 0) {
						List<String> statusList = Tools.getConfigValue(deleteStatusConfig);
						for (Iterator<Long> iterator = selectMap.keySet().iterator(); iterator.hasNext();) {
							Long key = iterator.next();
							if (null == applicationService.getAssetBinaryById(selectMap.get(key).getId())) {
								ContentManagementPanel.this.promptPanel.setMessage(selectMap.get(key).getName() + getLocalizer().getString("msg.error.batch.notexist", ContentManagementPanel.this, " is not exist!"));
								ContentManagementPanel.this.promptPanel.show();
								ContentManagementPanel.this.activate(ContentManagementPanel.this);
								return;
							}

							if (!statusList.contains(selectMap.get(key).getStatus().getStatus())) {
								ContentManagementPanel.this.promptPanel.setMessage(selectMap.get(key).getName() + getLocalizer().getString("msg.error.can.not.delete", ContentManagementPanel.this, " can not be permited to delete!"));
								ContentManagementPanel.this.promptPanel.show();
								return;
							}

							assetBinaryVersionIdList.add(selectMap.get(key).getId());
						}
					}

					for (Long id : assetBinaryVersionIdList)
						applicationService.deleteAssetBinary(id);

					promptPanel.setMessage(getLocalizer().getString("msg.result.delete.success", this, "You successfully batch delete the selected asset binary version!"));
					promptPanel.show();
					selectAll = false;
					selectMap.clear();

				}
			};
			add(checkPanel);

			Button deleteButton = new Button("deleteSubmit") {
				private static final long serialVersionUID = 7670145284920200375L;

				@Override
				public void onSubmit() {
					checkPanel.show();
				}
			};
			ContentManagementUpdateForm.this.add(deleteButton);
			MetaDataRoleAuthorizationStrategy.authorize(deleteButton, Component.RENDER, Privilege.CONTENTDELETE);

			final CheckPanel checkPanel1 = new CheckPanel("checkPanel1", getLocalizer().getString("title.prompt", ContentManagementPanel.this), getLocalizer().getString("msg.warn.update", ContentManagementPanel.this)) {

				private static final long serialVersionUID = 7465424124110025236L;

				@Override
				public void howDo() {
					List<Long> assetBinaryVersionIdList = new ArrayList<Long>();

					if (selectMap != null && selectMap.size() < 1) {
						error(getLocalizer().getString("msg.error.nobinaryselect", ContentManagementPanel.this, "Please select some asset binary versions!"));
						return;
					}

					if (selectMap != null && selectMap.size() > 0) {

						for (Iterator<Long> iterator = selectMap.keySet().iterator(); iterator.hasNext();) {
							Long key = iterator.next();
							if (null == applicationService.getAssetBinaryById(selectMap.get(key).getId())) {
								ContentManagementPanel.this.promptPanel.setMessage(selectMap.get(key).getName() + getLocalizer().getString("msg.error.batch.notexist", ContentManagementPanel.this, " is not exist!"));
								ContentManagementPanel.this.promptPanel.show();
								ContentManagementPanel.this.activate(ContentManagementPanel.this);
								return;
							}
							assetBinaryVersionIdList.add(selectMap.get(key).getId());
						}
					}

					Boolean errorFlag = false;

					if (null == updateType)
						return;

					switch (updateType) {
					case 1:
						if (null == statusOption) {
							error(getLocalizer().getString("need.insert.status", ContentManagementPanel.this, "Field 'Status' is required."));
							errorFlag = true;
						}
						if (null == commentTypeOption) {
							error(getLocalizer().getString("need.insert.commentTypeOption", ContentManagementPanel.this, "Field 'Status Change Comment Type' is required."));
							errorFlag = true;
						}
						if (null == comments) {
							error(getLocalizer().getString("need.insert.operatorComments", ContentManagementPanel.this, "Field 'Operator Comments' is required."));
							errorFlag = true;
						}

						if (errorFlag)
							return;

						synchronized (this) {
							List<String> statusList = new ArrayList<String>();
							for (Iterator<Long> iterator = selectMap.keySet().iterator(); iterator.hasNext();) {
								Long key = iterator.next();
								AssetBinaryVersion binaryVersion = selectMap.get(key);
								Status status = binaryVersion.getStatus();
								statusList.add(status.getStatus());
							}

							if (Tools.checkStatus(statusList, statusOption.getValue(), statusConfigPath)) {
								applicationService.batchUpdateAssetBinaryStatus(assetBinaryVersionIdList, statusOption.getKey());

								List<Long> deleteVersionList = new ArrayList<Long>();
								if (statusOption.getValue().equals(Constant.STATUS_PUBLISHED)) {
									if (selectMap != null && selectMap.size() > 0) {
										for (Iterator<Long> iterator = selectMap.keySet().iterator(); iterator.hasNext();) {
											Long key = iterator.next();
											AssetBinaryVersion assetBinaryVersion = selectMap.get(key);

											if (deleteVersionList.contains(assetBinaryVersion.getId()))
												continue;

											if (assetBinaryVersion.getAsset() != null && assetBinaryVersion.getAsset().getId() != null) {
												applicationService.updateAssetVersion(assetBinaryVersion.getAsset().getId(), key);

												List<AssetBinaryVersion> deleteList = applicationService.getAssetBinaryByAssetId(assetBinaryVersion.getAsset().getId());
												for (AssetBinaryVersion version : deleteList) {
													deleteVersionList.add(version.getId());
												}
												deleteVersionList.remove(assetBinaryVersion.getId());

												applicationService.removeAttributes(assetBinaryVersion.getId(), EntityType.BINARYVERSION, EavConstant.PUBLISH_FLG);
												applicationService.addAttribute(assetBinaryVersion.getId(), EntityType.BINARYVERSION, EavConstant.PUBLISH_FLG, Constant.PUBLISH_FLG_DEFAULT);
												if (procedureRunFlag.equals(Constant.STRING_TRUE)) {
													List<Object> parameters = new ArrayList<Object>();
													parameters.add(assetBinaryVersion.getAsset().getId());
													try {
														contentCatalogService.callDatabaseProcedure(procedureName, parameters);
													} catch (AssetCatalogServiceException exception) {
														log.error(exception.getMessage());
													}
												}
											}
										}
									}
								} else if (statusOption.getValue().equals(Constant.STATUS_KILL_SWITCH)) {
									if (selectMap != null && selectMap.size() > 0) {
										for (Iterator<Long> iterator = selectMap.keySet().iterator(); iterator.hasNext();) {
											Long key = iterator.next();
											AssetBinaryVersion assetBinaryVersion = selectMap.get(key);

											if (deleteVersionList.contains(assetBinaryVersion.getId()))
												continue;

											applicationService.addAttribute(assetBinaryVersion.getId(), EntityType.BINARYVERSION, EavConstant.KILL_DATE, new Date());
											applicationService.addAttribute(assetBinaryVersion.getId(), EntityType.BINARYVERSION, EavConstant.KILL_REASON, commentTypeOption.getValue());
										}
									}
								}
								batchUpdateStatusHistory(selectMap, statusOption.getValue(), comments, commentTypeOption.getValue());

								for (Long versionId : deleteVersionList)
									applicationService.deleteAssetBinary(versionId);
							} else {
								error(getLocalizer().getString("msg.error.statuschange.condition", ContentManagementPanel.this, "Can not conform to the condition of status change!"));
								selectAll = false;
								selectMap.clear();
								return;
							}
						}
						break;
					case 2:
						if (null == commissionRate) {
							error(getLocalizer().getString("need.insert.commissionRate", ContentManagementPanel.this, "Field 'commission Rate' is required."));
							return;
						}

						// applicationService.batchUpdateAssetBinaryProviderCommissionRate(assetBinaryVersionIdList, commissionRate);
						synchronized (this) {
							if (selectMap != null && selectMap.size() > 0) {
								for (Iterator<Long> iterator = selectMap.keySet().iterator(); iterator.hasNext();) {
									AssetBinaryVersion assetBinaryVersion = selectMap.get(iterator.next());
									applicationService.removeAttributes(assetBinaryVersion.getId(), EntityType.BINARYVERSION, EavConstant.COMMISSION_RATE);
									applicationService.addAttribute(assetBinaryVersion.getId(), EntityType.BINARYVERSION, EavConstant.COMMISSION_RATE, Tools.getScale(commissionRate));
								}
							}
						}
						break;
					case 3:
						if (null == recommendStartDate) {
							error(getLocalizer().getString("need.insert.recommendStartDate", ContentManagementPanel.this, "Field 'Recommend Start Date' is required."));
							errorFlag = true;
						}
						if (null == recommendDueDate) {
							error(getLocalizer().getString("need.insert.recommendDueDate", ContentManagementPanel.this, "Field 'Recommend Due Date' is required."));
							errorFlag = true;
						}

						if (errorFlag)
							return;

						synchronized (this) {

							Long recommendOrderTemp = Constant.RECOMMEND_ORDER;
							if (recommendOrder != null && StringUtils.isNotBlank(recommendOrder)) {
								try {
									recommendOrderTemp = Long.parseLong(recommendOrder);
								} catch (Exception ex) {
									log.error(ex.getMessage());
								}
							}

							applicationService.batchUpdateAssetBinaryRecommend(assetBinaryVersionIdList, Tools.parseBeginDate(recommendStartDate), Tools.parseEndDate(recommendDueDate), recommendOrderTemp);

							if (selectMap != null && selectMap.size() > 0) {
								for (Iterator<Long> iterator = selectMap.keySet().iterator(); iterator.hasNext();) {
									AssetBinaryVersion assetBinaryVersion = selectMap.get(iterator.next());
									if (assetBinaryVersion.getAsset() != null && assetBinaryVersion.getAsset().getId() != null) {
										if (procedureRunFlag.equals(Constant.STRING_TRUE)) {
											List<Object> parameters = new ArrayList<Object>();
											parameters.add(assetBinaryVersion.getAsset().getId());
											try {
												contentCatalogService.callDatabaseProcedure(procedureName, parameters);
											} catch (AssetCatalogServiceException exception) {
												log.error(exception.getMessage());
											}
										}
									}
								}
							}
						}
						break;
					case 4:
						if (null == newArrivalDueDate) {
							error(getLocalizer().getString("need.insert.newArrivalDueDate", ContentManagementPanel.this, "Field 'New Arrival Due Date' is required."));
							return;
						}

						synchronized (this) {
							applicationService.batchUpdateAssetBinaryNewArrivalDueDate(assetBinaryVersionIdList, Tools.parseEndDate(newArrivalDueDate));

							if (selectMap != null && selectMap.size() > 0) {
								for (Iterator<Long> iterator = selectMap.keySet().iterator(); iterator.hasNext();) {
									AssetBinaryVersion assetBinaryVersion = selectMap.get(iterator.next());
									if (assetBinaryVersion.getAsset() != null && assetBinaryVersion.getAsset().getId() != null) {
										if (procedureRunFlag.equals(Constant.STRING_TRUE)) {
											List<Object> parameters = new ArrayList<Object>();
											parameters.add(assetBinaryVersion.getAsset().getId());
											try {
												contentCatalogService.callDatabaseProcedure(procedureName, parameters);
											} catch (AssetCatalogServiceException exception) {
												log.error(exception.getMessage());
											}
										}
									}
								}
							}
						}
						break;
					}

					clearControlValue();
					clearInput();

					promptPanel.setMessage(getLocalizer().getString("msg.result.update.success", this, "You successfully batch update the selected asset binary version!"));
					promptPanel.show();
					selectMap.clear();

				}
			};
			add(checkPanel1);
			Button button = new Button("updateSubmit") {
				private static final long serialVersionUID = 7670145284920200375L;

				@Override
				public void onSubmit() {
					checkPanel1.show();
				}

			};

			ContentManagementUpdateForm.this.add(button);
			MetaDataRoleAuthorizationStrategy.authorize(button, Component.RENDER, Privilege.CONTENTUPDATE);

			WebResource export = new WebResource() {

				private static final long serialVersionUID = 449871856730754460L;

				@Override
				public IResourceStream getResourceStream() {
					CharSequence cs = getCsvData();
					IStringResourceStream srs = new StringResourceStream(cs, "text/csv");
					srs.setCharset(Charset.forName(Constant.UTF8));
					return srs;
				}

				@Override
				protected void setHeaders(WebResponse response) {
					super.setHeaders(response);
					response.setAttachmentHeader(getLocalizer().getString("download.file.name", ContentManagementPanel.this, "BinaryVersion.csv"));
				}
			};
			export.setCacheable(false);
			Link<Void> downLink = new ResourceLink<Void>("downLink", export);
			downLink.add(new AttributeModifier("onclick", true, new Model<String>("if (confirm('" + getLocalizer().getString("msg.warn.download", this, "Do you want to download the csv file?") + "')) { if (parseInt(document.getElementById('hidden').value) > " + Constant.EXCEL_MAX_ROWS + ") { alert('" + getLocalizer().getString("msg.error.download", this, "Search result row number exceeds the maximum in excel!") + "'); return false; } else { return true; } } else {return false;}")));
			add(downLink);
			MetaDataRoleAuthorizationStrategy.authorize(downLink, Component.RENDER, Privilege.CONTENTDOWNLOAD);

			hidden = new HiddenField<Integer>("hidden", new Model<Integer>(dataProvider.size()));
			hidden.setOutputMarkupId(false);
			add(hidden);
		}

		public StringBuilder getCsvData() {

			List<AssetBinaryVersion> list = ((ContentManagementDataProvider) dataView.getDataProvider()).getAll();

			StringBuilder sb = new StringBuilder();
			sb.append(getLocalizer().getString("providerId", this, "Provider External ID") + Constant.CSV_SUFFIX);
			sb.append(getLocalizer().getString("providerName", this, "Provider Name") + Constant.CSV_SUFFIX);
			sb.append(getLocalizer().getString("parentAssetId", this, "Parent Asset ID") + Constant.CSV_SUFFIX);
			sb.append(getLocalizer().getString("assetId", this, "Asset ID") + Constant.CSV_SUFFIX);
			sb.append(getLocalizer().getString("binaryVersionExternalId", this, "BinaryVersion External ID") + Constant.CSV_SUFFIX);
			sb.append(getLocalizer().getString("externalId", this, "Asset External ID") + Constant.CSV_SUFFIX);
			sb.append(getLocalizer().getString("assetName", this, "Name") + Constant.CSV_SUFFIX);
			sb.append(getLocalizer().getString("targetPublishDate", this, "Target Publish Date") + Constant.CSV_SUFFIX);
			sb.append(getLocalizer().getString("publishDate", this, "Publish Date") + Constant.CSV_SUFFIX);
			sb.append(getLocalizer().getString("category1", this, "Genre1") + Constant.CSV_SUFFIX);
			sb.append(getLocalizer().getString("category2", this, "Genre2") + Constant.CSV_SUFFIX);
			sb.append(getLocalizer().getString("status", this, "Status") + Constant.CSV_SUFFIX);
			sb.append(getLocalizer().getString("newArrivalDueDate", this, "New Arrival Due Date") + Constant.CSV_SUFFIX);
			sb.append(getLocalizer().getString("recommendStartDate", this, "Recommend Start Date") + Constant.CSV_SUFFIX);
			sb.append(getLocalizer().getString("recommendDueDate", this, "Recommend Due Date") + Constant.CSV_SUFFIX);
			sb.append(getLocalizer().getString("recommendOrder", this, "Recommend Order") + Constant.CSV_SUFFIX);
			sb.append(getLocalizer().getString("commissionRate", this, "Commission Rate") + getLocalizer().getString("commissionRateSymbol", this, "(%)") + "\n");

			if (list != null && list.size() > 0)
				for (AssetBinaryVersion assetBinaryVersion : list) {
					final Asset asset = applicationService.getAsset(assetBinaryVersion.getAsset().getId());
					if (null == asset)
						continue;

					Status status = applicationService.getStatusById(assetBinaryVersion.getStatus().getId());
					if (null == status)
						continue;

					Provider provider = null;
					if (asset.getAssetProvider() != null) {
						provider = applicationService.getAssetProviderById(asset.getAssetProvider().getId());
					} else {
						provider = new Provider();
					}

					// 1 : provider external id
					sb.append(Tools.getValue(provider.getExternalId()) + Constant.CSV_SUFFIX);
					// 2 : provider name
					sb.append(Tools.getValue(provider.getName()) + Constant.CSV_SUFFIX);
					// 3 : parent asset id
					sb.append(Tools.getValue(assetBinaryVersion.getOwnerAssetParentId()) + Constant.CSV_SUFFIX);
					// 4 : asset id
					sb.append(Tools.getValue(asset.getId()) + Constant.CSV_SUFFIX);
					// 5 : binary version external id
					sb.append(Tools.getValue(assetBinaryVersion.getExternalId()) + Constant.CSV_SUFFIX);
					// 6 : asset external id
					sb.append(Tools.getValue(asset.getExternalId()) + Constant.CSV_SUFFIX);
					// 7 : asset name
					sb.append(Tools.getValue(assetBinaryVersion.getName()) + Constant.CSV_SUFFIX);
					// 8 : target publish date
					List<AttributeValue> attributeValueList = applicationService.getAttributeValue(assetBinaryVersion.getId(), EntityType.BINARYVERSION, EavConstant.TARGET_PUBLISH_DATE);
					if (attributeValueList != null && attributeValueList.size() > 0)
						sb.append(Tools.getValue(attributeValueList.get(0).getValue()) + Constant.CSV_SUFFIX);
					else
						sb.append(Constant.CSV_SUFFIX);
					// 9 : publish date
					sb.append(Tools.getValue(asset.getPublishDate()) + Constant.CSV_SUFFIX);
					// 10 : category1
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
					// 11 : category2
					sb.append(StringUtils.chomp(category2, "|") + Constant.CSV_SUFFIX);
					// 12 : status
					sb.append(status.getStatus() + Constant.CSV_SUFFIX);
					// 13 : new arrival due date
					sb.append(Tools.getValue(assetBinaryVersion.getNewArrivalDueDate()) + Constant.CSV_SUFFIX);
					// 14 : recommend start date
					sb.append(Tools.getValue(assetBinaryVersion.getRecommendStartDate()) + Constant.CSV_SUFFIX);
					// 15 : recommend due date
					sb.append(Tools.getValue(assetBinaryVersion.getRecommendDueDate()) + Constant.CSV_SUFFIX);
					// 16 : recommend order
					sb.append(Tools.getValue(assetBinaryVersion.getRecommendOrder()) + Constant.CSV_SUFFIX);
					// 17 : commission rate
					Float rate = null;
					List<AttributeValue> rates = applicationService.getAttributeValue(assetBinaryVersion.getId(), EntityType.ASSET, EavConstant.COMMISSION_RATE);
					if (rates != null && rates.size() > 0) {
						rate = ((Float) rates.get(0).getValue()) * 100;
					}
					sb.append(Tools.getValue(rate) + "\n");
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
			selectAll = false;
			selectMap.clear();
			super.onError();
		}

		private void batchUpdateStatusHistory(Map<Long, AssetBinaryVersion> selectMap, String statusName, String comments, String commentType) {
			if (selectMap != null && selectMap.size() > 0) {

				// TODO Anders Zhu : 增加justin说的action
				for (Iterator<Long> iterator = selectMap.keySet().iterator(); iterator.hasNext();) {
					Long key = iterator.next();
					AssetBinaryVersion assetBinaryVersion = selectMap.get(key);

					if (assetBinaryVersion.getAsset() == null)
						continue;

					Asset asset = applicationService.getAsset(assetBinaryVersion.getAsset().getId());
					if (null == asset)
						continue;

					if (assetBinaryVersion.getStatus() == null)
						continue;

					Status status = applicationService.getStatusById(assetBinaryVersion.getStatus().getId());
					if (null == status)
						continue;

					AssetLifecycleActionHistory history = new AssetLifecycleActionHistory();
					history.setCommentType(commentType);
					history.setComments(comments);
					history.setCreateDate(new Date());
					history.setCompleteDate(new Date());
					// history.setEvent();
					// history.setResult();
					history.setSubmitterId(WicketSession.get().getUserId());
					history.setOwnerId(WicketSession.get().getUserId());
					// history.setProcessStatus();
					history.setDescription(assetBinaryVersion.getDescription());
					history.setAssetBinaryVersionId(assetBinaryVersion.getId());
					history.setPostStatus(statusName);
					history.setAssetId(asset.getId());
					history.setPrestatus(status.getStatus());
					// history.setNotificationDate();
					history.setExternalId(assetBinaryVersion.getExternalId());
					// history.setSource();
					history.setVersion(assetBinaryVersion.getVersion());
					applicationService.saveOrUpdateAssetLifecycleActionHistory(history);
				}

			}
		}
	}

	class ContentManagementDataView extends DataView<AssetBinaryVersion> {

		private static final long serialVersionUID = -6514262177740324330L;

		protected ContentManagementDataView(String id, IDataProvider<AssetBinaryVersion> dataProvider, int itemsPerPage) {
			super(id, dataProvider, itemsPerPage);
		}

		@SuppressWarnings("unchecked")
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
						AssetBinaryVersion model = selectMap.get(assetBinaryVersion.getId());
						return null == model ? false : true;
					}
					return false;
				}

				public void setObject(Boolean value) {
					if (value) {
						selectMap.put(assetBinaryVersion.getId(), assetBinaryVersion);
					}
				}

				public void detach() {
				}
			}));

			// 2 : provider external id
			item.add(new Label("providerId", Tools.getValue(provider.getExternalId())));

			// 3 : provider name
			item.add(new Label("providerName", Tools.getValue(provider.getName())));

			// 4 : parent asset id
			item.add(new Label("parentAssetId", Tools.getValue(assetBinaryVersion.getOwnerAssetParentId())));

			// 5 : asset id
			item.add(new Label("assetId", Tools.getValue(asset.getId())));

			// 6 : external id
			item.add(new Label("externalId", Tools.getValue(asset.getExternalId())));

			// 6.6 : added binaryVersionExternalId
			item.add(new Label("binaryVersionExternalId", Tools.getValue(assetBinaryVersion.getExternalId())));

			// 7 : asset name
			BreadCrumbPanelLink nameLink = new BreadCrumbPanelLink("assetNameLink", ContentManagementPanel.this.getBreadCrumbModel(), new IBreadCrumbPanelFactory() {

				private static final long serialVersionUID = 5387594107373306034L;

				public BreadCrumbPanel create(String componentId, IBreadCrumbModel breadCrumbModel) {
					if (null == applicationService.getAssetBinaryById(assetBinaryVersion.getId())) {
						ContentManagementPanel.this.promptPanel.setMessage(getLocalizer().getString("msg.error.link.notexist", ContentManagementPanel.this, "This asset binary version is not exist!"));
						ContentManagementPanel.this.promptPanel.show();
						return ContentManagementPanel.this;
					}
					return new ContentManagementDetailPanel(componentId, breadCrumbModel, assetBinaryVersion.getId(), ContentManagementPanel.this);
				}
			});
			nameLink.add(new Label("assetName", assetBinaryVersion.getName()));
			item.add(nameLink);
			Label assetNameHidden = new Label("assetNameHidden", assetBinaryVersion.getName());
			item.add(assetNameHidden);
			MetaDataRoleAuthorizationStrategy.authorize(nameLink, Component.RENDER, Privilege.UPDATECONTENT);
			MetaDataRoleAuthorizationStrategy.authorize(assetNameHidden, Component.RENDER, Privilege.NOTUPDATECONTENT);

			// 8 : target publish date
			List<AttributeValue> attributeValueList = applicationService.getAttributeValue(assetBinaryVersion.getId(), EntityType.BINARYVERSION, "TARGETPUBLISHDATE");
			if (attributeValueList != null && attributeValueList.size() > 0)
				item.add(new Label("targetPublishDate", Tools.getValue(attributeValueList.get(0).getValue())));
			else
				item.add(new Label("targetPublishDate", StringUtils.EMPTY));

			// 9 : publish date
			item.add(new Label("publishDate", Tools.getValue(assetBinaryVersion.getPublishDate())));

			// 10 : category1
			String category1 = StringUtils.EMPTY;
			String category2 = StringUtils.EMPTY;
			if (asset.getId() != null) {
				List<Category> categoryList = applicationService.getAllCategoryByAssetId(asset.getId(), 0, Integer.MAX_VALUE);
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
			}

			item.add(new Label("category1", StringUtils.chomp(category1, ", ")));

			// 11 : category2
			item.add(new Label("category2", StringUtils.chomp(category2, ", ")));

			// 12 : new arrival due date
			item.add(new Label("newArrivalDueDate", Tools.getValue(assetBinaryVersion.getNewArrivalDueDate())));

			// 13 : recommend start date
			item.add(new Label("recommendStartDate", Tools.getValue(assetBinaryVersion.getRecommendStartDate())));

			// 14 : recommend due date
			item.add(new Label("recommendDueDate", Tools.getValue(assetBinaryVersion.getRecommendDueDate())));

			// 15 : recommend order
			item.add(new Label("recommendOrder", Tools.getValue(assetBinaryVersion.getRecommendOrder())));

			// 16 : commission rate
			// item.add(new Label("commissionRate", Tools.getValue(provider == null ? "" : provider.getCommissionRate())));
			Float rate = null;
			List<AttributeValue> rates = applicationService.getAttributeValue(assetBinaryVersion.getId(), EntityType.BINARYVERSION, EavConstant.COMMISSION_RATE);
			if (rates != null && rates.size() > 0) {
				rate = (Float) rates.get(0).getValue();
			}
			String commissionRate = Tools.getValue(rate);
			// if (StringUtils.isNotBlank(commissionRate))
			// item.add(new Label("commissionRate", commissionRate + getLocalizer().getString("commissionSymbol", this, "%")));
			// else
			item.add(new Label("commissionRate", commissionRate));

			// 17 : status
			final Long assetId = asset.getId();
			BreadCrumbPanelLink statusLink = new BreadCrumbPanelLink("statusLink", ContentManagementPanel.this.getBreadCrumbModel(), new IBreadCrumbPanelFactory() {

				private static final long serialVersionUID = 5387594107373306034L;

				public BreadCrumbPanel create(String componentId, IBreadCrumbModel breadCrumbModel) {
					if (null == applicationService.getAssetBinaryById(assetBinaryVersion.getId())) {
						ContentManagementPanel.this.promptPanel.setMessage(getLocalizer().getString("msg.error.link.notexist", ContentManagementPanel.this, "This asset binary version is not exist!"));
						ContentManagementPanel.this.promptPanel.show();
						return ContentManagementPanel.this;
					}
					return new StatusChangeHistoryPanel(componentId, breadCrumbModel, assetId, ContentManagementPanel.this);
				}
			});
			Label statusHidden;
			if (asset.getId() != null) {
				statusLink.add(new Label("status", status.getStatus()));
				statusHidden = new Label("statusHidden", status.getStatus());
			} else {
				statusLink.add(new Label("status", StringUtils.EMPTY));
				statusHidden = new Label("statusHidden", StringUtils.EMPTY);
			}
			item.add(statusHidden);
			item.add(statusLink);
			MetaDataRoleAuthorizationStrategy.authorize(statusLink, Component.RENDER, Privilege.STATUSHISTORY);
			MetaDataRoleAuthorizationStrategy.authorize(statusHidden, Component.RENDER, Privilege.NOTSTATUSHISTORY);
		}

		public void updateModel(ContentManagementCondition condition) {
			ContentManagementDataProvider dataProvider = (ContentManagementDataProvider) this.getDataProvider();
			dataProvider.setCondition(condition);
			hidden.setDefaultModel(new Model<Integer>(dataProvider.size()));
		}
	}

	public String getTitle() {
		return this.getLocalizer().getString("title", this, Constant.CONTENT_MANAGEMENT);
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
				promptPanel.setMessage(getLocalizer().getString("msg.result.delete.success", this, "You successfully delete the asset binary version!"));
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

	public String getProcedureName() {
		return procedureName;
	}

	public void setProcedureName(String procedureName) {
		this.procedureName = procedureName;
	}

	public String getProcedureRunFlag() {
		return procedureRunFlag;
	}

	public void setProcedureRunFlag(String procedureRunFlag) {
		this.procedureRunFlag = procedureRunFlag;
	}

	public String getStatusConfigPath() {
		return statusConfigPath;
	}

	public void setStatusConfigPath(String statusConfigPath) {
		this.statusConfigPath = statusConfigPath;
	}
}