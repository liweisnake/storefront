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
package com.hp.sdf.ngp.ui.common;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.extensions.yui.calendar.DatePicker;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Radio;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.PropertyModel;

import com.hp.sdf.ngp.service.ApplicationService;
import com.hp.sdf.ngp.ui.common.Constant.ENABLE;

/**
 * @author zzhen
 */
public class GenerateControls implements Serializable {

	private static final long serialVersionUID = -8710309187559164842L;

	private static final Log log = LogFactory.getLog(GenerateControls.class);

	private ApplicationService applicationService;

	private WebMarkupContainer defineWebMarkupContainer;

	private WebMarkupContainer parentWebMarkupContainer;

	public GenerateControls(WebMarkupContainer webMarkupContainer, ApplicationService applicationService) {
		this.defineWebMarkupContainer = webMarkupContainer;
		this.parentWebMarkupContainer = webMarkupContainer;
		this.applicationService = applicationService;
	}

	public GenerateControls(WebMarkupContainer webMarkupContainer) {
		this.defineWebMarkupContainer = webMarkupContainer;
		this.parentWebMarkupContainer = webMarkupContainer;
	}

	public GenerateControls(WebMarkupContainer defineWebMarkupContainer, WebMarkupContainer parentWebMarkupContainer, ApplicationService applicationService) {
		this.defineWebMarkupContainer = defineWebMarkupContainer;
		this.parentWebMarkupContainer = parentWebMarkupContainer;
		this.applicationService = applicationService;
	}

	@SuppressWarnings("unchecked")
	public void Generate() {
		Field[] fields = this.defineWebMarkupContainer.getClass().getDeclaredFields();
		for (Field field : fields) {
			Generate generate = field.getAnnotation(Generate.class);
			if (null == generate)
				continue;

			String id = StringUtils.isEmpty(generate.id()) ? field.getName() : generate.id();
			switch (generate.controlType()) {
			case CheckBox:
				CheckBox(id, this.defineWebMarkupContainer, field.getName(), generate.setMarkupId(), generate.setEnable().toString());
				break;
			case DropDownChoice:
				DropDownChoiceSelectOption(id, this.defineWebMarkupContainer, field.getName(), generate.setMarkupId(), getAll(generate.dataType()), generate.key(), generate.value());
				break;
			case TextArea:
				TextAreaString(id, this.defineWebMarkupContainer, field.getName(), generate.setMarkupId());
				break;
			case Radio:
				RadioInteger(id, this.defineWebMarkupContainer, field.getName(), generate.setMarkupId());
				break;
			default:
				if (field.getType().getName().equals(Long.class.getName())) {
					TextFieldLong(id, this.defineWebMarkupContainer, field.getName(), generate.setMarkupId());
				} else if (field.getType().getName().equals(Double.class.getName())) {
					TextFieldDouble(id, this.defineWebMarkupContainer, field.getName(), generate.setMarkupId());
				} else if (field.getType().getName().equals(Integer.class.getName())) {
					TextFieldInteger(id, this.defineWebMarkupContainer, field.getName(), generate.setMarkupId());
				} else if (field.getType().getName().equals(Date.class.getName())) {
					TextFieldDate(id, this.defineWebMarkupContainer, field.getName(), generate.setMarkupId());
				} else if (field.getType().getName().equals(String.class.getName())) {
					TextFieldString(id, this.defineWebMarkupContainer, field.getName(), generate.setMarkupId());
				}
				break;
			}
		}
	}

	public void ClearControlsValue() {
		Field[] fields = this.defineWebMarkupContainer.getClass().getDeclaredFields();
		for (Field field : fields) {
			Generate generate = field.getAnnotation(Generate.class);
			if (null == generate)
				continue;

			field.setAccessible(true);
			try {
				if (generate.setEnable().toString().equals(ENABLE.enable.toString())) {
					if (field.getType().getName().equals(Boolean.class.getName())) {
						field.set(this.defineWebMarkupContainer, false);
					} else {
						field.set(this.defineWebMarkupContainer, null);
					}
				}
			} catch (Exception ex) {
				log.error(ex.getMessage());
			}
		}
	}

