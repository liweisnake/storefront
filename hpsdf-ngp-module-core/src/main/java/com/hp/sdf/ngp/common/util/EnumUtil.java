/**
 * 
 */
package com.hp.sdf.ngp.common.util;

import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;

import com.hp.sdf.ngp.api.search.OrderBy;
import com.hp.sdf.ngp.api.search.orderby.AssetOrderBy;

/**
 * @author wli12
 * 
 */
public class EnumUtil {

	private static Logger log = Logger.getLogger(EnumUtil.class);

	// public static <T extends Enum<T>> T fromEnumProperty(Class<T> enumClass,
	// String property, Object propValue) {
	//
	// T[] enumConstants = enumClass.getEnumConstants();
	//
	// for (T t : enumConstants) {
	// Object constantPropValue;
	// try {
	// constantPropValue = BeanUtils
	// .getDeclaredFieldValue(t, property);
	//
	// if (ObjectUtils.equals(constantPropValue, propValue))
	// return t;
	// } catch (Exception e) {
	// throw new RuntimeException(e);
	// }
	// }
	// return null;
	//
	// }

	public static <T extends Enum<T>> List<T> getAllEnumField(Class<T> enumClass) {
		try {

			T[] enumConstants = enumClass.getEnumConstants();

			return Arrays.asList(enumConstants);
		} catch (Exception e) {
			log.error(e);
		}
		return null;
	}

	public static <T extends Enum<T>> T getEnumFieldByStr(Class<T> enumClass,
			String fieldString) {
		try {
			// Class<T> enumClass = ClassUtils.getClass(enumClassName);

			T[] enumConstants = enumClass.getEnumConstants();

			for (T t : enumConstants) {
				if (t.toString().endsWith(fieldString))
					return t;
			}
		} catch (Exception e) {
			log.error(e);
		}
		return null;

	}

	public static <T extends Enum<T>> T getEnumFieldByName(Class<T> enumClass,
			String fieldString) {
		try {
			// Class<T> enumClass = ClassUtils.getClass(enumClassName);

			T[] enumConstants = enumClass.getEnumConstants();

			for (T t : enumConstants) {
				if (t.name().equals(fieldString))
					return t;
			}
		} catch (Exception e) {
			log.error(e);
		}
		return null;

	}

	public static <T extends Enum<T>> String getEnumNameStrByOrdinal(Class<T> enumClass, int ordinal) {
		try {
			T[] enumConstants = enumClass.getEnumConstants();

			for (T t : enumConstants) {
				if (t.ordinal() == ordinal)
					return t.name();
			}
		} catch (Exception e) {
			log.error(e);
		}
		return null;
	}

	public static void main(String[] args) {
		OrderBy c = AssetOrderBy.NAME;
		EnumUtil.getEnumFieldByName(((Enum) c).getClass(), "NAME");
	}

	// public static <T extends Enum<T>> T fromEnumConstantName(
	// Class<T> enumClass, String constantName) {
	// T[] enumConstants = enumClass.getEnumConstants();
	// for (T t : enumConstants) {
	// if (((Enum<?>) t).name().equals(constantName))
	// return t;
	// }
	// return null;
	// }

}
