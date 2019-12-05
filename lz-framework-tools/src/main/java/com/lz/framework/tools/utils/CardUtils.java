package com.lz.framework.tools.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 身份证信息算法类
 * 
 */
public class CardUtils {

	private static final String SEX_1 = "1";

	private static final String SEX_0 = "0";

	private static final Integer CARD_LENGTH_18 = 18;

	private static final Integer CARD_LENGTH_15 = 15;

	/**
	 * 根据身份证获取性别与年龄
	 * @param card
	 * @return
	 */
	public static Map<String, Object> getCarInfo(String card) {
		try {
			if (card.length() == CARD_LENGTH_18) {
				return getCarInfo18W(card);
			} else if (card.length() == CARD_LENGTH_15) {
				return getCarInfo15W(card);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 根据身份证的号码算出当前身份证持有者的性别和年龄 18位身份证
	 * 
	 * @return
	 * @throws Exception
	 */
	private static Map<String, Object> getCarInfo18W(String CardCode) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String year = CardCode.substring(6).substring(0, 4);// 得到年份
		String yue = CardCode.substring(10).substring(0, 2);// 得到月份
		// String day=CardCode.substring(12).substring(0,2);//得到日
		String sex;
		if (Integer.parseInt(CardCode.substring(16).substring(0, 1)) % 2 == 0) {// 判断性别
			sex = SEX_0;
		} else {
			sex = SEX_1;
		}
		Date date = new Date();// 得到当前的系统时间
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String fyear = format.format(date).substring(0, 4);// 当前年份
		String fyue = format.format(date).substring(5, 7);// 月份
		// String fday=format.format(date).substring(8,10);
		int age = 0;
		int ageMonth = 0;
		if (Integer.parseInt(yue) <= Integer.parseInt(fyue)) { // 当前月份大于用户出身的月份表示已过生
			age = Integer.parseInt(fyear) - Integer.parseInt(year) + 1;
		} else {// 当前用户还没过生
			age = Integer.parseInt(fyear) - Integer.parseInt(year);
		}
		ageMonth = (Integer.parseInt(fyear) - Integer.parseInt(year))*12 + (Integer.parseInt(fyue)-Integer.parseInt(yue));
		map.put("sex", sex);
		map.put("age", age);
		map.put("ageMonth", ageMonth);
		return map;
	}

	/**
	 * 15位身份证的验证
	 * 
	 * @param
	 * @throws Exception
	 */
	private static Map<String, Object> getCarInfo15W(String card) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String uyear = "19" + card.substring(6, 8);// 年份
		String uyue = card.substring(8, 10);// 月份
		// String uday=card.substring(10, 12);//日
		String usex = card.substring(14, 15);// 用户的性别
		String sex;
		if (Integer.parseInt(usex) % 2 == 0) {
			sex = "0";
		} else {
			sex = "1";
		}
		Date date = new Date();// 得到当前的系统时间
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String fyear = format.format(date).substring(0, 4);// 当前年份
		String fyue = format.format(date).substring(5, 7);// 月份
		// String fday=format.format(date).substring(8,10);
		int age = 0;
		int ageMonth = 0;
		if (Integer.parseInt(uyue) <= Integer.parseInt(fyue)) { // 当前月份大于用户出身的月份表示已过生
			age = Integer.parseInt(fyear) - Integer.parseInt(uyear) + 1;
		} else {// 当前用户还没过生
			age = Integer.parseInt(fyear) - Integer.parseInt(uyear);
		}
		ageMonth = (Integer.parseInt(fyear) - Integer.parseInt(uyear))*12 + (Integer.parseInt(fyue)-Integer.parseInt(uyue));
		map.put("sex", sex);
		map.put("age", age);
		map.put("ageMonth", ageMonth);
		return map;
	}
	
}
