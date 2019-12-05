package com.lz.framework.tools.convert;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.beanutils.Converter;

import com.lz.framework.tools.utils.StringUtil;

public class BeanUtilsDateConverter implements Converter {
	
	SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	@SuppressWarnings("unchecked")
	public <T> T convert(Class<T> myClass, Object myObj) {
		if (StringUtil.isEmpty(myObj))
			return null;
		if(myClass.getSimpleName().equals("String")){
			if(myObj instanceof Date){
				return (T) format.format(myObj);
			}
		}
		if(myClass.getSimpleName().equals("Long")){
			if(myObj instanceof Date){
				return (T) String.valueOf(((Date) myObj).getTime());
			}
		}
		return (T) myObj;
	}
}