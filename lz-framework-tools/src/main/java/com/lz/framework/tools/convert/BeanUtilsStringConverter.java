package com.lz.framework.tools.convert;

import org.apache.commons.beanutils.Converter;

import com.lz.framework.tools.utils.StringUtil;

public class BeanUtilsStringConverter implements Converter {

	@SuppressWarnings("unchecked")
	public <T> T convert(Class<T> myClass, Object myObj) {
		if (StringUtil.isEmpty(myObj))
			return null;
		return (T) String.valueOf(myObj);
	}
}