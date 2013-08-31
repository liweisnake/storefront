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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.extensions.breadcrumb.IBreadCrumbModel;
import org.apache.wicket.extensions.breadcrumb.panel.BreadCrumbPanel;
import org.apache.wicket.extensions.breadcrumb.panel.IBreadCrumbPanelFactory;
import org.apache.wicket.extensions.markup.html.form.DateTextField;
import org.apache.wicket.extensions.yui.calendar.DatePicker;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.hp.sdf.ngp.model.Status;
import com.hp.sdf.ngp.service.ApplicationService;
import com.hp.sdf.ngp.ui.common.Constant;
import com.hp.sdf.ngp.ui.common.SearchCondition;
import com.hp.sdf.ngp.ui.common.SelectOption;

public class ConfigurationBinarySearchPanel extends BreadCrumbPanel {

	private static final long serialVersionUID = 1L;

	private static final Log log = LogFactory.getLog(ConfigurationBinarySearchPanel.class);

	public ConfigurationBinarySearchPanel(String id, IBreadCrumbModel breadCrumbModel) {
		super(id, breadCrumbModel);
		this.add(new FeedbackPanel("feedBack"));
		this.add(new ConfigurationBinarySearchForm("configurationBinarySearchForm"));
	}

	public final class ConfigurationBinarySearchForm extends Form<Void> {

		private static final long serialVersionUID = -6735358751053853608L;

		/**
		 * searchCondition.
		 */
		private SearchCondition searchCondition;

		/**
		 * categoryOption.
		 */
		// private SelectOption categoryOption;
		/**
		 * platformOption.
		 */
		// private SelectOption platformOption;
		/**
		 * statusOption.
		 */
		private SelectOption statusOption;

		/**
		 * compareConditionOption.
		 */
		// private SelectOption compareConditionOption;
		/**
		 * uploadDate.
		 */
		// private String uploadDate;
		private Date beginDate;
		private Date endDate;

		/**
		 * applicationService injection.
		 */
		@SpringBean
		private ApplicationService applicationService;

		/**
		 * categories.
		 */
		// private List<Category> categories;
		/**
		 * platforms.
		 */
		// private List<Platform> platforms;
		/**
		 * allStatus.
		 */
		private List<Status> allStatus;

		private void init() {
			// categories = applicationService.getAllCategory(0, Integer.MAX_VALUE);
			// platforms = applicationService.getAllPlatform(0, Integer.MAX_VALUE);
			allStatus = applicationService.getAllStatus();
			searchCondition = new SearchCondition();
		}

		public ConfigurationBinarySearchForm(String id) {
			super(id);
			init();

			TextField<String> keyword = new TextField<String>("keyword", new PropertyModel<String>(searchCondition, "keyword"));
			add(keyword);

			ChoiceRenderer<SelectOption> choiceRenderer = new ChoiceRenderer<SelectOption>("value", "key");
			//
			// DropDownChoice<SelectOption> category = new DropDownChoice<SelectOption>("category", new PropertyModel<SelectOption>(this, "categoryOption"),
			// new AbstractReadOnlyModel<List<SelectOption>>() {
			//
			// private static final long serialVersionUID = 1L;
			//
			// public List<SelectOption> getObject() {
			// List<SelectOption> selects = new ArrayList<SelectOption>();
			// for (Category o : categories) {
			// selects.add(new SelectOption(o.getId(), o.getName()));
			// }
			// return selects;
			// }
			// }, choiceRenderer);
			// add(category);

			// DropDownChoice<SelectOption> platform = new DropDownChoice<SelectOption>("platform", new PropertyModel<SelectOption>(this, "platformOption"),
			// new AbstractReadOnlyModel<List<SelectOption>>() {
			//
			// private static final long serialVersionUID = 1L;
			//
			// public List<SelectOption> getObject() {
			// List<SelectOption> selects = new ArrayList<SelectOption>();
			// for (Platform o : platforms) {
			// selects.add(new SelectOption(o.getId(), o.getName()));
			// }
			// return selects;
			// }
			// }, choiceRenderer);
			// add(platform);

			DropDownChoice<SelectOption> status = new DropDownChoice<SelectOption>("status", new PropertyModel<SelectOption>(this, "statusOption"), new AbstractReadOnlyModel<List<SelectOption>>() {

				private static final long serialVersionUID = 1L;

				public List<SelectOption> getObject() {
					List<SelectOption> selects = new ArrayList<SelectOption>();
					for (Status o : allStatus) {
						selects.add(new SelectOption(o.getId(), o.getStatus()));
					}
					return selects;
				}
			}, choiceRenderer);
			add(status);

			// add compareCondition drop down list.
			// final List<CompareCondition> allCompareCondition = SearchCondition.getAllCompareCondition();
			//
			// DropDownChoice<SelectOption> compareCondition = new DropDownChoice<SelectOption>("compareCondition", new PropertyModel<SelectOption>(this, "compareConditionOption"),
			// new AbstractReadOnlyModel<List<SelectOption>>() {
			//
			// private static final long serialVersionUID = 1L;
			//
			// public List<SelectOption> getObject() {
			// List<SelectOption> selects = new ArrayList<SelectOption>();
			// for (CompareCondition cc : allCompareCondition) {
			// selects.add(new SelectOption(new Long(cc.ordinal()), cc.toString()));
			// }
			// return selects;
			// }
			// }, choiceRenderer);
			// add(compareCondition);

			DateTextField beginDateField = new DateTextField("beginDate", new PropertyModel<Date>(this, "beginDate"), Constant.DATE_PATTERN);
			beginDateField.add(new DatePicker());
			add(beginDateField);

			DateTextField endDateField = new DateTextField("endDate", new PropertyModel<Date>(this, "endDate"), Constant.DATE_PATTERN);
			endDateField.add(new DatePicker());
			add(endDateField);

			Button submit = new Button("search");
			add(submit);
		}

		public final void onSubmit() {
			generateSearchCondition();
			log.debug("searchCondition: " + searchCondition.toString());

			// jump to next panel
			activate(new IBreadCrumbPanelFactory() {

				private static final long serialVersionUID = 1L;

				public BreadCrumbPanel create(String componentId, IBreadCrumbModel breadCrumbModel) {
					return new ConfigurationBinarySearchResultPanel(componentId, breadCrumbModel, searchCondition);
				}
			});
		}

		/**
		 * generate SearchCondition.
		 */
		private void generateSearchCondition() {

			// SearchCondition.AdvanceSearchCondition advanceSearchCondition = searchCondition.new AdvanceSearchCondition();
			// advanceSearchCondition.setUploadTime(uploadDate);
			//
			// if (compareConditionOption != null) {
			// advanceSearchCondition.setUploadTimeCompareCondition(EnumUtil.getEnumFieldByStr(CompareCondition.class, compareConditionOption.getValue()));
			// }
			//
			// searchCondition.setAdvanceSearchCondition(advanceSearchCondition);

			// if (categoryOption != null) {
			// searchCondition.setCategoryId(categoryOption.getKey());
			// }
			//
			// if (platformOption != null) {
			// searchCondition.setPlatformId(platformOption.getKey());
			// }

			if (statusOption != null) {
				searchCondition.setStatusId(statusOption.getKey());
			}

			searchCondition.setBeginDate(beginDate);
			searchCondition.setEndDate(endDate);
		}
	}

	public String getTitle() {
		return this.getLocalizer().getString("title", this, "Binary search");
	}

}
