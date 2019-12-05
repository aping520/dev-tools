package com.lz.framework.tools.utils;

import java.math.BigDecimal;

/**
 * 由于Java的简单类型不能够精确的对浮点数进行运算，这个工具类提供精 确的浮点数运算，包括加减乘除和四舍五入。
 */
public class ArithUtil {
	
	// 默认除法运算精度
	private static final int DEF_DIV_SCALE = 10;

	// 这个类不能实例化
	private ArithUtil() {

	}
	
	/**
	 * 提供精度的加减法运算
	 * @param params
	 * @return
	 */
	public static double add(Double...params) {
		BigDecimal bg1 = BigDecimal.valueOf(0.00d);
		BigDecimal bg2 = null;
		if(params != null && params.length != 0){
			for(Double d : params){
				if(d != null){
					bg2 = new BigDecimal(d);
					bg1 = bg1.add(bg2);
				}
			}
		}
		return bg1.doubleValue();
	}

	/**
	 * 提供精确的减法运算。
	 * @param v1 被减数
	 * @param v2 减数
	 * @return 两个参数的差
	 */
	public static double sub(double v1, double v2) {
		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		return b1.subtract(b2).doubleValue();
	}
	
	/**
	 * 减多个数
	 * @param v
	 * @param params
	 * @return
	 */
	public static double subMore(Double v,Double...params) {
		BigDecimal bg1 = new BigDecimal(v);
		BigDecimal bg2 = null;
		if(params != null && params.length != 0){
			for(Double d : params){
				if(d != null){
					bg2 = new BigDecimal(d);
					bg1 = bg1.subtract(bg2);
				}
			}
		}
		return bg1.doubleValue();
	} 
	
	/**
	 * 提供精确的减法运算。
	 * @param v1 被减数
	 * @param params 多个被减数
	 * @return 两个参数的差
	 */
	public static double sub(double v1, Double...params) {
		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		for(Double db : params){
			if(db != null){
				BigDecimal b2 = new BigDecimal(Double.toString(db));
				Double value = b1.subtract(b2).doubleValue();
				b1 = new BigDecimal(value);
			}
		}
		return b1.doubleValue();
	}

	/**
	 * 提供精确的乘法运算。
	 * @param v1 被乘数
	 * @param v2 乘数
	 * @return 两个参数的积
	 */
	public static double mul(Double v1, Double v2) {
		if(v1 == null) {
			v1 = 0.0d;
		}
		if(v2 == null) {
			v2 = 0.0d;
		}
		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		return b1.multiply(b2).doubleValue();
	}

	/**
	 * 提供（相对）精确的除法运算，当发生除不尽的情况时，精确到 小数点以后10位，以后的数字四舍五入。
	 * @param v1 被除数
	 * @param v2 除数
	 * @return 两个参数的商
	 */
	public static double div(double v1, double v2) {
		return div(v1, v2, DEF_DIV_SCALE);
	}

	/**
	 * 提供（相对）精确的除法运算。当发生除不尽的情况时，由scale参数指 定精度，以后的数字四舍五入。
	 * @param v1 被除数
	 * @param v2 除数
	 * @param scale 表示表示需要精确到小数点以后几位。
	 * @return 两个参数的商
	 */
	public static double div(double v1, double v2, int scale) {
		if (scale < 0) {
			throw new IllegalArgumentException("The   scale   must   be   a   positive   integer   or   zero");
		}
		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
	}

