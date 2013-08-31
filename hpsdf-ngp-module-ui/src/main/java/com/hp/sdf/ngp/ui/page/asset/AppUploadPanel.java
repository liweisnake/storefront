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
package com.hp.sdf.ngp.ui.page.asset;

import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.extensions.breadcrumb.IBreadCrumbModel;
import org.apache.wicket.extensions.breadcrumb.panel.BreadCrumbPanel;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.jbpm.api.Execution;
import org.jbpm.api.ProcessInstance;

import com.hp.sdf.ngp.api.search.SearchExpression;
import com.hp.sdf.ngp.api.search.Condition.NumberComparer;
import com.hp.sdf.ngp.common.constant.AssetLifecycleConstants;
import com.hp.sdf.ngp.model.Asset;
import com.hp.sdf.ngp.model.AssetBinaryVersion;
import com.hp.sdf.ngp.model.AssetLifecycleAction;
import com.hp.sdf.ngp.model.Category;
import com.hp.sdf.ngp.model.Platform;
import com.hp.sdf.ngp.model.RoleCategory;
import com.hp.sdf.ngp.model.Status;
import com.hp.sdf.ngp.search.condition.category.CategoryAssetCategoryRelationAssetIdCondition;
import com.hp.sdf.ngp.search.engine.SearchExpressionImpl;
import com.hp.sdf.ngp.service.ApplicationService;
import com.hp.sdf.ngp.service.UserService;
import com.hp.sdf.ngp.ui.WicketSession;
import com.hp.sdf.ngp.workflow.AssetBinaryVersionLifeCycleEngine;
import com.hp.sdf.ngp.workflow.jbpm.JbpmHelper;
import com.hp.sdf.ngp.workflow.jbpm.JbpmServiceHolder;

public class AppUploadPanel extends BreadCrumbPanel {

	private static final long serialVersionUID = 2582326979849534414L;

	private final static Log log = LogFactory.getLog(AppUploadPanel.class);

	@SpringBean
	private UserService userService;

	@SpringBean
	private ApplicationService applicationService;

	@SpringBean
	private AssetBinaryVersionLifeCycleEngine assetBinaryVersionLifeCycleEngine;

	private Long appId, binaryId;

	private BreadCrumbPanel caller;

	public AppUploadPanel(String id, IBreadCrumbModel breadCrumbModel, Long appId, Long binaryId, BreadCrumbPanel caller) {
		super(id, breadCrumbModel);
		breadCrumbModel.setActive(this);
		this.appId = appId;
		this.caller = caller;
		this.binaryId = binaryId;
		log.debug("binaryId=" + binaryId);

		AppUploadForm appUploadForm = new AppUploadForm("appUploadForm", applicationService);
		add(appUploadForm);

		// Create feedback panel and add to page
		final FeedbackPanel feedback = new FeedbackPanel("feedback");
		add(feedback);
	}

	public String getTitle() {
		return this.getLocalizer().getString("title", this, "Application Binary");
	}

	public class AppUploadForm extends Form<Void> {

		private static final long serialVersionUID = -7900872350387847350L;

		private ApplicationService applicationService;

		AssetBinaryVersion appBinary = new AssetBinaryVersion();

		Platform platform = new Platform();

		FileUploadField fileField;

		public AppUploadForm(String id, ApplicationService applicationService) {
			super(id);
			this.applicationService = applicationService;

			if (binaryId != null) {
				appBinary = applicationService.getAssetBinaryById(binaryId);

			}

			fileField = new FileUploadField("file");
			fileField.setRequired(true);
			add(fileField);
			TextField<String> versionField = new TextField<String>("version", new PropertyModel<String>(appBinary, "version"));
			versionField.setRequired(true);
			if (binaryId != null) {
				versionField.setEnabled(false);
			}
			// TODO: add validator
			// versionField.add();
			add(versionField);

		}

