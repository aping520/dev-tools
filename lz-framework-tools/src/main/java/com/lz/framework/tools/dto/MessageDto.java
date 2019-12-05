package com.lz.framework.tools.dto;

import java.io.Serializable;

/**
 * 返回对象
 *
 */
public class MessageDto implements Serializable{

	/**
	 * 接口返回内容
	 */
	private static final long serialVersionUID = 8475031290992534279L;
	/**
	 * 返回码
	 */
	private String code;
	/**
	 * 返回内容
	 */
	private String message;
	/**
	 * 返回标识
	 */
	private String responseId;
	/**
	 * 报文体
	 */
	private Object data;
	
	public MessageDto(String code, String message) {
		super();
		this.code = code;
		this.message = message;
	}
	
	public MessageDto() {
		super();
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getResponseId() {
		return responseId;
	}
	public void setResponseId(String responseId) {
		this.responseId = responseId;
	}
	public Object getData() {
		return data;
	}
	public void setData(Object data) {
		this.data = data;
	}
	
	@Override
	public String toString() {
		return "MessageDto [code=" + code + ", message=" + message + ", data=" + data + "]";
	}
	
}