	/**
	 * 提供精确的小数位四舍五入处理。
	 * @param v 需要四舍五入的数字
	 * @param scale 小数点后保留几位
	 * @return 四舍五入后的结果
	 */
	public static double round(double v, int scale) {
		if (scale < 0) {
			throw new IllegalArgumentException("The   scale   must   be   a   positive   integer   or   zero");
		}
		BigDecimal b = new BigDecimal(Double.toString(v));
		BigDecimal one = new BigDecimal("1");
		return b.divide(one, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
	}

	/**
	 * 提供精确到十位四舍五入处理。
	 * @param v 需要四舍五入的数字
	 * @param scale 小数点后保留几位
	 * @return 四舍五入后的结果
	 */
	public static Double round_unit(double v, int scale) {
		if (scale < 0) {
			throw new IllegalArgumentException("The   scale   must   be   a   positive   integer   or   zero");
		}
		Double v_10 = div(v, 10, DEF_DIV_SCALE); // 原数据/10
		BigDecimal b = new BigDecimal(Double.toString(v_10));
		BigDecimal one = new BigDecimal("1");
		BigDecimal ten = new BigDecimal("10");
		BigDecimal _v10 = b.divide(one, scale, BigDecimal.ROUND_HALF_UP);
		return _v10.multiply(ten).doubleValue(); // 四舍五入结果*10
	}

	/**
	 * 提供精确到百位向下取整。
	 * @param v 需要取整的值
	 * @param scale 小数点后保留几位
	 * @return 取整后的结果
	 */
	public static Double round_down_hundreds(double v, int scale) {
		if (scale < 0) {
			throw new IllegalArgumentException("The   scale   must   be   a   positive   integer   or   zero");
		}
		Double v_10 = div(v, 100, DEF_DIV_SCALE); // 原数据/100
		BigDecimal b = new BigDecimal(Double.toString(v_10));
		BigDecimal one = new BigDecimal("1");
		BigDecimal hundreds = new BigDecimal("100");
		BigDecimal _v10 = b.divide(one, scale, BigDecimal.ROUND_DOWN);
		return _v10.multiply(hundreds).doubleValue(); // 四舍五入结果*100
	}

	/**
	 * 提供精确到百位向上取整。
	 * @param v 需要取整的值
	 * @param scale 小数点后保留几位
	 * @return 取整后的结果
	 */
	public static Double round_up_hundreds(double v, int scale) {
		if (scale < 0) {
			throw new IllegalArgumentException("The   scale   must   be   a   positive   integer   or   zero");
		}
		Double v_10 = div(v, 100, DEF_DIV_SCALE); // 原数据/100
		BigDecimal b = new BigDecimal(Double.toString(v_10));
		BigDecimal one = new BigDecimal("1");
		BigDecimal hundreds = new BigDecimal("100");
		BigDecimal _v10 = b.divide(one, scale, BigDecimal.ROUND_UP);
		return _v10.multiply(hundreds).doubleValue(); // 四舍五入结果*100
	}

	public static Double   f_parseFee(String c, Double p, Double fee) {
		if (c.equals("0")) { // 四舍五入
			fee = Math.round(fee / p) * p;
		} else if (c.equals("1")) { // 向上取整
			fee = Math.ceil(fee / p) * p;
		} else if (c.equals("2")) { // 向下取整
			fee = Math.floor(fee / p) * p;
		} else {// 默认向下取整,100位
			fee = Math.floor(fee / 100) * 100;
		}
		return fee;
	}
	
	public static boolean compareDouble(Double one, Double two){
		if(StringUtil.isNotEmpty(one) && StringUtil.isNotEmpty(two) && one.doubleValue() == two.doubleValue()){
			return true;
		}
		if(StringUtil.isEmpty(one) && StringUtil.isEmpty(two)){
			return true;
		}
		if(StringUtil.isEmpty(one) && StringUtil.isNotEmpty(two) && two == 0.00){
			return true;
		}
		if(StringUtil.isEmpty(two) && StringUtil.isNotEmpty(one) && one == 0.00){
			return true;
		}
		return false;
	}
	
	public static Double min(double v1, Double... params) {
		Double min = v1;
		for (Double db : params) {
			if (db != null) {
				if (db < v1) {
					min = db;
				}
			}
		}
		return min;
	}

	public static Double max(double v1, Double... params) {
		Double max = v1;
		for (Double db : params) {
			if (db != null) {
				if (db > v1) {
					max = db;
				}
			}
		}
		return max;
	}

	public static String intToZH(int i) {
		String[] zh = { "零", "一", "二", "三", "四", "五", "六", "七", "八", "九" };
		String[] unit = { "", "十", "百", "千", "万", "十", "百", "千", "亿", "十" };

		String str = "";
		StringBuilder sb = new StringBuilder(String.valueOf(i));
		sb = sb.reverse();
		int r = 0;
		int l = 0;
		for (int j = 0; j < sb.length(); j++) {
			/**
			 * 当前数字
			 */
			r = Integer.valueOf(sb.substring(j, j + 1));

			if (j != 0) {
			    /**
			     * 上一个数字
			     */
			    l = Integer.valueOf(sb.substring(j - 1, j));
			}

			if (j == 0) {
				if (r != 0 || sb.length() == 1) {
				    str = zh[r];
				}
				continue;
			}

			if (j == 1 || j == 2 || j == 3 || j == 5 || j == 6 || j == 7 || j == 9) {
				if (r != 0) {
				    str = zh[r] + unit[j] + str;
				} else if (l != 0) {
				    str = zh[r] + str;
				}
				continue;
			}

			if (j == 4 || j == 8) {
				str = unit[j] + str;
				if ((l != 0 && r == 0) || r != 0) {
				    str = zh[r] + str;
				}
				continue;
			}
		}
		return str;
	}
	
	public static void main(String[] args) {
		/*String c = "1";
		Double p = 1000.00d;
		Double fee = 1387432.32323;
		Double rate = 2.5d;
		Double serFinRebateRate = 1.1d;

		System.out.println(ArithUtil.round(ArithUtil.mul(fee, ArithUtil.div(rate, 100)), 2));
		System.out.println(f_parseFee(c, p, ArithUtil.round(ArithUtil.mul(fee, ArithUtil.div(rate, 100)), 2)));
		System.out.println((double) Math
				.round(ArithUtil.mul(f_parseFee(c, p, ArithUtil.round(ArithUtil.mul(fee, ArithUtil.div(rate, 100)), 2)),
						ArithUtil.div(serFinRebateRate, rate))));
		System.out.println(intToZH(36));*/

		System.out.println(f_parseFee("0",10d,1025.0d));//四舍五入
		System.out.println(f_parseFee("1",10d,1025.0d));//十位向上取整
		System.out.println(f_parseFee("2",10d,1025.0d));//十位向下取整
		System.out.println(f_parseFee("1",100d,1025.0d));//百位向上取整
		System.out.println(round_up_hundreds(10023.3443d,2));
	}
}