		/**
		 * @see org.apache.wicket.markup.html.form.Form#onSubmit()
		 */
		public final void onSubmit() {
			if (log.isDebugEnabled()) {
				log.debug("app binary submitted.");
				log.debug("app binary version : " + appBinary.getVersion());
			}

			final FileUpload fileUpload = fileField.getFileUpload();

			byte[] fileBuffer = null;
			if (fileUpload != null) {
				try {
					InputStream inputStream = fileUpload.getInputStream();
					fileBuffer = new byte[inputStream.available()];
					inputStream.read(fileBuffer);
					appBinary.setVersion(appBinary.getVersion());

					String fileName = fileUpload.getClientFileName();
					if (StringUtils.isNotEmpty(fileName)) {
						if (fileName.contains("\\")) {
							int beginIndex = fileName.lastIndexOf("\\");
							appBinary.setFileName(fileName.substring(beginIndex + 1));
						} else {
							appBinary.setFileName(fileName);
						}
					}

				} catch (Exception e) {
					throw new IllegalStateException("Unable to write file");
				}
			}

			if (binaryId != null) {
				AssetBinaryVersion bin = applicationService.getAssetBinaryById(binaryId);
				applicationService.deleteAssetBinary(bin.getId());
				appBinary.setId(null);
			}
			final Asset app = applicationService.getAsset(appId);
			appBinary.setAsset(app);
			// sets first status
			Status status = assetBinaryVersionLifeCycleEngine.getStartupStatus();
			appBinary.setStatus(status);
			appBinary.setCreateDate(new Date());
			applicationService.saveAssetBinary(fileBuffer, appBinary);

			SearchExpression searchExpression = new SearchExpressionImpl();
			searchExpression.addCondition(new CategoryAssetCategoryRelationAssetIdCondition(appId, NumberComparer.EQUAL));
			List<Category> categories = applicationService.searchCategory(searchExpression);

			if (categories != null && categories.size() > 0) {
				Set<Long> categoryIds = new HashSet<Long>();
				for (Category category : categories) {
					categoryIds.add(category.getId());
				}
				applicationService.associateCategory(appId, appBinary.getId(), categoryIds);
			}

			/** start assetLifecycleAction **/
			List<RoleCategory> roleCategories = userService.getRoleCategoryByUserId(WicketSession.get().getUserId());

			String userRoleString = "";
			if (roleCategories != null) {
				for (RoleCategory roleCategory : roleCategories) {
					if (StringUtils.isEmpty(userRoleString)) {
						userRoleString = roleCategory.getRoleName();
					} else {
						userRoleString = userRoleString + "," + roleCategory.getRoleName();
					}

				}
			}
			AssetLifecycleAction request = new AssetLifecycleAction();
			request.setAsset(app);
			request.setSubmitterid(WicketSession.get().getUserId());
			request.setOwnerid(WicketSession.get().getUserId());
			request.setBinaryVersion(appBinary);
			request.setCreateDate(new Date());
			request.setEvent(AssetLifecycleConstants.BIN_ACTION_TYPE_UPLOAD);
			applicationService.saveOrUpdateAssetLifecycleAction(request);

			Map<String, Object> variables = new HashMap<String, Object>();
			variables.put("cycleActionId", request.getId());
			variables.put("binId", appBinary.getId());
			variables.put("assetId", app.getId());
			variables.put("userRoles", userRoleString);
			/** appId is unique, no end for userId */
			String instanceId = JbpmHelper.genInstanceId("binaryLifeCycle", "", String.valueOf(appBinary.getId()));
			if (null != JbpmHelper.checkPromotionStatus(instanceId)) {
				ProcessInstance ins = JbpmServiceHolder.executionService.createProcessInstanceQuery().processInstanceKey(instanceId).uniqueResult();

				JbpmServiceHolder.executionService.endProcessInstance(ins.getId(), Execution.STATE_ENDED);
				log.warn("execution already exised:" + instanceId + ",end it as unnormal ended flow");
			}
			JbpmServiceHolder.executionService.startProcessInstanceByKey("binaryLifeCycle", variables, instanceId);

			activate(caller);
		}
	}

}

// $Id$