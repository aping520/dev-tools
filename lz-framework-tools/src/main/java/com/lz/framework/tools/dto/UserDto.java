package com.lz.framework.tools.dto;

import java.io.Serializable;

public class UserDto implements Serializable{

	/**
	 * 传递用户对象
	 */
	private static final long serialVersionUID = 7937470720257852491L;

	/**
	 * 登录用户ID
	 */
	private Integer userId;
	/**
	 * 用户代码
	 */
	private String userCode;
	/**
	 * 随机用户标识
	 */
	private String token;
	/**
	 * 登录名
	 */
	private String userName;
	/**
	 * 真实姓名
	 */
	private String trueName;
	/**
	 * 注册手机号
	 */
	private String mobile;
	/**
	 * 性别
	 */
	private String sex;
	/**
	 * 身份证号
	 */
	private String idno;
	/**
	 * 客户端IP
	 */
	private String clientIp;
	/**
	 * 用户职位
	 */
	private String position;
	
	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserCode() {
		return userCode;
	}

	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}

	public String getTrueName() {
		return trueName;
	}

	public void setTrueName(String trueName) {
		this.trueName = trueName;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getIdno() {
		return idno;
	}

	public void setIdno(String idno) {
		this.idno = idno;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getClientIp() {
		return clientIp;
	}

	public void setClientIp(String clientIp) {
		this.clientIp = clientIp;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	@Override
	public String toString() {
		return "UserDto [userId=" + userId + ", token=" + token + ", userName=" + userName + ", trueName=" + trueName
				+ ", mobile=" + mobile + ", sex=" + sex + ", clientIp=" + clientIp + "]";
	}

}
