package com.lz.framework.tools.enums;

/**
 * 返回结果枚举
 * @author zhoufei
 */
public enum ReturnCodeEnum {

	UNVALID("01", "校验异常"),
	NOPERM("02", "没有权限"),
	EMPTY("03", "内容为空或存在为空的必输项"),
	REPEAT("04", "重复请求"),
	UNACCEPT("05", "受理失败"),
	SIGNFAIL("06", "签名失败"),
	UNSIGN("07", "验签失败"),
	UNSERIES("08", "反序列化失败"),
	BADREQ("09", "请求非法或包含非法参数"),
	UNEXIST("10", "对象不存在"),
	FININTP("20", "投保异常"),
	ERROR("99", "未知异常"),
	SUCC("00", "请求成功"),
	;
	
	private String value;
	private String name;
	
	private ReturnCodeEnum(String value, String name){
		this.value = value;
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public String getName() {
		return name;
	}
	
}
