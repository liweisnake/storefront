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
package com.hp.sdf.ngp.ui.dynamicForm.parser;

import java.io.ByteArrayInputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.transform.stream.StreamSource;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.extensions.yui.calendar.DatePicker;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.AbstractTextComponent;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.RadioChoice;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.validation.validator.StringValidator;
import org.springframework.oxm.xstream.XStreamMarshaller;

import com.hp.sdf.ngp.common.exception.ObjectXMLTransformException;
import com.hp.sdf.ngp.ui.common.Constant;
import com.hp.sdf.ngp.ui.common.InputValidator;
import com.hp.sdf.ngp.ui.common.SelectOption;
import com.hp.sdf.ngp.ui.dynamicForm.DyFormConstants;
import com.hp.sdf.ngp.ui.dynamicForm.DynamicForm;
import com.hp.sdf.ngp.ui.dynamicForm.dataSource.AppServiceSource;
import com.hp.sdf.ngp.ui.dynamicForm.dataSource.ConstDefinedSource;
import com.hp.sdf.ngp.ui.dynamicForm.dataSource.GetIdListInterface;
import com.hp.sdf.ngp.ui.dynamicForm.dataSource.GetValueMapInterface;
import com.hp.sdf.ngp.ui.dynamicForm.dataSource.UserDefinedSource;
import com.hp.sdf.ngp.ui.dynamicForm.dataSource.VirtualSource;
import com.hp.sdf.ngp.ui.dynamicForm.model.DataField;
import com.hp.sdf.ngp.ui.dynamicForm.model.DyDataModel;
import com.hp.sdf.ngp.ui.dynamicForm.model.Pair;

