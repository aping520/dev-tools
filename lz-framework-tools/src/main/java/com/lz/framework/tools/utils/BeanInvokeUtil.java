package com.lz.framework.tools.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.util.Date;

/**
 * 对象操作类
 * 
 * @author zhoufei
 * @version createTime：2019年8月26日 上午11:49:03
 *
 */
public class BeanInvokeUtil {

	// 分隔字符串
	private static final String SPLIT_CHAR = "\\.";

	// 方法前缀
	private static final String METHOD_PREFIX = "get";

	public static Object invokeMethodMain(Object obj, String methodName) throws Exception {
		Class<?> ownerClass = obj.getClass();
		methodName = methodName.substring(0, 1).toUpperCase() + methodName.substring(1);

		Method method = null;

		try {
			method = ownerClass.getMethod(METHOD_PREFIX + methodName);
		} catch (SecurityException e) {
		} catch (NoSuchMethodException e) {
			return null;
		}
		return method.invoke(obj);
	}

	public static Object invokeMethod(Object obj, String methodName) throws Exception {
		if (methodName.indexOf(".") != -1) {
			String[] part = methodName.split(SPLIT_CHAR);
			int i = 0;
			while (i < part.length) {
				String method = part[i].substring(0, 1).toUpperCase() + part[i].substring(1);
				obj = invokeMethodMain(obj, method);
				if (StringUtil.isEmpty(obj)) {
					break;
				}
				i++;
			}
		} else {
			obj = invokeMethodMain(obj, methodName);
		}
		return obj;
	}

	static class CompareEntry {
		/**
		 * 结果
		 */
		private Boolean result;
		/**
		 * 命中字段
		 */
		private String field;

		public Boolean getResult() {
			return result;
		}

		public void setResult(Boolean result) {
			this.result = result;
		}

		public String getField() {
			return field;
		}

		public void setField(String field) {
			this.field = field;
		}
	}

	@SuppressWarnings("rawtypes")
	public static Boolean compare(Object srcObj, Object destObj, Class annotations) {
		CompareEntry compareEntry = new CompareEntry();
		compareEntry.setResult(true);
		cascadeCompare(srcObj, destObj, compareEntry, annotations);

		return compareEntry.getResult();
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static boolean cascadeCompare(Object srcObj, Object destObj, CompareEntry compareEntry, Class annotation) {
		Field[] srcFields = srcObj.getClass().getDeclaredFields();
		if (srcFields != null && srcFields.length != 0) {
			for (Field field : srcFields) {
				if ((annotation == null || field.getAnnotation(annotation) != null) && compareEntry.getResult()) {
					field.setAccessible(true);
					String fieldName = field.getName();

					Object srcValue = null;
					try {
						srcValue = field.get(srcObj);
					} catch (Exception e1) {
						e1.printStackTrace();
					}

					Object destValue = null;
					try {
						destValue = invokeMethod(destObj, fieldName);
					} catch (Exception e) {
						e.printStackTrace();
					}

					if ((srcValue == null && destValue != null) || (srcValue != null && destValue == null)) {
						compareEntry.setResult(false);
						compareEntry.setField(fieldName);
						return false;
					}

					if (field.getType().getSimpleName().equals("String")) {
						if (!((StringUtil.isEmpty(srcValue) && StringUtil.isEmpty(destValue))
								|| srcValue.equals(destValue))) {
							compareEntry.setResult(false);
							compareEntry.setField(fieldName);
							break;
						}
					} else if (field.getType().getSimpleName().equals("Boolean")) {
						if (!((srcValue == null && destValue == null) || (Boolean) srcValue == (Boolean) destValue)) {
							compareEntry.setResult(false);
							compareEntry.setField(fieldName);
							break;
						}
					} else if (field.getType().getSimpleName().equals("Integer")) {
						if (!((srcValue == null && destValue == null)
								|| ((Integer) srcValue).intValue() == ((Integer) destValue).intValue())) {
							compareEntry.setResult(false);
							compareEntry.setField(fieldName);
							break;
						}
					} else if (field.getType().getSimpleName().equals("Double")) {
						if (!((srcValue == null && destValue == null)
								|| ((Double) srcValue).doubleValue() == ((Double) destValue).doubleValue())) {
							compareEntry.setResult(false);
							compareEntry.setField(fieldName);
							break;
						}
					} else if (field.getType().getSimpleName().equals("Float")) {
						if (!((srcValue == null && destValue == null)
								|| ((Float) srcValue).floatValue() == ((Float) destValue).floatValue())) {
							compareEntry.setResult(false);
							compareEntry.setField(fieldName);
							break;
						}
					} else if (field.getType().getSimpleName().equals("Date")) {
						if (!((srcValue == null && destValue == null)
								|| ((Date) srcValue).getTime() == ((Date) destValue).getTime())) {
							compareEntry.setResult(false);
							compareEntry.setField(fieldName);
							break;
						}
					} else if (field.getType().getSimpleName().equals("Timestamp")) {
						if (!((srcValue == null && destValue == null)
								|| ((Timestamp) srcValue).getTime() == ((Timestamp) destValue).getTime())) {
							compareEntry.setResult(false);
							compareEntry.setField(fieldName);
							break;
						}
					} else {
						cascadeCompare(srcValue, destValue, compareEntry, annotation);
					}
				}
			}
		}
		return true;
	}
}