	public String getControlsValue() {
		Field[] fields = this.defineWebMarkupContainer.getClass().getDeclaredFields();
		StringBuilder sb = new StringBuilder();
		for (Field field : fields) {
			Generate generate = field.getAnnotation(Generate.class);
			if (null == generate)
				continue;

			field.setAccessible(true);
			try {
				if (field.getType().getName().equals(Boolean.class.getName())) {
					sb.append(field.getName() + " : " + field.get(this.defineWebMarkupContainer) + ";\n");
				} else if (field.getType().getName().equals(Double.class.getName())) {
					sb.append(field.getName() + " : " + String.valueOf(field.get(this.defineWebMarkupContainer)) + ";\n");
				} else if (field.getType().getName().equals(SelectOption.class.getName())) {
					Object object = field.get(this.defineWebMarkupContainer);
					if (null == object)
						sb.append(field.getName() + " : null;\n");
					else
						sb.append(field.getName() + " : key:" + ((SelectOption) object).getKey() + "; value:" + ((SelectOption) object).getValue() + ";\n");
				} else {
					sb.append(field.getName() + " : " + String.valueOf(field.get(this.defineWebMarkupContainer)) + ";\n");
				}
			} catch (Exception ex) {
				sb.append(field.getName() + " : error;\n");
			}
		}
		return sb.toString();
	}

	/**
	 * Radio<Integer>
	 * 
	 * @param id
	 * @param modelObject
	 * @param bindVariableName
	 * @param markupId
	 */
	public void RadioInteger(String id, Object modelObject, String bindVariableName, String markupId) {
		Radio<Integer> radio = new Radio<Integer>(id, new PropertyModel<Integer>(modelObject, bindVariableName));
		if (StringUtils.isNotBlank(markupId))
			radio.setMarkupId(markupId);
		this.parentWebMarkupContainer.add(radio);
	}

	/**
	 * CheckBox
	 * 
	 * @param id
	 * @param modelObject
	 * @param bindVariableName
	 * @param markupId
	 */
	public void CheckBox(String id, Object modelObject, String bindVariableName, String markupId, String enable) {
		CheckBox checkBox = new CheckBox(id, new PropertyModel<Boolean>(modelObject, bindVariableName));
		if (StringUtils.isNotBlank(markupId))
			checkBox.setMarkupId(markupId);
		if (!enable.equals(ENABLE.enable.toString()))
			checkBox.setEnabled(false);
		this.parentWebMarkupContainer.add(checkBox);
	}

	/**
	 * TextField<Long>
	 * 
	 * @param id
	 * @param modelObject
	 * @param bindVariableName
	 * @param markupId
	 */
	public void TextFieldLong(String id, Object modelObject, String bindVariableName, String markupId) {
		TextField<Long> textField = new TextField<Long>(id, new PropertyModel<Long>(modelObject, bindVariableName));
		if (StringUtils.isNotBlank(markupId))
			textField.setMarkupId(markupId);
		this.parentWebMarkupContainer.add(textField);
	}

	/**
	 * TextField<String>
	 * 
	 * @param id
	 * @param modelObject
	 * @param bindVariableName
	 * @param markupId
	 */
	public void TextFieldString(String id, Object modelObject, String bindVariableName, String markupId) {
		TextField<String> textField = new TextField<String>(id, new PropertyModel<String>(modelObject, bindVariableName));
		if (StringUtils.isNotBlank(markupId))
			textField.setMarkupId(markupId);
		this.parentWebMarkupContainer.add(textField);
	}

	/**
	 * TextField<Integer>
	 * 
	 * @param id
	 * @param modelObject
	 * @param bindVariableName
	 * @param markupId
	 */
	public void TextFieldInteger(String id, Object modelObject, String bindVariableName, String markupId) {
		TextField<Integer> textField = new TextField<Integer>(id, new PropertyModel<Integer>(modelObject, bindVariableName));
		if (StringUtils.isNotBlank(markupId))
			textField.setMarkupId(markupId);
		this.parentWebMarkupContainer.add(textField);
	}