@SuppressWarnings("unchecked")
public class FiledListParser implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1192467425364185993L;
	private final static Log log = LogFactory.getLog(FiledListParser.class);

	public FiledListParser() {
	}

	/************** read configuration file & un-marshal & parse entry point ***************************/

	/**
	 * get all domainField & eavField<br>
	 * 
	 * @param container
	 * @param dataModel
	 * @param temporaryValueMap
	 * @param fileContent
	 */
	public void parser(DyDataModel dyDataModel, MarkupContainer container, Object dataModel, Map<String, String> eavMap, Map<String, Object> temporaryValueMap) {

		DataField[] dfs = dyDataModel.getDataFields();

		for (DataField df : dfs) {

			if (!StringUtils.isEmpty(df.getSourcId())) {
				df.setDataSource(dyDataModel.getSource(df.getSourcId()));
			}

			if (DyFormConstants.COMPONENT_TEXTFIELD.equalsIgnoreCase(df.getCompnentType())) {
				genTextField(container, df, dataModel, eavMap);
			} else if (DyFormConstants.COMPONENT_DROPDOWNCHOICE.equalsIgnoreCase(df.getCompnentType())) {
				genDropDownChoice(container, df, temporaryValueMap);
			} else if (DyFormConstants.COMPONENT_RADIOCHOICE.equalsIgnoreCase(df.getCompnentType())) {
				genRadioChoice(container, df, temporaryValueMap);
			} else if (DyFormConstants.COMPONENT_TEXTAREA.equalsIgnoreCase(df.getCompnentType())) {
				genTextArea(container, df, dataModel, eavMap);
			} else {
				// TODO
			}

			// get label after filed generated
			if (null != dyDataModel.getLabled() && dyDataModel.getLabled()) {
				genLabel(container, df, dataModel, eavMap);
			}

		}
	}

	public DyDataModel unmarshal(String xml) throws ObjectXMLTransformException {
		XStreamMarshaller marshaller = new XStreamMarshaller();
		Map aliases = new HashMap();
		// mapping the class type with the user friendly name
		aliases.put("dataField", DataField.class);
		aliases.put("dyDataModel", DyDataModel.class);
		aliases.put("appServiceSource", AppServiceSource.class);
		aliases.put("virtualSource", VirtualSource.class);
		aliases.put("constDefinedSource", ConstDefinedSource.class);
		aliases.put("userDefinedSource", UserDefinedSource.class);
		aliases.put("pair", Pair.class);

		try {
			marshaller.setAliases(aliases);
			StreamSource streamSource = new StreamSource(new ByteArrayInputStream(xml.getBytes("UTF8")));
			DyDataModel dyDataModel = (DyDataModel) marshaller.unmarshal(streamSource);
			return dyDataModel;
		} catch (Throwable e) {
			log.error(e);
			throw new ObjectXMLTransformException("can't unmarshal the string[" + xml + "] to dyDataModel object", e);
		}
	}

	/************** common field Process method ***************************/

	private void setRadioChoiceField(DataField df, Map<String, Object> temporaryValueMap) {
		temporaryValueMap.put(df.getName(), 0L);
	}

	private void setSelectOptionField(DataField df, Map<String, Object> temporaryValueMap) {
		temporaryValueMap.put(df.getName(), new SelectOption(null, ""));
	}

	private void processEavField(MarkupContainer container, AbstractTextComponent<?> field, DataField df, Map<String, String> eavMap) {
		field.setDefaultModel(new PropertyModel<String>(eavMap, df.getName()) {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;
		});
		String value = ((DynamicForm) container).getAttributeValue(df.getName());
		if (!StringUtils.isEmpty(value))
			eavMap.put(df.getName(), value);
		else
			eavMap.put(df.getName(), new String());
	}

	private void processDomainField(AbstractTextComponent<?> field, DataField df, Object dataModel) {
		field.setDefaultModel(new PropertyModel<String>(dataModel, df.getName()));
	}

	private void checkDateField(DataField df, TextField<?> field) {
		if (DyFormConstants.DATATYPE_DATE.equalsIgnoreCase(df.getDataType())) {
			DatePicker dp = new DatePicker();
			field.add(dp);
		}
	}

	private void checkRequired(FormComponent<?> field, DataField df) {
		if (df.getRequired())
			field.setRequired(true);
	}

	private void checkMaxMin(DataField df, TextField<String> field) {

		if (DyFormConstants.DATATYPE_DATE.equalsIgnoreCase(df.getDataType())) {
			// TODO DATE max-min check needed?
			if (df.getMaxLength() != null) {
				// SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
				// field.add(DateValidator.maximum(sdf.parse(df.getMaxLength()),
				// "MM/dd/yyyy"));
			}

			if (df.getMinLength() != null) {
				// field.add(new
				// StringValidator.MinimumLengthValidator(df.getMinLength()));
			}
		} else {
			if (df.getMaxLength() != null) {
				field.add(new StringValidator.MaximumLengthValidator(Integer.valueOf(df.getMaxLength())));
			}

			if (df.getMinLength() != null) {
				field.add(new StringValidator.MinimumLengthValidator(Integer.valueOf(df.getMinLength())));
			}
		}
	}

	private void checkMaxMin(DataField df, TextArea<String> field) {
		if (df.getMaxLength() != null) {
			field.add(new StringValidator.MaximumLengthValidator(Integer.valueOf(df.getMaxLength())));
		}

		if (df.getMinLength() != null) {
			field.add(new StringValidator.MinimumLengthValidator(Integer.valueOf(df.getMinLength())));
		}
	}

	private void genImCheckValidator(MarkupContainer container, DataField df, AbstractTextComponent<String> field) {
		// IMCheck
		if (!StringUtils.isEmpty(df.getImCheck())) {
			if (df.getImCheck().equalsIgnoreCase("english")) {
				field.add(new InputValidator(com.hp.sdf.ngp.ui.common.Constant.VALID_TYPE.isEnglish));
			} else if (df.getImCheck().equalsIgnoreCase("number")) {
				field.add(new InputValidator(com.hp.sdf.ngp.ui.common.Constant.VALID_TYPE.isNumber));
			} else if (df.getImCheck().equalsIgnoreCase("all")) {
				field.add(new InputValidator(com.hp.sdf.ngp.ui.common.Constant.VALID_TYPE.isAll));
			}else if(df.getImCheck().equalsIgnoreCase("dbc")) {
				field.add(new InputValidator(com.hp.sdf.ngp.ui.common.Constant.VALID_TYPE.isDbc));
			}
		}
	}

	/************** TextField ***************************/

	private void genTextField(MarkupContainer container, DataField df, Object dataModel, Map<String, String> eavMap) {
		TextField<String> field = new TextField<String>(df.getName());
		container.add(field);
		if (DyFormConstants.MODELTYPE_EAV.equalsIgnoreCase(df.getModelType())) {
			// EAV field
			processEavField(container, field, df, eavMap);
		} else if (DyFormConstants.MODELTYPE_DOMAIN.equalsIgnoreCase(df.getModelType())) {
			// domain field
			processDomainField(field, df, dataModel);
		} else {
			log.error("Wrong modelType definition of field:" + df.getName());
		}

		checkDateField(df, field);
		checkRequired(field, df);
		checkMaxMin(df, field);
		genImCheckValidator(container, df, field);
	}

	/************** Label ***************************/

	private void genLabel(MarkupContainer container, DataField df, Object dataModel, Map<String, String> eavMap) {

		if (DyFormConstants.MODELTYPE_EAV.equalsIgnoreCase(df.getModelType())) {
			// EAV field
			Label label_ = new Label("label." + df.getName(), new PropertyModel<String>(eavMap, df.getName()));
			container.add(label_);
		} else if (DyFormConstants.MODELTYPE_DOMAIN.equalsIgnoreCase(df.getModelType())) {
			// domain field
			Label label_ = new Label("label." + df.getName(), new PropertyModel<String>(dataModel, df.getName()));
			container.add(label_);
		} else {
			log.error("Wrong modelType definition of field:" + df.getName());
		}
	}

	private String getFieldValue(Object dataHolder, String fieldName) {

		if (dataHolder instanceof Map) {
			return ((Map) dataHolder).containsKey(fieldName) ? String.valueOf(((Map) dataHolder).get(fieldName)) : null;
		} else {
			// TODO reflect get
			Field[] fields = dataHolder.getClass().getDeclaredFields();
			String fieldValue = "";
			for (Field field : fields) {
				if (fieldName.equalsIgnoreCase(field.getName())) {
					field.setAccessible(true);
					try {
						fieldValue = (String) field.get(dataHolder);
					} catch (IllegalArgumentException exception) {
						exception.printStackTrace();
					} catch (IllegalAccessException exception) {
						exception.printStackTrace();
					}
				}
			}
			return fieldValue;
		}
	}

	/************** DropDownChoice ***************************/

	/**
	 * put filed value like dropDownChoice to temporaryValueMap
	 * 
	 * @param container
	 * @param df
	 * @param temporaryValueMap
	 */

	private void genDropDownChoice(MarkupContainer container, final DataField df, Map<String, Object> temporaryValueMap) {

		ChoiceRenderer<String> choiceRenderer = new ChoiceRenderer<String>(Constant.SELECT_OPTION.VALUE.toString(), Constant.SELECT_OPTION.KEY.toString());

		final List dataList = df.getDataSource().getDataList();

		setSelectOptionField(df, temporaryValueMap);

		DropDownChoice dropDownChoice = new DropDownChoice(df.getName(), new PropertyModel<SelectOption>(temporaryValueMap, df.getName()), new AbstractReadOnlyModel<List<SelectOption>>() {

			private static final long serialVersionUID = 7653635619701254501L;

			public List<SelectOption> getObject() {
				List<SelectOption> selects = new ArrayList<SelectOption>();
				for (Object o : dataList) {
					try {
						Method keyMethod = o.getClass().getMethod("get" + StringUtils.capitalize(df.getDataSource().getKeyName()), new Class[0]);
						Method valueMethod = o.getClass().getMethod("get" + StringUtils.capitalize(df.getDataSource().getValueName()), new Class[0]);
						Object keyObject = keyMethod.invoke(o, new Object[0]);
						Object valueObject = valueMethod.invoke(o, new Object[0]);
						selects.add(new SelectOption((Long) keyObject, String.valueOf(valueObject)));
					} catch (Exception ex) {
						log.error(ex);
					}
				}
				return selects;
			}
		}, choiceRenderer);

		checkRequired(dropDownChoice, df);

		container.add(dropDownChoice);
	}

	/************** RadioChoice ***************************/
	/**
	 * put filed value like dropDownChoice to temporaryValueMap
	 * 
	 * @param container
	 * @param df
	 * @param temporaryValueMap
	 */
	private void genRadioChoice(MarkupContainer container, final DataField df, Map<String, Object> temporaryValueMap) {

		final List idList = (List) (((GetIdListInterface) df.getDataSource()).getIdList());

		final Map valueList = (Map) (((GetValueMapInterface) df.getDataSource()).getValueMap());

		setRadioChoiceField(df, temporaryValueMap);

		RadioChoice radioChoice = new RadioChoice(df.getName(), new PropertyModel(temporaryValueMap, df.getName()), idList, new ChoiceRenderer() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 5486007321993653335L;

			public Object getDisplayValue(Object object) {
				return valueList.get(object);
			}
		});

		checkRequired(radioChoice, df);

		container.add(radioChoice);
	}

	/************** TextArea ***************************/
	private void genTextArea(MarkupContainer container, DataField df, Object dataModel, Map<String, String> eavMap) {
		TextArea<String> field = new TextArea(df.getName());

		container.add(field);
		if (DyFormConstants.MODELTYPE_EAV.equalsIgnoreCase(df.getModelType())) {
			// EAV field
			processEavField(container, field, df, eavMap);
		} else if (DyFormConstants.MODELTYPE_DOMAIN.equalsIgnoreCase(df.getModelType())) {
			// domain field
			processDomainField(field, df, dataModel);
		} else {
			log.error("Wrong modelType definition of field:" + df.getName());
		}

		checkRequired(field, df);
		checkMaxMin(df, field);
		genImCheckValidator(container, df, field);
	}

}

// $Id$