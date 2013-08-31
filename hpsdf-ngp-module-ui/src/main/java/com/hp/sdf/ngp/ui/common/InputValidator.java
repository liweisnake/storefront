package com.hp.sdf.ngp.ui.common;

import java.util.Map;

import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.validator.AbstractValidator;

import com.hp.sdf.ngp.common.util.StringUtil;
import com.hp.sdf.ngp.ui.common.Constant.VALID_TYPE;

public class InputValidator extends AbstractValidator<String> {
	private static final long serialVersionUID = 3994340161713152898L;

	private VALID_TYPE valid_type;

	// private String errorMsg;

	// public InputValidator(VALID_TYPE valid_type, String errorMsg) {
	// this.valid_type = valid_type;
	// this.errorMsg = errorMsg;
	// }

	public InputValidator(VALID_TYPE valid_type) {
		this.valid_type = valid_type;
	}

	@Override
	protected void onValidate(IValidatable<String> validatable) {

		String value = validatable.getValue();
		if (Tools.isHfsz(value)) {
			switch (valid_type) {
			case isNumber:
				if (StringUtil.isAllNumber(value))
					return;
				break;
			case isEnglish:
				if (StringUtil.isAllEnglish(value))
					return;
				break;
			case isAll:
				if (StringUtil.isAllEnglishAndNumber(value))
					return;
				break;
			default:
				return;
			}
		}

		error(validatable);
	}

	@Override
	protected String resourceKey() {
		switch (valid_type) {
		case isNumber:
			return "NumberValidator";
		case isEnglish:
			return "EnglishValidator";
		case isAll:
			return "AllValidator";
		default:
			return "DbcValidator";
		}
	}

	@Override
	protected Map<String, Object> variablesMap(IValidatable<String> validatable) {
		Map<String, Object> map = super.variablesMap(validatable);
		// map.put("content", errorMsg);
		return map;
	}

	public VALID_TYPE getValid_type() {
		return valid_type;
	}

	public void setValid_type(VALID_TYPE valid_type) {
		this.valid_type = valid_type;
	}

	// public String getErrorMsg() {
	// return errorMsg;
	// }
	//
	// public void setErrorMsg(String errorMsg) {
	// this.errorMsg = errorMsg;
	// }
}
