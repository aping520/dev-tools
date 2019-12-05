package com.lz.framework.tools.convert;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.apache.commons.beanutils.Converter;

import com.lz.framework.tools.utils.StringUtil;

public class BeanUtilsDateParser implements Converter {
	@SuppressWarnings("unchecked")
	public <T> T convert(Class<T> myClass, Object myObj) {
		if (StringUtil.isEmpty(myObj))
			return null;
		try {
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			return (T) df.parse(myObj.toString());
		} catch (ParseException e) {
			try {
				SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
				return (T) df.parse(myObj.toString());
			} catch (ParseException e1) {
				try {
					SimpleDateFormat df = new SimpleDateFormat("yyyy年MM月dd日");
					return (T) df.parse(myObj.toString());
				} catch (ParseException e2) {
					try {
						SimpleDateFormat dfParse = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", Locale.ENGLISH);
						return (T) dfParse.parse(myObj.toString());
					} catch (ParseException e3) {
						try {
							return (T) new Date(Long.parseLong(String.valueOf(myObj)));
						} catch (Exception e4) {
							e4.printStackTrace();
						}
					}
				}
			}
		}
		return null;
	}
}