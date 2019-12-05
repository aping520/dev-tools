package com.lz.framework.tools.utils;

import java.lang.reflect.Field;

/**
 * 枚举帮助类
 * @author zhoufei
 *
 */
public class EnumUtils {

	/**
	 * 根据枚举值获取枚举对象
	 * @param clazz
	 * @param value
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T valueOf(Class<T> clazz, String value) {
		if(clazz.isEnum()){
			for(Object obj : clazz.getEnumConstants()){
				try{
					Field field = obj.getClass().getDeclaredField("value");
					field.setAccessible(true);
					String enumValue = String.valueOf(field.get(obj));
					if(value.equals(enumValue)){
						return (T) obj;
					}
				} catch(Exception e){
					throw new RuntimeException("该枚举类型未定义自定义属性value.", e);
				}
			}
		}
		return null;
	}
}
