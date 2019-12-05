package com.lz.framework.tools.utils;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.ConvertUtils;
import org.joda.time.DateTime;

import com.lz.framework.tools.constants.Constants;
import com.lz.framework.tools.convert.BeanUtilsDateParser;
import com.lz.framework.tools.convert.TypeConvertFactory;
import com.lz.framework.tools.enums.ExcludeModeEnum;
import com.lz.framework.tools.exception.BusinessException;

/**
 * 字符串帮助类
 *
 */
public class StringUtil {

	public static boolean isEmpty(Object pObj) {
		if (pObj == null) {
			return true;
		}
		if (pObj == "") {
			return true;
		}
		if ((pObj instanceof String)) {
			if (((String) pObj).length() == 0) {
				return true;
			}
		} else if ((pObj instanceof Collection)) {
			if (((Collection<?>) pObj).isEmpty()) {
				return true;
			}
		} else if (pObj instanceof Map && (((Map<?, ?>) pObj).size() == 0)) {
			return true;
		}

		return false;
	}

	public static boolean isNotEmpty(Object pObj) {
		if (pObj == null) {
			return false;
		}
		if (pObj == "") {
			return false;
		}
		if ((pObj instanceof String)) {
			if (((String) pObj).length() == 0) {
				return false;
			}
		} else if ((pObj instanceof Collection)) {
			if (((Collection<?>) pObj).isEmpty()) {
				return false;
			}
		} else if (pObj instanceof Map && (((Map<?, ?>) pObj).size() == 0)) {
			return false;
		}

		return true;
	}

	public static void copyProperties(Object pFromObj, Object pToObj, String... params) {
		copyProperties(pFromObj, pToObj, ExcludeModeEnum.INCLUDE, params);
	}

	public static void copyProperties(Object pFromObj, Object pToObj, ExcludeModeEnum updateMode, String... params) {
		if (pToObj != null) {
			Field[] fields = pToObj.getClass().getDeclaredFields();
			for (Field field : fields) {
				String fieldName = field.getName();
				String fieldType = field.getType().getSimpleName();
				if (((updateMode == null || updateMode == ExcludeModeEnum.INCLUDE) && contains(params, fieldName))
						|| (updateMode != null && updateMode == ExcludeModeEnum.EXCLUDE
								&& !contains(params, fieldName))) {
					field.setAccessible(true);

					try {
						Field sourceField = pFromObj.getClass().getDeclaredField(fieldName);
						String sourceFieldType = sourceField.getType().getSimpleName();
						sourceField.setAccessible(true);

						Object value = sourceField.get(pFromObj);
						if (value != null) {
							if (!fieldType.equals(sourceFieldType)) {
								if (fieldType.equals("String")) {
									if (sourceFieldType.equals("Date")) {
										field.set(pToObj, new DateTime(value).toString(Constants.DATE_FORMAT.DATE_FORMAT0));
									} else {
										field.set(pToObj, TypeConvertFactory.convert(value, "String", null));
									}
								}
								if (fieldType.equals("Integer")) {
									field.set(pToObj, TypeConvertFactory.convert(value, "Integer", null));
								}
								if (fieldType.equals("Long")) {
									if (sourceFieldType.equals("Date")) {
										field.set(pToObj, ((Date) value).getTime());
									} else {
										field.set(pToObj, Long.parseLong(String.valueOf(sourceField.get(pFromObj))));
									}
								}
								if (fieldType.equals("Date")) {
									if (sourceFieldType.equals("Long")) {
										field.set(pToObj, new Date((long) value));
									}
									if (sourceFieldType.equals("String")) {
										field.set(pToObj, DateTime.parse(String.valueOf(value)));
									}
								}
							} else {
								field.set(pToObj, sourceField.get(pFromObj));
							}
						} else {
							field.set(pToObj, null);
						}
					} catch (Exception e) {
						continue;
					}
				}
			}
		}
	}

	public static void copyProperties(Object pFromObj, Object pToObj) {
		copyProperties(pFromObj, pToObj, ExcludeModeEnum.EXCLUDE, "");
	}

	public static void copyProperties(Object pFromObj, Map<String, Object> pToDto) {
		if (pToDto != null)
			try {
				pToDto.putAll(BeanUtils.describe(pFromObj));
				pToDto.remove("class");
			} catch (Exception e) {
				throw new BusinessException("将JavaBean属性值拷贝到Dto对象发生错误", e);
			}
	}

	public static void copyProperties(Map<String, Object> pFromMap, Object pToobj) {
		if (pFromMap != null) {
			try {
				BeanUtilsBean.getInstance().getConvertUtils().register(false, true, 0);

				BeanUtilsDateParser converter = new BeanUtilsDateParser();
				ConvertUtils.register(converter, java.util.Date.class);
				ConvertUtils.register(converter, java.sql.Date.class);
				BeanUtils.populate(pToobj, pFromMap);
			} catch (Exception e) {
				throw new BusinessException("将Map<String, Object>属性值拷贝到对象发生错误", e);
			}
		}
	}

	public static boolean contains(String[] array, String key) {
		if ((isNotEmpty(array)) && (isNotEmpty(key))) {
			for (String str : array) {
				if (key.equals(str)) {
					return true;
				}
			}
		}
		return false;
	}

	public static String capitalize(String str) {
		int strLen;
		if ((str == null) || ((strLen = str.length()) == 0)) {
			return str;
		}
		int firstCodepoint = str.codePointAt(0);
		int newCodePoint = Character.toTitleCase(firstCodepoint);
		if (firstCodepoint == newCodePoint) {
			return str;
		}

		int[] newCodePoints = new int[strLen];
		int outOffset = 0;
		newCodePoints[(outOffset++)] = newCodePoint;
		for (int inOffset = Character.charCount(firstCodepoint); inOffset < strLen;) {
			int codepoint = str.codePointAt(inOffset);
			newCodePoints[(outOffset++)] = codepoint;
			inOffset += Character.charCount(codepoint);
		}
		return new String(newCodePoints, 0, outOffset);
	}

	/**
	 * 正则匹配
	 * 
	 * @param regex
	 * @param str
	 * @return
	 */
	public static boolean match(String regex, String str) {
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(str);
		return matcher.matches();
	}

	public static String getString(byte[] bytes) {
		try {
			return new String(bytes, Constants.CHARSET.UTF_8);
		} catch (UnsupportedEncodingException e) {

		}
		return null;
	}

	public static byte[] getBytes(String str) {
		if(isEmpty(str)) {
			return null;
		}
		
		try {
			return str.getBytes(Constants.CHARSET.UTF_8);
		} catch (UnsupportedEncodingException e) {

		}
		return new byte[] {};
	}

	public static String mask(String str, int begin, int end) {
		return str.substring(0, begin) + str.substring(begin, end).replaceAll(Constants.CHAR.MASK, Constants.CHAR.MASK_CHAR)
				+ str.substring(end, str.length());
	}
}