	/**
	 * TextField<Double>
	 * 
	 * @param id
	 * @param modelObject
	 * @param bindVariableName
	 * @param markupId
	 */
	public void TextFieldDouble(String id, Object modelObject, String bindVariableName, String markupId) {
		TextField<Double> textField = new TextField<Double>(id, new PropertyModel<Double>(modelObject, bindVariableName));
		if (StringUtils.isNotBlank(markupId))
			textField.setMarkupId(markupId);
		this.parentWebMarkupContainer.add(textField);
	}

	/**
	 * DateTextField
	 * 
	 * @param id
	 * @param modelObject
	 * @param bindVariableName
	 * @param markupId
	 */
	public void TextFieldDate(String id, Object modelObject, String bindVariableName, String markupId) {
		CustomizeDateTextField dateTextField = new CustomizeDateTextField(id, new PropertyModel<Date>(modelObject, bindVariableName), Constant.DATE_PATTERN);
		dateTextField.add(new DatePicker());
		this.parentWebMarkupContainer.add(dateTextField);
	}

	/**
	 * TextArea<String>
	 * 
	 * @param id
	 * @param modelObject
	 * @param bindVariableName
	 * @param markupId
	 */
	public void TextAreaString(String id, Object modelObject, String bindVariableName, String markupId) {
		TextArea<String> textArea = new TextArea<String>(id, new PropertyModel<String>(modelObject, bindVariableName));
		if (StringUtils.isNotBlank(markupId))
			textArea.setMarkupId(markupId);
		this.parentWebMarkupContainer.add(textArea);
	}

	/**
	 * DropDownChoice<SelectOption>
	 * 
	 * @param id
	 * @param modelObject
	 * @param bindVariableName
	 * @param markupId
	 * @param list
	 * @param key
	 * @param value
	 */
	public void DropDownChoiceSelectOption(String id, Object modelObject, String bindVariableName, String markupId, final List<Object> list, final String key, final String value) {
		ChoiceRenderer<SelectOption> choiceRenderer = new ChoiceRenderer<SelectOption>(Constant.SELECT_OPTION.VALUE.toString(), Constant.SELECT_OPTION.KEY.toString());
		DropDownChoice<SelectOption> dropDownChoice = new DropDownChoice<SelectOption>(id, new PropertyModel<SelectOption>(modelObject, bindVariableName), new AbstractReadOnlyModel<List<SelectOption>>() {

			private static final long serialVersionUID = 7653635619701254501L;

			public List<SelectOption> getObject() {
				List<SelectOption> selects = new ArrayList<SelectOption>();
				for (Object o : list) {
					try {
						Method keyMethod = o.getClass().getMethod("get" + StringUtils.capitalize(key), new Class[0]);
						Method valueMethod = o.getClass().getMethod("get" + StringUtils.capitalize(value), new Class[0]);
						Object keyObject = keyMethod.invoke(o, new Object[0]);
						Object valueObject = valueMethod.invoke(o, new Object[0]);
						selects.add(new SelectOption((Long) keyObject, String.valueOf(valueObject)));
					} catch (Exception ex) {
						log.error(ex.getMessage());
					}
				}
				return selects;
			}
		}, choiceRenderer);
		this.parentWebMarkupContainer.add(dropDownChoice);
	}

	/**
	 * get all
	 * 
	 * @param dataType
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List getAll(Constant.DATA_TYPE dataType) {
		switch (dataType) {
		case status:
			return applicationService.getAllStatus();
		case platform:
			return applicationService.getAllPlatform(0, Integer.MAX_VALUE);
		case category:
			return applicationService.getAllCategory(0, Integer.MAX_VALUE);
		default:
			return null;
		}
	}

	public ApplicationService getApplicationService() {
		return applicationService;
	}

	public void setApplicationService(ApplicationService applicationService) {
		this.applicationService = applicationService;
	}

	public WebMarkupContainer getDefineWebMarkupContainer() {
		return defineWebMarkupContainer;
	}

	public void setDefineWebMarkupContainer(WebMarkupContainer defineWebMarkupContainer) {
		this.defineWebMarkupContainer = defineWebMarkupContainer;
	}

	public WebMarkupContainer getParentWebMarkupContainer() {
		return parentWebMarkupContainer;
	}

	public void setParentWebMarkupContainer(WebMarkupContainer parentWebMarkupContainer) {
		this.parentWebMarkupContainer = parentWebMarkupContainer;
	}
}
